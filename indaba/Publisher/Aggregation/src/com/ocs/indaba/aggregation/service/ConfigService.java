/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.service;

import com.ocs.indaba.aggregation.po.Config;
import com.ocs.indaba.aggregation.dao.ConfigDAO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Luke
 */
public class ConfigService {
    
    private ConfigDAO configDao;

    public Config getConfig(){
        return configDao.get(1);
    }

    @Autowired
    public void setConfigDao(ConfigDAO configDao) {
        this.configDao = configDao;
    }

}
