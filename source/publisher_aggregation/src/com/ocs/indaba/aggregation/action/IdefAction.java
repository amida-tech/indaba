/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.action;

import com.ocs.common.Config;
import com.ocs.common.upload.ValumsFileUploader;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.service.IdefService;
import com.ocs.indaba.idef.exporter.GenericExporter;
import com.ocs.indaba.idef.exporter.SimpleExporter;
import com.ocs.indaba.idef.imp.ProcessContext;
import com.ocs.indaba.po.Imports;
import com.ocs.indaba.util.HttpUtils;
import com.ocs.util.FileUtils;
import com.ocs.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.lingala.zip4j.core.ZipFile;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author yc06x
 */
public class IdefAction extends BaseAction {

    private static final Logger log = Logger.getLogger(IdefAction.class);

    private static final String PARAM_IMPORT_ID = "id";
    private static final String PARAM_ACTION = "action";
    private static final String PARAM_PWD = "pwd";
    private static final String PARAM_PRODUCT_ID = "productId";
    private static final String PARAM_PRODUCT_IDS = "prods";

    private static final String PARAM_ACTION_IMPORT = "import";
    private static final String PARAM_ACTION_DELETE = "delete";
    private static final String PARAM_ACTION_UPLOAD = "upload";
    private static final String PARAM_ACTION_EXPORT = "export";
    private static final String PARAM_ACTION_GENERIC_EXPORT = "ge";

    private static final String NEWLINE = "<br>\n";

    @Autowired
    private IdefService idefService = null;


    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String action = request.getParameter(PARAM_ACTION);

        if (PARAM_ACTION_UPLOAD.equalsIgnoreCase(action)) {
            return handleUpload(request, response);
        } else if (PARAM_ACTION_EXPORT.equalsIgnoreCase(action)) {
            return handleSimpleExport(request, response);
        } else if (PARAM_ACTION_GENERIC_EXPORT.equalsIgnoreCase(action)) {
            return handleGenericExport(request, response);
        }

        PrintWriter writer = response.getWriter();

        String pwd = request.getParameter(PARAM_PWD);
        String idefPwd = Config.getString(Constants.KEY_IDEF_PWD);

        if (idefPwd != null && !idefPwd.equals(pwd)) {
            writer.write("[ERROR] Access Denied");
            return null;
        }

        String importIdStr = request.getParameter(PARAM_IMPORT_ID);
        int importId = StringUtils.str2int(importIdStr);

        if (importId < 0) {
            writer.write("[ERROR] Bad import ID: " + importIdStr);
            return null;
        }

        // make sure this import has been defined
        Imports imp = idefService.getImportById(importId);
        if (imp == null) {
            writer.write("[ERROR] Non-existent import definition: " + importIdStr);
            return null;
        }

        if (PARAM_ACTION_DELETE.equalsIgnoreCase(action)) {
            int rc = idefService.deleteIdef(imp);
            if (rc == 0) {
                writer.write("IDEF Package is deleted. <br/>");
                writer.write("[SUCCESS]");
            } else {
                writer.write("Error deleting IDEF Package - code " + rc + ". <br/>");
                writer.write("[FAILURE]");
            }
            return null;
        }

        if (!PARAM_ACTION_IMPORT.equalsIgnoreCase(action)) {
            writer.write("[ERROR] Unknown Action: " + action);
            return null;
        }

        // Import action
        String workDir = Config.getString(Constants.KEY_IDEF_WORK_DIR);
        if (StringUtils.isEmpty(workDir)) {
            writer.write("[ERROR] Import work directory not defined! Please check config parameters.");
            return null;
        }

        String importDir = imp.getFolder();
        if (StringUtils.isEmpty(importDir)) {
            writer.write("[ERROR] Import package directory not specified in Imports " + importId);
            return null;
        }

        String fullDir = workDir + "/" + importDir;

        writer.write("Importing IDEF package from " + fullDir + " ... <br/>");        

        ProcessContext ctx = idefService.importIdef(imp, fullDir);
        
        if (ctx.getErrorCount() > 0) {
            writer.write("Following errors are detected. Please fix them and try again. <br/>");

            List<String> errors = ctx.getErrors();
            for (String err : errors) {
                writer.write("<font color='red'>" + err + "<br/>" + "</font>");
            }
            writer.write("<font color='red'>[FAILURE]</font>");
        } else {
            writer.write("IDEF Package is loaded. <br/>");
            writer.write("[SUCCESS]");
        }

