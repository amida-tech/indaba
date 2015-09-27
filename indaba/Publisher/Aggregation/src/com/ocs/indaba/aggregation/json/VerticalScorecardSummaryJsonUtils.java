/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.json;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.vo.CategoryNode;
import com.ocs.indaba.aggregation.vo.ScorecardBaseNode;
import com.ocs.indaba.aggregation.vo.ScorecardInfo;
import com.ocs.indaba.util.LabelUtils;
import com.ocs.indaba.util.Tree;
import java.util.List;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author jiangjeff
 */
public class VerticalScorecardSummaryJsonUtils extends JsonUtils {

    private static final Logger log = Logger.getLogger(VerticalScorecardSummaryJsonUtils.class);
    private final static String KEY_AGGR_NAME_OVERALL = "Overall Score";
    private final static String KEY_AGGR_NAME_LEGAL_FRAMEWORK = "Legal Framework Score";
    private final static String KEY_AGGR_NAME_ACTUAL_IMPLEMENTATION = "Actual Implementation Score";

    public static JSONObject toJson(ScorecardInfo scorecard) {
        JSONObject dataJsonObj = new JSONObject();
        // base info
        dataJsonObj.put(KEY_TITLE, scorecard.getTitle());
        dataJsonObj.put(KEY_TARGET, scorecard.getTargetName());
        dataJsonObj.put(KEY_SURVEY_NAME, scorecard.getSurveyName());
        dataJsonObj.put(KEY_PRODUCT_NAME, scorecard.getProdouctName());
        dataJsonObj.put(KEY_STUDY_PERIOD, scorecard.getStudyPeriod());
        // additional aggregation
        JSONArray aggrArr = new JSONArray();
        aggrArr.add(aggr2Json(KEY_AGGR_NAME_OVERALL, scorecard.getOverall(), LabelUtils.getScoreLabel(scorecard.getOverall())));
        aggrArr.add(aggr2Json(KEY_AGGR_NAME_LEGAL_FRAMEWORK, scorecard.getLegalFramework(), LabelUtils.getScoreLabel(scorecard.getLegalFramework())));
        aggrArr.add(aggr2Json(KEY_AGGR_NAME_ACTUAL_IMPLEMENTATION, scorecard.getImplementation(), LabelUtils.getScoreLabel(scorecard.getImplementation())));
        dataJsonObj.put(KEY_AGGRS, aggrArr);

        // structured categories/indicators
        List<Tree<ScorecardBaseNode>> rootCategories = scorecard.getRootCategories();
        if (rootCategories != null && !rootCategories.isEmpty()) {
            JSONArray catArr = new JSONArray();
            for (Tree<ScorecardBaseNode> categoryNode : rootCategories) {
                ScorecardBaseNode node = categoryNode.getNode();
                if (!categoryNode.isLeaf() && node != null && node.getNodeType() == Constants.NODE_TYPE_CATEGORY) {
                    JSONObject catJsonObj = cat2Json((CategoryNode) node);
                    List<Tree<ScorecardBaseNode>> subcats = categoryNode.getChildren();
                    if (subcats != null && !subcats.isEmpty()) {
                        JSONArray subcatArr = new JSONArray();
                        for (Tree<ScorecardBaseNode> child : subcats) {
                            ScorecardBaseNode subcatNode = child.getNode();
                            if (!categoryNode.isLeaf() && subcatNode.getNodeType() == Constants.NODE_TYPE_CATEGORY) {
                                JSONObject subcatJsonObj = cat2Json((CategoryNode) subcatNode);
                                subcatArr.add(subcatJsonObj);
                            }
                        }
                        catJsonObj.put(KEY_SUBCATS, subcatArr);
                    }
                    catArr.add(catJsonObj);
                }
            }
            dataJsonObj.put(KEY_CATEGORIES, catArr);
        }
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_VERSION, Constants.JSON_VERSION);
        jsonObj.put(KEY_DATA, dataJsonObj);
        return dataJsonObj;
    }

    private static JSONObject aggr2Json(String name, double value, String label) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_NAME, name);
        jsonObj.put(KEY_VALUE, value);
        jsonObj.put(KEY_LABEL, label);
        return jsonObj;
    }

    private static JSONObject cat2Json(CategoryNode category) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_ID, category.getId());
        jsonObj.put(KEY_LABEL, formatStrVal(category.getLabel()));
        jsonObj.put(KEY_TITLE, formatStrVal(category.getTitle()));
        jsonObj.put(KEY_NAME, formatStrVal(category.getTitle()));
        jsonObj.put(KEY_SCORE_VALUE, category.getMean());
        jsonObj.put(KEY_SCORE_LABEL, LabelUtils.getScoreLabel(category.getMean()));
        jsonObj.put(KEY_USE_SCORE, category.hasUseScore());
        return jsonObj;
    }
}
