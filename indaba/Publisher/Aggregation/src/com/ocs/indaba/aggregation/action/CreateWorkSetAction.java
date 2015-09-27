/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.service.OrganizationService;
import com.ocs.indaba.aggregation.service.WorksetService;
import com.ocs.indaba.aggregation.vo.IndicatorVO;
import com.ocs.indaba.aggregation.vo.OrganizationVO;
import com.ocs.indaba.aggregation.vo.WorksetForm;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.vo.WsProjectVO;
import com.ocs.indaba.aggregation.vo.WsTargetVO;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.po.Target;
import com.ocs.indaba.service.UserService;
import com.ocs.util.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
 * @author jiangjeff
 */
public class CreateWorkSetAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(CreateWorkSetAction.class);
    private OrganizationService organizationService = null;
    private WorksetService worksetService = null;
    private UserService userService = null;

    private static final String PARAM_ORG_ID = "orgid";
    private static final String PARAM_WORKSETNAME = "worksetName";
    private static final String PARAM_WORKSETNOTES = "worksetNotes";
    private static final String PARAM_VISIBILITY = "visibility";
    private static final String PARAM_PROJECTLIST = "projectlist";
    private static final String PARAM_INDICATORLIST = "indicatorlist";
    private static final String PARAM_TARGETLIST = "targetlist";
    private static final String ATTR_WORKSET_ID = "worksetId";
    private static final int PUBLIC_VISIBILITY = 1;
    private static final int PRIVATE_VISIBILITY = 2;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionForward actionFwd = preprocess(mapping, request);
        if(actionFwd != null) {
            return actionFwd;
        }
        
        if (!manageWorksetVisible) {
            return noAccess(mapping, request);
        }

        int step = StringUtils.str2int(request.getParameter(PARAM_STEP), Constants.INVALID_INT_ID);
        logger.debug("Create Workset. [Step=" + step + "]");
        String fwd = null;
        switch (step) {
            case 1:
                fwd = handleStep1(request);
                break;
            case 2:
                fwd = handleStep2(request);
                break;
            case 3:
                fwd = handleStep3(request);
                break;
            case 4:
                fwd = handleStep4(request);
                break;
            case 5:
                JSONObject obj = new JSONObject();;
                fwd = handleStep5(request,obj);
                if(fwd == null){
                    response.getWriter().print(obj.toJSONString());
                    return null;
                }else
                    break;
            default:
                WorksetForm worksetForm = (WorksetForm) request.getSession().getAttribute(ATTR_WORKSET);
                if (worksetForm != null) {
                    request.getSession().removeAttribute(ATTR_WORKSET);
                }
                fwd = handleStep1(request);
                break;
        }
        return mapping.findForward(fwd);
    }

    private String handleStep1(HttpServletRequest request) {
        List<OrganizationVO> orgs = organizationService.getOrganizationsByUser(uid);
        if(orgs == null || orgs.size() == 0){
            request.setAttribute(ATTR_ERR_MSG, "no orgnizations for user " + uid);
            return FWD_ERROR;
        }
        WorksetForm worksetForm = (WorksetForm) request.getSession().getAttribute(ATTR_WORKSET);
        List<WsProjectVO> wsProjects = null;
        if(worksetForm != null){//have previous choosen
            List<Integer> projects = worksetForm.getProjectIds();
            int orgId = worksetForm.getOrgId();
            wsProjects = this.organizationService.getProjectsByOrgIdAndVisibility(orgId, worksetForm.getVisibility());
            for(OrganizationVO vo : orgs){
                if(vo.getId() == orgId){
                    vo.setChecked(true);
                    break;
                }
            }
            for(WsProjectVO project : wsProjects){
                for(int pid : projects){
                    if(project.getProjectId() == pid){
                        project.setChecked(true);
                        break;
                    }
                }
            }
            request.setAttribute("showOrigin", true);
            request.setAttribute("selectOrgId", orgId);
            request.setAttribute("visibility", worksetForm.getVisibility());
        }else{
            orgs.get(0).setChecked(true);
            int orgId = orgs.get(0).getId();
            wsProjects = this.organizationService.getProjectsByOrgIdAndVisibility(orgId, Constants.PUBLIC_WORKSET);
            request.setAttribute("visibility", this.PUBLIC_VISIBILITY);
        }

        request.setAttribute("wsProjects", wsProjects);
        request.setAttribute(ATTR_ORGANIZATIONS, orgs);
        return FWD_STEP1;
    }

    private String handleStep2(HttpServletRequest request) {
         WorksetForm worksetForm = (WorksetForm) request.getSession().getAttribute(ATTR_WORKSET);
         if (worksetForm == null) {
            worksetForm = new WorksetForm();
            worksetForm.setId(0);
            worksetForm.setChanged(true);
            request.getSession().setAttribute(ATTR_WORKSET, worksetForm);
        }

        String workingsetName = request.getParameter(PARAM_WORKSETNAME);
        List<Integer> projectIds;
        List<Integer> selectedIndicatorIds = null;
        boolean showOrigin = false;
        if(workingsetName != null){
            if(!workingsetName.equals(worksetForm.getName())){
                worksetForm.setChanged(true);
                if(worksetService.existWorksetName(workingsetName)){
                    request.setAttribute(ATTR_ERR_MSG, "workset name exist, please choose another one");
                    return FWD_ERROR;
                }
            }

            String workingsetNotes = request.getParameter(PARAM_WORKSETNOTES);
            if(workingsetNotes != null && !workingsetNotes.equals(worksetForm.getNotes()))
                worksetForm.setChanged(true);
            
            String projectListStr = request.getParameter(PARAM_PROJECTLIST);
            int visibility = StringUtils.str2int(request.getParameter(PARAM_VISIBILITY), Constants.WORKSET_VISIBILITY_PUBLIC);
            if(visibility != worksetForm.getVisibility())
                worksetForm.setChanged(true);
            
            int orgId = StringUtils.str2int(request.getParameter(PARAM_ORG_ID));
            if(orgId != worksetForm.getOrgId())
                worksetForm.setChanged(true);
            
            logger.debug("Parameters: worksetName: " + workingsetName
                    + ",\n\tworksetNotes: " + workingsetNotes
                    + ",\n\tvisibility: " + visibility
                    + ",\n\torgid: " + orgId
                    + ",\n\tprojectList: " + projectListStr);
            
            projectIds = new ArrayList<Integer>();
            if (projectListStr != null) {
                String[] projects = projectListStr.split(",");
                for (String proj : projects) {
                    try {
                        projectIds.add(Integer.parseInt(proj.trim()));
                    } catch (Exception ex) {
                        logger.error("Invalid project id: " + proj, ex);
                    }
                }
            }
            if(projectIds.size() == 0){
                request.setAttribute(ATTR_ERR_MSG, "must specify project selection");
                return FWD_ERROR;
            }

            if(!isSame(projectIds, worksetForm.getProjectIds()))
                worksetForm.setChanged(true);

            worksetForm.setOrgId(orgId);
            worksetForm.setName(workingsetName);
            worksetForm.setNotes(workingsetNotes);
            worksetForm.setVisibility((short) visibility);
            worksetForm.setProjectIds(projectIds);
            //worksetForm.setIndicatorIds(null);//if have previous choice, remove it
            //worksetForm.setTargetIds(null);//if have previous choice, remove it

            logger.debug(worksetForm);
        }else{//not from submit page
            projectIds = worksetForm.getProjectIds();
        }
        
        selectedIndicatorIds = worksetForm.getIndicatorIds();
        if(selectedIndicatorIds != null && selectedIndicatorIds.size() > 0)
                showOrigin = true;

        List<IndicatorVO> indicators = worksetService.getSurveyIndicatorsByProjectIds(projectIds);
        if(indicators == null || indicators.size() == 0){
            request.setAttribute(ATTR_ERR_MSG, "no indicators for selection");
            return FWD_ERROR;
        }
        if(showOrigin){
            for(IndicatorVO v : indicators){
                for(Integer indicatorId : selectedIndicatorIds){
                    if(v.getId() == indicatorId){
                        v.setChecked(true);
                        break;
                    }
                }
            }
        }
        request.setAttribute(ATTR_INDICATORS, indicators);
        return FWD_STEP2;
    }

    private String handleStep3(HttpServletRequest request) {
        WorksetForm worksetForm = (WorksetForm) request.getSession().getAttribute(ATTR_WORKSET);
        if (worksetForm == null) {
            return handleStep1(request);
        }

        String indicatorIdsStr = request.getParameter(PARAM_INDICATORLIST);
        logger.debug("Parameters indicatorList: " + indicatorIdsStr);

        List<Integer> indicatorIds;
        boolean showOrigin = false;
        if(indicatorIdsStr != null){
            indicatorIds = new ArrayList<Integer>();
            String[] indicators = indicatorIdsStr.split(",");
            for (String indicator : indicators) {
                try {
                    indicatorIds.add(Integer.parseInt(indicator.trim()));
                } catch (Exception ex) {
                    logger.error("Invalid indicator id: " + indicator, ex);
                }
            }
            if(indicatorIds.size() == 0){
                request.setAttribute(ATTR_ERR_MSG, "must specify indicator selection");
                return FWD_ERROR;
            }
            if(!isSame(indicatorIds, worksetForm.getIndicatorIds()))
                worksetForm.setChanged(true);
            worksetForm.setIndicatorIds(indicatorIds);
        }else{
            indicatorIds = worksetForm.getIndicatorIds();
        }
        List<Integer> selectedTargetIds = worksetForm.getTargetIds();
        if(selectedTargetIds != null && selectedTargetIds.size() > 0)
            showOrigin = true;
        
        List<WsTargetVO> targets = worksetService.getTargetsByProjectIds(worksetForm.getProjectIds());
        if(showOrigin){
            List<Boolean> isSelectedTarget = new ArrayList<Boolean>();
            for(WsTargetVO t : targets){
                for(int targetId : selectedTargetIds){
                    if(t.getTargetId() == targetId){
                        t.setChecked(true);
                        break;
                    }
                }
            }
            request.setAttribute("isSelectedTarget", isSelectedTarget);
        }
        request.setAttribute(ATTR_TARGETS, targets);
        for (WsTargetVO target : targets) {
            logger.debug("id: " + target.getTargetId() + ", name: " + target.getTargetName() + ", target_type: " + target.getTargetType());
        }
        return FWD_STEP3;
    }

    private String handleStep4(HttpServletRequest request) {
        WorksetForm worksetForm = (WorksetForm) request.getSession().getAttribute(ATTR_WORKSET);
        if (worksetForm == null) {
            return handleStep1(request);
        }


        List<Integer> targetIds = null;
        String targetListStr = request.getParameter(PARAM_TARGETLIST);
        logger.debug("Parameters targetList: " + targetListStr);

        if (targetListStr != null) {
            targetIds = new ArrayList<Integer>();
            String[] indicators = targetListStr.split(",");
            for (String indicator : indicators) {
                try {
                    targetIds.add(Integer.parseInt(indicator.trim()));
                } catch (Exception ex) {
                    logger.error("Invalid indicator id: " + indicator, ex);
                }
            }
            if(targetIds.size() == 0){
                request.setAttribute(ATTR_ERR_MSG, "must specify target selection");
                return FWD_ERROR;
            }
            if(!isSame(targetIds, worksetForm.getTargetIds()))
                worksetForm.setChanged(true);
            worksetForm.setTargetIds(targetIds);
        }else
            targetIds = worksetForm.getTargetIds();
        

        // Project names
        List<String> projectNames = null;
        List<Project> projects = worksetService.getProjectsByProjectIds(worksetForm.getProjectIds());
        if (projects != null && !projects.isEmpty()) {
            projectNames = new ArrayList<String>(projects.size());
            for (Project p : projects) {
                projectNames.add(p.getCodeName());
            }
        }
        worksetForm.setProjectNames(projectNames);

        // Indicator names
        List<String> indicatorNames = null;
        List<SurveyIndicator> indicators = worksetService.getSurveyIndicatorsByIndicatorIds(worksetForm.getIndicatorIds());
        if (indicators != null && !indicators.isEmpty()) {
            indicatorNames = new ArrayList<String>(indicators.size());
            for (SurveyIndicator si : indicators) {
                indicatorNames.add(si.getName());
            }
        }
        worksetForm.setIndicatorNames(indicatorNames);

        // Target names
        List<String> targetNames = null;
        List<Target> targets = worksetService.getTargetsByTargetIds(worksetForm.getTargetIds());
        if (targets != null && !targets.isEmpty()) {
            targetNames = new ArrayList<String>(targets.size());
            for (Target t : targets) {
                targetNames.add(t.getName());
            }
        }
        worksetForm.setTargetNames(targetNames);
        if(worksetForm.isHasDatapoint())
            request.setAttribute("showManageDatapint", true);
        
        request.setAttribute(ATTR_WORKSET_ID, worksetForm.getId());
        if(worksetForm.isChanged() || worksetForm.getId() <=0 )
            request.setAttribute("showSave", true);
        request.setAttribute(ATTR_WORKSET, worksetForm);
        return FWD_STEP4;
    }

    private String handleStep5(HttpServletRequest request, JSONObject obj) {
        WorksetForm worksetForm = (WorksetForm) request.getSession().getAttribute(ATTR_WORKSET);
        if (worksetForm == null) {
            return handleStep1(request);
        }

        if(worksetForm.getProjectIds() == null || worksetForm.getProjectIds().size() == 0)
            return handleStep1(request);

        if(worksetForm.getIndicatorIds() == null || worksetForm.getIndicatorIds().size() == 0)
            return handleStep2(request);

        if(worksetForm.getTargetIds() == null || worksetForm.getTargetIds().size() == 0)
            return handleStep3(request);
        
        worksetForm.setUserId((Integer) request.getSession().getAttribute(ATTR_USERID));
        int worksetId = worksetService.addWorkset(worksetForm);
        if(worksetId <= 0){
            obj.put("code", 0);
        }else{
            obj.put("code", 1);
            obj.put("worksetId", worksetId);
            request.getSession().removeAttribute(ATTR_WORKSET);
        }
        return null;
    }

    private boolean isSame(List<Integer> list1, List<Integer> list2){
        if(list1 == null || list2 == null)
            return false;

        if(list1.size() != list2.size())
            return false;

        Collections.sort(list1);
        Collections.sort(list2);
        boolean same = true;
        for(int i=0; i<list1.size(); i++){
            if(list1.get(i).intValue() != list2.get(i).intValue()){
                same = false;
                break;
            }
        }
        return same;
    }

    @Autowired
    public void setOrganizationService(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @Autowired
    public void setWorksetService(WorksetService worksetService) {
        this.worksetService = worksetService;
    }

    @Autowired
    public void setUserService(UserService userSrvc) {
        this.userService = userSrvc;
    }
}
