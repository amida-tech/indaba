/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.GoalDAO;
import com.ocs.indaba.dao.JournalConfigDAO;
import com.ocs.indaba.dao.ProductDAO;
import com.ocs.indaba.dao.ScContributorDAO;
import com.ocs.indaba.dao.SurveyConfigDAO;
import com.ocs.indaba.dao.TaskDAO;
import com.ocs.indaba.dao.ToolDAO;
import com.ocs.indaba.dao.WorkflowDAO;
import com.ocs.indaba.po.Goal;
import com.ocs.indaba.po.JournalConfig;
import com.ocs.indaba.po.Product;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.ScContributor;
import com.ocs.indaba.po.SurveyConfig;
import com.ocs.indaba.po.Task;
import com.ocs.indaba.po.Tool;
import com.ocs.indaba.po.Workflow;
import com.ocs.indaba.vo.ProductVO;
import com.ocs.util.Pagination;
import com.ocs.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class ProductService {

    private static final Logger logger = Logger.getLogger(ProductService.class);
    private ProductDAO productDao = null;
    private SurveyConfigDAO surveyConfigDao = null;
    private JournalConfigDAO journalConfigDao = null;
    private WorkflowDAO workflowDao = null;
    private GoalDAO goalDao = null;
    private ToolDAO toolDao = null;
    private TaskDAO taskDao = null;
    private ScContributorDAO sccDao = null;

    private ProjectService projSrvc = null;

    /**
     * Select all of the products in Indaba
     *
     * @return list of product
     */
    public List<Product> getAllProducts() {
        logger.debug("Get all of the products");

        return productDao.selectAllProducts();
    }

    public Product getProductByName(String prodName) {
        return productDao.getProductByName(prodName);
    }

    public boolean checkEachGoalHasTaskByProductId(int prodId) {
        List<Pair<Integer, Integer>> pairs = productDao.countTasksOfGoalByProductName(prodId);
        if (pairs != null && !pairs.isEmpty()) {
            for (Pair<Integer, Integer> p : pairs) {
                Integer count = p.getK2();
                if (count == null || count < 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public void deleteTasksByProductId(int prodId) {
        taskDao.deleteTasksByProductId(prodId);
    }

    public List<Product> getProductsByProjectId(int projectId) {
        return productDao.selectProductsByProjectId(projectId);
    }

    public List<Product> getActiveProductsByProjectId(int projectId) {
        return productDao.selectActiveProductsByProjectId(projectId);
    }

    public List<Product> getUserProductsByProjectId(int projectId, int userId) {
        return productDao.selectUserProductsByProjectId(projectId, userId);
    }

    public List<Product> getUserActiveProductsByProjectId(int projectId, int userId) {
        return productDao.selectUserActiveProductsByProjectId(projectId, userId);
    }

    public Task createTask(Task task) {
        return taskDao.create(task);
    }

    public Pagination<ProductVO> getProductsByProjectId(int projectId, String sortName, String sortOrder, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        int count = pageSize;
        long totalCount = productDao.countOfProductByProjectId(projectId);
        List<Product> products = productDao.selectProductsByProjectId(projectId, sortName, sortOrder, offset, count);
        List<SurveyConfig> surveyConfigs = surveyConfigDao.findAll();
        List<JournalConfig> journalConfigs = journalConfigDao.findAll();
        List<ProductVO> prodList = new ArrayList<ProductVO>();
        if (products != null && !products.isEmpty()) {
            for (Product prod : products) {
                ProductVO prodVo = new ProductVO();
                prodVo.setContentType(prod.getContentType());
                prodVo.setAccessMatrixId(prod.getAccessMatrixId());
                prodVo.setDescription(prod.getDescription());
                prodVo.setId(prod.getId());
                prodVo.setMode(prod.getMode());
                prodVo.setName(prod.getName());
                prodVo.setProductConfigId(prod.getProductConfigId());
                prodVo.setProjectId(prod.getProjectId());
                prodVo.setWorkflowId(prod.getWorkflowId());
                int configId = prodVo.getProductConfigId();
                if (prod.getContentType() == Constants.CONTENT_TYPE_SURVEY) {
                    if (surveyConfigs != null && !surveyConfigs.isEmpty()) {
                        for (SurveyConfig sc : surveyConfigs) {
                            if (configId == sc.getId()) {
                                prodVo.setConfigName(sc.getName());
                            }
                        }
                    }
                } else {
                    if (journalConfigs != null) {
                        for (JournalConfig jc : journalConfigs) {
                            if (configId == jc.getId()) {
                                prodVo.setConfigName(jc.getName());
                            }
                        }
                    }
                }
                prodList.add(prodVo);
            }
        }
        Pagination<ProductVO> pagination = new Pagination<ProductVO>(totalCount, page, pageSize);
        pagination.setRows(prodList);
        return pagination;
    }

    public Product getProductById(int productId) {
        logger.debug("Get the product by id: " + productId);

        return productDao.selectProductById(productId);
    }

    public Product getProductByHorseId(int horseId) {
        logger.debug("Get the product by horse id: " + horseId);

        return productDao.selectProductByHorseId(horseId);
    }

    public Product addProduct(Product product) {
        return productDao.create(product);
    }

    public Product updateProduct(Product product) {
        return productDao.update(product);
    }

    public void deleteProduct(int prodId) {
        productDao.delete(prodId);
    }

    public SurveyConfig createSurveyConfig(SurveyConfig surveyConfig) {
        return surveyConfigDao.create(surveyConfig);
    }

    public JournalConfig createJournalConfig(JournalConfig journalConfig) {
        return journalConfigDao.create(journalConfig);
    }

    public List<Goal> getGoalsOfWorkflow(int workflowId) {
        return goalDao.selectGoalsByWorkflowId(workflowId);
    }
    
    public List<Tool> getAllToolsByProductId(int productId) {
        Product product = productDao.get(productId);
        if (product == null) {
            return null;
        }
        List<Tool> tools = null;
        if (Constants.CONTENT_TYPE_SURVEY == product.getContentType()) {
            SurveyConfig sc = surveyConfigDao.get(product.getProductConfigId());
            boolean isTsc = (sc != null) ? sc.getIsTsc() : false;
            tools = toolDao.selectToolsOfSurvey(isTsc);
        } else {
            tools = toolDao.selectToolsOfJournal();
        }
        return tools;
    }

    public Workflow createWorkflow(Workflow workflow) {
        return workflowDao.create(workflow);
    }

    public List<SurveyConfig> getSurveyConfigsForProject(int projectId) {
        Project project = projSrvc.getProjectById(projectId);

        if (project == null) return null;

        if (project.getVisibility() == Constants.VISIBILITY_PUBLIC) {
            return surveyConfigDao.selectAllActiveSurveyConfigs(Constants.VISIBILITY_PUBLIC);
        }

        // private project only can use private SCs that belong to the project and owners
        List<SurveyConfig> scList = surveyConfigDao.selectAllActiveSurveyConfigs(Constants.VISIBILITY_PRIVATE);

        if (scList == null || scList.size() == 0) return scList;

        List<Integer> projOwners = projSrvc.getOwnedOrgIds(projectId);
        List<ScContributor> sccList = sccDao.selectContributorsByVisibility(Constants.VISIBILITY_PRIVATE);
        HashMap<Integer, List<Integer>> scOwnerMap = new HashMap<Integer, List<Integer>>();

        for (SurveyConfig sc : scList) {
            addSCOwner(scOwnerMap, sc.getId(), sc.getOwnerOrgId());
        }

        if (sccList != null) {
            for (ScContributor scc : sccList) {
                addSCOwner(scOwnerMap, scc.getSurveyConfigId(), scc.getOrgId());
            }
        }

        List<SurveyConfig> result = new ArrayList<SurveyConfig>();
        for (SurveyConfig sc : scList) {
            // see if the sc qualifies. All owners of the project must in the scOwner list
            List<Integer> scOwners = scOwnerMap.get(sc.getId());
            if (scOwners != null && scOwners.containsAll(projOwners)) {
                result.add(sc);
            }
        }

        return result;
    }


    private void addSCOwner(HashMap<Integer, List<Integer>> scOwnerMap, int scId, int orgId) {
        List<Integer> orgIdList = scOwnerMap.get(scId);

        if (orgIdList == null) {
            orgIdList = new ArrayList<Integer>();
            scOwnerMap.put(scId, orgIdList);
        }
        orgIdList.add(orgId);
    }


    public List<JournalConfig> getAllJournalConfigs() {
        return journalConfigDao.selectAllJournalConfigs();
    }

    public void updateProductMode(int productId, short newMode){
        productDao.updateProductMode(productId, newMode);
    }

    // see whether the SC visibility rule would be violated if the org is added
    public SurveyConfig findViolatedSurveyConfig(Product product, int orgId) {
        if (product == null) return null;

        if (product.getContentType() != Constants.CONTENT_TYPE_SURVEY) return null;

        SurveyConfig sc = surveyConfigDao.get(product.getProductConfigId());

        if (sc == null) return null;

        if (sc.getOwnerOrgId() == orgId) return null;
        List<ScContributor> sccList = sccDao.selectContributorsBySurveyConfigId(sc.getId());

        if (sccList == null || sccList.isEmpty()) return null;

        for (ScContributor scc : sccList) {
            if (scc.getOrgId() == orgId) return null;
        }

        return sc;
    }

    
    @Autowired
    public void setProductDao(ProductDAO productDao) {
        this.productDao = productDao;
    }

    @Autowired
    public void setGoalDao(GoalDAO goalDao) {
        this.goalDao = goalDao;
    }

    @Autowired
    public void setToolDao(ToolDAO toolDao) {
        this.toolDao = toolDao;
    }

    @Autowired
    public void setSurveyConfigDAO(SurveyConfigDAO surveyConfigDao) {
        this.surveyConfigDao = surveyConfigDao;
    }

    @Autowired
    public void setJournalConfigDAO(JournalConfigDAO journalConfigDAO) {
        this.journalConfigDao = journalConfigDAO;
    }

    @Autowired
    public void setWorkflowDao(WorkflowDAO workflowDAO) {
        this.workflowDao = workflowDAO;
    }

    @Autowired
    public void setTaskDao(TaskDAO taskDao) {
        this.taskDao = taskDao;
    }

    @Autowired
    public void setScContributorDao(ScContributorDAO dao) {
        this.sccDao = dao;
    }

    @Autowired
    public void setProjectService(ProjectService srvc) {
        this.projSrvc = srvc;
    }
}
