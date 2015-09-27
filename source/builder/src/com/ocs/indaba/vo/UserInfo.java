/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
*/

package com.ocs.indaba.vo;

import com.ocs.indaba.po.Cases;
import java.util.List;

/**
 *
 * @author menglong
 */
public class UserInfo {
    private Integer id;
    private String name;
    private String role;
    private String target;
    private List<TeamInfo> teamList;
    private List<Cases> openCaseList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Cases> getOpenCaseList() {
        return openCaseList;
    }

    public void setOpenCaseList(List<Cases> openCaseList) {
        this.openCaseList = openCaseList;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<TeamInfo> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<TeamInfo> teamList) {
        this.teamList = teamList;
    }

    
}
