/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller.proj;

import com.ocs.common.Config;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.controlpanel.common.ControlPanelErrorCode;
import com.ocs.indaba.controlpanel.controller.BaseController;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.ProjectMembership;
import com.ocs.indaba.po.Role;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.MailbatchService;
import com.ocs.indaba.service.NotificationItemService;
import com.ocs.indaba.service.ProjectService;
import com.ocs.indaba.service.RoleService; 
import com.ocs.indaba.service.TaskService;
import com.ocs.indaba.vo.NotificationView;
import com.ocs.indaba.vo.ProjectMemberShipVO;
import com.ocs.util.Pagination;
import com.ocs.util.StringUtils;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ResultPath;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import com.ocs.util.SendMail;
import java.io.File;
import com.ocs.indaba.controlpanel.common.ControlPanelConstants;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.userimporter.CsvContributor;
import com.ocs.indaba.controlpanel.userimporter.CsvContributorParser;
import com.ocs.indaba.controlpanel.userimporter.CsvParseResult;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;

/**
 * Project management
 *
 * @author Jeff Jiang
 *
 */
@ResultPath("/WEB-INF/pages")
public class ProjectMembershipController extends BaseController {

    private static final Logger logger = Logger.getLogger(ProjectMembershipController.class);
    private static final long serialVersionUID = -2487852558172383390L;
    private static final String PARAM_QTYPE = "qtype";
    //
    // Attribute keys
    @Autowired
    private ProjectService projSrvc;
    
    @Autowired
    private RoleService roleSrvc;
    
