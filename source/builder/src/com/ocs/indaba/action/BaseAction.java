/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.common.Config;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.Event;
import com.ocs.indaba.po.Product;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.Role;
import com.ocs.indaba.po.Target;
import com.ocs.indaba.po.Team;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.*;
import com.ocs.indaba.util.CookieUtils;
import com.ocs.indaba.util.FilePathUtil;
import com.ocs.indaba.util.FileStorageUtil;
import com.ocs.util.StringUtils;
import com.ocs.indaba.util.UserSessionManager;
import com.ocs.indaba.vo.CaseStatus;
import com.ocs.indaba.vo.FilterForm;
import com.ocs.indaba.vo.LoginUser;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is a base action.
 *
 * @author Jeff
 *
 */
public abstract class BaseAction extends Action {

    private static final Logger logger = Logger.getLogger(BaseAction.class);
    protected static final String COOKIE_LAST_LOGIN_TIME = "llt";
    protected static final String PARAM_TARGET_ID = "targetId";
    protected static final String PARAM_PRODUCT_ID = "productId";
    protected static final String PARAM_CTAG_ID = "cTagId";
    protected static final String PARAM_ROLE_ID = "roleId";
    protected static final String PARAM_TEAM_ID = "teamId";
    protected static final String PARAM_GOAL_ID = "goalid";
    protected static final String PARAM_HORSE_ID = "horseid";
    protected static final String PARAM_STATUS = "status";
    protected static final String PARAM_HAS_CASE = "hasCase";
    protected static final String PARAM_CASE_STATUS = "caseStatus";
    protected static final String PARAM_TARGET_SELECTION_TYPE = "targetSelectionType";
    protected static final String PARAM_PRODUCT_SELECTION_TYPE = "prodSelectionType";
    protected static final String PARAM_ROLE_SELECTION_TYPE = "roleSelectionType";
    protected static final String PARAM_TEAM_SELECTION_TYPE = "teamSelectionType";
    protected static final String PARAM_CTAG_SELECTION_TYPE = "cTagSelectionType";
    protected static final String PARAM_ASSIGNID = "assignid";
    protected static final String PARAM_TOOL_ID = "toolid";
    protected static final String PARAM_SUPER_EDIT = "superedit";
    protected static final String PARAM_ANSWER_ID = "answerid";
    protected static final String PARAM_QUESTION_ID = "questionid";
    protected static final String PARAM_FILE_DESC = "filedesc";
    protected static final String PARAM_CONTENT_ID = "cntid";
    protected static final String PARAM_CONTENT_VERSION_ID = "contentVersionId";
    protected static final String PARAM_MESSAGE_TYPE = "msgType";
    protected static final String PARAM_BOX_TYPE = "boxType";
    protected static final String PARAM_INITIAL_FLAG_GROUP_ID = "initialFlagGroupId";

