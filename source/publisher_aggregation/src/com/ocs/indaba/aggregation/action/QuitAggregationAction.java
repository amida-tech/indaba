/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.service.AggregationService;
import com.ocs.indaba.aggregation.vo.AggregationForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author luwb
 */
public class QuitAggregationAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(QuitAggregationAction.class);
    private AggregationService aggretationService = null;
    private static final String ATTR_ERRMSG = "errMsg";
   private static final String ATTR_AGGREGATION = "aggregation";

    @Autowired
    public void setAggregationService(AggregationService aggretationService) {
        this.aggretationService = aggretationService;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionForward actionFwd = preprocess(mapping, request);
        if (actionFwd != null) {
            return actionFwd;
        }

        AggregationForm aggForm = (AggregationForm) request.getSession().getAttribute(ATTR_AGGREGATION);
        if (aggForm != null)
            request.getSession().removeAttribute(ATTR_AGGREGATION);


        response.sendRedirect("manageaggregation.do?worksetId=" + request.getParameter("worksetId"));
        return null;
        //return mapping.findForward(FWD_SUCCESS);
    }
}
