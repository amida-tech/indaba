/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.action;

import com.google.gson.Gson;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.service.CaseService;
import com.ocs.util.StringUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeanbone
 */
public class CaseMessageBoardAction extends BaseAction {
    private static final String PARAM_ACTION = "action";
    private static final String PARAM_CASE_ID = "caseid";
    private CaseService caseService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        preprocess(mapping, request, response);

        String action = request.getParameter(PARAM_ACTION);
        if ("getCaseUserMsgboardId".equalsIgnoreCase(action)) {
            return getCaseUserMsgBoardId(mapping, form, request, response);
        } else {
            return getCaseStaffMsgBoardId(mapping, form, request, response);
        }
    }

    private ActionForward getCaseUserMsgBoardId(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        int caseId = StringUtils.str2int(request.getParameter(PARAM_CASE_ID), Constants.INVALID_INT_ID);

        response.getWriter().write(new Gson().toJson(caseService.getCaseUserMsgBoardId(caseId)));
        return null;
    }

    private ActionForward getCaseStaffMsgBoardId(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        int caseId = StringUtils.str2int(request.getParameter(PARAM_CASE_ID), Constants.INVALID_INT_ID);

        response.getWriter().write(new Gson().toJson(caseService.getCaseStaffMsgBoardId(caseId)));
        return null;
    }

    @Autowired
    public void setCaseService(CaseService caseService) {
        this.caseService = caseService;
    }
}
