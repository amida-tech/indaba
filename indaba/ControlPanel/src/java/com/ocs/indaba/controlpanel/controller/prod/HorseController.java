/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller.prod;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.controlpanel.common.ControlPanelErrorCode;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.controller.BaseController;
import com.ocs.indaba.po.Horse;
import com.ocs.indaba.po.WorkflowObject;
import com.ocs.indaba.service.HorseService;
import com.ocs.indaba.vo.HorseInfo;
import com.ocs.util.Pagination;
import com.ocs.util.Pair;
import com.ocs.util.StringUtils;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
public class HorseController extends BaseController {

    private static final Logger logger = Logger.getLogger(HorseController.class);
    private static final long serialVersionUID = -2487852558172383390L;
    private static final String PARAM_NAME = "prodName";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_MODE = "mode";
    private static final String PARAM_CONFIG_ID = "configId";
    private static final String PARAM_WORKFLOW_ID = "workflowId";
    private static final String PARAM_DESCRIPTION = "description";
    private static final String PARAM_ACTION_RIGHT = "prodActionRight";
    private static final String ACTION_START = "start";
    private static final String ACTION_STOP = "stop";
    private static final String ACTION_CANCEL = "cancel";
    private static final String ACTION_UNCANCEL = "uncancel";
    private static final String ACTION_RESET = "reset";
    @Autowired
    private HorseService horseSrvc;

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
            Pagination<HorseInfo> pagination = horseSrvc.getHorsesByProductId(prodId, sortName, sortOrder, page, pageSize);
            
