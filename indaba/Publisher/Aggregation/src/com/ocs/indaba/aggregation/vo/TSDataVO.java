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
public class TSDataVO {
    private int targetId;
    private int studyPeriodId;
    private List<OtisValue> pub_indicators;//indicator whose scorecard is published
    private List<OtisValue> nonpub_indicators;//indicator whose scorecard is not published
    private List<TdsValue> pub_datapoints;//datapoints which only include published data
    private List<TdsValue> all_datapoints;//datapoints which include both published and nonPublished data
    private HashMap<Integer, OtisValue> pub_indicatorMap;
    private HashMap<Integer, OtisValue> nonpub_indicatorMap;
    private HashMap<Integer, TdsValue> pub_datapointMap;
    private HashMap<Integer, TdsValue> all_datapointMap;

    public TSDataVO(){
        pub_indicators = new ArrayList<OtisValue>();
        nonpub_indicators = new ArrayList<OtisValue>();
        pub_datapoints = new ArrayList<TdsValue>();
        all_datapoints = new ArrayList<TdsValue>();
        pub_indicatorMap = new HashMap<Integer, OtisValue>();
        nonpub_indicatorMap = new HashMap<Integer, OtisValue>();
        pub_datapointMap = new HashMap<Integer, TdsValue>();
        all_datapointMap = new HashMap<Integer, TdsValue>();
    }

    /**
     * @return the targetId
     */
    public int getTargetId() {
        return targetId;
    }

    /**
     * @param targetId the targetId to set
     */
    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    /**
     * @return the studyPeriodId
     */
    public int getStudyPeriodId() {
        return studyPeriodId;
    }

    /**
     * @param studyPeriodId the studyPeriodId to set
     */
    public void setStudyPeriodId(int studyPeriodId) {
        this.studyPeriodId = studyPeriodId;
    }

    /**
     * @return the pub_indicators
     */
    public List<OtisValue> getPub_indicators() {
        return pub_indicators;
    }

    /**
     * @param pub_indicators the pub_indicators to set
     */
    public void setPub_indicators(List<OtisValue> pub_indicators) {
        this.pub_indicators = pub_indicators;
    }

    /**
     * @return the nonpub_indicators
     */
    public List<OtisValue> getNonpub_indicators() {
        return nonpub_indicators;
    }

    /**
     * @param nonpub_indicators the nonpub_indicators to set
     */
    public void setNonpub_indicators(List<OtisValue> nonpub_indicators) {
        this.nonpub_indicators = nonpub_indicators;
    }

    /**
     * @return the pub_datapoints
     */
    public List<TdsValue> getPub_datapoints() {
        return pub_datapoints;
    }

    /**
     * @param pub_datapoints the pub_datapoints to set
     */
    public void setPub_datapoints(List<TdsValue> pub_datapoints) {
        this.pub_datapoints = pub_datapoints;
    }

    /**
     * @return the all_datapoints
     */
    public List<TdsValue> getAll_datapoints() {
        return all_datapoints;
    }

    /**
     * @param all_datapoints the all_datapoints to set
     */
    public void setAll_datapoints(List<TdsValue> all_datapoints) {
        this.all_datapoints = all_datapoints;
    }

    /**
     * @return the pub_indicatorMap
     */
    public HashMap<Integer, OtisValue> getPub_indicatorMap() {
        return pub_indicatorMap;
    }

    /**
     * @param pub_indicatorMap the pub_indicatorMap to set
     */
    public void setPub_indicatorMap(HashMap<Integer, OtisValue> pub_indicatorMap) {
        this.pub_indicatorMap = pub_indicatorMap;
    }

    /**
     * @return the nonpub_indicatorMap
     */
    public HashMap<Integer, OtisValue> getNonpub_indicatorMap() {
        return nonpub_indicatorMap;
    }

    /**
     * @param nonpub_indicatorMap the nonpub_indicatorMap to set
     */
    public void setNonpub_indicatorMap(HashMap<Integer, OtisValue> nonpub_indicatorMap) {
        this.nonpub_indicatorMap = nonpub_indicatorMap;
    }

    /**
     * @return the pub_datapointMap
     */
    public HashMap<Integer, TdsValue> getPub_datapointMap() {
        return pub_datapointMap;
    }

    /**
     * @param pub_datapointMap the pub_datapointMap to set
     */
    public void setPub_datapointMap(HashMap<Integer, TdsValue> pub_datapointMap) {
        this.pub_datapointMap = pub_datapointMap;
    }

    /**
     * @return the all_datapointMap
     */
    public HashMap<Integer, TdsValue> getAll_datapointMap() {
        return all_datapointMap;
    }

    /**
     * @param all_datapointMap the all_datapointMap to set
     */
    public void setAll_datapointMap(HashMap<Integer, TdsValue> all_datapointMap) {
        this.all_datapointMap = all_datapointMap;
    }
}
