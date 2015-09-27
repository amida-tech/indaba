/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.action;

import static com.ocs.indaba.action.BaseAction.ATTR_QUESTION_ID;
import static com.ocs.indaba.action.BaseAction.PARAM_ANSWER_ID;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.Horse;
import com.ocs.indaba.service.HorseService;
import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.util.CookieUtils;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.SurveyAnswerView;
import com.ocs.indaba.vo.TargetTips;
import com.ocs.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author sjf
 */
public class AnswerDetailsAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SurveyAnswerAction.class);
    //private TagService tagService;
    private static final String FWD_SURVEY_QUESTION = "success";
    private static final String TARGET_NAME = "targetname";
    private HorseService horseService = null;
    private SurveyService surveyService;
    //private TaskService taskService;
    //private AnswerObjectService answerObjectService = null;
    //private SurveyCategoryService surveyCategoryService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response, true, true);

        request.setAttribute(Constants.COOKIE_ACTIVE_TAB, Constants.TAB_YOURCONTENT);
        CookieUtils.setCookie(response, Constants.COOKIE_ACTIVE_TAB, Constants.TAB_YOURCONTENT);

        int surveyAnswerId = StringUtils.str2int(request.getParameter(PARAM_ANSWER_ID), Constants.INVALID_INT_ID);
        request.setAttribute(ATTR_ANSWER_ID, surveyAnswerId);

        int surveyQuestionId = StringUtils.str2int(request.getParameter(PARAM_QUESTION_ID), Constants.INVALID_INT_ID);
        request.setAttribute(ATTR_QUESTION_ID, surveyQuestionId);

        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
        request.setAttribute(ATTR_HORSE_ID, horseId);

        String temp = request.getParameter(TARGET_NAME);
        if (surveyQuestionId == -1 && surveyAnswerId == -1) {
            return mapping.findForward(FWD_ERROR);
        } else if (temp != null && temp.length() > 0) {
            //String name = temp.trim();
            if (surveyQuestionId == -1 && surveyAnswerId != -1) {
                surveyQuestionId = surveyService.getQuestionIdbySurveyAnswerId(surveyAnswerId);
                request.setAttribute(ATTR_QUESTION_ID, surveyQuestionId);
            }

            ArrayList<Integer> targetIds = new ArrayList<Integer>();
            ArrayList<Integer> contentHeaderIds = new ArrayList<Integer>();
            List<Horse> horses = horseService.getHorsesWithTheSameProdIdByHorseId(horseId);

            if (horses != null) {
                for (Horse horse : horses) {
                    contentHeaderIds.add(horse.getContentHeaderId());
                    targetIds.add(horse.getTargetId());
                }
            }

            ArrayList<TargetTips> targets = new ArrayList<TargetTips>();
            for (int id : targetIds) {
                targets.add(new TargetTips(trgtService.getTargetById(id)));
            }

            for (int i = 0; i < targets.size(); i++) {
                if (targets.get(i).getName().equals(loginUser.getName())) {
                    surveyAnswerId = surveyService.getSurveyAnswerId(surveyQuestionId, horses.get(i).getId(), loginUser.getUid());
                    break;
                }
            }
        } else {
            return mapping.findForward(FWD_ERROR);
        }

        SurveyAnswerView view = surveyService.getSurveyAnswerView(surveyAnswerId, loginUser.getLanguageId());
        if (view == null) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_DATA_NOTEXISTED, "SurveyAnswer", surveyAnswerId));
            return mapping.findForward(FWD_ERROR);
        }
        int initialFlagGroupId = StringUtils.str2int(request.getParameter(PARAM_INITIAL_FLAG_GROUP_ID), 0);
        view.setInitialFlagGroupId(initialFlagGroupId);
        String action = "";
        if (request.getParameter("action") != null) {
            action = request.getParameter("action");
        }
        //logger.debug("-----ACTION---->"+action+"<------");
        boolean showIamdone = true;
//        if (action.equalsIgnoreCase("surveyOverallReview.do")) {
        request.setAttribute("type", Constants.TASK_TYPE_SURVEY_OVERALL_REVIEW);
        request.setAttribute("showStaff", true);
        request.setAttribute("showPeer", true);
        request.setAttribute("showOriginal", true);
//        } else {
//            request.setAttribute("type", SurveyService.SURVEY_UNKNOWN);
//        }

        request.setAttribute("showIamDone", showIamdone);
//        List<Target> targetList = trgtService.getOtherTargetsByHorseId(horseId);
//        SurveyQuestionUrlView urlView = surveyCategoryService.getSurveyQuestionUrlView(surveyAnswerId, horseId);
        String retUrl = request.getParameter("returl");
//        if (retUrl != null) {
//            urlView.setReturnUrl(URLEncoder.encode(retUrl, "UTF-8"));
//        }
//        request.setAttribute("urlView", urlView);
//        request.setAttribute("targets", targetList);
        request.setAttribute("surveyAnswer", view);
        request.setAttribute("action", "surveyAnswer.do");
        request.setAttribute("from", action);

//        String strSurveyQuestionId = request.getParameter(PARAM_QUESTION_ID);
//        int surveyQuestionId = NumberUtils.toInt(strSurveyQuestionId, 0);
//        request.setAttribute("tipinfo", surveyService.getSurveyIndicatorbyQuestionId(surveyQuestionId).getTip());
//
//        int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
//        AssignedTask assignedTask = taskService.getTaskTypeByAssignId(assignId);
//        if ((assignedTask != null)
//                && (assignedTask.getTaskType() == Constants.TASK_TYPE_SURVEY_REVIEW_RESPONSE)) {
//            List<SurveyAnswerProblemVO> problems = surveyService.getSurveyAnswerProblems(horseId);
//            request.setAttribute(ATTR_PROBLEMS, problems);
//            request.setAttribute("reviewResponse", true);
//        }
//        request.setAttribute(PARAM_ASSIGNID, assignId);

        return mapping.findForward(FWD_SURVEY_QUESTION);
    }

    @Autowired
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @Autowired
    public void setHorseService(HorseService horseService) {
        this.horseService = horseService;
    }

    /*
     @Autowired
     public void setTaskService(TaskService taskService) {
     this.taskService = taskService;
     }
     @Autowired
     public void setSurveyCategoryService(SurveyCategoryService surveyCategoryService) {
     this.surveyCategoryService = surveyCategoryService;
     }
    
     @Autowired
     public void setAnswerObjectService(AnswerObjectService answerObjectService) {
     this.answerObjectService = answerObjectService;
     }
     */

    /*
     @Autowired
     public void setTagService(TagService tagService) {
     this.tagService = tagService;
     } */
}
