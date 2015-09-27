/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.util;

import java.util.UUID;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Jeff
 */
public class UniqueTokenGenerator {

    private static UniqueTokenGenerator instance = new UniqueTokenGenerator();

    private UniqueTokenGenerator() {
    }

    public static UniqueTokenGenerator getInstance() {
        return instance;
    }

    public synchronized String generateUserSessionToken(int userId) {
        StringBuilder sb = new StringBuilder();
        sb.append(userId);
        sb.append(UUID.randomUUID().toString());
        sb.append("Oscar");
        sb.append(System.currentTimeMillis());
        return DigestUtils.md5Hex(sb.toString());
    }

    public synchronized String generateUeserUid(String username) {
        StringBuilder sb = new StringBuilder();
        sb.append(UUID.randomUUID().toString());
        sb.append(System.currentTimeMillis());
        sb.append(username);
        return DigestUtils.md5Hex(sb.toString());
    }
    
    
    public synchronized String generateInvitationToken(String email) {
        StringBuilder sb = new StringBuilder();
        sb.append(UUID.randomUUID().toString());
        sb.append(email);
        sb.append(System.currentTimeMillis());
        return DigestUtils.md5Hex(sb.toString());
    }
}
