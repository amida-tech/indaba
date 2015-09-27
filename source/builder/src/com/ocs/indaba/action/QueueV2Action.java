/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.common.ErrorCode;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.dao.ProductDAO;
import com.ocs.indaba.po.Product;
import com.ocs.indaba.po.Target;
import com.ocs.indaba.po.Task;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.HorseService;
import com.ocs.indaba.service.QueueService;
import com.ocs.indaba.service.TaskService;
import com.ocs.indaba.util.CookieUtils;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.QueueTaskView;
import com.ocs.indaba.vo.QueueUser;
import com.ocs.indaba.vo.TargetBasedTaskView;
import com.ocs.indaba.vo.TaskAssignmentView;
import com.ocs.util.ListUtils;
import com.ocs.util.StringUtils;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author menglong luwb
 */
public class QueueV2Action extends BaseAction {

    private static final Logger logger = Logger.getLogger(QueueV2Action.class);
    private static final String FWD_QUEUES = "queues";
    private static final String ACTION_FILTER_OPIONS = "filterOptions";
    private static final String ACTION_FIND = "findQueues";
    private static final String ACTION_UPDATE = "updateQueue";
    private static final String ACTION_ADD = "addQueue";
    private static final String ACTION_DEL = "delQueue";
    private static final String ACTION_SUBSCRIBE = "subscribe";
    private static final String ACTION_UNSUBSCRIBE = "unsubscribe";
    private static final String PARAM_ASSIGN_USER_ID = "assignUid";
    private static final String PARAM_PRIORITY = "priority";
    private static final String PARAM_TASK_ASSIGNMENT_ID = "taId";
    private static final String PARAM_TASK_STATUS = "status";
    private static final String PARAM_ASSIGNED_STATUS = "assignstatus";
    private static final String PARAM_TASK_ID = "taskId";
    private static final String PARAM_TARGET_IDS = "targetIds[]";
    private static final String ATTR_TASK_LIST = "taskList";
    private static final String ATTR_TARGET_LIST = "targetList";
    private QueueService queueService;
    private TaskService taskService;
    //private HorseService horseService;
    private ProductDAO productDao = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LoginUser loginUser = preprocess(mapping, request, response);
        // Pre-process the request (from the super class)
        String action = request.getParameter(ATTR_ACTION);
        int productId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID), 0);

        logger.debug("Request Parameters: \n\taction=" + action + "\n\tproductId=" + productId);

        if (ACTION_FILTER_OPIONS.equals(action)) {
            return refreshFilterOptions(response, productId, loginUser);
        } else if (ACTION_FIND.equals(action)) {
            return findQueues(request, response, loginUser, productId);
        } else if (ACTION_UPDATE.equals(action)) {
            return updateQueue(request, response, loginUser, productId);
        } else if (ACTION_ADD.equals(action)) {
            return addQueue(request, response, loginUser);
        } else if (ACTION_DEL.equals(action)) {
            return delQueue(request, response, loginUser, productId);
        } else if (ACTION_SUBSCRIBE.equals(action)) {
            return subscribe(request, response, loginUser);
        } else if (ACTION_UNSUBSCRIBE.equals(action)) {
            return unsubscribe(request, response, loginUser);
        } else {
            boolean isAdmin = isProductAdmin(loginUser);
            
            List<Product> products = isAdmin ?
                prdService.getActiveProductsByProjectId(loginUser.getPrjid()) : prdService.getUserActiveProductsByProjectId(loginUser.getPrjid(), loginUser.getUid());

            if (!ListUtils.isEmptyList(products)) {
                request.setAttribute(ATTR_TASK_LIST, (isAdmin ? taskService.getTasksByProductId(products.get(0).getId()): taskService.getUserTasksByProductId(products.get(0).getId(), loginUser.getUid())));
                request.setAttribute(ATTR_TARGET_LIST, trgtService.getTargetsByProjectId(loginUser.getPrjid()));
            }
            
            request.setAttribute(ATTR_PRODUCT_LIST, products);
            request.setAttribute(Constants.COOKIE_ACTIVE_TAB, Constants.TAB_QUEUES);
            CookieUtils.setCookie(response, Constants.COOKIE_ACTIVE_TAB, Constants.TAB_QUEUES);
            return mapping.findForward(FWD_QUEUES);
        }
    }


    private boolean isProductAdmin(LoginUser user) {
        return queueService.isProductManager(user.isSiteAdmin(), user.getPrjid(), user.getUid());
    }

    private ActionForward updateQueue(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser, int productId) throws IOException {
        short priority = StringUtils.str2short(request.getParameter(PARAM_PRIORITY));
        int assignUid = StringUtils.str2int(request.getParameter(PARAM_ASSIGN_USER_ID));
        int taId = StringUtils.str2int(request.getParameter(PARAM_TASK_ASSIGNMENT_ID));
        logger.debug("Update Queue: \n\tproductId=" + productId + "\n\ttaId=" + taId + "\n\tassignUid=" + assignUid + "\n\tpriority=" + priority);

        boolean isPM = isProductAdmin(loginUser);

        int ret = queueService.updateTaskAssignment(isPM, loginUser.getUid(), taId, priority, assignUid);
        logger.debug("updateTaskAssignment: " + ret);
        
        switch(ret) {
            case ErrorCode.ERR_ALREADY_EXISTS: {
                User user = userSrvc.getUser(assignUid);
                String errMsg = MessageFormat.format(Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_ALREADY_EXISTS_TA, loginUser.getLanguageId()), (user.getFirstName() + " " + user.getLastName()));
                logger.error(errMsg);
                super.writeRespJSON(response, ErrorCode.ERR_NON_EXISTENT, "", errMsg);
            }
            break;
            case ErrorCode.ERR_NON_EXISTENT: {
                logger.error("Cannot update queue because the task assignment is not existed!");
                super.writeRespJSON(response, ErrorCode.ERR_NON_EXISTENT, "", Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_NOT_EXISTS_TA, loginUser.getLanguageId()));
            }
            break;
            case ErrorCode.OK: {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("assignedUserId", assignUid);
                if (assignUid > 0) {
                    User user = userSrvc.getUser(assignUid);
                    jsonObj.put("assignedUsername", user.getFirstName() + " " + user.getLastName());
                } else {
                    jsonObj.put("assignedUsername", Messages.getInstance().getMessage(Messages.KEY_COMMON_JS_NO_ONE, loginUser.getLanguageId()));
                }
                super.writeRespJSON(response, ErrorCode.OK, jsonObj);
                logger.debug("Task updated successfully.");
            }
            break;
        }
        return null;
    }

    private ActionForward subscribe(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) throws IOException {
        int taskId = StringUtils.str2int(request.getParameter(PARAM_TASK_ID));
        try {
            int ret = queueService.subscribe(loginUser.getUid(), taskId);
            if (ret == ErrorCode.OK) {
                super.writeRespJSON(response, ErrorCode.OK, "");
            } else if (ret == ErrorCode.ERR_NO_PERMISSION) {
                super.writeRespJSON(response, ErrorCode.ERR_NO_PERMISSION, "", Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_NO_PERMISSION_UPDATE_QUEUE, loginUser.getLanguageId()));
            } else {
                super.writeRespJSON(response, ErrorCode.ERR_UNKNOWN, "");
            }
        } catch (Exception ex) {
            logger.error("ERROR!", ex);
        }
        return null;
    }

    private ActionForward unsubscribe(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) throws IOException {
        int taskId = StringUtils.str2int(request.getParameter(PARAM_TASK_ID));
        try {
            int ret = queueService.unsubscribe(loginUser.getUid(), taskId);
            if (ret == ErrorCode.OK) {
                super.writeRespJSON(response, ErrorCode.OK, "");
            } else if (ret == ErrorCode.ERR_NO_PERMISSION) {
                super.writeRespJSON(response, ErrorCode.ERR_NO_PERMISSION, "", Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_NO_PERMISSION_UPDATE_QUEUE, loginUser.getLanguageId()));
            } else {
                super.writeRespJSON(response, ErrorCode.ERR_UNKNOWN, "");
            }
        } catch (Exception ex) {
            logger.error("ERROR!", ex);
        }
        return null;
    }

    private ActionForward addQueue(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) throws IOException {

        int taskId = StringUtils.str2int(request.getParameter(PARAM_TASK_ID));
        int targetId = StringUtils.str2int(request.getParameter(PARAM_TARGET_ID));

        logger.debug("Request Parameters: \n\ttaskId=" + taskId + "\n\ttargetId=" + targetId);

        try {
            TaskAssignmentView taView = queueService.createDefaultTaskAssignment(taskId, targetId, loginUser.getLanguageId());
            if (taView == null) {
                logger.debug("Can't add queue task");
                super.writeRespJSON(response, ErrorCode.ERR_NO_PERMISSION, "", Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_NO_PERMISSION_UPDATE_QUEUE, loginUser.getLanguageId()));
            } else {
                logger.debug("Queue task added");
                super.writeRespJSON(response, taskAssignmentView2JSON(taView));
            }
        } catch (Exception ex) {
            logger.error("ERROR!", ex);
        }
        return null;
    }

    private ActionForward delQueue(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser, int productId) throws IOException {
        int taId = StringUtils.str2int(request.getParameter(PARAM_TASK_ASSIGNMENT_ID));
        logger.debug("Del Queue: \n\tproductId=" + productId + "\n\ttaId=" + taId);

        if (!isProductAdmin(loginUser)) {
            logger.error("No permission to update the task assignment!");
            super.writeRespJSON(response, ErrorCode.ERR_NO_PERMISSION, "", Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_NO_PERMISSION_UPDATE_QUEUE, loginUser.getLanguageId()));
        } else {
            int ret = queueService.delTaskAssignment(taId);
            if (ErrorCode.OK == ret) {
                super.writeRespJSON(response, ErrorCode.OK, "");
            } else if (ErrorCode.ERR_NON_EXISTENT == ret) {
                super.writeRespJSON(response, ErrorCode.ERR_NON_EXISTENT, "", Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_NOT_EXISTS_TA, loginUser.getLanguageId()));
            } else if (ErrorCode.ERR_LOWER_MINIUM == ret) {
                super.writeRespJSON(response, ErrorCode.ERR_LOWER_MINIUM, "", Messages.getInstance().getMessage(Messages.KEY_COMMON_ERR_ONLLY_ONE_TA, loginUser.getLanguageId()));
            }
        }
        return null;
    }

    private ActionForward findQueues(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser, int productId) throws IOException {
        String taskType = request.getParameter("tasktype");
        int taskStatus = StringUtils.str2int(request.getParameter(PARAM_TASK_STATUS));
        int assignedStatus = StringUtils.str2int(request.getParameter(PARAM_ASSIGNED_STATUS));
        int taskId = StringUtils.str2int(request.getParameter(PARAM_TASK_ID));
        List<Integer> targetIds = ListUtils.strArrToList(request.getParameterValues(PARAM_TARGET_IDS));

        logger.debug("Find queues by filter: \n\ttaskType=" + taskType + "\n\tproductId=" + productId + "\n\ttaskStatus=" + taskStatus
                + "\n\tassignedStatus=" + assignedStatus + "\n\ttaskId=" + taskId + "\n\ttargetIds=" + targetIds);

        List<QueueTaskView> queueTaskList = null;
        
        boolean isAdmin = isProductAdmin(loginUser);

        List<Task> tasks;

        if (taskType != null && taskType.equalsIgnoreCase("starting")) {
            tasks = isAdmin ? taskService.getUnassignedTasksByProjectId(loginUser.getPrjid()) : taskService.getUserUnassignedTasksByProjectId(loginUser.getPrjid(), loginUser.getUid());
            queueTaskList = queueService.findQueueTasks(isAdmin, loginUser.getPrjid(), loginUser, QueueService.FILTER_STATUS_STARTING, tasks, null, QueueService.ASSIGNED_STATUS_NOT_ASSIGNED);
        } else {
            Product product = productDao.get(productId);

            if (product == null) {
                logger.error("Invalid product ID " + productId);
                return null;
            }

            if (taskId > 0) {
                Task task = taskService.getTaskById(taskId);
                if (task == null) {
                    logger.error("Invalid task ID: " + taskId);
                    return null;
                }
                tasks = new ArrayList<Task>();
                tasks.add(task);
            } else {
                tasks = isAdmin ? taskService.getTasksByProductId(productId) : taskService.getUserTasksByProductId(productId, loginUser.getUid());
            }

            queueTaskList = queueService.findQueueTasks(isAdmin, product.getProjectId(), loginUser, taskStatus, tasks, targetIds, assignedStatus);
        }
        
        JSONArray qtArr = new JSONArray();

        if (!ListUtils.isEmptyList(queueTaskList)) {
            //request.setAttribute(FWD_QUEUE_TASKLIST, queueTaskList);
            for (QueueTaskView qt : queueTaskList) {
                qtArr.add(getQueueTaskViewToJson(qt));
            }
        }
        
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("isPM", isAdmin);
        jsonObj.put("productId", productId);
        jsonObj.put("qtList", qtArr);
        super.writeRespJSON(response, jsonObj);
        return null;
    }

    private ActionForward refreshFilterOptions(HttpServletResponse response, int productId, LoginUser loginUser) throws IOException {
        logger.debug("Get filter options for product: " + productId);

        boolean isAdmin = isProductAdmin(loginUser);

        List<Task> tasks = isAdmin ? taskService.getTasksByProductId(productId) : taskService.getUserTasksByProductId(productId, loginUser.getUid());
        JSONArray taskJsonArr = new JSONArray();
        if (tasks != null && !tasks.isEmpty()) {
            for (Task t : tasks) {
                JSONObject obj = new JSONObject();
                obj.put("id", t.getId());
                obj.put("name", t.getTaskName());
                taskJsonArr.add(obj);
            }
            
            // add an entry for ALL at the end
            /**
            JSONObject obj = new JSONObject();
            obj.put("id", 0);
            obj.put("name", Messages.getInstance().getMessage("jsp.choice.all", loginUser.getLanguageId()));
            taskJsonArr.add(obj);
             * ***/
        }

        List<Target> targets = trgtService.getTargetsByProductId(productId);
        JSONArray targetJsonArr = new JSONArray();
        if (targets != null && !targets.isEmpty()) {
            for (Target t : targets) {
                JSONObject obj = new JSONObject();
                obj.put("id", t.getId());
                obj.put("name", t.getName());
                targetJsonArr.add(obj);
            }         
        }
        JSONObject root = new JSONObject();
        root.put("tasks", taskJsonArr);
        root.put("targets", targetJsonArr);

        int taskCount = (tasks == null) ? 0 : tasks.size();
        int targetCount = (targets == null) ? 0 : targets.size();

        logger.debug("Found " + taskCount + " tasks and " + targetCount + " targets.");

        super.writeRespJSON(response, root);
        return null;
    }

    private JSONObject getQueueTaskViewToJson(QueueTaskView queueTask) {
        JSONObject root = new JSONObject();
        if (queueTask == null) {
            return root;
        }
        root.put("taskId", queueTask.getTaskId());
        //root.put("productId", queueTask.getProductId());
        root.put("taskName", queueTask.getTaskName());
        root.put("subscribed", queueTask.isSubscribed());
        root.put("canClaim", queueTask.isCanClaim());
        root.put("multiUser", queueTask.getIsMultiUser());
        JSONArray targetedTaskArr = new JSONArray();
        List<TargetBasedTaskView> targetBasedTasks = queueTask.getTargetBasedTasks();
        if (targetBasedTasks != null && !targetBasedTasks.isEmpty()) {
            for (TargetBasedTaskView ttask : targetBasedTasks) {
                JSONObject tObj = new JSONObject();
                tObj.put("goalObjStatus", ttask.getGoalObjectStatus());
                tObj.put("targetId", ttask.getTargetId());
                tObj.put("targetName", ttask.getTargetName());
                JSONArray taArr = new JSONArray();
                List<TaskAssignmentView> taList = ttask.getTaskAssigns();
                if (taList != null && !taList.isEmpty()) {
                    for (TaskAssignmentView taView : taList) {
                        taArr.add(taskAssignmentView2JSON(taView));
                    }
                }
                tObj.put("taList", taArr);
                targetedTaskArr.add(tObj);
            }
            root.put("targetedTaskList", targetedTaskArr);
        }
        return root;
    }

    private JSONObject taskAssignmentView2JSON(TaskAssignmentView taView) {
        JSONObject taObj = new JSONObject();
        taObj.put("username", taView.getUsername());
        taObj.put("priorityDisplay", taView.getPriority());
        taObj.put("status", taView.getStatus());
        taObj.put("statusDisplay", taView.getStatusDisplay());
        taObj.put("inQueueDays", taView.getInQueueDays());
        TaskAssignment ta = taView.getTaskAssignment();
        taObj.put("taId", ta.getId());
        taObj.put("assignedUserId", ta.getAssignedUserId());
        taObj.put("priority", ta.getQPriority());
        List<QueueUser> qualifiedUsers = taView.getQualifiedUsers();
        if (qualifiedUsers != null && !qualifiedUsers.isEmpty()) {
            JSONArray quArr = new JSONArray();
            for (QueueUser u : qualifiedUsers) {
                JSONObject quObj = new JSONObject();
                quObj.put("uid", u.getUserId());
                quObj.put("uname", u.getUserName());
                quArr.add(quObj);
            }
            taObj.put("quList", quArr);
        }
        return taObj;
    }

    @Autowired
    public void setQueueService(QueueService queueService) {
        this.queueService = queueService;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;


    }

    @Autowired
    public void setProductDAO(ProductDAO productDao) {
        this.productDao = productDao;
    }
    /*
    @Autowired
    public void setHorseService(HorseService horseService) {
    this.horseService = horseService;
    }
     */
}
