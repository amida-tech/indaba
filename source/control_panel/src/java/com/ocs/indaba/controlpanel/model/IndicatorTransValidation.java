/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.model;

import com.ocs.util.ValueObject;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author seanpcheng
 */
public class IndicatorTransValidation extends ValueObject {
    
    private List<String> generalErrors = null;
    private List<TransValidation> transValidation = null;
    
    private int errorCount = 0;
   
    //Setters
    
    public void addGeneralError(int lineNum, String errorMsg) {
        if (this.generalErrors == null) {
            this.generalErrors = new ArrayList<String>();
        }
        this.generalErrors.add("Line " + lineNum + ": " + errorMsg);
        errorCount++;
    }
    
    public void addTransValidation(TransValidation transValidation) {
        if (this.transValidation == null) {
            this.transValidation = new ArrayList<TransValidation>();
        }
        this.transValidation.add(transValidation);
    }
    
    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }
    
    //Getters
    public List<String> getGeneralErrors() {
        return this.generalErrors;
    }
    
    public List<TransValidation> getIndicatorValidations() {
        return this.transValidation;
    }
    
    public int getErrorCount() {
        return this.errorCount;
    }
    
    
    protected void incrementError() {
        errorCount++;
    }
    
    @Override
    public JSONObject toJson() {
        JSONObject root = new JSONObject();
        root.put("errCount", errorCount);
        if(generalErrors != null && !generalErrors.isEmpty()) {
            JSONArray genErrorArr = new JSONArray();
            for(String err: generalErrors) {
                genErrorArr.add(err);
            }
            root.put("genErrors", genErrorArr);
        }

        if(transValidation!= null && !transValidation.isEmpty()) {
            JSONArray indicatorErrorArr = new JSONArray();
            for(TransValidation val : transValidation) {
                if (val.getErrorMsg() != null) {
                    indicatorErrorArr.add(val.getName() + ": " + val.getErrorMsg());
                }
            }
            root.put("indicatorErrors", indicatorErrorArr);
        }
        return root;
    }
    
}
