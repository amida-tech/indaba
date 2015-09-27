/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.AtcChoiceIntl;
import java.util.List;

/**
 *
 * @author seanpcheng
 */
public class AtcChoiceIntlDAO extends SmartDaoMySqlImpl<AtcChoiceIntl, Integer>{
    
    private static final String SELECT_BY_CHOICE_ID_AND_LANG_ID =
            "SELECT * FROM atc_choice_intl WHERE atc_choice_id=? AND language_id=?";
    
    
    public AtcChoiceIntl findByChoiceIdAndLanguage(int atcChoiceId, int languageId) {
        return super.findSingle(SELECT_BY_CHOICE_ID_AND_LANG_ID, atcChoiceId, languageId);
    }

    private static final String SELECT_BY_TYPE_ID_AND_LANG_ID =
            "SELECT aci.* FROM atc_choice_intl aci, atc_choice ac " +
            "WHERE ac.answer_type_choice_id=? AND aci.atc_choice_id=ac.id AND aci.language_id=?";


    public List<AtcChoiceIntl> selectByTypeIdAndLanguage(int typeId, int languageId) {
        return super.find(SELECT_BY_TYPE_ID_AND_LANG_ID, (long)typeId, languageId);
    }


    private static final String SELECT_BY_CONFIG_ID_AND_LANG_ID =
            "SELECT aci.* FROM atc_choice_intl aci, atc_choice ac, survey_indicator si, survey_question sq " +
            "WHERE aci.atc_choice_id=ac.id AND sq.survey_config_id=? AND aci.language_id=? AND si.id = sq.survey_indicator_id AND (si.answer_type=1 OR si.answer_type=2) " +
            "AND ac.answer_type_choice_id = si.answer_type_id";

    public List<AtcChoiceIntl> selectByConfigIdAndLanguage(int surveyConfigId, int languageId) {
        return super.find(SELECT_BY_CONFIG_ID_AND_LANG_ID, (long)surveyConfigId, languageId);
    }
}
