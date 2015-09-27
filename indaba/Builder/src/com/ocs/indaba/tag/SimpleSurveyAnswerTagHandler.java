/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.tag;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.AnswerTypeFloat;
import com.ocs.indaba.po.AnswerTypeInteger;
import com.ocs.indaba.po.AnswerTypeText;
import com.ocs.indaba.po.AtcChoice;
import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.util.SpringContextUtil;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 *
 * @author jjiang
 */
public class SimpleSurveyAnswerTagHandler extends BaseSurveyAnswerTagHandler {

    private static final Logger logger = Logger.getLogger(SimpleSurveyAnswerTagHandler.class);
    private SurveyService surveySrvc = (SurveyService) SpringContextUtil.getBean("surveyService");
    protected int answerType = 0;
    private int answerTypeId = 0;
    //if showAnswer is true, show the answers, but if can't find answer, display as showAnswer is false
    private boolean showAnswer = false;
    private int answerObjectId = 0;
    //if disabled is true, the radio/checkbox/textarea can't be edited
    //protected boolean disabled = false;
    //if showDefault is true, show default selection
    private boolean showDefault = false;
    private String name = "";
    private String contents = "";

    @Override
    public int doStartTag() {
        JspWriter out = pageContext.getOut();
        try {
            String html = "";
            switch (answerType) {
                case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
                    if (this.showAnswer) {
                        html = this.getSingleChoiceHtmlWithAnswer();
                    } else {
                        html = this.getSingleChoiceHtml();
                    }
                    break;
                case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
                    if (this.showAnswer) {
                        html = this.getMultiChoiceHtmlWithAnswer();
                    } else {
                        html = this.getMultiChoiceHtml();
                    }
                    break;
                case Constants.SURVEY_ANSWER_TYPE_FLOAT:
                    if (this.showAnswer) {
                        html = this.getFloatHtmlWithAnswer();
                    } else {
                        html = this.getFloatHtml();
                    }
                    break;
                case Constants.SURVEY_ANSWER_TYPE_INTEGER:
                    if (this.showAnswer) {
                        html = this.getIntegerHtmlWithAnswer();
                    } else {
                        html = this.getIntegerHtml();
                    }
                    break;
                case Constants.SURVEY_ANSWER_TYPE_TEXT:
                    if (this.showAnswer) {
                        html = this.getTextHtmlWithAnswer();
                    } else {
                        html = this.getTextHtml();
                    }
                    break;
                default:
                    logger.error("Unsupported answer type: " + answerType);
                    break;
            }
            out.print(html);
            return Tag.SKIP_BODY;
        } catch (Exception ex) {
            logger.error("write to page error when doStartTag", ex);
            return Tag.SKIP_PAGE;
        }
    }

    private String getSingleChoiceHtml() {
        // List<AtcChoice> choiceList = surveySrvc.getAnswerTypeChoice(answerTypeId);
        List<AtcChoice> choiceList = surveySrvc.getAnswerTypeChoice(answerTypeId, getLanguageId());

        if (choiceList == null || choiceList.isEmpty()) {
            return "";
        }
        String strDisabled = "";
        if (this.disabled) {
            strDisabled = "DISABLED";
        }
        if (!contents.isEmpty() && contents.indexOf('B') == -1) {
            strDisabled = "DISABLED";
        }

        String html = "<table>";
        for (AtcChoice choice : choiceList) {
            String td1;
            String criteria = choice.getCriteria();
            if (criteria == null) {
                criteria = "";
            }
            if (this.showDefault) {
                if (choice.getDefaultSelected()) {
                    td1 = "<td NOWRAP valign='top'><input type=\"radio\" name=\"" + name + "\" value=\""
                            + choice.getMask() + "\" " + strDisabled + " CHECKED onclick=\"resetStatus();\"></input>" + choice.getLabel() + "</td>";
                } else {
                    td1 = "<td NOWRAP valign='top'><input type=\"radio\" name=\"" + name + "\" value=\""
                            + choice.getMask() + "\" " + strDisabled + " onclick=\"resetStatus();\"></input>" + choice.getLabel() + "</td>";
                }
            } else {
                td1 = "<td NOWRAP><input type=\"radio\" name=\"" + name + "\" value=\""
                        + choice.getMask() + "\" " + strDisabled + " onclick=\"resetStatus();\"></input>" + choice.getLabel() + "</td>";
            }
            String td2 = "<td><b>" + choice.getLabel() + " Criteria</b><br/>" + criteria
                    + "</td>";
            String tr = "<tr>" + td1 + td2 + "</tr>";
            html += tr;
        }
        html += "</table>";
        return html;
    }

