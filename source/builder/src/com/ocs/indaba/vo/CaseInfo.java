/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
*/

package com.ocs.indaba.vo;

import com.ocs.indaba.po.Ctags;
import com.ocs.indaba.po.CaseAttachment;
import com.ocs.indaba.po.ContentHeader;
import java.util.Date;
import java.util.List;
import com.ocs.indaba.po.Message;
import com.ocs.indaba.po.User;

/**
 *
 * @author menglong
 */
public class CaseInfo {

    private int caseId;
    private String title;
    private String status;
    private short statusCode;
    private String priority;
    private short priorityCode;
    private Boolean blockWorkFlow;
    private Boolean blockPulishing;

    private String owner;

    private String openedByUserName;
    private int openedByUserId;
    private String assignedUserName;
    private int assignedUserId;
    private Date openedTime;
    private String description;
    private Integer projectId;
    private Integer productId;
    private Integer horseId;
    private Integer goalId;
    private String reason;
    private Integer staffMsgboardId;
    private Integer userMsgboardId;
    private List<Message> messageInfoList;
    private List<MessageVO> userMessageList;
    private List<MessageVO> staffMessageList;
    private List<User> attachUsers;
    private List<String> attachContentTitles;
    private List<Long> attachContentIds;
    private List<ContentHeader> attachContents;
    private List<Ctags> tags;
    private List<CaseAttachment> attachFiles;
    private Date lastUpdateTime;

    public Boolean getBlockPulishing() {
        return blockPulishing;
    }

    public void setBlockPulishing(Boolean blockPulishing) {
        this.blockPulishing = blockPulishing;
    }

    public Boolean getBlockWorkFlow() {
        return blockWorkFlow;
    }

    public void setBlockWorkFlow(Boolean blockWorkFlow) {
        this.blockWorkFlow = blockWorkFlow;
    }

    public short getPriorityCode() {
        return priorityCode;
    }

    public void setPriorityCode(short priorityCode) {
        this.priorityCode = priorityCode;
    }

    public List<Long> getAttachContentIds() {
        return attachContentIds;
    }

    public void setAttachContentIds(List<Long> attachContentIds) {
        this.attachContentIds = attachContentIds;
    }

    public short getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(short statusCode) {
        this.statusCode = statusCode;
    }

    public int getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(int assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public int getOpenedByUserId() {
        return openedByUserId;
    }

    public void setOpenedByUserId(int openedByUserId) {
        this.openedByUserId = openedByUserId;
    }

    public List<CaseAttachment> getAttachFiles() {
        return attachFiles;
    }

    public void setAttachFiles(List<CaseAttachment> attachFiles) {
        this.attachFiles = attachFiles;
    }

    public List<Message> getMessageInfoList() {
        return messageInfoList;
    }

    public void setMessageInfoList(List<Message> messageInfoList) {
        this.messageInfoList = messageInfoList;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Ctags> getTags() {
        return tags;
    }

    public void setTags(List<Ctags> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAssignedUserName() {
        return assignedUserName;
    }

    public void setAssignedUserName(String assignedUserName) {
        this.assignedUserName = assignedUserName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getGoalId() {
        return goalId;
    }

    public void setGoalId(Integer goalId) {
        this.goalId = goalId;
    }

    public Integer getHorseId() {
        return horseId;
    }

    public void setHorseId(Integer horseId) {
        this.horseId = horseId;
    }

    public Integer getStaffMsgboardId() {
        return staffMsgboardId;
    }

    public void setStaffMsgboardId(Integer staffMsgboardId) {
        this.staffMsgboardId = staffMsgboardId;
    }

    public Integer getUserMsgboardId() {
        return userMsgboardId;
    }

    public void setUserMsgboardId(Integer userMsgboardId) {
        this.userMsgboardId = userMsgboardId;
    }

    public Date getOpenedTime() {
        return openedTime;
    }

    public void setOpenedTime(Date openedTime) {
        this.openedTime = openedTime;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOpenedByUserName() {
        return openedByUserName;
    }

    public void setOpenedByUserName(String openedByUserName) {
        this.openedByUserName = openedByUserName;
    }

    public List<User> getAttachUsers() {
        return attachUsers;
    }

    public void setAttachUsers(List<User> attachUsers) {
        this.attachUsers = attachUsers;
    }

    public List<String> getAttachContentTitles() {
        return attachContentTitles;
    }

    public void setAttachContentTitles(List<String> attachContentTitles) {
        this.attachContentTitles = attachContentTitles;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public List<MessageVO> getStaffMessageList() {
        return staffMessageList;
    }

    public void setStaffMessageList(List<MessageVO> staffMessageList) {
        this.staffMessageList = staffMessageList;
    }

    public List<MessageVO> getUserMessageList() {
        return userMessageList;
    }

    public void setUserMessageList(List<MessageVO> userMessageList) {
        this.userMessageList = userMessageList;
    }

    /**
     * @return the attachContents
     */
    public List<ContentHeader> getAttachContents() {
        return attachContents;
    }

    /**
     * @param attachContents the attachContents to set
     */
    public void setAttachContents(List<ContentHeader> attachContents) {
        this.attachContents = attachContents;
    }

}
