/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.service.MessageService;
import com.ocs.indaba.service.UserService;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.MessageForm;
import com.ocs.indaba.vo.MessageVO;
import java.io.BufferedReader;
import java.io.StringReader;
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
public class ReplyMessageAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(ReplyMessageAction.class);
    private static final String SUFFIX_REPLY = "Re: ";
    private static final String SUFFIX_FORWARD = "Fw: ";
    private static final String ATTR_SEND_TYPE = "sendType";
    private static final String ATTR_SENDER = "sender";
    private static final String FWD_REPLYPAGE = "replymessage";
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
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response);

        MessageForm messageForm = (MessageForm) form;
        if (messageForm != null) {
            request.setAttribute(ATTR_SEND_TYPE, messageForm.getSendType());
            final MessageVO message = messageService.getMessage(loginUser.getPrjid(), loginUser.getUid(), messageForm.getMsgId());

            switch (messageForm.getSendType()) {
                case Constants.MESSAGE_SEND_TYPE_REPLY:
                    message.setTitle(SUFFIX_REPLY + message.getTitle());
                    break;
                case Constants.MESSAGE_SEND_TYPE_FORWARD:
                    message.setTitle(SUFFIX_FORWARD + message.getTitle());
                    break;
                case Constants.MESSAGE_SEND_TYPE_NEW:
                default:
                    message.setTitle(message.getTitle());
            }
            // add '>' to each line of the message body
            StringBuilder sb = new StringBuilder("\r\n");
            StringReader sr = new StringReader(message.getBody());
            BufferedReader br = new BufferedReader(sr);
            String s = br.readLine();
            while (s != null) {
                sb.append("> ").append(s).append("\r\n");
                s = br.readLine();
            }
            message.setBody(sb.toString());
            request.setAttribute("message", message);
        }

        request.setAttribute(ATTR_SENDER, userService.getUser(loginUser.getUid()));

        return mapping.findForward(FWD_REPLYPAGE);
    }
}
