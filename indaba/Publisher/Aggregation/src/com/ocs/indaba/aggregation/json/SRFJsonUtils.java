/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.json;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.vo.CategoryNode;
import com.ocs.indaba.aggregation.vo.QuestionNode;
import com.ocs.indaba.aggregation.vo.ScorecardBaseNode;
import com.ocs.indaba.aggregation.vo.ScorecardInfo;
import com.ocs.indaba.util.Tree;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author jiangjeff
 */
public class SRFJsonUtils extends JsonUtils {

    public static JSONObject scorecard2Json(ScorecardInfo scorecard) {
        JSONObject root = new JSONObject();
        // base info
        root.put(KEY_HORSE_ID, scorecard.getHorseId());
        root.put(KEY_SURVEY_CONFIG_ID, scorecard.getSurveyConfigId());
        root.put(KEY_SURVEY_NAME, formatStrVal(scorecard.getSurveyName()));
        root.put(KEY_PRODUCT_ID, scorecard.getProductId());
        root.put(KEY_PRODUCT_NAME, formatStrVal(scorecard.getProdouctName()));
        root.put(KEY_PRODUCT_DESC, formatStrVal(scorecard.getProdouctDesc()));
        root.put(KEY_TARGET_ID, scorecard.getTargetId());
        root.put(KEY_TARGET_NAME, formatStrVal(scorecard.getTargetName()));
        root.put(KEY_TARGET_SHORT_NAME, formatStrVal(scorecard.getTargetShortName()));
        root.put(KEY_STUDY_PERIOD_ID, scorecard.getStudyPeriodId());
        root.put(KEY_STUDY_PERIOD, formatStrVal(scorecard.getStudyPeriod()));
        root.put(KEY_TITLE, formatStrVal(scorecard.getTitle()));
        // additional aggregation
        root.put(KEY_MOE, scorecard.getMoe());
        // root.put(KEY_INLAW, scorecard.getInLaw());
        // root.put(KEY_INPRACTICE, scorecard.getInPractice());
        root.put(KEY_LEGAL_FRAMEWORK, scorecard.getLegalFramework());
        root.put(KEY_IMPLEMENTATION, scorecard.getImplementation());
        root.put(KEY_IMPLEMENTATION_COUNT, scorecard.getImplementationCount());
        root.put(KEY_LEGAL_FRAMEWORK_COUNT, scorecard.getLegalFrameworkCount());
        root.put(KEY_IMPLEMENTATION_GAP, scorecard.getLegalFramework() - scorecard.getImplementation());
        root.put(KEY_OVERALL, scorecard.getOverall());

        // structured categories/questions
        List<Tree<ScorecardBaseNode>> rootCategories = scorecard.getRootCategories();
        if (rootCategories != null && !rootCategories.isEmpty()) {
            JSONArray arr = new JSONArray();
            for (Tree<ScorecardBaseNode> category : rootCategories) {
                arr.add(tree2Json(category));
            }
            root.put(KEY_TREE, arr);
        }
        //root.setStreamName("");
        return root;
    }

    public static JSONObject tree2Json(Tree<ScorecardBaseNode> tree) {
        JSONObject jsonObj = node2Json(tree.getNode());
        if (tree.isLeaf()) {
            return jsonObj;
        }

        List<Tree<ScorecardBaseNode>> children = tree.getChildren();
        JSONArray categoryArr = new JSONArray();
        JSONArray questionArr = new JSONArray();
        for (Tree<ScorecardBaseNode> subTree : children) {
            if (subTree == null || subTree.getNode() == null) {
                continue;
            }
            if (subTree.getNode().getNodeType() == Constants.NODE_TYPE_CATEGORY) {
                categoryArr.add(tree2Json(subTree));
            } else {
                questionArr.add(tree2Json(subTree));
            }
        }
        if (!categoryArr.isEmpty()) {
            jsonObj.put(KEY_CATEGORIES, categoryArr);
        }
        if (!questionArr.isEmpty()) {
            jsonObj.put(KEY_QUESTIONS, questionArr);
        }

        return jsonObj;
    }

    public static JSONObject node2Json(ScorecardBaseNode node) {
        return (node.getNodeType() == Constants.NODE_TYPE_CATEGORY)
                ? categoryNode2Json((CategoryNode) node) : questionDetail2Json((QuestionNode) node);
    }

