/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.rwi.action;

import com.ocs.indaba.aggregation.action.BaseExportAction;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.po.Product;

/**
 *
 * @author Jeff Jiang
 */
public class BaseRWIExportAction extends BaseExportAction {

    protected boolean isRWIProduct(int horseId) {
        Product prod = productDao.selectProductByHorseId(horseId);
        return (prod != null && Constants.RWI_PRODUCT_NAME.equals(prod.getName())) ? true : false;
    }
}
