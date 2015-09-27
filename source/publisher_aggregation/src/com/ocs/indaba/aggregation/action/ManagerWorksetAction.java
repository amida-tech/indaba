/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.po.Datapoint;
import com.ocs.indaba.aggregation.po.Workset;
import com.ocs.indaba.aggregation.service.AggregationService;
import com.ocs.indaba.aggregation.service.WorksetService;
import com.ocs.indaba.aggregation.vo.WorksetForm;
import com.ocs.indaba.aggregation.vo.WorksetVO;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.po.Target;
import com.ocs.util.StringUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author luwb
 */
public class ManagerWorksetAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(ManagerWorksetAction.class);
    private WorksetService worksetService = null;
    private AggregationService aggregationService = null;
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_WORKSET_ID = "id";
    private static final String ATTR_ERRMSG = "errMsg";
    private static final String ATTR_WORKSETS = "worksets";
    private static final String ATTR_ORGNAMES = "orgNames";

    private static final String DELETE_TYPE = "delete";
    private static final String EDIT_TYPE = "edit";
    private static final String ENABLE_TYPE = "enable";
    private static final String DISABLE_TYPE = "disable";


    @Autowired
    public void setWorksetService(WorksetService worksetService) {
        this.worksetService = worksetService;
    }

    @Autowired
    public void setAggregationService(AggregationService aggregationService) {
        this.aggregationService = aggregationService;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        ActionForward actionFwd = preprocess(mapping, request);
        if (actionFwd != null) {
            return actionFwd;
        }

        if (!manageWorksetVisible) {
            return noAccess(mapping, request);
        }
        
        String type = request.getParameter(PARAM_TYPE);
        String fwd;
        if(null == type){
            List<WorksetVO> worksets = aggregationService.getWorksetVOs(uid);
            if(worksets == null || worksets.size() == 0){
                //request.setAttribute(ATTR_ERRMSG, "No workset available now!");
                fwd =  FWD_SUCCESS;
            }else{
                //Collections.sort(worksets, new WorksetComprator());
                Set<String> orgNames = new HashSet<String>();
                for(WorksetVO v : worksets){
                    orgNames.add(v.getOrgName());
                }
                request.setAttribute(this.ATTR_WORKSETS, worksets);
                request.setAttribute(ATTR_ORGNAMES, orgNames);
                fwd = FWD_SUCCESS;
            }
            return mapping.findForward(fwd);
        }else{
            String id = request.getParameter(PARAM_WORKSET_ID);
            int worksetId = StringUtils.str2int(id, -1);
            if(worksetId <= 0){
               request.setAttribute(ATTR_ERRMSG, "Must specify workset ID!");
               return mapping.findForward(FWD_ERROR);
            }
            if(type.equals(DELETE_TYPE)){
                JSONObject obj = new JSONObject();
                if(worksetService.deleteWorkset(worksetId))
                    obj.put("code", 1);
                else 
                    obj.put("code", 0);
                response.getWriter().print(obj.toJSONString());
                return null;
            }else if(type.equals(ENABLE_TYPE)){
                JSONObject obj = new JSONObject();
                if(worksetService.isEnabledWorkset(worksetId)){
                    obj.put("code", 2);
                }else{
                    if(worksetService.enableWorkset(worksetId))
                        obj.put("code", 1);
                    else
                        obj.put("code", 0);
                }
                response.getWriter().print(obj.toJSONString());
                return null;
            }else if(type.equals(DISABLE_TYPE)){
                JSONObject obj = new JSONObject();
                if(worksetService.isDisabledWorkset(worksetId)){
                    obj.put("code", 2);
                }else{
                    if(worksetService.disableWorkset(worksetId))
                        obj.put("code", 1);
                    else
                        obj.put("code", 0);
                }
                response.getWriter().print(obj.toJSONString());
                return null;
            }else if(type.equals(EDIT_TYPE)){
                worksetService.disableWorkset(worksetId);
                Workset workset = worksetService.getWorksetByWorksetId(worksetId);
                if(workset == null){
                    request.setAttribute(ATTR_ERRMSG, "can't find workset with id " + worksetId);
                    return mapping.findForward(FWD_ERROR);
                }
                WorksetForm wForm = new WorksetForm();
                wForm.setId(workset.getId());
                wForm.setName(workset.getName());
                wForm.setOrgId(workset.getOrgId());
                wForm.setNotes(workset.getDescription());
                wForm.setUserId(workset.getDefinedByUserId());
                wForm.setVisibility(workset.getVisibility());
                List<Integer> indicatorIds = worksetService.getWSIndicatorIdsByWorksetId(worksetId);
                List<Integer> projectIds = worksetService.getProjectIdsByWorksetId(worksetId);
                List<Integer> targetIds = worksetService.getTargetIdsByWorksetId(worksetId);
                
                wForm.setIndicatorIds(indicatorIds);
                wForm.setProjectIds(projectIds);
                wForm.setTargetIds(targetIds);

                // Project names
                List<String> projectNames = null;
                List<Project> projects = worksetService.getProjectsByProjectIds(projectIds);
                if (projects != null && !projects.isEmpty()) {
                    projectNames = new ArrayList<String>(projects.size());
                    for (Project p : projects) {
                        projectNames.add(p.getCodeName());
                    }
                }
                wForm.setProjectNames(projectNames);

                // Indicator names
                List<String> indicatorNames = null;
                List<SurveyIndicator> indicators = worksetService.getSurveyIndicatorsByIndicatorIds(indicatorIds);
                if (indicators != null && !indicators.isEmpty()) {
                    indicatorNames = new ArrayList<String>(indicators.size());
                    for (SurveyIndicator si : indicators) {
                        indicatorNames.add(si.getName());
                    }
                }
                wForm.setIndicatorNames(indicatorNames);

                // Target names
                List<String> targetNames = null;
                List<Target> targets = worksetService.getTargetsByTargetIds(targetIds);
                if (targets != null && !targets.isEmpty()) {
                    targetNames = new ArrayList<String>(targets.size());
                    for (Target t : targets) {
                        targetNames.add(t.getName());
                    }
                }
                wForm.setTargetNames(targetNames);
                List<Datapoint> datapoints = aggregationService.getDataPointsByWorksetId(worksetId);
                if(datapoints != null && datapoints.size() > 0)
                    wForm.setHasDatapoint(true);
                
                request.getSession().setAttribute(ATTR_WORKSET, wForm);
                response.sendRedirect("createworkset.do?step=4");
            }
        }

        return null;
    }

    class WorksetComprator implements Comparator {
        public int compare(Object arg0, Object arg1) {
            WorksetVO first = (WorksetVO)(arg0);
            WorksetVO second = (WorksetVO)(arg1);
            return first.getName().compareTo(second.getName());
        }
    }
}
