/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.cache;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Jeff
 */
public class ServiceDataCachePersistence extends BasePersistence {

    private final static Logger logger = Logger.getLogger(ServiceDataCachePersistence.class);

    public static boolean loadCachedIndicatorSummary(int horseId, OutputStream out) {
        boolean ret = false;
        File cachedFile = getIndicatorSummaryCacheFile(horseId, null);
        if (!cachedFile.exists()) {
            return ret;
        }
        InputStream in = null;
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(cachedFile);
            in = new BufferedInputStream(fis);
            byte[] buf = new byte[BUF_SIZE];
            int n = 0;
            while ((n = in.read(buf, 0, BUF_SIZE)) != -1) {
                out.write(buf, 0, n);
                out.flush();
            }
            ret = true;
        } catch (Exception ex) {
            logger.error("Fail to load cache file: " + cachedFile.getAbsolutePath(), ex);
        } finally {
            // CLOSEFILE
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                }
            }
            
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                }
            }
        }

        return ret;
    }

    public static JSONObject loadCachedIndicatorSummaryJson(int horseId, String cacheDir) {
        File cachedFile = getIndicatorSummaryCacheFile(horseId, cacheDir);
        if (!cachedFile.exists()) {
            return null;
        }
        JSONObject jsonObj = null;
        BufferedReader reader = null;
        FileInputStream fis = null;
        InputStreamReader isr = null;

        try {
            fis = new FileInputStream(cachedFile);
            isr = new InputStreamReader(fis, "UTF-8");
            reader = new BufferedReader(isr);
            jsonObj = (JSONObject) JSONValue.parse(reader);
        } catch (Exception ex) {
            logger.error("Fail to load cache file: " + cachedFile.getAbsolutePath(), ex);
        } finally {
            // CLOSEFILE
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                }
            }

            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ex) {
                }
            }           

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                }
            }
        }

        return jsonObj;
    }

    public static JSONObject loadCachedDataSummaryJson(int horseId, String cacheDir) {
        File cachedFile = getDataSummaryCacheFile(horseId, cacheDir);
        if (!cachedFile.exists()) {
            return null;
        }
        JSONObject jsonObj = null;
        BufferedReader reader = null;
        FileInputStream fis = null;
        InputStreamReader isr = null;

        try {
            fis = new FileInputStream(cachedFile);
            isr = new InputStreamReader(fis, "UTF-8");
            reader = new BufferedReader(isr);
            jsonObj = (JSONObject) JSONValue.parse(reader);
        } catch (Exception ex) {
            logger.error("Fail to load cache file: " + cachedFile.getAbsolutePath(), ex);
        } finally {
            // CLOSEFILE
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                }
            }

            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ex) {
                }
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                }
            }
        }

        return jsonObj;
    }
}
