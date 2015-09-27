/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import static com.ocs.indaba.action.BaseAction.ATTR_ACTION;
import static com.ocs.indaba.action.BaseAction.ATTR_QUESTION_ID;
import static com.ocs.indaba.action.BaseAction.PARAM_ANSWER_ID;
import static com.ocs.indaba.action.BaseAction.PARAM_QUESTION_ID;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.Target;
import com.ocs.indaba.po.Horse;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.service.HorseService;
import com.ocs.indaba.service.SurveyAnswerService;
import com.ocs.indaba.service.SurveyCategoryService;
import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.SurveyAnswerView;
import com.ocs.indaba.vo.SurveyQuestionUrlView;
import com.ocs.util.StringUtils;
import java.net.URLEncoder;
import java.util.List;
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
public class SurveyAnswerDisplayAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SurveyAnswerDisplayAction.class);
    private static final String FWD_SURVEY_ANSWER = "success";
    private static final String ATTR_CONTENT_VERSION = "contentVersion";
    private static final String ATTR_CONTENT_VERSION_ID = "contentVersionId";
    private HorseService horseService;
    private SurveyService surveyService;
    private SurveyAnswerService surveyAnswerService;
    private SurveyCategoryService surveyCategoryService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response, true, true);
        setRequestUrl(request);

        // call serveyService functions
        String strHorseId = request.getParameter(PARAM_HORSE_ID);
        int horseId = NumberUtils.toInt(strHorseId, 0);
        request.setAttribute(ATTR_HORSE_ID, horseId);

        int surveyAnswerId = StringUtils.str2int(request.getParameter(PARAM_ANSWER_ID), 0);
        request.setAttribute(ATTR_ANSWER_ID, surveyAnswerId);

        int surveyQuestionId = StringUtils.str2int(request.getParameter(PARAM_QUESTION_ID), 0);
        request.setAttribute(ATTR_QUESTION_ID, surveyQuestionId);

        if (horseId <= 0) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "horseid"));
            return mapping.findForward(FWD_ERROR);
        }

        int targetId = NumberUtils.toInt(request.getParameter("targetid"), 0);
        if (targetId > 0) {
            Horse horse = horseService.getHorseById(horseId);
            horse = horseService.getHorseByProductAndTarget(horse.getProductId(), targetId);
            horseId = horse.getId();
            request.setAttribute("contentOnly", true);
            request.setAttribute("targetname", request.getParameter("targetname"));
        } else {
            request.setAttribute("contentOnly", false);
        }
        int answerType = -1;
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
        SurveyIndicator si = surveyService.getSurveyIndicatorByQuestionId(surveyQuestionId);
        answerType = si.getAnswerType();
        request.setAttribute(ATTR_ANSWER_TYPE, surveyQuestionId);
        SurveyAnswerView view = null;

        if ("preVersionDisplay".equals(request.getParameter(ATTR_ACTION))) {
            request.setAttribute(ATTR_CONTENT_VERSION_LIST, surveyService.getAllContentVersions(horseId));
            int cntVersionId = StringUtils.str2int(request.getParameter(PARAM_CONTENT_VERSION_ID));
            request.setAttribute(ATTR_CONTENT_VERSION_ID, cntVersionId);
            if (answerType == Constants.SURVEY_ANSWER_TYPE_TABLE) {
                view = surveyService.getSurveyAnswerPreVersionView(cntVersionId, surveyQuestionId, loginUser.getLanguageId());
            } else {
                view = surveyService.getSurveyAnswerPreVersionView(cntVersionId, surveyQuestionId, loginUser.getLanguageId());
            }
            request.setAttribute(ATTR_CONTENT_VERSION, surveyService.getContentVersion(cntVersionId));
            request.setAttribute("attachments", surveyAnswerService.getSurveyAnswerAttachmentsByVersionId(view.getSurveyAnswerId()));
        } else {
            view = surveyService.getSurveyAnswerView(surveyAnswerId, loginUser.getLanguageId());
            request.setAttribute("attachments", surveyAnswerService.getSurveyAnswerAttachments(view.getSurveyAnswerId()));
        }

        if (view == null) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_DATA_NOTEXISTED, "SurveyAnswer", surveyAnswerId));
            return mapping.findForward(FWD_ERROR);
        }
        int initialFlagGroupId = StringUtils.str2int(request.getParameter(PARAM_INITIAL_FLAG_GROUP_ID), 0);
        view.setInitialFlagGroupId(initialFlagGroupId);
        request.setAttribute("view", view);
        List<Target> targetList = trgtService.getOtherTargetsByHorseId(horseId);
        SurveyQuestionUrlView urlView = surveyCategoryService.getSurveyQuestionUrlView(surveyAnswerId, horseId);
        String retUrl = request.getParameter("returl");
        if (retUrl != null) {
            urlView.setReturnUrl(URLEncoder.encode(retUrl, "UTF-8"));
        }
        request.setAttribute("urlView", urlView);
        request.setAttribute("targets", targetList);
        request.setAttribute(ATTR_ACTION, request.getParameter(ATTR_ACTION)/*"surveyAnswerDisplay.do"*/);

        if (surveyAnswerService.hasOriginalAnswer(horseId, surveyAnswerId)) {
            request.setAttribute("showOriginal", true);
        }
        return mapping.findForward(FWD_SURVEY_ANSWER);
    }

    @Autowired
    public void setHorseService(HorseService horseService) {
        this.horseService = horseService;
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
    public void setSurveyCategoryService(SurveyCategoryService surveyCategoryService) {
        this.surveyCategoryService = surveyCategoryService;
    }
}
