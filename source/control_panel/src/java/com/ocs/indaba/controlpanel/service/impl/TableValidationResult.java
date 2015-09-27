/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.controlpanel.service.impl;

import java.util.List;

/**
 *
 * @author yc06x
 */
public class TableValidationResult {
    private List<String> errors;
    private String filePath;
    private String origFileName;
    private int errCount = 0;

    public List<String> getErrors() {
        return this.errors;
    }

    public void setErrors(List<String> errors) {
        if (errors == null)
            errCount = 0;
        else
            errCount = errors.size();
        this.errors = errors;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String path) {
        this.filePath = path;
    }

    public String getOriginalFileName() {
        return this.origFileName;
    }

    public void setOriginalFileName(String name) {
        this.origFileName = name;
    }

    public int getErrCount() {
        return errCount;
    }

    public void setErrCount(int errCount) {
        this.errCount = errCount;
    }


}
