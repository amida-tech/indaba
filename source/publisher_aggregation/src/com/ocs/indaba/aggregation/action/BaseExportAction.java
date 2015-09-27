/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.aggregation.action;

import com.ocs.indaba.aggregation.cache.BasePersistence;
import com.ocs.indaba.aggregation.cache.PIFPersistence;
import com.ocs.indaba.aggregation.cache.SRFPersistence;
import com.ocs.indaba.aggregation.vo.ProductInfo;
import com.ocs.indaba.aggregation.vo.ScorecardInfo;
import com.ocs.indaba.dao.ProductDAO;
import com.ocs.indaba.po.Product;
import com.ocs.util.StringUtils;
import java.io.File;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is a base action.
 *
 * @author Jeff
 *
 */
public abstract class BaseExportAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(BaseExportAction.class);
    private static final String PARAM_VERSION = "version";
    //protected static final String MIME_TYPE_EXCEL = "application/vnd.ms-excel";
    //protected static final String MIME_TYPE_CSV = "text/comma-separated-values";
    //protected static final String MIME_TYPE_ZIP = "application/zip";
    protected static final String PARAM_BASE_DIR = "basedir";
    protected static final String PARAM_CACHE_DIR = "cachedir";
    protected int version = 1;
    protected ProductDAO productDao = null;

    protected ActionForward prehandle(ActionMapping mapping, HttpServletRequest request) {
        version = StringUtils.str2int(request.getParameter(PARAM_VERSION), 1);
        return preprocess(mapping, request);
    }

    protected ScorecardInfo getScorecardInfo(int horseId) {

        File srfFile = BasePersistence.getSRFFile(horseId, null);

        if (srfFile == null) {
            return null;
        }

        SRFPersistence persistence = new SRFPersistence(null);

        return persistence.deserializeSRF(srfFile);
    }

    protected ProductInfo getProductInfo(int productId) {

        File pifFile = BasePersistence.getPIFFile(productId, null);

        if (pifFile == null || !pifFile.exists() || !pifFile.isFile()) {
            logger.error("PIF file isn't existed: " + pifFile.getAbsolutePath());
            return null;
        }
        logger.info("Load PIF file: " + pifFile.getAbsolutePath());
        PIFPersistence persistence = new PIFPersistence(null);

        ProductInfo info = persistence.deserializePIF(pifFile);
        
        Product product = productDao.selectProductById(productId);
        
        info.setProdouctDesc(product.getDescription());

        return info;
    }
    
    protected void setResponseAttachmentAndContentType(HttpServletResponse response, String filename) {
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setContentType(new MimetypesFileTypeMap().getContentType(filename));
    }

    @Autowired
    public void setProductDAO(ProductDAO productDao) {
        this.productDao = productDao;
    }
}
