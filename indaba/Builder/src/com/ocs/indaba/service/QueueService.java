/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.common.ErrorCode;
import com.ocs.indaba.dao.ProductDAO;
import com.ocs.indaba.dao.RoleDAO;
import com.ocs.indaba.dao.TargetDAO;
import com.ocs.indaba.dao.TaskAssignmentDAO;
import com.ocs.indaba.dao.TaskDAO;
import com.ocs.indaba.dao.UserDAO;
import com.ocs.indaba.po.Task;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.indaba.po.User;
import com.ocs.indaba.util.DateUtils;
import com.ocs.indaba.util.ListUtils;
import com.ocs.indaba.vo.QueueSubmitResultView;
import com.ocs.indaba.vo.QueueTask;
import com.ocs.indaba.vo.QueueTaskAssignment;
import com.ocs.indaba.vo.QueueUser;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.common.Rights;
import com.ocs.indaba.dao.GoalDAO;
import com.ocs.indaba.dao.GoalObjectDAO;
import com.ocs.indaba.dao.HorseDAO;
import com.ocs.indaba.dao.ToolDAO;
import com.ocs.indaba.po.Goal;
import com.ocs.indaba.po.GoalObject;
import com.ocs.indaba.po.Horse;
import com.ocs.indaba.po.Product;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.ProjectMembership;
import com.ocs.indaba.po.TaskRole;
import com.ocs.indaba.po.Tasksub;
import com.ocs.indaba.vo.HorseInfo;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.QueueTaskView;
import com.ocs.indaba.vo.TargetBasedTaskView;
import com.ocs.indaba.vo.TaskAssignmentView;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author luwb
 */
public class QueueService implements RightEvaluator {

    private static final Logger logger = Logger.getLogger(QueueService.class);
    //private static final int ADMIN_ROLE_ID = 3;//admin role Id
    public static final String[] PRIORITY_NAME = {
        Messages.KEY_JSP_QUEUES_LOW,
        Messages.KEY_JSP_QUEUES_MEDIUM,
        Messages.KEY_JSP_QUEUES_HIGH};
    
    public static final int FILTER_STATUS_ALL = 0;
    public static final int FILTER_STATUS_OVER = 1;
    public static final int FILTER_STATUS_INFLIGHT = 2;
    public static final int FILTER_STATUS_STARTING = 3;     // task already starting - waiting for assignment
    public static final int FILTER_STATUS_INACTIVE = 4;

    public static final int ASSIGNED_STATUS_ASSIGNED = 1;
    public static final int ASSIGNED_STATUS_NOT_ASSIGNED = 2;

    private TaskDAO taskDao = null;
    private RoleDAO roleDao = null;
    private GoalObjectDAO goalObjectDao = null;
    private ProductDAO productDao = null;
    private TargetDAO targetDao = null;
    private TaskAssignmentDAO taskAssignmentDao = null;
    private UserDAO userDao = null;
    private ToolDAO toolDao = null;
    private HorseDAO horseDao = null;
    private GoalDAO goalDao = null;

    private TaskService taskService;
    private ProjectService projectService;
    private AccessPermissionService accessPermissionService = null;

    public QueueService() {
        AccessPermissionService.setEvaluator(Rights.SEE_TASK_QUEUES, this);
    }

    
    @Autowired
    public void setTaskDAO(TaskDAO taskDao) {
        this.taskDao = taskDao;
    }

    @Autowired
    public void setRoleDAO(RoleDAO roleDao) {
        this.roleDao = roleDao;
    }

    @Autowired
    public void setGoalObjectDAO(GoalObjectDAO goalObjectDao) {
        this.goalObjectDao = goalObjectDao;
    }

    @Autowired
    public void setTaskAssignmentDAO(TaskAssignmentDAO taskAssignmentDao) {
        this.taskAssignmentDao = taskAssignmentDao;
    }

    @Autowired
    public void setProductDAO(ProductDAO productDao) {
        this.productDao = productDao;
    }

    @Autowired
    public void setToolDAO(ToolDAO toolDao) {
        this.toolDao = toolDao;
    }

    @Autowired
    public void setTargetDAO(TargetDAO targetDao) {
        this.targetDao = targetDao;
    }

    @Autowired
    public void setUserDAO(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setGoalDAO(GoalDAO dao) {
        this.goalDao = dao;
    }

    @Autowired
    public void setAccessPermissionService(AccessPermissionService accessPermissionService) {
        this.accessPermissionService = accessPermissionService;
    }

    @Autowired
    public void setHorseDAO(HorseDAO horseDao) {
        this.horseDao = horseDao;
    }

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Create default task assignment [For v2 Queue]
     * @param horseId
     * @param taskId
     * @param goalObjId
     * @param targetId
     * @return
     */
    public TaskAssignment createDefaultTaskAssignment(int horseId, int taskId, GoalObject goalObj, int targetId) {
        Date dueTime = null;
        switch (goalObj.getStatus()) {
            case Constants.GOAL_OBJECT_STATUS_WAITING:
            case Constants.GOAL_OBJECT_STATUS_STARTING:
                break;

            default:
                // need to set the deadline properly
                Goal goal = goalDao.get(goalObj.getGoalId());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DATE, goal.getDuration());
                dueTime = calendar.getTime();
        }

        TaskAssignment ta = taskAssignmentDao.createDefaultTaskAssignment(horseId, taskId, goalObj, targetId, dueTime);
        return ta;
    }

