/**
 * 
 */
package com.ocs.indaba.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.service.ReviewService;
import com.ocs.indaba.service.SurveyAnswerService;
import com.ocs.indaba.service.SurveyService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.SurveyAnswerView;
import com.ocs.indaba.vo.SurveyPeerReviewVO;

import org.apache.log4j.Logger;

/**
 * @author Tiger Tang
 *
 */
public class SurveyAnswerPeerReviewDisplayAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SurveyAnswerPeerReviewDisplayAction.class);
    private static final String PARAM_SURVEY_ANSWER_VERSION_ID = "saVerId";
    private SurveyService surveyService;
    private ReviewService reviewService;
    private SurveyAnswerService surveyAnswerService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response);
        setRequestUrl(request);

        //session.setAttribute(ATTR_ACTIVE, TAB_YOURCONTENT);

        // call serveyService functions// call serveyService functions
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
        request.setAttribute(ATTR_HORSE_ID, horseId);

        int surveyAnswerId = StringUtils.str2int(request.getParameter(PARAM_ANSWER_ID), Constants.INVALID_INT_ID);
        request.setAttribute(ATTR_ANSWER_ID, surveyAnswerId);

        int surveyQuestionId = StringUtils.str2int(request.getParameter(PARAM_QUESTION_ID), Constants.INVALID_INT_ID);
        int initialFlagGroupId = StringUtils.str2int(request.getParameter(PARAM_INITIAL_FLAG_GROUP_ID), 0);
        request.setAttribute(ATTR_QUESTION_ID, surveyQuestionId);
        if (surveyAnswerId == Constants.INVALID_INT_ID) {
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

        SurveyAnswerView view = null;
        if ("preVersionDisplay".equals(request.getParameter(ATTR_ACTION))) {
            int cntVersionId = StringUtils.str2int(request.getParameter(PARAM_CONTENT_VERSION_ID));
            view = surveyService.getSurveyAnswerPreVersionView(cntVersionId, surveyQuestionId, loginUser.getLanguageId());            
            request.setAttribute("attachments", surveyAnswerService.getSurveyAnswerAttachmentsByVersionId(view.getSurveyAnswerId()));
        } else {
            view = surveyService.getSurveyAnswerView(surveyAnswerId, loginUser.getLanguageId());
            request.setAttribute("attachments", surveyAnswerService.getSurveyAnswerAttachments(view.getSurveyAnswerId()));
        }

        //SurveyAnswerView view = surveyService.getSurveyAnswerView(surveyAnswerId);
        if (view == null) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_DATA_NOTEXISTED, "SurveyAnswer", surveyAnswerId));
            return mapping.findForward(FWD_ERROR);
        }
        view.setInitialFlagGroupId(initialFlagGroupId);
        request.setAttribute("view", view);

        int reviewerId = StringUtils.str2int(request.getParameter("reviewerid"), Constants.INVALID_INT_ID);
        SurveyPeerReviewVO peerReview = null;
        try{
        if ("preVersion".equals(request.getParameter("viewMode"))) {
            int cntVersionId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_ANSWER_VERSION_ID));
            peerReview = reviewService.getSurveyPeerReviewByVersion(loginUser.getPrjid(), loginUser.getUid(), reviewerId, cntVersionId);
        } else {
            peerReview = reviewService.getSurveyPeerReview(loginUser.getPrjid(), loginUser.getUid(), reviewerId, surveyAnswerId);
        }
        }catch(Exception ex) {
            logger.debug("Error: ", ex);
        }
        request.setAttribute("peerReview", peerReview);

        return mapping.findForward(FWD_SUCCESS);
    }

    @Autowired
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @Autowired
    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Autowired
    public void setSurveyAnswerService(SurveyAnswerService surveyAnswerService) {
        this.surveyAnswerService = surveyAnswerService;
    }
}
