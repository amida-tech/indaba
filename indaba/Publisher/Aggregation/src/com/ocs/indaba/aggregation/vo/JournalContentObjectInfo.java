/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.vo;

import com.ocs.indaba.po.Attachment;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Jeff
 */
public class JournalContentObjectInfo {
    private int id;
    private int horseId;
    private int productId;
    private String productName;
    private int targetId;
    private String author;
    private String targetName;
    private String targetShortName;
    private String body;
    private Date creation;
    private List<Attachment> attachments;
    private List<JournalReview> journalReviews;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    public int getHorseId() {
        return horseId;
    }
    
    public void setHorseId(int horseId) {
        this.horseId = horseId;
    }
    
    public List<JournalReview> getJournalReviews() {
        return journalReviews;
    }
    
    public void addJournalReview(JournalReview review) {
        if (journalReviews == null) {
            journalReviews = new ArrayList<JournalReview>();
        }
        journalReviews.add(review);
    }
    
    public void setJournalReviews(List<JournalReview> journalReview) {
        this.journalReviews = journalReview;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public int getTargetId() {
        return targetId;
    }
    
    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }
    
    public String getTargetName() {
        return targetName;
    }
    
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return "JournalContentObjectInfo{" + "id=" + id + "horseId=" + horseId + "productId=" + productId + "productName=" + productName + "targetId=" + targetId + "targetName=" + targetName + "body=" + body + "attachments=" + attachments + "journalReviews=" + journalReviews + '}';
    }

    /**
     * @return the targetShortName
     */
    public String getTargetShortName() {
        return targetShortName;
    }

    /**
     * @param targetShortName the targetShortName to set
     */
    public void setTargetShortName(String targetShortName) {
        this.targetShortName = targetShortName;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the creation
     */
    public Date getCreation() {
        return creation;
    }

    /**
     * @param creation the creation to set
     */
    public void setCreation(Date creation) {
        this.creation = creation;
    }
    
}
