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
import java.util.ArrayList;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 *
 * @author Jeff Jiang
 */
public class AllContentTagHandler extends BaseHorseContentTagHandler {

    private static final Logger logger = Logger.getLogger(AllContentTagHandler.class);
    private HorseService horseService = (HorseService) SpringContextUtil.getBean("horseService");
    private int status;
    private List<Integer> targetIds;
    private List<Integer> prodIds;

    @Override
    public int doStartTag() {
        logger.debug("########################### <indaba:allcontent> Tag Start###########################");
        logger.debug("All Content: [projectId=" + prjid + ", userId=" + uid + ", targetIds=" + targetIds + ", prodIds=" + prodIds + ", status=" + status + "].");
        JspWriter out = pageContext.getOut();
        List<ActiveHorseView> activeHorses = horseService.getAllActiveHorses(uid, prjid, targetIds, prodIds, status);
        if (activeHorses != null && activeHorses.size() > 0) {
            try {
                out.print("<thead>");
                out.print("<tr class='thead'>");
                out.print("<th id='column-content' width='200px'>" + getI18nMessage(Messages.KEY_COMMON_CONTENT) + "</th>");
                out.print("<th id='column-status'>" + getI18nMessage(Messages.KEY_COMMON_TH_STATUS) + "</th>");
                out.print("<th id='column-done' width='50'>" + getI18nMessage(Messages.KEY_COMMON_TH_DONE_PERCENT) + "</th>");
                out.print("<th id='column-view'>" + getI18nMessage(Messages.KEY_COMMON_TH_VIEW) + "</th>");
                out.print("<th id='column-history'>" + getI18nMessage(Messages.KEY_COMMON_TH_HISTORY) + "</th>");
                out.print("<th nowrap id='column-next-due'>" + getI18nMessage(Messages.KEY_COMMON_TH_NEXTDUE) + "</th>");
                out.print("<th id='column-open-cases' width='80'>" + getI18nMessage(Messages.KEY_COMMON_TH_OPENCASES) + "</th>");
                out.print("<th id='column-people-assigned'>" + getI18nMessage(Messages.KEY_COMMON_TH_PEOPLEASSIGNED) + "</th>");
                out.println("</tr>");
                out.print("</thead>");
                out.flush();

                out.print("<tbody>");
                for (int i = 0, n = activeHorses.size(); i < n; ++i) {
                    outputHorseDetails(out, activeHorses.get(i), true);
                }
                activeHorses.clear();
                activeHorses = null;
                out.print("</tbody>");
                //   outputTask(out, task);
            } catch (IOException ex) {
                logger.error("Error Occurs", ex);
            }
        }
        return Tag.SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        logger.debug("########################### <indaba:allcontent> Tag End ###########################");
        return Tag.EVAL_PAGE;

    }

    public void setBodyContent() {
    }

    /**
     * @param targetIds the target ids to set
     */
    public void setTargetIds(Object targetIds) {
        try {
            if (targetIds != null) {
                String targets = (String) ExpressionEvaluatorManager.evaluate("targetIds", targetIds.toString(), String.class, this, pageContext);
                String[] array = targets.split("[\\[|\\]|,| ]");
                this.targetIds = new ArrayList<Integer>();
                int val = 0;
                for (String s : array) {
                    try {
                        val = Integer.valueOf(s);
                        this.targetIds.add(val);
                    } catch (Exception ex) {
                    }
                }
            }
        } catch (JspException ex) {
            logger.error("set targetIds value error" + ex);
        }
    }

    /**
     * @param prodIds the product ids to set
     */
    public void setProdIds(Object prodIds) {
        try {
            if (prodIds != null) {
                String targets = (String) ExpressionEvaluatorManager.evaluate("prodIds", prodIds.toString(), String.class, this, pageContext);
                String[] array = targets.split("[\\[|\\]|,| ]");
                this.prodIds = new ArrayList<Integer>();
                int val = 0;
                for (String s : array) {
                    try {
                        val = Integer.valueOf(s);
                        this.prodIds.add(val);
                    } catch (Exception ex) {
                    }
                }
            }
        } catch (JspException ex) {
            logger.error("set prodIdsvalue error" + ex);
        }
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Object status) {
        try {
            this.status = (Integer) ExpressionEvaluatorManager.evaluate("status", status.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set status error" + ex);
        }
    }
}
