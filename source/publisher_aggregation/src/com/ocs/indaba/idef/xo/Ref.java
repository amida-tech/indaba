/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.xo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class Ref {
    String name;
    short type;
    String desc;

    List<RefChoice> choices;
    private int dboId;


    public void setDboId(int id) {
        this.dboId = id;
    }

    public int getDboId() {
        return this.dboId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return desc;
    }

    public void setDescription(String desc) {
        this.desc = desc;
    }

    public void setType(short type) {
        this.type = type;
    }

    public short getType() {
        return this.type;
    }

    public void setChoices(List<RefChoice> choices) {
        this.choices = choices;
    }

    public List<RefChoice> getChoices() {
        return this.choices;
    }
}
