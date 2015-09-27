/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.common.Config;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.tool.BaseScorecardReporter;
import com.ocs.indaba.aggregation.tool.ScorecardReporter;
import com.ocs.indaba.aggregation.tool.SimpleScorecardReporter;
import com.ocs.indaba.dao.HorseDAO;
import com.ocs.indaba.po.SurveyConfig;
import com.ocs.indaba.service.SurveyConfigService;
import com.ocs.indaba.util.FilePathUtil;
import com.ocs.util.StringUtils;
import com.ocs.indaba.util.ZipUtils;
import com.ocs.indaba.vo.HorseInfo;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class ScorecardReportAction extends BaseExportAction {

    private static final Logger log = Logger.getLogger(ScorecardReportAction.class);
    private static final String PARAM_PRODUCT_ID = "productId";
    private static final String PARAM_LANG = "lang";
    private static final String PARAM_EXPORT_FILENAME = "exportFilename";
    //private static final String PARAM_INCLUDE_OPTIONS = "includeOptions";
    private static final String PARAM_ANONYMIZE_REPORT = "anonymizeReport";
    private static final String PARAM_INCLUDE_AUTHOR_COMMENTS = "includeAuthorComments";
    private static final String PARAM_INCLUDE_REFERENCES = "includeReferences";
    private static final String PARAM_INCLUDE_PEER_REVIEWS = "includePeerReviews";
    private static final String PARAM_INCLUDE_STAFF_REVIEWS = "includeStaffReviews";
    private static final String PARAM_INCLUDE_DISCUSSIONS = "includeDiscussions";
    private static final String PARAM_INCLUDE_GLOBAL_INTEGRITY = "includeGlobalIntegrity";
    private static final String PARAM_INCLUDE_ATTACHED_FILES = "includeAttachedFiles";
    private static final String PARAM_INCLUDE_ANSWER_LABELS = "includeAnswerLabels";
    private static final String PARAM_INCLUDE_SCORING_OPTIONS = "includeScoringOptions";
    private static final String PARAM_KEEP_CRLF = "keepCRLF";
    private static final String PARAM_INCLUDE_TREE = "includeTree";
    private static final String PARAM_FORMAT = "format";
    private static final String FORMAT_EXCEL = "excel";

    private SurveyConfigService scService = null;
    private HorseDAO horseDao = null;

    @Autowired
    public void setSurveyConfigService(SurveyConfigService service) {
        this.scService = service;
    }

    @Autowired
    public void setHorseDao(HorseDAO dao) {
        this.horseDao = dao;
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionForward actionFwd = prehandle(mapping, request);
        if (actionFwd != null) {
            return actionFwd;
        }

        if (!exportScorecardReportVisible) {
            return noAccess(mapping, request);
        }
        
        productId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID), -1);

        int langId = StringUtils.str2int(request.getParameter(PARAM_LANG), -1);

        log.debug("Language ID: " + langId);
        
        String exportFilename = request.getParameter(PARAM_EXPORT_FILENAME);
        int includeOptions = 0;
        includeOptions |= StringUtils.str2int(request.getParameter(PARAM_ANONYMIZE_REPORT), 0);
        includeOptions |= StringUtils.str2int(request.getParameter(PARAM_INCLUDE_AUTHOR_COMMENTS), 0);
        includeOptions |= StringUtils.str2int(request.getParameter(PARAM_INCLUDE_REFERENCES), 0);
        includeOptions |= StringUtils.str2int(request.getParameter(PARAM_INCLUDE_PEER_REVIEWS), 0);
        includeOptions |= StringUtils.str2int(request.getParameter(PARAM_INCLUDE_STAFF_REVIEWS), 0);
        includeOptions |= StringUtils.str2int(request.getParameter(PARAM_INCLUDE_DISCUSSIONS), 0);
        includeOptions |= StringUtils.str2int(request.getParameter(PARAM_INCLUDE_GLOBAL_INTEGRITY), 0);
        includeOptions |= StringUtils.str2int(request.getParameter(PARAM_INCLUDE_ATTACHED_FILES), 0);
        includeOptions |= StringUtils.str2int(request.getParameter(PARAM_INCLUDE_ANSWER_LABELS), 0);
        includeOptions |= StringUtils.str2int(request.getParameter(PARAM_INCLUDE_SCORING_OPTIONS), 0);
        includeOptions |= StringUtils.str2int(request.getParameter(PARAM_KEEP_CRLF), 0);
        includeOptions |= StringUtils.str2int(request.getParameter(PARAM_INCLUDE_TREE), 0);
        
        //boolean includeGlobalIntegrity = StringUtils.str2boolean(request.getParameter(PARAM_INCLUDE_GLOBAL_INTEGRITY), false);
        //boolean includeAttachedFiles = StringUtils.str2boolean(request.getParameter(PARAM_INCLUDE_ATTACHED_FILES), false);

        String format = request.getParameter(PARAM_FORMAT);

        log.info("Export product. [productId=" + productId
                + ",\n\texportFilename=" + exportFilename 
                + ",\n\tformat=" + format 
                + ",\n\tincludeOptions=" + includeOptions
                + ",\n\tanonymizeReport=" + Constants.anonymizeReport(includeOptions)
                + ",\n\tincludeAuthorComments=" + Constants.includeAuthorComments(includeOptions)
                + ",\n\tincludeReferences=" + Constants.includeReferences(includeOptions)
                + ",\n\tincludePeerReviews=" + Constants.includePeerReviews(includeOptions)
                + ",\n\tincludeStaffReviews=" + Constants.includeStaffReviews(includeOptions)
                + ",\n\tincludeDiscussions=" + Constants.includeDiscussions(includeOptions)
                + ",\n\tincludeGlobalIntegrity=" + Constants.includeGlobalIntegrity(includeOptions)
                + ",\n\tincludeAttachedFiles=" + Constants.includeAttachedFiles(includeOptions)
                + ",\n\tincludeAnswerLabels=" + Constants.includeAnswerLabels(includeOptions)
                + ",\n\tkeepCRLF=" + Constants.keepCRLF(includeOptions)
                + ",\n\tincludeTree=" + Constants.includeTree(includeOptions)
                + ",\n\tincludeScoringOptions=" + Constants.includeScoringOptions(includeOptions) + "]");

        List<BaseScorecardReporter> reporters = null;

        if (Constants.includeTree(includeOptions)) {
            SurveyConfig sc = scService.getSurveyConfigOfProduct(productId);
            
            if (sc == null) {
                log.error("No survey config for product " + productId);
                return null;
            }
            
            if (sc.getIsTsc()) {
                reporters = createScorecardReporters(productId, includeOptions, langId);
            } else {
                log.debug("Product " + productId + " not TSC - export simple structure.");
                reporters = createSimpleScorecardReporters(productId, includeOptions, langId);
            }
        } else {
            reporters = createSimpleScorecardReporters(productId, includeOptions, langId);
        }

        if (reporters == null) {
            log.error("Cannot create scorecardReporter for product " + productId);
            return null;
        }

        List<Workbook> workbooks = new ArrayList<Workbook>();

        for (BaseScorecardReporter reporter : reporters) {
            workbooks.add(reporter.createWorkbook());
        }

        OutputStream out = response.getOutputStream();
        if (reporters.size() == 1 && !reporters.get(0).hasMultipleFiles()) {
            BaseScorecardReporter reporter = reporters.get(0);
            Workbook workbook = workbooks.get(0);

            setResponseAttachmentAndContentType(response, exportFilename);
            //response.setHeader("Content-Disposition", "attachment; filename=" + exportFilename);
            if (FORMAT_EXCEL.equals(format)) {
                //response.setContentType(MIME_TYPE_EXCEL);
                reporter.exportToExcel(workbook, out);
            } else {
                //response.setContentType(MIME_TYPE_CSV);
                reporter.exportToCSV(workbook, out);
            }
        } else {
            String baseFileName = FilePathUtil.getFilename(exportFilename);
            String fileExt = FilePathUtil.getFileExtention(exportFilename);

            setResponseAttachmentAndContentType(response, baseFileName + ".zip");

            int reporterIdx = 1;
            File exportPath = null;
            for (int i = 0; i < reporters.size(); i++) {
                BaseScorecardReporter reporter = reporters.get(i);
                Workbook workbook = workbooks.get(i);

                String fileName = (reporters.size() == 1) ? baseFileName + "." + fileExt : baseFileName + "." + reporterIdx + "." + fileExt;

                if (exportPath == null) exportPath = reporter.getOrCreateExportFolderFile();

                FileOutputStream fos = new FileOutputStream(new File(exportPath, fileName));
                OutputStream fOut = new BufferedOutputStream(fos);

                if (FORMAT_EXCEL.equals(format)) {
                    //response.setContentType(MIME_TYPE_EXCEL);
                    reporter.exportToExcel(workbook, fOut);
                } else {
                    //response.setContentType(MIME_TYPE_CSV);
                    reporter.exportToCSV(workbook, fOut);
                }
                fOut.flush();
                fOut.close();

                // CLOSEFILE
                fos.close();

                reporterIdx++;
            }

            ZipUtils.zipAll(exportPath, baseFileName, out);
        }

        for (BaseScorecardReporter reporter : reporters) {
            reporter.cleanup();
        }
        
        // This should not be closed - it's managed by the container. YC
        // out.close();

        return null;
    }



    private List<BaseScorecardReporter> createScorecardReporters(int productId, int includeOptions, int langId) {
        ScorecardReporter reporter = new ScorecardReporter(productId, includeOptions, langId);
        if (reporter == null) return null;

        ArrayList<BaseScorecardReporter> reporters = new ArrayList<BaseScorecardReporter>();
        reporters.add(reporter);
        return reporters;
    }


    private List<BaseScorecardReporter> createSimpleScorecardReporters(int productId, int includeOptions, int langId) {
        int maxHorsesPerSheet = Config.getInt(Constants.KEY_EXPORT_MAX_HORSES_PER_SHEET, 100);
        List<HorseInfo> horses = horseDao.selectNotCancelledHorsesByProductId(productId);

        if (horses == null || horses.isEmpty()) return null;

        if (horses.size() <= maxHorsesPerSheet) {
            SimpleScorecardReporter reporter = new SimpleScorecardReporter(productId, includeOptions, langId, horses);
            if (reporter == null) return null;

            ArrayList<BaseScorecardReporter> reporters = new ArrayList<BaseScorecardReporter>();
            reporters.add(reporter);
            return reporters;
        }

        ArrayList<BaseScorecardReporter> reporters = new ArrayList<BaseScorecardReporter>();
        SimpleScorecardReporter reporter1 = new SimpleScorecardReporter(productId, includeOptions, langId, horses.subList(0, maxHorsesPerSheet));
        reporters.add(reporter1);

        int numSheets = (horses.size() - 1) / maxHorsesPerSheet + 1;

        for (int i = 1; i < numSheets; i++) {
            int startIdx = maxHorsesPerSheet * i;
            int endIdx = startIdx + maxHorsesPerSheet;
            if (endIdx > horses.size()) endIdx = horses.size();
            
            SimpleScorecardReporter reporter = new SimpleScorecardReporter(reporter1, horses.subList(startIdx, endIdx));
            reporters.add(reporter);
        }

        return reporters;
    }

}
