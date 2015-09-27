/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import static com.ocs.indaba.action.BaseAction.PARAM_HORSE_ID;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.service.SurveyAnswerService;
import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.service.ViewPermissionService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.SurveyAnswerOriginalView;
import com.ocs.indaba.vo.UserDisplay;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author luwb
 */
public class SurveyAnswerOriginalAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SurveyAnswerOriginalAction.class);
    private SurveyService surveyService;
    private SurveyAnswerService surveyAnswerService;
    private ViewPermissionService viewPermissionService;
    private static final String FWD_SURVEY_ANSWER = "success";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response, true, true);

        //session.setAttribute(ATTR_ACTIVE, TAB_YOURCONTENT);
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), 0);
        request.setAttribute(ATTR_HORSE_ID, horseId);

        int surveyAnswerId = StringUtils.str2int(request.getParameter(PARAM_ANSWER_ID), 0);
        request.setAttribute(ATTR_ANSWER_ID, surveyAnswerId);

        int surveyQuestionId = StringUtils.str2int(request.getParameter(PARAM_QUESTION_ID), 0);
        request.setAttribute(ATTR_QUESTION_ID, surveyQuestionId);

        if (horseId <= 0) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "horseid"));
            return mapping.findForward(FWD_ERROR);
        }
        if (surveyAnswerId <= 0) {
            if (surveyQuestionId <= 0) {
                request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "answerid' or 'questionid"));
                return mapping.findForward(FWD_ERROR);
            }
            surveyAnswerId = surveyService.getSurveyAnswerId(surveyQuestionId, horseId, loginUser.getUid());
            if (surveyAnswerId <= 0) {
                request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "answerid"));
                return mapping.findForward(FWD_ERROR);
            }
        }
        SurveyAnswerOriginalView view = surveyAnswerService.getSurveyAnswerOriginalView(horseId, surveyAnswerId);
        if (view == null) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_DATA_NOTEXISTED, "SurveyAnswer", surveyAnswerId));
            return mapping.findForward(FWD_ERROR);
        }

        // determine user name
        UserDisplay userDisplay = (view.getUser() == null) ? null : viewPermissionService.getUserDisplayOfProject(loginUser.getPrjid(),
                Constants.DEFAULT_VIEW_MATRIX_ID, loginUser.getUid(), view.getUser().getId());

        if (userDisplay == null) {
            view.setUserName("NA");
        } else {
            view.setUserName(userDisplay.getDisplayUsername());
        }

        request.setAttribute("view", view);
        request.setAttribute(ATTR_HORSE_ID, horseId);

        request.setAttribute("attachments", surveyAnswerService.getSurveyAnswerAttachmentsVersion(view.getContentVersionId(), view.getSurveyAnswerId()));

        return mapping.findForward(FWD_SURVEY_ANSWER);
    }

    @Autowired
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @Autowired
    public void setSurveyAnswerService(SurveyAnswerService surveyAnswerService) {
        this.surveyAnswerService = surveyAnswerService;
    }

    @Autowired
    public void setViewPermissionService(ViewPermissionService viewPermissionService) {
        this.viewPermissionService = viewPermissionService;
    }
}