    private String getSingleChoiceHtmlWithAnswer() {
        if (this.answerObjectId <= 0) {
            return this.getSingleChoiceHtml();
        }
        // List<AtcChoice> choiceList = surveySrvc.getAnswerTypeChoice(answerTypeId);
        List<AtcChoice> choiceList = surveySrvc.getAnswerTypeChoice(answerTypeId, super.getLanguageId());

        if (choiceList == null || choiceList.isEmpty()) {
            return "";
        }

        long choiceResult = surveySrvc.getAnswerObjectChoice(answerObjectId).getChoices();
        String html = "<table>";
        String strDisabled = "";
        if (this.disabled) {
            strDisabled = "DISABLED";
        }
        if (!contents.isEmpty() && contents.indexOf('B') == -1) {
            strDisabled = "DISABLED";
        }

        for (AtcChoice choice : choiceList) {
            String td1;
            String criteria = choice.getCriteria();
            if (criteria == null) {
                criteria = "";
            }
            boolean isSelect = ((choiceResult & choice.getMask()) != 0);
            if (isSelect) {
                td1 = "<td NOWRAP valign='top'><input type=\"radio\" name=\"" + name + "\" value=\""
                        + choice.getMask() + "\"" + strDisabled + " CHECKED onclick=\"resetStatus();\"></input>" + choice.getLabel() + "</td>";
            } else {
                td1 = "<td NOWRAP valign='top'><input type=\"radio\" name=\"" + name + "\" value=\""
                        + choice.getMask() + "\"" + strDisabled + " onclick=\"resetStatus();\"></input>" + choice.getLabel() + "</td>";
            }
            String td2 = "<td><b>" + choice.getLabel() + " Criteria</b><br/>" + criteria
                    + "</td>";
            String tr = "<tr>" + td1 + td2 + "</tr>";
            html += tr;
        }
        html += "</table>";
        return html;
    }

    private String getMultiChoiceHtml() {
        List<AtcChoice> choiceList = surveySrvc.getAnswerTypeChoice(answerTypeId);
        if (choiceList == null || choiceList.isEmpty()) {
            return "";
        }
        String strDisabled = "";
        if (this.disabled) {
            strDisabled = "DISABLED";
        }
        if (!contents.isEmpty() && contents.indexOf('B') == -1) {
            strDisabled = "DISABLED";
        }

        String html = "<table>";
        int choiceId = 0;
        for (AtcChoice choice : choiceList) {
            choiceId++;
            String td1;
            String criteria = choice.getCriteria();
            if (criteria == null) {
                criteria = "";
            }
            if (this.showDefault) {
                if (choice.getDefaultSelected()) {
                    td1 = "<td NOWRAP valign='top'><label><input type=\"checkbox\" name=\"" + name + "\" value=\""
                            + choice.getMask() + "\" " + strDisabled + " checked=\"true\" onclick=\"resetStatus();\"></input>" + choice.getLabel() + "</label></td>";
                } else {
                    td1 = "<td NOWRAP valign='top'><label><input type=\"checkbox\" name=\"" + name + "\" value=\""
                            + choice.getMask() + "\" " + strDisabled + " onclick=\"resetStatus();\"></input>" + choice.getLabel() + "</label></td>";
                }
            } else {
                td1 = "<td NOWRAP valign='top'><label><input type=\"checkbox\" name=\"" + name + "\" value=\""
                        + choice.getMask() + "\" " + strDisabled + " onclick=\"resetStatus();\"></input>" + choice.getLabel() + "</label></td>";
            }
            String td2 = "<td><b>Choice " + choiceId + " Criteria</b><br/>" + criteria
                    + "</td>";
            String tr = "<tr>" + td1 + td2 + "</tr>";
            html += tr;
        }
        html += "</table>";
        return html;
    }

