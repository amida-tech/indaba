/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.util.FileStorageUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;

import com.ocs.indaba.workflow.WorkflowEngine;

import java.io.File;
import java.io.FilenameFilter;
import java.io.OutputStream;
//import java.util.StringTokenizer;

/**
 *
 * @author luke
 */
public class RuleManagerAction extends com.ocs.indaba.action.BaseAction {

    private static final Logger logger = Logger.getLogger(RuleManagerAction.class);
    private static final File RULEFILE_BASE_DIR = new File(WorkflowEngine.class.getResource("WorkflowEngine.class").getFile()).getParentFile();

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
        String content = request.getParameter("content");
        String path = request.getParameter("path");
        if (path == null || path.length() == 0)
            path = RULEFILE_BASE_DIR.getPath();
        logger.debug("=====>>>Path: " + path);
        String action = request.getParameter("action");
        logger.debug("=====>>>Action: " + action);
        String fileName = request.getParameter("fileName");
        logger.debug("=====>>>Filename: " + fileName);

        if ("delete".equals(action)) {
            FileStorageUtil.deleteFile(fileName, path);
            return null;
        }

        if ("save".equals(action)) {
            FileStorageUtil.saveFile(fileName, path, content);
            return null;
        }

        if ("merge".equals(action)) {
            try {
                String rules = request.getParameter("rules");
                logger.debug("=====>>>Rules: " + rules + " <<<===");
                FileStorageUtil.mergeFiles(response, RULEFILE_BASE_DIR.getPath(), rules);
            } catch (Exception ex) {
                logger.debug("Error occurs", ex);
            }
            return null;
        }

        if ("load".equals(action)) {
            try {
                OutputStream out = response.getOutputStream();
                FileStorageUtil.loadStream(out, WorkflowEngine.class.getResourceAsStream(fileName), false, true);
                out.close();
            } catch (Exception ex) {
                logger.debug("Error occurs", ex);
            }
            return null;
        }

        FilenameFilter ruleFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".drl") && Character.isUpperCase(name.charAt(0));
            }
        };

        FilenameFilter ruleFileFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".drl") && Character.isLowerCase(name.charAt(0));
            }
        };

        String[] rules = RULEFILE_BASE_DIR.list(ruleFilter);
        request.setAttribute("rules", rules);
        String[] files = RULEFILE_BASE_DIR.list(ruleFileFilter);
        request.setAttribute("files", files);
        request.setAttribute("path", path);
        return mapping.findForward(FWD_SUCCESS);
    }
}
