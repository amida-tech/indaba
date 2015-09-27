/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.AnswerObjectChoice;
import com.ocs.indaba.po.AnswerObjectFloat;
import com.ocs.indaba.po.AnswerObjectInteger;
import com.ocs.indaba.po.AnswerObjectText;
import com.ocs.indaba.vo.SurveyAnswerObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import static java.sql.Types.INTEGER;
/**
 *
 * @author luwb
 */
public class AnswerObjectDAO extends BaseDAO{
    private static final Logger logger = Logger.getLogger(AnswerObjectDAO.class);

    private static final String INSERT_ANSWER_OBJECT_CHOICE =
            "INSERT INTO answer_object_choice(choices) values (?)";

    private static final String INSERT_ANSWER_OBJECT_FLOAT =
            "INSERT INTO answer_object_float(value) values (?)";

    private static final String INSERT_ANSWER_OBJECT_INTEGER =
            "INSERT INTO answer_object_integer(value) values (?)";

    private static final String INSERT_ANSWER_OBJECT_TEXT =
            "INSERT INTO answer_object_text(value) values (?)";

     private static final String SELECT_ANSWER_OBJECT_CHOICE_BY_ID =
            "SELECT * from answer_object_choice where id=?";

    private static final String SELECT_ANSWER_OBJECT_FLOAT_BY_ID =
            "SELECT * from answer_object_float where id=?";

    private static final String SELECT_ANSWER_OBJECT_INTEGER_BY_ID =
            "SELECT * from answer_object_integer where id=?";

    private static final String SELECT_ANSWER_OBJECT_TEXT_BY_ID =
            "SELECT * from answer_object_text where id=?";


    private static final String SELECT_LAST_INSERT_ID =
            "SELECT LAST_INSERT_ID()";

    

    public int InsertAnswerObjectChoice(long choices){
        logger.debug("Insert answer_object_choice values" + choices);

        Object [] values = new Object[] { choices};
    	this.getJdbcTemplate().update(this.INSERT_ANSWER_OBJECT_CHOICE, values);
        return getLastInsertId();
    }

    public int InsertAnswerObjectFloat(float value){
        logger.debug("Insert answer_object_float values" + value);

        Object [] values = new Object[] { value};
    	this.getJdbcTemplate().update(this.INSERT_ANSWER_OBJECT_FLOAT, values);
        return getLastInsertId();
    }

    public int InsertAnswerObjectInteger(int value){
        logger.debug("Insert answer_object_integer values" + value);

        Object [] values = new Object[] { value};
    	this.getJdbcTemplate().update(this.INSERT_ANSWER_OBJECT_INTEGER, values);
        return getLastInsertId();
    }

    public int InsertAnswerObjectText(String value){
        logger.debug("Insert answer_object_text values" + value);

        Object [] values = new Object[] { value};
    	this.getJdbcTemplate().update(this.INSERT_ANSWER_OBJECT_TEXT, values);
        return getLastInsertId();
    }

    public AnswerObjectChoice getAnswerObjectChoice(int answerObjectId){
        logger.debug("Select answer_object_choice where id=" + answerObjectId);

        RowMapper mapper = new RowMapper() {
            public AnswerObjectChoice mapRow(ResultSet rs, int rowNum) throws SQLException {
               AnswerObjectChoice choice = new AnswerObjectChoice();
               choice.setId(rs.getInt("id"));
               choice.setChoices(rs.getLong("choices"));
               return choice;
            }
        };

        List<AnswerObjectChoice> list = getJdbcTemplate().query(this.INSERT_ANSWER_OBJECT_CHOICE,
                new Object[]{answerObjectId},
                new int[]{INTEGER},
                mapper);

        return list.get(0);
    }

    public AnswerObjectFloat getAnswerObjectFloat(int answerObjectId){
        logger.debug("Select answer_object_float where id=" + answerObjectId);

        RowMapper mapper = new RowMapper() {
            public AnswerObjectFloat mapRow(ResultSet rs, int rowNum) throws SQLException {
               AnswerObjectFloat aFloat = new AnswerObjectFloat();
               aFloat.setId(rs.getInt("id"));
               aFloat.setValue(rs.getFloat("value"));
               return aFloat;
            }
        };

        List<AnswerObjectFloat> list = getJdbcTemplate().query(this.SELECT_ANSWER_OBJECT_FLOAT_BY_ID,
                new Object[]{answerObjectId},
                new int[]{INTEGER},
                mapper);

        return list.get(0);
    }

