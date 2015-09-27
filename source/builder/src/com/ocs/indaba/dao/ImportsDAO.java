/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Imports;

/**
 *
 * @author yc06x
 */
public class ImportsDAO extends SmartDaoMySqlImpl<Imports, Integer> {

    public int deleteImport(int importId) {
        return super.call("delete_import", importId);
    }
}
