/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.workflow;

import com.ocs.indaba.common.Constants;

import com.ocs.indaba.dao.GoalDAO;
import com.ocs.indaba.dao.GoalObjectDAO;
import com.ocs.indaba.dao.SequenceObjectDAO;
import com.ocs.indaba.dao.WorkflowDAO;
import com.ocs.indaba.dao.WorkflowObjectDAO;
import com.ocs.indaba.dao.TaskAssignmentDAO;
//import com.ocs.indaba.dao.RuleDAO;
//import com.ocs.indaba.dao.PregoalDAO;
import com.ocs.indaba.dao.TaskDAO;
import com.ocs.indaba.dao.ProductDAO;
import com.ocs.indaba.dao.ProjectDAO;
import com.ocs.indaba.dao.TargetDAO;
import com.ocs.indaba.dao.UserDAO;
import com.ocs.indaba.dao.HorseDAO;

//import com.ocs.indaba.po.Workflow;
import com.ocs.indaba.po.WorkflowObject;
import com.ocs.indaba.po.SequenceObject;
import com.ocs.indaba.po.Goal;
import com.ocs.indaba.po.Workflow;
import com.ocs.indaba.po.GoalObject;
import com.ocs.indaba.po.TaskAssignment;
//import com.ocs.indaba.po.Rule;
import com.ocs.indaba.po.Task;
import com.ocs.indaba.po.Product;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.Target;
import com.ocs.indaba.po.User;
import com.ocs.indaba.po.Horse;
//import com.ocs.indaba.vo.PreGoalView;
import com.ocs.indaba.service.SiteMessageService;
import com.ocs.indaba.service.EventService;
import com.ocs.indaba.service.CaseService;
import com.ocs.indaba.service.HorseService;

import com.ocs.indaba.vo.WorkflowView;
import com.ocs.indaba.vo.WorkflowObjectView;

import com.ocs.common.Config;
import com.ocs.indaba.common.Rights;
import com.ocs.indaba.service.AccessPermissionService;
import com.ocs.indaba.util.DateUtils;
import com.ocs.indaba.vo.ProjectUserView;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.StatefulSession;
//import org.drools.audit.WorkingMemoryFileLogger;
import org.drools.compiler.PackageBuilder;

/**
 *
 * @author Luke
 */
public class WorkflowEngine {

    private static final Logger logger = Logger.getLogger(WorkflowEngine.class);
    private WorkflowDAO workflowDAO = null;
    private WorkflowObjectDAO workflowObjectDAO = null;
    private SequenceObjectDAO sequenceObjectDAO = null;
    private GoalObjectDAO goalObjectDAO = null;
    private GoalDAO goalDAO = null;
    private TaskAssignmentDAO taskAssignmentDAO = null;
    //private RuleDAO ruleDAO = null;
    //private PregoalDAO pregoalDAO = null;
    private TaskDAO taskDAO = null;
    private ProductDAO productDAO = null;
    private ProjectDAO projectDAO = null;
    private TargetDAO targetDAO = null;
    private UserDAO userDAO = null;
    private HorseDAO horseDAO = null;
    private SiteMessageService siteMessageService = null;
    private AccessPermissionService accessPermissionService = null;
    private EventService eventService;
    private CaseService caseService;
    private HorseService horseService;
    private String returnStr = "";
    private static HashMap<String, RuleBase> ruleBaseTable = new HashMap<String, RuleBase>();

    @Autowired
    public void setMessageService(SiteMessageService siteMessageService) {
        this.siteMessageService = siteMessageService;
    }

    public String runWorkflowObject(int wfoId) {
        return runWorkflowObject(workflowObjectDAO.selectWorkflowObjectById(wfoId));
    }

