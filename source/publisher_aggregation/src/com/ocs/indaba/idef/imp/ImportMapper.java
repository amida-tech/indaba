/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.imp;

import com.ocs.indaba.idef.common.Constants;
import com.ocs.indaba.idef.reader.IndicatorNameRuleReader;
import com.ocs.indaba.idef.reader.IndicatorRefMappingReader;
import com.ocs.indaba.idef.reader.MetaPropertyReader;
import com.ocs.indaba.idef.reader.ProductNameMappingReader;
import com.ocs.indaba.idef.reader.ProjectNameMappingReader;
import com.ocs.indaba.idef.reader.QuestionNameRuleReader;

import java.io.File;

/**
 *
 * @author yc06x
 */
public class ImportMapper {

    private String rootDir;
    private ProcessContext ctx;
    private int numMappings = 0;

    public ImportMapper(String rootDir, ProcessContext ctx) {
        this.rootDir = rootDir;
        this.ctx = ctx;
    }

    public void getMappings() {
        File dir = new File(rootDir);

        if (dir == null) {
            ctx.addError("Bad root directory: " + rootDir);
            return;
        }

        File dirList[] = dir.listFiles();

        if (dirList == null) {
            ctx.addError("Empty root directory: " + rootDir);
            return;
        }

        File projectMappingFile = null;
        File productMappingFile = null;
        File indicatorNameRuleFile = null;
        File indicatorRefMappingFile = null;
        File questionNameRuleFile = null;
        File metaPropFile = null;

        for (File file : dirList) {
            String fileName = file.getName();

            if (fileName.equalsIgnoreCase((Constants.FILE_NAME_PROJECT_MAPPING))) {
                projectMappingFile = file;
            } else if (fileName.equalsIgnoreCase((Constants.FILE_NAME_PRODUCT_MAPPING))) {
                productMappingFile = file;
            } else if (fileName.equalsIgnoreCase((Constants.FILE_NAME_INDICATOR_MAME_RULE))) {
                indicatorNameRuleFile = file;
            } else if (fileName.equalsIgnoreCase((Constants.FILE_NAME_QUESTION_MAME_RULE))) {
                questionNameRuleFile = file;
            } else if (fileName.equalsIgnoreCase((Constants.FILE_NAME_INDICATOR_REF_MAPPING))) {
                indicatorRefMappingFile = file;
            } else if (fileName.equalsIgnoreCase((Constants.FILE_NAME_META))) {
                metaPropFile = file;
            }
        }

        // process project name mapping
        if (projectMappingFile != null) {
            ProjectNameMappingReader reader = new ProjectNameMappingReader(ctx, projectMappingFile);
            reader.read();
            numMappings++;
        }

        if (productMappingFile != null) {
            ProductNameMappingReader reader = new ProductNameMappingReader(ctx, productMappingFile);
            reader.read();
            numMappings++;
        }

        if (indicatorNameRuleFile != null) {
            IndicatorNameRuleReader reader = new IndicatorNameRuleReader(ctx, indicatorNameRuleFile);
            reader.read();
            numMappings++;
        }

        if (questionNameRuleFile != null) {
            QuestionNameRuleReader reader = new QuestionNameRuleReader(ctx, questionNameRuleFile);
            reader.read();
            numMappings++;
        }

        if (indicatorRefMappingFile != null) {
            IndicatorRefMappingReader reader = new IndicatorRefMappingReader(ctx, indicatorRefMappingFile);
            reader.read();
            numMappings++;
        }

         if (metaPropFile != null) {
            MetaPropertyReader reader = new MetaPropertyReader(ctx, metaPropFile);
            reader.read();
        }
    }


    public int getMappingCount() {
        return numMappings;
    }

}
