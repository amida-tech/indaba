/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.util;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.vo.CategoryNode;
import com.ocs.indaba.aggregation.vo.QuestionNode;
import com.ocs.indaba.aggregation.vo.ScorecardBaseNode;
import com.ocs.indaba.aggregation.vo.ScorecardInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author jiangjeff
 */
public class StandardAggregation {

    private static final Logger log = Logger.getLogger(StandardAggregation.class);
    private static Map<Integer, StandardAggregationData> meanMap = new HashMap<Integer, StandardAggregationData>();

    public StandardAggregation() {
    }


    private static void loadScores(Tree<ScorecardBaseNode> node, List<Double> lfScores, List<Double>aiScores) {
        if (node.isLeaf()) {
            QuestionNode qst = (QuestionNode)node.getNode();
            Double score = qst.getScore();
            int refClass = qst.getRefClassification();
            int indicatorId = qst.getId();

            if (refClass == Constants.REF_CLASSIFICATION_LEGAL) {
                // In Law - LF
                // Further check indicator ID
                if (indicatorId == 464 || indicatorId == 488) {
                    // these two must be treated as AI
                    aiScores.add(score);
                } else {
                    lfScores.add(score);
                }
            } else {
                // AI - further check indicator ID
                if (indicatorId == 450) {
                    // must be treated as LF
                    lfScores.add(score);
                } else {
                    aiScores.add(score);
                }
            }
            return;
        }

        for (Tree<ScorecardBaseNode> n : node.getChildren()) {
            loadScores(n, lfScores, aiScores);
        }
    }


    private static double simpleAvg(List<Double> scores) {
        double sum = 0.0d;
        for (Double score : scores) sum += score;
        return sum / scores.size();
    }


    public static void compute(ScorecardInfo scorecard) {
        meanMap.clear();
        if (scorecard == null) {
            return;
        }
        List<Tree<ScorecardBaseNode>> rootCategories = scorecard.getRootCategories();
        if (rootCategories != null && !rootCategories.isEmpty()) {
            double scores = 0.0d;
            for (Tree<ScorecardBaseNode> catNode : rootCategories) {
                preProcessCategory(catNode);
                CategoryNode cat = (CategoryNode) catNode.getNode();
                /*
                scorecard.setOverall(computeMeanScore(cat.getMean(), (cat.getImplementationCount() + cat.getLegalFrameworkCount()), scorecard.getOverall(),
                scorecard.getImplementationCount() + scorecard.getLegalFrameworkCount()));
                 */
                if (cat.getUsedScoreCount() > 0) {
                    if (cat.getLegalFrameworkCount() > 0) {
                        scorecard.setLegalFramework(computeMeanScore(cat.getLegalFramework(), scorecard.getLegalFramework(),
                                scorecard.getLegalFrameworkCount()));
                        scorecard.incrementLegalFrameworkCount();
                    }

                    if (cat.getImplementationCount() > 0) {
                        scorecard.setImplementation(computeMeanScore(cat.getImplementation(), scorecard.getImplementation(),
                                scorecard.getImplementationCount()));
                        scorecard.incrementImplementationCount();
                    }
                    // Commented by Jeff 2011-07-27: duplicated to generate mean value
                    //genCategoryStandardAggregationData(catNode);
                    computeStandardAggregation(catNode);
                    scores += cat.getMean();
                    scorecard.incrementUsedScoreCount();
                    cat.setUseScore(true);
                }
                meanMap.clear();
            }
            //scorecard.setOverall(scores / categories.size());
            scorecard.setOverall(scores / scorecard.getUsedScoreCount());

            // Now compute the LF and AI scores for Mexico ATI product: product 11, survey config 4
            if (scorecard.getProductId() == 11 && scorecard.getSurveyConfigId() == 4) {
                log.debug("Processing Mexico ATI logic for scorecard " + scorecard.getTargetName());

                // Load LF nodes
                ArrayList<Double> lfScores = new ArrayList<Double>();
                ArrayList<Double> aiScores = new ArrayList<Double>();

                for (Tree<ScorecardBaseNode> node : rootCategories) {
                    loadScores(node, lfScores, aiScores);
                }

                double aiScore = simpleAvg(aiScores);
                double lfScore = simpleAvg(lfScores);
                double gap = lfScore - aiScore;

                log.debug("Old LF Score: " + scorecard.getLegalFramework());
                log.debug("New LF Score: " + lfScore);

                log.debug("Old AI Score: " + scorecard.getImplementation());
                log.debug("New AI Score: " + aiScore);

                log.debug("Old Gap Score: " + scorecard.getImplementationGap());
                log.debug("New Gap Score: " + gap);

                scorecard.setImplementation(aiScore);
                scorecard.setLegalFramework(lfScore);
                scorecard.setImplementationGap(gap);                
            }

        }
        meanMap.clear();
    }

