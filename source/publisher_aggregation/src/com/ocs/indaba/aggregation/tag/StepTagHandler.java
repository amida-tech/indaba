/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.tag;

import com.ocs.indaba.tag.AccessTagHandler;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 *
 * @author jiangjeff
 */
public class StepTagHandler extends TagSupport {

    private static final Logger logger = Logger.getLogger(AccessTagHandler.class);

    /*attribute for access*/
    private String name = null;
    private int steps = 0;
    private int current = 0;
    private String link = "";
    private String quit = "";

    @Override
    public int doStartTag() {
        logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        JspWriter out = pageContext.getOut();
        StringBuilder sb = new StringBuilder();
        /*
        sb.append("<div class=\"steps\">");
        sb.append("<span class=\"name\">Create a workset</span>");
        for (int i = 1; i <= steps; ++i)  {
        if(i < current) {
        sb.append("<span class=\"done\">").append(i).append("</span>");
        } else if(i == current) {
        sb.append("<span class=\"current\">").append(i).append("</span>");
        } else {
        sb.append("<span>").append(i).append("</span>");
        }
        if(i != steps) {
        sb.append(" - ");
        }
        }
        sb.append("</div>");
         */

        String stepLink = this.link + "?step=";
        sb.append("<div class=\"steps\">");
        sb.append("<span class=\"name\">").append(name).append("</span>");
        for (int i = 1; i <= steps; ++i)  {
            if(i < current) {
                sb.append("<span class=\"done\">").append("<a href='").append(stepLink).append(i).append("' >").append(i).append("</a></span>");
            } else if(i == current) {
                sb.append("<span class=\"current\">").append(i).append("</span>");
            } else {
                sb.append("<span>").append("<a href='").append(stepLink).append(i).append("' >").append(i).append("</a></span>");
            }
            if(i != steps) {
                sb.append(" - ");
            }
        }
        sb.append("<span style='text-align: right; margin-left: 500px'>").append(quit).append("</span>");
        sb.append("</div>");
        try {
            out.print(sb.toString());
            return Tag.SKIP_BODY;
        } catch (IOException ex) {
            logger.error("write to page error when doStartTag" + ex);
            return Tag.SKIP_PAGE;
        }
    }

    @Override
    public int doEndTag() {
        return Tag.EVAL_PAGE;
    }

    /**
     * @param right the right to set
     */
    public void setName(Object name) {
        try {
            this.name = (String) ExpressionEvaluatorManager.evaluate("name", name.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set right value error" + ex);
        }
    }

    /**
     * @param prjid the prjid to set
     */
    public void setSteps(Object steps) {
        try {
            this.steps = (Integer) ExpressionEvaluatorManager.evaluate("steps", steps.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set prjid value error" + ex);
        }
    }

    /**
     * @param uid the uid to set
     */
    public void setCurrent(Object current) {
        try {
            this.current = (Integer) ExpressionEvaluatorManager.evaluate("current", current.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set uid value error" + ex);
        }
    }

    /**
     * @param uid the uid to set
     */
    public void setLink(Object link) {
        try {
            this.link = (String) ExpressionEvaluatorManager.evaluate("link", link.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set uid value error" + ex);
        }
    }

    /**
     * @param quit the quit link to set
     */
    public void setQuit(Object quit) {
        try {
            this.quit = (String) ExpressionEvaluatorManager.evaluate("quit", quit.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set quit value error" + ex);
        }
    }
}
