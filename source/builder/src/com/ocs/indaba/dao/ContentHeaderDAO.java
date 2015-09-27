/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.ContentHeader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import static java.sql.Types.INTEGER;

/**
 *
 * @author menglong
 */
public class ContentHeaderDAO extends SmartDaoMySqlImpl<ContentHeader, Integer> {

    private static final Logger log = Logger.getLogger(ContentHeaderDAO.class);
    /*private static final String SELECT_CONTENT_HEADER_BY_HORSE_ID =
     "SELECT ch.* FROM  content_header ch, horse h WHERE h.id=? AND h.content_header_id=ch.id";*/
    private static final String SELECT_CONTENT_HEADER_BY_HORSE_ID = "SELECT * FROM content_header WHERE horse_id = ?";

    public ContentHeader selectContentHeaderById(int cntHdrId) {
        log.debug("Select content header by id: " + cntHdrId + ".");
        return super.get(cntHdrId);
    }

    public List<String> selectContentHeaderTitleByFilter(int casesId,
            List<Integer> targetFilter, List<Integer> productFilter) {
        String queryStr = "SELECT ch.title FROM content_header ch "
                + "JOIN case_object co ON (co.object_id = ch.id AND co.object_type = 1) "
                + "JOIN horse h ON (ch.horse_id = h.id) "
                + "WHERE co.cases_id = ?";
        RowMapper mapper = new RowMapper() {
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("title");
            }
        };

        List<String> retList = getJdbcTemplate().query(queryStr,
                new Object[]{casesId}, new int[]{INTEGER},
                mapper);
        if (retList.isEmpty()) {
            if (targetFilter.get(0) == 1 || productFilter.get(0) == 1) {
                return null;
            }
            return new ArrayList<String>();
        }


        StringBuffer sb = new StringBuffer(queryStr);

        int i;
        String op;
        if (targetFilter.size() == 1) {
            if (targetFilter.get(0) == 0) {
                return null;
            }
        } else {
            sb.append(" AND h.target_id");
            op = (targetFilter.get(0) == 0) ? " IN (" : " NOT IN (";
            sb.append(op);
            for (i = 1; i < targetFilter.size() - 1; i++) {
                sb.append(targetFilter.get(i) + ", ");
            }
            if (i == targetFilter.size() - 1) {
                sb.append(targetFilter.get(i) + ")");
            } else {
                sb.append(")");
            }
        }
        List<String> retVal = getJdbcTemplate().query(sb.toString(),
                new Object[]{casesId}, new int[]{INTEGER},
                mapper);
        if (targetFilter.get(0) == 1 && retVal.isEmpty()) {
            return null;
        }
        if (targetFilter.get(0) == 0 && retVal.size() < retList.size()) {
            return null;
        }

        sb = new StringBuffer(queryStr);
        if (productFilter.size() == 1) {
            if (productFilter.get(0) == 0) {
                return null;
            }
        } else {
            sb.append(" AND h.product_id");
            op = (productFilter.get(0) == 0) ? " IN (" : " NOT IN (";
            sb.append(op);
            for (i = 1; i < productFilter.size() - 1; i++) {
                sb.append(productFilter.get(i) + ", ");
            }
            if (i == productFilter.size() - 1) {
                sb.append(productFilter.get(i) + ")");
            } else {
                sb.append(")");
            }
        }

        retVal = getJdbcTemplate().query(sb.toString(),
                new Object[]{casesId}, new int[]{INTEGER},
                mapper);

