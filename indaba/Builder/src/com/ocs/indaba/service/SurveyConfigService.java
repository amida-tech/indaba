/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.util.SimpleTree;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.ContentHeaderDAO;
import com.ocs.indaba.dao.ProductDAO;
import com.ocs.indaba.dao.SurveyCategoryDAO;
import com.ocs.indaba.dao.SurveyConfigDAO;
import com.ocs.indaba.dao.SurveyIndicatorDAO;
import com.ocs.indaba.dao.SurveyQuestionDAO;
import com.ocs.indaba.po.SurveyCategory;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.po.SurveyQuestion;
import com.ocs.indaba.dao.ScContributorDAO;
import com.ocs.indaba.dao.ScIndicatorDAO;
import com.ocs.indaba.dao.SurveyAnswerDAO;
import com.ocs.indaba.dao.SurveyPeerReviewDAO;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.Organization;
import com.ocs.indaba.po.Product;
import com.ocs.indaba.po.ScContributor;
import com.ocs.indaba.po.ScIndicator;
import com.ocs.indaba.po.SurveyAnswer;
import com.ocs.indaba.po.SurveyConfig;
import com.ocs.indaba.po.SurveyPeerReview;
import com.ocs.indaba.survey.tree.CategoryNode;
import com.ocs.indaba.survey.tree.Node;
import com.ocs.indaba.survey.tree.QuestionNode;
import com.ocs.indaba.survey.tree.SurveyTree;
import com.ocs.indaba.vo.SurveyConfigVO;
import com.ocs.indaba.vo.SurveyConfigVO.OrgInfo;
import com.ocs.indaba.vo.SurveyConfigVO.ProductInfo;
import com.ocs.indaba.vo.SurveyIndicatorView;
import com.ocs.indaba.vo.SurveyQuestionTreeView;
import com.ocs.util.Leaf;
import com.ocs.util.ListUtils;
import com.ocs.util.Pagination;
import com.ocs.util.SimpleTree.NodeHandler;
import com.ocs.util.Tree;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author luwb
 */
public class SurveyConfigService {

    private static final Logger logger = Logger.getLogger(SurveyConfigService.class);
    private SurveyQuestionDAO surveyQuestionDao = null;
    private SurveyCategoryDAO surveyCategoryDao = null;
    private SurveyIndicatorDAO surveyIndicatorDao = null;
    private SurveyAnswerDAO surveyAnswerDao = null;
    private SurveyPeerReviewDAO surveyPeerReviewDao = null;
    private SurveyConfigDAO surveyConfigDao = null;
    private ScContributorDAO scContributorDao = null;
    private ScIndicatorDAO scIndicatorDao = null;
    private ProductDAO productDao = null;
    private ContentHeaderDAO contentHeaderDao = null;
    private OrganizationService orgSrvc = null;

    @Autowired
    public void setOrganizationService(OrganizationService orgSrvc) {
        this.orgSrvc = orgSrvc;
    }

    @Autowired
    public void setSurveyQuestionDao(SurveyQuestionDAO surveyQuestionDao) {
        this.surveyQuestionDao = surveyQuestionDao;
    }

    @Autowired
    public void setSurveyCategoryDao(SurveyCategoryDAO surveyCategoryDao) {
        this.surveyCategoryDao = surveyCategoryDao;
    }

    @Autowired
    public void setSurveyIndicatorDao(SurveyIndicatorDAO surveyIndicatorDao) {
        this.surveyIndicatorDao = surveyIndicatorDao;
    }

    @Autowired
    public void setSurveyAnswerDao(SurveyAnswerDAO surveyAnswerDao) {
        this.surveyAnswerDao = surveyAnswerDao;
    }

    @Autowired
    public void setSurveyPeerReviewDAO(SurveyPeerReviewDAO surveyPeerReviewDao) {
        this.surveyPeerReviewDao = surveyPeerReviewDao;
    }

    @Autowired
    public void setSurveyConfigDAO(SurveyConfigDAO surveyConfigDao) {
        this.surveyConfigDao = surveyConfigDao;
    }

    @Autowired
    public void setScContributorDao(ScContributorDAO scContributorDao) {
        this.scContributorDao = scContributorDao;
    }

    @Autowired
    public void setScIndicatorDao(ScIndicatorDAO scIndicatorDao) {
        this.scIndicatorDao = scIndicatorDao;
    }

    @Autowired
    public void setProductDao(ProductDAO productDao) {
        this.productDao = productDao;
    }

    @Autowired
    public void setContentHeaderDao(ContentHeaderDAO contentHeaderDao) {
        this.contentHeaderDao = contentHeaderDao;
    }

    public boolean existsScIndicator(int surveyConfigId, int indicatorId) {
        return scIndicatorDao.exists(surveyConfigId, indicatorId);
    }

