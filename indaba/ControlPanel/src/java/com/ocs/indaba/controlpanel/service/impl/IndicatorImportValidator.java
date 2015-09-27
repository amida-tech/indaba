/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.service.impl;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.controlpanel.common.ControlPanelConstants;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.model.Utilities;
import com.ocs.indaba.controlpanel.model.IndicatorChoice;
import com.ocs.indaba.controlpanel.model.IndicatorImportValidation;
import com.ocs.indaba.controlpanel.model.IndicatorValidation;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.dao.*;
import com.ocs.indaba.po.*;
import com.ocs.util.StringUtils;
import com.ocs.ssu.NotUTF8EncodedException;
import com.ocs.ssu.ReaderFactory;
import com.ocs.ssu.SpreadsheetReader;
import com.ocs.ssu.UnsupportedFileTypeException;
import com.ocs.util.StringUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author seanpcheng
 */
@Component
public class IndicatorImportValidator {

    private Map<String, Integer> orgMap = null;
    private Map<String, Integer> visMap = null;
    private Map<String, Integer> stateMap = null;
    private Map<String, Integer> langMap = null;
    private Map<String, Integer> tagMap = null;
    private Map<String, Integer> refMap = null;
    private Map<String, Integer> typeMap = null;

    private HashMap<String, String> nameMap = null;
    private SpreadsheetReader reader;
    private File file;
    private LoginUser user;

    // whether to skip indicator name validation against DB.
    private boolean componentMode = false;

    private static SurveyIndicatorDAO indicatorDao = null;
    private static final Logger logger = Logger.getLogger(IndicatorImportValidator.class);

    /**
     * Initialize all DAO objects
     *
     * NOTE: '@Autowired setter' is invoked to automatically pre-instantiate the
     * object(with non-parameters default contructor) when lanuching the webapp
     * by Spring framework. And all @Autowired classes MUST be specified in
     * Spring configuration file WEB-INF/applicationContext.xml(with <bean
     * id="xxx" class="xxx" />).
     *
     * In fact, @Autowired ONLY works with non-parameters default contructor.
     * Hence, in this class IndicatorImportValidator, it hasn't default
     * contructor and cannot leverage @Autowired yet. So, we'd better manually
     * initialize all the required DAO objects. Additionally, now that all DAO
     * classes are designed and implemented as thread-safe, we can specify it as
     * 'static' for better performance(That is, it is singleton and instantiated
     * only once in runtime.).
     */
    
   
    @Autowired
    public void setSurveyIndicatorDAO(SurveyIndicatorDAO dao) {
        indicatorDao = dao;
    }
        

    public IndicatorImportValidator(LoginUser user, File file) {
        this.file = file;
        this.user = user;
        nameMap = new HashMap<String, String>();
    }

    // this constructor is needed to make autowired work
    public IndicatorImportValidator() {        
    }
    
    public IndicatorImportValidator(LoginUser user, String file) {
        this.file = new File(file);
        this.user = user;
        nameMap = new HashMap<String, String>();
    }


    public void setComponentMode(boolean value) {
        this.componentMode = value;
    }
    
