/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.service;

import com.ocs.indaba.dao.ContentPaymentDAO;
import com.ocs.indaba.po.ContentPayment;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeanbone
 */
public class ContentPaymentService {
    private ContentPaymentDAO contentPaymentDao;

    public ContentPayment addContentPayment(ContentPayment contentPayment) {
        return contentPaymentDao.insertContentPayment(contentPayment);
    }

    public ContentPayment getContentPayment(int horseId, int userId, int taskAssignmentId) {
        return contentPaymentDao.selectContentPayment(horseId, userId, taskAssignmentId);
    }

    @Autowired
    public void setContentPaymentDao(ContentPaymentDAO contentPaymentDao) {
        this.contentPaymentDao = contentPaymentDao;
    }
}
