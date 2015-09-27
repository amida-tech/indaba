/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.tag;

import com.ocs.indaba.survey.table.SurveyTableService;
import com.ocs.indaba.util.SpringContextUtil;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 *
 * @author jjiang
 */
public class TableSurveyAnswerTagHandler extends BaseSurveyAnswerTagHandler {

    private static final Logger logger = Logger.getLogger(TableSurveyAnswerTagHandler.class);
    private SurveyTableService surveyTableSrvc = (SurveyTableService) SpringContextUtil.getBean("surveyTableService");
    private int horseId = 0; // horse id
    private int mainQuestionId = 0; // main question id
    private String contents = "";

    @Override
    public int doStartTag() {
        JspWriter out = pageContext.getOut();
        try {
            boolean disableEdit = disabled;
            if (!contents.isEmpty() && contents.indexOf('B') == -1) {
                disableEdit = true;
            }

            String html = surveyTableSrvc.generateTableHtml(horseId, mainQuestionId, getUserId(), getLanguageId(), disableEdit);
            out.print(html);
            return Tag.SKIP_BODY;
        } catch (Exception ex) {
            logger.error("write to page error when doStartTag", ex);
            return Tag.SKIP_PAGE;
        }
    }

    /**
     *
     * @param horseId horse id
     */
    public void setHorseId(Object horseId) {
        try {
            this.horseId = (Integer) ExpressionEvaluatorManager.evaluate("horseId", horseId.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set answerTypeId value error", ex);
        }
    }

    /**
     *
     * @param questionId question id
     */
    public void setMainQuestionId(Object mainQuestionId) {
        try {
            this.mainQuestionId = (Integer) ExpressionEvaluatorManager.evaluate("mainQuestionId", mainQuestionId.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set answerTypeId value error", ex);
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