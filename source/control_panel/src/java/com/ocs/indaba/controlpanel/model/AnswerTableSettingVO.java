/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.controlpanel.model;

import com.ocs.util.ValueObject;

/**
 *
 * @author yc06x
 */
public class AnswerTableSettingVO extends ValueObject {

    private int id;
    private String tdfFileName;
    private String pathName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTdfFileName() {
        return this.tdfFileName;
    }

    public void setTdfFileName(String name) {
        this.tdfFileName = name;
    }

    public String getPathName() {
        return this.pathName;
    }

    public void setPathName(String name) {
        this.pathName = name;
    }

}
