/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.common.Config;
import com.ocs.indaba.dao.ConfigDAO;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

/**
 * Web application lifecycle listener.
 * @author Jeff
 */
public class InitializationServletListener implements ServletContextListener {

    private static final Logger log = Logger.getLogger(InitializationServletListener.class);

    public void contextInitialized(ServletContextEvent sce) {
        Config config = Config.getInstance();
        try {
            ApplicationContext appCtx = (ApplicationContext)sce.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            ConfigDAO configDao = (ConfigDAO)appCtx.getBean("appConfigDao");
            com.ocs.indaba.po.Config configData = configDao.getConfig();
            config.init(configData.getPublisherConfig());
        } catch (ConfigurationException ex) {
            log.error("Fail to load congfiguration data.", ex);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }
}
