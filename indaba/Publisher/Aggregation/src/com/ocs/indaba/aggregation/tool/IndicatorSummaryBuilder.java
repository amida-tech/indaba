/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.tool;

import com.ocs.indaba.aggregation.cache.BasePersistence;
import com.ocs.indaba.aggregation.cache.ServiceDataCachePersistence;
import com.ocs.indaba.aggregation.json.IndicatorSummaryJsonUtils;
import java.io.File;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff
 */
public class IndicatorSummaryBuilder extends DataBuilder {

    public IndicatorSummaryBuilder() {
        log = Logger.getLogger(IndicatorSummaryBuilder.class);
    }

    @Override
    public void clean() {
    }

    @Override
    public void build(String baseDir, String cacheDir) {
        File[] prodDirs = BasePersistence.getAllProductDirs(baseDir);
        if (prodDirs == null) {
            return;
        }

        for (File dir : prodDirs) {
            File[] horseFiles = getAllHorseFiles(dir);
            for (File file : horseFiles) {
                int horseId = getHorseId(file.getName());
                if (horseId <= 0) {
                    log.warn("Invalid SRF filename: " + file.getAbsolutePath());
                    continue;
                }
                JSONObject jsonObj = IndicatorSummaryJsonUtils.toJson(horseId, baseDir, cacheDir, false);
                ServiceDataCachePersistence persistence = new ServiceDataCachePersistence();
                persistence.serialize(jsonObj, BasePersistence.getIndicatorSummaryCacheFile(horseId, cacheDir));
                writeLogLn("Indicator summary for horse [" + horseId + "] is cached: " + BasePersistence.getIndicatorSummaryCacheFile(horseId, cacheDir));
            }
        }
    }

    public static void main(String args[]) {
        IndicatorSummaryBuilder builder = new IndicatorSummaryBuilder();
        builder.build(null, null);
    }
}
