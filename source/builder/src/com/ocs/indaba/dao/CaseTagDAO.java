/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.po.CaseTag;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static java.sql.Types.INTEGER;

/**
 *
 * @author menglong
 */
public class CaseTagDAO extends BaseDAO {

    private static final String SQL_INSERT_CASE_TAG =
            "INSERT INTO case_tag (cases_id, ctags_id) VALUES (?, ?) ";
    private static final String SQL_DELETE_CASE_TAGS_BY_CASE_ID =
            "DELETE FROM case_tag WHERE cases_id = ? ";

    public void insertSingleCaseTag(CaseTag caseTag) {
        if (caseTag == null) {
            return;
        }
        getJdbcTemplate().update(
                SQL_INSERT_CASE_TAG,
                new Object[]{caseTag.getCasesId(),
                    caseTag.getCtagsId()});
    }

    public void insertCaseTags(final List<CaseTag> caseTagList) {
        if (caseTagList == null || caseTagList.size() == 0) {
            return;
        }
        getJdbcTemplate().batchUpdate(SQL_INSERT_CASE_TAG,
                new BatchPreparedStatementSetter() {

                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, caseTagList.get(i).getCasesId());
                        ps.setLong(2, caseTagList.get(i).getCtagsId());
                    }

                    public int getBatchSize() {
                        return caseTagList.size();
                    }
                });
    }

    public void deleteCaseTagsByCaseId(int caseId) {
        getJdbcTemplate().update(
                SQL_DELETE_CASE_TAGS_BY_CASE_ID,
                new Object[]{caseId},
                new int[]{INTEGER});
    }
}
