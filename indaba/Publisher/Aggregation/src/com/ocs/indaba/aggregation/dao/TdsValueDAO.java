/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.aggregation.po.TdsValue;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author luwb
 */
public class TdsValueDAO extends SmartDaoMySqlImpl<TdsValue, Integer>{
    private static final Logger log = Logger.getLogger(TdsValueDAO.class);
    private static final String SELECT_TDS_VALUE_BY_TDS = "SELECT * FROM tds_value WHERE dataset_id=? and target_id=? and datapoint_id=? and study_period_id=?";
    private static final String SELECT_TDS_VALUE_BY_FILTER =
            "SELECT tds.* FROM {1} tds "
            + "JOIN datapoint dp ON (tds.datapoint_id = dp.id) "
            + "JOIN dataset ds ON (tds.dataset_id = ds.id) "
            + "WHERE {0}";

    public void insertTdsValue(TdsValue value){
        TdsValue old = super.findSingle(SELECT_TDS_VALUE_BY_TDS, value.getDatasetId(), value.getTargetId(), value.getDatapointId(), value.getStudyPeriodId());
        if(old == null){
            super.create(value);
        }else{
            old.setValue(value.getValue());
            super.update(old);
        }
    }

    public List<TdsValue> selectTdsValueByFilter(String aggrScoreFilter, String srvTdsValue) {
        String sqlStr = MessageFormat.format(SELECT_TDS_VALUE_BY_FILTER, aggrScoreFilter, srvTdsValue);
        return super.find(sqlStr);
    }
}
