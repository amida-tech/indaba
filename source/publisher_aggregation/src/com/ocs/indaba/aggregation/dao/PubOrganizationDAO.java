/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Organization;
import java.util.List;

/**
 *
 * @author Gerry
 */
public class PubOrganizationDAO extends SmartDaoMySqlImpl<Organization, Integer> {

    private String SELECT_ORG_BY_WORKSET_ID =
            "SELECT distinct o.* FROM `indaba`.`organization` as o inner join `indaba_publisher`.`ws_indicator_instance` as w on o.id=w.org_id"
            + " where w.workset_id=?";
    private String SELECT_ORG_BY_WORKSET_ID_AND_INDICATOR_ID =
            "SELECT DISTINCT o.* FROM `indaba`.`organization` o "
            + "JOIN `indaba_publisher`.`ws_indicator_instance` w ON (w.org_id = o.id) "
            + "WHERE w.workset_id = ? AND w.indicator_id = ?";

    public Organization selectOrganizationByWorksetId(int worksetId) {
        return super.findSingle(SELECT_ORG_BY_WORKSET_ID, worksetId);
    }

    public List<Organization> selectOrganizationsByWorksetIdAndIndicatorId(int worksetId, int indicatorId) {
        return super.find(SELECT_ORG_BY_WORKSET_ID_AND_INDICATOR_ID, new Object[]{worksetId, indicatorId});
    }
}
