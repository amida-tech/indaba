/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.util;

import com.ocs.common.Config;
import com.ocs.common.Constants;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff
 */
public class UserSessionManager {

    private static final Logger logger = Logger.getLogger(UserSessionManager.class);
    private static UserSessionManager instance = new UserSessionManager();
    private static Map<String, IdentityUser> tokenUserMap = new ConcurrentHashMap<String, IdentityUser>();
    private static final long MONITOR_INTERVAL = 60 * 1000; // 60 s
    private static boolean stop = true;
    private static long sessionTimeout = 1000 * Config.getInt(Config.KEY_SESSION_TOKEN_TIMEOUT, Constants.DEFAULT_SESSION_TOKEN_TIMEOUT);

    private UserSessionManager() {
    }

    public static UserSessionManager getInstance() {
        return instance;
    }

    public void startMonitor() {
        stop = false;
        Thread monitor = new Thread(new Runnable() {

            public void run() {
                while (true) {
                    //logger.debug("Check user session token and remove those expired token(DEFAULT TIMEOUT=" + sessionTimeout + "ms).");
                    Set<String> tokens = tokenUserMap.keySet();
                    try {
                        if (tokens != null) {
                            long now = System.currentTimeMillis();
                            for (String token : tokens) {
                                IdentityUser user = tokenUserMap.get(token);
                                if (user != null) {
                                    long lastAccessTime = user.getLastAccessTime();
                                    if (now - lastAccessTime > sessionTimeout) {
                                        logger.info("Remove expired token: " + token + ". [now=" + now + ", lastAccess=" + lastAccessTime + ", (now-lastAccessTime)=" + (now - lastAccessTime) + ", sessionTimeout=" + sessionTimeout + "].");
                                        removeUserToken(token);
                                    }
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
        IdentityUser user = tokenUserMap.get(token);
        logger.debug("Refresh user's last access time: " + user);
        if (user != null) {
            long now = System.currentTimeMillis();
            logger.debug("Refresh last access time: " + now);
            user.setLastAccessTime(now);
        }
    }

    public int getUserId(String token) {
        if (StringUtils.isEmpty(token)) {
            return Constants.INVALID_INT_ID;
        }
        IdentityUser user = tokenUserMap.get(token);
        return (user != null) ? user.getUserId() : Constants.INVALID_INT_ID;
    }

    public IdentityUser getLoginUser(String token) {
        return (!StringUtils.isEmpty(token)) ? tokenUserMap.get(token) : null;
    }

    public void addUserToken(int userId, String token) {
        tokenUserMap.put(token, new IdentityUser(userId, System.currentTimeMillis()));
    }
    /*
     * public void removeUserToken(int userId, String token) {
     * tokenUserMap.remove(token); }
     */

    public void removeUserToken(String token) {
        if (!StringUtils.isEmpty(token)) {
            tokenUserMap.remove(token);
        }
    }

    public void clearUserTokens() {
        tokenUserMap.clear();
    }

    @Override
    public String toString() {
        return tokenUserMap.toString();
    }

    public class IdentityUser {

        int userId;
        long lastAccessTime;

        public IdentityUser() {
        }

        public IdentityUser(int userId, long lastAccessTime) {
            this.userId = userId;
            this.lastAccessTime = lastAccessTime;
        }

        public IdentityUser(int userId, int vendorId, long lastAccessTime) {
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
