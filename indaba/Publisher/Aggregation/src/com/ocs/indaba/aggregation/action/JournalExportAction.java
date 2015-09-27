/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.service.JournalObjectService;
import com.ocs.indaba.aggregation.vo.JournalContentObjectInfo;
import com.ocs.indaba.aggregation.json.JournalJsonUtils;
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
 * @author Jeff
 */
public class JournalExportAction extends BaseExportAction {

    private static final Logger log = Logger.getLogger(JournalExportAction.class);
    private static final String PARAM_HORSE_ID = "horseId";
    private static final String PARAM_INCLUDE_REVIEWS = "includeReviews";
    private static final int INCLUDE_REVIEWS = 1;
    private JournalObjectService journalObjectService = null;

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
        int includeReviews = StringUtils.str2int(request.getParameter(PARAM_INCLUDE_REVIEWS), -1);
        log.info("Get Jounal data. [horseId=" + horseId + ", includeReviews=" + includeReviews + ", version=" + version + "]");

        JournalContentObjectInfo journal = journalObjectService.getJournalInfoByHorseId(horseId);
        if (journal != null) {
            journal.setAttachments(journalObjectService.getAttachmentsByHorseId(horseId));
            if (includeReviews == INCLUDE_REVIEWS) {
                journal.setJournalReviews(journalObjectService.getJournalPeerReviewByContentObjectId(journal.getId()));
            }
        }
        JSONObject jsonObj = JournalJsonUtils.journal2Json(journal);
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
    public void setJournalObjectService(JournalObjectService journalObjectService) {
        this.journalObjectService = journalObjectService;
    }
}
