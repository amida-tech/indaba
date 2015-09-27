/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.builder.dao;

import com.ocs.indaba.aggregation.vo.ProductBriefVO;
import com.ocs.indaba.aggregation.vo.ProductForExportVO;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.aggregation.vo.ProductVO;
import com.ocs.indaba.po.Product;
import com.ocs.indaba.util.ListUtils;
import com.ocs.indaba.util.ResultSetUtil;
import com.ocs.util.StringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Jeanbone
 */
public class ProductDAO extends SmartDaoMySqlImpl<Product, Integer> {
    private static final Map<String, String> SORT_NAME_MAP = new HashMap<String, String>();
    static {
        SORT_NAME_MAP.put("prod", "product_name");
        SORT_NAME_MAP.put("proj", "pname");
        SORT_NAME_MAP.put("org", "oname");
        SORT_NAME_MAP.put("sp", "spname");
    }

    private static final String SELECT_JOURNAL_PRODUCTS_BY_PROJECT_ID =
            "SELECT pd.*, pj.code_name project_name FROM product pd "
            + "JOIN project pj ON (pd.project_id = pj.id) "
            + "WHERE pj.id = ? AND pd.content_type = 1";

    private static final String SELECT_PRODUCT_VO_BY_ID =
            "SELECT pd.*, pj.code_name project_name FROM product pd "
            + "JOIN project pj ON (pd.project_id = pj.id) "
            + "WHERE pd.id = ?";
    
    private static final String SELECT_COUNT_OF_COMPLETED_HORSES =
            "SELECT count(*) count FROM horse h, workflow_object wfo "
            + "WHERE h.product_id=? AND h.workflow_object_id=wfo.id AND wfo.status=3";


    private static final String SELECT_TSC_SURVEYS_BY_ORG_IDS_BASE =
            " FROM ( " +
            "  (SELECT prd.id pdid, prd.mode pmode, prj.id pid, prj.code_name pname, prj.organization_id oid, prd.name product_name, prj.study_period_id spid, prd.content_type ctype, prd.product_config_id pcid " +
            "   FROM project prj, product prd " +
            "   WHERE prj.organization_id IN ({0}) AND prj.id=prd.project_id) " +
            "UNION " +
            "  (SELECT prd.id pdid, prd.mode pmode, prj.id pid, prj.code_name pname, po.org_id oid, prd.name product_name, prj.study_period_id spid, prd.content_type ctype, prd.product_config_id pcid " +
            "   FROM project prj, product prd, project_owner po " +
            "   WHERE po.project_id=prj.id AND po.org_id IN ({0}) AND prj.id=prd.project_id) " +
            ") temp, organization org, survey_config sc, study_period sp " +
            "WHERE org.id=temp.oid AND temp.pmode != 1 AND temp.ctype=0 AND sc.id=temp.pcid AND sc.is_tsc=1 AND sp.id=temp.spid " +
            "GROUP BY pdid";


    private static final String SELECT_TSC_SURVEYS_BY_ORG_IDS_WITH_SORTING =
            "SELECT DISTINCT temp.pdid, temp.pid, temp.pname, temp.product_name, temp.oid, org.name oname, sp.name spname, sc.is_tsc " +
            SELECT_TSC_SURVEYS_BY_ORG_IDS_BASE +
            " ORDER BY {1} {2}";

    private static final String SELECT_ALL_TSC_SURVEYS_BASE =
            "SELECT prj.code_name pname, prj.organization_id oid, "
            + "prd.id pdid, prd.name product_name, sp.name spname, org.name oname, prj.id pid, sc.is_tsc "
            + "FROM project prj, product prd, study_period sp, organization org, survey_config sc "
            + "WHERE prj.id=prd.project_id AND prd.content_type=0 AND sc.id=prd.product_config_id AND sc.is_tsc=1 AND prj.study_period_id=sp.id AND prj.organization_id=org.id";

    private static final String SELECT_ALL_TSC_SURVEYS_WITH_SORTING =
            SELECT_ALL_TSC_SURVEYS_BASE + " ORDER BY {0} {1}";




