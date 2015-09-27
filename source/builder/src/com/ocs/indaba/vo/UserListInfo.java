/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.po.Cases;
import com.ocs.indaba.po.Target;
import com.ocs.indaba.po.Team;
import java.util.List;

/**
 *
 * @author menglong
 */
public class UserListInfo {

    private int id;
    private String username;
    private String role;
    private List<Target> targets;
    private List<Team> teams;
    private List<Cases> openCases;
    private List<String> assignContents;
    private List<AssignedTaskDisplay> assignedTaskDisplay;

    public List<String> getAssignContents() {
        return assignContents;
    }

    public void setAssignContent(List<String> assignContents) {
        this.assignContents = assignContents;
    }

    /*
    public List<Integer> getAssignId() {
    return assignId;
    }

    public void setAssignId(List<Integer> assignId) {
    this.assignId = assignId;
    }

    public List<Integer> getHorseId() {
    return horseId;
    }

    public void setHorseId(List<Integer> horseId) {
    this.horseId = horseId;
    }

     */
    public List<Cases> getOpenCases() {
        return openCases;
    }

    public void setOpenCases(List<Cases> openCases) {
        this.openCases = openCases;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Target> getTargets() {
        return targets;
    }

    public void setTargets(List<Target> targets) {
        this.targets = targets;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<AssignedTaskDisplay> getAssignedTaskDisplay() {
        return assignedTaskDisplay;
    }

    public void setAssignedTaskDisplay(List<AssignedTaskDisplay> assignedTaskDisplay) {
        this.assignedTaskDisplay = assignedTaskDisplay;
    }
}
