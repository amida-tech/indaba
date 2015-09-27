/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.json;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.vo.ProductInfo;
import com.ocs.indaba.aggregation.vo.ScorecardBaseNode;
import com.ocs.indaba.util.Tree;
import java.util.List;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author jiangjeff
 */
public class HorizontalScorecardSummaryJsonUtils extends JsonUtils {

    private static final Logger log = Logger.getLogger(HorizontalScorecardSummaryJsonUtils.class);

    public static JSONObject toJson(ProductInfo product) {

        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_VERSION, Constants.JSON_VERSION);

        // base info
        jsonObj.put(KEY_SURVEY_NAME, product.getSurveyName());
        jsonObj.put(KEY_PRODUCT_DESC, product.getProdouctDesc());
        jsonObj.put(KEY_PRODUCT_NAME, product.getProdouctName());
        jsonObj.put(KEY_PRODUCT_NAME, product.getProdouctName());
        jsonObj.put(KEY_STUDY_PERIOD, product.getStudyPeriod());

        // structured categories/indicators
        List<Tree<ScorecardBaseNode>> rootCategories = product.getRootCategories();
        if (rootCategories != null && !rootCategories.isEmpty()) {
            JSONArray arr = new JSONArray();
            for (Tree<ScorecardBaseNode> category : rootCategories) {
                arr.add(PIFJsonUtils.tree2Json(category));
            }
            jsonObj.put(KEY_DATA, arr);
        }
        return jsonObj;
    }
}
