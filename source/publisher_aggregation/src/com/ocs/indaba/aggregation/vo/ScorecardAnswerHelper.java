/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.vo;

import com.ocs.indaba.aggregation.po.ScorecardAnswer;
import com.ocs.indaba.aggregation.po.ScorecardAnswerA;
import com.ocs.indaba.aggregation.po.ScorecardAnswerB;

/**
 *
 * @author jiangjeff
 */
public class ScorecardAnswerHelper {
    public static ScorecardAnswerA sca2scaA(ScorecardAnswer sca) {
        if(sca == null) {
            return null;
        }
        ScorecardAnswerA scaA = new ScorecardAnswerA();
        scaA.setDataType(sca.getDataType());
        scaA.setId(sca.getId());
        scaA.setIndicatorId(sca.getIndicatorId());
        scaA.setQuestionId(sca.getQuestionId());
        scaA.setScore(sca.getScore());
        scaA.setScorecardId(sca.getScorecardId());
        scaA.setValue(sca.getValue());
        return scaA;
    }
    
    
    public static ScorecardAnswerB sca2scaB(ScorecardAnswer sca) {
        if(sca == null) {
            return null;
        }
        ScorecardAnswerB scaB = new ScorecardAnswerB();
        scaB.setDataType(sca.getDataType());
        scaB.setId(sca.getId());
        scaB.setIndicatorId(sca.getIndicatorId());
        scaB.setQuestionId(sca.getQuestionId());
        scaB.setScore(sca.getScore());
        scaB.setScorecardId(sca.getScorecardId());
        scaB.setValue(sca.getValue());
        return scaB;
    }

    public static ScorecardAnswer scaA2sca(ScorecardAnswerA scaA) {
        if(scaA == null) {
            return null;
        }
        ScorecardAnswer sca = new ScorecardAnswer();
        sca.setDataType(scaA.getDataType());
        sca.setId(scaA.getId());
        sca.setIndicatorId(scaA.getIndicatorId());
        sca.setQuestionId(scaA.getQuestionId());
        sca.setScore(scaA.getScore());
        sca.setScorecardId(scaA.getScorecardId());
        sca.setValue(scaA.getValue());
        return sca;
    }
    
    public static ScorecardAnswer scaB2sca(ScorecardAnswerB scaB) {
        if(scaB == null) {
            return null;
        }
        ScorecardAnswer sca = new ScorecardAnswer();
        sca.setDataType(scaB.getDataType());
        sca.setId(scaB.getId());
        sca.setIndicatorId(scaB.getIndicatorId());
        sca.setQuestionId(scaB.getQuestionId());
        sca.setScore(scaB.getScore());
        sca.setScorecardId(scaB.getScorecardId());
        sca.setValue(scaB.getValue());
        return sca;
    }
}
