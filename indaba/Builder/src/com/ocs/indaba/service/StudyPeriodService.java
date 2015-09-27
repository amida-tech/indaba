/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.dao.StudyPeriodDAO;
import com.ocs.indaba.po.StudyPeriod;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class StudyPeriodService {
    
    private StudyPeriodDAO studyPeriodDao = null;
    
    @Autowired
    public void setStudyPeriodDao(StudyPeriodDAO studyPeriodDao) {
        this.studyPeriodDao = studyPeriodDao;
    }
    
    public List<StudyPeriod> getAllStudyPeriods() {
        return studyPeriodDao.findAll();
    }
}
