/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.survey.table;

import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.vo.SurveyAnswerObject;

/**
 *
 * @author yc06x
 */
public class IndicatorInfo {

    private SurveyIndicator indicator;
    private Object answerTypeObj;
    private SurveyAnswerObject answerObj;

    public void setIndicator(SurveyIndicator value) {
        this.indicator = value;
    }

    public SurveyIndicator getIndicator() {
        return this.indicator;
    }

    public void setAnswerTypeObject(Object value) {
        this.answerTypeObj = value;
    }

    public Object getAnswerTypeObject() {
        return this.answerTypeObj;
    }

    public void setAnswerObject(SurveyAnswerObject value) {
        this.answerObj = value;
    }

    public SurveyAnswerObject getAnswerObject() {
        return this.answerObj;
    }
}
