/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.IndicatorTag;
import java.util.List;

/**
 *
 * @author seanpcheng
 */
public class IndicatorTagDAO extends SmartDaoMySqlImpl<IndicatorTag, Integer> {

    private static final String SELECT_BY_INDICATOR_ID_AND_ITAG_ID = "SELECT * FROM indicator_tag WHERE survey_indicator_id=? AND itags_id=?";
    private static final String SELECT_BY_INDICATOR_ID = "SELECT * FROM indicator_tag WHERE survey_indicator_id=?";
    private static final String DELETE_BY_INDICATOR_ID = "DELETE FROM indicator_tag WHERE survey_indicator_id=?";

    public IndicatorTag getIndicator(int indicatorId, int itagId) {
        return super.findSingle(SELECT_BY_INDICATOR_ID_AND_ITAG_ID, indicatorId, itagId);
    }

    public List<IndicatorTag> getIndicatorTagsByIndicatorId(int indicatorId) {
        return super.find(SELECT_BY_INDICATOR_ID, indicatorId);
    }

    public void deleteIndicatorTagsByIndicatorId(int indicatorId) {
        super.delete(DELETE_BY_INDICATOR_ID, indicatorId);
    }
}
