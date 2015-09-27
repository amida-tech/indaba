/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.vo;

import com.ocs.indaba.po.StudyPeriod;
import com.ocs.indaba.po.Target;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Jeanbone
 */
public class DataSummaryInfo {

    private String worksetName;
    private String orgName;
    private List<StudyPeriod> studyPeriods;
    private List<Target> targets;
    private Date exportDate;

    private static final String COLUMN_SEPARATOR = ",";
    private static final String OBJECT_SEPARATOR = "|";
    private static final String PART_SEPARATOR = ":";

    public List<String[]> toCSVFile() {
        List<String[]> csvFile = new ArrayList<String[]>();
        List<String> csvCols = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();

        // first line: title
        csvCols.add("Working Set");
        csvCols.add("Organization");
        csvCols.add("Study Period");
        csvCols.add("Targets");
        csvCols.add("Export Time");
        csvFile.add(csvCols.toArray(new String[]{}));

        csvCols.clear();
        csvCols.add(getWorksetName());
        csvCols.add(getOrgName());
        int i;
        if (getStudyPeriods().size() > 0) {
            for (i = 0; i < getStudyPeriods().size() - 1; i++) {
                sb.append(getStudyPeriods().get(i).getName())
                        .append(PART_SEPARATOR).append(i + 1).append(OBJECT_SEPARATOR);
            }
            sb.append(getStudyPeriods().get(i).getName())
                    .append(PART_SEPARATOR).append(i + 1);
            csvCols.add(sb.toString());
        }
        if (getTargets().size() > 0) {
            sb.setLength(0);
            Target target;
            for (i = 0; i < getTargets().size() - 1; i++) {
                target = getTargets().get(i);
                sb.append(target.getName()).append(PART_SEPARATOR).append(target.getShortName()).append(OBJECT_SEPARATOR);
            }
            target = getTargets().get(i);
            sb.append(target.getName()).append(PART_SEPARATOR).append(target.getShortName());
            csvCols.add(sb.toString());
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        csvCols.add(df.format(getExportDate()));
        csvFile.add(csvCols.toArray(new String[]{}));
        return csvFile;
    }

    @Override
    public String toString() {
        StringBuilder temp = new StringBuilder();
        temp.append(getWorksetName()).append(COLUMN_SEPARATOR);
        temp.append(getOrgName()).append(COLUMN_SEPARATOR);
        int i;
        if (getStudyPeriods().size() > 0) {
            for (i = 0; i < getStudyPeriods().size() - 1; i++) {
                temp.append(getStudyPeriods().get(i).getName())
                        .append(PART_SEPARATOR).append(i + 1).append(OBJECT_SEPARATOR);
            }
            temp.append(getStudyPeriods().get(i).getName())
                    .append(PART_SEPARATOR).append(i + 1).append(COLUMN_SEPARATOR);
        }
        if (getTargets().size() > 0) {
            Target target;
            for (i = 0; i < getTargets().size() - 1; i++) {
                target = getTargets().get(i);
                temp.append(target.getName()).append(PART_SEPARATOR).append(target.getDescription()).append(OBJECT_SEPARATOR);
            }
            target = getTargets().get(i);
            temp.append(target.getName()).append(PART_SEPARATOR).append(target.getDescription()).append(COLUMN_SEPARATOR);
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        temp.append(df.format(getExportDate()));
        return temp.toString();
    }

    /**
     * @return the worksetName
     */
    public String getWorksetName() {
        return worksetName;
    }

    /**
     * @param worksetName the worksetName to set
     */
    public void setWorksetName(String worksetName) {
        this.worksetName = worksetName;
    }

    /**
     * @return the orgName
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * @param orgName the orgName to set
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * @return the studyPeriods
     */
    public List<StudyPeriod> getStudyPeriods() {
        return studyPeriods;
    }

    /**
     * @param studyPeriods the studyPeriods to set
     */
    public void setStudyPeriods(List<StudyPeriod> studyPeriods) {
        this.studyPeriods = studyPeriods;
    }

    /**
     * @return the targets
     */
    public List<Target> getTargets() {
        return targets;
    }

    /**
     * @param targets the targets to set
     */
    public void setTargets(List<Target> targets) {
        this.targets = targets;
    }

    /**
     * @return the exportDate
     */
    public Date getExportDate() {
        return exportDate;
    }

    /**
     * @param exportDate the exportDate to set
     */
    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }
}
