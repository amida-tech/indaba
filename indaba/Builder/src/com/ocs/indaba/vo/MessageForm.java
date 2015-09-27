/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.common.Constants;
import java.util.Date;
import org.apache.struts.action.ActionForm;

/**
 *
 * @author Tiger Tang
 */
public class MessageForm extends ActionForm {

    /**
     *
     */
    private static final long serialVersionUID = 8827595008554041969L;
    private Integer msgId;
    private Integer msgboardId;
    private Short msgType;
    private Short boxType;
    private Short sendType;
    private Short publishable;
    private Integer authorUid;
    private Date createdTime;
    private String title;
    private String body;
    private int[] receiverIds;
    private int[] roleIds;
    private int[] teamIds;
    private boolean sendToProjectMsgboard;
    // inbox page number
    private int inpn = Constants.INVALID_INT_ID;
    // inbox page size
    private int inps = Constants.INVALID_INT_ID;
    // outbox page number
    private int outpn = Constants.INVALID_INT_ID;
    // outbox page size
    private int outps = Constants.INVALID_INT_ID;
    // project updates page number
    private int pupn = Constants.INVALID_INT_ID;
    // project updates page size
    private int pups = Constants.INVALID_INT_ID;

    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public Integer getAuthorUid() {
        return authorUid;
    }

    public void setAuthorUid(Integer authorUid) {
        this.authorUid = authorUid;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Short getMsgType() {
        return msgType;
    }

    public void setMsgType(Short msgType) {
        this.msgType = msgType;
    }

    public Short getBoxType() {
        return boxType;
    }

    public void setBoxType(Short boxType) {
        this.boxType = boxType;
    }

    public Short getSendType() {
        return sendType;
    }

    public void setSendType(Short sendType) {
        this.sendType = sendType;
    }

    public Integer getMsgboardId() {
        return msgboardId;
    }

    public void setMsgboardId(Integer msgboardId) {
        this.msgboardId = msgboardId;
    }

    public Short getPublishable() {
        return publishable;
    }

    public void setPublishable(Short publishable) {
        this.publishable = publishable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getInpn() {
        return inpn;
    }

    public void setInpn(int inpn) {
        this.inpn = inpn;
    }

    public int getInps() {
        return inps;
    }

    public void setInps(int inps) {
        this.inps = inps;
    }

    public int getOutpn() {
        return outpn;
    }

    public void setOutpn(int outpn) {
        this.outpn = outpn;
    }

    public int getOutps() {
        return outps;
    }

    public void setOutps(int outps) {
        this.outps = outps;
    }

    public int getPupn() {
        return pupn;
    }

    public void setPupn(int pupn) {
        this.pupn = pupn;
    }

    public int getPups() {
        return pups;
    }

    public void setPups(int pups) {
        this.pups = pups;
    }

    public int[] getReceiverIds() {
        return receiverIds;
    }

    public void setReceiverIds(int[] receiverIds) {
        this.receiverIds = receiverIds;
    }

    public int[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(int[] roleIds) {
        this.roleIds = roleIds;
    }

    public int[] getTeamIds() {
        return teamIds;
    }

    public void setTeamIds(int[] teamIds) {
        this.teamIds = teamIds;
    }

    public boolean isSendToProjectMsgboard() {
        return sendToProjectMsgboard;
    }

    public void setSendToProjectMsgboard(boolean sendToProjectMsgboard) {
        this.sendToProjectMsgboard = sendToProjectMsgboard;
    }
}