    protected static final String ATTR_INBOX_MESSAGES = "inboxMessages";
    protected static final String ATTR_OUTBOX_MESSAGES = "outboxMessages";
    protected static final String ATTR_PROJECT_MESSAGES = "projectMessages";
    protected static final String ATTR_INBOX_MESSAGE_COUNT = "inboxMsgCount";
    protected static final String ATTR_INBOX_NEW_MESSAGE_COUNT = "inboxNewMsgCount";
    protected static final String ATTR_OUTBOX_MESSAGE_COUNT = "outboxMsgCount";
    protected static final String ATTR_OUTBOX_NEW_MESSAGE_COUNT = "outboxNewMsgCount";
    protected static final String ATTR_PROJECT_MESSAGE_COUNT = "projMsgCount";
    protected static final String ATTR_PROJECT_NEW_MESSAGE_COUNT = "projNewMsgCount";
    protected static final int DEFAULT_PAGE_NUMBER = 1;
    protected static final int DEFAULT_PAGE_SIZE = 10;
    protected static final String FWD_ASSIGNMENT_MSG = "assignMsg";
    protected static final String FWD_LOGIN = "login";
    protected static final String FWD_SUCCESS = "success";
    protected static final String FWD_FAILURE = "failure";
    protected static final String FWD_ERROR = "error";
    protected static final String FWD_YOURCONTENT = "yourcontent";
    protected static final String FWD_ALLCONTENT = "allcontent";
    protected static final String FWD_NOTEBOOK = "notebook";
    protected static final String FWD_HELP = "help";
    protected static final String FWD_CONTENT_GENERAL = "cntgeneral";
    protected static final String FWD_SURVEY = "survey";
    protected static final String FWD_CREATE_NOTEBOOK = "createnb";
    protected static final String FWD_JOURNAL_EDIT = "journalEdit";
    protected static final String FWD_JOURNAL_CREATE = "journalCreate";
    protected static final String FWD_JOURNAL_DISPLAY = "journalDisplay";
    protected static final String FWD_JOURNAL_OVERALLREVIEW = "journalOverallReview";
    protected static final String ATTR_PRODUCTS_BY_PRJ = "prdListByPrj";
    protected static final String ATTR_TARGETS_BY_PRJ = "trgtListByPrj";
    protected static final String ATTR_HORSE_ID = "horseid";
    protected static final String ATTR_WORKFLOW_OBJECT_ID = "woid";
    protected static final String ATTR_PRODUCT_LIST = "prdList";
    protected static final String ATTR_TARGET_LIST = "trgtList";
    protected static final String ATTR_CTAG_LIST = "cTagList";
    protected static final String ATTR_ROLE_LIST = "roleList";
    protected static final String ATTR_TEAM_LIST = "teamList";
    protected static final String ATTR_TARGET = "target";
    protected static final String ATTR_MY_CASES = "myCase";
    protected static final String ATTR_ASSIGNID = "assignid";
    protected static final String ATTR_CASE_STATUS_LIST = "caseStatusList";
    protected static final String ATTR_CONTENT_NAME = "cntName";
    protected static final String ATTR_CONTENT_VERSION_LIST = "contentVersionList";
    protected static final String ATTR_CASES = "cases";
    protected static final String ATTR_MY_ASSIGNED_OF_HORSE_LIST = "myAssignedOfHorse";
    protected static final String ATTR_ALL_ASSIGNED_OF_HORSE_LIST = "allAssignedOfHorse";
    protected static final String ATTR_ALL_ASSIGNED_WITH_GRID = "allAssignedWithGrid";
    protected static final String ATTR_ALL_CASES = "allCases";
    protected static final String ATTR_ACTION = "action";
    protected static final String ATTR_NOTEBOOK = "notebook";
    protected static final String ATTR_ASSIGNMENT_LIST = "assginmentList";
    protected static final String ATTR_ASSIGNMENT_COUNT = "count";
    protected static final String ATTR_NOTEBOOK_MESSAGES = "mbMsgList";
    protected static final String ATTR_NOTEBOOK_MESSAGE_COUNT = "msgCount";
    protected static final String ATTR_RET_URL = "returl";
    protected static final String ATTR_ALERT_TITLE = "alertTitle";
    protected static final String ATTR_TASK = "task";
    protected static final String ATTR_TASKS = "tasks";
    protected static final String ATTR_TASK_TYPE = "tasktype";
    protected static final String ATTR_ANSWER_ID = "answerid";
    protected static final String ATTR_ANSWER_TYPE = "answerType";
    protected static final String ATTR_QUESTION_ID = "questionid";
    protected static final String ATTR_ALERT_FORWARD = "alertForward";
    protected static final String ATTR_ALERT_MSG = "alertMsg";
    protected static final String ATTR_ERRMSG = "errMsg";
    protected static final String ATTR_HAS_EDIT_DEADLINE_RIGHT = "hasEditDeadlineRight";
    protected static final String ATTR_CURRENT_PROJ = "currentProject";

    // Project service
    protected UserService userSrvc = null;
    protected EventService eventService = null;
    protected ProjectService prjService = null;
    protected ProductService prdService = null;
    protected TargetService trgtService = null;
    protected CTagService cTagService = null;
    protected RoleService roleService = null;
    protected TeamService teamService = null;
    protected AccessPermissionService accessPermissionService;
    // Message resources
    protected final static MessageResources msgRsrcs = MessageResources.getMessageResources("ApplicationResources");
    private AssignmentService taskAssignmentService;

    @Autowired
    public void setProjectService(ProjectService prjService) {
        this.prjService = prjService;
    }

    @Autowired
    public void setProductService(ProductService prdService) {
        this.prdService = prdService;
    }

