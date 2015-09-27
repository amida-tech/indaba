/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Language;
import org.apache.log4j.Logger;

import java.util.List;

/**
 *
 * @author Luke
 */
public class LanguageDAO extends SmartDaoMySqlImpl<Language, Integer> {

    private static final Logger log = Logger.getLogger(LanguageDAO.class);
    private static final String SELECT_LANGUAGE_BY_ID = "SELECT * from language WHERE id=?";
    private static final String SELECT_ALL_LANGUAGES = "SELECT * from language WHERE status=0";

    /**
     * SELECT Language
     * @param username
     * @param password
     * @return
     */
    public Language selectLanguageById(long id) {
        log.debug("Select table Language: " + SELECT_LANGUAGE_BY_ID + "[id=" + id + "].");
        return super.findSingle(SELECT_LANGUAGE_BY_ID, id);
    }

    public List<Language> selectAllLanguages() {
        return super.find(SELECT_ALL_LANGUAGES);
    }
}




