/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.loader;

import com.ocs.indaba.idef.imp.ProcessContext;
import com.ocs.indaba.idef.xo.Product;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class ProductLoader extends Loader { 

    public ProductLoader(ProcessContext ctx) {
        super.createLoader(ctx);
    }

    public void load() {
        List<Product> list = ctx.getProducts();

        if (list == null || list.isEmpty()) {
            ctx.addError("No products to load!");
            return;
        }

        for (Product product : list) {
            loadProduct(product);
        }
    }


    private void loadProduct(Product xo) {
        com.ocs.indaba.po.Product po = new com.ocs.indaba.po.Product();

        po.setAccessMatrixId(0);
        po.setContentType(xo.getContentType());
        po.setDescription(xo.getDescription());
        po.setMode(xo.getMode());
        po.setName(ctx.addImpPrefix() ? getQualifiedName(xo.getName()) : xo.getName());
        po.setProductConfigId(xo.getSurveyConfig().getDboId());
        po.setProjectId(xo.getProject().getDboId());
        po.setWorkflowId(1);

        prodDao.create(po);
        xo.setDboId(po.getId());
    }

}
