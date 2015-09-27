/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.ErrorCode;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.*;
import com.ocs.indaba.service.*;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.UserfinderVO;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Login Action. To help handle the user login request. <br/>
 *
 * Actually, although struts already provides an useful LoginForm component, in
 * this example we don't plan to leverage it. In a general use, we directly get
 * the request parameters fromHttpServletRequest.
 *
 * @author Jeff
 *
 */
public class FireUserFinderAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(FireUserFinderAction.class);
    private static final String PARAM_USERFINDER_ID = "userfinderId";
    private static final String PARAM_RESP_FORMAT = "format";
    private static final String RESP_FORMAT_JSON = "json";
    private static final String JSON_KEY_RET = "ret";
    private static final String JSON_KEY_TOTAL = "total";
    private static final String JSON_KEY_NEW = "newadd";
    private static final String TMPL_FULL_NAME = "fullName";
    private static final String TMPL_PROJECT_NAME = "projectName";
    private static final int OPENED_BY_USER_ID = 0;
    private CaseService caseSrvc = null;
    private UserFinderService userfinderSrvc = null;
    private TaskService taskSrvc = null;
    private HorseService horseSrvc = null;
    private SiteMessageService siteMessageService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int userfinderId = StringUtils.str2int(request.getParameter(PARAM_USERFINDER_ID), Constants.INVALID_INT_ID);
        String respFormat = request.getParameter(PARAM_RESP_FORMAT);
        logger.debug("Fire user finder [id=" + userfinderId + "].");
        int newCount = 0;
        int totalCount = 0;
        int ret = ErrorCode.OK;
        try {
            List<UserfinderVO> userfinders = userfinderSrvc.getAllUserfindersByStatus(userfinderId, Constants.USERFINDER_STATUS_ACTIVE);
            if (userfinders != null && !userfinders.isEmpty()) {
                for (UserfinderVO userfinder : userfinders) {
                    logger.debug("Execute userfinder: " + userfinder.getId());
                    List<Integer> users = taskSrvc.getAllCompletedUsersProjectProductAndRole(
                            userfinder.getProjectId(),
                            userfinder.getProductId(),
                            userfinder.getRoleId());

                    if (users == null || users.isEmpty()) {
                        continue;
                    }
                    for (int userId : users) {
                        TaskAssignment taskAssignment = taskSrvc.getLastCompletedTaskByUserAndProjectAndProductAndRole(
                                userfinder.getProjectId(),
                                userfinder.getProductId(),
                                userfinder.getRoleId(),
                                userId);

                        ++totalCount;
                        UserfinderEvent ufe = userfinderSrvc.getUserFinderEvent(
                                userfinder.getId(),
                                taskAssignment.getAssignedUserId(),
                                taskAssignment.getCompletionTime());
                        
                        if (ufe == null) {
                            logger.debug("Add userfinder_event for user: " + taskAssignment.getAssignedUserId());
                            Cases caze = addCaseByTaskAssignment(userfinder, taskAssignment);
                            ufe = new UserfinderEvent();
                            ufe.setUserfinderId(userfinder.getId());
                            ufe.setUserId(taskAssignment.getAssignedUserId());
                            ufe.setCasesId(caze.getId());
                            ufe.setExeTime(new Date());
                            userfinderSrvc.addUserFinderEvent(ufe);
                            ++newCount;

                            User user = userSrvc.getUser(userfinder.getAssignedUserId());
                            Project project = prjService.getProjectById(userfinder.getProjectId());
                            Map<String, String> params = new HashMap<String, String>();
                            params.put(Constants.NOTIFICATION_TOKEN_CASE_NAME, caze.getTitle());
                            params.put(Constants.NOTIFICATION_TOKEN_CASE_STATUS, String.valueOf(caze.getStatus()));
                            params.put(Constants.NOTIFICATION_TOKEN_CASE_ID, Integer.toString(caze.getId()));
                            params.put(Constants.NOTIFICATION_TOKEN_PROJECT_NAME, project.getCodeName());
                            params.put(Constants.NOTIFICATION_TOKEN_USER_NAME, user.getUsername());
                            siteMessageService.deliver(project.getId(), userfinder.getAssignedUserId(), Constants.NOTIFICATION_TYPE_SYS_CASE_ASSIGNED, params);

                            if (userfinder.isAttachUser()) {
                                CaseObject caseObject = new CaseObject();
                                caseObject.setCasesId(ufe.getCasesId());
                                caseObject.setObjectType(Constants.CASE_OBJECT_TYPE_USER);
                                caseObject.setObjectId(taskAssignment.getAssignedUserId());
                                if (!caseSrvc.checkCaseObjectExists(caseObject)) {
                                    logger.debug("Add attach user.");
                                    caseSrvc.addCaseObject(caseObject);
                                }
                                user = userSrvc.getUser(taskAssignment.getAssignedUserId());
                                params.put(Constants.NOTIFICATION_TOKEN_USER_NAME, user.getUsername());
                                siteMessageService.deliver(userfinder.getProjectId(), taskAssignment.getAssignedUserId(), Constants.NOTIFICATION_TYPE_SYS_CASE_ATTACHED, params);
                            }

                            if (userfinder.isAttachContent()) {
                                List<Integer> horseIds = taskSrvc.getCompletedTasksByProjectAndProductAndRoleAndUserId(
                                        userfinder.getProjectId(),
                                        userfinder.getProductId(),
                                        userfinder.getRoleId(), 
                                        userId);
                                
                                if (horseIds != null && !horseIds.isEmpty()) {
                                    CaseObject caseObject = new CaseObject();
                                    caseObject.setCasesId(ufe.getCasesId());
                                    caseObject.setObjectType(Constants.CASE_OBJECT_TYPE_CONTENT);
                                    for (int horseId : horseIds) {
                                        ContentHeader ch = horseSrvc.getContentHeaderByHorseId(horseId);
                                        caseObject.setObjectId(ch.getId());
                                        if (!caseSrvc.checkCaseObjectExists(caseObject)) {
                                            logger.debug("Add attach content.");
                                            caseSrvc.addCaseObject(caseObject);
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        } catch (Exception ex) {
            ret = ErrorCode.ERR_UNKNOWN;
            logger.error("Error occurs.", ex);
        }

        String resp = "";
        if (RESP_FORMAT_JSON.equals(respFormat)) {
            JSONObject root = new JSONObject();
            root.put(JSON_KEY_RET, ret);
            root.put(JSON_KEY_TOTAL, totalCount);
            root.put(JSON_KEY_NEW, newCount);
            resp = root.toJSONString();
        } else {
            resp = "[" + ((ret == ErrorCode.OK)
                    ? getMessage(request, Messages.KEY_COMMON_ALERT_FIREUSERFINDER_SUCCESS) : getMessage(request, Messages.KEY_COMMON_ALERT_FIREUSERFINDER_FAIL))
                    + "] " + MessageFormat.format("Total: {0, number, #}, New: {1, number, #}", totalCount, newCount);
        }
        super.writeMsgUTF8(response, resp);
        return null;
    }

    private Cases addCaseByTaskAssignment(UserfinderVO userfinder, TaskAssignment assignment) {
        Cases caze = new Cases();
        caze.setAssignedUserId(userfinder.getAssignedUserId());

        Date now = new Date();
        caze.setLastUpdatedTime(now);
        caze.setOpenedByUserId(OPENED_BY_USER_ID);
        caze.setPriority(userfinder.getCasePriority());
        caze.setProjectId(userfinder.getProjectId());
        caze.setStatus(Constants.CASE_STATUS_OPEN);
        caze.setSubstatus(Constants.CASE_SUB_STATUS_ASSIGNED);
        caze.setBlockPublishing(false);
        caze.setBlockWorkflow(false);
        //
        // Leave product_id, horse_id as NULL
        //caze.setHorseId(assignment.getHorseId());
        //caze.setProductId(horse.getProductId());
        //caze.setUserMsgboardId(user.getMsgboardId());
        //caze.setGoalId(goalObject.getGoalId());
        //caze.setOpenedByUserId(assignment.getAssignedUserId());
        caze.setOpenedTime(now);
        User user = userSrvc.getUser(assignment.getAssignedUserId());
        Project project = prjService.getProjectById(userfinder.getProjectId());
        caze.setTitle(preHandleTokens(userfinder.getCaseSubject(), user, project));
        caze.setDescription(preHandleTokens(userfinder.getCaseBody(), user, project));
        caze = caseSrvc.addCase(caze);
        return caze;
    }

    private String preHandleTokens(String s, User user, Project project) {
        String userProfileLink = user.getFirstName() + " " + user.getLastName();
        String projectName = project.getCodeName();
        s = substitutToken(s, TMPL_FULL_NAME, userProfileLink);
        return substitutToken(s, TMPL_PROJECT_NAME, projectName);
    }

    private String substitutToken(String origin, String tokenName, String value) {
        return origin.replaceAll("\\[" + tokenName + "\\]", value);
    }

    @Autowired
    public void setUserFinderSrvc(UserFinderService userfinderSrvc) {
        this.userfinderSrvc = userfinderSrvc;
    }

    @Autowired
    public void setTaskSrvc(TaskService taskSrvc) {
        this.taskSrvc = taskSrvc;
    }

    @Autowired
    public void setCaseSrvc(CaseService caseSrvc) {
        this.caseSrvc = caseSrvc;
    }

    @Autowired
    public void setHorseSrvc(HorseService horseSrvc) {
        this.horseSrvc = horseSrvc;
    }

    @Autowired
    public void setSiteMessageService(SiteMessageService siteMessageService) {
        this.siteMessageService = siteMessageService;
    }
}
