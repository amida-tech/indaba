/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.vo;

import com.ocs.indaba.aggregation.common.Constants;

/**
 *
 * @author jiangjeff
 */
public class CategoryNode extends ScorecardBaseNode {

    //private String name = null;
    private String title = null;
    private String label = null;
    private int weight;
    private double min;
    private double max;
    private double mean;
    private double median;
    private double moe;
    private double legalFramework = 0;
    private double implementation = 0;
    private int legalFrameworkCount = 0;
    private int implementationCount = 0;

    public CategoryNode() {
        setNodeType(Constants.NODE_TYPE_CATEGORY);
    }

    public CategoryNode(int id, String name, String title, String label, int weight) {
        super(Constants.NODE_TYPE_CATEGORY, id, name);
        //this.name = name;
        this.title = title;
        this.label = label;
        this.weight = weight;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getMedian() {
        return median;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMoe() {
        return moe;
    }

    public void setMoe(double moe) {
        this.moe = moe;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public double getImplementation() {
        return implementation;
    }

    public void setImplementation(double implementation) {
        this.implementation = implementation;
    }

    public double getLegalFramework() {
        return legalFramework;
    }

    public void setLegalFramework(double legalFramework) {
        this.legalFramework = legalFramework;
    }

    public void addLegalFrameworkCount(int legalFrameworkCount) {
        this.legalFrameworkCount += legalFrameworkCount;
    }

    public void incrementImplementationCount() {
        ++implementationCount;
    }

    public int getImplementationCount() {
        return implementationCount;
    }

    public void addImplementationCount(int implementationCount) {
        this.implementationCount += implementationCount;
    }

    public void setImplementationCount(int implementationCount) {
        this.implementationCount = implementationCount;
    }

    public void incrementLegalFrameworkCount() {
        ++legalFrameworkCount;
    }

    public int getLegalFrameworkCount() {
        return legalFrameworkCount;
    }

    public void setLegalFrameworkCount(int legalFrameworkCount) {
        this.legalFrameworkCount = legalFrameworkCount;
    }

    @Override
    public String toString() {
        return "CategoryNode{" + "title=" + title + "label=" + label + "weight=" + weight + "min=" + min + "max=" + max + "mean=" + mean + "median=" + median + "moe=" + moe + "legalFramework=" + legalFramework + "implementation=" + implementation + "legalFrameworkCount=" + legalFrameworkCount + "implementationCount=" + implementationCount + '}';
    }
}
