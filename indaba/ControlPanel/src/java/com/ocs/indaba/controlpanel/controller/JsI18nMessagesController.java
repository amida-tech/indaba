/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.controller;

import java.util.Map;
import java.util.Set;


import org.apache.log4j.Logger;
//import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.controlpanel.common.ControlPanelConstants;
import org.apache.struts2.convention.annotation.Action;

/**
 *
 * @author Jeff
 */
public class JsI18nMessagesController extends BaseController {

    private static final Logger logger = Logger.getLogger(JsI18nMessagesController.class);
    //private static final String ATTR_WORKFLOW_LIST = "workflowList";
    private static final String LANG_STR = "common.language";
    private static final StringBuilder jsI18nResources = new StringBuilder();

    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Action("/jsI18nMessages")
    public String index() {
        //no need, this action is work for auth and unauth users.
        super.sendResponseMessage(getJsI18nResoruces());

        logger.debug("Sent I18N Resources to client.");

        return null;
    }

    private synchronized String getJsI18nResoruces() {
        if (jsI18nResources.length() <= 0) {
            try {
                int langId = super.getLanguageId();
                Map<String, String> jsRsrcMap = Messages.getInstance().getMessagesWithRegexKey(langId, ControlPanelConstants.I18N_KEY_REGEX);
                Set<String> keys = jsRsrcMap.keySet();
                jsI18nResources.append("var resources={");
                String text = null;
                for (String k : keys) {
                    text = jsRsrcMap.get(k);
                    if (text == null) {
                        text = "";
                    } else {
                        text = text.replaceAll("\"", "\\\\\"").replaceAll("\\s+", " ");
                    }
                    jsI18nResources.append('"').append(k).append('"').append(':').append('"').append(text).append("\",");
                }
                jsI18nResources.append('"').append(LANG_STR).append('"').append(':').append('"').append(Messages.getInstance().getLanguage(langId)).append('"');
                jsI18nResources.append("}\n");
                jsI18nResources.append("$.i18n.setDictionary(resources);");
            } catch (Exception ex) {
                logger.error("Error Occurs!", ex);
            }
        }
        return jsI18nResources.toString();
    }
}
