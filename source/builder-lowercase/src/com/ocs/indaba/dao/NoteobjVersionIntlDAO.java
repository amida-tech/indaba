/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.po.NoteobjVersionIntl;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class NoteobjVersionIntlDAO extends SmartDaoMySqlImpl<NoteobjVersionIntl, Integer>  {

    static private final String SELECT_BY_OBJ_VERSION =
            "SELECT * FROM noteobj_version_intl WHERE noteobj_version_id=?";

    public List<NoteobjVersionIntl> getAllVersions(int noteobjVersionId) {
        return super.find(SELECT_BY_OBJ_VERSION, noteobjVersionId);
    }

    public NoteobjVersionIntl getDefault(int noteobjVersionId) {
        return super.findSingle(SELECT_BY_OBJ_VERSION, noteobjVersionId);
    }

    static private final String SELECT_BY_OBJ_AND_LANGUAGE =
            "SELECT * FROM noteobj_version_intl WHERE noteobj_version_id=? AND language_id=?";

    public NoteobjVersionIntl selectByObjAndLanguage(int noteobjVersionId, int langId) {
        return super.findSingle(SELECT_BY_OBJ_AND_LANGUAGE, noteobjVersionId, langId);
    }

}
