/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Product;
import com.ocs.util.Pair;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Types.INTEGER;
import java.text.MessageFormat;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Jeff
 */
public class ProductDAO extends SmartDaoMySqlImpl<Product, Integer> {

    private static final Logger log = Logger.getLogger(ProductDAO.class);
    private static final String SELECT_PRODUCTS =
            "SELECT * FROM product ORDER BY name";
    private static final String SELECT_PRODUCT_BY_ID =
            "SELECT * FROM product WHERE id=? ORDER BY name";
    private static final String SELECT_PRODUCT_NAME_BY_ID =
            "SELECT * FROM product WHERE id=?";
    private static final String SELECT_PRODUCT_BY_HORSE_ID =
            "SELECT prd.* FROM horse h, product prd WHERE h.id=? AND h.product_id=prd.id";
    private static final String SELECT_PRODUCT_BY_GOAL_OBJECT_ID =
            "SELECT prd.* FROM goal_object go, sequence_object so, workflow_object wo, product prd "
            + "WHERE go.id=? AND go.sequence_object_id=so.id AND so.workflow_object_id=wo.id AND wo.workflow_id=prd.workflow_id";
    private static final String SELECT_PRODUCT_BY_TASK_ID =
            "SELECT prd.* FROM task t, goal g, workflow_sequence wfseq, product prd "
            + "WHERE t.id=? AND t.goal_id=g.id AND g.workflow_sequence_id=wfseq.id AND wfseq.workflow_id=prd.workflow_id";

    private static final String SELECT_PRODUCTS_BY_PROJECT_ID =
            "SELECT * FROM product WHERE project_id = ?";

    private static final String SELECT_ACTIVE_PRODUCTS_BY_PROJECT_ID =
            "SELECT * FROM product WHERE mode != 1 AND project_id = ?";

    private static final String SELECT_USER_PRODUCTS_BY_PROJECT_ID =
            "SELECT DISTINCT prod.* FROM product prod, task t,task_role tr, project_membership pm "
            + "WHERE prod.project_id=? AND t.product_id=prod.id AND tr.task_id=t.id AND tr.can_claim!=0 "
            + "AND pm.user_id=? AND pm.project_id=prod.project_id AND pm.role_id=tr.role_id "
            + "ORDER BY prod.name";

    private static final String SELECT_USER_ACTIVE_PRODUCTS_BY_PROJECT_ID =
            "SELECT DISTINCT prod.* FROM product prod, task t,task_role tr, project_membership pm "
            + "WHERE prod.mode != 1 AND prod.project_id=? AND t.product_id=prod.id AND tr.task_id=t.id AND tr.can_claim!=0 "
            + "AND pm.user_id=? AND pm.project_id=prod.project_id AND pm.role_id=tr.role_id "
            + "ORDER BY prod.name";

    private static final String SELECT_PRODUCT_COUNT_BY_PROJECT_ID =
            "SELECT COUNT(id) FROM product WHERE project_id = ?";
    private static final String SELECT_PRODUCTS_BY_PROJECT_ID_WITH_PAGINATION =
            "SELECT * FROM product WHERE project_id = ? ORDER BY {0} {1} LIMIT {2},{3}";
    private static final String SELECT_PRODUCT_BY_NAME = "SELECT * FROM product WHERE name=?";
    private static final String COUNT_TASK_OF_GOAL_BY_PRODUCT_ID =
            "SELECT goal_id_list.id goal_id, count(t.id) COUNT FROM (SELECT g.id FROM goal g "
            + "JOIN workflow_sequence ws ON ws.id=g.workflow_sequence_id "
            + "JOIN workflow w ON w.id=ws.workflow_id "
            + "JOIN product p ON p.workflow_id=w.id "
            + "WHERE p.id=?) goal_id_list LEFT OUTER JOIN task t ON t.goal_id=goal_id_list.id GROUP BY goal_id_list.id";
    private static final String UPDATE_PRODUCT_MODE = "UPDATE product SET mode=? WHERE id=?";
    private static final String SELECT_SURVEY_PRODUCTS = "SELECT * FROM product WHERE product_config_id=? AND content_type=0";

    /**
     * Select all of the products in Indaba
     *
     * @return list of product
     */
    public List<Product> selectAllProducts() {
        log.debug("Select all of products:" + SELECT_PRODUCTS);

        return super.find(SELECT_PRODUCTS);
    }

