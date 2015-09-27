/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.po.NotedefUser;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class NotedefUserDAO extends SmartDaoMySqlImpl<NotedefUser, Integer> {

    private static final String SELECT_BY_DEF_AND_USER =
            "SELECT * FROM notedef_user WHERE notedef_id=? AND user_id=?";

    public NotedefUser selectByDefAndUser(int notedefId, int userId) {
        return super.findSingle(SELECT_BY_DEF_AND_USER, notedefId, userId);
    }


    private static final String SELECT_BY_NOTEOBJ =
            "SELECT ndu.* FROM notedef_user ndu, noteobj obj WHERE obj.id=? AND ndu.notedef_id=obj.notedef_id";

    public List<NotedefUser> selectByNoteobj(int noteobjId) {
        return super.find(SELECT_BY_NOTEOBJ, noteobjId);
    }
}
