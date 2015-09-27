/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller.proj;

import com.ocs.indaba.common.Constants;
import com.ocs.common.Config;
import com.ocs.indaba.controlpanel.common.ControlPanelConfig;
import com.ocs.indaba.controlpanel.common.ControlPanelConstants;
import com.ocs.indaba.controlpanel.common.ControlPanelErrorCode;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.exception.NoDataException;
import com.ocs.indaba.controlpanel.controller.BaseController;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.controlpanel.model.ProjectDetailVO;
import com.ocs.indaba.controlpanel.model.ProjectVO;
import com.ocs.indaba.controlpanel.service.ProjectControlPanelService;
import com.ocs.indaba.po.*;
import com.ocs.indaba.po.SurveyConfig;
import com.ocs.indaba.service.*;
import com.ocs.indaba.service.ProjectService.CheckOrgChangeResult;
import com.ocs.indaba.vo.ProjectAdminVO;
import com.ocs.indaba.vo.ProjectInfo;
import com.ocs.util.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
    @Result(name = "add", location = "project-form.jsp")})
public class ProjectsController extends BaseController {

    private static final Logger logger = Logger.getLogger(ProjectsController.class);
    private static final long serialVersionUID = -2487852558172383390L;
    private static final String PARAM_TAB = "tab";
    private static final String PARAM_LANGUAGE = "language";
    private static final String PARAM_PROJECT_NAME = "projName";
    private static final String PARAM_DESCRIPTON = "description";
    private static final String PARAM_FILTER_ORG_ID = "filterOrgId";
    private static final String PARAM_VISIBILITY = "visibility";
    private static final String PARAM_PRIMARY_ORG = "primaryOrg";
    private static final String PARAM_ORG_IDS = "orgIds[]";
    private static final String PARAM_PRIMARY_ADMIN = "primaryAdmin";
    private static final String PARAM_ACTION_RIGTH = "actionRight";
    private static final String PARAM_VIEW_RIGHT = "viewRight";
    private static final String PARAM_START_TIME = "startTime";
    private static final String PARAM_END_TIME = "endTime";
    private static final String PARAM_ACTIVE = "active";
    private static final String PARAM_STUDY_PERIOD = "studyPeriod";
    private static final String PARAM_PROJECT_LOGO = "projLogo";
    private static final String PARAM_SPONSOR_LOGOS = "sponsorLogos[]";
    private static final String PARAM_UPDATE_FIELD = "updateField";
    private static final String PARAM_VALUE = "value";
    //
    // Attribute keys
    private static final String ATTR_ORGANIZATIONS = "orgs";
    private static final String ATTR_ORG_ADMINS = "orgAdmins";
    private static final String ATTR_ACTION_RIGHTS = "actionRights";
    private static final String ATTR_VIEW_RIGHTS = "viewRights";
    //private static final String ATTR_TARGETS = "targets";
    private static final String ATTR_ROLES = "roles";
    private static final String ATTR_PROJECT_ROLES = "projRoles";
    private static final String ATTR_STUDY_PERIODS = "studyPeriods";
    private static final String ATTR_PROJECT_ID = "projId";
    //
    //
    private static final String UPDATE_SECONDARY_ORGS = "secondaryOrgs";
    private static final String UPDATE_SECONDARY_ADMINS = "secondaryAdmins";
    private static final String UPDATE_ROLES = "roles";
    //private static final String UPDATE_TARGETS = "targets";
    private static final String ACTION_ADD = "add";
    private static final String ACTION_DELETE = "delete";
    @Autowired
    private ProjectService projSrvc;
    @Autowired
    private LanguageService langSrvc;
    @Autowired
    private RoleService roleSrvc;
    @Autowired
    private ViewMatrixService viewMatrixSrvc;
    @Autowired
    private AccessMatrixService accessMatrixSrvc;
    @Autowired
    private StudyPeriodService studyPeriodSrvc;
    @Autowired
    private TargetService targetSrvc;
    @Autowired
    private UploadFileService uploadFileSrvc = null;
    @Autowired
    private ProjectControlPanelService projectCpService;

