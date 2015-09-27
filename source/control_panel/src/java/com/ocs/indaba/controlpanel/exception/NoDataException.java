/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.exception;

/**
 *
 * @author Jeff
 */
public class NoDataException extends Exception {

    private String message = null;

    public NoDataException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
