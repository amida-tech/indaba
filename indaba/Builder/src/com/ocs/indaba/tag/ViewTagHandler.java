/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.tag;

import com.ocs.indaba.service.AccessPermissionService;
import com.ocs.indaba.util.SpringContextUtil;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 *
 * @author Jeff Jiang
 */
public class ViewTagHandler extends BodyTagSupport {

    private static final Logger logger = Logger.getLogger(ViewTagHandler.class);

    /*
     * attribute for access
     */
    private int prjid = 0;
    private int uid = 0;
    private boolean flag = true;
    private boolean ignore = false;
    private String right = "";
    private String[] rights = null;
    private int numRights = 0;

    @Override
    public int doStartTag() {
        AccessPermissionService accessPermissionService = (AccessPermissionService) SpringContextUtil.getBean("accessPermissionService");
        logger.debug("########################### <indaba:view> Tag Start###########################");

        logger.debug("Check permission: [projectId=" + prjid + ", userId=" + uid + ", right=" + right + ", ignore=" + ignore + ", flag=" + flag + "].");
        boolean isPermitted;

        if (ignore || rights == null || numRights == 0) {
            isPermitted = true;
        } else {
            isPermitted = false;

            for (int i = 0; i < numRights; i++) {
                if (accessPermissionService.checkProjectPermission(prjid, uid, rights[i])) {
                    isPermitted = true;
                    break;
                }
            }
        }

        logger.debug("Check Permission: " + isPermitted);
        return (isPermitted && flag) ? Tag.EVAL_BODY_INCLUDE : Tag.SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        logger.debug("########################### <indaba:view> Tag End ###########################");
        return Tag.EVAL_PAGE;

    }

    public void setBodyContent() {
    }

    /**
     * @param right the right to set
     */
    public void setRight(Object right) {
        try {
            this.right = (String) ExpressionEvaluatorManager.evaluate("right", right.toString(), String.class, this, pageContext);
        } catch (Exception ex) {
            logger.error("set right value error, ", ex);
        }

        numRights = 0;
        String parts[] = this.right.split("\\|");
        rights = new String[parts.length];
        for (int i = 0; i < parts.length; i++) {
            String r = parts[i].trim();
            if (!r.isEmpty()) {
                rights[numRights++] = r;
                logger.debug("Got right: " + r);
            }
        }
    }

    /**
     * @param prjid the prjid to set
     */
    public void setPrjid(Object prjid) {
        try {
            if (prjid != null) {
                this.prjid = (Integer) ExpressionEvaluatorManager.evaluate("prjid", prjid.toString(), Integer.class, this, pageContext);
            }
        } catch (Exception ex) {
            logger.error("set prjid value error.", ex);
        }
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(Object uid) {
        try {
            if (uid != null) {
                this.uid = (Integer) ExpressionEvaluatorManager.evaluate("uid", uid.toString(), Integer.class, this, pageContext);
            } else {
                logger.info("User id is NULL!");
            }
        } catch (Exception ex) {
            logger.error("set uid value error.", ex);
        }
    }

    /**
     * @param flag the flag to set
     */
    public void setFlag(Object flag) {
        try {
            this.flag = (Boolean) ExpressionEvaluatorManager.evaluate("flag", flag.toString(), Boolean.class, this, pageContext);
        } catch (Exception ex) {
            logger.error("set uid value error" + ex);
        }
    }
    /**
     * @param ignore the ignore to set
     */
    public void setIgnore(Object ignore) {
        try {
            this.ignore = (Boolean) ExpressionEvaluatorManager.evaluate("ignore", ignore.toString(), Boolean.class, this, pageContext);
        } catch (Exception ex) {
            //logger.error("set uid value error" + ex);
        }
    }
}
