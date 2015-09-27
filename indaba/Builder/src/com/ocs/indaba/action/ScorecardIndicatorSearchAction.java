/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.action;

import java.util.List;
import com.ocs.indaba.service.TagService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Administrator
 */
public class ScorecardIndicatorSearchAction extends BaseAction {

    /* forward name="success" path="" */
    //private static final String SUCCESS = "success";
    private TagService tagService;
    private static final Logger log = Logger.getLogger(ScorecardIndicatorSearchAction.class);
    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        preprocess(mapping, request, response);

//        int userId = (Integer)request.getAttribute(ATTR_USERID);
        int horseId = Integer.parseInt(request.getParameter(PARAM_HORSE_ID));
        int assignId = Integer.parseInt(request.getParameter("assignid"));
        String action = request.getParameter("action");

        List<String[]> labelList = tagService.selectTag(horseId, "");
        StringBuffer sb = new StringBuffer();
        sb.append("<div id='indicatorSearch'>");
        sb.append("<ul class='tag-cloud'>");
        for (String[] par : labelList) {
            sb.append("<li><a onclick=\"getIndicatorContent('startWith=")
              .append("&horseid=").append(horseId).append("&label=").append(par[0])
              .append("&action=").append(action)
              .append("&assignid=").append(assignId).append("')\">").append(par[0])
              .append(" (").append(par[1]).append(")</a><span/></li>");
        }
        sb.append("</ul>");
        sb.append("<div class='clear'/></div>");
        sb.append("<div>");
        
        if (sb.length() > 0) {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(sb.toString());
        } else {
            response.getWriter().write("");
        }
        return null;
    }

    @Autowired
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }
}