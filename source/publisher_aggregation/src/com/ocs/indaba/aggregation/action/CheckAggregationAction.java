/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.service.AggregationService;
import com.ocs.indaba.aggregation.vo.AggregationForm;
import com.ocs.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.simple.JSONObject;

/**
 *
 * @author luwb
 */
public class CheckAggregationAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(CheckAggregationAction.class);
    private AggregationService aggregationService = null;
    private static final String ATTR_ERRMSG = "errMsg";
    private static final String PARAM_AGG_NAME = "name";
    private static final String PARAM_AGG_TYPE = "type";
    private static final String PARAM_WORKSET_ID = "worksetId";

    @Autowired
    public void setAggregationService(AggregationService aggregationService) {
        this.aggregationService = aggregationService;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionForward actionFwd = preprocess(mapping, request);
        JSONObject obj = new JSONObject();
        if (actionFwd != null) {
            obj.put("code", "0");
            response.getWriter().print(obj.toJSONString());
            return null;
        }

        String name = request.getParameter(this.PARAM_AGG_NAME);
        String type = request.getParameter(this.PARAM_AGG_TYPE);
        int worksetId = StringUtils.str2int(request.getParameter(this.PARAM_WORKSET_ID), -1);
        if(worksetId <= 0){
            obj.put("code", "0");
            response.getWriter().print(obj.toJSONString());
            return null;
        }
        AggregationForm aggForm = (AggregationForm) request.getSession().getAttribute(aggregationService.ATTR_AGGREGATION);
        if(type.equalsIgnoreCase("name")){
            if(aggForm != null){
                if(name.equals(aggForm.getName())){
                    obj.put("code", "1");
                    response.getWriter().print(obj.toJSONString());
                    return null;
                }
            }
            if(aggregationService.isAggNameExist(worksetId, name))
                obj.put("code", "0");
            else
                obj.put("code", "1");
        }else if(type.equalsIgnoreCase("shortname")){
            if(aggForm != null){
                if(name.equals(aggForm.getShortName())){
                    obj.put("code", "1");
                    response.getWriter().print(obj.toJSONString());
                    return null;
                }
            }
            if(aggregationService.isAggShortNameExist(worksetId, name))
                obj.put("code", "0");
            else
                obj.put("code", "1");
        }else
            obj.put("code", "0");

        response.getWriter().print(obj.toJSONString());
        return null;
    }
}
