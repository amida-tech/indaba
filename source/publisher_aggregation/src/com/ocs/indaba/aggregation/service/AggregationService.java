/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.service;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.dao.AggrMethodDAO;
import com.ocs.indaba.aggregation.dao.ConfigDAO;
import com.ocs.indaba.aggregation.dao.DatapointDAO;
import com.ocs.indaba.aggregation.dao.DatasetDAO;
import com.ocs.indaba.aggregation.dao.DpMemberDAO;
import com.ocs.indaba.aggregation.dao.OtisValueADAO;
import com.ocs.indaba.aggregation.dao.OtisValueBDAO;
import com.ocs.indaba.aggregation.dao.ScorecardADAO;
import com.ocs.indaba.aggregation.dao.ScorecardAnswerADAO;
import com.ocs.indaba.aggregation.dao.ScorecardAnswerBDAO;
import com.ocs.indaba.aggregation.dao.ScorecardBDAO;
import com.ocs.indaba.aggregation.dao.TdsValueADAO;
import com.ocs.indaba.aggregation.dao.TdsValueBDAO;
import com.ocs.indaba.aggregation.dao.WorksetDAO;
import com.ocs.indaba.aggregation.dao.WorksetIndicatorInstanceDAO;
import com.ocs.indaba.aggregation.dao.WorksetProjectDAO;
import com.ocs.indaba.aggregation.dao.WorksetTargetDAO;
import com.ocs.indaba.aggregation.po.AggrMethod;
import com.ocs.indaba.aggregation.po.Config;
import com.ocs.indaba.aggregation.po.Datapoint;
import com.ocs.indaba.aggregation.po.Dataset;
import com.ocs.indaba.aggregation.po.DpMember;
import com.ocs.indaba.aggregation.po.OtisValue;
import com.ocs.indaba.aggregation.po.OtisValueA;
import com.ocs.indaba.aggregation.po.OtisValueB;
import com.ocs.indaba.aggregation.po.Scorecard;
import com.ocs.indaba.aggregation.po.ScorecardAnswer;
import com.ocs.indaba.aggregation.po.TdsValue;
import com.ocs.indaba.aggregation.po.TdsValueA;
import com.ocs.indaba.aggregation.po.TdsValueB;
import com.ocs.indaba.aggregation.po.Workset;
import com.ocs.indaba.aggregation.po.WsIndicatorInstance;
import com.ocs.indaba.aggregation.po.WsTarget;
import com.ocs.indaba.aggregation.vo.AggBuildDataVO;
import com.ocs.indaba.aggregation.vo.AggIndicatorVO;
import com.ocs.indaba.aggregation.vo.AggMethodVO;
import com.ocs.indaba.aggregation.vo.AggregationForm;
import com.ocs.indaba.aggregation.vo.TSDataVO;
import com.ocs.indaba.aggregation.vo.WorksetVO;
import com.ocs.indaba.builder.dao.ProjectDAO;
import com.ocs.indaba.builder.dao.ScorecardQuestionDAO;
import com.ocs.indaba.builder.dao.UserDAO;
import com.ocs.indaba.common.OrgAuthorizer;
import com.ocs.indaba.dao.OrganizationDAO;
import com.ocs.indaba.po.Organization;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.po.User;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author luwb
 */
public class AggregationService {

    private static final Logger log = Logger.getLogger(AggregationService.class);
    private UserDAO userDao = null;
    private ProjectDAO projectDao;
    private WorksetDAO worksetDao = null;
    private WorksetIndicatorInstanceDAO wsIndicatorDao = null;
    private OrganizationDAO orgDao = null;
    private ScorecardQuestionDAO scorecardQuestionDao = null;
    private AggrMethodDAO aggrMethodDao = null;
    private DatapointDAO datapointDao = null;
    private DpMemberDAO dpMemberDao = null;
    private OtisValueADAO otisValueADao = null;
    private OtisValueBDAO otisValueBDao = null;
    private TdsValueADAO tdsValueADao = null;
    private TdsValueBDAO tdsValueBDao = null;
    private DatasetDAO datasetDao = null;
    private ScorecardAnswerADAO scorecardAnswerADao = null;
    private ScorecardAnswerBDAO scorecardAnswerBDao = null;
    private ScorecardADAO scorecardADao = null;
    private ScorecardBDAO scorecardBDao = null;
    private WorksetProjectDAO worksetProjectDao = null;
    private WorksetTargetDAO worksetTargetDao = null;
    private IndicatorService indicatorService = null;
    private ConfigDAO configDao = null;
    public static String INDICATOR_PREFIX = "indicator";
    public static String DATAPOINT_PREFIX = "datapoint";
    public static int INDICATOR_TYPE = 0;
    public static int DATAPOINT_TYPE = 1;
    public static int STRICT_POLOCY = 1;
    public static int CASUAL_POLOCY = 2;
    public static final String ATTR_AGGREGATION = "aggregation";

    @Autowired
    public void setProjectDao(ProjectDAO projectDao) {
        this.projectDao = projectDao;
    }

