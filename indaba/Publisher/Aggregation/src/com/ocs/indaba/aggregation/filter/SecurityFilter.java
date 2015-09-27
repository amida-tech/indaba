/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.filter;

import com.ocs.indaba.aggregation.service.ApicallService;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.vo.OrgSecurity;
import com.ocs.indaba.po.Apicall;
import com.ocs.indaba.po.Product;
import com.ocs.indaba.po.SurveyConfig;
import java.util.Calendar;
import java.util.Map;

/**
 *
 * @author luwenbin
 */
public class SecurityFilter implements Filter  {
    private static final Logger LOG = Logger.getLogger(SecurityFilter.class);
    private static final String ERROR_PAGE = "/error.jsp";
    private ServletContext servletContext = null;
    private ApicallService apicallService = null;
    
    private static final String PARAM_HORSE_ID = "horseId";
    private static final String PARAM_PRODUCT_ID = "productId";
    private static final String PARAM_ORG_ID = "so";
    private static final String PARAM_TIMESTAMP = "st";
    private static final String PARAM_RANDOM = "sr";
    private static final String PARAM_KEY_VERSION = "sv";
    private static final String PARAM_MESSAGE_DIGEST = "sd";
    private Set<String> pathSet = new HashSet<String>();

    @Autowired
    public void setApicallService(ApicallService apicallService) {
        this.apicallService = apicallService;
    }
    
