/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.action;

import com.ocs.common.Config;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.service.MessageService;
import com.ocs.indaba.util.CookieUtils;
import com.ocs.indaba.util.Page;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.MessageForm;
import com.ocs.indaba.vo.MessageVO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Tiger Tang
 */
public class MessagesAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(MessagesAction.class);
    private static final String FWD_MESSAGES = "messages";
    protected static final String ATTR_PROJECT_UPDATES_PAGE_NUMBER = "pupn";
    protected static final String ATTR_PROJECT_UPDATES_PAGE_SIZE = "pups";
    protected static final String ATTR_INBOX_PAGE_NUMBER = "inpn";
    protected static final String ATTR_INBOX_PAGE_SIZE = "inps";
    protected static final String ATTR_OUTBOX_PAGE_NUMBER = "outpn";
    protected static final String ATTR_OUTBOX_PAGE_SIZE = "outps";
    private MessageService messageService;

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LoginUser loginUser = preprocess(mapping, request, response);

        request.setAttribute(Constants.COOKIE_ACTIVE_TAB, Constants.TAB_MESSAGING);
        CookieUtils.setCookie(response, Constants.COOKIE_ACTIVE_TAB, Constants.TAB_MESSAGING);

        MessageForm messageForm = (MessageForm) form;

        int inboxPageNum = Constants.INVALID_INT_ID;
        int inboxPageSize = Constants.INVALID_INT_ID;
        int outboxPageNum = Constants.INVALID_INT_ID;
        int outboxPageSize = Constants.INVALID_INT_ID;
        int projUpdatesPageNum = Constants.INVALID_INT_ID;
        int projUpdatesPageSize = Constants.INVALID_INT_ID;

        if (messageForm != null) {
            inboxPageNum = messageForm.getInpn();
            inboxPageSize = messageForm.getInps();

            outboxPageNum = messageForm.getOutpn();
            outboxPageSize = messageForm.getOutps();

            projUpdatesPageNum = messageForm.getPupn();
            projUpdatesPageSize = messageForm.getPups();
        }
        logger.debug("MESSAGE TYPE=" + messageForm.getMsgType());
        request.setAttribute(PARAM_MESSAGE_TYPE, messageForm.getMsgType());

        if (inboxPageNum <= 0) {
            inboxPageNum = Page.DEFAULT_PAGE_NUMBER;
        }
        request.setAttribute(ATTR_INBOX_PAGE_NUMBER, inboxPageNum);

        if (inboxPageSize <= 0) {
            inboxPageSize = Config.getInt(Config.KEY_MESSAGE_INBOX_PAGESIZE);
        }
        request.setAttribute(ATTR_INBOX_PAGE_SIZE, inboxPageSize);

        if (outboxPageNum <= 0) {
            outboxPageNum = Page.DEFAULT_PAGE_NUMBER;
        }

        request.setAttribute(ATTR_OUTBOX_PAGE_NUMBER, outboxPageNum);

        if (outboxPageSize <= 0) {
            outboxPageSize = Config.getInt(Config.KEY_MESSAGE_INBOX_PAGESIZE);
        }
        request.setAttribute(ATTR_OUTBOX_PAGE_SIZE, outboxPageSize);

        if (projUpdatesPageNum <= 0) {
            projUpdatesPageNum = Page.DEFAULT_PAGE_NUMBER;
        }

        request.setAttribute(ATTR_PROJECT_UPDATES_PAGE_NUMBER, projUpdatesPageNum);

        if (projUpdatesPageSize <= 0) {
            projUpdatesPageSize = Config.getInt(Config.KEY_MESSAGE_INBOX_PAGESIZE);
        }

        request.setAttribute(ATTR_PROJECT_UPDATES_PAGE_SIZE, inboxPageSize);

        int userId = loginUser.getUid();
        int projId = loginUser.getPrjid();
        final Page<MessageVO> inboxMessages = messageService.getInboxMessagesOfUserMsgboard(projId, userId, inboxPageNum, inboxPageSize);
        request.setAttribute(ATTR_INBOX_NEW_MESSAGE_COUNT, messageService.getInboxNewMessageCountByUserId(userId));
        request.setAttribute(ATTR_INBOX_MESSAGE_COUNT, messageService.getInboxMessageCountByUserId(userId));
        request.setAttribute(ATTR_INBOX_MESSAGES, inboxMessages);

        final Page<MessageVO> outboxMessages = messageService.getOutboxMessagesByUserId(projId, userId, outboxPageNum, outboxPageSize);
        request.setAttribute(ATTR_OUTBOX_NEW_MESSAGE_COUNT, messageService.getOutboxNewMessageCountOfUser(userId));
        request.setAttribute(ATTR_OUTBOX_MESSAGE_COUNT, messageService.getOutboxMessageCountByUserId(userId));
        request.setAttribute(ATTR_OUTBOX_MESSAGES, outboxMessages);

        final Page<MessageVO> myProjectMessages = messageService.getMessagesOfProjectMsgboard(projId, userId, projUpdatesPageNum, projUpdatesPageSize);
        request.setAttribute(ATTR_PROJECT_NEW_MESSAGE_COUNT, messageService.getNewMessageCountOfProject(userId, projId));
        request.setAttribute(ATTR_PROJECT_MESSAGE_COUNT, messageService.getMessageCountOfProject(projId));
        request.setAttribute(ATTR_PROJECT_MESSAGES, myProjectMessages);

        logger.debug("Messaging request: [userId=" + userId + "].");
        return mapping.findForward(FWD_MESSAGES);
    }
}
