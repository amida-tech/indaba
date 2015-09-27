/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.listener;

import com.ocs.common.Config;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.dao.ConfigDAO;
import com.ocs.util.UserSessionManager;
import javax.servlet.ServletContext;
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
    private static final String INIT_PARAM_INDABA_CONFIG_FILE = "indabaConfigFile";
    private static final String INIT_PARAM_INTL_RESOURCE_DIR = "intlResourceDir";

    private static final Logger logger = Logger.getLogger(InitializationServletListener.class);
    
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        Config config = Config.getInstance();
        try {
            // get the configDao. Somehow autowire doesn't work in here. Need to get the bean explicitly.
            ApplicationContext appCtx = (ApplicationContext)sce.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            ConfigDAO configDao = (ConfigDAO)appCtx.getBean("configDao");
            com.ocs.indaba.po.Config configData = configDao.getConfig();
            config.init(configData.getCpConfig());

            // config.init(servletContext.getResourceAsStream(configFile));
        } catch (ConfigurationException ex) {
            logger.error("Fail to load congfiguration data.", ex);
        }

        // load intl resources
        String intlResourceDir = servletContext.getInitParameter(INIT_PARAM_INTL_RESOURCE_DIR);
        logger.debug("Load intl resources from: " + intlResourceDir);
        try {
            Messages.init(servletContext.getRealPath(intlResourceDir));
        } catch (Exception ex) {
            logger.error("Fail to initialize messages: " + intlResourceDir, ex);
        }

        // start user session token monitor
        UserSessionManager.getInstance().startMonitor();
    }

    public void contextDestroyed(ServletContextEvent sce) {
        UserSessionManager.getInstance().stopMonitor();
    }

}
