/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.common.ContentType;
import com.ocs.indaba.util.FileStorageUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Jeanbone
 */
public class HighResDownloadAction extends BaseExportAction {

    private static final Logger logger = Logger.getLogger(HighResDownloadAction.class);
    private static final int BLOCK_SIZE = 4096;

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
        
        prehandle(mapping, request);
        
        String filePath = request.getRealPath("/") + "images/export/" + request.getParameter("filename");
        logger.info(filePath);
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        InputStream bufIn = new BufferedInputStream(fis);
        response.setContentType(ContentType.getContentType(FileStorageUtil.getSuffix(filePath)));
        response.setContentType("application/force-download");
        response.setHeader("Content-Length", file.length() + "");
        response.setHeader("Content-Disposition", "Export;filename=" + filePath.substring(filePath.lastIndexOf("/") + 1));
        OutputStream bufOut = new BufferedOutputStream(response.getOutputStream());
        byte[] block = new byte[BLOCK_SIZE];
        int n = -1;
        while ((n = bufIn.read(block, 0, BLOCK_SIZE)) != -1) {
            bufOut.write(block, 0, n);
        }
        bufOut.flush();
        bufOut.close();

        // CLOSEFILE
        fis.close();
        bufIn.close();
        
        return null;
    }
}
