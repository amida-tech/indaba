/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.util;

import com.ocs.common.Constants;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author jiangjeff
 */
public class JSONUtils {

    private static final Logger logger = Logger.getLogger(JSONUtils.class);

    public static JSONArray listToJson(List list) {
        JSONArray arr = new JSONArray();
        if (list != null && !list.isEmpty()) {
            for (Object obj : list) {
                arr.add(toJson(obj));
            }
        }
        return arr;
    }

    public static JSONObject toJson(Object obj) {
        JSONObject jsonObj = new JSONObject();
        return toJson(jsonObj, obj, obj.getClass());
    }


    public static String toJsonString(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Inclusion.NON_NULL);
        
        try {
            String result = mapper.writeValueAsString(obj);
            return result;
        } catch (Exception ex) {
            return null;
        }
    }


    private static JSONObject toJson(JSONObject jsonObj, Object obj, Class clazz) {
        if (obj == null) {
            return null;
        }
        //Class clazz = obj.getClass();

        //handle parents' fields firstly
        Class superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            toJson(jsonObj, obj, superClazz);
        }

        return extractFieldsToJson(jsonObj, obj, clazz, clazz.getDeclaredFields());
    }

    private static JSONObject extractFieldsToJson(JSONObject jsonObj, Object instance, Class clazz, Field[] fields) {
        if (fields == null) {
            return jsonObj;
        }
        for (Field f : fields) {
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            String fieldName = f.getName();
            String fieldType = f.getType().getName();
            Method readMethod = null;
            try {
                if ("boolean".equals(fieldType) || "java.lang.Boolean".equals(fieldType)) {
                    try {
                        readMethod = clazz.getMethod("is" + StringUtils.capitalize(fieldName));
                    } catch (Exception ex) {
                        readMethod = clazz.getMethod("get" + StringUtils.capitalize(fieldName));
                    }
                } else if ("java.lang.String".equals(fieldType)
                        || ("int".equals(fieldType) || "java.lang.Integer".equals(fieldType))
                        || ("long".equals(fieldType) || "java.lang.Long".equals(fieldType))
                        || ("float".equals(fieldType) || "java.lang.Float".equals(fieldType))
                        || ("double".equals(fieldType) || "java.lang.Double".equals(fieldType))
                        || ("short".equals(fieldType) || "java.lang.Short".equals(fieldType))
                        || ("byte".equals(fieldType) || "java.lang.Byte".equals(fieldType))) {
                    readMethod = clazz.getMethod("get" + StringUtils.capitalize(fieldName));
                }
                if (readMethod != null) {
                    jsonObj.put(fieldName, readMethod.invoke(instance));
                }
            } catch (Exception ex) {
                logger.error("Error occurs[class: " + clazz.getCanonicalName() + ", fieldName: " + fieldName + ", filedType: " + fieldType + "].", ex);
            }
        }
        return jsonObj;
    }

    public static JSONArray listToJSONArray(List<?> list) {
        JSONArray jsonArr = new JSONArray();
        if (list != null && !list.isEmpty()) {
            for (Object o : list) {
                jsonArr.add(o);
            }
        }
        return jsonArr;
    }
    public static JSONArray listObjToJSONArray(List<?> list) {
        JSONArray jsonArr = new JSONArray();
        if (list != null && !list.isEmpty()) {
            for (Object o : list) {
                jsonArr.add(toJson(o));
            }
        }
        return jsonArr;
    }

    public static JSONObject parseJSONStr(String jsonData) {

        JSONParser jsonParser = new JSONParser();
        JSONObject root = null;
        try {
            root = (JSONObject) jsonParser.parse(jsonData);
        } catch (Exception ex) {
            logger.error("Fail to parse json string: " + jsonData, ex);
        }
        return root;
    }

    public static int getIntValue(JSONObject jsonObj, String key, int defaultVal) {
        Object obj = jsonObj.get(key);
        if (obj == null) {
            return defaultVal;
        }
        if (obj instanceof String) {
            return StringUtils.str2int((String) obj, defaultVal);
        } else if (obj instanceof Long) {
            return ((Long) obj).intValue();
        } else {
            return defaultVal;
        }
    }

    public static int getIntValue(JSONObject jsonObj, String key) {
        Object obj = jsonObj.get(key);
        if (obj == null) {
            return Constants.INVALID_INT_ID;
        }
        if (obj instanceof String) {
            return StringUtils.str2int((String) obj, Constants.INVALID_INT_ID);
        } else if (obj instanceof Long) {
            return ((Long) obj).intValue();
        } else {
            return Constants.INVALID_INT_ID;
        }
    }

    public static short getShortValue(JSONObject jsonObj, String key, short defaultVal) {
        Object obj = jsonObj.get(key);
        if (obj == null) {
            return defaultVal;
        }
        if (obj instanceof String) {
            return StringUtils.str2short((String) obj, defaultVal);
        } else if (obj instanceof Long) {
            return ((Long) obj).shortValue();
        } else {
            return defaultVal;
        }
    }

    public static short getShortValue(JSONObject jsonObj, String key) {
        Object obj = jsonObj.get(key);
        if (obj == null) {
            return Constants.INVALID_INT_ID;
        }
        if (obj instanceof String) {
            return StringUtils.str2short((String) obj, Constants.INVALID_SHORT_ID);
        } else if (obj instanceof Long) {
            return ((Long) obj).shortValue();
        } else {
            return Constants.INVALID_INT_ID;
        }
    }

    public static byte getByteValue(JSONObject jsonObj, String key, byte defaultVal) {
        Object obj = jsonObj.get(key);
        if (obj == null) {
            return defaultVal;
        }
        if (obj instanceof String) {
            return StringUtils.str2byte((String) obj, defaultVal);
        } else if (obj instanceof Long) {
            return ((Long) obj).byteValue();
        } else {
            return defaultVal;
        }
    }

    public static byte getByteValue(JSONObject jsonObj, String key) {
        Object obj = jsonObj.get(key);
        if (obj == null) {
            return Constants.INVALID_BYTE_ID;
        }
        if (obj instanceof String) {
            return StringUtils.str2byte((String) obj, Constants.INVALID_BYTE_ID);
        } else if (obj instanceof Long) {
            return ((Long) obj).byteValue();
        } else {
            return Constants.INVALID_BYTE_ID;
        }
    }

    public static long getLongValue(JSONObject jsonObj, String key, long defaultVal) {
        Object obj = jsonObj.get(key);
        if (obj == null) {
            return defaultVal;
        }
        if (obj instanceof String) {
            return StringUtils.str2long((String) obj, defaultVal);
        } else if (obj instanceof Long) {
            return ((Long) obj).byteValue();
        } else {
            return defaultVal;
        }
    }

    public static long getLongValue(JSONObject jsonObj, String key) {
        Object obj = jsonObj.get(key);
        if (obj == null) {
            return Constants.INVALID_LONG_ID;
        }
        if (obj instanceof String) {
            return StringUtils.str2long((String) obj, Constants.INVALID_LONG_ID);
        } else if (obj instanceof Long) {
            return ((Long) obj).byteValue();
        } else {
            return Constants.INVALID_LONG_ID;
        }
    }

    public static String getStringValue(JSONObject jsonObj, String key, String defaultVal) {
        Object obj = jsonObj.get(key);
        if (obj == null || !(obj instanceof String)) {
            return defaultVal;
        }

        return ((String) obj);
    }

    public static String getStringValue(JSONObject jsonObj, String key) {
        Object obj = jsonObj.get(key);
        if (obj == null || !(obj instanceof String)) {
            return "";
        }

        return ((String) obj);
    }
}
