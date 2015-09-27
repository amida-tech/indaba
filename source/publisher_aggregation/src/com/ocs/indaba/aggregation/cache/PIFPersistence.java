/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.cache;

import com.ocs.indaba.aggregation.vo.ProductInfo;
import com.ocs.indaba.aggregation.json.PIFJsonUtils;
import com.ocs.util.StringUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author jiangjeff
 */
public class PIFPersistence extends BasePersistence {

    private final static Logger log = Logger.getLogger(PIFPersistence.class);
    private String baseDir = null;

    public PIFPersistence(String baseDir) {
        this.baseDir = baseDir;
    }

    public void serializePIF(ProductInfo prod) {
        String filename = getPIFFilepath(prod.getProductId());
        File file = new File((StringUtils.isEmpty(baseDir) ? getBasePath() : baseDir), filename);
        if (!file.getParentFile().exists()) {
            log.error("Make directory: " + file.getParentFile().getAbsolutePath());
            boolean ret = file.getParentFile().mkdirs();
            if (!ret) {
                log.error("Fail to make directory: " + file.getParentFile().getAbsolutePath());
                return;
            }
        }

        JSONObject jsonObj = PIFJsonUtils.product2Json(prod);
        serialize(jsonObj, file);
    }

    public ProductInfo deserializePIF(File file) {
        ProductInfo tree = null;
        log.info("Load JSON file: " + file.getAbsolutePath());
        BufferedReader reader = null;
        InputStreamReader r1 = null;
        FileInputStream r2 = null;
        try {
            r2 = new FileInputStream(file);
            r1 = new InputStreamReader(r2, "UTF-8");
            reader = new BufferedReader(r1);
            JSONObject jsonObj = (JSONObject) JSONValue.parse(reader);
            tree = PIFJsonUtils.json2Product(jsonObj);
        } catch (IOException ioe) {
            log.error("IO Exception.", ioe);
        } catch (Exception ex) {
            log.error("Exception occurs.", ex);
        } finally {
            // CLOSEFILE
            if (r2 != null) {
                try {
                    r2.close();
                } catch (IOException e) {
                }
            }

            if (r1 != null) {
                try {
                    r1.close();
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

    public ProductInfo deserializePIF(String filename) {
        return deserializePIF(new File(filename));
    }
}
