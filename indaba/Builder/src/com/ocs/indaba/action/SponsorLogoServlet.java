/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.ErrorCode;
import com.ocs.indaba.util.FilePathUtil;
import com.ocs.util.StringUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff
 */
public class SponsorLogoServlet extends ImageServlet {

    private static final Logger logger = Logger.getLogger(SponsorLogoServlet.class);
    private static final String PARAM_LOGO_ID = "logoid";
    private static final String PARAM_PROJECT_ID = "prjid";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String logoId = request.getParameter(PARAM_LOGO_ID);
        int prjId = StringUtils.str2int(request.getParameter(PARAM_PROJECT_ID), Constants.INVALID_INT_ID);
        if (StringUtils.isEmpty(logoId) || StringUtils.isEmpty(logoId)) {
            logger.error("Logo id or prject id is invalid: [prjid=" + prjId + ", logoId=" + logoId + "]. Return the default people icon!");
            handleImageNotFound(request, response);
            return;
        }
        String logoPath = FilePathUtil.getSponsorLogoPath(prjId, logoId);
        logger.debug("Load people '" + prjId + "' icon: [" + logoPath + "].");
        int result = ErrorCode.ERR_UNKNOWN;
        try {
            if (logoPath != null) {
                result = readImageFile(logoPath, response);
            }
        } catch (Exception ex) {
            logger.error("Read image error. ", ex);
        } finally {
            if (result != ErrorCode.OK) {
                logger.info("Image not found. Redirect to the default image: " + defaultNotFoundImage);
                this.handleImageNotFound(request, response);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