    /**
     * Create default task assignment [For v2 Queue]
     *
     */
    public TaskAssignmentView createDefaultTaskAssignment(int taskId, int targetId, int languageId) {
        Task task = taskService.getTaskById(taskId);
        Horse horse = horseDao.selectHorseByProductIdAndTargetId(task.getProductId(), targetId);
        Product product = productDao.get(task.getProductId());
        GoalObject goalObj = goalObjectDao.selectGoalObjectsByGoalIdAndWorkflowObjectId(task.getGoalId(), horse.getWorkflowObjectId());
        if (goalObj == null) {
            return null;
        }
        TaskAssignment ta = createDefaultTaskAssignment(horse.getId(), taskId, goalObj, targetId);
        TaskAssignmentView taView = new TaskAssignmentView();
        taView.setPriority(Messages.getInstance().getMessage(PRIORITY_NAME[ta.getQPriority() - 1], languageId));
        taView.setInQueueDays(DateUtils.getIntervalInDays(ta.getQEnterTime(), Calendar.getInstance().getTime()));
        taView.setQualifiedUsers(getQualifiedUsersByTaskId(task.getId(), product.getProjectId()));
        taView.setStatus(ta.getStatus());
        taView.setStatusDisplay(getStatusDisplay(ta, goalObj, languageId));
        taView.setTaskAssignment(ta);
        return taView;
    }
    public int subscribe(int userId, int taskId) {
        Project project = projectService.getProjectByTaskId(taskId);
        ProjectMembership pm = projectService.getProjectMembership(project.getId(), userId);
        if(pm == null || taskService.getTaskRole(taskId, pm.getRoleId()) == null) {
            return ErrorCode.ERR_NO_PERMISSION;
        } else {
            Tasksub tasksub = taskService.getTasksub(taskId, userId);
            if(tasksub == null) {
                tasksub = new Tasksub();
                tasksub.setTaskId(taskId);
                tasksub.setUserId(userId);
                tasksub.setNotify(true);
                tasksub = taskService.addTasksub(tasksub);
            } else {
                tasksub.setNotify(true);
                tasksub = taskService.updateTasksub(tasksub);
            }

            return ErrorCode.OK;
        }
    }

    public int unsubscribe(int userId, int taskId) {
        Project project = projectService.getProjectByTaskId(taskId);
        ProjectMembership pm = projectService.getProjectMembership(project.getId(), userId);
        if(pm == null || taskService.getTaskRole(taskId, pm.getRoleId()) == null) {
            return ErrorCode.ERR_NO_PERMISSION;
        } else {
            Tasksub tasksub = taskService.getTasksub(taskId, userId);
            if(tasksub == null) {
                tasksub = new Tasksub();
                tasksub.setTaskId(taskId);
                tasksub.setUserId(userId);
                tasksub.setNotify(false);
                tasksub = taskService.addTasksub(tasksub);
            } else {
                tasksub.setNotify(false);
                tasksub = taskService.updateTasksub(tasksub);
            }

            return ErrorCode.OK;
        }
    }

    /**
     * Check if task assignment existed [For v2 Queue]
     * @param taskId
     * @param targetId
     * @param assignedUserId
     * @return
     */
    public boolean existsTaskAssignment(int taskId, int targetId, int assignedUserId) {
        return taskAssignmentDao.exists(taskId, targetId, assignedUserId);
    }

    /**
     * Search/Filter tasks [For v2 Queue]
     * @param ta
     * @param goalObj
     * @return
     */
    public List<QueueTaskView> findQueueTasks(boolean isAdmin, int projectId, LoginUser loginUser, int taskStatus, List<Task> tasks, List<Integer> targetIds, int assignedStatus) {
        if (tasks == null || tasks.isEmpty()) return null;

        List<Integer> taskIds = new ArrayList<Integer>();
        for (Task t : tasks) {
            taskIds.add(t.getId());
        }

        List<HorseInfo> horses = horseDao.selectHorsesByProjectIdAndTasksAndTargetIds(projectId, taskIds, targetIds);

        List<TaskAssignment> taskAssignments = null;
       
        taskAssignments = taskAssignmentDao.selectTaskAssignmentsByTaskIds(taskIds);

        List<QueueTaskView> queueTasks = checkAndFixTaskAssignments(isAdmin, projectId, taskStatus, tasks, taskAssignments, horses, loginUser, assignedStatus);
        return queueTasks;
    }


