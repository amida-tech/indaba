/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.util;

import com.ocs.common.Config;
import com.ocs.indaba.vo.GoalObjectView;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.PeriodAxis;
import org.jfree.chart.axis.PeriodAxisLabelInfo;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimePeriodAnchor;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author Jeff
 */
public class ChartUtil {
    private static final Logger log = Logger.getLogger(ChartUtil.class);
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;

    public void generate() {
        HistogramDataset dataset = new HistogramDataset();

        JFreeChart myChart = ChartFactory.createHistogram("History", "Time", "Percentage", dataset, PlotOrientation.VERTICAL, true, false, false);
    }

    public static void main(String args[]) {
        ChartUtil chartUtil = new ChartUtil();
        //chartUtil.generate();
        double[] value = new double[100];
        Random generator = new Random();
        for (int i = 1; i < 100; i++) {
            value[i] = generator.nextDouble();

        }
        int number = 10;
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.SCALE_AREA_TO_1);
        dataset.addSeries("Indaba Scorecard US History", value, number);
        String plotTitle = "Indaba Scorecard US History";
        String xaxis = "Date";
        String yaxis = "Percent";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = false;
        boolean urls = false;
        JFreeChart chart = ChartFactory.createHistogram(plotTitle, xaxis, yaxis,
                dataset, orientation, show, toolTips, urls);
        int width = 500;
        int height = 300;
        try {
            ChartUtilities.saveChartAsJPEG(new File("E:\\test.jpg"), chart, width, height);
        } catch (IOException e) {
        }
    }

    public static void generateAxisChart(List<GoalObjectView> govList, String caption, String path) {
        if (govList.isEmpty()) {
            return;
        }

        XYDataset xydataset = createDataset(govList);
        JFreeChart jfreechart = createChart(caption, xydataset);
        try {
            File file = new File(path);
            file.getParentFile().mkdirs();
            ChartUtilities.saveChartAsJPEG(file, jfreechart, 
                    Config.getInt(Config.KEY_IMAGE_WIDTH, DEFAULT_WIDTH),
                    Config.getInt(Config.KEY_IMAGE_WIDTH, DEFAULT_HEIGHT));

        } catch (IOException e) {
        }
    }

    public static OutputStream generateAxisChart(OutputStream out, List<GoalObjectView> govList, String caption) {
        if (govList.isEmpty() || out == null) {
            return out;
        }

        XYDataset xydataset = createDataset(govList);
        JFreeChart jfreechart = createChart(caption, xydataset);
        try {
            ChartUtilities.writeChartAsJPEG(out, jfreechart, 
                    Config.getInt(Config.KEY_IMAGE_WIDTH, DEFAULT_WIDTH),
                    Config.getInt(Config.KEY_IMAGE_WIDTH, DEFAULT_HEIGHT));

        } catch (IOException ex) {
            log.error("Fail to generate chart.", ex);
        }
        return out;
    }

    private static JFreeChart createChart(String caption, XYDataset xydataset) {
        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(caption, "Date", "WLS(%)", xydataset, true, true, false);
        jfreechart.setBackgroundPaint(Color.white);
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        xyplot.setBackgroundPaint(Color.lightGray);
        xyplot.setDomainGridlinePaint(Color.white);
        xyplot.setRangeGridlinePaint(Color.white);
        xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
        xyplot.setDomainCrosshairVisible(true);
        xyplot.setRangeCrosshairVisible(true);

        XYItemRenderer xyitemrenderer = xyplot.getRenderer();
        if (xyitemrenderer instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyitemrenderer;
            xylineandshaperenderer.setBaseShapesVisible(true);
            xylineandshaperenderer.setBaseLinesVisible(true);
            xylineandshaperenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
            xylineandshaperenderer.setBaseItemLabelsVisible(true);
//            xylineandshaperenderer.setShapesVisible(true);
//            xylineandshaperenderer.setShapesFilled(true);
//            xylineandshaperenderer.setItemLabelsVisible(true);
        }

        PeriodAxis periodaxis = new PeriodAxis("Date");
        periodaxis.setTimeZone(TimeZone.getDefault());
        periodaxis.setAutoRangeTimePeriodClass(org.jfree.data.time.Day.class);
        PeriodAxisLabelInfo aperiodaxislabelinfo[] = new PeriodAxisLabelInfo[3];
        aperiodaxislabelinfo[0] = new PeriodAxisLabelInfo(org.jfree.data.time.Day.class, new SimpleDateFormat("d"));
        aperiodaxislabelinfo[1] = new PeriodAxisLabelInfo(org.jfree.data.time.Month.class, new SimpleDateFormat("MMM"), new RectangleInsets(2D, 2D, 2D, 2D), new Font("SansSerif", 1, 10), Color.blue, false, new BasicStroke(0.0F), Color.lightGray);
        aperiodaxislabelinfo[2] = new PeriodAxisLabelInfo(org.jfree.data.time.Year.class, new SimpleDateFormat("yyyy"));
        periodaxis.setLabelInfo(aperiodaxislabelinfo);
        xyplot.setDomainAxis(periodaxis);
        return jfreechart;
    }

    private static XYDataset createDataset(List<GoalObjectView> govList) {
        int i;
        Date curDate, nextDate, today = new Date();
        DecimalFormat doubleFormat = new DecimalFormat("###,###.00");

        Calendar cal = Calendar.getInstance();

        TimeSeries timeseries1 = new TimeSeries("WLS of completed Goal Object");
        TimeSeries timeseries2 = new TimeSeries("WLS of planned Goal Object");
        for (i = 0; i < govList.size() - 1; i++) {
            curDate = govList.get(i).getCompletionTime();
            nextDate = govList.get(i + 1).getCompletionTime();
//            if (nextDate.getYear() == curDate.getYear() &&
//                    nextDate.getMonth() == curDate.getMonth() &&
//                    nextDate.getDate() == curDate.getDate()) {

            if (DateUtils.milliSecondsToDays(today.getTime())
                    < DateUtils.milliSecondsToDays(curDate.getTime())) {
                break;
            }
            if (DateUtils.milliSecondsToDays(nextDate.getTime())
                    == DateUtils.milliSecondsToDays(curDate.getTime())) {
                continue;
            }
            timeseries1.add(new Day(cal.get(Calendar.DATE), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR) + 1900),
                    Double.parseDouble(doubleFormat.format(govList.get(i).getWorkload() * 100)));
        }
        if (i > 0) {
            i--;
        }
        for (; i < govList.size() - 1; i++) {
            curDate = govList.get(i).getCompletionTime();
            nextDate = govList.get(i + 1).getCompletionTime();
            if (DateUtils.milliSecondsToDays(nextDate.getTime())
                    == DateUtils.milliSecondsToDays(curDate.getTime())) {
                continue;
            }
            timeseries2.add(new Day(curDate.getDate(), curDate.getMonth() + 1, curDate.getYear() + 1900),
                    Double.parseDouble(doubleFormat.format(govList.get(i).getWorkload() * 100)));
        }
        curDate = govList.get(i).getCompletionTime();
        if (DateUtils.milliSecondsToDays(today.getTime())
                >= DateUtils.milliSecondsToDays(curDate.getTime())) {
            timeseries1.add(new Day(curDate.getDate(), curDate.getMonth() + 1, curDate.getYear() + 1900),
                    Double.parseDouble(doubleFormat.format(govList.get(i).getWorkload() * 100)));
        } else {
            timeseries2.add(new Day(curDate.getDate(), curDate.getMonth() + 1, curDate.getYear() + 1900),
                    Double.parseDouble(doubleFormat.format(govList.get(i).getWorkload() * 100)));
        }

        TimeZone timezone = TimeZone.getDefault();
        TimeSeriesCollection timeseriescollection = new TimeSeriesCollection(timezone);
        timeseriescollection.addSeries(timeseries2);
        timeseriescollection.addSeries(timeseries1);
        timeseriescollection.setXPosition(TimePeriodAnchor.MIDDLE);
        return timeseriescollection;
    }
}

