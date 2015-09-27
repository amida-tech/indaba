/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jiangjeff
 */
public class FileUtils {

    private static final int BUF_SIZE = 4096;

    public static String readFile(InputStream inStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inStream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return sb.toString();
    }

    private static String getDirNameByFileId(long fileId) {
        return (fileId / (1000 * 1000)) + "/" + (fileId / 1000);
    }

    /**
     * get file name of relative path
     * @param ext
     * @param fileId
     * @return
     */
    public static String getFileNameByFileId(String ext, long fileId) {
        if (ext == null) {
            return getDirNameByFileId(fileId) + "/" + fileId;
        } else {
            return getDirNameByFileId(fileId) + "/" + fileId + "." + ext;
        }

    }

    /**
     * store file
     * @param fileName
     * @param dir
     */
    public static void storeFile(String fileName, InputStream is) {
        BufferedOutputStream dest = null;
        byte data[] = new byte[4916];
        try {
            File entryFile = new File(fileName);
            int count;

            FileOutputStream fos = new FileOutputStream(entryFile);
            dest = new BufferedOutputStream(fos, 4916);
            while ((count = is.read(data, 0, 4916)) != -1) {
                dest.write(data, 0, count);
            }

            dest.flush();
            dest.close();
            fos.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public static String getFileExtention(String filename) {
        if (filename == null) {
            return "";
        }
        int pos = filename.lastIndexOf('.');
        return ((pos >= 0) ? filename.substring(pos + 1) : "");
    }

    public static String getFilename(String filename) {
        if (filename == null) {
            return "";
        }
        int pos = filename.lastIndexOf('.');
        return ((pos >= 0) ? filename.substring(0, pos) : filename);
    }

    /**
     * Read inputstream and write to outputstream (the inputstream/outputstream
     * will be closed after completed.)
     * 
     * @param out
     * @param in
     * @throws IOException
     */
    public static void loadStream(OutputStream out, InputStream in)
            throws IOException {
        loadStream(out, in, true, true);
    }

    /**
     * Read inputstream and write to outputstream
     * 
     * @param out
     * @param in
     * @param closeIn
     *            - close inputstream after completed
     * @param closeOut
     *            - close outputstream after completed
     * @throws IOException
     */
    public static void loadStream(OutputStream out, InputStream in,
            boolean closeOut, boolean closeIn) throws IOException {
        if (in == null || out == null) {
            throw new NullPointerException();
        }
        byte[] buf = new byte[BUF_SIZE];
        try {
            for (int n = 0; (n = in.read(buf, 0, BUF_SIZE)) != -1; out.write(
                            buf, 0, n)) {
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

    public static void mergeFiles(HttpServletResponse response, String path,
            String fileNames) throws IOException {
        PrintWriter out = response.getWriter();
        String declare = "";
        String statements = "";
        StringTokenizer st = new StringTokenizer(fileNames, ",");
        while (st.hasMoreTokens()) {
            BufferedReader reader = new BufferedReader(new FileReader(path
                    + "/" + st.nextToken()));
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
                    if (line.startsWith("rule ")) {
                        statements += "\n";
                    }
                    statements += line + "\n";
                }
            }
            reader.close();
        }
        out.write(declare);
        out.write(statements + "\n");
        out.flush();
        out.close();
    }

    public static void saveFile(String fileName, String path, String content)
            throws IOException {
        FileWriter out = new FileWriter(path + "/" + fileName);
        out.write(content);
        out.close();
    }

    public static void deleteFile(String fileName, String path)
            throws IOException {
        File file = new File(path + "/" + fileName);
        file.delete();
    }
}
