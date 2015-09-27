/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.rwi.json;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.json.JsonUtils;
import com.ocs.indaba.aggregation.vo.CategoryNode;
import com.ocs.indaba.aggregation.vo.ScorecardBaseNode;
import com.ocs.indaba.aggregation.vo.ScorecardFinder;
import com.ocs.indaba.aggregation.vo.ScorecardInfo;
import com.ocs.indaba.util.LabelUtils;
import com.ocs.indaba.util.Tree;
import java.util.List;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff
 */
public class VerticalScorecardSubcatDetail4RWIJsonUtils extends JsonUtils {

    private static final Logger log = Logger.getLogger(VerticalScorecardSubcatDetail4RWIJsonUtils.class);
    private static final String CAT_NAME_OF_COMPREHENSIVE_REPORTS = "Comprehensive reports";

    public static JSONObject toJson(ScorecardInfo scorecard, int subcatId) {
        JSONObject jsonObj = new JSONObject();
        if (scorecard == null) {
            return jsonObj;
        }
        return (subcatId < 0) ? virtualToJson(scorecard, subcatId) : normalToJson(scorecard, subcatId);

    }

    public static JSONObject normalToJson(ScorecardInfo scorecard, int subcatId) {
        JSONObject jsonObj = new JSONObject();
        Tree<ScorecardBaseNode> subcat = ScorecardFinder.findCategory(scorecard.getRootCategories(), subcatId);

        if (subcat == null || subcat.isLeaf() || subcat.getNode() == null
                || subcat.getNode().getNodeType() == Constants.NODE_TYPE_QUESTION) {
            return jsonObj;
        }

        jsonObj.put(KEY_SCORE_RANGE_DEFINITION, RWIReportUtils.generateScoreDisplayJsonObj());
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
                jsonArr.add(questionSetNodeToJson((CategoryNode) child.getNode()));
            }
            dataJsonObj.put(KEY_QUESTION_SETS, jsonArr);
        }
        jsonObj.put(KEY_DATA, dataJsonObj);
        return jsonObj;

    }

    public static JSONObject virtualToJson(ScorecardInfo scorecard, int subcatId) {
        JSONObject jsonObj = new JSONObject();
        if (scorecard == null) {
            return jsonObj;
        }
        int[] arr = RWIReportUtils.parseVirtualSubcatNodeId(subcatId);
        subcatId = arr[0];
        int idx = arr[1];

        Tree<ScorecardBaseNode> subcat = ScorecardFinder.findCategory(scorecard.getRootCategories(), subcatId);
        int pIdx = ScorecardFinder.findIndexOfSiblings(subcat) + 3;
        String prefixLabel = pIdx + "." + idx + ".";

        if (subcat == null || subcat.isLeaf() || subcat.getNode() == null
                || subcat.getNode().getNodeType() == Constants.NODE_TYPE_QUESTION) {
            return jsonObj;
        }
        jsonObj.put(KEY_SCORE_RANGE_DEFINITION, RWIReportUtils.generateScoreDisplayJsonObj());
        jsonObj.put(KEY_VERSION, Constants.JSON_VERSION);
        jsonObj.put(KEY_TITLE, formatStrVal(scorecard.getTitle()));
        JSONObject dataJsonObj = new JSONObject();
        dataJsonObj.put(KEY_SUBCAT_NAME, formatStrVal(subcat.getNode().getName()));
        JSONArray jsonArr = new JSONArray();
        List<Tree<ScorecardBaseNode>> children = subcat.getChildren();
        CategoryNode qsNode = null;
        switch (idx) {
            case 1:
                qsNode = (CategoryNode) children.get(0).getNode();
                qsNode.setLabel(RWIReportUtils.fixQuestionSetLabel(qsNode.getLabel(), prefixLabel + 1));
                jsonArr.add(questionSetNodeToJson(qsNode));
                break;
            case 2:
                for (int i = 1, n = children.size() - 1; i < n; ++i) {
                    //++count;
                    qsNode = (CategoryNode) children.get(i).getNode();
                    qsNode.setLabel(RWIReportUtils.fixQuestionSetLabel(qsNode.getLabel(), prefixLabel + i));
                    if (pIdx == 4 && idx == 2 && i == 1) { // 4.2.1
                        qsNode.setName(CAT_NAME_OF_COMPREHENSIVE_REPORTS);
                        qsNode.setTitle(CAT_NAME_OF_COMPREHENSIVE_REPORTS);
                    }
                    jsonArr.add(questionSetNodeToJson(qsNode));
                }
                break;
            case 3:
                qsNode = (CategoryNode) children.get(children.size() - 1).getNode();
                qsNode.setLabel(RWIReportUtils.fixQuestionSetLabel(qsNode.getLabel(), prefixLabel + 1));
                jsonArr.add(questionSetNodeToJson(qsNode));
                break;
            default:
                log.error("Invalid virtual cateogry index: " + idx);
        }
        dataJsonObj.put(KEY_QUESTION_SETS, jsonArr);
        jsonObj.put(KEY_DATA, dataJsonObj);
        return jsonObj;
    }

    private static JSONObject questionSetNodeToJson(CategoryNode qsNode) {
        JSONObject qsJsonObj = new JSONObject();
        qsJsonObj.put(KEY_ID, qsNode.getId());
        qsJsonObj.put(KEY_TITLE, formatStrVal(qsNode.getTitle()));
        qsJsonObj.put(KEY_LABEL, formatStrVal(qsNode.getLabel()));
        qsJsonObj.put(KEY_NAME, qsNode.getTitle());
        qsJsonObj.put(KEY_SCORE_VALUE, qsNode.getMean());
        qsJsonObj.put(KEY_SCORE_LABEL, LabelUtils.getScoreLabel(qsNode.getMean()));
        qsJsonObj.put(KEY_USE_SCORE, qsNode.hasUseScore());
        return qsJsonObj;
    }
}
