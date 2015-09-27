/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.controlpanel.tableprocessor;

import com.ocs.indaba.controlpanel.model.IndicatorValidation;
import com.ocs.indaba.survey.table.Block;

/**
 *
 * @author yc06x
 */
public class AugmentedBlock extends Block {

    
    private IndicatorValidation indicator;

    public AugmentedBlock(short blockType, short dataType, int row, int col) {
        super(blockType, dataType, row, col);
        this.indicator = null;        
    }

    public void setIndicator(IndicatorValidation value) {
        this.indicator = value;
    }

    public IndicatorValidation getIndicator() {
        return this.indicator;
    }

}
