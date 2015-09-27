/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller.prod;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.controlpanel.common.ControlPanelConstants;
import com.ocs.indaba.controlpanel.common.ControlPanelErrorCode;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.controller.BaseController;
import com.ocs.indaba.controlpanel.model.LoginUser;
//import com.ocs.indaba.controlpanel.service.ProductControlPanelService;
import com.ocs.indaba.controlpanel.service.ProductControlPanelService;
import com.ocs.indaba.controlpanel.service.ProjectControlPanelService;
import com.ocs.indaba.dao.WorkflowDAO;
import com.ocs.indaba.po.AccessMatrix;
import com.ocs.indaba.po.Goal;
import com.ocs.indaba.po.Product;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.Target;
import com.ocs.indaba.po.Task;
import com.ocs.indaba.service.AccessMatrixService;
import com.ocs.indaba.service.ProductService;
import com.ocs.indaba.service.ProjectService;
import com.ocs.indaba.service.TargetService;
import com.ocs.indaba.service.TaskService;
import com.ocs.indaba.vo.ProductVO;
import com.ocs.util.JSONUtils;
import com.ocs.util.Pagination;
import com.ocs.util.StringUtils;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
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
public class ProductController extends BaseController {

    private static final Logger logger = Logger.getLogger(ProductController.class);
    private static final long serialVersionUID = -2487852558172383390L;
    private static final String PARAM_VISIBILITY = "visibility";
    private static final String PARAM_PRODUCT_NAME = "prodName";
    private static final String PARAM_CONTENT_TYPE = "contentType";
    private static final String PARAM_MODE = "mode";
    private static final String PARAM_CONTENT_CONFIG = "contentConfig";
    private static final String PARAM_WORKFLOW = "workflow";
    private static final String PARAM_DESCRIPTION = "description";
    private static final String ATTR_PRODUCT = "product";
    private static final String ATTR_JOURNAL_CONFIGS = "journalConfigs";
    private static final String ATTR_SURVEY_CONFIGS = "surveyConfigs";
    private static final String ATTR_WORKFLOWS = "workflows";
    private static final String ATTR_ROLES = "roles";
    private static final String ATTR_GOALS = "goals";
    private static final String ATTR_TOOLS = "tools";
    private static final String ATTR_PRODUCT_MODE = "prodMode";
    private static final String ATTR_TASKS = "tasks";
    private static final String ATTR_TARGETS = "targets";
    
    @Autowired
    private ProductService prodSrvc;
    @Autowired
    private ProjectService projSrvc;
    @Autowired
    private AccessMatrixService accessMatrixSrvc;
    @Autowired
    private WorkflowDAO workflowDao;
    @Autowired
    private ProjectControlPanelService projectCpService;
    @Autowired
    private ProductControlPanelService productCpService;
    @Autowired
    private TaskService taskSrvc;
    @Autowired
    private TargetService targetSrvc;

