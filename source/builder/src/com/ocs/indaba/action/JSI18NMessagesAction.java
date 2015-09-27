/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.action;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.util.StringUtils;

/**
 *
 * @author Jeff
 */
public class JSI18NMessagesAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(JSI18NMessagesAction.class);
    private static final String LANG_STR = "common.language";

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
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	//no need, this action is work for auth and unauth users.
//    	super.preprocess(mapping, request, response);
    	//
        StringBuilder sBuf = new StringBuilder();
        String lang = (String) request.getAttribute(Constants.ATTR_LANG);
        int langId = Constants.LANG_EN;

        if (!StringUtils.isEmpty(lang)) {
            langId = Integer.parseInt(lang);
        }
        
        Map<String, String> jsRsrcMap = Messages.getInstance().getJSMessages(langId);
        Set<String> keys = jsRsrcMap.keySet();
        sBuf.append("var resources={");
        String text = null;
        for(String k: keys) {
            text = jsRsrcMap.get(k);
            if(text == null) {
                text = "";
            } else {
                text = text.replaceAll("\"", "\\\\\"").replaceAll("\\s+", " ");
            }
            
            sBuf.append('"').append(k).append('"').append(':').append('"').append(text).append("\",");
            //logger.debug(k + "=" + text);
        }
        sBuf.append('"').append(LANG_STR).append('"').append(':').append('"').append(Messages.getInstance().getLanguage(langId)).append('"');
//        sBuf.setLength(sBuf.length() - 1);
        sBuf.append("}\n");
        sBuf.append("$.i18n.setDictionary(resources);");
        super.writeMsgUTF8(response, sBuf.toString());
        return null;
    }

}
