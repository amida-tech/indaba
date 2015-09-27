/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.po.SurveyCategory;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Jeff Jiang
 */
public class SurveyCategoryTree {

    SurveyCategoryTree parent = null;
    SurveyCategory curNode = null;
    int completed = 0;
    int incompleted = 0;
    List<SurveyCategoryTree> children = null;
    List<SurveyQuestionVO> leafNodes = null;
    private int agreed = 0;
    private int disagreed = 0;
    private int noQualified = 0;
    private int needClarified = 0;
    private int unreviewed = 0;

    /**
     * Get the value of unreviewed
     *
     * @return the value of unreviewed
     */
    public int getUnreviewed() {
        return unreviewed;
    }

    /**
     * Set the value of unreviewed
     *
     * @param unreviewed new value of unreviewed
     */
    public void setUnreviewed(int unreviewed) {
        this.unreviewed = unreviewed;
    }

    /**
     * Get the value of needClarified
     *
     * @return the value of needClarified
     */
    public int getNeedClarified() {
        return needClarified;
    }

    /**
     * Set the value of needClarified
     *
     * @param needClarified new value of needClarified
     */
    public void setNeedClarified(int needClarified) {
        this.needClarified = needClarified;
    }

    /**
     * Get the value of noQualified
     *
     * @return the value of noQualified
     */
    public int getNoQualified() {
        return noQualified;
    }

    /**
     * Set the value of noQualified
     *
     * @param noQualified new value of noQualified
     */
    public void setNoQualified(int noQualified) {
        this.noQualified = noQualified;
    }

    /**
     * Get the value of disagreed
     *
     * @return the value of disagreed
     */
    public int getDisagreed() {
        return disagreed;
    }

    /**
     * Set the value of disagreed
     *
     * @param disagreed new value of disagreed
     */
    public void setDisagreed(int disagreed) {
        this.disagreed = disagreed;
    }

    /**
     * Get the value of agreed
     *
     * @return the value of agreed
     */
    public int getAgreed() {
        return agreed;
    }

    /**
     * Set the value of agreed
     *
     * @param agreed new value of agreed
     */
    public void setAgreed(int agreed) {
        this.agreed = agreed;
    }

    public SurveyCategoryTree() {
    }

    public SurveyCategoryTree(SurveyCategoryTree parent, SurveyCategory curNode, List<SurveyCategoryTree> children, List<SurveyQuestionVO> leafNodes) {
        this.parent = parent;
        this.curNode = curNode;
        this.children = children;
        this.leafNodes = leafNodes;
    }

    public boolean isRoot() {
        return (parent == null);
    }

    public boolean isLeaf() {
        return (children == null || children.size() == 0);
    }

    public List<SurveyCategoryTree> getChildren() {
        return children;
    }

    public void addChild(SurveyCategoryTree child) {
        if (child == null) {
            return;
        }
        if (children == null) {
            children = new LinkedList<SurveyCategoryTree>();
        }
        int i = 0;
        for (SurveyCategoryTree aNode : children) {
            if (aNode.curNode != null && child.curNode != null) {
                if (aNode.curNode.getWeight() > child.curNode.getWeight()) {
                    break;
                }
            }
            ++i;
        }
        children.add(i, child);
    }

    public void incrementCompleted() {
        ++completed;
    }

    public void incrementIncompleted() {
        ++incompleted;
    }

    public void addCompleted(int n) {
        completed += n;
    }

    public void addIncompleted(int n) {
        incompleted += n;
    }

    public int getCompleted() {
        return completed;
    }

    public int getIncompleted() {
        return incompleted;
    }

    public List<SurveyQuestionVO> getLeafNodes() {
        return leafNodes;
    }

    public void setLeafNodes(List<SurveyQuestionVO> leafNodes) {
        this.leafNodes = leafNodes;
    }

    public void setChildren(List<SurveyCategoryTree> children) {
        this.children = children;
    }

    public SurveyCategory getCurNode() {
        return curNode;
    }

    public void setCurNode(SurveyCategory curNode) {
        this.curNode = curNode;
    }

    public SurveyCategoryTree getParent() {
        return parent;
    }

    public void setParent(SurveyCategoryTree parent) {
        this.parent = parent;
    }

    /**
     * Get readable string - all the tree
     *
     * @return string
     */
    /*
    @Override
    public String toString() {
    StringBuilder sBuf = new StringBuilder("SurveyCategoryTree:\n");
    sBuf.append("Parent: ");
    if (parent != null) {
    sBuf.append(toString(parent.curNode));
    } else {
    sBuf.append("[NIL]");
    }

    sBuf.append('\n').append(toString(this, "\t"));

    return sBuf.toString();
    }
     */
    private String toString(SurveyCategoryTree tree, String indent) {
        StringBuffer sBuf = new StringBuffer("");
        sBuf.append(indent).append(toString(tree.curNode)).append('\n');
        indent += '\t';
        if (tree.children != null) {
            for (SurveyCategoryTree node : tree.children) {
                sBuf.append(toString(node, indent)).append('\n');
            }
        } else {
            sBuf.append(indent).append("[NIL]");
        }
        return sBuf.toString();
    }

    private String toString(SurveyCategory obj) {
        StringBuilder sBuf = new StringBuilder("SurveyCategory: [");
        if (obj != null) {
            sBuf.append("id=").append(obj.getId()).append(", name=").append(obj.getName()).append(", weight=").append(obj.getWeight()).append(", label=").append(obj.getLabel()).append(", parent=").append(obj.getParentCategoryId());
        }
        sBuf.append(']');

        return sBuf.toString();
    }

    @Override
    public String toString() {
        return "SurveyCategoryTree{" + "completed=" + completed + ", incompleted=" + incompleted + ", agreed=" + agreed + ", disagreed=" + disagreed + ", noQualified=" + noQualified + ", needClarified=" + needClarified + ", unreviewed=" + unreviewed + '}';
    }
}