class CategoryItemChart {

    public static String generateBarChart(
            HttpSession session,
            PrintWriter pw,
            int w, int h) {
        String filename = null;
        try {
            CategoryDataset dataset = createDataset();
            JFreeChart chart = ChartFactory.createBarChart(
                    "世界傻瓜大奖赛",//图表标题
                    "比赛场次",//X轴标题
                    "傻瓜程度",//Y轴标题
                    dataset,//数据集合
                    PlotOrientation.VERTICAL,//图表显示方向（水平、垂直）
                    true,//是否使用图例
                    false,//是否使用工具提示
                    false//是否为图表增加URL
                    );
            /*------------配置图表属性--------------*/
// 1,设置整个图表背景颜色
            chart.setBackgroundPaint(Color.yellow);
            /*------------设定Plot参数-------------*/
            CategoryPlot plot = chart.getCategoryPlot();
// 2,设置详细图表的显示细节部分的背景颜色
            plot.setBackgroundPaint(Color.PINK);
// 3,设置垂直网格线颜色
            plot.setDomainGridlinePaint(Color.black);
//4,设置是否显示垂直网格线
            plot.setDomainGridlinesVisible(true);
//5,设置水平网格线颜色
            plot.setRangeGridlinePaint(Color.yellow);
//6,设置是否显示水平网格线
            plot.setRangeGridlinesVisible(true);
            /*---------将所有数据转换为整数形式---------*/
            final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            /*---------设置是否在柱图的状态条上显示边框----*/
            CategoryItemRenderer renderer = (CategoryItemRenderer) plot.getRenderer();
            BarRenderer render = (BarRenderer) plot.getRenderer();
            render.setItemMargin(0.1);
            /*---------设置状态条颜色的深浅渐变-----------*/
            GradientPaint gp0 = new GradientPaint(
                    0.0f, 0.0f, new Color(255, 200, 80),
                    0.0f, 0.0f, new Color(255, 255, 40));
            GradientPaint gp1 = new GradientPaint(
                    0.0f, 0.0f, new Color(50, 255, 50),
                    0.0f, 0.0f, new Color(100, 255, 100));
            GradientPaint gp2 = new GradientPaint(
                    0.0f, 0.0f, Color.red,
                    0.0f, 0.0f, new Color(255, 100, 100));
            GradientPaint gp3 = new GradientPaint(
                    0.0f, 0.0f, new Color(108, 108, 255),
                    0.0f, 0.0f, new Color(150, 150, 200));
            renderer.setSeriesPaint(0, gp0);
            renderer.setSeriesPaint(1, gp1);
            renderer.setSeriesPaint(2, gp2);
            renderer.setSeriesPaint(3, gp3);

            /*
             *
             * 解决柱状体与图片边框的间距问题
             *
             *
             * */
            /*------设置X轴标题的倾斜程度----*/
            CategoryAxis domainAxis = plot.getDomainAxis();
            domainAxis.setCategoryLabelPositions(
                    CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
            /*------设置柱状体与图片边框的左右间距--*/
            domainAxis.setLowerMargin(0.01);
            domainAxis.setUpperMargin(0.01);

            /*------设置柱状体与图片边框的上下间距---*/
            ValueAxis rAxis = plot.getRangeAxis();
            rAxis.setUpperMargin(0.15);
            rAxis.setLowerMargin(0.15);
            /*---------设置每一组柱状体之间的间隔---------*/
            render.setItemMargin(0.0);
            /*
             *
             * 解决柱状体与图片边框的间距问题
             *
             *
             * */
            /*
             *
             *
             * 解决JFREECHART的中文显示问题
             *
             *
             * */
            /*----------设置消除字体的锯齿渲染（解决中文问题）--------------*/
            chart.getRenderingHints().put(
                    RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            /*----------设置标题字体--------------------------*/
            TextTitle textTitle = chart.getTitle();
            textTitle.setFont(new Font("黑体", Font.PLAIN, 20));
            /*------设置X轴坐标上的文字-----------*/
            domainAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 11));
            /*------设置X轴的标题文字------------*/
            domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));
            /*------设置Y轴坐标上的文字-----------*/
            rAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 12));
            /*------设置Y轴的标题文字------------*/
            rAxis.setLabelFont(new Font("黑体", Font.PLAIN, 12));
            /*---------设置柱状体上的显示的字体---------*/
            renderer.setItemLabelGenerator(new LabelGenerator(0.0));
            renderer.setItemLabelFont(new Font("宋体", Font.PLAIN, 12));
            renderer.setItemLabelsVisible(true);
            /*
             *
             *
             * 解决JFREECHART的中文显示问题
             *
             *
             * */

            /*------得到chart的保存路径----*/
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsPNG(chart, w, h, info, session);
            /*------使用printWriter将文件写出----*/
            ChartUtilities.writeImageMap(pw, filename, info, true);
            pw.flush();
        } catch (Exception e) {
            System.out.println("Exception - " + e.toString());
            e.printStackTrace(System.out);
            filename = "public_error_500x300.png";
        }
        return filename;
    }


    /*-------------设置柱状体顶端的数据显示--------------*/
    static class LabelGenerator implements CategoryItemLabelGenerator {

        private double threshold;

        public LabelGenerator(double threshold) {
            this.threshold = threshold;
        }

        public String generateLabel(CategoryDataset dataset,
                int row, int column) {
            String result = null;
            final Number value = dataset.getValue(row, column);
            if (value != null) {
                final double v = value.doubleValue();
                if (v > this.threshold) {
                    result = value.toString();
                }
            }
            return result;
        }

        public String generateRowLabel(CategoryDataset dataset,
                int row) {
            return null;
        }

        public String generateColumnLabel(CategoryDataset dataset,
                int column) {
            return null;
        }
    }
    /*-----------数据集合封装-------------*/

    private static CategoryDataset createDataset() {
        String s1 = "笨笨";
        String s2 = "蛋蛋";
        String s3 = "傻傻";
        String s4 = "瓜瓜";
        String c1 = "第一场";
        String c2 = "第二场";
        String c3 = "第三场";
        String c4 = "第四场";
        String c5 = "第五场";
        /*-------------封装图表使用的数据集-------------*/
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(1.0, s1, c1);
        dataset.setValue(2.0, s1, c2);
        dataset.setValue(3.0, s1, c3);
        dataset.setValue(4.0, s1, c4);
        dataset.setValue(5.0, s1, c5);
        dataset.setValue(5.0, s2, c1);
        dataset.setValue(4.0, s2, c2);
        dataset.setValue(3.0, s2, c3);
        dataset.setValue(2.0, s2, c4);
        dataset.setValue(1.0, s2, c5);
        dataset.setValue(1.0, s3, c1);
        dataset.setValue(2.0, s3, c2);
        dataset.setValue(3.0, s3, c3);
        dataset.setValue(2.0, s3, c4);
        dataset.setValue(1.0, s3, c5);
        dataset.setValue(1.0, s4, c1);
        dataset.setValue(2.0, s4, c2);
        dataset.setValue(3.0, s4, c3);
        dataset.setValue(4.0, s4, c4);
        dataset.setValue(5.0, s4, c5);
        return dataset;
    }
}
