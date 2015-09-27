/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.service;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.controlpanel.common.ControlPanelConstants;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.dao.OrgadminDAO;
import com.ocs.indaba.dao.ProductDAO;
import com.ocs.indaba.dao.ProjectAdminDAO;
import com.ocs.indaba.dao.RoleDAO;
import com.ocs.indaba.dao.UserDAO;
import com.ocs.indaba.po.Orgadmin;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.ProjectAdmin;
import com.ocs.indaba.po.ProjectMembership;
import com.ocs.indaba.po.Role;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.MailbatchService;
import com.ocs.indaba.service.NotificationItemService;
import com.ocs.indaba.service.ProjectService;
import com.ocs.indaba.vo.NotificationView;
import com.ocs.util.SpringContextUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

/**
 *
 * @author luwenbin
 */
@Service
public class ProjectControlPanelService {

    private static final Logger log = Logger.getLogger(ProjectControlPanelService.class);
    @Autowired
    private ProjectAdminDAO projectAdminDao = null;
    @Autowired
    private OrgadminDAO orgadminDao = null;
    @Autowired
    private UserDAO userDao = null;
    @Autowired
    private RoleDAO roleDao = null;
    @Autowired
    private NotificationItemService notificationItemService;
    @Autowired
    private ProductDAO productDao;
    @Autowired
    private MailbatchService mailService;
    @Autowired
    private ProjectService projectService;

    public boolean hasCreateAuthority(LoginUser loginUser) {
        if (loginUser.isSiteAdmin()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasConfigAuthority(LoginUser user, int projectId) {
        return isSiteAdminOrProjectAdmin(user, projectId);
    }

    public boolean hasAddContributorAuthority(LoginUser user, int projectId) {
        return isSiteAdminOrProjectAdmin(user, projectId);
    }

    public boolean hasMangageAuthority(LoginUser user, int projectId) {
        return isSiteAdminOrProjectAdmin(user, projectId);
    }

    public boolean hasChangeModeAuthority(LoginUser user, int projectId) {
        return isSiteAdminOrProjectAdmin(user, projectId);
    }

    public boolean hasTaskManageAuthority(LoginUser user, int projectId) {
        return isSiteAdminOrProjectAdmin(user, projectId);
    }

    public boolean isSiteAdminOrProjectAdmin(LoginUser user, int projectId) {
        if (user.isSiteAdmin()) {
            return true;
        }

        ProjectAdmin admin = projectService.getProjectAdmin(projectId, user.getUserId());
        if (admin != null) {
            return true;
        } else {
            return false;
        }
    }

    public User checkPAForSecondaryOwner(Project project, int orgId) {
        int projectId = project.getId();
        List<Orgadmin> orgadmins = orgadminDao.selectOrgadminByOrgId(orgId);
        if (orgadmins != null && orgadmins.size() > 0) {
            Set<Integer> users = new HashSet<Integer>();
            for (Orgadmin orgadmin : orgadmins) {
                int userId = orgadmin.getUserId();
                users.add(userId);
            }
            List<ProjectAdmin> projectAdmins = projectAdminDao.selectProjectAdminByProjectId(projectId);
            if (projectAdmins == null || projectAdmins.isEmpty()) {
                return null;
            }
            for (ProjectAdmin pa : projectAdmins) {
                int userId = pa.getUserId();
                if (users.contains(userId)) {
                    User user = userDao.get(userId);
                    return user;
                }
            }
        }
        return null;
    }

    public void sendNotification(LoginUser loginUser, Project project, int userId, int action) {
        if (action != ControlPanelConstants.PROJECT_SECONDARY_OWNER_ACTION_ADD && action != ControlPanelConstants.PROJECT_SECONDARY_OWNER_ACTION_DEL) {
            return;
        }

        User user = userDao.get(userId);
        if (user == null) {
            return;
        }

        Map<String, String> tokens = new HashMap<String, String>();
        tokens.put(Constants.NOTIFICATION_TOKEN_ADDER_NAME, loginUser.getFirstname() + " " + loginUser.getLastname());
        tokens.put(Constants.NOTIFICATION_TOKEN_EMAIL, user.getEmail());
        tokens.put(Constants.NOTIFICATION_TOKEN_FIRST_NAME, user.getFirstName());
        tokens.put(Constants.NOTIFICATION_TOKEN_FULL_NAME, user.getFirstName() + " " + user.getLastName());
        tokens.put(Constants.NOTIFICATION_TOKEN_LAST_NAME, user.getLastName());
        tokens.put(Constants.NOTIFICATION_TOKEN_PROJECT_NAME, project.getCodeName());
        tokens.put(Constants.NOTIFICATION_TOKEN_USER_NAME, user.getUsername());
        // tokens.put(Constants.NOTIFICATION_TOKEN_NOTE, note);
        String notificationType = (action == ControlPanelConstants.PROJECT_SECONDARY_OWNER_ACTION_ADD
                ? Constants.NOTIFICATION_TYPE_NOTIFY_ADD_USER_PA
                : Constants.NOTIFICATION_TYPE_NOTIFY_DEL_USER_PA);
        NotificationView notification = notificationItemService.getDefaultNotificationView(notificationType, user.getLanguageId(), tokens);
        mailService.addSystemMail(user.getEmail(), notification.getSubject(), notification.getBody());
    }

    public String checkContributorForRole(Project project, int roleId) {
        Role role = roleDao.get(roleId);
        if (role == null) {
            return null;
        }

        List<ProjectMembership> pms = projectService.getProjectMembershipsByProjectAndRole(project.getId(), roleId);
        if (pms != null && pms.size() > 0) {
            return role.getName();
        }

        return null;
    }

    public int initializeHorse(int productId, int targetId) {
        //call init_horse procedure
        int rt = productDao.call(Constants.PROCEDURE_INIT_HORSE, productId, targetId);
        if (rt != 0) {
            log.error("call init_horse procedure return " + rt);
        }
        return rt;
    }

    public int deleteTarget(int projectId, int targetId) {
        int rt = productDao.call(Constants.PROCEDURE_DEL_TARGET, projectId, targetId);
        if (rt != 0) {
            log.error("Call delete_target procedure return " + rt);
        }
        return rt;
    }

    public int deleteProduct(int productId) {
        int rt = productDao.call(Constants.PROCEDURE_DEL_PRODUCT, productId);
        if (rt != 0) {
            log.error("Call delete_product procedure return " + rt);
        }
        return rt;
    }

    
}