    @Autowired
    public void setTargetService(TargetService trgtService) {
        this.trgtService = trgtService;
    }

    @Autowired
    public void setCTagService(CTagService cTagService) {
        this.cTagService = cTagService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    @Autowired
    public void setTaskAssignmentService(AssignmentService taskAssignmentService) {
        this.taskAssignmentService = taskAssignmentService;
    }

    @Autowired
    public void setUserSrvc(UserService userSrvc) {
        this.userSrvc = userSrvc;
    }

    @Autowired
    public void setAccessPermissionService(
            AccessPermissionService accessPermissionService) {
        this.accessPermissionService = accessPermissionService;
    }

    /**
     * Log the user action/operation event
     *
     * @param userId user id
     * @param eventLogId event log id
     * @param eventType event type(refer to the definition in Constants.java)
     * @param eventData the event data
     * @param arguments the arguments is used to format the event data if any
     */
    protected void logEvent(int userId, int eventLogId, int eventType, String eventData, Object... arguments) {
        Event event = new Event();
        event.setUserId(userId);
        event.setEventLogId(eventLogId);
        event.setEventType((short) eventType);
        if (arguments == null || arguments.length == 0) {
            event.setEventData(eventData);
        } else {
            eventData = MessageFormat.format(eventData, arguments);
        }
        event.setEventData(eventData);

        eventService.addEvent(event);
    }

    /**
     * Log the user action/operation event
     *
     * @param userId user id
     * @param eventLogId event log id
     * @param eventType event type(refer to the definition in Constants.java)
     * @param eventData the event data key
     * @param arguments the arguments is used to format the event data if any
     */
    protected void logEventByKey(int userId, int eventLogId, int eventType, String eventData, Object... arguments) {
        Event event = new Event();
        event.setUserId(userId);
        event.setEventLogId(eventLogId);
        event.setEventType((short) eventType);

        if (arguments == null || arguments.length == 0) {
            event.setEventData(eventData);
        } else {
            eventData = MessageFormat.format(eventData, arguments);
        }
        event.setEventData(eventData);

        eventService.addEvent(event);
    }

    public static int getLanguageId(HttpServletRequest request) {
        String lang = (String) request.getAttribute(Constants.ATTR_LANG);
        int langId = Constants.LANG_EN;

        if (!StringUtils.isEmpty(lang)) {
            langId = Integer.parseInt(lang);
        }

        return langId;
    }

    /**
     * Get resource messages
     *
     * @param key
     * @param arguments
     * @return
     */
    public String getMessage(HttpServletRequest request, String key) {
        return Messages.getInstance().getMessage(key, getLanguageId(request));
    }

    public String getMessage(HttpServletRequest request, String key, Object... arguments) {

        return MessageFormat.format(Messages.getInstance().getMessage(key, getLanguageId(request)), arguments);
    }

    protected LoginUser preprocess(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) {
        return preprocess(mapping, request, response, true, false);

    }

    protected LoginUser preprocess(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, boolean setRetUrl) {
        return preprocess(mapping, request, response, setRetUrl, false);

    }

    protected LoginUser preprocess(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, boolean setRetUrl, boolean withAbsoluteUrlPath) {
        return preprocess(mapping, request, response, setRetUrl, withAbsoluteUrlPath, false);
    }

    protected LoginUser preprocess(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, boolean setRetUrl, boolean withAbsoluteUrlPath, boolean checkExisted) {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException ex) {
            logger.error("Invalid encoding utf-8", ex);
        }
        
        if (setRetUrl) {
            setReturnUrl(request, withAbsoluteUrlPath, checkExisted);
        }
        /*
         * String actionPath = request.getServletPath(); if (actionPath != null)
         * { actionPath = actionPath.replaceAll("/", ""); } else { actionPath =
         * ""; }
         */
        LoginUser loginUser = new LoginUser();
        String token = (String) request.getAttribute(Constants.COOKIE_TOKEN);
        int uid = UserSessionManager.getInstance().getUserId(token);
        User user = userSrvc.getUser(uid);
        String username = user.getUsername();
        String name = user.getFirstName() + " " + user.getLastName();
        int prjid = user.getDefaultProjectId();
        Project project = prjService.getProjectById(prjid);
        String prjName = (project == null) ? "" : project.getCodeName();
        request.setAttribute(Constants.ATTR_NAME, name);
        List<Project> listProj = prjService.getProjects(uid);
        loginUser.setProject(project);
        loginUser.setUid(uid);
        loginUser.setUsername(username);
        loginUser.setToken(token);
        loginUser.setPrjid(prjid);
        loginUser.setName(name);
        loginUser.setSiteAdmin(user.getSiteAdmin());
        loginUser.setPrjName(prjName);
        loginUser.setListProj(listProj);
        loginUser.setLanguageId(user.getLanguageId());
        String lang = Integer.toString(user.getLanguageId());

        //Set lang cookie
        CookieUtils.setCookie(response, Constants.COOKIE_LANGUAGE, lang, Constants.COOKIE_90_DAYS_AGE);
        request.setAttribute(Constants.ATTR_LANG, lang);
        request.setAttribute(Constants.ATTR_USERID, uid);
        request.setAttribute(Constants.ATTR_USERNAME, username);
        request.setAttribute(ATTR_CURRENT_PROJ, project);
        request.setAttribute(Constants.ATTR_PROJECT_LIST, listProj);
        request.setAttribute(Constants.ATTR_PROJECT_NAME, prjName);
        request.setAttribute(Constants.ATTR_PROJECT_ID, prjid);
        request.setAttribute(Constants.ATTR_CONTEXT_PATH, request.getContextPath());
        request.setAttribute(Constants.ATTR_ADMINTOOL_BASEURL, Config.getString(Config.KEY_ADMINTOOL_BASEURL));
        /*
         * String activeTab = (String)
         * request.getAttribute(Constants.COOKIE_ACTIVE_TAB); if
         * (StringUtils.isEmpty(activeTab)) { activeTab =
         * Constants.TAB_YOURCONTENT; CookieUtils.setCookie(response,
         * Constants.COOKIE_ACTIVE_TAB, Constants.TAB_YOURCONTENT); }
         * request.setAttribute(Constants.COOKIE_ACTIVE_TAB, activeTab);
         */
        //uid = (int) request.getAttribute(Constants.ATTR_USERID);
        //username = (String) request.getAttribute(Constants.ATTR_USERNAME);
        //listProj = (List<Project>) request.getAttribute(Constants.ATTR_PROJECT_LIST);
        //prjName = (String) request.getAttribute(Constants.ATTR_PROJECT_NAME);
        //prjid = (int) request.getAttribute(Constants.ATTR_PROJECT_ID);
        //token = (String) request.getAttribute(Constants.COOKIE_TOKEN);
        //contextPath = (String)request.getAttribute(Constants.ATTR_CONTEXT_PATH);
        //adminToolBaseUrl = (String)request.getAttribute(Constants.ATTR_ADMINTOOL_BASEURL);
        return loginUser;
    }

