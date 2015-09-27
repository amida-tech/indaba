/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.controlpanel.tableprocessor;

import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.model.IndicatorImportValidation;
import com.ocs.indaba.controlpanel.model.IndicatorValidation;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.controlpanel.service.impl.IndicatorImportValidator;
import com.ocs.indaba.survey.table.Block;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author yc06x
 */
public class TableLoader {

    private TableParser parser;
    private IndicatorImportValidator indicatorValidator;
    private LoginUser user;
    IndicatorImportValidation indicatorVal;

    private String mainIndicatorName;
    private int mainIndicatorId;
    
    private List<String> errors = null;

    public TableLoader(LoginUser user, String fileName) {
        this.user = user;
        parser = new TableParser(user, fileName);
        indicatorValidator = new IndicatorImportValidator(user, fileName);
        indicatorValidator.setComponentMode(true);
    }

    public void validate() throws FileNotFoundException, IOException {
        indicatorVal = indicatorValidator.validate();
        if (indicatorVal.getErrorCount() > 0) return;

        parser.parse();        
        if (parser.getErrorCount() > 0) return;

        List<IndicatorValidation> indicators = indicatorVal.getIndicatorValidations();

        Map<String, IndicatorValidation> nameMap = new HashMap<String, IndicatorValidation>();
        Map<String, IndicatorValidation> useMap = new HashMap<String, IndicatorValidation>();

        for (IndicatorValidation ind : indicators) {
            nameMap.put(ind.getName(), ind);
        }

        List<List<Block>> blocks = parser.getBlocks();
        for (List<Block> rowBlocks : blocks) {
            for (Block b : rowBlocks) {
                AugmentedBlock block = (AugmentedBlock)b;
                if (block.getDataType() == Block.BLOCK_DATA_TYPE_QUESTION) {
                    IndicatorValidation ind = nameMap.get(block.getData());
                    if (ind == null) {
                        // referenced indicator doesn't exit
                        addError(user.message(ControlPanelMessages.TABLE_LOADER__BAD_INDICATOR_REF, block.getData()));
                    } else {
                        useMap.put(ind.getName(), ind);
                    }
                    block.setIndicator(ind);
                }
            }
        }

        // see whethere there is any unused indicators
        for (IndicatorValidation ind : indicators) {
            if (useMap.get(ind.getName()) == null) {
                addError(user.message(ControlPanelMessages.TABLE_LOADER__UNUSED_INDICATOR, ind.getName()));
            }
        }
        
    }

    private void addError(String error) {
        if (errors == null) {
            errors = new ArrayList<String>();
        }
        errors.add(error);
    }


    public List<String> getErrors() {
        if (indicatorVal.getErrorCount() > 0) {
            List<String> errs = new ArrayList<String>();

            if (indicatorVal.getGeneralErrors() != null) {
                errs.addAll(indicatorVal.getGeneralErrors());
            }
            
            List<IndicatorValidation> indicators = indicatorVal.getIndicatorValidations();
            if (indicators != null && !indicators.isEmpty()) {
                for (IndicatorValidation ind : indicators) {
                    if (ind.getErrorMsg() != null) {
                        errs.add(ind.getErrorMsg());
                    }
                }
            }
            
            return errs;
        }

        if (parser.getErrorCount() > 0) return parser.getErrors();
        return errors;
    }
    

    public void setMainIndicatorInfo(String name, int id, int orgId, int visibility, int langId, int state) {
        this.mainIndicatorId = id;
        this.mainIndicatorName = name;
        indicatorVal.setDefaultLang(langId);
        indicatorVal.setDefaultOrgID(orgId);
        indicatorVal.setDefaultState(state);
        indicatorVal.setDefaultVisibility(visibility);
    }


    // Before calling the load method, you must already validated with no errors.
    public void load() {
        List<List<Block>> blocks = parser.getBlocks();
        for (List<Block> rowBlocks : blocks) {
            for (Block b : rowBlocks) {
                AugmentedBlock block = (AugmentedBlock)b;
                if (block.getDataType() == Block.BLOCK_DATA_TYPE_QUESTION) {
                    IndicatorValidation ind = block.getIndicator();

                    // compute the name of the component indicator
                    String compName = mainIndicatorName + "::" + ind.getName() + ":" + block.getRowNumber() + "." + block.getColNumber();
                    ind.setParentIndicatorId(mainIndicatorId);
                    block.setIndicatorId(ind.load(compName));
                }
            }
        }
    }

    public List<List<Block>> getBlocks() {
        return parser.getBlocks();
    }

    public void close() {
        if (indicatorValidator != null) indicatorValidator.close();
        if (parser != null) parser.close();
    }
    
}
