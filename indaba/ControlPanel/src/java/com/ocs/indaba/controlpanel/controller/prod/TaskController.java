/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller.prod;

import com.ocs.indaba.common.Messages;
import com.ocs.indaba.controlpanel.common.ControlPanelErrorCode;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.controller.BaseController;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.po.AccessMatrix;
import com.ocs.indaba.po.Task;
import com.ocs.indaba.po.TaskRole;
import com.ocs.indaba.service.AccessMatrixService;
import com.ocs.indaba.service.TaskService;
import com.ocs.indaba.vo.TaskVO;
import com.ocs.util.JSONUtils;
import com.ocs.util.Pagination;
import com.ocs.util.StringUtils;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Project management
 *
 * @author Jeff Jiang
 *
 */
@ResultPath("/WEB-INF/pages")
@Results({
    @Result(name = "index", location = "product.jsp")})
public class TaskController extends BaseController {

    private static final Logger logger = Logger.getLogger(TaskController.class);
    private static final long serialVersionUID = -2487852558172383390L;
    private static final String PARAM_TASK_ID = "taskId";
    private static final String PARAM_TASK_NAME = "taskName";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_TASK_ROLES = "roles";
    private static final String PARAM_DESCRIPTION = "description";
    private static final String PARAM_GOAL_ID = "goalId";
    private static final String PARAM_TOOL_ID = "toolId";
    private static final String PARAM_ASSIGNMENT_METHOD = "assignmentMethod";
    private static final String PARAM_INSTRUCTIONS = "instructions";
    private static final String ATTR_TASK = "task";
    private static final String ATTR_TASK_ROLES = "taskRoles";
    private static final int INVALID_TASK_ID = -1;
    @Autowired
    private TaskService taskSrvc;
    @Autowired
    private AccessMatrixService accessMatrixSrvc;

    public String index() {
        return RESULT_INDEX;
    }