    public List<ScIndicator> getIndicatorsByVisibility(int visibility) {
        return scIndicatorDao.selectIndicatorsByVisibility(visibility);
    }

    public List<ScIndicator> getIndicatorsBySurveyConfigId(int scId) {
        return scIndicatorDao.selectIndicatorsBySurveyConfigId(scId);
    }

    public List<Integer> getIndicatorIdsBySurveyConfigId(int scId) {
        return scIndicatorDao.selectIndicatorIdsBySurveyConfigId(scId);
    }

    public void deleteScIndicatorsByConfigIdAndIndicatorIds(int scId, List<Integer> siIds) {
        scIndicatorDao.deleteByConfigIdAndIndicatorIds(scId, siIds);
    }

    public void deleteScIndicatorsByConfigId(int scId) {
        scIndicatorDao.deleteByConfigId(scId);
    }

    public ScIndicator addScIndicator(ScIndicator sci) {
        if (!scIndicatorDao.exists(sci.getSurveyConfigId(), sci.getSurveyIndicatorId())) {
            return scIndicatorDao.create(sci);
        } else {
            return null;
        }
    }

    public List<Integer> getSecondaryOrgIdsBySurveyConfigId(int scId) {
        return scContributorDao.selectOrgIdsBySurveyConfigId(scId);
    }

    public void deleteScContributorsByConfigId(int scId) {
        scContributorDao.deleteByConfigId(scId);
    }

    public SurveyConfig addSurveyConfig(SurveyConfig surveyConfig) {
        return surveyConfigDao.create(surveyConfig);
    }

    public SurveyConfig updateSurveyConfig(SurveyConfig surveyConfig) {
        return surveyConfigDao.update(surveyConfig);
    }

    public int cloneSurveyConfig(int srcSurveyConfigId, String name, int orgId) {
        int rc = surveyConfigDao.call(Constants.PROCEDURE_CLONE_SURVEY_CONFIG, srcSurveyConfigId, name, orgId);
        logger.debug("Stored proc RC: " + rc);
        return rc;
    }

    public void deleteSurveyConfig(int surveyConfigId) {
        scContributorDao.deleteByConfigId(surveyConfigId);
        scIndicatorDao.deleteByConfigId(surveyConfigId);
        surveyQuestionDao.deleteByConfigId(surveyConfigId);
        surveyCategoryDao.deleteByConfigId(surveyConfigId);
        surveyConfigDao.delete(surveyConfigId);
    }

    public SurveyConfig getSurveyConfig(int surveyConfigId) {
        return surveyConfigDao.get(surveyConfigId);
    }

    public SurveyConfig getSurveyConfigByName(String surveyConfigName) {
        return surveyConfigDao.selectByName(surveyConfigName);
    }

    public ScContributor addScContributor(ScContributor scc) {
        if (!scContributorDao.exists(scc.getSurveyConfigId(), scc.getOrgId())) {
            return scContributorDao.create(scc);
        } else {
            return null;
        }
    }

