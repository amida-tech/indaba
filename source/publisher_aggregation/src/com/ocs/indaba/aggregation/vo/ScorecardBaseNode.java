/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.vo;

/**
 *
 * @author jiangjeff
 */
public class ScorecardBaseNode {

    private int id;
    private String name = null;
    private int nodeType;
    private int usedScoreCount = 0;
    private boolean useScore;

    public ScorecardBaseNode() {
    }

    public ScorecardBaseNode(int id) {
        this.id = id;
    }

    public ScorecardBaseNode(int nodeType, int id, String name) {
        this.nodeType = nodeType;
        this.id = id;
        this.name = name;
        //this.score = score;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUsedScoreCount() {
        return usedScoreCount;
    }

    public void setUsedScoreCount(int usedScoreCount) {
        this.usedScoreCount = usedScoreCount;
    }

    public void incrementUsedScoreCount() {
        ++this.usedScoreCount;
    }
    
    public void increaseUsedScoreCount(int count) {
        this.usedScoreCount += count;
    }

    public boolean hasUseScore() {
        return useScore;
    }

    public void setUseScore(boolean useScore) {
        this.useScore = useScore;
    }
}
