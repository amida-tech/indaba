/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

/**
 *
 * @author yc06x
 */
public class AssignmentSubmissionResult {

    public static final int RESULT_OK = 0;
    public static final int RESULT_ERROR = 1;

    private int code;
    private String message;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMessage(String msg) {
        message = msg;
    }

    public String getMessage() {
        return message;
    }

}
