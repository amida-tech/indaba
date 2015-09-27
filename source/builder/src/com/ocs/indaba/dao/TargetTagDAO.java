/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.TargetTag;
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
public class TargetTagDAO extends SmartDaoMySqlImpl<TargetTag, Integer> {

    private static final Logger log = Logger.getLogger(TargetTagDAO.class);
    private static final String SELECT_TAGS_BY_TARGET_ID = "SELECT * FROM target_tag WHERE target_id=?";
    private static final String SELECT_TTAGS_IDS_BY_TARGET_ID = "SELECT ttags_id FROM target_tag WHERE target_id=?";
    private static final String DELETE_TAGS_BY_IDS = "DELETE FROM target_tag WHERE id IN (?)";
    private static final String DELETE_TAGS_BY_TARGET_ID = "DELETE FROM target_tag WHERE target_id=?";

    public List<TargetTag> selectTagsByTargetId(int targetId) {
        return super.find(SELECT_TAGS_BY_TARGET_ID, targetId);
    }

    public List<Integer> selectTtagsIdsByTargetId(int targetId) {
        return getJdbcTemplate().query(SELECT_TTAGS_IDS_BY_TARGET_ID, new Object[]{targetId}, new RowMapper() {
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("ttags_id");
            }
        });
    }
    public void deleteByIds(List<Integer> ids) {
        if (ids == null && !ids.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int id : ids) {
                sb.append(id).append(',');
            }
            sb.setLength(sb.length() - 1);
            delete(MessageFormat.format(DELETE_TAGS_BY_IDS, sb.toString()));
        }
    }

    public void deleteByTargetId(int targetId) {
        delete(DELETE_TAGS_BY_TARGET_ID, targetId);
    }
}
