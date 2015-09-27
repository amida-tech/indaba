/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.common.Config;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.common.ContentType;
import com.ocs.indaba.util.ExecuteOsCmdUtil;
import com.ocs.indaba.util.FileStorageUtil;
import com.ocs.util.StringUtils;

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
 * Export PDF for scorecard summary
 * @author davidcui Feb. 2012
 *
 */
public class ExportPdfForScorecardAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(ExportPdfForScorecardAction.class);
    private static final String EXPORT_PDF_NAME = "VerticalScorecardSummary";
    private static final int BLOCK_SIZE = 4096;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	preprocess(mapping, request);
//        ActionForward actionFwd = preprocess(mapping, request);
        /*
        if (actionFwd != null) {
            logger.info("User session is invalid. Redirect to login page!");
            return actionFwd;
        }
        */
        //
        String horseId = request.getParameter("horseId");
        String subcatIds = request.getParameter("subcatIds");
        if(StringUtils.isEmpty(horseId) || StringUtils.isEmpty(subcatIds)){
            return null;
        }
        
        String onlyExportSubcat = request.getParameter("onlyExportSubcat");
        String rwi = request.getParameter("rwi");
        String partFileName;
        String contextLocal = Config.getString(Constants.KEY_AGGREGATION_LOCAL_CONTEXT_PATH);
        
        String mainpage = (rwi != null) ?
            contextLocal+"widgets/vcardDisplay4RWI.html?horseId="+horseId+"&includeLogo=1&version=1&exportPdf=1 " :
            contextLocal+"widgets/vcardDisplay.html?horseId="+horseId+"&includeLogo=1&version=1&exportPdf=1 ";
        
        String prefix = (rwi != null) ?
            contextLocal+"widgets/vcardDisplayIndicators4RWI.html?horseId="+horseId+"&includeLogo=1&version=1&exportPdf=1&subcatId=" :
            contextLocal+"widgets/vcardDisplayIndicators.html?horseId="+horseId+"&includeLogo=1&version=1&exportPdf=1&subcatId=";

        String[] urls;

        logger.debug("SubcatIDs: " + subcatIds);
        
        if (onlyExportSubcat != null) {
            // Only the subpage - one URL
            partFileName = horseId + "_" +subcatIds;
            urls = new String[1];
            urls[0] = prefix + subcatIds;
        } else {
            // Main page plus all subpages
            partFileName = horseId;
            String[] subcatIdArr = subcatIds.split(",");
            urls = new String[subcatIdArr.length];

            urls[0] = mainpage;  // the firts of suncatIds is empty so ignore it!
            for (int i = 1; i < subcatIdArr.length; i++){
                urls[i] = prefix + subcatIdArr[i];
            }
        }
        
        //String exportTmpPdfFolder = "/pdf_" + horseId;
        //String pdfPath = EXPORT_BASE_PATH + exportTmpPdfFolder + EXPORT_PDF_NAME;
        String exportBasePath = Config.getString(Constants.KEY_EXPORT_BASE_PATH);
        File baseFile = new File(exportBasePath);
        String basePath = baseFile.getCanonicalPath();

        String pdfPath = basePath + "/" + EXPORT_PDF_NAME + partFileName + ".pdf";
        
        //
        File pdfFileJudge = new File(pdfPath);

        try {

            if(!pdfFileJudge.exists()){
            	String referer = request.getHeader("Referer");

            	if(referer == null){
                    referer = request.getHeader("referer");
            	}
            	String redirectPage = "window.location.href='"+referer+"';";
                
                
                long start = System.currentTimeMillis();
                StringBuffer error = new StringBuffer();
                int result = ExecuteOsCmdUtil.execWkhtmltopdf(basePath, urls, pdfPath, error);

                if (result < 0) {
                    super.writeMsgLn(response, "<script>alert('Failed to convert to PDF.');"+redirectPage+"</script>");
                    return null;
                }
           
                long end = System.currentTimeMillis();
                logger.info("Export PDF: timeConsume: "+(end-start)+"ms,horseId: "+horseId+", subcatIds: "+subcatIds+", fileName: "+pdfPath +", tips: "+result+" "+error);
            }else{
            	logger.info("Existing PDF: horseId: "+horseId+", subcatIds: "+subcatIds+", pdfFile: "+pdfPath);
            }
        
            File pdfFile = new File(pdfPath);
            FileInputStream fis = new FileInputStream(pdfFile);

            InputStream bufIn = new BufferedInputStream(fis);
            response.setContentType(ContentType.getContentType(FileStorageUtil.getSuffix(pdfPath)));
            response.setContentType("application/force-download");
            response.setHeader("Content-Length", pdfFile.length() + "");
            response.setHeader("Content-Disposition", "Export;filename=" + EXPORT_PDF_NAME + partFileName + ".pdf");
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
        } catch (Exception e) {}

        return null;
    }






}
