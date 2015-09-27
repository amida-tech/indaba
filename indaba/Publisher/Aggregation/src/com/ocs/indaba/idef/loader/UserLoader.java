/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.loader;

import com.ocs.indaba.idef.imp.ProcessContext;
import com.ocs.indaba.idef.xo.User;
import java.util.Date;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class UserLoader extends Loader {
       

    public UserLoader(ProcessContext ctx) {
        super.createLoader(ctx);
    }

    private void loadOneUser(User userXo) {
        com.ocs.indaba.po.User userPo = new com.ocs.indaba.po.User();

        userPo.setCellPhone(userXo.getCell());
        userPo.setCreateTime(new Date());
        userPo.setDefaultProjectId(0);
        userPo.setEmail(userXo.getEmail());
        userPo.setFirstName(userXo.getFirstName());
        userPo.setLanguageId(1);
        userPo.setLastName(userXo.getLastName());
        userPo.setAddress(userXo.getAddress());
        userPo.setBio(userXo.getBio());
        userPo.setPhone(userXo.getPhone());
        userPo.setUsername(getQualifiedName(userXo.getId()));
        userPo.setStatus((short)0);
        userPo.setPassword("notused");
        userPo.setImportId(ctx.getImportId());

        userDao.create(userPo);
        userXo.setDboId(userPo.getId());
    }


    public void load() {
        try {
            List<User> userList = ctx.getUsers();

            if (userList == null || userList.isEmpty()) {
                ctx.addError("No users to load!");
                return;
            }

            for (User user : userList) {
                loadOneUser(user);
            }

        } catch (Exception ex) {
            ctx.addError("Failed to Load: " + ex);
        }
    }

}
