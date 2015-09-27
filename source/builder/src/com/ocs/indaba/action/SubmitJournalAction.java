package com.ocs.indaba.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.indaba.service.AssignmentService;
import com.ocs.indaba.service.JournalService;
import com.ocs.indaba.service.SiteMessageService;
import com.ocs.indaba.service.TaskService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.AssignedTask;
import com.ocs.indaba.vo.LoginUser;

public class SubmitJournalAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SubmitJournalAction.class);
    private TaskService taskService;
    private JournalService journalService;
    private AssignmentService assignmentService;
    private SiteMessageService siteMessageService;

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Autowired
    public void setJournalService(JournalService journalService) {
        this.journalService = journalService;
    }

    @Autowired
    public void setAssignmentService(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @Autowired
    public void setSiteMessageService(SiteMessageService siteMessageService) {
        this.siteMessageService = siteMessageService;
    }

    @Override
    public ActionForward execute(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        LoginUser loginUser = preprocess(mapping, request, response);
        
        try {
            final PrintWriter writer = response.getWriter();            
            int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
            AssignedTask assignedTask = taskService.getTaskTypeByAssignId(assignId);
            TaskAssignment ta = taskService.findExistingJournalReviewResponseAssignment(assignedTask.getHorseId());
            if (ta != null) {
                assignmentService.cancelJournalReviewResponseAssignment(assignedTask.getHorseId());
                siteMessageService.sendContentNotice(assignedTask.getHorseId(), ta.getAssignedUserId(), Constants.NOTIFICATION_TYPE_SYS_REVIEW_FEEDBACK_CANCELED);
            }

            if (assignedTask.getTaskType() == Constants.TASK_TYPE_JOURNAL_PEER_REVIEW) {
                String opinions = request.getParameter("opinions");
                //try {
                //    opinions = new String(opinions.getBytes("ISO-8859-1"), "UTF-8");
                //} catch (Exception e) {}
                journalService.submitJournalPeerReview(loginUser.getUid(), assignedTask.getHorseId(), opinions);
            }

            taskService.updateStatusAndPercentage(loginUser.getUid(), assignId, Constants.TASK_STATUS_DONE, 1f);
            writer.write(new Gson().toJson(0));
        } catch (IOException e) {
        }

        return null;
    }
}
