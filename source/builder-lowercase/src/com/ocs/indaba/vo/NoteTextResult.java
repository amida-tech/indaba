/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

/**
 *
 * @author yc06x
 */
public class NoteTextResult extends NoteActionResult {

    private int langId;

    // Client Instructions:
    // This is the data to be set in the textbox.
    // If null, don't change the data in the textbox.
    private String text;
    
    public int getLanguageId() {
        return this.langId;
    }

    public void setLanguageId(int value) {
        this.langId = value;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String value) {
        this.text = value;
    }
}
