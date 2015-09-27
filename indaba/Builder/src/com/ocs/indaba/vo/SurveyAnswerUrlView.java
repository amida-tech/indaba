/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

/**
 *
 * @author Administrator
 */
public class SurveyAnswerUrlView {
    private int previousId;
    private int nextId;
    private int upId;
    private int downId;
    private String returnUrl;//the link back to scorecard page

    /**
     * @return the previousId
     */
    public int getPreviousId() {
        return previousId;
    }

    /**
     * @param previousId the previousId to set
     */
    public void setPreviousId(int previousId) {
        this.previousId = previousId;
    }

    /**
     * @return the nextId
     */
    public int getNextId() {
        return nextId;
    }

    /**
     * @param nextId the nextId to set
     */
    public void setNextId(int nextId) {
        this.nextId = nextId;
    }

    /**
     * @return the upId
     */
    public int getUpId() {
        return upId;
    }

    /**
     * @param upId the upId to set
     */
    public void setUpId(int upId) {
        this.upId = upId;
    }

    /**
     * @return the downId
     */
    public int getDownId() {
        return downId;
    }

    /**
     * @param downId the downId to set
     */
    public void setDownId(int downId) {
        this.downId = downId;
    }

    /**
     * @return the returnUrl
     */
    public String getReturnUrl() {
        return returnUrl;
    }

    /**
     * @param returnUrl the returnUrl to set
     */
    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

}