    public Product getProductByName(String prodName) {
        return super.findSingle(SELECT_PRODUCT_BY_NAME, prodName);
    }

    public List<Pair<Integer, Integer>> countTasksOfGoalByProductName(int prodId) {
        log.debug("Check if exists by project id: " + prodId + ". [" + COUNT_TASK_OF_GOAL_BY_PRODUCT_ID + "].");
        RowMapper mapper = new RowMapper() {

            public Pair<Integer, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Pair<Integer, Integer>(rs.getInt("goal_id"), rs.getInt("COUNT"));
            }
        };
        return getJdbcTemplate().query(
                COUNT_TASK_OF_GOAL_BY_PRODUCT_ID,
                new Object[]{prodId},
                new int[]{INTEGER},
                mapper);
    }

    public List<Product> selectProductsByProjectId(int projectId) {
        return super.find(SELECT_PRODUCTS_BY_PROJECT_ID, projectId);
    }

    public List<Product> selectActiveProductsByProjectId(int projectId) {
        return super.find(SELECT_ACTIVE_PRODUCTS_BY_PROJECT_ID, projectId);
    }

    public List<Product> selectUserProductsByProjectId(int projectId, int userId) {
        return super.find(SELECT_USER_PRODUCTS_BY_PROJECT_ID, (Object)projectId, userId);
    }

    public List<Product> selectUserActiveProductsByProjectId(int projectId, int userId) {
        return super.find(SELECT_USER_ACTIVE_PRODUCTS_BY_PROJECT_ID, (Object)projectId, userId);
    }

    public long countOfProductByProjectId(int projectId) {
        return super.count(SELECT_PRODUCT_COUNT_BY_PROJECT_ID, projectId);
    }

    public List<Product> selectProductsByProjectId(int projectId, String sortName, String sortOrder, int offset, int count) {
        if ("PRODNAME".equalsIgnoreCase(sortName)) {
            sortName = "name";
        } else if ("TYPE".equalsIgnoreCase(sortName)) {
            sortName = "type";
        } else if ("MODE".equalsIgnoreCase(sortName)) {
            sortName = "mode";
        } else {
            sortName = "name";
        }
        String sqlStr = MessageFormat.format(SELECT_PRODUCTS_BY_PROJECT_ID_WITH_PAGINATION, sortName, sortOrder, offset, count);
        logger.debug("Select project memberships of project[projId=" + projectId + "]: " + sqlStr);
        return super.find(sqlStr, projectId);
    }

    public Product selectProductById(int productId) {
        log.debug("Select product by id: " + productId + ". [" + SELECT_PRODUCT_BY_ID + "].");

        return super.findSingle(SELECT_PRODUCT_BY_ID, productId);
    }

    public Product selectProductByHorseId(int horseId) {
        log.debug("Select product by horse id: " + horseId + ". [" + SELECT_PRODUCT_BY_HORSE_ID + "].");

        return super.findSingle(SELECT_PRODUCT_BY_HORSE_ID, horseId);
    }

    public Product selectProductByTaskId(int taskId) {
        log.debug("Select product by task id: " + taskId + ". [" + SELECT_PRODUCT_BY_TASK_ID + "].");

        return super.findSingle(SELECT_PRODUCT_BY_TASK_ID, taskId);
    }

    public Product selectProductByGoalObjectId(int goalObjectId) {
        log.debug("Select product by goal object id: " + goalObjectId + ". [" + SELECT_PRODUCT_BY_GOAL_OBJECT_ID + "].");

        return super.findSingle(SELECT_PRODUCT_BY_GOAL_OBJECT_ID, goalObjectId);
    }

    public String selectProductNameById(int productId) {
        log.debug("Select product Name by id: " + productId + ". [" + SELECT_PRODUCT_BY_ID + "].");
        RowMapper mapper = new RowMapper() {

            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("name");
            }
        };

        List<String> list = getJdbcTemplate().query(
                SELECT_PRODUCT_NAME_BY_ID,
                new Object[]{productId},
                new int[]{INTEGER},
                mapper);

        return list.get(0);
    }

    public void updateProductMode(int productId, short newMode) {
        super.update(UPDATE_PRODUCT_MODE, newMode, productId);
    }

    public List<Product> selectSurveyProduct(int surveyConfigId) {
        return super.find(SELECT_SURVEY_PRODUCTS, surveyConfigId);
    }
}
