/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff Jiang
 */
public class ValueObject {

    private static final Logger logger = Logger.getLogger(ValueObject.class);

    public void initializeObject(Map<String, String> map) {
        Class clazz = getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null) {
            for (Field f : fields) {
                String fieldName = f.getName();
                String fieldType = f.getType().getName();
                try {
                    String value = map.get(fieldName);
                    if (StringUtils.isEmpty(value)) {
                        continue;
                    }

                    Method writeMethod = clazz.getMethod("set" + StringUtils.capitalize(fieldName), new Class[]{f.getType()});
                    if ("java.lang.String".equals(fieldType)) {
                        writeMethod.invoke(this, value);
                    } else if ("int".equals(fieldType) || "java.lang.Integer".equals(fieldType)) {
                        writeMethod.invoke(this, Integer.valueOf(value));
                    } else if ("boolean".equals(fieldType) || "java.lang.Boolean".equals(fieldType)) {
                        writeMethod.invoke(this, Boolean.valueOf(value));
                    } else if ("long".equals(fieldType) || "java.lang.Long".equals(fieldType)) {
                        writeMethod.invoke(this, Long.valueOf(value));
                    } else if ("short".equals(fieldType) || "java.lang.Short".equals(fieldType)) {
                        writeMethod.invoke(this, Short.valueOf(value));
                    } else if ("byte".equals(fieldType) || "java.lang.Byte".equals(fieldType)) {
                        writeMethod.invoke(this, Byte.valueOf(value));
                    } else if ("float".equals(fieldType) || "java.lang.Float".equals(fieldType)) {
                        writeMethod.invoke(this, Float.valueOf(value));
                    } else if ("double".equals(fieldType) || "java.lang.Double".equals(fieldType)) {
                        writeMethod.invoke(this, Double.valueOf(value));
                    }
                    logger.debug("Invoked setter " + writeMethod + ", type=" + fieldType + ", value=" + value);

                } catch (Exception ex) {
                    logger.error("Error occurs[class: " + clazz.getCanonicalName() + ", fieldName: " + fieldName + ", filedType: " + fieldType + "].", ex);
                }
            }
        }
    }

    public String toJsonString() {
        return toJson().toJSONString();
    }

    public JSONObject toJson() {
        return JSONUtils.toJson(this);
    }

    public static void main(String args[]) {
        IndicatorDetailVO vo = new IndicatorDetailVO();
        Map map = new java.util.HashMap();
        map.put("id", 123);
        vo.initializeObject(map);
        System.out.println(vo.toJsonString());

        System.out.println("END");
    }
}

class IndicatorDetailVO extends ValueObject {

    private int id;
    private int language;
    private String name;
    private int reference;
    private int organization;
    private String question = null;
    private String tip = null;
    private int answerType;
    private List<String> tags;

    public IndicatorDetailVO() {
    }

    @Override
    public void initializeObject(Map<String, String> map) {
        if (map == null) {
            return;
        }
        super.initializeObject(map);
    }

    public int getAnswerType() {
        return answerType;
    }

    public void setAnswerType(int answerType) {
        this.answerType = answerType;
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

    public List<String> getTags() {
        return tags;
    }

    public void addTag(String tag) {
        if (this.tags == null) {
            tags = new ArrayList<String>();
        }
        tags.add(tag);
    }

    public void setTags(String[] tags) {
        if (tags != null) {
            this.tags = Arrays.asList(tags);
        }
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return "IndicatorDetailVO{" + "id=" + id + ", language=" + language + ", name=" + name + ", reference=" + reference + ", organization=" + organization + ", question=" + question + ", tip=" + tip + ", answerType=" + answerType + ", tags=" + tags + '}';
    }
}
