/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.model;

import com.ocs.indaba.common.Constants;
import com.ocs.util.ValueObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Jeff Jiang
 */
public class IndicatorI18nVO extends ValueObject {

    private static final Logger logger = Logger.getLogger(IndicatorI18nVO.class);
    private int id;
    private int language;
    private String name;
    private String question = null;
    private String tip = null;
    private int answerType;
    private List<Integer> itags;
    private List<AnswerChoiceSettingVO> answerChoices = null;

    public IndicatorI18nVO() {
    }

    @Override
    public void initializeObject(Map<String, String> map) {
        if (map == null) {
            return;
        }
        super.initializeObject(map);
        JSONParser jsonParser = new JSONParser();
        // String tags = map.get("tags[]");
        String answerListJsonStr = map.get("answerChoices");

        try {
            JSONArray arr = null;
            if (Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE == this.getAnswerType()
                    || Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE == this.getAnswerType()) {
                if (StringUtils.isEmpty(answerListJsonStr)) {
                    return;
                }
                arr = (JSONArray) jsonParser.parse(answerListJsonStr);
                if (arr != null && !arr.isEmpty()) {
                    for (int i = 0, size = arr.size(); i < size; ++i) {
                        AnswerChoiceSettingVO choice = new AnswerChoiceSettingVO();
                        choice.initializeObject((JSONObject) arr.get(i));
                        this.addAnswerChoice(choice);
                    }
                }
            }
        } catch (ParseException ex) {
            logger.error("Fail to parse answer list json string: " + answerListJsonStr, ex);
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObj = super.toJson();
        if (itags == null || itags.isEmpty()) {
            jsonObj.put("tags", null);
        } else {
            jsonObj.put("tags", itags);
        }
        if (answerType == Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE
                || answerType == Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE) {
            JSONArray jsonArr = new JSONArray();
            if (answerChoices != null && !answerChoices.isEmpty()) {
                for (AnswerChoiceSettingVO answer : answerChoices) {
                    jsonArr.add(answer.toJson());
                }
            }
            jsonObj.put("answerChoices", jsonArr);
        }
        return jsonObj;
    }

    public static IndicatorI18nVO initWithIndicatorDetailVO(IndicatorDetailVO indicator) {
        IndicatorI18nVO indicatorI18n = new IndicatorI18nVO();
        indicatorI18n.setAnswerType(indicator.getAnswerType());
        indicatorI18n.setAnswerChoices(indicator.getAnswerChoices());
        indicatorI18n.setId(indicator.getId());
        indicatorI18n.setLanguage(indicator.getLanguage());
        indicatorI18n.setName(indicator.getName());
        indicatorI18n.setQuestion(indicator.getQuestion());
        indicatorI18n.setItags(indicator.getItags());
        indicatorI18n.setTip(indicator.getTip());
        return indicatorI18n;
    }

    public int getAnswerType() {
        return answerType;
    }

    public void setAnswerType(int answerType) {
        this.answerType = answerType;
    }

    public List<AnswerChoiceSettingVO> getAnswerChoices() {
        return answerChoices;
    }

    public void addAnswerChoice(AnswerChoiceSettingVO choice) {
        if (answerChoices == null) {
            answerChoices = new ArrayList<AnswerChoiceSettingVO>();
        }
        answerChoices.add(choice);
    }

    public void setAnswerChoices(List<AnswerChoiceSettingVO> answerChoices) {
        this.answerChoices = answerChoices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Integer> getItags() {
        return itags;
    }

    public void addItag(int itag) {
        if (this.itags == null) {
            itags = new ArrayList<Integer>();
        }
        itags.add(itag);
    }

    public void setItags(Integer[] itags) {
        if (itags != null) {
            this.itags = Arrays.asList(itags);
        }
    }

    public void setItags(List<Integer> itags) {
        this.itags = itags;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
