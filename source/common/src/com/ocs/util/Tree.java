/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jeff Jiang
 */
public class Tree<T, K> {

    private List<Tree<T, K>> subTrees;
    private List<Leaf<K>> leaves;
    private T value;

    public Tree() {
    }

    public Tree(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public List<Tree<T, K>> getSubTrees() {
        return subTrees;
    }

    public void addTree(Tree<T, K> tree) {
        if (subTrees == null) {
            subTrees = new ArrayList<Tree<T, K>>();
        }
        subTrees.add(tree);
    }

    public void setSubTrees(List<Tree<T, K>> subTrees) {
        this.subTrees = subTrees;
    }

    public List<Leaf<K>> getLeaves() {
        return leaves;
    }

    public void setLeaves(List<Leaf<K>> leaves) {
        this.leaves = leaves;
    }

    public void addLeaf(Leaf<K> leaf) {
        if (leaves == null) {
            leaves = new ArrayList<Leaf<K>>();
        }
        leaves.add(leaf);
    }
    public boolean isLeaf() {
        return ((subTrees == null || subTrees.isEmpty()) && (leaves == null || leaves.isEmpty()));
    }

    @Override
    public String toString() {
        return "Tree{" + "subTrees=" + subTrees + ", leaves=" + leaves + ", value=" + value + '}';
    }

}