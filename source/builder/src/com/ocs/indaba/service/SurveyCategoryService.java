/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.ContentHeaderDAO;
import com.ocs.indaba.dao.ProductDAO;
import com.ocs.indaba.dao.SurveyAnswerDAO;
import com.ocs.indaba.dao.SurveyCategoryDAO;
import com.ocs.indaba.dao.SurveyPeerReviewDAO;
import com.ocs.indaba.dao.SurveyQuestionDAO;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.Product;
import com.ocs.indaba.po.SurveyAnswer;
import com.ocs.indaba.po.SurveyCategory;
import com.ocs.indaba.po.SurveyPeerReview;
import com.ocs.indaba.po.SurveyQuestion;
import com.ocs.indaba.survey.tree.Node;
import com.ocs.indaba.survey.tree.SurveyTree;
import com.ocs.indaba.vo.SurveyAnswerUrlView;
import com.ocs.indaba.vo.SurveyCategoryTree;
import com.ocs.indaba.vo.SurveyQuestionUrlView;
import com.ocs.indaba.vo.SurveyQuestionVO;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class SurveyCategoryService {

    private static final Logger log = Logger.getLogger(SurveyCategoryService.class);
    private SurveyAnswerDAO surveyAnswerDao = null;
    private SurveyQuestionDAO surveyQuestionDao = null;
    private ContentHeaderDAO contentHeaderDao = null;
    private SurveyCategoryDAO surveyCategoryDao = null;
    private SurveyPeerReviewDAO surveyPeerReviewDao;
    private ProductDAO productDao = null;
    private SurveyConfigService surveyConfigService = null;

    public float getSurveyQuestionCompletedPercentageByHorseId(int horseId) {
        int allQuestionCount = surveyQuestionDao.selectSurveyQuestionCountByHorseId(horseId);
        int completedAnswerCount = surveyAnswerDao.selectCompletedSurveyAnswerCountByHorseId(horseId);

        return (allQuestionCount > 0) ? ((float) completedAnswerCount / allQuestionCount) : 0;
    }

    public int getSurveyQuestionCountByHorseId(int horseId) {
        int result = surveyQuestionDao.selectSurveyQuestionCountByHorseId(horseId);
        log.debug("Number of questions: " + result);
        return result;
    }

    public int getCompletedSurveyAnswerCountByHorseId(int horseId) {
        return surveyAnswerDao.selectCompletedSurveyAnswerCountByHorseId(horseId);
    }

    public int getCompletedEditedSurveyAnswerCountByHorseId(int horseId) {
        return surveyAnswerDao.selectCompletedEditedSurveyAnswerCountByHorseId(horseId);
    }

    public int getCompletedOverallReviewedSurveyAnswerCountByHorseId(int horseId) {
        return surveyAnswerDao.selectCompletedOverallReviewedSurveyAnswerCountByHorseId(horseId);
    }

    public int getCompletedSurveyPeerReviewCountByHorseIdAndReviewerId(int horseId, int uid) {
        return surveyAnswerDao.getPeerReviewedAnswerCountByHorseIdAndReviewerId(horseId, uid);
    }

    public int getRespondedProblemCountByHorseId(int horseId) {
        return surveyAnswerDao.getRespondedProblemCountByHorseId(horseId);
    }

    public int getProblemCountByHorseId(int horseId) {
        return surveyAnswerDao.getProblemCountByHorseId(horseId);
    }

    public boolean checkCompletedAllSurveyIndicators(String type, int horseId, int userId) {

        int completedCount = 0;
        int questionsCount = -1;

        if (Constants.SURVEY_ACTION_FLAGRESPONSE.equals(type) ||
            Constants.SURVEY_ACTION_FLAGRESOLVE.equals(type)) {
            // never complete assignment!
            return false;
        }
        if (Constants.SURVEY_ACTION_CREATE.equals(type)) {
            completedCount = this.getCompletedSurveyAnswerCountByHorseId(horseId);
        } else if (Constants.SURVEY_ACTION_EDIT.equals(type)) {
            completedCount = this.getCompletedEditedSurveyAnswerCountByHorseId(horseId);
        } else if (Constants.SURVEY_ACTION_OVERALLREVIEW.equals(type)) {
            completedCount = Integer.MAX_VALUE;
        } else if (Constants.SURVEY_ACTION_PRREVIEW.equals(type)) {
            completedCount = this.getCompletedSurveyPeerReviewCountByHorseIdAndReviewerId(horseId, userId);
        } else if (Constants.SURVEY_ACTION_REVIEW.equals(type)) {
            completedCount = Integer.MAX_VALUE;
        } else if (Constants.SURVEY_ACTION_PEERREVIEW.equals(type)) {
            completedCount = this.getCompletedSurveyPeerReviewCountByHorseIdAndReviewerId(horseId, userId);
        } else if (Constants.SURVEY_ACTION_REVIEWRESPONSE.equals(type)) {
            completedCount = this.getRespondedProblemCountByHorseId(horseId);
            questionsCount = this.getProblemCountByHorseId(horseId);
        } else {
            completedCount = Integer.MAX_VALUE;
        }

        if (questionsCount < 0) {
            questionsCount = getSurveyQuestionCountByHorseId(horseId);
        }
        return (questionsCount <= completedCount);
    }

    public List<SurveyCategoryTree> buildSurveyCategoryTrees(int horseId, int taskType, int userid) {
        ContentHeader cntHdr = contentHeaderDao.selectContentHeaderByHorseId(horseId);
        if (cntHdr == null) {
            return null;
        }
        List<SurveyCategory> surveyCategories = surveyCategoryDao.selectRootSurveyCategoriesByCntObjId(cntHdr.getContentObjectId());
        if (surveyCategories == null || surveyCategories.isEmpty()) {
            return null;
        }
        List<SurveyCategoryTree> trees = new ArrayList<SurveyCategoryTree>(surveyCategories.size());
        for (SurveyCategory sc : surveyCategories) {
            SurveyCategoryTree aTree = buildSurveyCategoryTree(cntHdr.getContentObjectId(), sc.getId(), taskType, userid);
            if (aTree != null) {
                trees.add(aTree);
            }
        }
        return trees;
    }

    public List<SurveyAnswer> getAllSurveyAnswers(int horseId) {
        ContentHeader cntHdr = contentHeaderDao.selectContentHeaderByHorseId(horseId);
        if (cntHdr == null) {
            return null;
        }
        List<SurveyCategory> surveyCategories = surveyCategoryDao.selectRootSurveyCategoriesByCntObjId(cntHdr.getContentObjectId());
        if (surveyCategories == null || surveyCategories.isEmpty()) {
            return null;
        }

        List<SurveyAnswer> surveyAnswers = new ArrayList<SurveyAnswer>();
        for (SurveyCategory category : surveyCategories) {
            getAllSurveyAnswers(surveyAnswers, cntHdr.getContentObjectId(), category.getId());
        }
        return surveyAnswers;
    }

    private List<SurveyAnswer> getAllSurveyAnswers(List<SurveyAnswer> surveyAnswers, int cntObjId, int surveyCategoryId) {
        List<SurveyCategory> surveyCategories = surveyCategoryDao.selectSubSurveyCategories(surveyCategoryId);
        if (surveyCategories == null || surveyCategories.isEmpty()) {
            List<Integer> questionIds = surveyQuestionDao.selectSurveyQuestionIdsBy(surveyCategoryId);
            if (questionIds == null) {
                return surveyAnswers;
            }
            for (int questionId : questionIds) {
                SurveyAnswer answer = surveyAnswerDao.selectSurveyAnswerBy(cntObjId, questionId);
                if (answer != null) {
                    surveyAnswers.add(answer);
                }
            }
        }

        if (surveyCategories != null) {
            for (SurveyCategory category : surveyCategories) {
                getAllSurveyAnswers(surveyAnswers, cntObjId, category.getId());
            }
        }
        return surveyAnswers;
    }

    public SurveyCategoryTree buildSurveyCategoryTree(int cntObjId, int surveyCategoryId, int taskType, int userid) {
        SurveyCategory surveyCategoy = surveyCategoryDao.selectSurveyCategoryById(surveyCategoryId);
        if (surveyCategoy == null) {
            return null;
        }
        SurveyCategoryTree root = new SurveyCategoryTree();
        root.setCurNode(surveyCategoy);
        buildSubSurveyCategoryTree(cntObjId, surveyCategoy.getId(), root, taskType, userid);
        return root;
    }

    public SurveyCategoryTree buildSubSurveyCategoryTree(int cntObjId, int parentId,
            SurveyCategoryTree parent, int taskType, int userid) {
        if (taskType == -1) {
            taskType = Constants.TASK_TYPE_SURVEY_CREATE;
        }

        List<SurveyCategory> surveyCategories = surveyCategoryDao.selectSubSurveyCategories(parentId);
        if (surveyCategories == null || surveyCategories.isEmpty()) {
            parent.setLeafNodes(surveyQuestionDao.selectSurveyQuestionsBy(parentId));
            List<SurveyQuestionVO> leafNodes = parent.getLeafNodes();
            if (leafNodes != null) {
                for (SurveyQuestionVO questionVo : leafNodes) {
                    SurveyAnswer answer = surveyAnswerDao.selectSurveyAnswerBy(cntObjId, questionVo.getQuestion().getId());
                    questionVo.setAnswer(answer);
                    boolean completed = (answer != null && answer.getAnswerObjectId() > 0);
                    SurveyPeerReview surveyPeerReview = null;
                    int reviewOption = Constants.SURVEY_REVIEW_OPTION_NONE;
                    switch (taskType) {
                        case Constants.TASK_TYPE_SURVEY_EDIT:
                            completed = completed && answer.getEdited();
                            break;
                        case Constants.TASK_TYPE_SURVEY_PEER_REVIEW:
                            if (answer != null) {
                                surveyPeerReview = surveyPeerReviewDao.getCompletedSurveyPeerReviewsBySurveyAnswerIdAndReviewerId(answer.getId(), userid);
                            }
                            completed = completed && (surveyPeerReview != null);
                            break;
                        case Constants.TASK_TYPE_SURVEY_REVIEW:
                            completed = completed && answer.getStaffReviewed();
                            break;
                        case Constants.TASK_TYPE_SURVEY_PR_REVIEW:
                            completed = completed && answer.getPrReviewed();
                            List<SurveyPeerReview> surveyPeerReviews = null;
                            if (answer != null) {
                                surveyPeerReviews = surveyPeerReviewDao.getSurveyPeerReviewsBySurveyAnswerId(answer.getId());
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

                                    // reviewOption = (opt > reviewOption) ? opt : reviewOption;
                                }
                            }
                            break;
                        case Constants.TASK_TYPE_SURVEY_OVERALL_REVIEW:
                            completed = completed && answer.getOverallReviewed();
                            break;
//                        case Constants.TASK_TYPE_SURVEY_REVIEW_RESPONSE:
//                            completed = completed && !answer.getReviewerHasProblem();
//                            break;
                        case Constants.TASK_TYPE_SURVEY_CREATE:
                        default:
                            break;
                    }
                    questionVo.setCompleted(completed);
                    if (completed) {
                        parent.incrementCompleted();
                    } else {
                        parent.incrementIncompleted();
                    }
                    //
                    // Fix bug #739
                    if (taskType == Constants.TASK_TYPE_SURVEY_PR_REVIEW) {
                        questionVo.setReviewOption(reviewOption);
                        switch (reviewOption) {
                            case Constants.SURVEY_REVIEW_OPTION_AGREE:
                                parent.setAgreed(parent.getAgreed() + 1);
                                break;
                            case Constants.SURVEY_REVIEW_OPTION_NO_QUALIFICATION:
                                parent.setNoQualified(parent.getNoQualified() + 1);
                                break;
                            case Constants.SURVEY_REVIEW_OPTION_AGREE_BUT_CLARIFICATION:
                                parent.setNeedClarified(parent.getNeedClarified() + 1);
                                break;
                            case Constants.SURVEY_REVIEW_OPTION_DISAGREE:
                                parent.setDisagreed(parent.getDisagreed() + 1);
                                break;
                            case Constants.SURVEY_REVIEW_OPTION_NONE:
                            default:
                                parent.setUnreviewed(parent.getUnreviewed() + 1);
                                break;
                        }
                    }
                }
            }
            return parent;
        }
        for (SurveyCategory category : surveyCategories) {
            SurveyCategoryTree child = new SurveyCategoryTree();
            child.setParent(parent);
            child.setCurNode(category);
            parent.addChild(child);
            buildSubSurveyCategoryTree(cntObjId, category.getId(), child, taskType, userid);
            parent.addCompleted(child.getCompleted());
            parent.addIncompleted(child.getIncompleted());

            //
            // Fix bug #739
            if (taskType == Constants.TASK_TYPE_SURVEY_PR_REVIEW) {
                parent.setAgreed(child.getAgreed() + parent.getAgreed());
                parent.setNoQualified(child.getNoQualified() + parent.getNoQualified());
                parent.setNeedClarified(child.getNeedClarified() + parent.getNeedClarified());
                parent.setDisagreed(child.getDisagreed() + parent.getDisagreed());
                parent.setUnreviewed(child.getUnreviewed() + parent.getUnreviewed());

            }
        }
        return parent;
    }

    @Autowired
    public void setSurveyAnswerDao(SurveyAnswerDAO surveyAnswerDao) {
        this.surveyAnswerDao = surveyAnswerDao;
    }

    @Autowired
    public void setSurveyQuestionDao(SurveyQuestionDAO surveyQuestionDao) {
        this.surveyQuestionDao = surveyQuestionDao;
    }

    @Autowired
    public void setContentHeaderDao(ContentHeaderDAO contentHeaderDao) {
        this.contentHeaderDao = contentHeaderDao;
    }

    @Autowired
    public void setSurveyCategoryDAO(SurveyCategoryDAO surveyCategoryDao) {
        this.surveyCategoryDao = surveyCategoryDao;
    }

    @Autowired
    public void setSurveyPeerReviewDAO(SurveyPeerReviewDAO surveyPeerReviewDao) {
        this.surveyPeerReviewDao = surveyPeerReviewDao;
    }

    @Autowired
    public void setProductDao(ProductDAO productDao) {
        this.productDao = productDao;
    }

    @Autowired
    public void setSurveyConfigSuvice(SurveyConfigService surveyConfigSurvice) {
        this.surveyConfigService = surveyConfigSurvice;
    }

    //get all the surveyAnswers in the same category
    private List<SurveyAnswer> getAllSurveyAnswerInCategory(int surveyAnswerId, int horseId) {
        ContentHeader cntHdr = contentHeaderDao.selectContentHeaderByHorseId(horseId);
        if (cntHdr == null) {
            return null;
        }
        SurveyAnswer surveyAnswer = surveyAnswerDao.get(surveyAnswerId);
        if (surveyAnswer == null) {
            return null;
        }
        SurveyQuestion question = surveyQuestionDao.get(surveyAnswer.getSurveyQuestionId());
        return surveyAnswerDao.getSurveyAnswerByCategoryIdAndContentId(cntHdr.getContentObjectId(), question.getSurveyCategoryId());
    }

    //get all the surveyQuestions in the same category
    private List<SurveyQuestion> getAllSurveyQuestionInCategory(int surveyQuestionId) {
        SurveyQuestion question = surveyQuestionDao.get(surveyQuestionId);
        if (question == null) {
            return null;
        }

        return surveyQuestionDao.getSurveyQuestionBySurveyCategoryId(question.getSurveyCategoryId());
    }

    //get all the surveyCategory of a horseId
    private List<SurveyCategory> getAllSurveyCategory(int horseId) {
        ContentHeader cntHdr = contentHeaderDao.selectContentHeaderByHorseId(horseId);
        if (cntHdr == null) {
            return null;
        }
        List<SurveyCategory> surveyCategories = surveyCategoryDao.selectRootSurveyCategoriesByCntObjId(cntHdr.getContentObjectId());
        if (surveyCategories == null || surveyCategories.isEmpty()) {
            return null;
        }

        List<SurveyCategory> surveyCategoryList = new ArrayList<SurveyCategory>();
        for (SurveyCategory category : surveyCategories) {
            getAllSurveyCategory(category, surveyCategoryList);
        }
        return surveyCategoryList;
    }

    private void getAllSurveyCategory(SurveyCategory surveyCategory, List<SurveyCategory> surveyCategoryList) {
        List<SurveyCategory> surveyCategories = surveyCategoryDao.selectSubSurveyCategories(surveyCategory.getId());
        if (surveyCategories == null || surveyCategories.isEmpty()) {
            surveyCategoryList.add(surveyCategory);
            return;
        }
        for (SurveyCategory category : surveyCategories) {
            getAllSurveyCategory(category, surveyCategoryList);
        }
    }

    public SurveyAnswerUrlView getSurveyAnswerUrlView(int surveyAnswerId, int horseId) {
        SurveyAnswerUrlView view = new SurveyAnswerUrlView();
        List<SurveyAnswer> surveyAnswerList = this.getAllSurveyAnswerInCategory(surveyAnswerId, horseId);
        if (surveyAnswerList == null || surveyAnswerList.isEmpty()) {
            view.setPreviousId(0);
            view.setNextId(0);
        } else {
            int num = 0;
            view.setPreviousId(0);
            view.setNextId(0);
            boolean match = false;
            for (; num < surveyAnswerList.size(); num++) {
                if (surveyAnswerList.get(num).getId() == surveyAnswerId) {
                    match = true;
                    break;
                }
            }
            if (match) {
                if (num != 0) {
                    view.setPreviousId(surveyAnswerList.get(num - 1).getId());
                }
                if (num != (surveyAnswerList.size() - 1)) {
                    view.setNextId(surveyAnswerList.get(num + 1).getId());
                }
            }
        }

        SurveyAnswer answer = surveyAnswerDao.get(surveyAnswerId);
        if (answer == null) {
            view.setUpId(0);
            view.setDownId(0);
            return view;
        }
        ContentHeader cntHdr = contentHeaderDao.selectContentHeaderByHorseId(horseId);
        if (cntHdr == null) {
            view.setUpId(0);
            view.setDownId(0);
            return view;
        }
        int surveyCategoryId = surveyQuestionDao.get(answer.getSurveyQuestionId()).getSurveyCategoryId();
        List<SurveyCategory> surveyCategoryList = this.getAllSurveyCategory(horseId);
        if (surveyCategoryList == null || surveyCategoryList.isEmpty()) {
            view.setUpId(0);
            view.setDownId(0);
        } else {
            int num = 0;
            view.setUpId(0);
            view.setDownId(0);
            boolean match = false;
            for (; num < surveyCategoryList.size(); num++) {
                if (surveyCategoryList.get(num).getId() == surveyCategoryId) {
                    match = true;
                    break;
                }
            }
            if (match) {
                if (num != 0) {
                    int previousCategoryId = surveyCategoryList.get(num - 1).getId();
                    SurveyAnswer first = surveyAnswerDao.getFirstSurveyAnswer(cntHdr.getContentObjectId(), previousCategoryId);
                    if (first != null) {
                        view.setUpId(first.getId());
                    }
                }
                if (num != (surveyCategoryList.size() - 1)) {
                    int nextCategoryId = surveyCategoryList.get(num + 1).getId();
                    SurveyAnswer first = surveyAnswerDao.getFirstSurveyAnswer(cntHdr.getContentObjectId(), nextCategoryId);
                    if (first != null) {
                        view.setDownId(first.getId());
                    }
                }
            }
        }
        return view;
    }

    public SurveyQuestionUrlView getSurveyQuestionUrlView(int surveyAnswerId, int horseId) {
        SurveyAnswer surveyAnswer = surveyAnswerDao.get(surveyAnswerId);
        if (surveyAnswer == null) {
            return null;
        }

        SurveyQuestionUrlView view = new SurveyQuestionUrlView();
        Product product = productDao.selectProductByHorseId(horseId);
        if (product == null) {
            log.error("Product isn't existed for horse[id=" + horseId + "].");
            return null;
        }
        int scId = product.getProductConfigId();
        SurveyTree surveyTree = surveyConfigService.buildTree(scId);
        surveyTree.listNodes();
        Node nextNode = surveyTree.findNextQuestion(surveyAnswer.getSurveyQuestionId());
        Node preNode = surveyTree.findPrevQuestion(surveyAnswer.getSurveyQuestionId());
        view.setNextId((nextNode != null) ? nextNode.getId() : 0);
        view.setPreviousId((preNode != null) ? preNode.getId() : 0);

        Node firstNodeOfNextCat = surveyTree.findFirstQuestionOfNextCategory(surveyAnswer.getSurveyQuestionId());
        Node firstNodeOfPreCat = surveyTree.findFirstQuestionOfPrevCategory(surveyAnswer.getSurveyQuestionId());
        view.setDownId((firstNodeOfNextCat != null) ? firstNodeOfNextCat.getId() : 0);
        view.setUpId((firstNodeOfPreCat != null) ? firstNodeOfPreCat.getId() : 0);
        /*
        int surveyQuestionId = surveyAnswer.getSurveyQuestionId();
        List<SurveyQuestion> surveQuestionList = this.getAllSurveyQuestionInCategory(surveyQuestionId);
        if (surveQuestionList == null || surveQuestionList.isEmpty()) {
        view.setPreviousId(0);
        view.setNextId(0);
        } else {
        int num = 0;
        view.setPreviousId(0);
        view.setNextId(0);
        boolean match = false;
        for (; num < surveQuestionList.size(); num++) {
        if (surveQuestionList.get(num).getId() == surveyQuestionId) {
        match = true;
        break;
        }
        }
        if (match) {
        if (num != 0) {
        view.setPreviousId(surveQuestionList.get(num - 1).getId());
        }
        if (num != (surveQuestionList.size() - 1)) {
        view.setNextId(surveQuestionList.get(num + 1).getId());
        }
        }
        }

        int surveyCategoryId = surveyQuestionDao.get(surveyAnswer.getSurveyQuestionId()).getSurveyCategoryId();
        List<SurveyCategory> surveyCategoryList = this.getAllSurveyCategory(horseId);
        if (surveyCategoryList == null || surveyCategoryList.isEmpty()) {
        view.setUpId(0);
        view.setDownId(0);
        } else {
        int num = 0;
        view.setUpId(0);
        view.setDownId(0);
        boolean match = false;
        for (; num < surveyCategoryList.size(); num++) {
        if (surveyCategoryList.get(num).getId() == surveyCategoryId) {
        match = true;
        break;
        }
        }
        if (match) {
        if (num != 0) {
        int previousCategoryId = surveyCategoryList.get(num - 1).getId();
        SurveyQuestion first = surveyQuestionDao.getFirstSurveyQuestionBySurveyCategoryId(previousCategoryId);
        if (first != null) {
        view.setUpId(first.getId());
        }
        if (view.getPreviousId() == 0) {
        SurveyQuestion last = surveyQuestionDao.getLastSurveyQuestionBySurveyCategoryId(previousCategoryId);
        if (last != null) {
        view.setPreviousId(last.getId());
        }
        }
        }
        if (num != (surveyCategoryList.size() - 1)) {
        int nextCategoryId = surveyCategoryList.get(num + 1).getId();
        SurveyQuestion first = surveyQuestionDao.getFirstSurveyQuestionBySurveyCategoryId(nextCategoryId);
        if (first != null) {
        view.setDownId(first.getId());
        if (view.getNextId() == 0) {
        view.setNextId(first.getId());
        }
        }
        }
        }
        }
         */
        return view;
    }
}
