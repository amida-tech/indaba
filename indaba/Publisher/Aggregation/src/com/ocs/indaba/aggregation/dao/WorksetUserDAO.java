/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.dao;

import com.ocs.indaba.aggregation.po.WsPuser;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import org.apache.log4j.Logger;

/**
 *
 * @author jiangjeff
 */
public class WorksetUserDAO extends SmartDaoMySqlImpl<WsPuser, Integer> {

    private static final Logger log = Logger.getLogger(WorksetUserDAO.class);
}
