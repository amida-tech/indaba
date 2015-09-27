/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.service.AccessPermissionService;
import com.ocs.indaba.service.AccessPermissionService.PermissionCtx;
import com.ocs.indaba.service.CTagService;
import com.ocs.indaba.service.CaseService;
import com.ocs.indaba.service.JournalService;
import com.ocs.indaba.service.UserService;
import com.ocs.indaba.util.CookieUtils;
import com.ocs.indaba.vo.CaseInfo;
import com.ocs.indaba.vo.CasePriority;
import com.ocs.indaba.vo.CaseStatus;
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
public class CasesDetailAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(CasesDetailAction.class);
    //foward
    private static final String FWD_CASESDETAIL = "casedetail";
    //private static final String FWD_CASEDETAILFAILED = "casedetailfailed";
    //parameter
    private static final String PARAM_CASEID = "caseid";
    //return value
    private static final String ATTR_CASE_INFO = "caseinfo";
    private static final String ATTR_ALL_CASE_STATUS = "allCaseStatus";
    private static final String ATTR_ALL_CASE_PRIORITY = "allCasePriority";
    private static final String ATTR_ALL_USERS = "allUsers";
    private static final String ATTR_ALL_CONTENTS = "allContents";
    private static final String ATTR_ALL_CTAGS = "allCtags";
    private CaseService caseService;
    private UserService userService;
    private JournalService journalService;
    private CTagService ctagService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response);
        
        String caseIdInput = request.getParameter(PARAM_CASEID);
        int caseId = 0;
        if (caseIdInput == null || caseIdInput.length() <= 0) {
            caseId = (Integer) request.getAttribute(Constants.COOKIE_CASE_ID);
            CookieUtils.clearCookie(request, response, Constants.COOKIE_CASE_ID);
        } else {
            caseId = Integer.valueOf(caseIdInput);
        }

        AccessPermissionService srvc = this.accessPermissionService;
        PermissionCtx ctx = srvc.getPermissionCtx(loginUser.getPrjid(), loginUser.getUid());
        boolean canEditCase = srvc.checkProjectPermission(ctx, "edit any cases") || srvc.checkProjectPermission(ctx, "manage all cases");
        boolean canCloseAllCases = srvc.checkProjectPermission(ctx, "close all cases") || srvc.checkProjectPermission(ctx, "manage all cases");
        boolean canBlockProgress = srvc.checkProjectPermission(ctx, "block content progress") || srvc.checkProjectPermission(ctx, "manage all cases");
        boolean canAssignAnyUser = srvc.checkProjectPermission(ctx, "assign any user to cases") || srvc.checkProjectPermission(ctx, "manage all cases");
        boolean canAttachAnyContent = srvc.checkProjectPermission(ctx, "attach any content to cases") || srvc.checkProjectPermission(ctx, "manage all cases");
        boolean canAccessStaffNote = srvc.checkProjectPermission(ctx, "access case staff notes") || srvc.checkProjectPermission(ctx, "manage all cases");
        boolean canManageUsers = srvc.checkProjectPermission(ctx, "manage all users");

        CaseInfo caseInfo = caseService.getCaseById(loginUser.getPrjid(), loginUser.getUid(), caseId);

        request.setAttribute(ATTR_ALL_CASE_PRIORITY, CasePriority.getAllPriority());
        request.setAttribute(ATTR_ALL_CASE_STATUS, CaseStatus.getAllStatus());

        if (canAssignAnyUser) {
            request.setAttribute(ATTR_ALL_USERS, userService.getAllUsersOrderByLastname(loginUser.getPrjid(), loginUser.getUid()));
        } else {
            request.setAttribute(ATTR_ALL_USERS, userService.getDefaultAssignedUserOrderByLastname(loginUser.getPrjid(), loginUser.getUid(), caseInfo.getAssignedUserId()));
        }

        if (canAttachAnyContent) {
            request.setAttribute(ATTR_ALL_CONTENTS, journalService.getAllStartedContentHeaderOrderByTitle(loginUser.getPrjid()));
        } else {
            request.setAttribute(ATTR_ALL_CONTENTS, journalService.getDefaultContentHeaderOrderByTitle(loginUser.getUid()));
        }

        request.setAttribute(ATTR_CASE_INFO, caseInfo);
        request.setAttribute(ATTR_ALL_CTAGS, ctagService.getAllCTagsOrderByTerm());

        request.setAttribute("canEditCase", canEditCase);
        request.setAttribute("canCloseAllCases", canCloseAllCases);
        request.setAttribute("canBlockProgress", canBlockProgress);
        request.setAttribute("canAssignAnyUser", canAssignAnyUser);
        request.setAttribute("canAttachAnyContent", canAttachAnyContent);
        request.setAttribute("canAccessStaffNote", canAccessStaffNote);
        request.setAttribute("canManageUsers", canManageUsers);

        return mapping.findForward(FWD_CASESDETAIL);
    }

    @Autowired
    public void setCaseService(CaseService caseService) {
        this.caseService = caseService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setJournalService(JournalService journalService) {
        this.journalService = journalService;
    }

    @Autowired
    public void setCtagService(CTagService ctagService) {
        this.ctagService = ctagService;
    }
}
