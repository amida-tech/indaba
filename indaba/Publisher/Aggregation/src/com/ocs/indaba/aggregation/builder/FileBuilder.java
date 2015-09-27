/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.builder;

import com.ocs.indaba.aggregation.po.Config;
import com.ocs.indaba.aggregation.po.Dataset;
import com.ocs.indaba.aggregation.po.OtisValue;
import com.ocs.indaba.aggregation.po.TdsValue;
import com.ocs.indaba.aggregation.po.Workset;
import com.ocs.indaba.aggregation.vo.AggBuildDataVO;
import com.ocs.indaba.aggregation.vo.TSDataVO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author luwb
 */
public class FileBuilder extends BaseBuilder{
    private final static Logger log = Logger.getLogger(FileBuilder.class);

    @Override
    public void build() {
        if(!initialized)
            return;
        
        File rootFold = new File(getRootPath());
        if(!rootFold.exists())
            rootFold.mkdir();

        Config conf = aggregationService.getConfig();
        if(conf == null){
            log.error("can't get configuration from config table");
            return;
        }
        List<Workset> worksetList = FileBuilder.aggregationService.getAllWorkset();
        for(Workset workset : worksetList){
            AggBuildDataVO aggDataWithPub = aggregationService.processAggregationData(conf, workset.getId());
            saveWorksetData(workset, aggDataWithPub);
        }
    }

    private void saveWorksetData(Workset workset, AggBuildDataVO data){
        List<TSDataVO> tsDatas = data.getTsDataList();
        if(tsDatas.size() == 0)
            return;

        //create fold first
        String worksetPath = getRootPath() + "/WS" + workset.getId();
        File worksetFold = new File(worksetPath);
        if(!worksetFold.exists())
            worksetFold.mkdir();

        for(TSDataVO tsData : tsDatas){
            saveTSData(worksetPath, workset, tsData);
        }

        //update dataset last_update_time
        aggregationService.updateDatasetLastUpdateTimeByWorksetId(workset.getId());
    }

    private void saveTSData(String worksetPath, Workset workset, TSDataVO tsData){
        //save published data
        JSONObject publishedData = worksetToJson(workset, tsData, false);
        if(publishedData != null){
            String publishedPath = worksetPath + "/T" + tsData.getTargetId() + "_S" + tsData.getStudyPeriodId() + "_" + getPublishedSuffix();
            File publishedFile = new File(publishedPath);
            saveJsonFile(publishedData, publishedFile);
        }

        //save usable data
        JSONObject usableData = worksetToJson(workset, tsData, true);
        if(usableData != null){
            String usablePath = worksetPath + "/T" + tsData.getTargetId() + "_S" + tsData.getStudyPeriodId() + "_" + getUsableSuffix();
            File usableFile = new File(usablePath);
            saveJsonFile(usableData, usableFile);
        }
    }

    public void saveJsonFile(JSONObject jsonObj, File file) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        OutputStreamWriter writer = null;
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            writer = new OutputStreamWriter(fos, "UTF-8");
            jsonObj.writeJSONString(writer);
        } catch (Exception ex) {
            log.error("IO Error.", ex);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.warn("Fail to close file output stream: " + file.getAbsolutePath(), e);
                }
            }

            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.warn("Fail to close Output Stream Writer: " + file.getAbsolutePath(), e);
                }
            }
        }
    }

    private static JSONObject worksetToJson(Workset workset, TSDataVO tsData, boolean includeNonPublished){
        JSONObject root = new JSONObject();
        root.put(KEY_WORKSET_ID, workset.getId());
        root.put(KEY_TARGET_ID, tsData.getTargetId());
        root.put(KEY_STUDY_PERIOD_ID, tsData.getStudyPeriodId());

        JSONArray indicators = new JSONArray();
        JSONArray datapoints = new JSONArray();
        for(OtisValue otis : tsData.getPub_indicators()){
            JSONObject node = new JSONObject();
            node.put(KEY_INDICATOR_ID, otis.getIndicatorId());
            node.put(KEY_VALUE, otis.getScore().doubleValue());
            node.put(KEY_ORG_ID, otis.getOrgId());
            indicators.add(node);
        }
        if(includeNonPublished){
            for(OtisValue otis : tsData.getNonpub_indicators()){
                JSONObject node = new JSONObject();
                node.put(KEY_INDICATOR_ID, otis.getIndicatorId());
                node.put(KEY_VALUE, otis.getScore().doubleValue());
                node.put(KEY_ORG_ID, otis.getOrgId());
                indicators.add(node);
            }
            for(TdsValue tds : tsData.getAll_datapoints()){
                JSONObject node = new JSONObject();
                node.put(KEY_DATAPOINT_ID, tds.getDatapointId());
                node.put(KEY_VALUE, tds.getValue().doubleValue());
                datapoints.add(node);
            }
        }else{
            for(TdsValue tds : tsData.getPub_datapoints()){
                JSONObject node = new JSONObject();
                node.put(KEY_DATAPOINT_ID, tds.getDatapointId());
                node.put(KEY_VALUE, tds.getValue().doubleValue());
                datapoints.add(node);
            }
        }
        if(indicators.size() > 0)
            root.put(KEY_INDICATORS, indicators);

        if(datapoints.size() > 0)
            root.put(KEY_DATAPOINTS, datapoints);

        return root;
    }

    public static void main(String[] args) {
        FileBuilder builder = new FileBuilder();
        builder.build();
    }
}


