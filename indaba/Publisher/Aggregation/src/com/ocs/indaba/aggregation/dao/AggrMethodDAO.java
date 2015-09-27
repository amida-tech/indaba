/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.dao;

import com.ocs.indaba.aggregation.po.AggrMethod;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.aggregation.po.Datapoint;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author luwb
 */
public class AggrMethodDAO extends SmartDaoMySqlImpl<AggrMethod, Integer>{
    private static final Logger log = Logger.getLogger(AggrMethodDAO.class);

    public List<AggrMethod> selectALLAggrMethod(){
        log.debug("select all aggr method");
        return super.findAll();
    }
}