    public String find() {
        int pageSize = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGESIZE));
        int page = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGE));
        int prodId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID));
        String sortName = request.getParameter(PARAM_SORT_NAME);
        String sortOrder = request.getParameter(PARAM_SORT_ORDER);
        //LoginUser loginUser = super.getLoginUser();
        //int userId = (loginUser == null) ? 1 : loginUser.getUserId();
        //boolean isSysAdmin = (loginUser == null) ? false : loginUser.isSiteAdmin();
        // TODO: check user access permission.
        logger.debug("Request Params: "
                + "\n\tpageSize=" + pageSize
                + "\n\tpage=" + page
                + "\n\tprodId=" + prodId
                + "\n\tsortName=" + sortName
                + "\n\tsortOrder=" + sortOrder);
        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            sortOrder = "asc";
        }
        try {
            Pagination<TaskVO> pagination = taskSrvc.getTasksByProductId(prodId, sortName, sortOrder, page, pageSize);
            pagination.addProperty(PARAM_PRODUCT_ID, prodId);
            logger.debug(pagination);
            sendResponseMessage(pagination.toJsonString());
        } catch (Exception ex) {
            logger.error("Error occurs!", ex);
        }

        return RESULT_EMPTY;
    }

    public String delete() {
        LoginUser loginUser = getLoginUser();
        int taskId = StringUtils.str2int(request.getParameter(PARAM_TASK_ID), INVALID_TASK_ID);
        logger.debug("Delet Task Request Params: taskId=" + taskId);
        Task task = taskSrvc.getTaskById(taskId);
        // either invalid task id or corresponding task not existing, treat as bad parameter
        if (task == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM,
                    loginUser.message(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_TASK));
            return RESULT_EMPTY;
        }
        try {
            if (task.getSticky() == 1) {
                // sticky tasks cannot be deleted
                String errMsg = getMessage(ControlPanelMessages.KEY_ERROR_CANT_DELETE_STICKY_TASK, task.getTaskName());
                super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, errMsg);
            } else {
                taskSrvc.deleteTask(taskId);
                super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
            }
        } catch (Exception ex) {
            logger.debug("Error occurs!", ex);
        }
        return RESULT_EMPTY;
    }

    public String get() {
        int taskId = StringUtils.str2int(request.getParameter(PARAM_TASK_ID));
        
        JSONObject jsonObj = new JSONObject();
        if (taskId > 0) {
            jsonObj.put(ATTR_TASK, JSONUtils.toJson(taskSrvc.getTaskById(taskId)));
            jsonObj.put(ATTR_TASK_ROLES, JSONUtils.listToJson(taskSrvc.getTaskRolesByTaskId(taskId)));
        }

        logger.debug("Task data: " + jsonObj);

        super.sendResponseResult(ControlPanelErrorCode.OK, jsonObj, "OK");

        return RESULT_EMPTY;
    }

    public String save() {
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        int prodId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID));
        int taskId = StringUtils.str2int(request.getParameter(PARAM_TASK_ID));
        int goalId = StringUtils.str2int(request.getParameter(PARAM_GOAL_ID));
        short type = StringUtils.str2short(request.getParameter(PARAM_TYPE));
        String description = request.getParameter(PARAM_DESCRIPTION);
        String instructions = request.getParameter(PARAM_INSTRUCTIONS);
        short assignmentMethod = StringUtils.str2short(request.getParameter(PARAM_ASSIGNMENT_METHOD));
        String name = request.getParameter(PARAM_TASK_NAME);
        int toolId = StringUtils.str2int(request.getParameter(PARAM_TOOL_ID));
        String roles = request.getParameter(PARAM_TASK_ROLES);
        logger.debug("Request Params: "
                + "\n\tprodId=" + prodId
                + "\n\ttaskId=" + taskId
                + "\n\tprojId=" + projId
                + "\n\tname=" + name
                + "\n\ttype=" + type
                + "\n\tdescription=" + description
                + "\n\tgoalId=" + goalId
                + "\n\tinstructions=" + instructions
                + "\n\tassignmentMethod=" + assignmentMethod
                + "\n\ttoolId=" + toolId
                + "\n\troles=" + roles);

        // check for required fields
        if (name == null || name.length() == 0) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM,
                        getMessage(ControlPanelMessages.KEY_ERROR_MISSING_TASK_NAME));
            return RESULT_EMPTY;
        }

        if (instructions == null || instructions.length() == 0) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM,
                        getMessage(ControlPanelMessages.KEY_ERROR_MISSING_TASK_INSTRUCTIONS));
            return RESULT_EMPTY;
        }

        if (toolId <= 0) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM,
                        getMessage(ControlPanelMessages.KEY_ERROR_MISSING_TASK_TOOL));
            return RESULT_EMPTY;
        }

        if (goalId <= 0) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM,
                        getMessage(ControlPanelMessages.KEY_ERROR_MISSING_TASK_GOAL));
            return RESULT_EMPTY;
        }

        if (prodId <= 0) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM,
                        getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_PRODUCT));
            return RESULT_EMPTY;
        }

         if (projId <= 0) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM,
                        getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_PROJECT));
            return RESULT_EMPTY;
        }

        if (StringUtils.isEmpty(roles)) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM,
                        getMessage(ControlPanelMessages.KEY_ERROR_MISSING_TASK_ROLES));
            return RESULT_EMPTY;
        }

        JSONArray taskRoles = null;
        JSONParser parser = new JSONParser();
        
        try {
            taskRoles = (JSONArray) parser.parse(roles);

            if (taskRoles.size() == 0) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM,
                        getMessage(ControlPanelMessages.KEY_ERROR_MISSING_TASK_ROLES));

                return RESULT_EMPTY;
            }
        } catch (Exception ex) {
            logger.error("Fail to parse roles JSON string", ex);

            super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM,
                        getMessage(ControlPanelMessages.KEY_ERROR_MISSING_TASK_ROLES));

            return RESULT_EMPTY;
        }

        Task task = null;

        if (taskId > 0) {
            // make sure this is the right task
            task = taskSrvc.validateTaskRelation(projId, prodId, taskId);

            if (task == null) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                        getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_TASK, name));
                return RESULT_EMPTY;
            }
        }

        if (taskId <= 0) {
            task = new Task();
        }

        task.setAssignmentMethod(assignmentMethod);
        task.setDescription(description);
        task.setGoalId(goalId);
        task.setInstructions(instructions);
        task.setProductId(prodId);
        task.setTaskName(name);
        task.setToolId(toolId);
        task.setType((short)toolId);   // type has the same value as toolId

        Task t = taskSrvc.getTaskByProductIdAndTaskName(prodId, name);

        if (taskId > 0) {
            // check name conflict
            if (t != null && t.getId() != taskId) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_EXISTS,
                        getMessage(ControlPanelMessages.KEY_DUPLICATED_TASK_NAME, name));
                return RESULT_EMPTY;
            }
            task = taskSrvc.updateTask(task);
        } else {
            if (t != null) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_EXISTS,
                        getMessage(ControlPanelMessages.KEY_DUPLICATED_TASK_NAME, name));
                return RESULT_EMPTY;
            }
            task = taskSrvc.createTask(task);
        }

        try {
            taskRoles = (JSONArray) parser.parse(roles);
            List<TaskRole> existedTaskRoles = taskSrvc.getTaskRolesByTaskId(taskId);
            for (Object o : taskRoles) {
                JSONObject jsonObj = (JSONObject) o;
                int rid = StringUtils.str2int((String) jsonObj.get("id"));
                boolean canClaim = (Boolean) jsonObj.get("claim");
                TaskRole tr = null;//taskSrvc.getTaskRole(task.getId(), rid);
                if (existedTaskRoles != null && !existedTaskRoles.isEmpty()) {
                    for (TaskRole r : existedTaskRoles) {
                        if (r.getRoleId() == rid) {
                            tr = r;
                            break;
                        }
                    }
                    if (tr != null) {
                        existedTaskRoles.remove(tr);
                    }
                }
                if (tr == null) {
                    tr = new TaskRole();
                }
                tr.setTaskId(task.getId());
                tr.setRoleId(rid);
                tr.setCanClaim((short) (canClaim ? 1 : 0)); // TBD: need to be set...
                if (tr.getId() == null) {
                    taskSrvc.addTaskRole(tr);
                } else {
                    taskSrvc.updateTaskRole(tr);
                }
            }
            if (existedTaskRoles != null && !existedTaskRoles.isEmpty()) {
                for (TaskRole r : existedTaskRoles) {
                    taskSrvc.deleteTaskRole(r.getId());
                }
            }
        } catch (Exception ex) {
            logger.error("Fail to process roles JSON string", ex);
        }

        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");

        return RESULT_EMPTY;
    }

    /**

    public String create() {
        LoginUser loginUser = getLoginUser();
        JSONObject retObj = new JSONObject();
        String name = request.getParameter(PARAM_TASK_NAME);
        String desc = request.getParameter(PARAM_DESCRIPTION);
        int goalId = StringUtils.str2int(request.getParameter(PARAM_GOAL_ID));
        int prodId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID));
        int toolId = StringUtils.str2int(request.getParameter(PARAM_TOOL_ID));
        short assignmentMethod = StringUtils.str2short(request.getParameter(PARAM_ASSIGNMENT_METHOD));
        String instructions = request.getParameter(PARAM_INSTRUCTIONS);
        short type = StringUtils.str2short(request.getParameter(PARAM_TYPE));
        List<Integer> roles = StringUtils.strArr2IntList(request.getParameterValues(ATTR_TASK_ROLES));
        logger.debug("Create Request Params: "
                + "\n\tname=" + name
                + "\n\tdescription=" + desc
                + "\n\tgoalId=" + goalId
                + "\n\tprodId=" + prodId
                + "\n\ttoolId=" + toolId
                + "\n\tassignmentMethod=" + assignmentMethod
                + "\n\tinstructions=" + instructions
                + "\n\ttype=" + type
                + "\n\troles=" + roles);

        Task task = taskSrvc.createTask(name, desc, goalId, prodId, toolId,
                assignmentMethod, instructions, type);

        if (task == null) {
            retObj.put(KEY_RET, ControlPanelErrorCode.OK);
            retObj.put(KEY_DESC, "OK");
        } else {
            retObj.put(KEY_RET, ControlPanelErrorCode.ERR_DB);
            retObj.put(KEY_DESC, loginUser.message(Messages.KEY_COMMON_ERR_INTERNAL_SERVER));
        }
        super.sendResponseJson(retObj);
        return RESULT_EMPTY;
    }
**/
    
    public String execute() {
        return index();
    }
}
