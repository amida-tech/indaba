/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.util;

import com.ocs.common.Config;
import com.ocs.common.Constants;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff
 */
public class CookieUtils {

    private static final Logger logger = Logger.getLogger(CookieUtils.class);

    public static void setCookie(HttpServletResponse response, String cookieName, String cookieValue, int maxAge, String path) {
        logger.debug("Save cookie: " + cookieName + "=" + cookieValue);
        Cookie c = new Cookie(cookieName, cookieValue);
        c.setPath(path);
        c.setMaxAge(maxAge);
        //c.setDomain(domain);
        response.addCookie(c);
    }


    public static void setCookie(HttpServletResponse response, String cookieName, String cookieValue, int maxAge) {
        setCookie(response, cookieName, cookieValue, maxAge, "/");
    }

    public static void setCookie(HttpServletResponse response, String cookieName, String cookieValue) {
        setCookie(response, cookieName, cookieValue, 1000 * Config.getInt(Config.KEY_SESSION_TOKEN_TIMEOUT, Constants.DEFAULT_SESSION_TOKEN_TIMEOUT));
    }

    public static void setCookie(HttpServletResponse response, String cookieName, int cookieValue) {
        setCookie(response, cookieName, String.valueOf(cookieValue), 1000 * Config.getInt(Config.KEY_SESSION_TOKEN_TIMEOUT, Constants.DEFAULT_SESSION_TOKEN_TIMEOUT));
    }
    
    public static String getCookie(HttpServletRequest request, String cookieName) {
        String value = null;
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (cookieName.equals(c.getName())) {
                    value = c.getValue();
                }
            }
        }
        return value;
    }
    
    public static void clearCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                c.setValue("");
                c.setPath("/");
                c.setMaxAge(0);
                response.addCookie(c);
                logger.debug("Clear cookie '" + c.getName() + "'");
            }
        }
    }

    public static String getTokenCookie(HttpServletRequest request) {
        return getCookie(request, getTokenCookieName());
    }


    public static void clearTokenCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie c = new Cookie(getTokenCookieName(), "");
        c.setPath("/");
        c.setMaxAge(0);
        response.addCookie(c);
    }

    public static void setTokenCookie(HttpServletResponse response, String cookieValue) {
        setCookie(response, getTokenCookieName(), cookieValue);
    }

    private static final String KEY_TOKEN_COOKIE_NAME = "token.cookie.name";
    private static String TOKEN_COOKIE_NAME = null;

    public static String getTokenCookieName() {
        if (TOKEN_COOKIE_NAME == null) {
            TOKEN_COOKIE_NAME = Config.getString(KEY_TOKEN_COOKIE_NAME, Constants.COOKIE_USER_TOKEN);
        }

        return TOKEN_COOKIE_NAME;
    }

    public static void clearCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Cookie c = new Cookie(cookieName, "");
        c.setPath("/");
        c.setMaxAge(0);
        response.addCookie(c);
    }
}
