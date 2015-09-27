/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.indaba.po.AnswerTypeChoice;
import com.ocs.indaba.po.AnswerTypeFloat;
import com.ocs.indaba.po.AnswerTypeInteger;
import com.ocs.indaba.po.AnswerTypeText;
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
public class AnswerTypeDAO extends BaseDAO{
    private static final Logger logger = Logger.getLogger(AnswerTypeDAO.class);

    private static final String SELECT_ANSWER_TYPE_CHOICE_BY_ID =
            "SELECT * from answer_type_choice where id=?";

    private static final String SELECT_ANSWER_TYPE_FLOAT_BY_ID =
            "SELECT * from answer_type_float where id=?";

    private static final String SELECT_ANSWER_TYPE_INTEGER_BY_ID =
            "SELECT * from answer_type_integer where id=?";

    private static final String SELECT_ANSWER_TYPE_TEXT_BY_ID =
            "SELECT * from answer_type_text where id=?";

    public AnswerTypeChoice getAnswerTypeChoice(int answerTypeId){
        logger.debug("Select answer_type_choice where id=" + answerTypeId);

        RowMapper mapper = new RowMapper() {
            public AnswerTypeChoice mapRow(ResultSet rs, int rowNum) throws SQLException {
               AnswerTypeChoice choice = new AnswerTypeChoice();
               choice.setId(rs.getInt("id"));

               return choice;
            }
        };

        List<AnswerTypeChoice> list = getJdbcTemplate().query(SELECT_ANSWER_TYPE_CHOICE_BY_ID,
                new Object[]{answerTypeId},
                new int[]{INTEGER},
                mapper);

        return list.get(0);
    }

    public AnswerTypeFloat getAnswerTypeFloat(int answerTypeId){
        logger.debug("Select answer_type_float where id=" + answerTypeId);

        RowMapper mapper = new RowMapper() {
            public AnswerTypeFloat mapRow(ResultSet rs, int rowNum) throws SQLException {
               AnswerTypeFloat aFloat = new AnswerTypeFloat();
               aFloat.setId(rs.getInt("id"));
               aFloat.setMinValue(rs.getFloat("min_value"));
               aFloat.setMaxValue(rs.getFloat("max_value"));
               aFloat.setDefaultValue(rs.getFloat("default_value"));
               aFloat.setCriteria(rs.getString("criteria"));
               return aFloat;
            }
        };

        List<AnswerTypeFloat> list = getJdbcTemplate().query(SELECT_ANSWER_TYPE_FLOAT_BY_ID,
                new Object[]{answerTypeId},
                new int[]{INTEGER},
                mapper);

        return list.get(0);
    }

     public AnswerTypeInteger getAnswerTypeInteger(int answerTypeId){
        logger.debug("Select answer_type_integer where id=" + answerTypeId);

        RowMapper mapper = new RowMapper() {
            public AnswerTypeInteger mapRow(ResultSet rs, int rowNum) throws SQLException {
               AnswerTypeInteger aInt = new AnswerTypeInteger();
               aInt.setId(rs.getInt("id"));
               aInt.setMinValue(rs.getInt("min_value"));
               aInt.setMaxValue(rs.getInt("max_value"));
               aInt.setDefaultValue(rs.getInt("default_value"));
               aInt.setCriteria(rs.getString("criteria"));
               return aInt;
            }
        };

        List<AnswerTypeInteger> list = getJdbcTemplate().query(SELECT_ANSWER_TYPE_INTEGER_BY_ID,
                new Object[]{answerTypeId},
                new int[]{INTEGER},
                mapper);

        return list.get(0);
    }

     public AnswerTypeText getAnswerTypeText(int answerTypeId){
        logger.debug("Select answer_type_text where id=" + answerTypeId);

        RowMapper mapper = new RowMapper() {
            public AnswerTypeText mapRow(ResultSet rs, int rowNum) throws SQLException {
               AnswerTypeText text = new AnswerTypeText();
               text.setId(rs.getInt("id"));
               text.setMinChars(rs.getInt("min_chars"));
               text.setMaxChars(rs.getInt("max_chars"));
               text.setCriteria(rs.getString("criteria"));
               return text;
            }
        };

        List<AnswerTypeText> list = getJdbcTemplate().query(SELECT_ANSWER_TYPE_TEXT_BY_ID,
                new Object[]{answerTypeId},
                new int[]{INTEGER},
                mapper);

        return list.get(0);
    }

}
