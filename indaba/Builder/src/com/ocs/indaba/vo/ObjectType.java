/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
*/

package com.ocs.indaba.vo;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author menglong
 */
public enum ObjectType {
    USER((short)0, "User"),
    CONTENT((short)1, "Content");

    Short typeCode;
    String typeDesc;

    ObjectType(Short code, String description){
        this.typeCode = code;
        this.typeDesc = description;
    }

    public Short getTypeCode() {
        return typeCode;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    private static final Map<Short, ObjectType> codeToEnum = new HashMap<Short, ObjectType>();
    private static final Map<String, ObjectType> descToEnum = new HashMap<String, ObjectType>();

    static {
        for (ObjectType objectType : values()) {
        	codeToEnum.put(objectType.typeCode, objectType);
        }
    }

    static {
        for (ObjectType objectType : values()) {
        	descToEnum.put(objectType.typeDesc, objectType);
        }
    }

    public static ObjectType fromObjectTypeCode(Short code){
        return codeToEnum.get(code);
    }

    public static ObjectType fromObjectTypeDesc(String description){
        return descToEnum.get(description);
    }

}
