/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.User;
import com.ocs.indaba.util.CookieUtils;
import com.ocs.indaba.util.UserSessionManager;
import com.ocs.indaba.vo.LoginUser;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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
public class LogoutAction extends BaseAction {
    
    private static final Logger logger = Logger.getLogger(LogoutAction.class);
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        LoginUser loginUser = preprocess(mapping, request, response);
        // now that we don't leverage struts ActionForm, we need to get the
        // parameters by ourselves
        logger.debug("User logout: ");
        if (loginUser.getUid() > 0) {
            User user = userSrvc.getUser(loginUser.getUid());
            if (user != null) {
                user.setLastLogoutTime(new Date());
                userSrvc.updateUser(user);
            }
            // log event into database
            logEventByKey(loginUser.getUid(), Constants.DEFAULT_EVENTLOG_ID, Constants.EVENT_TYPE_LOGOUT, "User Logout");
            
        }
        // To effectively reduce the database load, it is a better way to do
        // some pre-checks of the validation of parameters. Note, generally
        // the validation should be handled by javascript in front web page.
        request.getSession(true).invalidate();
        logger.debug("Clear user token from session pool: [" + loginUser.getUid() + "," + loginUser.getToken() + "]");
        CookieUtils.clearCookies(request, response);
        UserSessionManager.getInstance().removeUserToken(loginUser.getToken());
        
        return mapping.findForward(FWD_SUCCESS);
    }
}
