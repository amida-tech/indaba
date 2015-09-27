/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.fusioncharts.exporter.FusionChartsExportHelper;
import com.google.gson.Gson;
import com.ocs.indaba.aggregation.json.SparklineDataJsonUtils;
import com.ocs.indaba.aggregation.tool.JfreeChartGenerator;
import com.ocs.indaba.aggregation.tool.SparklineChartGenerator;
import com.ocs.indaba.aggregation.vo.ChartVO;
import com.ocs.util.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeanbone
 */
public class SparklineChartAction extends BaseExportAction {

    private static final String PARAM_HORSE_ID = "horseId";
    private static final String PARAM_PRODUCT_ID = "productId";

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
        int productId = StringUtils.str2int(request.getParameter(PARAM_PRODUCT_ID), -1);
        String horseIdStr = request.getParameter(PARAM_HORSE_ID);
        List<Integer> ids = parseProductIds(horseIdStr);
        JSONObject jsonSparkline = SparklineDataJsonUtils.toJson(productId, ids);
        JSONArray jsonCharts = SparklineDataJsonUtils.getJsonCharts(jsonSparkline);

        int size = jsonCharts.size();
        String fileName, filePath;
        StringBuilder sb = new StringBuilder();
        List<String> chartXmlList = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
            JSONObject jsonChart = (JSONObject) jsonCharts.get(i);
            sb.delete(0, sb.length());
            fileName = sb.append("spark_p").append(productId)
                    .append("_h").append((Integer) jsonChart.get("horseId")).toString();
            sb.insert(0, FusionChartsExportHelper.SAVEPATH);
            filePath = sb.append(".jpg").toString();
            sb.insert(0, request.getRealPath("/"));
            if (new File(sb.toString()).exists()) {
                chartXmlList.add(filePath);
            } else {
                ChartVO chart = SparklineDataJsonUtils.parseJsonChart(jsonChart);

                // Jfree
                JfreeChartGenerator chartGen = new SparklineChartGenerator(sb.toString(), chart);
                chartGen.createChart();
                chartXmlList.add(filePath);
            }
        }

        JSONObject info = new JSONObject();
        info.put("base", jsonSparkline);
        info.put("charts", new Gson().toJson(chartXmlList));
        response.getWriter().write(info.toJSONString());

        return null;
    }

    private List<Integer> parseProductIds(String productIdStr) {
        if (productIdStr == null) {
            return null;
        }
        String[] idArr = productIdStr.split("\\s");
        if (idArr == null) {
            return null;
        }
        List<Integer> ids = new ArrayList<Integer>();
        for (String s : idArr) {
            ids.add(Integer.parseInt(s.trim()));
        }
        return ids;
    }
}