    /**
     * Handle task assignments [For v2 Queue]
     * @param isAdmin
     * @param projectId
     * @param taskStatus
     * @param tasks
     * @param taskAssignments
     * @param horses
     * @param languageId
     * @return
     */
    private List<QueueTaskView> checkAndFixTaskAssignments(boolean isPM, int projectId, int taskStatus, List<Task> tasks, List<TaskAssignment> taskAssignments, List<HorseInfo> horses, LoginUser loginUser, int assignedStatus) {
        // Caching user names (userId :: username)
        List<Integer> assignedUserIds = new ArrayList<Integer>();
        if (taskAssignments != null) {
            for (TaskAssignment ta : taskAssignments) {
                int userId = ta.getAssignedUserId();
                if (userId > 0 && !assignedUserIds.contains(userId)) {
                    assignedUserIds.add(userId);
                }
            }
        }
        Map<Integer, String> userMap = userDao.selectUsernameMap(assignedUserIds);

        // Caching map of tool and multi-user (toolId :: isMultiUser)
        Map<Integer, Integer> toolMap = toolDao.selectToolMultiUserMap();

        List<Integer> taskIds = new ArrayList<Integer>();
        List<Integer> goalIds = new ArrayList<Integer>();

        for (Task task : tasks) {// each task
            taskIds.add(task.getId());
            goalIds.add(task.getGoalId());
        }

        // Caching map of task role(taskId :: can_claim)
        Map<Integer, Integer> trMap = null;
        ProjectMembership pm = projectService.getProjectMembership(projectId, loginUser.getUid());
        if (pm != null) {
            trMap = taskService.getTaskRoleMap(taskIds, pm.getRoleId());
        }
        // Caching map of task subscribe (taskId ::  notify)
        Map<Integer, Integer> tasksubMap = taskService.getTasksubMap(taskIds, loginUser.getUid());
        // Caching map of qualified users (taskId :: user)
        Map<Integer, List<QueueUser>> qualifiedUserMap = userDao.selectUsersByTaskIds(taskIds, projectId);

        List<Integer> workflowObjectIds = new ArrayList<Integer>();
        for (HorseInfo horse : horses) {
            workflowObjectIds.add(horse.getWorkflowObjectId());
        }

        Map<String, GoalObject> goalObjMap = goalObjectDao.selectGoalObjectsByGoalAndWorkflowObjectIdMap(goalIds, workflowObjectIds);

        List<QueueTaskView> queueTasks = new ArrayList<QueueTaskView>();

        for (Task task : tasks) {// each task
            QueueTaskView queueTask = new QueueTaskView();
            queueTask.setTaskId(task.getId());
            queueTask.setProductId(task.getProductId());
            queueTask.setTaskName(task.getTaskName());
            queueTask.setIsMultiUser(toolMap.get(task.getToolId()));

            for (HorseInfo horse : horses) {// each target
                GoalObject goalObj = goalObjMap.get(task.getGoalId() + "-" + horse.getWorkflowObjectId());

                if (goalObj == null) continue;   // don't need to process this horse

                TargetBasedTaskView targetBasedTask = new TargetBasedTaskView();
                int targetId = horse.getTargetId();
                targetBasedTask.setTargetId(targetId);
                targetBasedTask.setTargetName(horse.getTargetName());
                List<TaskAssignment> taList = new ArrayList<TaskAssignment>();
                for (TaskAssignment taElem : taskAssignments) {
                    if (taElem.getTaskId() == task.getId() && taElem.getTargetId() == targetId) {
                        taList.add(taElem);
                    }
                }
                
                if (taList.isEmpty()) {
                    if (goalObj == null) {
                        logger.error("Can't create the default task assignment: goal object is not existed[goalId=" + task.getGoalId() + ", wfoId=" + horse.getWorkflowObjectId() + "]!");
                        continue;
                    }                   
                    taList.add(createDefaultTaskAssignment(horse.getId(), task.getId(), goalObj, targetId));
                }

                if (goalObj != null) {
                    targetBasedTask.setGoalObjectStatus(goalObj.getStatus());
                }

                for (TaskAssignment ta : taList) {
                    boolean selected = false;
                    int taStatus = getTaskAssignmentStatus(ta, goalObj);

                    switch (taskStatus) {
                        case FILTER_STATUS_ALL:
                            selected = true;
                            break;
                        
                        default:
                            selected = (taStatus == taskStatus);
                    }

                    if (!selected) continue;

                    // check assignedStatus
                    if (assignedStatus == ASSIGNED_STATUS_ASSIGNED) {
                        selected = (ta.getAssignedUserId() != 0);
                    } else if (assignedStatus == ASSIGNED_STATUS_NOT_ASSIGNED) {
                        selected = (ta.getAssignedUserId() == 0);
                    }

                    if (!selected) continue;

                    TaskAssignmentView taView = new TaskAssignmentView();
                    taView.setTaskAssignment(ta);
                    if (ta.getAssignedUserId() > 0) {
                        taView.setUsername(userMap.get(ta.getAssignedUserId()));
                    }
                    if (ta.getQPriority() < Constants.TASK_ASSIGNMENT_PRIORITY_LOW || ta.getQPriority() > Constants.TASK_ASSIGNMENT_PRIORITY_HIGH) {
                        ta.setQPriority(Constants.TASK_ASSIGNMENT_PRIORITY_LOW);
                    }
                    String priority = Messages.getInstance().getMessage(Messages.KEY_JSP_QUEUES_LOW, loginUser.getLanguageId());
                    if (ta.getQPriority() > 0 && ta.getQPriority() <= PRIORITY_NAME.length) {
                        priority = Messages.getInstance().getMessage(PRIORITY_NAME[ta.getQPriority() - 1], loginUser.getLanguageId());
                    }
                    taView.setPriority(priority);
                    Date date = ta.getQEnterTime();
                    if (date != null) {
                        int days = DateUtils.getIntervalInDays(date, Calendar.getInstance().getTime());
                        taView.setInQueueDays(days);
                    }
                    int status = ta.getStatus();
                    taView.setStatus(status);
                    taView.setStatusDisplay(getStatusDisplay(ta, goalObj, loginUser.getLanguageId()));
                    if (isPM) {
                        List<QueueUser> queueUsers = qualifiedUserMap.get(task.getId());
                        if (queueUsers == null) {
                            queueUsers = new ArrayList<QueueUser>(1);
                        }
                        taView.setQualifiedUsers(queueUsers);
                    }
                    targetBasedTask.addTaskAssignment(taView);
                }
                if (targetBasedTask.getTaskAssigns() != null && !targetBasedTask.getTaskAssigns().isEmpty()) {
                    queueTask.addTargetBasedTask(targetBasedTask);
                }
            }

            // check if it is can claim or not
            Integer canClaim = (trMap != null && trMap.get(task.getId()) != null) ? trMap.get(task.getId()) : 0;
            if (canClaim != null) {
                queueTask.setCanClaim(canClaim == 1);
            } else {
                queueTask.setCanClaim(false);
            }
            
            Integer notify = (tasksubMap != null) ? tasksubMap.get(task.getId()) : null;
            
            if(notify != null) {
                queueTask.setSubscribed(notify != 0);
            } else {
                // assume true if not explicitly set up
                queueTask.setSubscribed(true);
            }
            logger.debug("Task Subscribed: " + queueTask.isSubscribed());

            if (queueTask.getTargetBasedTasks() != null && !queueTask.getTargetBasedTasks().isEmpty()) {
                queueTasks.add(queueTask);
            }
        }
        return queueTasks;
    }


