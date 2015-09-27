/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
*/

package com.ocs.indaba.util;

import com.ocs.indaba.po.Cases;
import com.ocs.indaba.vo.CaseInfo;
import com.ocs.indaba.vo.CaseStatus;


/**
 *
 * @author menglong
 */
public class ConvertUtils {
    public static CaseInfo convertToCaseInfo(Cases c) {
        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setCaseId(c.getId());
        caseInfo.setTitle(c.getTitle());

        CaseStatus caseStatus = CaseStatus.fromCaseStatusCode(c.getStatus());
        caseInfo.setStatus(caseStatus.getStatusDesc());
        //TODO:
        caseInfo.setPriority(null);
        caseInfo.setTags(null);
        caseInfo.setOwner(null);
        //caseInfo.setAttachedContent(null);

        //
        caseInfo.setOpenedTime(c.getOpenedTime());
        caseInfo.setDescription(c.getDescription());
        //caseInfo.setBlockWorkflow(c.getBlockWorkflow());
        //caseInfo.setBlockPublishing(c.getBlockPublishing());
        caseInfo.setProjectId(c.getProjectId());
        caseInfo.setProductId(c.getProductId());
        caseInfo.setHorseId(c.getHorseId());
        caseInfo.setGoalId(c.getGoalId());
        //caseInfo.setReason(c.getReason());
//        caseInfo.setMsgboardId(c.getUserMsgboardId());

        return caseInfo;

    }
}
