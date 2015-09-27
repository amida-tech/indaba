/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.vo;

import com.ocs.indaba.aggregation.po.OtisValue;
import com.ocs.indaba.aggregation.po.TdsValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author luwb
 */
public class AggBuildDataVO{
    private int worksetId;
    private List<TSDataVO> tsDataList;

    public AggBuildDataVO(){
        tsDataList = new ArrayList<TSDataVO>();
    }

    /**
     * @return the worksetId
     */
    public int getWorksetId() {
        return worksetId;
    }

    /**
     * @param worksetId the worksetId to set
     */
    public void setWorksetId(int worksetId) {
        this.worksetId = worksetId;
    }

    /**
     * @return the tsDataList
     */
    public List<TSDataVO> getTsDataList() {
        return tsDataList;
    }

    /**
     * @param tsDataList the tsDataList to set
     */
    public void setTsDataList(List<TSDataVO> tsDataList) {
        this.tsDataList = tsDataList;
    }
}
