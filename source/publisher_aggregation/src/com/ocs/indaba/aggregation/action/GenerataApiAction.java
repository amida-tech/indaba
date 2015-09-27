/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.service.ApicallService;
import com.ocs.indaba.aggregation.service.OrgkeyService;
import java.util.Calendar;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author luwenbin
 */
public class GenerataApiAction extends BaseAction{
    private static final Logger LOG = Logger.getLogger(GenerataApiAction.class);
    private ApicallService apicallService = null;
    private OrgkeyService orgkeyService = null;
    public static final String PARAM_REQUEST = "request";
    public static final String PARAM_ORG_ID = "so";
    public static final String PARAM_KEY_VERSION = "sv";
    public static final String PARAM_ORG_KEY = "key";

    @Autowired
    public void setOrgkeyService(OrgkeyService orgkeyService) {
        this.orgkeyService = orgkeyService;
    }

    @Autowired
    public void setApicallService(ApicallService apicallService){
        this.apicallService = apicallService;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        /**
        ActionForward actionFwd = preprocess(mapping, request);
        if (actionFwd != null) {
            JSONObject obj = jsonError("Please Login First");
            response.getWriter().print(obj.toJSONString());
            return null;
        }
        **/
        
        String originRequest = request.getParameter(PARAM_REQUEST);
        if(originRequest == null){
            JSONObject obj = jsonError("Paramtert request is missing");
            response.getWriter().print(obj.toJSONString());
            return null;
        }

        int orgId = NumberUtils.toInt(request.getParameter(PARAM_ORG_ID),0);
        if(orgId <= 0){
            JSONObject obj = jsonError("Parameter so is missing or invalid");
            response.getWriter().print(obj.toJSONString());
            return null;
        }

        int sv = NumberUtils.toInt(request.getParameter(PARAM_KEY_VERSION),0);
        if(sv <= 0){
            JSONObject obj = jsonError("Parameter sv is missing or invalid");
            response.getWriter().print(obj.toJSONString());
            return null;
        }

        String key = request.getParameter(PARAM_ORG_KEY);
        if(key == null){
            JSONObject obj = jsonError("Parameter key is missing");
            response.getWriter().print(obj.toJSONString());
            return null;
        }

        //generate st and sr
        long now = Calendar.getInstance().getTimeInMillis();
        int st = (int)(now / 1000);
        Random random = new Random();
        int sr = Math.abs(random.nextInt());
        
        String newParams = "so=" + orgId + "&st=" + st + "&sr=" + sr + "&sv=" + sv;
        String basicRequest;
        if(originRequest.indexOf("?") == -1)
            basicRequest = originRequest + "?" + newParams;
        else
            basicRequest = originRequest + "&" + newParams;

        String computationStr = basicRequest + "+" + key;
        String digest = orgkeyService.calculateDigest(computationStr);
        if(digest == null){
            JSONObject obj = jsonError("Failed to generat digest for string " + computationStr);
            response.getWriter().print(obj.toJSONString());
            return null;
        }

        String serverRequest = basicRequest + "&sd=" + digest;
        JSONObject obj = new JSONObject();
        obj.put("code", "1");
        obj.put("basicRequest", basicRequest);
        obj.put("serverRequest", serverRequest);
        obj.put("computationStr", computationStr);
        response.getWriter().print(obj.toJSONString());
        return null;
    }

    private JSONObject jsonError(String reason){
        JSONObject obj = new JSONObject();
        obj.put("code", "0");
        obj.put("reason", reason);
        return obj;
    }
}
