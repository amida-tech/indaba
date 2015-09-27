/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.controlpanel.notifimporter;

import com.ocs.indaba.po.NotificationType;
import com.ocs.indaba.po.Role;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class ImpProjectNotif {

    private NotificationType type;
    private String name;
    private String description;
    private int languageId;
    private String languageName;
    private String subject;
    private String body;
    private List<Integer> roleIds = null;
    private int lineNum;
    private int existingNotifId;

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

    public void setDescription(String value) {
        this.description = value;
    }

    public String getDescription() {
        return this.description;
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

    public void setRoleIds(List<Integer> value) {
        this.roleIds = value;
    }

    public List<Integer> getRoleIds() {
        return this.roleIds;
    }

    public void setLineNumber(int value) {
        this.lineNum = value;
    }

    public int getLineNumber() {
        return this.lineNum;
    }

    public void setExistingNotifId(int value) {
        this.existingNotifId = value;
    }

    public int getExistingNotifId() {
        return this.existingNotifId;
    }

    public void setLanguageName(String value) {
        this.languageName = value;
    }

    public String getLanguageName() {
        return this.languageName;
    }

}
