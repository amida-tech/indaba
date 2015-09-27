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
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author luwb
 */
public class MysqlBuilder extends BaseBuilder{
    private final static Logger log = Logger.getLogger(MysqlBuilder.class);

    @Override
    public void build() {
        Config conf = aggregationService.getConfig();
        if(conf == null){
            log.error("can't get configuration from config table");
            return;
        }
        List<Workset> worksetList = aggregationService.getAllWorkset();
        if (worksetList != null) {
            for (Workset workset : worksetList) {
                AggBuildDataVO aggDataWithPub = aggregationService.processAggregationData(conf, workset.getId());
                saveAggragationData(conf, workset, aggDataWithPub);
            }
        }
        aggregationService.updateConfig(conf);
    }

    private void saveAggragationData(Config conf, Workset workset, AggBuildDataVO data){
        if (data == null) return;
        
        List<TSDataVO> tsDatas = data.getTsDataList();
        if(tsDatas.size() == 0)
            return;

        int pubDatasetId = -1;
        int allDatasetId = -1;
        List<Dataset> datasets = aggregationService.getDatasetByWorkset(workset.getId());
        for(Dataset dataset : datasets){
            if(dataset.getIncludesNonpubData())
                allDatasetId = dataset.getId();
            else
                pubDatasetId = dataset.getId();
        }

        for(TSDataVO tsData : tsDatas){
            //save Published Indicators
            List<OtisValue> pubOtisValues = tsData.getPub_indicators();
            for(OtisValue value : pubOtisValues)
                aggregationService.saveOtisValue(conf, value);

            //save NonPublished Indicators
            List<OtisValue> nonpubOtisValues = tsData.getNonpub_indicators();
            for(OtisValue value : nonpubOtisValues)
                aggregationService.saveOtisValue(conf, value);

            //save Published Datapoint
            List<TdsValue> pubTdsValues = tsData.getPub_datapoints();
            for(TdsValue value : pubTdsValues){
                value.setDatasetId(pubDatasetId);
                aggregationService.saveTdsValue(conf, value);
            }
            aggregationService.updateDatasetLastUpdateTime(pubDatasetId);

            //save Useable Datapoint
            List<TdsValue> allTdsValues = tsData.getAll_datapoints();
            for(TdsValue value : allTdsValues){
                value.setDatasetId(allDatasetId);
                aggregationService.saveTdsValue(conf, value);
            }
            aggregationService.updateDatasetLastUpdateTime(allDatasetId);
        }
    }

    public static void main(String[] args) {
        MysqlBuilder builder = new MysqlBuilder();
        builder.build();
    }
}
