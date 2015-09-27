/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.vo;

import com.ocs.indaba.aggregation.common.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jeanbone
 */
public class DataForm {

    private int worksetId;
    private String worksetName;
    private boolean includeUnverifiedData;
    private boolean exportEntireSet;
    private List<Integer> wsIndicatorIds;
    private List<Integer> datapointIds;
    private List<Integer> targetIds;
    private List<Integer> studyPeriodIds;

    public DataForm() {
        init();
    }

    public final void init() {
        worksetId = Constants.INVALID_INT_ID;
        worksetName = null;
        includeUnverifiedData = false;
        exportEntireSet = false;
        wsIndicatorIds = new ArrayList<Integer>();
        datapointIds = new ArrayList<Integer>();
        targetIds = new ArrayList<Integer>();
        studyPeriodIds = new ArrayList<Integer>();
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
     * @return the worksetName
     */
    public String getWorksetName() {
        return worksetName;
    }

    /**
     * @param worksetName the worksetName to set
     */
    public void setWorksetName(String worksetName) {
        this.worksetName = worksetName;
    }

    /**
     * @return the includeUnverifiedData
     */
    public boolean isIncludeUnverifiedData() {
        return includeUnverifiedData;
    }

    /**
     * @param includeUnverifiedData the includeUnverifiedData to set
     */
    public void setIncludeUnverifiedData(boolean includeUnverifiedData) {
        this.includeUnverifiedData = includeUnverifiedData;
    }

    /**
     * @return the exportEntireSet
     */
    public boolean isExportEntireSet() {
        return exportEntireSet;
    }

    /**
     * @param exportEntireSet the exportEntireSet to set
     */
    public void setExportEntireSet(boolean exportEntireSet) {
        this.exportEntireSet = exportEntireSet;
    }

    /**
     * @return the wsIndicatorIds
     */
    public List<Integer> getWsIndicatorIds() {
        return wsIndicatorIds;
    }

    /**
     * @param wsIndicatorIds the wsIndicatorIds to set
     */
    public void setWsIndicatorIds(List<Integer> wsIndicatorIds) {
        this.wsIndicatorIds = wsIndicatorIds;
    }

    /**
     * @return the studyPeriodIds
     */
    public List<Integer> getStudyPeriodIds() {
        return studyPeriodIds;
    }

    /**
     * @param studyPeriodIds the studyPeriodIds to set
     */
    public void setStudyPeriodIds(List<Integer> studyPeriodIds) {
        this.studyPeriodIds = studyPeriodIds;
    }

    /**
     * @return the targetIds
     */
    public List<Integer> getTargetIds() {
        return targetIds;
    }

    /**
     * @param targetIds the targetIds to set
     */
    public void setTargetIds(List<Integer> targetIds) {
        this.targetIds = targetIds;
    }

    /**
     * @return the datapointIds
     */
    public List<Integer> getDatapointIds() {
        return datapointIds;
    }

    /**
     * @param datapointIds the datapointIds to set
     */
    public void setDatapointIds(List<Integer> datapointIds) {
        this.datapointIds = datapointIds;
    }
}
