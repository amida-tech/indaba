/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.action;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.Tool;
import java.util.List;
import com.ocs.indaba.service.TagService;
import com.ocs.indaba.service.ToolService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.ocs.indaba.vo.TagContent;
import java.net.URLEncoder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Administrator
 */
public class ScorecardIndicatorSearchContentAction extends BaseAction {
    
    /* forward name="success" path="" */
    //private static final String SUCCESS = "success";
    private TagService tagService;
    private ToolService toolService;
    
    private static final Logger log = Logger.getLogger(ScorecardIndicatorSearchContentAction.class);
    
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
        
        int horseId = Integer.parseInt(request.getParameter(PARAM_HORSE_ID));
        int assignId = Integer.parseInt(request.getParameter("assignid"));
        
        String label = request.getParameter("label");
        if(label == null) {
            label = "";
        }
        String returl = request.getParameter("returl");
        String srcAction = returl.substring(returl.lastIndexOf("/") + 1, returl.indexOf("?"));

        returl += "&horseid=" + horseId;
//        if (assignId != -1) {
            returl += "&assignid=" + assignId;
//        }

        String action = null;
        if (assignId == -1 || assignId == 0) {
            action = "surveyAnswerDisplay.do";
        } else {
            Tool tool = toolService.getToolActionByTaskAssignmentId(assignId);
            returl += "&tasktype=" + tool.getTaskType();
            action = Constants.surveyActionMap.get(tool.getAction());
        }
        
        List<TagContent> tagContentList = tagService.selectTagContent(horseId, label);

        if (tagContentList != null) {
            for (TagContent tagContent : tagContentList) {
                tagContent.setAction_url(action + "?questionid=" + tagContent.getSurvey_question_id()
                        + "&assignid=" + assignId + "&horseid=" + horseId
                        + "&action=" + srcAction + "&returl=" + URLEncoder.encode(returl, "UTF-8"));
            }
        }

        String div = "<div id=\"indicatorSearchContent\" align=\"left\"><hr>";
        if (tagContentList != null) {
            for (TagContent tagContent : tagContentList) {
                div += tagContent.getQ_public_name() + ": "
                        + "<a href=\"" + tagContent.getAction_url() + "\">"
                        + tagContent.getQuestion() + "</a><br><br>";
            }
        }
        div += "<input type='hidden' id='tag' value='" + label + "'>";

        String currentAction = request.getParameter("action");

        if (currentAction.equals("surveyReview.do") || currentAction.equals("surveyPRReview.do")) {
            String addQstLabel = super.getMessage(request, "jsp.surveryAnsBar.add.question");

            div += "<div style='float: right'><a href='#' onclick=\"addTaggedToQuestions('" + horseId + "', document.getElementById('tag').value); return false;\">" + 
                    addQstLabel +
                    "</a></div>";
        }
        
        div += "</div>";
 
        response.getWriter().write(div);
        return null;
    }

    @Autowired
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }
    
    @Autowired
    public void setToolService(ToolService toolService) {
        this.toolService = toolService;
    }
}
