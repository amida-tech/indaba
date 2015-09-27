/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.xo;

/**
 *
 * @author yc06x
 */
public class Review {

    User reviewer;
    String comments;
    short opinion;
    Answer answer;

    public void setReviewer(User user) {
        this.reviewer = user;
    }

    public User getReviewer() {
        return this.reviewer;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComments() {
        return this.comments;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Answer getAnswer() {
        return this.answer;
    }

    public short getOpinion() {
        return this.opinion;
    }

    public void setOpinion(short opinion) {
        this.opinion = opinion;
    }
}
