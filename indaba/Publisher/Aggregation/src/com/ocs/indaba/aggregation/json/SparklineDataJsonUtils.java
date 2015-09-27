/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.json;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.cache.BasePersistence;
import com.ocs.indaba.aggregation.cache.PIFPersistence;
import com.ocs.indaba.aggregation.cache.SRFPersistence;
import com.ocs.indaba.aggregation.vo.CategoryNode;
import com.ocs.indaba.aggregation.vo.ChartVO;
import com.ocs.indaba.aggregation.vo.ProductInfo;
import com.ocs.indaba.aggregation.vo.ScorecardBaseNode;
import com.ocs.indaba.aggregation.vo.ScorecardFinder;
import com.ocs.indaba.aggregation.vo.ScorecardInfo;
import com.ocs.indaba.util.Tree;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author jiangjeff
 */
public class SparklineDataJsonUtils extends JsonUtils {

    private static final Logger log = Logger.getLogger(DataSummaryJsonUtils.class);

    public static JSONObject toJson(int prodcutId, List<Integer> horseIds) {
        JSONObject jsonObj = new JSONObject();
        PIFPersistence pifPersistence = new PIFPersistence(null);
        ProductInfo prodInfo = pifPersistence.deserializePIF(BasePersistence.getPIFFile(prodcutId, null));
        if (prodInfo == null) {
            return jsonObj;
        }
        Map<Integer, Double> medianMap = extractSubcatMedianScores(prodInfo.getRootCategories());
        JSONObject dataJsonObj = new JSONObject();
        // base info
        dataJsonObj.put(KEY_SURVEY_NAME, prodInfo.getSurveyName());
        dataJsonObj.put(KEY_PRODUCT_NAME, prodInfo.getProdouctName());
        dataJsonObj.put(KEY_STUDY_PERIOD, prodInfo.getStudyPeriod());

        JSONArray chartArr = new JSONArray();
        SRFPersistence srfPersistence = new SRFPersistence(null);
        for (int hid : horseIds) {
            ScorecardInfo scorecard = srfPersistence.deserializeSRF(new File(BasePersistence.getBasePath(), BasePersistence.getSRFFilepath(prodcutId, hid)));
            if (scorecard == null) {
                continue;
            }
            List<CategoryNode> subcats = ScorecardFinder.listCategories(scorecard.getRootCategories());
            if (subcats == null || subcats.isEmpty()) {
                continue;
            }
            JSONObject o = new JSONObject();
            o.put(KEY_HORSE_ID, scorecard.getHorseId());
            o.put(KEY_TARGET_ID, scorecard.getTargetId());
            o.put(KEY_TARGET_NAME, scorecard.getTargetName());
            JSONArray valArr = new JSONArray();
            for (CategoryNode subcat : subcats) {
                Double median = medianMap.get(subcat.getId());
                valArr.add(computePercentDifference(subcat.getMean(), (median == null || median == 0) ? 1 : median));
            }
//            Collections.sort(valArr);
            o.put(KEY_VALUES, valArr);
            chartArr.add(o);
        }

        dataJsonObj.put(KEY_CHARTS, chartArr);

        jsonObj.put(KEY_VERSION, Constants.JSON_VERSION);
        jsonObj.put(KEY_DATA, dataJsonObj);
        return dataJsonObj;
    }

    private static Map<Integer, Double> extractSubcatMedianScores(List<Tree<ScorecardBaseNode>> rootCategories) {
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        if (rootCategories == null) {
            return map;
        }
        List<CategoryNode> subcats = ScorecardFinder.listCategories(rootCategories);
        if (subcats == null && subcats.isEmpty()) {
            return map;
        }
        for (CategoryNode cat : subcats) {
            map.put(cat.getId(), cat.getMedian());
        }
        return map;
    }

    private static double computePercentDifference(double score, double median) {
        if (median == 0) {
            median = 1;
        }
        return ((score / median) - 1) * 100;
    }

    public static JSONArray getJsonCharts(JSONObject jsonSparkline) {
        return (JSONArray) jsonSparkline.get(KEY_CHARTS);
    }

    public static ChartVO parseJsonChart(JSONObject jsonChart) {
        ChartVO chart = new ChartVO();
        chart.setChartName(jsonChart.get(KEY_TARGET_NAME).toString());
        chart.setTargetIndex(-1);
        JSONArray valArr = (JSONArray) jsonChart.get(KEY_VALUES);
        chart.setValues((Double[]) valArr.toArray(new Double[]{}));
        return chart;
    }
}
