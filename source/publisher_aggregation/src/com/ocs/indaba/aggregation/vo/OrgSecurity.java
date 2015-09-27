/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.vo;

/**
 *
 * @author luwenbin
 */
public class OrgSecurity {
    private int so;//orgId
    private int st;//timestamp
    private int sr;// random
    private int sv;//version
    private String sd;//message digest

    public OrgSecurity(int so, int st, int sr, int sv, String sd){
        this.so = so;
        this.st = st;
        this.sr = sr;
        this.sv = sv;
        this.sd = sd;
    }
    /**
     * @return the so
     */
    public int getSo() {
        return so;
    }

    /**
     * @param so the so to set
     */
    public void setSo(int so) {
        this.so = so;
    }

    /**
     * @return the st
     */
    public int getSt() {
        return st;
    }

    /**
     * @param st the st to set
     */
    public void setSt(int st) {
        this.st = st;
    }

    /**
     * @return the sr
     */
    public int getSr() {
        return sr;
    }

    /**
     * @param sr the sr to set
     */
    public void setSr(int sr) {
        this.sr = sr;
    }

    /**
     * @return the sv
     */
    public int getSv() {
        return sv;
    }

    /**
     * @param sv the sv to set
     */
    public void setSv(int sv) {
        this.sv = sv;
    }

    /**
     * @return the sd
     */
    public String getSd() {
        return sd;
    }

    /**
     * @param sd the sd to set
     */
    public void setSd(String sd) {
        this.sd = sd;
    }
}
