/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.util;

import com.ocs.indaba.aggregation.common.ErrorCode;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff
 */
public class HttpUtils {

    private static final Logger LOG = Logger.getLogger(HttpUtils.class);

    public static int download(String url, File destFile) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = null;
        OutputStream out = null;
        try {
            LOG.debug("Download file from url: " + url + " and save to: " + destFile.getAbsolutePath());
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                out = new BufferedOutputStream(new FileOutputStream(destFile));
                entity.writeTo(out);
                out.flush();
            }
        } catch (Exception ex) {
            LOG.error("Fail to download file by url: " + url, ex);
            return ErrorCode.ERR_DOWNLOAD_FULE;
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (Exception ex) {
                }
            }
        }
        return ErrorCode.OK;
    }


    static public boolean checkClientIp(String clientIp, List<String> whitelist) {
        if (whitelist == null) {
            return false;
        }

        for (String allowedIp : whitelist) {
            if ("localhost".equals(allowedIp)) {
                allowedIp = "127.0.0.1";
            }
            allowedIp = allowedIp.replaceAll("\\*", "[\\d\\D]*");
            if (clientIp.matches(allowedIp)) {
                return true;
            }
        }
        return false;
    }



    public static int download(String url, String filePath) {
        return download(url, new File(filePath));
    }
}
