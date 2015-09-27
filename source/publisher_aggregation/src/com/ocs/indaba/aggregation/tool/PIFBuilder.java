/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.tool;

import com.ocs.indaba.aggregation.cache.BasePersistence;
import com.ocs.indaba.aggregation.cache.PIFPersistence;
import com.ocs.indaba.aggregation.cache.SRFPersistence;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.service.ScorecardService;
import com.ocs.indaba.aggregation.vo.*;
import com.ocs.indaba.po.SurveyCategory;
import com.ocs.indaba.util.Tree;
import java.io.File;
import java.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class PIFBuilder extends DataBuilder {
    private ScorecardService scorecardSrvc = null;

    private Map<Integer, Double> meanMap = null;
    private Map<Integer, List<Double>> medianMap = null;

    public PIFBuilder() {
        log = Logger.getLogger(PIFBuilder.class);
        meanMap = new HashMap<Integer, Double>();
        medianMap = new HashMap<Integer, List<Double>>();
    }

    @Autowired
    public void setScorecardSrvc(ScorecardService scorecardSrvc) {
        this.scorecardSrvc = scorecardSrvc;
    }
    
    @Override
    public void clean() {
        meanMap.clear();
        medianMap.clear();
    }

    @Override
    public void build(String baseDir, String cacheDir) {
        File[] prodDirs = BasePersistence.getAllProductDirs(baseDir);
        if (prodDirs == null) {
            return;
        }
        for (File dir : prodDirs) {
            int prodId = getProductId(dir.getName());
            /*
             * if(prodId != 1) { continue; }
             */
            writeLogLn("Generating PIF for product [" + prodId + "] ...");
            ProductInfo prodInfo = scorecardSrvc.getProductInfo(prodId);
            prodInfo.setHorses(scorecardSrvc.getHorsesByProductId(prodId));

            File[] horseFiles = getAllHorseFiles(dir);
            if (horseFiles == null) {
                continue;
            }
            int count = 0;
            for (File file : horseFiles) {
                SRFPersistence scorcardPersistence = new SRFPersistence(baseDir);
                ScorecardInfo scorecard = scorcardPersistence.deserializeSRF(file);
                if (scorecard == null) {
                    return;
                }
                ++count;
                computeCategories(scorecard.getRootCategories());
            }
            Set<Integer> keys = meanMap.keySet();
            for (Integer key : keys) {
                meanMap.put(key, (meanMap.get(key)) / count);
            }

            prodInfo.setRootCategories(buildCategories(prodId));

            PIFPersistence pifPersistence = new PIFPersistence(baseDir);
            pifPersistence.serializePIF(prodInfo);

            writeLogLn("PIF for product [" + prodId + "] is generated: " + BasePersistence.getPIFFilepath(prodId));
        }
    }

    private List<Tree<ScorecardBaseNode>> buildCategories(int prodId) {
        List<Tree<ScorecardBaseNode>> rootCategories = null;
        List<SurveyCategory> categories = scorecardSrvc.getRootSurveyCategories(prodId);
        if (categories == null || categories.isEmpty()) {
            return null;
        }
        rootCategories = new ArrayList<Tree<ScorecardBaseNode>>(categories.size());
        for (SurveyCategory sc : categories) {
            CategoryNode catNode = new CategoryNode(sc.getId(), sc.getName(), sc.getTitle(), sc.getLabel(), sc.getWeight());
            Tree<ScorecardBaseNode> tree = new Tree<ScorecardBaseNode>(catNode);
            buildCategoriesByParentId(tree, sc.getId());
            rootCategories.add(tree);
            Double value = meanMap.get(sc.getId());
            //catNode.setMean((value == null) ? 0.0d : value);
            if (value != null) {
                catNode.setUseScore(true);
                catNode.setMean(meanMap.get(sc.getId()));
                catNode.setMedian(computeMedian(medianMap.get(catNode.getId())));
            } else {
                catNode.setUseScore(false);
            }
        }
        return rootCategories;
    }

    private void buildCategoriesByParentId(Tree<ScorecardBaseNode> parent, int parentId) {
        List<SurveyCategory> surveyCategories = scorecardSrvc.getSubSuveryCategories(parentId);
        if (surveyCategories == null || surveyCategories.isEmpty()) {
            List<QuestionNode> indicators = scorecardSrvc.getIndicatorsByCategoryId(parentId);
            if (indicators != null && !indicators.isEmpty()) {
                for (QuestionNode indicator : indicators) {
                    switch (indicator.getQuestionType()) {
                        case Constants.ANSWER_TYPE_SINGLE:
                        case Constants.ANSWER_TYPE_MULTI:
                            indicator.setOptions(scorecardSrvc.getQuestionOptionsByIndicatorId(indicator.getId()));
                            break;
                        default:
                            indicator.setCriteria(scorecardSrvc.getNonChoiceQuestionCriteraByIndicatorId(indicator.getId(), indicator.getQuestionType()));
                            break;
                    }
                    parent.addChild(new Tree<ScorecardBaseNode>(indicator, parent));
                    //parent.getNode().setUseScore(true);
                    //log.debug("###### indicator: " + indicator.getId() + ", value: " + indicator.getScore());
                }
            }
        } else {
            for (SurveyCategory cat : surveyCategories) {
                Tree<ScorecardBaseNode> child = new Tree<ScorecardBaseNode>();
                child.setParent(parent);
                CategoryNode catNode = new CategoryNode(cat.getId(), cat.getName(), cat.getTitle(), cat.getLabel(), cat.getWeight());
                Double val = meanMap.get(cat.getId());
                //catNodye.setMean((val == null) ? 0.0d : val);
                if (val != null) {
                    catNode.setUseScore(true);
                    catNode.setMean(meanMap.get(cat.getId()));
                    catNode.setMedian(computeMedian(medianMap.get(cat.getId())));
                }
                child.setNode(catNode);
                parent.addChild(child);
                buildCategoriesByParentId(child, cat.getId());
            }
        }
    }

    private void computeCategories(List<Tree<ScorecardBaseNode>> categories) {
        for (Tree<ScorecardBaseNode> subTree : categories) {
            ScorecardBaseNode node = subTree.getNode();
            if (node.getNodeType() == Constants.NODE_TYPE_QUESTION) {
                return;
            }
            int catId = ((CategoryNode) node).getId();
            if (node.getUsedScoreCount() > 0) {
                Double mean = meanMap.get(catId);
                if (mean == null) {
                    meanMap.put(catId, ((CategoryNode) node).getMean());
                } else {
                    meanMap.put(catId, mean + ((CategoryNode) node).getMean());
                }
                List<Double> medianScores = medianMap.get(catId);
                if (medianScores == null) {
                    medianScores = new ArrayList<Double>();
                    medianMap.put(catId, medianScores);
                }
                // product's median is the median of all target's mean.
                medianScores.add(((CategoryNode) node).getMean());
                computeCategories(subTree.getChildren());
            } else {
                //log.debug(">>>> Ignore category: " + node.getId() + ", name: " + node.getName());
            }
        }
    }

    private double computeMedian(List<Double> medianScores) {
        if (medianScores == null || medianScores.isEmpty()) {
            return 0;
        } else {
            Collections.sort(medianScores);
            int n = medianScores.size();
            int index = (n % 2 == 0) ? n / 2 : ((n + 1) / 2 - 1);
            return medianScores.get(index);
        }
    }

    public static void main(String args[]) {
        PIFBuilder builder = new PIFBuilder();
        builder.build(null, null);
    }
}
