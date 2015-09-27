/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.vo.WorksetForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author luwb
 */
public class QuitWorksetAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(QuitWorksetAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionForward actionFwd = preprocess(mapping, request);
        if (actionFwd != null) {
            return actionFwd;
        }

        WorksetForm worksetForm = (WorksetForm) request.getSession().getAttribute(ATTR_WORKSET);
        if (worksetForm != null)
            request.getSession().removeAttribute(ATTR_WORKSET);

        response.sendRedirect("manageworkset.do");
        return null;
        //return mapping.findForward(FWD_SUCCESS);
    }
}