    protected void setReturnUrl(HttpServletRequest request) {
        setReturnUrl(request, true);
    }

    protected void setReturnUrl(HttpServletRequest request, boolean withAbsoluteUrlPath) {
        setReturnUrl(request, withAbsoluteUrlPath, true);
    }


    protected void setRequestUrl(HttpServletRequest request) {
        StringBuffer reqUrlBuf = request.getRequestURL();
        String query = request.getQueryString();
        if (query != null) {
            reqUrlBuf.append('?').append(query);
        }
        String reqUrl = null;
        try {
            reqUrl = URLEncoder.encode(reqUrlBuf.toString(), "UTF-8");
        } catch (Exception ex) {
        }
        logger.debug("Set attribute requrl=" + reqUrl);
        request.setAttribute("requrl", reqUrl);
    }


    protected void setReturnUrl(HttpServletRequest request, boolean withAbsoluteUrlPath, boolean checkExisted) {
        if (checkExisted && request.getAttribute(ATTR_RET_URL) != null) {
            return;
        }
        StringBuffer reqUrlBuf = (withAbsoluteUrlPath) ? request.getRequestURL() : new StringBuffer(request.getServletPath());
        String query = request.getQueryString();
        if (query != null) {
            reqUrlBuf.append('?').append(query);
        }
        String reqUrl = null;
        try {
            reqUrl = URLEncoder.encode(reqUrlBuf.toString(), "UTF-8");
        } catch (Exception ex) {
        }
        logger.debug("Set attribute returl=" + reqUrl);
        request.setAttribute(ATTR_RET_URL, reqUrl);
    }

