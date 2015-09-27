/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.json;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.vo.QuestionNode;
import com.ocs.indaba.aggregation.vo.ScorecardFinder;
import com.ocs.indaba.aggregation.vo.ScorecardInfo;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff
 */
public class ScorecardAnswerJsonUtils extends JsonUtils {

    private static final Logger log = Logger.getLogger(ScorecardAnswerJsonUtils.class);

    public static JSONObject toJson(ScorecardInfo scorecard, int questionId) {
        JSONObject jsonObj = new JSONObject();
        if (scorecard == null) {
            return jsonObj;
        }
        QuestionNode question = ScorecardFinder.findQuestion(scorecard.getRootCategories(), questionId);
        if (question != null && Constants.ANSWER_TYPE_SINGLE == question.getQuestionType()) {
            jsonObj.put(KEY_VERSION, Constants.JSON_VERSION);
            JSONObject dataJsonObj = questionDetail2Json(question);
                jsonObj.put(KEY_DATA, dataJsonObj);
        }
        return jsonObj;

    }
}
