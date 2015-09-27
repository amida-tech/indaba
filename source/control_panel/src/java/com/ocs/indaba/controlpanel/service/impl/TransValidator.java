/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.service.impl;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.model.IndicatorTransValidation;
import com.ocs.indaba.controlpanel.model.TransValidation;
import com.ocs.indaba.controlpanel.model.Utilities;
import com.ocs.indaba.controlpanel.model.IndicatorChoiceTrans;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.dao.AtcChoiceDAO;
import com.ocs.indaba.dao.SurveyIndicatorDAO;
import com.ocs.indaba.po.AtcChoice;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.ssu.NotUTF8EncodedException;
import com.ocs.ssu.ReaderFactory;
import com.ocs.ssu.SpreadsheetReader;
import com.ocs.ssu.UnsupportedFileTypeException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author seanpcheng
 */
public class TransValidator {
    
    private SpreadsheetReader reader;
    private File file;
    private LoginUser user;
    
    private Map<String, Integer> langMap = null;
    
    private static SurveyIndicatorDAO indicatorDao = null;
    private static AtcChoiceDAO atcChoiceDao = null;
    
    
    @Autowired
    public void setAtcChoiceDAO(AtcChoiceDAO dao) {
        atcChoiceDao = dao;
    }
    
    
    @Autowired
    public void setSurveyIndicatorDAO(SurveyIndicatorDAO dao) {
        indicatorDao = dao;
    }
    
    // This dummy constructor is needed for autowired to work
    public TransValidator() {
        
    }
    
    
    public TransValidator(LoginUser user, File file) {
        this.file = file;
        this.user = user;
    }
    
    public TransValidator(LoginUser user, String file) {
        this.file = new File(file);
        this.user = user;
    }
    
    
    public IndicatorTransValidation validate() throws FileNotFoundException, IOException {
        langMap = Utilities.buildLanguageMapN2I();

        // check for UTF-8 encoding
        IndicatorTransValidation result = new IndicatorTransValidation();

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
               
        row = reader.readNext();
        
        // Look for header line
        if (!checkHeaderLine(row, result)) {
            return result;
        }
        
        boolean readNextLine;
        row = reader.readNext();

        while (row != null) {

            readNextLine = true;

            if (!row[0].isEmpty()) {

                TransValidation val = processFullLine(row, result);

                if (val.getErrorMsg() == null) {
                    // good
                    // process secondary lines
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
                }
                                
                result.addTransValidation(val);
                validateIndicatorTrans(val);
            }

            if (readNextLine) {
                row = reader.readNext();
            }
        }       
        
      return result; 
    }


    private void validateIndicatorTrans(TransValidation tv) {
        if (tv.getErrorMsg() != null) return;

        if (tv.getQuestion() == null || tv.getQuestion().isEmpty()) {
            tv.setErrorMsg(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, "QUESTION TEXT"));
            return;
        }

