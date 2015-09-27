/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
*/

package com.ocs.indaba.vo;

import com.ocs.indaba.po.User;
import java.util.List;

/**
 *
 * @author menglong
 */
public class TeamInfo {
    private Integer id;
    private String name;
    private List<User> userList;
    //private List<UserDisplay> userDisplays;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
    
}
