package com.ocs.indaba.action;

import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.NotificationItem;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.MessageService;
import com.ocs.indaba.service.NotificationItemService;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

public class ForgetPasswordAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(TargetHoverAction.class);
    private static final String PARAM_USERNAME = "username";
    private MessageService messageService = null;
    private NotificationItemService notificationItemService = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action").trim();
        String receivename = request.getParameter(PARAM_USERNAME).trim();
        User temp = userSrvc.getUser(receivename);
        if (temp == null) {
            out.write(getMessage(request, Messages.KEY_COMMON_ERR_INVALIDUSER));
            return null;
        }
        if (action.equals("2")) {
            String password = temp.getPassword();
            String mialAdd = temp.getEmail();

            NotificationItem notificationItem = notificationItemService.getNotificationItemByNameAndLanguage("Sys - User Password", getLanguageId(request));
            String body = notificationItem.getBodyText().replace("<username>", receivename);
            body = body.replace("<password>", password);
            out.write(messageService.sendSysMails(receivename, notificationItem.getSubjectText(), body, mialAdd));
        } else if (action.equals("1")) {
            out.write("validusername");
        }
        return null;
    }

    @Autowired
    public void setNotificationItemService(NotificationItemService notificationItemService) {
        this.notificationItemService = notificationItemService;
    }


    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
}
