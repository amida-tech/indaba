/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.model;

import au.com.bytecode.opencsv.CSVReader;
import com.ocs.indaba.po.Language;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.controlpanel.service.impl.IndicatorCsvDefs;
import com.ocs.indaba.dao.ItagsDAO;
import com.ocs.indaba.dao.LanguageDAO;
import com.ocs.indaba.dao.OrganizationDAO;
import com.ocs.indaba.dao.ReferenceDAO;
import com.ocs.indaba.po.Itags;
import com.ocs.indaba.po.Organization;
import com.ocs.indaba.po.Reference;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 *
 * @author seanpcheng
 */
@Component
public class Utilities {

    private static LanguageDAO langDao = null;
    private static OrganizationDAO orgDao = null;
    private static ReferenceDAO refDao = null;
    private static ItagsDAO tagDao = null;

    @Autowired
    public void setLanguageDAO(LanguageDAO dao) {
        langDao = dao;
    }

    @Autowired
    public void setOrganizationDAO(OrganizationDAO dao) {
        orgDao = dao;
    }

    
    @Autowired
    public void setItagsDAO(ItagsDAO dao) {
        tagDao = dao;
    }

    @Autowired
    public void setReferenceDAO(ReferenceDAO dao) {
        refDao = dao;
    }
    

    static public String normalizeString(String str) {
        if (str == null) {
            return null;
        }

        String value = str.replaceAll("\\s", "");

        return value.toUpperCase();
    }

    static public Map<String, Integer> buildLanguageMapN2I() {
        HashMap<String, Integer> langmap = new HashMap<String, Integer>();
        List<Language> langs = langDao.selectAllLanguages();

        for (Language lang : langs) {
            String str = normalizeString(lang.getLanguage());
            langmap.put(str, lang.getId());

            str = normalizeString(lang.getLanguageDesc());
            langmap.put(str, lang.getId());
        }

        return langmap;
    }

    static public HashMap<Integer, String> buildLanguageMapI2N() {
        HashMap<Integer, String> langmap = new HashMap<Integer, String>();
        List<Language> langs = langDao.selectAllLanguages();

        for (Language lang : langs) {
            String str = lang.getLanguageDesc();
            langmap.put(lang.getId(), str);
        }

        return langmap;
    }


    static public Map<String, Integer> buildIndicatorTypeMapN2I() {
        HashMap<String, Integer> typemap = new HashMap<String, Integer>();
        typemap.put(IndicatorCsvDefs.TYPE_LABEL_SINGLE_CHOICE, Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE);
        typemap.put(IndicatorCsvDefs.TYPE_LABEL_MULTI_CHOICE, Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE);
        typemap.put(IndicatorCsvDefs.TYPE_LABEL_FLOAT, Constants.SURVEY_ANSWER_TYPE_FLOAT);
        typemap.put(IndicatorCsvDefs.TYPE_LABEL_INTEGER, Constants.SURVEY_ANSWER_TYPE_INTEGER);
        typemap.put(IndicatorCsvDefs.TYPE_LABEL_INT, Constants.SURVEY_ANSWER_TYPE_INTEGER);
        typemap.put(IndicatorCsvDefs.TYPE_LABEL_TEXT, Constants.SURVEY_ANSWER_TYPE_TEXT);

        return typemap;
    }

    static public Map<String, Integer> buildOrgMapN2I() {
        HashMap<String, Integer> orgmap = new HashMap<String, Integer>();
        List<Organization> orgs = orgDao.selectAllOrgs();

        for (Organization org : orgs) {
            String str = normalizeString(org.getName());
            orgmap.put(str, org.getId());
        }

        return orgmap;
    }

    static public Map<String, Integer> buildVisibilityMapN2I() {
        HashMap<String, Integer> vismap = new HashMap<String, Integer>();
        vismap.put(IndicatorCsvDefs.VIS_LABEL_PUBLIC, (int) Constants.VISIBILITY_PUBLIC);
        vismap.put(IndicatorCsvDefs.VIS_LABEL_PRIVATE, (int) Constants.VISIBILITY_PRIVATE);

        return vismap;
    }

