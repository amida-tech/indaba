/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.service;

import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.util.Pagination;
import com.ocs.indaba.controlpanel.model.OrganizationVO;
import com.ocs.indaba.controlpanel.model.OrganizationDetailVO;
import com.ocs.indaba.po.Organization;
import com.ocs.indaba.controlpanel.model.OrgAdminVO;
import com.ocs.indaba.controlpanel.model.OrgKeyVO;
import com.ocs.indaba.po.Orgadmin;
import java.util.Date;
import java.util.List;

/**
 *
 * @author rick
 */
public interface IOrganizationsService {
    
    public List<OrganizationVO> getVisibleOrgVO(LoginUser user);
    public OrganizationVO getOrg(String name);
    public Pagination<OrganizationVO> getAllOrgs();
    boolean AddOrg(String name, String addr, boolean enforceAPISec, int adminId);
    public Organization AddOrg(String name, String addr, String url, boolean enforceAPISec);
    public Organization UpdateOrg(Organization org);
    public OrganizationDetailVO getOrgDetail(int id);
    public void addOrgOA(int orgId, int usrId);
    public Orgadmin removeOrgOA(int id);
    public List<OrgAdminVO> getOrgOA(int id);
    public OrganizationVO getOrg(int id);
    public Organization getOrgPO(int id);
    
    // for security key
    public List<OrgKeyVO> getSecurityKeys(int orgId);
    public OrgKeyVO getSecurityKey(int id);
    public boolean AddSecurityKey(int orgId, String algorithom, Date effectiveTime, int validDays, int issueUserId);
    public boolean RevokeKey(int id, int userId, String reason);
    public boolean ChangeKeyValidDays(int id, int validDays);
}
