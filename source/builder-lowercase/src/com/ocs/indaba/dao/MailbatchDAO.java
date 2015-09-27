/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Mailbatch;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class MailbatchDAO extends SmartDaoMySqlImpl<Mailbatch, Integer> {

    public static final int MAIL_SENT = 1;
    public static final int MAIL_NOT_SENT = 0;

    private static final String SELECT_PENDING_MAILS =
            "SELECT * FROM mailbatch WHERE is_sent=0 AND send_count < ? ORDER BY last_send_time LIMIT 0, ?";

    private static final String UPDATE_MAIL_SEND =
            "UPDATE mailbatch SET last_send_time=now(), is_sent=?, send_count=? WHERE id=?";


    public List<Mailbatch> selectPendingMails(int sendCountLimit, int maxMails) {
        List<Mailbatch> mails = super.find(SELECT_PENDING_MAILS, (Object)sendCountLimit, (Object)maxMails);
        return mails;
    }


    public void updateMailSendStatus(int mailId, int isSent, int sendCount) {
        super.update(UPDATE_MAIL_SEND, isSent, sendCount, mailId);
    }
}
