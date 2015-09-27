/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.ContentVersion;
import com.ocs.indaba.service.SurveyService;
import com.ocs.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class SurveyPreVersionDisplayAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SurveyPreVersionDisplayAction.class);
    //private SurveyCategoryService surveyCategoryService;
    //private static final String PARAM_SURVEY_CATEGORY_ID = "catid";
    //private static final String FWD_SURVEY_DISPLAY = "surveyDisplay";
    private SurveyService surveyService = null;

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
        int preVersionId = StringUtils.str2int(request.getParameter(PARAM_CONTENT_VERSION_ID));
        if (preVersionId <= 0) {
            ContentVersion cntVer = surveyService.getLastestContentVersionByHorseId(horseId);
            if (cntVer != null) {
                preVersionId = cntVer.getId();
            }
        }
        request.setAttribute(PARAM_CONTENT_VERSION_ID, preVersionId);

        return mapping.findForward(FWD_SUCCESS);
    }

    @Autowired
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }
}
