/**
 * 
 */
package com.ocs.indaba.action;

import com.ocs.indaba.po.Message;
import com.ocs.indaba.service.MessageService;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.MessageForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Tiger Tang
 *
 */
public class CreateMessageAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(CreateMessageAction.class);
    private MessageService messageService;
    
    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        LoginUser loginUser = preprocess(mapping, request, response);

        logger.debug("CreateMessageAction - Write new message request: [userid=" + loginUser.getUid() + "].");

        MessageForm messageForm = (MessageForm) form;
        if (messageForm != null) {
            Message message = new Message();
            //try {
                message.setTitle(messageForm.getTitle());
                message.setBody(messageForm.getBody());

                messageService.newMessage(loginUser.getUid(), message, loginUser.getPrjid(), messageForm.getReceiverIds(), messageForm.getRoleIds(), messageForm.getTeamIds(), messageForm.isSendToProjectMsgboard());
            //} catch (Exception e) {}
        }
        
        return mapping.findForward(FWD_SUCCESS);
    }
	
}
