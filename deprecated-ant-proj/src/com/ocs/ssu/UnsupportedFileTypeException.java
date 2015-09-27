/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.ssu;

/**
 *
 * @author yc06x
 */
public class UnsupportedFileTypeException extends Exception {

    private String fileType = null;

    public UnsupportedFileTypeException(String fileType) {
        this.fileType = fileType;
    }

    public String getFileType() {
        return fileType;
    }
}
