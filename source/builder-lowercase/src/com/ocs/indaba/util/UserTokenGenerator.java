/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.util;

import java.util.UUID;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Jeff
 */
public class UserTokenGenerator {

    private static UserTokenGenerator instance = new UserTokenGenerator();
    private static long previous = 0;

    private UserTokenGenerator() {
    }

    public static UserTokenGenerator getInstance() {
        return instance;
    }

    public synchronized String generateToken(int userId) {
        /***

        StringBuilder sb = new StringBuilder();
        sb.append(userId);
        sb.append(UUID.randomUUID().toString());
        sb.append("Indaba");
        sb.append(System.currentTimeMillis());
        return DigestUtils.md5Hex(sb.toString());
         * ***/
       return UUID.randomUUID().toString();
    }
}
