/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.dao.TaskAssignmentDAO;
import com.ocs.indaba.dao.TaskDAO;
import com.ocs.indaba.dao.GoalDAO;
import com.ocs.indaba.dao.ContentHeaderDAO;
import com.ocs.indaba.dao.GroupobjFlagDAO;
import com.ocs.indaba.dao.HorseDAO;
import com.ocs.indaba.dao.ProductDAO;
import com.ocs.indaba.dao.ProjectDAO;
import com.ocs.indaba.dao.TargetDAO;
import com.ocs.indaba.dao.TaskRoleDAO;
import com.ocs.indaba.dao.TasksubDAO;
import com.ocs.indaba.dao.UserDAO;
import com.ocs.indaba.po.Attachment;
import com.ocs.indaba.po.ContentVersion;
import com.ocs.indaba.po.Event;
import com.ocs.indaba.po.JournalAttachmentVersion;
import com.ocs.indaba.po.JournalContentObject;
import com.ocs.indaba.po.JournalContentVersion;
import com.ocs.indaba.po.Task;
import com.ocs.indaba.po.Goal;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.GroupobjFlag;
import com.ocs.indaba.po.Horse;
import com.ocs.indaba.po.Product;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.SurveyAnswer;
import com.ocs.indaba.po.Target;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.indaba.po.TaskRole;
import com.ocs.indaba.po.Tasksub;
import com.ocs.indaba.po.Tool;
import com.ocs.indaba.po.User;
import com.ocs.indaba.survey.tree.Node;
import com.ocs.indaba.survey.tree.QuestionNode;
import com.ocs.indaba.survey.tree.SurveyTree;
import com.ocs.indaba.util.DateUtils;
import com.ocs.indaba.vo.AssignedTask;
import com.ocs.indaba.vo.AttachmentAdapter;
import com.ocs.indaba.vo.JournalContentView;
import com.ocs.indaba.vo.JournalVersionView;
import com.ocs.indaba.vo.NotificationView;
import com.ocs.indaba.vo.SurveyAnswerStatus;
import com.ocs.indaba.vo.SurveyPeerReviewBasicView;
import com.ocs.indaba.vo.TaskAssignmentStatusData;
import com.ocs.indaba.vo.TaskAssignmentVO;
import com.ocs.indaba.vo.TaskAssignmentWithGrid;
import com.ocs.indaba.vo.TaskVO;
import com.ocs.util.Pagination;
import com.ocs.util.StringUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Jeff
 */
public class TaskService {

    private static final Logger logger = Logger.getLogger(TaskService.class);
    private TaskDAO taskDao = null;
    private TaskRoleDAO taskRoleDao = null;
    private TaskAssignmentDAO taskAssignmentDao = null;
    private TasksubDAO tasksubDao = null;
    //private SurveyQuestionDAO surveyQuestionDao;
    //private SurveyAnswerDAO surveyAnswerDao;
    private EventService eventService = null;
    private GoalDAO goalDao = null;
    private ContentHeaderDAO contentHeaderDao = null;
    private JournalService journalService = null;
    private JournalVersionService jouralVersionService = null;
    private ToolService toolService = null;
    private SurveyConfigService surveyConfigService = null;
    private SurveyService surveyService = null;

    @Autowired 
    UserDAO userDao = null;

    @Autowired
    HorseDAO horseDao = null;

    @Autowired
    ProductDAO productDao = null;

    @Autowired
    ProjectDAO projectDao = null;

    @Autowired
    TargetDAO targetDao = null;

    @Autowired
    GroupobjFlagDAO groupobjFlagDao = null;

    @Autowired
    NotificationItemService notificationItemService = null;

    @Autowired
    MailbatchService mailbatchService = null;

    static public final String JOURNAL_REVIEW_RESP_TASK = "journal review response";
    static public final String SURVEY_REVIEW_RESP_TASK = "survey review response";

    public boolean journalReviewResponseAssignmentExist(int horseId) {
        return reviewResponseAssignmentExist(horseId, JOURNAL_REVIEW_RESP_TASK);
    }

    public boolean surveyReviewResponseAssignmentExist(int horseId) {
        return reviewResponseAssignmentExist(horseId, SURVEY_REVIEW_RESP_TASK);
    }

    public boolean reviewResponseAssignmentExist(int horseId, String toolName) {
        TaskAssignment ta = findExistingReviewResponseAssignment(horseId, toolName);
        //logger.debug("-----------<<<" + horseId + ":" + toolName + "-" + ta + ">>>------------");
        return ta != null;
    }

