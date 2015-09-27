/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.json;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.cache.BasePersistence;
import com.ocs.indaba.aggregation.cache.SRFPersistence;
import com.ocs.indaba.aggregation.cache.ServiceDataCachePersistence;
import com.ocs.indaba.aggregation.vo.CategoryNode;
import com.ocs.indaba.aggregation.vo.ChartVO;
import com.ocs.indaba.aggregation.vo.ScorecardBaseNode;
import com.ocs.indaba.aggregation.vo.ScorecardFinder;
import com.ocs.indaba.aggregation.vo.ScorecardInfo;
import com.ocs.indaba.builder.dao.StudyPeriodDAO;
import com.ocs.indaba.po.StudyPeriod;
import com.ocs.indaba.util.Tree;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jiangjeff
 */
@Component
public class IndicatorSummaryJsonUtils extends JsonUtils {
    
    private static final Logger log = Logger.getLogger(IndicatorSummaryJsonUtils.class);
    //private final static String TITLE = "Global Integrity Report";
    private final static String CHARTS_TITLE = "Integrity Indicators Scorecard Summary, ";

    private static StudyPeriodDAO studyPeriodDao = null;

    public static JSONObject toJson(int horseId, String baseDir, String cacheDir, Boolean genericLabel) {
        JSONObject jsonObj = ServiceDataCachePersistence.loadCachedIndicatorSummaryJson(horseId, cacheDir);
        if (jsonObj == null) {
            SRFPersistence scorcardPersistence = new SRFPersistence(baseDir);
            log.info("Processing Horse " + horseId);
            File srfFile = BasePersistence.getSRFFile(horseId, baseDir);
            if (srfFile == null || !srfFile.exists()) {
                log.error("File not found for SRF for horse[" + horseId + "]!");
                return new JSONObject();
            } else {
                ScorecardInfo scorecard = scorcardPersistence.deserializeSRF(srfFile);
                jsonObj = toJson(scorecard, baseDir);
            }
        }
        if (jsonObj != null) {
            if (genericLabel)
                jsonObj.put(KEY_CHARTS_TITLE, "");
            else {
                StudyPeriod sp = studyPeriodDao.selectStudyPeriodByHorseId(horseId);
                jsonObj.put(KEY_CHARTS_TITLE, CHARTS_TITLE + sp.getName());
            }
        }
        return jsonObj;
    }

