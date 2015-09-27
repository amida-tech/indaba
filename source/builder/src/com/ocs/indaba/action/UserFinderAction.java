/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.Role;
import com.ocs.indaba.po.Userfinder;
import com.ocs.indaba.service.ProjectService;
import com.ocs.indaba.service.RoleService;
import com.ocs.indaba.service.UserFinderService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ocs.util.StringUtils;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Login Action. To help handle the user login request. <br/>
 * 
 * Actually, although struts already provides an useful LoginForm component, in
 * this example we don't plan to leverage it. In a general use, we directly get
 * the request parameters fromHttpServletRequest.
 * 
 * @author Jeff
 * 
 */
public class UserFinderAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(UserFinderAction.class);
    private ProjectService projSrvc = null;
    private RoleService roleSrvc = null;
    private UserFinderService userfinderSrvc = null;
    private static final String ATTR_PROJECTS = "projects";
    private static final String ATTR_ROLES = "roles";
    private static final String ATTR_USERS = "users";
    private static final String ATTR_USER_TRIGGERS = "userTriggers";
    private static final String FWD_USERFINDER = "userfinder";
    private static final String PARAM_ACTION = "action";
    private static final String PARAM_PROJECT_ID = "prjid";
    private static final String PARAM_DESCRIPTION = "desc";
    private static final String PARAM_ROLE_ID = "roleid";
    private static final String PARAM_ASSIGNED_USER_ID = "assignedUserId";
    private static final String PARAM_CASE_SUBJECT = "caseSubject";
    private static final String PARAM_CASE_BODY = "caseBody";
    private static final String PARAM_CASE_PRIORITY = "casePriority";
    private static final String PARAM_ATTACH_USER = "attachUser";
    private static final String PARAM_ATTACH_CONTENT = "attachContent";
    private static final String PARAM_STATUS = "status";
    private static final String ACTION_GET_ROLE = "getrole";
    private static final String ACTION_ADD_TRIGGER = "add";
    private static final String JSON_KEY_RET = "ret";
    //private static final String JSON_KEY_DATA = "data";
    private static final String JSON_KEY_ROLE_IDS = "idList";
    private static final String JSON_KEY_ROLE_NAMES = "nameList";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String action = request.getParameter(PARAM_ACTION);
        if (ACTION_ADD_TRIGGER.equals(action)) {
            addUserTrigger(request);
        } else if (ACTION_GET_ROLE.equals(action)) {
            int prjId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
            List<Role> roles = roleSrvc.getAllRoles(prjId);
            JSONObject root = new JSONObject();
            if (roles != null && roles.size() > 0) {
                JSONArray idList = new JSONArray();
                JSONArray nameList = new JSONArray();
                for (Role role : roles) {
                    idList.add(role.getId());
                    nameList.add(role.getName());
                }
                root.put(JSON_KEY_ROLE_IDS, idList);
                root.put(JSON_KEY_ROLE_NAMES, nameList);
            }
            root.put(JSON_KEY_RET, 0);
            super.writeMsgUTF8(response, root.toJSONString());
            return null;
        } else {
            request.setAttribute(ATTR_USER_TRIGGERS, userfinderSrvc.getAllUserfinders());
            request.setAttribute(ATTR_PROJECTS, projSrvc.getAllProjects());
            request.setAttribute(ATTR_USERS, userSrvc.getSiteAdminUsers());
        }
        return mapping.findForward(FWD_USERFINDER);
    }

    private void addUserTrigger(HttpServletRequest request) {
        String desc = request.getParameter(PARAM_DESCRIPTION);
        int projectId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        int roleId = StringUtils.str2int(request.getParameter(PARAM_ROLE_ID));
        int assignedUserId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNED_USER_ID));
        String caseSubject = request.getParameter(PARAM_CASE_SUBJECT);
        String caseBody = request.getParameter(PARAM_CASE_BODY);
        int casePriority = StringUtils.str2int(request.getParameter(PARAM_CASE_PRIORITY), Constants.CASE_PRIORITY_HIGH);
        boolean attachUser = StringUtils.str2boolean(request.getParameter(PARAM_ATTACH_USER), true);
        boolean attachContent = StringUtils.str2boolean(request.getParameter(PARAM_ATTACH_CONTENT), true);
        int status = StringUtils.str2int(request.getParameter(PARAM_STATUS), Constants.USER_STATUS_ACTIVE);
        Userfinder userfinder = new Userfinder();
        userfinder.setDescription(desc);
        userfinder.setProjectId(projectId);
        userfinder.setRoleId(roleId);
        userfinder.setAssignedUserId(assignedUserId);
        userfinder.setCaseSubject(caseSubject);
        userfinder.setCaseBody(caseBody);
        userfinder.setCasePriority((short) casePriority);
        userfinder.setAttachUser(attachUser);
        userfinder.setAttachContent(attachContent);
        userfinder.setStatus((short) status);
        Date now = new Date();
        userfinder.setCreateTime(now);
        userfinder.setLastUpdateTime(now);
        userfinderSrvc.addUserFinder(userfinder);
    }

    @Autowired
    public void setProjectSrvc(ProjectService projSrvc) {
        this.projSrvc = projSrvc;
    }

    @Autowired
    public void setRoleSrvc(RoleService roleSrvc) {
        this.roleSrvc = roleSrvc;
    }

    @Autowired
    public void setUserFinderSrvc(UserFinderService userfinderSrvc) {
        this.userfinderSrvc = userfinderSrvc;
    }
}