    public String runWorkflowObject(WorkflowObject wfo) {
        logger.debug("-----------<<< RUNNING Workflow Object: " + wfo.getId() + " >>>---------------");
        returnStr = "<p/>Running Workflow Object " + wfo.getId() + "...<br/>";

        if (wfo.getStatus() == Constants.WORKFLOW_OBJECT_STATUS_SUSPENDED
                || wfo.getStatus() == Constants.WORKFLOW_OBJECT_STATUS_WAITING
                || wfo.getStatus() == Constants.WORKFLOW_OBJECT_STATUS_DONE
                || wfo.getStatus() == Constants.WORKFLOW_OBJECT_STATUS_CANCELLED
                || wfo.getStatus() < 0) {
            returnStr += "Workflow Object " + wfo.getId() + " Stopped: status = " + wfo.getStatus() + "<br/>";
            return returnStr;
        }

        boolean soAllDone = true;
        List<SequenceObject> soList = sequenceObjectDAO.selectSequenceObjectsByWorkflowObjectId(wfo.getId());
        if (soList != null && soList.size() > 0) {
            for (SequenceObject so : soList) {

                boolean justStarted = false;

                logger.debug("-----------[[[[ Sequence Object: " + so.getId() + " - " + so.getStatus() + " ]]]]------------");
                if (so.getStatus() == Constants.SEQUENCE_OBJECT_STATUS_DONE) {
                    continue;
                }
                soAllDone = false;

                if (so.getStatus() == Constants.SEQUENCE_OBJECT_STATUS_WAITING) {
                    List<Goal> preGoalList = goalDAO.selectPreGoalListBySO(so.getId());
                    if (preGoalList == null || preGoalList.isEmpty()) {
                        logger.debug("----------- No PreGoal ------------");
                        so.setStatus((short) Constants.SEQUENCE_OBJECT_STATUS_STARTED);
                        sequenceObjectDAO.update(so);
                        returnStr += "Started Sequence Object: " + so.getId() + "<br/>";
                    } else {
                        boolean allPreGoalsCompleted = true;
                        for (Goal preGoal : preGoalList) {
                            boolean preGoalComplete = true;
                            List<GoalObject> preGoalObjList = goalObjectDAO.selectGoalObjectListByWorkflowObject(so.getWorkflowSequenceId(), preGoal.getWorkflowSequenceId(), so.getWorkflowObjectId());
                            if (preGoalObjList != null && !preGoalObjList.isEmpty()) {
                                for (GoalObject preGoalObj : preGoalObjList) {
                                    logger.debug("----------- PreGoal Object: " + preGoalObj.getId() + " -----");
                                    if (preGoalObj.getStatus() != Constants.GOAL_OBJECT_STATUS_DONE) {
                                        preGoalComplete = false;
                                        break;
                                    }
                                }
                                preGoalObjList.clear();
                                preGoalObjList = null;
                            }
                            if (!preGoalComplete) {
                                allPreGoalsCompleted = false;
                                break;
                            }
                        }
                        if (allPreGoalsCompleted) {
                            so.setStatus((short) Constants.SEQUENCE_OBJECT_STATUS_STARTED);
                            sequenceObjectDAO.update(so);
                            returnStr += "Started Sequence Object: " + so.getId() + "<br/>";
                        } else {
                            continue;
                        }
                    }
                }

                // now the SO is started
                // Get the active GO of this SO. Note that there can be at most one such GO (its status is either STARTING or STARTED);
                GoalObject activeGoalObj = goalObjectDAO.selectActiveGoalObjectBySo(so.getId());

                if (activeGoalObj == null) {
                    List<GoalObject> waitingGoalObjList = goalObjectDAO.selectGoalObjectListBySoAndStatus(so.getId(), Constants.GOAL_OBJECT_STATUS_WAITING);
                    if (waitingGoalObjList == null || waitingGoalObjList.size() == 0) {
                        // all GO's have been complete
                        // set SO status = DONE and update DB record of SO;
                        so.setStatus((short) Constants.SEQUENCE_OBJECT_STATUS_DONE);
                        sequenceObjectDAO.update(so);
                        returnStr += "Finished Sequence Object: " + so.getId() + "<br/>";
                        // skip to next SO;
                        continue;
                    } else {
                        //find the 1st GO that is WAITING. This GO could be the very 1st GO of the SO, or a GO after other completed GO's;
                        GoalObject waitingGoalObj = waitingGoalObjList.get(0);
                        // officially enter the goal
                        // update the enter_time of the GO to now;
                        waitingGoalObj.setEnterTime(new java.util.Date());
                        goalObjectDAO.update(waitingGoalObj);

                        // try to start this GO
                        if (!tryToStartGO(waitingGoalObj)) {
                            //can't be started
                            //skip to next SO;
                            continue;
                        } else {
                            justStarted = true;
                        }

                        if (waitingGoalObj.getStatus() != Constants.GOAL_OBJECT_STATUS_STARTED) {
                            // this GO cannot be started and is still in WAITING
                            // skip to next SO;
                            continue;
                        } else {
                            // update the enter_time of the GO to now;
                            waitingGoalObj.setEnterTime(new java.util.Date());
                            goalObjectDAO.update(waitingGoalObj);
                            returnStr += "Set Goal Object Enter Time: " + waitingGoalObj.getId() + "<br/>";
                            activeGoalObj = waitingGoalObj;
                        }
                    }
                }

                // Process the active GO that is either STARTING or STARTED
                // Call the goal's exit rules; The rules may or may not get the GO's status set to DONE;
                if (activeGoalObj.getStatus() == Constants.GOAL_OBJECT_STATUS_STARTING) {
                    // try to start again
                    if (!tryToStartGO(activeGoalObj)) {
                        // still cannot be started
                        continue;
                    } else {
                        justStarted = true;
                    }
                }

                // the GO must be STARTED
                TaskAssignment ta = taskAssignmentDAO.selectIncompleteAssignment(activeGoalObj.getId());
                //if all TA's are completed
                if (ta == null) {
                    //set GO status to DONE;
                    //update the GO's exit_time to now;
                    activeGoalObj.setStatus((short) Constants.GOAL_OBJECT_STATUS_DONE);
                    activeGoalObj.setExitTime(new Date());
                    goalObjectDAO.update(activeGoalObj);                    

                    returnStr += "Goal Object " + activeGoalObj.getId() + " is DONE.<br/>";
                    returnStr += "Set Goal Object Exit Time: " + activeGoalObj.getId() + "<br/>";

                    // create a new version of the survey
                    returnStr += "Create Survey Version: " + CreateSurveyVersion(activeGoalObj, wfo) + "<br/>";
                } else {
                    logger.info("---------- Found incomplete Task Assignment: " + ta.getId());
                }

                // need to call exit rules regardless. Even though not all TA's are completed, exit rules can still force the GO to be set to DONE (e.g. 80% of TA's are completed, the the GO is considered done)
                //Call the goal's exit rules; The rules may or may not get the GO's status set to DONE;
                excuteExitRule(activeGoalObj);

                if (activeGoalObj.getStatus() != Constants.GOAL_OBJECT_STATUS_DONE) {
                    //the GO status is still STARTED {
                    //Call the goal's inflight rules; This shouldn't change the status of the GO.
                    excuteInflightRule(activeGoalObj);

                    logger.debug("======>>> ActiveGoalObj: " + activeGoalObj.getId() + ", Status: " + activeGoalObj.getStatus());
                    // then we also need to check for bad tasks because some tasks may become unassigned again.
                    //if (activeGoalObj.getStatus() is not just started)
                    if (!justStarted) {
                        // only need to do this if the GO is not just started
                        checkBadTasks(activeGoalObj);
                    }
                }
            }
            soList.clear();
            soList = null;
        }
        if (soAllDone) {
            wfo.setStatus(Constants.WORKFLOW_OBJECT_STATUS_DONE);
            workflowObjectDAO.update(wfo);
            Horse horse = horseService.getHorseByWorkflowObjectId(wfo.getId());
            horse.setCompletionTime(new Date());
            horseDAO.update(horse);
            returnStr += "Finished Workflow Object: " + wfo.getId() + "<br/>";
        }

        returnStr += "Done with Workflow Object " + wfo.getId() + ".<br/>";
        return returnStr;
    }


