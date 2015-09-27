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
public enum CasePriority {
    LOW((short)0, "Low", "jsp.queues.low"),
    MIDDLE((short)1, "Medium", "jsp.queues.medium"),
    HIGH((short)2, "High", "jsp.queues.high");

    Short priorityCode;
    String priorityDesc;
    String transKey;

    CasePriority(Short code, String description, String transKey){
        this.priorityCode = code;
        this.priorityDesc = description;
        this.transKey = transKey;
    }

    public Short getPriorityCode() {
        return priorityCode;
    }

    public String getPriorityDesc() {
        return priorityDesc;
    }

    public String getTransKey() {
        return this.transKey;
    }

    private static final Map<Short, CasePriority> codeToEnum = new HashMap<Short, CasePriority>();
    private static final Map<String, CasePriority> descToEnum = new HashMap<String, CasePriority>();

    static {
        for (CasePriority casePriority : values()) {
        	codeToEnum.put(casePriority.priorityCode, casePriority);
        }
    }

    static {
        for (CasePriority casePriority : values()) {
        	descToEnum.put(casePriority.priorityDesc, casePriority);
        }
    }

    public static CasePriority fromCasePriorityCode(Short code){
        return codeToEnum.get(code);
    }

    public static CasePriority fromCasePriorityDesc(String description){
        return descToEnum.get(description);
    }

    public static List<CasePriority> getAllPriority() {
        List<CasePriority> list = new ArrayList<CasePriority>();

        for (CasePriority casePriority : CasePriority.values()) {
            list.add(casePriority);
        }

        return list;
    }

}
