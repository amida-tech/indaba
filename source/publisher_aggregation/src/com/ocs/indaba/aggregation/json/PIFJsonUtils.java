/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.json;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.vo.*;
import com.ocs.indaba.util.Tree;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author jiangjeff
 */
public class PIFJsonUtils extends JsonUtils {

    public static JSONObject product2Json(ProductInfo prodInfo) {
        JSONObject root = new JSONObject();
        // base info
        root.put(KEY_SURVEY_CONFIG_ID, prodInfo.getSurveyConfigId());
        root.put(KEY_SURVEY_NAME, prodInfo.getSurveyName());
        root.put(KEY_PRODUCT_ID, prodInfo.getProductId());
        root.put(KEY_PRODUCT_NAME, prodInfo.getProdouctName());
        root.put(KEY_PRODUCT_DESC, prodInfo.getProdouctDesc());
        root.put(KEY_STUDY_PERIOD_ID, prodInfo.getStudyPeriodId());
        root.put(KEY_STUDY_PERIOD, prodInfo.getStudyPeriod());
        // add all horses
        List<HorseVO> horses = prodInfo.getHorses();
        if (horses != null && !horses.isEmpty()) {
            JSONArray jsonArr = new JSONArray();
            for (HorseVO h : horses) {
                jsonArr.add(horse2Json(h));
            }
            root.put(KEY_HORSES, jsonArr);
        }

        // structured categories/indicators
        List<Tree<ScorecardBaseNode>> rootCategories = prodInfo.getRootCategories();
        if (rootCategories != null && !rootCategories.isEmpty()) {
            JSONArray arr = new JSONArray();
            for (Tree<ScorecardBaseNode> category : rootCategories) {
                arr.add(tree2Json(category));
            }
            root.put(KEY_TREE, arr);
        }

        return root;
    }

