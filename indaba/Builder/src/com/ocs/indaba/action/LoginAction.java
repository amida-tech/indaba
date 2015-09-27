/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
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
 * Login Action. To help handle the user login request. <br/>
 * 
 * Actually, although struts already provides an useful LoginForm component, in
 * this example we don't plan to leverage it. In a general use, we directly get
 * the request parameters fromHttpServletRequest.
 * 
 * @author Jeff
 * 
 */
public class LoginAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(LoginAction.class);
    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // now that we don't leverage struts ActionForm, we need to get the
        // parameters by ourselves
        String username = request.getParameter(PARAM_USERNAME);
        String password = request.getParameter(PARAM_PASSWORD);
        logger.debug("User login: " + username);

        // To effectively reduce the database load, it is a better way to do
        // some pre-checks of the validation of parameters. Note, generally
        // the validation should be handled by javascript in front web page.
        boolean valid = userSrvc.authenticate(username, password);

        if (valid) {
            User user = userSrvc.getUser(username);
            // TODO: do some extra works, e.g. record user login status(by
            // cookie, session or rewrite url)
            request.setAttribute(PARAM_USERNAME, username);

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
            return new ActionRedirect(mapping.findForward(FWD_SUCCESS));
            //return mapping.findForward(FWD_SUCCESS);
        } else {
            // not allowed
            request.setAttribute(ATTR_ERRMSG, getResources(request).getMessage(
                    Messages.KEY_COMMON_ERR_INVALID_USER));
            logger.debug("Authentication failure.");
            String retUrl = (String) request.getAttribute(ATTR_RET_URL);
            if (retUrl != null) {
                ActionForward actionFwd = new ActionForward(retUrl);
                return actionFwd;
            } else {
                return mapping.findForward(FWD_FAILURE);
            }
        }
    }
}