    private static final String SELECT_SURVEYS_BY_ORG_IDS_BASE =
            " FROM ( " +
            "  (SELECT prd.id pdid, prd.mode pmode, prj.id pid, prj.code_name pname, prj.organization_id oid, prd.name product_name, prj.study_period_id spid, prd.content_type ctype, prd.product_config_id pcid " +
            "   FROM project prj, product prd " +
            "   WHERE prj.organization_id IN ({0}) AND prj.id=prd.project_id) " +
            "UNION " +
            "  (SELECT prd.id pdid, prd.mode pmode, prj.id pid, prj.code_name pname, po.org_id oid, prd.name product_name, prj.study_period_id spid, prd.content_type ctype, prd.product_config_id pcid " +
            "   FROM project prj, product prd, project_owner po " +
            "   WHERE po.project_id=prj.id AND po.org_id IN ({0}) AND prj.id=prd.project_id) " +
            ") temp, organization org, survey_config sc, study_period sp " +
            "WHERE org.id=temp.oid AND temp.pmode != 1 AND temp.ctype=0 AND sc.id=temp.pcid AND sp.id=temp.spid " +
            "GROUP BY pdid";


    private static final String SELECT_SURVEYS_BY_ORG_IDS_WITH_SORTING =
            "SELECT DISTINCT temp.pdid, temp.pid, temp.pname, temp.product_name, temp.oid, org.name oname, sp.name spname, sc.is_tsc " +
            SELECT_SURVEYS_BY_ORG_IDS_BASE +
            " ORDER BY {1} {2}";

    private static final String SELECT_ALL_SURVEYS_BASE =
            "SELECT prj.code_name pname, prj.organization_id oid, "
            + "prd.id pdid, prd.name product_name, sp.name spname, org.name oname, prj.id pid, sc.is_tsc "
            + "FROM project prj, product prd, study_period sp, organization org, survey_config sc "
            + "WHERE prj.id=prd.project_id AND prd.content_type=0 AND sc.id=prd.product_config_id AND prj.study_period_id=sp.id AND prj.organization_id=org.id";

    private static final String SELECT_ALL_SURVEYS_WITH_SORTING =
            SELECT_ALL_SURVEYS_BASE + " ORDER BY {0} {1}";



    private static final String SELECT_PRODUCTS_BY_ORG_IDS_BASE =
            " FROM ( " +
            "  (SELECT prd.id pdid, prd.mode pmode, prj.id pid, prj.code_name pname, prj.organization_id oid, prd.name product_name, prj.study_period_id spid, prd.content_type ctype, prd.product_config_id pcid " +
            "   FROM project prj, product prd " +
            "   WHERE prj.organization_id IN ({0}) AND prj.id=prd.project_id) " +
            "UNION " +
            "  (SELECT prd.id pdid, prd.mode pmode, prj.id pid, prj.code_name pname, po.org_id oid, prd.name product_name, prj.study_period_id spid, prd.content_type ctype, prd.product_config_id pcid " +
            "   FROM project prj, product prd, project_owner po " +
            "   WHERE po.project_id=prj.id AND po.org_id IN ({0}) AND prj.id=prd.project_id) " +
            ") temp, organization org, study_period sp " +
            "WHERE org.id=temp.oid AND temp.pmode != 1 AND sp.id=temp.spid " +
            "GROUP BY pdid";


    private static final String SELECT_PRODUCTS_BY_ORG_IDS_WITH_SORTING =
            "SELECT DISTINCT temp.pdid, temp.pid, temp.pname, temp.product_name, temp.oid, org.name oname, sp.name spname " +
            SELECT_PRODUCTS_BY_ORG_IDS_BASE +
            " ORDER BY {1} {2}";

    private static final String SELECT_ALL_PRODUCTS_BASE =
            "SELECT prj.code_name pname, prj.organization_id oid, "
            + "prd.id pdid, prd.name product_name, sp.name spname, org.name oname, prj.id pid "
            + "FROM project prj, product prd, study_period sp, organization org "
            + "WHERE prj.id=prd.project_id AND prj.study_period_id=sp.id AND prj.organization_id=org.id";

    private static final String SELECT_ALL_PRODUCTS_WITH_SORTING =
            SELECT_ALL_PRODUCTS_BASE + " ORDER BY {0} {1}";




    private static final String SELECT_JOURNALS_BY_ORG_IDS_BASE =
            " FROM ( " +
            "  (SELECT prd.id pdid, prd.mode pmode, prj.id pid, prj.code_name pname, prj.organization_id oid, prd.name product_name, prj.study_period_id spid, prd.content_type ctype, prd.product_config_id pcid " +
            "   FROM project prj, product prd " +
            "   WHERE prj.organization_id IN ({0}) AND prj.id=prd.project_id) " +
            "UNION " +
            "  (SELECT prd.id pdid, prd.mode pmode, prj.id pid, prj.code_name pname, po.org_id oid, prd.name product_name, prj.study_period_id spid, prd.content_type ctype, prd.product_config_id pcid " +
            "   FROM project prj, product prd, project_owner po " +
            "   WHERE po.project_id=prj.id AND po.org_id IN ({0}) AND prj.id=prd.project_id) " +
            ") temp, organization org, study_period sp " +
            "WHERE org.id=temp.oid AND temp.pmode != 1 AND temp.ctype=1 AND sp.id=temp.spid " +
            "GROUP BY pdid";


