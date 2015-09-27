/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.builder.dao;

import static java.sql.Types.INTEGER;
import static java.sql.Types.VARCHAR;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.Target;
import com.ocs.indaba.util.ResultSetUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import com.ocs.util.StringUtils;


/**
 *
 * @author jiangjeff
 */
public class ProjectDAO extends SmartDaoMySqlImpl<Project, Integer> {

    private static final Logger log = Logger.getLogger(ProjectDAO.class);

    private static final String SELECT_TARGETS_BY_PROJECT_IDS = "SELECT DISTINCT t.* FROM target t, project_target pt WHERE t.id=pt.target_id AND pt.project_id IN ({0})";

    private static final String SELECT_PROJECTS_BY_PROJECT_IDS = "SELECT * FROM project WHERE id IN ({0})";

    private static final String SELECT_STUDY_PERIOD_ID_BY_HORSE_ID = "SELECT prj.study_period_id FROM horse h, product prd, project prj WHERE h.id=? AND h.product_id=prd.id AND prj.id=prd.project_id";

    private String SELECT_PROJECT_IDS_BY_USERNAME =
            "SELECT DISTINCT p.id FROM project p, user u "
            + "WHERE u.username = ? AND p.visibility = 2 AND (u.site_admin = 1 OR u.organization_id = p.organization_id)";

    private String SELECT_PUBLIC_PROJECT_IDS =
            "SELECT DISTINCT p.id FROM project p WHERE p.visibility = 1";

    private static final String SELECT_STUDY_PERIID_IDS_BY_PROJECT_IDS = "SELECT DISTINCT study_period_id FROM project WHERE id IN ({0})";

    private static final String CHECK_USER_IN_PROJECT = "SELECT count(*) count FROM user u, project p, project_membership pm WHERE u.id=? AND pm.user_id=u.id AND p.code_name=? AND p.id=pm.project_id";

    private static final String SELECT_PROJECT_BY_NAME = "SELECT * FROM project WHERE code_name=?";


    private static final String SELECT_PUBLIC_PROJECTS_BY_ORG_ID =
            "SELECT prj.* FROM project prj, product prd " +
            "WHERE prj.organization_id=? AND prj.visibility=1 AND prd.project_id=prj.id AND prd.mode != 1 AND prd.content_type=0 " +
            "UNION " +
            "SELECT prj.* FROM project prj, product prd, project_owner po " +
            "WHERE po.project_id=prj.id AND po.org_id=? AND prj.visibility=1 AND prd.project_id=prj.id AND prd.mode != 1 AND prd.content_type=0 " +
            "GROUP BY id " +
            "ORDER BY code_name ASC";


    private static final String SELECT_PRIVATE_PROJECTS_BY_ORG_ID =
            "SELECT prj.* FROM project prj, product prd " +
            "WHERE prj.organization_id=? AND prj.visibility=2 AND prd.project_id=prj.id AND prd.mode != 1 AND prd.content_type=0 " +
            "UNION " +
            "SELECT prj.* FROM project prj, product prd, project_owner po " +
            "WHERE po.project_id=prj.id AND po.org_id=? AND prj.visibility=2 AND prd.project_id=prj.id AND prd.mode != 1 AND prd.content_type=0 " +
            "GROUP BY id " +
            "ORDER BY code_name ASC";

    private static final String SELECT_ALL_PROJECTS_BY_ORG_ID =
            "SELECT prj.* FROM project prj WHERE prj.organization_id=? " +
            "UNION " +
            "SELECT prj.* FROM project prj, project_owner po " +
            "WHERE po.project_id=prj.id AND po.org_id=? " +
            "GROUP BY id " +
            "ORDER BY code_name ASC";


    public List<Integer> selectProjectIdsByUsername(String username) {

        logger.debug("Select project ids by username: " + username);
        return getJdbcTemplate().query(SELECT_PROJECT_IDS_BY_USERNAME,
                new Object[]{username}, new ProjectIdRowMapper());
    }

