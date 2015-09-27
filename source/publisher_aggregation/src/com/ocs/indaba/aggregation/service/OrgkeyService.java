/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.service;

import com.ocs.indaba.aggregation.vo.OrgkeyVO;
import com.ocs.indaba.dao.OrganizationDAO;
import com.ocs.indaba.dao.OrgkeyDAO;
import com.ocs.indaba.po.Organization;
import com.ocs.indaba.po.Orgkey;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author luwenbin
 */
public class OrgkeyService {
    public static final int DEFAULT_VALID_DAYS = 365;
    public static final int DAY_IN_MILLISECONDS = 24*3600*1000;
    public static final int DEFAULT_VERSION = 1;
    public static final String DEFAULT_HASH_ALGORITHM = "sha1";

    public static final short ORGKEY_STATUS_NORMAL = 1;
    public static final short ORGKEY_STATUS_REVOKED = 2;
    
    private static final Logger LOG = Logger.getLogger(OrgkeyService.class);
    private static final String SECRET_KEY = "b40@^36&097~b36e#06*58F";
    private OrgkeyDAO orgkeyDao = null;
    private OrganizationDAO organizationDao = null;

    @Autowired
    public void setOrgkeyDao(OrgkeyDAO orgkeyDao) {
        this.orgkeyDao = orgkeyDao;
    }

    @Autowired
    public void setOrganizationDao(OrganizationDAO organizationDao) {
        this.organizationDao = organizationDao;
    }

    private String byte2Hex(byte[] bytes){
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<bytes.length; i++){
            String s = Integer.toHexString(bytes[i] & 0xFF);
            if(s.length() == 1)
                s = "0" + s;
            sb.append(s);
        }
        return sb.toString().toLowerCase();
    }

    private String generateKeyForOrg(Organization org){
        String now = Long.toString(System.currentTimeMillis());
        String name = org.getName() + now + SECRET_KEY;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(name.getBytes());
            byte[] bytes = md.digest();
            String key = byte2Hex(bytes);
            LOG.debug("generate key:" + key);
            return key;
        } catch (NoSuchAlgorithmException ex) {
            LOG.error(ex);
            return null;
        }
    }

    public Orgkey generateOrgkey(int orgId, int userId){
        Organization org = organizationDao.get(orgId);
        if(org == null)
            return null;
        return generateOrgkey(org, userId);
    }

    public Orgkey generateOrgkey(Organization org, int userId){
        String key = generateKeyForOrg(org);
        if(key == null){
            LOG.error("generate orgkey for organization " + org.getName() + " failed");
            return null;
        }
        int version = DEFAULT_VERSION;
        List<Orgkey> orgkeys = orgkeyDao.selectOrgkeyByOrgId(org.getId());
        if(orgkeys != null && orgkeys.size() > 0){//have other version orgkey, get max version
            for(Orgkey orgkey : orgkeys){
                int v = orgkey.getVersion();
                if(v > version)
                    version = v;
            }
            version++;
        }
        Calendar now = Calendar.getInstance();
        Orgkey orgkey = new Orgkey();
        orgkey.setOrganizationId(org.getId());
        orgkey.setVersion(version);
        orgkey.setHashAlgorithm(DEFAULT_HASH_ALGORITHM);
        orgkey.setIssueTime(now.getTime());
        orgkey.setIssueUserId(userId);
        orgkey.setEffectiveTime(now.getTime());
        orgkey.setValidDays(DEFAULT_VALID_DAYS);
        orgkey.setStatus(ORGKEY_STATUS_NORMAL);
        orgkey.setData(key);
        return orgkeyDao.save(orgkey);
    }

    public void generateAllOrgkey(){
        List<Organization> orgs = organizationDao.selectAllOrgs();
        for(Organization org : orgs){
            generateOrgkey(org, 0);
        }
    }

    public List<OrgkeyVO> getAllOrgkeyView(){
        List<Organization> orgs = organizationDao.selectAllOrgs();
        if(orgs == null || orgs.size() == 0)
            return null;

        List<OrgkeyVO> orgkeyViews = new ArrayList<OrgkeyVO>();
        for(Organization org : orgs){
            OrgkeyVO orgkeyView = new OrgkeyVO();
            orgkeyView.setOrgName(org.getName());
            orgkeyView.setOrgId(org.getId());
            List<Orgkey> orgkeys = orgkeyDao.selectOrgkeyByOrgId(org.getId());
            if(orgkeys == null || orgkeys.size() == 0){//no keys for this organization
                orgkeyView.setKeys(null);
            }else{
                orgkeyView.setKeys(orgkeys);
            }
            orgkeyViews.add(orgkeyView);
        }
        return orgkeyViews;
    }

    public boolean isKeyExpired(Orgkey key){
        long begin = key.getEffectiveTime().getTime();
        long now = Calendar.getInstance().getTimeInMillis();
        long end = begin + key.getValidDays() * DAY_IN_MILLISECONDS;
        if((now >= begin) && (now <= end))
            return false;
        else
            return true;
    }

    public String calculateDigest(String data, Orgkey orgkey){
        try {
            MessageDigest md = MessageDigest.getInstance(orgkey.getHashAlgorithm());
            String content = data + "+" + orgkey.getData();
            LOG.debug(content);
            md.update(content.getBytes());
            byte[] bytes = md.digest();
            return byte2Hex(bytes);
        } catch (NoSuchAlgorithmException ex) {
            LOG.error("not algorithm function " + orgkey.getHashAlgorithm(), ex);
            return null;
        }
    }

    public String calculateDigest(String url){
        try {
            MessageDigest md = MessageDigest.getInstance(DEFAULT_HASH_ALGORITHM);
            md.update(url.getBytes());
            byte[] bytes = md.digest();
            return byte2Hex(bytes);
        } catch (NoSuchAlgorithmException ex) {
            LOG.error("not algorithm function " + DEFAULT_HASH_ALGORITHM, ex);
            return null;
        }
    }

    public static void main(String args[]){
        Orgkey orgkey = new Orgkey();
        orgkey.setHashAlgorithm("sha1");
        orgkey.setData("HJWE883BJQWRKLMO3823RNA12NSD8h2w");
        OrgkeyService service = new OrgkeyService();
        String content = "http://indabaplatform.com/ids/apiname&horse=123&parms&so=101&st=20120421102215&sr=32498&sv=a1";
        LOG.debug(service.calculateDigest(content, orgkey));
    }
}