/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.fusioncharts.exporter.FusionChartsExportHelper;
import com.ocs.indaba.aggregation.json.IndicatorSummaryJsonUtils;
import com.ocs.indaba.aggregation.tool.IndicatorSummaryChartGenerator;
import com.ocs.indaba.aggregation.tool.JfreeChartGenerator;
import com.ocs.indaba.util.FileUtil;
import com.ocs.util.StringUtils;
import java.io.File;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeanbone
 */
public class IndicatorSummaryChartAction extends BaseExportAction {

    private static final Logger log = Logger.getLogger(DataSummaryAction.class);
    private static final String PARAM_HORSE_ID = "horseId";

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
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), -1);
        String imageSize = request.getParameter("size");
        String baseDir = request.getParameter(PARAM_BASE_DIR);
        String cacheDir = request.getParameter(PARAM_CACHE_DIR);
        Boolean genericLabel = StringUtils.str2int(request.getParameter("genericLabel"), 0) == 1;
        boolean isLarge = (imageSize != null && imageSize.equals("large")) ? true : false;

        log.info("Get indicator summary chart [horseId=" + horseId + ", version=" + version + " baseDir=" + baseDir
                + ", cacheDir=" + cacheDir + "] - " + genericLabel);

        StringBuilder sb = new StringBuilder();
        sb.append("indicatorSummary_h").append(horseId);
        if (genericLabel) {
            sb.append("_g");
        }
        if (isLarge) {
            sb.append("_l");
        } else {
            sb.append("_s");
        }
        String filename = sb.append(".jpg").toString();
        String fileDir = request.getRealPath("/").concat(
                FusionChartsExportHelper.SAVEPATH);

        JSONObject info = new JSONObject();
        if (new File(fileDir.concat(filename)).exists()) {
            info.put("charts", FusionChartsExportHelper.SAVEPATH.concat(filename));
            response.getWriter().write(info.toJSONString());
            return null;
        }

        JSONObject jsonIndicatorSummary = IndicatorSummaryJsonUtils.toJson(horseId, baseDir, cacheDir, genericLabel);
        if (jsonIndicatorSummary != null && !jsonIndicatorSummary.isEmpty()) {
            String tempPath = fileDir.concat(UUID.randomUUID().toString()).concat(".jpg");
            JfreeChartGenerator chartGen = new IndicatorSummaryChartGenerator(tempPath, jsonIndicatorSummary, isLarge, genericLabel);
            chartGen.createChart();

            if (new File(fileDir.concat(filename)).exists()) {
                info.put("charts", FusionChartsExportHelper.SAVEPATH.concat(filename));
                response.getWriter().write(info.toJSONString());
                return null;
            }
            FileUtil.rename(tempPath, fileDir.concat(filename));
            info.put("charts", FusionChartsExportHelper.SAVEPATH.concat(filename));
        } else {
            info.put("charts", "No Data");
        }
        response.getWriter().write(info.toJSONString());
        return null;
    }
}
