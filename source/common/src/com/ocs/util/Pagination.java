/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff
 */
public class Pagination<T> implements Serializable {

    private long total = 0;
    private int page = 0;
    private int pageSize = 0;
    private List<T> rows = null;
    private Map<String, Object> props = null;

    public Pagination() {
    }

    public Pagination(long total, int page, int pageSize) {
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
    }

    public Pagination(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public List<T> getRows() {
        return rows;
    }

    public void addRow(T item) {
        if (rows == null) {
            rows = new ArrayList<T>();
        }
        rows.add(item);
    }

    public void addRow(List<T> item) {
        if (rows == null) {
            rows = new ArrayList<T>();
        }
        rows.addAll(item);
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void subPage(List<T> list) {
        this.total = list.size();
        int offset = (page - 1) * pageSize;
        if (offset > total) {
        } else if (offset + pageSize > total) {
            setRows(list.subList(offset, Long.valueOf(total).intValue()));
        } else {
            setRows(list.subList(offset, offset + pageSize));
        }
    }

    @Override
    public String toString() {
        return "Pagination{" + "total=" + total + ", page=" + page + ", pageSize=" + pageSize + ", rows=" + rows + '}';
    }

    public String toJsonString() {
        return toJson().toJSONString();
    }

    public JSONObject toJsonObject() {
        JSONObject root = new JSONObject();
        root.put("total", total);
        root.put("offset", (page - 1) * pageSize);
        root.put("page", page);
        root.put("pageSize", pageSize);

        // add properties if any
        if (props != null && !props.isEmpty()) {
            Set<String> keys = props.keySet();
            for (String k : keys) {
                root.put(k, props.get(k));
            }
        }

        JSONArray jsonArr = new JSONArray();
        if (rows != null && !rows.isEmpty()) {
            for (T item : rows) {
                jsonArr.add(JSONUtils.parseJSONStr(JSONUtils.toJsonString(item)));
            }
        }
        root.put("rows", jsonArr);
        return root;
    }
    
    public JSONObject toJson() {
        JSONObject root = new JSONObject();
        root.put("total", total);
        root.put("offset", (page - 1) * pageSize);
        root.put("page", page);
        root.put("pageSize", pageSize);

        // add properties if any
        if (props != null && !props.isEmpty()) {
            Set<String> keys = props.keySet();
            for (String k : keys) {
                root.put(k, props.get(k));
            }
        }

        JSONArray jsonArr = new JSONArray();
        if (rows != null && !rows.isEmpty()) {
            for (T item : rows) {
                jsonArr.add(JSONUtils.toJson(item));
            }
        }
        root.put("rows", jsonArr);
        return root;
    }

    private JSONObject toJsonByItem(T obj) {
        JSONObject jsonObj = new JSONObject();
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null) {
            for (Field f : fields) {
                String fieldName = f.getName();
                String fieldType = f.getType().getName();
                try {
                    Method readMethod = null;
                    if ("boolean".equals(fieldType) || "java.lang.Boolean".equals(fieldType)) {
                        readMethod = clazz.getMethod("is" + StringUtils.capitalize(fieldName));
                    } else {
                        readMethod = clazz.getMethod("get" + StringUtils.capitalize(fieldName));
                    }
                    Object val = readMethod.invoke(obj);
                    if ((val == null) && "java.lang.String".equals(fieldType)) {
                        jsonObj.put(fieldName, "");
                    } else {
                        jsonObj.put(fieldName, readMethod.invoke(obj));
                    }
                } catch (NoSuchMethodException ex) {
                } catch (SecurityException ex) {
                } catch (IllegalAccessException ex) {
                } catch (IllegalArgumentException ex) {
                } catch (InvocationTargetException ex) {
                }
            }
        }
        return jsonObj;
    }

    public void addProperty(String key, Object value) {
        if (props == null) {
            props = new HashMap<String, Object>();
        }
        props.put(key, value);
    }
}
