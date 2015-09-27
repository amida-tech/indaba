/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.tool;

import com.ocs.indaba.aggregation.json.IndicatorSummaryJsonUtils;
import com.ocs.indaba.aggregation.vo.ChartVO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
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
public class IndicatorSummaryChartGenerator extends JfreeChartGenerator {
    private final String fontName = "arial";
    
    public IndicatorSummaryChartGenerator(String filePath, JSONObject jsonIndicatorSummary, boolean isLarge, boolean genericLabel) {
        super(filePath, jsonIndicatorSummary, genericLabel);
        
        this.width = 300;
        this.height = 60;
        if (isLarge) {
            this.width *= 6;
            this.height *= 6;
            this.fontSize *= 6;
            this.gap *= 6;
        }
        this.lowerBound = 0;
        this.upperBound = this.height * 4/5;
        
        this.startMark = this.upperBound * 1/4 * 6/5;
        this.endMark = this.upperBound;
        this.markFontSize = this.fontSize * 4/5;
        
        this.titleLineNum = 2;
        this.catMargin = 0.0;
        this.yVisible = false;
    }

    @Override
    public void setSubTitles() {
        title = jsonChart.get("surveyName") + ": " + jsonChart.get("target");
        
        if (this.genericLabel) {
            subTitles.add(new TextTitle("", 
                new Font(fontName, Font.PLAIN, fontSize * 6/7)));
        } else {
            subTitles.add(new TextTitle(jsonChart.get("chartsTitle").toString(), 
                new Font(fontName, Font.PLAIN, fontSize * 6/7)));
        }
        //subTitles.add(new TextTitle("  ", 
        //        new Font(fontName, Font.PLAIN, fontSize * 1/5)));
    }

    class CustomRenderer extends BarRenderer {

        private Paint colors[];

        public CustomRenderer(Paint[] colors) {
            this.setShadowVisible(false);
            this.setBaseItemLabelsVisible(false);
            this.setDrawBarOutline(false);
            this.setBarPainter(new StandardBarPainter());
            this.setItemMargin(0.0);
            this.colors = colors;
        }

        @Override
        public Paint getItemPaint(int i, int j) {
            return colors[j];
        }
    }

    protected int getIndex(Integer[] vals, int targetNdx, int startNdx, int endNdx) {
        int midNdx = (startNdx + endNdx) / 2;
        if (startNdx == midNdx) {
            if (vals[midNdx] - vals[targetNdx] == midNdx - targetNdx) {
                return midNdx;
            } else {
                return midNdx - 1;
            }
        } else {
            if (vals[midNdx] - vals[targetNdx] == midNdx - targetNdx) {
                return getIndex(vals, targetNdx, midNdx, endNdx);
            } else {
                return getIndex(vals, targetNdx, startNdx, midNdx);
            }
        }
    }

    protected String getPointerColor(int targetVal) {
        String color;
        switch (targetVal / 10) {
            case 10:
            case 9:
                color = "#00A62E";
                break;
            case 8:
                color = "#6FD13E";
                break;
            case 7:
                color = "#FFD241";
                break;
            case 6:
                color = "#FF7826";
                break;
            default:
                color = "#EB1110";
        }
        return color;
    }

    protected void setColor(DefaultCategoryDataset dataset, List<Color> colorSet, String colorVal, int start, int end, int height) {
        if (end < start) {
            return;
        }

        for (int i = 0; i < end - start + 1; i++) {
            dataset.addValue(height, "Series", "" + (start + i));
            colorSet.add(Color.decode(colorVal));
        }
    }

    @Override
    public List<CategoryDataset> createDataset() {
        JSONArray jsonCharts = IndicatorSummaryJsonUtils.getJsonCharts(jsonChart);
        List<CategoryDataset> datasets = new ArrayList<CategoryDataset>();

        int colHeight = upperBound * 1/4;
        String bgColor = "#F6F6F5", foreColor = "#B7B7B7";

        for (int i = 0; i < jsonCharts.size(); i++) {
            JSONArray jsonCat = (JSONArray) jsonCharts.get(i);
            int catSize = jsonCat.size();
            for (int j = 0; j < catSize; j++) {
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                List<Color> colorSet = new ArrayList<Color>();
                
                JSONObject jsonSubChart = (JSONObject) jsonCat.get(j);
                ChartVO chartVO = IndicatorSummaryJsonUtils.parseJsonChart(jsonSubChart);

                Double[] vals = chartVO.getValues();
                int targetVal = vals[chartVO.getTargetIndex()].intValue();

                TreeSet<Integer> ts = new TreeSet<Integer>();
                for (int k = 0; k < vals.length; k++) {
                    ts.add(vals[k].intValue());
                }
                Integer[] distinctVals = ts.toArray(new Integer[]{});

                int bgStart = 0;
                for (int targetNdx = 0, index; targetNdx < distinctVals.length;) {
                    if (bgStart < distinctVals[targetNdx]) {
                        setColor(dataset, colorSet, bgColor, bgStart, distinctVals[targetNdx] - 1, colHeight);
                    }

                    index = getIndex(distinctVals, targetNdx, targetNdx, distinctVals.length - 1);

                    if (distinctVals[targetNdx] <= targetVal && targetVal <= distinctVals[index]) {
                        setColor(dataset, colorSet, foreColor, distinctVals[targetNdx], targetVal - 1, colHeight);
                        setColor(dataset, colorSet, getPointerColor(targetVal), targetVal, targetVal, colHeight * 6/5);
                        setColor(dataset, colorSet, foreColor, targetVal + 1, distinctVals[index], colHeight);
                    } else {
                        setColor(dataset, colorSet, foreColor, distinctVals[targetNdx], distinctVals[index], colHeight);
                    }

                    targetNdx = index + 1;
                    bgStart = distinctVals[index] + 1;
                }
                if (bgStart <= 100) {
                    setColor(dataset, colorSet, bgColor, bgStart, 100, colHeight);
                }
                datasets.add(dataset);
                
                // sub chart title
                subChartTitles.add(chartVO.getChartName());

                // Renderer
                CustomRenderer cusRen = new CustomRenderer(colorSet.toArray(new Color[0]));
                customRenderer.add(cusRen);
            }
        }
        return datasets;
    }
}
