/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.builder;

import com.ocs.common.Config;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.service.AggregationService;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author luwb
 */
public abstract class BaseBuilder {
    private final static Logger log = Logger.getLogger(BaseBuilder.class);

    protected static final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    protected static AggregationService aggregationService = null;
    protected final static String KEY_WORKSET_ID = "worksetId";
    protected final static String KEY_STUDY_PERIOD_ID = "studyPeriodId";
    protected final static String KEY_TARGET_ID = "targetId";
    protected final static String KEY_INDICATOR_ID = "indicatorId";
    protected final static String KEY_DATAPOINT_ID = "datapointId";
    protected final static String KEY_ORG_ID = "orgId";
    protected final static String KEY_INDICATORS = "indicators";
    protected final static String KEY_DATAPOINTS = "datapoints";
    protected final static String KEY_VALUE = "value";
    protected static boolean initialized = false;
    

    static {
        init();
    }

    protected static void init() {
        if(initialized){
            return;
        }
        aggregationService = (AggregationService) ctx.getBean("aggregationService");
        
        initialized = true;
    }

    static protected String getRootPath() {
        return Config.getString(Constants.KEY_WORKSET_BASE_PATH);
    }

    static protected String getPublishedSuffix() {
        return Config.getString(Constants.KEY_WORKSET_PUBLISHED_SUFFIX);
    }

    static protected String getUsableSuffix() {
        return Config.getString(Constants.KEY_WORKSET_USABLE_SUFFIX);
    }

    public abstract void build();
}
