/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.json.JournalJsonUtils;
import com.ocs.indaba.aggregation.service.JournalSummaryService;
import com.ocs.indaba.aggregation.vo.JournalSummaryInfo;
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
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author rocky
 */
public class JournalSummaryAction extends BaseExportAction {

    private static final Logger log = Logger.getLogger(JournalSummaryAction.class);
    private static final String PARAM_PRODUCT_ID = "productId";
    private static final String PARAM_PROJECT_ID = "projectId";
    private JournalSummaryService journalSummaryService = null;

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
        int productId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID), -1);
        //int projectId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID), -1);
        log.info("Get Jounal summary. [productId=" + productId + ", version=" + version + "]");

        //JournalSummaryInfo journal = journalSummaryService.selectJournalInfoByProductIdandProjectId(productId, projectId);
        JournalSummaryInfo journal = journalSummaryService.getJournalInfoByProductId(productId);

        if (journal == null) {
            return null;
        }
        JSONObject jsonObj = JournalJsonUtils.journalSummary2Json(journal);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.write(jsonObj.toJSONString());
        } finally {
            out.close();
        }
        return null;
    }

    @Autowired
    public void setJournalSummaryService(JournalSummaryService journalSummaryService) {
        this.journalSummaryService = journalSummaryService;
    }
}