    private String getMultiChoiceHtmlWithAnswer() {
        if (this.answerObjectId <= 0) {
            return this.getMultiChoiceHtml();
        }

        List<AtcChoice> choiceList = surveySrvc.getAnswerTypeChoice(answerTypeId);
        if (choiceList == null || choiceList.isEmpty()) {
            return "";
        }

        long choiceResult = surveySrvc.getAnswerObjectChoice(answerObjectId).getChoices();
        String html = "<table>";
        String strDisabled = "";
        if (this.disabled) {
            strDisabled = "DISABLED";
        }
        if (!contents.isEmpty() && contents.indexOf('B') == -1) {
            strDisabled = "DISABLED";
        }

        int choiceId = 0;
        for (AtcChoice choice : choiceList) {
            choiceId++;
            boolean isSelect = ((choiceResult & choice.getMask()) != 0);
            String td1;
            String criteria = choice.getCriteria();
            if (criteria == null) {
                criteria = "";
            }
            if (isSelect) {
                td1 = "<td NOWRAP valign='top'><label><input type=\"checkbox\" name=\"" + name + "\" value=\""
                        + choice.getMask() + "\" " + strDisabled + " onclick=\"resetStatus();\" checked=\"true\"></input>" + choice.getLabel() + "</label></td>";
            } else {
                td1 = "<td NOWRAP valign='top'><label><input type=\"checkbox\" name=\"" + name + "\" value=\""
                        + choice.getMask() + "\" " + strDisabled + " onclick=\"resetStatus();\"></input>" + choice.getLabel() + "</label></td>";
            }
            String td2 = "<td><b>Choice " + choiceId + " Criteria</b><br/>" + criteria
                    + "</td>";
            String tr = "<tr>" + td1 + td2 + "</tr>";
            html += tr;
        }
        html += "</table>";
        return html;
    }

    private String getFloatHtml() {
        AnswerTypeFloat aFloat = surveySrvc.getAnswerTypeFloat(answerTypeId);
        String strDisabled = "";
        if (this.disabled) {
            strDisabled = "DISABLED";
        }
        if (!contents.isEmpty() && contents.indexOf('B') == -1) {
            strDisabled = "DISABLED";
        }

        String html = getI18nMessage(Messages.KEY_COMMON_ALT_ANSWER) + ": <input type=\"text\" class=\"text\" name=\"" + name + "\" value=\"\" " + strDisabled + " onclick=\"resetStatus();\"></input> ("
                + aFloat.getMinValue() + "-" + aFloat.getMaxValue() + ")";
        return html;
    }

    private String getFloatHtmlWithAnswer() {
        if (this.answerObjectId <= 0) {
            return this.getFloatHtml();
        }

        AnswerTypeFloat aFloat = surveySrvc.getAnswerTypeFloat(answerTypeId);
        float fAnswer = surveySrvc.getAnswerObjectFloat(answerObjectId).getValue();
        String answer = String.valueOf(fAnswer);
        String strDisabled = "";
        if (this.disabled) {
            strDisabled = "DISABLED";
        }
        if (!contents.isEmpty() && contents.indexOf('B') == -1) {
            strDisabled = "DISABLED";
        }

        String html = getI18nMessage(Messages.KEY_COMMON_ALT_ANSWER) + ": <input type=\"text\" class=\"text\" name=\"" + name + "\" value=\"" + answer + "\" " + strDisabled + " onclick=\"resetStatus();\"></input> ("
                + aFloat.getMinValue() + "-" + aFloat.getMaxValue() + ")";
        return html;
    }

    private String getIntegerHtml() {
        AnswerTypeInteger aInt = surveySrvc.getAnswerTypeInteger(answerTypeId);
        String strDisabled = "";
        if (this.disabled) {
            strDisabled = "DISABLED";
        }
        if (!contents.isEmpty() && contents.indexOf('B') == -1) {
            strDisabled = "DISABLED";
        }

        String html = getI18nMessage(Messages.KEY_COMMON_ALT_ANSWER) + ": <input type=\"text\" class=\"text\" name=\"" + name + "\" value=\"\" " + strDisabled + " onclick=\"resetStatus();\"></input> ("
                + aInt.getMinValue() + "-" + aInt.getMaxValue() + ")";
        return html;
    }