    public List<Integer> selectPublicProjectIds() {

        logger.debug("Select public project ids.");
        return getJdbcTemplate().query(SELECT_PUBLIC_PROJECT_IDS,
                new Object[]{}, new ProjectIdRowMapper());
    }

    public List<Project> selectPublicProjectsByOrgId(int orgId) {
        return super.find(SELECT_PUBLIC_PROJECTS_BY_ORG_ID, (long)orgId, orgId);
    }

    public List<Project> selectPrivateProjectsByOrgId(int orgId) {
        return super.find(SELECT_PRIVATE_PROJECTS_BY_ORG_ID, (long)orgId, orgId);
    }

    private final class ProjectIdRowMapper implements RowMapper {

        public Integer mapRow(ResultSet rs, int i) throws SQLException {
            return rs.getInt("id");
        }
    }

    public List<Project> selectProjectsByOrgId(int orgId) {
        logger.debug("Select projects by organization id: " + orgId);
        return super.find(SELECT_ALL_PROJECTS_BY_ORG_ID, (long)orgId, orgId);
    }


    public List<Project> selectProjectsByProjectIds(List<Integer> projectIds) {
        logger.debug("Select projects by ids: " + projectIds);
        if (projectIds == null || projectIds.isEmpty()) {
            return null;
        }

        String ids = projectIds.toString();
        String sqlStr = MessageFormat.format(SELECT_PROJECTS_BY_PROJECT_IDS, ids.substring(1, ids.length() - 1));

        return super.find(sqlStr);
    }

    public List<Target> selectTargetsByProjectIds(List<Integer> projectIds) {
        logger.debug("Select targets by project ids: " + projectIds);
        if (projectIds == null || projectIds.isEmpty()) {
            return null;
        }

        RowMapper mapper = new RowMapper() {

            public Target mapRow(ResultSet rs, int rowNum) throws SQLException {
                Target target = new Target();
                target.setId(ResultSetUtil.getInt(rs, "id"));
                target.setName(ResultSetUtil.getString(rs, "name"));
                target.setTargetType(ResultSetUtil.getShort(rs, "target_type"));
                return target;
            }
        };

        String ids = projectIds.toString();
        String sqlStr = MessageFormat.format(SELECT_TARGETS_BY_PROJECT_IDS, ids.substring(1, ids.length() - 1));

        return getJdbcTemplate().query(
                sqlStr,
                new Object[]{},
                new int[]{},
                mapper);
    }

    public Integer selectStudyPeriodIdByHorseId(Integer horseId) {
        logger.debug("Select study period id by horse id: " + horseId);
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return (ResultSetUtil.getInt(rs, "study_period_id"));
            }
        };
        List<Integer> list = getJdbcTemplate().query(
                SELECT_STUDY_PERIOD_ID_BY_HORSE_ID,
                new Object[]{horseId},
                new int[]{INTEGER},
                mapper);
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }

    public List<Integer> selectStudyPeriodIdsByProjectIds(List<Integer> projectIds) {
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return (ResultSetUtil.getInt(rs, "study_period_id"));
            }
        };
        String ids = projectIds.toString();
        String sqlStr = MessageFormat.format(SELECT_STUDY_PERIID_IDS_BY_PROJECT_IDS, ids.substring(1, ids.length() - 1));
        return getJdbcTemplate().query(
                sqlStr,
                new Object[]{},
                new int[]{},
                mapper);
    }

    public boolean checkUserInProject(int userId, String projectName) {
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return (ResultSetUtil.getInt(rs, "count"));
            }
        };
        List<Integer> list = getJdbcTemplate().query(
                CHECK_USER_IN_PROJECT,
                new Object[]{userId, projectName},
                new int[]{INTEGER, VARCHAR},
                mapper);
         return (list == null || list.isEmpty()) ? false : (list.get(0) > 0);
    }
    
    public Project selectProjectByName(String porjectName) {
        logger.debug("Select project by name: " + porjectName);
        return super.findSingle(SELECT_PROJECT_BY_NAME, porjectName);
    }
}
