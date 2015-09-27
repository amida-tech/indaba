/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.po.Workset;
import com.ocs.indaba.aggregation.service.AggregationService;
import com.ocs.indaba.aggregation.vo.AggIndicatorVO;
import com.ocs.indaba.aggregation.vo.AggMethodVO;
import com.ocs.indaba.aggregation.vo.AggregationForm;
import com.ocs.indaba.common.Constants;
import com.ocs.util.StringUtils;
import java.util.ArrayList;
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
 * @author luwb
 */
public class CreateAggregationAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(CreateAggregationAction.class);
    private AggregationService aggregationService = null;
    private static final String PARAM_AGG_NAME = "aggName";
    private static final String PARAM_AGG_SHORTNAME = "aggShortName";
    private static final String PARAM_WORKSET_ID = "worksetId";
    private static final String PARAM_WORKSET_NAME = "worksetName";
    private static final String PARAM_INDICATORLIST = "indicatorlist";
    private static final String PARAM_AGG_NOTES = "aggNotes";
    private static final String PARAM_ACTION = "action";
    private static final String PARAM_AGG_METHOD = "aggMethod";
    private static final String PARAM_AGG_VALIDATE = "aggValidate";
    private static final String PARAM_AGG_WEIGHTS = "aggWeights";
    private static final String EDIT_ACTION = "edit";
    private static final String ATTR_AGG_INDICATORS = "aggIndicators";
    private static final String ATTR_METHODS = "methods";
    private static final String ATTR_METHOD_VO = "aggMethodView";
    private static final String ATTR_METHOD_WEIGHTS = "aggMethodWeights";

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
        int step = StringUtils.str2int(request.getParameter(PARAM_STEP), Constants.INVALID_INT_ID);
        logger.debug("Create aggregation. [Step=" + step + "]");
        String fwd = null;

        switch (step) {
            case 1:
                fwd = handleStepCreate(request);
                break;
            case 2:
                fwd = handleStepLabel(request);
                break;
            case 3:
                fwd = handleStepSelection(request);
                break;
            case 4:
                fwd = handleStepMethod(request);
                break;
            case 5:
                JSONObject obj = new JSONObject();;
                fwd = handleStepSave(request, obj);
                if(fwd == null){
                    response.getWriter().print(obj.toJSONString());
                    return null;
                }else
                    break;
            default:
                AggregationForm aggForm = (AggregationForm) request.getSession().getAttribute(aggregationService.ATTR_AGGREGATION);
                if (aggForm != null) {
                    request.getSession().removeAttribute(aggregationService.ATTR_AGGREGATION);
                }
                fwd = handleStepCreate(request);
                break;
        }
        return mapping.findForward(fwd);
    }

    private String handleStepCreate(HttpServletRequest request) {
        /*List<Workset> worksets = aggregationService.getWorksetByUser(uid);
        if(worksets == null || worksets.size() == 0){
                request.setAttribute(ATTR_ERR_MSG, "no Working Set available");
                return FWD_ERROR;
        }
        request.setAttribute("worksets", worksets);*/
        int worksetId = 0;
        AggregationForm aggForm = (AggregationForm) request.getSession().getAttribute(aggregationService.ATTR_AGGREGATION);
        if(aggForm != null){
            request.setAttribute("showOrigin", true);
            worksetId = aggForm.getWorksetId();
        }else{
            worksetId = StringUtils.str2int(request.getParameter(PARAM_WORKSET_ID), -1);
            if(worksetId <= 0){
                request.setAttribute(ATTR_ERR_MSG, "must specify worksetId");
                return FWD_ERROR;
            }
        }
        
        Workset workset = this.aggregationService.getWorkset(worksetId);
        request.setAttribute("workset", workset);
        return FWD_STEP1;
    }

    private String handleStepLabel(HttpServletRequest request) {
        int worksetId = -1;
        String aggNotes = "";
        String worksetName = "";
        String aggShortName = "";
        String aggName = request.getParameter(this.PARAM_AGG_NAME);
        boolean showOrigin = false;
        List<AggIndicatorVO> selectIndicators = null;
        AggregationForm aggForm = null;
        if(null == aggName){
            aggForm = (AggregationForm) request.getSession().getAttribute(aggregationService.ATTR_AGGREGATION);
            if (null == aggForm) {
                request.setAttribute(ATTR_ERR_MSG, "no session data");
                return FWD_ERROR;
            }else{
                aggName = aggForm.getName();
                aggShortName = aggForm.getShortName();
                aggNotes = aggForm.getDescription();
                worksetName = aggForm.getWorksetName();
                worksetId = aggForm.getWorksetId();
                selectIndicators = aggForm.getAggIndicators();
            }
        }else{
            aggShortName = request.getParameter(this.PARAM_AGG_SHORTNAME);
            if(aggShortName == null){
                request.setAttribute(ATTR_ERR_MSG, "must specify short name");
                return FWD_ERROR;
            }
            aggNotes = request.getParameter(this.PARAM_AGG_NOTES);
            worksetId = StringUtils.str2int(request.getParameter(this.PARAM_WORKSET_ID));
            worksetName = aggregationService.getWorksetName(worksetId);
            logger.debug("Parameters: aggName: " + aggName
                    + ",\n\taggNotes: " + aggNotes
                    +",\n\taggShortName: " + aggShortName
                    + ",\n\tworksetId: " + worksetId
                    + ",\n\tworksetName: " + worksetName);
            aggForm = (AggregationForm) request.getSession().getAttribute(aggregationService.ATTR_AGGREGATION);
            if (null == aggForm) {
                aggForm = new AggregationForm();
                aggForm.setId(0);
                aggForm.setWorksetId(0);
                request.getSession().setAttribute(aggregationService.ATTR_AGGREGATION, aggForm);
            }else{
                if(!aggShortName.equals(aggForm.getShortName()))
                    aggForm.setChanged(true);

                if(!aggName.equals(aggForm.getName()))
                    aggForm.setChanged(true);

                if(worksetId != aggForm.getWorksetId())
                    aggForm.setChanged(true);

                if(aggNotes != null && !aggNotes.equals(aggForm.getDescription()))
                    aggForm.setChanged(true);
                
                selectIndicators = aggForm.getAggIndicators();
            }
            aggForm.setWorksetId(worksetId);
            aggForm.setDescription(aggNotes);
            aggForm.setName(aggName);
            aggForm.setShortName(aggShortName);
            aggForm.setWorksetName(worksetName);
            //aggForm.setAggIndicators(null);//remove previous choice
            //aggForm.setAggMethod(null);//remove previous choice
        }
        if(selectIndicators != null && selectIndicators.size() > 0)
            showOrigin = true;
        
        List<AggIndicatorVO> indicators;
        if(aggForm.getId() == 0)
            indicators = aggregationService.getAggIndicatorsByWorksetId(worksetId);
        else
            indicators = aggregationService.getAggIndicatorsByWorksetAndDatapoint(worksetId, aggForm.getId());
        if(indicators.size() == 0){
            request.setAttribute(ATTR_ERR_MSG, "no indicators or datapoints for this Working Set " + worksetId);
            return FWD_ERROR;
        }
        
        request.setAttribute(PARAM_AGG_NAME, aggName);
        request.setAttribute(PARAM_AGG_SHORTNAME, aggShortName);
        request.setAttribute(PARAM_WORKSET_NAME, worksetName);
        request.setAttribute(ATTR_AGG_INDICATORS, indicators);
        if(showOrigin){
            request.setAttribute("showOrigin", true);
            for(AggIndicatorVO vo : indicators){
                for(AggIndicatorVO selectVo : selectIndicators){
                    if(vo.getId().equals(selectVo.getId())){
                        vo.setChecked(true);
                        break;
                    }
                }
            }
        }
        return FWD_STEP2;
    }

    private String handleStepSelection(HttpServletRequest request) {
        AggregationForm aggForm = (AggregationForm) request.getSession().getAttribute(aggregationService.ATTR_AGGREGATION);
        if (aggForm == null) {
            return handleStepCreate(request);
        }

        List<AggIndicatorVO>  aggIndicators;
        String indicatorIdsStr = request.getParameter(PARAM_INDICATORLIST);
        boolean showOrigin = false;
        if(null == indicatorIdsStr){
            aggIndicators = aggForm.getAggIndicators();
        }else{
            logger.debug("Parameters indicatorList: " + indicatorIdsStr);
            List<Integer> indicatorIds = new ArrayList<Integer>();
            List<Integer> datapointIds = new ArrayList<Integer>();
            if (indicatorIdsStr != null) {
                String[] indicators = indicatorIdsStr.split(",");
                for (String indicator : indicators) {
                    String data = indicator.trim();
                    int index = data.indexOf("-");
                    if(index <= 0)
                        continue;
                    String prefix = data.substring(0, index);
                    String strId = data.substring(index+1);
                    int id = StringUtils.str2int(strId, -1);
                    if(id <= 0)
                        continue;

                    if(prefix.equalsIgnoreCase(AggregationService.DATAPOINT_PREFIX))
                        datapointIds.add(id);
                    else if(prefix.equalsIgnoreCase(AggregationService.INDICATOR_PREFIX))
                        indicatorIds.add(id);
                }
            }
            if(indicatorIds.size() == 0 && datapointIds.size() == 0){
                request.setAttribute(ATTR_ERR_MSG, "no indicators or datapoints selected");
                return FWD_ERROR;
            }
            aggIndicators = aggregationService.getAggIndicatorsByIds(aggForm.getWorksetId(), indicatorIds, datapointIds);
            aggForm.setAggIndicators(aggIndicators);
            aggForm.setChanged(true);
            //aggForm.setAggMethod(null);//remove previous choice
        }
        if(aggForm.getAggMethod() != null){
            showOrigin = true;
            if(aggForm.getAggMethod().getShowWeight() == 1)
                request.setAttribute("showWeights", true);
        }

        if(aggIndicators == null || aggIndicators.size() == 0){
            request.setAttribute(ATTR_ERR_MSG, "no indicators or datapoints selected");
            return FWD_ERROR;
        }

        List<AggMethodVO> aggMethodList = aggregationService.getAllAggrMethod();
        request.setAttribute(PARAM_AGG_NAME, aggForm.getName());
        request.setAttribute(PARAM_AGG_SHORTNAME, aggForm.getShortName());
        request.setAttribute(PARAM_WORKSET_NAME, aggForm.getWorksetName());
        request.setAttribute(ATTR_AGG_INDICATORS, aggIndicators);
        request.setAttribute(ATTR_METHODS, aggMethodList);
        if(showOrigin){
            request.setAttribute("showOrigin", true);
            request.setAttribute("selectMethod", aggForm.getAggMethod().getId());
            request.setAttribute("selectHole", aggForm.getHolePolicy());
        }else{
            request.setAttribute("selectMethod", aggMethodList.get(0).getId());
            request.setAttribute("selectHole", aggregationService.STRICT_POLOCY);
        }
        
        return FWD_STEP3;
    }

    private String handleStepMethod(HttpServletRequest request) {
        AggregationForm aggForm = (AggregationForm) request.getSession().getAttribute(aggregationService.ATTR_AGGREGATION);
        if (aggForm == null) {
            return handleStepCreate(request);
        }

        List<AggIndicatorVO>  aggIndicators = aggForm.getAggIndicators();
        if(null == aggIndicators || aggIndicators.size() == 0){
            return handleStepLabel(request);
        }

        AggMethodVO method = null;
        int holePolicy = 0;
        String aggMethod = request.getParameter(PARAM_AGG_METHOD);
        if(null == aggMethod){
            method = aggForm.getAggMethod();
            holePolicy = aggForm.getHolePolicy();
        }else{
            int methodId = StringUtils.str2int(aggMethod);
            method = aggregationService.getAggrMethodById(methodId);
            holePolicy = StringUtils.str2int(request.getParameter(PARAM_AGG_VALIDATE));
            if(method.getShowWeight() == 1){
                String weights = request.getParameter(PARAM_AGG_WEIGHTS);
                if(null == weights){
                    request.setAttribute(ATTR_ERR_MSG, "method " + method.getName() + " must specify weighting points");
                    return FWD_ERROR;
                }
                String[] strWeights = weights.trim().split(",");
                if(aggIndicators.size() != strWeights.length){
                    logger.error("indicator size " + aggIndicators.size() + "is not equal to weights size " + strWeights.length);
                    request.setAttribute(ATTR_ERR_MSG, "method " + method.getName() + " must specify weighting points");
                    return FWD_ERROR;
                }
                int count = 0;
                for(String weight : strWeights){
                    int num = StringUtils.str2int(weight.trim(), -1);
                    if(num <= 0){
                        logger.error("weight " + weight + " is not valid");
                        continue;
                    }

                    aggIndicators.get(count).setWeight(num);
                    count++;
                }
            }
            aggForm.setAggMethod(method);
            aggForm.setHolePolicy(holePolicy);
            aggForm.setChanged(true);
        }
        request.setAttribute(ATTR_METHOD_VO, method);
        request.setAttribute(PARAM_AGG_NAME, aggForm.getName());
        request.setAttribute(PARAM_AGG_SHORTNAME, aggForm.getShortName());
        request.setAttribute(PARAM_WORKSET_NAME, aggForm.getWorksetName());
        request.setAttribute(ATTR_AGG_INDICATORS, aggIndicators);
        if(aggForm.getId() <= 0 || aggForm.isChanged())
            request.setAttribute("showSaveButton", true);
        return FWD_STEP4;
    }

    private String handleStepSave(HttpServletRequest request, JSONObject obj) {
        AggregationForm aggForm = (AggregationForm) request.getSession().getAttribute(aggregationService.ATTR_AGGREGATION);
        if (aggForm == null) {
            return handleStepCreate(request);
        }

        if(aggForm.getWorksetId() <= 0)
            return handleStepCreate(request);

        if(aggForm.getAggIndicators() == null || aggForm.getAggIndicators().size() == 0)
            return handleStepLabel(request);

        if(aggForm.getAggMethod() == null)
            return handleStepSelection(request);
        
        if(aggregationService.saveAggregation(aggForm)){
            request.getSession().removeAttribute(aggregationService.ATTR_AGGREGATION);
            obj.put("code", 1);
        }
        else{
            obj.put("code", "0");
        }
        return null;
    }

}
