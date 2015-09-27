/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.vo.ProjectInfo;
import com.ocs.util.ListUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Types.INTEGER;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Jeff
 */
public class ProjectDAO extends SmartDaoMySqlImpl<Project, Integer> {

    private static final Logger log = Logger.getLogger(ProjectDAO.class);
    private static final String SELECT_PROJECTS =
            "SELECT * FROM project ORDER BY code_name";
    private static final String SELECT_PROJECTS_BY_USER_ID =
            "SELECT DISTINCT prj.* FROM project prj, project_membership pm "
            + "WHERE ((pm.user_id=? AND prj.id=pm.project_id) OR prj.owner_user_id=? OR prj.admin_user_id=?) AND prj.start_time<=now() AND prj.is_active=1 ORDER BY prj.code_name";
    private static final String SELECT_PROJECT_BY_ID =
            "SELECT * FROM project WHERE id=?";
    private static final String SELECT_PROJECT_BY_NAME =
            "SELECT * FROM project WHERE code_name=?";
    /*
    private static final String SELECT_PROJECT_BY_TASK_ID =
            "SELECT prj.* FROM task t, goal g, workflow_sequence wfseq, product prd, project prj "
            + "WHERE t.id=? AND t.goal_id=g.id AND g.workflow_sequence_id=wfseq.id AND "
            + "wfseq.workflow_id=prd.workflow_id AND prd.project_id=prj.id";
     * *
     */

    private static final String SELECT_PROJECT_BY_TASK_ID =
            "SELECT proj.* FROM project proj, task t, product prod WHERE t.id=? AND prod.id=t.product_id AND proj.id=prod.project_id";

    private static final String UPDATE_MSGBOARD_ID =
            "UPDATE `project` SET msgboard_id=? WHERE `id`=?";
    private static final String SELECT_PROJECT_BY_TASK_ASSIGNMENT =
            "SELECT project.* FROM project, horse, product, task_assignment ta "
            + "WHERE ta.id=? AND horse.id=ta.horse_id AND horse.product_id=product.id AND project.id=product.project_id";
    private static final String SELECT_PROJECT_BY_HORSE_ID =
            "SELECT prj.* FROM project prj, horse h, product prd "
            + "WHERE h.id=? AND prd.id=h.product_id AND prj.id=prd.project_id";
    private static final String SELECT_PROJECT_BY_PRODUCT_ID =
            "SELECT prj.* FROM project prj, product prd "
            + "WHERE prd.id=? AND prj.id=prd.project_id";
    private static final String SELECT_EXISTS_BY_PROJECT_NAME = "SELECT COUNT(id) FROM project WHERE code_name=?";
    private static final String SELECT_PROJECT_BY_MSGBORAD_ID = "SELECT * FROM project WHERE msgboard_id=?";
    private static final String SELECT_PROJECT_INFO_BY_ORG_IDS_AND_VISIBILITY_BASE =
            " FROM ( "
            + "  (SELECT prj.code_name pname, prj.id pid, prj.organization_id oid, prj.admin_user_id auid, prj.owner_user_id ouid FROM project prj "
            + "   WHERE prj.organization_id IN ({0}) AND prj.visibility={1}) "
            + "UNION DISTINCT "
            + "  (SELECT prj.code_name pname, prj.id pid, prj.organization_id oid, prj.admin_user_id auid, prj.owner_user_id ouid FROM project prj, project_owner po "
            + "   WHERE po.project_id=prj.id AND po.org_id IN ({0}) AND prj.visibility={1}) "
            + ") temp, user, organization org, project_admin pa "
            + "WHERE user.id=temp.auid AND org.id=temp.oid AND ({2,number,#}=0 OR temp.auid={2,number,#} OR (pa.project_id=temp.pid AND pa.user_id={2,number,#}))";
    private static final String COUNT_PROJECT_INFO_BY_ORG_IDS_AND_VISIBILITY =
            "SELECT DISTINCT COUNT(temp.pid) count " + SELECT_PROJECT_INFO_BY_ORG_IDS_AND_VISIBILITY_BASE;
    private static final String SELECT_PROJECT_INFO_BY_ORG_IDS_AND_VISIBILITY_PAGINATION =
            "SELECT DISTINCT temp.*, org.name AS oname,user.last_name AS last_name, user.first_name AS first_name "
            + SELECT_PROJECT_INFO_BY_ORG_IDS_AND_VISIBILITY_BASE
            + " ORDER BY {3} {4} LIMIT {5,number,#},{6,number,#}";
    private static final String COUNT_PROJECT_INFO_BY_VISIBILITY =
            "SELECT COUNT(prj.id) count FROM project prj WHERE prj.visibility={0}";
    private static final String SELECT_PROJECT_INFO_BY_VISIBILITY_PAGINATION =
            "SELECT prj.code_name pname, prj.id pid, prj.organization_id oid, prj.admin_user_id auid, prj.owner_user_id ouid, org.name oname, user.last_name AS last_name, user.first_name AS first_name "
            + "FROM project prj, user, organization org "
            + "WHERE prj.visibility={0} AND user.id=prj.admin_user_id AND org.id=prj.organization_id "
            + "ORDER BY {1} {2} LIMIT {3,number,#},{4,number,#}";
    private static final String SELECT_SECONDARY_ORGS_OF_PROJECTS =
            "SELECT DISTINCT prj.id pid, GROUP_CONCAT(org.name SEPARATOR \", \") oname "
            + "FROM project prj, organization org, project_owner po "
            + "WHERE prj.id in ({0}) AND po.project_id=prj.id AND org.id=po.org_id "
            + "GROUP BY prj.id";
    private static final String SELECT_SECONDARY_ADMINS_OF_PROJECTS =
            "SELECT DISTINCT prj.id pid, GROUP_CONCAT(CAST(CONCAT(user.first_name, \" \", user.last_name) AS CHAR) SEPARATOR \", \") paname "
            + "FROM project prj, user, project_admin pa "
            + "WHERE prj.id in ({0}) AND pa.project_id=prj.id AND user.id=pa.user_id "
            + "GROUP BY prj.id";
    private static final String COUNT_BY_PROJECT_ID = "SELECT COUNT(*) FROM project WHERE id=?";
    private static final String COUNT_TARGET_BY_PROJECT_ID = "SELECT COUNT(*) FROM project_target WHERE project_id=?";
    private static final String COUNT_MEMBERSHIP_BY_PROJECT_ID = "SELECT COUNT(*) FROM project_membership WHERE project_id=?";

