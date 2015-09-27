/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.po.GroupdefRole;

/**
 *
 * @author yc06x
 */
public class GroupdefRoleDAO extends SmartDaoMySqlImpl<GroupdefRole, Integer> {

    private static final String SELECT_BY_DEF_AND_ROLE =
            "SELECT * FROM groupdef_role WHERE groupdef_id=? AND role_id=?";

    public GroupdefRole selectByDefAndRole(int groupdefId, int roleId) {
        return super.findSingle(SELECT_BY_DEF_AND_ROLE, groupdefId, roleId);
    }


    private static final String SELECT_BY_DEF_AND_USER =
            "SELECT gdr.* FROM groupdef def, groupdef_role gdr, product prod, project_membership pm, task_assignment ta " +
            "WHERE def.id=? AND prod.id=def.product_id AND pm.project_id=prod.project_id AND pm.user_id=? " +
            "AND gdr.groupdef_id=def.id AND gdr.role_id=pm.role_id AND ta.horse_id=? AND ta.assigned_user_id=pm.user_id";

    public GroupdefRole selectByDefAndUserOnHorse(int groupdefId, int userId, int horseId) {
        return super.findSingle(SELECT_BY_DEF_AND_USER, groupdefId, userId, horseId);
    }

}