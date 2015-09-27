/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.po.Widget;
import com.ocs.indaba.aggregation.service.WidgetService;
import com.ocs.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONObject;

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
public class SaveWidgetAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SaveWidgetAction.class);
    private static final String PARAM_WIDGET_ID = "widgetId";
    private static final String PARAM_WIDGET_DISPLAY_NAME = "displayName";
    private static final String PARAM_WIDGET_DESC = "desc";
    private static final String PARAM_WIDGET_AUTHOR = "author";
    private WidgetService widgetService = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // now that we don't leverage struts ActionForm, we need to get the
        // parameters by ourselves
        int widgetId = StringUtils.str2int(request.getParameter(PARAM_WIDGET_ID), -1);
        String displayName = request.getParameter(PARAM_WIDGET_DISPLAY_NAME);
        String desc = request.getParameter(PARAM_WIDGET_DESC);
        String author = request.getParameter(PARAM_WIDGET_AUTHOR);
        Widget widget = widgetService.getWidget(widgetId);
        logger.debug("Save widget: id=" + widgetId + ", displayName=" + displayName + ", desc="+ desc + ", suthor=" + author);
        int ret = 1;
        String errMsg = "FAIL";
        if (widget == null) {
            ret = 1;
            errMsg = "FAIL";
            logger.debug("Fail to saved.");
        } else {
            widget.setDisplayName(displayName);
            widget.setDescription(desc);
            widget.setAuthor(author);
            widgetService.updateWidget(widget);
            ret = 0;
            errMsg = "OK";
            logger.debug("Success to saved.");
        }
        super.writeMsg(response, toJson(ret, errMsg));
        return null;
    }

    public String toJson(int ret, String desc) {
        JSONObject jsonObj = new JSONObject();

        jsonObj.put("ret", ret);
        jsonObj.put("desc", desc);
        return jsonObj.toJSONString();
    }

    @Autowired
    public void setWidgetService(WidgetService widgetService) {
        this.widgetService = widgetService;
    }
}