    private int getTaskAssignmentStatus(TaskAssignment ta, GoalObject goalObj) {
        switch (goalObj.getStatus()) {
            case Constants.GOAL_OBJECT_STATUS_DONE:
                return FILTER_STATUS_OVER;

            case Constants.GOAL_OBJECT_STATUS_WAITING:
                return FILTER_STATUS_INACTIVE;

            case Constants.GOAL_OBJECT_STATUS_STARTING:
                if (ta.getStatus() == Constants.TASK_ASSIGNMENT_STATUS_DONE)
                    return FILTER_STATUS_OVER;

                return FILTER_STATUS_STARTING;

            default:  // started
                if (ta.getStatus() == Constants.TASK_ASSIGNMENT_STATUS_DONE)
                    return FILTER_STATUS_OVER;

                if (ta.getAssignedUserId() == 0) {
                    return FILTER_STATUS_STARTING;
                }

                return FILTER_STATUS_INFLIGHT;
        }
    }

    /**
     * Get readable status [For v2 Queue]
     * @param filterStatus
     * @param languageId
     * @return
     */
    private String getStatusDisplay(TaskAssignment ta, GoalObject goalObj, int languageId) {
        String key;

        switch (getTaskAssignmentStatus(ta, goalObj)) {
            case FILTER_STATUS_OVER:
                key = Messages.KEY_FILTER_STATUS_OVER;
                break;

            case FILTER_STATUS_INACTIVE:
                key = Messages.KEY_FILTER_STATUS_INACTIVE;
                break;

            case FILTER_STATUS_STARTING:
                key = Messages.KEY_FILTER_STATUS_STARTING;
                break;

            default:
                key = Messages.KEY_FILTER_STATUS_INFLIGHT;
        }

        return Messages.getInstance().getMessage(key, languageId);
    }

