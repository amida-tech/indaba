/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.rwi.json;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.json.JsonUtils;
import com.ocs.indaba.aggregation.vo.QuestionNode;
import com.ocs.indaba.aggregation.vo.ScorecardBaseNode;
import com.ocs.indaba.aggregation.vo.ScorecardFinder;
import com.ocs.indaba.aggregation.vo.ScorecardInfo;
import com.ocs.indaba.util.Tree;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff
 */
public class VerticalScorecardQuestionSetDetail4RWIJsonUtils extends JsonUtils {

    private static final Logger log = Logger.getLogger(VerticalScorecardQuestionSetDetail4RWIJsonUtils.class);

    public static JSONObject toJson(ScorecardInfo scorecard, int questionSetId) {
        JSONObject jsonObj = new JSONObject();
        if (scorecard == null) {
            return jsonObj;
        }
        jsonObj.put(KEY_SCORE_RANGE_DEFINITION, RWIReportUtils.generateScoreDisplayJsonObj());
        jsonObj.put(KEY_VERSION, Constants.JSON_VERSION);
        jsonObj.put(KEY_TITLE, scorecard.getTitle());

        Tree<ScorecardBaseNode> questionSet = ScorecardFinder.findCategory(scorecard.getRootCategories(), questionSetId);
        if (questionSet == null) {
            return jsonObj;
        }
        Tree<ScorecardBaseNode> subcat = questionSet.getParent();
        Tree<ScorecardBaseNode> cat = subcat.getParent();
        int catIdx = ScorecardFinder.findIndexOfSiblings(scorecard.getRootCategories(), cat.getNode().getId());
        int subcatIdx = ScorecardFinder.findIndexOfSiblings(subcat);
        int qsIdx = ScorecardFinder.findIndexOfSiblings(questionSet);
        List<QuestionNode> questions = new ArrayList<QuestionNode>();
        ScorecardFinder.listAllQuestions(questions, questionSet);
        if (questions.isEmpty()) {
            return jsonObj;
        }
        JSONObject dataJsonObj = new JSONObject();
        dataJsonObj.put(KEY_QUESTION_SET_NAME, questionSet.getNode().getName());
        JSONArray jsonArr = new JSONArray();
        for (QuestionNode question : questions) {
            if (catIdx >= RWIReportUtils.ORIG_CAT_COUNT) {
                String prefixLabel = (3 + subcatIdx) + ".";
                if (qsIdx == 0) {
                    prefixLabel += 1 + "." + 1;
                } else if (qsIdx == subcat.getChildren().size() - 1) {
                    prefixLabel += 3 + "." + 1;
                } else {
                    prefixLabel += 2 + "." + qsIdx;
                }
                question.setPublicName(RWIReportUtils.fixQuestionLabel(question.getPublicName(), prefixLabel));
            }
            if (Constants.ANSWER_TYPE_SINGLE == question.getQuestionType()) {
                jsonArr.add(questionDetail2Json(question));
            }
        }
        dataJsonObj.put(KEY_QUESTIONS, jsonArr);

        jsonObj.put(KEY_DATA, dataJsonObj);
        return jsonObj;
    }
}
