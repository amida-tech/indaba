/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.service;

import com.ocs.common.Config;
import com.ocs.indaba.aggregation.vo.OrgSecurity;
import com.ocs.indaba.dao.ProjectDAO;
import com.ocs.indaba.dao.ApicallDAO;
import com.ocs.indaba.dao.HorseDAO;
import com.ocs.indaba.dao.OrgkeyDAO;
import com.ocs.indaba.po.Apicall;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.dao.OrganizationDAO;
import com.ocs.indaba.dao.ProductDAO;
import com.ocs.indaba.dao.SurveyConfigDAO;
import com.ocs.indaba.po.Organization;
import com.ocs.indaba.po.Orgkey;
import com.ocs.indaba.po.Product;
import com.ocs.indaba.po.SurveyConfig;
import java.util.Calendar;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author luwenbin
 */
public class ApicallService {
    private static final Logger LOG = Logger.getLogger(ApicallService.class);
    private ApicallDAO apicallDao = null;
    private OrgkeyDAO orgkeyDao = null;
    private OrganizationDAO organizationDao = null;
    private ProjectDAO projectDao = null;
    private HorseDAO horseDao = null;
    private ProductDAO productDao = null;
    private SurveyConfigDAO scDao = null;
    private OrgkeyService orgkeyService = null;

    public static final short PROJECT_PUBLIC_VISIBILITY = 1;
    public static final short PROJECT_PRIVATE_VISIBILITY = 2;

    public static final int CONTENT_NO_CHECK = 1;
    public static final int CONTENT_PUBLIC_CHECK = 2;
    public static final int CONTENT_PRIVATE_CHECK = 3;

    @Autowired
    public void setApicallDao(ApicallDAO apicallDao){
        this.apicallDao = apicallDao;
    }

    @Autowired
    public void setOrgkeyDao(OrgkeyDAO orgkeyDao) {
        this.orgkeyDao = orgkeyDao;
    }

    @Autowired
    public void setOrganizationDao(OrganizationDAO organizationDao) {
        this.organizationDao = organizationDao;
    }

    @Autowired
    public void setProjectDao(ProjectDAO projectDao){
        this.projectDao = projectDao;
    }

    @Autowired
    public void setHorseDao(HorseDAO horseDao){
        this.horseDao = horseDao;
    }

    @Autowired
    public void setProductDao(ProductDAO productDao){
        this.productDao = productDao;
    }

    @Autowired
    public void setSurveyConfigDao(SurveyConfigDAO dao){
        this.scDao = dao;
    }

    @Autowired
    public void setOrgkeyService(OrgkeyService service){
        this.orgkeyService = service;
    }

    public short authorization(Apicall apicall){
        Project project = projectDao.selectProjectByProductId(apicall.getProductId());
        if(project == null)
            return Constants.AUTHORIZATION_FAIL;

        /*public data*/
        if(project.getVisibility() == PROJECT_PUBLIC_VISIBILITY)
            return Constants.AUTHORIZATION_OK;

        /*private data, the caller must be the data owner*/
        int ownOrgId = project.getOrganizationId();
        if(ownOrgId == apicall.getOrganizationId())
            return Constants.AUTHORIZATION_OK;
        else
            return Constants.AUTHORIZATION_FAIL;
    }

    public int getCheckType(int productId){
        Project project = projectDao.selectProjectByProductId(productId);
        if(project == null)
            return CONTENT_PRIVATE_CHECK;//not valid project should be logged

        Organization org = organizationDao.get(project.getOrganizationId());
        if(org == null)
            return CONTENT_PRIVATE_CHECK;//not valid organization should be logged

        if(!org.getEnforceApiSecurity())
            return CONTENT_NO_CHECK;
        else{
            if(project.getVisibility() == PROJECT_PUBLIC_VISIBILITY)
                return CONTENT_PUBLIC_CHECK;
            else
                return CONTENT_PRIVATE_CHECK;
        }
    }

    public Product getProductByHorseId (int horseId){
        return productDao.selectProductByHorseId(horseId);
    }

    public Product getProductById (int productId){
        return productDao.selectProductById(productId);
    }

    public SurveyConfig getSurveyConfigById(int scId) {
        return scDao.get(scId);
    }

    public short authentication(Apicall apicall, OrgSecurity os){
        /*timestamp check*/
        long time = Calendar.getInstance().getTimeInMillis();
        int timeInSeconds = (int)(time / 1000);
        int interval = Math.abs(timeInSeconds - os.getSt());
        if(interval > Config.getInt(Constants.KEY_API_TIMESTAMP_INTERVAL))
            return Constants.AUTHENTICATION_CALL_TOO_OLD;

        /*check url*/
        Apicall oldCall = apicallDao.selectApicallByUrlAndAuthz(apicall.getUrl(), Constants.AUTHENTICATION_OK);
        if(oldCall != null)
            return Constants.AUTHENTICATION_REPLAY;

        /*check org*/
        Organization org = organizationDao.get(os.getSo());
        if(org == null)
            return Constants.AUTHENTICATION_BAD_ORG;

        /*check key*/
        Orgkey orgkey = orgkeyDao.selectOrgkeyByOrgIdAndVersion(os.getSo(), os.getSv());
        if(orgkey == null)
            return Constants.AUTHENTICATION_KEY_NOT_FOUND;
        if(orgkey.getStatus() == OrgkeyService.ORGKEY_STATUS_REVOKED)
            return Constants.AUTHENTICATION_KEY_REVOKED;
        if(orgkeyService.isKeyExpired(orgkey))
            return Constants.AUTHENTICATION_KEY_EXPIRED;

        /*calculate digest*/
        String url = apicall.getUrl();
        int index = url.lastIndexOf("&");
        String content = url.substring(0, index);
        String digest = orgkeyService.calculateDigest(content, orgkey);
        if(!digest.equalsIgnoreCase(os.getSd()))
            return Constants.AUTHENTICATIOND_BAD_DIGEST;

        return Constants.AUTHENTICATION_OK;
    }

    public void recordApicall(Apicall apicall){
        apicallDao.save(apicall);
    }
}
