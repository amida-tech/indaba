package com.ocs.indaba.action;

import com.google.gson.Gson;
import static com.ocs.indaba.action.BaseAction.FWD_ERROR;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.service.SurveyCategoryService;
import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.survey.table.SurveyTableService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.SurveyAnswerSubmitView;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

public class SubmitIndicatorAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SubmitIndicatorAction.class);
    private static final String PARAM_ANSWER = "answer";
    private static final String PARAM_SOURCE = "source";
    private static final String PARAM_SOURCE_DESC = "sourceDesc";
    private static final String PARAM_COMMENTS = "comments";
    private static final String PARAM_PRREVIEWS = "prreviews";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_OPINION = "opinion";
    private static final String PARAM_SUGGESTED_SCORE = "suggestedScore";
    private static final String PARAM_ANSWER_TYPE = "answerType";
    private static final String PARAM_PEER_REVIEW_ID = "peerReviewId";
    private SurveyService surveyService;
    private SurveyTableService surveyTableService;
    private SurveyCategoryService surveyCategoryService;

    @Autowired
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @Autowired
    public void setSurveyCategoryService(SurveyCategoryService surveyCategoryService) {
        this.surveyCategoryService = surveyCategoryService;
    }

    @Autowired
    public void setSurveyTableService(SurveyTableService surveyTableService) {
        this.surveyTableService = surveyTableService;
    }

    @Override
    public ActionForward execute(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        LoginUser loginUser = preprocess(mapping, request, response);
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            /*
             if (preprocess(mapping, request, response) != null) {
             writer.write("Invalid user!");
             return null;
             }
             */
            int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
            int surveyAnswerId = StringUtils.str2int(request.getParameter(PARAM_ANSWER_ID), Constants.INVALID_INT_ID);
            int surveyQuestionId = StringUtils.str2int(request.getParameter(PARAM_QUESTION_ID), Constants.INVALID_INT_ID);

            if (surveyQuestionId <= 0) {
                request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "answerid or questionid"));
                return mapping.findForward(FWD_ERROR);
            }

            if (surveyAnswerId <= 0) {                
                surveyAnswerId = surveyService.getSurveyAnswerId(surveyQuestionId, horseId, loginUser.getUid());
                if (surveyAnswerId <= 0) {
                    request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "answerid"));
                    return mapping.findForward(FWD_ERROR);
                }
            }

            SurveyIndicator indicator = surveyService.getSurveyIndicatorByQuestionId(surveyQuestionId);
            if (indicator == null) {
                request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "questionid"));
                return mapping.findForward(FWD_ERROR);
            }

            String type = request.getParameter(PARAM_TYPE);
            int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
            SurveyAnswerSubmitView view = null;
            if (Constants.SURVEY_ACTION_PEERREVIEW.equalsIgnoreCase(type)) {
                int opinion = StringUtils.str2int(request.getParameter(PARAM_OPINION), Constants.INVALID_INT_ID);
                String comments = request.getParameter(PARAM_COMMENTS);
                boolean continueProcessing = true;

                if (opinion == Constants.SURVEY_PEER_DISAGREE && Constants.SURVEY_ANSWER_TYPE_TABLE == indicator.getAnswerType()) {
                    int peerReviewId = StringUtils.str2int(request.getParameter(PARAM_PEER_REVIEW_ID));
                    logger.debug("****************** peerReviewId=" + peerReviewId);
                    if (peerReviewId <= 0) {
                        request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "peerReviewId"));
                        return mapping.findForward(FWD_ERROR);
                    }
                    view = surveyTableService.savePeerReviewComponentAnswers(request, peerReviewId, surveyQuestionId, loginUser.getUid(), loginUser.getLanguageId());
                    logger.debug("****************** savePeerReviewComponentAnswers is done: " + view.getErrorMsg());
                    continueProcessing = view.getErrorMsg() == null;
                }

                if (continueProcessing) {
                    String suggestedScore = request.getParameter(PARAM_SUGGESTED_SCORE);
                    view = surveyService.submitIndicatorPeerReview(loginUser.getUid(), horseId, assignId, surveyAnswerId, opinion, comments, suggestedScore, loginUser.getLanguageId());
                }
            } else {
                String answer = request.getParameter(PARAM_ANSWER);
                String source = request.getParameter(PARAM_SOURCE);
                String sourceDesc = request.getParameter(PARAM_SOURCE_DESC);
                String comments = request.getParameter(PARAM_COMMENTS);
                String prreviews = request.getParameter(PARAM_PRREVIEWS);
                boolean continueProcessing = true;

                if (Constants.SURVEY_ANSWER_TYPE_TABLE == indicator.getAnswerType()) {
                    view = surveyService.processSurveyAnswerPage(request, surveyAnswerId, horseId, surveyQuestionId, loginUser.getUid(), loginUser.getLanguageId(), prreviews);
                    continueProcessing = view.getErrorMsg() == null;
                }
                if (continueProcessing) {
                    view = surveyService.submitIndicatorAnswer(loginUser.getUid(), type, horseId, assignId, surveyAnswerId, answer, source, sourceDesc, comments, prreviews, loginUser.getLanguageId());
                }
            }

            boolean done = surveyCategoryService.checkCompletedAllSurveyIndicators(type, horseId, loginUser.getUid());
            view.setDone(done);
            if (!done) {
                view.I18NErrorMsg(BaseAction.getLanguageId(request));
            }
            writer.write(new Gson().toJson(view));

        } catch (Exception ex) {
            logger.error("Fail to save indicator!", ex);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

        return null;
    }
}
