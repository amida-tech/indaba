/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.service.CaseService;
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
public class AddCaseNotesAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(AddCaseNotesAction.class);
    private CaseService caseService;
    //foward
    private static final String FWD_ADD_CASE_NOTE = "addCaseNoteSuccess";
    private static final String ATTR_ALERT_INFO = "alertInfo";
    

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response);

        String type = request.getParameter("type");
        String userNote = request.getParameter("userNote");
        String staffNote = request.getParameter("staffNote");
        String userMsgBoardIdStr = request.getParameter("userMsgBoardId");
        String staffMsgBoardIdStr = request.getParameter("staffMsgBoardId");
        Integer caseId = Integer.valueOf(request.getParameter("caseOldId"));

        request.setAttribute(Constants.COOKIE_CASE_ID, caseId);
        CookieUtils.setCookie(response, Constants.COOKIE_CASE_ID, caseId);
        
        Boolean result = false;

        if (type.equals(Constants.CASE_NOTE_TYPE_USER)) {
            // add user note;
            Integer userMsgBoardId;
            if (userMsgBoardIdStr == null || userMsgBoardIdStr.length() <= 0) {
                userMsgBoardId = 0;
            } else {
                userMsgBoardId = Integer.valueOf(userMsgBoardIdStr);
            }
            result = caseService.addNoteInCase(
                    Long.valueOf(caseId), type, userNote, userMsgBoardId, loginUser.getUid());
        } else if (type.equals(Constants.CASE_NOTE_TYPE_STAFF)) {
            // add staff note;
            Integer staffMsgBoardId;
            if (staffMsgBoardIdStr == null || staffMsgBoardIdStr.length() <= 0) {
                staffMsgBoardId = 0;
            } else {
                staffMsgBoardId = Integer.valueOf(staffMsgBoardIdStr);
            }
            result = caseService.addNoteInCase(
                    Long.valueOf(caseId), type, staffNote, staffMsgBoardId, loginUser.getUid());
        } else {
            request.setAttribute(ATTR_ALERT_INFO, getMessage(request, Messages.KEY_COMMON_ALERT_ADDCASENOTE_FAILL, type));
            return mapping.findForward(FWD_ADD_CASE_NOTE);
        }

        if (!result) {
            request.setAttribute(ATTR_ALERT_INFO, getMessage(request, Messages.KEY_COMMON_ALERT_ADDCASENOTE_FAILL, type));
            return mapping.findForward(FWD_ADD_CASE_NOTE);
        }

        request.setAttribute(ATTR_ALERT_INFO, getMessage(request, Messages.KEY_COMMON_ALERT_ADDCASENOTE_SUCCESS));
        return mapping.findForward(FWD_ADD_CASE_NOTE);

    }

    @Autowired
    public void setCaseService(CaseService caseService) {
        this.caseService = caseService;
    }
}
