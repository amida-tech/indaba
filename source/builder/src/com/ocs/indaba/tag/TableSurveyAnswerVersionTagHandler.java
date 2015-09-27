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
public class TableSurveyAnswerVersionTagHandler extends BaseSurveyAnswerTagHandler {

    private static final Logger logger = Logger.getLogger(TableSurveyAnswerVersionTagHandler.class);
    private SurveyTableService surveyTableSrvc = (SurveyTableService) SpringContextUtil.getBean("surveyTableService");
    private int contentVersionId = 0; // content version id
    private int mainQuestionId = 0; // main question id

    @Override
    public int doStartTag() {
        JspWriter out = pageContext.getOut();
        try {
            String html = surveyTableSrvc.generateContentVersionTableHtml(contentVersionId, mainQuestionId, getLanguageId());
            out.print(html);
            return Tag.SKIP_BODY;
        } catch (Exception ex) {
            logger.error("write to page error when doStartTag", ex);
            return Tag.SKIP_PAGE;
        }
    }

    /**
     *
     * @param contentVersionId content version id
     */
    public void setContentVersionId(Object contentVersionId) {
        try {
            this.contentVersionId = (Integer) ExpressionEvaluatorManager.evaluate("contentVersionId", contentVersionId.toString(), Integer.class, this, pageContext);
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
}