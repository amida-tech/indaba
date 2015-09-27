/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.aggregation.po.Dataset;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author luwb
 */
public class DatasetDAO extends SmartDaoMySqlImpl<Dataset, Integer>{
    private static final Logger log = Logger.getLogger(DatasetDAO.class);
    private static final String SELECT_DATASET_BY_WORKSET_ID = "SELECT * FROM dataset WHERE workset_id=?";
    private static final String DELETE_DATASET_BY_WORKSET_ID = "DELETE  FROM dataset WHERE workset_id=?";
    private static final String UPDATE_DATESET_UPDATE_TIME_BY_ID =
            "UPDATE dataset SET last_update_time=now() WHERE id=?";
    private static final String UPDATE_DATESET_UPDATE_TIME_BY_WORKSET_ID =
            "UPDATE dataset SET last_update_time=now() WHERE workset_id=?";

    public List<Dataset> selectDatasetByWorksetId(int worksetId){
        return super.find(SELECT_DATASET_BY_WORKSET_ID, worksetId);
    }

    public void deleteDatasetByWorksetId(int worksetId){
        super.delete(DELETE_DATASET_BY_WORKSET_ID, worksetId);
    }

    public void updateDatasetLastUpdateTime(int datasetId){
        super.update(UPDATE_DATESET_UPDATE_TIME_BY_ID, datasetId);
    }

    public void updateDatasetLastUpdateTimeByWorksetId(int worksetId){
        super.update(UPDATE_DATESET_UPDATE_TIME_BY_WORKSET_ID, worksetId);
    }
}
