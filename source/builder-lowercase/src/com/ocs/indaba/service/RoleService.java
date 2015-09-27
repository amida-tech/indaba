/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.dao.RoleDAO;
import com.ocs.indaba.po.Role;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author menglong
 */
public class RoleService {

    private RoleDAO roleDAO;

    public String getRoleByProjectIdAndUserId(int projectId, int userId) {
        return roleDAO.selectRoleNameByProjectIdAndUserId(projectId, userId);
    }

    @Autowired
    public void setRoleDao(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    public List<Role> getRolesByProjectId(int prjid, int uid) {
        // FIXME - add permission control logic here

        return roleDAO.selectRolesByProjectId(prjid);
    }

    public List<Role> getAllRoles(int prjid) {

        return roleDAO.selectRolesByProjectId(prjid);
    }

    public List<Role> getAllRoles() {

        return roleDAO.findAll();
    }

    public List<Role> getRoleByProductId(int productId) {
        return roleDAO.selectRoleByProductId(productId);
    }
}
