package com.ocs.indaba.controlpanel.notifimporter;

import com.ocs.indaba.po.NotificationType;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ningshan
 */
public class ImpNotificationItem {
    
    private NotificationType type;
    private String name;
    private int languageId;
    private String languageName;
    private String subject;
    private String body;
    private int lineNum;
    private int existingNotificationItemId;

    public void setType(NotificationType value) {
        this.type = value;
    }

    public NotificationType getType() {
        return this.type;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }


    public void setLanguageId(int value) {
        this.languageId = value;
    }

    public int getLanguageId() {
        return this.languageId;
    }

    public void setSubject(String value) {
        this.subject = value;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setBody(String value) {
        this.body = value;
    }

    public String getBody() {
        return this.body;
    }

    public void setLineNumber(int value) {
        this.lineNum = value;
    }

    public int getLineNumber() {
        return this.lineNum;
    }

    public int getExistingNotificationItemId() {
        return existingNotificationItemId;
    }

    public void setExistingNotificationItemId(int existingNotificationItemId) {
        this.existingNotificationItemId = existingNotificationItemId;
    }

    public void setLanguageName(String value) {
        this.languageName = value;
    }

    public String getLanguageName() {
        return this.languageName;
    }
 
}