        return null;
    }


    private ActionForward handleSimpleExport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();

        String pwd = request.getParameter(PARAM_PWD);
        String idefPwd = Config.getString(Constants.KEY_IDEF_PWD);

        if (idefPwd != null && !idefPwd.equals(pwd)) {
            writer.write("[ERROR] Access Denied");
            return null;
        }

        String prodIdStr = request.getParameter(PARAM_PRODUCT_ID);
        int prodId = StringUtils.str2int(prodIdStr);

        if (prodId < 0) {
            writer.write("[ERROR] Bad Product ID: " + prodId);
            return null;
        }

        SimpleExporter exporter = idefService.export(prodId);

        List<String> msgs = exporter.getMsgs();
        for (String msg : msgs) {
            writer.append("[INFO] " + msg + NEWLINE);
        }

        String error = exporter.getError();
        if (error != null) {
            writer.append("[ERROR] " + error + NEWLINE);
        } else {
            writer.append("[SUCCESS] export generated: " + exporter.getExportFileName());
        }

        return null;
    }


    private ActionForward handleUpload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            ValumsFileUploader uploader = new ValumsFileUploader(request);
            String fileName = uploader.getFileName();

            log.debug("Received uploaded file: " + fileName);

            String workDir = Config.getString(Constants.KEY_IDEF_WORK_DIR);
            String baseName = UUID.randomUUID().toString();
            String destDirName = workDir + "/" + baseName;
            String baseFileName = baseName + "." + FileUtils.getFileExtention(fileName);
            String destFileName = workDir + "/" + baseFileName;

            FileUtils.storeFile(destFileName, uploader.getData());

            log.debug("Saved zip file in " + destFileName);

            // now unzip into dir
            ZipFile zipFile = new ZipFile(destFileName);
            zipFile.extractAll(destDirName);

            // process it
            ProcessContext ctx = idefService.validateIdef(destDirName);

            // remove the files
            log.debug("Deleting file: " + baseFileName + " from " + workDir);
            FileUtils.deleteFile(baseFileName, workDir);

            log.debug("Deleting directory " + destDirName);
            org.apache.commons.io.FileUtils.deleteDirectory(new File(destDirName));
            
            JSONObject json = new JSONObject();

            if (ctx.getErrorCount() > 0) {
                json.put("success", true);
                StringBuilder sb = new StringBuilder();
                sb.append(ctx.getErrorCount()).append(" errors are detected. Please fix them and try again. <br/>");
                sb.append("<p align='left' style='color:red'>");
                List<String> errors = ctx.getErrors();
                int errIdx = 1;
                for (String err : errors) {
                     sb.append(errIdx).append(": ").append(err).append("<br/>");
                     errIdx++;
                }
                sb.append("</p>");
                json.put("msg", sb.toString());

                log.debug("There are " + ctx.getErrorCount() + " errors.");
            } else {
                json.put("success", true);
                json.put("msg", "The data set is successfully validated!");
                log.debug("Data set is valid.");
            }
            PrintWriter out = response.getWriter();
            out.write(json.toJSONString());

            return null;
        } catch (Exception ex) {
            JSONObject json = new JSONObject();
            json.put("rc", "error");
            json.put("msg", "Internal server error. Please contact system admin.");
            PrintWriter out = response.getWriter();
            out.write(json.toJSONString());
            log.error("Validation Exception: " + ex);
            return null;
        }
    }

    private boolean checkClientIp(String clientIp) {
        List<String> whitelist = Config.getList(Constants.KEY_DATA_AGGREGATION_WHITELIST);
        if (whitelist == null) {
            return false;
        }
        return HttpUtils.checkClientIp(clientIp, whitelist);
    }


    private ActionForward handleGenericExport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();

        String pwd = request.getParameter(PARAM_PWD);
        String idefPwd = Config.getString(Constants.KEY_IDEF_PWD);

        if (idefPwd != null && !idefPwd.equals(pwd)) {
            writer.write("[ERROR] Access Denied");
            return null;
        }

        String prodsStr = request.getParameter(PARAM_PRODUCT_IDS);
        if (StringUtils.isEmpty(prodsStr)) {
            writer.write("[ERROR] No Product IDs");
            return null;
        }

        String idStrs[] = prodsStr.split(" ");
        if (idStrs.length == 0) {
            writer.write("[ERROR] No Product IDs");
            return null;
        }

        int errorCount = 0;
        int prodIds[] = new int[idStrs.length];
        for (int i = 0; i < idStrs.length; i++) {
            prodIds[i] = StringUtils.str2int(idStrs[i], 0);
            if (prodIds[i] == 0) {
                writer.append("[ERROR] Bad product ID: " + idStrs[i] + NEWLINE);
                errorCount++;
            }
        }
        if (errorCount > 0) return null;

        for (int prodId : prodIds) {
            GenericExporter exporter = idefService.exportGeneric(prodId);
            List<String> msgs = exporter.getMsgs();
            for (String msg : msgs) {
                writer.append("[INFO] " + msg + NEWLINE);
            }
            String error = exporter.getError();
            if (error != null) {
                writer.append("[ERROR] " + error + NEWLINE);
                errorCount++;
            }
        }

        if (errorCount == 0) {
            writer.append("[SUCCESS] number of products processed: " + idStrs.length);
        } else {
            writer.append("[FAILED] number of errors: " + errorCount);
        }

        return null;
    }
}