    @Autowired
    private MailbatchService mailService;
    @Autowired
    private NotificationItemService notificationItemService;
    @Autowired
    private TaskService taskSrvc;

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
        String queryType = request.getParameter(PARAM_QTYPE);
        String query = request.getParameter(PARAM_QUERY);
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
                + "\n\tfilterRoleId=" + filterRoleId
                + "\n\tqtype=" + queryType
                + "\n\tquery=" + query
                + "\n\tsortName=" + sortName
                + "\n\tsortOrder=" + sortOrder);
        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            sortOrder = "asc";
        }
        try {
            Map<String, Object> filters = new HashMap<String, Object>();
            filters.put("projectid", new Integer(projId));
            filters.put("roleid", filterRoleId);
            if(queryType != null && query != null && !queryType.isEmpty() && !query.isEmpty()) {
                filters.put(queryType, query);
            }
            Pagination<ProjectMemberShipVO> pagination = projSrvc.getProjectMembershipsByFilter(filters, sortName, sortOrder, page, pageSize);            
            logger.debug(pagination);
            sendResponseMessage(pagination.toJsonString());
        } catch (Exception ex) {
            logger.error("Error occurs!", ex);
        }

        return RESULT_EMPTY;
    }

    public String getAllUsersAndRoles() {
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        int userId = StringUtils.str2int(request.getParameter(PARAM_USER_ID));
        logger.debug("Request Params: projId=" + projId + ", userId=" + userId);
        try {
            List<Integer> userIds = projSrvc.getUserIdsByProjectId(projId);
            if (userId > 0 && userIds != null) {
                int index = userIds.indexOf(userId);
                if (index >= 0) {
                    userIds.remove(index);
                }
            }
            List<User> users = userSrvc.getAllActiveUsersByStatus(userIds);
            List<Role> roles = roleSrvc.getAllRoles();
            JSONObject root = new JSONObject();
            JSONArray userArr = new JSONArray();
            root.put("users", userArr);
            if (users != null && !users.isEmpty()) {
                for (User user : users) {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("id", user.getId());
                    jsonObj.put("firstName", user.getFirstName());
                    jsonObj.put("lastName", user.getLastName());
                    userArr.add(jsonObj);
                }
            }
            JSONArray roleArr = new JSONArray();
            root.put("roles", roleArr);
            if (roles != null && !roles.isEmpty()) {
                for (Role r : roles) {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("id", r.getId());
                    jsonObj.put("name", r.getName());
                    roleArr.add(jsonObj);
                }
            }
            super.sendResponseResult(ControlPanelErrorCode.OK, root, "OK");
        } catch (Exception ex) {
            logger.debug("Error occurs!", ex);
        }
        return RESULT_EMPTY;
    }

    public String hasTask() {
        JSONObject retObj = new JSONObject();
        LoginUser loginUser = getLoginUser();
        ProjectMembership pm = projSrvc.getProjectMembership(pmId);

        if (pm == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                    getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_MEMBERSHIP));
            return RESULT_EMPTY;
        }

        if (taskSrvc.hasTask(pm.getUserId(), pm.getProjectId())) {
            retObj.put(KEY_RET, ControlPanelErrorCode.ERR_STILL_HAS_TASK);
            retObj.put(KEY_DESC, loginUser.message(ControlPanelMessages.KEY_ERROR_CONTRIBUTOR_HAS_TASK));
        }
        else {
            retObj.put(KEY_RET, ControlPanelErrorCode.OK);
        }
        super.sendResponseJson(retObj);
        return RESULT_EMPTY;
    }

    public String delete() {
        logger.debug("Delete Project Contributor Request Params: pmId=" + pmId + ",sendNotify=" + sendNotify);
        JSONObject retObj = new JSONObject();
        LoginUser loginUser = getLoginUser();
        ProjectMembership pm = projSrvc.getProjectMembership(pmId);

        if (pm == null) {
            // the user is not in the project
            retObj.put(KEY_RET, ControlPanelErrorCode.OK);
            super.sendResponseJson(retObj);
            return RESULT_EMPTY;
        }

        if (taskSrvc.hasTask(pm.getUserId(), pm.getProjectId())) {
            retObj.put(KEY_RET, ControlPanelErrorCode.ERR_STILL_HAS_TASK);
            retObj.put(KEY_DESC, loginUser.message(ControlPanelMessages.KEY_ERROR_CONTRIBUTOR_HAS_TASK));
        } else {
            projSrvc.deleteProjectMembership(pmId);
            if(sendNotify == true) {
                User user = userSrvc.getUser(pm.getUserId());
                Project proj = projSrvc.getProjectById(pm.getProjectId());
                sendNotification(user, proj, pm.getRoleId(), note);
            }
            retObj.put(KEY_RET, ControlPanelErrorCode.OK);
        }
        super.sendResponseJson(retObj);
        logger.debug("Delete Project Contributor Response: " + retObj.toJSONString());
        return RESULT_EMPTY;
    }

    /***
    public void addNewUser() {
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        String email = request.getParameter(PARAM_EMAIL);
        String firstName = request.getParameter(PARAM_FIRST_NAME);
        String lastName = request.getParameter(PARAM_LAST_NAME);
        int roleId = StringUtils.str2int(request.getParameter(PARAM_ROLE));
        
        logger.debug("Request Params: "
                + "\n\tprojId=" + projId
                + "\n\temail=" + email
                + "\n\tfirstName=" + firstName
                + "\n\tlastName=" + lastName
                + "\n\troleId=" + roleId);

        Project proj = projSrvc.getProjectById(projId);

        if (proj == null) {

        }

        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(email);
        user.setPassword("123456");
        user.setOrganizationId(proj.getOrganizationId());
        user.setDefaultProjectId(projId);
        user.setCreateTime(new Date());
        user = userSrvc.createUser(user);
        saveUserToProjectMembership(0, user.getId(), projId, roleId);
    }

    public void saveUserToProjectMembership(int pmId, int userId, int projId, int roleId) {
        ProjectMembership projMembership = new ProjectMembership();
        projMembership.setId(pmId);
        projMembership.setUserId(userId);
        projMembership.setProjectId(projId);
        projMembership.setRoleId(roleId);
        if (pmId > 0) {
            projSrvc.updateProjectMembership(projMembership);
        } else {
            if (projSrvc.existsProjectMembership(projId, userId)) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_EXISTS, "The project user has already existed!");
                return;
            }
            projSrvc.addProjectMembership(projMembership);
        }
        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
    }
     * ***/

    public String execute() {
        return index();
    }

    // handle to add a project contributor interactively
    public String getProjectRoles() {
        logger.debug("ProjectMembershipController::getProjectRoles -- project ID=" + projId);
        JSONObject obj = new JSONObject();
        List<Role> roles = roleSrvc.getAllRoles(projId);
        JSONArray arrayObj = new JSONArray();

        if (roles != null && !roles.isEmpty()) {
            for (Role role : roles) {
                JSONObject element = new JSONObject();
                element.put(KEY_ID, role.getId().toString());
                element.put(KEY_NAME, role.getName());
                arrayObj.add(element);
            }
        }
        obj.put(KEY_ROLES, arrayObj);
        super.sendResponseJson(obj);
        logger.debug("ProjectMembershipController::getProjectRoles -- returned: " + obj.toJSONString());
        return RESULT_EMPTY;
    }

    public String userExists() {
        JSONObject obj = new JSONObject();
        LoginUser loginUser = getLoginUser();
        // check incoming request data first
        // expects legal project id and legal email
        if (projId == null || email == null ||
                projSrvc.getProjectById(projId) == null ||
                !SendMail.ValidateAddress(email)) {
            obj.put(KEY_RET, ControlPanelErrorCode.ERR_INVALID_PARAM);
            obj.put(KEY_DESC, loginUser.message(Messages.KEY_COMMON_ERR_INVALID_PARAMETER));
        } else {
            User user = userSrvc.getUserByEmail(email);
            if (user == null) {
                obj.put(KEY_RET, ControlPanelErrorCode.OK);
                obj.put(KEY_USER_NAME, userSrvc.suggestUserNameByEmail(email));
                obj.put(KEY_NEW_USER, true);
            } else {
                ProjectMembership pm = projSrvc.getProjectMembership(projId, user.getId());

                if (pm != null) {
                    obj.put(KEY_RET, ControlPanelErrorCode.ERR_ALREADY_EXISTS);
                    obj.put(KEY_DESC, loginUser.message(Messages.KEY_COMMON_ERR_PROJECT_CONTRIBUTOR_EXISTS));
                }
                else {
                    obj.put(KEY_RET, ControlPanelErrorCode.OK);
                    obj.put(KEY_USER_NAME, user.getUsername());
                    obj.put(KEY_NEW_USER, false);
                    obj.put(KEY_FIRST_NAME, user.getFirstName());
                    obj.put(KEY_LAST_NAME, user.getLastName());
                    obj.put(KEY_ORG_ID, user.getOrganizationId());
                }
            }
        }
        super.sendResponseJson(obj);
        return RESULT_EMPTY;
    }

    

    private String addContributorsToProject(int projId, List<CsvContributor> contributors, String note) {
        Project project = projSrvc.getProjectById(projId);

        if (project == null) {
            return getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_PROJECT);
        }

        // get all roles
        List<Role> roles = roleSrvc.getAllRoles();
        HashMap<Integer, Role> roleMap = new HashMap<Integer, Role>();

        for (Role role : roles) {
            roleMap.put(role.getId(), role);
        }

        for (CsvContributor contrib : contributors) {
            addContributorToProject(project, contrib, roleMap, note);
        }

        return null;
    }


    private void addContributorToProject(Project project, CsvContributor contrib, HashMap<Integer, Role> roleMap, String note) {
        User user = userSrvc.getUserByEmail(contrib.getEmail());
        boolean isNewUser = false;

        if (user == null) {
            String userName = contrib.getUserName();

            if (userName == null) {
                userName = userSrvc.suggestUserNameByEmail(contrib.getEmail());
            } else {
                // see whether there is already a user by the username
                if (userSrvc.getUser(userName) != null) {
                    userName = userSrvc.suggestUserNameByEmail(contrib.getEmail());
                }
            }

            user = userSrvc.createUser(contrib.getFirstName(), contrib.getLastName(), contrib.getEmail(), userName);

            if (user == null) {
                logger.error("Can't create new user: firstName= " + contrib.getFirstName() + " lastName=" + contrib.getLastName() + " email=" + contrib.getEmail() + " userName=" + userName);
                return;
            }

            isNewUser = true;
        }

        if (!isNewUser) {
            // see if the user is already in the project
            ProjectMembership member = projSrvc.getProjectMembership(project.getId(), user.getId());
            if (member != null) {
                // need to update the role if changed
                if (member.getRoleId() != contrib.getRoleId()) {
                    member.setRoleId(contrib.getRoleId());
                    projSrvc.updateProjectMembership(member);                    
                }
                return;
            }           
        }

        // new membership
        ProjectMembership member = new ProjectMembership();
        member.setProjectId(project.getId());
        member.setUserId(user.getId());
        member.setRoleId(contrib.getRoleId());
        projSrvc.addProjectMembership(member);

        // send notification to the new project contributor
        Role role = roleMap.get(contrib.getRoleId());
        sendNotification(user, isNewUser, project, role, note);
    }

    public String create() {
        // should have more defensive code to add, e.g. checking existence of
        // project, validation of email, first name, last name etc.
        logger.debug("Create membership: "
                + firstName + " lastName=" + lastName + " email=" + email + " userName=" + userName + " role=" + roleId);

        List<CsvContributor> contributors = new ArrayList<CsvContributor>();
        CsvContributor contrib = new CsvContributor(email, firstName, lastName, userName, roleId);
        contributors.add(contrib);

        String errMsg = addContributorsToProject(projId, contributors, note);

        if (errMsg != null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS, errMsg);
        } else {
            super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        }

        return RESULT_EMPTY;
    }
    
    public String bulkCreate() {

        logger.debug("Bulk create from file: " + csvFilename);
        LoginUser loginUser = getLoginUser();
        
        try {
            File uploadFile = getUploadFileByFilename(csvFilename, ControlPanelConstants.UPLOAD_TYPE_CONTRIBUTOR);
            logger.debug("bulkCreate: " + uploadFile.getAbsolutePath());
            CsvContributorParser parser = new CsvContributorParser(loginUser, uploadFile, projId);
            CsvParseResult parseResult = parser.parse();

            if (parseResult.getErrorCount() == 0) {
                 String errMsg = addContributorsToProject(projId, parseResult.getUsers(), note);

                if (errMsg != null) {
                    super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS, errMsg);
                } else {
                    JSONObject retObj = new JSONObject();
                    retObj.put(KEY_RET, ControlPanelErrorCode.OK);
                    retObj.put(KEY_COUNT, parseResult.getUsers().size());
                    super.sendResponseJson(retObj);
                }
            } else {
                JSONObject retObj = new JSONObject();

                retObj.put(KEY_RET, ControlPanelErrorCode.ERR_CONTRIBUTOR_CSV);
                JSONArray jsonArray = new JSONArray();
                
                for (String line : parseResult.getErrors()) {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put(KEY_LINE, line);
                    jsonArray.add(jsonObj);
                }
                retObj.put(KEY_ERR_CSV, jsonArray);
                super.sendResponseJson(retObj);
            }
        } catch (IOException ex) {
            logger.error("bulCreate: " + ex.getMessage());
            super.sendResponseResult(ControlPanelErrorCode.ERR_DB, getMessage(Messages.KEY_COMMON_ERR_INTERNAL_SERVER));
        }
        return RESULT_EMPTY;
    }
    
    public String validateCsv() {
        
        LoginUser loginUser = getLoginUser();
        
        try {
            JSONObject retObj = new JSONObject();
            File uploadFile = getUploadFile(filename, ControlPanelConstants.UPLOAD_TYPE_CONTRIBUTOR);
            logger.debug("validateCsv: " + uploadFile.getAbsolutePath());
            FileUtils.copyInputStreamToFile(request.getInputStream(), uploadFile);

            CsvContributorParser parser = new CsvContributorParser(loginUser, uploadFile, projId);
            CsvParseResult parseResult = parser.parse();

            if (parseResult.getErrorCount() == 0) {
                int newUserCount = 0;
                int existUserCount = 0;
                for (CsvContributor contributor : parseResult.getUsers()) {
                    if (userSrvc.getUserByEmail(contributor.getEmail()) == null) {
                        ++newUserCount;
                    }
                    else {
                        ++existUserCount;
                    }
                }
                retObj.put(KEY_RET, ControlPanelErrorCode.OK);
                JSONObject jsonData = new JSONObject();
                jsonData.put(KEY_NEW_USER_COUNT, newUserCount);
                jsonData.put(KEY_EXIST_USER_COUNT, existUserCount);
                jsonData.put(KEY_ERR_COUNT, 0);
                jsonData.put(KEY_FILENAME, uploadFile.getName());
                retObj.put(KEY_DATA, jsonData);
            }
            else {
                retObj.put(KEY_RET, ControlPanelErrorCode.OK);
                JSONObject jsonData = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                for (String line : parseResult.getErrors()) {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put(KEY_LINE, line);
                    jsonArray.add(jsonObj);
                }
                jsonData.put(KEY_ERR_COUNT, parseResult.getErrors().size());
                jsonData.put(KEY_ERR_CSV, jsonArray);
                retObj.put(KEY_DATA, jsonData);
            }
            super.sendResponseJson(retObj);
        } catch (IOException ex) {
            logger.error("validateCsv: " + ex.getMessage());
            super.sendResponseResult(ControlPanelErrorCode.ERR_DB, loginUser.message(Messages.KEY_COMMON_ERR_INTERNAL_SERVER));
        }
        return RESULT_EMPTY;
    }
    
    public String updateRole() {
        logger.debug("ProjectMembershipController::updateRole: pmId=" + pmId + ", roleId=" + roleId);
        LoginUser loginUser = getLoginUser();
        JSONObject obj = new JSONObject();
        ProjectMembership pm = projSrvc.getProjectMembership(pmId);

        if (pm == null) {
            obj.put(KEY_RET, ControlPanelErrorCode.ERR_NOT_EXISTS);
            obj.put(KEY_DESC, loginUser.message(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_MEMBERSHIP));
            super.sendResponseJson(obj);
            return RESULT_EMPTY;
        }

        if (pm.getRoleId() != roleId) {
            pm.setRoleId(roleId);
            projSrvc.updateProjectMembership(pm);
        }

        obj.put(KEY_RET, ControlPanelErrorCode.OK);
        super.sendResponseJson(obj);

        logger.debug("ProjectMembershipController::updateRole returned: " + obj.toJSONString());
        return RESULT_EMPTY;
    }
    
    // send notification when adding a project contributor
    private void sendNotification(User user, boolean isNewUser, Project project, Role role, String note) {
        Map<String, String> tokens = new HashMap<String, String>();
        if (isNewUser) {
            String pattern = Config.getString(Config.KEY_NEW_USER_ACCESS_LINK);
            String accessLink = MessageFormat.format(pattern, user.getUsername(), user.getPassword());
            tokens.put(Constants.NOTIFICATION_TOKEN_ACCESS_LINK, accessLink);
        }
        LoginUser loginUser = getLoginUser();
        tokens.put(Constants.NOTIFICATION_TOKEN_ADDER_NAME, loginUser.getFirstname() + " " + loginUser.getLastname());
        tokens.put(Constants.NOTIFICATION_TOKEN_EMAIL, user.getEmail());
        tokens.put(Constants.NOTIFICATION_TOKEN_FIRST_NAME, user.getFirstName());
        tokens.put(Constants.NOTIFICATION_TOKEN_FULL_NAME, user.getFirstName() + " " + user.getLastName());
        tokens.put(Constants.NOTIFICATION_TOKEN_LAST_NAME, user.getLastName());
        tokens.put(Constants.NOTIFICATION_TOKEN_PROJECT_NAME, project.getCodeName());
        tokens.put(Constants.NOTIFICATION_TOKEN_ROLE_NAME, role.getName());
        tokens.put(Constants.NOTIFICATION_TOKEN_USER_NAME, user.getUsername());
        tokens.put(Constants.NOTIFICATION_TOKEN_NOTE, note);
        String notificationType = isNewUser ? Constants.NOTIFICATION_TYPE_NOTIFY_NEW_USER_PC
                : Constants.NOTIFICATION_TYPE_NOTIFY_EXISTING_USER_PC;
        NotificationView notification = notificationItemService.getProjectNotificationView(notificationType, project.getId(), role.getId(), user.getLanguageId(), tokens);
        mailService.addSystemMail(user.getEmail(), notification.getSubject(), notification.getBody());
    }
    
    // send notification when removing a project contributor
    private void sendNotification(User user, Project project, int roleId, String note) {
        Map<String, String> tokens = new HashMap<String, String>();
        LoginUser loginUser = getLoginUser();
        tokens.put(Constants.NOTIFICATION_TOKEN_ADDER_NAME, loginUser.getFirstname() + " " + loginUser.getLastname());
        tokens.put(Constants.NOTIFICATION_TOKEN_FULL_NAME, user.getFirstName() + " " + user.getLastName());
        tokens.put(Constants.NOTIFICATION_TOKEN_PROJECT_NAME, project.getCodeName());
        tokens.put(Constants.NOTIFICATION_TOKEN_NOTE, note);
        String notificationType = Constants.NOTIFICATION_TYPE_NOTIFY_DEL_USER_PC;
        NotificationView notification = notificationItemService.getProjectNotificationView(notificationType, project.getId(), roleId, user.getLanguageId(), tokens);
        mailService.addSystemMail(user.getEmail(), notification.getSubject(), notification.getBody());
    }

    // keys for returned JSON data
    private static final String KEY_USER_NAME     = "username";
    private static final String KEY_NEW_USER      = "newuser";
    private static final String KEY_FIRST_NAME    = "firstname";
    private static final String KEY_LAST_NAME     = "lastname";
    private static final String KEY_ORG_ID        = "orgid";
    private static final String KEY_ROLES         = "roles";
    private static final String KEY_ID            = "id";
    private static final String KEY_NAME          = "name";
    private static final String KEY_ERR_CSV       = "errcsv";
    private static final String KEY_LINE          = "line";
    private static final String KEY_NEW_USER_COUNT      = "newcount";
    private static final String KEY_EXIST_USER_COUNT    = "existcount";
    private static final String KEY_ERR_COUNT           = "errCount";
    private static final String KEY_DATA                = "data";
    private static final String KEY_FILENAME            = "uploadedFilename";
    private static final String KEY_COUNT               = "count";

    // locus of transfered request data
    private Integer projId;
    public void setProjId(Integer projId) {
        this.projId = projId;
    }
    private String email;
    public void setEmail(String email) {
        this.email = (email == null) ? null : email.trim();
    }
    private String firstName;
    public void setFirstName(String firstName) {
        this.firstName = (firstName == null) ? null : firstName.trim();
    }
    private String lastName;
    public void setLastName(String lastName) {
        this.lastName = (lastName == null) ? null : lastName.trim();
    }
    private String userName;
    public void setUserName(String userName) {
        this.userName = (userName == null) ? null : userName.trim();
    }
    private Integer roleId;
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
    private String note;
    public void setNote(String note) {
        this.note = note;
    }
    private Integer pmId;
    public void setPmId(Integer pmId) {
        this.pmId = pmId;
    }
    private Integer filterRoleId;
    public void setFilterRoleId(Integer roleId) {
        this.filterRoleId = roleId;
    }
    private Boolean sendNotify;
    public void setSendNotify(Boolean sendNotify) {
        this.sendNotify = sendNotify;
    }
    private String csvFilename;
    public void setCsvFilename(String name) {
        this.csvFilename = name;
    }
    private String filename;
    public void setFilename(String name) {
        this.filename = name;
    }
}