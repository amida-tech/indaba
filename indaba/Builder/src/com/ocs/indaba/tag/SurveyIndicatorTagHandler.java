/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.tag;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.SurveyCategory;
import com.ocs.indaba.service.SurveyCategoryService;
import com.ocs.indaba.service.ToolService;
import com.ocs.indaba.util.SpringContextUtil;
import com.ocs.indaba.vo.SurveyCategoryTree;
import com.ocs.indaba.vo.SurveyQuestionVO;
import java.io.IOException;
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

/* YC: 2014-02-03
 * 
 * NOTE: This tag handler is no longer used. It was used to render the survey tree on the scorecard home page.
 *
 * The survey tree is now rendered with the SurveyTreeTagHanlder.
 */


public class SurveyIndicatorTagHandler extends BodyTagSupport {

    private static final Logger logger = Logger.getLogger(SurveyIndicatorTagHandler.class);
    //private static final String SURVEY_CREATE_ACTION = "surveyCreate.do";
    //private static final String SURVEY_EDIT_ACTION = "surveyEdit.do";
    //private static final String SURVEY_REVIEW_ACTION = "surveyReview.do";
    //private static final String SURVEY_PEER_REVIEW_ACTION = "surveyPeerReview.do";
    //private static final String SURVEY_PRREVIEW_ACTION = "surveyPRReview.do";
    private static final String ACTION_SURVEY_PRE_VERSION_DISPLAY = "surveyPreVersionDisplay";
    /*attribute for access*/
    private int userid = 0;
    private int horseid = 0;
    private int assignid = 0;
    private int taskType = 0;
    private int cntverid = 0;
    private String action = "display";
    private String returl = "";
    private String actionHandler = "";

    @Override
    public int doStartTag() {
        logger.debug("########################### <indaba:indicator> Tag Start###########################");
        SurveyCategoryService surveyCategoryService = (SurveyCategoryService) SpringContextUtil.getBean("surveyCategoryService");
        ToolService toolService = (ToolService) SpringContextUtil.getBean("toolService");
        this.taskType = toolService.getToolTaskTypeByTaskAssignmentId(assignid);
        logger.debug("Check suvey category tree: [horseid=" + horseid + ", action=" + action + ", userid=" + userid + ", assignid=" + assignid + ", taskType=" + taskType + "].");
        actionHandler = Constants.surveyActionMap.get(action);
        if (actionHandler == null) {
            actionHandler = Constants.surveyActionMap.get(Constants.SURVEY_ACTION_DISPLAY);
        }

        List<SurveyCategoryTree> trees = surveyCategoryService.buildSurveyCategoryTrees(horseid, taskType, userid);
        JspWriter out = pageContext.getOut();
        if (trees != null) {
            try {
                for (SurveyCategoryTree aTree : trees) {
                    StringBuffer sBuf = new StringBuffer();
                    output(sBuf, aTree);
                    out.println(sBuf);
                }
            } catch (IOException ex) {
                logger.debug("Error occurs when output survey category tree.", ex);
            }
        }
        return Tag.SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        logger.debug("########################### <indaba:indicator> Tag End ###########################");
        return Tag.EVAL_PAGE;
    }

