/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.po.Widget;
import com.ocs.indaba.aggregation.service.ProductService;
import com.ocs.indaba.aggregation.service.WidgetService;
import com.ocs.indaba.aggregation.vo.ProductBriefVO;
import com.ocs.indaba.common.OrgAuthorizer;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.UserService;
import com.ocs.util.StringUtils;
import java.util.ArrayList;
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
public class WidgetLibraryAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(WidgetLibraryAction.class);
    //private ProjectService projectService = null;
    private static final String PARAM_WIDGET_ID = "widgetId";
    private static final String ATTR_PRODUCTS = "products";
    //private static final String ATTR_TARGETS = "targets";
    private static final String ATTR_WIDGETS = "widgets";
    private static final String PARAM_WIDGET_LIBRARY = "widgetlibrary";
    private ProductService productService = null;
    private WidgetService widgetService = null;
    private UserService userService = null;


    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // now that we don't leverage struts ActionForm, we need to get the
        // parameters by ourselves
        preprocess(mapping, request);

        int widgetId = StringUtils.str2int(request.getParameter(PARAM_WIDGET_ID), -1);
        logger.debug("Access widget library [widgetId=" + widgetId + "].");
        List<ProductBriefVO> products = null;
        int widgetType = Constants.WIDGET_TYPE_ALL;
        if (widgetId > 0) {
            Widget widget = widgetService.getWidget(widgetId);
            if (widget != null) {
                widgetType = widget.getContentTypes();
            }
        }

        logger.debug("User ID: " + uid);

        User user = (uid > 0) ? userService.getUser(uid) : null;
        OrgAuthorizer orgAuth = (user == null) ? null : new OrgAuthorizer(user);
        
        switch (widgetType) {
            case Constants.WIDGET_TYPE_JOURNAL:
                products = productService.getAccessibleJournalProducts(orgAuth, 0, null, null);
                break;
            case Constants.WIDGET_TYPE_SURVEY:
                products = productService.getAccessibleTscSurveyProducts(orgAuth);
                break;
            case Constants.WIDGET_TYPE_ALL:
            default:
                products = productService.getAccessibleProducts(orgAuth, 0, null, null);
                break;
        }

        //List<Target> targets = trgtService.getAllTargets();
        List<Widget> widgets = widgetService.getAllWidgets();

        request.setAttribute(ATTR_WIDGETS, (orgAuth != null && orgAuth.isSiteAdmin()) ? widgets : checkWidgets(orgAuth, widgets));
        request.setAttribute(ATTR_PRODUCTS, products);

        //request.setAttribute(ATTR_TARGETS, targets);
        request.setAttribute(PARAM_WIDGET_ID, widgetId);

        return mapping.findForward(PARAM_WIDGET_LIBRARY);
    }


    private List<Widget> checkWidgets(OrgAuthorizer orgAuth, List<Widget> widgets) {
        if (widgets == null || widgets.isEmpty()) {
            return null;
        }

        List<Widget> list = new ArrayList<Widget>();
        for (Widget w : widgets) {
            switch (w.getVisibility()) {
                case Constants.WIDGET_VISIBILITY_PUBLIC: {
                    list.add(w);
                    break;
                }
                case Constants.WIDGET_VISIBILITY_PRIVATE: {
                    if (orgAuth != null && orgAuth.hasOrgAuthority(w.getOrgId())) {
                        list.add(w);
                        continue;
                    }
                    break;
                }
                case Constants.WIDGET_VISIBILITY_AUTHENTICATED: {
                    if (orgAuth != null) {
                        list.add(w);
                    }
                }
                default:
                    break;
            }
        }
        return list;
    }
    

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setWidgetService(WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    @Autowired
    public void setUserService(UserService userSrvc) {
        this.userService = userSrvc;
    }
}
