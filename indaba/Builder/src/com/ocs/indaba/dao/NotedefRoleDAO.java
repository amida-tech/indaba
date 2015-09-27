/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.po.NotedefRole;

/**
 *
 * @author yc06x
 */
public class NotedefRoleDAO extends SmartDaoMySqlImpl<NotedefRole, Integer> {

    private static final String SELECT_BY_DEF_AND_ROLE =
            "SELECT * FROM notedef_role WHERE notedef_id=? AND role_id=?";

    public NotedefRole selectByDefAndRole(int notedefId, int roleId) {
        return super.findSingle(SELECT_BY_DEF_AND_ROLE, notedefId, roleId);
    }


    private static final String SELECT_BY_DEF_AND_USER =
            "SELECT ndr.* FROM notedef nd, notedef_role ndr, product prod, project_membership pm, task_assignment ta " +
            "WHERE nd.id=? AND prod.id=nd.product_id AND pm.project_id=prod.project_id AND pm.user_id=? " +
            "AND ndr.notedef_id=nd.id AND ndr.role_id=pm.role_id AND ta.horse_id=? AND ta.assigned_user_id=pm.user_id";

    public NotedefRole selectByDefAndUserOnHorse(int notedefId, int userId, int horseId) {
        return super.findSingle(SELECT_BY_DEF_AND_USER, notedefId, userId, horseId);
    }

}
