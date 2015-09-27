/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller.proj;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.controlpanel.common.ControlPanelErrorCode;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.controller.BaseController;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.controlpanel.model.TargetVO;
import com.ocs.indaba.controlpanel.service.ProjectControlPanelService;
import com.ocs.indaba.po.Product;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.ProjectTarget;
import com.ocs.indaba.po.Target;
import com.ocs.indaba.service.LanguageService;
import com.ocs.indaba.service.OrganizationService;
import com.ocs.indaba.service.ProductService;
import com.ocs.indaba.service.ProjectService;
import com.ocs.indaba.service.TargetService;
import com.ocs.indaba.vo.ProjectTargetVO;
import com.ocs.util.JSONUtils;
import com.ocs.util.Pagination;
import com.ocs.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Administrator
 */
@Results({
    @Result(name = "success", type = "redirectAction", params = {"actionName", "libraries"})
})
public class ProjectTargetController extends BaseController {

    private static final long serialVersionUID = 7550588769978806967L;
    private static final Logger logger = Logger.getLogger(ProjectTargetController.class);
    private static final String PARAM_USER_TAG = "userTag";
    private static final String PARAM_TARGET_IDS = "targetIds[]";
    private static final String PARAM_TARGET_ID = "targetId";
    private static final String TARGET_SHORT_NAME = "tsname";
    private static final String ACTION_ADD = "add";
    private static final String ACTION_DELETE = "delete";
    @Autowired
    private TargetService targetSrvc;
    @Autowired
    private OrganizationService orgSrvc;
    @Autowired
    private LanguageService langSrvc;
    @Autowired
    private ProjectService projSrvc;
    @Autowired
    private ProductService prodSrvc;
    @Autowired
    private ProjectControlPanelService projectCpService;

    public String index() {
        return RESULT_INDEX;
    }

    public String create() {
        return RESULT_EMPTY;
    }

