/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 *
 * @author jjiang
 */
public class BaseSurveyAnswerTagHandler extends BaseTagHandler {

    private static final Logger logger = Logger.getLogger(BaseSurveyAnswerTagHandler.class);
    //protected int questionId = 0;
    //if showAnswer is true, show the answers, but if can't find answer, display as showAnswer is false
    //protected boolean showAnswer = false;
    //protected int answerObjectId = 0;
    //if disabled is true, the radio/checkbox/textarea can't be edited
    protected boolean disabled = false;
    //if showDefault is true, show default selection
    //protected boolean showDefault = false;
    protected String clazz = "";
    protected String style = "";
    //protected String name = "";
    //protected String contents = "";

    @Override
    public int doEndTag() {
        return Tag.EVAL_PAGE;
    }

    /**
     * @param style the style to set
     */
    public void setStyle(Object style) {
        try {
            this.style = (String) ExpressionEvaluatorManager.evaluate("style", style.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set style value error", ex);
        }
    }

    /**
     * @param clazz the clazz to set
     */
    public void setClass(Object clazz) {
        try {
            this.clazz = (String) ExpressionEvaluatorManager.evaluate("class", clazz.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set class value error", ex);
        }
    }

    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(Object disabled) {
        try {
            this.disabled = (Boolean) ExpressionEvaluatorManager.evaluate("disabled", disabled.toString(), Boolean.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set disabled value error", ex);
        }
    }

}