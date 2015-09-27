/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.vo;

import com.ocs.indaba.aggregation.po.Datapoint;
import com.ocs.indaba.aggregation.po.TdsValue;
import com.ocs.indaba.po.Target;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jeanbone
 */
public class AggrScoreVO {

    private int spid;
    private List<Target> targets;
    private List<List<TdsValue>> values;
    private List<Datapoint> datapoints;
    private static final String COLUMN_SEPARATOR = ",";
    private static final String OBJECT_SEPARATOR = "|";
    private static final String PART_SEPARATOR = ":";

    public AggrScoreVO(int spid) {
        this.spid = spid;
    }

    public List<String[]> toCSVFile() {
        List<String[]> csvFile = new ArrayList<String[]>();
        List<String> csvCols = new ArrayList<String>();
        csvCols.add("Targets");
        for (Datapoint dp : getDatapoints()) {
            csvCols.add("D" + dp.getId());
        }
        csvFile.add(csvCols.toArray(new String[]{}));

        for (int i = 0; i < getTargets().size(); i++) {  // row
            csvCols.clear();
            csvCols.add(targets.get(i).getShortName());

            List<TdsValue> vals = getValues().get(i);
            for (int j = 0, tdsNdx = 0; j < getDatapoints().size(); j++) {
                Datapoint dp = getDatapoints().get(j);
                BigDecimal value = BigDecimal.ZERO;
                if (tdsNdx < vals.size()) {
                    TdsValue tdsVal = vals.get(tdsNdx);
                    if (tdsVal.getDatapointId() == dp.getId()) {
                        value = tdsVal.getValue();
                        tdsNdx++;
                    }
                }
                csvCols.add(value.toString());
            }
            csvFile.add(csvCols.toArray(new String[]{}));
        }
        return csvFile;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Targets").append(COLUMN_SEPARATOR);
        for (int j = 0; j < getDatapoints().size(); j++) {
            Datapoint dp = getDatapoints().get(j);
            sb.append("D").append(dp.getId());
            if (j < getDatapoints().size() - 1) {
                sb.append(COLUMN_SEPARATOR);
            }
        }
        sb.append("\r\n");

        for (int i = 0; i < getTargets().size(); i++) {  // row
            sb.append(getTargets().get(i).getDescription()).append(COLUMN_SEPARATOR);
            List<TdsValue> vals = getValues().get(i);
            for (int j = 0, tdsNdx = 0; j < getDatapoints().size(); j++) {
                Datapoint dp = getDatapoints().get(j);
                BigDecimal value = BigDecimal.ZERO;
                if (tdsNdx < vals.size()) {
                    TdsValue tdsVal = vals.get(tdsNdx);
                    if (tdsVal.getDatapointId() == dp.getId()) {
                        value = tdsVal.getValue();
                        tdsNdx++;
                    }
                }
                sb.append(value);
                if (j < getDatapoints().size() - 1) {
                    sb.append(COLUMN_SEPARATOR);
                }
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    /**
     * @return the spid
     */
    public int getSpid() {
        return spid;
    }

    /**
     * @param spid the spid to set
     */
    public void setSpid(int spid) {
        this.spid = spid;
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
     * @return the values
     */
    public List<List<TdsValue>> getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(List<List<TdsValue>> values) {
        this.values = values;
    }

    /**
     * @return the datapoints
     */
    public List<Datapoint> getDatapoints() {
        return datapoints;
    }

    /**
     * @param datapoints the datapoints to set
     */
    public void setDatapoints(List<Datapoint> datapoints) {
        this.datapoints = datapoints;
    }
}