    public Pagination<SurveyConfigVO> getSurveyConfigs(boolean isSA, int userId, Map<Integer, Boolean> ownedOrgMap, int visibility, List<Integer> orgIds, String sortName, String sortOrder, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        int count = pageSize;
        long totalCount = 0;
        List<SurveyConfig> surveyConfigs = null;
        List<Integer> ownedOrgIds = orgSrvc.getOrgIdsByAdminId(userId);
        if (isSA || visibility == Constants.VISIBILITY_PUBLIC) {
            totalCount = surveyConfigDao.selectSurveyConfigCountForSA(visibility, orgIds);
            surveyConfigs = surveyConfigDao.selectSurveyConfigsWithPaginationForSA(visibility, orgIds, sortName, sortOrder, offset, count);
        } else {
            totalCount = surveyConfigDao.selectSurveyConfigCountForNonSA(ownedOrgIds, visibility, orgIds);
            surveyConfigs = surveyConfigDao.selectSurveyConfigsForNonSAWithPagination(ownedOrgIds, visibility, orgIds, sortName, sortOrder, offset, count);
        }
        List<SurveyConfigVO> scVoList = new ArrayList<SurveyConfigVO>();
        if (surveyConfigs != null && !surveyConfigs.isEmpty()) {
            List<Integer> surveyConfigIds = new ArrayList<Integer>();
            for (SurveyConfig sc : surveyConfigs) {
                surveyConfigIds.add(sc.getId());
            }
            Map<Integer, List<Product>> configProdMap = surveyConfigDao.getProductMap(surveyConfigIds);
            Map<Integer, List<Organization>> configOrgmap = surveyConfigDao.selectOrgsBySurveyConfigIds(surveyConfigIds);
            for (SurveyConfig sc : surveyConfigs) {
                SurveyConfigVO scVo = new SurveyConfigVO();
                scVo.setId(sc.getId());
                scVo.setPrimaryOrgId(sc.getOwnerOrgId());
                scVo.setName(sc.getName());
                scVo.setTsc(sc.getIsTsc());
                scVo.setVisibility(sc.getVisibility());
                List<Product> prodList = configProdMap.get(sc.getId());
                if (prodList != null && !prodList.isEmpty()) {
                    for (Product prod : prodList) {
                        ProductInfo pi = new ProductInfo();
                        pi.setName(prod.getName());
                        pi.setMode(prod.getMode());
                        scVo.addProduct(pi);
                    }
                }
                surveyConfigIds.add(scVo.getId());
                scVoList.add(scVo);
            }
            for (SurveyConfigVO scVo : scVoList) {
                boolean isOwned = false;
                boolean isPrimaryOwner = false;
                List<Organization> orgs = configOrgmap.get(scVo.getId());
                if (orgs != null && !orgs.isEmpty()) {
                    for (Organization org : orgs) {
                        OrgInfo oi = new OrgInfo();
                        oi.setName(org.getName());
                        scVo.addOrg(oi);
                        if (scVo.getPrimaryOrgId() == org.getId()) {
                            oi.setPrimary(true);
                        }
                        if (ownedOrgMap.get(org.getId()) != null) {
                            isOwned = true;
                        }
                    }
                }
                boolean inUse = false;
                boolean inActiveUse = false;
                List<Product> prodList = configProdMap.get(scVo.getId());
                if (prodList != null && !prodList.isEmpty()) {
                    inUse = true;
                    for (Product p : prodList) {
                        if (p.getMode() != Constants.PRODUCT_MODE_CONFIG) {
                            inActiveUse = true;
                            break;
                        }
                    }
                }
                scVo.setInActiveUse(inActiveUse);
                scVo.setInUse(inUse);
                if (isSA) {
                    isOwned = true;
                    isPrimaryOwner = true;
                } else {
                    isPrimaryOwner = ownedOrgMap.get(scVo.getPrimaryOrgId()) != null;
                }
                scVo.setOwned(isOwned);
                scVo.setPrimaryOwner(isPrimaryOwner);
            }
        }
        Pagination<SurveyConfigVO> pagination = new Pagination<SurveyConfigVO>(totalCount, page, pageSize);
        pagination.setRows(scVoList);
        return pagination;
    }

    public List<Integer> getOrgIdsBySurveyConfigId(int surveyConfigId) {
        return surveyConfigDao.selectOrgIdsBySurveyConfigId(surveyConfigId);
    }

    public List<SurveyIndicator> getAvaialbleScIndicators(int surveyConfigId, int withQstnId) {
        return surveyIndicatorDao.selectAvailableScIndicators(surveyConfigId, withQstnId);
    }

    public Pagination<SurveyIndicatorView> findScIndicators(int surveyConfigId, String sortName, String sortOrder, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        int count = pageSize;
        int totalCount = (int) surveyIndicatorDao.countOfSurveyIndicatorsByConfigId(surveyConfigId);
        List<SurveyIndicatorView> siViewList = surveyIndicatorDao.selectSurveyIndicatorsByConfigId(surveyConfigId, sortName, sortOrder, offset, count);
        if (!ListUtils.isEmptyList(siViewList)) {
            List<Integer> usedSiIds = surveyIndicatorDao.selectUsedSurveyIndicatorIds(surveyConfigId);
            if (!ListUtils.isEmptyList(usedSiIds)) {
                for (SurveyIndicatorView siView : siViewList) {
                    if (usedSiIds.contains(siView.getIndicatorId())) {
                        siView.setUsed(true);
                    }
                }
            }
        }
        Pagination<SurveyIndicatorView> pagination = new Pagination<SurveyIndicatorView>(totalCount, page, pageSize);
        pagination.setRows(siViewList);
        return pagination;
    }

    public Pagination<SurveyIndicatorView> findIndicators(int surveyConfigId, int visibility, List<Integer> candidateOrgIds, String name, String question, String sortName, String sortOrder, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        int count = pageSize;
        int totalCount = (int) surveyIndicatorDao.countOfAvailableSurveyIndicatorsByConfigId(surveyConfigId, visibility, candidateOrgIds, name, question);
        List<SurveyIndicatorView> siViewList = surveyIndicatorDao.selectAvailableSurveyIndicatorsByConfigId(surveyConfigId, visibility, candidateOrgIds, name, question, sortName, sortOrder, offset, count);
        Pagination<SurveyIndicatorView> pagination = new Pagination<SurveyIndicatorView>(totalCount, page, pageSize);
        pagination.setRows(siViewList);
        return pagination;
    }

