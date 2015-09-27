/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.vo;

import java.util.Date;

/**
 *
 * @author Jeff
 */
public class JournalReview {

    private int userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String comment;
    private Date lastUpdated;
    private Date submitTime;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    @Override
    public String toString() {
        return "JournalReview{" + "userId=" + userId + "userName=" + userName + "firstName=" + firstName + "lastName=" + lastName + "comment=" + comment + "lastUpdated=" + lastUpdated + "submitTime=" + submitTime + '}';
    }
}