    protected FilterForm extractFilterFormData(HttpServletRequest request) {
        FilterForm filterForm = new FilterForm();

        boolean targetSelectionInclude = StringUtils.str2boolean(request.getParameter(PARAM_TARGET_SELECTION_TYPE));
        boolean prodSelectionInclude = StringUtils.str2boolean(request.getParameter(PARAM_PRODUCT_SELECTION_TYPE));
        boolean roleSelectionInclude = StringUtils.str2boolean(request.getParameter(PARAM_ROLE_SELECTION_TYPE));
        boolean teamSelectionInclude = StringUtils.str2boolean(request.getParameter(PARAM_TEAM_SELECTION_TYPE));
        filterForm.setTargetSelectionInclude(targetSelectionInclude);
        filterForm.setProdSelectionInclude(prodSelectionInclude);
        filterForm.setRoleSelectionInclude(roleSelectionInclude);
        filterForm.setTeamSelectionInclude(teamSelectionInclude);

        String[] targetIdValues = request.getParameterValues(PARAM_TARGET_ID);
        String[] productIdValues = request.getParameterValues(PARAM_PRODUCT_ID);
        String[] roleIdValues = request.getParameterValues(PARAM_ROLE_ID);
        String[] teamIdValues = request.getParameterValues(PARAM_TEAM_ID);

        // Handle target id list
        List<Target> targets = (List<Target>) request.getAttribute(ATTR_TARGET_LIST);
        List<Integer> targetIds = new ArrayList<Integer>();
        if (targets != null) {
            for (Target t : targets) {
                targetIds.add(t.getId());
            }
            targets.clear();
            targets = null;
            request.removeAttribute(ATTR_TARGET_LIST);
        }

        List<Integer> targetSelectedIds = null;
        if (targetIdValues != null && targetIdValues.length > 0) {
            targetSelectedIds = new ArrayList<Integer>(targetIdValues.length);
            for (String t : targetIdValues) {
                targetSelectedIds.add(StringUtils.str2int(t, Constants.SELECT_ALL));
            }
        }
        filterForm.setTargetIds(analyzeFilterData(targetIds, targetSelectedIds, targetSelectionInclude));

        // Handle product id list
        List<Product> products = (List<Product>) request.getAttribute(ATTR_PRODUCT_LIST);
        List<Integer> productIds = new ArrayList<Integer>();
        if (products != null) {
            for (Product p : products) {
                productIds.add(p.getId());
            }
            products.clear();
            products = null;
            request.removeAttribute(ATTR_PRODUCT_LIST);
        }
        List<Integer> productSelectedIds = null;
        if (productIdValues != null && productIdValues.length > 0) {
            productSelectedIds = new ArrayList<Integer>(productIdValues.length);
            for (String p : productIdValues) {
                productSelectedIds.add(StringUtils.str2int(p, Constants.SELECT_ALL));
            }
        }
        filterForm.setProductIds(analyzeFilterData(productIds, productSelectedIds, prodSelectionInclude));

        // Handle role id list
        List<Role> roles = (List<Role>) request.getAttribute(ATTR_ROLE_LIST);
        List<Integer> roleIds = new ArrayList<Integer>();
        if (roles != null) {
            for (Role r : roles) {
                roleIds.add(r.getId());
            }
            roles.clear();
            roles = null;
            request.removeAttribute(ATTR_ROLE_LIST);
        }
        List<Integer> roleSelectedIds = null;
        if (roleIdValues != null && roleIdValues.length > 0) {
            roleSelectedIds = new ArrayList<Integer>(roleIdValues.length);
            for (String r : roleIdValues) {
                roleSelectedIds.add(StringUtils.str2int(r, Constants.SELECT_ALL));
            }
        }
        filterForm.setRoleIds(analyzeFilterData(roleIds, roleSelectedIds, roleSelectionInclude));

        // Handle team id list
        List<Team> teams = (List<Team>) request.getAttribute(ATTR_TEAM_LIST);
        List<Integer> teamIds = new ArrayList<Integer>();
        if (teams != null) {
            for (Team t : teams) {
                teamIds.add(t.getId());
            }
            teams.clear();
            teams = null;
            request.removeAttribute(ATTR_TEAM_LIST);
        }
        List<Integer> teamSelectedIds = null;
        if (teamIdValues != null && teamIdValues.length > 0) {
            teamSelectedIds = new ArrayList<Integer>(teamIdValues.length);
            for (String t : teamIdValues) {
                teamSelectedIds.add(StringUtils.str2int(t, Constants.SELECT_ALL));
            }
        }
        filterForm.setTeamIds(analyzeFilterData(teamIds, teamSelectedIds, teamSelectionInclude));

        filterForm.setStatus(StringUtils.str2int(request.getParameter(PARAM_STATUS), Constants.SELECT_ALL));

        filterForm.setCaseStatus(StringUtils.str2int(request.getParameter(PARAM_CASE_STATUS), CaseStatus.OPENNEW.getStatusCode()));

        //////////////////
        // Set the request parameters
        request.setAttribute(PARAM_PRODUCT_SELECTION_TYPE, request.getParameter(PARAM_PRODUCT_SELECTION_TYPE));
        request.setAttribute(PARAM_TARGET_SELECTION_TYPE, request.getParameter(PARAM_TARGET_SELECTION_TYPE));
        StringBuffer sBuf = new StringBuffer();
        if (productIdValues != null) {
            for (String v : productIdValues) {
                sBuf.append(v).append(',');
            }
        }
        request.setAttribute(PARAM_PRODUCT_ID, sBuf.toString());

        sBuf = new StringBuffer();
        if (targetIdValues != null) {
            for (String v : targetIdValues) {
                sBuf.append(v).append(',');
            }
        }

        request.setAttribute(PARAM_TARGET_ID, sBuf.toString());
        request.setAttribute(PARAM_STATUS, filterForm.getStatus());
        /////////////////

        return filterForm;
    }

