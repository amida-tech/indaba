/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.ReferenceChoice;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author luwb
 */
public class ReferenceChoiceDAO extends SmartDaoMySqlImpl<ReferenceChoice, Integer> {

    private static final Logger log = Logger.getLogger(ReferenceChoiceDAO.class);
    private static final String SELECT_REFERENCE_CHOICE_BY_REFER_ID =
            "SELECT * FROM reference_choice WHERE reference_id = ? ORDER BY weight";

    public List<ReferenceChoice> selectReferenceByReferId(int referenceId) {
        return super.find(SELECT_REFERENCE_CHOICE_BY_REFER_ID, referenceId);
    }
}
