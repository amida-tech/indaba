/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller.prod;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.controlpanel.common.ControlPanelConstants;
import com.ocs.indaba.controlpanel.common.ControlPanelErrorCode;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.controller.BaseController;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.ProjectService;
import com.ocs.indaba.service.TargetService;
import com.ocs.indaba.service.TaskService;
import com.ocs.indaba.vo.QueueUser;
import com.ocs.indaba.vo.TaskAssignmentVO;
import com.ocs.util.JSONUtils;
import com.ocs.util.Pagination;
import com.ocs.util.StringUtils;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * Project management
 *
 * @author Jeff Jiang
 *
 */
@ResultPath("/WEB-INF/pages")
@Results({
    @Result(name = "index", location = "product.jsp")})
public class TaskAssignmentController extends BaseController {

    private static final Logger logger = Logger.getLogger(TaskAssignmentController.class);
    private static final long serialVersionUID = -2487852558172383390L;
    private static final String PARAM_ASSIGNMENT_ID = "assignmentId";
    private static final String PARAM_TASK_ID = "taskId";
    private static final String PARAM_TARGET_ID = "targetId";
    private static final String ATTR_TASKS = "tasks";
    private static final String ATTR_TARGETS = "targets";
    private static final String ATTR_USERS = "users";
    private static final String ATTR_ASSIGNMENT = "assignment";
    @Autowired
    private ProjectService projSrvc;
    @Autowired
    private TaskService taskSrvc;
    @Autowired
    private TargetService targetSrvc;

    public String index() {
        logger.debug("I'M CALLED!!!!");
        return RESULT_INDEX;
    }

