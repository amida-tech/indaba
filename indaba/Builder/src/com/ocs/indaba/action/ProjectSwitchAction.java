/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.Project;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.LoginUser;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Login Action. To help handle the user login request. <br/>
 * 
 * Actually, although struts already provides an useful LoginForm component, in
 * this example we don't plan to leverage it. In a general use, we directly get
 * the request parameters fromHttpServletRequest.
 * 
 * @author Jeff
 * 
 */
public class ProjectSwitchAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(ProjectSwitchAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        LoginUser loginUser = preprocess(mapping, request, response);

        //String returl = request.getParameter(ATTR_RET_URL);
        int toPrjId = StringUtils.str2int(request.getParameter(Constants.ATTR_PROJECT_ID), Constants.INVALID_INT_ID);

        logger.debug("Switch to project. [toPrjId=" + toPrjId);

        if (toPrjId < 1) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "prjid"));
            return mapping.findForward(FWD_ERROR);
        }

        if (toPrjId != loginUser.getPrjid()) {
            Project project = prjService.getProjectById(toPrjId);
            if (project == null) {
                request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_DATA_NOTEXISTED, "Project", toPrjId));
                return mapping.findForward(FWD_ERROR);
            }

            userSrvc.updateUserDefaultProjectId(loginUser.getUid(), toPrjId);
            //session.setAttribute(ATTR_PROJECT_ID, toPrjId);
            //session.setAttribute(ATTR_PROJECT_NAME, project.getCodeName());
            //setCookie(response, request.getServerName(), COOKIE_PROJECT_ID, toPrjId);
            //setCookie(response, request.getServerName(), COOKIE_PROJECT_NAME, project.getCodeName());
        }
        //logger.debug("Forward to " + URLDecoder.decode(returl, "UTF-8"));
        //actionFwd = new ActionForward(URLDecoder.decode(returl, "UTF-8"));

        logger.debug("Switching to yourcontent. of project " + toPrjId);
        return mapping.findForward(FWD_YOURCONTENT);
    }
}
