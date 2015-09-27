/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.service;

import com.ocs.indaba.aggregation.dao.DatapointDAO;
import com.ocs.indaba.aggregation.dao.DatasetDAO;
import com.ocs.indaba.aggregation.dao.DpMemberDAO;
import com.ocs.indaba.aggregation.dao.WorksetDAO;
import com.ocs.indaba.aggregation.dao.WorksetIndicatorInstanceDAO;
import com.ocs.indaba.aggregation.dao.WorksetProjectDAO;
import com.ocs.indaba.aggregation.dao.WorksetTargetDAO;
import com.ocs.indaba.aggregation.po.Datapoint;
import com.ocs.indaba.aggregation.po.Dataset;
import com.ocs.indaba.aggregation.po.DpMember;
import com.ocs.indaba.aggregation.po.Workset;
import com.ocs.indaba.aggregation.po.WsIndicatorInstance;
import com.ocs.indaba.aggregation.po.WsProject;
import com.ocs.indaba.aggregation.po.WsTarget;
import com.ocs.indaba.aggregation.vo.IndicatorVO;
import com.ocs.indaba.aggregation.vo.WorksetForm;
import com.ocs.indaba.aggregation.vo.WsTargetVO;
import com.ocs.indaba.builder.dao.ProjectDAO;
import com.ocs.indaba.builder.dao.ScorecardQuestionDAO;
import com.ocs.indaba.builder.dao.StudyPeriodDAO;
import com.ocs.indaba.dao.TargetDAO;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.StudyPeriod;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.po.Target;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jiangjeff
 */
public class WorksetService {

    private static final Logger log = Logger.getLogger(WorksetService.class);
    private ScorecardQuestionDAO scorecardQuestionDao = null;
    private ProjectDAO projectDao = null;
    private TargetDAO targetDao = null;
    private WorksetDAO worksetDao = null;
    private WorksetProjectDAO worksetProjectDao = null;
    private WorksetTargetDAO worksetTargetDao = null;
    private WorksetIndicatorInstanceDAO worksetIndicatorInstanceDao = null;
    private StudyPeriodDAO studyPeriodDao = null;
    private DatasetDAO datasetDao = null;
    private DatapointDAO datapointDao = null;
    private AggregationService aggregationService = null;
    private IndicatorService indicatorService = null;
    private TargetService targetService = null;
    private DpMemberDAO dpMemberDao;
    private static String[] targetTypes = {"Country", "International Region", "Sub-national: Province", "Sub-national: State", "Sub-national: Region", "Sub-national: City/Municipality", "Organization", "Government Unit/Project", "Sector"};


    public int addWorkset(WorksetForm worksetForm) {
        Workset workset = null;

        boolean update = false;
        if(worksetForm.getId() > 0) {
            update = true;
            workset = worksetDao.get(worksetForm.getId());

            if (workset == null) {
                // recreate a new workset
                update = false;
            }
        }

        Date now = new Date();
        if (!update) {
            workset = new Workset();
        }

        workset.setName(worksetForm.getName());
        workset.setDescription(worksetForm.getNotes());
        workset.setVisibility(worksetForm.getVisibility());
        if(!update){
            workset.setDefinedByUserId(worksetForm.getUserId());
            workset.setDefinedTime(now);
        }
        
        workset.setLastUpdateTime(now);
        workset.setOrgId(worksetForm.getOrgId());
        workset.setIsActive(false);
        if(update){            
            worksetDao.update(workset);
        } else {
            workset = worksetDao.create(workset);
            if (workset == null) return -1;
        }

        //delete indicators/targets/projects
        if (update) {
            worksetProjectDao.deleteProjectsByWorksetId(workset.getId());
            worksetTargetDao.deleteWsTargetsByWorksetId(workset.getId());
            worksetIndicatorInstanceDao.deleteIndicatorsByWorksetId(workset.getId());
            //datasetDao.deleteDatasetByWorksetId(worksetId);
        }

        //create projects
        WsProject wsProject = new WsProject();
        wsProject.setWorksetId(workset.getId());
        List<Integer> projectIds = worksetForm.getProjectIds();
        if (projectIds != null) {
            for (Integer projId : projectIds) {
                wsProject.setProjectId(projId);
                wsProject.setId(null);  // must set to null to auto create ID - YC
                worksetProjectDao.create(wsProject);
            }
        }

        //create targets
        WsTarget wsTarget = new WsTarget();
        wsTarget.setWorksetId(workset.getId());
        List<Integer> targetIds = worksetForm.getTargetIds();
        if (targetIds != null) {
            for (Integer targetId : targetIds) {
                wsTarget.setTargetId(targetId);
                wsTarget.setId(null);           // must set to null to auto create ID - YC
                worksetTargetDao.create(wsTarget);
            }
        }

        //create indicators
        WsIndicatorInstance wsIndicator = new WsIndicatorInstance();
        wsIndicator.setWorksetId(workset.getId());
        wsIndicator.setOrgId(worksetForm.getOrgId());
        List<Integer> indicatorIds = worksetForm.getIndicatorIds();
        if (indicatorIds != null) {
            for (Integer indicatorId : indicatorIds) {
                wsIndicator.setIndicatorId(indicatorId);
                wsIndicator.setId(null);  // must set to null to auto create ID - YC
                worksetIndicatorInstanceDao.create(wsIndicator);
            }
        }

        //create dataset
        if(!update){
            Dataset dataset = new Dataset();
            dataset.setWorksetId(workset.getId());
            dataset.setLastUpdateTime(now);
            dataset.setIncludesNonpubData(true);
            datasetDao.create(dataset);
            dataset.setIncludesNonpubData(false);

            dataset.setId(null); // must set to null to auto create ID - YC
            datasetDao.create(dataset);
        }
        
        return workset.getId();
    }
    

