/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.json;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.vo.ProductInfo;
import com.ocs.indaba.aggregation.vo.QuestionNode;
import com.ocs.indaba.aggregation.vo.ScorecardFinder;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author jiangjeff
 */
public class HorizontalScorecardSummaryJsonUtils1 extends JsonUtils {

    private static final Logger log = Logger.getLogger(HorizontalScorecardSummaryJsonUtils1.class);

    public static JSONObject toJson(ProductInfo product) {
        JSONObject dataJsonObj = new JSONObject();
        // base info
        dataJsonObj.put(KEY_SURVEY_NAME, product.getSurveyName());
        dataJsonObj.put(KEY_PRODUCT_NAME, product.getProdouctName());
        dataJsonObj.put(KEY_PRODUCT_NAME, product.getProdouctName());
        dataJsonObj.put(KEY_STUDY_PERIOD, product.getStudyPeriod());
        List<QuestionNode> questions = new ArrayList<QuestionNode>();
        ScorecardFinder.listAllQuestions(questions, product.getRootCategories());
        JSONArray questionArr = new JSONArray();
        if(questions != null && !questions.isEmpty()) {
            for(QuestionNode question: questions) {
                questionArr.add(question2Json(question));
            }
        }
        dataJsonObj.put(KEY_QUESTIONS, questionArr);
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_VERSION, Constants.JSON_VERSION);
        jsonObj.put(KEY_DATA, dataJsonObj);
        return dataJsonObj;
    }
}
