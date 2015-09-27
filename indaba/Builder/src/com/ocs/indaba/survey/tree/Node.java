/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.survey.tree;

/**
 *
 * @author yc06x
 */
public class Node {

    static public final short NODE_TYPE_CATEGORY = 1;
    static public final short NODE_TYPE_QUESTION = 2;
    private short type;
    private int id;
    private int parentId;
    private int weight;
    private String name;
    private Node next;
    private Node prev;

    private int level;
    private int depth;
    private Node parent;
    private Object appData;

    public Node(short type, int id, String name, int parentId, int weight) {
        this.type = type;
        this.id = id;
        this.parentId = parentId;
        this.weight = weight;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getParentId() {
        return parentId;
    }

    public short getType() {
        return type;
    }

    public int getWeight() {
        return weight;
    }

    public void setNext(Node node) {
        this.next = node;
    }

    public Node getNext() {
        return next;
    }

    public void setPrev(Node node) {
        this.prev = node;
    }

    public Node getPrev() {
        return prev;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int v) {
        level = v;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int v) {
        depth = v;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node p) {
        parent = p;
    }

    public Object getAppData() {
        return appData;
    }

    public void setAppData(Object data) {
        appData = data;
    }
}
