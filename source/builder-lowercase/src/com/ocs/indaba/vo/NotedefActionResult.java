/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

import com.ocs.indaba.po.Notedef;

/**
 *
 * @author ningshan
 */
public class NotedefActionResult {
    
    static public final short RESULT_CODE_OK = 0;
    static public final short RESULT_CODE_ERROR = 1;
    
    private short code;
    private String errMessage;
    private Notedef notedef;

    public short getCode() {
        return code;
    }

    public void setCode(short code) {
        this.code = code;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public Notedef getNotedef() {
        return notedef;
    }

    public void setNotedef(Notedef notedef) {
        this.notedef = notedef;
    }

    
}