    private String CreateSurveyVersion(GoalObject goalObj, WorkflowObject wfo) {
        Horse horse = horseService.getHorseByWorkflowObjectId(wfo.getId());
        if (horse == null) return "ERROR: no horse found for WFO " + wfo.getId();

        Product product = productDAO.get(horse.getProductId());
        if (product == null) {
            return "ERROR: No product for ID " + horse.getProductId();
        }

        if (product.getContentType() != Constants.CONTENT_TYPE_SURVEY) {
            return "Not survey content type";
        }

        Goal goal = goalDAO.get(goalObj.getGoalId());

        if (goal == null) {
            return "ERROR: No goal for ID " + goalObj.getGoalId();
        }
        
        if (goal.getCreateContentVersion() == 0) {
            return "No new version required";
        }

        // try to get user ID that completed tasks of the goal
        List<TaskAssignment> taList = taskAssignmentDAO.selectTaskAssignmentsByGoalObject(goalObj.getId());
        int userId = 0;
        Date lastCompletionTime = null;
        if (taList != null && !taList.isEmpty()) {
            for (TaskAssignment ta : taList) {
                if (userId == 0) {
                    userId = ta.getAssignedUserId();
                    lastCompletionTime = ta.getCompletionTime();
                } else if (lastCompletionTime != null && lastCompletionTime.before(ta.getCompletionTime())) {
                    userId = ta.getAssignedUserId();
                    lastCompletionTime = ta.getCompletionTime();
                }
            }
        }

        // call the proc to create a new version
        int rc = horseService.createSurveyVersion(horse.getId(), "GOAL: " + goal.getName(), userId);

        if (rc != 0) {
            logger.error("Failed to create new Survey Version for horse " + horse.getId() + " at goal '" + goal.getName() + "' with RC " + rc);
            return "Failed to create new Survey Version for horse " + horse.getId() + " at goal '" + goal.getName() + "' with RC " + rc;
        }

        return "Created new Survey Version for horse " + horse.getId() + " at goal '" + goal.getName();
    }
    

