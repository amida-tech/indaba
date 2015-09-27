/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.service.impl;

import com.ocs.indaba.controlpanel.model.IndicatorImportValidation;
import com.ocs.indaba.controlpanel.model.IndicatorValidation;
import com.ocs.indaba.controlpanel.model.LoginUser;
import java.io.File;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author seanpcheng
 */
public class IndicatorImporter {
    
    private File file;
    private LoginUser user;
    
    private static final Logger logger = Logger.getLogger(IndicatorImporter.class);

    
    public IndicatorImporter(LoginUser user, File file) {
        this.file = file;
        this.user = user;
    }
    
    public int load() {
  
        logger.debug("Importing file " + file + " uploaded by user " + user.getUserId());
        IndicatorImportValidator validator = new IndicatorImportValidator(user, file);
        IndicatorImportValidation result;

        try {
            result = validator.validate();
        } catch (Exception e) {
            return 0;
        }
        logger.debug("Finished validating CSV. ErrorCount=" + result.getErrorCount());

        if (result.getErrorCount() != 0) {
            return 0;
        }

        logger.debug("Now loading indicators...");
        
        List<IndicatorValidation> vals = result.getIndicatorValidations();

        for (IndicatorValidation val : vals) {
            val.load();
        }

        logger.debug("Loading done. Indicators loaded: " + vals.size());
        
        return vals.size();
    }
}
