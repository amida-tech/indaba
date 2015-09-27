/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.tag;

import com.ocs.indaba.common.Messages;
import com.ocs.indaba.service.HorseService;
import com.ocs.indaba.util.SpringContextUtil;
import com.ocs.indaba.vo.ActiveHorseView;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 *
 * @author Jeff Jiang
 */
public class TeamContentTagHandler extends BaseHorseContentTagHandler {

    private static final Logger logger = Logger.getLogger(TeamContentTagHandler.class);
    private static final String TASK_DISPLAY_PATTERN = "{0} - {1} {2}";
    private static final String DEFAULT_IMAGE_BASE = "./images";
    private static final int DEFAULT_IMG_WIDTH = 12;
    private HorseService horseService = (HorseService) SpringContextUtil.getBean("horseService");
    /*
     * attribute for access
     */
    private String imgbase = DEFAULT_IMAGE_BASE;
    private int width = DEFAULT_IMG_WIDTH;

    @Override
    public int doStartTag() {
        logger.debug("########################### <indaba:teamcontent> Tag Start###########################");
        logger.debug("Your assignments: [projectId=" + prjid + ", userId=" + uid + "].");
        JspWriter out = pageContext.getOut();
        Map<String, List<ActiveHorseView>> activeHorseMap = horseService.getActiveHorsesOfTeam(uid, prjid);
        if (activeHorseMap != null && activeHorseMap.size() > 0) {
            Set<String> entries = activeHorseMap.keySet();
            try {
                for (String entry : entries) {
                    out.print("<div class='box icon-home-content'>");
                    out.print("<h3>" + getI18nMessage(Messages.KEY_COMMON_MSG_TEAM_CONTENT) + " - <span style='color: #535353; '>" + entry + "</span><a href='#' class='toggleVisible'><img src='images/expand_icon.png' alt='expand' /></a></h3>");
                    out.print("<div class='content' style='padding: 0px; display: none;'>");
                    out.print("<table width='100%'>");
                    out.print("<tr class='thead'>");
                    out.print("<th width='250px'></th>");
                    out.print("<th>" + getI18nMessage(Messages.KEY_COMMON_TH_STATUS) + "</th>");
                    out.print("<th>" + getI18nMessage(Messages.KEY_COMMON_TH_VIEW) + "</th>");
                    out.print("<th>" + getI18nMessage(Messages.KEY_COMMON_TH_HISTORY) + "</th>");
                    out.print("<th>" + getI18nMessage(Messages.KEY_COMMON_TH_GOAL) + "</th>");
                    out.print("<th>" + getI18nMessage(Messages.KEY_COMMON_TH_ESTIMATE) + "</th>");
                    out.print("</tr>");
                    List<ActiveHorseView> activeHorses = activeHorseMap.get(entry);
                    if (activeHorses != null) {
                        for (int i = 0, n = activeHorses.size(); i < n; ++i) {
                            outputHorse(out, activeHorses.get(i));
                            if (i < n - 1) { // output delimitor line
                                out.println("<tr><td colspan='5'><hr/></td></tr>");
                            }
                        }
                    }
                    out.print("</table></div></div>");
                }
                //   outputTask(out, task);

            } catch (IOException ex) {
                logger.error("Error Occurs", ex);
            }
        }
        return Tag.SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        logger.debug("########################### <indaba:teamcontent> Tag End ###########################");
        return Tag.EVAL_PAGE;

    }
    /*
     * private void outputTask(JspWriter out, AssignedTask task) throws
     * IOException {
     *
     * out.print("<tr>"); // output task name out.print("<td>");
     * out.print("&nbsp;&nbsp;&nbsp;&nbsp;"); String taskDisplay =
     * MessageFormat.format(TASK_DISPLAY_PATTERN, task.getTaskName(),
     * task.getTargetName(), task.getProductName()); if (task.isClickable()) {
     * StringBuilder sBuf = new StringBuilder(); sBuf.append("<a
     * href='").append(task.getAction()).append('?').append("toolid=").append(task.getToolId()).append("&horseid=").append(task.getHorseId()).append("&assignid=").append(task.getAssignmentId()).append("'>").append(taskDisplay).append("</a>");
     * out.print(sBuf.toString()); } else { out.print(taskDisplay); }
     * out.print("</td>");
     *
     * // output task status out.print("<td>"); //outputStatus(out, task);
     * StringBuilder sBuf = new StringBuilder(); sBuf.append("<span
     * class='percentage_text'>"); sBuf.append("<img
     * src='").append(imgbase).append("/").append(task.getStatusIcon()).append("'
     * width='"). append(width).append("'
     * alt='").append(task.getRemarks()).append("'
     * ").append("title='").append(task.getRemarks()).append("'").append("/>");
     * sBuf.append("&nbsp;&nbsp;").append(task.getCompleteDisplay()).append("</span>");
     * out.print(sBuf.toString()); out.print("</td>");
     *
     * // output deadline out.print("<td>"); if (task.getDurTime() == null) {
     * task.setDisplayDeadline(" -- "); } if (task.getDisplayDeadline() != null)
     * { out.print(task.getDisplayDeadline()); } else {
     * out.print(task.getUtilDays() + " days until " +
     * DateUtils.date2Str(task.getDurTime(), DateUtils.DEFAULT_DATE_FORMAT_1));
     *
     * }
     * out.print("</td>"); out.print("</tr>");
     *
     * }
     */

    public void setBodyContent() {
    }

    /**
     * @param prjid the prjid to set
     */
    @Override
    public void setPrjid(Object prjid) {
        try {
            this.prjid = (Integer) ExpressionEvaluatorManager.evaluate("prjid", prjid.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set prjid value error" + ex);
        }
    }

    /**
     * @param uid the uid to set
     */
    @Override
    public void setUid(Object uid) {
        try {
            this.uid = (Integer) ExpressionEvaluatorManager.evaluate("uid", uid.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set uid value error" + ex);
        }
    }
}