    public AnswerObjectInteger getAnswerObjectInteger(int answerObjectId){
        logger.debug("Select answer_object_integer where id=" + answerObjectId);

        RowMapper mapper = new RowMapper() {
            public AnswerObjectInteger mapRow(ResultSet rs, int rowNum) throws SQLException {
               AnswerObjectInteger aInt = new AnswerObjectInteger();
               aInt.setId(rs.getInt("id"));
               aInt.setValue(rs.getInt("value"));
               return aInt;
            }
        };

        List<AnswerObjectInteger> list = getJdbcTemplate().query(this.SELECT_ANSWER_OBJECT_INTEGER_BY_ID,
                new Object[]{answerObjectId},
                new int[]{INTEGER},
                mapper);

        return list.get(0);
    }

    public AnswerObjectText getAnswerObjectText(int answerObjectId){
        logger.debug("Select answer_object_text where id=" + answerObjectId);

        RowMapper mapper = new RowMapper() {
            public AnswerObjectText mapRow(ResultSet rs, int rowNum) throws SQLException {
               AnswerObjectText text = new AnswerObjectText();
               text.setId(rs.getInt("id"));
               text.setValue(rs.getString("value"));
               return text;
            }
        };

        List<AnswerObjectText> list = getJdbcTemplate().query(this.SELECT_ANSWER_OBJECT_TEXT_BY_ID,
                new Object[]{answerObjectId},
                new int[]{INTEGER},
                mapper);

        return list.get(0);
    }

    private int getLastInsertId(){
        logger.debug("Select last insert id");

        RowMapper mapper = new RowMapper() {
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
               return rs.getInt(1);
            }
        };

        List<Integer> list = getJdbcTemplate().query(this.SELECT_LAST_INSERT_ID, mapper);

