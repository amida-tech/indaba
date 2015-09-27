/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.vo;

import java.util.List;

/**
 *
 * @author jiangjeff
 */
public class OrganizationVO {

    private int id;
    private String name = null;
    private List<WsProjectVO> projects = null;
    private boolean checked = false;

    public OrganizationVO(int id, String name) {
        this.id = id;
        this.name = name;
    }


    /**
     * Get the value of projects
     *
     * @return the value of projects
     */
    public List<WsProjectVO> getProjects() {
        return projects;
    }

    /**
     * Set the value of projects
     *
     * @param projects new value of projects
     */
    public void setProjects(List<WsProjectVO> projects) {
        this.projects = projects;
    }


    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        this.name = name;
    }



    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public int getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the checked
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * @param checked the checked to set
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