    /**
     * verify if the user is product manager [For v2 Queue]
     */
    public boolean isProductManager(boolean isSiteAdmin, int projectId, int userId) {
        return (isSiteAdmin || accessPermissionService.checkProjectPermission(projectId, userId, Rights.MANAGE_QUEUES));
    }

    /**
     * Update task assignment [For v2 Queue]
     * @param taId
     * @return
     */
    public int updateTaskAssignment(boolean isPM, int userId, int taId, short priority, int assignUid) {
        int ret;
        TaskAssignment ta = taskAssignmentDao.get(taId);
        if (ta == null) {
            ret = ErrorCode.ERR_NON_EXISTENT;
        } else {            
            if (isPM) {
                if (assignUid != 0 && existsTaskAssignment(ta.getTaskId(), ta.getTargetId(), assignUid)) {
                    ret = ErrorCode.ERR_ALREADY_EXISTS;
                } else {
                    ta.setQPriority(priority);
                    ret = ErrorCode.OK;
                }
            } else {
                if (userId == ta.getAssignedUserId()) {
                    if (assignUid == 0) { // return to queue                        
                        ret = ErrorCode.OK;
                    } else {
                        ret = ErrorCode.ERR_NO_PERMISSION;
                    }
                } else if (ta.getAssignedUserId() == 0) { // I want this
                    Project project = projectService.getProjectByTaskId(ta.getTaskId());
                    ProjectMembership pm = projectService.getProjectMembership(project.getId(), userId);
                    if (pm == null) {
                        ret = ErrorCode.ERR_NO_PERMISSION;
                    } else {
                        TaskRole taskRole = taskService.getTaskRole(ta.getTaskId(), pm.getRoleId());
                        if (taskRole == null || taskRole.getCanClaim() != 1) {
                            ret = ErrorCode.ERR_NO_PERMISSION;
                        } else {                            
                            ret = ErrorCode.OK;
                        }
                    }
                } else {
                    ret = ErrorCode.ERR_NO_PERMISSION;
                }
            }
        }

        if (ret == ErrorCode.OK) {
            ta.setQLastAssignedUid(ta.getAssignedUserId());
            ta.setQLastAssignedTime(new Date());
            ta.setAssignedUserId(assignUid);
            if (assignUid == 0) {
                // treat it as returned back to queue
                ta.setQLastReturnTime(new Date());
            }
            taskAssignmentDao.update(ta);
        }
        return ret;
    }


    /**
     * Delete task assignment [For v2 Queue]
     * @param taId
     * @return
     */
    public int delTaskAssignment(int taId) {
        TaskAssignment ta = taskAssignmentDao.get(taId);
        int errCode = ErrorCode.ERR_NON_EXISTENT;
        if (ta != null) {
            long count = taskAssignmentDao.countTaskAssignmentByTaskTarget(ta.getTaskId(), ta.getTargetId());
            if (count > 1) {
                taskAssignmentDao.delete(taId);
                errCode = ErrorCode.OK;
            } else {
                errCode = ErrorCode.ERR_LOWER_MINIUM;
            }
        }
        return errCode;
    }

