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
public class TableSurveyAnswerPeerReviewTagHandler extends BaseSurveyAnswerTagHandler {

    private static final Logger logger = Logger.getLogger(TableSurveyAnswerPeerReviewTagHandler.class);
    private SurveyTableService surveyTableSrvc = (SurveyTableService) SpringContextUtil.getBean("surveyTableService");
    protected int peerReviewId = 0; // horse id
    private int mainQuestionId = 0; // main question id

    @Override
    public int doStartTag() {
        JspWriter out = pageContext.getOut();
        try {
            String html = surveyTableSrvc.generatePeerReviewTableHtml(peerReviewId, mainQuestionId, getLanguageId(), disabled);
            out.print(html);
            return Tag.SKIP_BODY;
        } catch (Exception ex) {
            logger.error("write to page error when doStartTag", ex);
            return Tag.SKIP_PAGE;
        }
    }

    /**
     *
     * @param peerReviewId peer review id
     */
    public void setPeerReviewId(Object peerReviewId) {
        try {
            this.peerReviewId = (Integer) ExpressionEvaluatorManager.evaluate("peerReviewId", peerReviewId.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set peerReviewId value error", ex);
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