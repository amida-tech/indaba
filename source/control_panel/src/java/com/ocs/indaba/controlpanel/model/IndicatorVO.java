/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.model;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.util.ValueObject;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;

/**
 *
 * @author Jeff Jiang
 */
public class IndicatorVO extends ValueObject {

    private int id;
    private String name = null; // indicator 'name' in `survey_indicator` table
    private String question = null;
    private int orgId = 0;
    private int typeId = 0;
    private String orgName = null;
    private String typeName = null;
    private int libId = 0;
    private int langId = 0;
    private String libName = null;
    private boolean moveable = false;
    private boolean deleteable = false;
    private boolean editable = false;
    private boolean translateable = false;

    public IndicatorVO() {
    }

    public static IndicatorVO initWithSurveyIndicator(SurveyIndicator si) {
        IndicatorVO indicator = new IndicatorVO();
        indicator.setId(si.getId());
        indicator.setName(si.getName());
        indicator.setQuestion(si.getQuestion());
        indicator.setTypeId(si.getAnswerType());
        indicator.setOrgId(si.getOwnerOrgId());
        indicator.setLangId(si.getLanguageId());

        int libId = 0;

        if (si.getVisibility() == Constants.VISIBILITY_PRIVATE) {
            libId = Constants.INDICATOR_LIB_VISIBILITY_PRIVATE;
        } else {
            switch (si.getState()) {
                case Constants.RESOURCE_STATE_ENDORSED:
                    libId = Constants.INDICATOR_LIB_VISIBILITY_PUBLIC_ENDORSED;
                    break;

                case Constants.RESOURCE_STATE_EXTENDED:
                    libId = Constants.INDICATOR_LIB_VISIBILITY_PUBLIC_EXTENDED;
                    break;

                default:
                    libId = Constants.INDICATOR_LIB_VISIBILITY_PUBLIC_TEST;
                    break;
            }
        }

        indicator.setLibId(libId);

        return indicator;
    }

    public static List<IndicatorVO> initWithSurveyIndicators(List<SurveyIndicator> siList) {
        List<IndicatorVO> list = new ArrayList<IndicatorVO>();
        if (siList != null && !siList.isEmpty()) {
            for (SurveyIndicator si : siList) {
                list.add(IndicatorVO.initWithSurveyIndicator(si));
            }
        }
        return list;
    }

    public int getLibId() {
        return libId;
    }

    public void setLibId(int libId) {
        this.libId = libId;
    }

    public String getLibName() {
        return libName;
    }

    public void setLibName(String libName) {
        this.libName = libName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean isEditable() {
        return this.editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isDeleteable() {
        return this.deleteable;
    }

    public void setDeleteable(boolean deleteable) {
        this.deleteable = deleteable;
    }

    public boolean isMoveable() {
        return this.moveable;
    }

    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }

    public boolean isTranslateable() {
        return this.translateable;
    }

    public void setTranslateable(boolean translateable) {
        this.translateable = translateable;
    }

    public int getLangId() {
        return langId;
    }

    public void setLangId(int langId) {
        this.langId = langId;
    }

    public static JSONArray toJson(List<IndicatorVO> indicators) {
        JSONArray jsonArr = new JSONArray();
        if (indicators != null) {
            for (IndicatorVO item : indicators) {
                jsonArr.add(item.toJson());
            }
        }
        return jsonArr;
    }

    @Override
    public String toString() {
        return "IndicatorVO{" + "id=" + id + ", name=" + name + ", lib=" + libId + ", question=" + question + '}';
    }
}