            pagination.addProperty(PARAM_PRODUCT_ID, prodId);
            logger.debug(pagination);
            sendResponseMessage(pagination.toJsonString());
        } catch (Exception ex) {
            logger.error("Error occurs!", ex);
        }

        return RESULT_EMPTY;
    }

    public String delete() {
        int prodId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID));
        logger.debug("Request Params: prodId=" + prodId);
        try {
            //prodSrvc
            super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        } catch (Exception ex) {
            logger.debug("Error occurs!", ex);
        }
        return RESULT_EMPTY;
    }

    public String get() {
        int prodId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID));
        JSONObject jsonObj = new JSONObject();
        super.sendResponseResult(ControlPanelErrorCode.OK, jsonObj, "OK");

        return RESULT_EMPTY;
    }

    public String save() {
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        int prodId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID));
        short type = StringUtils.str2short(request.getParameter(PARAM_TYPE));
        short mode = StringUtils.str2short(request.getParameter(PARAM_MODE));
        int workflowId = StringUtils.str2int(request.getParameter(PARAM_WORKFLOW_ID));
        int configId = StringUtils.str2int(request.getParameter(PARAM_CONFIG_ID));
        String name = request.getParameter(PARAM_NAME);
        String description = request.getParameter(PARAM_DESCRIPTION);
        int actionRight = StringUtils.str2int(request.getParameter(PARAM_ACTION_RIGHT));
        logger.debug("Request Params: "
                + "\n\tprodId=" + prodId
                + "\n\ttype=" + type
                + "\n\tprojId=" + projId
                + "\n\tname=" + name
                + "\n\tdescription=" + description
                + "\n\tworkflowId=" + workflowId
                + "\n\tconfigId=" + configId
                + "\n\tmode=" + mode
                + "\n\tactionRight=" + actionRight);

        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return RESULT_EMPTY;
    }

    public String applyAction() {
        String action = request.getParameter(PARAM_ACTION);
        String[] horseIds = request.getParameterValues(PARAM_HORSE_IDS);
        logger.debug("Request Params: "
                + "\n\taction=" + action
                + "\n\tCOUNT(horse)=" + horseIds.length);
        JSONArray errors = new JSONArray();
        JSONArray successes = new JSONArray();
        if (horseIds != null) {
            List<Integer> horseIdList = new ArrayList<Integer>();
            for (String s : horseIds) {
                horseIdList.add(StringUtils.str2int(s));
            }

            List<Horse> horseList = horseSrvc.getHorsesByIds(horseIdList);

            if (horseList != null && !horseList.isEmpty()) {

                Map<Integer, String> targetMap = horseSrvc.getTargetsByHorseIds(horseIdList);

                for (Horse horse : horseList) {
                    Pair<Integer, String> result = handle(horse, action);
                    JSONObject resultJsonObj = new JSONObject();
                    resultJsonObj.put("ret", result.getK1());
                    resultJsonObj.put("msg", result.getK2());
                    resultJsonObj.put("target", targetMap.get(horse.getId()));
                    if (result.getK1() == ControlPanelErrorCode.OK) {
                        successes.add(resultJsonObj);
                    } else {
                        errors.add(resultJsonObj);
                    }
                }
            }
        }
        JSONObject root = new JSONObject();
        root.put("successes", successes);
        root.put("errors", errors);
        super.sendResponseResult(ControlPanelErrorCode.OK, root, "OK");
        return RESULT_EMPTY;
    }

    public Pair<Integer, String> handle(Horse horse, String action) {
        WorkflowObject workflow = horseSrvc.getWorkflowObject(horse.getWorkflowObjectId());
        int status = workflow.getStatus();
        if (ACTION_START.equalsIgnoreCase(action)) {
            // handle 'start' action here
            if (status != Constants.HORSE_STATUS_INITIAL && status != Constants.HORSE_STATUS_STOPPED) {
                return new Pair<Integer, String>(ControlPanelErrorCode.ERR_NO_PERMISSION, getMessage(ControlPanelMessages.KEY_ERROR_HORSE_MUST_BE_INIT_OR_STOPPED));
            }
            horseSrvc.updateWorkflowObjectStatus(horse.getWorkflowObjectId(), Constants.HORSE_STATUS_RUNNING);
        } else if (ACTION_STOP.equalsIgnoreCase(action)) {
            // handle 'stop' action here
            if (status != Constants.HORSE_STATUS_RUNNING) {
                return new Pair<Integer, String>(ControlPanelErrorCode.ERR_NO_PERMISSION, getMessage(ControlPanelMessages.KEY_ERROR_HORSE_MUST_BE_RUNNING));
            }
            horseSrvc.updateWorkflowObjectStatus(horse.getWorkflowObjectId(), Constants.HORSE_STATUS_STOPPED);
        } else if (ACTION_CANCEL.equalsIgnoreCase(action)) {
            // handle 'cancel' action here
            if (status == Constants.HORSE_STATUS_CANCELLED) {
                return new Pair<Integer, String>(ControlPanelErrorCode.ERR_NO_PERMISSION, getMessage(ControlPanelMessages.KEY_ERROR_HORSE_IS_CANCELLED));
            }
            horseSrvc.updateWorkflowObjectCancel(horse.getWorkflowObjectId());
        } else if (ACTION_UNCANCEL.equalsIgnoreCase(action)) {
            // handle 'uncancel' action here
            if (status != Constants.HORSE_STATUS_CANCELLED) {
                return new Pair<Integer, String>(ControlPanelErrorCode.ERR_NO_PERMISSION, getMessage(ControlPanelMessages.KEY_ERROR_HORSE_MUST_BE_CANCELLED));
            }
            horseSrvc.updateWorkflowObjectUncancel(horse.getWorkflowObjectId());
        } else if (ACTION_RESET.equalsIgnoreCase(action)) {
            // handle 'reset' action here
            if (status == Constants.HORSE_STATUS_INITIAL) {
                return new Pair<Integer, String>(ControlPanelErrorCode.ERR_NO_PERMISSION, getMessage(ControlPanelMessages.KEY_ERROR_HORSE_MUST_BE_INIT));
            }
            int rt = horseSrvc.resetHorse(horse);
            if (rt != 0) {
                return new Pair<Integer, String>(ControlPanelErrorCode.ERR_DB, getMessage(ControlPanelMessages.KEY_ERROR_INVOKE_DB_STORE_PROCEDURE, rt));
            }
        } else {
            // invalid mode
            return new Pair<Integer, String>(ControlPanelErrorCode.ERR_INVALID_PARAM, "Invalid Horse Mode");
        }
        return new Pair<Integer, String>(ControlPanelErrorCode.OK, "OK");
    }

    public String execute() {
        return index();
    }
}
