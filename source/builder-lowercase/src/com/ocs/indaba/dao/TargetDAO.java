/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Target;
import com.ocs.indaba.vo.ProjectTargetVO;
import com.ocs.util.ListUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Types.BIGINT;
import static java.sql.Types.INTEGER;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Jeff
 */
public class TargetDAO extends SmartDaoMySqlImpl<Target, Integer> {

    private static final Logger log = Logger.getLogger(TargetDAO.class);
    private static final String SELECT_TARGETS =
            "SELECT * FROM target ORDER BY name";
    private static final String SELECT_TARGETS_BY_LIMIT =
            "SELECT * FROM target ORDER BY name LIMIT {0},{1}";
    private static final String SELECT_TARGETS_COUNT =
            "SELECT COUNT(id) FROM target";
    private static final String SELECT_TARGET_BY_ID =
            "SELECT * FROM target WHERE id= ? ORDER BY name";
    private static final String SELECT_TARGETS_BY_USER_ID =
            " SELECT distinct t.id, t.name, t.description, t.target_type "
            + " FROM target t "
            + " JOIN task_assignment ta ON t.id = ta.target_id "
            + " WHERE ta.assigned_user_id = ? ";
    private static final String SELECT_TARGETS_BY_USER_ID_AND_PROJ_ID =
            " SELECT distinct t.id, t.name, t.short_name, t.description, t.target_type "
            + " FROM target t "
            + " JOIN task_assignment ta ON t.id = ta.target_id "
            + " JOIN task ON task.id = ta.task_id "
            + " JOIN product p ON p.id = task.product_id "
            + " WHERE p.project_id = ? AND ta.assigned_user_id = ? ";
    private static final String SELECT_TARGET_BY_HORSE_ID =
            "SELECT t.* FROM horse h, target t WHERE h.id=? AND h.target_id=t.id";
    private static final String SELECT_TARGETS_BY_PRODUCT_ID_AND_TARGET_ID =
            "SELECT t.* from target t, horse "
            + "WHERE horse.product_id=? and t.id = horse.target_id and t.id !=?";

    private static final String SELECT_TARGETS_BY_PROJECT_ID =
            "SELECT t.* FROM target t "
            + "JOIN project_target pt ON (pt.project_id = ? AND pt.target_id = t.id) ORDER BY t.short_name";
    
    private static final String SELECT_TARGETS_BY_PRODUCT_ID =            
            "SELECT t.* FROM target t, product prod, project_target pt "
            + "WHERE prod.id=? AND pt.project_id=prod.project_id AND t.id=pt.target_id "
            + "ORDER BY t.short_name";

    private static final String SELECT_TARGETS_BY_TARGET_IDS = "SELECT * FROM target WHERE id IN ({0})";
    private final static String SELECT_NOT_CANCELLED_TARGETS_FOR_PRODUCT = "SELECT t.* FROM target t, horse h, "
            + "workflow_object wfo WHERE h.product_id=? AND t.id=h.target_id AND h.workflow_object_id=wfo.id "
            + "AND wfo.status!=5";
    private final static String SELECT_ALL_TARGETS_COUNT = "SELECT count(id) FROM target t WHERE {0} {1} AND status=1";
    private final static String SELECT_ALL_TARGETS =
            "SELECT * FROM target t, organization o "
            + "WHERE {0} {1} AND t.status!=3 AND o.id=t.owner_org_id "
            + "ORDER BY {2} {3} LIMIT {4,number,#},{5,number,#}";
    private final static String SELECT_ALL_TARGETS_BY_ORG_IDS =
            "SELECT * FROM target WHERE visibility=1 OR owner_org_id IN ({0}) ORDER BY name";
    private final static String SELECT_ALL_TARGETS_BY_VISIBILITY =
            "SELECT * FROM target WHERE status=1 AND visibility=? ORDER BY name";
    private final static String SELECT_PUBLIC_TARGETS =
            "SELECT t.* FROM target t WHERE t.status=1 AND t.visibility=1";
    private final static String SELECT_PRIVATE_TARGETS_BY_PROJECT_ID =
            "SELECT DISTINCT t.* FROM target t, project p, project_owner po WHERE p.id=? AND t.status=1 AND t.visibility=p.visibility AND t.visibility=2 "
            + "AND (t.owner_org_id=p.organization_id OR (po.project_id=p.id AND po.org_id=t.owner_org_id))";

