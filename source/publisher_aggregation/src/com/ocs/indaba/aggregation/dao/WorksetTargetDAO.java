/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.dao;

import com.ocs.indaba.aggregation.po.WsTarget;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author jiangjeff
 */
public class WorksetTargetDAO extends SmartDaoMySqlImpl<WsTarget, Integer> {

    private static final Logger log = Logger.getLogger(WorksetTargetDAO.class);
    private String SELECT_WS_TARGETS_BY_WORKSET_ID =
            "SELECT * FROM ws_target wt WHERE workset_id = ?";
    private String SELECT_WS_TARGETS_IDS_BY_WORKSET_ID =
            "SELECT target_id FROM ws_target wt WHERE workset_id = ?";
    private String DELETE_WS_TARGETS_BY_WORKSET_ID =
            "DELETE FROM ws_target WHERE workset_id = ?";

    public List<WsTarget> selectWsTargetsByWorksetId(int worksetId) {
        return super.find(SELECT_WS_TARGETS_BY_WORKSET_ID, worksetId);
    }

    public List<Integer> selectWsTargetsIdsByWorksetId(int worksetId) {
        return getJdbcTemplate().query(SELECT_WS_TARGETS_IDS_BY_WORKSET_ID,
                new Object[]{worksetId}, new TargetIdRowMapper());
    }

    private final class TargetIdRowMapper implements RowMapper {

        public Integer mapRow(ResultSet rs, int i) throws SQLException {
            return rs.getInt("target_id");
        }
    }

    public void deleteWsTargetsByWorksetId(int worksetId) {
        super.delete(DELETE_WS_TARGETS_BY_WORKSET_ID, worksetId);
    }
}