    private static final String SELECT_JOURNALS_BY_ORG_IDS_WITH_SORTING =
            "SELECT DISTINCT temp.pdid, temp.pid, temp.pname, temp.product_name, temp.oid, org.name oname, sp.name spname " +
            SELECT_JOURNALS_BY_ORG_IDS_BASE +
            " ORDER BY {1} {2}";

    private static final String SELECT_ALL_JOURNALS_BASE =
            "SELECT prj.code_name pname, prj.organization_id oid, "
            + "prd.id pdid, prd.name product_name, sp.name spname, org.name oname, prj.id pid "
            + "FROM project prj, product prd, study_period sp, organization org "
            + "WHERE prj.id=prd.project_id AND prd.content_type=1 AND prj.study_period_id=sp.id AND prj.organization_id=org.id";

    private static final String SELECT_ALL_JOURNALS_WITH_SORTING =
            SELECT_ALL_JOURNALS_BASE + " ORDER BY {0} {1}";


    public List<ProductVO> selectJournalProductsByProjectId(int projectId) {
        logger.debug("Select journal products by project id: " + projectId);
        return getJdbcTemplate().query(SELECT_JOURNAL_PRODUCTS_BY_PROJECT_ID,
                new Object[]{projectId}, new ProductVORowMapper());
    }

    public ProductVO selectProductVOById(int productId) {
        logger.debug("Select product vo by id: " + productId);
        List<ProductVO> products = getJdbcTemplate().query(SELECT_PRODUCT_VO_BY_ID,
                new Object[]{productId}, new ProductVORowMapper());
        return products.isEmpty() ? null : products.get(0);
    }


    public List<ProductForExportVO> selectTscSurveyProductsByOrgIds(List<Integer> orgIds, String sortName, String sortType) {
        if("asc".equalsIgnoreCase(sortType)) {
            sortType = "asc";
        } else {
            sortType = "desc";
        }

        String fixedSortName = SORT_NAME_MAP.get(sortName);
        if(StringUtils.isEmpty(fixedSortName)) {
            fixedSortName = SORT_NAME_MAP.get("prod");
        }

        String sqlStr;

        if (orgIds == null || orgIds.isEmpty()) {
            sqlStr = MessageFormat.format(SELECT_ALL_TSC_SURVEYS_WITH_SORTING, fixedSortName, sortType);
        } else {
            sqlStr = MessageFormat.format(SELECT_TSC_SURVEYS_BY_ORG_IDS_WITH_SORTING,
                    ListUtils.listToString(orgIds),
                    fixedSortName, sortType);
        }

        logger.debug("Select TSC survey products: " + sqlStr);
        
        return getJdbcTemplate().query(sqlStr, new Object[]{}, new ProductExportVORowMapper());
    }



    public List<ProductForExportVO> selectSurveyProductsByOrgIds(List<Integer> orgIds, String sortName, String sortType) {
        if("asc".equalsIgnoreCase(sortType)) {
            sortType = "asc";
        } else {
            sortType = "desc";
        }

        String fixedSortName = SORT_NAME_MAP.get(sortName);
        if(StringUtils.isEmpty(fixedSortName)) {
            fixedSortName = SORT_NAME_MAP.get("prod");
        }

        String sqlStr;

        if (orgIds == null || orgIds.isEmpty()) {
            sqlStr = MessageFormat.format(SELECT_ALL_SURVEYS_WITH_SORTING, fixedSortName, sortType);
        } else {
            sqlStr = MessageFormat.format(SELECT_SURVEYS_BY_ORG_IDS_WITH_SORTING,
                    ListUtils.listToString(orgIds),
                    fixedSortName, sortType);
        }

        logger.debug("Select survey products: " + sqlStr);

        return getJdbcTemplate().query(sqlStr, new Object[]{}, new ProductExportVORowMapper());
    }


