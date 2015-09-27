/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.tool;

import com.ocs.indaba.aggregation.vo.ChartVO;
import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Jeanbone
 */
public class SparklineChartGenerator extends JfreeChartGenerator {

    public SparklineChartGenerator(String filePath, ChartVO chart) {
        super(filePath, chart);

        this.customRenderer = new ArrayList<BarRenderer>();
        CustomRenderer cusRen = new CustomRenderer();
        customRenderer.add(cusRen);

        this.upperBound = 7;
        this.lowerBound = -7;
        this.width = (chart.getValues().length + 2) * 3 + 12;
        this.height = 25;
        this.yVisible = false;
    }

    @Override
    public void setSubTitles() {
        
    }

    class CustomRenderer extends BarRenderer {

        private Paint colors[] = {Color.BLACK, Color.decode("#2CCF40"), Color.decode("#FF1212")};

        public CustomRenderer() {
            this.setShadowVisible(false);
            this.setBaseItemLabelsVisible(false);
            this.setDrawBarOutline(false);
            this.setBarPainter(new StandardBarPainter());
        }

        @Override
        public Paint getItemPaint(int i, int j) {
            Double[] vals = chart.getValues();

            if (Math.abs(vals[j]) < 1) {
                return colors[0];
            }
            if (vals[j] > 0) {
                return colors[1];
            }
            return colors[2];
        }
    }

    @Override
    public List<CategoryDataset> createDataset() {
        int maxVal = 7, minVal = -7;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Double[] vals = chart.getValues();
        for (int i = 0, size = vals.length; i < size; i++) {
            double val = vals[i] / 10;

            if (Math.abs(val) < 1) {
                val = 0.8;
            } else if (val > 0) {
                val++;
            } else if (val < 0) {
                val--;
            }

            if (val > maxVal) {
                val = maxVal;
            } else if (val < minVal) {
                val = minVal;
            }
            dataset.setValue(val, "Series", "" + i);
        }
        List<CategoryDataset> datasets = new ArrayList<CategoryDataset>();
        datasets.add(dataset);
        return datasets;
    }
}
