/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.AssignmentDAO;
import com.ocs.indaba.vo.Assignment;

import com.ocs.indaba.dao.TaskDAO;
import com.ocs.indaba.po.Task;

import com.ocs.indaba.dao.ContentHeaderDAO;
import com.ocs.indaba.po.ContentHeader;

import com.ocs.indaba.dao.TaskAssignmentDAO;
import com.ocs.indaba.po.TaskAssignment;

import com.ocs.indaba.dao.JournalPeerReviewDAO;
import com.ocs.indaba.dao.ProjectDAO;
import com.ocs.indaba.dao.SurveyPeerReviewDAO;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.vo.FlagWorkView;

import com.ocs.util.DateUtils;

import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
//import com.ocs.indaba.service.UserService;
//import com.ocs.indaba.service.SiteMessageService;
import java.util.Map;
//import org.drools.util.DateUtils;
/**
 * Assignment Service
 *
 * @author Jeff
 */
public class AssignmentService {
    private static final Logger logger = Logger.getLogger(AssignmentService.class);

    private AssignmentDAO assignmentDao = null;
    private TaskDAO taskDao = null;
    private TaskAssignmentDAO taskAssignmentDao = null;
    private ContentHeaderDAO contentHeaderDao = null;
    private TaskService taskService = null;
    private JournalPeerReviewDAO journalPeerReviewDAO = null;
    private SiteMessageService siteMessageService = null;
    private UserService userService = null;
    private SurveyPeerReviewDAO surveyPeerReviewDAO = null;
    private ProjectDAO projectDao = null;
    private CommPanelService commPanelService = null;

    public Assignment getAssignmentById(int id) {
        logger.debug("Get Assignment by id: " + id);
        Assignment assignment = assignmentDao.selectAssignmentById(id);
        return assignment;
    }

    public List<Assignment> getAssignmentsByOwnerId(int id) {
        logger.debug("Get notebook by owner id: " + id);
        List<Assignment> assginments = assignmentDao.selectAssignmentsByOwnerId(id);
        return assginments;
    }

    public boolean cancelJournalReviewResponseAssignment(int horseId) {
        String toolName = "journal review response";

        TaskAssignment assignment = taskService.findExistingReviewResponseAssignment(horseId, toolName);
        if (assignment != null) {
            taskAssignmentDao.delete(assignment.getId());
            return true;
        }
        return false;
    }

    public boolean cancelSurveyReviewResponseAssignment(int horseId) {
        String toolName = "survey review response";

        TaskAssignment assignment = taskService.findExistingReviewResponseAssignment(horseId, toolName);
        if (assignment != null) {
            taskAssignmentDao.delete(assignment.getId());
            return true;
        }
        return false;
    }
            
    public TaskAssignment createReviewResponseAssignment(String action, int oldAssignId, int horseId, String userId, int ruserId, String contents, String dueDate) {
        String toolName = "survey review response";
        if (action.equals("journalReviewResponse"))
            toolName = "journal review response";
        Task task = taskDao.selectTaskByToolName(toolName);
        TaskAssignment oldAssignment = taskAssignmentDao.get(oldAssignId);
        
        if (ruserId == Constants.INVALID_INT_ID) {
            ContentHeader ch = contentHeaderDao.selectContentHeaderByHorseId(horseId);
            ruserId = ch.getAuthorUserId();
        }
        
        //TaskAssignment assignment = new TaskAssignment(0, task.getId(), oldAssignment.getTargetId(), author_uid, (short)Constants.TASK_STATUS_ACTIVE, horseId);
        TaskAssignment assignment = new TaskAssignment();
        assignment.setTaskId(task.getId());
        assignment.setTargetId(oldAssignment.getTargetId());
        assignment.setAssignedUserId(ruserId);
        assignment.setStatus((short)Constants.TASK_STATUS_ACTIVE);
        assignment.setHorseId(horseId);
        assignment.setGoalObjectId(oldAssignment.getGoalObjectId());
        if (contents != null && contents.length() > 0)
            assignment.setData(userId + " " + contents);
        else
            assignment.setData(userId);
        assignment.setPercent(-1.0f);
        if (dueDate == null || dueDate.length() == 0) {
            assignment.setDueTime(oldAssignment.getDueTime());
        } else {
            Date date = DateUtils.parse(dueDate, "yyyy-MM-dd");
            assignment.setDueTime(date);
        }
        assignment.setQLastAssignedTime(new Date());
        assignment.setQEnterTime(new Date());
        taskAssignmentDao.create(assignment);

        /*
        if (ruserId == Constants.INVALID_INT_ID) {
            logger.error("Author UID 0 in Content Header " + ch.getId() + " for Horse " + horseId);
        }
        */
        
        return assignment;
    }

