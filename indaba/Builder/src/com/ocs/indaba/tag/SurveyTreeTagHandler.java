/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.tag;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.service.SurveyConfigService;
import com.ocs.indaba.service.ToolService;
import com.ocs.indaba.survey.tree.CategoryNode;
import com.ocs.indaba.survey.tree.Node;
import com.ocs.indaba.survey.tree.QuestionNode;
import com.ocs.indaba.survey.tree.SurveyTree;
import com.ocs.indaba.util.SpringContextUtil;
import com.ocs.util.SimpleTree;
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
public class SurveyTreeTagHandler extends BaseTagHandler {

    private static final Logger logger = Logger.getLogger(SurveyTreeTagHandler.class);
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
        logger.debug("########################### <indaba:surveyTree> Tag Start###########################");
        SurveyConfigService surveyConfigService = (SurveyConfigService) SpringContextUtil.getBean("surveyConfigService");
        ToolService toolService = (ToolService) SpringContextUtil.getBean("toolService");
        this.taskType = toolService.getToolTaskTypeByTaskAssignmentId(assignid);
        logger.debug("Check suvey category tree: [horseid=" + horseid + ", action=" + action + ", userid=" + userid + ", assignid=" + assignid + ", taskType=" + taskType + "].");
        actionHandler = Constants.surveyActionMap.get(action);
        if (actionHandler == null) {
            actionHandler = Constants.surveyActionMap.get(Constants.SURVEY_ACTION_DISPLAY);
        }

        // YANFIX
        logger.debug("Build survey tree for user " + userid + " with language " + super.getLanguageId());
        SurveyTree surveyTree = surveyConfigService.buildTreeAndInitAnswers(horseid, taskType, userid, super.getLanguageId());
        // SurveyTree surveyTree = surveyConfigService.buildTreeAndInitAnswers(horseid, taskType, userid);

        SimpleTree<Node> root = surveyTree.getRoot();
        if (surveyTree == null || (root = surveyTree.getRoot()) == null) {
            return Tag.SKIP_BODY;
        }

        try {
            List<SimpleTree<Node>> children = root.getChildren();
            if (children != null && !children.isEmpty()) {
                for (SimpleTree<Node> child : children) {
                    Node node = child.getValue();
                    if (node.getType() == Node.NODE_TYPE_QUESTION) {
                        handleQuestionNode(pageContext.getOut(), child);
                    } else {
                        handleCategoryNode(pageContext.getOut(), child);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Error occurs when output survey category tree.", ex);
        }
        return Tag.SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        logger.debug("########################### <indaba:surveyTree> Tag End ###########################");
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

    private void handleQuestionNode(JspWriter writer, SimpleTree<Node> tree) throws IOException {
        QuestionNode qNode = null;
        if (tree == null || (qNode = (QuestionNode) tree.getValue()) == null) {
            return;
        }
        StringBuilder sBuf = new StringBuilder();

        sBuf.append("\t<div class='content'>\n");
        sBuf.append("\t<table>\n");
        sBuf.append("\t\t<tr class='").append(getStatusStyle(taskType, qNode)).append("'>\n");
        sBuf.append("\t\t<td nowrap valign=top width=1%><span class='statusIndicator'>&nbsp;</span><b>").append(qNode.getPublicName()).append(":</b></td>");
        sBuf.append("<td><a href='").append(actionHandler).append("?questionid=").append(qNode.getId()).append("&assignid=").append(assignid).
                append("&horseid=").append(horseid).append("&action=").append(action);
        if (cntverid > 0) {
            sBuf.append("&contentVersionId=").append(cntverid);
        }
        sBuf.append("&returl=").append(returl).append("'>").
                append(qNode.getText()).append("</a></td>");
        sBuf.append("\t\t</tr>");
        sBuf.append("\t</table>\n");
        sBuf.append("\t</div>\n");
        flushBuffer(writer, sBuf);
    }

    private void handleCategoryNode(JspWriter writer, SimpleTree<Node> tree) throws IOException {
        CategoryNode cNode = null;

        if (tree == null || (cNode = (CategoryNode) tree.getValue()) == null) {
            return;
        }

        String statusStyle = "none";
        if (taskType == Constants.TASK_TYPE_SURVEY_PR_REVIEW) {
            int total = cNode.getCompleted() + cNode.getIncompleted();
            if (cNode.getAgreed() + cNode.getNoQualified() == total) {
                statusStyle = "complete";
            } else if (cNode.getUnreviewed() == total) {
                statusStyle = "none";
            } else if (cNode.getDisagreed() != 0 || cNode.getNoQualified() != 0) {
                statusStyle = "disagree";
            } else {
                statusStyle = "incomplete";
            }

        } else {
            if (cNode.getCompleted() == 0) {
                statusStyle = "none";
            } else if (cNode.getIncompleted() == 0) {
                statusStyle = "complete";
            } else {
                statusStyle = "disagree";
            }
        }

        String display = "";
        String displayHint = "<img src='images/collapse_icon.png' alt='collapse' />";
        if (tree.getParent().isRoot()) {
            displayHint = "<img src='images/expand_icon.png' alt='expand' />";
        } else if (tree.getParent().isRoot()) {
            display = "hide";
        }
        StringBuilder sBuf = new StringBuilder();
        sBuf.append("<div class='content ").append(display).append("'>\n");
        sBuf.append("\t<div class='box'>\n");
        sBuf.append("\t\t<h3 class='").append(statusStyle).append("'><a name='").append(cNode.getLabel()).append("'>").append(cNode.getLabel()).append("</a>&nbsp;&nbsp;&nbsp;").append(cNode.getTitle());
        sBuf.append("<a href='#").append(cNode.getLabel()).append("' class='toggleVisible'>").append(displayHint).append("</a>");
        sBuf.append("<span style='float:right;margin:0 10px'>").append(cNode.getCompleted()).append("/").append(cNode.getIncompleted() + cNode.getCompleted()).append("</span>");
        sBuf.append("</h3>\n");
        flushBuffer(writer, sBuf);

        List<SimpleTree<Node>> children = tree.getChildren();
        if (children != null && !children.isEmpty()) {
            for (SimpleTree<Node> child : children) {
                Node node = child.getValue();
                switch (node.getType()) {
                    case Node.NODE_TYPE_CATEGORY:
                        handleCategoryNode(writer, child);
                        break;
                    case Node.NODE_TYPE_QUESTION:
                        handleQuestionNode(writer, child);
                        break;
                }
            }
        }
        sBuf.append("\t</div>\n\t\t</div>\n");
        flushBuffer(writer, sBuf);
    }

    private void flushBuffer(JspWriter writer, StringBuilder sBuf) throws IOException {
        writer.println(sBuf);
        writer.flush();
        sBuf.setLength(0);
    }

    private static String getStatusStyle(int taskType, QuestionNode qNode) {
        String statusStyle = "none";
        if (qNode == null) {
            return statusStyle;
        }
        if (taskType != Constants.TASK_TYPE_SURVEY_PR_REVIEW) {
            switch (qNode.getStatus()) {
                case QuestionNode.ANSWER_STATUS_COMPLETE:
                    statusStyle = "complete";
                    break;

                case QuestionNode.ANSWER_STATUS_INCOMPLETE:
                    statusStyle = "incomplete";
                    break;

                default:
                    break;

            }
        } else {
            switch (qNode.getReviewOption()) {
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
