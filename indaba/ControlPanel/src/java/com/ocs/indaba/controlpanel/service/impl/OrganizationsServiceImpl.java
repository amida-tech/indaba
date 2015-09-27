/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.service.impl;

import com.ocs.indaba.controlpanel.model.OrganizationVO;
import com.ocs.indaba.controlpanel.service.IOrganizationsService;
import com.ocs.indaba.dao.*;
import com.ocs.indaba.po.Orgadmin;
import com.ocs.indaba.po.User;
import com.ocs.util.Pagination;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ocs.indaba.po.Organization;
import com.ocs.indaba.controlpanel.model.OrganizationDetailVO;
import com.ocs.indaba.po.Orgkey;
import com.ocs.indaba.controlpanel.model.OrgAdminVO;
import com.ocs.indaba.aggregation.service.OrgkeyService;
import com.ocs.indaba.common.Constants;
import java.util.Date;
import com.ocs.indaba.controlpanel.common.ControlPanelConstants;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.controlpanel.model.OrgKeyVO;
/**
 *
 * @author rick
 */
@Service
public class OrganizationsServiceImpl implements IOrganizationsService{
    
    public OrganizationVO getOrg(String name) {
        Organization org = orgDAO.selectOrgByName(name);
        return org == null ? null : PO2VO(org);
    }
    
    public List<OrganizationVO> getVisibleOrgVO(LoginUser user) {
        return PO2VO(getVisibleOrg(user));
    }
    // For site administrator, he/she can access to all organizations;
    // for organization administrator, he/she can only access to organizations administrated by him/her
    public List<Organization> getVisibleOrg(LoginUser user) {
        return user.getAccessibleOrgs(Constants.VISIBILITY_PRIVATE);
    }

    private List<OrganizationVO> PO2VO(List<Organization> orgs) {
        List<OrganizationVO> orgVOs = new ArrayList<OrganizationVO>();
        for (Organization org : orgs) {
            orgVOs.add(PO2VO(org));
        }
        return orgVOs;
    }
    private OrganizationVO PO2VO(Organization org) {
        User oa = userDAO.selectUserById(org.getAdminUserId());
        OrganizationVO orgVO = new OrganizationVO(org.getId(), org.getName(), org.getAddress(), org.getUrl(), 
                org.getEnforceApiSecurity(), oa.getFirstName(), oa.getLastName(), oa.getEmail(), oa.getUsername());

        if (orgVO.getAddress() == null) {
            orgVO.setAddress("");
        }
        
        return orgVO;
    }
    private OrgAdminVO PO2VO(Orgadmin oa) {        
        User user = userDAO.selectUserById(oa.getUserId());
        OrgAdminVO oaVO = new OrgAdminVO();
        oaVO.setId(oa.getId());
        oaVO.setFirstName(user.getFirstName());
        oaVO.setLastName(user.getLastName());
        oaVO.setUserName(user.getUsername());
        oaVO.setEmail(user.getEmail());
        return oaVO;
    }
    private OrgKeyVO PO2VO(Orgkey key) {
        return new OrgKeyVO(key.getId(),  key.getVersion(), key.getHashAlgorithm(), 
                key.getIssueTime(),key.getEffectiveTime(), key.getValidDays(), key.getStatus(), 
                key.getData());
    }
    public List<OrgAdminVO> getOrgOA(int id) {
        List<OrgAdminVO> oaVOs = new ArrayList<OrgAdminVO>();
        List<Orgadmin> oas = orgadminDAO.selectOrgadminByOrgId(id);
        for(Orgadmin oa : oas) {
            oaVOs.add(PO2VO(oa));
        }
        return oaVOs;
    }
    // handle security key funtionalities
    public List<OrgKeyVO> getSecurityKeys(int orgId) {
        List<OrgKeyVO> keyVOs = new ArrayList<OrgKeyVO>();
        List<Orgkey> keys = orgkeyDAO.selectOrgkeyByOrgId(orgId);
        for(Orgkey key : keys) {
            keyVOs.add(PO2VO(key));
        }
        return keyVOs;
    }
    public OrgKeyVO getSecurityKey(int id) {
        return PO2VO(orgkeyDAO.selectOrgkeyById(id));
    }
    public boolean AddSecurityKey(int orgId, String algorithom, Date effectiveTime, int validDays, int issueUserId) {
        Orgkey orgkey = keyGenerateService.generateOrgkey(orgId, issueUserId);
        // override validation algorithm, effective date and validDays
        orgkey.setHashAlgorithm(algorithom);
        orgkey.setEffectiveTime(effectiveTime);
        orgkey.setValidDays(validDays);
        orgkeyDAO.save(orgkey);
        return true;
    }
    public boolean RevokeKey(int id, int userId, String reason) {
        Orgkey key = orgkeyDAO.selectOrgkeyById(id);
        key.setStatus(ControlPanelConstants.SECURITY_KEY_STATUS_REVOKED);
        key.setRevokeReason(reason);
        key.setIssueUserId(userId);
        key.setRevokeTime(new Date());
        orgkeyDAO.save(key);
        return true;
    }
    public boolean ChangeKeyValidDays(int id, int validDays) {
        Orgkey key = orgkeyDAO.selectOrgkeyById(id);
        key.setValidDays(validDays);
        orgkeyDAO.save(key);
        return true;
    }
    public List<OrganizationVO> getOrgs(int userId) {
        List<OrganizationVO> orgs =
                new ArrayList<OrganizationVO>();
        List<Orgadmin> admins = orgadminDAO.selectOrgadminByUserId(userId);
        for (Orgadmin admin : admins) {
            com.ocs.indaba.po.Organization org = orgDAO.selectOrgById(admin.getUserId());
            User user = userDAO.selectUserById(org.getAdminUserId());
            orgs.add(new OrganizationVO(org.getId(), org.getName(), org.getAddress(), user.getFirstName(), user.getLastName()));
        }
        return orgs;
    }

