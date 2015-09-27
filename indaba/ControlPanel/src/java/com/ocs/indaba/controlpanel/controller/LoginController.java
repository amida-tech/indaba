/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller;

import com.ocs.indaba.common.Messages;
import com.ocs.indaba.controlpanel.common.ControlPanelConstants;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.MailbatchService;
import com.ocs.indaba.service.NotificationItemService;
import com.ocs.indaba.vo.NotificationView;
import com.ocs.util.CookieUtils;
import com.ocs.util.StringUtils;
import com.ocs.util.UniqueTokenGenerator;
import com.ocs.util.UserSessionManager;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
@Results({
    @Result(name = "success", type = "redirectAction", params = {"actionName", "proj/projects"})
//@Result(name = "success", type = "redirectAction", params = {"actionName", "organizations"})
})
public class LoginController extends BaseController {

    private static final long serialVersionUID = 7550588769978806963L;
    private static final Logger logger = Logger.getLogger(LoginController.class);
    protected static final String PARAM_EXPIRED = "expired";
    protected static final String PARAM_REDIRECT = "redirect";
    protected static final String PARAM_RETURL = "returl";
    protected static final String ATTR_ERRMSG = "errMsg";

    @Autowired
    private NotificationItemService notificationItemService;

    @Autowired
    private MailbatchService mailService;

    public String index() {
        if (StringUtils.str2boolean(request.getParameter(PARAM_EXPIRED))) {
            request.setAttribute(ATTR_ERRMSG, Messages.getInstance().getMessage(ControlPanelMessages.KEY_SESSION_TIMEOUT, ControlPanelConstants.DEFAULT_LANGUAGE_ID));
        }
        if (StringUtils.str2boolean(request.getParameter(PARAM_REDIRECT))) {
            response.setHeader("Redirect-Reason", "no-auth");
        }
        request.setAttribute(PARAM_RETURL, request.getParameter(PARAM_RETURL));
        return RESULT_INDEX;
    }
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String create() {
        // To effectively reduce the database load, it is a better way to do
        // some pre-checks of the validation of parameters. Note, generally
        // the validation should be handled by javascript in front web page.
        boolean valid = userSrvc.authenticateAdministrator(username, password);
        if (valid) {
            User user = userSrvc.getUser(username);

            /**
             * Check if the user is still in signing status(if token is existed in cookie).
             * If so, clear the token in server token pool.
             */
            String token = getToken(request);

            UserSessionManager userSessionMgr = UserSessionManager.getInstance();
            // clear token in case the user's old token is existed in browser's cookie
            if (!StringUtils.isEmpty(token) && userSessionMgr.getLoginUser(token) != null) {
                logger.debug("Removing old token " + token);
                userSessionMgr.removeUserToken(token);
            }

            token = UniqueTokenGenerator.getInstance().generateUserSessionToken(user.getId());
            userSessionMgr.addUserToken(user.getId(), token);
            CookieUtils.setTokenCookie(response, token);
            request.setAttribute(CookieUtils.getTokenCookieName(), token);
            user.setLastLoginTime(new Date());
            userSrvc.updateUser(user);
            logger.debug("Authentication success.");
            String returl = request.getParameter(PARAM_RETURL);
            if (StringUtils.isEmpty(returl)) {
                return RESULT_SUCCESS;
            } else {
                logger.debug("Redirect to: " + returl);
                try {
                    response.sendRedirect(URLDecoder.decode(returl, "UTF-8"));
                } catch (IOException ex) {
                    logger.error("Fail to send redirect.", ex);
                    return RESULT_SUCCESS;
                }
                return RESULT_EMPTY;
            }
        } else {
            // not allowed
            request.setAttribute("errMsg", ControlPanelMessages.NO_SYSTEM_ACCESS);
            logger.debug("Authentication failure.");
//            String retUrl = (String) request.getAttribute(ATTR_RET_URL);
//            if (retUrl != null) {
//                ActionForward actionFwd = new ActionForward(retUrl);
//                return actionFwd;
//            } else {
//                return mapping.findForward(FWD_FAILURE);
//            }
            return RESULT_INDEX;
        }
    }

    private String getToken(HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        String token = null;   // must initialize to null  YC 12/27/2011
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (CookieUtils.getTokenCookieName().equals(c.getName())) {
                    token = c.getValue();
                }
            }
        }
        return token;
    }

    public String logout() {
        String token = getToken(request);
        UserSessionManager.IdentityUser iu = UserSessionManager.getInstance().getLoginUser(token);
        if (iu != null) {
            request.getSession(true).invalidate();
            logger.debug("Clear user token from session pool: [" + iu.getUserId() + "," + token + "]");
            CookieUtils.clearCookies(request, response);
            UserSessionManager.getInstance().removeUserToken(token);
        }

        return "index";
    }


    public String forgetPassword() {
        User user = userSrvc.getUser(username);
        if (user == null) {
            this.sendResponseMessage("Invalid user name.");
        } else {
            // email the password to user
            Map<String, String> tokens = new HashMap<String, String>();
        
            tokens.put("username", user.getUsername());
            tokens.put("password", user.getPassword());

            String notificationType = com.ocs.indaba.common.Constants.NOTIFICATION_TYPE_SYS_USER_PASSWORD;
            NotificationView notification = notificationItemService.getDefaultNotificationView(notificationType, user.getLanguageId(), tokens);
            mailService.addSystemMail(user.getEmail(), notification.getSubject(), notification.getBody());
            this.sendResponseMessage("Your password has been emailed to you.");
        }

        return RESULT_EMPTY;
    }
}
