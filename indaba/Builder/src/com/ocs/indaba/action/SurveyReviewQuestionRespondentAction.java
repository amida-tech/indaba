/**
 * 
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.Role;
import com.ocs.indaba.po.Target;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.AssignmentService;
import com.ocs.indaba.service.RoleService;
import com.ocs.indaba.service.TaskService;
import com.ocs.indaba.service.TargetService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.util.JSONUtils;
import java.io.IOException;
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
 * @author Luke
 *
 */
public class SurveyReviewQuestionRespondentAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SurveyReviewQuestionRespondentAction.class);
    private static final String PARAM_USERNAME = "username";
    private static final String ACTION_GETOPTIONS = "getOptions";
    private static final String ACTION_FIND = "findSurveyReviewRespondentUsers";
    private AssignmentService assignmentService = null;
    private RoleService roleSrvc = null;
    private TaskService taskSrvc = null;
    private TargetService targetSrvc = null;

    @Autowired
    public void setTargetService(TargetService targetSrvc) {
        this.targetSrvc = targetSrvc;
    }

    @Autowired
    public void setTaskService(TaskService taskSrvc) {
        this.taskSrvc = taskSrvc;
    }

    @Autowired
    public void setRoleService(RoleService RoleService) {
        this.roleSrvc = RoleService;
    }

    @Autowired
    public void setAssignmentService(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @Override
    public ActionForward execute(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        LoginUser loginUser = preprocess(mapping, request, response, true, false, true);
        String action = request.getParameter(ATTR_ACTION);
        int projId = loginUser.getPrjid();
        if (action.equals(ACTION_GETOPTIONS)) {
            int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
            int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGNID), Constants.INVALID_INT_ID);
            //TaskAssignment ta = taskSrvc.getTaskAssignment(assignId);
            logger.debug("Request Parameters: "
                    + "\n\taction=" + action
                    + "\n\thorseId=" + horseId
                    + "\n\tassignId=" + assignId
                    + "\n\tprojId=" + projId);
            List<Role> roles = roleSrvc.getAllRoles(projId);
            List<Target> targets = targetSrvc.getTargetsByProjectId(projId);
            JSONObject options = new JSONObject();
            options.put("roles", JSONUtils.listObjToJSONArray(roles));
            options.put("targets", JSONUtils.listObjToJSONArray(targets));
            super.writeRespJSON(response, options);
        } else if (action.equals(ACTION_FIND)) {
            int roleId = StringUtils.str2int(request.getParameter(PARAM_ROLE_ID), Constants.INVALID_INT_ID);
            int targetId = StringUtils.str2int(request.getParameter(PARAM_TARGET_ID), Constants.INVALID_INT_ID);
            String username = request.getParameter(PARAM_USERNAME);
            logger.debug("Request Parameters: "
                    + "\n\taction=" + action
                    + "\n\troleId=" + roleId
                    + "\n\ttargetId=" + targetId
                    + "\n\tusername=" + username
                    + "\n\tprojId=" + projId);
            List<User> users = userSrvc.findSurveyReviewRespondentUsers(projId, roleId, targetId, username);
            super.writeRespJSON(response, JSONUtils.listObjToJSONArray(users));
        } else {
        }

        return null;
    }
}
