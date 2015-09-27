/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Jeff
 */
public class JsonViewerAction extends BaseExportAction {

    private static final Logger log = Logger.getLogger(JsonViewerAction.class);
    private static final String PARAM_URL = "url";
    private static final String FWD_JSON_VIEWER = "jsonViewer";

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String url = request.getParameter(PARAM_URL);
        log.debug("Load URL in Json viewer: " + url);

        if (url == null) {
            return null;
        }
        url = url.trim();
        if (url.endsWith("&")) {
            url = url.substring(0, url.lastIndexOf('&'));
        }
        if (url.endsWith("?")) {
            url = url.substring(0, url.lastIndexOf('?'));
        }

        url = url.replaceAll(" ", "");
        request.setAttribute(PARAM_URL, url.replaceAll("\\+", "%2B"));
        return mapping.findForward(FWD_JSON_VIEWER);
    }
}
