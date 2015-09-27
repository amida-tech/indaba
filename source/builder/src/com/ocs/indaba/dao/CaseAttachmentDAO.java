/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
*/

package com.ocs.indaba.dao;

import com.ocs.indaba.po.CaseAttachment;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import static java.sql.Types.BIGINT;
import static java.sql.Types.VARCHAR;
/**
 *
 * @author menglong
 */
public class CaseAttachmentDAO extends BaseDAO{

    private static final Logger logger = Logger.getLogger(CaseAttachmentDAO.class);

    private static final String SQL_SELECT_ATTACHMENTS_BY_CASE_ID =
        "SELECT id, file_name, file_path  "
        + " FROM case_attachment "
        + " WHERE cases_id = ?";

    private static final String SQL_SELECT_ATTACHMENT_BY_CASEID_AND_FILENAME =
            "SELECT id FROM case_attachment WHERE cases_id = ? AND file_name = ?";

    private static final String SQL_INSERT_CASE_ATTACHMENT =
            "INSERT INTO case_attachment (cases_id, file_name, file_path) VALUES (?, ?, ?)";

    private static final String SQL_SELECT_ATTACHMENTS_FILEPATH_BY_ID =
        "SELECT * FROM case_attachment WHERE id = ?";

    private static final String SQL_DELETE_ATTACHMENTS_BY_ID =
        "DELETE from case_attachment"
        + " WHERE id = ?";

    public List<CaseAttachment> selectAttachmentsByCaseId (final int caseId) {
        logger.debug("Select attach files by case ID.");
        RowMapper mapper = new RowMapper() {

            public CaseAttachment mapRow(ResultSet rs, int rowNum) throws SQLException {
                CaseAttachment caseAttachment = new CaseAttachment();
                caseAttachment.setId(rs.getInt("id"));
                caseAttachment.setCasesId(caseId);
                caseAttachment.setFileName(rs.getString("file_name"));
                caseAttachment.setFilePath(rs.getString("file_path"));
                return caseAttachment;

            }
        };

        return getJdbcTemplate().query(SQL_SELECT_ATTACHMENTS_BY_CASE_ID,
                new Object[]{caseId},
                new int[]{BIGINT},
                mapper);
    }

    public long insertCaseAttachment(CaseAttachment caseAttachment) {
        final int caseId = caseAttachment.getCasesId();
        final String fileName = caseAttachment.getFileName();
        final String filePath = caseAttachment.getFilePath();

        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement prepareStatement =
                        conn.prepareStatement(SQL_INSERT_CASE_ATTACHMENT, Statement.RETURN_GENERATED_KEYS);
                prepareStatement.setInt(1, caseId);
                prepareStatement.setString(2, fileName);
                prepareStatement.setString(3, filePath);
                return prepareStatement;
            }
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public List<CaseAttachment> selectAttachmentsFilePathById (final int Id) {
        logger.debug("Select attach file path by ID.");
        RowMapper mapper = new RowMapper() {

            public CaseAttachment mapRow(ResultSet rs, int rowNum) throws SQLException {
                CaseAttachment caseAttachment = new CaseAttachment();
                caseAttachment.setId(Id);
                caseAttachment.setFileName(rs.getString("file_name"));
                caseAttachment.setFilePath(rs.getString("file_path"));
                caseAttachment.setCasesId(rs.getInt("cases_id"));
                return caseAttachment;
            }
        };

        return getJdbcTemplate().query(SQL_SELECT_ATTACHMENTS_FILEPATH_BY_ID,
                new Object[]{Id},
                new int[]{BIGINT},
                mapper);
    }

    public int deleteAttachmentsById(final int Id){
        logger.debug("delete attach files by ID:"+Id);
        
        try{
            return getJdbcTemplate().update(SQL_DELETE_ATTACHMENTS_BY_ID, new Object[] { Id });
        }
        catch(Exception e){
            return 0;
        }
    }

    public int selectAttachmentByCaseIdAndFileName(int caseId, String fileName) {

        RowMapper mapper = new RowMapper() {

            public CaseAttachment mapRow(ResultSet rs, int rowNum) throws SQLException {
                CaseAttachment caseAttachment = new CaseAttachment();
                caseAttachment.setId(rs.getInt("id"));
                return caseAttachment;
            }
        };

        List<CaseAttachment> list = getJdbcTemplate().query(SQL_SELECT_ATTACHMENT_BY_CASEID_AND_FILENAME,
                new Object[]{caseId, fileName},
                new int[]{BIGINT, VARCHAR},
                mapper);

        return (list == null) ? -1 : list.get(0).getId();
    }
}