    public List<ProductForExportVO> selectProductsByOrgIds(List<Integer> orgIds, String sortName, String sortType) {
        if("asc".equalsIgnoreCase(sortType)) {
            sortType = "asc";
        } else {
            sortType = "desc";
        }

        String fixedSortName = SORT_NAME_MAP.get(sortName);
        if(StringUtils.isEmpty(fixedSortName)) {
            fixedSortName = SORT_NAME_MAP.get("prod");
        }

        String sqlStr;

        if (orgIds == null || orgIds.isEmpty()) {
            sqlStr = MessageFormat.format(SELECT_ALL_PRODUCTS_WITH_SORTING, fixedSortName, sortType);
        } else {
            sqlStr = MessageFormat.format(SELECT_PRODUCTS_BY_ORG_IDS_WITH_SORTING,
                    ListUtils.listToString(orgIds),
                    fixedSortName, sortType);
        }

        logger.debug("Select all products: " + sqlStr);

        return getJdbcTemplate().query(sqlStr, new Object[]{}, new ProductExportVORowMapper());
    }



    public List<ProductForExportVO> selectJournalsByOrgIds(List<Integer> orgIds, String sortName, String sortType) {
        if("asc".equalsIgnoreCase(sortType)) {
            sortType = "asc";
        } else {
            sortType = "desc";
        }

        String fixedSortName = SORT_NAME_MAP.get(sortName);
        if(StringUtils.isEmpty(fixedSortName)) {
            fixedSortName = SORT_NAME_MAP.get("prod");
        }

        String sqlStr;

        if (orgIds == null || orgIds.isEmpty()) {
            sqlStr = MessageFormat.format(SELECT_ALL_JOURNALS_WITH_SORTING, fixedSortName, sortType);
        } else {
            sqlStr = MessageFormat.format(SELECT_JOURNALS_BY_ORG_IDS_WITH_SORTING,
                    ListUtils.listToString(orgIds),
                    fixedSortName, sortType);
        }

        logger.debug("Select all products: " + sqlStr);

        return getJdbcTemplate().query(sqlStr, new Object[]{}, new ProductExportVORowMapper());
    }


    private final class ProductExportVORowMapper implements RowMapper {

        public ProductForExportVO mapRow(ResultSet rs, int i) throws SQLException {
                ProductForExportVO vo = new ProductForExportVO();

                vo.setId(rs.getInt("pdid"));
                vo.setProdName(rs.getString("product_name"));
                vo.setOrgId(rs.getInt("oid"));
                vo.setOrgName(rs.getString("oname"));
                vo.setProjId(rs.getInt("pid"));
                vo.setProjName(rs.getString("pname"));
                vo.setStudyPeriod(rs.getString("spname"));

                try {
                    vo.setIsTsc(rs.getBoolean("is_tsc") ? 1 : 0);
                } catch (Exception e) {
                    vo.setIsTsc(0);
                }

                return vo;
            }
    }


    private final class ProductVORowMapper implements RowMapper {

        public ProductVO mapRow(ResultSet rs, int i) throws SQLException {
            ProductVO productVO = new ProductVO();
            Product product = new Product();
            product.setId(rs.getInt("id"));
            product.setWorkflowId(rs.getInt("workflow_id"));
            product.setName(rs.getString("name"));
            product.setDescription(rs.getString("description"));
            product.setProjectId(rs.getInt("project_id"));
            product.setAccessMatrixId(rs.getInt("access_matrix_id"));
            product.setProductConfigId(rs.getInt("product_config_id"));
            product.setContentType(rs.getShort("content_type"));
            productVO.setProduct(product);
            productVO.setProjectName(rs.getString("project_name"));
            return productVO;
        }
    }

    private final class ProductBriefVORowMapper implements RowMapper {

        public ProductBriefVO mapRow(ResultSet rs, int i) throws SQLException {
            ProductBriefVO prd = new ProductBriefVO();
            prd.setProductId(rs.getInt("product_id"));
            prd.setProductName(rs.getString("product_name"));
            prd.setProjectName(rs.getString("project_name"));
            prd.setVisibility(rs.getInt("project_visibility"));
            prd.setProjectOrgId(rs.getInt("project_org_id"));
            return prd;
        }
    }

    public boolean checkHasCompletedHorse(int productId) {
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return (ResultSetUtil.getInt(rs, "count"));
            }
        };
        List<Integer> list = getJdbcTemplate().query(
                SELECT_COUNT_OF_COMPLETED_HORSES,
                new Object[]{productId},
                mapper);
        return (list == null || list.isEmpty()) ? false : (list.get(0) > 0);
    }
}
