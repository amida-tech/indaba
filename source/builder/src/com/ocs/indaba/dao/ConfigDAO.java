/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Config;

/**
 *
 * @author yc06x
 */
public class ConfigDAO extends SmartDaoMySqlImpl<Config, Integer> {

    public Config getConfig() {
        return super.get(1);
    }
}
