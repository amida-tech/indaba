/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.ContentHeaderDAO;
import com.ocs.indaba.dao.JournalConfigDAO;
import com.ocs.indaba.dao.JournalContentObjectDAO;
import com.ocs.indaba.dao.JournalPeerReviewDAO;
import com.ocs.indaba.po.Attachment;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.JournalConfig;
import com.ocs.indaba.po.JournalContentObject;
import com.ocs.indaba.po.JournalPeerReview;
import com.ocs.indaba.vo.JournalContentView;

import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Luke Shi
 */
public class JournalService {

    private static final Logger logger = Logger.getLogger(JournalService.class);
    private JournalContentObjectDAO journalCntObjDao = null;
    private JournalConfigDAO journalConfigDAO = null;
    private ContentHeaderDAO cntHdrDao = null;
    private JournalPeerReviewDAO journalPeerReviewDao = null;

    public void saveJournal(JournalContentView journalCntView) {
        saveJournal(journalCntView, 0);
    }

    public void saveJournal(JournalContentView journalCntView, int uid) {
        logger.debug("Save Journal.");
        if (journalCntView == null) {
            logger.warn("Can't save journal content view. The object is NULL!");
            return;
        }
        // Save Content Header
        ContentHeader cntHdr = journalCntView.getContentHeader();
        logger.debug("%%%%%%%%%%% Save Content_Header: " + cntHdr);
        if (cntHdr != null) {
            if (cntHdr.getId() == null || cntHdr.getId() == Constants.INVALID_INT_ID) {
                cntHdr = cntHdrDao.createContentHeader(cntHdr);
            } else {
                cntHdr = cntHdrDao.updateContentHeader(cntHdr);
            }
        }

        // Save Journal Content Object

        JournalContentObject cntObj = journalCntView.getJournalContentObject();
        logger.debug("%%%%%%%%%%% Save Content_Objectr: " + cntObj);
        if (cntObj != null) {
            cntObj.setContentHeaderId(cntHdr.getId());
            logger.debug("%%%%%%%%%%% Save Content_Objectr ID:  " + cntObj.getId());
            if (cntObj.getId() == Constants.INVALID_INT_ID) {
                journalCntObjDao.createJournalContentObject(cntObj);
            } else {
                journalCntObjDao.updateJournalContentObject(cntObj);
            }
        }

        List<Attachment> attachments = journalCntView.getAttachments();
        if (attachments != null) {
            for (Attachment attached : attachments) {
                attached.setContentHeaderId(cntHdr.getId());
                if (journalCntObjDao.getAttachmentByName(attached.getName()) != null) {
                    continue;
                }
                if (attached.getUserId() != null && attached.getUserId() == 0 && uid > 0)
                    attached.setUserId(uid);
                if (attached.getId() == Constants.INVALID_INT_ID) {
                    journalCntObjDao.addAttachment(attached);
                } else {
                    journalCntObjDao.updateAttachment(attached);
                }
            }
        }
        journalCntView.setAttachments(journalCntObjDao.getAttachmentsByCntHdrId(cntHdr.getId()));
    }

    /*public void saveJournal(JournalContentObject journal) {
    logger.debug("Save Journal.");
    if (journal.getId() == Constants.INVALID_INT_ID) {
    journalCntObjDao.createJournalContentObject(journal);
    } else {
    journalCntObjDao.updateJournalContentObject(journal);
    }
    }*/
    public JournalContentObject getJournalById(int id) {
        logger.debug("Get journal by id: " + id);
        JournalContentObject journal = journalCntObjDao.selectJournalById(id);
        return journal;
    }

    public JournalContentView getJournalContentById(int cntObjId) {
        logger.debug("Get journal content by id: " + cntObjId);
        JournalContentView journalContent = journalCntObjDao.getJournalContentById(cntObjId);
        // get attachments
        if (journalContent != null) {
            ContentHeader cntHdr = journalContent.getContentHeader();
            if (cntHdr != null) {
                journalContent.setAttachments(journalCntObjDao.getAttachmentsByCntHdrId(cntHdr.getId()));
            }
        }
        return journalContent;
    }

    public JournalContentView getJournalContentByCntObjIdOrHorseId(int cntObjId, int horseId) {
        logger.debug("Get journal content by id " + cntObjId + " or horse id " + horseId);
        JournalContentView journalContent = journalCntObjDao.getJournalContentByCntObjIdOrHorseId(cntObjId, horseId);
        
        // get attachments
        if (journalContent != null) {
            ContentHeader cntHdr = journalContent.getContentHeader();
            if (cntHdr != null) {
                journalContent.setAttachments(journalCntObjDao.getAttachmentsByCntHdrId(cntHdr.getId()));
            }
        }
        return journalContent;
    }