    /**
     * Pre-process the category structure and get all the required data of 
     * computing standard aggregation, which will be stored into a map.
     * 
     * @param node
     * @param data
     */
    private static StandardAggregationData genCategoryStandardAggregationData(Tree<ScorecardBaseNode> tree) {
        StandardAggregationData data = new StandardAggregationData();

        if (tree.getNode().getUsedScoreCount() <= 0) {
            return data;
        }

        meanMap.put(tree.getNode().getId(), data);
        double score = 0;
        for (Tree<ScorecardBaseNode> child : tree.getChildren()) {
            if (child.isLeaf()) {
                QuestionNode question = (QuestionNode) child.getNode();
                if (question.hasUseScore()) {
                    if (question.isCompleted()) {
                        data.addScore(question.getScore());
                        score += question.getScore();
                    }
                }
            } else {
                if (child.getNode().getUsedScoreCount() > 0) {
                    StandardAggregationData childData = genCategoryStandardAggregationData(child);
                    data.addScores(childData.getScores());
                    score += ((CategoryNode) child.getNode()).getMean();
                }
            }
        }
        if (tree.getChildren() != null && !tree.getChildren().isEmpty()) {
            //((CategoryNode) treeNode.getNode()).setMean(score / treeNode.getChildren().size());
            ((CategoryNode) tree.getNode()).setMean(score / tree.getNode().getUsedScoreCount());
        }
        return data;
    }

    private static void preProcessCategory(Tree<ScorecardBaseNode> tree) {
        double score = 0;
        CategoryNode parent = (CategoryNode) tree.getNode();
        List<Tree<ScorecardBaseNode>> children = tree.getChildren();
        for (Tree<ScorecardBaseNode> child : children) {
            if (child.isLeaf()) {
                if (child.getNode().hasUseScore()) {
                    parent.setUseScore(true);
                    preProcessIndicator(tree, child);
                    score += ((QuestionNode) child.getNode()).getScore();
                    parent.incrementUsedScoreCount();
                } else {
                    //log.debug("Ignore indicator node: " + child.getNode().getId() + ", title: " + child.getNode().getName());
                }
            } else {
                preProcessCategory(child);
                CategoryNode cat = (CategoryNode) child.getNode();
                // Mean score
                if (cat.getUsedScoreCount() > 0) {
                    parent.incrementUsedScoreCount();
                    score += cat.getMean();
                    /*
                    parent.setMean(computeMeanScore(cat.getMean(), (cat.getImplementationCount() + cat.getLegalFrameworkCount()), parent.getMean(),
                    parent.getLegalFrameworkCount() + parent.getImplementationCount()));
                     */
                    /*
                    // Legal Framework score
                    if (cat.getLegalFrameworkCount() > 0) {
                    parent.setLegalFramework(computeMeanScore(cat.getLegalFramework(), parent.getLegalFramework(),
                    parent.getLegalFrameworkCount()));
                    parent.incrementLegalFrameworkCount();
                    }
                    
                    // Implementation score
                    if (cat.getImplementationCount() > 0) {
                    parent.setImplementation(computeMeanScore(cat.getImplementation(), parent.getImplementation(),
                    parent.getImplementationCount()));
                    parent.incrementImplementationCount();
                    }
                     */
                    computeScores(parent, cat);
                    cat.setUseScore(true);
                } else {
                   // log.debug("Ignore category node: " + cat.getId());
                }
            }
        }
        if (parent.getUsedScoreCount() <= 0) {
            parent.setUseScore(false);
        } else {
            parent.setUseScore(true);
            if (tree.getChildren() != null && !tree.getChildren().isEmpty()) {
                //((CategoryNode) treeNode.getNode()).setMean(score / treeNode.getChildren().size());
                parent.setMean(score / parent.getUsedScoreCount());
            }
        }
    }

