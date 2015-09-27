package com.ocs.indaba.tag;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

public class AbbreviateStringHandler extends BaseTagHandler {

    /**
     *
     */
    private static final long serialVersionUID = -257924710839962479L;
    private static final Logger logger = Logger.getLogger(AbbreviateStringHandler.class);
    private String value;
    private int length;

    public void setValue(Object value) {
        try {
            if (value != null) {
                this.value = (String) ExpressionEvaluatorManager.evaluate("value", value.toString(), String.class, this, pageContext);
            } else {
                logger.warn("Name is empty.");
            }
        } catch (JspException ex) {
            logger.error("set right value error" + ex);
        }
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();

        try {
            out.print(StringUtils.abbreviate(value, 0, length));
        } catch (IOException e) {
            logger.error("Failed to truncate string due to: ", e);
        }
        return Tag.SKIP_BODY;
    }
}