    // Read and return the next non-blank line
    public IndicatorImportValidation validate() throws FileNotFoundException, IOException {
        
        logger.debug("Validating " + this.file + " uploaded by user " + user.getUserId());
        
        // Build orgMap
        orgMap = Utilities.buildOrgMapN2I();
        visMap = Utilities.buildVisibilityMapN2I();
        stateMap = Utilities.buildResourceStateMapN2I();
        langMap = Utilities.buildLanguageMapN2I();
        typeMap = Utilities.buildIndicatorTypeMapN2I();
        tagMap = Utilities.buildTagMapN2I();
        refMap = Utilities.buildRefMapN2I();

        IndicatorImportValidation result = new IndicatorImportValidation();

        try {
            reader = ReaderFactory.createReader(file);
        } catch (UnsupportedFileTypeException ex) {
            result.addGeneralError(0, user.message(ControlPanelMessages.INDICATOR_IMPORT__FILE_TYPE_NOT_SUPPORTED, ex.getFileType()));
            return result;
        } catch (NotUTF8EncodedException ex) {
            result.addGeneralError(0, user.message(ControlPanelMessages.INDICATOR_IMPORT__FILE_NOT_UTF8));
            return result;
        }
        
        String[] row;
        

        result.setUserId(user.getUserId());

        if (componentMode) {
            // Process main indicator name
            row = reader.readNext();
            if (!processMainName(row, result)) {
                return result;
            }
        } else {
            // Process default creator org
            row = reader.readNext();
            if (!processCreatorOrg(row, result)) {
                return result;
            }

            // Process default visibility
            row = reader.readNext();

            if (!processVisbility(row, result)) {
                return result;
            }

            // Process Maturity State
            row = reader.readNext();
            if (!processState(row, result)) {
                return result;
            }

            //Process Language
            row = reader.readNext();
            if (!processLang(row, result)) {
                return result;
            }
        }

        row = reader.readNext();
        
        // Look for header line
        if (!checkHeaderLine(row, result)) {
            return result;
        }


        // Start to process indicators
        boolean readNextLine;
        row = reader.readNext();

        while (row != null) {

            readNextLine = true;

            if (!row[0].isEmpty()) {

                IndicatorValidation val = processFullLine(row, result);

                if (val.getErrorMsg() == null) {
                    // good
                    int type = val.getType();

                    if (type == Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE || type == Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE) {
                        // process secondary lines
                        // see if the main line also contains choices
                        if (row.length > IndicatorCsvDefs.COL_POS_OPTION) {
                            String choiceLabel = row[IndicatorCsvDefs.COL_POS_OPTION];
                            if (!StringUtils.isEmpty(choiceLabel)) {
                                processSecondaryLine(row, val);
                            }
                        }

                        while ((row = reader.readNext()) != null) {

                            // check if this is a secondary line
                            if (row[0].isEmpty()) {
                                processSecondaryLine(row, val);

                                if (val.getErrorMsg() != null) {
                                    break;
                                }
                            } else {
                                // done with secondary lines
                                readNextLine = false;
                                break;
                            }
                        }

                        // make sure there is at least one choice 
                        if (val.getChoices() == null || val.getChoices().isEmpty()) {
                            val.setErrorMsg(reader.getLineNumber(),
                                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, "CHOICES"));
                        }
                    }
                }

                result.addIndicatorValidation(val);
            }


            if (readNextLine == true) {
                row = reader.readNext();
            }
        }

        // Validate user rights
        // check whether the user is a site-admin: user userDao
        
        
        // check whether the user is admin of the orgs
        // check each indicator
        List<IndicatorValidation> vals = result.getIndicatorValidations();

