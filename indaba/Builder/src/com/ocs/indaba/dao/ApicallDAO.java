/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Apicall;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author luwenbin
 */
public class ApicallDAO extends SmartDaoMySqlImpl<Apicall, Integer>{
    private static final Logger log = Logger.getLogger(OrgkeyDAO.class);
    private static final String SELECT_APICALL_BY_URL =
            "SELECT * FROM apicall where url=?";
    private static final String SELECT_APICALL_BY_URL_AND_AUTHZ =
            "SELECT * FROM apicall where url=? and authn_code=?";

    public List<Apicall> selectApicallByUrl(String url){
        log.debug("select apicalls by url:" + url);
        return super.find(SELECT_APICALL_BY_URL, url);
    }

    public Apicall selectApicallByUrlAndAuthz(String url, int authz){
        log.debug("select apicalls by url and authz:" + url + " " + authz);
        return super.findSingle(SELECT_APICALL_BY_URL_AND_AUTHZ, url, authz);
    }
}