    /**
     * verify if the user can access the taskAssignment
     */
    public boolean isQualifiedUser(int projectId, int userId, int taskId) {
        List<Integer> userRoleList = roleDao.selectRoleIdByProjectIdAndUserId(projectId, userId);
        if (ListUtils.isEmptyList(userRoleList)) {
            return false;
        }

        String strUserRoleIds = ListUtils.listToString(userRoleList);
        List<Integer> qualifiedRoleIds = roleDao.selectRoleIdByTaskIdAndRoleIds(taskId, strUserRoleIds);
        if (ListUtils.isEmptyList(qualifiedRoleIds)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Get qualified users
     */
    private List<QueueUser> getQualifiedUsersByTaskId(int taskId, int projectId) {
        return userDao.selectUsersByTaskId(taskId, projectId);
    }

    /**
     * verify if the user is admin
     * @deprecated It was used for the 1st Queue version.
     * @see isProductManager
     */
    public boolean isAdminUser(int projectId, int userId) {
        return accessPermissionService.checkProjectPermission(projectId, userId, Rights.MANAGE_QUEUES);
    }

    /**
     * verify if the task's method is queue
     * @deprecated It was used for the 1st Queue version.
     * @see
     */
    public boolean isQueueTask(int taskId) {
        return taskDao.get(taskId).getAssignmentMethod() == taskDao.TASK_ASSIGNMENT_METHOD_QUEUE;
    }

    /**
     * @deprecated It was used for the 1st Queue version.
     * @see
     */
    public List<QueueTask> getQueueTasks(int projectId, int userId, int languageId) {
        logger.debug("Get queueTasks based on projectId " + projectId + " userId " + userId);
        //get tasks relate to project id
        List<Task> taskList = taskDao.selectQueueTaskByProjectId(projectId);
        if (ListUtils.isEmptyList(taskList)) {
            return null;
        }

        boolean isAdmin = this.isAdminUser(projectId, userId);
        if (isAdmin) {// admin users
            List<QueueTask> queueTaskList = new ArrayList<QueueTask>(taskList.size());
            for (Task task : taskList) {
                //get the task_assignment of this task whose status is not inactive
                List<TaskAssignment> taskAssignmentList = taskAssignmentDao.selectQueueTAByTaskId(task.getId());
                if (ListUtils.isEmptyList(taskAssignmentList)) {
                    continue;
                }

                QueueTask queueTask = getTaskView(projectId, task, userId, languageId, true, taskAssignmentList);
                queueTaskList.add(queueTask);
            }
            return queueTaskList;
        } else {
            //get user's role based on project id and user id
            List<Integer> userRoleList = roleDao.selectRoleIdByProjectIdAndUserId(projectId, userId);
            if (ListUtils.isEmptyList(userRoleList)) {
                return null;
            }

            //get the string of user's role,the format is 1,3,4... will be used for query
            String strUserRoleIds = ListUtils.listToString(userRoleList);

            List<QueueTask> queueTaskList = new ArrayList<QueueTask>(taskList.size());
            for (Task task : taskList) {
                //get the intersection between user's role and task's role
                // Only select the roles that can claim tasks from queue - YC
                List<Integer> qualifiedRoleIds = roleDao.selectClaimableRoleIdByTaskIdAndRoleIds(task.getId(), strUserRoleIds);
                if (ListUtils.isEmptyList(qualifiedRoleIds))// no intersection, measn the user can't access the task
                {
                    continue;
                }
                //get the task_assignment of this task whose status is not inactive
                List<TaskAssignment> taskAssignmentList = taskAssignmentDao.selectQueueTAByTaskId(task.getId());
                if (ListUtils.isEmptyList(taskAssignmentList)) {
                    continue;
                }

                QueueTask queueTask = getTaskView(projectId, task, userId, languageId, false, taskAssignmentList);
                queueTaskList.add(queueTask);
            }
            return queueTaskList;
        }
    }

    /**
     * @deprecated It was used for the 1st Queue version.
     * @see
     */
    public List<QueueTask> getQueueTasksByProductId(int productId) {
        List<Task> taskList = taskDao.selectQueueTaskByProjectId(productId);
        if (ListUtils.isEmptyList(taskList)) {
            return null;
        }
        return null;
    }

    /**
     * @deprecated It was used for the 1st Queue version.
     * @see
     */
    private QueueTask getTaskView(int projectId, Task task, int userId, int languageId, boolean isAdmin, List<TaskAssignment> taskAssignmentList) {
        QueueTask queueTask = new QueueTask();
        queueTask.setTaskName(task.getTaskName());
        queueTask.setTaskDes(task.getDescription());
        queueTask.setProductName(productDao.selectProductNameById(task.getProductId()));
        queueTask.setIsAdmin(isAdmin);

        long totalAssignMseconds = 0;//the total time to assign
        int totalAssignNums = 0;//the total numbers of task
        long totalCompleteMseconds = 0;//the total time to complete
        int totalCompleteNums = 0;
        List<QueueTaskAssignment> queueTaList = new ArrayList<QueueTaskAssignment>(taskAssignmentList.size());
        for (TaskAssignment ta : taskAssignmentList) {
            int status = ta.getStatus();
            if (ta.getAssignedUserId() > 0) {//is assigned
                totalAssignNums++;
                totalAssignMseconds += DateUtils.getIntervalInMilliSeconds(ta.getQEnterTime(), ta.getQLastAssignedTime());
                if (status == Constants.TASK_ASSIGNMENT_STATUS_DONE) {
                    totalCompleteNums++;
                    totalCompleteMseconds += DateUtils.getIntervalInMilliSeconds(ta.getStartTime(), ta.getCompletionTime());
                }
            }
            if (isShowableTaskAssignment(ta)) {
                QueueTaskAssignment queueTa = getTaskAssignmentView(userId, languageId, ta);
                queueTaList.add(queueTa);
            }
        }
        if (totalAssignNums != 0) {
            queueTask.setAvgTimeToAssign(DateUtils.milliSecondsToDays(totalAssignMseconds / totalAssignNums));
        } else {
            queueTask.setAvgTimeToAssign(0);
        }
        if (totalCompleteNums != 0) {
            queueTask.setAvgTimeToComplete(DateUtils.milliSecondsToDays(totalCompleteMseconds / totalCompleteNums));
        } else {
            queueTask.setAvgTimeToComplete(0);
        }
        queueTask.setTaskAssignmentList(queueTaList);
        //if the user is admin, generate the user list which can be choose to assign
        if (isAdmin) {
            List<QueueUser> queueUsers = getQualifiedUsersByTaskId(task.getId(), projectId);
            QueueUser noUser = new QueueUser();//add NO ONE user to fix bug 623
            noUser.setUserId(0);
            noUser.setUserName(Constants.QUEUE_ASSIGN_NOBODY);
            queueUsers.add(noUser);
            queueTask.setQualifiedUsers(queueUsers);
        }
        return queueTask;
    }

    /**
     * @deprecated It was used for the 1st Queue version.
     * @see
     */
    private QueueTaskAssignment getTaskAssignmentView(int userId, int languageId, TaskAssignment ta) {
        QueueTaskAssignment queueTa = new QueueTaskAssignment();
        queueTa.setId(ta.getId());
        queueTa.setTargetName(targetDao.selectTargetById(ta.getTargetId()).getShortName());
        String priority = Messages.getInstance().getMessage(Messages.KEY_JSP_QUEUES_LOW, languageId);
        if (ta.getQPriority() > 0 && ta.getQPriority() <= PRIORITY_NAME.length) {
            priority = Messages.getInstance().getMessage(PRIORITY_NAME[ta.getQPriority() - 1], languageId);
        }
        //if(ta.getQPriority() > 0 && ta.getQPriority() <= PRIORITY_NAME.length)
        //  priority = PRIORITY_NAME[ta.getQPriority()-1];
        queueTa.setPriority(priority);
        if (ta.getAssignedUserId() > 0) {
            queueTa.setAssigned(true);
            queueTa.setAssignedUserId(ta.getAssignedUserId());
            if (ta.getAssignedUserId() == userId) {
                queueTa.setAssignedToMe(true);
            }
            if (ta.getStatus() == Constants.TASK_ASSIGNMENT_STATUS_DONE) {
                queueTa.setIsDone(true);
            }
            User user = userDao.get(ta.getAssignedUserId());
            queueTa.setAssignedUserName(user.getFirstName() + " " + user.getLastName());
        }
        Date date = ta.getQEnterTime();
        if (date != null) {
            int days = DateUtils.getIntervalInDays(date, Calendar.getInstance().getTime());
            queueTa.setInQueueDays(days);
        }
        return queueTa;
    }

    /**
     * @deprecated It was used for the 1st Queue version.
     * @see
     */
    private boolean isShowableTaskAssignment(TaskAssignment ta) {
        int status = ta.getStatus();
        if (status == Constants.TASK_ASSIGNMENT_STATUS_DONE || ta.getQEnterTime() == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * verify if the user can assign the taskAssignment
     * @deprecated It was used for the 1st Queue version.
     * @see
     */
    public boolean canApplyTaskAssignment(int projectId, int userId, int taskAssignmentId) {
        TaskAssignment ta = taskAssignmentDao.get(taskAssignmentId);
        if (ta == null) {
            return false;
        }
        if (!isQueueTask(ta.getTaskId())) {
            return false;
        }
        if (!this.isShowableTaskAssignment(ta)) {
            return false;
        }
        if (this.isAdminUser(projectId, userId)) {
            return true;
        }
        return isQualifiedUser(projectId, userId, ta.getTaskId());
    }

    /**
     * @deprecated It was used for the 1st Queue version.
     * @see
     */
    public boolean canReturnTaskAssignment(int userId, int taskAssignmentId) {
        TaskAssignment ta = taskAssignmentDao.get(taskAssignmentId);
        if (ta == null) {
            return false;
        }
        if (ta.getAssignedUserId() == 0) {
            return false;
        }
        if (!isQueueTask(ta.getTaskId())) {
            return false;
        }
        if (!this.isShowableTaskAssignment(ta)) {
            return false;
        }
        return userId == ta.getAssignedUserId();
    }

    /**
     * @deprecated It was used for the 1st Queue version.
     * @see
     */
    public boolean canUpdateTaskAssignment(int projectId, int userId, int taskAssignmentId, int selectUserId) {
        TaskAssignment ta = taskAssignmentDao.get(taskAssignmentId);
        if (ta == null) {
            return false;
        }
        if (!isQueueTask(ta.getTaskId())) {
            return false;
        }
        if (!this.isShowableTaskAssignment(ta)) {
            return false;
        }
        if (!isAdminUser(projectId, userId)) {
            return false;
        }
        //return isQualifiedUser(projectId, selectUserId, ta.getTaskId());
        return true;
    }

    /**
     * @deprecated It was used for the 1st Queue version.
     * @see
     */
    public QueueTaskAssignment getTaskAssignmentView(int userId, int languageId, int taskAssignmentId) {
        TaskAssignment ta = taskAssignmentDao.get(taskAssignmentId);
        return getTaskAssignmentView(userId, languageId, ta);
    }

    /**
     * @deprecated It was used for the 1st Queue version.
     * @see updateTaskAssignment
     */
    public QueueSubmitResultView submitApplyTaskAssignment(int userId, int taskAssignmentId) {
        try {
            taskAssignmentDao.updateApplyStatId(taskAssignmentId, userId);
            QueueSubmitResultView view = new QueueSubmitResultView();
            User user = userDao.get(userId);
            view.setAssignedUserId(userId);
            view.setAssignedUserName(user.getFirstName() + " " + user.getLastName());
            return view;
        } catch (SQLException ex) {
            logger.error("update apply taskAssignment error" + ex);
            return null;
        }
    }

    /**
     * @deprecated It was used for the 1st Queue version.
     * @see updateTaskAssignment
     */
    public boolean submitReturnTaskAssignment(int userId, int taskAssignmentId) {
        try {
            taskAssignmentDao.updateReturnStatId(taskAssignmentId, userId);
            return true;
        } catch (SQLException ex) {
            logger.error("update return taskAssignment error" + ex);
            return false;
        }
    }

    /**
     * @deprecated It was used for the 1st Queue version.
     * @see updateTaskAssignment
     */
    public QueueSubmitResultView submitUpdateTaskAssignment(int taskAssignmentId, int selectUserId, int selectPriority) {
        try {
            if (selectUserId == 0) {//assign to NO ONE
                TaskAssignment ta = this.taskAssignmentDao.get(taskAssignmentId);
                if (ta.getAssignedUserId() > 0)//if not assign to any user, don't update q_last_return_time
                {
                    this.taskAssignmentDao.updateReturnStatId(taskAssignmentId, ta.getAssignedUserId());
                }

                this.taskAssignmentDao.updatePriority(taskAssignmentId, selectPriority);
                QueueSubmitResultView view = new QueueSubmitResultView();
                view.setAssignedUserId(selectUserId);
                view.setAssignedUserName(Constants.QUEUE_ASSIGN_NOBODY);
                String priority = PRIORITY_NAME[selectPriority - 1];
                view.setPriority(priority);
                return view;
            } else {
                taskAssignmentDao.updateAssignAndPriorityId(taskAssignmentId, selectUserId, selectPriority);
                QueueSubmitResultView view = new QueueSubmitResultView();
                User user = userDao.get(selectUserId);
                view.setAssignedUserId(selectUserId);
                view.setAssignedUserName(user.getFirstName() + " " + user.getLastName());
                String priority = PRIORITY_NAME[selectPriority - 1];
                view.setPriority(priority);

                /*** Don't send notification any more - YC 9/27/2010
                sendNotification(taskAssignmentId, user);
                 ****/
                return view;
            }
        } catch (SQLException ex) {
            logger.error("update apply taskAssignment error" + ex);
            return null;
        }
    }

    /*
    private void sendNotification(int taskAssignmentId, User user){

    Map<String, String> parameters = new HashMap<String, String>();
    String userName = user.getFirstName() + " " + user.getLastName();
    TaskAssignment ta = this.taskAssignmentDao.get(taskAssignmentId);
    Task task = taskDao.get(ta.getTaskId());
    Target target = targetDao.get(ta.getTargetId());
    Product product = productDao.get(task.getProductId());
    Project project = projectDao.get(product.getProjectId());
    parameters.put(Constants.NOTIFICATION_TOKEN_USER_NAME, userName);
    parameters.put(Constants.NOTIFICATION_TOKEN_TASK_NAME, task.getTaskName());
    parameters.put(Constants.NOTIFICATION_TOKEN_TARGET_NAME, target.getName());
    parameters.put(Constants.NOTIFICATION_TOKEN_PRODUCT_NAME, product.getName());
    parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_NAME, project.getCodeName());
    parameters.put("contenttitle", target.getName() + ' ' + product.getName());

    siteMessageService.deliver(user.getId(), Constants.NOTIFICATION_TYPE_SYS_TASK_ASSIGNED, parameters);
    }
     * 
     */


    public int evaluate(String rightName, Project project, int userId, int userRoleId) {
        // evaluate the SEE_TASK_QUEUES right
        // only if the user has any claimable tasks
        logger.debug("Evaluating right [" + rightName + "]");

        // see if the right is set to allow access
        int result = accessPermissionService.checkProjectPermissionMatrix(project, userRoleId, rightName);

        if (result == Constants.ACCESS_PERMISSION_NO) return RightEvaluator.NO;

        int projectId = project.getId();

        List<Task> tasks = taskService.getClaimableTasks(projectId, userRoleId);

        if (tasks == null || tasks.isEmpty()) {
            logger.debug("No claimable tasks for user role " + userRoleId + " in project " + projectId);
            return RightEvaluator.NO;
        }
        else {
            logger.debug("Number of claimable tasks for user role " + userRoleId + " in project " + projectId + " : " + tasks.size());
            return RightEvaluator.YES;
        }
    }


}
