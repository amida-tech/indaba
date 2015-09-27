/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller.proj;

import com.ocs.common.Config;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.controlpanel.common.ControlPanelConfig;
import com.ocs.indaba.controlpanel.common.ControlPanelErrorCode;
import com.ocs.indaba.controlpanel.controller.BaseController;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.controlpanel.model.ProjectVO;
import com.ocs.indaba.po.*;
import com.ocs.indaba.service.AccessMatrixService;
import com.ocs.indaba.service.ProductService;
import com.ocs.indaba.service.ProjectService;
import com.ocs.indaba.service.TargetService;
import com.ocs.indaba.vo.ProductVO;
import com.ocs.indaba.vo.ProjectMemberShipVO;
import com.ocs.util.Pagination;
import com.ocs.util.StringUtils;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ResultPath;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Project management
 *
 * @author Jeff Jiang
 *
 */
@ResultPath("/WEB-INF/pages")
public class TriggerController extends BaseController {

    private static final Logger logger = Logger.getLogger(TriggerController.class);
    private static final String UPLOAD_BASE = Config.getString(ControlPanelConfig.KEY_STORAGE_UPLOAD_BASE);
    private static final long serialVersionUID = -2487852558172383390L;
    private static final String PARAM_PRODUCT_ID = "prodId";
    private static final String PARAM_PROJECT_ID = "projId";
    private static final String PARAM_NAME = "prodName";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_MODE = "mode";
    private static final String PARAM_CONFIG_ID = "configId";
    private static final String PARAM_WORKFLOW_ID = "workflowId";
    private static final String PARAM_DESCRIPTION = "description";
    private static final String PARAM_ACTION_RIGHT = "prodActionRight";
    //
    // Attribute keys
    private static final String ATTR_PROJECT_JSON = "projJson";
    @Autowired
    private ProjectService projSrvc;
    @Autowired
    private ProductService prodSrvc;
    @Autowired
    private AccessMatrixService accessMatrixSrvc;
    @Autowired
    private TargetService targetSrvc;

    public String index() {
        int pageSize = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGESIZE));
        int page = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGE));
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        String sortName = request.getParameter(PARAM_SORT_NAME);
        String sortOrder = request.getParameter(PARAM_SORT_ORDER);
        LoginUser loginUser = super.getLoginUser();
        int userId = (loginUser == null) ? 1 : loginUser.getUserId();
        boolean isSysAdmin = (loginUser == null) ? false : loginUser.isSiteAdmin();
        // TODO: check user access permission.
        logger.debug("Request Params: "
                + "\n\tuserId=" + userId + "(" + (isSysAdmin ? "ADMIN" : "NON-ADMIN") + ")"
                + "\n\tpageSize=" + pageSize
                + "\n\tpage=" + page
                + "\n\tprojId=" + projId
                + "\n\tsortName=" + sortName
                + "\n\tsortOrder=" + sortOrder);
        List<ProjectVO> projects = null;
        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            sortOrder = "asc";
        }
        try {
            Pagination<ProjectMemberShipVO> pagination = projSrvc.getProjectMembershipsByProjectId(projId, sortName, sortOrder, page, pageSize);

            logger.debug(pagination);
            sendResponseMessage(pagination.toJsonString());
        } catch (Exception ex) {
            logger.error("Error occurs!", ex);
        }

        return RESULT_EMPTY;
    }

    public String find() {
        int pageSize = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGESIZE));
        int page = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGE));
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        String sortName = request.getParameter(PARAM_SORT_NAME);
        String sortOrder = request.getParameter(PARAM_SORT_ORDER);
        //LoginUser loginUser = super.getLoginUser();
        //int userId = (loginUser == null) ? 1 : loginUser.getUserId();
        //boolean isSysAdmin = (loginUser == null) ? false : loginUser.isSiteAdmin();
        // TODO: check user access permission.
        logger.debug("Request Params: "
                + "\n\tpageSize=" + pageSize
                + "\n\tpage=" + page
                + "\n\tprojId=" + projId
                + "\n\tsortName=" + sortName
                + "\n\tsortOrder=" + sortOrder);
        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            sortOrder = "asc";
        }
        try {
            Pagination<ProductVO> pagination = prodSrvc.getProductsByProjectId(projId, sortName, sortOrder, page, pageSize);

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
        Product prod = prodSrvc.getProductById(prodId);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("id", prod.getId());
        jsonObj.put("name", prod.getName());
        jsonObj.put("type", prod.getContentType());
        jsonObj.put("actionRight", prod.getAccessMatrixId());
        jsonObj.put("description", prod.getDescription());
        jsonObj.put("mode", prod.getMode());
        jsonObj.put("configId", prod.getProductConfigId());
        jsonObj.put("projId", prod.getProjectId());
        jsonObj.put("workflowId", prod.getWorkflowId());
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

        Product prod = new Product();
        prod.setAccessMatrixId(actionRight);
        prod.setContentType(type);
        prod.setDescription(description);
        prod.setId(prodId);
        prod.setMode(mode);
        prod.setName(name);
        prod.setProjectId(projId);
        Date now = new Date();
        if (prodId > 0) {
            prod.setProductConfigId(configId);
            prod.setWorkflowId(workflowId);
            prod = prodSrvc.updateProduct(prod);
        } else {
            LoginUser loginUser = getLoginUser();
            Workflow workflow = new Workflow();
            workflow.setCreatedByUserId(loginUser.getUserId());
            workflow.setCreatedTime(now);
            workflow.setDescription("");
            workflow.setName("Workflow of product '" + name + "'");
            workflow.setTotalDuration(0);
            workflow = prodSrvc.createWorkflow(workflow);
            prod.setWorkflowId(workflow.getId());
            if (type == Constants.CONTENT_TYPE_SURVEY) {
                SurveyConfig sc = new SurveyConfig();
                sc.setCreateTime(now);
                sc.setCreatorOrgId(loginUser.getOrgId());
                sc.setDescription("");
                sc.setInstructions("");
                sc.setName("Survey config of product '" + name + "'");
                sc.setStatus((short) 1);
                sc.setVisibility((short) 1);
                sc = prodSrvc.createSurveyConfig(null);
                prod.setProductConfigId(sc.getId());
            } else {
                JournalConfig jc = new JournalConfig();
                jc.setDescription("");
                jc.setInstructions("");
                jc.setMaxWords(1000000);
                jc.setMinWords(1000);
                jc = prodSrvc.createJournalConfig(jc);
                prod.setProductConfigId(jc.getId());
            }
            prod = prodSrvc.addProduct(prod);
        }
        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return RESULT_EMPTY;
    }

    public String execute() {
        return index();
    }
}
