/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Reference;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author luwb
 */
public class ReferenceDAO extends SmartDaoMySqlImpl<Reference, Integer> {

    private static String SELECT_ALL_REFERENCES = "SELECT * FROM reference";
    
    private static final Logger log = Logger.getLogger(ReferenceDAO.class);

    public List<Reference> selectAllReferences() {
        return super.find(SELECT_ALL_REFERENCES);
    }
    
    public Reference selectReferenceById(int referenceId) {
        return super.get(referenceId);
    }

}
