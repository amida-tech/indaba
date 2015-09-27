/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author luwb
 */
public class SpringContextUtil implements ApplicationContextAware {
  private static ApplicationContext applicationContext;     //Spring Eviroment


  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringContextUtil.applicationContext = applicationContext;
  }

  /**
  * @return ApplicationContext
  */
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }
 
  public static Object getBean(String name) throws BeansException {
    return applicationContext.getBean(name);
  }

  public static Object getBean(String name, Class requiredType) throws BeansException {
    return applicationContext.getBean(name, requiredType);
  }

  public static boolean containsBean(String name) {
    return applicationContext.containsBean(name);
  }
}