    @Autowired
    public void setUserDAO(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setDatapointDAO(DatapointDAO datapointDao) {
        this.datapointDao = datapointDao;
    }

    @Autowired
    public void setDpMemberDAO(DpMemberDAO dpMemberDao) {
        this.dpMemberDao = dpMemberDao;
    }

    @Autowired
    public void AggrMethodDAO(AggrMethodDAO aggrMethodDao) {
        this.aggrMethodDao = aggrMethodDao;
    }

    @Autowired
    public void setWorksetDAO(WorksetDAO worksetDao) {
        this.worksetDao = worksetDao;
    }

    @Autowired
    public void setWorksetIndicatorInstanceDAO(WorksetIndicatorInstanceDAO wsIndicatorDao) {
        this.wsIndicatorDao = wsIndicatorDao;
    }

    @Autowired
    public void setOrganizationDAO(OrganizationDAO orgDao) {
        this.orgDao = orgDao;
    }

    @Autowired
    public void setScorecardQuestionDao(ScorecardQuestionDAO scorecardQuestionDao) {
        this.scorecardQuestionDao = scorecardQuestionDao;
    }

    @Autowired
    public void setOtisValueADAO(OtisValueADAO otisValueADao) {
        this.otisValueADao = otisValueADao;
    }

    @Autowired
    public void setOtisValueBDAO(OtisValueBDAO otisValueBDao) {
        this.otisValueBDao = otisValueBDao;
    }

    @Autowired
    public void setTdsValueADAO(TdsValueADAO tdsValueADao) {
        this.tdsValueADao = tdsValueADao;
    }

    @Autowired
    public void setTdsValueBDAO(TdsValueBDAO tdsValueBDao) {
        this.tdsValueBDao = tdsValueBDao;
    }

    @Autowired
    public void setDatasetDAO(DatasetDAO datasetDao) {
        this.datasetDao = datasetDao;
    }

    @Autowired
    public void setScorecardAnswerADAO(ScorecardAnswerADAO scorecardAnswerADao) {
        this.scorecardAnswerADao = scorecardAnswerADao;
    }

    @Autowired
    public void setScorecardAnswerBDAO(ScorecardAnswerBDAO scorecardAnswerBDao) {
        this.scorecardAnswerBDao = scorecardAnswerBDao;
    }

    @Autowired
    public void setScorecardADAO(ScorecardADAO scorecardADao) {
        this.scorecardADao = scorecardADao;
    }

    @Autowired
    public void setScorecardBDAO(ScorecardBDAO scorecardBDao) {
        this.scorecardBDao = scorecardBDao;
    }

    @Autowired
    public void setWorksetProjectDAO(WorksetProjectDAO worksetProjectDao) {
        this.worksetProjectDao = worksetProjectDao;
    }

    @Autowired
    public void setWorksetTargetDAO(WorksetTargetDAO worksetTargetDao) {
        this.worksetTargetDao = worksetTargetDao;
    }

    @Autowired
    public void setConfigDAO(ConfigDAO configDao) {
        this.configDao = configDao;
    }

    @Autowired
    public void setIndicatorService(IndicatorService indicatorService) {
        this.indicatorService = indicatorService;
    }


    public List<Workset> getWorksetByUser(int userId) {
        if (userId < 0) {
            List<Workset> worksets = worksetDao.selectPublicWorksets();
            return worksets;
        }
        User user = userDao.get(userId);
        if (user.getSiteAdmin() == 1) {//admin user
            //List<Workset> worksets = worksetDao.selectAllActiveWorkset();
            List<Workset> worksets = worksetDao.selectAllWorkset();
            return worksets;
        } else {
            int oragnizationId = userDao.selectOrgIdByUserId(userId);
            List<Workset> worksets = worksetDao.selectPublicWorksets();
            List<Workset> orgWorksets = worksetDao.selectPrivateWorksetsByOrgId(oragnizationId);
            worksets.addAll(orgWorksets);
            return worksets;
        }
    }

    public List<Workset> getManageWorksetByUser(int userId) {
        List<Workset> worksets = null;
        User user = userDao.get(userId);
        if (user != null) {
            OrgAuthorizer orgAuth = new OrgAuthorizer(user);

            if (orgAuth.isSiteAdmin()) {//admin user
                worksets = worksetDao.selectAllWorkset();
            } else {
                List<Integer> orgIds = orgAuth.getAccessibleOrgIdList(com.ocs.indaba.common.Constants.VISIBILITY_PRIVATE);

                if (orgIds == null || orgIds.isEmpty()) return null;

                worksets = worksetDao.selectManageWorksetsByOrgIds(orgIds);
            }
        }
        return worksets;
    }

    public List<Workset> getAllWorkset() {
        return worksetDao.selectAllActiveWorkset();
    }

    public Workset getWorkset(int worksetId) {
        return this.worksetDao.get(worksetId);
    }

    public List<AggIndicatorVO> getAggIndicatorsByWorksetId(int worksetId) {
        //get indicators
        List<WsIndicatorInstance> wsIndicatorList = this.wsIndicatorDao.selectWSIndicatorByWorksetId(worksetId);
        List<AggIndicatorVO> indicators = new ArrayList<AggIndicatorVO>();
        for (WsIndicatorInstance wsIndicator : wsIndicatorList) {
            int indicatorId = wsIndicator.getIndicatorId();
            int orgId = wsIndicator.getOrgId();
            Organization org = orgDao.get(orgId);
            if (null == org) {
                continue;
            }

            SurveyIndicator surveyIndicator = scorecardQuestionDao.get(indicatorId);
            if (null == surveyIndicator) {
                continue;
            }

            String aggIndicatorId = INDICATOR_PREFIX + "-" + indicatorId;
            AggIndicatorVO vo = new AggIndicatorVO();
            vo.setType(INDICATOR_TYPE);
            vo.setId(aggIndicatorId);
            vo.setOrgId(orgId);
            vo.setName(surveyIndicator.getName());
            vo.setDisplayName(surveyIndicator.getQuestion());
            vo.setOrgName(org.getName());
            vo.setDbId(indicatorId);

            String tagName = indicatorService.getItagsByIndicatorId(indicatorId);
            vo.setTag(tagName);
            indicators.add(vo);
        }

        //get datapoints
        List<Datapoint> datapoints = this.datapointDao.selectDataPointByWorksetId(worksetId);
        for (Datapoint dp : datapoints) {
            AggIndicatorVO vo = new AggIndicatorVO();
            String aggIndicatorId = DATAPOINT_PREFIX + "-" + dp.getId();
            vo.setType(DATAPOINT_TYPE);
            vo.setId(aggIndicatorId);
            vo.setName(dp.getShortName());
            vo.setDisplayName(dp.getName());
            vo.setOrgName("");
            vo.setDbId(dp.getId());
            indicators.add(vo);
        }
        return indicators;
    }

    public List<AggIndicatorVO> getAggIndicatorsByWorksetAndDatapoint(int worksetId, int datapointId) {
        //get indicators
        List<WsIndicatorInstance> wsIndicatorList = this.wsIndicatorDao.selectWSIndicatorByWorksetId(worksetId);
        List<AggIndicatorVO> indicators = new ArrayList<AggIndicatorVO>();
        for (WsIndicatorInstance wsIndicator : wsIndicatorList) {
            int indicatorId = wsIndicator.getIndicatorId();
            int orgId = wsIndicator.getOrgId();
            Organization org = orgDao.get(orgId);
            if (null == org) {
                continue;
            }

            SurveyIndicator surveyIndicator = scorecardQuestionDao.get(indicatorId);
            if (null == surveyIndicator) {
                continue;
            }

            String aggIndicatorId = INDICATOR_PREFIX + "-" + indicatorId;
            AggIndicatorVO vo = new AggIndicatorVO();
            vo.setType(INDICATOR_TYPE);
            vo.setId(aggIndicatorId);
            vo.setOrgId(orgId);
            vo.setName(surveyIndicator.getName());
            vo.setDisplayName(surveyIndicator.getQuestion());
            vo.setOrgName(org.getName());
            vo.setDbId(indicatorId);

            String tagName = indicatorService.getItagsByIndicatorId(indicatorId);
            vo.setTag(tagName);
            indicators.add(vo);
        }

        //get datapoints which are not ancestor of this datapoint
        Set<Integer> ancestors = new HashSet<Integer>();
        ancestors.add(datapointId);
        getNotAncestorDatapoint(datapointId, ancestors);

        List<Datapoint> datapoints = this.datapointDao.selectDataPointByWorksetId(worksetId);
        for (Datapoint dp : datapoints) {
            if(!ancestors.contains(dp.getId())){
                AggIndicatorVO vo = new AggIndicatorVO();
                String aggIndicatorId = DATAPOINT_PREFIX + "-" + dp.getId();
                vo.setType(DATAPOINT_TYPE);
                vo.setId(aggIndicatorId);
                vo.setName(dp.getShortName());
                vo.setDisplayName(dp.getName());
                vo.setOrgName("");
                vo.setDbId(dp.getId());
                indicators.add(vo);
            }
        }
        return indicators;
    }

    private void getNotAncestorDatapoint(int datapointId, Set<Integer> ancestor){
        List<Integer> parents = this.dpMemberDao.selectDatapointIdsByDpId(datapointId);
        if(parents == null || parents.size() == 0)
            return;

        for(int parentId : parents){
            if(!ancestor.contains(parentId)){
                ancestor.add(parentId);
                getNotAncestorDatapoint(parentId, ancestor);
            }
        }
    }

    public List<Datapoint> getDataPointsByWorksetId(int worksetId) {
        return this.datapointDao.selectDataPointByWorksetId(worksetId);
    }

    public List<Datapoint> getAllDataPoints() {
        return this.datapointDao.selectAllDatapoints();
    }

    public List<Datapoint> getDataPointsByUser(int uid) {
        List<Workset> worksets = this.getWorksetByUser(uid);
        if (worksets == null || worksets.size() == 0) {
            return null;
        }

        List<Integer> worksetIds = new ArrayList<Integer>(worksets.size());
        for (Workset workset : worksets) {
            worksetIds.add(workset.getId());
        }
        return this.datapointDao.selectDatapointByWorksetIds(worksetIds);
    }

    public Datapoint getDatapintById(int datapointId) {
        return this.datapointDao.get(datapointId);
    }

    public boolean deleteDatapoint(int datapointId) {
        this.datapointDao.delete(datapointId);
        this.dpMemberDao.deleteDpMemberByDatapointId(datapointId);
        if (this.datapointDao.exists(datapointId)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean hasDatapointReferenced(int datapointId){
        List<DpMember> dps = this.dpMemberDao.selectDpMemberByDpId(datapointId);
        if(dps == null || dps.size() == 0)
            return false;
        else
            return true;
    }

    public String getWorksetName(int worksetId) {
        Workset ws = worksetDao.get(worksetId);
        if (ws == null) {
            return "";
        }

        return ws.getName();
    }

    public List<AggIndicatorVO> getAggIndicatorsByDatapointId(int datapointId) {
        Datapoint datapoint = this.datapointDao.get(datapointId);
        List<DpMember> dps = this.dpMemberDao.selectDpMemberByDatapointId(datapointId);
        if (dps == null || dps.size() == 0) {
            return null;
        }

        List<Integer> indicatorList = new ArrayList<Integer>();
        List<Integer> datapointList = new ArrayList<Integer>();
        for (DpMember dp : dps) {
            if (dp.getIndicatorInstanceId() > 0) {//indicators
                indicatorList.add(dp.getIndicatorInstanceId());
            } else {
                datapointList.add(dp.getDpId());
            }
        }
        return getAggIndicatorsByIds(datapoint.getWorksetId(), indicatorList, datapointList);
    }

    public List<AggIndicatorVO> getAggIndicatorsByIds(int worksetId, List<Integer> indicatorList, List<Integer> datapointList) {
        List<AggIndicatorVO> aggIndicators = new ArrayList<AggIndicatorVO>();

        //get indicators
        if (indicatorList != null && indicatorList.size() > 0) {
            String strIndicators = "(";
            for (int i = 0; i < indicatorList.size(); i++) {
                strIndicators += indicatorList.get(i).toString();
                if (i != (indicatorList.size() - 1)) {
                    strIndicators += ",";
                }
            }
            strIndicators += ")";
            List<WsIndicatorInstance> wsIndicatorList = this.wsIndicatorDao.selectWSIndicatorByWorksetIdAndIndicatorIds(worksetId, strIndicators);
            for (WsIndicatorInstance wsIndicator : wsIndicatorList) {
                int indicatorId = wsIndicator.getIndicatorId();
                int orgId = wsIndicator.getOrgId();
                Organization org = orgDao.get(orgId);
                if (null == org) {
                    continue;
                }

                SurveyIndicator surveyIndicator = scorecardQuestionDao.get(indicatorId);
                if (null == surveyIndicator) {
                    continue;
                }

                String aggIndicatorId = INDICATOR_PREFIX + "-" + indicatorId;
                AggIndicatorVO vo = new AggIndicatorVO();
                vo.setType(INDICATOR_TYPE);
                vo.setId(aggIndicatorId);
                vo.setOrgId(orgId);
                vo.setName(surveyIndicator.getName());
                vo.setDisplayName(surveyIndicator.getQuestion());
                vo.setOrgName(org.getName());
                vo.setDbId(indicatorId);
                aggIndicators.add(vo);
            }
        }
        //get datapoints
        if (datapointList != null && datapointList.size() > 0) {
            String strDatapoints = "(";
            for (int i = 0; i < datapointList.size(); i++) {
                strDatapoints += datapointList.get(i).toString();
                if (i != (datapointList.size() - 1)) {
                    strDatapoints += ",";
                }
            }
            strDatapoints += ")";
            List<Datapoint> datapoints = this.datapointDao.selectDatapointByDatapointIds(strDatapoints);
            for (Datapoint dp : datapoints) {
                AggIndicatorVO vo = new AggIndicatorVO();
                String aggIndicatorId = DATAPOINT_PREFIX + "-" + dp.getId();
                vo.setType(DATAPOINT_TYPE);
                vo.setId(aggIndicatorId);
                vo.setName(dp.getShortName());
                vo.setDisplayName(dp.getName());
                vo.setOrgName("");
                vo.setDbId(dp.getId());
                aggIndicators.add(vo);
            }
        }

        //sort by id
        Collections.sort(aggIndicators);
        return aggIndicators;
    }

    public List<AggMethodVO> getAllAggrMethod() {
        List<AggrMethod> aggMethods = aggrMethodDao.selectALLAggrMethod();
        if (aggMethods.size() == 0) {
            return null;
        }

        List<AggMethodVO> aggMethodVOs = new ArrayList<AggMethodVO>();
        for (AggrMethod method : aggMethods) {
            AggMethodVO vo = new AggMethodVO();
            vo.setId(method.getId());
            vo.setName(method.getName());
            vo.setDescription(method.getDescription());
            if (method.getName().toLowerCase().startsWith("weighted")) {
                vo.setShowWeight(1);
            } else {
                vo.setShowWeight(0);
            }
            aggMethodVOs.add(vo);
        }

        return aggMethodVOs;
    }

    public AggMethodVO getAggrMethodById(int methodId) {
        AggrMethod method = aggrMethodDao.get(methodId);
        AggMethodVO vo = new AggMethodVO();
        vo.setId(methodId);
        vo.setName(method.getName());
        vo.setDescription(method.getDescription());
        if (method.getName().toLowerCase().startsWith("weighted")) {
            vo.setShowWeight(1);
        } else {
            vo.setShowWeight(0);
        }

        return vo;
    }

    public boolean isAggNameExist(int worksetId, String name) {
        Datapoint dp = this.datapointDao.selectDataPointByWroksetIdAndName(worksetId, name);
        if (null == dp) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isAggShortNameExist(int worksetId, String name) {
        Datapoint dp = this.datapointDao.selectDataPointByWroksetIdAndShortName(worksetId, name);
        if (null == dp) {
            return false;
        } else {
            return true;
        }
    }

    public boolean saveAggregation(AggregationForm form) {
        boolean update = false;
        if (form.getId() > 0) {
            update = true;
        }

        int dpId = 0;
        Datapoint dp = new Datapoint();
        dp.setName(form.getName());
        dp.setShortName(form.getShortName());
        dp.setDescription(form.getDescription());
        dp.setWorksetId(form.getWorksetId());
        dp.setAggrMethodId(form.getAggMethod().getId());
        dp.setHolePolicy((short) form.getHolePolicy());
        if (update) {
            dpId = form.getId();
            dp.setId(dpId);
            this.datapointDao.update(dp);
        } else {
            Datapoint cdp = this.datapointDao.create(dp);
            if (null == cdp) {
                return false;
            }
            dpId = cdp.getId();
        }

        List<AggIndicatorVO> indicators = form.getAggIndicators();
        if (indicators.size() > 0 && update) {
            this.dpMemberDao.deleteDpMemberByDatapointId(dpId);
        }
        for (AggIndicatorVO vo : indicators) {
            DpMember dm = new DpMember();
            dm.setDatapointId(dpId);
            if (vo.getType() == INDICATOR_TYPE) {
                dm.setIndicatorInstanceId(vo.getDbId());
                dm.setDpId(0);
            } else if (vo.getType() == DATAPOINT_TYPE) {
                dm.setIndicatorInstanceId(0);
                dm.setDpId(vo.getDbId());
            } else {
                log.error("bad AggIndicatorVO type:" + vo.getType());
                continue;
            }
            if (form.getAggMethod().getShowWeight() == 1) {
                dm.setWeight(vo.getWeight());
            }

            //if(this.dpMemberDao.save(dm) == null)
            //    return false;
            this.dpMemberDao.save(dm);
        }
        return true;
    }

    public AggBuildDataVO processAggregationData(Config conf, int worksetId) {
        List<WsIndicatorInstance> wsIndicators = this.wsIndicatorDao.selectWSIndicatorByWorksetId(worksetId);
        if (wsIndicators == null || wsIndicators.size() == 0) {
            return null;
        }

        List<Integer> indicators = new ArrayList<Integer>();
        HashMap<Integer, Integer> indicatorMaps = new HashMap<Integer, Integer>();
        for (WsIndicatorInstance ws : wsIndicators) {
            indicators.add(ws.getIndicatorId());
            indicatorMaps.put(ws.getIndicatorId(), ws.getOrgId());
        }
        if (indicators == null || indicators.size() == 0) {
            return null;
        }

        List<Integer> projectIds = this.worksetProjectDao.selectProjectIdsByWorksetId(worksetId);
        if (projectIds == null || projectIds.size() == 0) {
            return null;
        }

        List<Integer> studyPeriodIds = this.projectDao.selectStudyPeriodIdsByProjectIds(projectIds);
        if (studyPeriodIds == null || studyPeriodIds.size() == 0) {
            return null;
        }

        List<WsTarget> targets = this.worksetTargetDao.selectWsTargetsByWorksetId(worksetId);
        if (targets == null || targets.size() == 0) {
            return null;
        }

        //List<Datapoint> datapoints = this.datapointDao.selectDataPointByWorksetId(worksetId);
        List<Datapoint> datapoints = getDatapointTree(worksetId);
        log.debug(datapoints);
        AggBuildDataVO aggData = new AggBuildDataVO();
        for (int studyPeriodId : studyPeriodIds) {
            for (WsTarget target : targets) {
                int targetId = target.getTargetId();
                List<Scorecard> scorecards = getScorecardByConfig(conf, studyPeriodId, targetId, projectIds, Constants.SCORECARD_STATUS_SUBMITTED);
                if (scorecards == null || scorecards.size() == 0) {
                    continue;
                }

                TSDataVO tsData = new TSDataVO();
                tsData.setStudyPeriodId(studyPeriodId);
                tsData.setTargetId(targetId);
                //first get new indicator values
                for (Scorecard s : scorecards) {
                    boolean isPublished = false;

                    if (s.getStatus() == Constants.SCORECARD_STATUS_COMPLETED) {
                        isPublished = true;
                    }
                    //todo: should check lastupdatetime
                    List<ScorecardAnswer> answers = getScorecardAnswerByConfig(conf, s.getScorecardId(), indicators);
                    for (ScorecardAnswer answer : answers) {
                        OtisValue data = new OtisValue();
                        data.setIndicatorId(answer.getIndicatorId());
                        data.setStudyPeriodId(studyPeriodId);
                        data.setTargetId(targetId);
                        Integer orgId = indicatorMaps.get(answer.getIndicatorId());
                        if (orgId != null) {
                            data.setOrgId(orgId);
                        }
                        data.setDataType(answer.getDataType());
                        data.setValue(answer.getValue());
                        data.setScore(answer.getScore());
                        Integer key = answer.getIndicatorId();
                        if (isPublished) {
                            tsData.getPub_indicators().add(data);
                            tsData.getPub_indicatorMap().put(key, data);
                        } else {
                            tsData.getNonpub_indicators().add(data);
                            tsData.getNonpub_indicatorMap().put(key, data);
                        }
                    }
                }
                //then calculate datapoint value for each studyPeriodId and targetId
                processDatapoint(datapoints, tsData);
                String key = targetId + "_" + studyPeriodId;
                aggData.getTsDataList().add(tsData);
            }
        }
        return aggData;
    }

    private List<Scorecard> getScorecardByConfig(Config conf, int studyPeriodId, int targetId, List<Integer> projectIds, int status){
        String table = conf.getComScorecard().trim();
        if(table.equalsIgnoreCase(Constants.TABLE_NAME_SCORECARD_A))
            return this.scorecardADao.selectScorecardByProjectTargetAndStudyPeriod(studyPeriodId, targetId, projectIds, status);
        else if(table.equalsIgnoreCase(Constants.TABLE_NAME_SCORECARD_B))
            return this.scorecardBDao.selectScorecardByProjectTargetAndStudyPeriod(studyPeriodId, targetId, projectIds, status);
        else{
            log.error("bad name for config ComScorecard:" + table);
            return null;
        }
    }

    private  List<ScorecardAnswer> getScorecardAnswerByConfig(Config conf, int scorecardId, List<Integer> indicatorIds){
        String table = conf.getComScorecardAnswer().trim();
        if(table.equalsIgnoreCase(Constants.TABLE_NAME_SCORECARD_ANSWER_A))
            return this.scorecardAnswerADao.selectScorecardAnswerByScorecardAndIndicatorId(scorecardId, indicatorIds);
        else if(table.equalsIgnoreCase(Constants.TABLE_NAME_SCORECARD_ANSWER_B))
            return this.scorecardAnswerBDao.selectScorecardAnswerByScorecardAndIndicatorId(scorecardId, indicatorIds);
        else{
            log.error("bad name for config ComScorecardAnswer:" + table);
            return null;
        }
    }

    private TdsValueA tdsToTdsA(TdsValue value){
        if(value == null)
            return null;

        TdsValueA a = new TdsValueA();
        a.setId(value.getId());
        a.setDatapointId(value.getDatapointId());
        a.setDatasetId(value.getDatasetId());
        a.setStudyPeriodId(value.getStudyPeriodId());
        a.setTargetId(value.getTargetId());
        a.setValue(value.getValue());
        return a;
    }

    private TdsValueB tdsToTdsB(TdsValue value){
        if(value == null)
            return null;

        TdsValueB b = new TdsValueB();
        b.setId(value.getId());
        b.setDatapointId(value.getDatapointId());
        b.setDatasetId(value.getDatasetId());
        b.setStudyPeriodId(value.getStudyPeriodId());
        b.setTargetId(value.getTargetId());
        b.setValue(value.getValue());
        return b;
    }

    private OtisValueA otisToOtisA(OtisValue value){
        if(value == null)
            return null;

        OtisValueA a = new OtisValueA();
        a.setDataType(value.getDataType());
        a.setId(value.getId());
        a.setIndicatorId(value.getIndicatorId());
        a.setOrgId(value.getOrgId());
        a.setScore(value.getScore());
        a.setStudyPeriodId(value.getStudyPeriodId());
        a.setTargetId(value.getTargetId());
        a.setValue(value.getValue());
        return a;
    }

    private OtisValueB otisToOtisB(OtisValue value){
        if(value == null)
            return null;

        OtisValueB b = new OtisValueB();
        b.setDataType(value.getDataType());
        b.setId(value.getId());
        b.setIndicatorId(value.getIndicatorId());
        b.setOrgId(value.getOrgId());
        b.setScore(value.getScore());
        b.setStudyPeriodId(value.getStudyPeriodId());
        b.setTargetId(value.getTargetId());
        b.setValue(value.getValue());
        return b;
    }

    private List<Datapoint> getDatapointTree(int worksetId){
        List<Datapoint> datapoints = this.datapointDao.selectDataPointByWorksetId(worksetId);
        if(datapoints == null || datapoints.size() == 0)
            return null;

        List<Datapoint> result = new ArrayList<Datapoint>();
        
        class Node extends Object{
            //public HashSet<Integer> parents = null;
            public Set<Integer> childrens = null;
            public Datapoint dp;

            @Override
            public int hashCode(){
                return dp.getId().hashCode();
            }

            @Override
            public boolean equals(Object obj){
                Node node = (Node)obj;
                return this.dp.getId().equals(node.dp.getId());
            }

        }

        Set<Node> nodes = new HashSet<Node>(datapoints.size());
        for(Datapoint dp : datapoints){
            Node node = new Node();
            node.dp = dp;
            List<Integer> childrens = this.dpMemberDao.selectDpIdsByDatapointId(dp.getId());
            if(childrens != null && childrens.size() > 0){
                node.childrens = new HashSet<Integer>(childrens);
            }

            //List<Integer> parents = this.dpMemberDao.selectDatapointIdsByDpId(dp.getId());
            //if(parents != null && parents.size() > 0)
               // node.parents.addAll(parents);
            
            nodes.add(node);
        }

        while(nodes.size() > 0){
            //find the node whose children is empty
            List<Node> noChild =  new ArrayList<Node>();
            for(Node node : nodes){
                if(node.childrens == null || node.childrens.size() == 0)
                    noChild.add(node);
            }
            //must have empty children node
            if(noChild.size() == 0){
                log.error("bad datapint tree, can't generate relationship");
                return null;
            }
            //remove node
            for(Node c : noChild){
                nodes.remove(c);
                result.add(c.dp);
            }
            //update children
            for(Node node : nodes){
                for(Node c : noChild){
                    int cdpId = c.dp.getId();
                    if(node.childrens.contains(cdpId))
                        node.childrens.remove(cdpId);
                }
            }
        }

        return result;
    }

    private void processDatapoint(List<Datapoint> datapoints, TSDataVO tsData) {
        if (datapoints == null || datapoints.size() == 0) {
            return;
        }

        int studyPeriodId = tsData.getStudyPeriodId();
        int targetId = tsData.getTargetId();
        for (Datapoint datapoint : datapoints) {
            int datapointId = datapoint.getId();
            List<DpMember> dpMembers = this.dpMemberDao.selectDpMemberByDatapointId(datapointId);
            if (dpMembers == null || dpMembers.size() == 0) {
                continue;
            }

            AggrMethod method = this.aggrMethodDao.get(datapoint.getAggrMethodId());
            List<Double> pubValues = new ArrayList<Double>();
            List<Integer> pubWeights = new ArrayList<Integer>();
            List<Double> allValues = new ArrayList<Double>();
            List<Integer> allWeights = new ArrayList<Integer>();
            for (DpMember dp : dpMembers) {
                if (dp.getIndicatorInstanceId() > 0) {//indicator type
                    Integer key = dp.getIndicatorInstanceId();
                    //published data
                    OtisValue pubData = null;
                    pubData = (OtisValue) tsData.getPub_indicatorMap().get(key);
                    if (pubData != null) {
                        pubValues.add(pubData.getScore().doubleValue());
                        pubWeights.add(dp.getWeight());
                        allValues.add(pubData.getScore().doubleValue());
                        allWeights.add(dp.getWeight());
                    } else {
                        //nonPublished data
                        OtisValue nonPubData = null;
                        nonPubData = (OtisValue) tsData.getNonpub_indicatorMap().get(key);
                        if (nonPubData != null) {
                            allValues.add(nonPubData.getScore().doubleValue());
                            allWeights.add(dp.getWeight());
                        }
                    }
                } else if (dp.getDpId() > 0) {//datapoint type
                    Integer key = dp.getDpId();
                    //published data
                    TdsValue pubData = null;
                    pubData = (TdsValue) tsData.getPub_datapointMap().get(key);
                    if (pubData != null) {
                        pubValues.add(pubData.getValue().doubleValue());
                        pubWeights.add(dp.getWeight());
                    }
                    //NonPublished data
                    TdsValue nonPubData = null;
                    nonPubData = (TdsValue) tsData.getAll_datapointMap().get(key);
                    if (nonPubData != null) {
                        allValues.add(nonPubData.getValue().doubleValue());
                        allWeights.add(dp.getWeight());
                    }
                }
            }//end of dpmembers loop

            if (datapoint.getHolePolicy() == STRICT_POLOCY) {
                if (dpMembers.size() == pubValues.size()) {//published data
                    Double pubResult = calculateDatapoint(method.getModuleName(), pubValues, pubWeights);
                    if (pubResult != null) {
                        TdsValue pubData = new TdsValue();
                        pubData.setDatapointId(datapointId);
                        pubData.setStudyPeriodId(studyPeriodId);
                        pubData.setTargetId(targetId);
                        pubData.setValue(BigDecimal.valueOf(pubResult));
                        tsData.getPub_datapoints().add(pubData);
                        tsData.getPub_datapointMap().put(datapointId, pubData);
                    }
                } else {
                    log.debug("Hole Policy is Strict, Published data only " + pubValues.size() + " in " + dpMembers.size() + "dpMembers have values, igore datapoint " + datapointId);
                }

                if (dpMembers.size() == allValues.size()) {//all data
                    Double allResult = calculateDatapoint(method.getModuleName(), allValues, allWeights);
                    if (allResult != null) {
                        TdsValue allData = new TdsValue();
                        allData.setDatapointId(datapointId);
                        allData.setStudyPeriodId(studyPeriodId);
                        allData.setTargetId(targetId);
                        allData.setValue(BigDecimal.valueOf(allResult));
                        tsData.getAll_datapoints().add(allData);
                        tsData.getAll_datapointMap().put(datapointId, allData);
                    }
                } else {
                    log.debug("Hole Policy is Strict, All data only " + allValues.size() + " in " + dpMembers.size() + "dpMembers have values, igore datapoint " + datapointId);
                }
            } else {
                if (pubValues.size() > 0) {//published data
                    Double pubResult = calculateDatapoint(method.getModuleName(), pubValues, pubWeights);
                    if (pubResult != null) {
                        TdsValue pubData = new TdsValue();
                        pubData.setDatapointId(datapointId);
                        pubData.setStudyPeriodId(studyPeriodId);
                        pubData.setTargetId(targetId);
                        pubData.setValue(BigDecimal.valueOf(pubResult));
                        tsData.getPub_datapoints().add(pubData);
                        tsData.getPub_datapointMap().put(datapointId, pubData);
                    }
                }

                if (allValues.size() > 0) {//all data
                    Double allResult = calculateDatapoint(method.getModuleName(), allValues, allWeights);
                    if (allResult != null) {
                        TdsValue allData = new TdsValue();
                        allData.setDatapointId(datapointId);
                        allData.setStudyPeriodId(studyPeriodId);
                        allData.setTargetId(targetId);
                        allData.setValue(BigDecimal.valueOf(allResult));
                        tsData.getAll_datapoints().add(allData);
                        tsData.getAll_datapointMap().put(datapointId, allData);
                    }
                }
            }
        }
    }

    public void saveOtisValue(Config conf, OtisValue value) {
        String tableName = conf.getComOtisValue().trim();
        if(tableName.equalsIgnoreCase(Constants.TABLE_NAME_OTIS_VALUE_A)){
            OtisValueA a = this.otisToOtisA(value);
            this.otisValueADao.insertOtisValue(a);
        }else if(tableName.equalsIgnoreCase(Constants.TABLE_NAME_OTIS_VALUE_B)){
            OtisValueB b = this.otisToOtisB(value);
            this.otisValueBDao.insertOtisValue(b);
        }else{
            log.error("bad name for config ComOtisValue:" + tableName);
        }
    }

    public void saveTdsValue(Config conf, TdsValue value) {
        String tableName = conf.getComTdsValue().trim();
        if(tableName.equalsIgnoreCase(Constants.TABLE_NAME_TDS_VALUE_A)){
            TdsValueA a = this.tdsToTdsA(value);
            this.tdsValueADao.insertTdsValue(a);
        }else if(tableName.equalsIgnoreCase(Constants.TABLE_NAME_TDS_VALUE_B)){
            TdsValueB b = this.tdsToTdsB(value);
            this.tdsValueBDao.insertTdsValue(b);
        }else{
            log.error("bad name for config ComTdsValue:" + tableName);
        }
        
    }

    private Double calculateDatapoint(String module, List<Double> values, List<Integer> weights) {
        try {
            Class<?> c = Class.forName(module);
            Class<?>[] ptypes = new Class[2];
            ptypes[0] = Class.forName("java.util.List");
            ptypes[1] = Class.forName("java.util.List");
            Method m = c.getDeclaredMethod("calculateWithWeight", ptypes);
            //published data
            Object[] pubArgs = new Object[2];
            pubArgs[0] = values;
            pubArgs[1] = weights;
            Double result = (Double) m.invoke(c.newInstance(), pubArgs);
            return result;
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AggregationService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AggregationService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            java.util.logging.Logger.getLogger(AggregationService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            java.util.logging.Logger.getLogger(AggregationService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            java.util.logging.Logger.getLogger(AggregationService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            java.util.logging.Logger.getLogger(AggregationService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AggregationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Dataset> getDatasetByWorkset(int worksetId) {
        return this.datasetDao.selectDatasetByWorksetId(worksetId);
    }

    public List<WorksetVO> getWorksetVOs(int uid) {
        List<Workset> worksets = getManageWorksetByUser(uid);
        if (worksets == null || worksets.isEmpty()) {
            return null;
        }

        List<WorksetVO> worksetVOs = new ArrayList<WorksetVO>();
        for (Workset w : worksets) {
            Organization org = orgDao.get(w.getOrgId());
            if (null == org) {
                continue;
            }

            WorksetVO v = new WorksetVO();
            v.setId(w.getId());
            v.setName(w.getName());
            v.setOrgId(w.getOrgId());
            v.setVisibility(w.getVisibility());
            v.setOrgName(org.getName());
            v.setIsActive(w.getIsActive());
            //v.setHasDatapoint(datapointDao.hasDatapoints(w.getId()));
            worksetVOs.add(v);
        }
        return worksetVOs;
    }

    public void updateDatasetLastUpdateTime(int datesetId){
        this.datasetDao.updateDatasetLastUpdateTime(datesetId);
    }

    public void updateDatasetLastUpdateTimeByWorksetId(int worksetId){
        this.datasetDao.updateDatasetLastUpdateTimeByWorksetId(worksetId);
    }

    public Config getConfig(){
        return this.configDao.getConfig();
    }

    public void updateConfig(Config conf){
        Config newConf = new Config();
        newConf.setId(conf.getId());
        newConf.setSiteIntro(conf.getSiteIntro());
        newConf.setSiteName(conf.getSiteName());
        newConf.setComOtisValue(conf.getSrvOtisValue());
        newConf.setComScorecard(conf.getSrvScorecard());
        newConf.setComScorecardAnswer(conf.getSrvScorecardAnswer());
        newConf.setComTdsValue(conf.getSrvTdsValue());
        newConf.setSrvOtisValue(conf.getComOtisValue());
        newConf.setSrvScorecard(conf.getComScorecard());
        newConf.setSrvScorecardAnswer(conf.getComScorecardAnswer());
        newConf.setSrvTdsValue(conf.getComTdsValue());
        newConf.setTableSwapTime(Calendar.getInstance().getTime());
        this.configDao.update(newConf);
    }
}
