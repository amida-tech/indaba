/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Organization;
import com.ocs.indaba.util.ListUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author rick
 */
public class OrganizationDAO extends SmartDaoMySqlImpl<Organization, Integer> {

    private static final Logger logger = Logger.getLogger(OrganizationDAO.class);
    private static final String SELECT_ALL_ORGS = "SELECT * FROM organization ORDER BY name";
    private static final String SELECT_ORG_BY_ID = "SELECT * FROM organization WHERE id = ?";
    //private static String SELECT_ORG_IDS_BY_USERID = "SELECT organization_id FROM orgadmin WHERE user_id = ?";
    private static final String SELECT_ORGS_BY_USER_ID_FROM_ORGADMIN = "SELECT DISTINCT org.* FROM organization org, orgadmin oa WHERE oa.user_id = ? AND org.id=oa.organization_id";
    private static final String SELECT_ORGS_BY_USER_ID_FROM_ORG = "SELECT DISTINCT * FROM organization WHERE admin_user_id = ?";
    private static final String SELECT_ORGIDS_BY_USER_ID_FROM_ORGADMIN = "SELECT DISTINCT org.id organization_id FROM organization org, orgadmin oa WHERE oa.user_id = ? AND org.id=oa.organization_id";
    private static final String SELECT_ORGIDS_BY_USER_ID_FROM_ORG = "SELECT DISTINCT id FROM organization WHERE admin_user_id = ?";
    private static final String SELECT_ORGS_BY_IDS = "SELECT * FROM organization WHERE id IN (?)";
    private static final String SELECT_ORG_BY_NAME = "SELECT * FROM organization WHERE UPPER(name)=UPPER(?)";
    private static final String SELECT_ORGS_BY_CONFIGID =
            "SELECT o.* FROM  organization o JOIN "
            + "(SELECT owner_org_id FROM survey_config WHERE id=? "
            + "UNION DISTINCT SELECT org_id owner_org_id FROM sc_contributor WHERE survey_config_id=?) scorg ON o.id=scorg.owner_org_id "
            + "WHERE o.id IN ({0}) ORDER BY o.name";

    private static final String SELECT_ALL_ORGS_BY_CONFIGID =
            "SELECT o.* FROM  organization o JOIN "
            + "(SELECT owner_org_id FROM survey_config WHERE id=? "
            + "UNION DISTINCT SELECT org_id owner_org_id FROM sc_contributor WHERE survey_config_id=?) scorg ON o.id=scorg.owner_org_id "
            + "ORDER BY o.name";

    public List<Organization> selectOrganizationsByConfigIdAndOrgIds(int surveyConfigId, List<Integer> orgIds) {
        return super.find(MessageFormat.format(SELECT_ORGS_BY_CONFIGID, ListUtils.listToString(orgIds)), (Object)surveyConfigId, surveyConfigId);
    }

    public List<Organization> selectAllOrganizationsByConfigId(int surveyConfigId) {
        return super.find(SELECT_ALL_ORGS_BY_CONFIGID, (Object)surveyConfigId, surveyConfigId);
    }

    public Organization selectOrgByName(String name) {
        return super.findSingle(SELECT_ORG_BY_NAME, name);
    }
    public List<Organization> selectAllOrgs() {
        return super.find(SELECT_ALL_ORGS);
    }

    public Organization selectOrgById(int id) {
        return super.findSingle(SELECT_ORG_BY_ID, id);
    }

    public List<Organization> selectOrganizationsByIds(List<Integer> orgIds) {
        String idStr = ListUtils.listToString(orgIds);
        return super.find(SELECT_ORGS_BY_IDS, idStr);
    }

    public List<Organization> selectOrgsByUserIdFromOrgAdmin(int userId) {
        return super.find(SELECT_ORGS_BY_USER_ID_FROM_ORGADMIN, userId);
    }

    public List<Integer> selectOrgIdsByUserIdFromOrgAdmin(int userId) {
        return getJdbcTemplate().query(SELECT_ORGIDS_BY_USER_ID_FROM_ORGADMIN, new Object[]{userId}, new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("organization_id");
            }
        });
    }

    public List<Organization> selectOrgsByUserIdFromOrg(int userId) {
        return super.find(SELECT_ORGS_BY_USER_ID_FROM_ORG, userId);
    }

    public List<Integer> selectOrgIdsByUserIdFromOrg(int userId) {
        return getJdbcTemplate().query(SELECT_ORGIDS_BY_USER_ID_FROM_ORG, new Object[]{userId}, new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("id");
            }
        });
    }
}
