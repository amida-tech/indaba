/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.AccessMatrix;
import org.apache.log4j.Logger;

public class AccessMatrixDAO extends SmartDaoMySqlImpl<AccessMatrix, Integer> {
    private static final Logger log = Logger.getLogger(AccessMatrixDAO.class);
     public AccessMatrix selectMatrixById(int accessMatrixId) {
         log.debug("Select access matrix by id: " + accessMatrixId);
         return super.get(accessMatrixId);
     }
}
