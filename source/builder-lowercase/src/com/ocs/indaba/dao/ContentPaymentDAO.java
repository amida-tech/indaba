/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.ContentPayment;
import java.math.BigDecimal;

/**
 *
 * @author Jeanbone
 */
public class ContentPaymentDAO extends SmartDaoMySqlImpl<ContentPayment, Integer> {
    public ContentPayment insertContentPayment(ContentPayment contentPayment) {
        ContentPayment queryContentPayment = super.findSingle(
                "SELECT * FROM indaba.content_payment WHERE content_header_id = ? AND paid_by_user_id = ?",
                contentPayment.getContentHeaderId(), contentPayment.getPaidByUserId());

        if (queryContentPayment != null) {
            contentPayment.setId(queryContentPayment.getId());
//            contentPayment.setAmount(contentPayment.getAmount().add(queryContentPayment.getAmount()));
            return super.update(contentPayment);
        } else {
            return super.create(contentPayment);
        }
    }

    public ContentPayment selectContentPayment(int horseId, int userId, int assignId) {
        String queryStr = "SELECT * FROM content_payment cp " +
                "JOIN content_header ch ON (ch.horse_id = ? AND ch.id = cp.content_header_id) " +
                "WHERE cp.paid_by_user_id = ? AND cp.task_assignment_id = ?";

        return super.findSingle(queryStr, horseId, userId, assignId);
    }
}