    private final static String SELECT_ALL_AVAILABLE_TARGETS_BY_PROJECT_ID =
            "(" + SELECT_PUBLIC_TARGETS + ") UNION DISTINCT ("
            + SELECT_PRIVATE_TARGETS_BY_PROJECT_ID + ")";

    private final static String SELECT_ALL_TARGET_IDS_BY_TAGNAME =
            "SELECT tt.target_id target_id FROM target_tag tt, ttags t WHERE status=1 AND t.term LIKE ? AND tt.ttags_id=t.id";

    private final static String EXISTS_TARGET_NAME = "SELECT COUNT(1) FROM target WHERE id !=? AND name=?";
    private final static String EXISTS_TARGET_SHORT_NAME = "SELECT COUNT(1) FROM target WHERE id !=? AND short_name=?";
    private final static String SELECT_PRIVATE_TARGETS_BY_ORGID =
            "SELECT * FROM target WHERE owner_org_id=? AND visibility=2";
    private final static String COUNT_TARGETS_BY_PROJECT_ID = "SELECT COUNT(1) FROM target t, project_target pt "
            + "WHERE pt.project_id=? AND pt.target_id=t.id";
    private final static String SELECT_ALL_TARGETS_BY_PROJECT_ID =
            "SELECT pt.id project_target_id, t.* FROM target t, project_target pt "
            + "WHERE pt.project_id=? AND pt.target_id=t.id ORDER BY {1} {2} LIMIT {3,number,#},{4,number,#}";
    private static final String SELECT_AVAILABLE_TARGETS_BY_PRODUCT_IDS =
            "SELECT t.* FROM horse h JOIN product prd ON h.product_id=prd.id JOIN target t ON t.id=h.target_id "
            + "JOIN workflow_object wfo ON wfo.id=h.workflow_object_id "
            + "WHERE h.product_id=? ORDER BY t.name";

    public List<Target> selectAvailableTargetsByProductId(int productId) {
        return super.find(SELECT_AVAILABLE_TARGETS_BY_PRODUCT_IDS, productId);
    }

    public boolean existsByName(int targetId, String targetName) {
        return super.count(EXISTS_TARGET_NAME, targetId, targetName) > 0;
    }

    public boolean existsByShortName(int targetId, String targetShortName) {
        return super.count(EXISTS_TARGET_SHORT_NAME, targetId, targetShortName) > 0;
    }

    public List<Target> selectNotCancelledTargetsForProduct(int productId) {
        log.debug("Select not cancelled targets for product: " + productId);

        return super.find(SELECT_NOT_CANCELLED_TARGETS_FOR_PRODUCT, productId);
    }

