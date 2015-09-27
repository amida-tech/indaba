/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.service.UserService;
import com.ocs.indaba.util.CookieUtils;
import com.ocs.indaba.vo.LoginUser;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author menglong
 */
public class PeopleAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(PeopleAction.class);
    private static final String FWD_PEOPLE = "people";
    private static final String ATTR_ALL_USERS = "allUsers";
    private static final String ATTR_TEAMS = "teams";
    private static final String ATTR_SHOULD_KNOW_PEOPLES = "shouldKnowPeoples";
    private UserService userService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response);

        request.setAttribute(Constants.COOKIE_ACTIVE_TAB, Constants.TAB_PEOPLE);
        CookieUtils.setCookie(response, Constants.COOKIE_ACTIVE_TAB, Constants.TAB_PEOPLE);

        super.setFilters(request, loginUser.getPrjid());
        //Integer userIdInt = uid;
        //set user's team info;
        request.setAttribute(ATTR_TEAMS, teamService.getTeamsCanSee(loginUser.getPrjid(), loginUser.getUid()));
        //set should know people (in project contact);
        request.setAttribute(ATTR_SHOULD_KNOW_PEOPLES, userService.getUserByProjectContact(loginUser.getPrjid(), loginUser.getUid()));
        
        //set all user info;
        request.setAttribute(ATTR_ALL_USERS, userService.getAllUserInfoListByProjectId(loginUser.getPrjid()));
        return mapping.findForward(FWD_PEOPLE);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
