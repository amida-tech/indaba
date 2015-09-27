/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.User;
import com.ocs.indaba.util.CookieUtils;
import com.ocs.util.StringUtils;
import com.ocs.indaba.util.UserSessionManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ocs.indaba.util.UserTokenGenerator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.Cookie;
import org.apache.struts.action.ActionRedirect;

/**
 *
 * @author rick
 */
public class UserActivationAction extends BaseAction {
    
    private static final Logger logger = Logger.getLogger(UserActivationAction.class);

    private static final String PARAM_NEW_PASSWORD = "newpwd";
    private static final String PARAM_CUR_PASSWORD = "curpwd";
    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";
    private static final String ATTR_UID = "targetUid=";
    private static final String REDIRECT_URL_PREFIX = "/profile.do?targetUid=";
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        String username = request.getParameter(PARAM_USERNAME);
        String curpwd = request.getParameter(PARAM_CUR_PASSWORD);
        if (! userSrvc.authenticate(username, curpwd, Constants.USER_STATUS_INACTIVE)) {
            return mapping.findForward(FWD_FAILURE);
        }
        String password = request.getParameter(PARAM_NEW_PASSWORD);
        logger.debug("User Activate: " + username);
        User user = userSrvc.getUser(username);
        user.setPassword(password);
        user.setStatus(Constants.USER_STATUS_ACTIVE);
        userSrvc.updateUser(user);

//        request.setAttribute(PARAM_USERNAME, user.getUsername());
//        request.setAttribute(PARAM_PASSWORD, user.getPassword());

        logger.debug("Account activated - redirect to login page");
        request.setAttribute("errmsg", "Your account has been activated. Please login.");
        request.setAttribute(ATTR_UID, user.getId());

         // get the default project
        int projectId = Constants.INVALID_INT_ID;
        String projectName = null;

        List<Project> projects = prjService.getProjects(user.getId());
        if (projects != null && projects.size() > 0) {
            projectId = user.getDefaultProjectId();
            for (Project project : projects) {
                if (projectId == project.getId()) {
                    projectName = project.getCodeName();
                    break;
                }
            }
            if (projectName == null) {
                projectId = projects.get(0).getId();
                projectName = projects.get(0).getCodeName();
                user.setDefaultProjectId(projectId);
            }
            projects.clear();
            projects = null;
        }
        if (projectName == null) {
//                projectName = getMessage(request, Messages.KEY_COMMON_MSG_NOPROJECT);
            projectName = "No Project";
        }

        /**
            * Check if the user is still in signing status(if token is existed in cookie).
            * If so, clear the token in server token pool.
            */
        Cookie cookies[] = request.getCookies();
        String token = null;   // must initialize to null  YC 12/27/2011
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (Constants.COOKIE_TOKEN.equals(c.getName())) {
                    token = c.getValue();
                }
            }
        }
        if (StringUtils.isEmpty(token)) {
            token = (String) request.getAttribute(Constants.COOKIE_TOKEN);
        }

        UserSessionManager userSessionMgr = UserSessionManager.getInstance();
        // clear token in case the user's old token is existed in browser's cookie
        if(!StringUtils.isEmpty(token) && userSessionMgr.getLoginUser(token) != null) {
            logger.debug("Removing old token " + token);
            userSessionMgr.removeUserToken(token); 
        }

        token = UserTokenGenerator.getInstance().generateToken(user.getId());
        userSessionMgr.addUserToken(user.getId(), token);
        CookieUtils.setCookie(response, Constants.COOKIE_TOKEN, token);
        request.setAttribute(Constants.COOKIE_TOKEN, token);
        // log event into database
        logEventByKey(user.getId(), Constants.DEFAULT_EVENTLOG_ID, Constants.EVENT_TYPE_LOGIN, "User Login");
        logger.debug("Authentication success.");
        user.setLastLoginTime(new Date());
        userSrvc.updateUser(user);

        return new ActionRedirect(REDIRECT_URL_PREFIX + user.getId());
    }
}
