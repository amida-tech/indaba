/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller.proj;

import com.ocs.indaba.controlpanel.common.ControlPanelErrorCode;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.controller.BaseController;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.po.Product;
import com.ocs.indaba.po.Role;
import com.ocs.indaba.po.Userfinder;
import com.ocs.indaba.service.ProductService;
import com.ocs.indaba.service.ProjectService;
import com.ocs.indaba.service.RoleService;
import com.ocs.indaba.service.UserFinderService;
import com.ocs.indaba.vo.ProjectMemberShipVO;
import com.ocs.indaba.vo.UserfinderVO;
import com.ocs.util.JSONUtils;
import com.ocs.util.Pagination;
import com.ocs.util.StringUtils;
import java.util.Date;
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
public class UserfinderController extends BaseController {

    private static final long serialVersionUID = 7550588769978806967L;
    private static final Logger logger = Logger.getLogger(UserfinderController.class);
    private static final String PARAM_USERFINDER_ID = "userfinderId";
    private static final String PARAM_ROLE_ID = "roleId";
    private static final String PARAM_ASSIGNED_USER_ID = "assignedUserId";
    private static final String PARAM_CASE_SUBJECT = "caseSubject";
    private static final String PARAM_CASE_BODY = "caseBody";
    private static final String PARAM_CASE_PRIORITY = "casePriority";
    private static final String PARAM_ATTACH_USER = "attachUser";
    private static final String PARAM_ATTACH_CONTENT = "attachContent";
    private static final String PARAM_STATUS = "status";
    private static final String PARAM_DESCRIPTION = "description";
    @Autowired
    private UserFinderService userfinderSrvc;
    @Autowired
    private ProjectService projSrvc;
    @Autowired
    private ProductService prodSrvc;
    @Autowired
    private RoleService roleSrvc;

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
        //String filterUserTag = request.getParameter(PARAM_USER_TAG);
        String sortName = request.getParameter(PARAM_SORT_NAME);
        String sortOrder = request.getParameter(PARAM_SORT_ORDER);
        //String queryType = request.getParameter(PARAM_QUERY_TYPE);
        //String query = request.getParameter(PARAM_QUERY);
        logger.debug("Request Params: \n\tpageSize=" + pageSize
                + "\n\tpage=" + page
                + "\n\tprojId=" + projId
                //+ "\n\tfilterUserTag=" + filterUserTag
                + "\n\tsortName=" + sortName
                + "\n\tsortOrder=" + sortOrder);
        //LoginUser loginUser = super.getLoginUser();
        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            sortOrder = "asc";
        }
        Pagination<UserfinderVO> pagination = userfinderSrvc.getAllUserfindersByProjectId(projId, sortName, sortOrder, page, pageSize);
        String json = pagination.toJsonString();
        logger.debug("JSON: " + json);
        sendResponseMessage(json);
        return RESULT_EMPTY;
    }
    // GET a specified indicator

    public String get() {
        int userfinderId = StringUtils.str2int(request.getParameter(PARAM_USERFINDER_ID));
        //LoginUser loginUser = super.getLoginUser();
        logger.debug("Request Params: \n\tuserfinderId=" + userfinderId);
        Userfinder userfinder = userfinderSrvc.getUserfinder(userfinderId);
        if (userfinder == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                    getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_USERFINDER));
        } else {
            super.sendResponseResult(ControlPanelErrorCode.OK, JSONUtils.toJson(userfinder), "OK");
        }
        return RESULT_EMPTY;
    }

    public String save() {
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        int userfinderId = StringUtils.str2int(request.getParameter(PARAM_USERFINDER_ID));
        int roleId = StringUtils.str2int(request.getParameter(PARAM_ROLE_ID));
        int productId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID), 0);
        int assignedUserId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNED_USER_ID));
        String caseSubject = request.getParameter(PARAM_CASE_SUBJECT);
        String caseBody = request.getParameter(PARAM_CASE_BODY);
        short casePriority = StringUtils.str2short(request.getParameter(PARAM_CASE_PRIORITY));
        boolean attachUser = StringUtils.str2boolean(request.getParameter(PARAM_ATTACH_USER));
        boolean attachContent = StringUtils.str2boolean(request.getParameter(PARAM_ATTACH_CONTENT));
        short status = StringUtils.str2short(request.getParameter(PARAM_STATUS));
        String description = request.getParameter(PARAM_DESCRIPTION);
        logger.debug("Request Params: \n\tprojId=" + projId
                + "\n\tproductId=" + productId
                + "\n\tuserfinderId=" + userfinderId
                + "\n\troleId=" + roleId
                + "\n\tassignedUserId=" + assignedUserId
                + "\n\tcaseSubject=" + caseSubject
                + "\n\tcaseBody=" + caseBody
                + "\n\tcasePriority=" + casePriority
                + "\n\tattachUser=" + attachUser
                + "\n\tattachContent=" + attachContent
                + "\n\tstatus=" + status
                + "\n\tdescription=" + description);

        if (userfinderId > 0) {
            Userfinder userfinder = userfinderSrvc.getUserfinder(userfinderId);
            if (userfinder == null) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                        getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_USERFINDER));
                return RESULT_EMPTY;
            }

            if (userfinderSrvc.existsUserfinderByProjectIdAndRoleId(projId, productId, roleId, userfinderId)) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_EXISTS, getMessage(ControlPanelMessages.KEY_DUPLICATED_USERFINDER));
                return RESULT_EMPTY;
            }

            userfinder.setProjectId(projId);
            userfinder.setRoleId(roleId);
            userfinder.setProductId(productId);
            userfinder.setAssignedUserId(assignedUserId);
            userfinder.setCaseSubject(caseSubject);
            userfinder.setCaseBody(caseBody);
            userfinder.setCasePriority(casePriority);
            userfinder.setAttachUser(attachUser);
            userfinder.setAttachContent(attachContent);
            userfinder.setStatus(status);
            userfinder.setLastUpdateTime(new Date());
            userfinder.setDescription(description);
            userfinderSrvc.updateUserfinder(userfinder);

        } else {
            if (userfinderSrvc.existsUserfinderByProjectIdAndRoleId(projId, productId, roleId, 0)) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_EXISTS, getMessage(ControlPanelMessages.KEY_DUPLICATED_USERFINDER));
                return RESULT_EMPTY;
            }
            Userfinder userfinder = new Userfinder();
            userfinder.setProjectId(projId);
            userfinder.setProductId(productId);
            userfinder.setRoleId(roleId);
            userfinder.setAssignedUserId(assignedUserId);
            userfinder.setCaseSubject(caseSubject);
            userfinder.setCaseBody(caseBody);
            userfinder.setCasePriority(casePriority);
            userfinder.setAttachUser(attachUser);
            userfinder.setAttachContent(attachContent);
            userfinder.setStatus(status);
            userfinder.setCreateTime(new Date());
            userfinder.setLastUpdateTime(new Date());
            userfinder.setDescription(description);
            userfinderSrvc.addUserFinder(userfinder);
        }
        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return RESULT_EMPTY;
    }

    public String getOptions() {
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        logger.debug("Request Params: \n\tprojId=" + projId);
        List<ProjectMemberShipVO> pmList = projSrvc.getProjectMembershipsByProjectId(projId);
        List<Role> roles = roleSrvc.getAllRoles(projId);
        List<Product> products = prodSrvc.getProductsByProjectId(projId);

        // add a fake product for "project-wide" at the head of the list
        Product fake = new Product();
        fake.setName("<em>Project-wide</em>");
        fake.setId(0);
        products.add(0, fake);

        JSONObject root = new JSONObject();
        root.put("pmList", JSONUtils.listToJson(pmList));
        root.put("roles", JSONUtils.listToJson(roles));
        root.put("products", JSONUtils.listToJson(products));
        super.sendResponseJson(root);
        return RESULT_EMPTY;
    }

    public String delete() {
        int userfinderId = StringUtils.str2int(request.getParameter(PARAM_USERFINDER_ID));
        logger.debug("Request Params: \n\tuserfinderId=" + userfinderId);

        Userfinder userfinder = userfinderSrvc.getUserfinder(userfinderId);
        if (userfinder == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                    getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_USERFINDER));
        } else {
            userfinderSrvc.deleteUserFinder(userfinderId);
            super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        }
        return RESULT_EMPTY;
    }

    public String exists() {
        LoginUser user = getLoginUser();
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        int prodId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID));
        String fieldId = request.getParameter(PARAM_FIELD_ID);
        //String fieldName = request.getParameter(PARAM_FIELD_NAME);
        String fieldValue = request.getParameter(PARAM_FIELD_VALUE);
        logger.debug("Request Params: \n\tprojId=" + projId + "\n\tfieldId=" + fieldId + "\n\tfieldValue=" + fieldValue);
        JSONArray jsonArr = new JSONArray();
        jsonArr.add(fieldId);
        if (userfinderSrvc.existsUserfinderByProjectIdAndRoleId(projId, prodId, StringUtils.str2int(fieldValue), 0)) {
            jsonArr.add(false);
            jsonArr.add(user.message(ControlPanelMessages.KEY_DUPLICATED_USERFINDER, fieldValue));
        } else {
            jsonArr.add(true);
        }
        super.sendResponseJson(jsonArr);
        return RESULT_EMPTY;
    }
}
