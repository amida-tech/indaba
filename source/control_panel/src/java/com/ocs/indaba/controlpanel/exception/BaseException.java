/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.exception;

/**
 *
 * @author Administrator
 */
public class BaseException extends Exception {

    private String message = null;

    public BaseException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
