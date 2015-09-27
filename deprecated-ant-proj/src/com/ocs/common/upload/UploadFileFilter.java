/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.common.upload;

/**
 *
 * @author Administrator
 */
public interface UploadFileFilter {
    /**
     * Check if it is acceptable by file suffix extention
     * 
     * @param filename file name
     * @return
     */
    public boolean accept(String filename);
}
