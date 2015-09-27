/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.service;

import com.ocs.common.Config;
import com.ocs.indaba.dao.ContentHeaderDAO;
import com.ocs.indaba.dao.ProjectDAO;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.util.ChartUtil;
import com.ocs.indaba.util.DateUtils;
import com.ocs.indaba.util.SpringContextUtil;
import com.ocs.indaba.vo.GoalObjectView;
import com.ocs.indaba.vo.SequenceObjectView;
import java.io.File;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author Jeff Jiang
 */
public class ChartService {

    private static ContentHeaderDAO contentHeaderDao = (ContentHeaderDAO) SpringContextUtil.getBean("contentHeaderDao");
    private static ProjectDAO projectDao = (ProjectDAO) SpringContextUtil.getBean("projectDao");
    private static HorseService horseService = (HorseService) SpringContextUtil.getBean("horseService");

    public static String getChartFilename(final int horseId) {
        return MessageFormat.format(Config.getString(Config.KEY_STORAGE_IMAGE_FILENAME_FORMAT), horseId);
    }

    public static String generateChartForHorse(final int horseId) {
        final String caption = "Indaba " + contentHeaderDao.selectContentHeaderByHorseId(horseId).getTitle();
        final String filename = getChartFilename(horseId);
        (new Thread() {

            @Override
            public void run() {
                File chartFile = new File(Config.getString(Config.KEY_STORAGE_IMAGE_BASE), filename);
                // check if the chart is existed or not expired(< 1min)
                if (!chartFile.exists() || (System.currentTimeMillis() - chartFile.lastModified()) > DateUtils.MILLISECONDS_OF_MINUTE) {
                    List<SequenceObjectView> seqObjViewList = horseService.getSequenceObjectViewListByHorseId(horseId);
                    List<GoalObjectView> sortedGovList = horseService.sortGoal(seqObjViewList, false);
                    ChartUtil.generateAxisChart(sortedGovList, caption, chartFile.getAbsolutePath());
                }
            }
        }).start();
        return filename;
    }

    public static OutputStream generateChartForHorse(final OutputStream out, final int horseId) {
        final String caption = "Indaba " + contentHeaderDao.selectContentHeaderByHorseId(horseId).getTitle();
        // check if the chart is existed or not expired(< 1min)

        List<SequenceObjectView> seqObjViewList = horseService.getSequenceObjectViewListByHorseId(horseId);
        List<GoalObjectView> sortedGovList = horseService.sortGoal(seqObjViewList, false);
        ChartUtil.generateAxisChart(out, sortedGovList, caption);
        return out;
    }

    public static String getChartXmlByHorseId(final int horseId) {
        ContentHeader contentHeader = contentHeaderDao.selectContentHeaderByHorseId(horseId);
        String caption = contentHeader.getTitle();
        int projectId = contentHeader.getProjectId();
        Date closeTime = projectDao.selectProjectById(projectId).getCloseTime();
        closeTime.setTime(closeTime.getTime() + 24*3600*1000 - 1 );
        List<SequenceObjectView> seqObjViewList = horseService.getSequenceObjectViewListByHorseId(horseId);
        List<GoalObjectView> sortedGovList = horseService.sortGoal(seqObjViewList, false);
        
        // patch
        Date curDate, nextDate;
        for (int i = 0; i < sortedGovList.size() - 1; i ++) {
            curDate = sortedGovList.get(i).getCompletionTime();
            nextDate = sortedGovList.get(i + 1).getCompletionTime();
            if (calInterval(curDate, nextDate) == 0) {
                sortedGovList.get(i + 1).setGoalName(
                        sortedGovList.get(i).getGoalName() + ", " +
                        sortedGovList.get(i + 1).getGoalName());
                sortedGovList.remove(i);
                i --;
            }
        }
        
        sortedGovList.get(0).setGoalName(caption + "_start");

        return generateXml(sortedGovList, caption, closeTime);
    }

