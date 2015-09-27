/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.ContentApproval;

/**
 *
 * @author Jeanbone
 */
public class ContentApprovalDAO extends SmartDaoMySqlImpl<ContentApproval, Integer> {

    public ContentApproval insertContentApproval(ContentApproval contentApproval) {
        ContentApproval queryContentApproval = super.findSingle(
                "SELECT * FROM indaba.content_approval WHERE content_header_id = ?",
                contentApproval.getContentHeaderId());

        if (queryContentApproval != null) {
            contentApproval.setId(queryContentApproval.getId());
            return super.update(contentApproval);
        } else {
            return super.create(contentApproval);
        }
    }

    public ContentApproval selectContentApproval(int horseId, int userId, int assignId) {
        String queryStr = "SELECT * FROM content_approval ca " +
                "JOIN content_header ch ON (ch.horse_id = ? AND ch.id = ca.content_header_id) " +
                "WHERE ca.user_id = ? AND ca.task_assignment_id = ?";

        return super.findSingle(queryStr, horseId, userId, assignId);
    }
}
