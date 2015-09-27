/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.po.Datapoint;
import com.ocs.indaba.aggregation.service.AggregationService;
import com.ocs.indaba.aggregation.service.WorksetService;
import com.ocs.indaba.aggregation.vo.AggIndicatorVO;
import com.ocs.indaba.aggregation.vo.AggMethodVO;
import com.ocs.indaba.aggregation.vo.AggregationForm;
import com.ocs.util.StringUtils;
import java.util.Collections;
import java.util.Comparator;
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
public class ManagerAggregationAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(ManagerAggregationAction.class);
    private AggregationService aggregationService = null;
    private WorksetService worksetService = null;
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_AGG_ID = "id";
    private static final String PARAM_WORKSET_ID = "worksetId";
    private static final String ATTR_ERRMSG = "errMsg";
    private static final String ATTR_DATAPOINT = "datapoints";
    private static final String ATTR_WORKSET_NAME = "worksetName";

    private static final String DELETE_TYPE = "delete";
    private static final String EDIT_TYPE = "edit";


    @Autowired
    public void setAggregationService(AggregationService aggregationService) {
        this.aggregationService = aggregationService;
    }

    @Autowired
    public void setWorksetService(WorksetService worksetService) {
        this.worksetService = worksetService;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionForward actionFwd = preprocess(mapping, request);
        if (actionFwd != null) {
            return actionFwd;
        }

        String type = request.getParameter(PARAM_TYPE);
        String fwd;
        if(null == type){
            int worksetId = StringUtils.str2int(request.getParameter(PARAM_WORKSET_ID), -1);
            if(worksetId <= 0){
                request.setAttribute(ATTR_ERRMSG, "must specify workset");
                fwd =  FWD_ERROR;
                return mapping.findForward(fwd);
            }
            worksetService.disableWorkset(worksetId);
            String worksetName = aggregationService.getWorksetName(worksetId);
            List<Datapoint> datapoints = aggregationService.getDataPointsByWorksetId(worksetId);
            if(datapoints == null || datapoints.isEmpty()){
                request.setAttribute("ShowDatapoints", false);
            }else{
                Collections.sort(datapoints, new DatapointComprator());
                request.setAttribute(ATTR_DATAPOINT, datapoints);
                request.setAttribute("ShowDatapoints", true);
            }
            request.setAttribute(ATTR_WORKSET_NAME, worksetName);
            fwd = FWD_SUCCESS;
            return mapping.findForward(fwd);
        }else{
            String id = request.getParameter(PARAM_AGG_ID);
            int datapointId = StringUtils.str2int(id, -1);
            if(datapointId <= 0){
               request.setAttribute(ATTR_ERRMSG, "should specify datapint Id");
               return mapping.findForward(FWD_ERROR);
            }
            if(type.equals(DELETE_TYPE)){
                JSONObject obj = new JSONObject();
                if(aggregationService.hasDatapointReferenced(datapointId)){
                    obj.put("code", 2);
                }
                else{
                    if(aggregationService.deleteDatapoint(datapointId))
                        obj.put("code", 1);
                    else
                        obj.put("code", 0);
                }
                response.getWriter().print(obj.toJSONString());
                return null;
            }else if(type.equals(EDIT_TYPE)){
                Datapoint dp = aggregationService.getDatapintById(datapointId);
                if(dp == null){
                    request.setAttribute(ATTR_ERRMSG, "can't find datapoint with id " + datapointId);
                    return mapping.findForward(FWD_ERROR);
                }
                AggregationForm aggForm = new AggregationForm();
                AggMethodVO method = aggregationService.getAggrMethodById(dp.getAggrMethodId());
                aggForm.setAggMethod(method);
                aggForm.setHolePolicy(dp.getHolePolicy());
                aggForm.setDescription(dp.getDescription());
                aggForm.setId(dp.getId());
                aggForm.setName(dp.getName());
                aggForm.setShortName(dp.getShortName());
                aggForm.setWorksetId(dp.getWorksetId());
                String worksetName = aggregationService.getWorksetName(dp.getWorksetId());
                aggForm.setWorksetName(worksetName);

                List<AggIndicatorVO> indicators = aggregationService.getAggIndicatorsByDatapointId(datapointId);
                aggForm.setAggIndicators(indicators);

                request.getSession().setAttribute(AggregationService.ATTR_AGGREGATION, aggForm);
                response.sendRedirect("createaggregation.do?step=4");
            }
        }

        return null;
    }

    class DatapointComprator implements Comparator {
        public int compare(Object arg0, Object arg1) {
            Datapoint first = (Datapoint)(arg0);
            Datapoint second = (Datapoint)(arg1);
            return first.getShortName().compareTo(second.getShortName());
        }
    }
}