    public List<Workset> getWorksetByUsername(String username) {
        return worksetDao.selectWorksetByUsername(username);
    }

    public Workset getWorksetByWorksetName(String worksetName) {
        return worksetDao.selectWorksetByWorksetName(worksetName);
    }

    public Workset getWorksetByWorksetId(int worksetId) {
        return worksetDao.get(worksetId);
    }

    public List<IndicatorVO> getSurveyIndicatorsByProjectIds(List<Integer> projectIds) {
        List<IndicatorVO> indicators =  scorecardQuestionDao.selectSurveyIndicatorsByProjectIds(projectIds);
        if(indicators == null || indicators.isEmpty())
            return null;

        for(IndicatorVO v : indicators){
            String tagName = indicatorService.getItagsByIndicatorId(v.getId());
            v.setTag(tagName);
        }
        return indicators;
    }

    public List<SurveyIndicator> getSurveyIndicatorsByIndicatorIds(List<Integer> indicatorIds) {
        return scorecardQuestionDao.selectSurveyIndicatorsByIndicatorIds(indicatorIds);
    }

    public List<WsTargetVO> getTargetsByProjectIds(List<Integer> projectIds) {
        List<Target> targets = projectDao.selectTargetsByProjectIds(projectIds);
        if(targets == null || targets.isEmpty())
            return null;

        List<WsTargetVO> wsTargets = new ArrayList<WsTargetVO>(targets.size());
        for(Target t : targets){
            WsTargetVO wst = new WsTargetVO();
            wst.setTargetId(t.getId());
            wst.setTargetName(t.getName());
            short type = t.getTargetType();
            wst.setTargetType(type);
            if(type < targetTypes.length)
                wst.setTypeName(targetTypes[type]);
            String tag = targetService.getTargetTagsByTargetId(t.getId());
            wst.setTag(tag);
            wsTargets.add(wst);
        }
        return wsTargets;
    }

    public List<Target> getTargetsByTargetIds(List<Integer> targetIds) {
        List<Target> targets = targetDao.selectTargetsByTargetIds(targetIds);
        return targets;
    }

    public List<Project> getProjectsByProjectIds(List<Integer> projectIds) {
        return projectDao.selectProjectsByProjectIds(projectIds);
    }

    public List<StudyPeriod> getStudyPeriodByWorksetId(int worksetId) {
        return studyPeriodDao.selectStudyPeriodsByWorksetId(worksetId);
    }

    public List<Integer> getProjectIdsByWorksetId(int worksetId) {
        return worksetProjectDao.selectProjectIdsByWorksetId(worksetId);
    }

    public List<Integer> getTargetIdsByWorksetId(int worksetId) {
        return this.worksetTargetDao.selectWsTargetsIdsByWorksetId(worksetId);
    }

    public List<Integer> getWSIndicatorIdsByWorksetId(int worksetId) {
        return this.worksetIndicatorInstanceDao.selectIndicatorIdsByWorksetId(worksetId);
    }

    public List<Workset> getPublicWorksets() {
        return worksetDao.selectPublicWorksets();
    }

    public List<Datapoint> getDatapointByWorksetId(int worksetId) {
        return datapointDao.selectDataPointByWorksetId(worksetId);
    }

    public String[] getDatapointNames(List<Datapoint> datapointList) {
        TreeSet<String> ts = new TreeSet<String>();
        for (Datapoint dp : datapointList) {
            ts.add(dp.getName());
        }
        return ts.toArray(new String[]{});
    }

    public String[] getDatapointDes(List<Datapoint> datapointList){
        TreeSet<String> ts = new TreeSet<String>();
        for (Datapoint dp : datapointList) {
            ts.add(dp.getDescription());
        }
        return ts.toArray(new String[]{});
    }

