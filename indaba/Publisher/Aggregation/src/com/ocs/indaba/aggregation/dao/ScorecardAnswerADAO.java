/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.aggregation.po.ScorecardAnswer;
import com.ocs.indaba.aggregation.po.ScorecardAnswerA;
import com.ocs.indaba.aggregation.vo.ScorecardAnswerHelper;
import com.ocs.indaba.aggregation.vo.ScorecardAnswerVO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author luwb
 */
public class ScorecardAnswerADAO extends SmartDaoMySqlImpl<ScorecardAnswerA, Integer> {

    private static final Logger log = Logger.getLogger(ScorecardAnswerADAO.class);
    private static final String SELECT_SCORECARD_ANSWER_BY_SCORECARD_AND_INDICATOR_ID =
            "SELECT * from scorecard_answer_a WHERE scorecard_id=? and indicator_id IN ({0})";
    private static final String TRUNCATE_SCORECARD_ANSWER =
            "TRUNCATE scorecard_answer_a";
    private static final String SELECT_SCORECARD_ANSWER_BY_FILTER =
            "SELECT sa.*, s.org_id FROM scorecard_answer_a sa JOIN scorecard s ON (sa.scorecard_id = s.id) "
            + "WHERE {0} ORDER BY sa.data_type";

    public List<ScorecardAnswer> selectScorecardAnswerByScorecardAndIndicatorId(int scorecardId, List<Integer> indicatorIds) {
        String ids = indicatorIds.toString();
        String sqlStr = MessageFormat.format(SELECT_SCORECARD_ANSWER_BY_SCORECARD_AND_INDICATOR_ID, ids.substring(1, ids.length() - 1));
        List<ScorecardAnswerA> listA = super.find(sqlStr, scorecardId);
        List<ScorecardAnswer> list = new ArrayList<ScorecardAnswer>();
        if (listA != null) {
            for (ScorecardAnswerA scaA : listA) {
                list.add(ScorecardAnswerHelper.scaA2sca(scaA));
            }
        }
        return list;
    }

    public List<ScorecardAnswerVO> selectScorecardAnswerByFilter(String filter) {
        String sqlStr = MessageFormat.format(SELECT_SCORECARD_ANSWER_BY_FILTER, filter);
        return getJdbcTemplate().query(sqlStr, new ScorecardAnswerRowMapper());
    }

    public void deleteAll() {
        log.debug("delete all scorecard_answer_a data.");
        super.delete(TRUNCATE_SCORECARD_ANSWER);
    }

    private class ScorecardAnswerRowMapper implements RowMapper {

        public ScorecardAnswerVO mapRow(ResultSet rs, int i) throws SQLException {
            ScorecardAnswerVO saVO = new ScorecardAnswerVO();
            ScorecardAnswer sa = new ScorecardAnswer();
            sa.setId(rs.getInt("id"));
            sa.setDataType(rs.getShort("data_type"));
            sa.setIndicatorId(rs.getInt("indicator_id"));
            sa.setQuestionId(rs.getInt("question_id"));
            sa.setScore(rs.getBigDecimal("score"));
            sa.setScorecardId(rs.getInt("scorecard_id"));
            sa.setValue(rs.getString("value"));
            saVO.setOrgId(rs.getInt("org_id"));
            saVO.setScorecardAnswer(sa);
            return saVO;
        }
    }
}
