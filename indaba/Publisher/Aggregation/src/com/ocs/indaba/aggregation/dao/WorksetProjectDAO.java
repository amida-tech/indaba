/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.dao;

import com.ocs.indaba.aggregation.po.WsProject;
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
public class WorksetProjectDAO extends SmartDaoMySqlImpl<WsProject, Integer> {

    private static final Logger log = Logger.getLogger(WorksetProjectDAO.class);
    
    private String SELECT_PROJECT_IDS_BY_WORKSET_ID =
            "SELECT project_id FROM ws_project WHERE workset_id = ?";
    private String SELECT_PROJECT_BY_WORKSET_ID =
            "SELECT * FROM ws_project WHERE workset_id = ?";
    private String DELETE_PROJECTS_BY_WORKSET_ID =
            "DELETE FROM ws_project WHERE workset_id = ?";

    public List<Integer> selectProjectIdsByWorksetId(int worksetId) {
        return getJdbcTemplate().query(SELECT_PROJECT_IDS_BY_WORKSET_ID,
                new Object[]{worksetId}, new ProjectIdRowMapper());
    }

    public List<WsProject> selectProjectByWorksetId(int worksetId) {
        return super.find(SELECT_PROJECT_BY_WORKSET_ID, worksetId);
    }

    private final class ProjectIdRowMapper implements RowMapper {

        public Integer mapRow(ResultSet rs, int i) throws SQLException {
            return rs.getInt("project_id");
        }
    }

    public void deleteProjectsByWorksetId(int worksetId){
        super.delete(DELETE_PROJECTS_BY_WORKSET_ID, worksetId);
    }
}
