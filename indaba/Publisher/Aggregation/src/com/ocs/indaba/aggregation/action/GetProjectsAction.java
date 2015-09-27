/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.service.OrganizationService;
import com.ocs.indaba.aggregation.vo.WsProjectVO;
import com.ocs.util.StringUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author luwb
 */
public class GetProjectsAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(GetProjectsAction.class);
    private OrganizationService organizationService = null;
    private static final String PARAM_ORG_ID = "orgId";
    private static final String PARAM_VISIBILITY = "visibility";

    @Autowired
    public void setOrganizationService(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionForward actionFwd = preprocess(mapping, request);
        if (actionFwd != null) {
            return actionFwd;
        }

        int orgId = StringUtils.str2int(request.getParameter(this.PARAM_ORG_ID), -1);
        if(orgId <= 0)
            return null;

        int visibility = StringUtils.str2int(request.getParameter(PARAM_VISIBILITY), Constants.PUBLIC_WORKSET);
        
        List<WsProjectVO> projects = this.organizationService.getProjectsByOrgIdAndVisibility(orgId, visibility);
        String content = "";
        if(projects != null && projects.size() > 0){
            for(WsProjectVO v : projects){
                String option = "<option id='proj-" +  v.getProjectId() + "' value='" + v.getProjectId() + "'>"
                                + v.getProjcetName() + "</option>";
                content += option;
            }
        }
        response.getWriter().print(content);

        return null;
    }

}
