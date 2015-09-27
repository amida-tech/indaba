/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.vo;

/**
 *
 * @author Jeanbone
 */
public class ChartVO {

    private String chartName;
    private int targetIndex;
    private Double[] values;

    /**
     * @return the chartName
     */
    public String getChartName() {
        return chartName;
    }

    /**
     * @param chartName the chartName to set
     */
    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    /**
     * @return the targetIndex
     */
    public int getTargetIndex() {
        return targetIndex;
    }

    /**
     * @param targetIndex the targetIndex to set
     */
    public void setTargetIndex(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    /**
     * @return the values
     */
    public Double[] getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(Double[] values) {
        this.values = values;
    }
}
