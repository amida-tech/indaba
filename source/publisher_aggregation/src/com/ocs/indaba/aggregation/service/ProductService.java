/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.service;

import com.ocs.indaba.aggregation.vo.ProductBriefVO;
import com.ocs.indaba.aggregation.vo.ProductForExportVO;
import com.ocs.indaba.builder.dao.ProductDAO;
import com.ocs.indaba.aggregation.vo.ProductVO;
import com.ocs.indaba.common.OrgAuthorizer;
import com.ocs.indaba.po.Product;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.ocs.indaba.common.Constants;


/**
 *
 * @author Jeanbone
 */
public class ProductService {

    private static final Logger logger = Logger.getLogger(ProductService.class);

    private ProductDAO productDao;

    public List<ProductVO> getJournalProductsByProjectId(int projectId) {
        return productDao.selectJournalProductsByProjectId(projectId);
    }

    public List<ProductVO> getJournalProductsByProjectIds(List<Integer> projectIds) {

        List<ProductVO> products = new ArrayList<ProductVO>();
        for (Integer projectId : projectIds) {
            products.addAll(getJournalProductsByProjectId(projectId));
        }
        Collections.sort(products, new Comparator<ProductVO>() {

            public int compare(ProductVO p, ProductVO p1) {
                int retVal = p.getProjectName().compareTo(p1.getProjectName());
                if (retVal == 0) {
                    retVal = p.getProduct().getName().compareTo(p1.getProduct().getName());
                }
                return retVal;
            }
        });
        return products;
    }


    public List<ProductBriefVO> getAccessibleTscSurveyProducts(OrgAuthorizer orgAuth) {
        if (orgAuth == null) return null;
        List<ProductForExportVO> products = getAccessibleTscSurveyProducts(orgAuth, 0, null, null);
        return toProductBriefVO(products);
    }


    public static List<ProductBriefVO> toProductBriefVO(List<ProductForExportVO> products) {

        if (products == null || products.isEmpty()) return null;

        ArrayList<ProductBriefVO> vos = new ArrayList<ProductBriefVO>();
        for (ProductForExportVO evo : products) {
            ProductBriefVO bvo = new ProductBriefVO();
            bvo.setProductId(evo.getId());
            bvo.setProductName(evo.getProdName());
            bvo.setProjectName(evo.getProjName());
            bvo.setProjectOrgId(evo.getOrgId());
            bvo.setVisibility(evo.getProjVisibility());
            vos.add(bvo);
        }

        return vos;
    }

    public Product getProduct(int productId) {

        return productDao.get(productId);
    }

    public ProductVO getProductVOById(int productId) {

        return productDao.selectProductVOById(productId);
    }


    public String[] getProductNamesByProductVOs(List<ProductVO> products) {
        TreeSet<String> ts = new TreeSet<String>();
        for (ProductVO productVO : products) {
            ts.add(productVO.getProduct().getName());
        }
        return ts.toArray(new String[]{});
    }

    public List<ProductForExportVO> getAccessibleTscSurveyProducts(OrgAuthorizer orgAuth, int filterOrgId, String sortName, String sortType) {
        if (orgAuth == null) return null;
        
        List<Integer> orgIds = null;

        if (filterOrgId > 0) {
            orgIds = new ArrayList<Integer>();
            orgIds.add(filterOrgId);
        } else if (!orgAuth.isSiteAdmin()) {
            orgIds = orgAuth.getAccessibleOrgIdList(Constants.VISIBILITY_PRIVATE);
            if (orgIds == null || orgIds.isEmpty()) return null;
        }

        return productDao.selectTscSurveyProductsByOrgIds(orgIds, sortName, sortType);
    }


    public List<ProductForExportVO> getAccessibleSurveyProducts(OrgAuthorizer orgAuth, int filterOrgId, String sortName, String sortType) {
        if (orgAuth == null) return null;

        List<Integer> orgIds = null;

        if (filterOrgId > 0) {
            orgIds = new ArrayList<Integer>();
            orgIds.add(filterOrgId);
        } else if (!orgAuth.isSiteAdmin()) {
            orgIds = orgAuth.getAccessibleOrgIdList(Constants.VISIBILITY_PRIVATE);
            if (orgIds == null || orgIds.isEmpty()) return null;
        }

        return productDao.selectSurveyProductsByOrgIds(orgIds, sortName, sortType);
    }


    public List<ProductBriefVO> getAccessibleProducts(OrgAuthorizer orgAuth, int filterOrgId, String sortName, String sortType) {
        if (orgAuth == null) return null;

        List<Integer> orgIds = null;

        if (filterOrgId > 0) {
            orgIds = new ArrayList<Integer>();
            orgIds.add(filterOrgId);
        } else if (!orgAuth.isSiteAdmin()) {
            orgIds = orgAuth.getAccessibleOrgIdList(Constants.VISIBILITY_PRIVATE);
            if (orgIds == null || orgIds.isEmpty()) return null;
        }

        List<ProductForExportVO> evos = productDao.selectProductsByOrgIds(orgIds, sortName, sortType);

        return toProductBriefVO(evos);
    }


    public List<ProductBriefVO> getAccessibleJournalProducts(OrgAuthorizer orgAuth, int filterOrgId, String sortName, String sortType) {
        if (orgAuth == null) return null;

        List<Integer> orgIds = null;

        if (filterOrgId > 0) {
            orgIds = new ArrayList<Integer>();
            orgIds.add(filterOrgId);
        } else if (!orgAuth.isSiteAdmin()) {
            orgIds = orgAuth.getAccessibleOrgIdList(Constants.VISIBILITY_PRIVATE);
            if (orgIds == null || orgIds.isEmpty()) return null;
        }

        List<ProductForExportVO> evos = productDao.selectJournalsByOrgIds(orgIds, sortName, sortType);

        return toProductBriefVO(evos);
    }



    public boolean hasCompletedHorse(int productId) {
        return productDao.checkHasCompletedHorse(productId);
    }

   

    @Autowired
    public void setProductDao(ProductDAO productDao) {
        this.productDao = productDao;
    }

}
