/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.tool;

import com.ocs.indaba.aggregation.vo.ChartVO;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedDomainCategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.Title;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.Layer;
import org.jfree.ui.LengthAdjustmentType;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeanbone
 */
public abstract class JfreeChartGenerator {
    private final String fontName = "helvetica";
    
    String filePath, title;
    JSONObject jsonChart;
    ChartVO chart;
    List<Title> subTitles;
    List<String> subChartTitles;
    List<BarRenderer> customRenderer;
    int width, height;
    int startMark, endMark, markFontSize;
    int upperBound, lowerBound;
    int fontSize, titleLineNum;
    boolean xVisible, yVisible;
    double catMargin, gap;
    boolean genericLabel;

    private JfreeChartGenerator(String filePath, boolean genericLabel) {
        this.filePath = filePath;
        this.xVisible = false;
        this.yVisible = true;
        this.width = 300;
        this.height = 90;
        this.fontSize = 14;
        this.catMargin = 0.1;
        this.gap = 10;
        this.titleLineNum = 0;
        this.genericLabel = genericLabel;
    }

    public JfreeChartGenerator(String filePath, JSONObject jsonChart, boolean genericLabel) {
        this(filePath, genericLabel);
        this.jsonChart = jsonChart;
        this.chart = null;
        this.subTitles = new ArrayList<Title>();
        this.subChartTitles = new ArrayList<String>();
        this.customRenderer = new ArrayList<BarRenderer>();
    }

    public JfreeChartGenerator(String filePath, ChartVO chart) {
        this(filePath, false);
        this.jsonChart = null;
        this.chart = chart;
        this.subTitles = null;
        this.subChartTitles = null;
        this.customRenderer = null;
    }

    public CategoryAxis getDomainAxis() {
        CategoryAxis domainAxis = new CategoryAxis();
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        domainAxis.setVisible(xVisible);
        domainAxis.setCategoryMargin(catMargin);
        return domainAxis;
    }

    public ValueAxis getRangeAxis(String label) {
        ValueAxis rAxis = new NumberAxis(label);
        rAxis.setUpperMargin(this.height / 20);
        rAxis.setLowerMargin(this.height / 20);
        rAxis.setLowerBound(lowerBound);
        rAxis.setUpperBound(upperBound);
        rAxis.setTickLabelFont(new Font(fontName, Font.PLAIN, markFontSize));
        rAxis.setVisible(yVisible);
        return rAxis;
    }

    public IntervalMarker getMarker(double start, double end, String label, TextAnchor anchor) {
        IntervalMarker marker = new IntervalMarker(start, end);
        marker.setLabel(label);
        marker.setLabelFont(new Font(fontName, Font.PLAIN, markFontSize));
        marker.setLabelOffsetType(LengthAdjustmentType.CONTRACT);
        marker.setLabelAnchor(RectangleAnchor.LEFT);
        marker.setLabelTextAnchor(anchor);
        marker.setPaint(Color.WHITE);
        marker.setOutlinePaint(Color.WHITE);        
        return marker;
    }

    public abstract void setSubTitles();

    public abstract List<CategoryDataset> createDataset();

    public void createChart() {
        setSubTitles();
        List<CategoryDataset> datasets = createDataset();
        CombinedDomainCategoryPlot parent = new CombinedDomainCategoryPlot(getDomainAxis());
        ValueAxis defaultRAxis = getRangeAxis("");

        for (int i = 0; i < datasets.size(); i++) {
            CategoryPlot subPlot = new CategoryPlot(datasets.get(i), null, defaultRAxis, customRenderer.get(i));
            subPlot.setBackgroundPaint(Color.WHITE);
            subPlot.setDomainGridlinesVisible(false);
            subPlot.setRangeGridlinesVisible(false);
            subPlot.setOutlinePaint(Color.WHITE);

            if (subChartTitles != null && !subChartTitles.isEmpty()) {
                IntervalMarker marker = new IntervalMarker(startMark, endMark);

                marker.setLabelFont(new Font(fontName, Font.PLAIN, markFontSize));
                marker.setLabelOffsetType(LengthAdjustmentType.CONTRACT);
                marker.setLabelAnchor(RectangleAnchor.LEFT);
                marker.setPaint(Color.WHITE);
                marker.setOutlinePaint(Color.RED);

                String subChartTitle = subChartTitles.get(i);
                if (subChartTitle.length() < 60) {
                    subPlot.addRangeMarker(getMarker(startMark, endMark, subChartTitle, TextAnchor.CENTER_LEFT), Layer.FOREGROUND);
                } else {
                    int index = subChartTitle.indexOf(" ", subChartTitle.length() / 2);
                    subPlot.addRangeMarker(
                            getMarker(startMark + (endMark - startMark) / 2, endMark, 
                            subChartTitle.substring(0, index), TextAnchor.CENTER_LEFT), Layer.FOREGROUND);
                    subPlot.addRangeMarker(
                            getMarker(startMark, startMark + (endMark - startMark) / 2, 
                            subChartTitle.substring(index + 1), TextAnchor.CENTER_LEFT), Layer.FOREGROUND);
                }
            }
            parent.add(subPlot);
        }
        parent.setGap(gap);

        JFreeChart jfreeChart = new JFreeChart(title, new Font(fontName, Font.BOLD, fontSize), parent, false);
        jfreeChart.setBackgroundPaint(Color.WHITE);
        if (subTitles != null && !subTitles.isEmpty()) {
            for (Title subTitle : subTitles) {
                jfreeChart.addSubtitle(subTitle);
            }
        }

        try {
            ChartUtilities.saveChartAsJPEG(new File(filePath), jfreeChart, width, height * datasets.size() + fontSize * titleLineNum);
        } catch (IOException ex) {
            Logger.getLogger(DataSummaryChartGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
