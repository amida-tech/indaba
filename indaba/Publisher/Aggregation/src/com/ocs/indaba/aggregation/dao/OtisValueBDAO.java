/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.aggregation.po.OtisValueB;
import java.math.BigDecimal;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author luwb
 */
public class OtisValueBDAO extends SmartDaoMySqlImpl<OtisValueB, Integer>{
    private static final Logger log = Logger.getLogger(OtisValueBDAO.class);
    private static final String SELECT_OTIS_VALUE_BY_OTIS = "SELECT * FROM otis_value_b where org_id=? and target_id=? and indicator_id=? and study_period_id=?";

    public void insertOtisValue(OtisValueB value){
        OtisValueB old = super.findSingle(SELECT_OTIS_VALUE_BY_OTIS, value.getOrgId(), value.getTargetId(), value.getIndicatorId(), value.getStudyPeriodId());
        if(old == null){
            super.create(value);
        }else{
            old.setScore(value.getScore());
            //old.setValue(value.getValue());
            super.update(old);
        }
    }
}