        if (productFilter.get(0) == 1) {
            return retVal.isEmpty() ? null : retList;
        } else {
            return retVal.size() < retList.size() ? null : retList;
        }
    }

    public List<ContentHeader> selectContentHeaderByFilter(int casesId,
            List<Integer> targetFilter, List<Integer> productFilter) {
        String queryStr = "SELECT ch.* FROM content_header ch "
                + "JOIN case_object co ON (co.object_id = ch.id AND co.object_type = 1) "
                + "JOIN horse h ON (ch.horse_id = h.id) "
                + "WHERE co.cases_id = ?";
        RowMapper mapper = new RowMapper() {
            public ContentHeader mapRow(ResultSet rs, int rowNum) throws SQLException {
                ContentHeader contentHeader = new ContentHeader();
                contentHeader.setId(rs.getInt("id"));
                contentHeader.setProjectId(rs.getInt("project_id"));
                contentHeader.setContentType(rs.getInt("content_type"));
                contentHeader.setContentObjectId(rs.getInt("content_object_id"));
                contentHeader.setHorseId(rs.getInt("horse_id"));
                contentHeader.setTitle(rs.getString("title"));

                return contentHeader;
            }
        };

        List<ContentHeader> retList = getJdbcTemplate().query(queryStr,
                new Object[]{casesId}, new int[]{INTEGER},
                mapper);
        if (retList.isEmpty()) {
            if (targetFilter.get(0) == 1 || productFilter.get(0) == 1) {
                return null;
            }
            return new ArrayList<ContentHeader>();
        }


        StringBuffer sb = new StringBuffer(queryStr);

        int i;
        String op;
        if (targetFilter.size() == 1) {
            if (targetFilter.get(0) == 0) {
                return null;
            }
        } else {
            sb.append(" AND h.target_id");
            op = (targetFilter.get(0) == 0) ? " IN (" : " NOT IN (";
            sb.append(op);
            for (i = 1; i < targetFilter.size() - 1; i++) {
                sb.append(targetFilter.get(i) + ", ");
            }
            if (i == targetFilter.size() - 1) {
                sb.append(targetFilter.get(i) + ")");
            } else {
                sb.append(")");
            }
        }
        List<ContentHeader> retVal = getJdbcTemplate().query(sb.toString(),
                new Object[]{casesId}, new int[]{INTEGER},
                mapper);
        if (targetFilter.get(0) == 1 && retVal.isEmpty()) {
            return null;
        }
        if (targetFilter.get(0) == 0 && retVal.size() < retList.size()) {
            return null;
        }

        sb = new StringBuffer(queryStr);
        if (productFilter.size() == 1) {
            if (productFilter.get(0) == 0) {
                return null;
            }
        } else {
            sb.append(" AND h.product_id");
            op = (productFilter.get(0) == 0) ? " IN (" : " NOT IN (";
            sb.append(op);
            for (i = 1; i < productFilter.size() - 1; i++) {
                sb.append(productFilter.get(i) + ", ");
            }
            if (i == productFilter.size() - 1) {
                sb.append(productFilter.get(i) + ")");
            } else {
                sb.append(")");
            }
        }

        retVal = getJdbcTemplate().query(sb.toString(),
                new Object[]{casesId}, new int[]{INTEGER},
                mapper);

        if (productFilter.get(0) == 1) {
            return retVal.isEmpty() ? null : retList;
        } else {
            return retVal.size() < retList.size() ? null : retList;
        }
    }

    public ContentHeader selectContentHeaderByHorseId(int horseId) {
        log.debug("Select content header by horse id: " + horseId + ".");
        return super.findSingle(SELECT_CONTENT_HEADER_BY_HORSE_ID, horseId);
    }

    public ContentHeader createContentHeader(ContentHeader cntHdr) {
        if (cntHdr.getTitle() == null) {
            cntHdr.setTitle("No Title!");
        }
        if (cntHdr.getCreateTime() == null) {
            cntHdr.setCreateTime(new Date());
        }
        return create(cntHdr);
    }

    public ContentHeader updateContentHeader(ContentHeader cntHdr) {
        return super.update(cntHdr);
    }

    public List<ContentHeader> selectAllContentHeader() {
        log.debug("Select all content header.");
        return findAll();
    }

    public List<ContentHeader> selectContentHeaderByAssignedUserId(int userId) {
        String queryStr = "SELECT DISTINCT ch.* FROM task_assignment ta "
                + "JOIN task t ON (ta.assigned_user_id = ? AND ta.task_id = t.id) "
                + "JOIN horse h ON (ta.target_id = h.target_id AND t.product_id = h.product_id) "
                + "JOIN content_header ch ON (h.content_header_id = ch.id)";

        return super.find(queryStr, userId);
    }

    public List<ContentHeader> selectAllContentHeaderOrderByTitle() {
        String queryStr = "SELECT * FROM content_header ORDER BY title";
        return super.find(queryStr);
    }

    public List<ContentHeader> selectAllStartedContentHeaderOrderByTitle(int projectId) {
        String queryStr = "SELECT * FROM content_header ch "
                + "WHERE(SELECT wo.status FROM horse h JOIN workflow_object wo "
                + "ON (h.workflow_object_id = wo.id) WHERE h.content_header_id = ch.id AND ch.project_id=?)>1 "
                + "ORDER BY ch.title";
        return super.find(queryStr, projectId);
    }

    public List<ContentHeader> selectContentHeaderByAssignedUserIdOrderByTitle(int userId) {
        String queryStr = "SELECT DISTINCT ch.* FROM task_assignment ta "
                + "JOIN task t ON (ta.assigned_user_id = ? AND ta.task_id = t.id) "
                + "JOIN horse h ON (ta.target_id = h.target_id AND t.product_id = h.product_id) "
                + "JOIN content_header ch ON (h.content_header_id = ch.id) "
                + "ORDER BY ch.title";

        return super.find(queryStr, userId);
    }

    public int selectHorseIdByContentVersionId(int contentVersionId) {
        String queryStr = "SELECT ch.* FROM content_header ch JOIN content_version cv ON ch.id=cv.content_header_id WHERE cv.id=?";
        ContentHeader ch = findSingle(queryStr, contentVersionId);
        return ((ch == null) ? -1 : ch.getHorseId());
    }
}
