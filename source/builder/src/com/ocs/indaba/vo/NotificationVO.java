/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

/**
 *
 * @author Jeff
 */
public class NotificationVO {
    private int notificationTypeId;
    private String enSubjectText;
    private String frSubjectText;
    private String enBodyText;
    private String frBodyText;

    public String getEnBodyText() {
        return enBodyText;
    }

    public void setEnBodyText(String enBodyText) {
        this.enBodyText = enBodyText;
    }

    public String getEnSubjectText() {
        return enSubjectText;
    }

    public void setEnSubjectText(String enSubjectText) {
        this.enSubjectText = enSubjectText;
    }

    public String getFrBodyText() {
        return frBodyText;
    }

    public void setFrBodyText(String frBodyText) {
        this.frBodyText = frBodyText;
    }

    public String getFrSubjectText() {
        return frSubjectText;
    }

    public void setFrSubjectText(String frSubjectText) {
        this.frSubjectText = frSubjectText;
    }

    public int getNotificationTypeId() {
        return notificationTypeId;
    }

    public void setNotificationTypeId(int notificationTypeId) {
        this.notificationTypeId = notificationTypeId;
    }

    @Override
    public String toString() {
        return "NotificationVO{" + "notificationTypeId=" + notificationTypeId + ", enSubjectText=" + enSubjectText + ", frSubjectText=" + frSubjectText + ", enBodyText=" + enBodyText + ", frBodyText=" + frBodyText + '}';
    }
}
