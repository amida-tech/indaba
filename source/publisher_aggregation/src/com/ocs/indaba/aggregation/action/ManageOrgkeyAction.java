/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.service.OrgkeyService;
import com.ocs.indaba.aggregation.vo.OrgkeyVO;
import com.ocs.indaba.dao.UserDAO;
import com.ocs.indaba.po.Orgkey;
import com.ocs.indaba.po.User;
import java.util.List;
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
public class ManageOrgkeyAction extends BaseAction{
    private static final Logger LOG = Logger.getLogger(ManageOrgkeyAction.class);
    private OrgkeyService orgkeyService = null;
    private UserDAO userDao = null;
    private static final String ATTR_ORGKEYS = "orgkeys";
    private static final String FWD_SUCCESS = "success";
    private static final String PARAM_ORG_ID = "orgId";
    private static final String PARAM_TYPE = "type";
    private static final String TYPE_GENERATE = "generate";

    @Autowired
    public void setOrgkeyService(OrgkeyService orgkeyService) {
        this.orgkeyService = orgkeyService;
    }

    @Autowired
    public void setUserDao(UserDAO userDao){
        this.userDao = userDao;
    }
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionForward actionFwd = preprocess(mapping, request);
        if (actionFwd != null) {
            return actionFwd;
        }
        User user = userDao.get(uid);
        if(user == null || user.getSiteAdmin() != 1){
            request.setAttribute(ATTR_ERR_MSG, "You can't access this page");
            return mapping.findForward(FWD_ERROR);
        }
        String strOrgId = request.getParameter(PARAM_ORG_ID);
        if(strOrgId != null){
            int orgId = NumberUtils.toInt(strOrgId, 0);
            if(orgId > 0){
                String type = request.getParameter(PARAM_TYPE);
                if(TYPE_GENERATE.equalsIgnoreCase(type)){
                    Orgkey orgkey = orgkeyService.generateOrgkey(orgId, uid);
                    JSONObject obj = new JSONObject();
                    if (orgkey == null) {
                        obj.put("code", "0");
                    }else{
                        obj.put("code", "1");
                    }
                    response.getWriter().print(obj.toJSONString());
                    return null;
                }
            }
        }
        List<OrgkeyVO> orgkeys = orgkeyService.getAllOrgkeyView();
        if(orgkeys != null && orgkeys.size() > 0)
            request.setAttribute(ATTR_ORGKEYS, orgkeys);
        return mapping.findForward(FWD_SUCCESS);
    }
}