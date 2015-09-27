/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.rwi.json;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.json.JsonUtils;
import com.ocs.indaba.aggregation.vo.CategoryNode;
import com.ocs.indaba.aggregation.vo.ScoreVO;
import com.ocs.indaba.aggregation.vo.ScorecardBaseNode;
import com.ocs.indaba.aggregation.vo.ScorecardInfo;
import com.ocs.indaba.util.LabelUtils;
import com.ocs.indaba.util.Tree;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author jiangjeff
 */
public class VerticalScorecardSummary4RWIJsonUtils extends JsonUtils {

    private static final Logger log = Logger.getLogger(VerticalScorecardSummary4RWIJsonUtils.class);
    //private final static String KEY_AGGR_NAME_OVERALL = "Overall Score";
    //private final static String KEY_AGGR_NAME_LEGAL_FRAMEWORK = "Legal Framework Score";
    //private final static String KEY_AGGR_NAME_ACTUAL_IMPLEMENTATION = "Actual Implementation Score";
    //private final static String CAT_NAME_OF_3RD_NODE = "State Owned Companies";
    //private final static String CAT_NAME_OF_4TH_NODE = "Natural Resource Funds";
    //private final static String CAT_NAME_OF_5TH_NODE = "Subnational Transfers";
    private final static String SUBCAT_NAME_OF_1ST_NODE = "Context";
    private final static String SUBCAT_NAME_OF_2ND_NODE = "Disclosure";
    private final static String SUBCAT_NAME_OF_3RD_NODE = "Legal Framework and Practice";

    public VerticalScorecardSummary4RWIJsonUtils() {
    }

