/**
 * 
 */
package com.ocs.indaba.action.popup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.indaba.action.BaseAction;
import com.ocs.indaba.action.MessagesAction;
import com.ocs.indaba.service.TeamService;
import com.ocs.indaba.vo.LoginUser;

/**
 * @author Tiger Tang
 *
 */
public class TeamListAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(MessagesAction.class);
    /*
    private TeamService teamService;
    
    @Autowired
    public void setTeamService(TeamService teamService) {
    this.teamService = teamService;
    }
     */

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LoginUser loginUser = preprocess(mapping, request, response);

        request.setAttribute("varName", request.getParameter("varName"));
        request.setAttribute("teamList", teamService.getTeamsByProjectId(loginUser.getPrjid(), loginUser.getUid()));

        return mapping.findForward(FWD_SUCCESS);
    }
}