    protected void setFilters(HttpServletRequest request, int projId) {
        int roleId = StringUtils.str2int(request.getParameter(PARAM_ROLE_ID), Constants.PARAM_ALL_ROLES);
        int teamId = StringUtils.str2int(request.getParameter(PARAM_TEAM_ID), Constants.PARAM_ALL_TEAMS);
        int hasCase = StringUtils.str2int(request.getParameter(PARAM_HAS_CASE), Constants.PARAM_ALL_HAS_CASE);
        int status = StringUtils.str2int(request.getParameter(PARAM_STATUS), Constants.PARAM_ALL_STATUSES);

        logger.debug("Get AllContent request: [projectId=" + projId + ", status=" + status + "].");


        request.setAttribute(ATTR_PRODUCT_LIST, prdService.getAllProducts());
        request.setAttribute(ATTR_PRODUCTS_BY_PRJ, prdService.getProductsByProjectId(projId));
        request.setAttribute(ATTR_TARGET_LIST, trgtService.getAllTargets());
        request.setAttribute(ATTR_TARGETS_BY_PRJ, trgtService.getTargetsByProjectId(projId));
        request.setAttribute(ATTR_CTAG_LIST, cTagService.getAllCTags());

        request.setAttribute(PARAM_ROLE_ID, roleId);
        request.setAttribute(ATTR_ROLE_LIST, roleService.getAllRoles(projId));

        request.setAttribute(PARAM_TEAM_ID, teamId);
        request.setAttribute(ATTR_TEAM_LIST, teamService.getAllTeams(projId));

        request.setAttribute(PARAM_HAS_CASE, hasCase);
        request.setAttribute(PARAM_STATUS, status);

        request.setAttribute(ATTR_CASE_STATUS_LIST, CaseStatus.getAllStatus());
    }

    private List<Integer> analyzeFilterData(List<Integer> ids, List<Integer> selectedIds, boolean include) {
        if (ids == null) {
            return null;
        }

        List<Integer> list = null;
        if (selectedIds == null) {
            if (!include) {
                list = new ArrayList<Integer>();
                list.addAll(ids);
            }
        } else {
            list = new ArrayList<Integer>();
            for (int id : ids) {
                //for (int selectedId : selectedIds) {
                if (include && selectedIds.contains(id)) {
                    list.add(id);
                } else if (!include && !selectedIds.contains(id)) {
                    list.add(id);
                }
                //}
            }
        }

        return list;
    }

