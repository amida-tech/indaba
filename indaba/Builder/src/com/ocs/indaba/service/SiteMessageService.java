/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.ErrorCode;
import com.ocs.indaba.dao.*;
import com.ocs.indaba.po.*;
import com.ocs.indaba.vo.NotificationView;
import com.ocs.indaba.vo.ProjectUserView;
import com.ocs.indaba.vo.TaskAssignmentAlertInfo;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class SiteMessageService {

    private static final Logger logger = Logger.getLogger(SiteMessageService.class);

    private NotificationItemService notificationItemService = null;
    private EventService eventService = null;
    private MessageDAO messageDao = null;
    private MessageboardDAO messageboardDao = null;
    private UserDAO userDao = null;
    //private TaskDAO taskDao = null;
    private TaskAssignmentDAO taDao = null;
    private ContentHeaderDAO contentHeaderDao = null;
    //private ProductDAO productDao = null;
    private ProjectDAO projectDao = null;
    private TargetDAO targetDao = null;
    private GoalDAO goalDao = null;
    private GoalObjectDAO goalObjectDao = null;
    private MailbatchService mailbatchService = null;

    /**
     * The following is the logic for site message delivery:
     * 1. A site message is ALWAYS delivered to the user's inbox (the user's message board).<br/>
     *
     * 2. If the user's forward_inbox_msg flag (in user table) is set to true, then the site
     * message is also sent to the user's email box.<br/>
     *
     * 2.1 If the user's email_detail_level is set to FULL_MESSAGE, then send the site message to
     * the user's email address.<br/>
     *
     * 2.2 If the user's email_detail_level is set to ALERT_ONLY, then don't send the site message
     * to the user. Instead send the "Site Message Alert" notification (as defined in the notification_
     * type table) to the user's email address. Please note that the <username> token should be replaced
     * by the user's actual name before sending the email.
     * 
     * @param userId - user id to be delivered
     * @param notificationTypeName - notificaton type name(refer to 'notification_type'). Also defined in Constants.java, e.g. Constants.NOTIFICATION_TYPE_NOTIFICATION_TYPE_xxxx.
     * @param parameters - parameter map. Key: parameter name; Value: value
     *
     * @return the result o f delivery. 0: OK; others: error
     */
    public int deliver(int projectId, int userId, String notificationTypeName, Map<String, String> parameters) {
        logger.debug("Deliver message. [projectId=" + projectId + " userId=" + userId + ", notificationTypeName=" + notificationTypeName + "].");
        // Add msg to message_board
        ProjectUserView user = userDao.selectProjectUser(projectId, userId);
        if (user == null) {
            logger.debug("******ERROR: USER NOT FOUND - " + userId);
            return 0;
        }

        return deliver(user, notificationTypeName, parameters);
    }


    public int deliver(ProjectUserView user, String notificationTypeName, Map<String, String> parameters) {

        if (user == null) {
            return 0;
        }

        NotificationView notification = notificationItemService.getProjectNotificationView(
                notificationTypeName, user.getProjectId(), user.getRoleId(), user.getLanguageId(), parameters);

        if (notification == null) {
            logger.debug("******ERROR: CAN'T FIND NOTIFICATION " + notificationTypeName);
            return 0;
        }

        int msgBoardId = user.getMsgboardId();
        if (msgBoardId <= 0) {  // create messageBoard if not existed
            Msgboard msgBoard = new Msgboard();
            msgBoard.setCreateTime(new Date());
            messageboardDao.create(msgBoard);
            msgBoardId = msgBoard.getId();
            user.setMsgboardId(msgBoardId);
            userDao.updateUserMsgboardId(user.getId(), msgBoardId);
        }

        postMessage(msgBoardId, notification);

        /**
        int result = ErrorCode.OK;
        if (user.getForwardInboxMsg()) {
            try {
                SendMail sendMail = new SendMail(smtpHost, smtpPort, needAuth, username, password);
                result = sendMail.send(sender, user.getEmail(), notification.getSubject(), notification.getBody().replaceAll("(\r\n|\r|\n|\n\r)", "<br>"));
            } catch (Exception e) {
                // No email server configured
            }
        }
         * **/

        /* DO NOT replace line breaks with <br> since we now only send plain text email! YC - 2014/11/05
        mailbatchService.addSystemMail(user.getEmail(), notification.getSubject(),
            notification.getBody().replaceAll("(\r\n|\r|\n|\n\r)", "<br>"));
         * 
         */

        mailbatchService.addSystemMail(user.getEmail(), notification.getSubject(),
            notification.getBody());

        return ErrorCode.OK;
    }


    public void postMessage(int msgBoardId, NotificationView notification) {
        Message msg = new Message();
        msg.setTitle(notification.getSubject());
        msg.setBody(notification.getBody());
        msg.setCreatedTime(new Date());
        msg.setAuthorUserId(0);
        msg.setMsgboardId(msgBoardId);
        messageDao.create(msg);
    }


    public void postToProjectWall(TaskAssignment ta, String notificationTypeName) {
        // Since we don't know the language of the project, we'll use English.
        // In future, a project should have a default language for the wall  - YC
        postToProjectWall(ta, Constants.LANG_EN, notificationTypeName);
    }

    
    public void postToProjectWall(TaskAssignment ta, int langId, String notificationTypeName) {
        if (eventService.findMsgEvent(ta, notificationTypeName))
            return;

        /*
        ContentHeader ch = contentHeaderDao.selectContentHeaderByHorseId(ta.getHorseId());
        User user = userDao.get(ta.getAssignedUserId());
        Task task = taskDao.get(ta.getTaskId());
        Product product = productDao.get(task.getProductId());
        Project project = projectDao.get(product.getProjectId());

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(Constants.NOTIFICATION_TOKEN_USER_NAME, user.getFirstName()+" "+user.getLastName());
        parameters.put(Constants.NOTIFICATION_TOKEN_CONTENT_TITLE, ch.getTitle());
        parameters.put(Constants.NOTIFICATION_TOKEN_TASK_NAME, task.getTaskName());

        TaskAssignmentAlertInfo taaInfo = taDao.selectTAAlertInfo(ta.getId());
        //parameters.put("duetime", taaInfo.getDueTime());
        parameters.put(Constants.NOTIFICATION_TOKEN_TASK_DUE_TIME, taaInfo.getDueTime().toString());

        Target target = targetDao.get(ta.getTargetId());

        parameters.put(Constants.NOTIFICATION_TOKEN_PRODUCT_NAME, product.getName());
        parameters.put(Constants.NOTIFICATION_TOKEN_TARGET_NAME, target.getName());
        parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_NAME, project.getCodeName());
        
        //
        parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_START_TIME, project.getStartTime().toString());
        long days = ((new Date()).getTime() - taaInfo.getDueTime().getTime()) / (1000*3600*24);
        if (days > 0) {
            parameters.put(Constants.NOTIFICATION_TOKEN_DAYS_BEFORE_DUE, Long.toString(days));
            parameters.put(Constants.NOTIFICATION_TOKEN_DAYS_AFTER_DUE, "0");
        } else {
            parameters.put(Constants.NOTIFICATION_TOKEN_DAYS_BEFORE_DUE, "0");
            parameters.put(Constants.NOTIFICATION_TOKEN_DAYS_AFTER_DUE, Long.toString(days*-1));
        }
        
        User adminUser = userDao.get(project.getAdminUserId());
        parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_ADMIN, adminUser.getFirstName()+" "+adminUser.getLastName());
        */

        Map parameters = getParameters(ta);
        Project project = projectDao.selectProjectByTaskAssignment(ta.getId());
        postToProjectWall(project, notificationTypeName, langId, parameters);

        eventService.addMsgEvent(ta, notificationTypeName);
    }

    public int postToProjectWall(Project project, String notificationTypeName, int langId, Map<String, String> parameters) {
        NotificationView notification = notificationItemService.getDefaultNotificationView(notificationTypeName, langId, parameters);
        if (notification == null) {
            logger.info("******ERROR: CAN'T FIND NOTIFICATION " + notificationTypeName);
            return 0;
        }

        // Add msg to message_board
        int msgBoardId = project.getMsgboardId();
        if (msgBoardId <= 0) {  // create messageBoard if not existed
            Msgboard msgBoard = new Msgboard();
            msgBoard.setCreateTime(new Date());
            messageboardDao.create(msgBoard);
            msgBoardId = msgBoard.getId();
            project.setMsgboardId(msgBoardId);
            projectDao.updateMsgboardId(project.getId(), msgBoardId);
        }
        postMessage(msgBoardId, notification);
        return 1;
    }

    public void notifyAuthor(TaskAssignment ta, String notificationTypeName) {
        //User user = userDao.selectAuthorByHouseId(ta.getHorseId());
        if (eventService.findMsgEvent(ta, notificationTypeName))
            return;

        if (ta == null) 
            return;

        ContentHeader ch = contentHeaderDao.selectContentHeaderByHorseId(ta.getHorseId());
        Project project = projectDao.selectProjectByTaskAssignment(ta.getId());
        ProjectUserView user = userDao.selectProjectUser(project.getId(), ch.getAuthorUserId());

        /*
        Task task = taskDao.get(ta.getTaskId());

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(Constants.NOTIFICATION_TOKEN_USER_NAME, user.getFirstName()+" "+user.getLastName());
        parameters.put(Constants.NOTIFICATION_TOKEN_CONTENT_TITLE, ch.getTitle());
        parameters.put(Constants.NOTIFICATION_TOKEN_TASK_NAME, task.getTaskName());

        TaskAssignmentAlertInfo taaInfo = taDao.selectTAAlertInfo(ta.getId());
        //parameters.put("duetime", taaInfo.getDueTime());
        parameters.put(Constants.NOTIFICATION_TOKEN_TASK_DUE_TIME, taaInfo.getDueTime().toString());

        Product product = productDao.get(task.getProductId());
        Project project = projectDao.get(product.getProjectId());
        Target target = targetDao.get(ta.getTargetId());

        parameters.put(Constants.NOTIFICATION_TOKEN_PRODUCT_NAME, product.getName());
        parameters.put(Constants.NOTIFICATION_TOKEN_TARGET_NAME, target.getName());
        parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_NAME, project.getCodeName());

        parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_START_TIME, project.getStartTime().toString());
        long days = ((new Date()).getTime() - taaInfo.getDueTime().getTime()) / (1000*3600*24);
        if (days > 0) {
            parameters.put(Constants.NOTIFICATION_TOKEN_DAYS_BEFORE_DUE, Long.toString(days));
            parameters.put(Constants.NOTIFICATION_TOKEN_DAYS_AFTER_DUE, "0");
        } else {
            parameters.put(Constants.NOTIFICATION_TOKEN_DAYS_BEFORE_DUE, "0");
            parameters.put(Constants.NOTIFICATION_TOKEN_DAYS_AFTER_DUE, Long.toString(days*-1));
        }

        User adminUser = userDao.get(project.getAdminUserId());
        parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_ADMIN, adminUser.getFirstName()+" "+adminUser.getLastName());
        */

        Map parameters = getParameters(ta, user);
        deliver(user, notificationTypeName, parameters);

        eventService.addMsgEvent(ta, notificationTypeName);
    }

    public void reportToProjectAdmin(GoalObject goalObject, TaskAssignment ta, String notificationTypeName) {
        if (eventService.findMsgEventByGoalObject(goalObject, notificationTypeName))
            return;

        /*
        Goal goal = goalDao.get(goalObject.getGoalId());
        Task task = taskDao.get(ta.getTaskId());
        Product product = productDao.get(task.getProductId());
        Project project = projectDao.get(product.getProjectId());
        Target target = targetDao.get(ta.getTargetId());
        User user = userDao.get(project.getAdminUserId());
        ContentHeader ch = contentHeaderDao.selectContentHeaderByHorseId(ta.getHorseId());

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(Constants.NOTIFICATION_TOKEN_USER_NAME, user.getFirstName() + " " + user.getLastName());
        parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_ADMIN, user.getFirstName()+" "+user.getLastName());
        parameters.put(Constants.NOTIFICATION_TOKEN_GOAL_NAME, goal.getName());

        parameters.put(Constants.NOTIFICATION_TOKEN_PRODUCT_NAME, product.getName());
        parameters.put(Constants.NOTIFICATION_TOKEN_TARGET_NAME, target.getName());
        parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_NAME, project.getCodeName());
        parameters.put(Constants.NOTIFICATION_TOKEN_CONTENT_TITLE, ch.getTitle());
        parameters.put(Constants.NOTIFICATION_TOKEN_TASK_NAME, task.getTaskName());

        Date dueTime = goalObject.getEnterTime();
        dueTime.setTime(dueTime.getTime() + 1000*3600*24*goal.getDuration());
        parameters.put(Constants.NOTIFICATION_TOKEN_GOAL_DUE_TIME, dueTime.toString());

        TaskAssignmentAlertInfo taaInfo = taDao.selectTAAlertInfo(ta.getId());
        //parameters.put("duetime", taaInfo.getDueTime());
        parameters.put(Constants.NOTIFICATION_TOKEN_TASK_DUE_TIME, taaInfo.getDueTime().toString());

        parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_START_TIME, project.getStartTime().toString());
        long days = ((new Date()).getTime() - taaInfo.getDueTime().getTime()) / (1000*3600*24);
        if (days > 0) {
            parameters.put(Constants.NOTIFICATION_TOKEN_DAYS_BEFORE_DUE, Long.toString(days));
            parameters.put(Constants.NOTIFICATION_TOKEN_DAYS_AFTER_DUE, "0");
        } else {
            parameters.put(Constants.NOTIFICATION_TOKEN_DAYS_BEFORE_DUE, "0");
            parameters.put(Constants.NOTIFICATION_TOKEN_DAYS_AFTER_DUE, Long.toString(days*-1));
        }

        User adminUser = userDao.get(project.getAdminUserId());
        parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_ADMIN, adminUser.getFirstName()+" "+adminUser.getLastName());
        */
        
        Project project = projectDao.selectProjectByTaskAssignment(ta.getId());
        ProjectUserView user = userDao.selectProjectUser(project.getId(), project.getAdminUserId());
        Map parameters = getParameters(ta, user, goalObject);
        deliver(user, notificationTypeName, parameters);

        eventService.addMsgEventByGoalObject(goalObject, notificationTypeName);
    }

    public void notifyProjectAdminGoalExit(GoalObject goalObject, String notificationTypeName) {
        if (goalObject == null) return;

        if (eventService.findMsgEventByGoalObject(goalObject, notificationTypeName))
            return;

        /*
        Goal goal = goalDao.get(goalObject.getGoalId());
        Task task = taskDao.get(ta.getTaskId());
        Product product = productDao.get(task.getProductId());
        Project project = projectDao.get(product.getProjectId());
        Target target = targetDao.get(ta.getTargetId());
        User user = userDao.get(project.getAdminUserId());
        ContentHeader ch = contentHeaderDao.selectContentHeaderByHorseId(ta.getHorseId());

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(Constants.NOTIFICATION_TOKEN_USER_NAME, user.getFirstName() + " " + user.getLastName());
        parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_ADMIN, user.getFirstName()+" "+user.getLastName());
        parameters.put(Constants.NOTIFICATION_TOKEN_GOAL_NAME, goal.getName());

        parameters.put(Constants.NOTIFICATION_TOKEN_PRODUCT_NAME, product.getName());
        parameters.put(Constants.NOTIFICATION_TOKEN_TARGET_NAME, target.getName());
        parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_NAME, project.getCodeName());
        parameters.put(Constants.NOTIFICATION_TOKEN_CONTENT_TITLE, ch.getTitle());
        parameters.put(Constants.NOTIFICATION_TOKEN_TASK_NAME, task.getTaskName());

        Date dueTime = goalObject.getEnterTime();
        dueTime.setTime(dueTime.getTime() + 1000*3600*24*goal.getDuration());
        parameters.put(Constants.NOTIFICATION_TOKEN_GOAL_DUE_TIME, dueTime.toString());

        TaskAssignmentAlertInfo taaInfo = taDao.selectTAAlertInfo(ta.getId());
        //parameters.put("duetime", taaInfo.getDueTime());
        parameters.put(Constants.NOTIFICATION_TOKEN_TASK_DUE_TIME, taaInfo.getDueTime().toString());

        parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_START_TIME, project.getStartTime().toString());
        long days = ((new Date()).getTime() - taaInfo.getDueTime().getTime()) / (1000*3600*24);
        if (days > 0) {
            parameters.put(Constants.NOTIFICATION_TOKEN_DAYS_BEFORE_DUE, Long.toString(days));
            parameters.put(Constants.NOTIFICATION_TOKEN_DAYS_AFTER_DUE, "0");
        } else {
            parameters.put(Constants.NOTIFICATION_TOKEN_DAYS_BEFORE_DUE, "0");
            parameters.put(Constants.NOTIFICATION_TOKEN_DAYS_AFTER_DUE, Long.toString(days*-1));
        }

        User adminUser = userDao.get(project.getAdminUserId());
        parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_ADMIN, adminUser.getFirstName()+" "+adminUser.getLastName());
        */
        List<TaskAssignment> taList = taDao.selectUnfinishedTaskAssignmentsByGoalObject(goalObject.getId());

        if (taList == null || taList.isEmpty()) return;

        for (TaskAssignment ta : taList) {
            Project project = projectDao.selectProjectByTaskAssignment(ta.getId());
            ProjectUserView user = userDao.selectProjectUser(project.getId(), project.getAdminUserId());
            Map parameters = getParameters(ta, user, goalObject);
            deliver(user, notificationTypeName, parameters);
        }
        eventService.addMsgEventByGoalObject(goalObject, notificationTypeName);
    }


    @Autowired
    public void setNotificationItemService(NotificationItemService notificationItemService) {
        this.notificationItemService = notificationItemService;
    }

    @Autowired
    public void setMessageDao(MessageDAO messageDao) {
        this.messageDao = messageDao;
    }

    @Autowired
    public void setMessageboardDao(MessageboardDAO messageboardDao) {
        this.messageboardDao = messageboardDao;
    }

    @Autowired
    public void setUserDao(UserDAO userDao) {
        this.userDao = userDao;
    }

    /*
    @Autowired
    public void setTaskDao(TaskDAO taskDao) {
        this.taskDao = taskDao;
    }
     *
     */

    @Autowired
    public void setTaskAssignmentDao(TaskAssignmentDAO taDao) {
        this.taDao = taDao;
    }

    @Autowired
    public void setContentHeaderDao(ContentHeaderDAO contentHeaderDao) {
        this.contentHeaderDao = contentHeaderDao;
    }

    @Autowired
    public void setProjectDao(ProjectDAO projectDao) {
        this.projectDao = projectDao;
    }

    /*
    @Autowired
    public void setProductDao(ProductDAO productDao) {
        this.productDao = productDao;
    }
     *
     */

    @Autowired
    public void setTargetDao(TargetDAO targetDao) {
        this.targetDao = targetDao;
    }

    @Autowired
    public void setGoalDao(GoalDAO goalDao) {
        this.goalDao = goalDao;
    }

    @Autowired
    public void setGoalObjectDao(GoalObjectDAO goalObjectDao) {
        this.goalObjectDao = goalObjectDao;
    }

    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    @Autowired
    public void setMailbatchService(MailbatchService mailbatchService) {
        this.mailbatchService = mailbatchService;
    }

    /*
    public void sendTaskNotAssignedAlert(int userId, GoalObject go) {
        Map map = new HashMap();
        User user = userDao.get(userId);
        TaskAssignmentAlertInfo taaInfo = taDao.selectTAAlertInfoByGoalObjectId(go.getId());
        map.put("username", user.getFirstName() + " " + user.getLastName());
        map.put("taskname", taaInfo.getTaskName());
        map.put("projectname", taaInfo.getProjectName());
        map.put("productname", taaInfo.getProductName());
        map.put("targetname", taaInfo.getTargetName());
        deliver(userId, Constants.NOTIFICATION_TYPE_ALERT_TASK_NONASSIGNED, map);
    }

    public void sendTaskNotDoneAlert(int userId, TaskAssignment ta) {
        Map map = new HashMap();
        User user = userDao.get(userId);
        TaskAssignmentAlertInfo taaInfo = taDao.selectTAAlertInfo(ta.getId());
        map.put("username", user.getFirstName() + " " + user.getLastName());
        map.put("taskname", taaInfo.getTaskName());
        map.put("projectname", taaInfo.getProjectName());
        map.put("productname", taaInfo.getProductName());
        map.put("targetname", taaInfo.getTargetName());
        // FIX ME !!!
        deliver(userId, Constants.NOTIFICATION_TYPE_ALERT_TASK_NONASSIGNED, map);
    }
     * 
     */

    public void sendContentNotice(int horseId, String notification) {
        sendContentNotice(horseId, 0, notification);
    }
    
    public void sendContentNotice(int horseId, int userId, String notification) {
        ContentHeader ch = contentHeaderDao.selectContentHeaderByHorseId(horseId);
        if (userId == 0) userId = ch.getAuthorUserId();

        Project project = projectDao.selectProjectByHorseId(horseId);
        ProjectUserView user = userDao.selectProjectUser(project.getId(), userId);

        if (user != null) {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put(Constants.NOTIFICATION_TOKEN_USER_NAME, user.getFirstName()+" "+user.getLastName());
            parameters.put(Constants.NOTIFICATION_TOKEN_CONTENT_TITLE, ch.getTitle());
            parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_NAME, project.getCodeName());

            deliver(user, notification, parameters);
        }
    }

    public void sendTaskMessage(TaskAssignment ta, String notificationType) {
        if (eventService.findMsgEvent(ta, notificationType))
            return;

        Map map = getParameters(ta);
        Project project = projectDao.selectProjectByTaskAssignment(ta.getId());
        deliver(project.getId(), ta.getAssignedUserId(), notificationType, map);

        eventService.addMsgEvent(ta, notificationType);
    }

    public void sendThankUserMessage(TaskAssignment ta) {
        sendTaskMessage(ta, Constants.NOTIFICATION_TYPE_MSG_THANK_YOU);
    }


    public void sendWelcomeUserMessage(TaskAssignment ta) {
        sendTaskMessage(ta, Constants.NOTIFICATION_TYPE_MSG_WELCOME);
    }

    public Map getParameters(TaskAssignment ta) {
        return getParameters(ta, null);
    }

    public Map getParameters(TaskAssignment ta, User user) {
        return getParameters(ta, user, null);
    }

    public Map getParameters(TaskAssignment ta, User user, GoalObject goalObject) {
        Map<String, String> parameters = new HashMap<String, String>();

        if (user == null)
            user = userDao.get(ta.getAssignedUserId());
        if (user != null) 
            parameters.put(Constants.NOTIFICATION_TOKEN_USER_NAME, user.getFirstName() + " " + user.getLastName());

        TaskAssignmentAlertInfo taaInfo = taDao.selectTAAlertInfo(ta.getId());
        if (taaInfo != null) {
            parameters.put(Constants.NOTIFICATION_TOKEN_TASK_NAME, taaInfo.getTaskName());
            parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_NAME, taaInfo.getProjectName());
            parameters.put(Constants.NOTIFICATION_TOKEN_PRODUCT_NAME, taaInfo.getProductName());
            parameters.put(Constants.NOTIFICATION_TOKEN_TARGET_NAME, taaInfo.getTargetName());
            //parameters.put(dueTime, taaInfo.getDueTime());
            parameters.put(Constants.NOTIFICATION_TOKEN_TASK_DUE_TIME, taaInfo.getDueTime().toString());

            long days = ((new Date()).getTime() - taaInfo.getDueTime().getTime()) / (1000*3600*24);
            if (days > 0) {
                parameters.put(Constants.NOTIFICATION_TOKEN_DAYS_AFTER_DUE, Long.toString(days));
                parameters.put(Constants.NOTIFICATION_TOKEN_DAYS_BEFORE_DUE, "0");
            } else {
                parameters.put(Constants.NOTIFICATION_TOKEN_DAYS_AFTER_DUE, "0");
                parameters.put(Constants.NOTIFICATION_TOKEN_DAYS_BEFORE_DUE, Long.toString(days*-1));
            }
        }

        ContentHeader ch = contentHeaderDao.selectContentHeaderByHorseId(ta.getHorseId());
        if (ch != null) 
            parameters.put(Constants.NOTIFICATION_TOKEN_CONTENT_TITLE, ch.getTitle());

        Target target = targetDao.get(ta.getTargetId());
        if (target != null)
            parameters.put(Constants.NOTIFICATION_TOKEN_TARGET_NAME, target.getName());

        Project project = projectDao.selectProjectByTaskAssignment(ta.getId());
        if (project != null) {
            parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_START_TIME, project.getStartTime().toString());
            User adminUser = userDao.get(project.getAdminUserId());
            if (adminUser != null)
                parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_ADMIN, adminUser.getFirstName()+" "+adminUser.getLastName());
        }

        if (goalObject == null)
            goalObject = goalObjectDao.get(ta.getGoalObjectId());
        if (goalObject != null) {
            Goal goal = goalDao.get(goalObject.getGoalId());
            if (goal != null) {
                parameters.put(Constants.NOTIFICATION_TOKEN_GOAL_NAME, goal.getName());

                Date dueTime = goalObject.getEnterTime();
                if (dueTime != null) {
                    dueTime.setTime(dueTime.getTime() + 1000*3600*24*goal.getDuration());
                    parameters.put(Constants.NOTIFICATION_TOKEN_GOAL_DUE_TIME, dueTime.toString());
                }
            }
        }

        return parameters;
    }
}
