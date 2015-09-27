/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.SourceFileTextResource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Jeff
 */
public class SourceFileTextResourceDAO extends SmartDaoMySqlImpl<SourceFileTextResource, Integer> {

    private static final Logger log = Logger.getLogger(SourceFileTextResourceDAO.class);
    private static final String DELETE_BY_SOURCE_FILE_ID = "DELETE FROM source_file_text_resource WHERE source_file_id=?";
    private static final String DELETE_BY_TEXT_RESOURCE_ID = "DELETE FROM source_file_text_resource WHERE text_resource_id=?";
    private static final String SELECT_BY_TEXT_RESOURCE_ID = "SELECT * FROM source_file_text_resource WHERE text_resource_id=?";
    private static final String SELECT_TEXT_RESOURCE_IDS_BY_SOURCE_FILE_ID = "SELECT text_resource_id FROM source_file_text_resource WHERE source_file_id=?";
    private static final String SELECT_SOURCE_FILE_COUNT_OF_TEXT_RESOURCE = "SELECT source_file_id, COUNT(text_resource_id) count FROM source_file_text_resource GROUP BY source_file_id";

    public List<Integer> selectTextResourceIdsBySourceFileId(int sourceFileId) {
        return getJdbcTemplate().query(SELECT_TEXT_RESOURCE_IDS_BY_SOURCE_FILE_ID,
                new Object[]{sourceFileId},
                new RowMapper() {

                    public Integer mapRow(ResultSet rs, int i) throws SQLException {
                        return rs.getInt("text_resource_id");
                    }
                });
    }
    
    public SourceFileTextResource selectByTextResourceId(int txtRsrcId) {
        return super.findSingle(SELECT_BY_TEXT_RESOURCE_ID, txtRsrcId);
    }
    
    public Map<Integer, Integer> selectTextResourceCountsMap() {
        Map<Integer, Integer> rsrcCountMap = new HashMap<Integer, Integer>();
        getJdbcTemplate().query(SELECT_SOURCE_FILE_COUNT_OF_TEXT_RESOURCE, new TextResourceCountMapRowMapper(rsrcCountMap));
        return rsrcCountMap;
    }

    public void deleteByResourceId(int txtRsrcId) {
        super.delete(DELETE_BY_TEXT_RESOURCE_ID, txtRsrcId);
    }

    public void deleteBySourceFileId(int srcFileId) {
        super.delete(DELETE_BY_SOURCE_FILE_ID, srcFileId);
    }

    class TextResourceCountMapRowMapper implements RowMapper {

        private Map<Integer, Integer> map = null;

        TextResourceCountMapRowMapper(Map<Integer, Integer> map) {
            this.map = map;
        }

        public Map<Integer, Integer> mapRow(ResultSet rs, int i) throws SQLException {
            map.put(rs.getInt("source_file_id"), rs.getInt("count"));
            return map;
        }
    }
}