    private int checkBadTasks(GoalObject go) { // return number of bad tasks
        //Find current Product's tasks assigned to the Goal of the GO; Only need tasks whose assignment method is MANUAL or QUEUE;
        List<Task> taskList = taskDAO.selectTasksOfGoalObject(go.getId());
        int badTasks = 0;
        Horse horse = horseDAO.selectHorseByGoalObject(go.getId());
        logger.debug("====>>> Horse: " + horse + ", Goal Object: " + go.getId());

        Target target = targetDAO.get(horse.getTargetId());
        boolean taskIsBad = false;

        logger.debug("====>>> Check Bad Tasks: " + taskList);
        for (Task task : taskList) {
            //find the TA's corresponding to this task. Note that there could be multiple TA's (e.g. multiple peer reviewers)
            List<TaskAssignment> taList = taskAssignmentDAO.selectTaskAssignmentsByTaskAndGoalObject(task.getId(), go.getId());
            //
            taskIsBad = false;

            //if (no TA found or assigned_user_id is 0
            TaskAssignment ta = null;
            if (taList == null || taList.isEmpty() || taList.size() == 0) {
                badTasks++;
                taskIsBad = true;

                logger.debug("============= NO TASK ASSIGNMENT FOUND for TASK # " + task.getId());

                //ta = new TaskAssignment(0, task.getId(), target.getId(), 0, (short) Constants.TASK_STATUS_INACTIVE, horse.getId());
                ta = new TaskAssignment();
                ta.setTaskId(task.getId());
                ta.setTargetId(target.getId());
                ta.setAssignedUserId(0);
                ta.setStatus((short) Constants.TASK_STATUS_INACTIVE);
                ta.setHorseId(horse.getId());
                ta.setPercent((float) -1.0);
                ta.setGoalObjectId(go.getId());
                ta.setQEnterTime(new Date());
                taskAssignmentDAO.create(ta);
                returnStr += "Created Task Assignment: " + ta.getId() + "<br/>";

            } else {
                for (TaskAssignment tta : taList) {
                    ta = tta;
                    // add queue assigned TA to queue
                    // place the TA in queue.
                    // if (TA q_enter_time is not set or 0)
                    if (ta.getQEnterTime() == null) {
                        //set TA's q_enter_time to now;
                        ta.setQEnterTime(new Date());
                        //update the TA in DB;
                        taskAssignmentDAO.update(ta);
                        returnStr += "Set Task Assignment: " + ta.getId() + " QEnter Time: " + ta.getQEnterTime() + "<br/>";
                    }

                    if (ta.getAssignedUserId() == 0) {
                        // nobody is assigned to the task!
                        // Or the task assignment has not been completed
                        logger.debug("===== BAD TASK ASSIGNMENT:" + ta.getId() + " - NOBODY ASSIGNED ==========");
                        badTasks++;
                        taskIsBad = true;
                        break;
                    }
                }
            }

            if (!taskIsBad) {
                logger.debug("==== TASK NOT BAD ======");
                continue;
            }

            logger.debug("======== PROCESS BAD TASK ========");

            Product product = productDAO.get(task.getProductId());
            Project project = projectDAO.get(product.getProjectId());

            logger.info("======>>> Task: " + task.getId() + ", AssignmentMethod: " + task.getAssignmentMethod());


            // Treat every thing as QUEUE assigned - YC
            // send 'Sys - Please Claim' site msg to all users who have the roles required by the task - only if this msg has not been sent
            // AND if (now - TA.q_last_return_time >= X day);
            //if (!eventService.findMsgEvent(ta, Constants.NOTIFICATION_TYPE_NOTIFY_CLAIM)
            int minutes = eventService.findMinutesFromMsgEvent(ta, Constants.NOTIFICATION_TYPE_NOTIFY_CLAIM);
            logger.debug("======### Minutes:" + minutes + "; GetMinutesFrom:" + ta.getQLastReturnTime() + " => " + DateUtils.getMinutesFrom(ta.getQLastReturnTime()));

            if ((minutes < 0 && ta.getQLastReturnTime() == null)
                    || (minutes >= Config.getInt(Config.KEY_WORKFLOW_ALERT_RESEND_INTERVAL) && DateUtils.getMinutesFrom(ta.getQLastReturnTime()) >= Config.getInt(Config.KEY_WORKFLOW_ALERT_RESEND_INTERVAL))) {

                // Only send notifications to users who can claim from queue - YC
                // We first try to send the msg to users who can claim the task
                // But if there is no such users, we'll notify the PA.
                List<ProjectUserView> userList = userDAO.selectClaimableUsersByTaskInProject(task.getId(), project.getId());
                int usersNotified = 0;

                if (userList != null && !userList.isEmpty()) {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put(Constants.NOTIFICATION_TOKEN_TASK_NAME, task.getTaskName());
                    parameters.put(Constants.NOTIFICATION_TOKEN_PRODUCT_NAME, product.getName());
                    parameters.put(Constants.NOTIFICATION_TOKEN_TARGET_NAME, target.getName());
                    parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_NAME, project.getCodeName());
                    parameters.put(Constants.NOTIFICATION_TOKEN_CONTENT_TITLE, target.getName() + ' ' + product.getName());

                    for (ProjectUserView user : userList) {
                        // see if this user can see the QUEUE tab. If not, don't notify
                        if (accessPermissionService.checkProjectPermission(project.getId(), user.getId(), Rights.MANAGE_QUEUES) ||
                            accessPermissionService.checkProjectPermission(project.getId(), user.getId(), Rights.SEE_TASK_QUEUES)) {
                            usersNotified++;
                            parameters.put(Constants.NOTIFICATION_TOKEN_USER_NAME, user.getFirstName() + " " + user.getLastName());
                            logger.debug("============= Send msg " + Constants.NOTIFICATION_TYPE_NOTIFY_CLAIM + " to user " + user.getId() + " ==============");
                            siteMessageService.deliver(user, Constants.NOTIFICATION_TYPE_NOTIFY_CLAIM, parameters);
                            returnStr += "Sent Task Claim Notice To User " + user.getId() + "<br/>";
                        }
                    }
                }

                if (usersNotified == 0) {
                    // No claimable users available - notify the PA.
                    // send 'Sys - Task Not Assigned' site msg to Project's admin_user - Only if this msg has not been sent;
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put(Constants.NOTIFICATION_TOKEN_TASK_NAME, task.getTaskName());
                    parameters.put(Constants.NOTIFICATION_TOKEN_PRODUCT_NAME, product.getName());
                    parameters.put(Constants.NOTIFICATION_TOKEN_TARGET_NAME, target.getName());
                    parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_NAME, project.getCodeName());
                    parameters.put(Constants.NOTIFICATION_TOKEN_CONTENT_TITLE, target.getName() + ' ' + product.getName());
                    ProjectUserView user = userDAO.selectProjectUser(project.getId(), project.getAdminUserId());
                    logger.debug("=====>>>>> User:" + user + " ----- AdminUserId:" + project.getAdminUserId());
                    parameters.put(Constants.NOTIFICATION_TOKEN_USER_NAME, user.getFirstName() + " " + user.getLastName());

                    logger.debug("============= Send msg " + Constants.NOTIFICATION_TYPE_ALERT_TASK_NONASSIGNED + " to admin user " + user.getId() + " ==============");
                    siteMessageService.deliver(user, Constants.NOTIFICATION_TYPE_ALERT_TASK_NONASSIGNED, parameters);
                    returnStr += "Sent Task Not Assigned Alert To Admin User " + user.getId() + "<br/>";
                }
                eventService.addMsgEvent(ta, Constants.NOTIFICATION_TYPE_NOTIFY_CLAIM);
            }

        }

        return badTasks;
    }

    private boolean tryToStartGO(GoalObject go) { // return whether started or not
        // Try to start a GO
        int bad_tasks = checkBadTasks(go);
        if (bad_tasks > 0) {
            // can't start this GO
            //set GO status to STARTING;
            //update GO's DB record;
            go.setStatus((short) Constants.GOAL_OBJECT_STATUS_STARTING);
            goalObjectDAO.update(go);
            returnStr += "Set Goal Object Status to STARTING: " + go.getId() + "<br/>";
            return false;
        }

        // The GO can be started
        //set GO status to STARTED;
        //set enter_time of GO to now;
        //update GO's DB record;
        go.setStatus((short) Constants.GOAL_OBJECT_STATUS_STARTED);
        go.setEnterTime(new Date());
        goalObjectDAO.update(go);
        returnStr += "Set Goal Object Status to STARTED: " + go.getId() + "<br/>";

        Goal goal = goalDAO.get(go.getGoalId());
        Project project = projectDAO.getProjectByGoalObject(go.getId());
        
        // Activate TA's in this GO. Note that in this version, we don't activate any TA until all tasks in the GO have been assigned. This is not a problem now because there is usually only one task in each goal.
        List<TaskAssignment> taList = taskAssignmentDAO.selectTaskAssignmentsByGoalObject(go.getId());
        for (TaskAssignment ta : taList) {
            //set TA status to ACTIVE;
            ta.setStatus((short) Constants.TASK_STATUS_ACTIVE);
            //set the start_time of TA to now;
            ta.setStartTime(new Date());
            //set due_time of TA to now + goal's duration;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, goal.getDuration());
            ta.setDueTime(calendar.getTime());
            //update the TA record in DB;
            taskAssignmentDAO.update(ta);
            returnStr += "Set Task Assignment Status to ACTIVE: " + ta.getId() + "<br/>";

            // Need to keep this check just in case - otherwise exception occurs when sending msg to user. YC 9/27/2010
            if (ta.getAssignedUserId() == 0) {
                continue;
            }
            //send 'Sys - Task Activated' site msg to assigned user of the TA;
            Map parameters = siteMessageService.getParameters(ta);

            logger.debug("========= Send msg " + Constants.NOTIFICATION_TYPE_NOTIFY_TASK_ACTIVATED + " to user " + ta.getAssignedUserId() + " [TAID=" + ta.getId() + "] ==========");
            siteMessageService.deliver(project.getId(), ta.getAssignedUserId(), Constants.NOTIFICATION_TYPE_NOTIFY_TASK_ACTIVATED, parameters);
            returnStr += "Sent Task Active Notice To User " + ta.getAssignedUserId() + "<br/>";
        }

        // Call the goal's entrance rules against this GO. This may cause some other msgs to be sent;
        excuteEntranceRule(go);

        return true;
    }

    private void excuteEntranceRule(GoalObject go) {
        Goal goal = goalDAO.selectGoalByGoalObjectId(go.getId());
        excuteRule(go, goal.getEntranceRuleFileName());
    }

    private void excuteExitRule(GoalObject go) {
        Goal goal = goalDAO.selectGoalByGoalObjectId(go.getId());
        excuteRule(go, goal.getExitRuleFileName());
    }

    private void excuteInflightRule(GoalObject go) {
        Goal goal = goalDAO.selectGoalByGoalObjectId(go.getId());
        excuteRule(go, goal.getInflightRuleFileName());
    }

    private RuleBase getRuleBase(String ruleFile) {

        if (ruleFile == null || ruleFile.length() == 0) {
            logger.debug("========= No Rule File Specified ==========");
            return null;
        }

        RuleBase ruleBase = ruleBaseTable.get(ruleFile);
        PackageBuilder builder;

        if (ruleBase == null) {
            // create a new rule base
            try {
                logger.debug("============== Add Rule: " + ruleFile + " ==============");
                builder = new PackageBuilder();
                builder.addPackageFromDrl(new InputStreamReader(WorkflowEngine.class.getResourceAsStream(ruleFile)));
            } catch (Exception e) {
                logger.debug("=== Rule file " + ruleFile + " not found ===");
                return null;
            }

            try {
                ruleBase = RuleBaseFactory.newRuleBase();
                ruleBase.addPackage(builder.getPackage());
            } catch (Exception e) {
                logger.debug("=== Failed to create Rule Base for file: " + ruleFile);
                return null;
            }

            ruleBaseTable.put(ruleFile, ruleBase);
            logger.debug("Added new Rule Base for file: " + ruleFile);
        } else {
            logger.debug("Found Rule Base for: " + ruleFile);
        }

        return ruleBase;
    }

    private void excuteRule(GoalObject goalObject, String ruleFile) {

        RuleBase ruleBase = getRuleBase(ruleFile);
        StatefulSession session = null;

        if (ruleBase == null) {
            return;
        }

        try {
            List<TaskAssignment> taList = taskAssignmentDAO.selectTaskAssignmentsByGoalObject(goalObject.getId());
            Goal goal = goalDAO.get(goalObject.getGoalId());
            Integer taIndex = 0;

            for (TaskAssignment ta : taList) {
                try {
                    session = ruleBase.newStatefulSession();
                    taIndex++;

                    session.insert(goalObject);
                    session.insert(goal);
                    session.insert(ta);
                    session.insert(taIndex);  // added taIndex so rules can selectively process TA. YC 9/29/2010

                    session.insert(goalObjectDAO);
                    session.insert(siteMessageService);
                    session.insert(caseService);
                    session.insert(horseService);

                    logger.debug("========= FIRE ALL RULES in " + ruleFile + " for TA:#" + ta.getId() + ", TA status:" + ta.getStatus() + ", taIndex:" + taIndex + " ================= ");
                    session.fireAllRules();
                    session.dispose();
                } catch (Exception e) {
                    logger.debug("======= Excute Rule: " + ruleFile + ", Session:" + session + ", Exception:" + e + " =========");
                    if (session != null) {
                        session.dispose();
                    }
                }
            }
            logger.debug("======== DONE DONE DONE ========");
        } catch (Exception e) {
            logger.debug("=======<<<< Excute Rule: " + ruleFile + ">>>>, Exception:" + e + " =========");
            return;
        }
        //session = null;
        returnStr += "Excuted Rule File " + ruleFile + " For Goal Object " + goalObject.getId() + "<br/>";
    }

    public List<Workflow> getWorkflowList() {
        return workflowDAO.findAll();
    }

    public List<WorkflowView> getWorkflowViewList() {
        return workflowDAO.getWorkflowViewList();
    }

    public List<WorkflowObject> getWorkflowObjectList(int workflowId) {
        return workflowObjectDAO.selectWorkflowObjects(workflowId);
    }

    public List<WorkflowObjectView> getWorkflowObjectViewList() {
        return workflowDAO.getWorkflowObjectViewList();
    }

    @Autowired
    public void setWorkflowDao(WorkflowDAO workflowDAO) {
        this.workflowDAO = workflowDAO;
    }

    @Autowired
    public void setWorkflowObjectDao(WorkflowObjectDAO workflowObjectDAO) {
        this.workflowObjectDAO = workflowObjectDAO;
    }

    @Autowired
    public void setSequenceObjectDAO(SequenceObjectDAO sequenceObjectDAO) {
        this.sequenceObjectDAO = sequenceObjectDAO;
    }

    @Autowired
    public void setGoalDAO(GoalDAO goalDAO) {
        this.goalDAO = goalDAO;
    }

    @Autowired
    public void setGoalObjectDAO(GoalObjectDAO goalObjectDAO) {
        this.goalObjectDAO = goalObjectDAO;
    }

    @Autowired
    public void setTaskAssignmentDAO(TaskAssignmentDAO taskAssignmentDAO) {
        this.taskAssignmentDAO = taskAssignmentDAO;
    }

    /*
    @Autowired
    public void setRuleDAO(RuleDAO ruleDAO) {
    this.ruleDAO = ruleDAO;
    }
    
    
    @Autowired
    public void setPregoalDAO(PregoalDAO pregoalDAO) {
    this.pregoalDAO = pregoalDAO;
    }
     *
     */
    @Autowired
    public void setTaskDAO(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    @Autowired
    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Autowired
    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    @Autowired
    public void setTargetDAO(TargetDAO targetDAO) {
        this.targetDAO = targetDAO;
    }

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setHorseDAO(HorseDAO horseDAO) {
        this.horseDAO = horseDAO;
    }

    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    @Autowired
    public void setCaseService(CaseService caseService) {
        this.caseService = caseService;
    }

    @Autowired
    public void setHorseService(HorseService horseService) {
        this.horseService = horseService;
    }
    
    @Autowired
    public void setAccessPermissionService(AccessPermissionService service) {
        this.accessPermissionService = service;
    }
    
}