        if (tv.getLangId() <= 0) {
            tv.setErrorMsg(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, "LANGUAGE"));
            return;
        }

        // validate atc choices
        if (tv.getType() == Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE || tv.getType() == Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE) {
            List<AtcChoice> choices = atcChoiceDao.getAtcChoicesByAnswerTypeChoiceId(tv.getAnswerTypeId());

            // make sure specified choices exist
            if (tv.getChoices() != null) {
                for (IndicatorChoiceTrans ict : tv.getChoices()) {
                    boolean found = false;
                    for (AtcChoice ac : choices) {
                        if (ict.getAtcChoiceId() == ac.getId()) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        tv.setErrorMsg(reader.getLineNumber(),
                                user.message(ControlPanelMessages.INDICATOR_TRANS__WRONG_CHOICE_KEY, ict.getAtcChoiceId()));
                        return;
                    }

                    if (ict.getLabel() == null || ict.getLabel().isEmpty()) {
                        tv.setErrorMsg(reader.getLineNumber(),
                                user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, "OPTION LABEL"));
                        return;
                    }
                }
            }
        }
    }


    
    private boolean checkHeaderLine(String[] row, IndicatorTransValidation result) {
        if (row.length < IndicatorCsvDefs.TRANS_COL_COUNT) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INCOMPLETE_HEADER));
            return false;
        }

        for (int i = 0; i < row.length; i++) {
            if (row[i].isEmpty()) {
                result.addGeneralError(reader.getLineNumber(),
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_HEADER));
                return false;
            }
        }

        if (!Utilities.similar(row[IndicatorCsvDefs.TRANS_COL_POS_NAME], IndicatorCsvDefs.COL_LABEL_NAME)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, "NAME", row[IndicatorCsvDefs.TRANS_COL_POS_NAME]));
            return false;
        } else if (!Utilities.similar(row[IndicatorCsvDefs.TRANS_COL_POS_ITEM_TYPE], IndicatorCsvDefs.COL_LABEL_ITEM_TYPE)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, "ITEM TYPE", row[IndicatorCsvDefs.TRANS_COL_POS_ITEM_TYPE]));
            return false;
        } else if (!Utilities.similar(row[IndicatorCsvDefs.TRANS_COL_POS_ITEM_KEY], IndicatorCsvDefs.COL_LABEL_ITEM_KEY)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, "ITEM KEY", row[IndicatorCsvDefs.TRANS_COL_POS_ITEM_KEY]));
            return false;
        } else if (!Utilities.similar(row[IndicatorCsvDefs.TRANS_COL_POS_ITEM_TEXT], IndicatorCsvDefs.COL_LABEL_ITEM_TEXT)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, "ITEM TEXT", row[IndicatorCsvDefs.TRANS_COL_POS_ITEM_TEXT]));
            return false;
        } else if (!Utilities.similar(row[IndicatorCsvDefs.TRANS_COL_POS_LANGUAGE], IndicatorCsvDefs.COL_LABEL_LANGUAGE)) {
            result.addGeneralError(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN, "LANGUAGE", row[IndicatorCsvDefs.TRANS_COL_POS_LANGUAGE]));
            return false;
        } else {
            return true;
        }
    }

    private TransValidation processFullLine(String[] row, IndicatorTransValidation result) {
        TransValidation trans = new TransValidation(result);
        
        if (row.length < IndicatorCsvDefs.TRANS_COL_POS_NAME + 1 || row[IndicatorCsvDefs.TRANS_COL_POS_NAME].isEmpty()) {
            trans.setErrorMsg(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, "NAME"));
            return trans;
        }
        trans.setName(row[IndicatorCsvDefs.TRANS_COL_POS_NAME]);

        SurveyIndicator indicator = indicatorDao.selectSurveyIndicatorByName(row[IndicatorCsvDefs.TRANS_COL_POS_NAME]);
        if (indicator == null) {
            trans.setErrorMsg(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN_VALUE, "NAME", row[IndicatorCsvDefs.TRANS_COL_POS_NAME]));
            return trans;
        }        

        trans.setIndicatorId(indicator.getId());
        trans.setType(indicator.getAnswerType());
        trans.setAnswerTypeId(indicator.getAnswerTypeId());
        trans.setIndicatorLangId(indicator.getLanguageId());
                
        if (!user.getChecker().hasOrgAuthority(indicator.getCreatorOrgId())) {
            trans.setErrorMsg(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__NO_ORG_ACCESS));
            return trans;
        }
        
        processSecondaryLine(row, trans);

        return trans;
    }


    private IndicatorChoiceTrans getChoiceTrans(TransValidation trans, int atcChoiceId) {
        if (trans.getChoices() != null) {
            for (IndicatorChoiceTrans ict : trans.getChoices()) {
                if (ict.getAtcChoiceId() == atcChoiceId) {
                    return ict;
                }
            }
        }

        IndicatorChoiceTrans ict = new IndicatorChoiceTrans();
        ict.setAtcChoiceId(atcChoiceId);
        trans.addChoice(ict);
        return ict;
    }

    private void processSecondaryLine(String[] row, TransValidation trans) {
                    
        if (row.length < IndicatorCsvDefs.TRANS_COL_POS_ITEM_TYPE+1) {
            trans.setErrorMsg(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, "ITEM TYPE"));
            return;
        }

        if (row.length < IndicatorCsvDefs.TRANS_COL_POS_ITEM_TEXT+1) {
            trans.setErrorMsg(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, "ITEM TEXT"));
            return;
        }

        if (row.length < IndicatorCsvDefs.TRANS_COL_POS_ITEM_KEY+1) {
            trans.setErrorMsg(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, "ITEM KEY"));
            return;
        }

        if (row.length < IndicatorCsvDefs.TRANS_COL_POS_LANGUAGE+1) {
            trans.setErrorMsg(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, "LANGUAGE"));
            return;
        }

        String itemType = row[IndicatorCsvDefs.TRANS_COL_POS_ITEM_TYPE];
        String itemText = row[IndicatorCsvDefs.TRANS_COL_POS_ITEM_TEXT];
        String itemKey = row[IndicatorCsvDefs.TRANS_COL_POS_ITEM_KEY];
        String itemLang = row[IndicatorCsvDefs.TRANS_COL_POS_LANGUAGE];


        int itemKeyId = 0;
        try {
            itemKeyId = Integer.parseInt(itemKey);
        } catch (Exception e) {
            trans.setErrorMsg(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN_VALUE, "ITEM KEY", itemKey));
            return;
        }

        if (itemLang != null && !itemLang.isEmpty()) {
            Integer langId = langMap.get(Utilities.normalizeString(itemLang));
            if (langId == null) {
                trans.setErrorMsg(reader.getLineNumber(),
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN_VALUE, "LANGUAGE", itemLang));
                return;
            }

            if (langId == trans.getIndicatorLangId()) {
                trans.setErrorMsg(reader.getLineNumber(),
                        user.message(ControlPanelMessages.INDICATOR_TRANS__LANG_INDICATOR_ALREADY_EXISTS, itemLang));
                return;
            }

            if (trans.getLangId() > 0) {
                if (langId != trans.getLangId()) {
                    trans.setErrorMsg(reader.getLineNumber(),
                            user.message(ControlPanelMessages.INDICATOR_TRANS__INCONSISTENT_LANG, itemLang));
                    return;
                }
            } else {
                trans.setLangId(langId);
            }
        }

        if (Utilities.similar(itemType, IndicatorCsvDefs.TRANS_ITEM_TYPE_IQ)) {
            if (itemKeyId != trans.getIndicatorId()) {
                trans.setErrorMsg(reader.getLineNumber(),
                        user.message(ControlPanelMessages.INDICATOR_TRANS__WRONG_INDICATOR_KEY, itemKey));
                return;
            }
            trans.setQuestion(itemText);
        } else if (Utilities.similar(itemType, IndicatorCsvDefs.TRANS_ITEM_TYPE_IH)) {
            if (itemKeyId != trans.getIndicatorId()) {
                trans.setErrorMsg(reader.getLineNumber(),
                        user.message(ControlPanelMessages.INDICATOR_TRANS__WRONG_INDICATOR_KEY, itemKey));
                return;
            }
            trans.setTip(itemText);
        } else if (Utilities.similar(itemType, IndicatorCsvDefs.TRANS_ITEM_TYPE_OL)) {
            IndicatorChoiceTrans ict = getChoiceTrans(trans, itemKeyId);
            ict.setLabel(itemText);
        } else if (Utilities.similar(itemType, IndicatorCsvDefs.TRANS_ITEM_TYPE_OC)) {
            IndicatorChoiceTrans ict = getChoiceTrans(trans, itemKeyId);
            ict.setCriteria(itemText);
        } else {
            trans.setErrorMsg(reader.getLineNumber(),
                    user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN_VALUE, "ITEM TYPE", itemType));
            return;
        }
    }
       
}