    public static JSONObject categoryNode2Json(CategoryNode category) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_NODE_TYPE, category.getNodeType());
        jsonObj.put(KEY_CATEGORY_ID, category.getId());
        jsonObj.put(KEY_NAME, formatStrVal(category.getName()));
        jsonObj.put(KEY_TITLE, formatStrVal(category.getTitle()));
        jsonObj.put(KEY_LABEL, formatStrVal(category.getLabel()));
        jsonObj.put(KEY_WEIGHT, category.getWeight());
        //jsonObj.put(KEY_MIN, category.getMin());
        //jsonObj.put(KEY_LABEL, category.getLabel());
        //jsonObj.put(KEY_MAX, category.getMax());
        jsonObj.put(KEY_MEAN, category.getMean());
        jsonObj.put(KEY_MEDIAN, category.getMedian());

        jsonObj.put(KEY_IMPLEMENTATION, category.getImplementation());
        jsonObj.put(KEY_IMPLEMENTATION_COUNT, category.getImplementationCount());
        jsonObj.put(KEY_LEGAL_FRAMEWORK, category.getLegalFramework());
        jsonObj.put(KEY_LEGAL_FRAMEWORK_COUNT, category.getLegalFrameworkCount());
        jsonObj.put(KEY_USE_SCORE, category.hasUseScore());
        jsonObj.put(KEY_USED_SCORE_COUNT, category.getUsedScoreCount());
        return jsonObj;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //
    // JSON Object ==> JAVA object
    //
    /////////////////////////////////////////////////////////////////////////////////////////////
    public static ScorecardInfo json2Scorecard(JSONObject root) {
        ScorecardInfo scorecard = new ScorecardInfo();
        // base info
        scorecard.setHorseId(getIntVal(root, KEY_HORSE_ID));
        scorecard.setSurveyConfigId(getIntVal(root, KEY_SURVEY_CONFIG_ID));
        scorecard.setSurveyName(getStringVal(root, KEY_SURVEY_NAME));
        scorecard.setProductId(getIntVal(root, KEY_PRODUCT_ID));
        scorecard.setProdouctName(getStringVal(root, KEY_PRODUCT_NAME));
        scorecard.setProdouctDesc(getStringVal(root, KEY_PRODUCT_DESC));
        scorecard.setTargetId(getIntVal(root, KEY_TARGET_ID));
        scorecard.setTargetName(getStringVal(root, KEY_TARGET_NAME));
        scorecard.setTargetShortName(getStringVal(root, KEY_TARGET_SHORT_NAME));
        scorecard.setStudyPeriodId(getIntVal(root, KEY_STUDY_PERIOD_ID));
        scorecard.setStudyPeriod(getStringVal(root, KEY_STUDY_PERIOD));
        scorecard.setTitle(getStringVal(root, KEY_TITLE));
        // additional aggreation
        scorecard.setMoe(getDoubleVal(root, KEY_MOE));
        scorecard.setLegalFramework(getDoubleVal(root, KEY_LEGAL_FRAMEWORK));
        // scorecard.setInLaw(getFloatVal(root, KEY_INLAW));
        // scorecard.setInPractice(getFloatVal(root, KEY_INPRACTICE));
        scorecard.setImplementation(getDoubleVal(root, KEY_IMPLEMENTATION));
        scorecard.setImplementationCount(getIntVal(root, KEY_IMPLEMENTATION_COUNT));
        scorecard.setLegalFrameworkCount(getIntVal(root, KEY_LEGAL_FRAMEWORK_COUNT));
        scorecard.setImplementationGap(getDoubleVal(root, KEY_IMPLEMENTATION_GAP));
        scorecard.setOverall(getDoubleVal(root, KEY_OVERALL));
        // structured categories/questions
        JSONArray treeArr = (JSONArray) root.get(KEY_TREE);
        if (treeArr != null && !treeArr.isEmpty()) {
            for (int i = 0, size = treeArr.size(); i < size; ++i) {
                JSONObject childJsonObj = (JSONObject) treeArr.get(i);
                if (getIntVal(childJsonObj, KEY_NODE_TYPE) == Constants.NODE_TYPE_CATEGORY) {
                    scorecard.addRootCategory(json2CategoryNode(childJsonObj, null));
                } else {
                    scorecard.addRootCategory(json2QuestionNode(childJsonObj, null));
                }
            }
        }

        return scorecard;
    }

    public static Tree<ScorecardBaseNode> json2CategoryNode(JSONObject jsonObj, Tree<ScorecardBaseNode> parent) {
        CategoryNode category = new CategoryNode();
        Tree<ScorecardBaseNode> categoryTree = new Tree<ScorecardBaseNode>(category, parent);
        category.setNodeType(getIntVal(jsonObj, KEY_NODE_TYPE));
        category.setId(getIntVal(jsonObj, KEY_CATEGORY_ID));
        category.setName(getStringVal(jsonObj, KEY_NAME));
        category.setTitle(getStringVal(jsonObj, KEY_TITLE));
        category.setLabel(getStringVal(jsonObj, KEY_LABEL));
        category.setWeight(getIntVal(jsonObj, KEY_WEIGHT));
        //category.setMin(getFloatVal(jsonObj, KEY_MIN));
        //category.setMax(getFloatVal(jsonObj, KEY_MAX));
        category.setMean(getDoubleVal(jsonObj, KEY_MEAN));
        category.setMedian(getDoubleVal(jsonObj, KEY_MEDIAN));
        category.setImplementation(getDoubleVal(jsonObj, KEY_IMPLEMENTATION));
        category.setImplementationCount(getIntVal(jsonObj, KEY_IMPLEMENTATION_COUNT));
        category.setLegalFramework(getDoubleVal(jsonObj, KEY_LEGAL_FRAMEWORK));
        category.setLegalFrameworkCount(getIntVal(jsonObj, KEY_LEGAL_FRAMEWORK_COUNT));

        category.setUseScore(getBoolVal(jsonObj, KEY_USE_SCORE));
        category.setUsedScoreCount(getIntVal(jsonObj, KEY_USED_SCORE_COUNT));
        JSONArray categoryArr = (JSONArray) jsonObj.get(KEY_CATEGORIES);
        if (categoryArr != null && !categoryArr.isEmpty()) {
            for (int i = 0, size = categoryArr.size(); i < size; ++i) {
                categoryTree.addChild(json2CategoryNode((JSONObject) categoryArr.get(i), categoryTree));
            }
        }
        JSONArray questionArr = (JSONArray) jsonObj.get(KEY_QUESTIONS);
        if (questionArr != null && !questionArr.isEmpty()) {
            for (int i = 0, size = questionArr.size(); i < size; ++i) {
                categoryTree.addChild(json2QuestionNode((JSONObject) questionArr.get(i), categoryTree));
            }
        }
        return categoryTree;
    }

    public static Tree<ScorecardBaseNode> json2QuestionNode(JSONObject jsonObj, Tree<ScorecardBaseNode> parent) {
        QuestionNode question = new QuestionNode();
        question.setNodeType(getIntVal(jsonObj, KEY_NODE_TYPE));
        question.setId(getIntVal(jsonObj, KEY_INDICATOR_ID));
        question.setName(getStringVal(jsonObj, KEY_NAME));
        question.setQuestionId(getIntVal(jsonObj, KEY_QUESTION_ID));
        question.setQuestionName(getStringVal(jsonObj, KEY_QUESTION_NAME));
        question.setCriteria(getStringVal(jsonObj, KEY_CRITERIA));
        question.setTip(getStringVal(jsonObj, KEY_TIP));
        question.setPublicName(getStringVal(jsonObj, KEY_PUBLIC_NAME));
        question.setQuestionType(getIntVal(jsonObj, KEY_QUESTION_TYPE));
        question.setQuestionText(getStringVal(jsonObj, KEY_QUESTION_TEXT));
        question.setCompleted(getBoolVal(jsonObj, KEY_COMPLETED));
        question.setUseScore(getBoolVal(jsonObj, KEY_USE_SCORE));
        question.setUsedScoreCount(getIntVal(jsonObj, KEY_USED_SCORE_COUNT));
        question.setAnswerId(getIntVal(jsonObj, KEY_ANSWER_ID));
        question.setStaffAuthorMsgboardId(getIntVal(jsonObj, KEY_STAFF_AUTHOR_MSG_BOARD_ID));
        question.setInternalMsgboardId(getIntVal(jsonObj, KEY_INTERNAL_MSG_BOARD_ID));
        //if (question.isCompleted()) {
        question.setChoiceId(getIntVal(jsonObj, KEY_CHOICE_ID));
        question.setChoices(getIntVal(jsonObj, KEY_CHOICES));
        question.setScore(getDoubleVal(jsonObj, KEY_SCORE));
        question.setInputValue(getStringVal(jsonObj, KEY_INPUT_VALUE));
        question.setReferenceObjectId(getIntVal(jsonObj, KEY_REFERENCE_OBJECT_ID));
        question.setReferenceName(getStringVal(jsonObj, KEY_REFERENCE_NAME));
        question.setComments(getStringVal(jsonObj, KEY_COMMENTS));
        question.setAnswerUserId(getIntVal(jsonObj, KEY_ANSWER_USER_ID));
        question.setReferences(getStringVal(jsonObj, KEY_REFERENCES));
        if (jsonObj.get(KEY_REVIEWS) != null) {
            JSONArray jsonArr = (JSONArray) jsonObj.get(KEY_REVIEWS);
            List<String> reviews = new ArrayList<String>(jsonArr.size());
            for (int i = 0, size = jsonArr.size(); i < size; ++i) {
                reviews.add((String) jsonArr.get(i));
            }
            question.setReviews(reviews);
        }
        // }
        question.setOptions(json2OptionNode((JSONArray) jsonObj.get(KEY_OPTIONS)));
        question.setAttachements(json2Attachments((JSONArray) jsonObj.get(KEY_ATTACHMEN_TLIST)));
        Tree<ScorecardBaseNode> questionNode = new Tree<ScorecardBaseNode>(question);
        questionNode.setParent(parent);
        return questionNode;
    }
}
