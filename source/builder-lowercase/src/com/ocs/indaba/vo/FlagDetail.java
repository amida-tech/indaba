/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.common.Constants;
import com.ocs.util.DateUtils;
import java.util.Date;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class FlagDetail extends GroupActionResult {

    public static final int FLAG_PERMISSION_MAIN_CHANGE_SCORE = Constants.FLAG_PERMISSION_MAIN_CHANGE_SCORE;
    public static final int FLAG_PERMISSION_MAIN_CHANGE_SOURCE = Constants.FLAG_PERMISSION_MAIN_CHANGE_SOURCE;
    public static final int FLAG_PERMISSION_MAIN_CHANGE_COMMENT = Constants.FLAG_PERMISSION_MAIN_CHANGE_COMMENT;
    public static final int FLAG_PERMISSION_MAIN_CHANGE_ATTACHMENT = Constants.FLAG_PERMISSION_MAIN_CHANGE_ATTACHMENT;
    public static final int FLAG_PERMISSION_PR_CHANGE_OPINION = Constants.FLAG_PERMISSION_PR_CHANGE_OPINION;
    public static final int FLAG_PERMISSION_PR_DISCUSSION = Constants.FLAG_PERMISSION_PR_DISCUSSION;
    private int flagId;
    private List<CommMemberInfo> candidates;
    private int permissions;
    private int assignedUserId;
    private String description;
    private Date raiseTime;
    private Date dueTime;
    private Date unsetTime;
    private int raiseUserId;
    private Date respondTime;

    public void setCandidates(List<CommMemberInfo> value) {
        this.candidates = value;
    }

    public List<CommMemberInfo> getCandidates() {
        return this.candidates;
    }

    public void setAssignedUserId(int value) {
        this.assignedUserId = value;
    }

    public int getAssignedUserId() {
        return this.assignedUserId;
    }

    public void setRaiseTime(Date value) {
        this.raiseTime = value;
    }

    public Date getRaiseTime() {
        return this.raiseTime;
    }

    public void setDueTime(Date value) {
        this.dueTime = value;
    }

    public Date getDueTime() {
        return this.dueTime;
    }

    public String getDueTimeStr() {
        return DateUtils.format(this.dueTime, DateUtils.DATE_FORMAT_6);
    }

    public void setUnsetTime(Date value) {
        this.unsetTime = value;
    }

    public Date getUnsetTime() {
        return this.unsetTime;
    }

    public void setRespondTime(Date value) {
        this.respondTime = value;
    }

    public Date getRespondTime() {
        return this.respondTime;
    }

    public void setRaiseUserId(int value) {
        this.raiseUserId = value;
    }

    public int getRaiseUserId() {
        return this.raiseUserId;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public String getDescription() {
        return this.description;
    }

    public void setPermissions(int value) {
        this.permissions = value;
    }

    public int getPermissions() {
        return this.permissions;
    }

    public void setFlagId(int value) {
        this.flagId = value;
    }

    public int getFlagId() {
        return this.flagId;
    }
}
