/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.model;

import com.ocs.util.ValueObject;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author seanpcheng
 */
public class IndicatorImportValidation extends ValueObject {

    private int userId = 0;
    private int defaultCreatorOrgId = -1;
    private int defaultLangId = -1;
    private int defaultState = -1;
    private int defaultVisibility = -1;
    private List<String> generalErrors = null;
    private List<IndicatorValidation> indicatorValidations = null;
    private int errorCount = 0;
    private static final Logger logger = Logger.getLogger(IndicatorImportValidation.class);

    //Setters
    public void setDefaultOrgID(int orgId) {
        this.defaultCreatorOrgId = orgId;
    }

    public void setDefaultVisibility(int visibility) {
        this.defaultVisibility = visibility;
    }

    public void setDefaultState(int state) {
        this.defaultState = state;
    }

    public void setDefaultLang(int lang) {
        this.defaultLangId = lang;
    }

    public void addGeneralError(int lineNum, String errorMsg) {
        logger.debug("Adding error: " + errorMsg);

        if (this.generalErrors == null) {
            this.generalErrors = new ArrayList<String>();
        }
        this.generalErrors.add("Line " + lineNum + ": " + errorMsg);
        errorCount++;
    }

    public void addIndicatorValidation(IndicatorValidation indicatorValidation) {
        if (this.indicatorValidations == null) {
            this.indicatorValidations = new ArrayList<IndicatorValidation>();
        }
        this.indicatorValidations.add(indicatorValidation);
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    //Getters
    public List<String> getGeneralErrors() {
        return this.generalErrors;
    }

    public List<IndicatorValidation> getIndicatorValidations() {
        return this.indicatorValidations;
    }

    public int getErrorCount() {
        return this.errorCount;
    }

    protected void incrementError() {
        errorCount++;
    }

    public int getDefaultCreatorOrgId() {
        return this.defaultCreatorOrgId;
    }

    public int getDefaultVisibility() {
        return this.defaultVisibility;
    }

    public int getDefaultState() {
        return this.defaultState;
    }

    public int getDefaultLangId() {
        return this.defaultLangId;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public JSONObject toJson() {
        JSONObject root = new JSONObject();
        root.put("errCount", errorCount);
        if (generalErrors != null && !generalErrors.isEmpty()) {
            JSONArray genErrorArr = new JSONArray();
            for (String err : generalErrors) {
                genErrorArr.add(err);
            }
            root.put("genErrors", genErrorArr);
        }
        if (indicatorValidations != null && !indicatorValidations.isEmpty()) {
            JSONArray indicatorErrorArr = new JSONArray();
            for (IndicatorValidation val : indicatorValidations) {
                if (val.getErrorMsg() != null) {
                    indicatorErrorArr.add(val.getName() + ": " + val.getErrorMsg());
                }
            }
            root.put("indicatorErrors", indicatorErrorArr);
        }
        return root;
    }
}
