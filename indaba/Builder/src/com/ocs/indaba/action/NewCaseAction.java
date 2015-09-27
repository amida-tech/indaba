/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
*/

package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.service.AccessPermissionService;
import com.ocs.indaba.service.AccessPermissionService.PermissionCtx;
import com.ocs.indaba.service.CTagService;
import com.ocs.indaba.service.JournalService;
import com.ocs.indaba.service.UserService;
import com.ocs.indaba.vo.CasePriority;
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
public class NewCaseAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(NewCaseAction.class);

    private static final String FWD_NEW_CASE = "newcase";
    private static final String ATTR_ALL_USERS = "allUsers";
    private static final String ATTR_ALL_CTAGS = "allCtags";
    private static final String ATTR_ALL_CASE_PRIORITY = "allCasePriority";
    private static final String ATTR_ALL_CONTENTS = "allContents";
    private static final String ATTR_USERNAME_PAGE = "username";
    private static final String ATTR_USER_PAGE = "currUser";
    private static final String ATTR_DEFAULT_ASSIGN_USERID = "defaultAssignUserId";
    //private static final String ATTR_ACTION = "action";

    private UserService userService;
    private CTagService ctagService;
    private JournalService journalService = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response);

        AccessPermissionService srvc = this.accessPermissionService;
        PermissionCtx ctx = srvc.getPermissionCtx(loginUser.getPrjid(), loginUser.getUid());
        boolean canEditCase = srvc.checkProjectPermission(ctx, "edit any cases") || srvc.checkProjectPermission(ctx, "manage all cases");
        boolean canCloseAllCases = srvc.checkProjectPermission(ctx, "close all cases") || srvc.checkProjectPermission(ctx, "manage all cases");
        boolean canBlockProgress = srvc.checkProjectPermission(ctx, "block content progress") || srvc.checkProjectPermission(ctx, "manage all cases");
        boolean canAssignAnyUser = srvc.checkProjectPermission(ctx, "assign any user to cases") || srvc.checkProjectPermission(ctx, "manage all cases");
        boolean canAttachAnyContent = srvc.checkProjectPermission(ctx, "attach any content to cases") || srvc.checkProjectPermission(ctx, "manage all cases");
        boolean canAccessStaffNote = srvc.checkProjectPermission(ctx, "access case staff notes") || srvc.checkProjectPermission(ctx, "manage all cases");
        boolean canManageUsers = srvc.checkProjectPermission(ctx, "manage all users");

        String username = (String)request.getAttribute(Constants.ATTR_USERNAME);
        //get alert info;
        String alertInfo = (String)request.getAttribute("alertInfo");
        request.setAttribute("alertInfo", alertInfo);
        request.setAttribute(ATTR_USERNAME_PAGE, username);
        request.setAttribute(ATTR_USER_PAGE, userService.getUser(username));
        request.setAttribute(ATTR_DEFAULT_ASSIGN_USERID, prjService.getProjectById(loginUser.getPrjid()).getOwnerUserId());
        
        //request.setAttribute(ATTR_USERNAME_PAGE, username);
        if (canAssignAnyUser) request.setAttribute(ATTR_ALL_USERS, userService.getAllUsersOrderByLastname(loginUser.getPrjid(), loginUser.getUid()));
        else request.setAttribute(ATTR_ALL_USERS, userService.getDefaultAssignedUserOrderByLastname(loginUser.getPrjid(), loginUser.getUid(), 0));

        request.setAttribute(ATTR_ALL_CTAGS, ctagService.getAllCTagsOrderByTerm());

        if (canAttachAnyContent) request.setAttribute(ATTR_ALL_CONTENTS, journalService.getAllStartedContentHeaderOrderByTitle(loginUser.getPrjid()));
        else request.setAttribute(ATTR_ALL_CONTENTS, journalService.getDefaultContentHeaderOrderByTitle(loginUser.getUid()));
        
        request.setAttribute(ATTR_ALL_CASE_PRIORITY, CasePriority.getAllPriority());

        request.setAttribute("canEditCase", canEditCase);
        request.setAttribute("canCloseAllCases", canCloseAllCases);
        request.setAttribute("canBlockProgress", canBlockProgress);
        request.setAttribute("canAssignAnyUser", canAssignAnyUser);
        request.setAttribute("canAttachAnyContent", canAttachAnyContent);
        request.setAttribute("canAccessStaffNote", canAccessStaffNote);
        request.setAttribute("canManageUsers", canManageUsers);
        
        request.setAttribute(ATTR_ACTION, "new");
        return mapping.findForward(FWD_NEW_CASE);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setCtagService(CTagService ctagService) {
        this.ctagService = ctagService;
    }

    @Autowired
    public void setJournalService(JournalService journalService) {
        this.journalService = journalService;
    }
}