    public List<SurveyQuestion> getSurveyQuestionsByConfigIdAndCategoryId(int surveyConfigId, int surveyCategoryId) {
        return surveyQuestionDao.selectSurveyQuestionsByConfigIdAndCategoryId(surveyConfigId, surveyCategoryId);
    }

    public List<SurveyQuestionTreeView> getSurveyQuestionTreeViewsByConfigIdAndCategoryId(int surveyConfigId, int surveyCategoryId) {
        return surveyQuestionDao.selectSurveyQuestionTreeViewsByConfigIdAndCategoryId(surveyConfigId, surveyCategoryId);
    }

    public List<Tree<SurveyCategory, SurveyQuestionTreeView>> getSurveyTree(int surveyConfigId) {
        List<Tree<SurveyCategory, SurveyQuestionTreeView>> forest = new ArrayList<Tree<SurveyCategory, SurveyQuestionTreeView>>();
        List<SurveyQuestionTreeView> questions = surveyQuestionDao.selectSurveyQuestionTreeViewsByConfigId(surveyConfigId);
        List<SurveyCategory> categories = surveyCategoryDao.selectSurveyCategorysByConfigId(surveyConfigId);
        Map<Integer, Tree<SurveyCategory, SurveyQuestionTreeView>> catTreeMap = new HashMap<Integer, Tree<SurveyCategory, SurveyQuestionTreeView>>();
        List<Integer> questionIds = new ArrayList<Integer>();
        if (!ListUtils.isEmptyList(questions)) {
            for (SurveyQuestion q : questions) {
                questionIds.add(q.getId());
            }
        }
        List<Integer> rootCatIds = new ArrayList<Integer>();
        if (!ListUtils.isEmptyList(categories)) {
            for (SurveyCategory cat : categories) {
                Tree<SurveyCategory, SurveyQuestionTreeView> catTree = new Tree<SurveyCategory, SurveyQuestionTreeView>(cat);
                catTreeMap.put(cat.getId(), catTree);
                if (cat.getParentCategoryId() == 0) {
                    rootCatIds.add(cat.getId());
                }
            }
            if (questions != null) {
                for (SurveyQuestionTreeView qstn : questions) {
                    int parentCatId = qstn.getSurveyCategoryId();
                    Tree<SurveyCategory, SurveyQuestionTreeView> parent = catTreeMap.get(parentCatId);
                    if (parent != null) {
                        parent.addLeaf(new Leaf<SurveyQuestionTreeView>(qstn));
                    } else {
                        logger.error("Survey question's parent does not exist: [curQstnId=" + qstn.getId() + ", parentCatId=" + parentCatId + "]!");
                    }
                }
            }
            for (SurveyCategory cat : categories) {
                int parentCatId = cat.getParentCategoryId();
                if (parentCatId > 0) {
                    Tree<SurveyCategory, SurveyQuestionTreeView> parent = catTreeMap.get(parentCatId);
                    if (parent != null) {
                        parent.addTree(catTreeMap.get(cat.getId()));
                    } else {
                        logger.error("Survey category's parent is not existed: [curCatId=" + cat.getId() + ", parentCatId=" + parentCatId + "]!");
                    }
                }
            }
            for (int rootCatId : rootCatIds) {
                forest.add(catTreeMap.get(rootCatId));
            }
        }
        return forest;
    }

    public void addSurveyConfigIndicators(int surveyConfigId, List<Integer> indicatorIds) {
        if (surveyConfigId < 0 || ListUtils.isEmptyList(indicatorIds)) {
            return;
        }
        List<Integer> scIndicatorIds = scIndicatorDao.selectIndicatorIdsBySurveyConfigId(surveyConfigId);
        for (int siId : indicatorIds) {
            if (!ListUtils.isEmptyList(scIndicatorIds) && scIndicatorIds.contains(siId)) {
                continue;
            }
            ScIndicator sci = new ScIndicator();
            sci.setSurveyConfigId(surveyConfigId);
            sci.setSurveyIndicatorId(siId);
            scIndicatorDao.create(sci);
        }
    }

    public SurveyCategory getSurveyCateogry(int catId) {
        return surveyCategoryDao.get(catId);
    }

    public SurveyQuestion getSurveyQuestion(int qstnId) {
        return surveyQuestionDao.get(qstnId);
    }

    public SurveyCategory updateSurveyCateogry(SurveyCategory surveyCategory) {
        return surveyCategoryDao.save(surveyCategory);
    }

    public SurveyQuestion updateSurveyQuestion(SurveyQuestion surveyQuestion) {
        return surveyQuestionDao.save(surveyQuestion);
    }

    public SurveyCategory addSurveyCateogry(SurveyCategory surveyCategory) {
        SurveyCategory cat = surveyCategoryDao.create(surveyCategory);
        computeSurveyConfigType(cat.getSurveyConfigId());
        return cat;
    }

