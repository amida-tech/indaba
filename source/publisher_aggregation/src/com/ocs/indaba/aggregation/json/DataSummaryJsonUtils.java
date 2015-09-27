/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.json;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.cache.BasePersistence;
import com.ocs.indaba.aggregation.cache.SRFPersistence;
import com.ocs.indaba.aggregation.cache.ServiceDataCachePersistence;
import com.ocs.indaba.aggregation.vo.ChartVO;
import com.ocs.indaba.aggregation.vo.ScorecardInfo;
import com.ocs.indaba.util.LabelUtils;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author jiangjeff
 */
public class DataSummaryJsonUtils extends JsonUtils {

    private static final Logger log = Logger.getLogger(DataSummaryJsonUtils.class);
    private final static String KEY_AGGR_NAME_OVERALL = "Overall Score";
    private final static String KEY_AGGR_NAME_LEGAL_FRAMEWORK = "Legal Framework Score";
    private final static String KEY_AGGR_NAME_ACTUAL_IMPLEMENTATION = "Actual Implementation Score";
    private final static String CHART_TITLE_PATTERN = "Comparison to {0} diverse countries";
    private final static String GENERIC_CHART_TITLE = "Comparison to Peers";

    public static JSONObject toJson(int horseId, String baseDir, String cacheDir, Boolean genericLabel) {
        JSONObject jsonObj = ServiceDataCachePersistence.loadCachedDataSummaryJson(horseId, cacheDir);

        if (jsonObj == null) {
            SRFPersistence scorcardPersistence = new SRFPersistence(baseDir);
            ScorecardInfo scorecard = scorcardPersistence.deserializeSRF(BasePersistence.getSRFFile(horseId, baseDir));
            if (scorecard != null)
                jsonObj = toJson(scorecard, baseDir, genericLabel);
        }
        if (genericLabel && jsonObj != null) {
            jsonObj.put(KEY_CHART_TITLE, GENERIC_CHART_TITLE);
        }

        return jsonObj;
    }