    public String index() {
        int visibility = StringUtils.str2int(request.getParameter(PARAM_VISIBILITY));
        logger.debug("Request Params: \n\tvisibility=" + visibility);
        //Pagination<IndicatorVO> indicatorPage = libSrvc.findIndicators(type, userTag, indicatorTag, organization);
        //request.setAttribute("totalCount", indicatorPage.getCurPageList().size());
        int orgId = StringUtils.str2int(request.getParameter(PARAM_FILTER_ORG_ID));
        int page = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGE));
        request.setAttribute(PARAM_FILTER_ORG_ID, orgId);
        request.setAttribute(PARAM_PAGINATION_PAGE, page);
        request.setAttribute(PARAM_VISIBILITY, (visibility > 0) ? visibility : ControlPanelConstants.VISIBILITY_PUBLIC);
        LoginUser loginUser = getLoginUser();
        request.setAttribute(ATTR_ORGANIZATIONS, loginUser.getAccessibleOrgs(Constants.VISIBILITY_PRIVATE));
        return RESULT_INDEX;
    }

    public String find() {
        int visibility = StringUtils.str2int(request.getParameter(PARAM_VISIBILITY));
        int pageSize = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGESIZE));
        int page = StringUtils.str2int(request.getParameter(PARAM_PAGINATION_PAGE));
        int orgId = StringUtils.str2int(request.getParameter(PARAM_FILTER_ORG_ID));
        String sortName = request.getParameter(PARAM_SORT_NAME);
        String sortOrder = request.getParameter(PARAM_SORT_ORDER);
        LoginUser loginUser = super.getLoginUser();
        int userId = (loginUser == null) ? 1 : loginUser.getUserId();

        boolean isSysAdmin = (loginUser == null) ? false : loginUser.isSiteAdmin();
        request.setAttribute(ATTR_IS_SITE_ADMIN, isSysAdmin);

        logger.debug("Request Params: "
                + "\n\tuserId=" + userId + "(" + (isSysAdmin ? "ADMIN" : "NON-ADMIN") + ")"
                + "\n\tvisibility=" + visibility
                + "\n\tpageSize=" + pageSize
                + "\n\tpage=" + page
                + "\n\torgId=" + orgId
                + "\n\tsortName=" + sortName
                + "\n\tsortOrder=" + sortOrder);

        List<ProjectVO> projects = null;
        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            sortOrder = "asc";
        }
        try {
            List<Integer> orgIds = null;

            if (orgId > 0) {
                orgIds = new ArrayList<Integer>();
                orgIds.add(orgId);
            } else {
                orgIds = isSysAdmin ? null : loginUser.getAccessibleOrgIds(Constants.VISIBILITY_PRIVATE);
            }

            Pagination<ProjectInfo> projPage = projSrvc.getProjects((isSysAdmin) ? 0 : loginUser.getUserId(), orgIds, visibility, sortName, sortOrder, page, pageSize);
            List<ProjectInfo> projInfoList = projPage.getRows();
            if (projInfoList != null && !projInfoList.isEmpty()) {
                projects = new ArrayList<ProjectVO>(projInfoList.size());
                for (ProjectInfo prj : projInfoList) {
                    projects.add(ProjectVO.initWithProjectInfo(prj));
                }
            }
            projPage.addProperty(ATTR_IS_SITE_ADMIN, isSysAdmin);
            Pagination<ProjectVO> pagination = new Pagination<ProjectVO>(projPage.getTotal(), page, pageSize);
            pagination.setRows(projects);
            logger.debug(pagination);
            sendResponseMessage(pagination.toJsonString());
        } catch (Exception ex) {
            logger.error("Error occurs!", ex);
        }

        return RESULT_EMPTY;
    }

    public String exists() {
        LoginUser user = getLoginUser();
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        String fieldId = request.getParameter(PARAM_FIELD_ID);
        String fieldValue = request.getParameter(PARAM_FIELD_VALUE);
        logger.debug("Request Params: \n\tprojId=" + projId + "\n\tfieldId=" + fieldId + "\n\tfieldValue=" + fieldValue);
        JSONArray jsonArr = new JSONArray();
        jsonArr.add(fieldId);

        // check whether a DIFFERENT project with this name already exists
        Project p = projSrvc.getProjectByName(fieldValue);
        if (p != null && p.getId() != projId) {
            jsonArr.add(false);
            jsonArr.add(user.message(ControlPanelMessages.KEY_DUPLICATED_PROJECT_NAME, fieldValue));
        } else {
            jsonArr.add(true);
        }
        super.sendResponseJson(jsonArr);
        return RESULT_EMPTY;
    }

    public String projectForm() throws NoDataException{
        LoginUser loginUser = getLoginUser();
        int visibility = StringUtils.str2int(request.getParameter(PARAM_VISIBILITY));
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));

        logger.debug("Request Params: \n\tvisibility=" + visibility);

        boolean isSysAdmin = (loginUser == null) ? false : loginUser.isSiteAdmin();
        request.setAttribute(PARAM_TAB, request.getParameter(PARAM_TAB));
        request.setAttribute(ATTR_IS_SITE_ADMIN, isSysAdmin);
        request.setAttribute(ATTR_PROJECT_ID, projId);
        request.setAttribute(PARAM_VISIBILITY, (visibility > 0) ? visibility : ControlPanelConstants.TARGET_LIB_VISIBILITY_PUBLIC);
        List<Organization> orgs = loginUser.getAccessibleOrgs(Constants.VISIBILITY_PRIVATE);
        request.setAttribute(ATTR_ORGANIZATIONS, orgs);
        request.setAttribute(ATTR_LANGUAGES, langSrvc.getAllLanguages());
        request.setAttribute(ATTR_ROLES, roleSrvc.getAllRoles());
        request.setAttribute(ATTR_PROJECT_ROLES, roleSrvc.getAllRoles(projId));
        request.setAttribute(ATTR_STUDY_PERIODS, studyPeriodSrvc.getAllStudyPeriods());
        request.setAttribute(ATTR_VIEW_RIGHTS, viewMatrixSrvc.getAllViewMatrixes());
        request.setAttribute(ATTR_ACTION_RIGHTS, accessMatrixSrvc.getAllAccessMatrixes());
        Project project = null;
        if (projId > 0 && (project = projSrvc.getProjectById(projId)) != null) {
            request.setAttribute(ATTR_IS_PROJECT_ADMIN, (loginUser.getUserId() == project.getAdminUserId()));
            request.setAttribute(ATTR_ORG_ADMINS, userSrvc.getOrgAdminUsersByOrgIds(projSrvc.getOwnedOrgIds(projId)));
        } else if (projId > 0) {
            throw new NoDataException(getMessage(ControlPanelMessages.KEY_ERROR_INVALID_PROJECT_ID));
        }

        return RESULT_ADD;
    }


    static private void fixFilePath(String path, UploadFile uf) {
        String absPath = path + "/" + uf.getFileName();
        uf.setFilePath(absPath);
        if (uf.getSize() <= 0) {
            File file = new File(absPath);
            uf.setSize((int)file.length());
        }
    }



    public String getProject() {
        String projLogoPath = Config.getString(ControlPanelConfig.KEY_STORAGE_DIR_PROJECTLOGO);
        String sponsorLogoPath = Config.getString(ControlPanelConfig.KEY_STORAGE_DIR_SPONSORLOGO);

        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        logger.debug("Request Params: \n\tprojId=" + projId);
        Project project = projSrvc.getProjectById(projId);
        if (project == null) {
            super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                    getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_PROJECT));

            return RESULT_EMPTY;
        }
        ProjectDetailVO projDetail = ProjectDetailVO.initWithProject(project);
        String projLogo = projDetail.getProjLogo();
        if (!StringUtils.isEmpty(projLogo)) {
            UploadFile upFile = uploadFileSrvc.getUploadFileByName(projLogo);

            if (upFile == null) {
                // try old way
                upFile = new UploadFile();
                upFile.setDisplayName(projLogo);
                upFile.setFileName(projLogo);
            }

            fixFilePath(projLogoPath, upFile);
            projDetail.setProjLogoFile(upFile);
        }
        List<String> sponsorLogos = projDetail.getSponsorLogos();
        if (sponsorLogos != null && !sponsorLogos.isEmpty()) {
            List<UploadFile> upFiles = uploadFileSrvc.getUploadFilesByNames(sponsorLogos);
            if (upFiles != null && !upFiles.isEmpty()) {
                for (UploadFile f : upFiles) {                    
                    projDetail.addSponsorLogoFile(f);
                    fixFilePath(sponsorLogoPath, f);
                }
            } else {
                // try the old way
                logger.debug("Try old way to get sponsor logos from " + sponsorLogoPath);
                for (String logo : sponsorLogos) {
                    UploadFile uf = new UploadFile();
                    uf.setDisplayName(logo);
                    uf.setFileName(logo);
                    fixFilePath(sponsorLogoPath, uf);
                    projDetail.addSponsorLogoFile(uf);
                }
            }
        }

        List<ProjectAdminVO> projAdmins = projSrvc.getProjectAdmins(projId);
        if (projAdmins != null && !projAdmins.isEmpty()) {
            for (ProjectAdminVO item : projAdmins) {
                projDetail.addSecondaryAdminId(item.getUserId());
            }
        }

        List<ProjectOwner> projOwners = projSrvc.getProjectOwners(projId);
        if (projOwners != null && !projOwners.isEmpty()) {
            for (ProjectOwner item : projOwners) {
                projDetail.addSecondaryOrgId(item.getOrgId());
            }
        }
        List<ProjectTarget> projTargets = projSrvc.getProjectTargets(projId);
        if (projTargets != null && !projTargets.isEmpty()) {
            for (ProjectTarget item : projTargets) {
                projDetail.addTargetId(item.getTargetId());
            }
        }
        List<ProjectRoles> projRoles = projSrvc.getProjectRoles(projId);
        if (projRoles != null && !projRoles.isEmpty()) {
            for (ProjectRoles item : projRoles) {
                projDetail.addRoleId(item.getRoleId());
            }
        }

        JSONObject json = projDetail.toJson();

        logger.debug("Project Detail: " + json);

        super.sendResponseResult(ControlPanelErrorCode.OK, json, "OK");
        return RESULT_EMPTY;
    }

    public String create() {
        LoginUser loginUser = super.getLoginUser();
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        int langId = StringUtils.str2int(request.getParameter(PARAM_LANGUAGE));
        String projName = request.getParameter(PARAM_PROJECT_NAME);
        String description = request.getParameter(PARAM_DESCRIPTON);
        int primaryOrgId = StringUtils.str2int(request.getParameter(PARAM_PRIMARY_ORG));
        //List<Integer> secondaryOrgIds = StringUtils.strArr2IntList(request.getParameterValues(PARAM_SECONDARY_ORGS));
        int primaryAdminId = StringUtils.str2int(request.getParameter(PARAM_PRIMARY_ADMIN));
        //List<Integer> secondaryAdminIds = StringUtils.strArr2IntList(request.getParameterValues(PARAM_SECONDARY_ADMINS));
        short visibility = StringUtils.str2short(request.getParameter(PARAM_VISIBILITY));
        //List<Integer> roleIds = StringUtils.strArr2IntList(request.getParameterValues(PARAM_ROLES));
        int actionRightId = StringUtils.str2int(request.getParameter(PARAM_ACTION_RIGTH));
        int viewRightId = StringUtils.str2int(request.getParameter(PARAM_VIEW_RIGHT));
        String sStartTime = request.getParameter(PARAM_START_TIME);
        Date startTime = DateUtils.parse(sStartTime, DateUtils.DATE_FORMAT_6);
        String sEndTime = request.getParameter(PARAM_END_TIME);
        Date endTime = DateUtils.parse(sEndTime, DateUtils.DATE_FORMAT_6);
        boolean active = (ControlPanelConstants.PROJECT_IS_ACTIVE == StringUtils.str2short(request.getParameter(PARAM_ACTIVE)));
        int studyPeriodId = StringUtils.str2int(request.getParameter(PARAM_STUDY_PERIOD));
        //List<Integer> targetIds = StringUtils.strArr2IntList(request.getParameterValues(PARAM_TARGETS));
        String projLogo = request.getParameter(PARAM_PROJECT_LOGO);
        String[] sponsorLogos = request.getParameterValues(PARAM_SPONSOR_LOGOS);
        logger.debug("Request Params: "
                + "\n\tprojId=" + projId
                + "\n\tlangId=" + langId
                + "\n\tprojName=" + projName
                + "\n\tdescription=" + description
                + "\n\tprimaryOrg=" + primaryOrgId
                //+ "\n\tsecondaryOrgs=" + secondaryOrgIds
                + "\n\tprimaryAdmin=" + primaryAdminId
                //+ "\n\tsecondaryAdmins=" + secondaryAdminIds
                + "\n\tvisibility=" + visibility
                //+ "\n\troles=" + roleIds
                + "\n\tactionRight=" + actionRightId
                + "\n\tviewRight=" + viewRightId
                + "\n\tstartTime=" + sStartTime
                + "\n\tendTime=" + sEndTime
                + "\n\tactive=" + active
                + "\n\tstudyPeriod=" + studyPeriodId
                //+ "\n\ttargets=" + targetIds
                + "\n\tprojLogo=" + projLogo
                + "\n\tsponsorLogos=" + sponsorLogos);
        try {
            Date now = new Date();
            Project project = new Project();
            project.setIsActive(active);
            project.setAdminUserId(primaryAdminId);
            project.setAccessMatrixId(actionRightId);
            project.setCloseTime(endTime);
            project.setCodeName(projName);
            project.setCreationTime(now);
            project.setDescription(description);
            project.setLogoPath(projLogo);
            project.setOrganizationId(primaryOrgId);
            project.setOwnerUserId(loginUser.getUserId()); // Owner user id is the user creating the project

            if (sponsorLogos != null && sponsorLogos.length > 0) {
                project.setSponsorLogos(ListUtils.listToString(Arrays.asList(sponsorLogos), " "));
            }
            project.setStatus(ControlPanelConstants.RESOURCE_STATUS_ACTIVE);
            project.setStartTime(startTime);
            project.setStudyPeriodId(studyPeriodId);
            project.setViewMatrixId(viewRightId);
            project.setVisibility(visibility);

            if (projId <= 0) {
                // create new project
                Project p = projSrvc.getProjectByName(projName);

                if (p != null) {
                    super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_EXISTS,
                            loginUser.message(ControlPanelMessages.KEY_DUPLICATED_PROJECT_NAME, projName));
                    return RESULT_EMPTY;
                }

                project = projSrvc.addProject(project);
            } else {
                // update existing project
                // check name conflict
                Project p = projSrvc.getProjectByName(projName);

                if (p != null && projId != p.getId()) {
                    super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_EXISTS,
                            loginUser.message(ControlPanelMessages.KEY_DUPLICATED_PROJECT_NAME, projName));
                    return RESULT_EMPTY;
                }

                // get original project data
                p = projSrvc.getProjectById(projId);

                if (p == null) {
                    // should never happen!
                    super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN,
                            loginUser.message(ControlPanelMessages.PROGRAM_ERROR));
                    return RESULT_EMPTY;
                }

                project.setId(projId);

                // visibility can never be changed!
                project.setVisibility(p.getVisibility());

                // owner cannot be changed
                project.setOwnerUserId(p.getOwnerUserId());

                // get ready for primary owner change
                project.setAdminUserId(p.getAdminUserId());
                project.setOrganizationId(p.getOrganizationId());

                CheckOrgChangeResult result = projSrvc.changePrimaryOwner(project, primaryOrgId, primaryAdminId);

                switch (result.status) {
                    case ProjectService.OK:
                        break;

                    case ProjectService.ORG_PROJECT_ADMINS_IN_USE:
                        super.sendResponseResult(ControlPanelErrorCode.ERR_NO_PERMISSION,
                                getMessage(ControlPanelMessages.KEY_ERROR_PRIMARY_ORG_ADMIN_USED,
                                (result.paInUse.getFirstName() + " " + result.paInUse.getLastName())));
                        return RESULT_EMPTY;

                    case ProjectService.ORG_PRIVATE_TARGETS_IN_USE:
                        super.sendResponseResult(ControlPanelErrorCode.ERR_NO_PERMISSION,
                                getMessage(ControlPanelMessages.KEY_ERROR_PRIMARY_ORG_PRIVATE_TARGETS_USED,
                                result.targetInUse.getName()));
                        return RESULT_EMPTY;

                    case ProjectService.SC_VISIBILITY_VIOLATION:
                        Organization org = loginUser.getOrg(primaryOrgId);
                        super.sendResponseResult(ControlPanelErrorCode.ERR_NO_PERMISSION,
                                getMessage(ControlPanelMessages.KEY_ERROR_SC_VISIBILITY_VIOLATED_CHANGE_PRIMARY,
                                org.getName(), result.violatedConfig.getName()));
                        return RESULT_EMPTY;

                    default:
                        super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN,
                                getMessage(ControlPanelMessages.PROGRAM_ERROR));
                        return RESULT_EMPTY;
                }

                project = projSrvc.updateProject(project);
            }

            super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        } catch (Exception ex) {
            logger.error("Error occurs!", ex);
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, getMessage(ControlPanelMessages.PROGRAM_ERROR));
        }
        return RESULT_EMPTY;
    }

    public String updateSponsorLogo() {
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        String[] sponsorLogoArr = request.getParameterValues(PARAM_SPONSOR_LOGOS);
        String sponsorLogos = (sponsorLogoArr != null && sponsorLogoArr.length > 0) ? ListUtils.listToString(Arrays.asList(sponsorLogoArr)) : "";

        logger.debug("Updating Sponsor Logo: projectId=" + projId + " Logos=" + sponsorLogos);

        if (projId > 0) {
            Project project = projSrvc.getProjectById(projId);

            if (project == null) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, getMessage(ControlPanelMessages.PROGRAM_ERROR));
                return RESULT_EMPTY;
            }

            project.setSponsorLogos(sponsorLogos);
            projSrvc.updateProject(project);
            super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        } else {
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, getMessage(ControlPanelMessages.PROGRAM_ERROR));
        }
        return RESULT_EMPTY;
    }

    public String updateProjLogo() {
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        String projLogo = request.getParameter(PARAM_PROJECT_LOGO);

        logger.debug("Update Project Logo: pojectId=" + projId + " Logo=" + projLogo);
        
        if (projId > 0) {
            Project project = projSrvc.getProjectById(projId);
            
            if (project == null) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS,
                        getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_PROJECT));
                
                return RESULT_EMPTY;
            }

            if (StringUtils.isEmpty(projLogo)) {
                project.setLogoPath(null);
            } else {
                project.setLogoPath(projLogo);
            }
            projSrvc.updateProject(project);
            super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        } else {
            super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS, getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_PROJECT));
        }
        return RESULT_EMPTY;
    }

    public String updateOrgData() {
        List<Integer> orgIds = StringUtils.strArr2IntList(request.getParameterValues(PARAM_ORG_IDS));
        logger.debug("Request Params: " + orgIds);
        JSONObject root = new JSONObject();
        try {
            List<User> adminUsers = userSrvc.getOrgAdminUsersByOrgIds(orgIds);
            JSONArray adminsJsonArr = new JSONArray();
            if (adminUsers != null && !adminUsers.isEmpty()) {
                for (User u : adminUsers) {
                    JSONObject user = new JSONObject();
                    user.put("id", u.getId());
                    user.put("firstName", u.getFirstName());
                    user.put("lastName", u.getLastName());
                    adminsJsonArr.add(user);
                }
            }
            root.put("admins", adminsJsonArr);
            JSONArray targetsJsonArr = new JSONArray();
            List<Target> targets = targetSrvc.getAllTargets(orgIds);
            if (targets != null && !targets.isEmpty()) {
                for (Target t : targets) {
                    JSONObject target = new JSONObject();
                    target.put("id", t.getId());
                    target.put("name", t.getName());
                    targetsJsonArr.add(target);
                }
            }
            root.put("targets", targetsJsonArr);
            super.sendResponseResult(ControlPanelErrorCode.OK, root, "OK");
        } catch (Exception ex) {
            logger.error("Error occurs!", ex);
            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN, root,
                    getMessage(ControlPanelMessages.PROGRAM_ERROR));
        }
        return RESULT_EMPTY;
    }

    public String getAdminSelectionList() {
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        List<User> user = userSrvc.getOrgAdminUsersByOrgIds(projSrvc.getOwnedOrgIds(projId));

        logger.debug("getAdminSelectionList Request Parms: project=" + projId);

        JSONArray result = JSONUtils.listToJson(user);

        logger.debug("Selected Admins: " + result);

        super.sendResponseResult(ControlPanelErrorCode.OK, result, "OK");
        return RESULT_EMPTY;
    }

    public String updateData() {
        String updateField = request.getParameter(PARAM_UPDATE_FIELD);
        int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
        String action = request.getParameter(PARAM_ACTION);
        int value = StringUtils.str2int(request.getParameter(PARAM_VALUE), -1);

        logger.debug("Request Params: "
                + "\n\tprojId=" + projId
                + "\n\tupdateField=" + updateField
                + "\n\taction=" + action
                + "\n\tvalue=" + value);

        Project project = projSrvc.getProjectById(projId);
        if (project == null) {
            super.sendResponseResult(ControlPanelErrorCode.OK, getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_PROJECT));
            return RESULT_EMPTY;
        }
        // TBD: To handle field updates (secondary owners, secondary administrators, roles and targets)
        if (UPDATE_SECONDARY_ORGS.equalsIgnoreCase(updateField)) {
            int orgId = value;
            if (orgId < 0) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM, getMessage(ControlPanelMessages.KEY_ERROR_INVALID_ORG_ID));
                return RESULT_EMPTY;
            }

            if (ACTION_ADD.equalsIgnoreCase(action)) { // add
                if (orgId == project.getOrganizationId()) {
                    super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM, getMessage(ControlPanelMessages.KEY_ERROR_ALREADY_EXISTS_PRIMARY_OWNER));
                    return RESULT_EMPTY;
                }

                if (projSrvc.hasProjectOwner(projId, orgId)) {
                    super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_EXISTS, getMessage(ControlPanelMessages.KEY_ERROR_ALREADY_EXISTS_SECONDARY_OWNER));
                    return RESULT_EMPTY;
                }

                // check whether the new org would violate private survey_config use rule:
                // all orgs must be contributors of the SC.
                SurveyConfig sc = projSrvc.findViolatedSurveyConfig(project, orgId);

                if (sc != null) {
                    super.sendResponseResult(ControlPanelErrorCode.ERR_NO_PERMISSION,
                            getMessage(ControlPanelMessages.KEY_ERROR_SC_VISIBILITY_VIOLATED_ADD_ORG, sc.getName()));
                    return RESULT_EMPTY;
                }

                ProjectOwner po = new ProjectOwner();
                po.setProjectId(projId);
                po.setOrgId(orgId);
                projSrvc.addProjectOwner(po);
                super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
                return RESULT_EMPTY;

            } else if (ACTION_DELETE.equalsIgnoreCase(action)) { // delete
                ProjectOwner projectOwner = projSrvc.getProjectOwner(projId, orgId);

                if (projectOwner == null) {
                    super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS, getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_PROJECT_OWNER));
                    return RESULT_EMPTY;
                }

                if (orgId != project.getOrganizationId()) {
                    CheckOrgChangeResult result = projSrvc.checkRemoveOrg(project, orgId);

                    switch (result.status) {
                        case ProjectService.ORG_PROJECT_ADMINS_IN_USE:
                            super.sendResponseResult(ControlPanelErrorCode.ERR_NO_PERMISSION,
                                    getMessage(ControlPanelMessages.KEY_ERROR_ORG_ADMIN_USED,
                                    (result.paInUse.getFirstName() + " " + result.paInUse.getLastName())));
                            return RESULT_EMPTY;

                        case ProjectService.ORG_PRIVATE_TARGETS_IN_USE:
                            super.sendResponseResult(ControlPanelErrorCode.ERR_NO_PERMISSION,
                                    getMessage(ControlPanelMessages.KEY_ERROR_ORG_PRIVATE_TARGETS_USED,
                                    result.targetInUse.getName()));
                            return RESULT_EMPTY;

                        case ProjectService.OK:
                            break;

                        default:
                            super.sendResponseResult(ControlPanelErrorCode.ERR_UNKNOWN,
                                    getMessage(ControlPanelMessages.PROGRAM_ERROR));
                            return RESULT_EMPTY;
                    }
                }

                projSrvc.deleteProjectOwner(projectOwner.getId());
            }
        } else if (UPDATE_SECONDARY_ADMINS.equalsIgnoreCase(updateField)) {
            int userId = value;
            if (userId < 0) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM, getMessage(ControlPanelMessages.KEY_ERROR_INVALID_USER_ID));
                return RESULT_EMPTY;
            }

            if (ACTION_ADD.equalsIgnoreCase(action)) { // add
                if (userId == project.getAdminUserId()) {
                    super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM, getMessage(ControlPanelMessages.KEY_ERROR_ALREADY_EXISTS_PRIMARY_ADMIN));
                    return RESULT_EMPTY;
                }

                ProjectAdmin pa = projSrvc.getProjectAdmin(projId, userId);

                if (pa != null) {
                    super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_EXISTS, getMessage(ControlPanelMessages.KEY_ERROR_ALREADY_EXISTS_SECONDARY_ADMIN));
                    return RESULT_EMPTY;
                }

                pa = new ProjectAdmin();
                pa.setProjectId(projId);
                pa.setUserId(userId);
                projSrvc.addProjectAdmin(pa);
                projectCpService.sendNotification(getLoginUser(), project, userId, ControlPanelConstants.PROJECT_SECONDARY_OWNER_ACTION_ADD);
                super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
                return RESULT_EMPTY;

            } else if (ACTION_DELETE.equalsIgnoreCase(action)) { // delete
                ProjectAdmin pa = projSrvc.getProjectAdmin(projId, userId);

                if (pa == null) {
                    super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS, getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_PROJECT_ADMIN));
                    return RESULT_EMPTY;
                }
                projSrvc.deleteProjectAdmin(pa.getId());
                projectCpService.sendNotification(getLoginUser(), project, userId, ControlPanelConstants.PROJECT_SECONDARY_OWNER_ACTION_DEL);
                super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
                return RESULT_EMPTY;
            }
        } else if (UPDATE_ROLES.equalsIgnoreCase(updateField)) {
            int roleId = value;
            if (roleId <= 0) {
                super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM, getMessage(ControlPanelMessages.KEY_ERROR_INVALID_ROLE_ID));
                return RESULT_EMPTY;
            }

            ProjectRoles projectRole = projSrvc.getProjectRoles(projId, roleId);

            if (ACTION_ADD.equalsIgnoreCase(action)) { // add
                if (projectRole != null) {
                    super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_EXISTS, getMessage(ControlPanelMessages.KEY_ERROR_ALREADY_EXISTS_PROJECT_ROLE));
                    return RESULT_EMPTY;
                }

                projectRole = new ProjectRoles();
                projectRole.setProjectId(projId);
                projectRole.setRoleId(roleId);
                projSrvc.addProjectRole(projectRole);
                super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
                return RESULT_EMPTY;

            } else if (ACTION_DELETE.equalsIgnoreCase(action)) { // delete
                if (projectRole == null) {
                    super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS, getMessage(ControlPanelMessages.KEY_ERROR_NON_EXISTENT_PROJECT_ROLE));
                    return RESULT_EMPTY;
                }

                String roleName = projectCpService.checkContributorForRole(project, roleId);
                if (roleName != null) {
                    super.sendResponseResult(ControlPanelErrorCode.ERR_NO_PERMISSION,
                            getMessage(ControlPanelMessages.KEY_ERROR_PROJECT_ROLE_USED, roleName));

                    return RESULT_EMPTY;
                }
                projSrvc.deleteProjectRoles(projectRole.getId());
                super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
                return RESULT_EMPTY;
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // NOTE: target add/delete is moved to ProjectTargetController.java (addTarget/deleteTarget methods).
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*else if (UPDATE_TARGETS.equalsIgnoreCase(updateField)) {
        int targetId = value;
        if (targetId <= 0) {
        super.sendResponseResult(ControlPanelErrorCode.ERR_INVALID_PARAM, "Invalid Target Id");
        return RESULT_EMPTY;
        }
        ProjectTarget projectTarget = projSrvc.getProjectTarget(projId, targetId);
        if (ACTION_ADD.equalsIgnoreCase(action)) { // add
        if (projectTarget != null) {
        super.sendResponseResult(ControlPanelErrorCode.ERR_ALREADY_EXISTS, "The Project Target already exsit");
        return RESULT_EMPTY;
        }
        int rt = projectCpService.initializeHorse(projId, targetId);
        if(rt != 0){
        super.sendResponseResult(ControlPanelErrorCode.ERR_DB, "There is something wrong with db");
        return RESULT_EMPTY;
        }
        projectTarget = new ProjectTarget();
        projectTarget.setProjectId(projId);
        projectTarget.setTargetId(targetId);
        projSrvc.addProjectTarget(projectTarget);
        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return RESULT_EMPTY;
        } else if (ACTION_DELETE.equalsIgnoreCase(action)) { // delete
        if (projectTarget == null) {
        super.sendResponseResult(ControlPanelErrorCode.ERR_NOT_EXISTS, "This Project Target don't exsit");
        return RESULT_EMPTY;
        }
        int rt = projectCpService.deleteTarget(projId, targetId);
        if(rt != 0){
        super.sendResponseResult(ControlPanelErrorCode.ERR_DB, "There is something wrong with db");
        return RESULT_EMPTY;
        }
        //projSrvc.deleteProjectAdmin(projectTarget.getId());//since delete_target procedure delete project_target already
        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return RESULT_EMPTY;
        }
        } */
        super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
        return RESULT_EMPTY;
    }
    /*
    public String updateData_v1() {
    String updateField = request.getParameter(PARAM_UPDATE_FIELD);
    int projId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID));
    Project project = projSrvc.getProjectById(projId);
    if (project == null) {
    super.sendResponseResult(ControlPanelErrorCode.OK, "Project is not existing!");
    return RESULT_EMPTY;
    }
    if (UPDATE_SECONDARY_ORGS.equalsIgnoreCase(updateField)) {
    List<Integer> secondaryOrgIds = StringUtils.strArr2IntList(request.getParameterValues(PARAM_SECONDARY_ORGS));
    if (secondaryOrgIds != null) {
    int index = secondaryOrgIds.indexOf(project.getOrganizationId());
    if (index != -1) {
    secondaryOrgIds.remove(index);
    }
    if (secondaryOrgIds != null && !secondaryOrgIds.isEmpty()) {
    for (int orgId : secondaryOrgIds) {
    ProjectOwner projOwner = new ProjectOwner();
    projOwner.setOrgId(orgId);
    projOwner.setProjectId(projId);
    projOwner = projSrvc.addProjectOwner(projOwner);
    }
    }
    }
    } else if (UPDATE_SECONDARY_ADMINS.equalsIgnoreCase(updateField)) {
    List<Integer> secondaryAdminIds = StringUtils.strArr2IntList(request.getParameterValues(PARAM_SECONDARY_ADMINS));
    if (secondaryAdminIds != null) {
    int index = secondaryAdminIds.indexOf(project.getAdminUserId());
    if (index != -1) {
    secondaryAdminIds.remove(index);
    }
    if (!secondaryAdminIds.isEmpty()) {
    for (int adminId : secondaryAdminIds) {
    ProjectAdmin projAdmin = new ProjectAdmin();
    projAdmin.setUserId(adminId);
    projAdmin.setProjectId(projId);
    projAdmin = projSrvc.addProjectAdmin(projAdmin);
    }
    }
    }
    } else if (UPDATE_ROLES.equalsIgnoreCase(updateField)) {
    List<Integer> roleIds = StringUtils.strArr2IntList(request.getParameterValues(PARAM_ROLES));
    if (roleIds != null && !roleIds.isEmpty()) {
    for (int roleId : roleIds) {
    ProjectRoles projRoles = new ProjectRoles();
    projRoles.setRoleId(roleId);
    projRoles.setProjectId(projId);
    projRoles = projSrvc.addProjectRole(projRoles);
    }
    }
    } else if (UPDATE_TARGETS.equalsIgnoreCase(updateField)) {
    List<Integer> targetIds = StringUtils.strArr2IntList(request.getParameterValues(PARAM_TARGETS));
    if (targetIds != null && !targetIds.isEmpty()) {
    for (int targetId : targetIds) {
    ProjectTarget projTarget = new ProjectTarget();
    projTarget.setTargetId(targetId);
    projTarget.setProjectId(projId);
    projTarget = projSrvc.addProjectTarget(projTarget);
    }
    }
    }
    super.sendResponseResult(ControlPanelErrorCode.OK, "OK");
    return RESULT_EMPTY;
    }
     */

    public String execute() {
        return index();
    }
}
