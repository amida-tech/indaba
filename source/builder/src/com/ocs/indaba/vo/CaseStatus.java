/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
*/

package com.ocs.indaba.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author menglong
 */
public enum CaseStatus {
    OPENNEW((short)0, "Open", "common.label.open"),
    CLOSE((short)1, "Closed", "common.label.closed");

    Short statusCode;
    String statusDesc;
    String transKey;

    CaseStatus(Short code, String description, String transKey){
        this.statusCode = code;
        this.statusDesc = description;
        this.transKey = transKey;
    }

    public Short getStatusCode() {
        return statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public String getTransKey() {
        return this.transKey;
    }

    private static final Map<Short, CaseStatus> codeToEnum = new HashMap<Short, CaseStatus>();
    private static final Map<String, CaseStatus> descToEnum = new HashMap<String, CaseStatus>();

    static {
        for (CaseStatus caseStatus : values()) {
        	codeToEnum.put(caseStatus.statusCode, caseStatus);
        }
    }

    static {
        for (CaseStatus caseStatus : values()) {
        	descToEnum.put(caseStatus.statusDesc, caseStatus);
        }
    }

    public static CaseStatus fromCaseStatusCode(Short code){
        return codeToEnum.get(code);
    }

    public static CaseStatus fromCaseStatusDesc(String description){
        return descToEnum.get(description);
    }

    public static List<CaseStatus> getAllStatus() {
        List<CaseStatus> list = new ArrayList<CaseStatus>();

        for (CaseStatus caseStatus : CaseStatus.values()) {
            list.add(caseStatus);
        }

        return list;
    }

}
