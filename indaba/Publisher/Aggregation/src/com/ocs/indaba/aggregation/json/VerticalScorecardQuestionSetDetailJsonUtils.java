/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.json;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.vo.QuestionNode;
import com.ocs.indaba.aggregation.vo.ScorecardBaseNode;
import com.ocs.indaba.aggregation.vo.ScorecardFinder;
import com.ocs.indaba.aggregation.vo.ScorecardInfo;
import com.ocs.indaba.util.Tree;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff
 */
public class VerticalScorecardQuestionSetDetailJsonUtils extends JsonUtils {

    public static JSONObject toJson(ScorecardInfo scorecard, int questionSetId) {
        JSONObject jsonObj = new JSONObject();
        if (scorecard == null) {
            return jsonObj;
        }
        jsonObj.put(KEY_VERSION, Constants.JSON_VERSION);
        jsonObj.put(KEY_TITLE, scorecard.getTitle());

        Tree<ScorecardBaseNode> subcat = ScorecardFinder.findCategory(scorecard.getRootCategories(), questionSetId);
        if (subcat == null) {
            return jsonObj;
        }
        List<QuestionNode> questions = new ArrayList<QuestionNode>();
        ScorecardFinder.listAllQuestions(questions, subcat);
        if (questions.isEmpty()) {
            return jsonObj;
        }
        JSONObject dataJsonObj = new JSONObject();
        dataJsonObj.put(KEY_QUESTION_SET_NAME, subcat.getNode().getName());
        JSONArray jsonArr = new JSONArray();
        for (QuestionNode question : questions) {
            if (Constants.ANSWER_TYPE_SINGLE == question.getQuestionType()) {
                jsonArr.add(questionDetail2Json(question));
            }
        }
        dataJsonObj.put(KEY_QUESTIONS, jsonArr);

        jsonObj.put(KEY_DATA, dataJsonObj);
        return jsonObj;

    }
}