    public List<Integer> selectAllTargetIdsByTagname(String tagName) {
        return getJdbcTemplate().query(SELECT_ALL_TARGET_IDS_BY_TAGNAME, new Object[]{"%" + tagName + "%"}, new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("target_id");
            }
        });
    }

    /**
     * Select all of the targets in Indaba
     *
     * @return list of target
     */
    public List<Target> selectAllTargets() {
        log.debug("Select all of targets:" + SELECT_TARGETS);

        return super.find(SELECT_TARGETS);
    }

    /**
     * Select all of the targets in Indaba
     *
     * @return list of target
     */
    public List<Target> selectAllTargetsByOrgIds(List<Integer> orgIds) {
        String orgStr = "-1";
        if (orgIds != null && !orgIds.isEmpty()) {
            orgStr = ListUtils.listToString(orgIds);
        }
        String sqlStr = MessageFormat.format(SELECT_ALL_TARGETS_BY_ORG_IDS, orgStr);
        log.debug("Select target count[orgIds=" + orgIds + "]:" + sqlStr);
        return super.find(sqlStr);
    }

    /**
     * Select all of the targets in Indaba
     *
     * @return list of target
     */
    public List<Target> selectAllTargetsByVisibility(int visibility) {
        log.debug("Select all targets [visibility=" + visibility + "]:" + SELECT_ALL_TARGETS_BY_VISIBILITY);
        return super.find(SELECT_ALL_TARGETS_BY_VISIBILITY, visibility);
    }

    /**
     * Select all of the targets in Indaba
     *
     * @return list of target
     */
    public List<Target> selectAllAvailableTargetsByProjectId(int projectId) {
        log.debug("Select all targets [project=" + projectId + "]:" + SELECT_ALL_AVAILABLE_TARGETS_BY_PROJECT_ID);
        return super.find(SELECT_ALL_AVAILABLE_TARGETS_BY_PROJECT_ID, (long) projectId);
    }


    /**
     * Select all of the targets in Indaba
     *
     * @return list of target
     */
    public long selectAllTargetCountWithinIds(List<Integer> fromTargetIds, List<Integer> oaOfOrgIds, int orgId, int visibility, String queryType, String query) {
        if (fromTargetIds == null || fromTargetIds.isEmpty()) {
            return 0;
        }
        String condition = "id IN (" + ListUtils.listToString(fromTargetIds) + ")";
        String sqlStr = MessageFormat.format(SELECT_ALL_TARGETS_COUNT, condition, generateQueryConditionStatement(oaOfOrgIds, orgId, visibility, queryType, query));
        log.debug("Select target count[oaOfOrgIds=" + oaOfOrgIds + ",orgId="
                + orgId + ",visibility=" + visibility + "]:" + sqlStr);
        return super.count(sqlStr);
    }

    /**
     * Select all of the targets in Indaba
     *
     * @return list of target
     */
    public long selectAllTargetCount(List<Integer> oaOfOrgIds, int orgId, int visibility, String queryType, String query) {
        String sqlStr = MessageFormat.format(SELECT_ALL_TARGETS_COUNT, "1=1", generateQueryConditionStatement(oaOfOrgIds, orgId, visibility, queryType, query));
        log.debug("Select target count[oaOfOrgIds=" + oaOfOrgIds + ",orgId="
                + orgId + ",visibility=" + visibility + "]:" + sqlStr);
        return super.count(sqlStr);
    }

    static private String getSortColumnName(String sortName) {
        if (sortName == null) {
            return "t.name";
        } else if (sortName.equalsIgnoreCase("id")) {
            return "t.id";
        } else if (sortName.equalsIgnoreCase("shortname")) {
            return "t.short_name";
        } else if (sortName.equalsIgnoreCase("description")) {
            return "t.description";
        } else if (sortName.equalsIgnoreCase("type")) {
            return "t.type";
        } else if (sortName.equalsIgnoreCase("orgname")) {
            return "o.name";
        } else {
            return "t.name";
        }
    }

    /**
     * Select all of the targets by limit in Indaba
     *
     * @return list of target
     */
    public List<Target> selectAllTargetsWithinIds(List<Integer> fromTargetIds, List<Integer> oaOfOrgIds, int orgId, int visibility, String sortName, String sortOrder, int offset, int count, String queryType, String query) {
        if (fromTargetIds == null || fromTargetIds.isEmpty()) {
            return null;
        }
        String condition = "t.id IN (" + ListUtils.listToString(fromTargetIds) + ")";

        sortName = getSortColumnName(sortName);

        String sqlStr = MessageFormat.format(SELECT_ALL_TARGETS, condition,
                generateQueryConditionStatement(oaOfOrgIds, orgId, visibility, queryType, query),
                sortName, sortOrder, offset, offset + count);

        log.debug("Select all of targets by limit[oaOfOrgIds=" + oaOfOrgIds + ",orgId="
                + orgId + ",visibility=" + visibility + ",sortName=" + sortName + ",sortOrder=" + sortName
                + ",offset=" + offset + ",count=" + count + "]:" + sqlStr);
        return super.find(sqlStr);
    }

    /**
     * Select all of the targets by limit in Indaba
     *
     * @return list of target
     */
    public List<Target> selectAllTargets(List<Integer> oaOfOrgIds, int orgId, int visibility, String sortName, String sortOrder, int offset, int count, String queryType, String query) {

        sortName = getSortColumnName(sortName);

        String sqlStr = MessageFormat.format(SELECT_ALL_TARGETS, "1=1",
                generateQueryConditionStatement(oaOfOrgIds, orgId, visibility, queryType, query),
                sortName, sortOrder, offset, offset + count);

        log.debug("Select all of targets by limit[oaOfOrgIds=" + oaOfOrgIds + ",orgId="
                + orgId + ",visibility=" + visibility + ",sortName=" + sortName + ",sortOrder=" + sortName
                + ",offset=" + offset + ",count=" + count + "]:" + sqlStr);
        return super.find(sqlStr);
    }

    /**
     * Select all of the targets in Indaba
     *
     * @return list of target
     */
    public long countTargetByProjectId(int projectId) {
        log.debug("Select target count[projectId=" + projectId + "]:" + COUNT_TARGETS_BY_PROJECT_ID);
        return super.count(COUNT_TARGETS_BY_PROJECT_ID, projectId);
    }

    /**
     * Select all of the targets by limit in Indaba
     *
     * @return list of target
     */
    public List<ProjectTargetVO> selectAllTargetsByProjectId(int projectId, String sortName, String sortOrder, int offset, int count) {

        sortName = getSortColumnName(sortName);

        String sqlStr = MessageFormat.format(SELECT_ALL_TARGETS_BY_PROJECT_ID, projectId,
                sortName, sortOrder, offset, offset + count);

        log.debug("Select all of targets[projectId=" + projectId + "]:" + sqlStr);

        RowMapper mapper = new RowMapper() {

            public ProjectTargetVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                ProjectTargetVO target = new ProjectTargetVO();
                target.setProjectTargetId(rs.getInt("pt.project_target_id"));
                target.setId(rs.getInt("t.id"));
                target.setName(rs.getString("t.name"));
                target.setShortName(rs.getString("t.short_name"));
                target.setTargetType(rs.getShort("t.target_type"));
                target.setDescription(rs.getString("t.description"));
                return target;
            }
        };

        return getJdbcTemplate().query(sqlStr, new Object[]{projectId}, new int[]{INTEGER}, mapper);
    }

    /**
     * Select all of the targets by limit in Indaba
     *
     * @return list of target
     */
    public List<Target> selectAllTargets(int offset, int count) {
        log.debug("Select all of targets by limit:" + SELECT_TARGETS_BY_LIMIT);

        return super.find(MessageFormat.format(SELECT_TARGETS_BY_LIMIT, offset, offset + count));
    }

    /**
     * Select all of the target count
     *
     * @return list of target
     */
    public int selectTargetCount() {
        log.debug("Select target count:" + SELECT_TARGETS_COUNT);

        return Long.valueOf(super.count(SELECT_TARGETS_COUNT)).intValue();
    }

    /**
     * Select target by the specified id
     *
     * @param targetId
     * @return
     */
    public Target selectTargetById(int targetId) {
        log.debug("Select target by id: " + targetId + ". [" + SELECT_TARGET_BY_ID + "].");

        return super.findSingle(SELECT_TARGET_BY_ID, targetId);
    }

    /**
     * Select target by the horse id
     *
     * @param targetId
     * @return
     */
    public Target selectTargetByHorseId(int horseId) {
        log.debug("Select target by horse id: " + horseId + ". [" + SELECT_TARGET_BY_HORSE_ID + "].");

        return super.findSingle(SELECT_TARGET_BY_HORSE_ID, horseId);
    }

    public List<Target> selectTargetByProductIdAndTargetId(int productId, int targetId) {
        logger.debug("Select targets by product ID and targetId.");
        RowMapper mapper = new RowMapper() {

            public Target mapRow(ResultSet rs, int rowNum) throws SQLException {
                Target target = new Target();
                target.setId(rs.getInt("id"));
                target.setName(rs.getString("short_name"));
                target.setTargetType(rs.getShort("target_type"));
                target.setDescription(rs.getString("description"));
                return target;
            }
        };

        return getJdbcTemplate().query(this.SELECT_TARGETS_BY_PRODUCT_ID_AND_TARGET_ID,
                new Object[]{productId, targetId},
                new int[]{INTEGER, INTEGER},
                mapper);
    }

    public List<Target> selectTargetsByFilter(int userId, List<Integer> targetFilter, int projectId) {
        RowMapper mapper = new RowMapper() {

            public Target mapRow(ResultSet rs, int rowNum) throws SQLException {
                Target target = new Target();
                target.setId(rs.getInt("id"));
                target.setName(rs.getString("short_name"));
                target.setTargetType(rs.getShort("target_type"));
                target.setDescription(rs.getString("description"));
                return target;
            }
        };

        List<Target> retList = getJdbcTemplate().query(SELECT_TARGETS_BY_USER_ID_AND_PROJ_ID,
                new Object[]{projectId, userId},
                new int[]{BIGINT, BIGINT},
                mapper);

        if (retList.isEmpty()) {
            return (targetFilter.get(0).equals(1)) ? null : retList;
        }

        int i;
        String op1 = (targetFilter.get(0).equals(1)) ? " AND " : " OR ";
        String op2 = (targetFilter.get(0).equals(1)) ? " <> " : " = ";
        StringBuffer sb = new StringBuffer(SELECT_TARGETS_BY_USER_ID_AND_PROJ_ID);
        if (targetFilter.size() > 1) {
            sb.append(" AND (");
            for (i = 1; i < targetFilter.size() - 1; i++) {
                sb.append("t.id" + op2 + targetFilter.get(i) + op1);
            }
            sb.append("t.id" + op2 + targetFilter.get(i) + ")");
        } else if (targetFilter.get(0).equals(0)) {
            return null;
        }
        List<Target> retList1 = getJdbcTemplate().query(sb.toString(),
                new Object[]{projectId, userId},
                new int[]{BIGINT, BIGINT},
                mapper);
        return retList1.isEmpty() ? null : retList;
    }

    public List<Target> selectTargetsByUserId(int userId) {
        logger.debug("Select targets by user ID.");
        RowMapper mapper = new RowMapper() {

            public Target mapRow(ResultSet rs, int rowNum) throws SQLException {
                Target target = new Target();
                target.setId(rs.getInt("id"));
                target.setName(rs.getString("name"));
                target.setTargetType(rs.getShort("target_type"));
                target.setDescription(rs.getString("description"));
                return target;
            }
        };

        return getJdbcTemplate().query(SELECT_TARGETS_BY_USER_ID,
                new Object[]{userId},
                new int[]{BIGINT},
                mapper);
    }

    public List<Target> selectTargetsByUserAndProject(int userId, int projectId) {
        logger.debug("Select targets by user ID + project ID.");
        RowMapper mapper = new RowMapper() {

            public Target mapRow(ResultSet rs, int rowNum) throws SQLException {
                Target target = new Target();
                target.setId(rs.getInt("id"));
                target.setName(rs.getString("short_name"));
                target.setTargetType(rs.getShort("target_type"));
                target.setDescription(rs.getString("description"));
                return target;
            }
        };

        return getJdbcTemplate().query(SELECT_TARGETS_BY_USER_ID_AND_PROJ_ID,
                new Object[]{projectId, userId},
                new int[]{BIGINT, BIGINT},
                mapper);
    }

    public List<Target> selectTargetsByTargetIds(List<Integer> targetIds) {
        logger.debug("Select target by ids: " + targetIds);
        if (targetIds == null || targetIds.isEmpty()) {
            return null;
        }

        String ids = targetIds.toString();
        String sqlStr = MessageFormat.format(SELECT_TARGETS_BY_TARGET_IDS, ids.substring(1, ids.length() - 1));

        return super.find(sqlStr);
    }

    public List<Target> selectTargetsByProjectId(int projectId) {
        return super.find(SELECT_TARGETS_BY_PROJECT_ID, projectId);
    }

    public List<Target> selectTargetsByProductId(int productId) {
        return super.find(SELECT_TARGETS_BY_PRODUCT_ID, productId);
    }

    private String generateQueryConditionStatement(List<Integer> oaOfOrgIds, int orgId, int visibility, String queryType, String query) {
        StringBuilder sb = new StringBuilder();
        String orgs = null;
        if (oaOfOrgIds != null && !oaOfOrgIds.isEmpty()) {
            StringBuilder orgIds = new StringBuilder("(");
            for (int v : oaOfOrgIds) {
                orgIds.append(v).append(',');
            }
            orgIds.setLength(orgIds.length() - 1);
            orgIds.append(')');
            orgs = orgIds.toString();
        }

        if (visibility > 0 && orgId > 0) {
            // return targets of specified org
            sb.append(" AND t.visibility=").append(visibility);
            if (visibility == Constants.VISIBILITY_PRIVATE) {
                if (oaOfOrgIds == null || oaOfOrgIds.contains(orgId)) {
                    sb.append(" AND t.owner_org_id=").append(orgId);
                } else {
                    sb.append(" AND (1=0)");
                }
            } else {
                sb.append(" AND t.owner_org_id=").append(orgId);
            }
        } else if (visibility > 0 && orgId <= 0) {
            // org not specified
            sb.append("AND t.visibility=").append(visibility);
            if (visibility == Constants.VISIBILITY_PRIVATE) {
                if (oaOfOrgIds != null) {
                    sb.append(" AND t.owner_org_id IN ").append(orgs);
                }
            }
        } else if (visibility <= 0 && orgId > 0) {
            sb.append("t.owner_org_id=").append(orgId);
            if (oaOfOrgIds != null && !oaOfOrgIds.contains(orgId)) {
                sb.append(" AND t.visibility=").append(Constants.VISIBILITY_PUBLIC);
            }
        } else {
            if (oaOfOrgIds != null) {
                sb.append(" AND (t.owner_org_id IN ").append(orgs).append(" OR t.visibility=").append(Constants.VISIBILITY_PUBLIC).append(')');
            }
        }

        if (queryType != null && query != null && !query.isEmpty()) {
            if (queryType.equalsIgnoreCase("name")) {
                sb.append(" AND t.name LIKE \"%").append(query).append("%\"");
            } else if (queryType.equalsIgnoreCase("description")) {
                sb.append(" AND t.description LIKE \"%").append(query).append("%\"");
            } else if (queryType.equalsIgnoreCase("shortname")) {
                sb.append(" AND t.short_name LIKE \"%").append(query).append("%\"");
            }
        }

        return sb.toString();
    }

    public List<Target> selectPrivateTargetByOrg(int orgId) {
        return super.find(SELECT_PRIVATE_TARGETS_BY_ORGID, orgId);
    }
    private static final String SELECT_TARGETS_BY_PROJ_ORG_VISIBILITY =
            "SELECT t.* FROM target t, project_target pt "
            + "WHERE pt.project_id=? AND t.owner_org_id=? AND t.visibility=? AND pt.target_id=t.id";

    public List<Target> selectTargetsByProjectAndOrg(int projId, int orgId, int visibility) {
        // get all targets owned by the org and used in the project with specified visibility

        return super.find(SELECT_TARGETS_BY_PROJ_ORG_VISIBILITY, (long) projId, orgId, visibility);
    }
}
