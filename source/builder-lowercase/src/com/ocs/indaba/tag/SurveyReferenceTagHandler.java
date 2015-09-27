/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.tag;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.ReferenceChoice;
import com.ocs.indaba.service.ReferenceService;
import com.ocs.indaba.util.SpringContextUtil;
import java.util.List;
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
public class SurveyReferenceTagHandler extends TagSupport {

    private static final Logger logger = Logger.getLogger(SurveyReferenceTagHandler.class);
    private static final String HTML_TAG_SOURCES_DESC = "<b>Description of Sources:</b><br/>";
    private int referenceType = 0;
    private int referenceId = 0;
    private boolean showAnswer = false;
    private int referenceObjectId = 0;
    private boolean disabled = false;
    private String name = "";
    private String contents = "";
    private ReferenceService service = null;

    @Override
    public int doStartTag() {
        service = (ReferenceService) SpringContextUtil.getBean("referenceService");
        logger.debug(" ########################### <indaba:source> Tag Start ###########################");
        JspWriter out = pageContext.getOut();
        try {
            String content = "";
            switch (this.referenceType) {
                case Constants.REFERENCE_TYPE_NO_CHOICE:
                    content = this.getTextHtmlWithAnswer();
                    break;
                case Constants.REFERENCE_TYPE_SINGLE_CHOICE:
                    if (this.showAnswer) {
                        content = this.getSingleChoiceHtmlWithAnswer()
                                + HTML_TAG_SOURCES_DESC
                                + this.getTextHtmlWithAnswer();
                    } else {
                        content = this.getSingleChoiceHtml()
                                + HTML_TAG_SOURCES_DESC
                                + this.getTextHtml();
                    }
                    break;
                case Constants.REFERENCE_TYPE_MULTI_CHOICE:
                    if (this.showAnswer) {
                        content = this.getMultiChoiceHtmlWithAnswer()
                                + HTML_TAG_SOURCES_DESC
                                + this.getTextHtmlWithAnswer();
                    } else {
                        content = this.getMultiChoiceHtml()
                                + HTML_TAG_SOURCES_DESC
                                + this.getTextHtml();
                    }
                    break;
                default:
                    break;
            }
            out.print(content);
            return Tag.SKIP_BODY;
        } catch (Exception ex) {
            logger.error("write to page error when doStartTag" + ex);
            return Tag.SKIP_PAGE;
        }
    }

    @Override
    public int doEndTag() {
        logger.debug(" ########################### <indaba:source> Tag End ###########################");
        return Tag.EVAL_PAGE;
    }

    private String getSingleChoiceHtml() {
        List<ReferenceChoice> choiceList = service.getReferenceChoice(referenceId);
        if (choiceList == null || choiceList.isEmpty()) {
            return "";
        }
        String content = "<table>";
        String strDisabled = "";
        if (this.disabled) {
            strDisabled = "DISABLED";
        }
        if (!contents.isEmpty() && contents.indexOf('C') == -1) {
            strDisabled = "DISABLED";
        }

        for (ReferenceChoice choice : choiceList) {
            String td1 = "<td><input type=\"radio\" name=\"" + name + "\" value=\""
                    + choice.getMask() + "\" " + strDisabled + " onclick='resetStatus();'></input></td>";
            String td2 = "<td>" + choice.getLabel() + "</td>";
            String tr = "<tr>" + td1 + td2 + "</tr>";
            content += tr;
        }
        content += "</table>";
        return content;
    }

    private String getSingleChoiceHtmlWithAnswer() {
        if (this.referenceObjectId <= 0) {
            return this.getSingleChoiceHtml();
        }

        List<ReferenceChoice> choiceList = service.getReferenceChoice(referenceId);
        if (choiceList == null || choiceList.isEmpty()) {
            return "";
        }

        long choiceResult = service.getReferenceObject(referenceObjectId).getChoices();

        String content = "<table>";
        String strDisabled = "";
        if (this.disabled) {
            strDisabled = "DISABLED";
        }
        if (!contents.isEmpty() && contents.indexOf('C') == -1) {
            strDisabled = "DISABLED";
        }
        
        for (ReferenceChoice choice : choiceList) {
            String td1;
            boolean isSelect = ((choiceResult & choice.getMask()) != 0);
            if (isSelect) {
                td1 = "<td><input type=\"radio\" name=\"" + name + "\" value=\""
                        + choice.getMask() + "\" " + strDisabled + " onclick='resetStatus();' CHECKED></input></td>";
            } else {
                td1 = "<td><input type=\"radio\" name=\"" + name + "\" value=\""
                        + choice.getMask() + "\" " + strDisabled + " onclick='resetStatus();'></input></td>";
            }
            String td2 = "<td>" + choice.getLabel() + "</td>";
            String tr = "<tr>" + td1 + td2 + "</tr>";
            content += tr;
        }
        content += "</table>";
        return content;
    }

    private String getMultiChoiceHtml() {
        List<ReferenceChoice> choiceList = service.getReferenceChoice(referenceId);
        if (choiceList == null || choiceList.isEmpty()) {
            return "";
        }
        String content = "<table>";
        String strDisabled = "";
        if (this.disabled) {
            strDisabled = "DISABLED";
        }
        if (!contents.isEmpty() && contents.indexOf('C') == -1) {
            strDisabled = "DISABLED";
        }
        
        for (ReferenceChoice choice : choiceList) {
            String td1 = "<td><input type=\"checkbox\" name=\"" + name + "\" value=\""
                    + choice.getMask() + "\" " + strDisabled + " onclick='resetStatus();'></input></td>";
            String td2 = "<td>" + choice.getLabel() + "</td>";
            String tr = "<tr>" + td1 + td2 + "</tr>";
            content += tr;
        }
        content += "</table>";
        return content;
    }