    public SurveyQuestion addSurveyQuestion(SurveyQuestion surveyQuestion) {
        SurveyQuestion qst = surveyQuestionDao.create(surveyQuestion);
        computeSurveyConfigType(qst.getSurveyConfigId());
        return qst;
    }

    public SurveyQuestionTreeView getSurveyQuestionTreeView(int qstnId) {
        return surveyQuestionDao.getSurveyQuestionTreeView(qstnId);
    }

    public SurveyQuestion getSurveyQuestionExcept(int suerveyQuestionId, int surveyConfigId, int surveyIndicatorId) {
        return surveyQuestionDao.selectSurveyQuestionExcept(suerveyQuestionId, surveyConfigId, surveyIndicatorId);
    }

    public int getMaxSurveyCategoryWeight(int surveyConfigId, int surveyCategoryId) {
        return surveyCategoryDao.selectMaxWeight(surveyConfigId, surveyCategoryId);
    }

    public int getMaxSurveyQuestionWeight(int surveyConfigId, int surveyCategoryId) {
        return surveyQuestionDao.selectMaxWeight(surveyConfigId, surveyCategoryId);
    }

    public Map<Integer, Boolean> checkSurveyConfigsInUse(List<Integer> surveyConfigIds) {
        return surveyConfigDao.checkSurveyConfigsInUse(surveyConfigIds);
    }

    public Map<Integer, Boolean> checkSurveyConfigsInActiveUse(List<Integer> surveyConfigIds) {
        return surveyConfigDao.checkSurveyConfigsInActiveUse(surveyConfigIds);
    }

    public boolean checkSurveyConfigInUse(int surveyConfigId) {
        return surveyConfigDao.checkSurveyConfigInUse(surveyConfigId);
    }

    public boolean checkSurveyConfigInActiveUse(int surveyConfigId) {
        return surveyConfigDao.checkSurveyConfigInActiveUse(surveyConfigId);
    }

    public void deleteSurveyCategory(int surveyConfigId, int categoryId) {
        SurveyTree tree = this.buildTree(surveyConfigId);
        ArrayList<Integer> catIds = new ArrayList<Integer>();
        ArrayList<Integer> qstIds = new ArrayList<Integer>();
        tree.findSubElements(categoryId, catIds, qstIds);

        surveyQuestionDao.deleteQuestionsInCategories(surveyConfigId, catIds);
        surveyCategoryDao.deleteCategories(surveyConfigId, catIds);

        computeSurveyConfigType(surveyConfigId);
    }

    public void deleteSurveyQuestion(int surveyConfigId, int qstnId) {
        surveyQuestionDao.delete(qstnId);
        computeSurveyConfigType(surveyConfigId);
    }
    /*
     * Determine whether the config is TSC or not
     */

    private boolean computeSurveyConfigType(int scId) {
        SurveyConfig sc = surveyConfigDao.get(scId);
        if (sc == null) {
            return false;   // not exist
        }
        boolean isTsc = false;
        SurveyTree tree = buildTree(scId);
        if (tree != null) {
            isTsc = tree.isTraditional();
        }
        // update the survey config
        sc.setIsTsc(isTsc);
        surveyConfigDao.update(sc);
        return isTsc;
    }

    public SurveyTree buildTree(int scId) {
        List<CategoryNode> cList = getCategoryList(scId);
        List<QuestionNode> qList = getQuestionList(scId);
        try {
            return new SurveyTree(cList, qList);
        } catch (Exception e) {
            logger.error("Can't build survey tree for Survey Config " + scId + ": " + e);
            return null;
        }
    }



    public SurveyTree buildTreeByHorse(int horseId) {
        Product product = productDao.selectProductByHorseId(horseId);
        if (product == null) {
            logger.error("Product doesn't exist for horse[id=" + horseId + "].");
            return null;
        }
        return this.buildTree(product.getProductConfigId());
    }



    public SurveyTree buildTreeAndInitAnswers(int horseId, int taskType, int userId, int langId) {
        ContentHeader cntHdr = contentHeaderDao.selectContentHeaderByHorseId(horseId);
        if (cntHdr == null || cntHdr.getContentType() != Constants.CONTENT_TYPE_SURVEY) {
            logger.error("Content header doesn't exist or type isn't survey for horse[id=" + horseId + "].");
            return null;
        }
        Product product = productDao.selectProductByHorseId(horseId);
        if (product == null) {
            logger.error("Product doesn't exist for horse[id=" + horseId + "].");
            return null;
        }
        int scId = product.getProductConfigId();
        List<CategoryNode> cList = getCategoryList(scId);
        List<QuestionNode> qList = getQuestionList(scId, langId);

        List<Integer> questionIds = new ArrayList<Integer>();
        if (qList != null && !qList.isEmpty()) {
            for (QuestionNode q : qList) {
                questionIds.add(q.getId());
            }
        }
        logger.info("cntObjId: " + cntHdr.getContentObjectId() + ", questionIds: " + questionIds);
        Map<Integer, SurveyAnswer> saMap = surveyAnswerDao.selectSurveyAnswersBy(cntHdr.getContentObjectId(), questionIds);

        SurveyTree surveyTree = null;
        try {
            surveyTree = new SurveyTree(cList, qList);
            surveyTree.listNodes();
            // init with answers
            SimpleTree.<Node>traverseByDfs(surveyTree.getRoot(), new SurveyTreeHandler<Node>(taskType, userId, saMap));
        } catch (Exception ex) {
            logger.error("Can't build survey tree for Survey Config [" + scId + "].", ex);
        }
        return surveyTree;
    }

