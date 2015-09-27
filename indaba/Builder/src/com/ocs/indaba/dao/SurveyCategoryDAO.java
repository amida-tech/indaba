/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.SurveyCategory;
import com.ocs.indaba.util.ListUtils;
import com.ocs.util.StringUtils;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff Jiang
 */
public class SurveyCategoryDAO extends SmartDaoMySqlImpl<SurveyCategory, Integer> {

    private static final Logger log = Logger.getLogger(SurveyCategoryDAO.class);
    private static final String SELECT_ROOT_SURVEY_CATEGORIES = "SELECT * FROM survey_category WHERE parent_category_id='' OR parent_category_id IS NULL ORDER BY weight";
    private static final String SELECT_SUB_SURVEY_CATEGORIES = "SELECT * FROM survey_category WHERE parent_category_id=? ORDER BY weight";
    private static final String SELECT_ROOT_SURVEY_CATEGORIES_BY_CONTENT_OBJECT_ID = "SELECT sc.* FROM survey_content_object sco, survey_category sc "
            + "WHERE sco.id=? AND sco.survey_config_id=sc.survey_config_id AND (sc.parent_category_id IS NULL OR sc.parent_category_id=0) ORDER BY sc.weight";
    private static final String SELECT_ROOT_SURVEY_CATEGORIES_BY_HORSE_ID = "SELECT sc.* FROM survey_content_object sco, survey_category sc, content_header ch "
            + "WHERE ch.horse_id=? AND ch.content_type=0 AND sco.id=ch.content_object_id AND sco.survey_config_id=sc.survey_config_id AND (sc.parent_category_id IS NULL OR sc.parent_category_id=0) ORDER BY sc.weight";
    private static final String SELECT_ROOT_SURVEY_CATEGORIES_BY_PRODUCT_ID = "SELECT scat.* FROM product prd, survey_config sc, survey_category scat "
            + "WHERE prd.id=? AND sc.id=prd.product_config_id AND scat.survey_config_id=sc.id AND (scat.parent_category_id=0 OR scat.parent_category_id IS NULL) ORDER BY scat.weight";
    private static final String SELECT_SURVEY_CATEGORY_BY_CONFIG_ID =
            "SELECT * FROM survey_category WHERE survey_config_id=? ORDER BY parent_category_id, weight";
    private static final String SELECT_MAX_WEIGHT_BY_CONFIGID_AND_PARENT_CATID =
            "SELECT MAX(weight) FROM survey_category WHERE survey_config_id=? AND parent_category_id=?";
    private static final String DELETE_SURVEY_CATEGORY_BY_CONFIGID_AND_PARENT_CATEGORYID =
            "DELETE FROM survey_category WHERE survey_config_id=? AND parent_category_id=?";
    private static final String DELETE_SURVEY_CATEGORY_BY_CONFIGID =
            "DELETE FROM survey_category WHERE survey_config_id=?";
    private static final String INCREASE_WEIGHT_OF_SURVEY_CATEGOIES_BY_CONFIGID_AND_CATEGORYID =
            "UPDATE survey_category SET weight=weight+1 WHERE weight>? AND survey_config_id=? AND parent_category_id=?";
    private static final String DECREASE_WEIGHT_OF_SURVEY_CATEGOIES_BY_CONFIGID_AND_CATEGORYID =
            "UPDATE survey_category SET weight=weight-1 WHERE weight>? AND survey_config_id=? AND parent_category_id=?";
    private static final String IS_LAST_SURVEY_CATEGOY_BY_CONFIGID_AND_CATEGORYID =
            "SELECT COUNT(1) FROM survey_category WHERE id=? AND "
            + "weight=(SELECT MAX(weight) FROM survey_category WHERE survey_config_id=? AND parent_category_id=?)";
    private static final String IS_FIRST_SURVEY_CATEGOY_BY_CONFIGID_AND_CATEGORYID =
            "SELECT COUNT(1) FROM survey_category WHERE id=? AND "
            + "weight=(SELECT MIN(weight) FROM survey_category WHERE survey_config_id=? AND parent_category_id=?)";

    public boolean isLastSurveyCategoryByConfigIdAndCategoryId(int catId, int surveyConfigId, int surveyCategoryId) {
        return super.count(IS_LAST_SURVEY_CATEGOY_BY_CONFIGID_AND_CATEGORYID, catId, surveyConfigId, surveyCategoryId) > 0;
    }

    public boolean isFirstSurveyCategoryByConfigIdAndCategoryId(int catId, int surveyConfigId, int surveyCategoryId) {
        return super.count(IS_FIRST_SURVEY_CATEGOY_BY_CONFIGID_AND_CATEGORYID, catId, surveyConfigId, surveyCategoryId) > 0;
    }

