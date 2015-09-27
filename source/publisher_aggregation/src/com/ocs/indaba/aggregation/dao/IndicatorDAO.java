/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.dao;

import com.ocs.indaba.aggregation.vo.PubIndicatorVO;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.SurveyIndicator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Gerry
 */
public class IndicatorDAO extends SmartDaoMySqlImpl<SurveyIndicator, Integer>{

    private String SELECT_PUB_INDICATOR_VO_BY_WORKSET_ID =
            "SELECT w.id as ws_indicator_instance_id,w.workset_id,i.*  " +
            "FROM `indaba`.`survey_indicator` as i inner join `indaba_publisher`.`ws_indicator_instance` as w on w.indicator_id=i.id "+
            "where w.workset_id=?";

    public List<PubIndicatorVO> selectIndicatorVOByWorksetId(int worksetId){
        List<PubIndicatorVO> ret = getJdbcTemplate().query(SELECT_PUB_INDICATOR_VO_BY_WORKSET_ID,
                new Object[]{worksetId}, new WsIndicatorInstanceRowMapper());
        return ret;
    }
    private static class WsIndicatorInstanceRowMapper implements RowMapper {

        public PubIndicatorVO mapRow(ResultSet rs, int i) throws SQLException {
            PubIndicatorVO pubIndicatorVO = new PubIndicatorVO();
            pubIndicatorVO.setWorksetId(rs.getInt("workset_id"));
            pubIndicatorVO.setWsIndicatorId(rs.getInt("ws_indicator_instance_id"));
            SurveyIndicator indicator = new SurveyIndicator();
            indicator.setId(rs.getInt("id"));
            indicator.setName(rs.getString("name"));
            indicator.setQuestion(rs.getString("question"));
            indicator.setAnswerType(rs.getShort("answer_type"));
            indicator.setAnswerTypeId(rs.getInt("answer_type_id"));
            indicator.setReferenceId(rs.getInt("reference_id"));
            indicator.setTip(rs.getString("tip"));
            indicator.setCreateUserId(rs.getInt("create_user_id"));
            indicator.setCreateTime(rs.getDate("create_time"));
            indicator.setReusable(rs.getBoolean("reusable"));
            indicator.setOriginalIndicatorId(rs.getInt("original_indicator_id"));
            pubIndicatorVO.setIndicator(indicator);
            return pubIndicatorVO;
        }
    }
}
