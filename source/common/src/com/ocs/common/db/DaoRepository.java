/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.common.db;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

/**
 *
 * @author Tiger Tang
 */
@SuppressWarnings("unchecked")
public class DaoRepository {

    private static final Logger logger = Logger.getLogger(DaoRepository.class);

    private static final Map<Class<? extends Serializable>, SmartDao> daoMap = new ConcurrentHashMap<Class<? extends Serializable>, SmartDao>();

    public static void registerDao(Class<? extends Serializable> clazz, SmartDao dao) {
        daoMap.put(clazz, dao);
    }

    public static SmartDao getDao(Class<? extends Serializable> clazz) {
        if (!daoMap.containsKey(clazz)) {
            logger.debug("Dao layer may not initialized properly.");
        }

        return daoMap.get(clazz);
    }
}
