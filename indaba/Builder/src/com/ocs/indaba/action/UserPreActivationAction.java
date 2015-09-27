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
public class UserPreActivationAction extends BaseAction {
    
    private static final Logger logger = Logger.getLogger(UserPreActivationAction.class);
    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";
    private static final String ATTR_USER = "user";
    private static final String ATTR_USERNAME = "username";
    private static final String ATTR_PASSWORD = "password";
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        String username = request.getParameter(PARAM_USERNAME);
        String password = request.getParameter(PARAM_PASSWORD);
        logger.debug("User Activate: " + username);
        
        boolean valid = userSrvc.authenticate(username, password, Constants.USER_STATUS_INACTIVE);
        if(valid) {
            User user = userSrvc.getUser(username);
            request.setAttribute(ATTR_USER, user);
            request.setAttribute(ATTR_USERNAME, user.getUsername());
            request.setAttribute(ATTR_PASSWORD, user.getPassword());
            return mapping.findForward(FWD_SUCCESS);
        }
        else {
            
        }
        return mapping.findForward(FWD_FAILURE);
    }
}
