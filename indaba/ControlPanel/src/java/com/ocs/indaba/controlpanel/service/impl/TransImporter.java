/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.service.impl;

import com.ocs.indaba.controlpanel.model.IndicatorTransValidation;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.controlpanel.model.TransValidation;
import java.io.File;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author seanpcheng
 */
public class TransImporter {
    private File file;
    private LoginUser user;
    
    private static final Logger logger = Logger.getLogger(TransImporter.class);
    
    public TransImporter(LoginUser user, File file) {
        this.file = file;
        this.user = user;
    }
    
    public int load() {
  
        logger.debug("Importing file " + file + " uploaded by user " + user.getUserId());
        TransValidator validator = new TransValidator(user, file);
        IndicatorTransValidation result;

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
        
        List<TransValidation> vals = result.getIndicatorValidations();

        for (TransValidation val : vals) {
            val.load();
        }

        logger.debug("Loading done. Indicators loaded: " + vals.size());
        
        return vals.size();
    }
}
