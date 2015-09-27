/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.service.ChartService;
import com.ocs.util.StringUtils;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Jeanbone
 */
public class LoadHistoryChartAction extends BaseAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
        response.getWriter().write(URLEncoder.encode(ChartService.getChartXmlByHorseId(horseId), "UTF-8"));
        return null;
    }
}
