/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
*/

package com.ocs.indaba.dao;

import com.ocs.indaba.po.Ctags;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import static java.sql.Types.INTEGER;

/**
 *
 * @author menglong
 */
public class CTagDAO extends BaseDAO{

    private static final Logger logger = Logger.getLogger(CTagDAO.class);
    
    private static final String SQL_SELECT_TAGS_BY_CASE_ID =
        " SELECT c.* FROM ctags c "
        + " JOIN case_tag ct ON c.id = ct.ctags_id "
        + " WHERE ct.cases_id = ?";

    private static final String SQL_SELECT_ALL_TAGS =
        " SELECT * FROM ctags";

    private static final String SQL_SELECT_ALL_TAGS_ORDER_BY_TERM =
             "SELECT * FROM ctags ORDER BY term";

    public List<Ctags> selectTagsByFilter(final int caseId, List<Integer> filter) {
        RowMapper mapper = new RowMapper() {
            public Ctags mapRow(ResultSet rs, int rowNum) throws SQLException {
                Ctags ctag = new Ctags();
                ctag.setId(rs.getInt("id"));
                ctag.setTerm(rs.getString("term"));
                ctag.setDescription(rs.getString("description"));
                return ctag;
            }
        };
        
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT ctags.* FROM ctags ")
          .append("JOIN case_tag ON case_tag.ctags_id = ctags.id ")
          .append("WHERE case_tag.cases_id = ?");

        List<Ctags> retList = getJdbcTemplate().query(sb.toString(),
                new Object[]{caseId},
                new int[]{INTEGER},
                mapper);

        if (retList.isEmpty()) {
            return (filter.get(0).equals(1)) ? null : retList;
        }

        int i;
        String op;
        if (filter.size() == 1) {
            if (filter.get(0) == 0) {
                return null;
            }
        } else {
            sb.append(" AND ctags.id");
            op = (filter.get(0) == 0) ? " IN (" : " NOT IN (";
            sb.append(op);
            for (i = 1; i < filter.size() - 1; i ++) {
                sb.append(filter.get(i) + ", ");
            }
            if (i == filter.size() - 1) {
                sb.append(filter.get(i) + ")");
            } else {
                sb.append(")");
            }
        }
        List<Ctags> retList1 = getJdbcTemplate().query(sb.toString(),
                new Object[]{caseId}, new int[]{INTEGER},
                mapper);

        if (filter.get(0) == 1)
            return retList1.isEmpty() ? null : retList;
        else
            return retList1.size() < retList.size() ? null : retList;
    }
    
    
    public List<Ctags> selectTagsByCaseId (final int caseId) {
        logger.debug("Select tags by case ID.");
        RowMapper mapper = new RowMapper() {

            public Ctags mapRow(ResultSet rs, int rowNum) throws SQLException {
                Ctags ctag = new Ctags();
                ctag.setId(rs.getInt("id"));
                ctag.setTerm(rs.getString("term"));
                ctag.setDescription(rs.getString("description"));
                return ctag;

            }
        };

        return getJdbcTemplate().query(SQL_SELECT_TAGS_BY_CASE_ID,
                new Object[]{caseId},
                new int[]{INTEGER},
                mapper);
    }


    private List<Ctags> selectTags(String sql) {
        RowMapper mapper = new RowMapper() {

            public Ctags mapRow(ResultSet rs, int rowNum) throws SQLException {
                Ctags ctag = new Ctags();
                ctag.setId(rs.getInt("id"));
                ctag.setTerm(rs.getString("term"));
                ctag.setDescription(rs.getString("description"));
                return ctag;

            }
        };

        return getJdbcTemplate().query(sql, mapper);
    }


    public List<Ctags> selectAllTags() {
        logger.debug("Select all tags");
        return selectTags(SQL_SELECT_ALL_TAGS);
    }

    public List<Ctags> selectAllTagsOrderByTerm() {
        return selectTags(SQL_SELECT_ALL_TAGS_ORDER_BY_TERM);
    }
}
