/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.service;

import com.ocs.indaba.aggregation.vo.JournalSummaryInfo;
import com.ocs.indaba.builder.dao.JounalDataDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author rocky
 */
public class JournalSummaryService {

    private static final Logger log = Logger.getLogger(JournalSummaryService.class);
    private JounalDataDAO jounalDataDao = null;

    public JournalSummaryInfo selectJournalInfoByProductIdandProjectId(int productId, int projectId) {
        JournalSummaryInfo journalSummary = jounalDataDao.selectJournalInfoByProductIdandProjectId(productId, projectId);
        journalSummary.setExportDate(new Date());
        journalSummary.setTargetName((ArrayList)jounalDataDao.selectTargetNameListByProductI(productId));
        return journalSummary;
    }

    public JournalSummaryInfo selectJournalInfo(int productId, int projectId, List<Integer> targetIdList) {
        JournalSummaryInfo journalSummary = jounalDataDao.selectJournalInfoByProductIdandProjectId(productId, projectId);
        journalSummary.setTargetName(new ArrayList<String>());
        journalSummary.setExportDate(new Date());
        for (Integer targetId : targetIdList) {
            journalSummary.getTargetName().add(jounalDataDao.selectTargetNameByTargetId(targetId.intValue()));
        }
        return journalSummary;
    }

    public JournalSummaryInfo getJournalInfoByProductId(int productId) {
        JournalSummaryInfo journalSummary = jounalDataDao.selectJournalInfoByProductId(productId);
        journalSummary.setExportDate(new Date());
        journalSummary.setTargetName((ArrayList)jounalDataDao.selectTargetNameListByProductI(productId));
        return journalSummary;
    }

    @Autowired
    public void setJounalDataDao(JounalDataDAO jounalDataDao) {
        this.jounalDataDao = jounalDataDao;
    }
}
