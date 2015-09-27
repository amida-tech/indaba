/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Orgadmin;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author rick
 */
public class OrgadminDAO extends SmartDaoMySqlImpl<Orgadmin, Integer> {

    private static final Logger logger = Logger.getLogger(OrganizationDAO.class);
    private static String SELECT_ORGADMIN_BY_USERID = "SELECT * FROM orgadmin WHERE user_id = ?";
    private static String SELECT_ORGADMIN_BY_USERID_AND_ORGID = "SELECT COUNT(1) FROM organization o "
            + "WHERE (o.id=? AND o.admin_user_id=?) OR (SELECT COUNT(1) FROM orgadmin oa WHERE (oa.organization_id=? AND oa.user_id=?)) > 0";
    private static String SELECT_ORGADMIN_BY_ID = "SELECT * FROM orgadmin WHERE id = ?";
    private static String SELECT_ORGADMIN_BY_ORGID = "SELECT * FROM orgadmin WHERE organization_id= ?";

    public List<Orgadmin> selectOrgadminByUserId(int userId) {
        return super.find(SELECT_ORGADMIN_BY_USERID, userId);
    }

    public boolean existsByUserIdAndOrgId(int userId, int orgId) {
        return count(SELECT_ORGADMIN_BY_USERID_AND_ORGID, userId, orgId) > 0;
    }

    public Orgadmin selectOrgadminById(int id) {
        return super.findSingle(SELECT_ORGADMIN_BY_ID, id);
    }

    public List<Orgadmin> selectOrgadminByOrgId(int orgId) {
        return super.find(SELECT_ORGADMIN_BY_ORGID, orgId);
    }
}
