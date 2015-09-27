package com.ocs.indaba.vo;

import com.ocs.indaba.po.Message;
import com.ocs.indaba.po.Project;
import java.util.Date;

public class MessageVO {

    private UserDisplay author;
    private UserDisplay toUser;
    private Project toProject;
    private boolean isToUser;
    private boolean readStatus;
    private Message delegate;

    public MessageVO() {
    }

    public MessageVO(Message delegate) {
        this.delegate = delegate;
    }

    public UserDisplay getAuthor() {
        return author;
    }

    public void setAuthor(UserDisplay author) {
        this.author = author;
    }

    public Message getDelegate() {
        return delegate;
    }

    public void setDelegate(Message delegate) {
        this.delegate = delegate;
    }

    public boolean equals(Object object) {
        return delegate.equals(object);
    }

    public Integer getAuthorUserId() {
        return delegate.getAuthorUserId();
    }

    public String getBody() {
        return delegate.getBody();
    }

    public String getDispBody() {
        return delegate.getBody().replaceAll("\n", "<br/>");
    }

    public Date getCreatedTime() {
        return delegate.getCreatedTime();
    }

    public String getEnhanceBody() {
        return delegate.getEnhanceBody();
    }

    public Date getEnhanceTime() {
        return delegate.getEnhanceTime();
    }

    public String getEnhanceTitle() {
        return delegate.getEnhanceTitle();
    }

    public Integer getEnhancerUserId() {
        return delegate.getEnhancerUserId();
    }

    public Integer getId() {
        return delegate.getId();
    }

    public Integer getMsgboardId() {
        return delegate.getMsgboardId();
    }

    public Boolean getPublishable() {
        return delegate.getPublishable();
    }

    public Boolean getReadStatus() {
        return this.readStatus;
    }
    public int getDeleteUserId() {
        return this.delegate.getDeleteUserId();
    }
    public Date getDeleteTime() {
        return this.delegate.getDeleteTime();
    }

    public String getTitle() {
        return delegate.getTitle();
    }

    public int hashCode() {
        return delegate.hashCode();
    }

    public void setAuthorUserId(Integer authorUserId) {
        delegate.setAuthorUserId(authorUserId);
    }

    public void setBody(String body) {
        delegate.setBody(body);
    }

    public void setCreatedTime(Date createdTime) {
        delegate.setCreatedTime(createdTime);
    }

    public void setEnhanceBody(String enhanceBody) {
        delegate.setEnhanceBody(enhanceBody);
    }

    public void setEnhanceTime(Date enhanceTime) {
        delegate.setEnhanceTime(enhanceTime);
    }

    public void setEnhanceTitle(String enhanceTitle) {
        delegate.setEnhanceTitle(enhanceTitle);
    }

    public void setEnhancerUserId(Integer enhancerUserId) {
        delegate.setEnhancerUserId(enhancerUserId);
    }

    public void setId(Integer id) {
        delegate.setId(id);
    }

    public void setMsgboardId(Integer msgboardId) {
        delegate.setMsgboardId(msgboardId);
    }

    public void setPublishable(Boolean publishable) {
        delegate.setPublishable(publishable);
    }

    public void setReadStatus(Boolean readStatus) {
        this.readStatus = readStatus;
    }

    public void setTitle(String title) {
        delegate.setTitle(title);
    }

    public boolean isIsToUser() {
        return isToUser;
    }

    public void setIsToUser(boolean isToUser) {
        this.isToUser = isToUser;
    }

    public UserDisplay getToUser() {
        return toUser;
    }

    public void setToUser(UserDisplay toUser) {
        this.toUser = toUser;
    }

    public Project getToProject() {
        return toProject;
    }

    public void setToProject(Project toProject) {
        this.toProject = toProject;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public String toString() {
        return delegate.toString();
    }
}