    /**
     * Select all of the projects in Indaba
     *
     * @return list of project
     */
    public List<Project> selectAllProjects() {
        log.debug("Select all of projects:" + SELECT_PROJECTS);
        return super.find(SELECT_PROJECTS);
    }

    public Project selectProjectById(int projectId) {
        log.debug("Select project by id: " + projectId + ". [" + SELECT_PROJECT_BY_ID + "].");
        return super.findSingle(SELECT_PROJECT_BY_ID, projectId);
    }

    public boolean existsTargetByProjectId(int projId) {
        return super.count(COUNT_TARGET_BY_PROJECT_ID, projId) > 0;
    }

    public boolean existsMembershipByProjectId(int projId) {
        return super.count(COUNT_MEMBERSHIP_BY_PROJECT_ID, projId) > 0;
    }

    public Project selectProjectByMsgboradId(int msgboardId) {
        return super.findSingle(SELECT_PROJECT_BY_MSGBORAD_ID, msgboardId);
    }

    public List<Project> selectProjectsByUserId(int userId) {
        log.debug("Select projects by user id: " + userId + ". [" + SELECT_PROJECTS_BY_USER_ID + "].");
        //return super.find(SELECT_PROJECTS_BY_USER_ID, userId);

        return getJdbcTemplate().query(
                SELECT_PROJECTS_BY_USER_ID,
                new Object[]{userId, userId, userId},
                new int[]{INTEGER, INTEGER, INTEGER},
                getRowMapper(false));
    }

    public Project selectProjectByTaskId(int taskId) {
        log.debug("Select project by task id: " + taskId + ". [" + SELECT_PROJECT_BY_TASK_ID + "].");
        return super.findSingle(SELECT_PROJECT_BY_TASK_ID, taskId);
    }

    public Project selectProjectByTaskAssignment(int taId) {
        log.debug("Select project by task assignment id: " + taId + ". [" + SELECT_PROJECT_BY_TASK_ASSIGNMENT + "].");
        return super.findSingle(SELECT_PROJECT_BY_TASK_ASSIGNMENT, taId);
    }

    public void updateMsgboardId(int projectId, int msgBoardId) {
        update(UPDATE_MSGBOARD_ID, msgBoardId, projectId);
    }

    public Project selectProjectByHorseId(int horseId) {
        log.debug("Select project by horse id: " + horseId + ". [" + SELECT_PROJECT_BY_HORSE_ID + "].");
        return super.findSingle(SELECT_PROJECT_BY_HORSE_ID, horseId);
    }

    public Project selectProjectByProductId(int productId) {
        log.debug("Select project by product id: " + productId + ". [" + SELECT_PROJECT_BY_PRODUCT_ID + "].");
        return super.findSingle(SELECT_PROJECT_BY_PRODUCT_ID, productId);
    }

    /**
     *
     * @param ownedProjIds
     * @param orgIds
     * @return
     */
    public long selectProjectsCountByOrgIdsAndVisibility(int userId, int visibility, List<Integer> orgIds) {
        if (orgIds == null || orgIds.isEmpty()) {
            return super.count(MessageFormat.format(COUNT_PROJECT_INFO_BY_VISIBILITY, visibility));
        } else {
            return super.count(MessageFormat.format(COUNT_PROJECT_INFO_BY_ORG_IDS_AND_VISIBILITY, ListUtils.listToString(orgIds), visibility, userId));
        }
    }


    private static final String SELECT_PROJECT_BY_GOAL_OBJECT =
            "SELECT proj.* FROM project proj, goal_object go, sequence_object so, workflow_object wo, horse h, product prod " +
            "WHERE go.id=? AND so.id=go.sequence_object_id AND wo.id=so.workflow_object_id AND h.workflow_object_id=wo.id " +
            "AND prod.id=h.product_id AND proj.id=prod.project_id";

