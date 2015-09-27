/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.service;

import com.ocs.indaba.controlpanel.model.IndicatorImportValidation;
import com.ocs.indaba.controlpanel.model.IndicatorCloneVO;
import com.ocs.indaba.controlpanel.model.IndicatorDetailVO;
import com.ocs.indaba.controlpanel.model.IndicatorTransValidation;
import com.ocs.indaba.controlpanel.model.IndicatorVO;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.util.Pagination;
import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;

/**
 *
 * @author Jeff Jiang
 */
public interface IIndicatorLibraryService {

    /*
    public Pagination<IndicatorVO> findIndicators(int visibility, String userTag, int indicatorTag, int organization);

    public Pagination<IndicatorVO> findIndicators(int visibility, String userTag, int indicatorTag, int organization, int offset, int count);
     */
    public Pagination<IndicatorVO> findAllIndicators(
            List<Integer> oaOfOrgIds, int orgId, int visibility, int state, int iTagId, String userTag,
            String sortName, String sortOrder, int offset, int count, String queryType, String query);

    public long findCountOfAllIndicators(List<Integer> oaOfOrgIds, int orgId, int visibility, int state, int iTagId, String userTag, String queryType, String query);

    public String deleteIndicator(LoginUser user, int id);

    public String moveIndicator(LoginUser user, int id, int toLib);

    public String editIndicator(LoginUser user, IndicatorDetailVO indicator);

    public String cloneIndicator(LoginUser user, IndicatorCloneVO ic);

    public IndicatorImportValidation validateIndicatorImport(LoginUser user, File file);

    public int importIndicators(LoginUser user, File file);

    public IndicatorDetailVO getIndicator(LoginUser user, int id, int languageId);

    public int translate(LoginUser user, int origIndicatorId, int langId, IndicatorDetailVO indicator);

    /**
     * Get indicator by language
     *
     * @param id The Indicator Id
     * @param langId The Language Id (By default, if langId is not
     * specified(e.g. langId<= 0), it will return the original indicator text.
     * The original text comes from the survey_indicator and act_choice tables.
     * The language is whatever in the survey_indicator table.)
     * @return The corresponding indicator text.
     */
    //public IndicatorI18nVO getIndicatorByLang(int id, int langId);

    public String createIndicator(LoginUser user, IndicatorDetailVO indicator);

    /**
     * 
     * @param indicatorIDs
     * @param languageId
     * @param writer
     * @return
     */
    public int exportIndicators(LoginUser user, List<Integer> indicatorIDs, int languageId, OutputStream output);

    
    public int exportIndicatorText(LoginUser user, List<Integer> indicatorIDs, int languageId, OutputStream output);


    /**
     *  Validate a csv file that contains indicator translations
     * 
     * @param file - the csv file
     * @param userId - the id of the user doing the import
     * @return validation result
     */
    public IndicatorTransValidation validateTranslationImport(LoginUser user, File file);

    
    /**
     *  Import a csv file that contains indicator translations
     * 
     * @param file - the csv file
     * @param userId - the id of the user doing the import
     * @return number of indicators
     */
    public int importTranslations(LoginUser user, File file);

    /**
     * Whether the user has editing authority for the indicator
     * @param userId
     * @param indicatorId
     * @return
     */
    public boolean hasEditAuthority(LoginUser user, SurveyIndicator indicator);
    
    public boolean hasEditAuthority(LoginUser user, IndicatorVO indicator);

    /**
     * check any bans on edit
     * @param indicatorId
     * @return error string if there is any ban. Null if no ban. The error string is localized.
     */
    public String checkEditBans(LoginUser user, SurveyIndicator indicator);

    /**
     * Whether the user has moving authority for the indicator
     *
     * @param userId
     * @param indicatorId
     * @return
     */
    public boolean hasMoveAuthority(LoginUser user, SurveyIndicator indicator);

    public boolean hasMoveAuthority(LoginUser user, IndicatorVO indicator);

    /**
     * check any bans on move
     *
     * @param indicatorId
     * @return error string if there is any ban. Null if no ban. The error string is localized.
     */
    public String checkMoveBans(LoginUser user, SurveyIndicator indicator, int toLib);


    public boolean hasDeleteAuthority(LoginUser user, SurveyIndicator indicator);

    public boolean hasDeleteAuthority(LoginUser user, IndicatorVO indicator);

    public String checkDeleteBans(LoginUser user, SurveyIndicator indicator);

    public boolean hasTranslateAuthority(LoginUser user, IndicatorVO indicator);

}
