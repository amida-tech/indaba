/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.json;

import com.ocs.indaba.aggregation.cache.BasePersistence;
import com.ocs.indaba.aggregation.cache.SRFPersistence;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.service.TargetService;
import com.ocs.indaba.aggregation.vo.HorseBriefVO;
import com.ocs.indaba.aggregation.vo.ProductInfo;
import com.ocs.indaba.aggregation.vo.QuestionNode;
import com.ocs.indaba.aggregation.vo.ScorecardFinder;
import com.ocs.indaba.aggregation.vo.ScorecardInfo;
import com.ocs.indaba.util.LabelUtils;
import com.ocs.indaba.util.SpringContextUtil;
import java.io.File;
import java.util.List;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author jiangjeff
 */
public class HorizontalScorecardQuestionSummaryJsonUtils extends JsonUtils {

    private static final Logger log = Logger.getLogger(HorizontalScorecardQuestionSummaryJsonUtils.class);

    public static JSONObject toJson(int productId, int questionId, ProductInfo productInfo) {
        JSONObject jsonObj = new JSONObject();
        File[] srfFiles = BasePersistence.getSRFFiles(productId, null);
        if (srfFiles == null || srfFiles.length == 0) {
            return jsonObj;
        }
        TargetService trgtService = (TargetService) SpringContextUtil.getBean("pubTargetService");
        List<HorseBriefVO> completedHorses = trgtService.getCompletedTargetsForProduct(productId);

        JSONArray targetQuestionJsonArr = new JSONArray();
        SRFPersistence srfPersistence = new SRFPersistence(null);
        ScorecardInfo scorecard = null;
        for (File srfFile : srfFiles) {
            scorecard = srfPersistence.deserializeSRF(srfFile);
            if (!isCompleted(scorecard.getHorseId(), completedHorses)) {
                continue;
            }
            QuestionNode questionNode = ScorecardFinder.findQuestion(scorecard.getRootCategories(), questionId);
            targetQuestionJsonArr.add(question2Json(scorecard.getHorseId(), scorecard.getTargetName(), /*questionNode.getQuestionText(), questionNode.isCompleted(),*/ questionNode.getScore(), questionNode.hasUseScore()));
            if(jsonObj.get(KEY_QUESTION_TEXT) == null){
                jsonObj.put(KEY_QUESTION_TEXT, questionNode.getQuestionText());
            }
        }

        JSONObject dataJsonObj = new JSONObject();
        // base info
        dataJsonObj.put(KEY_TARGETS, targetQuestionJsonArr);
        jsonObj.put(KEY_VERSION, Constants.JSON_VERSION);
        if (scorecard != null) {
            jsonObj.put(KEY_SURVEY_NAME, scorecard.getSurveyName());
        }
        jsonObj.put(KEY_PRODUCT_DESC, productInfo.getProdouctDesc());
        jsonObj.put(KEY_DATA, dataJsonObj);
        return jsonObj;
    }

    private static boolean isCompleted(int horseId, List<HorseBriefVO> completedHorses) {
        if (completedHorses == null || completedHorses.isEmpty()) {
            return false;
        }
        for (HorseBriefVO h : completedHorses) {
            if (h.getHorseId() == horseId) {
                return true;
            }
        }
        return false;
    }

    private static JSONObject question2Json(int horseId, String targetName, /*String questionText, boolean done,*/ double scoreValue, boolean useScore) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_HORSE_ID, horseId);
        jsonObj.put(KEY_TARGET_NAME, targetName);
        //jsonObj.put(KEY_DONE, done);
        jsonObj.put(KEY_SCORE_VALUE, scoreValue);
        //jsonObj.put(KEY_QUESTION_TEXT, questionText);
        jsonObj.put(KEY_SCORE_LABEL, LabelUtils.getScoreLabel(scoreValue));
        jsonObj.put(KEY_USE_SCORE, useScore);
        return jsonObj;
    }
}
