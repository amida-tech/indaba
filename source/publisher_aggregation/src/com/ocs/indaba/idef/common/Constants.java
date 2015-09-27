/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.common;

/**
 *
 * @author yc06x
 */
public class Constants {

    public static final short VISIBILITY_PUBLIC = 1;
    public static final short VISIBILITY_PRIVATE = 2;

    public static final short CONTENT_TYPE_SURVEY = 0;
    public static final short CONTENT_TYPE_JOURNAL = 1;

    public static final short PRODUCT_MODE_CONFIG = 0;
    public static final short PRODUCT_MODE_TEST = 1;
    public static final short PRODUCT_MODE_PROD = 2;

    public static final short INDICATOR_TYPE_SINGLE_CHOICE = 1;
    public static final short INDICATOR_TYPE_MULTI_CHOICE = 2;
    public static final short INDICATOR_TYPE_INTEGER = 3;
    public static final short INDICATOR_TYPE_FLOAT = 4;
    public static final short INDICATOR_TYPE_TEXT = 5;

    public static final short PEER_REVIEW_OPINION_AGREE = 0;
    public static final short PEER_REVIEW_OPINION_AGREE_COMMENT = 1;
    public static final short PEER_REVIEW_OPINION_DISAGREE = 2;
    public static final short PEER_REVIEW_OPINION_NOT_QUALIFIED = 3;

    public static final short REF_TYPE_NO_CHOICE = 0;
    public static final short REF_TYPE_SINGLE_CHOICE = 1;
    public static final short REF_TYPE_MULTI_CHOICE = 2;

    public static final double DOUBLE_ERROR = -99999999.99;

    public static final String FILE_PATTERN_INDICATOR = "indicator.csv";
    public static final String FILE_PATTERN_ANSWER = "answer.csv";
    public static final String FILE_PATTERN_SURVEY_QUESTION = "survey_question.csv";
    public static final String FILE_PATTERN_PROJECT = "project.csv";
    public static final String FILE_PATTERN_PRODUCT = "product.csv";
    public static final String FILE_PATTERN_SURVEY_CONFIG = "survey_config.csv";
    public static final String FILE_PATTERN_SCORECARD = "scorecard.csv";
    public static final String FILE_PATTERN_SURVEY_CATEGORY = "survey_category.csv";
    public static final String FILE_PATTERN_USER = "user.csv";
    public static final String FILE_PATTERN_REF = "reference.csv";

    public static final String FILE_NAME_PROJECT_MAPPING = "project.name.map.csv";
    public static final String FILE_NAME_PRODUCT_MAPPING = "product.name.map.csv";
    public static final String FILE_NAME_INDICATOR_MAME_RULE = "indicator.name.rule.csv";
    public static final String FILE_NAME_QUESTION_MAME_RULE = "question.name.rule.csv";
    public static final String FILE_NAME_INDICATOR_REF_MAPPING = "indicator.ref.map.csv";
    public static final String FILE_NAME_META = "meta.csv";

    public static final String META_PROP_NAME_VISIBILITY_OVERRIDE = "visibilityOverride";
    public static final String META_PROP_NAME_ORGANIZATION_OVERRIDE = "organizationOverride";
    public static final String META_PROP_NAME_LANGUAGE_OVERRIDE = "languageOverride";
    public static final String META_PROP_NAME_STUDY_PERIOD_OVERRIDE = "studyPeriodOverride";
}
