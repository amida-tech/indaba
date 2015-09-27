/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.cache;

import com.ocs.common.Config;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.util.StringUtils;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.MessageFormat;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author jiangjeff
 */
public abstract class BasePersistence {

    private static Logger log = Logger.getLogger(BasePersistence.class);
    protected static final int BUF_SIZE = 1024;

    public static String getBasePath() {
        return getBasePath(null);
    }

    public static String getBasePath(String baseDir) {
        return (StringUtils.isEmpty(baseDir) ? Config.getString(Constants.KEY_STORAGE_BASE_PATH) : baseDir);
    }

    public static String getCacheBasePath() {
        return getCacheBasePath(null);
    }

    public static String getCacheBasePath(String cacheDir) {
        return StringUtils.isEmpty(cacheDir) ? Config.getString(Constants.KEY_CACHE_BASE_PATH) : cacheDir;
    }

    public void serialize(JSONObject jsonObj, File file) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        OutputStreamWriter writer = null;
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            writer = new OutputStreamWriter(fos, "UTF-8");
            jsonObj.writeJSONString(writer);
        } catch (Exception ex) {
            log.error("IO Error.", ex);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.warn("Fail to close Output Stream Writer: " + file.getAbsolutePath(), e);
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.warn("Fail to close File Output Stream: " + file.getAbsolutePath(), e);
                }
            }
        }
    }

    public static String getSRFFilepath(int productId, int horseId) {
        String prodDirpath = MessageFormat.format(Config.getString(Constants.KEY_PROD_DIRPATH_PATTERN), productId);
        String srfFilename = MessageFormat.format(Config.getString(Constants.KEY_SRF_FILENAME_PATTERN), horseId);

        return MessageFormat.format(Config.getString(Constants.KEY_SCORECARD_FILEPATH_PATTERN), prodDirpath, srfFilename);
    }

    public static String getPIFFilepath(int productId) {
        String productFilename = MessageFormat.format(Config.getString(Constants.KEY_PROD_DIRPATH_PATTERN), productId);

        return MessageFormat.format(Config.getString(Constants.KEY_SCORECARD_FILEPATH_PATTERN), productFilename,
                Config.getString(Constants.KEY_PIF_FILENAME));
    }

    public static File[] getAllProductDirs() {
        return getAllProductDirs(null);
    }

    public static File[] getAllProductDirs(String baseDir) {
        File baseFile = StringUtils.isEmpty(baseDir) ? new File(Config.getString(Constants.KEY_STORAGE_BASE_PATH)) : new File(baseDir);
        return baseFile.listFiles(new FileFilter() {

            public boolean accept(File file) {
                return file.isDirectory() && file.getName().startsWith(Constants.PRODUCT_FILENAME_PREFIX);
            }
        });
    }

    public static File[] getSRFFiles(int productId) {
        return getSRFFiles(productId, null);
    }

    public static File[] getSRFFiles(int productId, String baseDir) {
        File baseFile = StringUtils.isEmpty(baseDir) ? new File(Config.getString(Constants.KEY_STORAGE_BASE_PATH)) : new File(baseDir);
        final String prodDirpath = MessageFormat.format(Config.getString(Constants.KEY_PROD_DIRPATH_PATTERN), productId);
        File[] dirs = baseFile.listFiles(new FileFilter() {

            public boolean accept(File file) {
                return (file.isDirectory() && prodDirpath.equals(file.getName()));
            }
        });
        if (dirs == null || dirs.length == 0) {
            return null;
        }
        return dirs[0].listFiles(new FileFilter() {

            public boolean accept(File file) {
                return file.getName().startsWith(Constants.HORSE_FILENAME_PREFIX);
            }
        });
    }

    public static File getSRFFile(int horseId) {
        return getSRFFile(horseId, null);
    }

    public static File getSRFFile(int horseId, String baseDir) {
        File[] prodDirs = getAllProductDirs(baseDir);
        if (prodDirs == null) {
            return null;
        }
        final String srfFilename = MessageFormat.format(Config.getString(Constants.KEY_SRF_FILENAME_PATTERN), horseId);
        for (File dir : prodDirs) {
            File[] files = dir.listFiles(new FileFilter() {

                public boolean accept(File file) {
                    return srfFilename.equals(file.getName());
                }
            });
            if (files != null && files.length > 0) {
                return files[0];
            }
        }
        return null;
    }

    public static File getPIFFile(int productId) {
        return getPIFFile(productId, null);
    }

    public static File getPIFFile(int productId, String baseDir) {
        return new File(StringUtils.isEmpty(baseDir) ? Config.getString(Constants.KEY_STORAGE_BASE_PATH) : baseDir, getPIFFilepath(productId));
    }

    public static File getCacheBaseDir() {
        return getCacheBaseDir(null);
    }

    public static File getCacheBaseDir(String cacheDir) {
        return new File(StringUtils.isEmpty(cacheDir) ? Config.getString(Constants.KEY_CACHE_BASE_PATH) : cacheDir);
    }

    public static File getIndicatorSummaryCacheFile(int horseId) {
        return getIndicatorSummaryCacheFile(horseId, null);
    }

    public static File getIndicatorSummaryCacheFile(int horseId, String cacheDir) {
        return new File(StringUtils.isEmpty(cacheDir) ? Config.getString(Constants.KEY_CACHE_BASE_PATH) : cacheDir, MessageFormat.format(Config.getString(Constants.KEY_INDICATOR_SUMMARY_FILENAME_PATTERN), horseId));
    }

    public static File getDataSummaryCacheFile(int horseId) {
        return getDataSummaryCacheFile(horseId, null);
    }

    public static File getDataSummaryCacheFile(int horseId, String cacheDir) {
        //log.debug("======== cacheBasePath:" + cacheBasePath);
        return new File(StringUtils.isEmpty(cacheDir) ? Config.getString(Constants.KEY_CACHE_BASE_PATH) : cacheDir, MessageFormat.format(Config.getString(Constants.KEY_DATA_SUMMARY_FILENAME_PATTERN), horseId));
    }
}