    public static JSONObject toJson(ScorecardInfo scorecard) {
        JSONObject dataJsonObj = new JSONObject();
        dataJsonObj.put(KEY_SCORE_RANGE_DEFINITION, RWIReportUtils.generateScoreDisplayJsonObj());
        // base info
        dataJsonObj.put(KEY_TITLE, scorecard.getTitle());
        dataJsonObj.put(KEY_TARGET, scorecard.getTargetName());
        dataJsonObj.put(KEY_SURVEY_NAME, scorecard.getSurveyName());
        dataJsonObj.put(KEY_PRODUCT_NAME, scorecard.getProdouctName());
        dataJsonObj.put(KEY_STUDY_PERIOD, scorecard.getStudyPeriod());
        // additional aggregation
        JSONArray aggrArr = new JSONArray();
        //Ignore overall summary scores
        //aggrArr.add(aggr2Json(KEY_AGGR_NAME_OVERALL, scorecard.getOverall(), LabelUtils.getScoreLabel(scorecard.getOverall())));
        //aggrArr.add(aggr2Json(KEY_AGGR_NAME_LEGAL_FRAMEWORK, scorecard.getLegalFramework(), LabelUtils.getScoreLabel(scorecard.getLegalFramework())));
        //aggrArr.add(aggr2Json(KEY_AGGR_NAME_ACTUAL_IMPLEMENTATION, scorecard.getImplementation(), LabelUtils.getScoreLabel(scorecard.getImplementation())));
        dataJsonObj.put(KEY_AGGRS, aggrArr);
        int catCount = 0;
        // structured categories/indicators
        List<Tree<ScorecardBaseNode>> rootCategories = scorecard.getRootCategories();
        if (rootCategories != null && !rootCategories.isEmpty()) {
            JSONArray catArr = new JSONArray();
            Tree<ScorecardBaseNode> the3rdCatTree = null;
            for (Tree<ScorecardBaseNode> catTree : rootCategories) {
                ScorecardBaseNode node = catTree.getNode();
                if (!catTree.isLeaf() && node != null && node.getNodeType() == Constants.NODE_TYPE_CATEGORY) {
                    ++catCount;
                    if (catCount > RWIReportUtils.ORIG_CAT_COUNT) {
                        the3rdCatTree = catTree;
                        break;
                    }
                    String catName = ((CategoryNode) node).getTitle();
                    JSONObject catJsonObj = cat2Json((CategoryNode) node, false, catName);
                    List<Tree<ScorecardBaseNode>> subcats = catTree.getChildren();
                    if (subcats != null && !subcats.isEmpty()) {
                        JSONArray subcatArr = new JSONArray();
                        int subcatCount = 0;
                        for (Tree<ScorecardBaseNode> child : subcats) {
                            ScorecardBaseNode subcatNode = child.getNode();
                            if (!catTree.isLeaf() && subcatNode.getNodeType() == Constants.NODE_TYPE_CATEGORY) {
                                ++subcatCount;
                                String subcatName = ((CategoryNode) subcatNode).getTitle();
                                JSONObject subcatJsonObj = cat2Json((CategoryNode) subcatNode, (subcatCount > 1), subcatName);
                                subcatArr.add(subcatJsonObj);
                            }
                        }
                        catJsonObj.put(KEY_SUBCATS, subcatArr);
                    }
                    catArr.add(catJsonObj);
                }
            }
            // Split the 3rd category
            JSONArray vCatArr = splitCategory(the3rdCatTree);
            if (vCatArr != null) {
                catArr.addAll(vCatArr);
            }
            dataJsonObj.put(KEY_CATEGORIES, catArr);
        }
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_VERSION, Constants.JSON_VERSION);
        jsonObj.put(KEY_DATA, dataJsonObj);
        return dataJsonObj;
    }

    private static JSONArray splitCategory(Tree<ScorecardBaseNode> catTree) {
        if (catTree == null) {
            return null;
        }
        List<Tree<ScorecardBaseNode>> subcats = catTree.getChildren();
        if (subcats == null || subcats.isEmpty()) {
            return null;
        }
        int catId = 3;
        JSONArray catArr = new JSONArray();
        for (Tree<ScorecardBaseNode> subcatTree : subcats) {
            int usedScoreCount = 0;
            double catScore = 0.0d;
            CategoryNode subcatNode = (CategoryNode) subcatTree.getNode();
            List<Tree<ScorecardBaseNode>> questionSet = subcatTree.getChildren();
            CategoryNode catNode = new CategoryNode(); // Generate the virtual category node
            catNode.setId(subcatNode.getId());
            catNode.setName(subcatNode.getName());
            catNode.setTitle(subcatNode.getTitle());
            catNode.setLabel(String.valueOf(catId));
            JSONArray subcatArr = new JSONArray();
            for (int subcatId = 1; subcatId <= 3; ++subcatId) {
                CategoryNode theSubcatNode = new CategoryNode();
                String name = null;
                ScoreVO scoreVo = (catId == 4 && subcatId == 2)
                        ? RWIReportUtils.computeAverageScore4IV2(questionSet)
                        : RWIReportUtils.computeAverageScore(questionSet, subcatId);
                double score = scoreVo.getScore();
                if (score == -1) {
                    theSubcatNode.setUseScore(false);
                } else {
                    theSubcatNode.setUseScore(true);
                    theSubcatNode.setMean(score);
                    catScore += score;
                    ++usedScoreCount;
                    //usedScoreCount += scoreVo.getUsedCount();
                }
                switch (subcatId) {
                    case 1:
                        name = SUBCAT_NAME_OF_1ST_NODE;
                        break;
                    case 2:
                        name = SUBCAT_NAME_OF_2ND_NODE;
                        break;
                    case 3:
                        name = SUBCAT_NAME_OF_3RD_NODE;
                        break;
                }

                theSubcatNode.setId(subcatNode.getId());
                theSubcatNode.setWeight(((CategoryNode) catTree.getNode()).getWeight() + subcatId);
                JSONObject subcatJsonObj = cat2Json(theSubcatNode, theSubcatNode.hasUseScore(), name, (catId + "." + subcatId));
                subcatJsonObj.put(KEY_ID, RWIReportUtils.generateVirtualSubcatNodeId(subcatNode.getId(), subcatId));
                subcatArr.add(subcatJsonObj);
            }
            if(usedScoreCount > 0) {
                catNode.setUseScore(true);
                catNode.setMean(catScore/usedScoreCount);
                catNode.setUsedScoreCount(usedScoreCount);
            } else {
                catNode.setUseScore(false);
                catNode.setMean(-1);
                catNode.setUsedScoreCount(0);
            }
            JSONObject catJsonObj = cat2Json(catNode, false);
            catJsonObj.put(KEY_SUBCATS, subcatArr);
            catArr.add(catJsonObj);
            ++catId;
        }
        return catArr;
    }
    /*
     * private static JSONObject aggr2Json(String name, double value, String
     * label) { JSONObject jsonObj = new JSONObject(); jsonObj.put(KEY_NAME,
     * name); jsonObj.put(KEY_VALUE, value); jsonObj.put(KEY_LABEL, label);
     * return jsonObj; }
     */

    private static JSONObject cat2Json(CategoryNode category, boolean hasScore) {
        return cat2Json(category, hasScore, null);
    }

    private static JSONObject cat2Json(CategoryNode category, boolean hasScore, String catName) {
        return cat2Json(category, hasScore, catName, null);
    }

    private static JSONObject cat2Json(CategoryNode category, boolean hasScore, String catName, String label) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_ID, category.getId());
        if (StringUtils.isEmpty(label)) {
            jsonObj.put(KEY_LABEL, formatStrVal(category.getLabel()));
        } else {
            jsonObj.put(KEY_LABEL, label);
        }
        if (StringUtils.isEmpty(catName)) {
            jsonObj.put(KEY_TITLE, formatStrVal(category.getTitle()));
            jsonObj.put(KEY_NAME, formatStrVal(category.getTitle()));
        } else {
            jsonObj.put(KEY_TITLE, catName);
            jsonObj.put(KEY_NAME, catName);
        }

        jsonObj.put(KEY_USE_SCORE, hasScore);
        if (hasScore) {
            jsonObj.put(KEY_SCORE_VALUE, category.getMean());
            jsonObj.put(KEY_SCORE_LABEL, LabelUtils.getScoreLabel(category.getMean()));
        }
        return jsonObj;
    }
}
