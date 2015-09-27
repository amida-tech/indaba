/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.service.UserService;
import com.ocs.indaba.service.ViewPermissionService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.UserDisplay;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Tiger Tang
 */
public class NewMessageAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(NewMessageAction.class);
    private static final String PARAM_RECEIVER_ID = "receiverId";
    private static final String ATTR_SENDER = "sender";
    private static final String ATTR_RECEIVER = "receiver";
    private static final String FWD_NEWMESSAGE = "newmessage";
    private UserService userService;
    private ViewPermissionService viewPermissionService;
    
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setViewPermissionService(ViewPermissionService viewPermissionService) {
        this.viewPermissionService = viewPermissionService;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LoginUser loginUser = preprocess(mapping, request, response);
        
        int receiverId = StringUtils.str2int(request.getParameter(PARAM_RECEIVER_ID), Constants.INVALID_INT_ID);
        if (receiverId != Constants.INVALID_INT_ID) {
            UserDisplay u = viewPermissionService.getUserDisplayOfProject(loginUser.getPrjid(), Constants.DEFAULT_VIEW_MATRIX_ID, loginUser.getUid(), receiverId);
            request.setAttribute(ATTR_RECEIVER, u);
        }
        
        request.setAttribute(ATTR_SENDER, userService.getUser(loginUser.getUid()));
        
        request.setAttribute(Constants.ATTR_USERNAME, request.getAttribute(Constants.ATTR_USERNAME));
        logger.debug("NewMessageAction - Write new message request: [userid=" + loginUser.getUid() + "].");

        return mapping.findForward(FWD_NEWMESSAGE);
    }

}
