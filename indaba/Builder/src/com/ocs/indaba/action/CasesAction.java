/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
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
public class CasesAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(CasesAction.class);
    private static final String FWD_CASES = "cases";
    // private static final String ATTR_OPEN_CASE_LIST = "openCases";
    // private static final String ATTR_ALL_CASE_LIST = "allCases";
    private static final String ATTR_FILTER_CASE_LIST = "filterCases";
    private static final String ATTR_ALERT_INFO = "alertInfo";
    private CaseService caseService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response);

        request.setAttribute(Constants.COOKIE_ACTIVE_TAB, Constants.TAB_CASES);
        CookieUtils.setCookie(response, Constants.COOKIE_ACTIVE_TAB, Constants.TAB_CASES);
        // load filter data
        setFilters(request, loginUser.getPrjid());
        // FilterForm filterData = extractFilterFormData(request);

        request.setAttribute(ATTR_MY_CASES, caseService.getOpenCaseListByUserIdAndProjectId(loginUser.getUid(), loginUser.getPrjid()));

        //get open cases
        //request.setAttribute(ATTR_OPEN_CASE_LIST, caseService.getOpenCases());
        //get all cases
        //request.setAttribute(ATTR_ALL_CASE_LIST, caseService.getAllCases());
        //set filter cases
        request.setAttribute(ATTR_FILTER_CASE_LIST, caseService.getAllCasesByProjectId(loginUser.getPrjid()));
        request.setAttribute(ATTR_ALERT_INFO, "");

        return mapping.findForward(FWD_CASES);
    }

    @Autowired
    public void setCaseService(CaseService caseService) {
        this.caseService = caseService;
    }
}