    /**
     * Extract the upload form files. After extracted, the files and
     * descriptions are stored into fileMap and descMap.
     *
     * @param fileMap - the map in memory which stores the uploaed form files.
     * KEY: filename; VALUE: FormFile object.
     * @param descMap - the map in memory which stores the file descriptions.
     * KEY: filename; VALUE: description.
     * @param form
     * @param request
     * @param horseId
     */
    public void extractMultiUploadFormFiles(Map<String, FormFile> fileMap, Map<String, String> descMap, MultiUploadForm form, HttpServletRequest request, int horseId) {
        List<FormFile> formFiles = form.getFiles();
        if (formFiles != null) {
            String filename = "";
            String fullFilePath = "";
            for (FormFile f : formFiles) {
                filename = f.getFileName();
                if (StringUtils.isEmpty(filename)) {
                    continue;
                }
                fullFilePath = FilePathUtil.getContentAttachmentPath(horseId, filename);
                try {
                    FileStorageUtil.store(f, fullFilePath);
                    if (!StringUtils.isEmpty(fullFilePath)) {
                        fileMap.put(filename, f);
                        descMap.put(filename, request.getParameter(filename));
                    }
                    logger.debug("Success to save uploaded file. [contentType=" + f.getContentType()
                            + ", fileName=" + filename + ", fileSize=" + f.getFileSize() + ", filePath=" + fullFilePath + "].");
                } catch (Exception ex) {
                    logger.error("Fail to store uploaded file: " + filename, ex);
                }
            }
        }
    }

    /**
     * Extract single file
     *
     * @param form
     * @param request
     * @param horseId
     * @return Map of extracted form file. KEY: FormFile object; VALUE: file
     * description string
     */
    public FormFile extractSingleUploadFormFiles(SingleUploadForm form, String storedFilename, int horseId) {
        FormFile formFile = form.getFile();
        if (formFile == null) {
            return null;
        }
        String filename = "";
        String fullFilePath = null;
        filename = formFile.getFileName();
        if (StringUtils.isEmpty(filename)) {
            return null;
        }
        /*
         * try { filename = new String(filename.getBytes("ISO-8859-1"),
         * "UTF-8"); } catch (Exception e) { logger.debug(e); }
         */
        fullFilePath = FilePathUtil.getContentAttachmentPath(horseId, storedFilename);
        try {
            FileStorageUtil.store(formFile, fullFilePath);
            logger.debug("Success to save uploaded file. [contentType=" + formFile.getContentType()
                    + ", fileName=" + filename + ", storedFilename=" + storedFilename + ", fileSize=" + formFile.getFileSize() + ", filePath=" + fullFilePath + "].");
        } catch (Exception ex) {
            logger.error("Fail to store uploaded file: " + filename, ex);
        }
        return formFile;
    }

    public ActionForward checkAssignmentStatus(ActionMapping mapping, HttpServletRequest request) {
        int userId = (Integer) request.getAttribute(Constants.ATTR_USERID);
        int taskAssignmentId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
        if (taskAssignmentId != 0) {
            String retMessage = taskAssignmentService.checkAssignmentStatus(userId, taskAssignmentId);
            if (!retMessage.equals("OK")) {
                request.setAttribute("alertInfo", retMessage);
                return mapping.findForward(FWD_ASSIGNMENT_MSG);
            }
        }
        return null;
    }

    public ActionForward setAssignmentCompleteMessage(ActionMapping mapping, HttpServletRequest request) {
        String retMessage = null;
        request.setAttribute("alertInfo", retMessage);
        return mapping.findForward(FWD_ASSIGNMENT_MSG);
    }

//    public void writeMsg(HttpServletResponse response, String msg) throws IOException {
//        this.writeMsgWithCharset(response, msg, null);
//    }
    public void writeMsgUTF8(HttpServletResponse response, String msg) throws IOException {
        this.writeMsgWithCharset(response, msg, Constants.CHARSET_UTF8);
    }

