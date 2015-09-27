/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.tool;

import com.ocs.indaba.aggregation.cache.BasePersistence;
import com.ocs.indaba.aggregation.cache.ServiceDataCachePersistence;
import com.ocs.indaba.aggregation.json.DataSummaryJsonUtils;
import java.io.File;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff
 */
public class DataSummaryBuilder extends DataBuilder {

    public DataSummaryBuilder() {
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
            if (horseFiles == null) {
                continue;
            }
            for (File file : horseFiles) {
                int horseId = getHorseId(file.getName());
               
                JSONObject jsonObj = DataSummaryJsonUtils.toJson(horseId, baseDir, cacheDir, false);
                ServiceDataCachePersistence persistence = new ServiceDataCachePersistence();
                persistence.serialize(jsonObj, BasePersistence.getDataSummaryCacheFile(horseId, cacheDir));
                writeLogLn("Data summary for horse [" + horseId + "] is cached: " + BasePersistence.getDataSummaryCacheFile(horseId, cacheDir));
            }
        }
    }

    public static void main(String args[]) {
        DataSummaryBuilder builder = new DataSummaryBuilder();
        builder.build(null, null);
    }
}