    public Project getProjectByGoalObject(int goalObjId) {
        return super.findSingle(SELECT_PROJECT_BY_GOAL_OBJECT, (long)goalObjId);
    }


    /**
     *
     * @param ownedProjIds
     * @param orgIds
     * @param sortName
     * @param sortOrder
     * @param offset
     * @param count
     * @return
     */
    public List<ProjectInfo> selectProjectsByOrgIdsAndVisibilityWithPagination(int userId, int visibility, List<Integer> orgIds, String sortName, String sortOrder, int offset, int count) {
        if ("ORGNAME".equalsIgnoreCase(sortName)) {
            sortName = "oname";
        } else {
            sortName = "pname";
        }

        String sqlStr;

        if (orgIds == null || orgIds.isEmpty()) {
            sqlStr = MessageFormat.format(SELECT_PROJECT_INFO_BY_VISIBILITY_PAGINATION, visibility, sortName, sortOrder, offset, offset + count);
        } else {
            sqlStr = MessageFormat.format(SELECT_PROJECT_INFO_BY_ORG_IDS_AND_VISIBILITY_PAGINATION, ListUtils.listToString(orgIds), visibility, userId, sortName, sortOrder, offset, offset + count);
        }

        log.debug("Select projects by limit[sortName=" + sortName + ", sortOrder=" + sortOrder + ", offset=" + offset
                + ", orgIds=" + orgIds + ", count=" + count + "]:" + sqlStr);

        List<ProjectInfo> prjList = getJdbcTemplate().query(sqlStr, new Object[]{}, new ProjectInfoRowMapper());


        return getProjectSecondaryInfo(prjList);
    }

    private List<ProjectInfo> getProjectSecondaryInfo(List<ProjectInfo> projList) {
        if (projList == null || projList.size() == 0) {
            return projList;
        }

        List<Integer> idList = new ArrayList<Integer>();

        for (ProjectInfo p : projList) {
            idList.add(p.getPrjId());
        }

        String idListStr = ListUtils.listToString(idList);
        String sqlStr = MessageFormat.format(SELECT_SECONDARY_ORGS_OF_PROJECTS, idListStr);

        log.debug("Select Secondary Orgs: " + sqlStr);

        List<ProjectInfo> piList = getJdbcTemplate().query(sqlStr, new Object[]{}, new ProjectInfoRowMapperForOrgs());

        if (piList != null && piList.size() > 0) {
            for (ProjectInfo pi : piList) {
                for (ProjectInfo p : projList) {
                    if (p.getPrjId() == pi.getPrjId()) {
                        p.setSecondaryOrgNames(pi.getSecondaryOrgNames());
                    }
                }
            }
        }

        // get secondary admins
        sqlStr = MessageFormat.format(SELECT_SECONDARY_ADMINS_OF_PROJECTS, idListStr);

        log.debug("Select Secondary Admins: " + sqlStr);

        piList = getJdbcTemplate().query(sqlStr, new Object[]{}, new ProjectInfoRowMapperForAdmins());

        if (piList != null && piList.size() > 0) {
            for (ProjectInfo pi : piList) {
                for (ProjectInfo p : projList) {
                    if (p.getPrjId() == pi.getPrjId()) {
                        p.setSecondaryAdminFullNames(pi.getSecondaryAdminFullNames());
                    }
                }
            }
        }

        return projList;
    }

    public Project getProjectByName(String projName) {
        return super.findSingle(SELECT_PROJECT_BY_NAME, projName);
    }

    public Project getProjectById(Integer id) {
        return super.get(id);
    }

    private class ProjectInfoRowMapper implements RowMapper {

        public ProjectInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProjectInfo project = new ProjectInfo();
            project.setPrjId(rs.getInt("pid"));
            project.setPrjName(rs.getString("pname"));
            project.setPrimaryOrgName(rs.getString("oname"));
            project.setPrimaryAdminUID(rs.getInt("auid"));
            project.setPrimaryAdminFullName(rs.getString("first_name") + " " + rs.getString("last_name"));
            project.setOwnerUserId(rs.getInt("auid"));

            return project;
        }
    };

    private class ProjectInfoRowMapperForOrgs implements RowMapper {

        public ProjectInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProjectInfo project = new ProjectInfo();
            project.setPrjId(rs.getInt("pid"));
            project.setSecondaryOrgNames(rs.getString("oname"));
            return project;
        }
    };

    private class ProjectInfoRowMapperForAdmins implements RowMapper {

        public ProjectInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProjectInfo project = new ProjectInfo();
            project.setPrjId(rs.getInt("pid"));
            project.setSecondaryAdminFullNames(rs.getString("paname"));
            return project;
        }
    };


    private static final String SELECT_BY_NOTEDEF =
            "SELECT proj.* FROM project proj, product prod, notedef nd " +
            "WHERE nd.id=? AND prod.id=nd.product_id AND proj.id=prod.project_id";

    public Project getProjectByNotedef(int notedefId) {
        return super.findSingle(SELECT_BY_NOTEDEF, notedefId);
    }
}
