/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.tool;

import com.ocs.indaba.aggregation.json.DataSummaryJsonUtils;
import com.ocs.indaba.aggregation.vo.ChartVO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeanbone
 */
public class DataSummaryChartGenerator extends JfreeChartGenerator {
    private final String fontName = "arial";

    public DataSummaryChartGenerator(String filePath, JSONObject jsonDataSummary, boolean isLarge, boolean genericLabel) {
        super(filePath, jsonDataSummary, genericLabel);
                
        this.upperBound = 120;
        this.lowerBound = -5;
        this.height = 112;
        if (isLarge) {
            this.width *= 6;
            this.height *= 6;
            this.fontSize *= 5;
            this.gap *= 6;
        }
        this.startMark = this.upperBound * 5/6;
        this.endMark = this.upperBound;
        this.markFontSize = fontSize * 4/5;
        
        this.titleLineNum = 7;
    }

    @Override
    public void setSubTitles() {
        title = jsonChart.get("surveyName") + ": " + jsonChart.get("target");
        
        subTitles.add(new TextTitle(jsonChart.get("studyPeroid").toString(), 
                new Font(fontName, Font.PLAIN, fontSize)));
        
        subTitles.add(new TextTitle("  ", 
                new Font(fontName, Font.PLAIN, fontSize * 1/5)));
        
        subTitles.add(new TextTitle("Overall Rating: " + jsonChart.get("overallLabel") + 
                " (" + Math.round(Double.parseDouble(
                jsonChart.get("overall").toString())) + " of 100)", 
                new Font(fontName, Font.PLAIN, fontSize * 6/7)));
        
        subTitles.add(new TextTitle("  ", 
                new Font(fontName, Font.PLAIN, fontSize * 1/5)));
        
        subTitles.add(new TextTitle("Legal Framework: " + Math.round(Double.parseDouble(
                jsonChart.get("legalFramework").toString())) + " of 100", 
                new Font(fontName, Font.PLAIN, fontSize * 6/7)));
        subTitles.add(new TextTitle("Actual Implementation: " + Math.round(Double.parseDouble(
                jsonChart.get("implementation").toString())) + " of 100", 
                new Font(fontName, Font.PLAIN, fontSize * 6/7)));
        subTitles.add(new TextTitle("Implementation Gap: (" + Math.round(Double.parseDouble(
                jsonChart.get("gap").toString())) + ")", 
                new Font(fontName, Font.PLAIN, fontSize * 6/7)));
        
        subTitles.add(new TextTitle("  ", 
                new Font(fontName, Font.PLAIN, fontSize * 2/5)));
        
        if (this.genericLabel) {
            subTitles.add(new TextTitle("Comparison to Peers", 
                new Font(fontName, Font.BOLD, fontSize)));
        } else {
            subTitles.add(new TextTitle(jsonChart.get("chartTitle").toString(), 
                new Font(fontName, Font.BOLD, fontSize)));
        }
        subTitles.add(new TextTitle("  ", 
                new Font(fontName, Font.PLAIN, fontSize * 3/5)));
    }

    class CustomRenderer extends BarRenderer {

        private Paint colors[] = {Color.decode("#8BBA00"), Color.decode("#D6D6D6")};
        int targetNdx;

        public CustomRenderer(int targetNdx) {
            this.targetNdx = targetNdx;
            this.setShadowVisible(false);
            this.setBaseItemLabelsVisible(false);
            this.setDrawBarOutline(false);
            this.setBarPainter(new StandardBarPainter());
            this.setItemMargin(0.0);
        }

        @Override
        public Paint getItemPaint(int i, int j) {
            if (j == targetNdx) {
                return colors[0];
            }
            return colors[1];
        }
    }

    @Override
    public List<CategoryDataset> createDataset() {
        JSONArray jsonCharts = DataSummaryJsonUtils.getJsonCharts(jsonChart);
        List<CategoryDataset> datasets = new ArrayList<CategoryDataset>();
        for (int i = 0; i < jsonCharts.size(); i++) {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            int cntZero = 0;
            ChartVO chart = DataSummaryJsonUtils.parseJsonChart((JSONObject) jsonCharts.get(i));
            Double[] vals = chart.getValues();
            for (int j = 0, size = vals.length; j < size; j++) {
                if (vals[j] == 0.0) {
                    cntZero++;
                    continue;
                }
                dataset.setValue(vals[j], "Series", "" + j);
            }
            datasets.add(dataset);
            
            // sub chart title
            subChartTitles.add(chart.getChartName());
            
            // Renderer
            CustomRenderer cusRen = new CustomRenderer(chart.getTargetIndex() - cntZero);
            customRenderer.add(cusRen);
        }
        return datasets;
    }
}
