/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.po.Product;

/**
 *
 * @author Jeff Jiang
 */
public class BaseRWIExportAction extends BaseExportAction {

    private static final String RWI_PRODUCT_NAME = "RWI Index Questionnaire";

    protected boolean isRWIProduct(int horseId) {
        Product prod = productDao.selectProductByHorseId(horseId);
        return (prod != null && RWI_PRODUCT_NAME.equals(prod.getName())) ? true : false;
    }
}
