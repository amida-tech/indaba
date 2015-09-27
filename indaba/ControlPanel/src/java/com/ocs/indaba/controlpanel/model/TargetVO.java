/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.model;

import com.ocs.indaba.controlpanel.common.ControlPanelConstants;
import com.ocs.indaba.po.Target;
import com.ocs.util.ValueObject;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff Jiang
 */
public class TargetVO extends ValueObject {

    private int id;
    private String name;
    private String shortName;
    private String statusDisplay;
    private String orgname;
    private String description;
    private short targetType;
    private String guid;
    private int creatorOrgId;
    private int ownerOrgId;
    private short visibility;
    private int languageId;
    private short status;
    private boolean editable = false;
    private List<Integer> tagIds;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getShortName() {
        return shortName;
    }
    
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    
    public String getOrgname() {
        return orgname;
    }
    
    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }
        
    public int getCreatorOrgId() {
        return creatorOrgId;
    }
    
    public void setCreatorOrgId(int creatorOrgId) {
        this.creatorOrgId = creatorOrgId;
    }
        
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getGuid() {
        return guid;
    }
    
    public void setGuid(String guid) {
        this.guid = guid;
    }
    
    public int getLanguageId() {
        return languageId;
    }
    
    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }
    
    public int getOwnerOrgId() {
        return ownerOrgId;
    }
    
    public void setOwnerOrgId(int ownerOrgId) {
        this.ownerOrgId = ownerOrgId;
    }
    
    public short getStatus() {
        return status;
    }
    
    public void setStatus(short status) {
        this.status = status;
    }
    
    public String getStatusDisplay() {
        return statusDisplay;
    }
    
    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }
    
    public short getTargetType() {
        return targetType;
    }
    
    public void setTargetType(short targetType) {
        this.targetType = targetType;
    }
    
    public short getVisibility() {
        return visibility;
    }
    
    public void setVisibility(short visibility) {
        this.visibility = visibility;
    }
    
    public List<Integer> getTagIds() {
        return tagIds;
    }
    
    public void addTagIds(List<Integer> tagIds) {
        if (this.tagIds == null) {
            this.tagIds = new ArrayList<Integer>();
        }
        if (tagIds != null) {
            this.tagIds.addAll(tagIds);
        }
    }
    
    public void setTagIds(List<Integer> tagIds) {
        this.tagIds = tagIds;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isEditable() {
        return this.editable;
    }

    
    @Override
    public JSONObject toJson() {
        JSONObject jsonObj = super.toJson();
        if(tagIds == null || tagIds.isEmpty()) {
            jsonObj.put("tagIds", null);
        } else {
            jsonObj.put("tagIds", tagIds);
        }
        return jsonObj;
    }
    public static TargetVO initWithTarget(Target target) {
        TargetVO targetVo = new TargetVO();
        if(target == null){
            return null;
        }
        targetVo.setId(target.getId());
        targetVo.setName(target.getName());
        targetVo.setShortName(target.getShortName());
        targetVo.setDescription(target.getDescription());
        targetVo.setStatus((short) target.getStatus());
        targetVo.setGuid(target.getGuid());
        targetVo.setOwnerOrgId(target.getOwnerOrgId());
        targetVo.setCreatorOrgId(target.getCreatorOrgId());
        targetVo.setLanguageId(target.getLanguageId());
        targetVo.setVisibility(target.getVisibility());
        targetVo.setTargetType(target.getTargetType());

        switch (target.getStatus()) {
            case ControlPanelConstants.TARGET_STATUS_ACTIVE:
                targetVo.setStatusDisplay("ACTIVE");
                break;
            case ControlPanelConstants.TARGET_STATUS_INACTIVE:
                targetVo.setStatusDisplay("INACTIVE");
                break;
            case ControlPanelConstants.TARGET_STATUS_DELETED:
                targetVo.setStatusDisplay("DELETED");
                break;
        }
        return targetVo;
    }


    
    @Override
    public String toString() {
        return "TargetVO{" + "id=" + id + ", name=" + name + ", shortName=" + shortName + ", statusDisplay=" + statusDisplay + ", orgname=" + orgname + ", description=" + description + ", targetType=" + targetType + ", guid=" + guid + ", creatorOrgId=" + creatorOrgId + ", ownerOrgId=" + ownerOrgId + ", visibility=" + visibility + ", languageId=" + languageId + ", status=" + status +  '}';
    }
}
