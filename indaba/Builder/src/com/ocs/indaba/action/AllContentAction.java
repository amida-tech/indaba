/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.util.CookieUtils;
import com.ocs.indaba.vo.FilterForm;
import com.ocs.indaba.vo.LoginUser;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Jeff
 */
public class AllContentAction extends com.ocs.indaba.action.BaseAction {

    private static final Logger logger = Logger.getLogger(AllContentAction.class);
    private static final String PARAM_TARGET_IDS = "targetIds";
    private static final String PARAM_PRODUCT_IDS = "prodIds";

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        LoginUser loginUser = preprocess(mapping, request, response);
        
        // load filter data
        setFilters(request, loginUser.getPrjid());

        FilterForm filterData = extractFilterFormData(request);

        logger.debug(">>>>>>>>>>>>>>>> TargetIds: " + filterData.getTargetIds() + ", ProductIds: " + filterData.getProductIds() + ", Status: " + filterData.getStatus());
        CookieUtils.setCookie(response, Constants.COOKIE_ACTIVE_TAB, Constants.TAB_ALLCONTENT);
        request.setAttribute(Constants.COOKIE_ACTIVE_TAB, Constants.TAB_ALLCONTENT);
        List<Integer> targetList = filterData.getTargetIds();
        String targetIds = (targetList == null) ? "[]" : targetList.toString();
        request.setAttribute(PARAM_TARGET_IDS, targetIds);

        List<Integer> prodList = filterData.getProductIds();
        String prodIds = (prodList == null) ? "[]" : prodList.toString();
        request.setAttribute(PARAM_PRODUCT_IDS, prodIds);
        
        Project project = loginUser.getProject();
        request.setAttribute("reportUrl", (project != null) ? project.getReportUrl() : null);
        request.setAttribute("analyticsUrl", (project != null) ? project.getAnalyticsUrl() : null);

        //request.setAttribute(ATTR_ALL_CONTENT, horseService.getAllActiveHorses(uid, prjid, filterData.getTargetIds(), filterData.getProductIds(), filterData.getStatus()));

        return mapping.findForward(FWD_ALLCONTENT);
    }
}
