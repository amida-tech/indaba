/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.util;

import com.ocs.common.Config;
import com.ocs.indaba.common.Constants;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff
 */
public class UserSessionManager {

    private static final Logger logger = Logger.getLogger(UserSessionManager.class);
    private static UserSessionManager instance = new UserSessionManager();
    private static Map<String, UserSession> tokenUserMap = new ConcurrentHashMap<String, UserSession>();
    private static final long MONITOR_INTERVAL = 60 * 1000; // 60 s
    private static boolean stop = true;

    private UserSessionManager() {
    }

    public static UserSessionManager getInstance() {
        return instance;
    }

    public void startMonitor() {
        
        stop = false;
        Thread monitor = new Thread(new Runnable() {

            public void run() {
                long sessionTimeout = 1000 * Config.getInt(Config.KEY_SESSION_TOKEN_TIMEOUT, Constants.DEFAULT_SESSION_TOKEN_TIMEOUT);

                while (true) {
                    logger.debug("Check user session token and remove those expired token.");
                    Set<String> tokens = tokenUserMap.keySet();
                    try {
                        if (tokens != null) {
                            long now = System.currentTimeMillis();
                            for (String token : tokens) {
                                UserSession user = tokenUserMap.get(token);
                                if (user != null) {
                                    long lastAccessTime = user.getLastAccessTime();
                                    logger.debug("Checking token: " + token + " LAT=" + lastAccessTime + " NOW=" + now + " TOUT= " + sessionTimeout + " LIFE=" + (now-lastAccessTime));
                                    if (now - lastAccessTime > sessionTimeout) {
                                        logger.info("Remove expired token after " + sessionTimeout + "seconds: " + token + ". [lastAccess=" + lastAccessTime + " Now=" + now + "].");
                                        removeUserToken(token);
                                    }
                                } else {
                                    logger.info("Logic Error: no user associated with session token: " + token);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        logger.error("Fail to check session token!", ex);
                    }
                    try {
                        Thread.sleep(MONITOR_INTERVAL);
                    } catch (InterruptedException ex) {
                    }
                    if (stop) {
                        break;
                    }
                }
            }
        });
        monitor.start();
    }

    public void stopMonitor() {
        stop = true;
    }

    public boolean checkTokenExisted(String token) {
        return (!StringUtils.isEmpty(token) && tokenUserMap.get(token) != null);
    }
    
    public void refreshLastAccessTime(String token) {
        UserSession user = tokenUserMap.get(token);
        long now = System.currentTimeMillis();
        if(user != null) {
            user.setLastAccessTime(now);
            logger.debug("Set lastAccessTime for session token: " + token + " to " + now);
        }
        else {
            logger.debug("Can't find session token: " + token);
        }
    }

    public int getUserId(String token) {
        UserSession user = tokenUserMap.get(token);
        return (user != null) ? user.getUserId() : Constants.INVALID_INT_ID;
    }

    public UserSession getLoginUser(String token) {
        return (!StringUtils.isEmpty(token)) ? tokenUserMap.get(token) : null;
    }

    public void addUserToken(int userId, String token) {
        tokenUserMap.put(token, new UserSession(userId, System.currentTimeMillis()));
    }
    /*
    public void removeUserToken(int userId, String token) {
    tokenUserMap.remove(token);
    }
     */

    public void removeUserToken(String token) {
        if (!StringUtils.isEmpty(token)) {
            tokenUserMap.remove(token);
            logger.debug("Removed token: " + token);
        }
    }

    public void clearUserTokens() {
        tokenUserMap.clear();
    }

    @Override
    public String toString() {
        return tokenUserMap.toString();
    }

    public class UserSession {

        int userId;
        long lastAccessTime;

        public UserSession() {
        }

        public UserSession(int userId, long lastAccessTime) {
            this.userId = userId;
            this.lastAccessTime = lastAccessTime;
        }

        public long getLastAccessTime() {
            return lastAccessTime;
        }

        public void setLastAccessTime(long lastAccessTime) {
            this.lastAccessTime = lastAccessTime;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "User{" + "userId=" + userId + ", lastAccessTime=" + lastAccessTime + '}';
        }
    }
}
