/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.util;

import com.ocs.indaba.action.NoteBookAction;
import com.ocs.common.Config;
import com.ocs.indaba.common.Constants;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.text.MessageFormat;
import java.util.Date;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author Jeff
 */
public class FileStorageUtil {

    private static final Logger logger = Logger.getLogger(NoteBookAction.class);
    private static final int BUF_SIZE = 4096;

    public static String store(FormFile formFile, long userId) throws IOException {
        return store(formFile, Config.getString(Config.KEY_STORAGE_UPLOAD_BASE), userId);
    }

    public static String store(FormFile formFile, String basePath, long userId) throws IOException {
        String filePath = MessageFormat.format(Config.getString(Config.KEY_STORAGE_PATH_FORMAT), basePath, userId,
                DateUtils.date2Str(new Date(), Config.getString(Config.KEY_STORAGE_DATE_FORMAT, DateUtils.DEFAULT_DATE_SIMPLE_FORMAT)), formFile.getFileName());
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        BufferedOutputStream bufOut = new BufferedOutputStream(new FileOutputStream(file));
        InputStream in = formFile.getInputStream();
        byte[] buf = new byte[BUF_SIZE];
        int n = -1;
        while ((n = in.read(buf)) != -1) {
            bufOut.write(buf, 0, n);
        }
        bufOut.flush();
        bufOut.close();
        in.close();
        return filePath;
    }

    public static String store(InputStream in, String basePath, long userId, String imgName) throws IOException {
        String filePath = MessageFormat.format(Config.getString(Config.KEY_STORAGE_PATH_FORMAT),
                basePath, userId,
                DateUtils.date2Str(new Date(), Config.getString(Config.KEY_STORAGE_DATE_FORMAT, DateUtils.DEFAULT_DATE_SIMPLE_FORMAT)),
                imgName);

        File file = new File(filePath);
        file.getParentFile().mkdirs();
        BufferedOutputStream bufOut = new BufferedOutputStream(new FileOutputStream(file));
        byte[] block = new byte[BUF_SIZE];
        int n = -1;
        while ((n = in.read(block)) != -1) {
            bufOut.write(block, 0, n);
        }
        bufOut.flush();
        bufOut.close();
        in.close();
        return filePath;
    }

    public static String store(FormFile formFile, String filePath) throws IOException {
        return store(formFile.getInputStream(), filePath);
    }

    public static String store(InputStream in, String filePath) throws IOException {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        BufferedOutputStream bufOut = new BufferedOutputStream(new FileOutputStream(file));
        byte[] block = new byte[BUF_SIZE];
        int n = -1;
        while ((n = in.read(block)) != -1) {
            bufOut.write(block, 0, n);
        }
        bufOut.flush();
        bufOut.close();
        in.close();
        return filePath;
    }

    public static String getSuffix(String fileName) {
        if (fileName == null) {
            return Constants.DEFAULT_FILE_TYPE;
        }

        int p = fileName.lastIndexOf('.');
        if (p < 0) {
            return Constants.DEFAULT_FILE_TYPE;
        } else {
            return fileName.substring(p + 1);
        }
    }
 /**
     * Read inputstream and write to outputstream (the inputstream/outputstream will be closed after completed.)
     * @param out
     * @param in
     * @throws IOException
     */
    public static void loadStream(OutputStream out, InputStream in) throws IOException {
        loadStream(out, in, true, true);
    }
    
    /**
     * Read inputstream and write to outputstream
     * @param out
     * @param in
     * @param closeIn - close inputstream after completed
     * @param closeOut - close outputstream after completed
     * @throws IOException
     */
    public static void loadStream(OutputStream out, InputStream in, boolean closeOut, boolean closeIn) throws IOException {
        if (in == null || out == null) {
            throw new NullPointerException();
        }
        byte[] buf = new byte[BUF_SIZE];
        try {
            for (int n = 0; (n = in.read(buf, 0, BUF_SIZE)) != -1; out.write(buf, 0, n)) {
            }
            out.flush();
        } finally {
            try {
                if (closeIn) {
                    in.close();
                }
            } catch (Exception ex) {
            }

            try {
                out.flush();
                if (closeOut) {
                    out.close();
                }
            } catch (Exception ex) {
            }
        }
    }

    public static void mergeFiles(HttpServletResponse response, String path, String fileNames) {
        try {
            PrintWriter out = response.getWriter();
            String declare = "";
            String statements = "";
            StringTokenizer st = new StringTokenizer(fileNames, ",");
            while (st.hasMoreTokens()) {
                BufferedReader reader = new BufferedReader(new FileReader(path + "/" + st.nextToken()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("package")) {
                        if (declare.indexOf(line) == -1) {
                            declare += line + "\n\n";
                        }
                    } else if (line.startsWith("import")) {
                        if (declare.indexOf(line) == -1) {
                            declare += line + "\n";
                        }
                    } else if (line.length() > 0) {
                        if (line.startsWith("rule "))
                            statements += "\n";
                        statements += line + "\n";
                    }
                }
                reader.close();
            }
            out.write(declare);
            out.write(statements + "\n");
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.debug(e);
        }
    }

    public static void saveFile(String fileName, String path, String content)
    {
        try {
            FileWriter out = new FileWriter(path + "/" + fileName);
            out.write(content);
            out.close();
        } catch (Exception e) {
            logger.debug(e);
        }
    }

    public static void deleteFile(String fileName, String path)
    {
        try {
            File file = new File(path + "/" + fileName);
            file.delete();
        } catch (Exception e) {
            logger.debug(e);
        }
    }
}