    public int decreaseWeightOfSurveyCategoriesByConfigId(int weight, int surveyConfigId, int pCatId) {
        logger.debug("Decrease weight[weight=" + weight + ", confId=" + surveyConfigId + ", pCatId=" + "]:\n\t" + DECREASE_WEIGHT_OF_SURVEY_CATEGOIES_BY_CONFIGID_AND_CATEGORYID);
        return super.update(DECREASE_WEIGHT_OF_SURVEY_CATEGOIES_BY_CONFIGID_AND_CATEGORYID, weight, surveyConfigId, pCatId);
    }

    public int increaseWeightOfSurveyCategoriesByConfigId(int weight, int surveyConfigId, int pCatId) {
        logger.debug("Increase weight[weight=" + weight + ", confId=" + surveyConfigId + ", pCatId=" + "]:\n\t" + INCREASE_WEIGHT_OF_SURVEY_CATEGOIES_BY_CONFIGID_AND_CATEGORYID);
        return super.update(INCREASE_WEIGHT_OF_SURVEY_CATEGOIES_BY_CONFIGID_AND_CATEGORYID, weight, surveyConfigId, pCatId);
    }

    public void deleteByConfigIdAndParentCategoryId(int surveyConfigId, int parentCategoryId) {
        super.delete(DELETE_SURVEY_CATEGORY_BY_CONFIGID_AND_PARENT_CATEGORYID, surveyConfigId, parentCategoryId);
    }

    public void deleteByConfigId(int surveyConfigId) {
        super.delete(DELETE_SURVEY_CATEGORY_BY_CONFIGID, surveyConfigId);
    }

    public int selectMaxWeight(int surveyConfigId, int surveyCategoryId) {
        return (int) super.count(SELECT_MAX_WEIGHT_BY_CONFIGID_AND_PARENT_CATID, surveyConfigId, surveyCategoryId);
    }

    public List<SurveyCategory> selectSurveyCategorysByConfigId(int surveyConfigId) {
        return super.find(SELECT_SURVEY_CATEGORY_BY_CONFIG_ID, surveyConfigId);
    }

    public List<SurveyCategory> selectSurveyCategories() {
        log.debug("Select all survey categories.");

        return super.findAll();
    }

    public List<SurveyCategory> selectRootSurveyCategoriesByCntObjId(int cntObjId) {
        log.debug("Select all root survey categories by content header id: " + cntObjId);

        return super.find(SELECT_ROOT_SURVEY_CATEGORIES_BY_CONTENT_OBJECT_ID, cntObjId);
    }

    public List<SurveyCategory> selectRootSurveyCategoriesByHorseId(int horseId) {
        log.debug("Select all root survey categories by horse id: " + horseId);

        return super.find(SELECT_ROOT_SURVEY_CATEGORIES_BY_HORSE_ID, horseId);
    }

    public List<SurveyCategory> selectRootSurveyCategoriesByProductId(int productId) {
        log.debug("Select all root survey categories by product id: " + productId);

        return super.find(SELECT_ROOT_SURVEY_CATEGORIES_BY_PRODUCT_ID, productId);
    }

    public List<SurveyCategory> selectRootSurveyCategories() {
        log.debug("Select root survey categories.");

        return super.find(SELECT_ROOT_SURVEY_CATEGORIES);
    }

    public List<SurveyCategory> selectSubSurveyCategories(int parentId) {
        log.debug("Select root survey categories.");

        return super.find(SELECT_SUB_SURVEY_CATEGORIES, parentId);
    }

    public SurveyCategory selectSurveyCategoryById(int id) {
        log.debug("Select survey category by id: " + id);

        return super.get(id);
    }

    
    public void updateParentAndWeights(List<Integer> catIds, int parentId, List<Integer> weights) {
        if (catIds == null || catIds.isEmpty()) return;

        String baseSql = "UPDATE survey_category SET parent_category_id = ?, weight = CASE id {0} END "
                + "WHERE id in ({1})";

        StringBuilder wsb = new StringBuilder();

        for (int i = 0; i < catIds.size(); i++) {
            wsb.append("WHEN " + catIds.get(i) + " THEN " + weights.get(i) + " ");
        }

        String sql = MessageFormat.format(baseSql, wsb.toString(), ListUtils.listToString(catIds));
        logger.debug("SQL: " + sql);

        super.update(sql, parentId);
    }

    private static final String DELETE_CATEGORIES_BY_CONFIGID =
            "DELETE FROM survey_category WHERE survey_config_id=? AND id IN ({0})";

    public void deleteCategories(int scId, List<Integer> catIds) {
        if (catIds == null || catIds.isEmpty()) return;

        String catIdsString = StringUtils.list2Str(catIds);

        logger.debug("Delete categories of survey-config " + scId + ": " + catIdsString);

        String sql = MessageFormat.format(DELETE_CATEGORIES_BY_CONFIGID, catIdsString);
        super.delete(sql, scId);
    }

}
