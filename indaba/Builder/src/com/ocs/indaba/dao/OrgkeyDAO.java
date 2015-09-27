/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Orgkey;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author luwenbin
 */
public class OrgkeyDAO extends SmartDaoMySqlImpl<Orgkey, Integer>{
    private static final Logger log = Logger.getLogger(OrgkeyDAO.class);
    private static final String SELECT_ALL_ORGKEY =
            "SELECT * FROM orgkey";
    private static final String SELECT_ORGKEY_BY_ORGID =
            "SELECT * FROM orgkey where organization_id=?";
    private static final String SELECT_ORGKEY_BY_ORGID_AND_VERSION =
            "SELECT * FROM orgkey where organization_id=? and version=?";
    private static final String SELECT_ORGKEY_BY_ID = "SELECT * FROM orgkey WHERE id = ?";
    
    public List<Orgkey> selectAllOrgkey(){
        log.debug("select all orgkeys");
        return super.find(SELECT_ALL_ORGKEY);
    }

    public Orgkey selectOrgkeyByOrgIdAndVersion(int orgId, int version){
        log.debug("select orgkey by orgId and version:" + orgId + " " + version);
        return super.findSingle(SELECT_ORGKEY_BY_ORGID_AND_VERSION, orgId, version);
    }

    public List<Orgkey> selectOrgkeyByOrgId(int orgId){
        log.debug("select orgkeys by orgId:" + orgId);
        return super.find(SELECT_ORGKEY_BY_ORGID, orgId);
    }
    
    public Orgkey selectOrgkeyById(int id) {
        return super.findSingle(SELECT_ORGKEY_BY_ID, id);
    }
}
