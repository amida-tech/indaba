/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.tag;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.service.ViewPermissionService;
import com.ocs.indaba.util.SpringContextUtil;
import com.ocs.indaba.vo.UserDisplay;
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
public class UserDisplayTagHandler extends BodyTagSupport {

    private static final Logger logger = Logger.getLogger(AccessTagHandler.class);

    /*attribute for access*/
    private int prjid = 0;
    private int subjectUid = 0;
    private int targetUid = 0;
    private boolean pureText = false;
    private boolean showBio = false;

    @Override
    public int doStartTag() {
        ViewPermissionService viewPermissionService = (ViewPermissionService) SpringContextUtil.getBean("viewPermissionService");
        logger.debug("########################### <indaba:userDisplay> Tag Start###########################");

        UserDisplay userDisplay = viewPermissionService.getUserDisplayOfProject(prjid, Constants.DEFAULT_VIEW_MATRIX_ID, subjectUid, targetUid);
        logger.debug("Get User Display[permisstion=" + userDisplay.getPermission() + ", display=" + userDisplay.getDisplayUsername() + "] <== [projectId=" + prjid + ", subjectUid=" + subjectUid + ", targetUid=" + targetUid + "].");

        JspWriter out = pageContext.getOut();
        try {
            if (userDisplay.getPermission() == Constants.VIEW_PERMISSION_NONE) {
                out.print(userDisplay.getDisplayUsername());
            } else {
                if (pureText) {
                    out.print(userDisplay.getDisplayUsername());
                } else {
                    out.print("<a href='profile.do?targetUid=" + targetUid + "'>" + userDisplay.getDisplayUsername() + "</a>");
                }
            }
            if (showBio) {
                out.print("<p style='color: gray;'> ");
                if (userDisplay.getBio() != null) {
                    out.print(userDisplay.getBio());
                }
                out.print("</p>");
            }
        } catch (Exception ex) {
            logger.debug("I/O Error.", ex);
        }
        return Tag.SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        logger.debug("########################### <indaba:userDisplay> Tag End ###########################");
        return Tag.EVAL_PAGE;

    }

    public void setBodyContent() {
    }

    /**
     * @param prjid the prjid to set
     */
    public void setPrjid(Object prjid) {
        try {
            this.prjid = (Integer) ExpressionEvaluatorManager.evaluate("prjid", prjid.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set prjid value error" + ex);
        }
    }

    /**
     * @param subjectUid the subjectUid to set
     */
    public void setSubjectUid(Object subjectUid) {
        try {
            this.subjectUid = (Integer) ExpressionEvaluatorManager.evaluate("subjectUid", subjectUid.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set uid value error" + ex);
        }
    }

    /**
     * @param subjectUid the subjectUid to set
     */
    public void setTargetUid(Object targetUid) {
        try {
            this.targetUid = (Integer) ExpressionEvaluatorManager.evaluate("targetUid", targetUid.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set uid value error" + ex);
        }
    }

    /**
     * @param subjectUid the subjectUid to set
     */
    public void setShowBio(Object showBio) {
        try {
            if (showBio != null) {
                this.showBio = (Boolean) ExpressionEvaluatorManager.evaluate("showBio", showBio.toString(), Boolean.class, this, pageContext);
            }
        } catch (JspException ex) {
            logger.error("set uid value error" + ex);
        }
    }

    /**
     * @param setPureText the pureText to set
     */
    public void setPureText(Object pureText) {
        try {
            if (pureText != null) {
                this.pureText = (Boolean) ExpressionEvaluatorManager.evaluate("pureText", pureText.toString(), Boolean.class, this, pageContext);
            }
        } catch (JspException ex) {
            logger.error("set pureText error" + ex);
        }
    }
}

