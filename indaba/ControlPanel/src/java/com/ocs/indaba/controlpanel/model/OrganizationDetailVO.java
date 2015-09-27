/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.model;
import com.ocs.util.ValueObject;
import org.apache.log4j.Logger;
import java.util.List;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
/**
 *
 * @author rick
 */
public class OrganizationDetailVO extends ValueObject {
    private static final Logger logger = Logger.getLogger(OrganizationDetailVO.class);
    private int id;
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return this.id;
    }
    private String name;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    private String address;
    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return this.address;
    }
    private String url;
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return this.url;
    }
    private boolean enforceAPISecurity;
    public void setEnforceAPISecurity(boolean ef) {
        this.enforceAPISecurity = ef;
    }
    public boolean getEnforceAPISecurity() {
        return this.enforceAPISecurity;
    }
    private List<OrgAdminVO> OAs;
    public void AddOrgAdmin(OrgAdminVO admin) {
        if(OAs == null) {
            OAs = new ArrayList<OrgAdminVO>();
        }
        OAs.add(admin);
    }
    private List<OrgKeyVO> keys;
    public void AddOrgKey(OrgKeyVO key) {
        if(keys == null) {
            keys = new ArrayList<OrgKeyVO>();
        }
        keys.add(key);
    }
    @Override
    public JSONObject toJson() {
        JSONObject jObj = new JSONObject();
        jObj.put(ATTR_ID, this.id);
        jObj.put(ATTR_NAME, this.name);
        jObj.put(ATTR_ADDRESS, this.address);
        jObj.put(ATTR_URL, this.url);
        jObj.put(ATTR_ENFORCE_API_SECURITY, this.enforceAPISecurity);
        if(OAs != null) {
            JSONArray jArray = new JSONArray();
            for(OrgAdminVO a : OAs) {
                jArray.add(a.toJson());
            }
            jObj.put(ATTR_OAS, jArray);
        }
        if(keys != null) {
            JSONArray jArray = new JSONArray();
            for(OrgKeyVO k : keys) {
                jArray.add(k.toJson());
            }
            jObj.put(ATTR_KEYS, jArray);
        }
        return jObj;
    }
    private static String ATTR_ID = "id";
    private static String ATTR_NAME = "name";
    private static String ATTR_ADDRESS = "address";
    private static String ATTR_URL = "url";
    private static String ATTR_ENFORCE_API_SECURITY = "enforce_api_security";
    private static String ATTR_OAS = "oas";
    private static String ATTR_KEYS = "keys";
}
