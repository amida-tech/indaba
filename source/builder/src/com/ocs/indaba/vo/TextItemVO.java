/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

/**
 *
 * @author jiangjeff
 */
public class TextItemVO {
    private int textResourceId;
    private String textResourceName = null;
    private String enText = null;
    private String frText = null;

    public String getEnText() {
        return enText;
    }

    public void setEnText(String enText) {
        this.enText = enText;
    }

    public String getFrText() {
        return frText;
    }

    public void setFrText(String frText) {
        this.frText = frText;
    }

    public int getTextResourceId() {
        return textResourceId;
    }

    public void setTextResourceId(int textResourceId) {
    	this.textResourceId = textResourceId;
//        System.err.println(this.textResourceId);
    }

    public String getTextResourceName() {
        return textResourceName;
    }

    public void setTextResourceName(String textResourceName) {
        this.textResourceName = textResourceName;
    }

    @Override
    public String toString() {
        return "TextItemVO{" + "textResourceId=" + textResourceId + ", textResourceName=" + textResourceName + ", enText=" + enText + ", frText=" + frText + '}';
    }

}