    private static String generateXml(List<GoalObjectView> govList, String caption, Date closeTime) {
        Document doc = new Document(new Element("chart"));
        Element root = doc.getRootElement();
        setXmlRootAttr(root, caption);

        Element categories = new Element("categories");
        Element dataset1 = new Element("dataset");
        Element dataset2 = new Element("dataset");
        dataset1.setAttribute("seriesName", "completed events");
        dataset2.setAttribute("seriesName", "predicted events");
        dataset1.setAttribute("color", "009900");
        dataset2.setAttribute("color", "999999");
        dataset1.setAttribute("plotBorderColor", "009900");
        dataset2.setAttribute("plotBorderColor", "999999");

        int i;
        double workload = govList.get(govList.size() - 1).getWorkload();
        Date today = new Date();
        for (i = 0; i < govList.size() - 1; i ++) {
            if ( !fillDataset(govList, i, today, closeTime, categories, dataset1, dataset2) ) {
                workload = govList.get(i).getWorkload();
                Date curDate = govList.get(i).getCompletionTime();
                Date nextDate = govList.get(i + 1).getCompletionTime();
                int xIntLen = calInterval(nextDate, curDate);
                double yInterval = (govList.get(i + 1).getWorkload() - workload) / xIntLen;
                xIntLen = calInterval(closeTime, curDate);
                workload += xIntLen * yInterval;
                break;
            }
        }

        if (i == govList.size() - 1) {
            GoalObjectView gov = govList.get(i);
            int xIntLen;
            if ((xIntLen = calInterval(closeTime,gov.getCompletionTime())) > 0) {
                fillDataset(gov.getCompletionTime(), gov.getWorkload(),
                        gov.getGoalName(), xIntLen, 0, categories, dataset1, dataset2,
                        (calInterval(gov.getCompletionTime(), today) <= 0));
            }
        }
        fillDataset(closeTime, workload, "Project's Close Time", 0, 0,
                categories, dataset1, dataset2, calInterval(closeTime, today) <= 0);
        
        root.addContent(categories);
        root.addContent(dataset1);
        root.addContent(dataset2);

        return new XMLOutputter().outputString(doc);
    }

    private static void setXmlRootAttr(Element root, String caption) {
        root.setAttribute("logoURL", "images/indaba-logo.gif");
        root.setAttribute("caption", "Indaba History Chart");
        root.setAttribute("subCaption", caption);

        root.setAttribute("xAxisName", "Date");
        root.setAttribute("yAxisName", "Completion Percentage");
        root.setAttribute("showLabels", "0");
        root.setAttribute("showValues", "0");

        root.setAttribute("decimals", "2");
        root.setAttribute("forceDecimals", "1");
        root.setAttribute("yAxisMaxvalue", "100");
        root.setAttribute("yAxisMinValue", "0");
        root.setAttribute("numDivLines", "5");
        root.setAttribute("labelDisplay", "rotate");
        root.setAttribute("slantLabels", "1");

        root.setAttribute("palette", "3");
    }

    private static boolean fillDataset(List<GoalObjectView> govList, int index, Date today, Date closeTime,
            Element categories, Element dataset1, Element dataset2) {
        Date curDate = govList.get(index).getCompletionTime();
        if (calInterval(closeTime, curDate) < 0)
            return false;
        Date nextDate = govList.get(index + 1).getCompletionTime();

        int xIntLen;
        double workload, yInterval;
        String goalName;

        // same xAxis
        if ((xIntLen = calInterval(nextDate, curDate)) == 0) {
            return true;
        }
        workload = govList.get(index).getWorkload();
        yInterval = (govList.get(index + 1).getWorkload() - workload) / xIntLen;
        goalName = govList.get(index).getGoalName();

        if (calInterval(closeTime, nextDate) < 0) {
            xIntLen = calInterval(closeTime, curDate);
        }

        if (calInterval(nextDate, today) <= 0) {        // all completed
            fillDataset(curDate, workload, goalName, xIntLen, yInterval, 
                    categories, dataset1, dataset2, true);
        } else if (calInterval(curDate, today) >= 0) {  // all planned
            fillDataset(curDate, workload, goalName, xIntLen, yInterval,
                    categories, dataset1, dataset2, false);
        } else {                                        // partly completed
            goalName = "Working on " + govList.get(index + 1).getGoalName();

            if (calInterval(closeTime, today) > 0)
            {
                xIntLen = calInterval(today, curDate);
                fillDataset(curDate, workload, goalName, xIntLen, yInterval,
                        categories, dataset1, dataset2, true);

                workload += xIntLen * yInterval;
                if (calInterval(closeTime, nextDate) < 0)
                    xIntLen = calInterval(closeTime, today);
                else
                    xIntLen = calInterval(nextDate, today);
                fillDataset(today, workload, goalName, xIntLen, yInterval,
                        categories, dataset1, dataset2, false);
            }else {
                fillDataset(curDate, workload, goalName, xIntLen, yInterval,
                        categories, dataset1, dataset2, true);
            }
        }

        return (calInterval(closeTime, nextDate) > 0);

    }

