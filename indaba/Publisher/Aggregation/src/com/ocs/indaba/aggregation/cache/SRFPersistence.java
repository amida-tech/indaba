/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.cache;

import com.ocs.indaba.aggregation.json.SRFJsonUtils;
import com.ocs.indaba.aggregation.vo.ScorecardInfo;
import com.ocs.util.StringUtils;
import java.io.*;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author jiangjeff
 */
public class SRFPersistence extends BasePersistence {

    private final static Logger log = Logger.getLogger(SRFPersistence.class);
    private String baseDir = null;

    public SRFPersistence(String baseDir) {
        this.baseDir = baseDir;
    }

    public void serializeSRF(ScorecardInfo tree) {
        String filename = getSRFFilepath(tree.getProductId(), tree.getHorseId());
        File file = new File(StringUtils.isEmpty(baseDir) ? getBasePath() : baseDir, filename);
        if (!file.getParentFile().exists()) {
            log.info("Make directory: " + file.getParentFile().getAbsolutePath());
            boolean ret = file.getParentFile().mkdirs();
            if (!ret) {
                log.error("Fail to make directory: " + file.getParentFile().getAbsolutePath());
                return;
            }
        }

        JSONObject jsonObj = SRFJsonUtils.scorecard2Json(tree);
        serialize(jsonObj, file);
    }

    public ScorecardInfo deserializeSRF(File file) {
        ScorecardInfo tree = null;
        if(file == null || !file.exists()) {
            return null;
        }
        
        log.debug("Load JSON file: " + file.getAbsolutePath());
        BufferedReader reader = null;
        FileInputStream fis = null;
        InputStreamReader isr = null;

        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis, "UTF-8");
            reader = new BufferedReader(isr);
            JSONObject jsonObj = (JSONObject) JSONValue.parse(reader);
            tree = SRFJsonUtils.json2Scorecard(jsonObj);
        } catch (IOException ioe) {
            log.error("IO Exception.", ioe);
        } catch (Exception ex) {
            log.error("Exception occurs.", ex);
        } finally {
            // CLOSEFILE
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }

            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                }
            }
           
             if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return tree;
    }

    public ScorecardInfo deserializeSRF(String filename) {
        return deserializeSRF(new File(filename));
    }
}