    class SurveyTreeHandler<T> implements NodeHandler<Node> {

        private Map<Integer, SurveyAnswer> surveyAnswerMap;
        private int taskType;
        private int userId;

        public SurveyTreeHandler(int taskType, int userId, Map<Integer, SurveyAnswer> surveyAnswerMap) {
            this.surveyAnswerMap = surveyAnswerMap;
            this.taskType = taskType;
            this.userId = userId;
        }

        public boolean handle(SimpleTree<Node> tree, Object... data) {
            if (tree == null) {
                return true;
            }
            Node node = tree.getValue();
            if (node == null) {
                logger.warn("Node's value is null!");
                return true;
            }

            switch (node.getType()) {
                case Node.NODE_TYPE_CATEGORY:
                    handleCategoryNode(tree);
                    break;
                case Node.NODE_TYPE_QUESTION:
                    handleQuestionNode(tree);
                    break;
            }

            return true;
        }

        private void handleCategoryNode(SimpleTree<Node> catTree) {
            CategoryNode cNode = (CategoryNode) catTree.getValue();
            if (cNode == null) {
                return;
            }
            SimpleTree<Node> pTree = catTree.getParent();
            CategoryNode parent = null;
            if ((pTree == null) || (parent = (CategoryNode) pTree.getValue()) == null) {
                return;
            }
            parent.incrementCompleted(cNode.getCompleted());
            parent.incrementIncompleted(cNode.getIncompleted());

            //
            // Fix bug #739
            if (taskType == Constants.TASK_TYPE_SURVEY_PR_REVIEW) {
                parent.incrementAgreed(cNode.getAgreed());
                parent.incrementNoQualified(cNode.getNoQualified());
                parent.incrementNeedClarified(cNode.getNeedClarified());
                parent.incrementDisagreed(cNode.getDisagreed());
                parent.incrementUnreviewed(cNode.getUnreviewed());

            }
        }

