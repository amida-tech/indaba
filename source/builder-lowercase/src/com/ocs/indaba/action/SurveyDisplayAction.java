/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.util.StringUtils;
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
public class SurveyDisplayAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SurveyDisplayAction.class);
    //private SurveyCategoryService surveyCategoryService;
    //private static final String PARAM_SURVEY_CATEGORY_ID = "catid";
    //private static final String FWD_SURVEY_DISPLAY = "surveyDisplay";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        preprocess(mapping, request, response, true, true);
        
        int horseId = StringUtils.str2int(request.getParameter(ATTR_HORSE_ID), Constants.INVALID_INT_ID);
        request.setAttribute(ATTR_HORSE_ID, horseId);
        request.setAttribute(PARAM_ASSIGNID, request.getParameter(PARAM_ASSIGNID));
        request.setAttribute(ATTR_ACTION, getActionPath(request));

        return mapping.findForward(FWD_SUCCESS);
    }

    /*@Autowired
    public void setSurveyCategoryService(SurveyCategoryService surveyCategoryService) {
    this.surveyCategoryService = surveyCategoryService;
    }*/
}
