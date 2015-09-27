/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.util;

/**
 *
 * @author jiangjeff
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.log4j.Logger;

/**
 * File utilities
 */
public final class FileUtil {

    private static Logger logger = Logger.getLogger(FileUtil.class);
    private static final int BUF_SIZE = 1024;

    public static boolean copy(String src, String dest) {
        InputStream in = null;
        OutputStream out = null;
        boolean ret = false;
        logger.debug("Copy file " + src + " ==> " + dest);
        try {
            File srcFile = new File(src);
            if (!(srcFile.exists())) {
                logger.error("File isn't existed: " + src);
                return false;
            }
            File destFile = new File(dest);
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] bytes = new byte[BUF_SIZE];
            int c;
            while ((c = in.read(bytes)) != -1) {
                out.write(bytes, 0, c);
            }
            ret = true; // if success then return true
        } catch (Exception ex) {
            ret = false; //if fail then return false
            logger.error("Fail to copy file.", ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                }
            }
        }
        return ret;
    }

    public static void delete(String filename) {
        delete(new File(filename));
    }

    public static void delete(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                delete(file);
            }
        }
        dir.delete();
    }

    public static boolean rename(String src, String dest) {
        File srcFile = new File(src);
        File destFile = new File(dest);

        if(!srcFile.exists()) {
            logger.error("Source file doesn't existed: " + src);
            return false;
        }
        if(destFile.exists()) {
            logger.error("Dest file has existed: " + dest);
            return false;
        }

        return srcFile.renameTo(destFile);
    }
    
    public static void main(String[] args) throws IOException {
        String src = "/data/k12_sid/packs/Land+Animals/work/Land+Animals.pack/Content/Resources/images/Art/";
        String dest = "/data/k12_sid/packs/Land+Animals/work/Land+Animals.pack/Content/Resources/images/";
        delete(new File("C:\\data\\indaba\\aggregation\\export\\20111019155655532"));
    }
}