    public void init(FilterConfig fc) throws ServletException {
        if(fc != null){
            servletContext = fc.getServletContext();
            String param = fc.getInitParameter("checkUrl");
            LOG.info("should checked urls:" + param);
            if(param != null){
                String[] urls = param.split(",");
                for(String url : urls)
                    pathSet.add(url);
            }
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        //HttpServletResponse httpResponse = (HttpServletResponse) response;
        try{
            boolean pass = checkApi(httpRequest);
            if(pass){
                LOG.debug("passed request:" + httpRequest.getRequestURL() + "?" + httpRequest.getQueryString());
                chain.doFilter(request, response);
                return;
            }
            else{
                LOG.debug("reject request:" + httpRequest.getRequestURL() + "?" + httpRequest.getQueryString());
                 request.setAttribute("errMsg", "API Check Failed!");
                 servletContext.getRequestDispatcher(ERROR_PAGE).forward(request, response);
                 return;
            }
        }catch (Throwable t) {
            LOG.error("Server internal error occurs!", t);
            request.setAttribute("errMsg", "Internal server error/exception occurs. Please contact your administrator!");
            servletContext.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        }
    }

    public void destroy() {
    }

    private boolean checkApi(HttpServletRequest request){
        /*get function*/
        String uri = request.getRequestURI();
        String func;
        int index = uri.lastIndexOf("/");
        if(index > 0)
            func = uri.substring(index + 1);
        else
            func = uri;
        /*test if should be checked*/
        if(!shouldCheckUri(func))
            return true;

        /*get productId and horseId*/
        int productId = NumberUtils.toInt(request.getParameter(PARAM_PRODUCT_ID), 0);
        int horseId = NumberUtils.toInt(request.getParameter(PARAM_HORSE_ID), 0);
        Product product = null;

        if (productId <= 0) {//no productId, use horseId instead
            if (horseId > 0) {
                product = apicallService.getProductByHorseId(horseId);
                if (product == null) {
                    LOG.error("No product found for horse " + horseId);
                    return false;
                }
                productId = product.getId();
            }
        } else {
            product = apicallService.getProductById(productId);

            if (product == null) {
                LOG.error("No product found for product ID " + productId);
                return false;
            }
        }

        if (product != null) {
            // check TSC for survey product.
            if (product.getContentType() == com.ocs.indaba.common.Constants.CONTENT_TYPE_SURVEY) {
                SurveyConfig sc = apicallService.getSurveyConfigById(product.getProductConfigId());
                if (sc == null) {
                    LOG.error("No survey-config found for product " + productId);
                    return false;
                }
                if (!sc.getIsTsc()) {
                    LOG.error("Survey-config " + sc.getId() + " used by [product " + productId + ", horse " + horseId + "] is not TSC!");
                    return false;
                }
            }
        }

        if (productId > 0){
            int type = apicallService.getCheckType(productId);
            if(type == ApicallService.CONTENT_NO_CHECK)//content of this org should not be checked
                return true;

            int params = getParameterNums(request);
            if(type == ApicallService.CONTENT_PUBLIC_CHECK && params == 0)//public visit which not contains any security parameters should passed
                return true;
        }

        //private data(include invalid product Id) and public data with any security parameter should be logged
        Apicall apicall = new Apicall();
        apicall.setCallTime(Calendar.getInstance().getTime());
        apicall.setFunc(func);
        apicall.setHorseId(horseId);
        apicall.setProductId(productId);
        /*get url*/
        String url = request.getRequestURL() + "?" + request.getQueryString();
        apicall.setUrl(url);
        /*get ip*/
        String ip = request.getRemoteAddr();
        apicall.setIpAddr(ip);
        short authn = Constants.AUTHENTICATION_OK;
        short authz = Constants.AUTHORIZATION_OK;

        if(productId <= 0){
            authn = Constants.AUTHENTICATION_BAD_SYNTAX;
        }

        int last = url.lastIndexOf("&");
        String lastParam = url.substring(last+1);
        if(!lastParam.startsWith("sd="))//last parameter must be sd
            authn = Constants.AUTHENTICATION_BAD_SYNTAX;
        
        /*get org id*/
        int so = NumberUtils.toInt(request.getParameter(PARAM_ORG_ID), 0);
        apicall.setOrganizationId(so);
        if(so <= 0)
            authn = Constants.AUTHENTICATION_BAD_SYNTAX;

        /*get timestamp*/
        int st = NumberUtils.toInt(request.getParameter(PARAM_TIMESTAMP), 0);
        if(st <= 0)
            authn = Constants.AUTHENTICATION_BAD_SYNTAX;

        /*get random*/
        int sr = NumberUtils.toInt(request.getParameter(PARAM_RANDOM), -1);
        if(sr < 0)
            authn = Constants.AUTHENTICATION_BAD_SYNTAX;

        /*get key version*/
        int sv = NumberUtils.toInt(request.getParameter(PARAM_KEY_VERSION), 0);
        apicall.setKeyVersion(sv);
        if(sv <= 0)
            authn = Constants.AUTHENTICATION_BAD_SYNTAX;

        String sd = request.getParameter(PARAM_MESSAGE_DIGEST);
        if(sd == null)
            authn = Constants.AUTHENTICATION_BAD_SYNTAX;

        if(authn == Constants.AUTHENTICATION_OK){
            OrgSecurity security = new OrgSecurity(so, st, sr, sv, sd);
            authn = apicallService.authentication(apicall, security);
            if(authn == Constants.AUTHENTICATION_OK){
                authz = apicallService.authorization(apicall);
            }
        }
        apicall.setAuthnCode(authn);
        apicall.setAuthzCode(authz);
        apicallService.recordApicall(apicall);
        return authn == Constants.AUTHENTICATION_OK && authz == Constants.AUTHORIZATION_OK;
    }

    private boolean shouldCheckUri(String url){
        return pathSet.contains(url);
    }

    private int getParameterNums(HttpServletRequest request){
        Map params = request.getParameterMap();
        int result = 0;
        if(params.containsKey(PARAM_ORG_ID))
            result++;
        if(params.containsKey(PARAM_TIMESTAMP))
            result++;
        if(params.containsKey(PARAM_KEY_VERSION))
            result++;
        if(params.containsKey(PARAM_RANDOM))
            result++;
        if(params.containsKey(PARAM_MESSAGE_DIGEST))
            result++;
        return result;
    }

}
