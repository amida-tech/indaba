/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ocs.indaba.aggregation.builder.FileBuilder;
import com.ocs.indaba.aggregation.builder.MysqlBuilder;

import org.apache.log4j.Logger;

/**
 *
 * @author luke
 */
public class AggregateAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(AggregateAction.class);
    
    /**
     * This is the action called from the Struts framework.
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
        String target = request.getParameter("target");
        if (target == null || !target.equals("file")) {
            MysqlBuilder builder = new MysqlBuilder();
            builder.build();
        } else {
            FileBuilder builder = new FileBuilder();
            builder.build();
        }
        return null;
    }

}
