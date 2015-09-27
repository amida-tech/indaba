/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

import com.ocs.indaba.po.NotedefUser;

/**
 *
 * @author ningshan
 */
public class NotedefUserView extends NotedefUser {
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "NotedefUserView{" + "userName=" + userName + '}';
    }
    
}
