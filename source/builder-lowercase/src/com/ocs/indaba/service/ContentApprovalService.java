/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.service;

import com.ocs.indaba.dao.ContentApprovalDAO;
import com.ocs.indaba.po.ContentApproval;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeanbone
 */
public class ContentApprovalService {
    private ContentApprovalDAO contentApprovalDao;

    public ContentApproval addContentApproval(ContentApproval contentApproval) {
        return contentApprovalDao.insertContentApproval(contentApproval);
    }

    public ContentApproval getContentApproval(int horseId, int userId, int taskAssignmentId) {
        return contentApprovalDao.selectContentApproval(horseId, userId, taskAssignmentId);
    }

    @Autowired
    public void setContentApprovalDao(ContentApprovalDAO contentApprovalDao) {
        this.contentApprovalDao = contentApprovalDao;
    }
}
