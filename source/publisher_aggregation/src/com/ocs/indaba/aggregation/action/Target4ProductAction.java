/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.service.TargetService;
import com.ocs.indaba.aggregation.vo.HorseBriefVO;
import com.ocs.util.StringUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Targets for production
 * 
 * @author Jeff
 * 
 */
public class Target4ProductAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(Target4ProductAction.class);
    private static final String ATTR_PRODID = "productId";
    private static final String HORSE_ID = "hid";
    private static final String TARGET_ID = "tid";
    private static final String TARGET_NAME = "tname";
    private static final String TARGET_LIST = "tlist";
    private TargetService trgtService = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // now that we don't leverage struts ActionForm, we need to get the
        // parameters by ourselves
        productId = StringUtils.str2int(request.getParameter(ATTR_PRODID), -1);
        logger.debug("Get targets for product [id=" + productId + "].");
       
        List<HorseBriefVO> horses = trgtService.getCompletedTargetsForProduct(productId);
        //request.setAttribute(ATTR_TARGETS, targets);
        super.writeMsg(response, targets2Json(horses));
        return null;
    }
    
    public String targets2Json(List<HorseBriefVO> horses){
        JSONObject jsonObj = new JSONObject();
        if(!horses.isEmpty()) {
            JSONArray jsonArr = new JSONArray();
            for(HorseBriefVO h: horses) {
                JSONObject tObj = new JSONObject();
                tObj.put(HORSE_ID, h.getHorseId());
                tObj.put(TARGET_ID, h.getTargetId());
                tObj.put(TARGET_NAME, h.getTargetName());
                jsonArr.add(tObj);
            }
            jsonObj.put(TARGET_LIST, jsonArr);
        }
        return jsonObj.toJSONString();
    }
    
    @Autowired
    public void setTargetService(TargetService trgtService) {
        this.trgtService = trgtService;
    }
}
