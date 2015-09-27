/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.json;

import com.ocs.indaba.aggregation.cache.BasePersistence;
import com.ocs.indaba.aggregation.cache.SRFPersistence;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.service.TargetService;
import com.ocs.indaba.aggregation.vo.CategoryNode;
import com.ocs.indaba.aggregation.vo.HorseBriefVO;
import com.ocs.indaba.aggregation.vo.ProductInfo;
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
public class HorizontalScorecardSubCategorySummaryJsonUtils extends JsonUtils {

    private static final Logger log = Logger.getLogger(HorizontalScorecardSubCategorySummaryJsonUtils.class);

    public static JSONObject toJson(int productId, int subcatId, ProductInfo productInfo) {
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
            CategoryNode subcatNode = ScorecardFinder.findSubCategory(scorecard.getRootCategories(), subcatId);
            targetQuestionJsonArr.add(subcat2Json(scorecard.getHorseId(), scorecard.getTargetName(), subcatNode.getMean(), subcatNode.hasUseScore()));
            if(jsonObj.get(KEY_SUBCAT_NAME) == null){
                jsonObj.put(KEY_SUBCAT_NAME, subcatNode.getTitle());
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

    private static JSONObject subcat2Json(int horseId, String targetName, double meanValue, boolean useScore) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_HORSE_ID, horseId);
        jsonObj.put(KEY_TARGET_NAME, targetName);
        //jsonObj.put(KEY_DONE, done);
        jsonObj.put(KEY_SCORE_VALUE, meanValue);
        //jsonObj.put(KEY_QUESTION_TEXT, questionText);
        jsonObj.put(KEY_SCORE_LABEL, LabelUtils.getScoreLabel(meanValue));
        jsonObj.put(KEY_USE_SCORE, useScore);
        return jsonObj;
    }
}
