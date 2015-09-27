/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.aggregation.po.DpMember;
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
public class DpMemberDAO extends SmartDaoMySqlImpl<DpMember, Integer>{
    private static final Logger log = Logger.getLogger(DpMemberDAO.class);

    private static final String SELECT_DP_MEMBERS_BY_DP_ID =
            "SELECT * FROM dp_member WHERE dp_id = ?";
    private static final String SELECT_DP_MEMBER_BY_DATAPOINT_ID =
            "SELECT * FROM dp_member WHERE datapoint_id = ?";
    private static final String DELETE_DP_MEMBER_BY_DATAPOINT_ID =
            "DELETE FROM dp_member WHERE datapoint_id = ?";
    private static final String SELECT_DATAPOINT_IDS_BY_DP_ID =
            "SELECT datapoint_id FROM dp_member WHERE dp_id = ?";
    private static final String SELECT_DP_IDS_BY_DATAPOINT_ID =
            "SELECT dp_id FROM dp_member WHERE datapoint_id = ? AND dp_id > 0";
    public List<DpMember> selectDpMemberByDpId(int dpId) {
        return super.find(SELECT_DP_MEMBERS_BY_DP_ID, dpId);
    }

    public List<DpMember> selectDpMemberByDatapointId(int datapointId) {
        return super.find(SELECT_DP_MEMBER_BY_DATAPOINT_ID, datapointId);
    }

    public void deleteDpMemberByDatapointId(int datapontId){
        super.delete(DELETE_DP_MEMBER_BY_DATAPOINT_ID, datapontId);
    }

    public List<Integer> selectDatapointIdsByDpId(int dpId){
         RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("datapoint_id");
            }
        };

        return getJdbcTemplate().query(
                SELECT_DATAPOINT_IDS_BY_DP_ID,
                new Object[]{dpId},
                new int[]{INTEGER},
                mapper);
    }

    public List<Integer> selectDpIdsByDatapointId(int datapintId){
         RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("dp_id");
            }
        };

        return getJdbcTemplate().query(
                SELECT_DP_IDS_BY_DATAPOINT_ID,
                new Object[]{datapintId},
                new int[]{INTEGER},
                mapper);
    }
}
