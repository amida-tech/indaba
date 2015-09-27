package com.ocs.indaba.action;

import com.google.gson.Gson;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.common.Rights;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.ReviewService;
import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.service.TaskService;
import com.ocs.indaba.service.UserService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.AssignedTask;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.SurveyAnswerSubmitView;
import com.ocs.indaba.vo.SurveyPeerReviewVO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

public class ReviewAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(ReviewAction.class);
    private static final String PARAM_SURVEY_ANSWER_VERSION_ID = "saVerId";
    private static final String PARAM_VIEW_MODE = "viewMode";
    private static final String PARAM_CONTNET_VERSION_ID = "contentVersionId";
    private static final String PARAM_QUESTION_ID = "questionid";
    private ReviewService reviewService;
    private SurveyService surveyService;
    private TaskService taskService;
    private UserService userService;

    @Autowired
    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Autowired
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        LoginUser loginUser = preprocess(mapping, request, response);

        logger.debug("ReviewAction - Write new message request: [userid=" + loginUser.getUid() + "].");

        String action = request.getParameter(ATTR_ACTION);
        logger.debug("Action requested: " + action);
        
        try {
            // commmon
            if ("getInternalMsgboardId".equalsIgnoreCase(action)) {
                return getInternalMsgboardId(mapping, request, response);
            }

            if ("getStaffAuthorMsgboardId".equalsIgnoreCase(action)) {
                return getStaffAuthorMsgboardId(mapping, request, response);
            }

            // journal
            if ("getJournalPeerReview".equalsIgnoreCase(action)) {
                return getJournalPeerReview(request, response, loginUser);
            }

            if ("getJournalPRReviews".equalsIgnoreCase(action)) {
                return getJournalPRReviews(request, response, loginUser);
            }

            if ("saveJournalPeerReviewOpinions".equalsIgnoreCase(action)) {
                return saveJournalPeerReviewOpinions(request, response, loginUser);
            }

            if ("changeJournalPeerReviewOpinions".equalsIgnoreCase(action)) {
                return changeJournalPeerReviewOpinions(mapping, form, request, response, loginUser);
            }

            // survey
            if ("getSurveyAnswerInternalMsgboardId".equalsIgnoreCase(action)) {
                return getSurveyAnswerInternalMsgboardId(mapping, request, response, loginUser);
            }

            if ("getSurveyAnswerStaffAuthorMsgboardId".equalsIgnoreCase(action)) {
                return getSurveyAnswerStaffAuthorMsgboardId(mapping, request, response, loginUser);
            }

            if ("getSurveyPeerReview".equalsIgnoreCase(action)) {
                return getSurveyPeerReview(mapping, request, response, loginUser);
            }

            if ("getSurveyPRReviews".equalsIgnoreCase(action)) {
                return getSurveyPRReviews(mapping, request, response, loginUser);
            }

            if ("saveSurveyPeerReview".equalsIgnoreCase(action)) {
                return saveSurveyPeerReview(mapping, request, response, loginUser);
            }

            response.getWriter().write("Invalid action!");
        } catch (IOException e) {
            logger.error(e);
        }

        return null;
    }

    private ActionForward changeJournalPeerReviewOpinions(
            ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, LoginUser loginUser) throws IOException {
        int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
        AssignedTask assignedTask = taskService.getTaskTypeByAssignId(assignId);
        if ((assignedTask != null)
                && (assignedTask.getTaskType() == Constants.TASK_TYPE_JOURNAL_PR_REVIEW
                || assignedTask.getTaskType() == Constants.TASK_TYPE_JOURNAL_OVERALL_REVIEW)) {
            if (assignedTask.getStatus() < Constants.TASK_STATUS_STARTED) {
                taskService.updateTaskAssignmentStatus(assignId, Constants.TASK_STATUS_STARTED);
            }

            int journalPeerReviewId = StringUtils.str2int(request.getParameter("peerreviewid"), Constants.INVALID_INT_ID);
            String opinions = request.getParameter("opinions");
            reviewService.changeJournalPeerReviewOpinions(journalPeerReviewId, opinions);
            response.getWriter().write("OK");
        } else {
            // super edit
            User user = userService.getUser(loginUser.getUid());
            if (user.getSiteAdmin() == 1 || accessPermissionService.checkProjectPermission(loginUser.getPrjid(), loginUser.getUid(), "super edit content")) {
                int journalPeerReviewId = StringUtils.str2int(request.getParameter("peerreviewid"), Constants.INVALID_INT_ID);
                String opinions = request.getParameter("opinions");
                reviewService.changeJournalPeerReviewOpinions(journalPeerReviewId, opinions);
                response.getWriter().write("OK");
            }
        }

        return null;
    }

    private ActionForward getInternalMsgboardId(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String viewMode = request.getParameter(PARAM_VIEW_MODE);
        int msgBoardId = 0;
        int horseId = 0;
        int contentVersionId = 0;

        if (StringUtils.isEmpty(viewMode)) {
            horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
            msgBoardId = reviewService.getInternalMsgboardId(horseId);
        } else {
            contentVersionId = StringUtils.str2int(request.getParameter(PARAM_CONTNET_VERSION_ID), Constants.INVALID_INT_ID);
            msgBoardId = reviewService.getInternalMsgboardIdByVersionId(contentVersionId);
        }

        if (msgBoardId <= 0) {
            logger.error("Got bad msgboard ID: " + msgBoardId);
            logger.error("ViewMode=" + viewMode);
            logger.error("HorseID=" + horseId);
            logger.error("ContentVersionId=" + contentVersionId);
        }

        response.getWriter().write(new Gson().toJson(msgBoardId));
        return null;
    }

    private ActionForward getStaffAuthorMsgboardId(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String viewMode = request.getParameter(PARAM_VIEW_MODE);
        int msgBoardId = 0;
        int horseId = 0;
        int contentVersionId = 0;

        if (StringUtils.isEmpty(viewMode)) {
            horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
            msgBoardId = reviewService.getStaffAuthorMsgboardId(horseId);
        } else {
            contentVersionId = StringUtils.str2int(request.getParameter(PARAM_CONTNET_VERSION_ID), Constants.INVALID_INT_ID);
            msgBoardId = reviewService.getStaffAuthorMsgboardIdByVersionId(contentVersionId);
        }

        if (msgBoardId <= 0) {
            logger.error("Got bad msgboard ID: " + msgBoardId);
            logger.error("ViewMode=" + viewMode);
            logger.error("HorseID=" + horseId);
            logger.error("ContentVersionId=" + contentVersionId);
        }

        response.getWriter().write(new Gson().toJson(msgBoardId));
        return null;
    }

    private ActionForward getJournalPeerReview(HttpServletRequest request,
            HttpServletResponse response, LoginUser loginUser) throws IOException {
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(new Gson().toJson(reviewService.getJournalPeerReview(loginUser.getPrjid(), loginUser.getUid(), horseId)));
        return null;
    }

    private ActionForward getJournalPRReviews(HttpServletRequest request,
            HttpServletResponse response, LoginUser loginUser) throws IOException {
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
        boolean superEdit = StringUtils.str2boolean(request.getParameter("superedit"));

        response.setContentType("text/html;charset=utf-8");
        if (superEdit) {
            response.getWriter().write(new Gson().toJson(reviewService.getJournalPRReviews(loginUser.getPrjid(), loginUser.getUid(), horseId)));
        } else {
            response.getWriter().write(new Gson().toJson(reviewService.getSubmittedJournalPRReviews(loginUser.getPrjid(), loginUser.getUid(), horseId)));
        }
        return null;
    }

    private ActionForward saveJournalPeerReviewOpinions(HttpServletRequest request,
            HttpServletResponse response, LoginUser loginUser) throws IOException {
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
        int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
        String opinions = request.getParameter("opinions");
        reviewService.saveJournalPeerReviewOpinions(horseId, assignId, loginUser.getUid(), opinions);
        response.getWriter().write("OK");
        return null;
    }

    private ActionForward getSurveyAnswerInternalMsgboardId(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, LoginUser loginUser) throws IOException {
        String viewMode = request.getParameter(PARAM_VIEW_MODE);
        int surveyQuestionId = StringUtils.str2int(request.getParameter(PARAM_QUESTION_ID));
        int contentVersionId = 0;
        int surveyAnswerId = 0;
        int msgBoardId = 0;
        int horseId = 0;

        if (!StringUtils.isEmpty(viewMode)) {
            contentVersionId = StringUtils.str2int(request.getParameter(PARAM_CONTNET_VERSION_ID), Constants.INVALID_INT_ID);
            msgBoardId = reviewService.getSurveyInternalMsgboardIdByVersionId(contentVersionId, surveyQuestionId);
        } else {
            surveyAnswerId = StringUtils.str2int(request.getParameter(PARAM_ANSWER_ID), Constants.INVALID_INT_ID);
            if (surveyAnswerId == Constants.INVALID_INT_ID) {
                horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
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
            msgBoardId = reviewService.getSurveyInternalMsgboardId(surveyAnswerId);
        }

        if (msgBoardId <= 0) {
            logger.error("Got bad msgboard ID: " + msgBoardId);
            logger.error("ViewMode=" + viewMode);
            logger.error("HorseID=" + horseId);
            logger.error("ContentVersionId=" + contentVersionId);
            logger.error("SurveyQuestionId" + surveyQuestionId);
            logger.error("SurveyAnswerId" + surveyAnswerId);
        }

        response.getWriter().write(new Gson().toJson(msgBoardId));
        return null;
    }

    private ActionForward getSurveyAnswerStaffAuthorMsgboardId(ActionMapping mapping,
            HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) throws IOException {
        String viewMode = request.getParameter(PARAM_VIEW_MODE);
        int surveyQuestionId = StringUtils.str2int(request.getParameter(PARAM_QUESTION_ID));
        int msgBoardId = 0;
        int contentVersionId = 0;
        int surveyAnswerId = 0;
        int horseId = 0;

        if (!StringUtils.isEmpty(viewMode)) {
            contentVersionId = StringUtils.str2int(request.getParameter(PARAM_CONTNET_VERSION_ID), Constants.INVALID_INT_ID);
            msgBoardId = reviewService.getSurveyStaffAuthorMsgboardIdByVersionId(contentVersionId, surveyQuestionId);
        } else {
            surveyAnswerId = StringUtils.str2int(request.getParameter(PARAM_ANSWER_ID), Constants.INVALID_INT_ID);
            if (surveyAnswerId == Constants.INVALID_INT_ID) {
                horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
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
            msgBoardId = reviewService.getSurveyStaffAuthorMsgboardId(surveyAnswerId);
        }

        if (msgBoardId <= 0) {
            logger.error("Got bad msgboard ID: " + msgBoardId);
            logger.error("ViewMode=" + viewMode);
            logger.error("HorseID=" + horseId);
            logger.error("ContentVersionId=" + contentVersionId);
            logger.error("SurveyQuestionId" + surveyQuestionId);
            logger.error("SurveyAnswerId" + surveyAnswerId);
        }

        response.getWriter().write(new Gson().toJson(msgBoardId));
        return null;
    }

    private ActionForward getSurveyPeerReview(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, LoginUser loginUser) throws IOException {
        int surveyAnswerId = StringUtils.str2int(request.getParameter(PARAM_ANSWER_ID), Constants.INVALID_INT_ID);
        if (surveyAnswerId == Constants.INVALID_INT_ID) {
            int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
            String strSurveyQuestionId = request.getParameter(PARAM_QUESTION_ID);
            int surveyQuestionId = NumberUtils.toInt(strSurveyQuestionId, 0);
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
        response.setContentType("text/html;charset=utf-8");
        SurveyPeerReviewVO sprvo = null;
        if ("preVersion".equals(request.getParameter("viewMode"))) {
            int saVerId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_ANSWER_VERSION_ID));
            sprvo = reviewService.getSurveyPeerReviewByVersion(loginUser.getPrjid(), loginUser.getUid(), saVerId);
        } else {
            sprvo = reviewService.getSurveyPeerReview(loginUser.getPrjid(), loginUser.getUid(), surveyAnswerId);
        }
        response.getWriter().write(new Gson().toJson(sprvo));
        return null;
    }

    private ActionForward getSurveyPRReviews(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, LoginUser loginUser) throws IOException {
        int surveyAnswerId = StringUtils.str2int(request.getParameter(PARAM_ANSWER_ID), Constants.INVALID_INT_ID);
        if (surveyAnswerId == Constants.INVALID_INT_ID) {
            int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
            String strSurveyQuestionId = request.getParameter(PARAM_QUESTION_ID);
            int surveyQuestionId = NumberUtils.toInt(strSurveyQuestionId, 0);
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

        //boolean superEdit = StringUtils.str2boolean(request.getParameter("superedit"));
        response.setContentType("text/html;charset=utf-8");
        //if (superEdit)
        //    response.getWriter().write(new Gson().toJson(reviewService.getSurveyPRReviews(loginUser.getPrjid(), loginUser.getUid(), surveyAnswerId)));
        //else
        boolean byUserOnly = !accessPermissionService.checkProjectPermission(loginUser.getPrjid(), loginUser.getUid(), Rights.READ_SURVEY_PEER_REVIEWS);
        List<SurveyPeerReviewVO> list = null;
        if ("preVersion".equals(request.getParameter("viewMode"))) {
            int saVerId = StringUtils.str2int(request.getParameter(PARAM_SURVEY_ANSWER_VERSION_ID));
            list = reviewService.getSubmittedSurveyPRReviewsByVersionId(loginUser.getPrjid(), loginUser.getUid(), surveyAnswerId, byUserOnly, saVerId);
        } else {
            list = reviewService.getSubmittedSurveyPRReviews(loginUser.getPrjid(), loginUser.getUid(), surveyAnswerId, byUserOnly);
        }
        if (list == null) {
            list = new ArrayList<SurveyPeerReviewVO>();
        }

        String reply = new Gson().toJson(list);

        logger.debug("Reply Data: " + reply);

        super.writeMsgLnUTF8(response, reply);
        //response.getWriter().write(new Gson().toJson(reviewService.getSubmittedSurveyPRReviews(loginUser.getPrjid(), loginUser.getUid(), surveyAnswerId, byUserOnly)));

        return null;
    }

    private ActionForward saveSurveyPeerReview(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, LoginUser loginUser) throws IOException {
        int surveyAnswerId = StringUtils.str2int(request.getParameter(PARAM_ANSWER_ID), Constants.INVALID_INT_ID);
        if (surveyAnswerId == Constants.INVALID_INT_ID) {
            int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
            String strSurveyQuestionId = request.getParameter(PARAM_QUESTION_ID);
            int surveyQuestionId = NumberUtils.toInt(strSurveyQuestionId, 0);
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
        int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
        int opinion = StringUtils.str2int(request.getParameter("opinion"), Constants.INVALID_INT_ID);
        String comments = request.getParameter("comments");
        String suggestedScore = request.getParameter("suggestedScore");
        final SurveyAnswerSubmitView view = surveyService.saveSurveyAnswerPeerReviewOpinions(horseId, assignId, surveyAnswerId, loginUser.getUid(), opinion, comments, suggestedScore, loginUser.getLanguageId());
        response.getWriter().write(new Gson().toJson(view));
        return null;
    }
}