    /**
     * @param userid the userid to set
     */
    public void setUserid(Object userid) {
        try {
            this.userid = (Integer) ExpressionEvaluatorManager.evaluate("userid", userid.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set horseid value error." + ex);
        }
    }

    /**
     * @param horseid the horseid to set
     */
    public void setHorseid(Object horseid) {
        try {
            this.horseid = (Integer) ExpressionEvaluatorManager.evaluate("horseid", horseid.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set horseid value error." + ex);


        }
    }

    /**
     * @param horseid the horseid to set
     */
    public void setAssignid(Object assignid) {
        try {
            if (assignid != null) {
                this.assignid = (Integer) ExpressionEvaluatorManager.evaluate("assignid", assignid.toString(), Integer.class, this, pageContext);
            }
        } catch (JspException ex) {
            logger.error("set horseid value error." + ex);


        }
    }

    /**
     * @param cntverid the cntverid to set
     */
    public void setCntverid(Object cntverid) {
        try {
            if (cntverid != null) {
                this.cntverid = (Integer) ExpressionEvaluatorManager.evaluate("cntverid", cntverid.toString(), Integer.class, this, pageContext);
            }
        } catch (JspException ex) {
            logger.error("set cntverid value error." + ex);
        }
    }

    /**
     * @param action the action to set
     */
    public void setAction(Object action) {
        try {
            this.action = (String) ExpressionEvaluatorManager.evaluate("action", action.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set action value error." + ex);


        }
    }

    /**
     * @param action the action to set
     */
    public void setReturl(Object returl) {
        try {
            if (returl != null) {
                this.returl = (String) ExpressionEvaluatorManager.evaluate("returl", returl.toString(), String.class, this, pageContext);
            }
        } catch (JspException ex) {
            logger.error("set action value error." + ex);
        }
    }

    private void output(StringBuffer sBuf, SurveyCategoryTree tree) throws IOException {
        if (tree == null) {
            return;
        }
        SurveyCategory node = tree.getCurNode();

        if (node == null) {
            return;
        }
        String statusStyle = "none";
        if (taskType == Constants.TASK_TYPE_SURVEY_PR_REVIEW) {
            int total = tree.getCompleted() + tree.getIncompleted();
            if (tree.getAgreed() + tree.getNoQualified() == total) {
                statusStyle = "complete";
            } else if (tree.getUnreviewed() == total) {
                statusStyle = "none";
            } else if (tree.getDisagreed() != 0 || tree.getNoQualified() != 0) {
                statusStyle = "disagree";
            } else {
                statusStyle = "incomplete";
            }

        } else {
            if (tree.getCompleted() == 0) {
                statusStyle = "none";
            } else if (tree.getIncompleted() == 0) {
                statusStyle = "complete";
            } else {
                statusStyle = "disagree";
            }
        }

        String display = "";
        String displayHint = "<img src='images/collapse_icon.png' alt='collapse' />";
        if (tree.getParent() == null) {
            displayHint = "<img src='images/expand_icon.png' alt='expand' />";
        } else if (tree.getParent().getParent() == null) {
            display = "hide";
        }
        sBuf.append("<div class='content ").append(display).append("'>\n");
        sBuf.append("\t<div class='box'>\n");
        sBuf.append("\t\t<h3 class='").append(statusStyle).append("'><a name='").append(node.getLabel()).append("'>").append(node.getLabel()).append("</a>&nbsp;&nbsp;&nbsp;").append(node.getTitle());
        sBuf.append("<a href='#").append(node.getLabel()).append("' class='toggleVisible'>").append(displayHint).append("</a>");
        sBuf.append("<span style='float:right;margin:0 10px'>").append(tree.getCompleted()).append("/").append(tree.getIncompleted() + tree.getCompleted()).append("</span>");
        sBuf.append("</h3>\n");

        if (!tree.isLeaf()) {
            List<SurveyCategoryTree> children = tree.getChildren();
            if (children != null && children.size() > 0) {
                for (SurveyCategoryTree aChild : children) {
                    output(sBuf, aChild);
                }
            }
        } else {
            List<SurveyQuestionVO> questions = tree.getLeafNodes();
            sBuf.append("\t<div class='content'>\n");
            if (questions != null) {
                for (SurveyQuestionVO question : questions) {
                    sBuf.append("\t<table>\n");
                    sBuf.append("\t\t<tr class='").append(getStatusStyle(taskType, question)).append("'>\n");
                    sBuf.append("\t\t<td nowrap valign=top width=1%><span class='statusIndicator'>&nbsp;</span><b>").append(question.getQuestion().getPublicName()).append(":</b></td>");
                    sBuf.append("<td><a href='").append(actionHandler).append("?questionid=").append(question.getQuestion().getId()).append("&assignid=").append(assignid).
                            append("&horseid=").append(horseid).append("&action=").append(action);
                    if (cntverid > 0) {
                        sBuf.append("&contentVersionId=").append(cntverid);
                    }
                    sBuf.append("&returl=").append(returl).append("'>").
                            append(question.getIndicator().getQuestion()).append("</a></td>");
                    sBuf.append("\t\t</tr>");
                    sBuf.append("\t</table>\n");
                }
            }
            sBuf.append("\t</div>\n");
        }
        sBuf.append("\t</div>\n");
        sBuf.append("</div>\n");
    }

    private static String getStatusStyle(int taskType, SurveyQuestionVO question) {
        String statusStyle = "none";
        if (question == null) {
            return statusStyle;
        }
        if (taskType != Constants.TASK_TYPE_SURVEY_PR_REVIEW) {
            statusStyle = question.isCompleted() ? "complete" : "incomplete";
        } else {
            switch (question.getReviewOption()) {
                case Constants.SURVEY_REVIEW_OPTION_AGREE:
                case Constants.SURVEY_REVIEW_OPTION_NO_QUALIFICATION:
                    statusStyle = "complete";
                    break;
                case Constants.SURVEY_REVIEW_OPTION_DISAGREE:
                case Constants.SURVEY_REVIEW_OPTION_AGREE_BUT_CLARIFICATION:
                    statusStyle = "disagree-leaf";
                    break;
                case Constants.SURVEY_REVIEW_OPTION_NONE:
                default:
                    statusStyle = "none";
            }
        }
        return statusStyle;
    }
}
