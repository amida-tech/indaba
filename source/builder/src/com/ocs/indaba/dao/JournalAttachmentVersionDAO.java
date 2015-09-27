/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.JournalAttachmentVersion;
import com.ocs.indaba.vo.JournalAttachmentVersionView;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import static java.sql.Types.INTEGER;

/**
 *
 * @author Jeff
 */
public class JournalAttachmentVersionDAO extends SmartDaoMySqlImpl<JournalAttachmentVersion, Integer> {

    private static final Logger logger = Logger.getLogger(JournalAttachmentVersionDAO.class);
    private static final String SELECT_ATTACHMENT_VERSION_BY_CONTENT_VERSION_ID =
            "SELECT * FROM journal_attachment_version WHERE content_version_id=?";
    private static final String SELECT_ATTACHMENT_VERSION_BY_ATTACH_VERSION_ID =
            "SELECT cv.id content_version_id, cv.content_header_id, create_time, jav.user_id, description, "
            + "jav.id attach_version_id, name, size, type, note, file_path, update_time "
            + "FROM content_version cv, journal_attachment_version jav "
            + "WHERE cv.id=jav.content_version_id and jav.id=?";

    public List<JournalAttachmentVersion> getJournalAttachmentVersions(int cntVerId) {
        return super.find(SELECT_ATTACHMENT_VERSION_BY_CONTENT_VERSION_ID, cntVerId);
    }

    public JournalAttachmentVersionView getJournalAttachmentVersionByAttachVersionId(int attachVersionId) {
        logger.debug("Select jouranl attachment version by attachemnt version id: " + attachVersionId + "\n" + SELECT_ATTACHMENT_VERSION_BY_ATTACH_VERSION_ID);
        List<JournalAttachmentVersionView> list = getJdbcTemplate().query(SELECT_ATTACHMENT_VERSION_BY_ATTACH_VERSION_ID,
                new Object[]{attachVersionId}, new int[]{INTEGER},
                new JournalAttachmentVersionViewRowMapper());
        return (list == null || list.size() == 0) ? null : list.get(0);
    }
}

class JournalAttachmentVersionViewRowMapper implements RowMapper {

    public JournalAttachmentVersionView mapRow(ResultSet rs, int rowNum) throws SQLException {
        JournalAttachmentVersionView attachVersionView = new JournalAttachmentVersionView();
        attachVersionView.setAttachVersionId(rs.getInt("attach_version_id"));
        attachVersionView.setContentHeaderId(rs.getInt("content_header_id"));
        attachVersionView.setContentVersionId(rs.getInt("content_version_id"));
        attachVersionView.setCreateTime(rs.getDate("create_time"));
        attachVersionView.setUpdateTime(rs.getDate("update_time"));
        attachVersionView.setDescription(rs.getString("description"));
        attachVersionView.setFilePath(rs.getString("file_path"));
        attachVersionView.setName(rs.getString("name"));
        attachVersionView.setNote(rs.getString("note"));
        attachVersionView.setSize(rs.getInt("size"));
        attachVersionView.setType(rs.getString("type"));
        attachVersionView.setUserId(rs.getInt("user_id"));

        return attachVersionView;
    }
}
