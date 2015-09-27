/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.common.Config;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.CaseAttachment;
import com.ocs.indaba.po.Cases;
import com.ocs.indaba.po.Ctags;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.CaseService;
import com.ocs.indaba.service.SiteMessageService;
import com.ocs.indaba.service.UserService;
import com.ocs.indaba.util.CookieUtils;
import com.ocs.indaba.vo.CaseInfo;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.ProjectUserView;
import java.io.File;
import java.text.MessageFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author menglong
 */
public class UpdateCaseAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(UpdateCaseAction.class);
    //foward
    private static final String FWD_CREATE_SUCCESS = "updateCaseSuccess";
    private static final String ATTR_ALERT_INFO = "alertInfo";
    private static final String SPLIT_FOR_ID = ";";
    private static final String CHECKBOX_SELECT_VALUE = "on";
    private CaseService caseService;
    private UserService userService;
    private SiteMessageService siteMessageService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response);
        int projectId = loginUser.getPrjid();

        int caseId = Integer.parseInt(request.getParameter("caseid"));
        String caseTitle = request.getParameter("title");
        String caseDescription = request.getParameter("description");
        String assignUser = request.getParameter("assignUserSelect");
        int assignUserId = -1;
        if (assignUser != null) {
            assignUserId = Integer.valueOf(assignUser.trim());
        }
        short statusCode = Short.valueOf(request.getParameter("caseStatusSelect"));
        Short priorityCode = Short.valueOf(request.getParameter("casePrioritySelect"));
        String newAttachUserIds = request.getParameter("attachUserIds");
        String newAttachContentIds = request.getParameter("attachContentIds");
        String attachTagIds = request.getParameter("attachTagIds");
        String blockWorkFlowStr = request.getParameter("blockWorkFlowCheckBox");
        String blockPublishingStr = request.getParameter("blockPublishingCheckBox");
        String deleteFileIds = request.getParameter("deleteFileIds");
        Boolean blockWorkFlow;
        Boolean blockPublishing;

        Cases formerCases = caseService.getCaseById(caseId);
        if (statusCode == 1) {  // closed
            blockWorkFlow = false;
        } else if (blockWorkFlowStr != null) {
            if (blockWorkFlowStr.equals(CHECKBOX_SELECT_VALUE)) {
                blockWorkFlow = true;
            } else {
                blockWorkFlow = false;
            }
        } else {
            blockWorkFlow = false;
        }
        if (statusCode == 1) {  // closed
            blockPublishing = false;
        } else if (blockPublishingStr != null) {
            if (blockPublishingStr.equals(CHECKBOX_SELECT_VALUE)) {
                blockPublishing = true;
            } else {
                blockPublishing = false;
            }
        } else {
            blockPublishing = false;
        }

        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setCaseId(caseId);
        caseInfo.setTitle(caseTitle);
        caseInfo.setDescription(caseDescription);
        if (assignUser != null) {
            caseInfo.setAssignedUserId(assignUserId);
        }
        caseInfo.setStatusCode(statusCode);
        if (statusCode == 0) {
            caseInfo.setStatus("open");
        } else {
            caseInfo.setStatus("closed");
        }
        caseInfo.setPriorityCode(priorityCode);
        caseInfo.setBlockWorkFlow(blockWorkFlow);
        caseInfo.setBlockPulishing(blockPublishing);

        List<User> attachUserList = new ArrayList<User>();
        if (newAttachUserIds == null) {
            caseInfo.setAttachUsers(null);
        } else {
            if (newAttachUserIds != null && newAttachUserIds.trim().length() > 0) {
                String[] attachUserIdArray = newAttachUserIds.split(SPLIT_FOR_ID);

                if (attachUserIdArray != null) {
                    for (String userIdString : attachUserIdArray) {
                        Long userIdTmp = Long.parseLong(userIdString.trim());
                        attachUserList.add(userService.getUser(userIdTmp.intValue()));
                    }
                }
            }
            caseInfo.setAttachUsers(attachUserList);
        }

        //set case attchach content;
        if (newAttachContentIds == null) {
            caseInfo.setAttachContentIds(null);
        } else {
            List<Long> attachContentIdList = new ArrayList<Long>();
            if (newAttachContentIds != null && newAttachContentIds.trim().length() > 0) {
                String[] attachContentIdArray = newAttachContentIds.split(SPLIT_FOR_ID);
                if (attachContentIdArray != null) {
                    for (String contentIdString : attachContentIdArray) {
                        attachContentIdList.add(Long.parseLong(contentIdString.trim()));
                    }
                }
            }
            caseInfo.setAttachContentIds(attachContentIdList);
        }

        //set attach tags
        if (attachTagIds == null) {
            caseInfo.setTags(null);
        } else {
            List<Ctags> attachTagList = new ArrayList<Ctags>();
            if (attachTagIds != null && attachTagIds.trim().length() > 0) {
                String[] attachTagIdArray = attachTagIds.split(SPLIT_FOR_ID);

                if (attachTagIdArray != null) {
                    for (String tagIdString : attachTagIdArray) {
                        Long tagId = Long.parseLong(tagIdString.trim());
                        attachTagList.add(new Ctags(tagId.intValue()));
                    }
                }
            }
            caseInfo.setTags(attachTagList);
        }

        //delete old case attach file;
        if (deleteFileIds != null && deleteFileIds.trim().length() > 0) {
            String[] deleteFileIdsArray = deleteFileIds.split(";");

            if (deleteFileIdsArray != null) {
                for (String deleteFileIdString : deleteFileIdsArray) {
                    caseService.deleteAttachmentsById(Integer.valueOf(deleteFileIdString));
                }
            }
        }

        // attach files
        String[] fileNames = request.getParameterValues("fileNames");
        List<CaseAttachment> caseAttachmentList = new ArrayList<CaseAttachment>();
        if (fileNames != null && fileNames.length > 0) {
            String uploadBase = Config.getString(Config.KEY_STORAGE_UPLOAD_BASE);
            String pathFormat = Config.getString(Config.KEY_STORAGE_PATH_FORMAT);

            CaseAttachment caseAttachment;
            String destPath, srcPath;
            for (String fileName : fileNames) {
                srcPath = caseService.getAttachmentTempPath(loginUser.getUid(), fileName);
                destPath = caseService.getAttachmentFullPath(fileName, caseId);

                File srcFile = new File(srcPath);
                File destFile = new File(destPath);
                destFile.getParentFile().mkdirs();
                if (srcFile.renameTo(destFile)) {
                    caseAttachment = new CaseAttachment();
                    caseAttachment.setCasesId(caseId);
                    caseAttachment.setFileName(fileName);
                    caseAttachment.setFilePath(caseService.getAttachmentPartialPath(fileName, caseId));
                    caseAttachmentList.add(caseAttachment);
                }
            }
        }

        //set new case attach file;
        caseInfo.setAttachFiles(caseAttachmentList);


        boolean isStatusChanged = (formerCases.getStatus() == caseInfo.getStatusCode()) ? false : true;
        HashSet<User> formerUserList = caseService.getCaseUsersByCaseId(caseInfo.getCaseId());
        Integer formerAssignedUserId = caseService.getCaseById(caseId).getAssignedUserId();
        Integer result = caseService.updateCase(caseInfo);

        request.setAttribute(Constants.COOKIE_CASE_ID, caseId);
        CookieUtils.setCookie(response, Constants.COOKIE_CASE_ID, caseId);
        if (result == null || result <= 0) {
            request.setAttribute(ATTR_ALERT_INFO, getMessage(request, Messages.KEY_COMMON_ALERT_UPDATECASE_FAIL));
        } else {
            request.setAttribute(ATTR_ALERT_INFO, getMessage(request, Messages.KEY_COMMON_ALERT_UPDATECASE_SUCCESS));

            // inform new assigned user
            Map<String, String> parameters;
            ProjectUserView projUser = userService.getProjectUserView(loginUser.getPrjid(), assignUserId);
            parameters = getParameters(projUser, caseInfo, loginUser);
            if (formerAssignedUserId != caseInfo.getAssignedUserId()) {
                siteMessageService.deliver(projUser, Constants.NOTIFICATION_TYPE_SYS_CASE_ASSIGNED, parameters);
            } 

            // inform new attached users
            for (User attachUser : attachUserList) {
                if (!formerUserList.contains(attachUser)) {
                    parameters = getParameters(attachUser, caseInfo, loginUser);
                    siteMessageService.deliver(projectId, attachUser.getId(), Constants.NOTIFICATION_TYPE_SYS_CASE_ATTACHED, parameters);
                }
            }

            // inform assigned and attached users
            if (isStatusChanged) {
                if (assignUser != null) {
                    attachUserList.add(userService.getUser(caseInfo.getAssignedUserId()));
                }

                for (User user : attachUserList) {
                    parameters = getParameters(user, caseInfo, loginUser);
                    siteMessageService.deliver(projectId, user.getId(), Constants.NOTIFICATION_TYPE_SYS_CASE_STATUS_CHANGE, parameters);
                }
            }
        }
        return mapping.findForward(FWD_CREATE_SUCCESS);
    }

    private Map getParameters(User user, CaseInfo caseInfo, LoginUser loginUser) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(Constants.NOTIFICATION_TOKEN_USER_NAME, user.getUsername());
        parameters.put(Constants.NOTIFICATION_TOKEN_CASE_NAME, caseInfo.getTitle());
        parameters.put(Constants.NOTIFICATION_TOKEN_CASE_STATUS, caseInfo.getStatus());
        parameters.put(Constants.NOTIFICATION_TOKEN_CASE_ID, Integer.toString(caseInfo.getCaseId()));
        parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_NAME, loginUser.getPrjName());
        return parameters;
    }

    @Autowired
    public void setCaseService(CaseService caseService) {
        this.caseService = caseService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSiteMessageService(SiteMessageService siteMessageService) {
        this.siteMessageService = siteMessageService;
    }
}
