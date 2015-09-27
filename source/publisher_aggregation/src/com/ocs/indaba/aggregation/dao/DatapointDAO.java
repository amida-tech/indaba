/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.dao;

import com.ocs.indaba.aggregation.vo.DataForm;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.aggregation.po.Datapoint;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import static java.sql.Types.INTEGER;

/**
 *
 * @author luwb
 */
public class DatapointDAO extends SmartDaoMySqlImpl<Datapoint, Integer> {

    private static final Logger log = Logger.getLogger(DatapointDAO.class);
    private static final String SELECT_ALL_DATAPOINTS = "select * from datapoint";
    private static final String SELECT_DATAPOINT_BY_NAME = "select * from datapoint where name=?";
    private static final String SELECT_DATAPOINT_BY_SHORTNAME = "select * from datapoint where short_name=?";
    private static final String SELECT_DATAPOINT_BY_WORKSET_ID = "select * from datapoint where workset_id=? order by id";
    private static final String SELECT_DATAPOINT_BY_DATAPOINT_IDS = "select * from datapoint where id in ";
    private static final String SELECT_DATAPOINT_IN_ORDER = "select * from datapoint order by id";
    private static final String SELECT_DATAPOINT_BY_FILTER =
            "SELECT DISTINCT dp.* FROM datapoint dp "
            + "JOIN tds_value tds ON (tds.datapoint_id = dp.id) "
            + "JOIN dataset ds ON (tds.dataset_id = ds.id) "
            + "WHERE ds.workset_id = ? AND tds.study_period_id IN ({0}) "
            + "AND tds.target_id IN ({1}) AND ds.includes_nonpub_data = ?";
    private static final String SELECT_DATAPOINT_BY_WORKSET_IDS =
            "SELECT * FROM datapoint WHERE workset_id IN ({0})";
    private static final String SELECT_DATAPOINT_BY_WORKSET_ID_AND_NAME =
            "SELECT * FROM datapoint WHERE workset_id=? AND name=?";
    private static final String SELECT_DATAPOINT_BY_WORKSET_ID_AND_SHORTNAME =
            "SELECT * FROM datapoint WHERE workset_id=? AND short_name=?";
    private static final String SELECT_DATAPOINT_COUNT_BY_WORKSET_ID =
            "SELECT count(*) count FROM datapoint WHERE workset_id=?";

    public Datapoint selectDataPointsById(int datapointId) {
        log.debug("Select Dadapoint by  id: " + datapointId);
        return super.get(datapointId);
    }

    public Datapoint selectDataPointByName(String name) {
        log.debug("Select DataPoint by name: " + name);
        return super.findSingle(SELECT_DATAPOINT_BY_NAME, name);
    }

    public Datapoint selectDataPointByShortName(String name) {
        log.debug("Select DataPoint by name: " + name);
        return super.findSingle(SELECT_DATAPOINT_BY_SHORTNAME, name);
    }

    public List<Datapoint> selectDataPointByWorksetId(int worksetId) {
        log.debug("Select DataPoint by workset Id: " + worksetId);
        return super.find(SELECT_DATAPOINT_BY_WORKSET_ID, worksetId);
    }

    public List<Integer> selectDatapointIdsByWorksetId(int worksetId) {
        List<Datapoint> dpList = selectDataPointByWorksetId(worksetId);
        List<Integer> dpIdList = new ArrayList<Integer>();
        for (Datapoint dp : dpList) {
            dpIdList.add(dp.getId());
        }
        return dpIdList;
    }

    public List<Datapoint> selectDatapointByDatapointIds(String datapointIds) {
        log.debug("select Datapoint by datapoint ids " + datapointIds);
        String query = SELECT_DATAPOINT_BY_DATAPOINT_IDS + datapointIds;
        return super.find(query);
    }

    public List<Datapoint> selectDatapointOderById() {
        log.debug("select Datapoint order by datapoint id");
        return super.find(SELECT_DATAPOINT_IN_ORDER);
    }

    public List<Datapoint> selectDataPointByFilter(DataForm dataForm) {
        String spStr = dataForm.getStudyPeriodIds().toString();
        String tStr = dataForm.getTargetIds().toString();
        String sqlStr = MessageFormat.format(SELECT_DATAPOINT_BY_FILTER,
                spStr.substring(1, spStr.length() - 1), tStr.substring(1, tStr.length() - 1));
        int include_nonpub_data = (dataForm.isIncludeUnverifiedData()) ? 1 : 0;
        // TODO: indicator filter


        return super.find(sqlStr, new Object[]{dataForm.getWorksetId(), include_nonpub_data});
    }

    public List<Datapoint> selectAllDatapoints(){
        return super.find(SELECT_ALL_DATAPOINTS);
    }

    public List<Datapoint> selectDatapointByWorksetIds(List<Integer> worksetIds){
        String ids = worksetIds.toString();
        String sqlStr = MessageFormat.format(SELECT_DATAPOINT_BY_WORKSET_IDS, ids.substring(1, ids.length() - 1));
        return super.find(sqlStr);
    }

    public Datapoint selectDataPointByWroksetIdAndName(int worksetId, String name) {
        return super.findSingle(SELECT_DATAPOINT_BY_WORKSET_ID_AND_NAME, worksetId, name);
    }

    public Datapoint selectDataPointByWroksetIdAndShortName(int worksetId, String name) {
        return super.findSingle(SELECT_DATAPOINT_BY_WORKSET_ID_AND_SHORTNAME, worksetId, name);
    }

    public boolean hasDatapoints(int worksetId){
        RowMapper mapper = new RowMapper() {
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("count");
            }
        };
        List<Integer> list = getJdbcTemplate().query(SELECT_DATAPOINT_COUNT_BY_WORKSET_ID,
                new Object[]{worksetId},
                new int[]{INTEGER},
                mapper);
        return (list != null && list.get(0).intValue() > 0);
    }
}
