/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

import com.ocs.indaba.po.Groupdef;


/**
 *
 * @author ningshan
 */
public class GroupdefActionResult {
    static public final short RESULT_CODE_OK = 0;
    static public final short RESULT_CODE_ERROR = 1;
    
    private short code;
    private String errMessage;
    private Groupdef groupdef;

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

    public Groupdef getGroupdef() {
        return groupdef;
    }

    public void setGroupdef(Groupdef groupdef) {
        this.groupdef = groupdef;
    }

    
}
