/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Organization;
import com.ocs.indaba.po.Product;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.text.MessageFormat;
import com.ocs.util.ListUtils;
import com.ocs.indaba.po.SurveyConfig;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import static java.sql.Types.INTEGER;

/**
 *
 * @author sjf
 */
public class SurveyConfigDAO extends SmartDaoMySqlImpl<SurveyConfig, Integer> {

    private static final Logger log = Logger.getLogger(SurveyConfigDAO.class);
    private static final String GET_INSTRUCTIONS_BY_HORSEID =
            "select * from survey_config where id="
            + "(select survey_config_id from survey_content_object where content_header_id  = "
            + "(select content_header_id from horse where id=?))";
    private static final String SELECT_SURVEY_CONFIG_BY_INDICATOR_ID =
            "SELECT sc.name FROM survey_config sc, survey_question sq WHERE sq.survey_indicator_id=? AND sq.survey_config_id=sc.id";
    private static final String SELECT_ACTIVE_SURVEY_CONFIG_BY_INDICATOR_ID =
            "SELECT sc.name FROM survey_config sc, survey_question sq, product WHERE "
            + "sq.survey_indicator_id=? AND sq.survey_config_id=sc.id "
            + "AND product.content_type=0 AND product.product_config_id=sc.id AND product.mode != 1";
    private static final String SELECT_ACTIVE_SURVEY_CONFIGS =
            "SELECT * FROM survey_config WHERE status=1 AND visibility=? ORDER BY name";
    private static final String SELECT_FROM_SURVEY_CONFIG_FOR_SA_BASE =
            "FROM ((SELECT sc.* FROM survey_config sc, sc_contributor scc WHERE scc.org_id IN ({0}) AND sc.id = scc.survey_config_id) "
            + "UNION DISTINCT (SELECT sc.* FROM survey_config sc WHERE sc.owner_org_id IN ({0}))) sc ";
    private static final String SELECT_FROM_SURVEY_CONFIG_FOR_NONSA_BASE =
            "FROM (SELECT sc.* FROM survey_config sc JOIN sc_contributor scc ON sc.id = scc.survey_config_id WHERE sc.visibility=1 AND scc.org_id IN ({0}) "
            + "UNION DISTINCT SELECT sc.* FROM survey_config sc WHERE sc.visibility=1 AND sc.owner_org_id IN ({0}) "
            + "UNION DISTINCT SELECT sc.* FROM survey_config sc WHERE sc.visibility=2) sc ";
    //////////////////////////////////////////////////////////////////
    // For SA
    //////////////////////////////////////////////////////////////////
    private static final String COUNT_SURVEY_CONFIGS_FOR_SA =
            "SELECT COUNT(1) FROM survey_config";
    private static final String COUNT_SURVEY_CONFIGS_FOR_SA_BY_VISIBILITY =
            "SELECT COUNT(1) FROM survey_config WHERE visibility={0}";
    private static final String COUNT_SURVEY_CONFIGS_FOR_SA_BY_ORGIDS =
            "SELECT COUNT(1) " + SELECT_FROM_SURVEY_CONFIG_FOR_SA_BASE;
    private static final String COUNT_SURVEY_CONFIGS_FOR_SA_BY_ORGIDS_AND_VISIBLITY =
            "SELECT COUNT(1) " + SELECT_FROM_SURVEY_CONFIG_FOR_SA_BASE + " WHERE sc.visibility={1}";
    private static final String SELECT_SURVEY_CONFIGS_FOR_SA =
            "SELECT DISTINCT * FROM survey_config sc ORDER BY {0} {1} LIMIT {2,number,#},{3,number,#}";
    private static final String SELECT_SURVEY_CONFIGS_FOR_SA_BY_VISIBILITY =
            "SELECT DISTINCT * FROM survey_config sc WHERE visibility={0} ORDER BY {1} {2} LIMIT {3,number,#},{4,number,#}";
    private static final String SELECT_SURVEY_CONFIGS_FOR_SA_BY_ORGIDS =
            "SELECT DISTINCT sc.* " + SELECT_FROM_SURVEY_CONFIG_FOR_SA_BASE
            + "ORDER BY {1} {2} LIMIT {3,number,#},{4,number,#}";
    private static final String SELECT_SURVEY_CONFIGS_FOR_SA_BY_ORGIDS_AND_VISIBLITY =
            "SELECT DISTINCT sc.* " + SELECT_FROM_SURVEY_CONFIG_FOR_SA_BASE + " WHERE sc.visibility={1} "
            + "ORDER BY {2} {3} LIMIT {4,number,#},{5,number,#}";
    //////////////////////////////////////////////////////////////////
    // For non-SA
    //////////////////////////////////////////////////////////////////
    private static final String COUNT_SURVEY_CONFIGS_FOR_NONSA =
            "SELECT COUNT(1) " + SELECT_FROM_SURVEY_CONFIG_FOR_NONSA_BASE;
    private static final String COUNT_SURVEY_CONFIGS_FOR_NONSA_BY_VISIBILITY =
            "SELECT COUNT(1) " + SELECT_FROM_SURVEY_CONFIG_FOR_NONSA_BASE + " WHERE sc.visibility={1}";
    private static final String COUNT_SURVEY_CONFIGS_FOR_NONSA_BY_ORGIDS =
            "SELECT COUNT(1) " + SELECT_FROM_SURVEY_CONFIG_FOR_NONSA_BASE;
    private static final String COUNT_SURVEY_CONFIGS_FOR_NONSA_BY_ORGIDS_AND_VISIBLITY =
            "SELECT COUNT(1) " + SELECT_FROM_SURVEY_CONFIG_FOR_NONSA_BASE + " WHERE sc.visibility={1}";
    ///////////////////
    private static final String SELECT_SURVEY_CONFIGS_FOR_NONSA =
            "SELECT DISTINCT * " + SELECT_FROM_SURVEY_CONFIG_FOR_NONSA_BASE + " ORDER BY {1} {2} LIMIT {3,number,#},{4,number,#}";
    private static final String SELECT_SURVEY_CONFIGS_FOR_NONSA_BY_VISIBILITY =
            "SELECT DISTINCT * " + SELECT_FROM_SURVEY_CONFIG_FOR_NONSA_BASE + " WHERE sc.visibility={1} ORDER BY {2} {3} LIMIT {4,number,#},{5,number,#}";
    private static final String SELECT_SURVEY_CONFIGS_FOR_NONSA_BY_ORGIDS =
            "SELECT DISTINCT sc.* " + SELECT_FROM_SURVEY_CONFIG_FOR_NONSA_BASE
            + " ORDER BY {1} {2} LIMIT {3,number,#},{4,number,#}";
    private static final String SELECT_SURVEY_CONFIGS_FOR_NONSA_BY_ORGIDS_AND_VISIBLITY =
            "SELECT DISTINCT sc.* " + SELECT_FROM_SURVEY_CONFIG_FOR_NONSA_BASE + " WHERE sc.visibility={1} "
            + "ORDER BY {2} {3} LIMIT {4,number,#},{5,number,#}";
    private static final String SELECT_ORGANIZATION_MAP_BY_SURVEY_CONFIG_IDS =
            "SELECT o.*, sc.survey_config_id survey_config_id FROM organization o, ( "
            + "SELECT sc.id survey_config_id, sc.owner_org_id org_id FROM survey_config sc WHERE sc.id IN ({0}) "
            + "UNION DISTINCT SELECT scc.survey_config_id survey_config_id, scc.org_id org_id FROM sc_contributor scc WHERE scc.survey_config_id IN ({0}) "
            + ") sc WHERE o.id = sc.org_id";
    private static final String SELECT_ORGIDS_BY_SURVEY_CONFIG_ID =
            "SELECT DISTINCT o.id org_id FROM organization o, ( "
            + "SELECT sc.id survey_config_id, sc.owner_org_id org_id FROM survey_config sc WHERE sc.id=? "
            + "UNION DISTINCT SELECT scc.survey_config_id survey_config_id, scc.org_id org_id FROM sc_contributor scc WHERE scc.survey_config_id=? "
            + ") sc WHERE o.id = sc.org_id";

