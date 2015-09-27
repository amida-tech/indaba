/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.survey.tree;

/**
 *
 * @author Jeff
 */
public class CategoryNode extends Node {

    private int completed = 0;
    private int incompleted = 0;
    private int agreed = 0;
    private int disagreed = 0;
    private int noQualified = 0;
    private int needClarified = 0;
    private int unreviewed = 0;
    private String label = null;
    private String title = null;

    public CategoryNode(int id, String label, String title, int parentId, int weight) {
        super(Node.NODE_TYPE_CATEGORY, id, (label + ". " + title), parentId, weight);
        this.label = label;
        this.title = title;
    }

    public void incrementCompleted() {
        ++completed;
    }

    public void incrementIncompleted() {
        ++incompleted;
    }

    public void incrementAgreed() {
        ++agreed;
    }

    public void incrementDisagreed() {
        ++disagreed;
    }

    public void incrementNoQualified() {
        ++noQualified;
    }

    public void incrementNeedClarified() {
        ++needClarified;
    }

    public void incrementUnreviewed() {
        ++unreviewed;
    }

    public void incrementCompleted(int val) {
        completed += val;
    }

    public void incrementIncompleted(int val) {
        incompleted += val;
    }

    public void incrementAgreed(int val) {
        agreed += val;
    }

    public void incrementDisagreed(int val) {
        disagreed += val;
    }

    public void incrementNoQualified(int val) {
        noQualified += val;
    }

    public void incrementNeedClarified(int val) {
        needClarified += val;
    }

    public void incrementUnreviewed(int val) {
        unreviewed += val;
    }

    public int getAgreed() {
        return agreed;
    }

    public void setAgreed(int agreed) {
        this.agreed = agreed;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getDisagreed() {
        return disagreed;
    }

    public void setDisagreed(int disagreed) {
        this.disagreed = disagreed;
    }

    public int getIncompleted() {
        return incompleted;
    }

    public void setIncompleted(int incompleted) {
        this.incompleted = incompleted;
    }

    public int getNeedClarified() {
        return needClarified;
    }

    public void setNeedClarified(int needClarified) {
        this.needClarified = needClarified;
    }

    public int getNoQualified() {
        return noQualified;
    }

    public void setNoQualified(int noQualified) {
        this.noQualified = noQualified;
    }

    public int getUnreviewed() {
        return unreviewed;
    }

    public void setUnreviewed(int unreviewed) {
        this.unreviewed = unreviewed;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "CategoryNode{" + "completed=" + completed + "incompleted=" + incompleted + "agreed=" + agreed + "disagreed=" + disagreed + "noQualified=" + noQualified + "needClarified=" + needClarified + "unreviewed=" + unreviewed + '}';
    }
}
