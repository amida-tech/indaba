/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Itags;
import java.util.List;

/**
 *
 * @author Gerry
 */
public class ItagsDAO extends SmartDaoMySqlImpl<Itags, Integer>{
    private String SELECT_ITAGS_BY_INDICATOR_ID =
            "SELECT DISTINCT t.* FROM `indaba`.`itags` as t "
            + "inner join `indaba`.`indicator_tag` as r on r.itags_id=t.id "
            + "where r.survey_indicator_id=?";
    private String SELECT_DISTINCT_ITAGS_BY_WORKSET_ID =
            "SELECT DISTINCT it.* FROM `indaba`.`itags` it "
            + "JOIN `indaba`.`indicator_tag` i_t ON (i_t.itags_id = it.id) "
            + "JOIN `indaba_publisher`.`ws_indicator_instance` wii ON (wii.indicator_id = i_t.survey_indicator_id) "
            + "WHERE wii.workset_id = ?";
    
    public List<Itags> selectItagsByIndicatorId( int indicatorId ){
        return super.find(SELECT_ITAGS_BY_INDICATOR_ID,indicatorId);
    }

    public List<Itags> selectDistinctItagsByWorksetId( int worksetId ){
        return super.find(SELECT_DISTINCT_ITAGS_BY_WORKSET_ID,worksetId);
    }
}
