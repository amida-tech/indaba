/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.vo;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.po.WsIndicatorInstance;
import com.ocs.indaba.po.Target;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jeanbone
 */
public class RawScoreVO {

    private int spid;
    private List<Target> targets;
    private List<List<ScorecardAnswerVO>> scorecardAnswers;
    private List<WsIndicatorInstance> wiiList;
    private static final String COLUMN_SEPARATOR = ",";
    private static final String OBJECT_SEPARATOR = "|";
    private static final String PART_SEPARATOR = ":";

    public RawScoreVO(int spid) {
        this.spid = spid;
    }

    public List<String[]> toCSVFile() {
        List<String[]> csvFile = new ArrayList<String[]>();
        List<String> csvCols = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        csvCols.add("Targets");
        for (WsIndicatorInstance wii : wiiList) {
            sb.setLength(0);
            sb.append("I").append(wii.getIndicatorId()).append(".").append(wii.getOrgId());
            csvCols.add(sb.toString());
            csvCols.add("Score");
        }
        csvFile.add(csvCols.toArray(new String[]{}));

        for (int i = 0; i < targets.size(); i++) {      // row
            csvCols.clear();
            csvCols.add(targets.get(i).getShortName());

            List<ScorecardAnswerVO> saList = getScorecardAnswers().get(i);
            for (int j = 0; j < wiiList.size(); j++) {  // column
                WsIndicatorInstance wii = wiiList.get(j);
                String value = "NULL";
                BigDecimal score = BigDecimal.ZERO;
                for (int k = 0; k < saList.size(); k++) {
                    ScorecardAnswerVO sa = saList.get(k);
                    if (sa.getOrgId() == wii.getOrgId()
                            && sa.getScorecardAnswer().getIndicatorId() == wii.getIndicatorId()) {
                        value = sa.getScorecardAnswer().getValue();
                        score = sa.getScorecardAnswer().getScore();
                        break;
                    }
                }
                csvCols.add(value);
                csvCols.add(score.toString());
            }
            csvFile.add(csvCols.toArray(new String[]{}));
        }
        return csvFile;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Targets").append(COLUMN_SEPARATOR);
        for (int j = 0; j < wiiList.size(); j++) {
            WsIndicatorInstance wii = wiiList.get(j);
            sb.append("I").append(wii.getIndicatorId()).append(".").append(wii.getOrgId()).append(COLUMN_SEPARATOR).append("Score");
            if (j < wiiList.size() - 1) {
                sb.append(COLUMN_SEPARATOR);
            }
        }
        sb.append("\r\n");

        for (int i = 0; i < targets.size(); i++) {      // row
            sb.append(targets.get(i).getDescription()).append(COLUMN_SEPARATOR);
            List<ScorecardAnswerVO> saList = getScorecardAnswers().get(i);
            for (int j = 0, saNdx = 0; j < wiiList.size(); j++) {  // column
                WsIndicatorInstance wii = wiiList.get(j);
                String value = "NULL";
                BigDecimal score = BigDecimal.ZERO;
                if (saNdx < saList.size()) {
                    ScorecardAnswerVO sa = saList.get(saNdx);
                    if (sa.getOrgId() == wii.getOrgId()
                            && sa.getScorecardAnswer().getIndicatorId() == wii.getIndicatorId()) {
                        value = sa.getScorecardAnswer().getValue();
                        score = sa.getScorecardAnswer().getScore();
                        saNdx++;
                    }
//                    switch (sa.getScorecardAnswer().getDataType()) {
//                        case Constants.ANSWER_TYPE_SINGLE:
//                            sb.append(value).append(COLUMN_SEPARATOR).append(score);
//                            break;
//                        default:
//                            sb.append(value).append(COLUMN_SEPARATOR).append(score);
//                            break;
//                    }
                }
                sb.append(value).append(COLUMN_SEPARATOR).append(score);
                if (j < wiiList.size() - 1) {
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
     * @return the wiiList
     */
    public List<WsIndicatorInstance> getWiiList() {
        return wiiList;
    }

    /**
     * @param wiiList the wiiList to set
     */
    public void setWiiList(List<WsIndicatorInstance> wiiList) {
        this.wiiList = wiiList;
    }

    /**
     * @return the scorecardAnswers
     */
    public List<List<ScorecardAnswerVO>> getScorecardAnswers() {
        return scorecardAnswers;
    }

    /**
     * @param scorecardAnswers the scorecardAnswers to set
     */
    public void setScorecardAnswers(List<List<ScorecardAnswerVO>> scorecardAnswers) {
        this.scorecardAnswers = scorecardAnswers;
    }
}
