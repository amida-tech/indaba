/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

import com.ocs.indaba.po.Language;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class NoteobjView {

    // Client Instructions:
    // You need to send the noteobjId for future note-related requests
    private int objId;

    // Client Instructions:
    // Show the name and description as HTML text
    private String name;
    private String description;

    // Client Instructions:
    // if canEditNote, the textbox is in edit mode;
    // Otherwise the textbox must be in read-only mode.
    private boolean canEditNote;

    // Client Instructions:
    // If canTranslateNote, show the "translate" link;
    // Otherwise don't show this link.
    private boolean canTranslateNote;

    // Client Instructions:
    // Set the language selector to this language.
    // If 0, set the selector to "Choose a language"
    private int langId;

    // Client Instructions:
    // This is the data to be set in the textbox.
    // If null, don't change the data in the textbox.
    private String text;

    // The language list contains all supported languages.
    // They are included into the language selector.
    // The selector must also include one extra options for "Choose a language".
    private List<Language> languages;

    public int getObjId() {
        return this.objId;
    }

    public void setObjId(int value) {
        this.objId = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public boolean getCanEditNote() {
        return this.canEditNote;
    }

    public void setCanEditNote(boolean value) {
        this.canEditNote = value;
    }

    public boolean getCanTranslateNote() {
        return this.canTranslateNote;
    }

    public void setCanTranslateNote(boolean value) {
        this.canTranslateNote = value;
    }

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

    public List<Language> getLanguages() {
        return this.languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }


}
