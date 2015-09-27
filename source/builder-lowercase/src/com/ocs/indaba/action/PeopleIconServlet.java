/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.ErrorCode;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.UserService;
import com.ocs.indaba.util.FilePathUtil;
import com.ocs.indaba.util.SpringContextUtil;
import com.ocs.util.StringUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff Jiang
 */
public class PeopleIconServlet extends ImageServlet {

    private static final Logger logger = Logger.getLogger(ImageServlet.class);
    private static final String PARAM_USER_ID = "uid";
    private UserService userService = (UserService) SpringContextUtil.getBean("userService");

    @Override
    public void init() {
        defaultNotFoundImage = getInitParameter(INIT_PARAM_NOTFOUND);
    }

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
        int uid = StringUtils.str2int(request.getParameter(PARAM_USER_ID), Constants.INVALID_INT_ID);
        if (uid == Constants.INVALID_INT_ID) {
            logger.error("Invalid user id: " + uid + ". Return the default people icon!");
            handleImageNotFound(request, response);
            return;
        }
        User user = userService.getUser(uid);
        String photoPath = FilePathUtil.getPeopleIconPath(user.getPhoto());
        logger.debug("Load people '" + uid + "' icon: [" + photoPath + "].");
        int result = ErrorCode.ERR_UNKNOWN;
        try {
            if (photoPath != null) {
                result = readImageFile(photoPath, response);
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
