/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

import com.ocs.util.JSONUtils;
import com.ocs.util.StringUtils;
import org.json.simple.JSONObject;

/**
 *
 * @author yc06x
 */
public class TaskAssignmentStatusData {

    private short version;
    private int totalItems;
    private int completedItems;

    public TaskAssignmentStatusData(short version) {
        this.version = version;
    }

    public TaskAssignmentStatusData() {
        this.version = 1;
    }

    public short getVersion() {
        return this.version;
    }

    public void setTotalItems(int num) {
        this.totalItems = num;
    }

    public int getTotalItems() {
        return this.totalItems;
    }

    public void setCompletedItems(int num) {
        this.completedItems = num;
    }

    public int getCompletedItems() {
        return this.completedItems;
    }

    public String encode() {
        return JSONUtils.toJsonString(this);
    }

    static public TaskAssignmentStatusData decode(String data) {
        if (data == null && StringUtils.isEmpty(data)) return null;

        JSONObject obj = JSONUtils.parseJSONStr(data);

        if (obj == null) return null;

        short version = JSONUtils.getShortValue(obj, "version");
        TaskAssignmentStatusData tad = new TaskAssignmentStatusData(version);

        int totalItems = JSONUtils.getIntValue(obj, "totalItems");
        tad.setTotalItems(totalItems);

        int completedItems = JSONUtils.getIntValue(obj, "completedItems");
        tad.setCompletedItems(completedItems);

        return tad;
    }

}