        private void handleQuestionNode(SimpleTree<Node> qstnTree) {
            QuestionNode qNode = (QuestionNode) qstnTree.getValue();
            SurveyAnswer sa = surveyAnswerMap.get(qNode.getId());
            if (sa == null) {
                return;
            }

            boolean hasData = (sa != null && sa.getAnswerObjectId() > 0);
            short answerStatus = QuestionNode.ANSWER_STATUS_NONE;

            int reviewOption = Constants.SURVEY_REVIEW_OPTION_NONE;
            switch (taskType) {
                case Constants.TASK_TYPE_SURVEY_EDIT:
                    if (hasData && sa.getEdited()) answerStatus = QuestionNode.ANSWER_STATUS_COMPLETE;
                    break;
                case Constants.TASK_TYPE_SURVEY_PEER_REVIEW:
                    SurveyPeerReview surveyPeerReview = null;
                    if (sa != null) {
                        surveyPeerReview = surveyPeerReviewDao.getCompletedSurveyPeerReviewsBySurveyAnswerIdAndReviewerId(sa.getId(), userId);
                    }
                    if (hasData && surveyPeerReview != null) answerStatus = QuestionNode.ANSWER_STATUS_COMPLETE;
                    break;
                case Constants.TASK_TYPE_SURVEY_REVIEW:
                    if (hasData && sa.getStaffReviewed()) answerStatus = QuestionNode.ANSWER_STATUS_COMPLETE;
                    break;
                case Constants.TASK_TYPE_SURVEY_PR_REVIEW:
                    if (hasData && sa.getPrReviewed()) answerStatus = QuestionNode.ANSWER_STATUS_COMPLETE;
                    
                    List<SurveyPeerReview> surveyPeerReviews = null;
                    if (sa != null) {
                        surveyPeerReviews = surveyPeerReviewDao.getSurveyPeerReviewsBySurveyAnswerId(sa.getId());
                    }
                    if (surveyPeerReviews != null && !surveyPeerReviews.isEmpty()) {
                        for (SurveyPeerReview spr : surveyPeerReviews) {
                            int opt = spr.getOpinion();
                            // determine the reviewOption based on worse opinion - YC 12/29/2011
                            switch (opt) {
                                case Constants.SURVEY_REVIEW_OPTION_DISAGREE:
                                    reviewOption = opt;
                                    break;
                                case Constants.SURVEY_REVIEW_OPTION_AGREE_BUT_CLARIFICATION:
                                    if (reviewOption != Constants.SURVEY_REVIEW_OPTION_DISAGREE) {
                                        reviewOption = opt;
                                    }
                                    break;
                                case Constants.SURVEY_REVIEW_OPTION_NO_QUALIFICATION:
                                    if (reviewOption != Constants.SURVEY_REVIEW_OPTION_DISAGREE
                                            && reviewOption != Constants.SURVEY_REVIEW_OPTION_AGREE_BUT_CLARIFICATION) {
                                        reviewOption = opt;
                                    }
                                    break;
                                default:
                                    if (reviewOption != Constants.SURVEY_REVIEW_OPTION_DISAGREE
                                            && reviewOption != Constants.SURVEY_REVIEW_OPTION_AGREE_BUT_CLARIFICATION
                                            && reviewOption != Constants.SURVEY_REVIEW_OPTION_NO_QUALIFICATION) {
                                        reviewOption = opt;
                                    }
                                    break;
                            }
                        }
                    }
                    break;
                case Constants.TASK_TYPE_SURVEY_OVERALL_REVIEW:
                    if (hasData && sa.getOverallReviewed()) answerStatus = QuestionNode.ANSWER_STATUS_COMPLETE;
                    break;
                case Constants.TASK_TYPE_SURVEY_CREATE:
                default:
                    if (hasData) {
                        if (qNode.getAnswerType() == Constants.SURVEY_ANSWER_TYPE_TABLE) {
                            answerStatus = sa.getCompleted() ? QuestionNode.ANSWER_STATUS_COMPLETE : QuestionNode.ANSWER_STATUS_INCOMPLETE;
                        } else {
                            answerStatus = QuestionNode.ANSWER_STATUS_COMPLETE;
                        }
                    }
            }
            qNode.setStatus(answerStatus);
            qNode.setReviewOption(reviewOption);
            
            // Handle Parent Node
            SimpleTree<Node> pTree = qstnTree.getParent();
            CategoryNode parent = null;
            if ((pTree == null) || (parent = (CategoryNode) pTree.getValue()) == null) {
                return;
            }

            if (answerStatus == QuestionNode.ANSWER_STATUS_COMPLETE) {
                parent.incrementCompleted();
            } else {
                parent.incrementIncompleted();
            }
            //
            // For CR #739
            if (taskType == Constants.TASK_TYPE_SURVEY_PR_REVIEW) {                
                switch (reviewOption) {
                    case Constants.SURVEY_REVIEW_OPTION_AGREE:
                        parent.incrementAgreed();
                        break;
                    case Constants.SURVEY_REVIEW_OPTION_NO_QUALIFICATION:
                        parent.incrementNoQualified();
                        break;
                    case Constants.SURVEY_REVIEW_OPTION_AGREE_BUT_CLARIFICATION:
                        parent.incrementNeedClarified();
                        break;
                    case Constants.SURVEY_REVIEW_OPTION_DISAGREE:
                        parent.incrementDisagreed();
                        break;
                    case Constants.SURVEY_REVIEW_OPTION_NONE:
                    default:
                        parent.incrementUnreviewed();
                        break;
                }
            } // end if
        }
    }

    private List<CategoryNode> getCategoryList(int scId) {
        List<CategoryNode> cList = new ArrayList<CategoryNode>();
        List<SurveyCategory> catList = surveyCategoryDao.selectSurveyCategorysByConfigId(scId);
        if (catList != null && !catList.isEmpty()) {
            for (SurveyCategory c : catList) {
                cList.add(new CategoryNode(c.getId(), c.getLabel(), c.getTitle(), c.getParentCategoryId(), c.getWeight()));
            }
        }
        return cList;
    }

    private List<QuestionNode> getQuestionList(int scId) {
        List<QuestionNode> qList = new ArrayList<QuestionNode>();
        List<SurveyQuestionTreeView> qstList = surveyQuestionDao.selectSurveyQuestionTreeViewsByConfigId(scId);
        if (qstList != null && !qstList.isEmpty()) {
            for (SurveyQuestionTreeView q : qstList) {
                qList.add(new QuestionNode(q.getId(), q.getPublicName(), q.getText(), q.getSurveyCategoryId(), q.getWeight(), q.getAnswerType()));
            }
        }
        return qList;
    }
    
