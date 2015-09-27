/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.AtcChoice;
import com.ocs.util.ResultSetUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;


/**
 *
 * @author luwb
 */
public class AtcChoiceDAO extends SmartDaoMySqlImpl<AtcChoice, Integer> {

    private static final Logger logger = Logger.getLogger(AtcChoiceDAO.class);
    private static final String SELECT_ATC_CHOICE_BY_ANSWER_TYPE_CHOICE_ID =
            "SELECT * from atc_choice where answer_type_choice_id=? order by weight";
    private static final String GET_BY_ANSWER_TYPE_CHOICE_ID = "select * from atc_choice where answer_type_choice_id = ?";
    private static final String DELETE_BY_ANSWER_TYPE_CHOICE_ID = "DELETE FROM atc_choice WHERE answer_type_choice_id = ?";

    public List<AtcChoice> getAtcChoicesbyId(int answerTypeChoiceId) {
        return this.find(GET_BY_ANSWER_TYPE_CHOICE_ID, answerTypeChoiceId);
    }

    public void deleteByAnswerTypeId(int answerTypeChoiceId) {
        super.delete(DELETE_BY_ANSWER_TYPE_CHOICE_ID, answerTypeChoiceId);
    }

    public List<AtcChoice> getAtcChoicesByAnswerTypeChoiceId(int answerTypeChoiceId) {
        return super.find(SELECT_ATC_CHOICE_BY_ANSWER_TYPE_CHOICE_ID, answerTypeChoiceId);
        // return super.find(SELECT_ATC_CHOICE_BY_ANSWER_TYPE_CHOICE_ID, answerTypeChoiceId);
        /*
        RowMapper mapper = new RowMapper() {
        public AtcChoice mapRow(ResultSet rs, int rowNum) throws SQLException {
        AtcChoice choice = new AtcChoice();
        choice.setId(rs.getInt("id"));
        choice.setAnswerTypeChoiceId(rs.getInt("answer_type_choice_id"));
        choice.setLabel(rs.getString("label"));
        //choice.setScore(rs.getFloat("score"));
        choice.setScore(rs.getInt("score"));
        choice.setCriteria(rs.getString("criteria"));
        choice.setWeight(rs.getInt("weight"));
        choice.setMask(rs.getLong("mask"));
        choice.setDefaultSelected(rs.getBoolean("default_selected"));
        return choice;
        }
        };

        return getJdbcTemplate().query(this.SELECT_ATC_CHOICE_BY_ANSWER_TYPE_CHOICE_ID,
        new Object[]{answerTypeChoiceId},
        new int[]{INTEGER},
        mapper);
         */
    }
    
    
    private static final String SELECT_ATC_CHOICES_OF_SURVEY_CONFIG =
            "SELECT DISTINCT ac.* " +
            "FROM atc_choice ac, survey_question sq, survey_indicator si " +
            "WHERE sq.survey_config_id=? AND si.id = sq.survey_indicator_id AND (si.answer_type=1 OR si.answer_type=2) " +
            "AND ac.answer_type_choice_id = si.answer_type_id";

    public List<AtcChoice> getAtcChoicesOfSurveyConfig(int surveyConfigId) {
        return super.find(SELECT_ATC_CHOICES_OF_SURVEY_CONFIG, surveyConfigId);
    }


    private static final String SELECT_CHOICES_BY_TYPE_ID_AND_LANGUAGE =
            "SELECT ac.*, aci.label label_intl, aci.criteria criteria_intl " +
            "FROM atc_choice ac " +
            "JOIN answer_type_choice atc ON ac.answer_type_choice_id=atc.id " +
            "LEFT JOIN atc_choice_intl aci ON aci.atc_choice_id=ac.id AND aci.language_id=? " +
            "WHERE atc.id=? " +
            "ORDER BY ac.weight ASC";

    public List<AtcChoice> getChoices(int answerTypeId, int languageId) {

        RowMapper mapper = new RowMapper() {
            public AtcChoice mapRow(ResultSet rs, int rowNum) throws SQLException {
               AtcChoice ac = new AtcChoice();
               ac.setId(ResultSetUtil.getInt(rs, "id"));
               ac.setAnswerTypeChoiceId(ResultSetUtil.getInt(rs, "answer_type_choice_id"));
               ac.setLabel(ResultSetUtil.getString(rs, "label"));
               ac.setScore(ResultSetUtil.getInt(rs, "score"));
               ac.setCriteria(ResultSetUtil.getString(rs, "criteria"));
               ac.setWeight(ResultSetUtil.getInt(rs, "weight"));
               ac.setMask(ResultSetUtil.getLong(rs, "mask"));
               ac.setDefaultSelected(ResultSetUtil.getBoolean(rs, "default_selected"));
               ac.setUseScore(ResultSetUtil.getBoolean(rs, "use_score"));

               String intlLabel = ResultSetUtil.getString(rs, "label_intl");
               if (intlLabel != null) ac.setLabel(intlLabel);

               String intlCriteria = ResultSetUtil.getString(rs, "criteria_intl");
               if (intlCriteria != null) ac.setCriteria(intlCriteria);

               return ac;
            }
        };

        return super.getJdbcTemplate().query(SELECT_CHOICES_BY_TYPE_ID_AND_LANGUAGE,
                mapper, languageId, answerTypeId);
    }

}
