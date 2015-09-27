/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import static com.ocs.indaba.action.BaseAction.ATTR_ACTION;
import static com.ocs.indaba.action.BaseAction.ATTR_QUESTION_ID;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.service.CommPanelService;
import com.ocs.indaba.service.SurveyAnswerService;
import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.vo.FlagNavView;
import com.ocs.indaba.vo.FlagWorkView;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.SurveyAnswerView;
import com.ocs.util.JSONUtils;
import com.ocs.util.StringUtils;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author yc06x
 */
public class FlagUnsetAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SurveyAnswerDisplayAction.class);
    private static final String FWD_SURVEY_ANSWER = "success";
    private SurveyService surveyService;
    private SurveyAnswerService surveyAnswerService;

    @Autowired
    private CommPanelService commPanelService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response, true, true);

        String strHorseId = request.getParameter(PARAM_HORSE_ID);
        int horseId = StringUtils.str2int(strHorseId, 0);

        if (horseId <= 0) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "horseid"));
            return mapping.findForward(FWD_ERROR);
        }
        request.setAttribute(ATTR_HORSE_ID, horseId);

        int flagId = StringUtils.str2int(request.getParameter("flagid"), 0);
        FlagWorkView flag = commPanelService.getFlagWorkView(flagId, loginUser.getUid());
        if (flag == null) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "flagid"));
            return mapping.findForward(FWD_ERROR);
        }

        String api = request.getParameter("api");
        if ("getNext".equalsIgnoreCase(api)) {
            return getDirectedFlag(response, loginUser, horseId, flag, 1);
        } else if ("getPrev".equalsIgnoreCase(api)) {
            return getDirectedFlag(response, loginUser, horseId, flag, -1);
        }

        int surveyQuestionId = flag.getSurveyQuestionId();
        int surveyAnswerId = surveyService.getSurveyAnswerId(surveyQuestionId, horseId, loginUser.getUid());
        int initialFlagGroupId = flag.getGroupobjId();
        int permissions = flag.getPermissions();
        flagId = flag.getFlagId();

        request.setAttribute(ATTR_QUESTION_ID, surveyQuestionId);
        request.setAttribute("flagid", flagId);
        request.setAttribute(ATTR_ANSWER_ID, surveyAnswerId);

        SurveyIndicator si = surveyService.getSurveyIndicatorByQuestionId(surveyQuestionId);
        int answerType = si.getAnswerType();
        request.setAttribute(ATTR_ANSWER_TYPE, answerType);
        SurveyAnswerView view = surveyService.getSurveyAnswerView(surveyAnswerId, loginUser.getLanguageId());
        request.setAttribute("attachments", surveyAnswerService.getSurveyAnswerAttachments(view.getSurveyAnswerId()));

        if (view == null) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_DATA_NOTEXISTED, "SurveyAnswer", surveyAnswerId));
            return mapping.findForward(FWD_ERROR);
        }

        view.setInitialFlagGroupId(initialFlagGroupId);
        request.setAttribute("surveyAnswerView", view);

        request.setAttribute("tipinfo", view.getTip());
        request.setAttribute("tipDisplayMethod", view.getTipDisplayMethod());

        FlagNavView flagNavView = new FlagNavView();
        flagNavView.setFlagId(flagId);
        flagNavView.setHorseId(horseId);

        String fromUrl = request.getParameter("fromurl");

        if (fromUrl != null) {
            flagNavView.setExitUrl(URLEncoder.encode(fromUrl, "UTF-8"));
            String fromUrlDecoded = URLDecoder.decode(fromUrl, "UTF-8");

            // get the returl
            int pos = fromUrlDecoded.indexOf("&returl=");
            if (pos >= 0) {
                String baseUrl = fromUrlDecoded.substring(0, pos);
                String retUrl = fromUrlDecoded.substring(pos+8);
                fromUrlDecoded = baseUrl + "&returl=" + URLEncoder.encode(retUrl, "UTF-8");
            }
            flagNavView.setExitUrlDecoded(fromUrlDecoded);
        }

        String retUrl = request.getParameter("returl");
        if (retUrl != null) {
            flagNavView.setHomeUrl(URLEncoder.encode(retUrl, "UTF-8"));
        }
        request.setAttribute("flagNavView", flagNavView);

        // get permissions
        String contents = "";
        if ((permissions & Constants.FLAG_PERMISSION_MAIN_CHANGE_SCORE) != 0) {
            contents = contents + Constants.SEND_QST_OPTION_MAIN_CHANGE_SCORE;
        }
        if ((permissions & Constants.FLAG_PERMISSION_MAIN_CHANGE_SOURCE) != 0) {
            contents = contents + Constants.SEND_QST_OPTION_MAIN_CHANGE_SRC_DESC;
        }
        if ((permissions & Constants.FLAG_PERMISSION_MAIN_CHANGE_COMMENT) != 0) {
            contents = contents + Constants.SEND_QST_OPTION_MAIN_CHANGE_COMMENTS;
        }
        if ((permissions & Constants.FLAG_PERMISSION_MAIN_CHANGE_ATTACHMENT) != 0) {
            contents = contents + Constants.SEND_QST_OPTION_MAIN_CHANGE_ATTACHMENT;
        }
        if ((permissions & Constants.FLAG_PERMISSION_PR_CHANGE_OPINION) != 0) {
            contents = contents + Constants.SEND_QST_OPTION_PR_CHANGE_OPTIONS;
        }
        if ((permissions & Constants.FLAG_PERMISSION_PR_DISCUSSION) != 0) {
            contents = contents + Constants.SEND_QST_OPTION_PR_DISCUSSION;
        }

        request.setAttribute("targets", null);
        request.setAttribute(ATTR_ACTION, request.getParameter(ATTR_ACTION));
        request.setAttribute("showOriginal", false);
        request.setAttribute("contentOnly", false);
        request.setAttribute("contents", contents);
        request.setAttribute("taskType", Constants.TASK_TYPE_SURVEY_FLAG_RESPONSE);
        request.setAttribute("fromurl", request.getParameter("fromurl"));

        int assignmentId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), 0);
        request.setAttribute(PARAM_ASSIGNID, assignmentId);

        return mapping.findForward(FWD_SURVEY_ANSWER);
    }


    private ActionForward getDirectedFlag(HttpServletResponse response, LoginUser loginUser, int horseId, FlagWorkView flag, int dir) {
        FlagWorkView flagView = commPanelService.getDirectedFlagRaisedByMe(loginUser, horseId, flag.getFlagId(), dir);
        if (flagView == null) {
            flagView = new FlagWorkView();
            flagView.setFlagId(0);
        }

        try {
            String resultJson = JSONUtils.toJsonString(flagView);
            logger.debug("RESULT: " + resultJson);
            super.writeMsgLnUTF8(response, resultJson);
        } catch (IOException ex) {
            logger.error("Fail to response JSON text.", ex);
        }
        return null;
    }


    @Autowired
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @Autowired
    public void setSurveyAnswerService(SurveyAnswerService surveyAnswerService) {
        this.surveyAnswerService = surveyAnswerService;
    }


}

