/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.service;

import com.ocs.indaba.aggregation.dao.PubOrganizationDAO;
import com.ocs.indaba.aggregation.dao.WorksetDAO;
import com.ocs.indaba.aggregation.vo.DataForm;
import com.ocs.indaba.aggregation.vo.DataSummaryInfo;
import com.ocs.indaba.builder.dao.StudyPeriodDAO;
import com.ocs.indaba.builder.dao.TargetDAO;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeanbone
 */
public class DataSummaryService {
    private WorksetDAO worksetDao;
    private PubOrganizationDAO orgDao;
    private StudyPeriodDAO studyPeriodDao;
    private TargetDAO targetDao;

    public DataSummaryInfo getDataSummaryInfoByDataForm(DataForm dataForm) {
        DataSummaryInfo dataSummary = new DataSummaryInfo();
        dataSummary.setWorksetName(worksetDao.get(dataForm.getWorksetId()).getName());
        dataSummary.setOrgName(orgDao.selectOrganizationByWorksetId(dataForm.getWorksetId()).getName());
        dataSummary.setStudyPeriods(studyPeriodDao.selectStudyPeriodsByIds(dataForm.getStudyPeriodIds()));
        dataSummary.setTargets(targetDao.selectTargetsByIds(dataForm.getTargetIds()));
        dataSummary.setExportDate(new Date());
        return dataSummary;
    }

    @Autowired
    public void setWorksetDao(WorksetDAO worksetDao) {
        this.worksetDao = worksetDao;
    }

    @Autowired
    public void setOrganizationDao(PubOrganizationDAO orgDao) {
        this.orgDao = orgDao;
    }
    
    @Autowired
    public void setStudyPeriodDao(StudyPeriodDAO studyPeriodDao) {
        this.studyPeriodDao = studyPeriodDao;
    }
    
    @Autowired
    public void setTargetDao(TargetDAO targetDao) {
        this.targetDao = targetDao;
    }
}
