/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.tag;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.vo.GoalObjectView;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 *
 * @author Jeff Jiang
 */
public abstract class GoalTagHandler extends BodyTagSupport {

    private static final Logger logger = Logger.getLogger(GoalTagHandler.class);
    private static final int MAX_BLOCKS_PER_LINE = 15;
    /*attribute for access*/
    private int status = 0;
    private int index = 0;
    private int seqid = 0;
    private String name = "";

    @Override
    public int doStartTag() {
        logger.debug("########################### <indaba:goal> Tag Start###########################");
        logger.debug("Your assignments: [status=" + status + ", index=" + index + ", seqid=" + seqid + ", name=" + name + "].");
        JspWriter out = pageContext.getOut();
        try {
            outputGoal(out);
        } catch (Exception ex) {
            logger.debug("I/O error. " + ex);
        }
        return Tag.SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        logger.debug("########################### <indaba:goal> Tag End ###########################");
        return Tag.EVAL_PAGE;

    }

    public void setBodyContent() {
    }

    /**
     *  Get the horseView content url
     *
     * @param contentType
     * @param horseId
     * @return
     */
    protected void outputGoal(JspWriter out) throws IOException {
        String cssStyle = "";
        switch (status) {
            case Constants.GOAL_OBJECT_STATUS_WAITING: {
                //sBuf.append("<a  href = '#' class='blk_incompleted' title='S").append(seqId).append(". ").append(goalObj.getGoalName()).append("' style='color:#FFFFFF'></a>");
                cssStyle = "status-blk-incompleted";
                break;
            }
            case Constants.GOAL_OBJECT_STATUS_STARTING: {
                //sBuf.append("<a  href = '#' class='blk_ongoing' title='S").append(seqId).append(". ").append(goalObj.getGoalName()).append("' style='color:#FFFFFF'>+</a>");
                cssStyle = "status-blk-starting";
                break;
            }
            case Constants.GOAL_OBJECT_STATUS_STARTED: {
                //sBuf.append("<a  href = '#' class='blk_ongoing' title='S").append(seqId).append(". ").append(goalObj.getGoalName()).append("' style='color:#FFFFFF'>+</a>");
                cssStyle = "status-blk-ongoing";
                break;
            }
            case Constants.GOAL_OBJECT_STATUS_DONE: {
                //sBuf.append("<a  href = '#' class='blk_completed' title='S").append(seqId).append(". ").append(goalObj.getGoalName()).append("' style='color:#FFFFFF'></a>");
                cssStyle = "status-blk-completed";
                break;
            }
            case Constants.GOAL_OBJECT_STATUS_OVERDUE:
            default: {
                //sBuf.append("<a  href = '#' class='blk_overdue' title='S").append(seqId).append(". ").append(goalObj.getGoalName()).append("' style='color:#FFFFFF'></a>");
                cssStyle = "status-blk-overdue";
                break;
            }
        }
        StringBuilder sBuf = new StringBuilder();
        sBuf.append("<a  href = '#' class='status-blk ").append(cssStyle).append("' title='").append(name).append("'>").append(seqid).append("</a>");
        if (seqid > MAX_BLOCKS_PER_LINE) {
            seqid = 0;
            sBuf.append("<br/>");
        }
        out.write(sBuf.toString());
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Object status) {
        try {
            this.status = (Integer) ExpressionEvaluatorManager.evaluate("status", status.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set prjid value error" + ex);
        }
    }

    /**
     * @param status the status to set
     */
    public void setIndex(Object index) {
        try {
            this.index = (Integer) ExpressionEvaluatorManager.evaluate("index", index.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set prjid value error" + ex);
        }
    }

    /**
     * @param status the status to set
     */
    public void setSeqid(Object seqid) {
        try {
            this.seqid = (Integer) ExpressionEvaluatorManager.evaluate("seqid", seqid.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set prjid value error" + ex);
        }
    }

    /**
     * @param index the index to set
     */
    public void setName(Object name) {
        try {
            this.name = (String) ExpressionEvaluatorManager.evaluate("name", name.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set uid value error" + ex);
        }
    }
}