    public String checkAssignmentStatus(int userId, int assignid) {
        String retMessage;
        TaskAssignment taskAssignment = taskAssignmentDao.
                selectTAByAssignedUserIdAndTAId(userId, assignid);
        if (taskAssignment == null) {
            retMessage = "You do not have access to this assignment.";
        } else if (taskAssignment.getStatus() == Constants.TASK_STATUS_INACTIVE) {
            retMessage = "This assignment is currently inactive.";
        } else if (taskAssignment.getStatus() == Constants.TASK_STATUS_DONE) {
            retMessage = "This assignment has been completed.";
        } else {
            retMessage = "OK";
        }
        return retMessage;
    }

    public void changeAssignmentDeadline(int assignId, String deadline) {
        TaskAssignment taskAssignment = taskAssignmentDao.get(assignId);
        if (taskAssignment == null)
            return;

        Project project = projectDao.selectProjectByTaskAssignment(assignId);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date duetime = df.parse(deadline);
            taskAssignment.setDueTime(duetime);
            taskAssignmentDao.update(taskAssignment);

            Map<String, String> parameters = siteMessageService.getParameters(
                                                                    taskAssignment,
                                                                    userService.getUser(taskAssignment.getAssignedUserId()),
                                                                    null);
            parameters.put("taskduetime", deadline);
            siteMessageService.deliver(project.getId(), taskAssignment.getAssignedUserId(), Constants.NOTIFICATION_TYPE_TASK_DEADLINE_CHANGE, parameters);
        } catch (Exception e) {
            logger.error("Error occurs!", e);
        }
    }

    public void exitAssignment(int assignId, int exitOption) {
        logger.debug("==== End Assignment: ID-" + assignId + " Option-" + exitOption);

        TaskAssignment taskAssignment = taskAssignmentDao.get(assignId);
        if (taskAssignment == null)
            return;

        if (taskAssignment.getTaskId() < 0) {
            // this is a dynamically created assignment. Remove it from DB.
            taskAssignmentDao.delete(assignId);
            return;
        }

        //taskAssignment.setPercent(1f);
        taskAssignment.setStatus((short) Constants.TASK_STATUS_DONE);
        taskAssignment.setExitType((short) Constants.TASK_EXIT_TYPE_FORCED);
        
        taskAssignment.setCompletionTime(new Date());
        taskService.updateTaskAssignment(taskAssignment);

        Task task = taskDao.get(taskAssignment.getTaskId());
        if (task.getType() == Constants.TASK_TYPE_SURVEY_PEER_REVIEW) {
            if (exitOption == 0) {
                surveyPeerReviewDAO.completeSurveyPeerReviewByAssignment(taskAssignment);
            } else {
                surveyPeerReviewDAO.removeSurveyPeerReviewByAssignment(taskAssignment);
            }
        } else if (task.getType() == Constants.TASK_TYPE_JOURNAL_PEER_REVIEW) {
            if (exitOption == 0) {
                journalPeerReviewDAO.completeJournalPeerReviewByAssignment(taskAssignment);
            } else {
                journalPeerReviewDAO.removeJournalPeerReviewByAssignment(taskAssignment);
            }
        }
    }


    /*
     * Check whether the horse has any standing flags assigned to the user
     */
    public boolean horseHasStandingFlagsAssignedToUser(int userId, int horseId) {
        List<FlagWorkView> flags = commPanelService.getActiveFlagsAssignedToUser(userId, horseId);
        return (flags != null && !flags.isEmpty());
    }


    /*
     * Check whether the horse has any standing flags raised by the user
     */
    public boolean horseHasStandingFlagsRaisedByUser(int userId, int horseId) {
        List<FlagWorkView> flags = commPanelService.getActiveFlagsRaisedByUser(userId, horseId);
        return (flags != null && !flags.isEmpty());
    }
    

    @Autowired
    public void setAssignmentDao(AssignmentDAO assignmentDao) {
        this.assignmentDao = assignmentDao;
    }

    @Autowired
    public void setTaskDao(TaskDAO taskDao) {
        this.taskDao = taskDao;
    }

    @Autowired
    public void setTaskAssignmentDao(TaskAssignmentDAO taskAssignmentDao) {
        this.taskAssignmentDao = taskAssignmentDao;
    }

    @Autowired
    public void setContentHeaderDao(ContentHeaderDAO contentHeaderDao) {
        this.contentHeaderDao = contentHeaderDao;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSiteMessageService(SiteMessageService siteMessageService) {
        this.siteMessageService = siteMessageService;
    }

    @Autowired
    public void setCommPanelService(CommPanelService srvc) {
        this.commPanelService = srvc;
    }

    @Autowired
    public void setJournalPeerReviewDAO(JournalPeerReviewDAO journalPeerReviewDAO) {
        this.journalPeerReviewDAO = journalPeerReviewDAO;
    }

    @Autowired
    public void setSurveyPeerReviewDAO(SurveyPeerReviewDAO surveyPeerReviewDAO) {
        this.surveyPeerReviewDAO = surveyPeerReviewDAO;
    }

    @Autowired
    public void setProjectDAO(ProjectDAO dao) {
        this.projectDao = dao;
    }

}
