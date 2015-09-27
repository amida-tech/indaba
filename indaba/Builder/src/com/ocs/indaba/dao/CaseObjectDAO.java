/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.po.CaseObject;
import java.sql.ResultSet;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.sql.Types.SMALLINT;
import static java.sql.Types.INTEGER;

/**
 *
 * @author menglong
 */
public class CaseObjectDAO extends BaseDAO {

    private static final Logger logger = Logger.getLogger(CaseObjectDAO.class);
    private static final String SQL_SELECT_CASEOBJECT_BY_CASE_ID_AND_OBJECT_TYPE =
            "SELECT c.object_id, c.id "
            + " FROM case_object c where c.cases_id = ? "
            + " AND c.object_type = ?";
    private static final String SQL_INSERT_CASE_OBJECT =
            "INSERT INTO case_object (cases_id,object_type,object_id) VALUES (?, ?, ?) ";
    private static final String SQL_DELETE_CASE_OBJECT_BY_CASE_ID =
            "DELETE FROM case_object WHERE cases_id = ? ";
    private static final String SQL_DELETE_CASE_OBJECT_BY_CASE_ID_AND_OBJECT_TYPE =
            "DELETE FROM case_object WHERE cases_id = ? AND object_type = ?";
    private static final String SQL_SELECT_CASEOBJECT_EXISTS =
            "SELECT count(id) count FROM case_object WHERE cases_id=? AND object_type=? AND object_id=?";

    public List<CaseObject> selectCaseObjectByCaseIdAndObjectType(final int caseId, final Short objectType) {
        logger.debug("Select case by case ID.");
        RowMapper mapper = new RowMapper() {

            public CaseObject mapRow(ResultSet rs, int rowNum) throws SQLException {
                CaseObject caseObject = new CaseObject();
                caseObject.setCasesId(caseId);
                caseObject.setObjectType(objectType);
                caseObject.setObjectId(rs.getInt("object_id"));
                caseObject.setId(rs.getInt("id"));

                return caseObject;

            }
        };

        List<CaseObject> list = getJdbcTemplate().query(SQL_SELECT_CASEOBJECT_BY_CASE_ID_AND_OBJECT_TYPE,
                new Object[]{caseId, objectType},
                new int[]{INTEGER, SMALLINT},
                mapper);

        return list;
    }

    public int selectCaseObjectExists(final int caseId, final int objectType, final int objectId) {
        logger.debug("Select case by case ID.");
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("count");
            }
        };

        List<Integer> list = getJdbcTemplate().query(SQL_SELECT_CASEOBJECT_EXISTS,
                new Object[]{caseId, objectType, objectId},
                mapper);

        return list.get(0);
    }

    public void insertSingleCaseObject(CaseObject caseObject) {
        if (caseObject == null) {
            return;
        }
        getJdbcTemplate().update(
                SQL_INSERT_CASE_OBJECT,
                new Object[]{caseObject.getCasesId(),
                    caseObject.getObjectType(),
                    caseObject.getObjectId()});
    }

    public void insertCaseObject(final List<CaseObject> caseObjectList) {
        if (caseObjectList == null || caseObjectList.size() == 0) {
            return;
        }

        getJdbcTemplate().batchUpdate(SQL_INSERT_CASE_OBJECT,
                new BatchPreparedStatementSetter() {

                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, caseObjectList.get(i).getCasesId());
                        ps.setShort(2, caseObjectList.get(i).getObjectType());
                        ps.setLong(3, caseObjectList.get(i).getObjectId());
                    }

                    public int getBatchSize() {
                        return caseObjectList.size();
                    }
                });
    }

    public void deleteCaseObjectByCaseId(int caseId) {
        getJdbcTemplate().update(
                SQL_DELETE_CASE_OBJECT_BY_CASE_ID,
                new Object[]{caseId},
                new int[]{INTEGER});
    }

    public void deleteCaseObjectByCaseIdAndObjectType(final int caseId, final Short objectType) {
        getJdbcTemplate().update(
                SQL_DELETE_CASE_OBJECT_BY_CASE_ID_AND_OBJECT_TYPE,
                new Object[]{caseId, objectType},
                new int[]{INTEGER, SMALLINT});
    }
}
