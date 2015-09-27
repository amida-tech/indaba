/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.po.Widget;
import com.ocs.indaba.aggregation.service.WidgetService;
import com.ocs.util.StringUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.springframework.beans.factory.annotation.Autowired;

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
public class ManageWidgetAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(ManageWidgetAction.class);
    private static final String PARAM_WIDGET_ID = "widgetId";
    private static final String ATTR_WIDGETS = "widgets";
    private static final String ATTR_WIDGET = "widget";
    private static final String PARAM_MANAGE_WIDGET = "managewidget";
    private WidgetService widgetService = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionForward actionFwd = preprocess(mapping, request);
        if(actionFwd != null) {
            return actionFwd;
        }

        if (!manageWidgetVisible) {
            return noAccess(mapping, request);
        }

        // now that we don't leverage struts ActionForm, we need to get the
        // parameters by ourselves
        int widgetId = StringUtils.str2int(request.getParameter(PARAM_WIDGET_ID), -1);
        logger.debug("Manage widget [id=" + widgetId + "].");
        
        if (widgetId > 0) {
            Widget widget = widgetService.getWidget(widgetId);
        }
        List<Widget> widgets = widgetService.getAllWidgets();
        request.setAttribute(ATTR_WIDGETS, widgets);
        request.setAttribute(ATTR_WIDGET,  widgetService.getWidget(widgetId));
        //request.setAttribute(PARAM_WIDGET_ID, widgetId);

        return mapping.findForward(PARAM_MANAGE_WIDGET);
    }


    @Autowired
    public void setWidgetService(WidgetService widgetService) {
        this.widgetService = widgetService;
    }
}
