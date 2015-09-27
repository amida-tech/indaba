/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
*/

package com.ocs.indaba.service;

import java.util.List;

import com.ocs.indaba.dao.LanguageDAO;
import com.ocs.indaba.po.Language;

import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Luke
 */
public class LanguageService {

    private LanguageDAO languageDAO;

    public Language getLanguageById(int languageId) {
        return languageDAO.selectLanguageById(languageId);
    }

    public List<Language> getAllLanguages() {
        return languageDAO.selectAllLanguages();
    }

    @Autowired
    public void setLanguageDao(LanguageDAO languageDAO) {
        this.languageDAO = languageDAO;
    }

}
