/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.service.CommPanelService;
import com.ocs.indaba.vo.FlagWorkView;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.util.StringUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author yc06x
 */
public class SurveyFlagResponseAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(SurveyFlagResponseAction.class);

    @Autowired
    private CommPanelService commPanelService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Pre-process the request (from the super class)
        LoginUser loginUser = preprocess(mapping, request, response, true, true);

        String strHorseId = request.getParameter(PARAM_HORSE_ID);
        int horseId = StringUtils.str2int(strHorseId, 0);

        if (horseId <= 0) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "horseid"));
            return mapping.findForward(FWD_ERROR);
        }

        String strAssignmentId = request.getParameter(PARAM_ASSIGNID);
        int assignmentId = StringUtils.str2int(strAssignmentId, 0);

        List<FlagWorkView> flags = commPanelService.getActiveFlagsAssignedToMe(loginUser, horseId);

        if (flags == null || flags.isEmpty()) {
            // user no longer has assigned flags - redirect to the yourcontent page again
            commPanelService.adjustFlagResponseAssignment(loginUser.getUid(), horseId, null, false);
            return new ActionRedirect("/yourcontent.do");
        }

        FlagWorkView flag = flags.get(0);

        // redirect to the first flag
        String firstFlagUrl = 
                "flagResponse.do?horseid=" + horseId + 
                "&flagid=" + flag.getFlagId() + 
                "&assignid=" + assignmentId + 
                "&questionid=" + flag.getSurveyQuestionId() +
                "&initialFlagGroupId=" + flag.getGroupobjId() +
                "&fromurl=";
        
        return new ActionRedirect(firstFlagUrl);
    }

}
