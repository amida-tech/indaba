/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.dao;

import java.util.ArrayList;
import com.ocs.indaba.aggregation.po.WsIndicatorInstance;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.util.ResultSetUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import static java.sql.Types.INTEGER;

/**
 *
 * @author jiangjeff
 */
public class WorksetIndicatorInstanceDAO extends SmartDaoMySqlImpl<WsIndicatorInstance, Integer> {

    private static final Logger log = Logger.getLogger(WorksetIndicatorInstanceDAO.class);
    private static final String SELECT_WSINDICATOR_BY_WORKSET_ID = "select * from ws_indicator_instance where workset_id=?";
    private static final String SELECT_WSINDICATOR_BY_INDICATOR_IDS = "select * from ws_indicator_instance where indicator_id in ";
    private static final String SELECT_INDICATOR_IDS_BY_WORKSET_ID = "select indicator_id from ws_indicator_instance where workset_id=?";
    private static final String DELETE_WSINDICATOR_BY_WORKSET_ID = "delete from ws_indicator_instance where workset_id=?";
    private static final String SELECT_WSINDICATOR_BY_WORKSET_AND_INDICATOR_IDS = "select * from ws_indicator_instance where workset_id=? and indicator_id in ";
    private static final String SELECT_WSINDICATOR_BY_IDS = "select * from ws_indicator_instance where id in ";

    public List<WsIndicatorInstance> selectWSIndicatorByWorksetId(int worksetId){
        log.debug("select  worksetIndicatorInstance by workset id " + worksetId);
        return super.find(SELECT_WSINDICATOR_BY_WORKSET_ID, worksetId);
    }

    public List<Integer> selectWsIndicatorIdsByWorksetId(int worksetId) {
        List<WsIndicatorInstance> wiiList = selectWSIndicatorByWorksetId(worksetId);
        List<Integer> wsIndicatorIds = new ArrayList<Integer>();
        for (WsIndicatorInstance wii : wiiList) {
            wsIndicatorIds.add(wii.getId());
        }
        return wsIndicatorIds;
    }

    public List<Integer> selectIndicatorIdsByWsIndicatorIds(List<Integer> ids) {
        List<WsIndicatorInstance> wiiList = selectWSIndicatorByIds(ids);
        List<Integer> indicatorIds = new ArrayList<Integer>();
        for (WsIndicatorInstance wii : wiiList) {
            indicatorIds.add(wii.getIndicatorId());
        }
        return indicatorIds;
    }

    public List<WsIndicatorInstance> selectWSIndicatorByIds(List<Integer> ids) {
        String query = SELECT_WSINDICATOR_BY_IDS + ids.toString().replace("[", "(").replace("]", ")");
        return super.find(query);
    }

    public List<WsIndicatorInstance> selectWSIndicatorByIndicatorIds(String indicatorIds){
        log.debug("select worksetIndicatorInstance by indicator ids " + indicatorIds);
        String query = SELECT_WSINDICATOR_BY_INDICATOR_IDS +  indicatorIds;
        return super.find(query);
    }

    public List<WsIndicatorInstance> selectWSIndicatorByWorksetIdAndIndicatorIds(int worksetId, String indicatorIds){
        log.debug("select worksetIndicatorInstance by indicator ids " + indicatorIds);
        String query = SELECT_WSINDICATOR_BY_WORKSET_AND_INDICATOR_IDS +  indicatorIds;
        return super.find(query, worksetId);
    }

    public List<Integer> selectIndicatorIdsByWorksetId(int worksetId){
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return (ResultSetUtil.getInt(rs, "indicator_id"));
            }
        };
        return getJdbcTemplate().query(
                SELECT_INDICATOR_IDS_BY_WORKSET_ID,
                new Object[]{worksetId},
                new int[]{INTEGER},
                mapper);
    }


    public void deleteIndicatorsByWorksetId(int worksetId){
        super.delete(DELETE_WSINDICATOR_BY_WORKSET_ID, worksetId);
    }
}
