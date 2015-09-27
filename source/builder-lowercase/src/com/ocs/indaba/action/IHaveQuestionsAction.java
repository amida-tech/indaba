/**
 * 
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.service.AssignmentService;
import com.ocs.indaba.service.SiteMessageService;
import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.service.TaskService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.util.DateUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @author Luke
 *
 */
public class IHaveQuestionsAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(IHaveQuestionsAction.class);
    private AssignmentService assignmentService = null;
    private SiteMessageService siteMsgSrvc = null;
    private SurveyService surveyService = null;
    private TaskService taskService = null;

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
    
    @Autowired
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @Autowired
    public void setSiteMessageService(SiteMessageService siteMessageService) {
        this.siteMsgSrvc = siteMessageService;
    }

    @Autowired
    public void setAssignmentService(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @Override
    public ActionForward execute(ActionMapping mapping,
                    ActionForm form, HttpServletRequest request,
                    HttpServletResponse response) {
        String action = request.getParameter("action");
        
        LoginUser loginUser= preprocess(mapping, request, response, true, false, true);
        
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
        
        logger.debug("=================== ACTION:" + action + ", HorseID:" + horseId);
        if (action.equals("cancel")) {
            surveyService.cancelProblemSurveyAnswers(horseId);
            TaskAssignment ta = taskService.findExistingSurveyReviewResponseAssignment(horseId);
            if (ta != null) {
                assignmentService.cancelSurveyReviewResponseAssignment(horseId);
                siteMsgSrvc.sendContentNotice(horseId, ta.getAssignedUserId(), Constants.NOTIFICATION_TYPE_SYS_REVIEW_FEEDBACK_CANCELED);
            }
        } else if (action.equals("journalSubmitDone")) {
            try {
                PrintWriter out = response.getWriter();
                if (taskService.journalReviewResponseAssignmentExist(horseId)) {
                    out.print("NO");
                } else {
                    out.print("OK");
                }
            } catch (IOException e) {
                logger.info("======ERROR - Exception: " + e + "=============");
            }
        } else if (action.equals("surveySubmitDone")) {
            try {
                PrintWriter out = response.getWriter();
                if (surveyService.allSurveyProblemAnswered(horseId)) {
                    out.print("OK");
                } else {
                    out.print("NO");
                }
            } catch (Exception e) {
                logger.info("======ERROR - Exception: " + e + "=============");
            }
        } else {
            TaskAssignment ta = null;
            if (action.equals("journalReviewResponse")) {
                ta = taskService.findExistingReviewResponseAssignment(horseId, "journal review response");
            } else {
                ta = taskService.findExistingReviewResponseAssignment(horseId, "survey review response");
            }
            int ruserId = StringUtils.str2int(request.getParameter("ruserid"), Constants.INVALID_INT_ID);
            String contents = request.getParameter("contents");
            String dueDate = request.getParameter("duedate");
            
            if (ta == null) {
                makeNewReviewResponse(request, horseId, action, ruserId, loginUser, contents, dueDate);
                /*
                int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
                String userId = String.valueOf(loginUser.getUid());
                
                String contents = request.getParameter("contents");
                String dueDate = request.getParameter("duedate");
                
                assignmentService.createReviewResponseAssignment(action, assignId, horseId, userId, ruserId, contents, dueDate);
                surveyService.resetProblemSurveyAnswers(horseId);
                siteMsgSrvc.sendContentNotice(horseId, Constants.NOTIFICATION_TYPE_SYS_REVIEW_FEEDBACK_POSTED);
                * 
                */
            } else if (ruserId != ta.getAssignedUserId()) {
                siteMsgSrvc.sendContentNotice(horseId, ta.getAssignedUserId(), Constants.NOTIFICATION_TYPE_SYS_REVIEW_FEEDBACK_CANCELED);
                
                /*
                taskService.removeTaskAssignment(ta.getId());
                makeNewReviewResponse(request, horseId, action, ruserId, loginUser, contents, dueDate);
                * 
                */
                ta.setAssignedUserId(ruserId);
                if (dueDate != null && dueDate.length() > 0) {
                    Date date = DateUtils.parse(dueDate, "yyyy-MM-dd");
                    ta.setDueTime(date);
                }
                ta.setData(loginUser.getUid() + " " + contents);
                taskService.updateTaskAssignment(ta);
                
                siteMsgSrvc.sendContentNotice(horseId, ruserId, Constants.NOTIFICATION_TYPE_SYS_REVIEW_FEEDBACK_POSTED);
            } else {
                if (dueDate != null && dueDate.length() > 0) {
                    Date date = DateUtils.parse(dueDate, "yyyy-MM-dd");
                    ta.setDueTime(date);
                }
                ta.setData(loginUser.getUid() + " " + contents);
                taskService.updateTaskAssignment(ta);
            }
        }

        return null;
    }

    private void makeNewReviewResponse(HttpServletRequest request, int horseId, String action, int ruserId, LoginUser loginUser, String contents, String dueDate) {
        int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
        String userId = String.valueOf(loginUser.getUid());

        assignmentService.createReviewResponseAssignment(action, assignId, horseId, userId, ruserId, contents, dueDate);
        if (action.equals("surveyReviewResponse"))
            surveyService.resetProblemSurveyAnswers(horseId);
        siteMsgSrvc.sendContentNotice(horseId, ruserId, Constants.NOTIFICATION_TYPE_SYS_REVIEW_FEEDBACK_POSTED);
    }
}
