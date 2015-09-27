/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.JournalContentVersion;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff
 */
public class JournalContentVersionDAO extends SmartDaoMySqlImpl<JournalContentVersion, Integer> {

    private static final Logger logger = Logger.getLogger(JournalContentVersionDAO.class);
    private static final String SELECT_CONTENT_VERSION_BY_CONTENT_VERSION_ID =
            "SELECT * FROM journal_content_version WHERE content_version_id=?";

    public JournalContentVersion getJournalContentVersion(int cntVerId) {
        return super.findSingle(SELECT_CONTENT_VERSION_BY_CONTENT_VERSION_ID, cntVerId);
    }

}
