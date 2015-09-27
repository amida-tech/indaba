/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.dao.ContentHeaderDAO;
import com.ocs.indaba.dao.ContentVersionDAO;
import com.ocs.indaba.dao.JournalAttachmentVersionDAO;
import com.ocs.indaba.dao.JournalContentVersionDAO;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.ContentVersion;
import com.ocs.indaba.po.JournalAttachmentVersion;
import com.ocs.indaba.po.JournalContentVersion;
import com.ocs.indaba.vo.JournalAttachmentVersionView;
import com.ocs.indaba.vo.JournalVersionView;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class JournalVersionService {

    private static final Logger logger = Logger.getLogger(JournalVersionService.class);
    private ContentVersionDAO contentVersionDao = null;
    private JournalContentVersionDAO journalContentVersionDao = null;
    private JournalAttachmentVersionDAO journalAttachVersionDao = null;
    private ContentHeaderDAO contentHeaderDao = null;

    public void saveJournalVersion(JournalVersionView journalVerView) {
        logger.debug("Save Journal to the backup version tables.");
        if (journalVerView == null) {
            logger.warn("Can't save journal content view. The object is NULL!");
            return;
        }
        ContentVersion cntVer = journalVerView.getContentVersion();
        cntVer = contentVersionDao.save(cntVer);

        JournalContentVersion journalCntVersion = journalVerView.getJournalContentVersion();
        if (journalCntVersion != null) {
            journalCntVersion.setContentVersionId(cntVer.getId());
            journalContentVersionDao.save(journalCntVersion);
        }
        List<JournalAttachmentVersion> attachments = journalVerView.getJournalAttachmentVersions();

        if (attachments != null) {
            for (JournalAttachmentVersion attached : attachments) {
                attached.setContentVersionId(cntVer.getId());
                journalAttachVersionDao.save(attached);
            }
        }
    }

    public List<ContentVersion> getAllContentVersions(int horseId) {
        return contentVersionDao.getAllContentVersionsByHorseId(horseId);
    }

    public JournalVersionView getJournalContentVersion(int contentVersionId) {
        ContentVersion contentVersion = contentVersionDao.get(contentVersionId);
        ContentHeader contentHeader = contentHeaderDao.selectContentHeaderById(contentVersion.getContentHeaderId());
        JournalContentVersion journalContentVersion = journalContentVersionDao.getJournalContentVersion(contentVersionId);
        List<JournalAttachmentVersion> journalAttachmentVersions = journalAttachVersionDao.getJournalAttachmentVersions(contentVersionId);
        JournalVersionView journalVersionView = new JournalVersionView();
        journalVersionView.setContentVersion(contentVersion);
        journalVersionView.setContentHeader(contentHeader);
        journalVersionView.setJournalContentVersion(journalContentVersion);
        journalVersionView.setJournalAttachmentVersions(journalAttachmentVersions);
        return journalVersionView;
    }

    public JournalAttachmentVersionView getJournalAttachmentVersionByAttachVersionId(int attachVerId) {
        return journalAttachVersionDao.getJournalAttachmentVersionByAttachVersionId(attachVerId);
    }

    public ContentHeader getContentHeaderById(int cntHdrId) {
        return contentHeaderDao.selectContentHeaderById(cntHdrId);
    }

    public List<ContentHeader> getDefaultContentHeader(int userId) {
        return contentHeaderDao.selectContentHeaderByAssignedUserId(userId);
    }

    public List<ContentHeader> getDefaultContentHeaderOrderByTitle(int userId) {
        return contentHeaderDao.selectContentHeaderByAssignedUserIdOrderByTitle(userId);
    }

    public List<ContentHeader> getAllContentHeader() {
        return contentHeaderDao.selectAllContentHeader();
    }

    public List<ContentHeader> getAllContentHeaderOrderByTitle() {
        return contentHeaderDao.selectAllContentHeaderOrderByTitle();
    }

    @Autowired
    public void setJournalContentVersionDao(JournalContentVersionDAO journalCntVerDao) {
        this.journalContentVersionDao = journalCntVerDao;
    }

    @Autowired
    public void setContentHeaderDao(ContentHeaderDAO cntHdrDao) {
        this.contentHeaderDao = cntHdrDao;
    }

    @Autowired
    public void setContentVersionDao(ContentVersionDAO cntVerDao) {
        this.contentVersionDao = cntVerDao;
    }

    @Autowired
    public void setJournalAttachmentVersionDAO(JournalAttachmentVersionDAO journalAttachVerDao) {
        this.journalAttachVersionDao = journalAttachVerDao;
    }
}