    public String find() {
        //int visibility = StringUtils.str2int(request.getParameter(PARAM_VISIBILITY));
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        int pageSize = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGESIZE));
        int page = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGE));
        String filterUserTag = request.getParameter(PARAM_USER_TAG);
        String sortName = request.getParameter(PARAM_SORT_NAME);
        String sortOrder = request.getParameter(PARAM_SORT_ORDER);
        //String queryType = request.getParameter(PARAM_QUERY_TYPE);
        //String query = request.getParameter(PARAM_QUERY);
        logger.debug("Request Params: \n\tpageSize=" + pageSize
                + "\n\tpage=" + page
                + "\n\tprojId=" + projId
                + "\n\tfilterUserTag=" + filterUserTag
                + "\n\tsortName=" + sortName
                + "\n\tsortOrder=" + sortOrder);
        //LoginUser loginUser = super.getLoginUser();
        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            sortOrder = "asc";
        }
        Pagination<ProjectTargetVO> pagination = targetSrvc.getAllTargetsByProjectId(projId, sortName, sortOrder, page, pageSize);
        String json = pagination.toJsonString();
        logger.debug("JSON: " + json);
        sendResponseMessage(json);
        return RESULT_EMPTY;
    }
    // GET a specified indicator

    public String get() {
        int targetId = StringUtils.str2int(request.getParameter(PARAM_ID));
        LoginUser loginUser = super.getLoginUser();
        logger.debug("Request Params: \n\ttargetId=" + targetId);
        Target target = targetSrvc.getTargetById(targetId);

        if (target == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                    getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_TARGET));
            return RESULT_EMPTY;
        }

        List<Integer> ttagsIds = targetSrvc.getTtagsIdsByTargetId(targetId);
        TargetVO targetVo = TargetVO.initWithTarget(target);
        targetVo.addTagIds(ttagsIds);
        targetVo.setOrgname(loginUser.getOrg(targetVo.getOwnerOrgId()).getName());
        targetVo.setEditable(loginUser.getChecker().hasOrgAuthority(targetVo.getOwnerOrgId()));
        super.sendResponseResult(ControlPanelErrorCode.OK, targetVo.toJson(), "OK");
        return RESULT_EMPTY;
    }

    public String getAvailiabeTargets() {
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        logger.debug("Request Params: \n\tprojId=" + projId);

        Project project = projSrvc.getProjectById(projId);

        if (project == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                    getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_PROJECT));
            return RESULT_EMPTY;
        }

        List<Target> targets = targetSrvc.getAllAvailableTargetsByProjectId(projId);
        JSONArray arr = new JSONArray();
        for (Target t : targets) {
            arr.add(JSONUtils.toJson(t));
        }
        super.sendResponseResult(ControlPanelErrorCode.OK, arr, "OK");
        return RESULT_EMPTY;
    }


    private static Target findTarget(List<Target> targets, int targetId) {
        if (targets == null || targets.size() == 0) return null;
        for (Target t : targets) {
            if (t.getId() == targetId) return t;
        }
        return null;
    }

    public String addTarget() {
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        String[] targetIds = request.getParameterValues(PARAM_TARGET_IDS);
        logger.debug("Request Params: \n\tprojId=" + projId + "\n\ttargets=" + targetIds);

        Project proj = projSrvc.getProjectById(projId);
        if (proj == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                    getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_PROJECT));
            return RESULT_EMPTY;
        }
       
        if (targetIds == null || targetIds.length == 0) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM,
                    getMessage(ControlPanelMessages.KEY_ERROR_MISSING_TARGETS));
            return RESULT_EMPTY;
        }

        JSONArray errors = new JSONArray();
        JSONArray oks = new JSONArray();
        List<Target> addedList = new ArrayList<Target>();

        // get existing targets of the project
        List<Target> curTargets = targetSrvc.getTargetsByProjectId(projId);
        List<Target> availTargets = targetSrvc.getAllAvailableTargetsByProjectId(projId);

        for (String t : targetIds) {
            int targetId = StringUtils.str2int(t);
            if (targetId <= 0) {
                continue;
            }

            // see if the target already in the project
            if (findTarget(curTargets, targetId) != null) continue;

            // make sure the target is legit for the project
            Target target = findTarget(availTargets, targetId);
            if (target == null) continue;

            addedList.add(target);
        }

        if (addedList.size() == 0) {
            // nothing to add - just return
            super.sendResponseResult(ControlPanelErrorCode.OK, (JSONObject)null, null);
            return RESULT_EMPTY;
        }

        // now add the targets
        for (Target target : addedList) {
            ProjectTarget projectTarget = new ProjectTarget();
            projectTarget.setProjectId(projId);
            projectTarget.setTargetId(target.getId());
            projSrvc.addProjectTarget(projectTarget);
            
            JSONObject item = new JSONObject();
            item.put("id", target.getId());
            item.put("name", target.getName());
            oks.add(item);
        }

        // get all products of the project in preparation of init_horse
        List<Product> products = prodSrvc.getProductsByProjectId(projId);

        if (products != null && products.size() > 0) {
            for (Product prod : products) {
                if (prod.getMode() == Constants.PRODUCT_MODE_CONFIG) continue;

                for (Target target : addedList) {
                    int rt = projectCpService.initializeHorse(prod.getId(), target.getId());
                    if (rt != 0) {
                        errors.add("DB error occurs when adding target " + target.getName());
                        continue;
                    }
                }
            }
        }

        JSONObject result = new JSONObject();
        result.put("added", oks);
        result.put("errCount", errors.size());
        result.put("errors", errors);
        super.sendResponseResult((errors.size() == 0 ? ControlPanelErrorCode.OK : ControlPanelErrorCode.ERR_UNKNOWN), result, null);
        return RESULT_EMPTY;
    }

    public String deleteTarget() {
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        int targetId = StringUtils.str2int(request.getParameter(PARAM_TARGET_ID));
        logger.debug("Request Params: \n\tprojId=" + projId + "\n\ttargetId=" + targetId);

        ProjectTarget projectTarget = projSrvc.getProjectTarget(projId, targetId);
        if (projectTarget == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                    getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_TARGET));
            return RESULT_EMPTY;
        }
        try {
            int rt = projectCpService.deleteTarget(projId, targetId);
            if (rt != 0) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_DB, getMessage(ControlPanelMessages.KEY_ERROR_FAIL_TO_INVOKE_STORED_PROCEDURE));
            } else {
                super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
            }
        } catch (Exception ex) {
            logger.error("Error occurs when invoking 'del_target' stored procedure!", ex);
            super.sendResponseResult(ControlPanelErrorCode.ERR_DB, getMessage(ControlPanelMessages.KEY_ERROR_FAIL_TO_INVOKE_STORED_PROCEDURE));
        }
        return RESULT_EMPTY;
    }

    public String exists() {
        LoginUser user = getLoginUser();
        int targetId = StringUtils.str2int(request.getParameter(PARAM_TARGET_ID));
        String fieldId = request.getParameter(PARAM_FIELD_ID);
        String fieldName = request.getParameter(PARAM_FIELD_NAME);
        String fieldValue = request.getParameter(PARAM_FIELD_VALUE);
        JSONArray jsonArr = new JSONArray();
        jsonArr.add(fieldId);
        if (TARGET_SHORT_NAME.equals(fieldName)) {
            if (targetSrvc.existsByShortName(targetId, fieldValue)) {
                jsonArr.add(false);
                jsonArr.add(user.message(ControlPanelMessages.KEY_DUPLICATED_TARGET_SHORT_NAME, fieldValue));
            } else {
                jsonArr.add(true);
            }
        } else {
            if (targetSrvc.existsByName(targetId, fieldValue)) {
                jsonArr.add(false);
                jsonArr.add(user.message(ControlPanelMessages.KEY_DUPLICATED_TARGET_NAME, fieldValue));
            } else {
                jsonArr.add(true);
            }
        }
        super.sendResponseJson(jsonArr);
        return RESULT_EMPTY;
    }

    public static void main(String args[]) {
        for (int i = 0; i
                < 33;
                ++i) {
            System.out.println("(" + (i + 1) + "," + 1 + "," + i + ")");
        }
    }
}
