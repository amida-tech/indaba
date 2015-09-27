/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */

package com.ocs.indaba.vo;

/**
 * 
 *  Jeff
 */
public class NoteBook implements java.io.Serializable {

    private int id;
    private String name = null;
    private String description = null;
    private int status;
    private int ownerId;
    private int action;
    private String content = null;

    public NoteBook() {
    }

    public NoteBook(int id, int status, int ownerId, int action) {
        this.id = id;
        this.status = status;
        this.ownerId = ownerId;
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