    public List<Attachment> getAttachmentsByCntHdrId(int cntHdrId) {
        return journalCntObjDao.getAttachmentsByCntHdrId(cntHdrId);
    }

    public List<Attachment> getAttachmentsByHorseId(int horseId) {
        return journalCntObjDao.getAttachmentsByHorseId(horseId);
    }

    public JournalContentView getJournalContentByHorseId(int horseId) {
        logger.debug("Get journal content by horse id: " + horseId);
        JournalContentView journalContent = journalCntObjDao.selectJournalContentByHorseId(horseId);
        // get attachments
        if (journalContent != null) {
            ContentHeader cntHdr = journalContent.getContentHeader();
            if (cntHdr != null) {
                journalContent.setAttachments(journalCntObjDao.getAttachmentsByCntHdrId(cntHdr.getId()));
            }
        }
        return journalContent;
    }

    public ContentHeader getContentHeaderById(int cntHdrId) {
        return cntHdrDao.selectContentHeaderById(cntHdrId);
    }

    public ContentHeader getContentHeaderByHorseId(int horseId) {
        return cntHdrDao.selectContentHeaderByHorseId(horseId);
    }

    public List<ContentHeader> getDefaultContentHeader(int userId) {
        return cntHdrDao.selectContentHeaderByAssignedUserId(userId);
    }

    public List<ContentHeader> getDefaultContentHeaderOrderByTitle(int userId) {
        return cntHdrDao.selectContentHeaderByAssignedUserIdOrderByTitle(userId);
    }

    public List<ContentHeader> getAllContentHeader() {
        return cntHdrDao.selectAllContentHeader();
    }

    public List<ContentHeader> getAllStartedContentHeaderOrderByTitle(int projectId) {
        return cntHdrDao.selectAllStartedContentHeaderOrderByTitle(projectId);
    }
    ///////////////////////////////////////////////////
    //
    // Attachment
    //
    ///////////////////////////////////////////////////

    public Attachment addAttachment(Attachment attachment) {
        return addAttachment(attachment, 0);
    }

    public Attachment addAttachment(Attachment attachment, int uid) {
        if (uid != 0) {
            attachment.setUserId(uid);
        }
        return journalCntObjDao.addAttachment(attachment);
    }

    public Attachment updateAttachment(Attachment attachment) {
        return journalCntObjDao.updateAttachment(attachment);
    }

    public Attachment getAttachmentById(int attachmentId) {
        return journalCntObjDao.getAttachmentById(attachmentId);
    }

    public void deleteAttachmentById(int attachmentId) {
        journalCntObjDao.deleteAttachmentById(attachmentId);
    }

    public String getInstrunctionbyhorseID(int id) {
        return journalConfigDAO.getInstructionbyID(id);
    }

    public JournalConfig getJournalConfigbyHorseId(int id) {
        return journalConfigDAO.getJournalConfigbyHorseid(id);
    }

    ///////////////////////////////////////////////////
    //
    // Journal Peer Review
    //
    ///////////////////////////////////////////////////

    public void submitJournalPeerReview(int peerReviewerId, int horseId, String opinions) {
        final ContentHeader contentHeader = cntHdrDao.selectContentHeaderByHorseId(horseId);
        JournalPeerReview jpr = journalPeerReviewDao.getPeerReviewsByJournalContentAndUserId(contentHeader.getContentObjectId(), peerReviewerId);
        if (jpr != null) {
            jpr.setOpinions(opinions);
            jpr.setSubmitTime(new Date());
            journalPeerReviewDao.update(jpr);
        }
    }

    @Autowired
    public void setJournalDao(JournalContentObjectDAO journalCntObjDao) {
        this.journalCntObjDao = journalCntObjDao;
    }

    @Autowired
    public void setJournalConfigDAO(JournalConfigDAO journalConfigDAO) {
        this.journalConfigDAO = journalConfigDAO;
    }

    @Autowired
    public void setContentHeaderDao(ContentHeaderDAO cntHdrDao) {
        this.cntHdrDao = cntHdrDao;
    }

    @Autowired
    public void setJournalPeerReviewDao(JournalPeerReviewDAO journalPeerReviewDao) {
        this.journalPeerReviewDao = journalPeerReviewDao;
    }
}
