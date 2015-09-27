/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.aggregation.po.TdsValueA;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author luwb
 */
public class TdsValueADAO extends SmartDaoMySqlImpl<TdsValueA, Integer>{
    private static final Logger log = Logger.getLogger(TdsValueADAO.class);
    private static final String SELECT_TDS_VALUE_BY_TDS = "SELECT * FROM tds_value_a WHERE dataset_id=? and target_id=? and datapoint_id=? and study_period_id=?";
   
    public void insertTdsValue(TdsValueA value){
        TdsValueA old = super.findSingle(SELECT_TDS_VALUE_BY_TDS, value.getDatasetId(), value.getTargetId(), value.getDatapointId(), value.getStudyPeriodId());
        if(old == null){
            super.create(value);
        }else{
            old.setValue(value.getValue());
            super.update(old);
        }
    }
}
