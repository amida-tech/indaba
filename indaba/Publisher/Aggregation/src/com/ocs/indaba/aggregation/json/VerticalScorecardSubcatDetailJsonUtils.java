/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.json;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.vo.CategoryNode;
import com.ocs.indaba.aggregation.vo.ScorecardBaseNode;
import com.ocs.indaba.aggregation.vo.ScorecardFinder;
import com.ocs.indaba.aggregation.vo.ScorecardInfo;
import com.ocs.indaba.util.LabelUtils;
import com.ocs.indaba.util.Tree;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff
 */
public class VerticalScorecardSubcatDetailJsonUtils extends JsonUtils {

    public static JSONObject toJson(ScorecardInfo scorecard, int subcatId) {
        JSONObject jsonObj = new JSONObject();
        if (scorecard == null) {
            return jsonObj;
        }
        Tree<ScorecardBaseNode> subcat = ScorecardFinder.findCategory(scorecard.getRootCategories(), subcatId);

        if (subcat == null || subcat.isLeaf() || subcat.getNode() == null
                || subcat.getNode().getNodeType() == Constants.NODE_TYPE_QUESTION) {
            return jsonObj;
        }

        jsonObj.put(KEY_VERSION, Constants.JSON_VERSION);
        jsonObj.put(KEY_TITLE, formatStrVal(scorecard.getTitle()));
        JSONObject dataJsonObj = new JSONObject();
        dataJsonObj.put(KEY_SUBCAT_NAME, formatStrVal(subcat.getNode().getName()));
        List<Tree<ScorecardBaseNode>> children = subcat.getChildren();
        if (children != null && !children.isEmpty()) {
            dataJsonObj.put(KEY_TITLE, formatStrVal(((CategoryNode) subcat.getNode()).getTitle()));
            dataJsonObj.put(KEY_LABEL, formatStrVal(((CategoryNode) subcat.getNode()).getLabel()));
            JSONArray jsonArr = new JSONArray();
            for (Tree<ScorecardBaseNode> child : children) {
                if (child.isLeaf() || child.getNode() == null || child.getNode().getNodeType() == Constants.NODE_TYPE_QUESTION) {
                    continue;
                }
                CategoryNode category = (CategoryNode) child.getNode();
                JSONObject questionSetObj = new JSONObject();
                questionSetObj.put(KEY_ID, category.getId());
                questionSetObj.put(KEY_TITLE, formatStrVal(category.getTitle()));
                questionSetObj.put(KEY_LABEL, formatStrVal(category.getLabel()));
                questionSetObj.put(KEY_NAME, category.getTitle());
                questionSetObj.put(KEY_SCORE_VALUE, category.getMean());
                questionSetObj.put(KEY_SCORE_LABEL, LabelUtils.getScoreLabel(category.getMean()));
                questionSetObj.put(KEY_USE_SCORE, category.hasUseScore());
                jsonArr.add(questionSetObj);
            }
            dataJsonObj.put(KEY_QUESTION_SETS, jsonArr);
        }
        jsonObj.put(KEY_DATA, dataJsonObj);
        return jsonObj;

    }
}
