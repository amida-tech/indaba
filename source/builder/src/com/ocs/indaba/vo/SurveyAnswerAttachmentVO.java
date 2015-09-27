/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

import com.ocs.indaba.po.Attachment;

/**
 *
 * @author yc06x
 */
public class SurveyAnswerAttachmentVO {

    private int surveyAnswerId;
    private Attachment attachment;

    public int getSurveyAnswerId() {
        return this.surveyAnswerId;
    }

    public void setSurveyAnswerId(int id) {
        this.surveyAnswerId = id;
    }

    public Attachment getAttachment() {
        return this.attachment;
    }

    public void setAttachment(Attachment a) {
        this.attachment = a;
    }
}
