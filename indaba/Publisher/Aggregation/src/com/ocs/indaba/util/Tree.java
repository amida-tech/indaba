/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jiangjeff
 */
public class Tree<T> {

    private T node = null;
    private Tree<T> parent = null;
    private List<Tree<T>> children = null;

    public Tree() {
    }

    public Tree(T node) {
        this(node, null);
    }
    
    public Tree(T node, Tree<T> parent) {
        this.node = node;
        this.parent = parent;
    }

    public Tree(T node, Tree<T> parent, List<Tree<T>> children) {
        this.node = node;
        this.parent = parent;
        this.children = children;
    }

    public boolean isLeaf() {
        return (children == null || children.isEmpty());
    }

    public void addChild(Tree<T> node) {
        if (children == null) {
            children = new ArrayList<Tree<T>>();
        }
        children.add(node);
    }

    public List<Tree<T>> getChildren() {
        return children;
    }

    public void setChildren(List<Tree<T>> children) {
        this.children = children;
    }

    public T getNode() {
        return node;
    }

    public void setNode(T node) {
        this.node = node;
    }

    public Tree<T> getParent() {
        return parent;
    }

    public void setParent(Tree<T> parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Tree{" + "node=" + node + "parent=" + parent + "children=" + children + '}';
    }

}
