/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.tool;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.util.StringUtils;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Writer;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff
 */
public abstract class DataBuilder {

    protected Logger log = null;
    //protected static final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    
    protected String newLine = "\n";
    protected Writer logWriter;

    public String getNewLine() {
        return newLine;
    }

    public void setNewLine(String newLine) {
        this.newLine = newLine;
    }

    public Writer getLogWriter() {
        return logWriter;
    }

    public void setLogWriter(Writer logWriter) {
        this.logWriter = logWriter;
    }

    protected void writeLog(String msg) {
        if (logWriter == null) {
            return;
        }
        try {
            logWriter.write(msg);
            logWriter.flush();
        } catch (IOException ex) {
        } finally {
        }
    }

    protected void writeLogLn(String msg) {
        if (logWriter != null) {
            try {
                logWriter.write(msg);
                logWriter.write(newLine);
                logWriter.flush();
            } catch (IOException ex) {
            } finally {
            }
        }
        if(log != null) {
            log.info(msg);
        }
    }

    protected File[] getAllHorseFiles(File prodFile) {
        return prodFile.listFiles(new FileFilter() {

            public boolean accept(File file) {
                return file.isFile() && file.getName().startsWith(Constants.HORSE_FILENAME_PREFIX);
            }
        });
    }

    protected int getProductId(String filename) {
        return StringUtils.str2int(filename.replaceAll(Constants.PRODUCT_FILENAME_PREFIX, ""), -1);
    }
    
    protected int getHorseId(String filename) {
        String s = filename.replaceAll(Constants.HORSE_FILENAME_PREFIX, "").replaceAll(",", "").replaceAll(" ", "");
        int index = s.indexOf('.');
        if(index != -1) {
            s = s.substring(0, index);
        }
        return StringUtils.str2int(s, -1);
    }
    
    public abstract void build(String baseDir, String cacheDir);

    public abstract void clean();
}
