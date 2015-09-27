/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.Tag;
import com.ocs.indaba.service.TagService;
import com.ocs.indaba.vo.LoginUser;
import org.apache.log4j.Logger;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeanbone
 */
public class TagAction extends BaseAction {
    
    /* forward name="success" path="" */
    //private static final String SUCCESS = "success";
    private TagService tagService;

    private static final Logger logger = Logger.getLogger(TagAction.class);
    
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
        
        LoginUser loginUser = preprocess(mapping, request, response);
        
        String label = request.getParameter("new_tag").toString();
        Date time = new Date();

        int surveyAnswerId = Integer.valueOf(request.getParameter(PARAM_ANSWER_ID));
        int surveyContentObjectId = Integer.valueOf(request.getParameter("contentobjectid"));
        int action = Integer.valueOf(request.getParameter("action"));

        if (!label.equals("")) {
            Tag tag = new Tag();
            tag.setTagType((short)Constants.USER_TAG);
            tag.setTaggedObjectType((short)Constants.TAGGED_OBJECT_TYPE_SURVEY_ANSWER);
            tag.setTaggedObjectId(surveyAnswerId);
            tag.setTaggedObjectScopeId(surveyContentObjectId);
            tag.setTaggingTime(time);
            tag.setUserId(loginUser.getUid());
            //tag.setLabel(label.toLowerCase());
            tag.setLabel(label);
            if (action == 0) {
                tag = tagService.addTag(tag);
            } else if (action == 1){
                tagService.deleteTag(tag);
            }
        }
        List<String[]> tagList = tagService.selectTagLabelsByAnswerId(surveyAnswerId,surveyContentObjectId);
        StringBuffer sb = new StringBuffer();
        sb.append("<ul class='tag-cloud'>");

        if (tagList != null) {
            for (String[] par : tagList) {
                if (par[1].equals("1")) {
                    /*
                    sb.append("<a onclick=\"submitAddTag(")
                    .append(""+surveyAnswerId+","+surveyContentObjectId+",1,'"+par[0])
                    .append("')\" ><font color='green' size=2><b>").append(par[0])
                    .append("</b></font></a>&nbsp;&nbsp;");
                     *
                     */
                    sb.append("<li><a onclick=\"submitAddTag(").append("" + surveyAnswerId + "," + surveyContentObjectId + ",1,'" + par[0]).append("')\">").append(par[0]).append("</a><span/></li>");
                } else {
                    /*
                    sb.append("<a onclick=\"submitAddTag(")
                    .append(""+surveyAnswerId+","+surveyContentObjectId+",0,'"+par[0])
                    .append("')\" ><font color='blue' size=2>").append(par[0])
                    .append("</font></a>&nbsp;&nbsp;");
                     *
                     */
                    sb.append("<li class=\"tag-detached\"><a onclick=\"submitAddTag(").append("" + surveyAnswerId + "," + surveyContentObjectId + ",0,'" + par[0]).append("')\">").append(par[0]).append("</a><span/></li>");
                }
            }
        }
        sb.append("</ul>");
        sb.append("<div class='clear'/></div>");
        try {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(sb.toString());
        } catch (Exception ex) {
            logger.error("bad parameters from client", ex);
        }
        return null;
    }

    @Autowired
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }
}
