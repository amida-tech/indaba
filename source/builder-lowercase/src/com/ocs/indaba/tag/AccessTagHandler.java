/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */

package com.ocs.indaba.tag;

import com.ocs.indaba.service.AccessPermissionService;
import com.ocs.indaba.util.SpringContextUtil;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 *
 * @author luwb
 */
public class AccessTagHandler extends TagSupport{
    private static final Logger logger = Logger.getLogger(AccessTagHandler.class);

    /*attribute for access*/
    private int prjid = 0;
    private int uid = 0;
    private String right = "";
    private String tool = "";
    private String link = "";
    private String text = "";
    private String hint = "";
    private String style = "";
    private String clazz = "";

    private boolean isPermitted;

    @Override
    public int doStartTag(){
        AccessPermissionService accessPermissionService = (AccessPermissionService) SpringContextUtil.getBean("accessPermissionService");
        isPermitted = (tool == null)? accessPermissionService.isPermitted(prjid, uid, right) : accessPermissionService.isPermitted(prjid, uid, right, tool);
        JspWriter out = pageContext.getOut();
        try {
            String html;
            if(isPermitted){
                html = "<a href=\"" + link + "\"";
                if(!this.hint.equals(""))
                    html += " title=\"" + hint + "\"";
                if(!this.clazz.equals(""))
                    html += " class=\"" + clazz + "\"";
                if(!this.style.equals(""))
                    html += " style=\"" + style + "\"";
                html += ">";
            }else{
                html = "<span";
                if(!this.clazz.equals(""))
                    html += " class=\"" + clazz + "\"";
                if(!this.style.equals(""))
                    html += " style=\"" + style + "\"";
                html += ">";
            }
            if(!this.text.equals("")){//if has text attribute, ignore the body content
                html += text;
                out.print(html);
                return Tag.SKIP_BODY;
            }else{
                out.print(html);
                return Tag.EVAL_BODY_INCLUDE;
            }
        } catch (IOException ex) {
           logger.error("write to page error when doStartTag" + ex);
           return Tag.SKIP_PAGE;
        }
    }

    @Override
    public int doEndTag(){
        JspWriter out = pageContext.getOut();
        try {
            String html;
            if(isPermitted){
                html = "</a>";
            }else{
                html = "</span>";
            }
            out.print(html);
            return Tag.EVAL_PAGE;
        } catch (IOException ex) {
           logger.error("write to page error when doEndTag" + ex);
           return Tag.SKIP_PAGE;
        }
    }

    /**
     * @param right the right to set
     */
    public void setRight(Object right) {
        try {
            this.right = (String) ExpressionEvaluatorManager.evaluate("right", right.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set right value error" + ex);
        }
    }

    /**
     * @param tool the tool to set
     */
    public void setTool(Object tool) {
        try {
            this.tool = (String) ExpressionEvaluatorManager.evaluate("tool", tool.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set tool value error" + ex);
        }
    }

    /**
     * @param link the link to set
     */
    public void setLink(Object link) {
        try {
            this.link = (String) ExpressionEvaluatorManager.evaluate("link", link.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set link value error" + ex);
        }
    }

    /**
     * @param text the text to set
     */
    public void setText(Object text) {
        try {
            this.text = (String) ExpressionEvaluatorManager.evaluate("text", text.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set text value error" + ex);
        }
    }

    /**
     * @param hint the hint to set
     */
    public void setHint(Object hint) {
        try {
            this.hint = (String) ExpressionEvaluatorManager.evaluate("hint", hint.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set hint value error" + ex);
        }
    }

    /**
     * @param style the style to set
     */
    public void setStyle(Object style) {
        try {
            this.style = (String) ExpressionEvaluatorManager.evaluate("style", style.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set style value error" + ex);
        }
    }

    /**
     * @param clazz the clazz to set
     */
    public void setClass(Object clazz) {
       try {
            this.clazz = (String) ExpressionEvaluatorManager.evaluate("class", clazz.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set clazz value error" + ex);
        }
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
     * @param uid the uid to set
     */
    public void setUid(Object uid) {
        try {
            this.uid = (Integer) ExpressionEvaluatorManager.evaluate("uid", uid.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set uid value error" + ex);
        }
    }
}