    private static JSONObject toJson(ScorecardInfo scorecard, String baseDir) {
        JSONObject dataJsonObj = new JSONObject();
        // base info
        dataJsonObj.put(KEY_TITLE, scorecard.getTitle());
        dataJsonObj.put(KEY_TARGET, scorecard.getTargetName());
        dataJsonObj.put(KEY_SURVEY_NAME, scorecard.getSurveyName());
        dataJsonObj.put(KEY_PRODUCT_NAME, scorecard.getProdouctName());
        dataJsonObj.put(KEY_STUDY_PERIOD, scorecard.getStudyPeriod());
        
        Map<Integer, List<Double>> map = new HashMap<Integer, List<Double>>();
        int index = listAllSubcats(baseDir, scorecard.getProductId(), scorecard.getHorseId(), map);
        //List<CategoryNode> subcats = ScorecardFinder.findSubCats(scorecard.getRootCategories());

        
        List<Tree<ScorecardBaseNode>> rootCategories = scorecard.getRootCategories();
        if (rootCategories == null || rootCategories.isEmpty()) {
            return null;
        }
        
        JSONArray chartArr = new JSONArray();
        //List<CategoryNode> subcats = new ArrayList<CategoryNode>();
        for (Tree<ScorecardBaseNode> category : rootCategories) {
            if (category.isLeaf() || category.getNode() == null || category.getNode().getNodeType() == Constants.NODE_TYPE_QUESTION) {
                continue;
            }
            List<Tree<ScorecardBaseNode>> subcatNodes = category.getChildren();
            if (subcatNodes == null) {
                continue;
            }
            JSONArray catArrJsonObj = new JSONArray();
            for (Tree<ScorecardBaseNode> subcatTree : subcatNodes) {
                if (subcatTree.isLeaf() || subcatTree.getNode() == null || subcatTree.getNode().getNodeType() == Constants.NODE_TYPE_QUESTION) {
                    continue;
                }
                CategoryNode subcat = (CategoryNode) subcatTree.getNode();
                List<Double> values = map.get(subcat.getId());
                if (values == null || values.isEmpty() || index == -1) {
                    continue;
                }
                //log.debug("==> [" + subcat.getId() + "] INDEX: " + index + ", BaseDir: " + baseDir);
                Double curVal = values.get(index);
                values.remove(curVal);
                JSONObject chartJsonObj = new JSONObject();
                chartJsonObj.put(KEY_CHART_NAME, subcat.getTitle());
                chartJsonObj.put(KEY_TARGET_INDEX, addToSortedList(values, curVal));
                JSONArray valArr = new JSONArray();
                for (double val : values) {
                    valArr.add(val);
                }
                chartJsonObj.put(KEY_VALUES, valArr);
                //chartJsonObj.put(KEY_EOS, false);
                catArrJsonObj.add(chartJsonObj);
            }
            chartArr.add(catArrJsonObj);
        }
        map.clear();
        dataJsonObj.put(KEY_CHARTS, chartArr);
        
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_VERSION, Constants.JSON_VERSION);
        jsonObj.put(KEY_DATA, dataJsonObj);
        return dataJsonObj;
    }
    
    private static int listAllSubcats(String baseDir, int productId, int theHorseId, Map<Integer, List<Double>> map) {
        int index = -1;
        
        File[] srfFiles = BasePersistence.getSRFFiles(productId, baseDir);
        if (srfFiles == null || srfFiles.length == 0) {
            return index;
        }
        SRFPersistence persistence = new SRFPersistence(baseDir);
        int i = -1;
        for (File srfFile : srfFiles) {
            ScorecardInfo scorecard = persistence.deserializeSRF(srfFile);
            if (scorecard == null) {
                //log.debug("************* SCORECARD IS EMPTY!!!");
                continue;
            }
            List<CategoryNode> subcats = ScorecardFinder.listCategories(scorecard.getRootCategories());
            if (subcats == null || subcats.isEmpty()) {
                //log.debug("************* SUBCATS IS EMPTY!!!");
                continue;
            }
            ++i;
            //log.debug(">>>>>>>>>>>> PRD: " + productId + " i: " + i);
            if (theHorseId == scorecard.getHorseId()) {
                index = i;
            }
            for (CategoryNode subcat : subcats) {
                List<Double> values = map.get(subcat.getId());
                
                if (values == null) {
                    values = new ArrayList<Double>();
                    map.put(subcat.getId(), values);
                }
                values.add(subcat.getMean());
                //log.debug("PUT: " + subcat.getId() + ", Len of values: " + values.size());
            }
        }
        return index;
    }
    
    public static JSONArray getJsonCharts(JSONObject jsonIndicatorSummary) {
        return (JSONArray) jsonIndicatorSummary.get(KEY_CHARTS);
    }
    
    public static ChartVO parseJsonChart(JSONObject jsonChart) {
        ChartVO chart = new ChartVO();
        chart.setChartName(jsonChart.get(KEY_CHART_NAME).toString());
        chart.setTargetIndex(getIntVal(jsonChart, KEY_TARGET_INDEX));
        JSONArray valArr = (JSONArray) jsonChart.get(KEY_VALUES);
        chart.setValues((Double[]) valArr.toArray(new Double[]{}));
        return chart;
    }

    @Autowired
    public void setStudyPeriodDAO(StudyPeriodDAO studyPeriodDao) {
        IndicatorSummaryJsonUtils.studyPeriodDao = studyPeriodDao;
        //log.debug("StudyPeriodDAO set!");
    }
}
