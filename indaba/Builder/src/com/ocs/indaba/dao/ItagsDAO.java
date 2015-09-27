/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Itags;
import java.util.List;

/**
 *
 * @author Jeff Jiang
 */
public class ItagsDAO extends SmartDaoMySqlImpl<Itags, Integer> {
    private static String SELECT_ALL_TAGS = "SELECT * FROM itags";
    
    public List<Itags> selectAllItags() {
       return super.find(SELECT_ALL_TAGS); 
    }
    
    private static final String SELECT_ALL_ITAGS_BY_INDICATOR_ID = "SELECT t.* FROM itags t, indicator_tag it WHERE it.survey_indicator_id=? AND it.itags_id=t.id";

    public List<Itags> getIndicatorTagsByIndicatorId(int indicatorId) {
        return super.find(SELECT_ALL_ITAGS_BY_INDICATOR_ID, indicatorId);
    }
}
