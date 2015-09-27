/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.rwi.action;

import com.ocs.indaba.aggregation.action.BaseRWIExportAction;
import com.ocs.indaba.aggregation.rwi.json.VerticalScorecardSummary4RWIJsonUtils;
import com.ocs.indaba.aggregation.vo.ScorecardInfo;
import com.ocs.util.StringUtils;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff
 */
public class VerticalScorecardSummary4RWIAction extends BaseRWIExportAction {

    private static final Logger log = Logger.getLogger(VerticalScorecardSummary4RWIAction.class);
    private static final String PARAM_HORSE_ID = "horseId";

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        prehandle(mapping, request);
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), -1);
        log.info("Get vertical scorecard summary for RWI - [horseId=" + horseId + ", version=" + version + "]");
        if (!isRWIProduct(horseId)) {
            super.writeMsg(response, "{'errmsg': 'The horse doesn't run with a RWI producnt.'}");
            log.error("The horse doesn't run with a RWI product.");
            return null;
        }
        ScorecardInfo scorecard = getScorecardInfo(horseId);
        JSONObject jsonObj = VerticalScorecardSummary4RWIJsonUtils.toJson(scorecard);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.write(jsonObj.toJSONString());
        } finally {
            out.close();
        }
        return null;
    }
}
