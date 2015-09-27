/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Rights;
import com.ocs.indaba.dao.AccessMatrixDAO;
import com.ocs.indaba.dao.AccessPermissionDAO;
import com.ocs.indaba.dao.GoalDAO;
import com.ocs.indaba.dao.GoalObjectDAO;
import com.ocs.indaba.dao.ProductDAO;
import com.ocs.indaba.dao.ProjectDAO;
import com.ocs.indaba.dao.ProjectMembershipDAO;
import com.ocs.indaba.dao.ToolDAO;
import com.ocs.indaba.dao.UserDAO;
import com.ocs.indaba.po.AccessMatrix;
import com.ocs.indaba.po.AccessPermission;
import com.ocs.indaba.po.Goal;
import com.ocs.indaba.po.GoalObject;
import com.ocs.indaba.po.Product;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.ProjectMembership;
import com.ocs.indaba.po.Tool;
import com.ocs.indaba.po.User;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class AccessPermissionService {

    static public class PermissionCtx {
        protected int userId;
        protected int projectId;
        protected Project project;
        protected boolean isSuperUser;
        protected boolean isProjectAdmin;
        protected int roleId;
    }


    private static final Logger logger = Logger.getLogger(AccessPermissionService.class);

    private UserDAO userDao = null;
    private ProductDAO productDao = null;
    private ProjectDAO projectDao = null;
    private GoalDAO goalDao = null;
    private GoalObjectDAO goalObjectDao = null;
    private ToolDAO toolDao = null;
    private ProjectMembershipDAO projectMembershipDao = null;
    private AccessPermissionDAO accessPermissionDao = null;
    private AccessMatrixDAO accessMatrixDao = null;
    private ProjectService projService = null;

    static private HashMap<String, RightEvaluator> evalMap = null;

    static public void setEvaluator(String rightName, RightEvaluator evaluator) {
        if (rightName == null || rightName.isEmpty()) return;
        
        if (evaluator == null) return;

        if (evalMap == null) {
            evalMap = new HashMap<String, RightEvaluator>();
        }

        evalMap.put(rightName, evaluator);
    }

    /**
     * check the user's permission when performing a task (submit, edit, staff
     * review, peer review, pay, approve, etc)
     *
     * @param projectId
     * @param userId
     * @param rightName
     * @param toolName
     * @return
     */
    public boolean checkTaskPermission(int projectId, int taskAssignmentId, int userId, String rightName, String toolName) {
        if (userId <= 0) return false;

        if (isSuperUser(userId)) return true;

        Tool tool = toolDao.selectToolByName(toolName);
        ProjectMembership membership = projectMembershipDao.selectProjectMembership(projectId, userId);

        if (membership == null) return false;

        int accessMatrixId = -1;
        if (tool != null && (accessMatrixId = tool.getAccessMatrixId()) != Constants.ACCESS_MATRIX_UNDIFINED) {
            return (checkPermissionMatrix(accessMatrixId, membership.getRoleId(), rightName) == Constants.ACCESS_PERMISSION_YES);
        } else {
            GoalObject go = goalObjectDao.selectGoalObjectByTaskAssignmentId(taskAssignmentId);
            return this.hasAccess(go.getId(), userId, rightName);
        }
    }

    /**
     * Check non-content related rights (e.g. see project wall, open cases, etc)
     *
     * @param projectId
     * @param userId
     * @param rightName
     * @return
     */
    public boolean checkProjectPermission(int projectId, int userId, String rightName) {
        PermissionCtx ctx = getPermissionCtx(projectId, userId);
        return checkProjectPermission(ctx, rightName);
    }


    public PermissionCtx getPermissionCtx(int projectId, int userId) {
        PermissionCtx ctx = new PermissionCtx();
        ctx.projectId = projectId;
        ctx.userId = userId;
        ctx.roleId = -1;

        if (userId <= 0) return ctx;

        if (isSuperUser(userId)) {
            ctx.isSuperUser = true;
            return ctx;
        }

        ctx.project = projectDao.selectProjectById(projectId);
        if (ctx.project == null) return ctx;

        // see if the user is the Primary Admin of the project
        if (userId == ctx.project.getAdminUserId()) {
            ctx.isProjectAdmin = true;
            return ctx;
        }

        // see if the user is a secondary admin of the project
        if (projService.getProjectAdmin(projectId, userId) != null) {
            ctx.isProjectAdmin = true;
            return ctx;
        }

        ProjectMembership membership = projectMembershipDao.selectProjectMembership(projectId, userId);
        if (membership == null) return ctx;

        ctx.roleId = membership.getRoleId();
        return ctx;
    }


    public boolean checkProjectPermission(PermissionCtx ctx, String rightName) {
        if (ctx.userId <= 0) return false;
        if (ctx.isSuperUser || ctx.isProjectAdmin) return true;
        if (ctx.project == null) return false;
        if (ctx.roleId < 0) return false;

        int result = RightEvaluator.UNDEFINED;

        if (evalMap != null) {
            RightEvaluator evaluator = evalMap.get(rightName);

            if (evaluator != null) {
                result = evaluator.evaluate(rightName, ctx.project, ctx.userId, ctx.roleId);
            }
        }

        if (result != RightEvaluator.UNDEFINED) {
            return (result == RightEvaluator.YES);
        } else {
            return (checkProjectPermissionMatrix(ctx.project, ctx.roleId, rightName) == Constants.ACCESS_PERMISSION_YES);
        }
    }



    /**
     * Check permission controls of content viewing
     *
     * @param workflowObjectId
     * @param projectId
     * @param userId
     * @param rightName
     * @return
     */
    public boolean checkViewingPermission(int projectId, int workflowObjectId, int userId, String rightName) {
        if (userId <= 0) return false;

        // Check if the user is a super user or has the permission of 'manage site'
        if (isSuperUser(userId) || this.checkProjectPermission(projectId, userId, Rights.MANAGE_SITE)) {
            return true;
        }
        List<GoalObject> activeGoalObjects = goalObjectDao.selectActiveGoalObjectsByWorkflowObjectId(workflowObjectId);
        if (activeGoalObjects != null) {
            for (GoalObject go : activeGoalObjects) {
                if (hasAccess(go.getId(), userId, rightName)) {// okay if allowed by any active go
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Check the permission
     *
     * @param projectId
     * @param userId
     * @param rightName
     * @param toolName
     * @return
     */
    public boolean isPermitted(int projectId, int userId, String rightName, String toolName) {
        if (userId <= 0) return false;

        if (isSuperUser(userId)) return true;

        AccessPermission permission = accessPermissionDao.selectAccessPermission(projectId, userId, rightName, toolName);

        return (permission != null && permission.getPermission() == Constants.ACCESS_PERMISSION_YES);

    }

    /**
     * Check the permission
     *
     * @param projectId
     * @param userId
     * @param rightName
     * @param toolName
     * @return
     */
    public boolean isPermitted(int projectId, int userId, String rightName) {

        if (userId <= 0) return false;
        
        if (isSuperUser(userId)) return true;

        //AccessPermission permission = accessPermissionDao.selectAccessPermissionByUser(projectId, userId, rightName);
        //return (permission != null && permission.getPermission() == Constants.ACCESS_PERMISSION_YES);

        return checkProjectPermission(projectId, userId, rightName);
    }

    /**
     * checks if the user has access to the specified right within the scope of
     * the specified goal object follows matrix cascade
     *
     * @param goalObjectId
     * @param userId
     * @param rightName
     * @return
     */
    private boolean hasAccess(int goalObjectId, int userId, String rightName) {
        if (userId <= 0) return false;

        Product product = productDao.selectProductByGoalObjectId(goalObjectId);

        if (product == null) return false;

        int projectId = product.getProjectId();
        ProjectMembership membership = projectMembershipDao.selectProjectMembership(projectId, userId);

        if (membership == null) return false;

        int roleId = membership.getRoleId();
        // check the matrix of the goal first
        Goal goal = goalDao.selectGoalByGoalObjectId(goalObjectId);
        int result;
        if (goal != null) {
            result = this.checkPermissionMatrix(goal.getAccessMatrixId(), roleId, rightName);
            if (result != Constants.ACCESS_PERMISSION_UNDIFINED) {
                return (result == Constants.ACCESS_PERMISSION_YES);
            }
        }

        // if goal-level's value is UNDEFINED, check product level
        result = this.checkPermissionMatrix(product.getAccessMatrixId(), roleId, rightName);
        if (result != Constants.ACCESS_PERMISSION_UNDIFINED) {
            return (result == Constants.ACCESS_PERMISSION_YES);
        }
        // finally, check project level
        Project project = projectDao.selectProjectById(projectId);

        if (project == null) return false;

        result = this.checkPermissionMatrix(project.getAccessMatrixId(), roleId, rightName);

        return (result == Constants.ACCESS_PERMISSION_YES);
    }

    /**
     * The basic function that checks a permission matrix
     *
     * @param accessMatrixId
     * @param roleId
     * @param rightName
     * @return
     */
    private int checkPermissionMatrix(int accessMatrixId, int roleId, String rightName) {
        AccessPermission permission = accessPermissionDao.selectAccessPermission(accessMatrixId, roleId, rightName);
        if (permission != null) {
            return permission.getPermission();
        } else {
            AccessMatrix matrix = accessMatrixDao.selectMatrixById(accessMatrixId);
            return matrix.getDefaultValue();
        }
    }


    public int checkProjectPermissionMatrix(Project project, int roleId, String rightName) {
        if (project == null) return Constants.ACCESS_PERMISSION_NO;
        
        return checkPermissionMatrix(project.getAccessMatrixId(), roleId, rightName);
    }
    
    /**
     * Check if the specified user is a super-user(with 'site admin')
     *
     * @param userId
     * @return
     */
    private boolean isSuperUser(int userId) {
        if (userId <= 0) return false;

        User user = userDao.get(userId);

        if (user == null) {
            logger.error("Non-existent user: " + userId);
            return false;
        } else {
            return (user.getSiteAdmin() != 0);
        }
    }

    @Autowired
    public void setProductDao(ProductDAO productDao) {
        this.productDao = productDao;
    }

    @Autowired
    public void setGoalDao(GoalDAO goalDao) {
        this.goalDao = goalDao;
    }

    @Autowired
    public void setGoalObjectDao(GoalObjectDAO goalObjectDao) {
        this.goalObjectDao = goalObjectDao;
    }

    @Autowired
    public void setToolDao(ToolDAO toolDao) {
        this.toolDao = toolDao;
    }

    @Autowired
    public void setAccessPermissionDao(AccessPermissionDAO accessPermissionDao) {
        this.accessPermissionDao = accessPermissionDao;
    }

    @Autowired
    public void setAccessMatrixDao(AccessMatrixDAO accessMatrixDao) {
        this.accessMatrixDao = accessMatrixDao;
    }

    @Autowired
    public void setProjectDao(ProjectDAO projectDao) {
        this.projectDao = projectDao;
    }

    @Autowired
    public void setProjectMembershipDao(ProjectMembershipDAO projectMembershipDao) {
        this.projectMembershipDao = projectMembershipDao;
    }

    @Autowired
    public void setUserDao(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setProjectService(ProjectService srvc) {
        this.projService = srvc;
    }
}
