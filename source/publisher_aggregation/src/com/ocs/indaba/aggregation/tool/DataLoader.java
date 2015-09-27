/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.tool;

import com.ocs.indaba.aggregation.cache.BasePersistence;
import com.ocs.indaba.util.FileUtil;
import java.io.File;
import java.io.Writer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jiangjeff
 */
public class DataLoader {

    protected String newLine = "\n";
    protected Writer logWriter = null;
    protected String baseDir = null;
    protected String cacheDir = null;
    private SRFBuilder srfBuilder = null;
    private PIFBuilder pifBuilder = null;

    public DataLoader() {
    }
/*
    public DataLoader(String baseDir, String cacheDir) {
        this.baseDir = baseDir;
        this.cacheDir = cacheDir;
    }

    public DataLoader(String baseDir, String cacheDir, String newLine, Writer logWriter) {
        this.baseDir = baseDir;
        this.cacheDir = cacheDir;
        this.newLine = newLine;
        this.logWriter = logWriter;
    }
*/
    @Autowired
    public void setSRFBuilder(SRFBuilder srfBuilder) {
        this.srfBuilder = srfBuilder;
    }
    @Autowired
    public void setPIFBuilder(PIFBuilder pifBuilder) {
        this.pifBuilder = pifBuilder;
    }
    public Writer getLogWriter() {
        return logWriter;
    }

    public void setLogWriter(Writer logWriter) {
        this.logWriter = logWriter;
    }

    public String getNewLine() {
        return newLine;
    }

    public void setNewLine(String newLine) {
        this.newLine = newLine;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBasedir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String getCacheBaseDir() {
        return cacheDir;
    }

    public void setCacheBaseDir(String cacheDir) {
        this.cacheDir = cacheDir;
    }

    public void clearCachedWidgetData() {
        File cacheFolder = BasePersistence.getCacheBaseDir(cacheDir);
        if (cacheFolder.exists()) {
            FileUtil.delete(cacheFolder);
        }
    }

    public void clearLoadedData() {
        File dir = new File(BasePersistence.getBasePath(baseDir));
        if (dir.exists()) {
            FileUtil.delete(dir);
        }
    }

    public void loadSRF() {
        //DataBuilder srfBuilder = new SRFBuilder();
        srfBuilder.setLogWriter(logWriter);
        srfBuilder.setNewLine(newLine);
        srfBuilder.build(baseDir, cacheDir);
        srfBuilder.clean();
    }

    public void loadPIF() {
        //DataBuilder pifBuilder = new PIFBuilder();
        pifBuilder.setLogWriter(logWriter);
        pifBuilder.setNewLine(newLine);
        pifBuilder.build(baseDir, cacheDir);
        pifBuilder.clean();
    }

    public void cacheIndicatorSummary() {
        DataBuilder isBuilder = new IndicatorSummaryBuilder();
        isBuilder.setLogWriter(logWriter);
        isBuilder.setNewLine(newLine);
        isBuilder.build(baseDir, cacheDir);
        isBuilder.clean();
    }

    public void cacheDataSummary() {
        DataBuilder dataBuilder = new DataSummaryBuilder();
        dataBuilder.setLogWriter(logWriter);
        dataBuilder.setNewLine(newLine);
        dataBuilder.build(baseDir, cacheDir);
        dataBuilder.clean();
    }

    public static void main(String[] args) {
//        DataLoader dataLoader = new DataLoader();
//        dataLoader.clearLoadedData();
//        dataLoader.clearCachedWidgetData();
//        dataLoader.loadSRF();
//        dataLoader.loadPIF();
//        dataLoader.cacheIndicatorSummary();
    }
}