    public String index() {
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        int prodId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID));
        int visibility = StringUtils.str2int(request.getParameter(PARAM_VISIBILITY));
        LoginUser loginUser = super.getLoginUser();
        int userId = (loginUser == null) ? 1 : loginUser.getUserId();
        boolean isSysAdmin = (loginUser == null) ? false : loginUser.isSiteAdmin();
        // TODO: check user access permission.
        logger.debug("Request Params: "
                + "\n\tuserId=" + userId + "(" + (isSysAdmin ? "ADMIN" : "NON-ADMIN") + ")"
                + "\n\tprodId=" + prodId);
        request.setAttribute(PARAM_PROJECT_ID, projId);
        request.setAttribute(PARAM_PRODUCT_ID, prodId);
        request.setAttribute(PARAM_VISIBILITY, (visibility > 0) ? visibility : ControlPanelConstants.VISIBILITY_PUBLIC);

        int mode = Constants.PRODUCT_MODE_CONFIG;
        int contentType = -1;
        if (prodId > 0) {
            Product product = prodSrvc.getProductById(prodId);

            if (product != null) {
                mode = product.getMode();
                contentType = product.getContentType();
                List<Task> tasks = taskSrvc.getTasksByProductId(prodId);
                List<Target> targets = targetSrvc.getTargetsByProjectId(projId);

                request.setAttribute(ATTR_TARGETS, targets);
                request.setAttribute(ATTR_TASKS, tasks);
                request.setAttribute(ATTR_ROLES, projSrvc.getRolesByProjectId(projId));
                request.setAttribute(ATTR_GOALS, prodSrvc.getGoalsOfWorkflow(product.getWorkflowId()));
                request.setAttribute(ATTR_TOOLS, prodSrvc.getAllToolsByProductId(prodId));
            }
        }
        request.setAttribute(ATTR_PRODUCT_MODE, mode);
        request.setAttribute("contentType", contentType);

        //request.setAttribute(ATTR_ACCESS_RIGHTS, accessMatrixSrvc.getAllAccessMatrixes());
        //request.setAttribute(ATTR_PRODUCTS, prodSrvc.getAllProducts());
        //request.setAttribute(ATTR_JOURNAL_CONFIGS, prodSrvc.getAllJournalConfigs());
        //request.setAttribute(ATTR_SURVEY_CONFIGS, prodSrvc.getAllSurveyConfigs());
        //request.setAttribute(ATTR_WORKFLOWS, workflowDao.selectAllWorkflows());
        /*
        if (projId > 0) {
        request.setAttribute(ATTR_ROLES, projSrvc.getRolesByProjectId(projId));
        }
        if (prodId > 0) {
        request.setAttribute(ATTR_GOALS, prodSrvc.getAllGoalsByProductId(prodId));
        request.setAttribute(ATTR_TOOLS, prodSrvc.getAllToolssByProductId(prodId));

        Product product = prodSrvc.getProductById(prodId);
        request.setAttribute(ATTR_PRODUCT_JSON, JSONUtils.toJson(product));

        }
         */
        return RESULT_INDEX;
    }

    public String find() {
        int pageSize = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGESIZE));
        int page = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGE));
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        String sortName = request.getParameter(PARAM_SORT_NAME);
        String sortOrder = request.getParameter(PARAM_SORT_ORDER);

        logger.debug("Request Params: "
                + "\n\tpageSize=" + pageSize
                + "\n\tpage=" + page
                + "\n\tprojId=" + projId
                + "\n\tsortName=" + sortName
                + "\n\tsortOrder=" + sortOrder);

        LoginUser loginUser = super.getLoginUser();
        boolean isSysAdmin = (loginUser == null) ? false : loginUser.isSiteAdmin();

        
        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            sortOrder = "asc";
        }
        try {
            Pagination<ProductVO> pagination = prodSrvc.getProductsByProjectId(projId, sortName, sortOrder, page, pageSize);

            pagination.addProperty("isSysAdmin", isSysAdmin ? "yes" : "no");

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

        Product prod = prodSrvc.getProductById(prodId);
        
        if (prod == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                    getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_PRODUCT));
            return RESULT_EMPTY;
        }

        try {
            if (projectCpService.deleteProduct(prodId) != 0) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_DB, super.getMessage(ControlPanelMessages.KEY_ERROR_SERVER_INTERNAL));
            } else {
                super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
            }
        } catch (Exception ex) {
            logger.debug("Error occurs!", ex);
        }
        return RESULT_EMPTY;
    }

    public String get() {
        int prodId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID));
        Product prod = prodSrvc.getProductById(prodId);

        if (prod == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                    getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_PRODUCT));
            return RESULT_EMPTY;
        }

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

    public String getActionRights() {
        try {
            List<AccessMatrix> accessMatrixes = accessMatrixSrvc.getAllAccessMatrixes();
            JSONArray jsonArr = new JSONArray();
            if (accessMatrixes != null && !accessMatrixes.isEmpty()) {
                for (AccessMatrix m : accessMatrixes) {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("id", m.getId());
                    jsonObj.put("name", m.getName());
                }
            }
            super.sendResponseResult(ControlPanelErrorCode.OK, jsonArr, "OK");
        } catch (Exception ex) {
            logger.debug("Error occurs!", ex);
        }
        return RESULT_EMPTY;
    }

    public String save() {
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        int prodId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID));
        short contentType = StringUtils.str2short(request.getParameter(PARAM_CONTENT_TYPE));
        short mode = StringUtils.str2short(request.getParameter(PARAM_MODE));
        int workflowId = StringUtils.str2int(request.getParameter(PARAM_WORKFLOW));
        int contentConfigId = StringUtils.str2int(request.getParameter(PARAM_CONTENT_CONFIG));
        String prodName = request.getParameter(PARAM_PRODUCT_NAME);
        String description = request.getParameter(PARAM_DESCRIPTION);
        //int actionRight = StringUtils.str2int(request.getParameter(PARAM_ACTION_RIGHT));
        logger.debug("Request Params: "
                + "\n\tprojId=" + projId
                + "\n\tprodId=" + prodId
                + "\n\tcontentType=" + contentType
                + "\n\tprodName=" + prodName
                + "\n\tdescription=" + description
                + "\n\tworkflow=" + workflowId
                + "\n\tcontentConfig=" + contentConfigId
                + "\n\tmode=" + mode //+ "\n\tactionRight=" + actionRight
                );

        Product prod;
        int oldWorkflowId = -1;
        boolean isNewProd = false;

        if (prodId > 0) {
            // update existing product - check for name conflict
            prod = prodSrvc.getProductById(prodId);

            if (prod == null) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                    getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_PRODUCT));
                return RESULT_EMPTY;
            }

            Product p = prodSrvc.getProductByName(prodName);

            if (p != null && p.getId() != prodId) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_EXISTS,
                        getMessage(ControlPanelMessages.KEY_DUPLICATED_PRODUCT_NAME, prodName));

                return RESULT_EMPTY;
            }

            oldWorkflowId = prod.getWorkflowId();
        } else {
            Product p = prodSrvc.getProductByName(prodName);

            if (p != null) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_EXISTS,
                        getMessage(ControlPanelMessages.KEY_DUPLICATED_PRODUCT_NAME, prodName));

                return RESULT_EMPTY;
            }
            
            prod = new Product();
            isNewProd = true;
        }
        
        //prod.setAccessMatrixId(actionRight);
        prod.setContentType(contentType);
        prod.setDescription(description);

        if (isNewProd) {
            // mode is set automatically for new product
            prod.setMode(Constants.PRODUCT_MODE_CONFIG);
        }

        prod.setName(prodName);
        prod.setProductConfigId(contentConfigId);
        prod.setProjectId(projId);
        prod.setWorkflowId(workflowId);

        if (!isNewProd) {
            prod.setId(prodId);
            prod = prodSrvc.updateProduct(prod);
        } else {
            prod = prodSrvc.addProduct(prod);
        }

        if(isNewProd || oldWorkflowId != workflowId) {
            // create default tasks for the product based on the workflow
            if (!isNewProd) {
                // remove old tasks because they are based on old workflow.
                prodSrvc.deleteTasksByProductId(prodId);
            }

            List<Goal> goals = prodSrvc.getGoalsOfWorkflow(workflowId);
            if(goals != null && !goals.isEmpty()) {
                for(Goal g: goals) {
                    Task task = new Task();
                    task.setTaskName(g.getName());
                    task.setGoalId(g.getId());
                    task.setProductId(prod.getId());
                    task.setToolId(0);
                    task.setType((short)0);
                    task.setAssignmentMethod((short)1);
                    task.setInstructions("");
                    task.setSticky((short)1);   // auto-created tasks are sticky!
                    prodSrvc.createTask(task);
                }
            }
        }
        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return RESULT_EMPTY;
    }

    public String getOptions() {
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        int prodId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID));
        logger.debug("Request Params: "
                + "\n\tprojId=" + projId
                + "\n\tprodId=" + prodId);

        JSONObject root = new JSONObject();
        root.put(PARAM_PROJECT_ID, projId);
        root.put(PARAM_PRODUCT_ID, prodId);
        if (projId > 0) {
            root.put(ATTR_JOURNAL_CONFIGS, JSONUtils.listToJson(prodSrvc.getAllJournalConfigs()));
            root.put(ATTR_SURVEY_CONFIGS, JSONUtils.listToJson(prodSrvc.getSurveyConfigsForProject(projId)));
            root.put(ATTR_WORKFLOWS, JSONUtils.listToJson(workflowDao.selectAllWorkflows()));
            root.put(ATTR_ROLES, JSONUtils.listToJson(projSrvc.getRolesByProjectId(projId)));
        }

        if (prodId > 0) {
            Product product = prodSrvc.getProductById(prodId);
            if (product != null) {
                root.put(ATTR_GOALS, JSONUtils.listToJson(prodSrvc.getGoalsOfWorkflow(product.getWorkflowId())));
                // root.put(ATTR_TOOLS, JSONUtils.listToJson(prodSrvc.getAllToolsByProductId(prodId)));
                root.put(ATTR_PRODUCT, JSONUtils.toJson(product));
            }
        }
        
        super.sendResponseResult(ControlPanelErrorCode.OK, root, "OK");
        return RESULT_EMPTY;
    }

    public String exists() {
        LoginUser user = getLoginUser();
        int prodId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID));
        String fieldId = request.getParameter(PARAM_FIELD_ID);
        String fieldValue = request.getParameter(PARAM_FIELD_VALUE);
        logger.debug("Request Params: \n\tprodId=" + prodId + "\n\tfieldId=" + fieldId + "\n\tfieldValue=" + fieldValue);
        JSONArray jsonArr = new JSONArray();
        jsonArr.add(fieldId);

        String prodName = fieldValue;
        Product p = prodSrvc.getProductByName(prodName);

        if (p != null && p.getId() != prodId) {
            jsonArr.add(false);
            jsonArr.add(user.message(ControlPanelMessages.KEY_DUPLICATED_PRODUCT_NAME, fieldValue));
        } else {
            jsonArr.add(true);
        }
        super.sendResponseJson(jsonArr);
        return RESULT_EMPTY;
    }

    public String clone() {
        int prodId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID));
        String clonedProdName = request.getParameter(PARAM_PRODUCT_NAME);
        logger.debug("Request Params: "
                + "\n\tclonedProdName=" + clonedProdName
                + "\n\tprodId=" + prodId);

        Product p = prodSrvc.getProductByName(clonedProdName);

        if (p != null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_EXISTS, getMessage(ControlPanelMessages.KEY_DUPLICATED_PRODUCT_NAME, clonedProdName));
            return RESULT_EMPTY;
        }
        
        Product origProd = prodSrvc.getProductById(prodId);
        if (origProd == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS, 
                    getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_PRODUCT));
            return RESULT_EMPTY;
        }

        int rt = productCpService.cloneProduct(origProd.getId(), clonedProdName);

        if (rt != 0) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_DB,
                    getMessage(ControlPanelMessages.KEY_ERROR_SERVER_INTERNAL));
            return RESULT_EMPTY;
        }

        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return RESULT_EMPTY;
    }

    public String applyMode() {
        String mode = request.getParameter(PARAM_MODE);
        int prodId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID));
        logger.debug("Request Params: "
                + "\n\tmode=" + mode
                + "\n\tprodId=" + prodId);
        Product product = prodSrvc.getProductById(prodId);
        if (product == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS, getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_PRODUCT));
            return RESULT_EMPTY;
        }

        Project project = projSrvc.getProjectById(product.getProjectId());
        if (product == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS, getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_PROJECT));
            return RESULT_EMPTY;
        }

        int toMode;

        if ("test".equalsIgnoreCase(mode)) {
            toMode = Constants.PRODUCT_MODE_TEST;
        } else if ("config".equalsIgnoreCase(mode)) {
            toMode = Constants.PRODUCT_MODE_CONFIG;
        } else if ("launch".equalsIgnoreCase(mode)) {
            toMode = Constants.PRODUCT_MODE_PROD;
        } else {
            super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM, getMessage(ControlPanelMessages.PROGRAM_ERROR));
            return RESULT_EMPTY;
        }

        // check whether the operation makes sense
        switch(product.getMode()) {
            case Constants.PRODUCT_MODE_CONFIG:
                // current mode is CONFIG
                if (toMode == Constants.PRODUCT_MODE_CONFIG) {
                    super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM,
                            getMessage(ControlPanelMessages.KEY_ERROR_PROD_ALREADY_IN_CONFIG));
                    return RESULT_EMPTY;
                }

                if (toMode == Constants.PRODUCT_MODE_PROD) {
                    // from CONFIG to PROD -- not allowed
                    super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM,
                            getMessage(ControlPanelMessages.KEY_ERROR_NO_CONFIG_TO_PROD));
                    return RESULT_EMPTY;
                }

                break;

            case Constants.PRODUCT_MODE_TEST:
                // current mode is TEST
                if (toMode == Constants.PRODUCT_MODE_TEST) {
                    super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM,
                            getMessage(ControlPanelMessages.KEY_ERROR_PROD_ALREADY_IN_TEST));
                    return RESULT_EMPTY;
                }
                break;

             default:
                // current mode is PROD
                if (toMode == Constants.PRODUCT_MODE_PROD) {
                    super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM,
                            getMessage(ControlPanelMessages.KEY_ERROR_PROD_ALREADY_IN_PROD));
                    return RESULT_EMPTY;
                }
                break;
        }

        // check whether the user can do it
        LoginUser user = getLoginUser();

        /*** This rule is disabled now
        if (!user.isSiteAdmin()) {
            if (toMode == Constants.PRODUCT_MODE_PROD || product.getMode() == Constants.PRODUCT_MODE_PROD) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM,
                            getMessage(ControlPanelMessages.KEY_ERROR_SA_OPERATION_ONLY));
                    return RESULT_EMPTY;
            }
        }
         * ***/

        // now try to change the mode
        switch (toMode) {
            case Constants.PRODUCT_MODE_CONFIG:
                // change to CONFIG mode
                logger.debug("Reset the product to CONFIG");
                productCpService.resetProduct(prodId);
                break;

            case Constants.PRODUCT_MODE_TEST:
                // change to TEST mode
                if (product.getMode() == Constants.PRODUCT_MODE_CONFIG) {
                    // from CONFIG mode
                    // check for readiness
                    int code = productCpService.checkReadyForTest(product);
                    
                    if (code != 0) {//product is not ready
                        String content = getMessage(ControlPanelMessages.KEY_ERROR_CANNOT_START_PROD, product.getName());
                        switch (code) {
                            case ControlPanelConstants.PRODUCT_NOT_READY_REASON_TARGET:
                                content += getMessage(ControlPanelMessages.KEY_ERROR_NO_TARGET_IN_PROD);
                                break;
                            case ControlPanelConstants.PRODUCT_NOT_READY_REASON_CONTRIBUTOR:
                                content += getMessage(ControlPanelMessages.KEY_ERROR_NO_CONTRIBUTOR_IN_PROD);
                                break;
                            case ControlPanelConstants.PRODUCT_NOT_READY_REASON_TASK_FOR_EACH_GOAL:
                                content += getMessage(ControlPanelMessages.KEY_ERROR_GOAL_MUST_HAVE_TASK);
                                break;
                            case ControlPanelConstants.PRODUCT_NOT_READY_REASON_TASK_BELONG_TO_GOAL:
                                content += getMessage(ControlPanelMessages.KEY_ERROR_NO_GOAL_FOR_TASK);
                                break;
                            case ControlPanelConstants.PRODUCT_NOT_READY_REASON_TASK_VALID_ID_TYPE:
                                content += getMessage(ControlPanelMessages.KEY_ERROR_INVALID_TOOLID_AND_TYPE);
                                break;
                            default:
                                break;
                        }

                        super.sendResponseResult(ControlPanelErrorCode.ERR_NO_PERMISSION_BANS, content);
                        return RESULT_EMPTY;
                    }

                    logger.debug("Starting product " + prodId + " to TEST mode.");
                    productCpService.startProduct(prodId);
                } else if (product.getMode() == Constants.PRODUCT_MODE_PROD) {
                    // from PROD mode - simply change the mode
                    logger.debug("Set product " + prodId + " to TEST.");
                    prodSrvc.updateProductMode(prodId,  Constants.PRODUCT_MODE_TEST);
                }
                break;

            default:
                // change to PROD mode - must be from TEST mode
                // for public project, move used public-test indicators to public-extended lib
                logger.debug("Set product " + prodId + " to production.");
                if (project.getVisibility() == Constants.VISIBILITY_PUBLIC && product.getContentType() == Constants.CONTENT_TYPE_SURVEY) {
                    logger.debug("Promote indicators.");
                    productCpService.promoteIndicatorsUsedByProduct(product);
                }
                prodSrvc.updateProductMode(prodId,  Constants.PRODUCT_MODE_PROD);
        }

        JSONObject root = new JSONObject();
        super.sendResponseResult(ControlPanelErrorCode.OK, root, "OK");
        return RESULT_EMPTY;
    }

    public String execute() {
        return index();
    }
}
