/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.dao;

import com.ocs.indaba.aggregation.po.Config;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author Luke
 */
public class ConfigDAO extends SmartDaoMySqlImpl<Config, Integer> {

    private static final Logger log = Logger.getLogger(ConfigDAO.class);
    private static Map<Integer, Config> configMap = null;

    private void loadConfig() {
        List<Config> configList = super.findAll();
        if (configList != null && !configList.isEmpty()) {
            configMap = new HashMap<Integer, Config>();
            for (Config config : configList) {
                configMap.put(config.getId(), config);
            }
        }
    }

    public Config getConfig() {
        if (configMap == null) {
            loadConfig();
        }
        return (configMap == null || configMap.isEmpty()) ? null : configMap.get(1);
    }

    public Config get(int id) {
        if (configMap == null) {
            loadConfig();
        }
        return (configMap == null || configMap.isEmpty()) ? null : configMap.get(id);
    }
}
