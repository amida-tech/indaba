/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.po.Attachment;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.JournalContentObject;
import java.util.List;

/**
 *
 * @author Jeff
 */
public class JournalContentView {

    private ContentHeader contentHeader = null;
    private JournalContentObject journalContentObject = null;
    private List<Attachment> attachments = null;
    private int minWords;
    private int maxWords;

    public JournalContentView() {
    }

    public JournalContentView(ContentHeader contentHeader, JournalContentObject journalContentObject, List<Attachment> attachments) {
        this.contentHeader = contentHeader;
        this.journalContentObject = journalContentObject;
        this.attachments = attachments;
    }

    public ContentHeader getContentHeader() {
        return contentHeader;
    }

    public void setContentHeader(ContentHeader cntHdr) {
        this.contentHeader = cntHdr;
    }

    public JournalContentObject getJournalContentObject() {
        return journalContentObject;
    }

    public void setJournalContentObject(JournalContentObject journalCntObj) {
        this.journalContentObject = journalCntObj;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public int getMaxWords() {
        return maxWords;
    }

    public void setMaxWords(int maxWords) {
        this.maxWords = maxWords;
    }

    public int getMinWords() {
        return minWords;
    }

    public void setMinWords(int minWords) {
        this.minWords = minWords;
    }
}
