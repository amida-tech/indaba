/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.model;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.SurveyIndicator;
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
public class IndicatorDetailVO extends ValueObject {

    private static final Logger logger = Logger.getLogger(IndicatorDetailVO.class);
    private int id; // intl suervey indiator id
    private int surveyIndicatorId; // orginal survey indicator id
    private int visibility;
    private int userId;
    private int language;
    private String name;
    private int reference;
    private int organization;
    private String orgname = null;
    private String question = null;
    private String tip = null;
    private short answerType;
    private int answerTypeId;
    private boolean writable;
    private List<Integer> itags;
    private AnswerValueSettingVO answerValueSetting = null;
    private List<AnswerChoiceSettingVO> answerChoices = null;
    private AnswerTableSettingVO answerTableSetting = null;

    public IndicatorDetailVO() {
    }

    public static IndicatorDetailVO initWithSurveyIndicator(SurveyIndicator si) {
        IndicatorDetailVO indicator = new IndicatorDetailVO();
        indicator.setId(si.getId());
        indicator.setLanguage(si.getLanguageId());
        indicator.setName(si.getName());
        indicator.setReference(si.getReferenceId());
        indicator.setOrganization(si.getOwnerOrgId());
        indicator.setQuestion(si.getQuestion());
        indicator.setTip(si.getTip());
        indicator.setAnswerType(si.getAnswerType());
        indicator.setAnswerTypeId(si.getAnswerTypeId());
        return indicator;
    }

    @Override
    public void initializeObject(Map<String, String> map)  {
        if (map == null) {
            return;
        }
        super.initializeObject(map);
        JSONParser jsonParser = new JSONParser();
        String answerListJsonStr = map.get("answerChoices");
        JSONArray arr = null;

        try {
            switch(this.getAnswerType()) {

                case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
                case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
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
                    break;

                case Constants.SURVEY_ANSWER_TYPE_TABLE:
                    AnswerTableSettingVO answerTableSetting = new AnswerTableSettingVO();
                    answerTableSetting.initializeObject((JSONObject) jsonParser.parse(map.get("answerTableSetting")));
                    this.setAnswerTableSetting(answerTableSetting);
                    break;

                default:
                    AnswerValueSettingVO answerValueSetting = new AnswerValueSettingVO();
                    answerValueSetting.initializeObject((JSONObject) jsonParser.parse(map.get("answerValueSetting")));
                    this.setAnswerValueSetting(answerValueSetting);
            }
        } catch (ParseException ex) {
            logger.error("Fail to parse answer list json string: " + answerListJsonStr, ex);
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObj = super.toJson();
        if (itags == null || itags.isEmpty()) {
            jsonObj.put("itags", null);
        } else {
            jsonObj.put("itags", itags);
        }

        switch(answerType) {
            case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
            case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
                JSONArray jsonArr = new JSONArray();
                if (answerChoices != null && !answerChoices.isEmpty()) {
                    for (AnswerChoiceSettingVO answer : answerChoices) {
                        jsonArr.add(answer.toJson());
                    }
                }
                jsonObj.put("answerChoices", jsonArr);
                break;

            case Constants.SURVEY_ANSWER_TYPE_TABLE:
                if (answerTableSetting != null) {
                    jsonObj.put("answerTableSetting", answerTableSetting.toJson());
                }
                break;

            default:
                if (answerValueSetting != null) {
                    jsonObj.put("answerValueSetting", answerValueSetting.toJson());
                }
        }
        return jsonObj;
    }

    public short getAnswerType() {
        return answerType;
    }

    public void setAnswerType(short answerType) {
        this.answerType = answerType;
    }

    public int getAnswerTypeId() {
        return answerTypeId;
    }

    public void setAnswerTypeId(int answerTypeId) {
        this.answerTypeId = answerTypeId;
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

    public void setAnswerChoices(List<AnswerChoiceSettingVO> choices) {
        this.answerChoices = choices;
    }

    public AnswerValueSettingVO getAnswerValueSetting() {
        return answerValueSetting;
    }

    public void setAnswerValueSetting(AnswerValueSettingVO answerValueSetting) {
        this.answerValueSetting = answerValueSetting;
    }


    public AnswerTableSettingVO getAnswerTableSetting() {
        return answerTableSetting;
    }

    public void setAnswerTableSetting(AnswerTableSettingVO answerTableSetting) {
        this.answerTableSetting = answerTableSetting;
    }


    public int getSurveyIndicatorId() {
        return surveyIndicatorId;
    }

    public void setSurveyIndicatorId(int surveyIndicatorId) {
        this.surveyIndicatorId = surveyIndicatorId;
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

    public int getOrganization() {
        return organization;
    }

    public void setOrganization(int organization) {
        this.organization = organization;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getReference() {
        return reference;
    }

    public void setReference(int reference) {
        this.reference = reference;
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

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public boolean isWritable() {
        return writable;
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    @Override
    public String toString() {
        return "IndicatorDetailVO{" + "id=" + id + ", language=" + language + ", name=" + name + ", reference=" + reference + ", organization=" + organization + ", question=" + question + ", tip=" + tip + ", answerType=" + answerType + ", tags=" + itags + ", answerChoices=" + answerChoices + ", answerValueSetting=" + answerValueSetting + '}';
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }
}