    private String getMultiChoiceHtmlWithAnswer() {
        if (this.referenceObjectId <= 0) {
            return this.getMultiChoiceHtml();
        }

        List<ReferenceChoice> choiceList = service.getReferenceChoice(referenceId);
        if (choiceList == null || choiceList.isEmpty()) {
            return "";
        }

        long choiceResult = service.getReferenceObject(referenceObjectId).getChoices();

        String content = "<table>";
        String strDisabled = "";
        if (this.disabled) {
            strDisabled = "DISABLED";
        }
        if (!contents.isEmpty() && contents.indexOf('C') == -1) {
            strDisabled = "DISABLED";
        }
        
        for (ReferenceChoice choice : choiceList) {
            String td1;
            boolean isSelect = ((choiceResult & choice.getMask()) != 0);
            if (isSelect) {
                td1 = "<td><input type=\"checkbox\" name=\"" + name + "\" value=\""
                        + choice.getMask() + "\" " + strDisabled + " onclick='resetStatus();' checked=\"true\"></input></td>";
            } else {
                td1 = "<td><input type=\"checkbox\" name=\"" + name + "\" value=\""
                        + choice.getMask() + "\" " + strDisabled + " onclick='resetStatus();'></input></td>";
            }
            String td2 = "<td>" + choice.getLabel() + "</td>";
            String tr = "<tr>" + td1 + td2 + "</tr>";
            content += tr;
            logger.debug(choice);
        }
        content += "</table>";
        return content;
    }

    private String getTextHtml() {
        String strDisabled = "";
        if (this.disabled) {
            strDisabled = "READONLY";
        }
        if (!contents.isEmpty() && contents.indexOf('C') == -1) {
            strDisabled = "READONLY";
        }
        
        String content = "<TEXTAREA NAME=\"" + name + "Desc\" class=\"text\" rows=\"6\" " + strDisabled + " onclick='resetStatus();'></TEXTAREA>";
        return content;
    }

    private String getTextHtmlWithAnswer() {
        if (this.referenceObjectId <= 0) {
            return this.getTextHtml();
        }
        String value = service.getReferenceObject(referenceObjectId).getSourceDescription();

        String strDisabled = "";
        if (this.disabled) {
            strDisabled = "READONLY";
        }
        if (!contents.isEmpty() && contents.indexOf('C') == -1) {
            strDisabled = "READONLY";
        }
        
        String content = "<TEXTAREA NAME=\"" + name + "Desc\" class=\"text\" rows=\"6\" " + strDisabled + " onclick='resetStatus();'>" + value + "</TEXTAREA>";
        return content;
    }
    /*
     * private String getTextSourceWithAnswer() { String value = ""; if
     * (this.referenceObjectId > 0) { value =
     * service.getReferenceObject(referenceObjectId).getSourceDescription(); }
     * String strDisabled = ""; if (this.disabled) { strDisabled = "DISABLED"; }
     * String content = "<TEXTAREA NAME=\"" + name + "\" class=\"text\" rows=\"6\" " +
     * strDisabled + " onclick='resetStatus();'>" + value + "</TEXTAREA>";
     * return content; }
     */

    /**
     * @param referenceId the referenceId to set
     */
    public void setReferenceId(Object referenceId) {
        try {
            this.referenceId = (Integer) ExpressionEvaluatorManager.evaluate("referenceId", referenceId.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set referenceId value error" + ex);
        }
    }

    /**
     * @param name the name to set
     */
    public void setName(Object name) {
        try {
            this.name = (String) ExpressionEvaluatorManager.evaluate("name", name.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set name value error" + ex);
        }
    }

    /**
     * @param referenceType the referenceType to set
     */
    public void setReferenceType(Object referenceType) {
        try {
            this.referenceType = (Integer) ExpressionEvaluatorManager.evaluate("referenceType", referenceType.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set referenceId value error" + ex);
        }
    }

    /**
     * @param showAnswer the showAnswer to set
     */
    public void setShowAnswer(Object showAnswer) {
        try {
            this.showAnswer = (Boolean) ExpressionEvaluatorManager.evaluate("showAnswer", showAnswer.toString(), Boolean.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set referenceId value error" + ex);
        }
    }

    /**
     * @param referenceObjectId the referenceObjectId to set
     */
    public void setReferenceObjectId(Object referenceObjectId) {
        try {
            this.referenceObjectId = (Integer) ExpressionEvaluatorManager.evaluate("referenceObjectId", referenceObjectId.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set referenceId value error" + ex);
        }
    }

    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(Object disabled) {
        try {
            this.disabled = (Boolean) ExpressionEvaluatorManager.evaluate("disabled", disabled.toString(), Boolean.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set disabled value error" + ex);
        }
    }
    
    public void setContents(Object contents) {
        try {
            this.contents = (String) ExpressionEvaluatorManager.evaluate("contents", contents.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set contents value error" + ex);
        }
    }

    @Override
    public String toString() {
        return "SurveyReferenceTagHandler{" + "referenceType=" + referenceType + ", referenceId=" + referenceId + ", showAnswer=" + showAnswer + ", referenceObjectId=" + referenceObjectId + ", disabled=" + disabled + ", name=" + name + '}';
    }
}