    private static final String SELECT_BY_NAME = "SELECT * FROM survey_config WHERE name=?";

    private static final String CHECK_SURVEY_CONFIG_IN_ACTIVE_USE =
            "SELECT product_config_id, COUNT(1) count FROM product "
            + "WHERE product_config_id IN ({0}) AND content_type=0 AND mode!=1 GROUP BY product_config_id";

    private static final String CHECK_SURVEY_CONFIG_IN_USE =
            "SELECT product_config_id, COUNT(1) count FROM product "
            + "WHERE product_config_id IN ({0}) AND content_type=0 GROUP BY product_config_id";

    private static final String CHECK_SURVEY_CONFIG_IN_ACTIVE_USE_BY_ID =
            "SELECT COUNT(1) count FROM product "
            + "WHERE product_config_id=? AND content_type=0 AND mode!=1";

    private static final String CHECK_SURVEY_CONFIG_IN_USE_BY_ID =
            "SELECT COUNT(1) count FROM product "
            + "WHERE product_config_id=? AND content_type=0";

    private static final String SELECT_OWNER_OF_SURVEY_CONFIG =
            "SELECT id sc_id, owner_org_id org_id FROM survey_config WHERE id IN ({0}) "
            + "UNION SELECT id sc_id, owner_org_id org_id FROM survey_config WHERE id IN ({0}) "
            + "UNION SELECT survey_config_id sc_id, scc.org_id org_id FROM sc_contributor scc WHERE scc.survey_config_id IN ({0})";

