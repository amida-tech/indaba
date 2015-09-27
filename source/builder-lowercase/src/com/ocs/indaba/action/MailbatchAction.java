/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.action;

import com.ocs.indaba.service.MailbatchService;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author yc06x
 */
public class MailbatchAction extends BaseAction {

    private MailbatchService mailbatchService = null;

    private static final Logger logger = Logger.getLogger(MailbatchAction.class);


    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        logger.debug("Processing batch mails ...");
        mailbatchService.processPendingMails();
        return null;
    }


    @Autowired
    public void setMailbatchService(MailbatchService mailbatchService) {
        this.mailbatchService = mailbatchService;
    }
}
