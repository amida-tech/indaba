/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.json.DataSummaryJsonUtils;
import com.ocs.util.StringUtils;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff
 */
public class DataSummaryAction extends BaseExportAction {

    private static final Logger log = Logger.getLogger(DataSummaryAction.class);
    private static final String PARAM_HORSE_ID = "horseId";

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
        prehandle(mapping, request);
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), -1);
        String baseDir = request.getParameter(PARAM_BASE_DIR);
        String cacheDir = request.getParameter(PARAM_CACHE_DIR);
        Boolean genericLabel = StringUtils.str2int(request.getParameter("genericLabel"), 0) == 1;

        log.info("Get data summary - [horseId=" + horseId + ", version=" + version + " baseDir=" + baseDir
                + ", cacheDir=" + cacheDir + "] - " + genericLabel);

        JSONObject jsonObj = DataSummaryJsonUtils.toJson(horseId, baseDir, cacheDir, genericLabel);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
           out.write(jsonObj.toJSONString());
        } finally {
            out.close();
        }
        return null;
    }
}
