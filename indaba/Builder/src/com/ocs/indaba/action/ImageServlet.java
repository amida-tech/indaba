/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.action;

import com.ocs.common.Config;
import com.ocs.indaba.common.ErrorCode;
import com.ocs.indaba.util.FilePathUtil;
import com.ocs.util.StringUtils;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff Jiang
 */
public class ImageServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ImageServlet.class);
    private static final String DEFAULT_NOTFOUND_IMAGE = "/images/notfound.jpg";
    private static final String PARAM_IMAGE_ID = "id";
    private static final int BUF_SIZE = 1024;
    protected static final String INIT_PARAM_NOTFOUND = "notfound";
    protected String defaultNotFoundImage = null;

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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter(PARAM_IMAGE_ID);
        logger.debug("Load image [id=" + id + "].");
        int result = ErrorCode.ERR_UNKNOWN;
        try {
            if (id != null) {
                result = readImageFile(Config.getString(Config.KEY_STORAGE_IMAGE_BASE), id, response);
            }
        } catch (Exception ex) {
            logger.error("Read image error. ", ex);
        } finally {
            if (result != ErrorCode.OK) {
                logger.info("Image not found. Redirect to the default image!");
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
        return "Image Servlet";
    }// </editor-fold>

    protected int readImageFile(String filename, HttpServletResponse response) throws IOException {
        return readImageFile(new File(filename), response);
    }

    protected int readImageFile(String basePath, String filename, HttpServletResponse response) throws IOException {
        return readImageFile(new File(basePath, filename), response);
    }

    protected int readImageFile(File imgFile, HttpServletResponse response) throws IOException {
        logger.debug("Read image file: " + imgFile.getCanonicalPath());
        String suffix = FilePathUtil.getFileExtention(imgFile.getName());
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType(getImageContentType(suffix));
        if (imgFile.exists()) {
            readImage(new FileInputStream(imgFile), response.getOutputStream());
            return ErrorCode.OK;
        } else {
            logger.warn("Can't find image: " + imgFile.getCanonicalPath());
            return ErrorCode.ERR_FILE_NOT_FOUND;
        }
    }

    protected void readImage(InputStream in, OutputStream out) throws IOException {
        InputStream bufIn = null;
        OutputStream bufOut = null;
        try {
            bufIn = new BufferedInputStream(in);
            bufOut = new BufferedOutputStream(out);

            byte[] buf = new byte[BUF_SIZE];
            int n = -1;
            while ((n = bufIn.read(buf, 0, BUF_SIZE)) != -1) {
                bufOut.write(buf, 0, n);
            }
            bufOut.flush();
        } finally {
            if (bufIn != null) {
                bufIn.close();
            }
            if (bufOut != null) {
                bufOut.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    protected void handleImageNotFound(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (defaultNotFoundImage == null) {
            defaultNotFoundImage = DEFAULT_NOTFOUND_IMAGE;
        }
        response.sendRedirect(request.getContextPath() + defaultNotFoundImage);
    }

    private String getImageContentType(String suffix) {
        String subtype = "jpeg";
        if (StringUtils.isEmpty(suffix)) {
            return "image/jpeg";
        }
        suffix = suffix.toLowerCase();
        if ("jpg".equals(suffix) || "jpeg".equals(suffix) || "jpe".equals(suffix)) {
            subtype = "jpeg";
        } else if ("bmp".equals(suffix)) {
            subtype = "bmp";
        } else if ("gif".equals(suffix)) {
            subtype = "gif";
        } else if ("png".equals(suffix)) {
            subtype = "png";
        } else if ("svn".equals(suffix)) {
            subtype = "svg+xml";
        } else if ("ief".equals(suffix)) {
            subtype = "ief";
        } else if ("cod".equals(suffix)) {
            subtype = "cis-cod";
        } else if ("jfif".equals(suffix)) {
            subtype = "pipeg";
        } else if ("tiff".equals(suffix) || "tif".equals(suffix)) {
            subtype = "tiff";
        } else if ("ico".equals(suffix)) {
            subtype = "x-icon";
        } else if ("rgb".equals(suffix)) {
            subtype = "x-rgb";
        } else {
            subtype = suffix;
        }
        return "image/" + subtype;
    }
}
