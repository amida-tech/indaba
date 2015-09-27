/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.common.Config;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.common.ContentType;
import com.ocs.indaba.util.FileStorageUtil;
import java.io.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author sjf
 */
public class ExportDownloadAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(ExportDownloadAction.class);
    private static final String EXPORT_ZIP_NAME = "/indaba_export.zip";
    private static final int BLOCK_SIZE = 4096;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionForward actionFwd = preprocess(mapping, request);
        /*
        if (actionFwd != null) {
            logger.info("User session is invalid. Redirect to login page!");
            return actionFwd;
        }
        */
        
        String exportTmpZipFolder = "/" + request.getParameter("zipFolder") + "_" + (uid < 0 ? session.getAttribute("temp_uid") : uid);
        String exportBasePath = Config.getString(Constants.KEY_EXPORT_BASE_PATH);
        String zipPath = exportBasePath + exportTmpZipFolder + EXPORT_ZIP_NAME;
        File zipFile = new File(zipPath);
        FileInputStream fis = new FileInputStream(zipFile);
        InputStream bufIn = new BufferedInputStream(fis);
        response.setContentType(ContentType.getContentType(FileStorageUtil.getSuffix(zipPath)));
        response.setHeader("Content-Length", zipFile.length() + "");
        response.setHeader("Content-Disposition", "Export;filename=" + EXPORT_ZIP_NAME.substring(1));
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

//        File exportFile = new File(EXPORT_BASE_PATH + EXPORT_TEMP_ZIP_FOLDER);
//        File []files = exportFile.listFiles();
//        for (int i = 0; i < files.length; i++) {
//            files[i].delete();
//        }
//        exportFile.delete();
//        session.removeAttribute("process_flag");
        return null;
    }
}