    private static final String CHECK_OWNERED_SURVEY_CONFIG =
            "SELECT sc_id, COUNT(*) count FROM (SELECT id sc_id, owner_org_id org_id FROM survey_config WHERE id IN ({0}) "
            + "UNION SELECT id sc_id, owner_org_id org_id FROM survey_config WHERE id IN ({0}) "
            + "UNION SELECT survey_config_id sc_id, scc.org_id org_id FROM sc_contributor scc WHERE scc.survey_config_id IN ({0})) sc_org "
            + "WHERE sc_org.org_id IN ({1})";

    public SurveyConfig selectByName(String surveyConfigName) {
        return super.findSingle(SELECT_BY_NAME, surveyConfigName);
    }

    public List<SurveyConfig> selectAllActiveSurveyConfigs(int visiblity) {
        return super.find(SELECT_ACTIVE_SURVEY_CONFIGS, visiblity);
    }

    public String getInstructionsbyHorseId(int horseId) {
        log.debug(GET_INSTRUCTIONS_BY_HORSEID + horseId);
        String temp = super.findSingle(GET_INSTRUCTIONS_BY_HORSEID, horseId).getInstructions();
        //log.debug("result: "+temp);
        return temp;
    }

    public List<String> getSurveyConfigNameByIndicatorId(int indicatorId) {
        RowMapper mapper = new RowMapper() {

            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("name");
            }
        };

        List<String> list = getJdbcTemplate().query(
                SELECT_SURVEY_CONFIG_BY_INDICATOR_ID,
                new Object[]{indicatorId},
                new int[]{INTEGER},
                mapper);

