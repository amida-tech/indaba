/**
 * 
 */
package com.ocs.indaba.action.popup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.indaba.action.BaseAction;
import com.ocs.indaba.service.UserService;
import com.ocs.indaba.vo.LoginUser;

/**
 * @author Tiger Tang
 *
 */
public class UserListAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(UserListAction.class);
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LoginUser loginUser = preprocess(mapping, request, response);

        request.setAttribute("varName", request.getParameter("varName"));
        request.setAttribute("userList", userService.getActiveUsersInViewOfProject(loginUser.getPrjid(), loginUser.getUid()));

        return mapping.findForward(FWD_SUCCESS);
    }
}
