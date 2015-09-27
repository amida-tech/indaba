/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import java.util.Date;

/**
 *
 * @author Jeff
 */
public class JournalAttachmentVersionView {

    private Integer attachVersionId;
    private String name;
    private Integer size;
    private String type;
    private String note;
    private String filePath;
    private Date updateTime;
    private Integer contentVersionId;
    private int contentHeaderId;
    private Date createTime;
    private int userId;
    private String description;

    public JournalAttachmentVersionView() {
    }

    public JournalAttachmentVersionView(Integer attachVersionId, String name, Integer size, String type, String note, String filePath, Date updateTime, Integer contentVersionId, int contentHeaderId, Date createTime, int userId, String description) {
        this.attachVersionId = attachVersionId;
        this.name = name;
        this.size = size;
        this.type = type;
        this.note = note;
        this.filePath = filePath;
        this.updateTime = updateTime;
        this.contentVersionId = contentVersionId;
        this.contentHeaderId = contentHeaderId;
        this.createTime = createTime;
        this.userId = userId;
        this.description = description;
    }

    public Integer getAttachVersionId() {
        return attachVersionId;
    }

    public void setAttachVersionId(Integer attachVersionId) {
        this.attachVersionId = attachVersionId;
    }

    public int getContentHeaderId() {
        return contentHeaderId;
    }

    public void setContentHeaderId(int contentHeaderId) {
        this.contentHeaderId = contentHeaderId;
    }

    public Integer getContentVersionId() {
        return contentVersionId;
    }

    public void setContentVersionId(Integer contentVersionId) {
        this.contentVersionId = contentVersionId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