        return list.get(0);
    }


    private static final String SELECT_COMPONENT_ANSWER_OBJECTS =
             "SELECT si.id indid, si.answer_type, aoi.value vi, aoi.id aoiid, " +
             "aof.value vf, aof.id aofid, aoc.choices vc, aoc.id aocid, aot.value vt, aot.id aotid " +
             "FROM survey_content_object sco " +
             "JOIN horse h ON sco.content_header_id=h.content_header_id " +
             "JOIN survey_answer sa ON sa.survey_content_object_id=sco.id " +
             "JOIN survey_answer_component sac ON sac.survey_answer_id=sa.id " +
             "JOIN survey_indicator si ON si.id=sac.component_indicator_id " +
             "LEFT JOIN answer_object_integer aoi ON (aoi.id=sac.answer_object_id AND si.answer_type=3) " +
             "LEFT JOIN answer_object_float aof ON (aof.id=sac.answer_object_id AND si.answer_type=4) " +
             "LEFT JOIN answer_object_text aot ON (aot.id=sac.answer_object_id AND si.answer_type=5) " +
             "LEFT JOIN answer_object_choice aoc ON (aoc.id=sac.answer_object_id AND (si.answer_type=1 OR si.answer_type=2)) " +
             "WHERE h.id=? AND sa.survey_question_id=?";

    public List<SurveyAnswerObject> selectComponentAnswerObjects(int horseId, int mainQuestionId) {
        RowMapper mapper = new SurveyAnswerObjectRowMapper();
        List<SurveyAnswerObject> list = getJdbcTemplate().query(
                AnswerObjectDAO.SELECT_COMPONENT_ANSWER_OBJECTS,
                new Object[]{horseId, mainQuestionId},
                new int[]{INTEGER, INTEGER},
                mapper);

        return list;
    }


    private static final String SELECT_VERSION_COMPONENT_ANSWER_OBJECTS =
             "SELECT si.id indid, si.answer_type, aoi.value vi, aoi.id aoiid, " +
             "aof.value vf, aof.id aofid, aoc.choices vc, aoc.id aocid, aot.value vt, aot.id aotid " +
             "FROM survey_answer_version sav " +
             "JOIN survey_answer_component_version sacv ON sacv.survey_answer_version_id=sav.id " +
             "JOIN survey_indicator si ON si.id=sacv.component_indicator_id " +
             "LEFT JOIN answer_object_integer aoi ON (aoi.id=sacv.answer_object_id AND si.answer_type=3) " +
             "LEFT JOIN answer_object_float aof ON (aof.id=sacv.answer_object_id AND si.answer_type=4) " +
             "LEFT JOIN answer_object_text aot ON (aot.id=sacv.answer_object_id AND si.answer_type=5) " +
             "LEFT JOIN answer_object_choice aoc ON (aoc.id=sacv.answer_object_id AND (si.answer_type=1 OR si.answer_type=2)) " +
             "WHERE sav.content_version_id=? AND sav.survey_question_id=?";

    public List<SurveyAnswerObject> selectContentVersionComponentAnswerObjects(int contentVersionId, int mainQuestionId) {
        RowMapper mapper = new SurveyAnswerObjectRowMapper();

        List<SurveyAnswerObject> list = getJdbcTemplate().query(
                AnswerObjectDAO.SELECT_VERSION_COMPONENT_ANSWER_OBJECTS,
                new Object[]{contentVersionId, mainQuestionId},
                new int[]{INTEGER, INTEGER},
                mapper);

        return list;
    }


    private static final String SELECT_SPR_COMPONENT_OBJECTS =
             "SELECT si.id indid, si.answer_type, aoi.value vi, aoi.id aoiid, " +
             "aof.value vf, aof.id aofid, aoc.choices vc, aoc.id aocid, aot.value vt, aot.id aotid " +
             "FROM spr_component sprc " +
             "JOIN survey_indicator si ON si.id=sprc.component_indicator_id " +
             "LEFT JOIN answer_object_integer aoi ON (aoi.id=sprc.answer_object_id AND si.answer_type=3) " +
             "LEFT JOIN answer_object_float aof ON (aof.id=sprc.answer_object_id AND si.answer_type=4) " +
             "LEFT JOIN answer_object_text aot ON (aot.id=sprc.answer_object_id AND si.answer_type=5) " +
             "LEFT JOIN answer_object_choice aoc ON (aoc.id=sprc.answer_object_id AND (si.answer_type=1 OR si.answer_type=2)) " +
             "WHERE sprc.survey_peer_review_id=?";


    public List<SurveyAnswerObject> selectPeerReviewComponentAnswerObjects(int surveyPeerReviewId) {
        RowMapper mapper = new SurveyAnswerObjectRowMapper();

        List<SurveyAnswerObject> list = getJdbcTemplate().query(
                AnswerObjectDAO.SELECT_SPR_COMPONENT_OBJECTS,
                new Object[]{surveyPeerReviewId},
                new int[]{INTEGER},
                mapper);

        return list;
     }


     private static final String SELECT_SPR_VERSION_COMPONENT_OBJECTS =
             "SELECT si.id indid, si.answer_type, aoi.value vi, aoi.id aoiid, " +
             "aof.value vf, aof.id aofid, aoc.choices vc, aoc.id aocid, aot.value vt, aot.id aotid " +
             "FROM spr_component_version sprcv " +
             "JOIN survey_indicator si ON si.id=sprcv.component_indicator_id " +
             "LEFT JOIN answer_object_integer aoi ON (aoi.id=sprcv.answer_object_id AND si.answer_type=3) " +
             "LEFT JOIN answer_object_float aof ON (aof.id=sprcv.answer_object_id AND si.answer_type=4) " +
             "LEFT JOIN answer_object_text aot ON (aot.id=sprcv.answer_object_id AND si.answer_type=5) " +
             "LEFT JOIN answer_object_choice aoc ON (aoc.id=sprcv.answer_object_id AND (si.answer_type=1 OR si.answer_type=2)) " +
             "WHERE sprcv.survey_peer_review_version_id=?";

    public List<SurveyAnswerObject> selectPeerReviewVersionComponentAnswerObjects(int surveyPeerReviewVersionId) {
        RowMapper mapper = new SurveyAnswerObjectRowMapper();

        List<SurveyAnswerObject> list = getJdbcTemplate().query(
                AnswerObjectDAO.SELECT_SPR_VERSION_COMPONENT_OBJECTS,
                new Object[]{surveyPeerReviewVersionId},
                new int[]{INTEGER},
                mapper);

        return list;
     }


    static private class SurveyAnswerObjectRowMapper implements RowMapper {
        
        public SurveyAnswerObject mapRow(ResultSet rs, int rowNum) throws SQLException {
            SurveyAnswerObject sao = new SurveyAnswerObject();
            short answerType = rs.getShort("answer_type");
            sao.setAnswerType(answerType);
            Object obj = null;
            int id = 0;

            switch (answerType) {
                case Constants.SURVEY_ANSWER_TYPE_INTEGER:
                    obj = (Integer) rs.getInt("vi");
                    id = rs.getInt("aoiid");
                    break;
                case Constants.SURVEY_ANSWER_TYPE_FLOAT:
                    obj = (Float) rs.getFloat("vf");
                    id = rs.getInt("aofid");
                    break;
                case Constants.SURVEY_ANSWER_TYPE_TEXT:
                    obj = rs.getString("vt");
                    id = rs.getInt("aotid");
                    break;
                case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
                case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
                    obj = (Long) rs.getLong("vc");
                    id = rs.getInt("aocid");
                    break;
            }

            sao.setValue(obj);
            sao.setId(id);
            sao.setIndicatorId(id = rs.getInt("indid"));
            return sao;
        }
    }

}