        if (vals == null || vals.isEmpty()) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__NO_INDICATORS));
        }

        return result;
    }
    
    

    static private String normalizeString(String str) {
        return Utilities.normalizeString(str);
    }


    private boolean processMainName(String[] row, IndicatorImportValidation result) {
        if (row == null || row.length < 2) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_LINE, "MAIN INDICATOR"));
            return false;
        }

        if (row[0].isEmpty() || row[1].isEmpty()) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_LINE, "MAIN INDICATOR"));
            return false;
        }

        String value = normalizeString(row[0]);

        if (!value.startsWith(IndicatorCsvDefs.COL_LABEL_MAIN_INDICATOR)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_LINE, "MAIN INDICATOR"));
            return false;
        }

        return true;
    }


    private boolean processCreatorOrg(String[] row, IndicatorImportValidation result) {
        if (row == null || row.length < 2) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_LINE, "CREATOR ORG"));
            return false;
        }

        if (row[0].isEmpty() || row[1].isEmpty()) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_LINE, "CREATOR ORG"));
            return false;
        }

        String value = normalizeString(row[0]);


        if (!value.startsWith(IndicatorCsvDefs.COL_LABEL_CREATOR_ORG)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_LINE, "CREATOR ORG"));
            return false;
        }

        value = normalizeString(row[1]);
        Integer orgId = orgMap.get(value);

        if (orgId == null) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_LINE_VALUE, "CREATOR ORG", value));
        } else {
            result.setDefaultOrgID(orgId);
        }

        return true;
    }

    private boolean processVisbility(String[] row, IndicatorImportValidation result) {
        if (row == null || row.length < 2) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_LINE, "VISIBILITY"));
            return false;
        }
        if (row[0].isEmpty() || row[1].isEmpty()) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_LINE, "VISIBILITY"));
            return false;
        }
        String value = normalizeString(row[0]);


        if (!value.startsWith(IndicatorCsvDefs.COL_LABEL_VISIBILITY)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_LINE, "VISIBILITY"));
            return false;
        }

        value = normalizeString(row[1]);
        Integer visibility = visMap.get(value);

        if (visibility == null) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_LINE_VALUE, "VISIBILITY", row[1]));
        } else {
            result.setDefaultVisibility(visibility);
        }

        return true;
    }

    private boolean processState(String[] row, IndicatorImportValidation result) {
        if (row == null || row.length < 1) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_LINE, "MATURITY STATE"));
            return false;
        }
        if (row[0].isEmpty()) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_LINE, "MATURITY STATE"));
            return false;
        }

        String value = normalizeString(row[0]);

        if (!value.startsWith(IndicatorCsvDefs.COL_LABEL_STATE)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_LINE, "MATURITY STATE"));
            return false;
        }

        if (row.length > 1 && row[1] != null && !row[1].isEmpty()) {
            value = normalizeString(row[1]);
            Integer stateID = stateMap.get(value);

            if (stateID == null) {
                result.addGeneralError(reader.getLineNumber(),
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_LINE_VALUE, "MATURITY STATE", row[1]));
            } else {
                result.setDefaultState(stateID);
            }
        }

        // public indicators are always put in Test library
        result.setDefaultState(Constants.RESOURCE_STATE_TEST);

        return true;
    }

    private boolean processLang(String[] row, IndicatorImportValidation result) {        
        if (row == null || row.length < 2) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_LINE, "LANGUAGE"));
            return false;
        }
        if (row[0].isEmpty() || row[1].isEmpty()) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_LINE, "LANGUAGE"));
            return false;
        }

        String value = normalizeString(row[0]);
        
        if (!value.startsWith(IndicatorCsvDefs.COL_LABEL_LANGUAGE)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_LINE, "LANGUAGE"));
            return false;
        }

        value = normalizeString(row[1]);
        Integer langID = langMap.get(value);

        if (langID == null) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_LINE_VALUE, "LANGUAGE", row[1]));
        } else {
            result.setDefaultLang(langID);
        }

        return true;
    }
    

    private IndicatorValidation processFullLine(String[] row, IndicatorImportValidation result) {
        IndicatorValidation val = new IndicatorValidation(row[IndicatorCsvDefs.COL_POS_NAME], result);

        if (nameMap.containsKey(row[IndicatorCsvDefs.COL_POS_NAME]) == true) {
            val.setErrorMsg(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__NAME_NOT_UNIQUE));
            return val;
        }

        if (val.getName().length() > ControlPanelConstants.MAX_LENGTH_INDICATOR_NAME) {
            val.setErrorMsg(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__FIELD_TOO_LONG, "NAME",
                        ControlPanelConstants.MAX_LENGTH_INDICATOR_NAME));
            return val;
        }
        
        nameMap.put(val.getName(), val.getName());

        if (!this.componentMode) {
            SurveyIndicator indicator = indicatorDao.selectSurveyIndicatorByName(row[IndicatorCsvDefs.COL_POS_NAME]);

            if (indicator != null) {
                val.setErrorMsg(reader.getLineNumber(),
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__NAME_ALREADY_IN_DB));
                return val;
            }
        }

        if (row.length < IndicatorCsvDefs.COL_POS_QUESTION + 1 || row[IndicatorCsvDefs.COL_POS_QUESTION].isEmpty()) {
            val.setErrorMsg(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, "QUESTION"));
            return val;
        }
        val.setQuestion(row[IndicatorCsvDefs.COL_POS_QUESTION]);
        
        
        
        if (row.length < IndicatorCsvDefs.COL_POS_TYPE + 1 || row[IndicatorCsvDefs.COL_POS_TYPE].isEmpty()) {
            // missing type
            val.setErrorMsg(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, "TYPE"));
            return val;
        }

        String v = normalizeString(row[IndicatorCsvDefs.COL_POS_TYPE]);
        Integer type = typeMap.get(v);

        if (type == null) {
            val.setErrorMsg(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN_VALUE, "TYPE", row[IndicatorCsvDefs.COL_POS_TYPE]));
            return val;
        }
        val.setType(type);

        if (row.length < IndicatorCsvDefs.COL_POS_TYPE + 1 || row[IndicatorCsvDefs.COL_POS_TYPE].isEmpty()) {
            val.setErrorMsg(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, "REFERENCE"));
            return val;
        }

        v = normalizeString(row[IndicatorCsvDefs.COL_POS_REFERENCE]);
        Integer refId = refMap.get(v);

        if (refId == null) {
            val.setErrorMsg(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN_VALUE, "REFERENCE", row[IndicatorCsvDefs.COL_POS_REFERENCE]));
            return val;
        }
        val.setRefId(refId);

        if (row.length > IndicatorCsvDefs.COL_POS_HINT) {
            val.setTip(row[IndicatorCsvDefs.COL_POS_HINT]);
        }

        if (row.length > IndicatorCsvDefs.COL_POS_TAGS && !row[IndicatorCsvDefs.COL_POS_TAGS].isEmpty()) {
            String tags[] = row[IndicatorCsvDefs.COL_POS_TAGS].split("[,]");

            for (int i = 0; i < tags.length; i++) {
                v = normalizeString(tags[i]);
                Integer tid = tagMap.get(v);

                if (tid == null) {
                    val.setErrorMsg(reader.getLineNumber(),
                            user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN_VALUE, "TAGS", row[IndicatorCsvDefs.COL_POS_TAGS]));
                    return val;
                }
                val.addTagId(tid);
            }
        }

        if (row.length > IndicatorCsvDefs.COL_POS_CRITERIA) {
            val.setCriteria(row[IndicatorCsvDefs.COL_POS_CRITERIA]);
        }

        switch (type) {
            case Constants.SURVEY_ANSWER_TYPE_FLOAT:
            case Constants.SURVEY_ANSWER_TYPE_INTEGER:
            case Constants.SURVEY_ANSWER_TYPE_TEXT:
                // check for min and max
                if (row.length < IndicatorCsvDefs.COL_POS_MAX + 1) {
                    val.setErrorMsg(reader.getLineNumber(),
                            user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, "MAX"));
                    return val;
                }

                if (row.length < IndicatorCsvDefs.COL_POS_MIN + 1) {
                    val.setErrorMsg(reader.getLineNumber(),
                            user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, "MIN"));
                    return val;
                }

                double minf, maxf;

                try {
                    logger.debug("Parsing double: " + row[IndicatorCsvDefs.COL_POS_MIN]);
                    minf = Double.parseDouble(row[IndicatorCsvDefs.COL_POS_MIN]);
                    logger.debug("Double value=" + minf);
                } catch (Exception e) {
                    val.setErrorMsg(reader.getLineNumber(),
                            user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN_VALUE, "MIN", row[IndicatorCsvDefs.COL_POS_MIN]));
                    return val;
                }

                try {
                    maxf = Double.parseDouble(row[IndicatorCsvDefs.COL_POS_MAX]);
                } catch (Exception e) {
                    val.setErrorMsg(reader.getLineNumber(),
                            user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN_VALUE, "MAX", row[IndicatorCsvDefs.COL_POS_MAX]));
                    return val;
                }

                if (minf > maxf) {
                    val.setErrorMsg(reader.getLineNumber(),
                            user.message(ControlPanelMessages.INDICATOR_IMPORT__MIN_GREATER_THAN_MAX));
                    return val;
                }

                switch (type) {
                    case Constants.SURVEY_ANSWER_TYPE_INTEGER:
                    case Constants.SURVEY_ANSWER_TYPE_TEXT:                       
                        val.setMinInt((int)minf);
                        val.setMaxInt((int)maxf);
                        break;

                    default:                        
                        val.setMinFloat(minf);
                        val.setMaxFloat(maxf);
                }

                break;

            default:
                // for single or multi choice types
                if (row.length > IndicatorCsvDefs.COL_POS_MIN && !row[IndicatorCsvDefs.COL_POS_MIN].isEmpty()) {
                    val.setErrorMsg(reader.getLineNumber(),
                            user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN_VALUE, "MIN", row[IndicatorCsvDefs.COL_POS_MIN]));
                    return val;
                }

                if (row.length > IndicatorCsvDefs.COL_POS_MAX && !row[IndicatorCsvDefs.COL_POS_MAX].isEmpty()) {
                    val.setErrorMsg(reader.getLineNumber(),
                            user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN_VALUE, "MAX", row[IndicatorCsvDefs.COL_POS_MAX]));
                    return val;
                }
        }

        if (!this.componentMode) {
            if (row.length > IndicatorCsvDefs.COL_POS_CREATOR_ORG && !row[IndicatorCsvDefs.COL_POS_CREATOR_ORG].isEmpty()) {
                v = normalizeString(row[IndicatorCsvDefs.COL_POS_CREATOR_ORG]);
                Integer orgId = orgMap.get(v);

                if (orgId == null) {
                    val.setErrorMsg(reader.getLineNumber(),
                            user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN_VALUE, "CREATOR ORG", row[IndicatorCsvDefs.COL_POS_CREATOR_ORG]));
                    return val;
                }

                val.setCreatorOrgId(orgId);
            }

            boolean hasAccess;
            if (val.getCreatorOrgId() > 0) {
                hasAccess = user.getChecker().hasOrgAuthority(val.getCreatorOrgId());
            } else {
                hasAccess = user.getChecker().hasOrgAuthority(result.getDefaultCreatorOrgId());
            }

            if (!hasAccess) {
                result.addGeneralError(reader.getLineNumber(),
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__NO_ORG_ACCESS));
            }

            if (row.length > IndicatorCsvDefs.COL_POS_VISIBILITY && !row[IndicatorCsvDefs.COL_POS_VISIBILITY].isEmpty()) {
                v = normalizeString(row[IndicatorCsvDefs.COL_POS_VISIBILITY]);
                Integer vis = visMap.get(v);

                if (vis == null) {
                    val.setErrorMsg(reader.getLineNumber(),
                            user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN_VALUE, "VISIBILITY", row[IndicatorCsvDefs.COL_POS_VISIBILITY]));
                    return val;
                }

                val.setVisibility(vis);
            }

            if (row.length > IndicatorCsvDefs.COL_POS_MATURITY_STATE && !row[IndicatorCsvDefs.COL_POS_MATURITY_STATE].isEmpty()) {
                v = normalizeString(row[IndicatorCsvDefs.COL_POS_MATURITY_STATE]);
                Integer state = stateMap.get(v);

                if (state == null) {
                    val.setErrorMsg(reader.getLineNumber(),
                            user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN_VALUE, "MATURITY STATE", row[IndicatorCsvDefs.COL_POS_MATURITY_STATE]));
                    return val;
                }

                // always set to Test library
                val.setState(Constants.RESOURCE_STATE_TEST);
            }

            if (row.length > IndicatorCsvDefs.COL_POS_LANGUAGE && !row[IndicatorCsvDefs.COL_POS_LANGUAGE].isEmpty()) {
                v = normalizeString(row[IndicatorCsvDefs.COL_POS_LANGUAGE]);
                Integer lang = langMap.get(v);

                if (lang == null) {
                    val.setErrorMsg(reader.getLineNumber(),
                            user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN_VALUE, "LANGUAGE", row[IndicatorCsvDefs.COL_POS_LANGUAGE]));
                    return val;
                }
                val.setLangId(lang);
            }
        }

        return val;
    }

    private void processSecondaryLine(String[] row, IndicatorValidation val) {
        IndicatorChoice choice = new IndicatorChoice();

        if (row.length < IndicatorCsvDefs.COL_POS_OPTION + 1) {
            val.setErrorMsg(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, "OPTION"));
            return;
        }

        choice.setLabel(row[IndicatorCsvDefs.COL_POS_OPTION]);

        if (choice.getLabel().length() > ControlPanelConstants.MAX_LENGTH_INDICATOR_CHOICE_LABEL) {
            val.setErrorMsg(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__FIELD_TOO_LONG, "OPTION",
                        ControlPanelConstants.MAX_LENGTH_INDICATOR_CHOICE_LABEL));
            return;
        }

        if (row.length > IndicatorCsvDefs.COL_POS_SCORE && row[IndicatorCsvDefs.COL_POS_SCORE] != null && !row[IndicatorCsvDefs.COL_POS_SCORE].isEmpty()) {
            double score;

            try {
                score = Double.parseDouble(row[IndicatorCsvDefs.COL_POS_SCORE]);
            } catch (Exception e) {
                val.setErrorMsg(reader.getLineNumber(),
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN_VALUE, "SCORE", row[IndicatorCsvDefs.COL_POS_SCORE]));
                return;
            }

            choice.setScore(score);
            choice.setUseScore(true);            
        } else {
            choice.setScore(0);
            choice.setUseScore(false);
        }

        if (row.length > IndicatorCsvDefs.COL_POS_CRITERIA) {
            choice.setCriteria(row[IndicatorCsvDefs.COL_POS_CRITERIA]);
        }

        val.addChoice(choice);
    }

    private boolean checkHeaderLine(String[] row, IndicatorImportValidation result) {
        int colCount = (this.componentMode) ? IndicatorCsvDefs.COL_COUNT - 4 : IndicatorCsvDefs.COL_COUNT;

        if (row.length < colCount) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INCOMPLETE_HEADER));
            return false;
        }

        for (int i = 0; i < colCount; i++) {
            if (row[i].isEmpty()) {
                result.addGeneralError(reader.getLineNumber(),
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_HEADER));
                return false;
            }
        }

        if (!normalizeString(row[IndicatorCsvDefs.COL_POS_NAME]).startsWith(IndicatorCsvDefs.COL_LABEL_NAME)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, "NAME", row[IndicatorCsvDefs.COL_POS_NAME]));
            return false;
        } else if (!normalizeString(row[IndicatorCsvDefs.COL_POS_QUESTION]).startsWith(IndicatorCsvDefs.COL_LABEL_QUESTION)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, "QUESTION", row[IndicatorCsvDefs.COL_POS_QUESTION]));
            return false;
        } else if (!normalizeString(row[IndicatorCsvDefs.COL_POS_TYPE]).startsWith(IndicatorCsvDefs.COL_LABEL_TYPE)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, "TYPE", row[IndicatorCsvDefs.COL_POS_TYPE]));
            return false;
        } else if (!normalizeString(row[IndicatorCsvDefs.COL_POS_REFERENCE]).startsWith(IndicatorCsvDefs.COL_LABEL_REFERENCE)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, "REFERENCE", row[IndicatorCsvDefs.COL_POS_REFERENCE]));
            return false;
       } else if (!normalizeString(row[IndicatorCsvDefs.COL_POS_HINT]).startsWith(IndicatorCsvDefs.COL_LABEL_HINT)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, "HINT", row[IndicatorCsvDefs.COL_POS_HINT]));
            return false;
       } else if (!normalizeString(row[IndicatorCsvDefs.COL_POS_MIN]).startsWith(IndicatorCsvDefs.COL_LABEL_MIN)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, "MIN", row[IndicatorCsvDefs.COL_POS_MIN]));
            return false;
        } else if (!normalizeString(row[IndicatorCsvDefs.COL_POS_MAX]).startsWith(IndicatorCsvDefs.COL_LABEL_MAX)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, "MAX", row[IndicatorCsvDefs.COL_POS_MAX]));
            return false;
        } else if (!normalizeString(row[IndicatorCsvDefs.COL_POS_SCORE]).startsWith(IndicatorCsvDefs.COL_LABEL_SCORE)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, "SCORE", row[IndicatorCsvDefs.COL_POS_SCORE]));
            return false;
        } else if (!normalizeString(row[IndicatorCsvDefs.COL_POS_CRITERIA]).startsWith(IndicatorCsvDefs.COL_LABEL_CRITERIA)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, "CRITERIA", row[IndicatorCsvDefs.COL_POS_CRITERIA]));
            return false;
        } else if (!normalizeString(row[IndicatorCsvDefs.COL_POS_OPTION]).startsWith(IndicatorCsvDefs.COL_LABEL_OPTION)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, "OPTION", row[IndicatorCsvDefs.COL_POS_OPTION]));
            return false;
        } else if (!normalizeString(row[IndicatorCsvDefs.COL_POS_TAGS]).startsWith(IndicatorCsvDefs.COL_LABEL_TAGS)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, "TAGS", row[IndicatorCsvDefs.COL_POS_TAGS]));
            return false;
       } else if (!componentMode && !normalizeString(row[IndicatorCsvDefs.COL_POS_CREATOR_ORG]).startsWith(IndicatorCsvDefs.COL_LABEL_CREATOR_HEADER)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, "CREATOR ORG", row[IndicatorCsvDefs.COL_POS_CREATOR_ORG]));
            return false;
        } else if (!componentMode && !normalizeString(row[IndicatorCsvDefs.COL_POS_VISIBILITY]).startsWith(IndicatorCsvDefs.COL_LABEL_VISIBILITY_HEADER)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, "VISIBILITY", row[IndicatorCsvDefs.COL_POS_VISIBILITY]));
           return false;
        } else if (!componentMode && !normalizeString(row[IndicatorCsvDefs.COL_POS_MATURITY_STATE]).startsWith(IndicatorCsvDefs.COL_LABEL_STATE)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, "MATURITY STATE", row[IndicatorCsvDefs.COL_POS_MATURITY_STATE]));
            return false;
        } else if (!componentMode && !normalizeString(row[IndicatorCsvDefs.COL_POS_LANGUAGE]).startsWith(IndicatorCsvDefs.COL_LABEL_LANGUAGE_HEADER)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, "LANGUAGE", row[IndicatorCsvDefs.COL_POS_LANGUAGE]));
            return false;
        } else {
            return true;
        }
    }

    public void close() {
        if (reader != null) reader.close();
    }

}