    private static void preProcessIndicator(Tree<ScorecardBaseNode> parentNode, Tree<ScorecardBaseNode> treeNode) {
        QuestionNode question = (QuestionNode) treeNode.getNode();
        if (question.isCompleted()) {
            CategoryNode parent = (CategoryNode) parentNode.getNode();
            //parent.setMean(computeMeanScore(question.getScore(), 1, parent.getMean(), (parent.getImplementationCount() + parent.getLegalFrameworkCount())));
            if (Constants.REF_CLASSIFICATION_PRACTICE == question.getRefClassification()) { // Implementation
                parent.setImplementation(computeMeanScore(question.getScore(), parent.getImplementation(), parent.getImplementationCount()));
                parent.incrementImplementationCount();
            } else { // Legal framework
                parent.setLegalFramework(computeMeanScore(question.getScore(), parent.getLegalFramework(), parent.getLegalFrameworkCount()));
                parent.incrementLegalFrameworkCount();
            }
        }
    }

    /*
    private void preProcessIndicator(Tree<ScorecardBaseNode> parentNode, Tree<ScorecardBaseNode> treeNode, StandardAggregationData parentData) {
    QuestionNode question = (QuestionNode) treeNode.getNode();
    if (question.isCompleted()) {
    CategoryNode parent = (CategoryNode) parentNode.getNode();
    if (Constants.REFERENCE_IMPLEMENTATION_ID == question.getReferenceId()) {// Implementation
    parent.setImplementation(computeMeanScore(question.getScore(), parent.getImplementation(), parent.getImplementationCount()));
    parent.incrementImplementationCount();
    } else { // Legal framework
    parent.setLegalFramework(computeMeanScore(question.getScore(), parent.getLegalFramework(), parent.getLegalFrameworkCount()));
    parent.incrementLegalFrameworkCount();
    }
    
    parent.setMean(computeMeanScore(question.getScore(), parent.getMean(), (parent.getImplementationCount() + parent.getLegalFrameworkCount())));
    parentData.addScore(question.getScore());
    }
    parentData.increment();
    }
     */
    private static void computeStandardAggregation(Tree<ScorecardBaseNode> treeNode) {
        if (treeNode.isLeaf()) { // ignore indicator node
            return;
        }
        CategoryNode node = (CategoryNode) treeNode.getNode();
        StandardAggregationData data = meanMap.get(node.getId());
        if (data == null) {
            //log.error("====> Ignore node: " + node.getId() + ", label: " + node.getLabel() + ", title: " + node.getTitle());
            return;
        } else {
        }
        List<Double> scores = data.getScores();
        if (scores != null && !scores.isEmpty()) {
            Collections.sort(scores);
            int n = 0;
            //double sum = 0.0d;
            double min = 0;
            double max = 0;
            for (double score : scores) {
                ++n;
                //sum += score;
                if (min > score) {
                    min = score;
                }
                if (max < score) {
                    max = score;
                }
            }
            node.setMin(min);
            node.setMax(max);
            //node.setMean(sum / n);
            int index = (n % 2 == 0) ? n / 2 : ((n + 1) / 2 - 1);
            node.setMedian(scores.get(index));
        }

        for (Tree<ScorecardBaseNode> child : treeNode.getChildren()) {
            computeStandardAggregation(child);
        }
    }

    private static void computeScores(CategoryNode parent, CategoryNode child) {
        // Legal Framework score
        if (child.getLegalFrameworkCount() > 0) {
            parent.setLegalFramework(computeMeanScore(child.getLegalFramework(), parent.getLegalFramework(),
                    parent.getLegalFrameworkCount()));
            parent.incrementLegalFrameworkCount();
        }

        // Implementation score
        if (child.getImplementationCount() > 0) {
            parent.setImplementation(computeMeanScore(child.getImplementation(), parent.getImplementation(),
                    parent.getImplementationCount()));
            parent.incrementImplementationCount();
        }
    }

    private static double computeMeanScore(double val, double existedVal, int existedCount) {
        return (val + existedVal * existedCount) / (1 + existedCount);
    }
}

class StandardAggregationData {

    private List<Double> scores = null;

    public void addScore(double score) {
        if (scores == null) {
            scores = new ArrayList<Double>();
        }
        scores.add(score);
    }

    public void addScores(List<Double> values) {
        if (values == null) {
            return;
        }
        if (scores == null) {
            scores = new ArrayList<Double>();
        }
        scores.addAll(values);
    }

    public List<Double> getScores() {
        return scores;
    }

    public void setScores(List<Double> scores) {
        this.scores = scores;
    }

    @Override
    public String toString() {
        return "StandardAggregationData{" + "scores=" + scores + '}';
    }
}
