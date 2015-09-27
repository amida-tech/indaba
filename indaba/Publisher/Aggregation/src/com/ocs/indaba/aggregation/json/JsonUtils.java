/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.json;

import com.ocs.indaba.aggregation.vo.QuestionNode;
import com.ocs.indaba.aggregation.vo.QuestionOption;
import com.ocs.indaba.po.Attachment;
import com.ocs.indaba.util.LabelUtils;
import com.ocs.util.StringUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff
 */
public class JsonUtils {

    private static final Logger log = Logger.getLogger(JsonUtils.class);
    protected final static String KEY_AGGRS = "aggrs";
    protected final static String KEY_ATTACH_ID = "id";
    protected final static String KEY_ATTACHMEN_TLIST = "attachmentList";
    protected final static String KEY_ATTACH_FILEPATH = "filepath";
    protected final static String KEY_ATTACH_NAME = "name";
    protected final static String KEY_ATTACH_TYPE = "type";
    protected final static String KEY_ATTACH_SIZE = "size";
    protected final static String KEY_ATTACH_URL = "url";
    protected final static String KEY_CATEGORIES = "categories";
    protected final static String KEY_CATEGORY_ID = "categoryId";
    protected final static String KEY_CHART_NAME = "chartName";
    protected final static String KEY_CHARTS = "charts";
    protected final static String KEY_CHART_TITLE = "chartTitle";
    protected final static String KEY_CHARTS_TITLE = "chartsTitle";
    protected final static String KEY_CHOICES = "choices";
    protected final static String KEY_CHOICE_ID = "choiceId";
    protected final static String KEY_COMMENTS = "comments";
    protected final static String KEY_ANSWER_USER_ID = "answerUserId";
    protected final static String KEY_COMPLETED = "completed";
    protected final static String KEY_CRITERIA = "criteria";
    protected final static String KEY_DATA = "data";
    protected final static String KEY_DONE = "done";
    protected final static String KEY_EOS = "eos";
    protected final static String KEY_EXPORT_DATE = "exportDate";
    protected final static String KEY_GAP = "gap";
    protected final static String KEY_GAP_LABEL = "gapLabel";
    protected final static String KEY_HINT = "hint";
    protected final static String KEY_HORSE_ID = "horseId";
    protected final static String KEY_HORSES = "horses";
    protected final static String KEY_ID = "id";
    protected final static String KEY_INPUT_VALUE = "inputValue";
    protected final static String KEY_IMPLEMENTATION = "implementation";
    protected final static String KEY_IMPLEMENTATION_COUNT = "implementationCount";
    protected final static String KEY_IMPLEMENTATION_GAP = "implementationGap";
    protected final static String KEY_INDICATOR_ID = "indicatorId";
    protected final static String KEY_INLAW = "inLaw";
    protected final static String KEY_INPRACTICE = "inPractice";
    protected final static String KEY_LEGAL_FRAMEWORK = "legalFramework";
    protected final static String KEY_LEGAL_FRAMEWORK_COUNT = "legalFrameworkCount";
    protected final static String KEY_LABEL = "label";
    protected final static String KEY_MASK = "mask";
    protected final static String KEY_MAX = "max";
    protected final static String KEY_MEAN = "mean";
    protected final static String KEY_MEDIAN = "median";
    protected final static String KEY_MIN = "min";
    protected final static String KEY_MOE = "moe";
    protected final static String KEY_NAME = "name";
    protected final static String KEY_NODE_TYPE = "nodeType";
    protected final static String KEY_OPTIONS = "options";
    protected final static String KEY_OVERALL = "overall";
    protected final static String KEY_OVERALL_LABEL = "overallLabel";
    protected final static String KEY_ORGANIZATION_NAME = "organizationName";
    protected final static String KEY_PRODUCT = "product";
    protected final static String KEY_PRODUCT_ID = "productId";
    protected final static String KEY_PRODUCT_NAME = "productName";
    protected final static String KEY_PROJECTT_NAME = "projectName";
    protected final static String KEY_PUBLIC_NAME = "publicName";
    protected final static String KEY_QUESTION_ID = "questionId";
    protected final static String KEY_QUESTION_LABEL = "questionLabel";
    protected final static String KEY_QUESTION_NAME = "questionName";
    protected final static String KEY_QUESTIONS = "questions";
    protected final static String KEY_QUESTION_TEXT = "questionText";
    protected final static String KEY_QUESTION_TYPE = "questionType";
    protected final static String KEY_QUESTION_SET_NAME = "questionSetName";
    protected final static String KEY_QUESTION_SETS = "questionSets";
    protected final static String KEY_REFERENCE_OBJECT_ID = "refObjId";
    protected final static String KEY_REFERENCE_NAME = "referenceName";
    protected final static String KEY_REFERENCES = "references";
    protected final static String KEY_REVIEWS = "reviews";
    protected final static String KEY_ANSWER_ID = "answerId";
    protected final static String KEY_INTERNAL_MSG_BOARD_ID = "iMsgBrdId";
    protected final static String KEY_STAFF_AUTHOR_MSG_BOARD_ID = "saMsgBrdId";
    protected final static String KEY_SELECTED = "selected";
    protected final static String KEY_SCORE = "score";
    protected final static String KEY_SCORE_LABEL = "scoreLabel";
    protected final static String KEY_SCORE_RANGE_DEFINITION = "scoreRangeDefArr";
    protected final static String KEY_SCORE_VALUE = "scoreValue";
    protected final static String KEY_STUDY_PERIOD = "studyPeroid";
    protected final static String KEY_STUDY_PERIOD_ID = "studyPeriodId";
    protected final static String KEY_SUBCAT_NAME = "subcatName";
    protected final static String KEY_SUBCATS = "subcats";
    protected final static String KEY_SURVEY_CONFIG_ID = "surveyConfigId";
    protected final static String KEY_SURVEY_NAME = "surveyName";
    protected final static String KEY_PRODUCT_DESC = "productDesc";
    protected final static String KEY_TARGET = "target";
    protected final static String KEY_TARGET_ID = "targetId";
    protected final static String KEY_TARGET_INDEX = "targetIndex";
    protected final static String KEY_TARGET_NAME = "targetName";
    protected final static String KEY_TARGET_SHORT_NAME = "targetShortName";
    protected final static String KEY_TARGET_NAME_LIST = "targetNameList";
    protected final static String KEY_TARGETS = "targets";
    protected final static String KEY_TIP = "tip";
    protected final static String KEY_TITLE = "title";
    protected final static String KEY_TREE = "tree";
    protected final static String KEY_USE_SCORE = "useScore";
    protected final static String KEY_USED_SCORE_COUNT = "usedScoreCount";
    protected final static String KEY_VALUE = "value";
    protected final static String KEY_VALUES = "values";
    protected final static String KEY_VERSION = "version";
    protected final static String KEY_WEIGHT = "weight";