    static public Map<String, Integer> buildResourceStateMapN2I() {
        HashMap<String, Integer> statemap = new HashMap<String, Integer>();
        statemap.put(IndicatorCsvDefs.STATE_LABEL_ENDORSED, Constants.RESOURCE_STATE_ENDORSED);
        statemap.put(IndicatorCsvDefs.STATE_LABEL_EXTENDED, Constants.RESOURCE_STATE_EXTENDED);
        statemap.put(IndicatorCsvDefs.STATE_LABEL_TEST, Constants.RESOURCE_STATE_TEST);

        return statemap;
    }


    static public Map<String, Integer> buildTagMapN2I() {
        HashMap<String, Integer> tagmap = new HashMap<String, Integer>();
        List<Itags> tags = tagDao.selectAllItags();

        for (Itags tag : tags) {
            String str = normalizeString(tag.getTerm());
            tagmap.put(str, tag.getId());
        }
        return tagmap;
    }


    static public Map<String, Integer> buildRefMapN2I() {
        HashMap<String, Integer> refmap = new HashMap<String, Integer>();
        List<Reference> refs = refDao.selectAllReferences();

        for (Reference ref : refs) {
            String str = normalizeString(ref.getName());
            refmap.put(str, ref.getId());
        }
        return refmap;
    }


    static public HashMap<Integer, String> buildOrgMapI2N() {
        HashMap<Integer, String> orgmap = new HashMap<Integer, String>();
        List<Organization> orgs = orgDao.selectAllOrgs();

        for (Organization org : orgs) {
            String str = org.getName();
            orgmap.put(org.getId(), str);
        }

        return orgmap;
    }

    static public HashMap<Integer, String> buildVisibilityMapI2N() {
        HashMap<Integer, String> vismap = new HashMap<Integer, String>();
        vismap.put((int) Constants.VISIBILITY_PUBLIC, IndicatorCsvDefs.VIS_LABEL_PUBLIC);
        vismap.put((int) Constants.VISIBILITY_PRIVATE, IndicatorCsvDefs.VIS_LABEL_PRIVATE);
        return vismap;
    }

    static public HashMap<Integer, String> buildResourceStateMapI2N() {
        HashMap<Integer, String> statemap = new HashMap<Integer, String>();
        statemap.put(Constants.RESOURCE_STATE_ENDORSED, IndicatorCsvDefs.STATE_LABEL_ENDORSED);
        statemap.put(Constants.RESOURCE_STATE_EXTENDED, IndicatorCsvDefs.STATE_LABEL_EXTENDED);
        statemap.put(Constants.RESOURCE_STATE_TEST, IndicatorCsvDefs.STATE_LABEL_TEST);
        return statemap;
    }

    static public HashMap<Integer, String> buildTagMapI2N() {
        HashMap<Integer, String> tagmap = new HashMap<Integer, String>();
        List<Itags> tags = tagDao.selectAllItags();

        for (Itags tag : tags) {
            String str = tag.getTerm();
            tagmap.put(tag.getId(), str);
        }
        return tagmap;
    }

    static public HashMap<Integer, String> buildRefMapI2N() {
        HashMap<Integer, String> refmap = new HashMap<Integer, String>();
        List<Reference> refs = refDao.selectAllReferences();

        for (Reference ref : refs) {
            String str = ref.getName();
            refmap.put(ref.getId(), str);
        }
        return refmap;
    }


    static public HashMap<Integer, String> buildIndicatorTypeMapI2N() {
        HashMap<Integer, String> typemap = new HashMap<Integer, String>();
        typemap.put(Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE, IndicatorCsvDefs.TYPE_LABEL_SINGLE_CHOICE);
        typemap.put(Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE, IndicatorCsvDefs.TYPE_LABEL_MULTI_CHOICE);
        typemap.put(Constants.SURVEY_ANSWER_TYPE_FLOAT, IndicatorCsvDefs.TYPE_LABEL_FLOAT);
        typemap.put(Constants.SURVEY_ANSWER_TYPE_INTEGER, IndicatorCsvDefs.TYPE_LABEL_INTEGER);
        typemap.put(Constants.SURVEY_ANSWER_TYPE_TEXT, IndicatorCsvDefs.TYPE_LABEL_TEXT);
        return typemap;
    }

    static public boolean similar(String value, String expected) {
        return (normalizeString(value).startsWith(expected));
    }
}
