/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.filter;

import com.ocs.common.Config;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.util.CookieUtils;
import com.ocs.indaba.util.UserSessionManager;
import com.ocs.util.StringUtils;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff
 */
public class AuthFilter implements Filter {

    private static final Logger logger = Logger.getLogger(AuthFilter.class);
    //private static final String IGNORE_LOGIN_URI = "login.do";
    //private static final String IGNORE_RUNWORKFLOW_URI = "runWorkflow.do";
    //private static final String IGNORE_USERFINDER_URI = "userfinder.do";
    //private static final String IGNORE_FIREUSERFINDER_URI = "fireuserfinder.do";
    //private static final String IGNORE_I18NSRCFILES_URI = "i18nSrcFiles.do";

    private static String IPV6_LOCALHOST = "0:0:0:0:0:0:0:1";
    private static String IPV4_127_0_0_1 = "127.0.0.1";
    private static String IPV4_LOCALHOST = "localhost";
    
    private static final String REDIRECT_TO_LOGIN_PAGE = "/login.jsp";
    private static final String ERROR_PAGE = "/error.jsp";
    private ServletContext servletContext = null;
    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    public AuthFilter() {
    }

    private void doBeforeProcessing(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String lang = CookieUtils.getCookie(request, Constants.COOKIE_LANGUAGE);

        if (StringUtils.isEmpty(lang)) {
            lang = Integer.toString(Constants.LANG_EN);
            CookieUtils.setCookie(response, Constants.COOKIE_LANGUAGE, lang);
        }
        /*
         * Locale locale = null; if (Constants.LANG_NAME_FR.equals(lang)) {
         * locale = Locale.FRENCH; } else { locale = Locale.ENGLISH;
        }
         */
        request.setAttribute(Constants.ATTR_LANG, lang);
    }

    private void doAfterProcessing(ServletRequest HttpServletRequest, HttpServletResponse response)
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
        String reqUri = httpReq.getRequestURI();

        doBeforeProcessing(httpReq, httpResp);

        try {
            // check whether the access is permitted
            boolean restricted = false;
            for (String path : Config.getStringArray(Config.KEY_AUTH_RESTRICTED_PATHS)) {
                if (reqUri.endsWith(path)) {
                    restricted = true;
                    break;
                }
            }

            logger.debug("Received request for: " + reqUri);
            if (restricted) {
                logger.debug("The access has IP restriction");

                String clientIp = request.getRemoteAddr();
                logger.debug("Access is from: " + clientIp);

                boolean permitted = verifyWhitelist(getClientIp(httpReq));
                if (!permitted) {
                    logger.info("Deny access from " + clientIp);
                    request.setAttribute("errMsg", "Access prohibited!");
                    servletContext.getRequestDispatcher(REDIRECT_TO_LOGIN_PAGE).forward(request, response);
                    return;
                }

                logger.debug("Access passed restriction from: " + clientIp);
            } else {
                logger.debug("The access has no IP restriction");
            }

            if (isAuthenticated(httpReq, httpResp)) {
                chain.doFilter(request, httpResp);
            } else {
                request.setAttribute("errMsg", "Please sign in!");
                //setReturnUrl(request, true);
                servletContext.getRequestDispatcher(REDIRECT_TO_LOGIN_PAGE).forward(request, response);
                return;
            }
        } catch (Exception ex) {
            logger.error("Server internal error occurs!", ex);
            request.setAttribute(reqUri, "Internal server error/exception occurs. Please contact your administrator!");
            servletContext.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            return;
        }

        doAfterProcessing(httpReq, httpResp);

    }

    private static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (checkClientIp(ip)) {
            return ip;
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (checkClientIp(ip)) {
            return ip;
        }
        if (checkClientIp(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    private static boolean checkClientIp(String clientIp) {
        if (clientIp == null || clientIp.length() == 0 || "unkown".equalsIgnoreCase(clientIp)
                || clientIp.split(".").length != 4) {
            return false;
        }
        return true;
    }

    private static boolean verifyWhitelist(String clientIp) {
        String[] whiteList = Config.getStringArray(Config.KEY_AUTH_ACCESS_WHITELIST);
        logger.debug("IP ADDRESS: " + clientIp);
        if (whiteList != null) {
            for (String ip : whiteList) {
                logger.debug("White IP: " + ip + ", " + IPV4_127_0_0_1.equals(ip) + ", " + IPV4_LOCALHOST.equals(ip) + ", " + clientIp.startsWith(IPV6_LOCALHOST));
                if (ip.equals(clientIp)
                        || ((IPV4_127_0_0_1.equals(ip) || IPV4_LOCALHOST.equals(ip)) && clientIp.startsWith(IPV6_LOCALHOST))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAuthenticated(HttpServletRequest request, HttpServletResponse response) {
        /*
         * if (request.getRequestURI().endsWith(IGNORE_LOGIN_URI) ||
         * request.getRequestURI().endsWith(IGNORE_RUNWORKFLOW_URI) ||
         * request.getRequestURI().endsWith(IGNORE_USERFINDER_URI) ||
         * request.getRequestURI().endsWith(IGNORE_FIREUSERFINDER_URI)) {
         * logger.debug("Ignore user authentication."); return true; }
         */
        String reqUri = request.getRequestURI();
        for (String ignorePath : Config.getStringArray(Config.KEY_AUTH_IGNORE_PATHS)) {
            if (reqUri.endsWith(ignorePath)) {
                return true;
            }
        }
        String token = null;
        String activeTab = null;
        int caseId = Constants.INVALID_INT_ID;
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (Constants.COOKIE_TOKEN.equals(c.getName())) {
                    token = c.getValue();
                }
                if (Constants.COOKIE_ACTIVE_TAB.equals(c.getName())) {
                    activeTab = c.getValue();
                }
                if (Constants.COOKIE_CASE_ID.equals(c.getName())) {
                    caseId = StringUtils.str2int(c.getValue(), Constants.INVALID_INT_ID);
                }
            }
        }

        if (StringUtils.isEmpty(token)) {
            token = (String) request.getAttribute(Constants.COOKIE_TOKEN);
        }

        logger.debug("Check user's token: " + token);
        if (!UserSessionManager.getInstance().checkTokenExisted(token)) {
            logger.debug("Token doesn't exist: " + token);
            return false;
        } else {
            request.setAttribute(Constants.COOKIE_TOKEN, token);
            request.setAttribute(Constants.COOKIE_CASE_ID, Integer.valueOf(caseId));
            if (StringUtils.isEmpty(activeTab)) {
                activeTab = Constants.TAB_YOURCONTENT;
                CookieUtils.setCookie(response, Constants.COOKIE_ACTIVE_TAB, Constants.TAB_YOURCONTENT);
            }
            request.setAttribute(Constants.COOKIE_ACTIVE_TAB, activeTab);

            UserSessionManager.getInstance().refreshLastAccessTime(token);
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
        }
        // userSrvc = (UserService) SpringContextUtil.getBean("userService");
        // projectService = (ProjectService) SpringContextUtil.getBean("projectService");
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