    // dataset1 use green color, dataset2 use gray
    // if isCompleted, dataset1 show anchor; else dataset2 shows.
    private static void fillDataset(Date startDate, double workload, String goalName,
            int xIntLen, double yInterval, Element categories,
            Element dataset1, Element dataset2, Boolean isCompleted) {

        DecimalFormat doubleFormat = new DecimalFormat("###,###.00");
        Element category, set1, set2;
        
        category = new Element("category");
        category.setAttribute("label", DateUtils.date2Str(startDate));
        category.setAttribute("showName", "1");
        categories.addContent(category);
        
        set1 = new Element("set");
        set2 = new Element("set");

        if (calInterval(startDate, new Date()) == 0) {
            fillSetAttr(set1, doubleFormat.format(workload * 100),
                    goalName, startDate, true, isCompleted);
            fillSetAttr(set2, doubleFormat.format(workload * 100),
                    goalName, startDate, true, isCompleted);
        } else {
            if (isCompleted) {
                fillSetAttr(set1, doubleFormat.format(workload * 100),
                        goalName, startDate, false, isCompleted);
            } else {
                fillSetAttr(set2, doubleFormat.format(workload * 100),
                        goalName, startDate, false, isCompleted);
            }
        }
        dataset1.addContent(set1);
        dataset2.addContent(set2);

        Date duringDate = new Date(startDate.getTime());
        for (int i = 1; i < xIntLen; i ++) {
            duringDate.setTime(duringDate.getTime() + DateUtils.MILLISECONDS_OF_DAY);
            category = new Element("category");
            category.setAttribute("label", DateUtils.date2Str(duringDate));
            categories.addContent(category);
            
            workload += yInterval;
            set1 = new Element("set");
            set2 = new Element("set");
            if (isCompleted) {
                set1.setAttribute("value", doubleFormat.format(workload * 100));
            } else {
                set2.setAttribute("value", doubleFormat.format(workload * 100));
            }
            dataset1.addContent(set1);
            dataset2.addContent(set2);
        }
    }
    
    private static void fillSetAttr(Element set, String workload, String goalName,
            Date date, Boolean isToday, Boolean isCompleted) {
        String anchorBorderColor, anchorBgColor, anchorRadius = "3";
        
        if (isToday) {
            anchorBorderColor = "0000FF";
            anchorBgColor = "D5D5FF";
            anchorRadius = "4";
        } else if (isCompleted) {
            anchorBorderColor = "00FF00";
            anchorBgColor = "D5FFD5";
        } else {
            anchorBorderColor = "555555";
            anchorBgColor = "FFFFFF";
        }
        
        set.setAttribute("value", workload);
        set.setAttribute("showValue", "1");
        if (goalName != null) {
            set.setAttribute("toolText", goalName + "\nDate: " + DateUtils.date2Str(date));
        }
        set.setAttribute("anchorSides", "20");
        set.setAttribute("anchorRadius", anchorRadius);
        set.setAttribute("anchorBorderColor", anchorBorderColor);
        set.setAttribute("anchorBorderThickness", "2");
        set.setAttribute("anchorBgColor", anchorBgColor);
        set.setAttribute("anchorAlpha", "100");
        set.setAttribute("anchorBgAlpha", "100");
    }

    private static int calInterval(Date date1, Date date2) {
        return (DateUtils.milliSecondsToDays(date1.getTime()) -
                DateUtils.milliSecondsToDays(date2.getTime()));
    }
}
