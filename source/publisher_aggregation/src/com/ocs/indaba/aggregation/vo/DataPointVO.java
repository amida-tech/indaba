/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.vo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jeanbone
 */
public class DataPointVO {

    private int id;
    private String name;
    private String method;
    private List<String> members;
    protected static final String COLUMN_SEPARATOR = ",";
    protected static final String OBJECT_SEPARATOR = "|";
    protected static final String PART_SEPARATOR = ":";

    public String[] toCSVLine() {
        List<String> csvCols = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        csvCols.add("D" + id);
        csvCols.add(name);
        csvCols.add(method);

        int i, size;
        for (i = 0, size = members.size(); i < size - 1; i++) {
            sb.append(members.get(i)).append(OBJECT_SEPARATOR);
        }
        if (i < members.size()) {
            sb.append(members.get(i));
        }
        csvCols.add(sb.toString());

        return csvCols.toArray(new String[]{});
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("D").append(id).append(COLUMN_SEPARATOR).append(name).append(COLUMN_SEPARATOR).append(method).append(COLUMN_SEPARATOR);

        int i, size;
        for (i = 0, size = members.size(); i < size - 1; i++) {
            sb.append(members.get(i)).append(OBJECT_SEPARATOR);
        }
        if (i < members.size()) {
            sb.append(members.get(i));
        }
        return sb.append("\r\n").toString();
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * @return the members
     */
    public List<String> getMembers() {
        return members;
    }

    /**
     * @param members the members to set
     */
    public void setMembers(List<String> members) {
        this.members = members;
    }
}
