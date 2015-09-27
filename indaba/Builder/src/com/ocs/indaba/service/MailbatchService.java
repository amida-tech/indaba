/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.service;

import com.ocs.common.Config;
import com.ocs.indaba.common.ErrorCode;
import com.ocs.indaba.dao.MailbatchDAO;
import com.ocs.indaba.po.Mailbatch;
import com.ocs.indaba.util.SendMail;
import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Iterator;
import java.util.List;
import com.ocs.indaba.common.Constants;

/**
 *
 * @author yc06x
 */
public class MailbatchService {

    private static final Logger logger = Logger.getLogger(MailbatchService.class);

    private MailbatchDAO mailbatchDao = null;


    public void addMail(String fromAddr, String toAddr, String subject, String mailBody) {
        if (Constants.SYSTEM_MODE_DEMO.equals(Config.getString(Config.KEY_SYSTEM_MODE, Constants.SYSTEM_MODE_PROD))) {
            logger.info("System is running in demo mode, skip to send email to " + toAddr + ", subject: " + subject);
            return;
        }
        Mailbatch mail = new Mailbatch();
        mail.setFromEmail(fromAddr);
        mail.setBody(mailBody);
        mail.setToEmail(toAddr);
        mail.setSubject(subject);
        mail.setIsSent(Boolean.FALSE);
        mail.setSendCount(0);
        mail.setCreateTime(new Date());

        mailbatchDao.create(mail);
    }


    public void addSystemMail(String toAddr, String subject, String mailBody) {
        String sysSender = Config.getString(Config.KEY_SMTP_SENDER);
        addMail(sysSender, toAddr, subject, mailBody);
    }
    

    public void processPendingMails() {
        List<Mailbatch> mails = mailbatchDao.selectPendingMails(
                Config.getInt(Config.KEY_MAIL_BATCH_MAX_SEND),
                Config.getInt(Config.KEY_MAIL_BATCH_MAX_SIZE));

        if (mails == null || mails.isEmpty()) {
            // nothing to process
            logger.debug("Nothing to process.");
            return;
        }

        logger.debug("Start to process pending mails...");

        SendMail mailer = new SendMail(
                    Config.getString(Config.KEY_SMTP_HOST), // host
                    Config.getInt(Config.KEY_SMTP_PORT, 25),
                    Config.getString(Config.KEY_SMTP_AUTH),
                    Config.getString(Config.KEY_SMTP_USERNAME), // username
                    Config.getString(Config.KEY_SMTP_PASSWORD));

        if (mailer == null) {
            logger.error("No Mailer available!!!");
            return;
        }

        Iterator<Mailbatch> it = mails.iterator();
        while (it.hasNext()) {
            // try to send it
            Mailbatch mail = it.next();

            int result = mailer.send(mail.getFromEmail(), mail.getToEmail(), mail.getSubject(), mail.getBody());
            int isSent = (result == ErrorCode.OK) ? MailbatchDAO.MAIL_SENT : MailbatchDAO.MAIL_NOT_SENT;

            logger.debug("Mail isSent: " + isSent);

            if (result == ErrorCode.ERR_SEND_EMAIL_CANT_CONNECT) {
                // don't increment send count
                mailbatchDao.updateMailSendStatus(mail.getId(), isSent, mail.getSendCount());
            } else {
                mailbatchDao.updateMailSendStatus(mail.getId(), isSent, mail.getSendCount()+1);
            }
        }

        logger.debug("Finished processing.");
    }


    @Autowired
    public void setMailbatchDao(MailbatchDAO dao) {
        this.mailbatchDao = dao;
    }

}
