/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.aggregation.po.Workset;
import com.ocs.util.StringUtils;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author luwb
 */
public class WorksetDAO extends SmartDaoMySqlImpl<Workset, Integer>{
    private static final Logger log = Logger.getLogger(WorksetDAO.class);
    private static final String SELECT_ALL_WORKSETS = "SELECT * FROM workset";
    private static final String SELECT_ALL_ACTIVE_WORKSETS = "SELECT * FROM workset " +
            "WHERE is_active=true";
    private static final String SELECT_MANAGE_WORKSETS_BY_ORGIDS = "SELECT * FROM workset " +
            "WHERE org_id in ({0})";
    private static final String SELECT_PUBLIC_WORKSETS = "SELECT * FROM workset " +
            "WHERE visibility=1 and is_active=1";
    private static final String SELECT_WORKSETS_BY_ORGID = "SELECT * FROM workset " +
            "WHERE org_id=?";
    private static final String SELECT_PRIVATE_WORKSETS_BY_ORGID = "SELECT * FROM workset " +
            "WHERE org_id=? AND visibility=2";
    private String SELECT_WS_TARGETS_BY_USER_ID =
            "select w.* "+
            "from (ws_puser as u inner join workset as w on u.workset_id=w.id)"+
            "where u.user_id=?";
    private String SELECT_WORKSET_ID_BY_NAME =
            "select w.* from workset w where w.name=?";
    private String SELECT_PRIVATE_WORKSETS_BY_USERNAME =
            "SELECT w.* FROM workset w JOIN `indaba`.`user` u ON (w.org_id = u.organization_id) "
            + "WHERE w.visibility = 2 AND u.username = ? AND w.is_active = 1";
    private String ENABLE_WORKSET_BY_ID =
            "UPDATE workset set is_active=true WHERE id=?";
    private String DISABLE_WORKSET_BY_ID =
            "UPDATE workset set is_active=false WHERE id=?";

    public List<Workset> selectWorksetByUsername(String username) {
        return super.find(SELECT_PRIVATE_WORKSETS_BY_USERNAME, username);
    }

    public List<Workset> selectWorksetByUserId(int userId){
        return super.find(SELECT_WS_TARGETS_BY_USER_ID, userId);
    }

    public Workset selectWorksetByWorksetName( String worksetName ){
        return super.findSingle(SELECT_WORKSET_ID_BY_NAME, worksetName);
    }
    
    public Workset selectById(int worksetId) {
        logger.debug("Select Workset by  id: " + worksetId);
        return super.get(worksetId);
    }

    public List<Workset> selectManageWorksetsByOrgIds(List<Integer> orgIds){
        String sql = MessageFormat.format(SELECT_MANAGE_WORKSETS_BY_ORGIDS, StringUtils.list2Str(orgIds));
        return super.find(sql);
    }
    
    public List<Workset> selectPublicWorksets(){
        logger.debug("select public worksets");
        return super.find(SELECT_PUBLIC_WORKSETS);
    }

    public List<Workset> selectWorksetsByOrgId(int orgId){
        logger.debug("select worksets by orgId : " + orgId);
        return super.find(SELECT_WORKSETS_BY_ORGID, orgId);
    }

    public List<Workset> selectPrivateWorksetsByOrgId(int orgId){
        logger.debug("select private worksets by orgId : " + orgId);
        return super.find(SELECT_PRIVATE_WORKSETS_BY_ORGID, orgId);
    }

    public List<Workset> selectAllActiveWorkset(){
        logger.debug("select all active worsets");
        return super.find(SELECT_ALL_ACTIVE_WORKSETS);
    }

    public List<Workset> selectAllWorkset(){
        logger.debug("select all worsets");
        return super.find(SELECT_ALL_WORKSETS);
    }

    public void enableWorkset(int worksetId){
        super.update(ENABLE_WORKSET_BY_ID, worksetId);
    }

    public void disableWorkset(int worksetId){
        super.update(DISABLE_WORKSET_BY_ID, worksetId);
    }
    
}
