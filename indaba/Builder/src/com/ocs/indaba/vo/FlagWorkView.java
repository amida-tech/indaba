/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

/**
 *
 * @author yc06x
 */
public class FlagWorkView {

    public static final short FLAG_TYPE_RAISED_BY_ME = 1;
    public static final short FLAG_TYPE_ASSIGNED_TO_ME = 2;
    public static final short FLAG_TYPE_OTHER = 3;

    private int flagId;
    private int surveyQuestionId;
    private int groupobjId;
    private String title;
    private boolean worked;
    private short flagType;
    private int assignedUserId;
    private int raiseUserId;
    private int permissions;
    private int index;

    public void setFlagId(int id) {
        this.flagId = id;
    }

    public int getFlagId() {
        return this.flagId;
    }

    public void setFlagType(short type) {
        this.flagType = type;
    }

    public int getFlagType() {
        return this.flagType;
    }

    public void setSurveyQuestionId(int id) {
        this.surveyQuestionId = id;
    }

    public int getSurveyQuestionId() {
        return this.surveyQuestionId;
    }

    public void setGroupobjId(int id) {
        this.groupobjId = id;
    }

    public int getGroupobjId() {
        return this.groupobjId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getWorked() {
        return this.worked;
    }

    public void setWorked(boolean value) {
        this.worked = value;
    }

    public void setRaiseUserId(int id) {
        this.raiseUserId = id;
    }

    public int getRaiseUserId() {
        return this.raiseUserId;
    }

    public void setAssignedUserId(int id) {
        this.assignedUserId = id;
    }

    public int getAssignedUserId() {
        return this.assignedUserId;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setPermissions(int value) {
        this.permissions = value;
    }

    public int getPermissions() {
        return permissions;
    }

}
