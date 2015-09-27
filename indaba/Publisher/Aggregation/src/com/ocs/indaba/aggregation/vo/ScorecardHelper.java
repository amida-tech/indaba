/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.vo;

import com.ocs.indaba.aggregation.po.Scorecard;
import com.ocs.indaba.aggregation.po.ScorecardA;
import com.ocs.indaba.aggregation.po.ScorecardB;

/**
 *
 * @author jiangjeff
 */
public class ScorecardHelper {
    
    public static ScorecardA sc2ScA(Scorecard sc) {
        if(sc == null) {
            return null;
        }
        ScorecardA scA = new ScorecardA();
        scA.setContentHeaderId(sc.getContentHeaderId());
        scA.setId(sc.getId());
        scA.setLastUpdateTime(sc.getLastUpdateTime());
        scA.setOrgId(sc.getOrgId());
        scA.setProductId(sc.getProductId());
        scA.setProjectId(sc.getProjectId());
        scA.setScorecardId(sc.getScorecardId());
        scA.setStatus(sc.getStatus());
        scA.setStudyPeriodId(sc.getStudyPeriodId());
        scA.setSurveyConfigId(sc.getSurveyConfigId());
        scA.setTargetId(sc.getTargetId());
        return scA;
    } 
    
    
    public static ScorecardB sc2ScB(Scorecard sc) {
        if(sc == null) {
            return null;
        }
        ScorecardB scB = new ScorecardB();
        scB.setContentHeaderId(sc.getContentHeaderId());
        scB.setId(sc.getId());
        scB.setLastUpdateTime(sc.getLastUpdateTime());
        scB.setOrgId(sc.getOrgId());
        scB.setProductId(sc.getProductId());
        scB.setProjectId(sc.getProjectId());
        scB.setScorecardId(sc.getScorecardId());
        scB.setStatus(sc.getStatus());
        scB.setStudyPeriodId(sc.getStudyPeriodId());
        scB.setSurveyConfigId(sc.getSurveyConfigId());
        scB.setTargetId(sc.getTargetId());
        return scB;
    } 
    
    public static Scorecard scA2Sc(ScorecardA scA) {
        if(scA == null) {
            return null;
        }
        Scorecard sc = new Scorecard();
        sc.setContentHeaderId(scA.getContentHeaderId());
        sc.setId(scA.getId());
        sc.setLastUpdateTime(scA.getLastUpdateTime());
        sc.setOrgId(scA.getOrgId());
        sc.setProductId(scA.getProductId());
        sc.setProjectId(scA.getProjectId());
        sc.setScorecardId(scA.getScorecardId());
        sc.setStatus(scA.getStatus());
        sc.setStudyPeriodId(scA.getStudyPeriodId());
        sc.setSurveyConfigId(scA.getSurveyConfigId());
        sc.setTargetId(scA.getTargetId());
        return sc;
    } 
    
    public static Scorecard scB2Sc(ScorecardB scB) {
        if(scB == null) {
            return null;
        }
        Scorecard sc = new Scorecard();
        sc.setContentHeaderId(scB.getContentHeaderId());
        sc.setId(scB.getId());
        sc.setLastUpdateTime(scB.getLastUpdateTime());
        sc.setOrgId(scB.getOrgId());
        sc.setProductId(scB.getProductId());
        sc.setProjectId(scB.getProjectId());
        sc.setScorecardId(scB.getScorecardId());
        sc.setStatus(scB.getStatus());
        sc.setStudyPeriodId(scB.getStudyPeriodId());
        sc.setSurveyConfigId(scB.getSurveyConfigId());
        sc.setTargetId(scB.getTargetId());
        return sc;
    } 
}