    public static JSONObject toJson(ScorecardInfo scorecard, String baseDir, Boolean genericLabel) {
        JSONObject dataJsonObj = new JSONObject();
        // base info
        dataJsonObj.put(KEY_TITLE, scorecard.getTitle());
        dataJsonObj.put(KEY_TARGET, scorecard.getTargetName());
        dataJsonObj.put(KEY_SURVEY_NAME, scorecard.getSurveyName());
        dataJsonObj.put(KEY_PRODUCT_NAME, scorecard.getProdouctName());
        dataJsonObj.put(KEY_STUDY_PERIOD, scorecard.getStudyPeriod());
        // additional aggregation
        dataJsonObj.put(KEY_OVERALL, scorecard.getOverall());
        dataJsonObj.put(KEY_OVERALL_LABEL, LabelUtils.getScoreLabel(scorecard.getOverall()));
        dataJsonObj.put(KEY_MOE, scorecard.getMoe());
        dataJsonObj.put(KEY_LEGAL_FRAMEWORK, scorecard.getLegalFramework());
        dataJsonObj.put(KEY_IMPLEMENTATION, scorecard.getImplementation());
        dataJsonObj.put(KEY_GAP, (scorecard.getLegalFramework() - scorecard.getImplementation()));
        dataJsonObj.put(KEY_GAP_LABEL, LabelUtils.getScoreLabel(scorecard.getLegalFramework() - scorecard.getImplementation()));

        dataJsonObj.put(KEY_USE_SCORE, (scorecard.getUsedScoreCount() > 0));
        List<Double> overalls = new ArrayList<Double>();
        List<Double> legalFrameworks = new ArrayList<Double>();
        List<Double> implementations = new ArrayList<Double>();
        int[] indexes = listAggregations(baseDir, scorecard.getProductId(), scorecard.getHorseId(), overalls, legalFrameworks, implementations);
        
        if (genericLabel) {
            dataJsonObj.put(KEY_CHART_TITLE, GENERIC_CHART_TITLE);
        } else {
            dataJsonObj.put(KEY_CHART_TITLE, MessageFormat.format(CHART_TITLE_PATTERN, overalls.size()));
        }
        
        JSONArray chartJsonArr = new JSONArray();
        if (indexes != null && indexes.length >= 3) {
            chartJsonArr.add(chart2Json(KEY_AGGR_NAME_OVERALL, indexes[0], overalls));
            chartJsonArr.add(chart2Json(KEY_AGGR_NAME_LEGAL_FRAMEWORK, indexes[1], legalFrameworks));
            chartJsonArr.add(chart2Json(KEY_AGGR_NAME_ACTUAL_IMPLEMENTATION, indexes[2], implementations));
        }
        dataJsonObj.put(KEY_AGGRS, chartJsonArr);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_VERSION, Constants.JSON_VERSION);
        jsonObj.put(KEY_DATA, dataJsonObj);
        return dataJsonObj;
    }

    private static JSONObject chart2Json(String chartName, int index, List<Double> values) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_CHART_NAME, chartName);
        jsonObj.put(KEY_TARGET_INDEX, index);
        JSONArray valArr = new JSONArray();
        for (double val : values) {
            valArr.add(val);
        }
        jsonObj.put(KEY_VALUES, valArr);
        return jsonObj;
    }

    /**
     * List all standard aggregations of other targets and return the corresponding indexes of current target's values
     * 
     * @param productId
     * @param theHorseId
     * @param overalls
     * @param legalFramworks
     * @param implementations
     * @return index array: [0] - overall index; [1] - legal framework index; [2] - implementation index.
     */
    private static int[] listAggregations(String baseDir, int productId, int theHorseId, List<Double> overalls, List<Double> legalFramworks, List<Double> implementations) {
        //int index = 0;
        if (overalls == null) {
            overalls = new ArrayList<Double>();
        }
        if (legalFramworks == null) {
            legalFramworks = new ArrayList<Double>();
        }
        if (implementations == null) {
            implementations = new ArrayList<Double>();
        }
        File[] srfFiles = BasePersistence.getSRFFiles(productId, baseDir);
        if (srfFiles == null || srfFiles.length == 0) {
            return null;
        }
        SRFPersistence persistence = new SRFPersistence(baseDir);
        //int i = 0;
        double overall = 0.0d;
        double legal = 0.0d;
        double impl = 0.0d;
        for (File srfFile : srfFiles) {
            ScorecardInfo scorecard = persistence.deserializeSRF(srfFile);
            if (scorecard == null) {
                continue;
            }
            //++i;
            if (theHorseId == scorecard.getHorseId()) {
                //index = i;
                overall = scorecard.getOverall();
                legal = scorecard.getLegalFramework();
                impl = scorecard.getImplementation();
            } else {
                overalls.add(scorecard.getOverall());
                legalFramworks.add(scorecard.getLegalFramework());
                implementations.add(scorecard.getImplementation());
            }
        }
        int[] indexes = new int[]{addToSortedList(overalls, overall), addToSortedList(legalFramworks, legal), addToSortedList(implementations, impl)};
        return indexes;
    }

    public static JSONArray getJsonCharts(JSONObject jsonObj) {
        return (JSONArray) jsonObj.get(KEY_AGGRS);
    }

    public static ChartVO parseJsonChart(JSONObject jsonChart) {
        ChartVO chart = new ChartVO();
        chart.setChartName(jsonChart.get(KEY_CHART_NAME).toString());
        chart.setTargetIndex(getIntVal(jsonChart, KEY_TARGET_INDEX));
        JSONArray valArr = (JSONArray) jsonChart.get(KEY_VALUES);
        chart.setValues((Double[]) valArr.toArray(new Double[]{}));
        return chart;
    }
}
