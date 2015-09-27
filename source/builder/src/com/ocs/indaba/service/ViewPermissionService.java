/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.ProjectDAO;
import com.ocs.indaba.dao.RoleDAO;
import com.ocs.indaba.dao.UserDAO;
import com.ocs.indaba.dao.ViewPermissionDAO;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.Role;
import com.ocs.indaba.po.User;
import com.ocs.indaba.po.ViewMatrix;
import com.ocs.indaba.po.ViewPermission;
import com.ocs.indaba.vo.CommMemberInfo;
import com.ocs.indaba.vo.UserDisplay;
import com.ocs.util.StringUtils;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class ViewPermissionService {

    private static final Logger logger = Logger.getLogger(ViewPermissionService.class);
    private RoleDAO roleDao = null;
    private UserDAO userDao = null;
    private ViewPermissionDAO viewPermissionDao = null;
    private ProjectDAO projectDao = null;

    /**
     * 
     * @param projectId
     * @param subjectUserId
     * @param targetUserId
     * @return
     */
    public UserDisplay getUserDisplayOfProject(int projectId, int viewMatrixId, int subjectUserId, int targetUserId) {
        logger.debug("Get UserDisplay: [projectId=" + projectId + ", viewMatrixId=" + viewMatrixId + ", subjectUserId=" + subjectUserId + ", targetUserId=" + targetUserId + "].");
        Project project = projectDao.selectProjectById(projectId);
        UserDisplay userDisplay = new UserDisplay();
        userDisplay.setUserId(targetUserId);
        User targetUser = userDao.selectUserById(targetUserId);

        Role subjectRole = roleDao.selectRoleByProjectIdAndUserId(projectId, subjectUserId);
        Role targetRole = roleDao.selectRoleByProjectIdAndUserId(projectId, targetUserId);
        ViewPermission viewPermission = null;

        if (project != null) viewMatrixId = project.getViewMatrixId();

        try {
            viewPermission = viewPermissionDao.selectViewPermission(viewMatrixId, subjectRole.getId(), targetRole.getId());
        } catch (Exception e) {}

        int permission = Constants.VIEW_PERMISSION_NONE;

        if (viewPermission == null) { // if no definition in view permission, enable the default specification in view matrix
            ViewMatrix viewMatrix = viewPermissionDao.getViewMatrix(viewMatrixId);
            if (viewMatrix != null) {
                permission = viewMatrix.getDefaultValue();
            }
        } else {
            permission = viewPermission.getPermission();
        }

        if (subjectUserId == targetUserId) {
            userDisplay.setPermission(Constants.VIEW_PERMISSION_STATS);
        } else {
            userDisplay.setPermission(permission);
        }

        switch (userDisplay.getPermission()) {
            case Constants.VIEW_PERMISSION_LIMITED:
            case Constants.VIEW_PERMISSION_STATS:
            case Constants.VIEW_PERMISSION_FULL:
                String fullName = "";
                if (targetUser != null) {
                    fullName = getUserDisplayNameFull(targetUser.getFirstName(), targetUser.getLastName(), targetUser.getUsername());
                    userDisplay.setBio(targetUser.getBio());
                }
                userDisplay.setDisplayUsername(fullName);     
                break;

            case Constants.VIEW_PERMISSION_NONE:
            default: // no need to display bio info
                if (targetRole == null) {                     
                    userDisplay.setDisplayUsername("user" + targetUserId);
                } else {
                    userDisplay.setDisplayUsername(targetRole.getName() + targetUserId);
                }
                break;
        }
        return userDisplay;
    }


    private String getUserDisplayNameFull(String firstName, String lastName, String userName) {
        String fullName = "";
        if (firstName != null) {
            fullName = firstName;
        }
        if (lastName != null) {
            fullName += " " + lastName;
        }
        if (fullName.length() == 0) {
            fullName = userName;
        }
        return fullName;
    }


    static public class ViewPermissionCtx {
        protected int subjectRoleId;
        protected int matrixId;
        protected int projectId;
        protected ViewMatrix viewMatrix;
        protected HashMap<Integer, Short> roleMap;
    }

    public ViewPermissionCtx getViewPermissionCtx(int projectId, int viewMatrixId, int subjectRoleId) {
        ViewPermissionCtx ctx = new ViewPermissionCtx();

        Project project = projectDao.selectProjectById(projectId);
        if (project != null) viewMatrixId = project.getViewMatrixId();
        ctx.viewMatrix = viewPermissionDao.getViewMatrix(viewMatrixId);
        List<ViewPermission> permissions = viewPermissionDao.selectViewPermissionsBySubject(viewMatrixId, subjectRoleId);
        if (permissions != null && !permissions.isEmpty()) {
            ctx.roleMap = new HashMap<Integer, Short>();
            for (ViewPermission p : permissions) {
                ctx.roleMap.put(p.getTargetRoleId(), p.getPermission());
            }
        }
        return ctx;
    }


    /*
     * Set the display name of the users. The roleId of the users must have been set already!
     */
    public void setDisplayNames(ViewPermissionCtx ctx, List<CommMemberInfo> users, int subjectUserId) {
        if (ctx == null || users == null || users.isEmpty()) return;   // nothing to do

        for (CommMemberInfo user : users) {
            if (user.getUserId() == subjectUserId) {
                user.setDisplayName(getUserDisplayNameFull(user.getFirstName(), user.getLastName(), user.getUserName()));
                continue;
            }
            short permission = Constants.VIEW_PERMISSION_NONE;
            String displayName = "";
            if (ctx.viewMatrix != null) permission = ctx.viewMatrix.getDefaultValue();
            if (ctx.roleMap != null) {
                Short perm = ctx.roleMap.get(user.getRoleId());
                if (perm != null) permission = perm;
            }

            switch (permission) {
                case Constants.VIEW_PERMISSION_LIMITED:
                case Constants.VIEW_PERMISSION_STATS:
                case Constants.VIEW_PERMISSION_FULL:
                    displayName = getUserDisplayNameFull(user.getFirstName(), user.getLastName(), user.getUserName());
                    break;

                default:
                    if (!StringUtils.isEmpty(user.getRoleName())) {
                        displayName = user.getRoleName() + user.getUserId();
                    } else {
                        displayName = "user" + user.getUserId();
                    }
                    break;
            }
            user.setDisplayName(displayName);
        }
    }



    @Autowired
    public void setRoleDao(RoleDAO roleDao) {
        this.roleDao = roleDao;
    }

    @Autowired
    public void setUserDao(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setViewPermissionDao(ViewPermissionDAO viewPermissionDao) {
        this.viewPermissionDao = viewPermissionDao;
    }

    @Autowired
    public void setProjectDao(ProjectDAO projectDao) {
        this.projectDao = projectDao;
    }
}
