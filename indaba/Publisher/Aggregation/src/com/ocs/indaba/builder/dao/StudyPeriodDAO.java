/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.builder.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.StudyPeriod;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jeanbone
 */
public class StudyPeriodDAO extends SmartDaoMySqlImpl<StudyPeriod, Integer> {

    private String SELECT_STUDY_PERIODS_BY_IDS =
            "SELECT * FROM study_period WHERE id IN #1";
    private String SELECT_STUDY_PERIODS_BY_WORKSET_ID =
            "SELECT DISTINCT sp.* FROM `indaba`.`study_period` sp "
            + "JOIN `indaba`.`project` p ON (p.study_period_id = sp.id) "
            + "JOIN `indaba_publisher`.`ws_project` wp ON (wp.project_id = p.id) "
            + "WHERE wp.workset_id = ?";
    private String SELECT_STUDY_PERIOD_BY_HORSE_ID = 
            "SELECT sp.* FROM study_period sp, horse h, product prd, project prj "
            + "WHERE h.id=? AND prd.id = h.product_id AND prj.id=prd.project_id AND sp.id=prj.study_period_id";

    public List<StudyPeriod> selectStudyPeriodsByWorksetId(int id) {
        return super.find(SELECT_STUDY_PERIODS_BY_WORKSET_ID, id);
    }

    public List<Integer> selectStudyPeriodIdsByWorksetId(int worksetId) {
        List<StudyPeriod> spList = selectStudyPeriodsByWorksetId(worksetId);
        List<Integer> spIds = new ArrayList<Integer>();
        for (StudyPeriod sp : spList) {
            spIds.add(sp.getId());
        }
        return spIds;
    }

    public List<StudyPeriod> selectStudyPeriodsByIds(List<Integer> ids) {
        String idsStr = ids.toString().replace("[", "(").replace("]", ")");
        return super.find(SELECT_STUDY_PERIODS_BY_IDS.replace("#1", idsStr));
    }

    public StudyPeriod selectStudyPeriodByHorseId(int horseId) {
        List<StudyPeriod> spList = super.find(SELECT_STUDY_PERIOD_BY_HORSE_ID, horseId);

        return (spList == null) ? null : spList.get(0);
    }
}
