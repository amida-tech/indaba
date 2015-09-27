/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.tag;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.service.AccessPermissionService;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;
import org.drools.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff Jiang
 */
public class MessageTagHandler extends BodyTagSupport {

    private static final Logger logger = Logger.getLogger(MessageTagHandler.class);

    /*
     * attribute for access
     */
    private boolean debug = false;
    private String value = null;
    private String key = null;
    //private String pageid = null;
    private Object[] args = null;
    private List<Object> arguments = new ArrayList<Object>();

    @Override
    public int doStartTag() throws JspException {
        //AccessPermissionService accessPermissionService = (AccessPermissionService) SpringContextUtil.getBean("accessPermissionService");

        // ensure we have a key
        if (StringUtils.isEmpty(key)) {
            throw new JspTagException("indaba:msg tag requires a key attribute.");
        }
        // Reset the arguments
        if (arguments != null) {
            arguments.clear();
        }
        if (args != null) {
            arguments.addAll(Arrays.asList(args));
        }

        boolean needDisplay = true;
        return (needDisplay) ? Tag.EVAL_BODY_INCLUDE : Tag.SKIP_BODY;
    }

    /**
     * If key is not found, use the body content as a default value.
     */
    @Override
    public int doAfterBody() throws JspException {
        // if the value is null, use the body content
        if (value == null) {
            //value = bodyContent.getString();
        }
        // cleanup
        //bodyContent.clearBody();
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspTagException {
        try {
            String lang = (String) pageContext.getRequest().getAttribute(Constants.ATTR_LANG);

            int langId = Constants.LANG_EN;
            if (!StringUtils.isEmpty(lang)) {
                langId = Integer.parseInt(lang);
            }

            value = Messages.getInstance().getMessage(key, langId);
            //value = bundle.getString(key);
            if (debug) {
                logger.debug("indaba:msg tag's attributes: \n\t"
                        + "key=" + key + ", value=" + value + ", lang=" + lang + ", debug=" + debug + ", arguments=" + arguments);
            }

            // perform parameter substitutions
            if (value != null && arguments != null && !arguments.isEmpty()) {
                // reformat the value as specified
                value = value.replace("'", "''");
                value = MessageFormat.format(value, arguments.toArray());
            }

            if (value == null) {
                if (debug) {
                    logger.debug("indaba:msg: skipping null value for " + key);
                }
                this.pageContext.getOut().print(key);
            } else if (id != null) {
                // define the variable in the page context
                pageContext.setAttribute(id, value);
            } else {
                // print the value to the JspWriter
                this.pageContext.getOut().print(value);
            }
        } catch (java.io.IOException e) {
            throw new JspTagException("indaba:msg tag IO Error: " + e.getMessage());
        }
        if (debug) {
            logger.debug("########################### <indaba:msg> Tag End ###########################");
        }
        // only process the body once
        return Tag.EVAL_PAGE;

    }

    public void setBodyContent() {
    }

    /**
     * @param key the key to set
     */
    public void setKey(Object key) {
        try {
            this.key = (String) ExpressionEvaluatorManager.evaluate("key", key.toString(), String.class, this, pageContext);
        } catch (Exception ex) {
            logger.error("set key value error" + ex);
        }
    }

    /**
     * allows setting all the arguments at once
     */
    public final void setArgs(Object[] args) {
        this.args = args;
    }

    /**
     * adds to the list of arguments used when formatting the message
     */
    protected final void addArg(Object arg) {
        arguments.add(arg);
        if (debug) {
            logger.debug("<indaba:msg> added arg: " + arg.toString());
        }
    }

    /**
     * Turn debugging log messages on or off
     */
    public final void setDebug(boolean value) {
        debug = value;
    }

    /**
     * clears the argument list so that sub tags can call addArgument clears out
     * any previous key and value in case key is not provided, or the value is
     * null
     */
    public final void release() {
        super.release();
        if (arguments != null) {
            arguments.clear();
        }
        key = null;
        value = null;
        args = null;
        debug = false;
        arguments = null;
    }
}