    public Pagination<OrganizationVO> getAllOrgs() {
        Pagination<OrganizationVO> orgs = new Pagination<OrganizationVO>(1, 10);
        List<com.ocs.indaba.po.Organization> orgPOs = orgDAO.selectAllOrgs();
        for (com.ocs.indaba.po.Organization po : orgPOs) {
            User user = userDAO.selectUserById(po.getAdminUserId());
            orgs.addRow(new OrganizationVO(po.getId(), po.getName(), po.getAddress(), user.getFirstName(), user.getLastName()));
        }
        orgs.setTotal(orgPOs.size());
        return orgs;
    }
    public OrganizationVO getOrg(int id) {
        Organization po = orgDAO.selectOrgById(id);
        User user = userDAO.selectUserById(po.getAdminUserId());
        return new OrganizationVO(po.getId(), po.getName(), po.getAddress(), po.getUrl(), po.getEnforceApiSecurity(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getUsername());
    }
    public Organization getOrgPO(int id) {
        return orgDAO.selectOrgById(id);
    }
    public void addOrgOA(int orgId, int usrId) {
        Orgadmin orgAdmin = new Orgadmin();
        orgAdmin.setOrganizationId(orgId);
        orgAdmin.setUserId(usrId);
        orgadminDAO.create(orgAdmin);
    }
    public void RemoveOrgOA(int orgId, int usrId) {
        
    }
    public Orgadmin removeOrgOA(int id) {
        Orgadmin oa = orgadminDAO.get(id);

        if (oa != null) orgadminDAO.delete(id);
        return oa;
    }

    public boolean AddOrg(String name, String addr, boolean enforceAPISec, int adminId) {
        Organization org = new Organization();
        org.setName(name);
        org.setAddress(addr);
        org.setEnforceApiSecurity(enforceAPISec);
        org.setAdminUserId(adminId);
        orgDAO.create(org);
        return true;
    }
    public Organization AddOrg(String name, String addr, String url, boolean enforceAPISec) {
        Organization org = new Organization();
        org.setName(name);
        org.setAddress(addr);
        org.setUrl(url);
        org.setEnforceApiSecurity(enforceAPISec);
        return orgDAO.create(org);
    }
    public Organization UpdateOrg(Organization org) {
        return orgDAO.update(org);
    }
    public void UpdateOrg(OrganizationVO orgVO) {
        
        Organization org = orgDAO.selectOrgById(orgVO.getId());
        // check if super administrator is changed
        User u = userDAO.selectUserById(org.getAdminUserId());
        if(!u.getEmail().equals(orgVO.getEmail())) {
            u.setSiteAdmin((short)0);
            userDAO.update(u);
            u = userDAO.selectUserByEmail(orgVO.getEmail());
            if(u == null) {
            }
            u.setSiteAdmin((short)1);
            userDAO.update(u);
        }
    }
    public OrganizationDetailVO getOrgDetail(int id) {
        OrganizationDetailVO org = new OrganizationDetailVO();
        Organization orgPO = orgDAO.selectOrgById(id);
        org.setId(orgPO.getId());
        org.setName(orgPO.getName());
        org.setAddress(orgPO.getAddress());
        org.setUrl(orgPO.getUrl());
        org.setEnforceAPISecurity(orgPO.getEnforceApiSecurity());
        List<Orgadmin> oas = orgadminDAO.selectOrgadminByOrgId(id);
        for(Orgadmin a : oas) {
            OrgAdminVO oav = new OrgAdminVO();
            User u = userDAO.selectUserById(a.getUserId());
            oav.setId(a.getId());
            oav.setFirstName(u.getFirstName());
            oav.setLastName(u.getLastName());
            oav.setUserName(u.getUsername());
            oav.setEmail(u.getEmail());
            org.AddOrgAdmin(oav);
        }
        List<Orgkey> oks = orgkeyDAO.selectOrgkeyByOrgId(id);
        for(Orgkey k : oks) {
            
        }
        return org;
    }
    
    @Autowired
    public void setOrganizationDAO(OrganizationDAO orgDAO) {
        this.orgDAO = orgDAO;
    }

    @Autowired
    public void setOrgadminDAO(OrgadminDAO orgadminDAO) {
        this.orgadminDAO = orgadminDAO;
    }

    @Autowired
    public void setOrgkeyDAO(OrgkeyDAO orgkeyDAO) {
        this.orgkeyDAO = orgkeyDAO;
    }

    @Autowired
    public void setRoleDAO(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    @Autowired
    void setKeyGenerateService(OrgkeyService keyGenerateService) {
        this.keyGenerateService = keyGenerateService;
    }
    private OrgkeyService keyGenerateService;
    private OrganizationDAO orgDAO;
    private OrgadminDAO orgadminDAO;
    private OrgkeyDAO orgkeyDAO;
    private RoleDAO roleDAO;
    private UserDAO userDAO;
    private static final Logger logger = Logger.getLogger(OrganizationsServiceImpl.class);
}
