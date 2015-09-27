/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.service;

import com.ocs.indaba.aggregation.vo.JournalContentObjectInfo;
import com.ocs.indaba.aggregation.vo.JournalReview;
import com.ocs.indaba.builder.dao.JounalDataDAO;
import com.ocs.indaba.dao.AttachmentDAO;
import com.ocs.indaba.dao.HorseDAO;
import com.ocs.indaba.po.Attachment;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jiangjeff
 */
public class JournalObjectService {

    private static final Logger log = Logger.getLogger(HorseDAO.class);
    private JounalDataDAO jounalDataDao = null;
    private AttachmentDAO attachmentDao = null;

    public JournalContentObjectInfo getJournalInfoByHorseId(int horseId) {
        return jounalDataDao.selectJournalInfoByHorseId(horseId);
    }

    public String getAuthorNamebyHorseId(int horseId) {
        String name = jounalDataDao.getAuthorNamebyHorseId(horseId);
        if(name ==null)
            name = "author not found";
        return name;
    }

    public List<JournalReview> getJournalPeerReviewByContentObjectId(int contentObjectId) {
        return jounalDataDao.selectJournalPeerReviewByContentObjectId(contentObjectId);
    }

    public List<Attachment> getAttachmentsByHorseId(int horseId) {
        return attachmentDao.selectAttachmentsByHorseId(horseId);
    }

    @Autowired
    public void setAttachmentDAO(AttachmentDAO attachmentDao) {
        this.attachmentDao = attachmentDao;
    }

    @Autowired
    public void setJounalDataDao(JounalDataDAO jounalDataDao) {
        this.jounalDataDao = jounalDataDao;
    }
}