    public boolean deleteWorkset(int worksetId){
        this.datasetDao.deleteDatasetByWorksetId(worksetId);
        this.worksetProjectDao.deleteProjectsByWorksetId(worksetId);
        this.worksetTargetDao.deleteWsTargetsByWorksetId(worksetId);
        this.worksetIndicatorInstanceDao.deleteIndicatorsByWorksetId(worksetId);
        List<Integer> dps = this.datapointDao.selectDatapointIdsByWorksetId(worksetId);
        for(int dpId : dps ){
            this.aggregationService.deleteDatapoint(dpId);
        }
        this.worksetDao.delete(worksetId);
        if(worksetDao.exists(worksetId))
            return false;
        else
            return true;
    }

    public boolean existWorksetName(String worksetName){
        Workset workset = this.worksetDao.selectWorksetByWorksetName(worksetName);
        if(workset != null)
            return true;
        else
            return false;
    }

    public boolean enableWorkset(int worksetId){
        /*check whether indicators has been deleted*/
        List<Integer> indicators = this.worksetIndicatorInstanceDao.selectIndicatorIdsByWorksetId(worksetId);
        Set<Integer> indicatorSet = new HashSet<Integer>(indicators);
        List<Datapoint> datapoints = this.datapointDao.selectDataPointByWorksetId(worksetId);
        Set<Integer> expiredDatapoints  = new HashSet<Integer>();
        for(Datapoint datapoint : datapoints){
            int datapointId = datapoint.getId();
            boolean hasDeleted = false;
            List<DpMember> dpMembers = this.dpMemberDao.selectDpMemberByDatapointId(datapointId);
            for(DpMember dpMember : dpMembers){
                if(dpMember.getIndicatorInstanceId() > 0){//indicator type
                    if(!indicatorSet.contains(dpMember.getIndicatorInstanceId())){
                        hasDeleted = true;
                        break;
                    }
                }else{//datapoint type
                    if(expiredDatapoints.contains(dpMember.getDpId())){
                        hasDeleted = true;
                        break;
                    }
                }
            }
            if(hasDeleted){
                log.info("datapoint " + datapointId + " of workset " + worksetId + " has been expired, should be deleted");
                expiredDatapoints.add(datapointId);
            }
        }
        if(expiredDatapoints.size() > 0){
            for(Integer dpId : expiredDatapoints){
                this.aggregationService.deleteDatapoint(dpId);
            }
        }
        this.worksetDao.enableWorkset(worksetId);
        return true;
    }

    public boolean disableWorkset(int worksetId){
        this.worksetDao.disableWorkset(worksetId);
        return true;
    }

    public boolean isEnabledWorkset(int worksetId){
        Workset workset = this.worksetDao.get(worksetId);
        return workset.getIsActive();
    }

    public boolean isDisabledWorkset(int worksetId){
        Workset workset = this.worksetDao.get(worksetId);
        return workset.getIsActive() == false;
    }

    @Autowired
    public void setDatapointDao(DatapointDAO datapointDao) {
        this.datapointDao = datapointDao;
    }

    @Autowired
    public void setStudyPeriodDao(StudyPeriodDAO studyPeriodDao) {
        this.studyPeriodDao = studyPeriodDao;
    }

    @Autowired
    public void setScorecardQuestionDao(ScorecardQuestionDAO indicatorDao) {
        this.scorecardQuestionDao = indicatorDao;
    }

    @Autowired
    public void setProjectDAO(ProjectDAO projectDao) {
        this.projectDao = projectDao;
    }

    @Autowired
    public void setTargetDAO(TargetDAO targetDao) {
        this.targetDao = targetDao;
    }

    @Autowired
    public void setTargetDAO(WorksetDAO worksetDao) {
        this.worksetDao = worksetDao;
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
    public void setWorksetIndicatorInstanceDAO(WorksetIndicatorInstanceDAO worksetIndicatorInstanceDao) {
        this.worksetIndicatorInstanceDao = worksetIndicatorInstanceDao;
    }

    @Autowired
    public void setDatasetDAO(DatasetDAO datasetDao) {
        this.datasetDao = datasetDao;
    }

    @Autowired
    public void setAggregationService(AggregationService aggregationService) {
        this.aggregationService = aggregationService;
    }

    @Autowired
    public void setTargetService(TargetService targetService) {
        this.targetService = targetService;
    }


    @Autowired
    public void setIndicatorService(IndicatorService indicatorService) {
        this.indicatorService = indicatorService;
    }

    @Autowired
    public void setDpMemberDAO(DpMemberDAO dpMemberDao) {
        this.dpMemberDao = dpMemberDao;
    }
}
