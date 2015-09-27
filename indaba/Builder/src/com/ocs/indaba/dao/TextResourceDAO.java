/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.TextResource;
import com.ocs.indaba.util.ResultSetUtil;
import com.ocs.indaba.vo.TextResourceVO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Jeff
 */
public class TextResourceDAO extends SmartDaoMySqlImpl<TextResource, Integer> {

    private static final Logger log = Logger.getLogger(TextResourceDAO.class);
    private static final String SELECT_TEXT_RESOURCE_BY_SOURCEFILEID_AND_KEY =
            "SELECT txt_rsrc.*, map.* FROM text_resource txt_rsrc, source_file_text_resource map "
            + "WHERE map.source_file_id=? AND txt_rsrc.resource_name=? AND map.text_resource_id=txt_rsrc.id limit 1";
    private static final String SELECT_TEXT_RESOURCE_BY_RESOURCE_NAME =
            "SELECT * FROM text_resource WHERE resource_name=?";
    private static final String SELECT_ALL_TEXT_RESOURCES_OF_SOURCE_FILE =
            "SELECT txt_rsrc.* FROM text_resource txt_rsrc, source_file_text_resource map "
            + "WHERE map.source_file_id=? AND map.text_resource_id=txt_rsrc.id";
    private static final String SELECT_TEXT_RESOURCES_COUNT_OF_SOURCE_FILE =
            "SELECT COUNT(txt_rsrc.id) count FROM text_resource txt_rsrc, source_file_text_resource map "
            + "WHERE map.source_file_id=? AND map.text_resource_id=txt_rsrc.id";
    private static final String SELECT_MAX_TEXT_RESOURCE_ID_OF_SOURCE_FILE =
            "SELECT MAX(txt_rsrc.id) max_txt_rsrc_id FROM text_resource txt_rsrc, source_file_text_resource map "
            + "WHERE map.source_file_id=? AND map.text_resource_id=txt_rsrc.id";
    private static final String DELETE_TEXT_RESOURCES_BY_IDS =
            "DELETE FROM text_resource WHERE id IN {0}";

    public List<TextResource> selectAllTextResourcesOfSouceFile(int sourceFileId) {
        return super.find(SELECT_ALL_TEXT_RESOURCES_OF_SOURCE_FILE, sourceFileId);
    }

    public TextResource selectTextResourceByResourceName(String rsrcName) {
        return super.findSingle(SELECT_TEXT_RESOURCE_BY_RESOURCE_NAME, rsrcName);
    }

    public long selectTextResourceCountOfSouceFile(int sourceFileId) {
        return super.count(SELECT_TEXT_RESOURCES_COUNT_OF_SOURCE_FILE, sourceFileId);
    }

    public int selectMaxTextResourceIdOfSouceFile(int sourceFileId) {
       return getJdbcTemplate().queryForInt(SELECT_MAX_TEXT_RESOURCE_ID_OF_SOURCE_FILE, new Object[]{sourceFileId});
    }

    public TextResourceVO selectTextResource(int sourceFileId, String resourceName) {
        return (TextResourceVO) getJdbcTemplate().queryForObject(SELECT_TEXT_RESOURCE_BY_SOURCEFILEID_AND_KEY,
                new Object[]{sourceFileId, resourceName},
                new RowMapper() {

                    public TextResourceVO mapRow(ResultSet rs, int i) throws SQLException {
                        TextResourceVO txtRsrc = new TextResourceVO();
                        txtRsrc.setSourceFileTextResourceId(ResultSetUtil.getInt(rs, "map.id"));
                        txtRsrc.setSourceFileId(ResultSetUtil.getInt(rs, "map.source_file_id"));
                        txtRsrc.setTextResourceId(ResultSetUtil.getInt(rs, "text_rsrc.id"));
                        txtRsrc.setResourceName(ResultSetUtil.getString(rs, "text_rsrc.resource_name"));
                        txtRsrc.setDescription(ResultSetUtil.getString(rs, "text_rsrc.description"));
                        return txtRsrc;
                    }
                });
    }

    public void deleteTextResourcesByIds(List<Integer> txtRsrcIds) {
        super.delete(MessageFormat.format(DELETE_TEXT_RESOURCES_BY_IDS, txtRsrcIds.toString().replaceAll("[\\[|\\]]", "")));
    }
}
