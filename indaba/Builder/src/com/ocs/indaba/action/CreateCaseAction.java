/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.CaseAttachment;
import com.ocs.indaba.po.Ctags;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.CaseService;
import com.ocs.indaba.service.SiteMessageService;
import com.ocs.indaba.service.UserService;
import com.ocs.indaba.vo.CaseInfo;
import com.ocs.indaba.vo.CaseStatus;
import com.ocs.indaba.vo.LoginUser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
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
public class CreateCaseAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(CreateCaseAction.class);

    private static final String FWD_CREATE_SUCCESS = "createCaseSuccess";
    private static final String ATTR_ALERT_INFO = "alertInfo";
    private static final String SPLIT_FOR_ID = ";";
    private static final String CHECKBOX_SELECT_VALUE = "on";
    private CaseService caseService;
    private SiteMessageService siteMessageService;
    private UserService userService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response);

        String caseTitle = request.getParameter("title");
        String caseDescription = request.getParameter("description");
        String assignUser = request.getParameter("assignUserSelect");
        
        int assignUserId = -1;
        if (assignUser != null) {
            assignUserId = Integer.valueOf(assignUser);
        }
        String attachUserIds = request.getParameter("attachUserIds");
        String attachTagIds = request.getParameter("attachTagIds");
        String attachContentIds = request.getParameter("attachContentIds");
        String blockWorkFlowStr = request.getParameter("blockWorkFlowCheckBox");
        String blockPublishingStr = request.getParameter("blockPublishingCheckBox");
        Boolean blockWorkFlow = false;
        Boolean blockPublishing = false;
        if (blockWorkFlowStr != null && blockWorkFlowStr.equals(CHECKBOX_SELECT_VALUE)) {
            blockWorkFlow = true;
        }
        if (blockPublishingStr != null && blockPublishingStr.equals(CHECKBOX_SELECT_VALUE)) {
            blockPublishing = true;
        }
        Short priorityCode = Short.valueOf(request.getParameter("casePrioritySelect"));

        // attach files
        String[] fileNames = request.getParameterValues("fileNames");
        List<CaseAttachment> caseAttachmentList = new ArrayList<CaseAttachment>();
        CaseAttachment caseAttachment;
        if (fileNames != null && fileNames.length > 0) {
            for (String fileName : fileNames) {
                caseAttachment = new CaseAttachment();
                caseAttachment.setFileName(fileName);
                caseAttachmentList.add(caseAttachment);
            }
        }

        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setTitle(caseTitle);
        caseInfo.setDescription(caseDescription);
        if (assignUser != null) {
            caseInfo.setAssignedUserId(assignUserId);
        }
        caseInfo.setOpenedByUserId(loginUser.getUid());
        caseInfo.setStatusCode(CaseStatus.OPENNEW.getStatusCode());
        caseInfo.setProjectId(loginUser.getPrjid());
        caseInfo.setBlockPulishing(blockPublishing);
        caseInfo.setBlockWorkFlow(blockWorkFlow);
        caseInfo.setPriorityCode(priorityCode);

        List<User> attachUserList = new ArrayList<User>();
        if (attachUserIds != null && attachUserIds.length() > 0) {
            String[] attachUserIdArray = attachUserIds.split(SPLIT_FOR_ID);
            if (attachUserIdArray != null) {
                for (String userIdString : attachUserIdArray) {
                    Long userIdTmp = Long.parseLong(userIdString);
                    attachUserList.add(userService.getUser(userIdTmp.intValue()));
                }
            }
        }
        caseInfo.setAttachUsers(attachUserList);

        List<Ctags> attachTagList = new ArrayList<Ctags>();
        if (attachTagIds != null && attachTagIds.length() > 0) {
            String[] attachTagIdArray = attachTagIds.split(SPLIT_FOR_ID);
            if (attachTagIdArray != null) {
                for (String tagIdString : attachTagIdArray) {
                    Long tagId = Long.parseLong(tagIdString);
                    attachTagList.add(new Ctags(tagId.intValue()));
                }
            }
        }
        caseInfo.setTags(attachTagList);

        //set case attchach content;
        List<Long> attachContentIdList = new ArrayList<Long>();
        if (attachContentIds != null && attachContentIds.length() > 0) {
            String[] attachContentIdArray = attachContentIds.split(SPLIT_FOR_ID);
            if (attachContentIdArray != null) {
                for (String contentIdString : attachContentIdArray) {
                    attachContentIdList.add(Long.parseLong(contentIdString));
                }
            }
        }
        caseInfo.setAttachContentIds(attachContentIdList);

//        if (attachContentIdList.size() > 0) {
//            long contentId = attachContentIdList.get(0);
//            Horse horse = horseService.getHorseByContentHeaderId(contentId);
//            caseInfo.setProductId(horse.getProductId());
//            caseInfo.setHorseId(horse.getId());
//        }
        caseInfo.setAttachFiles(caseAttachmentList);

        Long caseId = caseService.createCase(caseInfo);
        if (caseId == null || caseId <= 0) {
            request.setAttribute(ATTR_ALERT_INFO, getMessage(request, Messages.KEY_COMMON_ALERT_CREATECASE_FAIL));
        } else {
            request.setAttribute(ATTR_ALERT_INFO, getMessage(request, Messages.KEY_COMMON_ALERT_CREATECASE_SUCCESS));

            Map<String, String> parameters;
            if (assignUser != null) {
                parameters = getParameters(userService.getUser(assignUserId), caseInfo, loginUser.getPrjName());
                siteMessageService.deliver(loginUser.getPrjid(), assignUserId, Constants.NOTIFICATION_TYPE_SYS_CASE_ASSIGNED, parameters);
            }

            for (User attachUser : attachUserList) {
                parameters = getParameters(attachUser, caseInfo, loginUser.getPrjName());
                siteMessageService.deliver(loginUser.getPrjid(), attachUser.getId(), Constants.NOTIFICATION_TYPE_SYS_CASE_ATTACHED, parameters);
            }
        }

        return mapping.findForward(FWD_CREATE_SUCCESS);
    }

    private Map getParameters(User user, CaseInfo caseInfo, String prjName) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(Constants.NOTIFICATION_TOKEN_USER_NAME, user.getUsername());
        parameters.put(Constants.NOTIFICATION_TOKEN_CASE_NAME, caseInfo.getTitle());
        parameters.put(Constants.NOTIFICATION_TOKEN_CASE_STATUS, caseInfo.getStatus());
        parameters.put(Constants.NOTIFICATION_TOKEN_CASE_ID, Integer.toString(caseInfo.getCaseId()));
        parameters.put(Constants.NOTIFICATION_TOKEN_PROJECT_NAME, prjName);
        return parameters;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSiteMessageService(SiteMessageService siteMessageService) {
        this.siteMessageService = siteMessageService;
    }

    @Autowired
    public void setCaseService(CaseService caseService) {
        this.caseService = caseService;
    }
}