        return list;
    }

    public List<String> getActiveSurveyConfigNameByIndicatorId(int indicatorId) {
        RowMapper mapper = new RowMapper() {

            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("name");
            }
        };

        List<String> list = getJdbcTemplate().query(
                SELECT_ACTIVE_SURVEY_CONFIG_BY_INDICATOR_ID,
                new Object[]{indicatorId},
                new int[]{INTEGER},
                mapper);

        return list;
    }

    public Map<Integer, List<Integer>> selectOwnedOrgIdsOfSurveyConfig(List<Integer> scIds) {
        final Map<Integer, List<Integer>> ownedScOrgMap = new HashMap<Integer, List<Integer>>();
        if (ListUtils.isEmptyList(scIds)) {
            return ownedScOrgMap;
        }
        RowMapper mapper = new RowMapper() {
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                int scId = rs.getInt("id");
                int orgId = rs.getInt("org_id");
                List<Integer> orgIds = ownedScOrgMap.get(scId);
                if (orgIds == null) {
                    orgIds = new ArrayList<Integer>();
                    ownedScOrgMap.put(scId, orgIds);
                }
                orgIds.add(orgId);
                return scId;
            }
        };

        getJdbcTemplate().query(MessageFormat.format(SELECT_OWNER_OF_SURVEY_CONFIG, ListUtils.listToString(scIds)),
                mapper);
        return ownedScOrgMap;
    }

    public Map<Integer, Boolean> checkIfOwnedSurveyConfigs(List<Integer> scIds, List<Integer> userOwnedOrgIds) {
        final Map<Integer, Boolean> ownedScOrgMap = new HashMap<Integer, Boolean>();
        if (ListUtils.isEmptyList(scIds) || ListUtils.isEmptyList(userOwnedOrgIds)) {
            return ownedScOrgMap;
        }

        RowMapper mapper = new RowMapper() {

            public Boolean mapRow(ResultSet rs, int rowNum) throws SQLException {
                return ownedScOrgMap.put(rs.getInt("sc_id"), (rs.getInt("count") > 0));
            }
        };
        String sqlStr = MessageFormat.format(CHECK_OWNERED_SURVEY_CONFIG, ListUtils.listToString(scIds), ListUtils.listToString(userOwnedOrgIds));

        getJdbcTemplate().query(sqlStr, mapper);
        return ownedScOrgMap;
    }

    public long selectSurveyConfigCountForSA(int visibility, List<Integer> orgIds) {
        String sqlStr = "";
        if (visibility <= 0) {
            if (ListUtils.isEmptyList(orgIds) || orgIds.contains(-1)/*all*/) { // visibility = all & orgId = all
                sqlStr = COUNT_SURVEY_CONFIGS_FOR_SA;
            } else { // visibility = all & orgId = specified
                sqlStr = MessageFormat.format(COUNT_SURVEY_CONFIGS_FOR_SA_BY_ORGIDS, ListUtils.listToString(orgIds));
            }
        } else {
            if (ListUtils.isEmptyList(orgIds) || orgIds.contains(-1)/*all*/) {// visibility = specified & orgId = all
                sqlStr = MessageFormat.format(COUNT_SURVEY_CONFIGS_FOR_SA_BY_VISIBILITY, visibility);
            } else {  // visibility = specified & orgId = specified
                sqlStr = MessageFormat.format(COUNT_SURVEY_CONFIGS_FOR_SA_BY_ORGIDS_AND_VISIBLITY, ListUtils.listToString(orgIds), visibility);
            }
        }
        logger.debug("SQL String[count of survey config]: " + sqlStr);
        return super.count(sqlStr);
    }

    public List<SurveyConfig> selectSurveyConfigsWithPaginationForSA(int visibility, List<Integer> orgIds, String sortName, String sortOrder, int offset, int count) {
        String sqlStr = "";
        sortName = "sc.name";
        if (visibility <= 0) {
            if (ListUtils.isEmptyList(orgIds) || orgIds.contains(-1)/*all*/) { // visibility = all & orgId = all
                sqlStr = MessageFormat.format(SELECT_SURVEY_CONFIGS_FOR_SA, sortName, sortOrder, offset, count);
            } else { // visibility = all & orgId = specified
                sqlStr = MessageFormat.format(SELECT_SURVEY_CONFIGS_FOR_SA_BY_ORGIDS, ListUtils.listToString(orgIds), sortName, sortOrder, offset, count);
            }
        } else {
            if (ListUtils.isEmptyList(orgIds) || orgIds.contains(-1)/*all*/) {// visibility = specified & orgId = all
                sqlStr = MessageFormat.format(SELECT_SURVEY_CONFIGS_FOR_SA_BY_VISIBILITY, visibility, sortName, sortOrder, offset, count);
            } else { // visibility = specified & orgId = specified
                sqlStr = MessageFormat.format(SELECT_SURVEY_CONFIGS_FOR_SA_BY_ORGIDS_AND_VISIBLITY, ListUtils.listToString(orgIds), visibility, sortName, sortOrder, offset, count);
            }
        }
        logger.debug("SQL String: " + sqlStr);
        return super.find(sqlStr);
    }

    public long selectSurveyConfigCountForNonSA(List<Integer> userOwnedOrgIds, int visibility, List<Integer> orgIds) {
        String sqlStr = "";
        if (userOwnedOrgIds == null) {
            userOwnedOrgIds = new ArrayList<Integer>();
        }
        if (userOwnedOrgIds.isEmpty()) {
            userOwnedOrgIds.add(-1);
        }
        if (visibility <= 0) {
            if (ListUtils.isEmptyList(orgIds) || orgIds.contains(-1)/*all*/) { // visibility = all & orgId = all
                sqlStr = MessageFormat.format(COUNT_SURVEY_CONFIGS_FOR_NONSA, ListUtils.listToString(userOwnedOrgIds));
            } else { // visibility = all & orgId = specified
                sqlStr = MessageFormat.format(COUNT_SURVEY_CONFIGS_FOR_NONSA_BY_ORGIDS, ListUtils.listToString(orgIds));
            }
        } else {
            if (ListUtils.isEmptyList(orgIds) || orgIds.contains(-1)/*all*/) {// visibility = specified & orgId = all
                sqlStr = MessageFormat.format(COUNT_SURVEY_CONFIGS_FOR_NONSA_BY_VISIBILITY, ListUtils.listToString(userOwnedOrgIds), visibility);
            } else {  // visibility = specified & orgId = specified
                sqlStr = MessageFormat.format(COUNT_SURVEY_CONFIGS_FOR_NONSA_BY_ORGIDS_AND_VISIBLITY, ListUtils.listToString(orgIds), visibility);
            }
        }
        logger.debug("SQL String[count of survey config]: " + sqlStr);
        return super.count(sqlStr);
    }

    public List<SurveyConfig> selectSurveyConfigsForNonSAWithPagination(List<Integer> userOwnedOrgIds, int visibility, List<Integer> orgIds, String sortName, String sortOrder, int offset, int count) {
        if (userOwnedOrgIds == null) {
            userOwnedOrgIds = new ArrayList<Integer>();
        }
        if (userOwnedOrgIds.isEmpty()) {
            userOwnedOrgIds.add(-1);
        }
        String sqlStr = "";
        sortName = "sc.name";
        if (visibility <= 0) {
            if (ListUtils.isEmptyList(orgIds) || orgIds.contains(-1)/*all*/) { // visibility = all & orgId = all
                sqlStr = MessageFormat.format(SELECT_SURVEY_CONFIGS_FOR_NONSA, ListUtils.listToString(userOwnedOrgIds), sortName, sortOrder, offset, count);
            } else { // visibility = all & orgId = specified
                sqlStr = MessageFormat.format(SELECT_SURVEY_CONFIGS_FOR_NONSA_BY_ORGIDS, ListUtils.listToString(orgIds), sortName, sortOrder, offset, count);
            }
        } else {
            if (ListUtils.isEmptyList(orgIds) || orgIds.contains(-1)/*all*/) {// visibility = specified & orgId = all
                sqlStr = MessageFormat.format(SELECT_SURVEY_CONFIGS_FOR_NONSA_BY_VISIBILITY, ListUtils.listToString(userOwnedOrgIds), visibility, sortName, sortOrder, offset, count);
            } else { // visibility = specified & orgId = specified
                sqlStr = MessageFormat.format(SELECT_SURVEY_CONFIGS_FOR_NONSA_BY_ORGIDS_AND_VISIBLITY, ListUtils.listToString(orgIds), visibility, sortName, sortOrder, offset, count);
            }
        }
        logger.debug("SQL String: " + sqlStr);
        return super.find(sqlStr);
    }

    public Map<Integer, List<Organization>> selectOrgsBySurveyConfigIds(List<Integer> surveyConfigIds) {
        final Map<Integer, List<Organization>> orgConfigMap = new HashMap<Integer, List<Organization>>();
        RowMapper mapper = new RowMapper() {

            public Organization mapRow(ResultSet rs, int rowNum) throws SQLException {
                Organization org = new Organization();
                org.setId(rs.getInt("id"));
                org.setName(rs.getString("name"));
                int surveyConfigId = rs.getInt("survey_config_id");
                List<Organization> orgs = orgConfigMap.get(surveyConfigId);
                if (orgs == null) {
                    orgs = new ArrayList<Organization>();
                    orgConfigMap.put(surveyConfigId, orgs);
                }
                orgs.add(org);
                return org;
            }
        };

        getJdbcTemplate().query(MessageFormat.format(SELECT_ORGANIZATION_MAP_BY_SURVEY_CONFIG_IDS, ListUtils.listToString(surveyConfigIds)),
                mapper);
        return orgConfigMap;
    }

    public List<Integer> selectOrgIdsBySurveyConfigId(int surveyConfigId) {
        return getJdbcTemplate().query(SELECT_ORGIDS_BY_SURVEY_CONFIG_ID, new Object[]{surveyConfigId, surveyConfigId},
                super.getIdRowMapper("org_id"));
    }


    private Map<Integer, Boolean> checkSurveyConfigsUsed(List<Integer> surveyConfigIds, String sql) {
        final Map<Integer, Boolean> usedConfigMap = new HashMap<Integer, Boolean>();
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                int configId = rs.getInt("product_config_id");
                usedConfigMap.put(rs.getInt("product_config_id"), (rs.getInt("count") > 0));
                return configId;
            }
        };

        getJdbcTemplate().query(MessageFormat.format(sql, ListUtils.listToString(surveyConfigIds)), mapper);
        return usedConfigMap;
    }


    public Map<Integer, Boolean> checkSurveyConfigsInActiveUse(List<Integer> surveyConfigIds) {
        return checkSurveyConfigsUsed(surveyConfigIds, CHECK_SURVEY_CONFIG_IN_ACTIVE_USE);
    }

    public Map<Integer, Boolean> checkSurveyConfigsInUse(List<Integer> surveyConfigIds) {
        return checkSurveyConfigsUsed(surveyConfigIds, CHECK_SURVEY_CONFIG_IN_USE);
    }

    public boolean checkSurveyConfigInActiveUse(int surveyConfigId) {
        return super.count(CHECK_SURVEY_CONFIG_IN_ACTIVE_USE_BY_ID, surveyConfigId) > 0;
    }

    public boolean checkSurveyConfigInUse(int surveyConfigId) {
        return super.count(CHECK_SURVEY_CONFIG_IN_USE_BY_ID, surveyConfigId) > 0;
    }

    private static final String SELECT_SURVEY_CONFIG_BY_PRODUCT_ID =
            "SELECT sc.* FROM survey_config sc, product prod WHERE prod.id=? AND prod.content_type=0 AND sc.id=prod.product_config_id";


    public SurveyConfig selectByProductId(int productId) {
        return super.findSingle(SELECT_SURVEY_CONFIG_BY_PRODUCT_ID, productId);
    }

    static private final String SELECT_PRODUCTS_BY_SURVEY_CONFIG_ID = "SELECT * FROM product WHERE content_type=0 AND product_config_id IN ({0})";

    public Map<Integer, List<Product>> getProductMap(List<Integer> surveyConfigIds) {
        final Map<Integer, List<Product>> prodMap = new HashMap<Integer, List<Product>>();

        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                int configId = rs.getInt("product_config_id");
                Product product = new Product();

                product.setId(rs.getInt("id"));
                product.setContentType((short)0);
                product.setMode(rs.getShort("mode"));
                product.setName(rs.getString("name"));
                product.setProductConfigId(configId);
                product.setProjectId(rs.getInt("project_id"));

                List<Product> prodList = prodMap.get(configId);
                if (prodList == null) {
                    prodList = new ArrayList<Product>();
                    prodMap.put(configId, prodList);
                }
                prodList.add(product);

                return configId;
            }
        };

        getJdbcTemplate().query(MessageFormat.format(SELECT_PRODUCTS_BY_SURVEY_CONFIG_ID, ListUtils.listToString(surveyConfigIds)), mapper);
        return prodMap;
    }

    private static final String SELECT_BY_ANSWER_ID =
        "SELECT sc.* FROM survey_config sc, survey_question sq, survey_answer sa " +
        "WHERE sa.id=? AND sq.id=sa.survey_question_id AND sc.id=sq.survey_config_id";

    public SurveyConfig selectByAnswerId(int surveyAnswerId) {
        return super.findSingle(SELECT_BY_ANSWER_ID, surveyAnswerId);
    }
}
