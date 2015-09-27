/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff Jiang
 */
public class MessageArgumentTagHandler extends TagSupport {

    private static final Logger logger = Logger.getLogger(MessageArgumentTagHandler.class);

    /**
     * locate the parent tag and add the argument to the Message's arg list
     */
    public void setValue(Object argumentValue) throws JspException {
        // Get the parent MessageTag
        MessageTagHandler messageTag = null;
        try {
            messageTag = (MessageTagHandler) this.getParent();
        } catch (ClassCastException e) {
            logger.error("Error Occus!", e);
        }

        if (messageTag == null) {
            throw new JspTagException(
                    "<i18n:arg> tag must be nested inside a message tag.");
        }

        // now we know we're safe to add the argument
        messageTag.addArg(argumentValue);
    }
}