    public String find() {
        int pageSize = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGESIZE));
        int page = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGE));
        int prodId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID));
        int taskId = StringUtils.str2int(request.getParameter(PARAM_TASK_ID));
        int targetId = StringUtils.str2int(request.getParameter(PARAM_TARGET_ID));
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
                + "\n\ttaskId=" + taskId
                + "\n\ttargetId=" + targetId
                + "\n\tsortName=" + sortName
                + "\n\tsortOrder=" + sortOrder);
        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            sortOrder = "asc";
        }
        try {
            Map<String, Object> filters = new HashMap<String, Object>();
            filters.put("taskid", taskId);
            filters.put("targetid", targetId);
            
            Pagination<TaskAssignmentVO> pagination = taskSrvc.getTaskAssignmentsByFilter(prodId, filters, sortName, sortOrder, page, pageSize);
            pagination.addProperty(PARAM_PRODUCT_ID, prodId);

            logger.debug(pagination);
            sendResponseMessage(pagination.toJsonString());
        } catch (Exception ex) {
            logger.error("Error occurs!", ex);
        }

        return RESULT_EMPTY;
    }

    public String deleteable() {
        int assignmentId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNMENT_ID));
        logger.debug("Deleteable Request Params: assignmentId=" + assignmentId);
        sendResponseJson(deleteable(assignmentId));
        return RESULT_EMPTY;
    }

    public String delete() {
        int assignmentId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNMENT_ID));
        logger.debug("Delete Request Params: assignmentId=" + assignmentId);
        JSONObject retObj = deleteable(assignmentId);
        if ((Integer) retObj.get(KEY_RET) == ControlPanelErrorCode.OK) {
            taskSrvc.deleteTaskAssignmentsByAssignmentId(assignmentId);
        }
        sendResponseJson(retObj);
        return RESULT_EMPTY;
    }

    private JSONObject deleteable(int assignmentId) {
        LoginUser loginUser = getLoginUser();
        JSONObject retObj = new JSONObject();
        TaskAssignment taskAssignment = taskSrvc.getTaskAssignment(assignmentId);
        if (taskAssignment != null) {
            switch (taskAssignment.getStatus()) {
                case Constants.TASK_STATUS_INACTIVE: {
                    retObj.put(KEY_RET, ControlPanelErrorCode.OK);
                    retObj.put(KEY_DESC, "OK");
                    break;
                }
                case Constants.TASK_STATUS_ACTIVE:
                case Constants.TASK_STATUS_AWARE:
                case Constants.TASK_STATUS_NOTICED:
                case Constants.TASK_STATUS_STARTED: {
                    retObj.put(KEY_RET, ControlPanelErrorCode.ERR_TASK_INFLIGHT);
                    retObj.put(KEY_DESC, loginUser.message(Messages.KEY_COMMON_ERR_TASK_ASSIGNMENT_INFLIGHT));
                    break;
                }
                case Constants.TASK_STATUS_DONE: {
                    retObj.put(KEY_RET, ControlPanelErrorCode.ERR_TASK_DONE);
                    retObj.put(KEY_DESC, loginUser.message(Messages.KEY_COMMON_ERR_TASK_ASSIGNMENT_DONE));
                    break;
                }
                default: {
                    retObj.put(KEY_RET, ControlPanelErrorCode.ERR_DB);
                    retObj.put(KEY_DESC, loginUser.message(Messages.KEY_COMMON_ERR_INTERNAL_SERVER));
                    logger.error("Task assignment " + assignmentId + " in an unknown status + " + taskAssignment.getStatus());
                }
            }
        } else {
            retObj.put(KEY_RET, ControlPanelErrorCode.ERR_NOT_EXISTS);
            retObj.put(KEY_DESC, loginUser.message(Messages.KEY_COMMON_ERR_TASK_ASSIGNMENT_NOT_EXIST));
        }
        return retObj;
    }

    public String get() {
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        int prodId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID));
        int assignmentId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNMENT_ID));
        logger.debug("Request Params: "
                + "\n\tprojId=" + projId
                + "\n\tprodId=" + prodId
                + "\n\tassignmentId=" + assignmentId);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put(PARAM_PROJECT_ID, projId);
            jsonObj.put(PARAM_PRODUCT_ID, prodId);
            //jsonObj.put(PARAM_ASSIGNMENT_ID, assignmentId);
            jsonObj.put(ATTR_TASKS, JSONUtils.listToJson(taskSrvc.getTasksByProductId(prodId)));
            jsonObj.put(ATTR_TARGETS, JSONUtils.listToJson(targetSrvc.getTargetsByProjectId(projId)));
            //jsonObj.put(ATTR_USERS, JSONUtils.listToJson(projSrvc.getUsersByProjectId(projId)));

            if (assignmentId > 0) {
                TaskAssignment taskAssignment = taskSrvc.getTaskAssignment(assignmentId);
                if (taskAssignment != null) {
                    List<QueueUser> users = getEligibleUsers(taskAssignment.getTaskId(), projId);
                    jsonObj.put(ATTR_ASSIGNMENT, JSONUtils.toJson(taskAssignment));
                    jsonObj.put(ATTR_USERS, JSONUtils.listToJson(users));
                }
            }
        } catch (Exception ex) {
            logger.error("Error occurs!", ex);

        }
        super.sendResponseResult(ControlPanelErrorCode.OK, jsonObj, "OK");

        return RESULT_EMPTY;
    }

    private List<QueueUser> getEligibleUsers(int taskId, int projId) {
        List<QueueUser> users = userSrvc.getUsersByTaskId(taskId, projId);

        // add a "NO ONE" user
        if (users == null) users = new ArrayList<QueueUser>();
        QueueUser nobody = new QueueUser();
        nobody.setUserId(0);
        nobody.setUserName("NO ONE");
        users.add(0, nobody);

        return users;
    }


    public String updateUserOptionsByTaskId() {
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        int taskId = StringUtils.str2int(request.getParameter(PARAM_TASK_ID));
        logger.debug("Request Params: "
                + "\n\tprojId=" + projId
                + "\n\ttaskId=" + taskId);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put(ATTR_USERS, JSONUtils.listToJson(getEligibleUsers(taskId, projId)));
        } catch (Exception ex) {
            logger.error("Error occurs!", ex);

        }
        super.sendResponseResult(ControlPanelErrorCode.OK, jsonObj, "OK");
        return RESULT_EMPTY;
    }

    public String save() {
        int assignmentId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNMENT_ID));
        int userId = StringUtils.str2int(request.getParameter(PARAM_USER_ID));

        logger.debug("Save Request Params: "
                + "\n\tassignmentId=" + assignmentId
                + "\n\tuserId=" + userId);

        LoginUser loginUser = getLoginUser();
        JSONObject retObj = new JSONObject();

        int ret = taskSrvc.updateTaskAssignment(assignmentId, userId);

        switch (ret) {
            case ControlPanelConstants.UPDATE_ASSIGNMENT_RT_OK:
                retObj.put(KEY_RET, ControlPanelErrorCode.OK);
                retObj.put(KEY_DESC, "OK");
                break;

            case ControlPanelConstants.UPDATE_ASSIGNMENT_RT_NO_SUCH_ASSIGNMENT:
                retObj.put(KEY_RET, ControlPanelErrorCode.ERR_NON_EXISTENT);
                retObj.put(KEY_DESC, loginUser.message(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_ASSIGNMENT));
                break;

            case ControlPanelConstants.UPDATE_ASSIGNMENT_RT_DUPLICATE_ASSIGNMENT_SINGLE:
                retObj.put(KEY_RET, ControlPanelErrorCode.ERR_DB);
                retObj.put(KEY_DESC, loginUser.message(ControlPanelMessages.KEY_ERROR_DUPLICATE_ASSIGNMENT_SINGLE));
                break;

            case ControlPanelConstants.UPDATE_ASSIGNMENT_RT_DUPLICATE_ASSIGNMENT_MULTI:
                retObj.put(KEY_RET, ControlPanelErrorCode.ERR_DB);
                retObj.put(KEY_DESC, loginUser.message(ControlPanelMessages.KEY_ERROR_DUPLICATE_ASSIGNMENT_MULTI));
                break;

            case ControlPanelConstants.UPDATE_ASSIGNMENT_RT_INVALID_TASK:
                retObj.put(KEY_RET, ControlPanelErrorCode.ERR_DB);
                retObj.put(KEY_DESC, loginUser.message(ControlPanelMessages.KEY_ERROR_INVALID_TASK_DEF));
                break;

            case ControlPanelConstants.UPDATE_ASSIGNMENT_RT_NO_SUCH_USER:
                retObj.put(KEY_RET, ControlPanelErrorCode.ERR_NON_EXISTENT);
                retObj.put(KEY_DESC, loginUser.message(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_USER));
                break;

            default:
                logger.debug("update assignment return code: " + ret);
                retObj.put(KEY_RET, ControlPanelErrorCode.ERR_UNKNOWN);
                retObj.put(KEY_DESC, loginUser.message(Messages.KEY_COMMON_ERR_INTERNAL_SERVER));
        }

        super.sendResponseJson(retObj);
        return RESULT_EMPTY;
    }

    public String create() {
        LoginUser loginUser = getLoginUser();
        JSONObject retObj = new JSONObject();

        int prodId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID));
        int targetId = StringUtils.str2int(request.getParameter(PARAM_TARGET_ID));
        int userId = StringUtils.str2int(request.getParameter(PARAM_USER_ID));
        int taskId = StringUtils.str2int(request.getParameter(PARAM_TASK_ID));

        logger.debug("Create Request Params: "
                + "\n\tprodId=" + prodId
                + "\n\ttargetId=" + targetId
                + "\n\ttaskId=" + taskId
                + "\n\tuserId=" + userId);

        int ret = taskSrvc.createAssignment(taskId, prodId, targetId, userId);
        
        switch (ret) {
            case ControlPanelConstants.ADD_ASSIGNMENT_RT_OK:
                retObj.put(KEY_RET, ControlPanelErrorCode.OK);
                retObj.put(KEY_DESC, "OK");
                break;

            case ControlPanelConstants.ADD_ASSIGNMENT_RT_GOAL_DONE:
                retObj.put(KEY_RET, ControlPanelErrorCode.ERR_DB);
                retObj.put(KEY_DESC, loginUser.message(Messages.KEY_COMMON_ERR_TASK_ASSIGNMENT_DONE));
                break;
            
            case ControlPanelConstants.ADD_ASSIGNMENT_RT_NO_PRODUCT_OR_IN_CONFIG:
                retObj.put(KEY_RET, ControlPanelErrorCode.ERR_DB);
                retObj.put(KEY_DESC, loginUser.message(Messages.KEY_COMMON_ERR_PROD_NO_OR_CONFIG));
                break;
            
            case ControlPanelConstants.ADD_ASSIGNMENT_RT_NO_HORSE:
                retObj.put(KEY_RET, ControlPanelErrorCode.ERR_NON_EXISTENT);
                retObj.put(KEY_DESC, loginUser.message(Messages.KEY_COMMON_ERR_HORSE_NOT_EXIST));
                break;

            case ControlPanelConstants.ADD_ASSIGNMENT_RT_DUPLICATE_ASSIGNMENT_SINGLE:
                retObj.put(KEY_RET, ControlPanelErrorCode.ERR_DB);
                retObj.put(KEY_DESC, loginUser.message(ControlPanelMessages.KEY_ERROR_DUPLICATE_ASSIGNMENT_SINGLE));
                break;

            case ControlPanelConstants.ADD_ASSIGNMENT_RT_DUPLICATE_ASSIGNMENT_MULTI:
                retObj.put(KEY_RET, ControlPanelErrorCode.ERR_DB);
                retObj.put(KEY_DESC, loginUser.message(ControlPanelMessages.KEY_ERROR_DUPLICATE_ASSIGNMENT_MULTI));
                break;

            case ControlPanelConstants.ADD_ASSIGNMENT_RT_INVALID_TASK:
                retObj.put(KEY_RET, ControlPanelErrorCode.ERR_DB);
                retObj.put(KEY_DESC, loginUser.message(ControlPanelMessages.KEY_ERROR_INVALID_TASK_DEF));
                break;

            case ControlPanelConstants.ADD_ASSIGNMENT_RT_NO_SUCH_USER:
                retObj.put(KEY_RET, ControlPanelErrorCode.ERR_NON_EXISTENT);
                retObj.put(KEY_DESC, loginUser.message(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_USER));
                break;

            default:
                logger.debug("create assignment return code: " + ret);
                retObj.put(KEY_RET, ControlPanelErrorCode.ERR_UNKNOWN);
                retObj.put(KEY_DESC, loginUser.message(Messages.KEY_COMMON_ERR_INTERNAL_SERVER));
        }

        super.sendResponseJson(retObj);
        return RESULT_EMPTY;
    }

    public String execute() {
        return index();
    }
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TASKS = "tasks";
    private static final String KEY_TARGETS = "targets";
}
