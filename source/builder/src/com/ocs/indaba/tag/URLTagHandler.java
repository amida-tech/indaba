/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.tag;

import com.ocs.util.StringUtils;
import java.net.URLDecoder;
import java.net.URLEncoder;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 *
 * @author luwb
 */
public class URLTagHandler extends BaseTagHandler {

    private static final Logger logger = Logger.getLogger(URLTagHandler.class);
    private static final String ACTION_ENCODE = "encode";
    private static final String ACTION_DECODE = "decode";
    private String action = "";
    private String value = "";

    @Override
    public int doStartTag() {
        JspWriter out = pageContext.getOut();
        if (StringUtils.isEmpty(value)) {
            value = this.getBodyContent().getString();
        }
        try {
            if (ACTION_DECODE.equals(action)) {
                out.print(URLDecoder.decode(value, "UTF-8"));
            } else {
                out.print(URLEncoder.encode(value, "UTF-8"));
            }
        } catch (Exception ex) {
            logger.error("write to page error when doStartTag" + ex);
        }
        return Tag.SKIP_PAGE;
    }

    @Override
    public int doEndTag() {
        return Tag.EVAL_PAGE;
    }

    /**
     * @param action the answerType to set
     */
    public void setAction(Object action) {
        try {
            this.action = (String) ExpressionEvaluatorManager.evaluate("action", action.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set answerType value error" + ex);
        }
    }

    /**
     * @param value the answerTypeId to set
     */
    public void setValue(Object value) {
        try {
            this.value = (String) ExpressionEvaluatorManager.evaluate("value", value.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set answerTypeId value error" + ex);
        }
    }
}
