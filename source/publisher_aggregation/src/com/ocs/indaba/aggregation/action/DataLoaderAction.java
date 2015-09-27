/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.common.Config;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.tool.DataLoader;
import com.ocs.indaba.util.HttpUtils;
import com.ocs.util.StringUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class DataLoaderAction extends BaseExportAction {

    private static final Logger log = Logger.getLogger(DataLoaderAction.class);
    private static final String PARAM_DEBUG = "debug";
    private static final String PARAM_LOG = "log";
    private static final String PARAM_LOG_TYPE_HTML = "html";
    private static final String PARAM_LOG_TYPE_TEXT = "text";
    private DataLoader dataLoader = null;

    @Autowired
    public void setDataLoader(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }
    //private static final String FWD_AUTO_EXPORT_CHARTS = "autoExportCharts";

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
        String baseDir = request.getParameter(PARAM_BASE_DIR);
        String cacheDir = request.getParameter(PARAM_CACHE_DIR);
        boolean debug = !("0".equals(request.getParameter(PARAM_DEBUG)));
        String logType = (PARAM_LOG_TYPE_TEXT.equals(request.getParameter(PARAM_LOG))) ? request.getParameter(PARAM_LOG) : PARAM_LOG_TYPE_HTML;
        boolean enabled = Config.getBoolean(Constants.KEY_DATA_AGGREGATION_ENABLE, true);

        log.debug("Access from " + request.getRemoteAddr() + ".[baseDir=" + baseDir
                + ", cacheDir=" + cacheDir + ", debug=" + debug + ", logType=" + logType + ", enabled=" + enabled);

        PrintWriter writer = response.getWriter();
        if (!enabled) {
            log.warn("Data loader is disabled!");
            if (PARAM_LOG_TYPE_HTML.equals(logType)) {
                writer.write("[<span style='color:red;font-weight:bold;'>ERROR</span>] <span style='color:orange;font-weight:bold;'>Data aggreation is disabled. Please contact your administrator!</span><br/>");
            } else {
                writer.write("[ERROR] Data aggreation is disabled. Please contact your administrator!");
            }
            return null;
        }

        if (!checkClientIp(request.getRemoteAddr()) && !"0:0:0:0:0:0:0:1%0".equals(request.getRemoteAddr())) {
            log.warn("Client IP is not in whitelist: " + request.getRemoteAddr());
            if (PARAM_LOG_TYPE_HTML.equals(logType)) {
                writer.write("[<span style='color:red;font-weight:bold;'>ERROR</span>] <span style='color:orange;font-weight:bold;'>Client IP is not in whitelist. Please contact your administrator!</span><br/>");
            } else {
                writer.write("[ERROR] Client IP is not in whitelist. Please contact your administrator!");
            }
            return null;
        }

        log.debug("Load data from Builder database.");
        try {
            //DataLoader loader = new DataLoader(baseDir, cacheDir);
            if (StringUtils.isEmpty(baseDir)) {
                baseDir = Config.getString(Constants.KEY_WORK_STORAGE_BASE_PATH);
            }

            if (StringUtils.isEmpty(cacheDir)) {
                cacheDir = Config.getString(Constants.KEY_WORK_CACHE_BASE_PATH);
            }

            dataLoader.setBasedir(baseDir);
            dataLoader.setCacheBaseDir(cacheDir);
            if (debug) {
                dataLoader.setLogWriter(writer);
                dataLoader.setNewLine("<br/>");
            }
            if (PARAM_LOG_TYPE_HTML.equals(logType)) {
                writer.write("<strong>This process may take a few minutes. Please waiting...</strong><br/>");
            } else {
                writer.write("This process may take few minutes. Please waiting...\n");
            }
            writer.flush();

            log.info("Apply 'basedir': " + baseDir);
            log.info("Apply 'cachedir': " + cacheDir);

            writer.write("Apply 'basedir': <strong>" + baseDir + "</strong>.<br/>");
            writer.write("Apply 'cachedir': <strong>" + cacheDir + "</strong>.<br/>");
            writer.write("Apply 'log': <strong>" + logType + "</strong>.<br/>");
            writer.write("Apply 'debug': <strong>" + debug + "</strong>.<br/>");

            dataLoader.clearLoadedData();
            if (PARAM_LOG_TYPE_HTML.equals(logType)) {
                writer.write("[<span style='color:green;font-weight:bold;'>DONE</span>] Clear old SRF/PIF data files.<br/>");
            } else {
                writer.write("[DONE] Clear old SRF/PIF data files.\n");
            }
            writer.write("Loading data from Builder database ...<br/>");
            writer.write("Generating SRF files. Base Path:[<strong>" + baseDir + "]</strong> ...<br/>");
            writer.flush();

            dataLoader.loadSRF();
            writer.flush();
            if (PARAM_LOG_TYPE_HTML.equals(logType)) {
                writer.write("[<span style='color:green;font-weight:bold;'>DONE</span>] SRF files are generated.<br/>");
                writer.write("Generating PIF files Base Path:[<strong>" + baseDir + "]</strong> ...<br/>");
            } else {
                writer.write("[DONE] SRF files are generated.\n");
                writer.write("Generating PIF files Base Path:[" + baseDir + "]...\n");
            }
            writer.flush();

            dataLoader.loadPIF();
            if (PARAM_LOG_TYPE_HTML.equals(logType)) {
                writer.write("[<span style='color:green;font-weight:bold;'>DONE</span>] PIF files are generated.<br/>");
                writer.write("[<span style='color:green;font-weight:bold;'>DONE</span>] Clear old cached files.<br/>");
            } else {
                writer.write("[DONE] PIF files are generated. \n");
                writer.write("[DONE] Clear old cached files.\n");
            }
            writer.flush();

            dataLoader.clearCachedWidgetData();
            if (PARAM_LOG_TYPE_HTML.equals(logType)) {
                writer.write("Caching 'Indicator Summary' data ...<br/>");
            } else {
                writer.write("Caching 'Indicator Summary' data ...\n");
            }
            writer.flush();
            dataLoader.cacheIndicatorSummary();
            if (PARAM_LOG_TYPE_HTML.equals(logType)) {
                writer.write("[<span style='color:green;font-weight:bold;'>DONE</span>] 'Indicator Summary' files are cached.<br/>");
                writer.write("Caching 'Data Summary' data ...<br/>");
            } else {
                writer.write("[DONE] 'Indicator Summary' files are cached.\n");
                writer.write("Caching 'Data Summary' data...\n");
            }
            writer.flush();

            dataLoader.cacheDataSummary();
            if (PARAM_LOG_TYPE_HTML.equals(logType)) {
                writer.write("[<span style='color:green;font-weight:bold;'>DONE</span>] 'Data Summary' files are cached.<br/>");
                writer.write("[<span style='color:green;font-weight:bold;'>SUCCESS</span>]<br/>");
            } else {
                writer.write("[DONE] 'Data Summary' files are cached.\n");
                writer.write("[SUCCESS]\n");
            }
            writer.flush();
        } catch (Exception ex) {
            if (PARAM_LOG_TYPE_HTML.equals(logType)) {
                writer.write("[<span style='color:red;font-weight:bold;'>ERROR</span>] <span style='color:orange;font-weight:bold;'>Server internal error/exception. Please contact your administrator!</span><br/>");
            } else {
                writer.write("[ERROR] Server internal error/exception. Please contact your administrator!\n");
            }
            log.error("Error occurs when generating SRF/PIF files.", ex);
        } finally {
            writer.close();
        }
        //return mapping.findForward(FWD_AUTO_EXPORT_CHARTS);
        return null;
    }

    private boolean checkClientIp(String clientIp) {
        List<String> whitelist = Config.getList(Constants.KEY_DATA_AGGREGATION_WHITELIST);
        if (whitelist == null) {
            return false;
        }
        return HttpUtils.checkClientIp(clientIp, whitelist);
    }
}