    private String getIntegerHtmlWithAnswer() {
        if (this.answerObjectId <= 0) {
            return this.getIntegerHtml();
        }

        AnswerTypeInteger aInt = surveySrvc.getAnswerTypeInteger(answerTypeId);
        int iAnswer = surveySrvc.getAnswerObjectInteger(answerObjectId).getValue();
        String answer = String.valueOf(iAnswer);
        String strDisabled = "";
        if (this.disabled) {
            strDisabled = "DISABLED";
        }
        if (!contents.isEmpty() && contents.indexOf('B') == -1) {
            strDisabled = "DISABLED";
        }

        String html = getI18nMessage(Messages.KEY_COMMON_ALT_ANSWER) + ": <input type=\"text\" class=\"text\" name=\"" + name + "\" value=\"" + answer + "\" " + strDisabled + " onclick=\"resetStatus();\"></input> ("
                + aInt.getMinValue() + "-" + aInt.getMaxValue() + ")";
        return html;
    }

    private String getTextHtml() {
        AnswerTypeText text = surveySrvc.getAnswerTypeText(answerTypeId);
        String strDisabled = "";
        if (this.disabled) {
            strDisabled = "DISABLED";
        }
        if (!contents.isEmpty() && contents.indexOf('B') == -1) {
            strDisabled = "DISABLED";
        }

        String html = "<TEXTAREA NAME=\"" + name + "\" class=\"text\" rows=\"6\" " + strDisabled + " onclick=\"resetStatus();\"></TEXTAREA>";
        return html;
    }

    private String getTextHtmlWithAnswer() {
        if (this.answerObjectId <= 0) {
            return this.getTextHtml();
        }

        AnswerTypeText text = surveySrvc.getAnswerTypeText(answerTypeId);
        String answer = surveySrvc.getAnswerObjectText(answerObjectId).getValue();
        String strDisabled = "";
        if (this.disabled) {
            strDisabled = "DISABLED";
        }
        if (!contents.isEmpty() && contents.indexOf('B') == -1) {
            strDisabled = "DISABLED";
        }

        String html = "<TEXTAREA NAME=\"" + name + "\"  class=\"text\" rows=\"6\" " + strDisabled + " onclick=\"resetStatus();\">" + answer + "</TEXTAREA>";
        return html;
    }

    /**
     * @param answerType the answerType to set
     */
    public void setAnswerType(Object answerType) {
        try {
            this.answerType = (Integer) ExpressionEvaluatorManager.evaluate("answerType", answerType.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set answerType value error", ex);
        }
    }

    /**
     * @param answerTypeId the answerTypeId to set
     */
    public void setAnswerTypeId(Object answerTypeId) {
        try {
            this.answerTypeId = (Integer) ExpressionEvaluatorManager.evaluate("answerTypeId", answerTypeId.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set answerTypeId value error", ex);
        }
    }

    /**
     * @param name the name to set
     */
    public void setName(Object name) {
        try {
            this.name = (String) ExpressionEvaluatorManager.evaluate("name", name.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set name value error", ex);
        }
    }

    /**
     * @param showAnswer the showAnswer to set
     */
    public void setShowAnswer(Object showAnswer) {
        try {
            this.showAnswer = (Boolean) ExpressionEvaluatorManager.evaluate("showAnswer", showAnswer.toString(), Boolean.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set showAnswer value error", ex);
        }
    }

    /**
     * @param answerObjectId the answerObjectId to set
     */
    public void setAnswerObjectId(Object answerObjectId) {
        try {
            this.answerObjectId = (Integer) ExpressionEvaluatorManager.evaluate("answerObjectId", answerObjectId.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set name value error", ex);
        }
    }

    /**
     * @param showDefault the showDefault to set
     */
    public void setShowDefault(Object showDefault) {
        try {
            this.showDefault = (Boolean) ExpressionEvaluatorManager.evaluate("showDefault", showDefault.toString(), Boolean.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set showDefault value error", ex);
        }
    }

    public void setContents(Object contents) {
        try {
            this.contents = (String) ExpressionEvaluatorManager.evaluate("contents", contents.toString(), String.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set contents value error", ex);
        }
    }
}