    public void writeMsgWithCharset(HttpServletResponse response, String msg, String charset) throws IOException {
        String contentType = "text/html";
        if (charset != null) {
            contentType += ";charset=" + charset;
        }
        response.setContentType(contentType);
        PrintWriter writer = response.getWriter();
        writer.print(msg);
        writer.flush();
        writer.close();
    }

//    public void writeMsg(HttpServletResponse response, long val) throws IOException {
//        response.setContentType("text/html");
//        PrintWriter writer = response.getWriter();
//        writer.print(val);
//        writer.flush();
//        writer.close();
//    }
//
//    public void writeMsgLn(HttpServletResponse response, String msg) throws IOException {
//        response.setContentType("text/html");
//        PrintWriter writer = response.getWriter();
//        writer.println(msg);
//        writer.flush();
//        writer.close();
//    }
    public void writeMsgLnWithCharset(HttpServletResponse response, String msg, boolean close, String charset) throws IOException {
        String contentType = "text/html";
        if (charset != null) {
            contentType += ";charset=" + charset;
        }
        response.setContentType(contentType);
        PrintWriter writer = response.getWriter();
        writer.println(msg);
        writer.flush();
        if (close) {
            writer.close();
        }
    }

    public void writeMsgLn(HttpServletResponse response, String msg, boolean close) throws IOException {
        this.writeMsgLnWithCharset(response, msg, close, null);
    }

    public void writeMsgLnUTF8(HttpServletResponse response, String msg) throws IOException {
        this.writeMsgLnWithCharset(response, msg, true, Constants.CHARSET_UTF8);
    }

    public void writeMsgLnUTF8(HttpServletResponse response, String msg, boolean close) throws IOException {
        this.writeMsgLnWithCharset(response, msg, close, Constants.CHARSET_UTF8);
    }

    public void writeMsgLn(HttpServletResponse response, long val) throws IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println(val);
        writer.flush();
        writer.close();
    }

    public void writeMsg(HttpServletResponse response, byte[] bytes) throws IOException {
        response.setContentType("text/html");
        OutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(bytes);
        out.flush();
        out.close();
    }

    protected void writeRespJSON(HttpServletResponse response, JSONObject data) throws IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        Writer writer = null;
        try {
            writer = response.getWriter();
            writer.write(data.toJSONString());
            writer.flush();
        } catch (IOException ex) {
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
            }
        }
    }

    protected void writeRespJSON(HttpServletResponse response, JSONArray data) throws IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        Writer writer = null;
        try {
            writer = response.getWriter();
            writer.write(data.toJSONString());
            writer.flush();
        } catch (IOException ex) {
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
            }
        }
    }

    protected void writeRespJSON(HttpServletResponse response, int result, String data) throws IOException {
        writeRespJSON(response, result, data, "");
    }

    protected void writeRespJSON(HttpServletResponse response, int result, JSONObject data) throws IOException {
        writeRespJSON(response, result, data, "");
    }

    protected void writeRespJSON(HttpServletResponse response, int result, JSONObject data, String desc) throws IOException {
        JSONObject root = new JSONObject();
        root.put("ret", result);
        root.put("desc", desc);
        root.put("data", data);
        writeMsgUTF8(response, root.toJSONString());
    }

    protected void writeRespJSON(HttpServletResponse response, int result, JSONArray data) throws IOException {
        writeRespJSON(response, result, data, "");
    }

    protected void writeRespJSON(HttpServletResponse response, int result, JSONArray data, String desc) throws IOException {
        JSONObject root = new JSONObject();
        root.put("ret", result);
        root.put("desc", desc);
        root.put("data", data);
        writeMsgUTF8(response, root.toJSONString());
    }

    protected void writeRespJSON(HttpServletResponse response, int result, String data, String desc) throws IOException {
        JSONObject root = new JSONObject();
        root.put("ret", result);
        root.put("desc", desc);
        root.put("data", data);
        writeMsgUTF8(response, root.toJSONString());
    }

    protected String digestSurveyAnswerAttachmentFilename(String filename, int horseId) {
        String extension = FilePathUtil.getFileExtention(filename);
        return UUID.randomUUID().toString() + ("".equals(extension) ? "" : "." + extension);
    }

    protected String digestJournalAttachmentFilename(String filename, int horseId) {
        String extension = FilePathUtil.getFileExtention(filename);
        return UUID.randomUUID().toString() + ("".equals(extension) ? "" : "." + extension);
    }

    protected String getActionPath(HttpServletRequest request) {
        String actionPath = request.getServletPath();
        if (actionPath != null) {
            actionPath = actionPath.replaceAll("/", "");
        } else {
            actionPath = "";
        }
        return actionPath;
    }
}
