/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.common.Config;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Login Action. To help handle the user login request. <br/>
 * 
 * Actually, although struts already provides an useful LoginForm component, in
 * this example we don't plan to leverage it. In a general use, we directly get
 * the request parameters fromHttpServletRequest.
 * 
 * @author Jeff
 * 
 */
public class I18nToolsAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(I18nToolsAction.class);    
    private static String ATTR_AUTHENTICATED = "authenticated";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(ATTR_AUTHENTICATED, true);
        return mapping.findForward(FWD_SUCCESS);
    }

    
}
