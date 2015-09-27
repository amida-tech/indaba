/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.ContentVersion;
import com.ocs.indaba.po.JournalAttachmentVersion;
import com.ocs.indaba.po.JournalConfig;
import com.ocs.indaba.po.JournalContentVersion;
import java.util.List;

/**
 *
 * @author Jeff
 */
public class JournalVersionView {

    private ContentHeader contentHeader = null;
    private ContentVersion contentVersion = null;
    private JournalContentVersion journalContentVersion = null;
    private List<JournalAttachmentVersion> journalAttachmentVersions = null;
    private JournalConfig jouornalConfig = null;

    public JournalVersionView() {
    }

    public ContentHeader getContentHeader() {
        return contentHeader;
    }

    public void setContentHeader(ContentHeader contentHeader) {
        this.contentHeader = contentHeader;
    }

    public ContentVersion getContentVersion() {
        return contentVersion;
    }

    public void setContentVersion(ContentVersion contentVersion) {
        this.contentVersion = contentVersion;
    }

    public JournalConfig getJouornalConfig() {
        return jouornalConfig;
    }

    public void setJouornalConfig(JournalConfig jouornalConfig) {
        this.jouornalConfig = jouornalConfig;
    }

    public List<JournalAttachmentVersion> getJournalAttachmentVersions() {
        return journalAttachmentVersions;
    }

    public void setJournalAttachmentVersions(List<JournalAttachmentVersion> journalAttachmentVersions) {
        this.journalAttachmentVersions = journalAttachmentVersions;
    }

    public JournalContentVersion getJournalContentVersion() {
        return journalContentVersion;
    }

    public void setJournalContentVersion(JournalContentVersion journalContentVersion) {
        this.journalContentVersion = journalContentVersion;
    }
}
