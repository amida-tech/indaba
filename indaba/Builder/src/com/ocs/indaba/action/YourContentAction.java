/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.common.Config;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.Announcement;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.service.AnnouncementService;
import com.ocs.indaba.service.CaseService;
import com.ocs.indaba.service.MessageService;
import com.ocs.indaba.util.CookieUtils;
import com.ocs.indaba.util.Page;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.MessageVO;
import com.ocs.util.StringUtils;
import java.text.MessageFormat;

/**
 *
 * @author Jeff
 */
public class YourContentAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(YourContentAction.class);
    private static final String ATTR_ANNOUNCEMENT_SIZE = "announcementSize";
    
    private static final String ATTR_LOADER_SPONSORLOGOS_BASEURL = "sponsorLogosBaseUrl";
    private static final String MY_ANNOUNCEMENT = "announcements";   
    private AnnouncementService announcementService = null;
    /*
     * private TaskService taskService = null; private HorseService horseService
     * = null; private ToolService toolService = null;
     */
    private MessageService messageService = null;
    private CaseService caseService;

    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LoginUser loginUser = preprocess(mapping, request, response);

        logger.debug("Get YourContent request: [userId=" + loginUser.getUid() + ", projectId=" + loginUser.getPrjid() + "].");

        request.setAttribute(Constants.COOKIE_ACTIVE_TAB, Constants.TAB_YOURCONTENT);
        CookieUtils.setCookie(response, Constants.COOKIE_ACTIVE_TAB, Constants.TAB_YOURCONTENT);
        
        List<Announcement> announcements = announcementService.getAnnouncementByProject();
        if (announcements != null) {
            for (Announcement announcement : announcements) {
                announcement.setBody(announcement.getBody().replaceAll("\n", "<br/>"));
            }
        }

        logger.debug("Finished announcements");

        String sponsorLogosBaseUrl = MessageFormat.format(Config.getString(Config.KEY_LOADER_SPONSOR_LOGO_BASEURL),
            Config.getString(Config.KEY_ADMINTOOL_BASEURL, Config.DEFAULT_ADMINTOOL_BASEURL));

        request.setAttribute(ATTR_ANNOUNCEMENT_SIZE, announcements.size());
        request.setAttribute(MY_ANNOUNCEMENT, announcements);
        request.setAttribute(ATTR_LOADER_SPONSORLOGOS_BASEURL, sponsorLogosBaseUrl);
        request.setAttribute(ATTR_MY_CASES, caseService.getOpenCaseListByUserIdAndProjectId(loginUser.getUid(), loginUser.getPrjid()));

        logger.debug("Start Inbox msgs count");
        request.setAttribute(ATTR_INBOX_MESSAGE_COUNT, messageService.getInboxMessageCountByUserId(loginUser.getUid()));
        logger.debug("Done Inbox msgs count");

        //request.setAttribute(ATTR_INBOX_NEW_MESSAGE_COUNT, messageService.getInboxNewMessageCountByUserId(loginUser.getUid()));
        logger.debug("Start Inbox msgs");
        final Page<MessageVO> inboxMessages = messageService.getInboxMessagesOfUserMsgboard(loginUser.getPrjid(), loginUser.getUid(), 1, Config.getInt(Config.KEY_MESSAGE_INBOX_SIDEBAR_SIZE));
        request.setAttribute(ATTR_INBOX_MESSAGES, inboxMessages);
        logger.debug("Done Inbox msgs");

        /****
        logger.debug("Start Outbox msgs count");
        request.setAttribute(ATTR_OUTBOX_MESSAGE_COUNT, messageService.getOutboxMessageCountByUserId(loginUser.getUid()));
        logger.debug("Done Outbox msgs count");

        //request.setAttribute(ATTR_OUTBOX_NEW_MESSAGE_COUNT, messageService.getOutboxNewMessageCountOfUser(loginUser.getUid()));
        logger.debug("Start Outbox msgs");
        final Page<MessageVO> outboxMessages = messageService.getOutboxMessagesByUserId(loginUser.getPrjid(), loginUser.getUid(), 1, Config.getInt(Config.KEY_MESSAGE_INBOX_SIDEBAR_SIZE));
        request.setAttribute(ATTR_OUTBOX_MESSAGES, outboxMessages);
        logger.debug("Done Outbox msgs");
        ****/

        logger.debug("Start project msgs count");
        request.setAttribute(ATTR_PROJECT_MESSAGE_COUNT, messageService.getMessageCountOfProject(loginUser.getPrjid()));
        logger.debug("Done project msgs count");

        //request.setAttribute(ATTR_PROJECT_NEW_MESSAGE_COUNT, messageService.getNewMessageCountOfProject(loginUser.getUid(), loginUser.getPrjid()));
        logger.debug("Start project msgs");
        final Page<MessageVO> projectMessages = messageService.getMessagesOfProjectMsgboard(loginUser.getPrjid(), loginUser.getUid(), 1, Config.getInt(Config.KEY_MESSAGE_INBOX_SIDEBAR_SIZE));
        if (projectMessages != null) {
            /*
             * for (MessageVO msg : projectMessages.getThisPageElements()) {
             * final String title = msg.getTitle(); msg.setTitle((title.length()
             * > 45 ? title.substring(0, 40) : title) + "..."); }
             */
            request.setAttribute(ATTR_PROJECT_MESSAGES, projectMessages);
        }
        logger.debug("Done project msgs");

        return mapping.findForward(FWD_YOURCONTENT);
    }

    @Autowired
    public void setAnnouncementService(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setCaseService(CaseService caseService) {
        this.caseService = caseService;
    }
}