    public TaskAssignment findExistingSurveyReviewResponseAssignment(int horseId) {
        return findExistingReviewResponseAssignment(horseId, SURVEY_REVIEW_RESP_TASK);
    }

    public TaskAssignment findExistingJournalReviewResponseAssignment(int horseId) {
        return findExistingReviewResponseAssignment(horseId, JOURNAL_REVIEW_RESP_TASK);
    }

    public TaskAssignment findExistingReviewResponseAssignment(int horseId, String toolName) {
        Task task = taskDao.selectTaskByToolName(toolName);
        try {
            return taskAssignmentDao.selectTaskAssignmentByTaskHorse(task.getId(), horseId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Task> getTasksOfGoal(int goalId, int horseId) {
        return taskDao.selectTasksOfGoal(goalId, horseId);
    }

    public Goal getGoal(int goalId) {
        return goalDao.get(goalId);
    }

    public TaskAssignment getTaskAssignment(int taskAssignId) {
        return taskAssignmentDao.get(taskAssignId);
    }

    public void removeTaskAssignment(int taskAssignId) {
        taskAssignmentDao.delete(taskAssignId);
    }

    public void deleteTask(int taskId) {
        this.deleteTaskAssignmentsByTaskId(taskId);
        this.deleteTaskRoleByTaskId(taskId);
        taskDao.delete(taskId);
    }

    public Map<Integer, Integer> getTaskRoleMap(List<Integer> taskIds, int roleId) {
        return taskRoleDao.selectTaskRoleMap(taskIds, roleId);
    }

    public TaskRole getTaskRole(int taskId, int roleId) {
        return taskRoleDao.getTaskRole(taskId, roleId);
    }

    public TaskRole addTaskRole(TaskRole taskRole) {
        return taskRoleDao.create(taskRole);
    }

    public TaskRole updateTaskRole(TaskRole taskRole) {
        return taskRoleDao.save(taskRole);
    }

    public void deleteTaskRoleByTaskId(int taskId) {
        taskRoleDao.deleteByTaskId(taskId);
    }

    public void deleteTaskAssignmentsByTaskId(int taskId) {
        taskAssignmentDao.deleteByTaskId(taskId);
    }

    public void deleteTaskAssignmentsByAssignmentId(int assignmentId) {
        taskAssignmentDao.deleteByTaskAssignmentId(assignmentId);
    }

    public Date getMaxDuetimeByGoalObjectId(int goalObjectId) {
        return taskAssignmentDao.selectMaxDuetimeByGoalObjectId(goalObjectId);
    }

    public AssignedTask getTaskTypeByAssignId(int assignid) {
        return taskDao.selectTaskTypeAndDataByAssignId(assignid);
    }

    public void updateTaskAssignment(TaskAssignment ta) {
        taskAssignmentDao.update(ta);
    }

    public List<AssignedTask> getAssignedTasksByUserId(int userId, int projectId) {
        logger.debug("Get the assigned task list for user: " + userId);

        return taskDao.selectAssignedTasksByUserId(userId, projectId);
    }

    public boolean hasTask(int userId, int projectId) {
        List<AssignedTask> tasks = getAssignedTasksByUserId(userId, projectId, true);
        return (tasks == null || tasks.isEmpty()) ? false : true;
    }

    public List<AssignedTask> getAssignedTasksByUserId(int userId, int project, boolean includeCompleted) {
        logger.debug("Get the assigned task list for user: " + userId);

        return taskDao.selectAssignedTasksByUserId(userId, project, includeCompleted);
    }

    public void updateTaskAssignmentStatus(int assignId, int status) {
        taskAssignmentDao.updateTaskAssignmentStatus(assignId, status);
    }

    public void updateTaskAssignment(int assignId, int status, float percentage) {
        taskAssignmentDao.updateTaskAssignment(assignId, status, percentage);
    }

    public void updateTaskAssignment(int assignId, float percentage) {
        taskAssignmentDao.updateTaskAssignment(assignId, percentage);
    }

    public void updateTaskAssignment(int assignId, int status, float percentage, Date completionTime) {
        taskAssignmentDao.updateTaskAssignment(assignId, status, percentage, completionTime);
    }

    public List<AssignedTask> getAssignedTasksOfHorseByUserId(int horseId, int userId) {
        logger.debug("Get the assigned task list of horse " + horseId + " for user: " + userId);

        return taskDao.selectAssignedTasksOfHorseByUserId(horseId, userId);
    }

    public AssignedTask getAssignedTaskOfHorseByUserIdAndAssignId(int horseId, int userId, int assignId) {
        logger.debug("Get the assigned task of horse " + horseId + " for user: " + userId + " and assignmentId: " + assignId);

        AssignedTask assignedTask = taskDao.selectAssignedTasksOfHorseByUserIdAndAssignId(horseId, userId, assignId);
        computeDeadline(assignedTask);
        return assignedTask;
    }

    public String getInstructionsByTaskAssignId(int assginId) {
        return taskDao.selectInstructionsByTaskAssignId(assginId);
    }

    public List<AssignedTask> getTaskAssignmentsOfHorseAndGoal(int horseId, int goalId) {
        logger.debug("Get the task assignment list of horse #" + horseId + " and goal #" + goalId);

        List<AssignedTask> assignedTasks = taskDao.selectTaskAssignmentsOfHorseAndGoal(horseId, goalId);
        if (assignedTasks != null) {
            for (AssignedTask task : assignedTasks) {
                computeDeadline(task);
            }
        }
        return assignedTasks;
    }
    static private final int[] GRID_TASK_TYPES = {
        Constants.TASK_TYPE_SURVEY_CREATE,
        Constants.TASK_TYPE_SURVEY_EDIT,
        Constants.TASK_TYPE_SURVEY_REVIEW,
        Constants.TASK_TYPE_SURVEY_PEER_REVIEW,
        Constants.TASK_TYPE_SURVEY_PR_REVIEW,
        Constants.TASK_TYPE_SURVEY_REVIEW_RESPONSE,
        Constants.TASK_TYPE_SURVEY_OVERALL_REVIEW};

    private boolean hasAnswerGrid(AssignedTask task) {
        if (task.getStatus() == Constants.TASK_STATUS_INACTIVE) {
            return false;
        }

        switch (task.getTaskType()) {
            case Constants.TASK_TYPE_SURVEY_CREATE:
            case Constants.TASK_TYPE_SURVEY_EDIT:
            case Constants.TASK_TYPE_SURVEY_REVIEW:            
            case Constants.TASK_TYPE_SURVEY_PR_REVIEW:
            case Constants.TASK_TYPE_SURVEY_REVIEW_RESPONSE:
            case Constants.TASK_TYPE_SURVEY_OVERALL_REVIEW:
                return task.getStatus() != Constants.TASK_STATUS_DONE;

            case Constants.TASK_TYPE_SURVEY_PEER_REVIEW:
                return true;  // always show the opinion status
                
            default:
                return false;
        }

    }

    private class GridComputationCtx {

        SurveyTree tree;
        List<Node> questions;
        List<SurveyAnswer> answers;
    }

    public List<TaskAssignmentWithGrid> getTaskAssignmentWithGridsOfHorseAndGoal(int horseId, int goalId) {

        logger.debug("Get the task assignment with grid of horse #" + horseId + " and goal #" + goalId);

        List<AssignedTask> assignedTasks = taskDao.selectTaskAssignmentsOfHorseAndGoal(horseId, goalId);
        if (assignedTasks == null) {
            return null;
        }

        GridComputationCtx ctx = new GridComputationCtx();

        List<TaskAssignmentWithGrid> result = new ArrayList<TaskAssignmentWithGrid>();
        for (AssignedTask task : assignedTasks) {
            computeDeadline(task);
            TaskAssignmentWithGrid tawg = new TaskAssignmentWithGrid();
            tawg.setAssignment(task);
            result.add(tawg);

            if (hasAnswerGrid(task)) {
                if (ctx.tree == null) {
                    // get questions listed in natural order
                    ctx.tree = surveyConfigService.buildTreeByHorse(horseId);
                    ctx.questions = ctx.tree.listQuestions();
                }
                getGridData(tawg, ctx);
            }
        }

        return result;
    }

    private void getGridData(TaskAssignmentWithGrid tawg, GridComputationCtx ctx) {
        List<SurveyAnswerStatus> statusList = new ArrayList<SurveyAnswerStatus>();
        Map<Integer, SurveyAnswerStatus> statusMap = new HashMap<Integer, SurveyAnswerStatus>();

        int index = 1;
        for (Node node : ctx.questions) {
            QuestionNode qst = (QuestionNode) node;
            SurveyAnswerStatus sas = new SurveyAnswerStatus();
            sas.setIndex(index++);
            sas.setQuestionText(qst.getPublicName() + ": " + qst.getText());
            sas.setAnswerType(qst.getAnswerType());
            sas.setFlagCount(0);
            sas.setStatus(SurveyAnswerStatus.ANSWER_STATUS_NOT_WROKED);
            statusList.add(sas);
            statusMap.put(qst.getId(), sas);
        }
        tawg.setAnswerStatusList(statusList);

        AssignedTask task = tawg.getAssignment();
        int taskType = task.getTaskType();
        int totalQuestions = 0;
        int answeredQuestions = 0;

        switch (taskType) {
            case Constants.TASK_TYPE_SURVEY_PR_REVIEW:
            case Constants.TASK_TYPE_SURVEY_CREATE:
            case Constants.TASK_TYPE_SURVEY_EDIT:
            case Constants.TASK_TYPE_SURVEY_REVIEW:
            case Constants.TASK_TYPE_SURVEY_OVERALL_REVIEW:
                totalQuestions = ctx.questions.size();

                // get survey answers
                if (ctx.answers == null) {
                    ctx.answers = surveyService.getAllSurveyAnswersByHorse(task.getHorseId());
                }

                if (ctx.answers != null) {
                    
                    for (SurveyAnswer sa : ctx.answers) {
                        short status = SurveyAnswerStatus.ANSWER_STATUS_NOT_WROKED;
                        SurveyAnswerStatus sas = statusMap.get(sa.getSurveyQuestionId());
                        if (sas == null) continue;

                        if (taskType == Constants.TASK_TYPE_SURVEY_CREATE) {
                            if (sa.getAnswerObjectId() > 0) {
                                if (sas.getAnswerType() != Constants.SURVEY_ANSWER_TYPE_TABLE || sa.getCompleted()) {
                                    status = SurveyAnswerStatus.ANSWER_STATUS_COMPLETE;
                                    answeredQuestions++;
                                } else {
                                    status = SurveyAnswerStatus.ANSWER_STATUS_INCOMPLETE;
                                }
                            }
                        } else if ((taskType == Constants.TASK_TYPE_SURVEY_EDIT && sa.getEdited())
                                || (taskType == Constants.TASK_TYPE_SURVEY_REVIEW && sa.getStaffReviewed())
                                || (taskType == Constants.TASK_TYPE_SURVEY_OVERALL_REVIEW && sa.getOverallReviewed())
                                || (taskType == Constants.TASK_TYPE_SURVEY_PR_REVIEW && sa.getPrReviewed())) {
                            status = SurveyAnswerStatus.ANSWER_STATUS_COMPLETE;
                            answeredQuestions++;
                        }
                        sas.setStatus(status);
                    }
                }
                break;

            case Constants.TASK_TYPE_SURVEY_PEER_REVIEW:
                totalQuestions = ctx.questions.size();

                // get all peer review
                List<SurveyPeerReviewBasicView> prs = surveyService.selectSurveyPeerReviewBasicViewsByUserAndAssignment(task.getAssignedUserId(), task.getAssignmentId());
                if (prs != null) {
                    for (SurveyPeerReviewBasicView pr : prs) {
                        SurveyAnswerStatus sas = statusMap.get(pr.getQuestionId());
                        if (sas != null) {
                            answeredQuestions++;

                            switch (pr.getOpinion()) {
                                case Constants.SURVEY_PEER_OPTION_AGREE:
                                    sas.setStatus(SurveyAnswerStatus.ANSWER_STATUS_AGREE);
                                    break;

                                case Constants.SURVEY_PEER_DISAGREE:
                                    sas.setStatus(SurveyAnswerStatus.ANSWER_STATUS_DISAGREE);
                                    break;

                                case Constants.SURVEY_PEER_OPTION_AGREE_WITH_COMMENTS:
                                    sas.setStatus(SurveyAnswerStatus.ANSWER_STATUS_AGREE_W_RESERVATION);
                                    break;

                                case Constants.SURVEY_PEER_NOT_QUALIFIED:
                                    sas.setStatus(SurveyAnswerStatus.ANSWER_STATUS_NOT_QUALIFIED);
                                    break;

                                default:
                                    break;
                            }
                        }

                    }
                }
                break;

            case Constants.TASK_TYPE_SURVEY_REVIEW_RESPONSE:
                // get survey answers
                if (ctx.answers == null) {
                    ctx.answers = surveyService.getAllSurveyAnswersByHorse(task.getHorseId());
                }
                if (ctx.answers != null) {
                    for (SurveyAnswer sa : ctx.answers) {
                        SurveyAnswerStatus sas = statusMap.get(sa.getSurveyQuestionId());
                        if (sas == null) {
                            continue;
                        }

                        if (!sa.getReviewerHasProblem()) {
                            sas.setStatus(SurveyAnswerStatus.ANSWER_STATUS_NOT_TO_BE_WORKED);
                        } else {
                            totalQuestions++;
                            if (sa.getAuthorResponded()) {
                                sas.setStatus(SurveyAnswerStatus.ANSWER_STATUS_COMPLETE);
                                answeredQuestions++;
                            }
                        }
                    }
                }
                break;

            default:
        }

        tawg.setProgressDisplay("" + answeredQuestions + " / " + totalQuestions);
    }

    public List<AssignedTask> getTaskAssignmentsOfHorse(int horseId) {
        return getTaskAssignmentsOfHorseAndGoal(horseId, 0);
    }

    public List<AssignedTask> getAssignedTasksOfHorse(int horseId) {
        logger.debug("Get the assigned task list of horse " + horseId);

        List<AssignedTask> assignedTasks = taskDao.selectAssignedTasksOfHorse(horseId);
        if (assignedTasks != null) {
            for (AssignedTask task : assignedTasks) {
                computeDeadline(task);
            }
        }
        return assignedTasks;
    }

    public List<AssignedTask> getActiveAssignedTasksOfHorse(int horseId) {
        logger.debug("Get the assigned task list of horse " + horseId);

        List<AssignedTask> assignedTasks = taskDao.selectActiveAssignedTasksOfHorse(horseId);
        if (assignedTasks != null) {
            for (AssignedTask task : assignedTasks) {
                computeDeadline(task);
            }
        }
        return assignedTasks;
    }

    public int getTaskStatus(int uid, int taskAssignmentId) {
        TaskAssignment ta = taskAssignmentDao.get(taskAssignmentId);
        if (ta == null) {
            return Constants.TASK_STATUS_INACTIVE;
        }
        Date startTime = ta.getStartTime();

        if (startTime == null || startTime.after(new Date())) {
            return Constants.TASK_STATUS_INACTIVE;
        }

        Event event = eventService.getSpacialEventAfter(uid, Constants.EVENT_TYPE_COMPLETE_TASK, startTime, "%assignid=" + taskAssignmentId + "%");
        if (event != null) {
            return Constants.TASK_ACTION_USERCOMPLETED;
        }

        event = eventService.getSpacialEventAfter(uid, Constants.EVENT_TYPE_WORK_ON_TASK, startTime, "%assignid=" + taskAssignmentId + "%");
        if (event != null) {
            return Constants.TASK_ACTION_USERINPROGRESS;
        }

        event = eventService.getSpacialEventAfter(uid, Constants.EVENT_TYPE_CLICK_ASSIGNMENT, startTime, "%assignid=" + taskAssignmentId + "%");
        if (event != null) {
            return Constants.TASK_ACTION_USERCLICKED;
        }

        event = eventService.getEventBefore(uid, Constants.EVENT_TYPE_LOGIN, new Date());
        if (event != null) {
            return Constants.TASK_ACTION_USERLOGIN;
        }
        return Constants.TASK_STATUS_INACTIVE;
    }

    public void computeDeadline(AssignedTask assignedTask) {
        if (assignedTask == null) {
            return;
        }

        switch (assignedTask.getStatus()) {
            case Constants.TASK_STATUS_INACTIVE:
            case Constants.TASK_STATUS_AWARE:
                assignedTask.setDisplayDeadline(" -- ");
                break;
            case Constants.TASK_STATUS_ACTIVE:
            case Constants.TASK_STATUS_STARTED:
                Date startTime = assignedTask.getStartTime();
                if (startTime != null) {
                    Date deadline = new Date(assignedTask.getStartTime().getTime() + assignedTask.getDuration() * 24 * 3600 * 1000);
                    assignedTask.setDisplayDeadline(DateUtils.date2Str(deadline, DateUtils.DEFAULT_DATE_FORMAT_1));
                } else {
                    assignedTask.setDisplayDeadline(" -- ");
                }
                break;
            case Constants.TASK_STATUS_DONE:
                assignedTask.setDisplayDeadline("done");
                break;
        }
    }

    public void updateStatusAndPercentage(int uid, int assignId, int status, float percentage) {
        AssignedTask task = getTaskTypeByAssignId(assignId);
        if (task != null) {
            status = status > task.getStatus() ? status : task.getStatus();

            if (task.getTaskType() == Constants.TASK_TYPE_JOURNAL_CREATE) {
                ContentHeader ch = contentHeaderDao.selectContentHeaderByHorseId(task.getHorseId());
                JournalContentView journalCntView = journalService.getJournalContentByCntObjIdOrHorseId(ch.getContentObjectId(), ch.getHorseId());

                final String body = journalCntView.getJournalContentObject().getBody();

                long words = StringUtils.isEmpty(body) ? 0 : StringUtils.wordcount(body);
                int maxWords = journalCntView.getMaxWords();
                int minWords = journalCntView.getMinWords();
                ;
                if (maxWords == 0 && minWords == 0) {
                    //logger.debug("===-----00000000=>>>" + taskName);
                    percentage = 0.500001f;
                } else {
                    percentage = (minWords > 0) ? (float) words / minWords : 0f;
                }
            }

            if (status == Constants.TASK_STATUS_DONE) {
                updateTaskAssignment(assignId, status, percentage, new Date());
            } else {
                updateTaskAssignment(assignId, status, percentage);
            }

            if (status == Constants.TASK_STATUS_DONE
                    && (task.getTaskType() == Constants.TASK_TYPE_JOURNAL_REVIEW
                    || task.getTaskType() == Constants.TASK_TYPE_JOURNAL_PR_REVIEW)) {
                ContentHeader cntHdr = contentHeaderDao.selectContentHeaderByHorseId(task.getHorseId());
                JournalContentView journalCntView = journalService.getJournalContentByCntObjIdOrHorseId(cntHdr.getContentObjectId(), cntHdr.getHorseId());
                JournalContentObject cntObj = journalCntView.getJournalContentObject();
                List<Attachment> attachments = journalCntView.getAttachments();
                ContentVersion cntVer = new ContentVersion(null, cntHdr.getId(), new Date(), uid);
                cntVer.setDescription(task.getTaskName());
                JournalContentVersion journalCntVer = new JournalContentVersion();
                if (cntObj != null) {
                    journalCntVer.setBody(cntObj.getBody());
                }
                List<JournalAttachmentVersion> journalAttachVerions = null;
                if (attachments != null && attachments.size() > 0) {
                    journalAttachVerions = new ArrayList<JournalAttachmentVersion>(attachments.size());
                    for (Attachment attach : attachments) {
                        journalAttachVerions.add(AttachmentAdapter.attachmentToAttachmentVersion(attach));
                    }
                }
                JournalVersionView journalVersionView = new JournalVersionView();
                journalVersionView.setContentVersion(cntVer);
                journalVersionView.setJournalContentVersion(journalCntVer);
                journalVersionView.setJournalAttachmentVersions(journalAttachVerions);
                jouralVersionService.saveJournalVersion(journalVersionView);
            }
        }
    }

    public List<Integer> getCompletedTasksByProjectAndProductAndRoleAndUserId(int projectId, int productId, int roleId, int userId) {
        return (productId > 0)
                ? taskAssignmentDao.selectCompletedTasksByProjectAndProductAndRoleAndUserId(projectId, productId, roleId, userId)
                : taskAssignmentDao.selectCompletedTasksByProjectAndRoleAndUserId(projectId, roleId, userId);
    }

    public List<Integer> getAllCompletedUsersProjectProductAndRole(int projectId, int productId, int roleId) {
        return (productId > 0)
                ? taskAssignmentDao.selectAllCompletedUsersProjectProductAndRole(projectId, productId, roleId)
                : taskAssignmentDao.selectAllCompletedUsersProjectAndRole(projectId, roleId);
    }

    public TaskAssignment getLastCompletedTaskByUserAndProjectAndProductAndRole(int projectId, int productId, int roleId, int userId) {
        return (productId > 0)
                ? taskAssignmentDao.selectLastCompletedTaskByUserAndProjectAndProductAndRole(projectId, productId, roleId, userId)
                : taskAssignmentDao.selectLastCompletedTaskByUserAndProjectAndRole(projectId, roleId, userId);
    }

    public Task getTaskById(int taskId) {
        return taskDao.get(taskId);
    }

    public List<Task> getTasksByProductId(int productId) {
        return taskDao.selectTasksByProductId(productId);
    }

    public List<Task> getUserTasksByProductId(int productId, int userId) {
        return taskDao.selectUserTasksByProductId(productId, userId);
    }

    public List<Task> getTasksByProjectId(int projectId) {
        return taskDao.selectTasksByProjectId(projectId);
    }

    public List<Task> getUserTasksByProjectId(int projectId, int userId) {
        return taskDao.selectUserTasksByProjectId(projectId, userId);
    }

    public List<Task> getUnassignedTasksByProjectId(int projectId) {
        return taskDao.selectUnassignedTasksByProjectId(projectId);
    }

    public List<Task> getUserUnassignedTasksByProjectId(int projectId, int userId) {
        return taskDao.selectUserUnassignedTasksByProjectId(projectId, userId);
    }

    public Task getTaskByProductIdAndTaskName(int productId, String taskName) {
        return taskDao.selectTaskByProductIdAndTaskName(productId, taskName);
    }

    public Pagination<TaskVO> getTasksByProductId(int productId, String sortName, String sortOrder, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        int count = pageSize;
        long totalCount = taskDao.countOfTaskCountByProductId(productId);
        List<TaskVO> tasks = taskDao.selectTasksByProductId(productId, sortName, sortOrder, offset, count);

        if (tasks != null && !tasks.isEmpty()) {
            for (TaskVO t : tasks) {
                Tool tool = toolService.getToolById(t.getToolId());
                if (tool == null) {
                    t.setUserType(Constants.TASK_USER_TYPE_UNKNOWN);
                } else if (tool.getMultiUser()) {
                    t.setUserType(Constants.TASK_USER_TYPE_MULTI);
                } else {
                    t.setUserType(Constants.TASK_USER_TYPE_SINGLE);
                }
            }
        }

        Pagination<TaskVO> pagination = new Pagination<TaskVO>(totalCount, page, pageSize);
        pagination.setRows(tasks);
        pagination.setTotal((int) totalCount);
        return pagination;
    }

    public Pagination<TaskAssignmentVO> getTaskAssignmentsByFilter(int productId, Map<String, Object> filters, String sortName, String sortOrder, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        int count = pageSize;

        long totalCount = taskAssignmentDao.countOfTaskAssignmentCountByFilter(productId, filters);

        List<TaskAssignmentVO> taskAssignments = taskAssignmentDao.selectTaskAssignmentsByFilter(productId, filters, sortName, sortOrder, offset, count);

        Pagination<TaskAssignmentVO> pagination = new Pagination<TaskAssignmentVO>(totalCount, page, pageSize);
        pagination.setRows(taskAssignments);

        return pagination;
    }

    public List<TaskRole> getTaskRolesByTaskId(int taskId) {
        return taskRoleDao.selectTaskRolesByTaskId(taskId);
    }

    public void deleteTaskRole(int trid) {
        taskRoleDao.delete(trid);
    }

    public Goal getGoalById(int goalId) {
        return goalDao.get(goalId);
    }

    public int createAssignment(int taskId, int prodId, int targetId, int userId) {
        return taskAssignmentDao.create(taskId, prodId, targetId, userId);
    }

    public Task createTask(Task task) {
        return taskDao.create(task);
    }

    public Task updateTask(Task task) {
        return taskDao.update(task);
    }

    public Task createTask(String name, String desc, int goalId, int productId,
            int toolId, short assignmentMethod, String instructions, short type) {

        Task task = new Task(name, desc, goalId, productId, toolId,
                assignmentMethod, instructions, type);
        return taskDao.create(task);
    }

    public boolean hasOtherTasksOfGoal(int taskId) {
        return taskDao.countTasksOfGoalExcpetTaskId(taskId) > 0;
    }

    public Task validateTaskRelation(int projId, int prodId, int taskId) {
        return taskDao.validateTaskRelation(projId, prodId, taskId);
    }

    public int updateTaskAssignment(int assignmentId, int userId) {
        return taskAssignmentDao.update(assignmentId, userId);
    }

    public Map<Integer, Integer> getTasksubMap(List<Integer> taskIds, int userId) {
        return tasksubDao.selectTasksubMap(taskIds, userId);
    }

    public Tasksub getTasksub(int taskId, int userId) {
        return tasksubDao.selectTasksub(taskId, userId);
    }

    public Tasksub updateTasksub(Tasksub tasksub) {
        return tasksubDao.update(tasksub);
    }

    public Tasksub addTasksub(Tasksub tasksub) {
        return tasksubDao.create(tasksub);
    }

    public List<Task> getClaimableTasks(int projectId, int roleId) {
        return taskDao.selectClaimableTasks(projectId, roleId);
    }

    public List<TaskAssignment> getActiveFlagAssignments() {
        return taskAssignmentDao.selectActiveFlagAssignments();
    }


    static private class UserTaskSummary {
        User user;
        List<TaskAssignment> assignments;
    }

    private void addAssignments(Map<Integer, UserTaskSummary> userTaskMap, List<TaskAssignment> assignments) {
        if (assignments == null || assignments.isEmpty()) return;

        // create users to be processed
        for (TaskAssignment assignment : assignments) {
            int userId = assignment.getAssignedUserId();
            UserTaskSummary uts = userTaskMap.get(userId);
            if (uts == null) {
                // create a new UTS
                uts = new UserTaskSummary();
                uts.assignments = new ArrayList<TaskAssignment>();
                uts.user = userDao.get(userId);
                userTaskMap.put(userId, uts);
            }
            uts.assignments.add(assignment);
        }
    }


    private void generateTaskSummaryForUser(UserTaskSummary taskSummary) {
        if (taskSummary.assignments == null || taskSummary.assignments.isEmpty()) return;

        int numFlagsRaisedByMe = 0;
        int numRespsToFlagsRaisedByMe = 0;
        int numFlagsAssignedToMe = 0;
        int numRespsToFlagsAssignedToMe = 0;

        List<GroupobjFlag> flagsRaisedByMe = groupobjFlagDao.selectAllActiveFlagsRaisedByUser(taskSummary.user.getId());
        if (flagsRaisedByMe != null && !flagsRaisedByMe.isEmpty()) {
            numFlagsRaisedByMe = flagsRaisedByMe.size();
            for (GroupobjFlag flag : flagsRaisedByMe) {
                if (flag.getRespondTime() != null) numRespsToFlagsRaisedByMe++;
            }
        }

        List<GroupobjFlag> flagsAssignedToMe = groupobjFlagDao.selectAllActiveFlagsAssignedToUser(taskSummary.user.getId());
        if (flagsAssignedToMe != null && !flagsAssignedToMe.isEmpty()) {
            numFlagsAssignedToMe = flagsAssignedToMe.size();
            for (GroupobjFlag flag : flagsAssignedToMe) {
                if (flag.getRespondTime() != null) numRespsToFlagsAssignedToMe++;
            }
        }

        if (numFlagsRaisedByMe == 0 && numFlagsAssignedToMe == 0) return;
        
        Map<String, Object> tokenMap = new HashMap<String, Object>();
        tokenMap.put("user", taskSummary.user);
        tokenMap.put("numFlagsRaisedByMe", numFlagsRaisedByMe);
        tokenMap.put("numRespsToFlagsRaisedByMe", numRespsToFlagsRaisedByMe);
        tokenMap.put("numFlagsAssignedToMe", numFlagsAssignedToMe);
        tokenMap.put("numRespsToFlagsAssignedToMe", numRespsToFlagsAssignedToMe);

        NotificationView notifView = notificationItemService.getDefaultNotificationView(
                Constants.NOTIFICATION_TYPE_TASK_SUMMARY, taskSummary.user.getLanguageId(), tokenMap);

        if (notifView != null) {
            // send to user's email
            mailbatchService.addSystemMail(taskSummary.user.getEmail(), notifView.getSubject(), notifView.getBody());
        }
    }


    public void generateTaskSummary() {
        Map<Integer, UserTaskSummary> userTaskMap = new HashMap<Integer, UserTaskSummary>();
        addAssignments(userTaskMap, getActiveFlagAssignments());

        // process each user.
        Iterator it = userTaskMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            UserTaskSummary taskSummary = (UserTaskSummary) pairs.getValue();
            generateTaskSummaryForUser(taskSummary);
        }
    }
    
    
    


    @Autowired
    public void setTaskDao(TaskDAO taskDao) {
        this.taskDao = taskDao;
    }

    @Autowired
    public void setTaskRoleDao(TaskRoleDAO taskRoleDao) {
        this.taskRoleDao = taskRoleDao;
    }

    @Autowired
    public void setTaskAssignmentDao(TaskAssignmentDAO taskAssignmentDao) {
        this.taskAssignmentDao = taskAssignmentDao;
    }

    /*
     @Autowired
     public void setSurveyQuestionDao(SurveyQuestionDAO surveyQuestionDao) {
     this.surveyQuestionDao = surveyQuestionDao;
     }
    
     @Autowired
     public void setSurveyAnswerDao(SurveyAnswerDAO surveyAnswerDao) {
     this.surveyAnswerDao = surveyAnswerDao;
     }
     */
    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    @Autowired
    public void setContentHeaderDao(ContentHeaderDAO contentHeaderDao) {
        this.contentHeaderDao = contentHeaderDao;
    }

    @Autowired
    public void setGoalDao(GoalDAO goalDao) {
        this.goalDao = goalDao;
    }

    @Autowired
    public void setTasksubDao(TasksubDAO tasksubDao) {
        this.tasksubDao = tasksubDao;
    }

    @Autowired
    public void setJournalService(JournalService journalService) {
        this.journalService = journalService;
    }

    @Autowired
    public void setJournalVersionService(JournalVersionService jouralVersionService) {
        this.jouralVersionService = jouralVersionService;
    }

    @Autowired
    public void setToolService(ToolService toolService) {
        this.toolService = toolService;
    }

    @Autowired
    public void setSurveyConfigService(SurveyConfigService srvc) {
        this.surveyConfigService = srvc;
    }

    @Autowired
    public void setSurveyService(SurveyService srvc) {
        this.surveyService = srvc;
    }
}
