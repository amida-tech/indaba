/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.service.MessageService;
import com.ocs.indaba.service.UserService;
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
public class MessageDetailAction extends BaseAction {

    private static final String ATTR_MESSAGE = "message";
    private static final String FWD_MESSAGEDETAIL = "messageDetail";
    private static final Logger logger = Logger.getLogger(MessageDetailAction.class);
    private UserService userService;
    private MessageService messageService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LoginUser loginUser = preprocess(mapping, request, response);

        MessageForm msgForm = (MessageForm) form;
        int messageId = msgForm.getMsgId();
        Short boxType = msgForm.getBoxType();

        if (boxType == null) boxType = 1;  // default box type is INBOX
        
        request.setAttribute(PARAM_MESSAGE_TYPE, msgForm.getMsgType());
        request.setAttribute(PARAM_BOX_TYPE, boxType);
        
        logger.info("Get Message Detail request: [userid=" + loginUser.getUid() + ", messageid=" + messageId + ", msgType=" + msgForm.getMsgType() + ", boxType=" + msgForm.getBoxType() + ".");
        final MessageVO msg = messageService.viewMessageDetail(loginUser.getPrjid(), loginUser.getUid(), messageId);
        request.setAttribute(ATTR_MESSAGE, msg);
        
        if (boxType == 2) {
            // OUTBOX
            request.setAttribute("prevMsgId", messageService.getPreviousOutMsgId(msg.getDelegate(), loginUser.getUid()));
            request.setAttribute("nextMsgId", messageService.getNextOutMsgId(msg.getDelegate(), loginUser.getUid()));
        } else {
            // INBOX
            request.setAttribute("prevMsgId", messageService.getPreviousMsgId(msg.getDelegate(), msg.getMsgboardId()));
            request.setAttribute("nextMsgId", messageService.getNextMsgId(msg.getDelegate(), msg.getMsgboardId()));
        }

        if (msg.isIsToUser()) {
            request.setAttribute("receiver", userService.getUser(msg.getToUser().getUserId()));
        } else {
            request.setAttribute("receiver", userService.getUser(loginUser.getUid()));
        }
 
        return mapping.findForward(FWD_MESSAGEDETAIL);
    }
}