    public static JSONObject questionDetail2Json(QuestionNode question/*
             * , boolean includeNonChoiceAnswers
             */) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_NODE_TYPE, question.getNodeType());
        jsonObj.put(KEY_NAME, formatStrVal(question.getName()));
        jsonObj.put(KEY_INDICATOR_ID, question.getId());
        jsonObj.put(KEY_QUESTION_ID, question.getQuestionId());
        jsonObj.put(KEY_QUESTION_TYPE, question.getQuestionType());
        jsonObj.put(KEY_QUESTION_NAME, formatStrVal(question.getQuestionName()));
        jsonObj.put(KEY_PUBLIC_NAME, formatStrVal(question.getPublicName()));
        jsonObj.put(KEY_TIP, question.getTip());
        jsonObj.put(KEY_CRITERIA, question.getCriteria());
        jsonObj.put(KEY_QUESTION_TEXT, formatStrVal(question.getQuestionText()));
        jsonObj.put(KEY_COMPLETED, question.isCompleted());
        jsonObj.put(KEY_USE_SCORE, question.hasUseScore());
        jsonObj.put(KEY_ANSWER_ID, question.getAnswerId());
        jsonObj.put(KEY_INTERNAL_MSG_BOARD_ID, question.getInternalMsgboardId());
        jsonObj.put(KEY_STAFF_AUTHOR_MSG_BOARD_ID, question.getStaffAuthorMsgboardId());
        //if (question.isCompleted()) {
        jsonObj.put(KEY_CHOICE_ID, question.getChoiceId());
        jsonObj.put(KEY_CHOICES, question.getChoices());
        jsonObj.put(KEY_SCORE, question.getScore());
        jsonObj.put(KEY_INPUT_VALUE, question.getInputValue());
        jsonObj.put(KEY_COMMENTS, formatStrVal(question.getComments()));
        jsonObj.put(KEY_ANSWER_USER_ID, question.getAnswerUserId());
        jsonObj.put(KEY_REFERENCE_OBJECT_ID, question.getReferenceObjectId());
        //jsonObj.put(KEY_REVIEWS, question.getReviews());
        jsonObj.put(KEY_REFERENCES, question.getReferenceObjectId());
        jsonObj.put(KEY_REFERENCE_NAME, formatStrVal(question.getReferenceName()));
        jsonObj.put(KEY_REFERENCES, formatStrVal(question.getReferences()));
        jsonObj.put(KEY_OPTIONS, options2Json(question.getChoiceId(), question.getOptions()));
        JSONArray jsonArr = new JSONArray();
        List<String> reviews = question.getReviews();
        if (reviews != null && !reviews.isEmpty()) {
            for (String rev : reviews) {
                jsonArr.add(formatStrVal(rev));
            }
        }
        jsonObj.put(KEY_REVIEWS, jsonArr);
        // }

        jsonObj.put(KEY_ATTACHMEN_TLIST, attachments2Json(question.getAttachements()));
        return jsonObj;
    }

    public static JSONObject question2Json(QuestionNode question) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_QUESTION_ID, question.getQuestionId());
        jsonObj.put(KEY_QUESTION_LABEL, question.getPublicName());
        jsonObj.put(KEY_QUESTION_NAME, question.getQuestionName());
        jsonObj.put(KEY_QUESTION_TEXT, question.getQuestionText());
        jsonObj.put(KEY_SCORE_VALUE, question.getScore());
        jsonObj.put(KEY_SCORE_LABEL, LabelUtils.getScoreLabel(question.getScore()));
        jsonObj.put(KEY_OPTIONS, options2Json(question.getChoiceId(), question.getOptions()));
        jsonObj.put(KEY_USE_SCORE, question.hasUseScore());
        return jsonObj;
    }

    public static JSONArray options2Json(int choice, List<QuestionOption> options) {
        JSONArray arr = new JSONArray();
        if (options != null && !options.isEmpty()) {
            for (QuestionOption opt : options) {
                JSONObject obj = new JSONObject();
                obj.put(KEY_CHOICE_ID, opt.getId());
                obj.put(KEY_LABEL, JsonUtils.formatStrVal(opt.getLabel()));
                obj.put(KEY_CRITERIA, JsonUtils.formatStrVal(opt.getCriteria()));
                obj.put(KEY_SCORE, opt.getScore());
                obj.put(KEY_MASK, opt.getMask());
                obj.put(KEY_USE_SCORE, opt.hasUseScore());
                obj.put(KEY_SELECTED, (opt.getId() == choice));
                obj.put(KEY_HINT, StringUtils.isEmpty(opt.getHint()) ? "" : opt.getHint());
                arr.add(obj);
            }
        }
        return arr;
    }

    public static JSONArray attachments2Json(List<Attachment> attachments) {
        JSONArray arr = new JSONArray();
        if (attachments == null) {
            return arr;
        }
        for (Attachment attached : attachments) {
            JSONObject obj = new JSONObject();
            obj.put(KEY_ATTACH_ID, attached.getId());
            obj.put(KEY_ATTACH_FILEPATH, attached.getFilePath());
            obj.put(KEY_ATTACH_NAME, attached.getName());
            obj.put(KEY_ATTACH_SIZE, attached.getSize());
            obj.put(KEY_ATTACH_TYPE, attached.getType());
            arr.add(obj);
        }
        return arr;
    }

    public static List<Attachment> json2Attachments(JSONArray jsonArr) {
        if (jsonArr == null) {
            return null;
        }
        List<Attachment> attachments = new ArrayList<Attachment>();
        for (int i = 0, size = jsonArr.size(); i < size; ++i) {
            Attachment attached = new Attachment();
            JSONObject jsonObj = (JSONObject) jsonArr.get(i);
            attached.setId(getIntVal(jsonObj, KEY_ATTACH_ID));
            attached.setFilePath(getStringVal(jsonObj, KEY_ATTACH_FILEPATH));
            attached.setName(getStringVal(jsonObj, KEY_ATTACH_NAME));
            attached.setSize(getIntVal(jsonObj, KEY_ATTACH_SIZE));
            attached.setType(getStringVal(jsonObj, KEY_ATTACH_TYPE));
            attachments.add(attached);
        }
        return attachments;
    }

    /**
     * Add a element to a sorted list and return the corresponding index in the
     * list.
     *
     * @param list
     * @param val
     * @return
     */
    public static int addToSortedList(List<Double> list, double val) {
        Collections.sort(list);
        int index = 0;
        for (int size = list.size(); index < size; ++index) {
            if (val < list.get(index)) {
                break;
            }
        }
        list.add(index, val);
        //log.debug("[index]: " + index + ", [size]: " + list.size()  + ", [val]: " + val);
        return index;
    }

    public static List<QuestionOption> json2OptionNode(JSONArray arr) {
        if (arr == null || arr.isEmpty()) {
            return null;
        }
        List<QuestionOption> options = new ArrayList<QuestionOption>(arr.size());
        for (int i = 0, size = arr.size(); i < size; ++i) {
            JSONObject jsonObj = (JSONObject) arr.get(i);
            options.add(new QuestionOption(getIntVal(jsonObj, KEY_CHOICE_ID), getStringVal(jsonObj, KEY_LABEL),
                    getStringVal(jsonObj, KEY_CRITERIA), getDoubleVal(jsonObj, KEY_SCORE),
                    getBoolVal(jsonObj, KEY_USE_SCORE), getStringVal(jsonObj, KEY_HINT), getIntVal(jsonObj, KEY_MASK)));
        }
        return options;
    }

    public static String formatStrVal(String s) {
        return (s == null) ? "" : s;
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    public static boolean getBoolVal(JSONObject jsonObj, String key) {
        return getBoolVal(jsonObj, key, false);
    }

    public static boolean getBoolVal(JSONObject jsonObj, String key, boolean defaultVal) {
        boolean val = defaultVal;
        Object o = jsonObj.get(key);
        if (o != null) {
            try {
                val = ((Boolean) o).booleanValue();
            } catch (Exception ex) {
            }
        }
        return val;
    }

    public static short getShortVal(JSONObject jsonObj, String key) {
        return getShortVal(jsonObj, key, (short) 0);
    }

    public static short getShortVal(JSONObject jsonObj, String key, short defaultVal) {
        short val = defaultVal;
        Object o = jsonObj.get(key);
        if (o != null) {
            try {
                val = ((Long) o).shortValue();
            } catch (Exception ex) {
            }
        }
        return val;
    }

    public static int getIntVal(JSONObject jsonObj, String key) {
        return getIntVal(jsonObj, key, 0);
    }

    public static int getIntVal(JSONObject jsonObj, String key, int defaultVal) {
        int val = defaultVal;
        Object o = jsonObj.get(key);
        if (o != null) {
            try {
                val = ((Long) o).intValue();
            } catch (Exception ex) {
            }
        }
        return val;
    }

    public static double getDoubleVal(JSONObject jsonObj, String key) {
        return getDoubleVal(jsonObj, key, 0);
    }

    public static double getDoubleVal(JSONObject jsonObj, String key, double defaultVal) {
        double val = defaultVal;
        Object o = jsonObj.get(key);
        if (o != null) {
            try {
                val = ((Double) o).doubleValue();
            } catch (Exception ex) {
            }
        }
        return val;
    }

    public static double getFloatVal(JSONObject jsonObj, String key) {
        return getDoubleVal(jsonObj, key, 0);
    }

    public static double getFloatVal(JSONObject jsonObj, String key, double defaultVal) {
        double val = defaultVal;
        Object o = jsonObj.get(key);
        if (o != null) {
            try {
                val = ((Double) o).floatValue();
            } catch (Exception ex) {
            }
        }
        return val;
    }

    public static String getStringVal(JSONObject jsonObj, String key) {
        return getStringVal(jsonObj, key, null);
    }

    public static String getStringVal(JSONObject jsonObj, String key, String defaultVal) {
        String val = defaultVal;
        Object o = jsonObj.get(key);
        if (o != null) {
            try {
                val = String.valueOf(o);
            } catch (Exception ex) {
            }
        }
        return val;
    }
}
