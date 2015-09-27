/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class SimpleTree<T> {

    private T value = null;
    SimpleTree<T> parent = null;
    List<SimpleTree<T>> children = null;

    public SimpleTree(T value) {
        this.value = value;
    }

    public void addChild(T value) {
        addChild(new SimpleTree<T>(value));
    }

    public void addChild(SimpleTree<T> child) {
        if (child == null) {
            return;
        }
        if (children == null) {
            children = new ArrayList<SimpleTree<T>>();
        }
        children.add(child);
        child.parent = this;
    }

    public T getValue() {
        return value;
    }

    public SimpleTree<T> getParent() {
        return parent;
    }

    public List<SimpleTree<T>> getChildren() {
        return children;
    }

    public void delete(SimpleTree<T> child) {
        if (child == null) {
            return;
        }

        SimpleTree<T> p = child.parent;
        if (p == null) {
            return;
        }
        p.children.remove(child);
        child.parent = null;
    }

    public boolean isRoot() {
        return (parent == null);
    }

    public boolean isLeaf() {
        return (children == null || children.isEmpty());
    }

    static private class SimpleTreeComparator<T> implements Comparator<SimpleTree<T>> {

        private Comparator<T> valueComparator;

        public void setValueComparator(Comparator<T> c) {
            this.valueComparator = c;
        }

        public int compare(SimpleTree<T> x, SimpleTree<T> y) {
            T vx = x.getValue();
            T vy = y.getValue();

            return valueComparator.compare(vx, vy);
        }
    }

    public void sort(Comparator<T> valueComparator) {
        SimpleTreeComparator<T> treeComparator = new SimpleTreeComparator<T>();
        treeComparator.setValueComparator(valueComparator);
        sort(treeComparator);
    }

    private void sort(SimpleTreeComparator<T> comparator) {
        if (children == null || children.isEmpty()) {
            return;
        }
        Collections.sort(children, comparator);

        // also sort all children
        for (SimpleTree<T> child : children) {
            child.sort(comparator);
        }
    }

    public interface ValueHandler<T> {

        public boolean handle(T value, Object... appData);
    }

    public interface NodeHandler<T> {

        public boolean handle(SimpleTree<T> value, Object... appData);
    }

    /**
     * Wide-First Search
     * @param handler
     * @param appData
     * @return
     */
    public boolean traverse(ValueHandler<T> handler, Object... appData) {
        boolean contiueTraverse = handler.handle(this.value, appData);

        if (!contiueTraverse) {
            return false;
        }

        // traverse children
        if (children == null || children.isEmpty()) {
            return true;
        }

        for (SimpleTree<T> child : children) {
            contiueTraverse = child.traverse(handler, appData);
            if (!contiueTraverse) {
                return false;
            }
        }

        return true;
    }

    /**
     * Depth-First Search
     * @param handler
     * @param appData
     * @return
     */
    public static <E> boolean traverseByDfs(SimpleTree<E> tree, NodeHandler<E> handler, Object... appData) {
        if (tree == null) {
            return false;
        }

        boolean contiueTraverse = false;
        List<SimpleTree<E>> children = tree.getChildren();
        // traverse children
        if (children != null && !children.isEmpty()) {
            for (SimpleTree<E> child : children) {
                contiueTraverse = child.traverseByDfs(child, handler, appData);
                if (!contiueTraverse) {
                    return false;
                }
            }
        }

        contiueTraverse = handler.handle(tree, appData);

        if (!contiueTraverse) {
            return false;
        }
        return true;
    }
}
