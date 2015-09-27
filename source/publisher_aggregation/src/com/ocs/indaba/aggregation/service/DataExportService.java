/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.service;

import au.com.bytecode.opencsv.CSVWriter;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.common.Config;
import com.ocs.indaba.aggregation.dao.AggrMethodDAO;
import com.ocs.indaba.aggregation.dao.ConfigDAO;
import com.ocs.indaba.aggregation.dao.DatapointDAO;
import com.ocs.indaba.aggregation.dao.DpMemberDAO;
import com.ocs.indaba.aggregation.dao.PubOrganizationDAO;
import com.ocs.indaba.aggregation.dao.ScorecardAnswerDAO;
import com.ocs.indaba.aggregation.dao.TdsValueDAO;
import com.ocs.indaba.aggregation.dao.WorksetIndicatorInstanceDAO;
import com.ocs.indaba.aggregation.po.Datapoint;
import com.ocs.indaba.aggregation.po.DpMember;
import com.ocs.indaba.aggregation.po.TdsValue;
import com.ocs.indaba.aggregation.po.WsIndicatorInstance;
import com.ocs.indaba.aggregation.vo.AggrScoreVO;
import com.ocs.indaba.aggregation.vo.DataForm;
import com.ocs.indaba.aggregation.vo.DataPointVO;
import com.ocs.indaba.aggregation.vo.DataSummaryInfo;
import com.ocs.indaba.aggregation.vo.IndicatorVO;
import com.ocs.indaba.aggregation.vo.RawScoreVO;
import com.ocs.indaba.aggregation.vo.ScorecardAnswerVO;
import com.ocs.indaba.builder.dao.ScorecardQuestionDAO;
import com.ocs.indaba.builder.dao.StudyPeriodDAO;
import com.ocs.indaba.builder.dao.TargetDAO;
import com.ocs.indaba.dao.AnswerTypeDAO;
import com.ocs.indaba.dao.AtcChoiceDAO;
import com.ocs.indaba.dao.OrganizationDAO;
import com.ocs.indaba.po.AtcChoice;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.po.Target;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeanbone
 */
public class DataExportService extends ExportService {

    private DataForm dataForm = null;
    private String EXPORT_INDICATORS_NAME;
    private String EXPORT_DATAPOINTS_NAME;
    private DataSummaryService dataSummaryService;
    private DatapointDAO dataPointDao;
    private ScorecardQuestionDAO scorecardQuestionDao;
    private AtcChoiceDAO atcChoiceDao;
    private AnswerTypeDAO answerTypeDao;
    private PubOrganizationDAO pubOrganizationDao;
    private AggrMethodDAO aggrMethodDao;
    private DpMemberDAO dpMemberDao;
    private WorksetIndicatorInstanceDAO wiiDao;
    private ScorecardAnswerDAO scorecardAnswerDao;
    private TargetDAO targetDao;
    private TdsValueDAO tdsValueDao;
    private OrganizationDAO orgDao;
    private StudyPeriodDAO studyPeriodDao;
    private ConfigDAO configDao;

    public void createDataExport(DataForm dataForm, HttpSession session) {
        this.session = session;
        initDataForm(dataForm);
        initExportPath();
        createExportFolder();
        executeExport();

        String basePath = Config.getString(Constants.KEY_EXPORT_BASE_PATH);
        createZIPFile(basePath + EXPORT_TEMP_FILE_FOLDER, basePath + EXPORT_TEMP_ZIP_FOLDER);
    }

    private void initDataForm(DataForm dataForm) {
        this.dataForm = dataForm;
        if (dataForm.isExportEntireSet()) {
            dataForm.setWsIndicatorIds(wiiDao.selectWsIndicatorIdsByWorksetId(dataForm.getWorksetId()));
            dataForm.setDatapointIds(dataPointDao.selectDatapointIdsByWorksetId(dataForm.getWorksetId()));
            dataForm.setTargetIds(targetDao.selectTargetIdsByWorksetId(dataForm.getWorksetId()));
            dataForm.setStudyPeriodIds(studyPeriodDao.selectStudyPeriodIdsByWorksetId(dataForm.getWorksetId()));
        }
    }

    protected void initExportPath() {
        EXPORT_BASE_PATH = Config.getString(Constants.KEY_EXPORT_BASE_PATH);
        EXPORT_TEMP_FILE_FOLDER = "/data_export_temporary";
        EXPORT_TEMP_ZIP_FOLDER = "/data_export_zip";
        EXPORT_ZIP_NAME = "/indaba_export.zip";
        EXPORT_SUMMARY_NAME = "/summary.csv";
        EXPORT_INDICATORS_NAME = "/indicators.csv";
        EXPORT_DATAPOINTS_NAME = "/datapoints.csv";
    }

