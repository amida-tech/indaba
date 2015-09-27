/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.filter;

import com.ocs.common.Constants;
import com.ocs.util.CookieUtils;
import com.ocs.util.StringUtils;
import com.ocs.util.UserSessionManager;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff
 */
public class AuthFilter implements Filter {

    private static final Logger logger = Logger.getLogger(AuthFilter.class);
    /*
     * private static final String[] IGNORE_URLS = { "aboutUs.do",
     * "services.do", "contactUs.do", "faq.do", "login.do" };
     */
    private static final String PARAM_EXCLUDE_PATTERNS = "excludePatterns";
    private static final String PARAM_COOKIE_NAME_OF_TOKEN = "cookieOfUsertoken";
    private static final String PARAM_UNAUTHED_ERROR_PAGE = "errorPage";
    private static final String DEFAULT_UNAUTHED_ERROR_PAGE = "/error.jsp";
    @SuppressWarnings("unused")
    private ServletContext servletContext = null;
    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;
    private String cookieNameOfUserToken = null;
    private String unauthedErrorPage = null;
    private List<String> excludePatterns = null;

    public AuthFilter() {
    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;
        String lang = CookieUtils.getCookie(httpReq, Constants.COOKIE_LANGUAGE);

        if (StringUtils.isEmpty(lang)) {
            lang = Integer.toString(Constants.DEFAULT_LANGUAGE_ID);
            CookieUtils.setCookie(httpResp, Constants.COOKIE_LANGUAGE, lang);
        }
        request.setAttribute(Constants.ATTR_LANG, lang);
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;
        String loginPage = httpReq.getContextPath() + unauthedErrorPage;

        doBeforeProcessing(request, response);

        try {
            if (isAuthenticated(httpReq, httpResp)) {
                chain.doFilter(request, httpResp);
            } else {
//                servletContext.getRequestDispatcher(unauthedErrorPage).forward(request, response);
                logger.debug("Not authenticated. Redirect to login page!");
                StringBuffer reqURLBuf = httpReq.getRequestURL();
                String queryStr = httpReq.getQueryString();
                if(!StringUtils.isEmpty(queryStr)) {
                    reqURLBuf.append('?').append(queryStr);
                }
                httpResp.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
                httpResp.sendRedirect(loginPage+"?redirect=true&returl=" + URLEncoder.encode(reqURLBuf.toString(), "UTF-8"));
                return;
            }
        } catch (Throwable t) {
            logger.error("Server internal error occurs!", t);
            request.setAttribute("errmsg", "Internal server error/exception occurs. Please contact your administrator!");
//            servletContext.getRequestDispatcher(unauthedErrorPage).forward(request, response);
            httpResp.sendRedirect(loginPage);
            return;
        }

        doAfterProcessing(request, response);

    }

    public boolean isAuthenticated(HttpServletRequest request, HttpServletResponse response) {
        String reqUri = request.getRequestURI();
        //ignore the static resources
        if (isExcluded(excludePatterns, reqUri, request.getContextPath())) {
            return true;
        }
        
        logger.debug("Accept request: " + reqUri);

        String token = CookieUtils.getCookie(request, cookieNameOfUserToken);

        if (StringUtils.isEmpty(token)) {
            token = (String) request.getAttribute(cookieNameOfUserToken);
        }

        logger.debug("Check user's token: " + token);
        UserSessionManager userSessionMgr = UserSessionManager.getInstance();
        if (!userSessionMgr.checkTokenExisted(token)) {
            return false;
        } else {
            CookieUtils.setTokenCookie(response, token);
            request.setAttribute(cookieNameOfUserToken, token);
            userSessionMgr.refreshLastAccessTime(token);
            return true;
        }

    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            servletContext = filterConfig.getServletContext();
            String nonAuthUrls = filterConfig.getInitParameter(PARAM_EXCLUDE_PATTERNS);
            if (!StringUtils.isBlank(nonAuthUrls)) {
                String[] arr = nonAuthUrls.split("[\\s|,|;|\\|]+");
                excludePatterns = Arrays.asList(arr);
            }

            cookieNameOfUserToken = filterConfig.getInitParameter(PARAM_COOKIE_NAME_OF_TOKEN);
            if (StringUtils.isBlank(cookieNameOfUserToken)) {
                cookieNameOfUserToken = CookieUtils.getTokenCookieName();
            }

            unauthedErrorPage = filterConfig.getInitParameter(PARAM_UNAUTHED_ERROR_PAGE);
            if (StringUtils.isBlank(cookieNameOfUserToken)) {
                unauthedErrorPage = DEFAULT_UNAUTHED_ERROR_PAGE;
            }
        }
    }

    private static boolean isExcluded(List<String> excludePatterns, String reqUri, String cntxtPath) {
        if (excludePatterns != null && !excludePatterns.isEmpty()) {
            reqUri = (cntxtPath.length() == 1) ? reqUri.substring(1) : reqUri.substring(cntxtPath.length() + 1);

            for (String url : excludePatterns) {
                if (reqUri.matches(url)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("^login.*");
        list.add("^logout.*");
        list.add("^resources.*");
        list.add("jsMessages");
        System.out.println(isExcluded(list, "/ControlPanel/", "/ControlPanel"));
        System.out.println(isExcluded(list, "/ControlPanel/login", "/ControlPanel"));
        System.out.println(isExcluded(list, "/ControlPanel/login/", "/ControlPanel"));
        System.out.println(isExcluded(list, "/ControlPanel/resources/css/styles.css", "/ControlPanel"));
        System.out.println(isExcluded(list, "/", "/"));
        System.out.println(isExcluded(list, "/login", "/"));
        System.out.println(isExcluded(list, "/login/", "/"));
        System.out.println(isExcluded(list, "/resources/css/styles.css", "/"));
        System.out.println(isExcluded(list, "/ControlPanel/jsMessages", "/ControlPanel"));
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("AuthFilter()");
        }
        StringBuilder sb = new StringBuilder("AuthFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }
}
