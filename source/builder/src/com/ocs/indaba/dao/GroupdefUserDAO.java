/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.po.GroupdefUser;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class GroupdefUserDAO extends SmartDaoMySqlImpl<GroupdefUser, Integer> {

    private static final String SELECT_BY_DEF_AND_USER =
            "SELECT * FROM groupdef_user WHERE groupdef_id=? AND user_id=?";

    public GroupdefUser selectByDefAndUser(int groupdefId, int userId) {
        return super.findSingle(SELECT_BY_DEF_AND_USER, groupdefId, userId);
    }


    private static final String SELECT_BY_GROUPOBJ =
            "SELECT gdu.* FROM groupdef_user gdu, groupobj obj WHERE obj.id=? AND gdu.groupdef_id=obj.groupdef_id";

    public List<GroupdefUser> selectByGroupobj(int groupobjId) {
        return super.find(SELECT_BY_GROUPOBJ, groupobjId);
    }
}