    private static JSONObject horse2Json(HorseVO horse) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_HORSE_ID, horse.getHorseId());
        jsonObj.put(KEY_TARGET_ID, horse.getTargetId());
        jsonObj.put(KEY_TARGET_NAME, horse.getTargetName());
        jsonObj.put(KEY_DONE, horse.isDone());
        return jsonObj;
    }

    protected static JSONObject tree2Json(Tree<ScorecardBaseNode> tree) {
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

    private static JSONObject node2Json(ScorecardBaseNode node) {
        if (node.getNodeType() == Constants.NODE_TYPE_CATEGORY) {
            return category2Json((CategoryNode) node);
        } else {
            return question2Json((QuestionNode) node);
        }
    }

    private static JSONObject category2Json(CategoryNode category) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_NODE_TYPE, category.getNodeType());
        jsonObj.put(KEY_CATEGORY_ID, category.getId());
        jsonObj.put(KEY_TITLE, category.getTitle());
        jsonObj.put(KEY_NAME, category.getName());
        jsonObj.put(KEY_LABEL, category.getLabel());
        jsonObj.put(KEY_WEIGHT, category.getWeight());
        jsonObj.put(KEY_LABEL, category.getLabel());
        jsonObj.put(KEY_MEAN, category.getMean());
        jsonObj.put(KEY_MEDIAN, category.getMedian());
        jsonObj.put(KEY_USE_SCORE, category.hasUseScore());
        jsonObj.put(KEY_USED_SCORE_COUNT, category.getUsedScoreCount());
        return jsonObj;
    }

    public static JSONObject question2Json(QuestionNode question) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY_NODE_TYPE, question.getNodeType());
        jsonObj.put(KEY_CATEGORY_ID, question.getId());
        jsonObj.put(KEY_NAME, question.getName());
        jsonObj.put(KEY_PUBLIC_NAME, question.getPublicName());
        jsonObj.put(KEY_CRITERIA, question.getCriteria());
        jsonObj.put(KEY_TIP, question.getTip());
        jsonObj.put(KEY_QUESTION_ID, question.getQuestionId());
        jsonObj.put(KEY_QUESTION_NAME, question.getQuestionName());
        jsonObj.put(KEY_QUESTION_TEXT, question.getQuestionText());
        jsonObj.put(KEY_QUESTION_TYPE, question.getQuestionType());
        jsonObj.put(KEY_SCORE, question.getScore());
        jsonObj.put(KEY_OPTIONS, options2Json(question.getChoiceId(), question.getOptions()));
        jsonObj.put(KEY_USE_SCORE, question.hasUseScore());
        return jsonObj;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //
    // JSON Object ==> JAVA object
    //
    /////////////////////////////////////////////////////////////////////////////////////////////
    public static ProductInfo json2Product(JSONObject root) {
        ProductInfo product = new ProductInfo();
        // base info
        product.setSurveyConfigId(JsonUtils.getIntVal(root, KEY_SURVEY_CONFIG_ID));
        product.setSurveyName(JsonUtils.getStringVal(root, KEY_SURVEY_NAME));
        product.setProductId(JsonUtils.getIntVal(root, KEY_PRODUCT_ID));
        product.setProdouctName(JsonUtils.getStringVal(root, KEY_PRODUCT_NAME));
        product.setProdouctDesc(JsonUtils.getStringVal(root, KEY_PRODUCT_DESC));
        product.setStudyPeriodId(JsonUtils.getIntVal(root, KEY_STUDY_PERIOD_ID));
        product.setStudyPeriod(JsonUtils.getStringVal(root, KEY_STUDY_PERIOD));
        Object obj = root.get(KEY_HORSES);
        if(obj != null) {
            product.setHorses(json2Horses((JSONArray)obj));
        }
        
        // structured categories/indicators
        JSONArray treeArr = (JSONArray) root.get(KEY_TREE);
        if (treeArr != null && !treeArr.isEmpty()) {
            for (int i = 0, size = treeArr.size(); i < size; ++i) {
                JSONObject childJsonObj = (JSONObject) treeArr.get(i);
                if (JsonUtils.getIntVal(childJsonObj, KEY_NODE_TYPE) == Constants.NODE_TYPE_CATEGORY) {
                    product.addRootCategory(json2CategoryNode(childJsonObj, null));
                }
            }
        }

        return product;
    }

    private static List<HorseVO> json2Horses(JSONArray jsonArr) {
        if (jsonArr == null || jsonArr.isEmpty()) {
            return null;
        }
        List<HorseVO> horses = new ArrayList<HorseVO>();
        
        for (int i = 0, size = jsonArr.size(); i < size; ++i) {
            JSONObject jsonObj = (JSONObject)jsonArr.get(i);
            HorseVO horse = new HorseVO();
            horse.setHorseId(JsonUtils.getIntVal(jsonObj, KEY_HORSE_ID));
            horse.setTargetId(JsonUtils.getIntVal(jsonObj, KEY_TARGET_ID));
            horse.setTargetName(JsonUtils.getStringVal(jsonObj, KEY_TARGET_NAME));
            horse.setDone(JsonUtils.getBoolVal(jsonObj, KEY_DONE));
            horses.add(horse);
        }
        
        return horses;
    }

    private static Tree<ScorecardBaseNode> json2CategoryNode(JSONObject jsonObj, Tree<ScorecardBaseNode> parent) {
        CategoryNode category = new CategoryNode();
        Tree<ScorecardBaseNode> categoryTree = new Tree<ScorecardBaseNode>(category, parent);
        category.setNodeType(JsonUtils.getIntVal(jsonObj, KEY_NODE_TYPE));
        category.setId(JsonUtils.getIntVal(jsonObj, KEY_CATEGORY_ID));
        category.setName(JsonUtils.getStringVal(jsonObj, KEY_NAME));
        category.setTitle(JsonUtils.getStringVal(jsonObj, KEY_TITLE));
        category.setLabel(JsonUtils.getStringVal(jsonObj, KEY_LABEL));
        category.setWeight(JsonUtils.getIntVal(jsonObj, KEY_WEIGHT));
        category.setMean(JsonUtils.getDoubleVal(jsonObj, KEY_MEAN));
        category.setMedian(JsonUtils.getDoubleVal(jsonObj, KEY_MEDIAN));
        
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
        question.setPublicName(getStringVal(jsonObj, KEY_PUBLIC_NAME));
        question.setQuestionType(getIntVal(jsonObj, KEY_QUESTION_TYPE));
        question.setQuestionText(getStringVal(jsonObj, KEY_QUESTION_TEXT));
        question.setUseScore(getBoolVal(jsonObj, KEY_USE_SCORE));
        question.setUsedScoreCount(getIntVal(jsonObj, KEY_USED_SCORE_COUNT));
        question.setCriteria(getStringVal(jsonObj, KEY_CRITERIA));
        question.setTip(getStringVal(jsonObj, KEY_TIP));

        question.setOptions(json2OptionNode((JSONArray) jsonObj.get(KEY_OPTIONS)));
        Tree<ScorecardBaseNode> questionNode = new Tree<ScorecardBaseNode>(question);
        questionNode.setParent(parent);
        return questionNode;
    }
}
