/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.po.User;
import com.ocs.indaba.service.UserService;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
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
public class LogoutAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(LoginAction.class);
    private UserService userSrvc = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.preprocess(mapping, request);
        // now that we don't leverage struts ActionForm, we need to get the
        // parameters by ourselves
        logger.debug("User logout: " + username);
        if (uid > 0) {
            User user = userSrvc.getUser(uid);
            if (user != null) {
                user.setLastLogoutTime(new Date());
                userSrvc.updateUser(user);
            }
            request.setAttribute(ATTR_USERNAME, null);
            request.setAttribute(ATTR_USERID, null);

        }
        // To effectively reduce the database load, it is a better way to do
        // some pre-checks of the validation of parameters. Note, generally
        // the validation should be handled by javascript in front web page.
        //session = request.getSession(true);
        session.invalidate();
        //String retUrl = request.getParameter(ATTR_RET_URL);
        //if (StringUtils.isEmpty(retUrl)) {
        return mapping.findForward(FWD_SUCCESS);
        //} else {
        //retUrl = URLDecoder.decode(retUrl, "UTF-8");
        //ActionForward actionFwd = new ActionForward(retUrl);
        //return actionFwd;
        // }
    }

    @Autowired
    public void setUserSrvc(UserService userSrvc) {
        this.userSrvc = userSrvc;
    }
}
