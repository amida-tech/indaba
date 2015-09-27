/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.util;

import com.ocs.indaba.common.Constants;
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

    public static void setCookie(HttpServletResponse response, String cookieName, String cookieValue, int maxAge) {
        logger.debug("Save cookie: " + cookieName + "=" + cookieValue);
        Cookie c = new Cookie(cookieName, cookieValue);
        c.setPath("/");
        c.setMaxAge(maxAge);
        response.addCookie(c);
    }

    public static void setCookie(HttpServletResponse response, String cookieName, String cookieValue) {
        setCookie(response, cookieName, cookieValue, Constants.COOKIE_MAX_AGE_DEL_WHEN_EXITS);
    }

    public static void setCookie(HttpServletResponse response, String cookieName, int cookieValue) {
        setCookie(response, cookieName, String.valueOf(cookieValue), Constants.COOKIE_MAX_AGE_DEL_WHEN_EXITS);
    }

    public static void clearCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
            	if(Constants.COOKIE_LANGUAGE.equals(c.getName())){
            		continue;
            	}
                c.setValue("");
                c.setPath("/");
                c.setMaxAge(0);
                response.addCookie(c);
                logger.debug("Clear cookie '" + c.getName() + "'");
            }
        }
    }

    public static void clearTokenCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie c = new Cookie(Constants.COOKIE_TOKEN, "");
        c.setPath("/");
        c.setMaxAge(0);
        response.addCookie(c);
    }

    public static void clearCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Cookie c = new Cookie(cookieName, "");
        c.setPath("/");
        c.setMaxAge(0);
        response.addCookie(c);
    }
    
    public static String getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null || cookies.length == 0) {
            return null;
        } else {
            for(Cookie c: cookies) {
                if(cookieName.equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        return null;
    }
}