    private List<QuestionNode> getQuestionList(int scId, int langId) {
        List<QuestionNode> qList = new ArrayList<QuestionNode>();
        List<SurveyQuestionTreeView> qstList = surveyQuestionDao.selectSurveyQuestionTreeViewsByConfigIdAndLanguage(scId, langId);
        if (qstList != null && !qstList.isEmpty()) {
            for (SurveyQuestionTreeView q : qstList) {
                qList.add(new QuestionNode(q.getId(), q.getPublicName(), q.getText(), q.getSurveyCategoryId(), q.getWeight(), q.getAnswerType()));
            }
        }
        return qList;
    }


    public static final int MOVE_INNER = 0;
    public static final int MOVE_BEFORE = -1;
    public static final int MOVE_AFTER = 1;
    public static final int MOVE_RC_OK = 0;
    public static final int MOVE_RC_NO_SURVEY_CONFIG = 1;
    public static final int MOVE_RC_NO_ORIG_NODE = 2;
    public static final int MOVE_RC_NO_TARGET_NODE = 3;

    public int handleMove(int scId, int origNodeId, boolean origNodeIsCat, int targetNodeId, int targetParentId, boolean targetNodeIsCat, int moveType) {
        int targetCatId = targetParentId;
        if (moveType == MOVE_INNER) {
            targetCatId = targetNodeId;
        }
        SurveyTree tree = buildTree(scId);
        if (tree == null) {
            return MOVE_RC_NO_SURVEY_CONFIG;
        }
        Node oldOrigNode = tree.getNode(origNodeIsCat ? Node.NODE_TYPE_CATEGORY : Node.NODE_TYPE_QUESTION, origNodeId);
        if (oldOrigNode == null) {
            // tree out of date from the client
            return MOVE_RC_NO_ORIG_NODE;
        }
        List<Node> children = tree.getChildren(targetCatId);
        // see whether the orig is in the list
        Node origNode = null;
        if (children != null) {
            for (Node child : children) {
                if (origNodeIsCat) {
                    if (child.getType() == Node.NODE_TYPE_CATEGORY && child.getId() == origNodeId) {
                        origNode = child;
                        break;
                    }
                } else {
                    if (child.getType() == Node.NODE_TYPE_QUESTION && child.getId() == origNodeId) {
                        origNode = child;
                        break;
                    }
                }
            }
        } else {
            children = new ArrayList<Node>();
        }
        if (origNode == null) {
            origNode = new Node(origNodeIsCat ? Node.NODE_TYPE_CATEGORY : Node.NODE_TYPE_QUESTION,
                    origNodeId, "", targetCatId, 0);
        } else {
            children.remove(origNode);
        } // now put the origNode in the right position
        if (moveType == MOVE_INNER) {
            // add it to the end of the list
            children.add(origNode);
        } else {
            Node targetNode = null;
            int idx = 0;
            for (Node child : children) {
                // look for the target node
                if (targetNodeIsCat) {
                    if (child.getType() == Node.NODE_TYPE_CATEGORY && child.getId() == targetNodeId) {
                        targetNode = child;
                        break;
                    }
                } else {
                    if (child.getType() == Node.NODE_TYPE_QUESTION && child.getId() == targetNodeId) {
                        targetNode = child;
                        break;
                    }
                }
                idx++;
            }
            if (targetNode == null) {
                return MOVE_RC_NO_TARGET_NODE;
            } else if (moveType == MOVE_BEFORE) {
                children.add(idx, origNode);
            } else {
                children.add(idx + 1, origNode);
            }
        }
        // update the weights
        int weight = 1;
        List<Integer> cIds = new ArrayList<Integer>();
        List<Integer> cWeights = new ArrayList<Integer>();
        List<Integer> qIds = new ArrayList<Integer>();
        List<Integer> qWeights = new ArrayList<Integer>();
        for (Node child : children) {
            if (child.getType() == Node.NODE_TYPE_CATEGORY) {
                cIds.add(child.getId());
                cWeights.add(weight);
            } else {
                qIds.add(child.getId());
                qWeights.add(weight);
            }
            weight++;
        }
        if (!cIds.isEmpty()) {
            surveyCategoryDao.updateParentAndWeights(cIds, targetCatId, cWeights);
        }
        if (!qIds.isEmpty()) {
            surveyQuestionDao.updateParentAndWeights(qIds, targetCatId, qWeights);
        }
        // see whether the BSC/TSC type needs to be recomputed
        if (oldOrigNode.getParentId() != targetCatId) {
            // moved to different parent - recompute the BSC type
            logger.debug("Moved to different parent - need to recompute BSC type.");
            computeSurveyConfigType(
                    scId);
        } else {
            logger.debug("Moved within the same parent - don't need to recompute BSC type.");
        }
        return MOVE_RC_OK;
    }


    public SurveyConfig getSurveyConfigOfProduct(int productId) {
        return surveyConfigDao.selectByProductId(productId);
    }
}
