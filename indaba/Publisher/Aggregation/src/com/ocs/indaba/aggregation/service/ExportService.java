/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeanbone
 */
public abstract class ExportService {

    protected Logger logger = Logger.getLogger(ExportService.class);
    protected boolean initialized = false;
    protected ArrayList<String> targetFileNames = null;

    protected String EXPORT_BASE_PATH = null;
    protected String EXPORT_TEMP_FILE_FOLDER = null;
    protected String EXPORT_TEMP_ZIP_FOLDER = null;
    protected String EXPORT_ZIP_NAME = null;
    protected String EXPORT_SUMMARY_NAME = null;

    protected final int BUFFER = 2048;
    protected int version = 1;
    protected SimpleDateFormat df = null;
    protected HttpSession session = null;
    protected static final String COLUMN_SEPARATOR = ",";
    protected static final String OBJECT_SEPARATOR = "|";
    protected static final String PART_SEPARATOR = ":";
    protected int uid;
    protected int tempUid;

    protected abstract void initExportPath();

    protected void createExportFolder() {
        if (session.getAttribute("uid") != null) {
            uid = Integer.parseInt(session.getAttribute("uid").toString());
        } else {
            uid = -1;
            tempUid = Integer.parseInt(session.getAttribute("temp_uid").toString());
        }

        EXPORT_TEMP_FILE_FOLDER += "_" + (uid < 0 ? tempUid : uid);
        EXPORT_TEMP_ZIP_FOLDER += "_" + (uid < 0 ? tempUid : uid);
        try {
            File exportFile = new File(EXPORT_BASE_PATH, EXPORT_TEMP_FILE_FOLDER);
            if (!exportFile.exists()) {
                exportFile.mkdirs();
            } else {
                File files[] = exportFile.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }

            File zipFile = new File(EXPORT_BASE_PATH, EXPORT_TEMP_ZIP_FOLDER);
            if (!zipFile.exists()) {
                zipFile.mkdirs();
            } else {
                File files[] = zipFile.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
            logger.info("Create tempporary file forder for Export:" + EXPORT_BASE_PATH + EXPORT_TEMP_FILE_FOLDER);
        } catch (Exception e) {
            logger.error("meet error when create temp folder" + e.getLocalizedMessage());
        }
    }

    protected abstract void createSummaryCSV();

    protected abstract void executeExport();

    protected String createZIPFile(String inputPath, String outputPath) {
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(outputPath + EXPORT_ZIP_NAME);
            BufferedOutputStream bos = new BufferedOutputStream(dest);
            ZipOutputStream out = new ZipOutputStream(bos);
            byte data[] = new byte[BUFFER];
            File f = new File(inputPath);
            File files[] = f.listFiles();

            for (int i = 0; i < files.length; i++) {
                FileInputStream fi = new FileInputStream(files[i]);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(new String(files[i].getName().getBytes(), "UTF-8"));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();

                // CLOSEFILE: Don't we need to close fi?
                fi.close();
            }
            out.close();

            // CLOSEFILE
            bos.close();
            dest.close();

            File exportFile = new File(EXPORT_BASE_PATH, EXPORT_TEMP_FILE_FOLDER);
            files = exportFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
            exportFile.delete();

            logger.info("Create ZIP file successful!");
            return outputPath + EXPORT_ZIP_NAME;
        } catch (Exception e) {
            logger.error("Meet error when creating ZIP " + e.getLocalizedMessage(), e);
        }
        return null;
    }
}