    protected void createSummaryCSV() {
        DataSummaryInfo dataSummary = dataSummaryService.getDataSummaryInfoByDataForm(dataForm);
        try {
            FileOutputStream fos = new FileOutputStream(Config.getString(Constants.KEY_EXPORT_BASE_PATH)
                    + EXPORT_TEMP_FILE_FOLDER + EXPORT_SUMMARY_NAME, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            BufferedWriter writer = new BufferedWriter(osw);

            CSVWriter csvWriter = new CSVWriter(writer);
            csvWriter.writeAll(dataSummary.toCSVFile());

            // CLOSEFILE
            csvWriter.close();
            writer.close();
            osw.close();
            fos.close();
        } catch (IOException e) {
            logger.error("Meet error when create summary csv", e);
        }
    }

    private List<Integer> getIndicatorIds(List<WsIndicatorInstance> wiiList) {
        TreeSet<Integer> ts = new TreeSet<Integer>();
        for (WsIndicatorInstance wii : wiiList) {
            ts.add(wii.getIndicatorId());
        }
        return Arrays.asList(ts.toArray(new Integer[]{}));
    }


    private boolean isInList(List<Integer> list, int value) {
        if (list == null || list.isEmpty()) return false;
        for (Integer v : list) {
            if (v.intValue() == value) return true;
        }
        return false;
    }


    private List<Integer> getOrgIds(List<WsIndicatorInstance> wiiList, int indicatorId) {
        ArrayList<Integer> ts = new ArrayList<Integer>();
        for (WsIndicatorInstance wii : wiiList) {
            if (wii.getIndicatorId() == indicatorId) {
                if (!isInList(ts, wii.getOrgId())) ts.add(wii.getOrgId());
            }
        }
        return ts;
    }

    private void createIndicatorsCSV() {
        List<WsIndicatorInstance> wiiList;
        wiiList = wiiDao.selectWSIndicatorByIds(dataForm.getWsIndicatorIds());
        List<SurveyIndicator> surveyIndicators =
                scorecardQuestionDao.selectSurveyIndicatorsByIndicatorIds(getIndicatorIds(wiiList));

        List<String[]> csvFile = new ArrayList<String[]>();
        for (SurveyIndicator si : surveyIndicators) {
            IndicatorVO ivo = new IndicatorVO(si);
            ivo.setAnswerType(Constants.ANSWER_TYPES[si.getAnswerType()]);
            ivo.setOrgList(orgDao.selectOrganizationsByIds(getOrgIds(wiiList, si.getId())));
            List<String> scoreCriterias = new ArrayList<String>();
            switch (si.getAnswerType()) {
                // no multi, no text
                case Constants.ANSWER_TYPE_SINGLE:
//                case Constants.ANSWER_TYPE_MULTI:
                    List<AtcChoice> atcChoices = atcChoiceDao.getAtcChoicesByAnswerTypeChoiceId(si.getAnswerTypeId());
                    for (AtcChoice atcChoice : atcChoices) {
//                        if (si.getAnswerType() == Constants.ANSWER_TYPE_SINGLE) {
                        scoreCriterias.add(atcChoice.getLabel() + PART_SEPARATOR
                                + atcChoice.getScore() / 10000 + PART_SEPARATOR + atcChoice.getCriteria());
//                        } else {
//                            scoreCriterias.add(atcChoice.getLabel() + PART_SEPARATOR + atcChoice.getCriteria());
//                        }
                    }
                    break;
                case Constants.ANSWER_TYPE_INTEGER:
                    scoreCriterias.add(answerTypeDao.getAnswerTypeInteger(si.getAnswerTypeId()).getCriteria());
                    break;
                case Constants.ANSWER_TYPE_FLOAT:
                    scoreCriterias.add(answerTypeDao.getAnswerTypeFloat(si.getAnswerTypeId()).getCriteria());
                    break;
//                case Constants.ANSWER_TYPE_TEXT:
//                    scoreCriterias.add(answerTypeDao.getAnswerTypeText(si.getAnswerTypeId()).getCriteria());
//                    break;
            }
            ivo.setScoreCriterias(scoreCriterias);
            csvFile.add(ivo.toCSVLine());
        }

        try {
            FileOutputStream fos = new FileOutputStream(Config.getString(Constants.KEY_EXPORT_BASE_PATH)
                    + EXPORT_TEMP_FILE_FOLDER + EXPORT_INDICATORS_NAME, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            BufferedWriter writer = new BufferedWriter(osw);

            CSVWriter csvWriter = new CSVWriter(writer);
            csvWriter.writeAll(csvFile);
            csvWriter.close();

            // CLOSEFILE
            osw.close();
            fos.close();
        } catch (IOException e) {
            logger.error("Meet error when create indicators csv", e);
        }
    }

    private void createDPDefCSV() {
        List<Datapoint> dpList = dataPointDao.selectDatapointByDatapointIds(
                dataForm.getDatapointIds().toString().replace("[", "(").replace("]", ")"));
        List<String[]> csvFile = new ArrayList<String[]>();
        for (Datapoint dp : dpList) {
            DataPointVO dpVO = new DataPointVO();
            dpVO.setId(dp.getId());
            dpVO.setName(dp.getName());
            dpVO.setMethod(aggrMethodDao.get(dp.getAggrMethodId()).getName());
            List<DpMember> dpMembers = dpMemberDao.selectDpMemberByDpId(dp.getId());
            List<String> dpMemberStrList = new ArrayList<String>();
            for (DpMember dpMember : dpMembers) {
                // data point
                if (dpMember.getIndicatorInstanceId() == null || dpMember.getIndicatorInstanceId() < 1) {
                    dpMemberStrList.add("D" + dpMember.getDatapointId() + PART_SEPARATOR + dpMember.getWeight());
                } else {
                    WsIndicatorInstance wii = wiiDao.get(dpMember.getIndicatorInstanceId());
                    dpMemberStrList.add("I" + wii.getIndicatorId() + PART_SEPARATOR + "." + wii.getOrgId());
                }
            }
            dpVO.setMembers(dpMemberStrList);
            csvFile.add(dpVO.toCSVLine());
        }

        try {
            FileOutputStream fos = new FileOutputStream(Config.getString(Constants.KEY_EXPORT_BASE_PATH)
                    + EXPORT_TEMP_FILE_FOLDER + EXPORT_DATAPOINTS_NAME, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter writer = new BufferedWriter(osw);

            CSVWriter csvWriter = new CSVWriter(writer);
            csvWriter.writeAll(csvFile);
            csvWriter.close();

            // CLOSEFILE
            osw.close();
            fos.close();
        } catch (IOException e) {
            logger.error("Meet error when create indicators csv", e);
        }
    }

    private void createRawScore() {
        for (Integer spid : dataForm.getStudyPeriodIds()) {
            RawScoreVO rawScore = new RawScoreVO(spid);
            List<Target> targets = targetDao.selectTargetsByIds(dataForm.getTargetIds());
            List<List<ScorecardAnswerVO>> scorecardAnswers = new ArrayList<List<ScorecardAnswerVO>>();
            List<WsIndicatorInstance> wiiList = wiiDao.selectWSIndicatorByIds(dataForm.getWsIndicatorIds());

            // get table name
            com.ocs.indaba.aggregation.po.Config config = configDao.getConfig();
            
            for (Integer tid : dataForm.getTargetIds()) {
                List<ScorecardAnswerVO> saList =
                        scorecardAnswerDao.selectScorecardAnswerByFilter(getRawScoreFilter(spid, tid),
                        config.getSrvScorecard(), config.getSrvScorecardAnswer());
                scorecardAnswers.add(saList);
            }
            rawScore.setTargets(targets);
            rawScore.setScorecardAnswers(scorecardAnswers);
            rawScore.setWiiList(wiiList);

            try {
                FileOutputStream fos = new FileOutputStream(Config.getString(Constants.KEY_EXPORT_BASE_PATH)
                        + EXPORT_TEMP_FILE_FOLDER + "/raw." + spid + ".csv", true);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                BufferedWriter writer = new BufferedWriter(osw);

                CSVWriter csvWriter = new CSVWriter(writer);
                csvWriter.writeAll(rawScore.toCSVFile());
                csvWriter.close();

                // CLOSEFILE
                osw.close();
                fos.close();
            } catch (IOException e) {
                logger.error("Meet error when create raw score csv", e);
            }
        }
    }

    private String getRawScoreFilter(int spid, int tid) {
        int i;
        StringBuilder sb = new StringBuilder();
        sb.append("study_period_id = ").append(spid).append(" AND ");
        sb.append("target_id = ").append(tid).append(" AND ");
        List<WsIndicatorInstance> wiiList = wiiDao.selectWSIndicatorByIds(dataForm.getWsIndicatorIds());
        sb.append("(");
        for (i = 0; i < wiiList.size() - 1; i++) {
            WsIndicatorInstance wii = wiiList.get(i);
            sb.append("indicator_id = ").append(wii.getIndicatorId()).append(" AND org_id = ").append(wii.getOrgId()).append(" OR ");
        }
        WsIndicatorInstance wii = wiiList.get(i);
        sb.append("indicator_id = ").append(wii.getIndicatorId()).append(" AND org_id = ").append(wii.getOrgId()).append(")");
        return sb.toString();
    }

    private void createAggrScore() {
        for (Integer spid : dataForm.getStudyPeriodIds()) {
            AggrScoreVO aggrScore = new AggrScoreVO(spid);
            List<Target> targets = targetDao.selectTargetsByIds(dataForm.getTargetIds());
            List<List<TdsValue>> values = new ArrayList<List<TdsValue>>();
//            List<Datapoint> dpList = dataPointDao.selectDataPointByFilter(dataForm);
            List<Datapoint> dpList = dataPointDao.selectDatapointByDatapointIds(
                    dataForm.getDatapointIds().toString().replace("[", "(").replace("]", ")"));

            // get table name
            com.ocs.indaba.aggregation.po.Config config = configDao.getConfig();

            for (Integer tid : dataForm.getTargetIds()) {
                List<TdsValue> tdsList = tdsValueDao.selectTdsValueByFilter(getAggrScoreFilter(spid, tid),
                        config.getSrvTdsValue());
                values.add(tdsList);
            }
            aggrScore.setTargets(targets);
            aggrScore.setValues(values);
            aggrScore.setDatapoints(dpList);

            try {
                FileOutputStream fos = new FileOutputStream(Config.getString(Constants.KEY_EXPORT_BASE_PATH)
                        + EXPORT_TEMP_FILE_FOLDER + "/aggr." + spid + ".csv", true);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                BufferedWriter writer = new BufferedWriter(osw);

                CSVWriter csvWriter = new CSVWriter(writer);
                csvWriter.writeAll(aggrScore.toCSVFile());
                csvWriter.close();

                // CLOSEFILE
                osw.close();
                fos.close();
            } catch (IOException e) {
                logger.error("Meet error when create raw score csv", e);
            }
        }
    }

    private String getAggrScoreFilter(int spid, int tid) {
        int include_nonpub_data = (dataForm.isIncludeUnverifiedData()) ? 1 : 0;
        StringBuilder sb = new StringBuilder();
        sb.append("study_period_id = ").append(spid).append(" AND ");
        sb.append("target_id = ").append(tid).append(" AND ");
        sb.append("dp.workset_id = ").append(dataForm.getWorksetId()).append(" AND ");
        sb.append("includes_nonpub_data = ").append(include_nonpub_data);
        return sb.toString();
    }

    protected void executeExport() {
        createSummaryCSV();
        if (dataForm.getWsIndicatorIds() != null && !dataForm.getWsIndicatorIds().isEmpty()) {
            createIndicatorsCSV();
            createRawScore();
        }
        if (dataForm.getDatapointIds() != null && !dataForm.getDatapointIds().isEmpty()) {
            createDPDefCSV();
            createAggrScore();
        }
    }

    @Autowired
    public void setTdsValueDao(TdsValueDAO tdsValueDao) {
        this.tdsValueDao = tdsValueDao;
    }

    @Autowired
    public void setTargetDao(TargetDAO targetDao) {
        this.targetDao = targetDao;
    }

    @Autowired
    public void setScorecardAnswerDao(ScorecardAnswerDAO scorecardAnswerDao) {
        this.scorecardAnswerDao = scorecardAnswerDao;
    }

    @Autowired
    public void setDataPointDao(DatapointDAO dataPointDao) {
        this.dataPointDao = dataPointDao;
    }

    @Autowired
    public void setScorecardQuestionDao(ScorecardQuestionDAO scorecardQuestionDao) {
        this.scorecardQuestionDao = scorecardQuestionDao;
    }

    @Autowired
    public void setAtcChoiceDao(AtcChoiceDAO atcChoiceDao) {
        this.atcChoiceDao = atcChoiceDao;
    }

    @Autowired
    public void setAnswerTypeDao(AnswerTypeDAO answerTypeDao) {
        this.answerTypeDao = answerTypeDao;
    }

    @Autowired
    public void setPubOrganizationDao(PubOrganizationDAO pubOrganizationDao) {
        this.pubOrganizationDao = pubOrganizationDao;
    }

    @Autowired
    public void setOrgDao(OrganizationDAO orgDao) {
        this.orgDao = orgDao;
    }

    @Autowired
    public void setAggrMethodDao(AggrMethodDAO aggrMethodDao) {
        this.aggrMethodDao = aggrMethodDao;
    }

    @Autowired
    public void setDpMemberDao(DpMemberDAO dpMemberDao) {
        this.dpMemberDao = dpMemberDao;
    }

    @Autowired
    public void setWorksetIndicatorInstanceDao(WorksetIndicatorInstanceDAO wiiDao) {
        this.wiiDao = wiiDao;
    }

    @Autowired
    public void setStudyPeriodDao(StudyPeriodDAO studyPeriodDao) {
        this.studyPeriodDao = studyPeriodDao;
    }

    @Autowired
    public void setConfigDao(ConfigDAO configDao) {
        this.configDao = configDao;
    }

    @Autowired
    public void setDataSummaryService(DataSummaryService dataSummaryService) {
        this.dataSummaryService = dataSummaryService;
    }
}
