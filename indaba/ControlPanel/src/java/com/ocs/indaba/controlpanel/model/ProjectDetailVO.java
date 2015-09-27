/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.model;

import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.UploadFile;
import com.ocs.util.DateUtils;
import com.ocs.util.JSONUtils;
import com.ocs.util.ValueObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff Jiang
 */
public class ProjectDetailVO extends ValueObject {

    private int id;
    private String name;
    private int langId;
    private String description;
    private int primaryOrgId;
    private List<Integer> secondaryOrgIds;
    private int primaryAdminId;
    private List<Integer> secondaryAdminIds;
    private int visibility;
    private List<Integer> roleIds;
    private int actionRightId;
    private int viewRightId;
    private String startTimeTxt;
    private String endTimeTxt;
    private Date startTime;
    private Date endTime;
    private boolean active;
    private int studyPeriodId;
    private List<Integer> targetIds;
    private String projLogo;
    private UploadFile projLogoFile;
    private List<String> sponsorLogos;
    private List<UploadFile> sponsorLogoFiles;

    @Override
    public JSONObject toJson() {
        JSONObject root = super.toJson();
        root.put("secondaryOrgIds", JSONUtils.listToJSONArray(secondaryOrgIds));
        root.put("secondaryAdminIds", JSONUtils.listToJSONArray(secondaryAdminIds));
        root.put("roleIds", JSONUtils.listToJSONArray(roleIds));
        root.put("targetIds", JSONUtils.listToJSONArray(targetIds));
        root.put("sponsorLogos", JSONUtils.listToJSONArray(sponsorLogos));
        if (projLogoFile != null) {
            root.put("projLogoFile", uploadFile2JSON(projLogoFile));
        }
        JSONArray jsonArr = new JSONArray();
        if (sponsorLogoFiles != null && !sponsorLogoFiles.isEmpty()) {
            for (UploadFile file : sponsorLogoFiles) {
                jsonArr.add(uploadFile2JSON(file));
            }
        }
        root.put("sponsorLogoFiles", jsonArr);
        return root;
    }

    public static ProjectDetailVO initWithProject(Project proj) {
        ProjectDetailVO projDetail = new ProjectDetailVO();
        projDetail.setActionRightId(proj.getAccessMatrixId());
        projDetail.setActive(proj.getIsActive());
        projDetail.setDescription(proj.getDescription());
        projDetail.setEndTime(proj.getCloseTime());
        projDetail.setEndTimeTxt(DateUtils.format(proj.getCloseTime(), DateUtils.DATE_FORMAT_6));
        projDetail.setId(proj.getId());
        projDetail.setName(proj.getCodeName());
        projDetail.setPrimaryAdminId(proj.getAdminUserId());
        projDetail.setPrimaryOrgId(proj.getOrganizationId());
        projDetail.setProjLogo(proj.getLogoPath());
        projDetail.setStartTime(proj.getStartTime());
        projDetail.setStartTimeTxt(DateUtils.format(proj.getStartTime(), DateUtils.DATE_FORMAT_6));
        projDetail.setStudyPeriodId(proj.getStudyPeriodId());
        projDetail.setViewRightId(proj.getViewMatrixId());
        projDetail.setVisibility(proj.getVisibility());
        String sponsorLogos = proj.getSponsorLogos();
        if (!StringUtils.isEmpty(sponsorLogos)) {
            String[] logos = sponsorLogos.split("[,| ]");
            for (String s : logos) {
                projDetail.setSponsorLogos(Arrays.asList(logos));
            }
        }
        return projDetail;
    }

    public int getActionRightId() {
        return actionRightId;
    }

    public void setActionRightId(int actionRightId) {
        this.actionRightId = actionRightId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLangId() {
        return langId;
    }

    public void setLangId(int langId) {
        this.langId = langId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrimaryAdminId() {
        return primaryAdminId;
    }

    public void setPrimaryAdminId(int primaryAdminId) {
        this.primaryAdminId = primaryAdminId;
    }

    public int getPrimaryOrgId() {
        return primaryOrgId;
    }

    public void setPrimaryOrgId(int primaryOrgId) {
        this.primaryOrgId = primaryOrgId;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public void addRoleId(int roleId) {
        if (roleIds == null) {
            roleIds = new ArrayList<Integer>();
        }
        roleIds.add(roleId);
    }

    public List<Integer> getSecondaryAdminIds() {
        return secondaryAdminIds;
    }

    public void setSecondaryAdminIds(List<Integer> secondaryAdminIds) {
        this.secondaryAdminIds = secondaryAdminIds;
    }

    public void addSecondaryAdminId(int secondaryAdminId) {
        if (secondaryAdminIds == null) {
            secondaryAdminIds = new ArrayList<Integer>();
        }
        secondaryAdminIds.add(secondaryAdminId);
    }

    public List<Integer> getSecondaryOrgIds() {
        return secondaryOrgIds;
    }

    public void setSecondaryOrgIds(List<Integer> secondaryOrgIds) {
        this.secondaryOrgIds = secondaryOrgIds;
    }

    public void addSecondaryOrgId(int secondaryOrgId) {
        if (secondaryOrgIds == null) {
            secondaryOrgIds = new ArrayList<Integer>();
        }
        secondaryOrgIds.add(secondaryOrgId);
    }

    public String getProjLogo() {
        return projLogo;
    }

    public void setProjLogo(String projLogo) {
        this.projLogo = projLogo;
    }

    public UploadFile getProjLogoFile() {
        return projLogoFile;
    }

    public void setProjLogoFile(UploadFile projLogoFile) {
        this.projLogoFile = projLogoFile;
    }

    public List<UploadFile> getSponsorLogoFiles() {
        return sponsorLogoFiles;
    }

    public void setSponsorLogoFiles(List<UploadFile> sponsorLogoFiles) {
        this.sponsorLogoFiles = sponsorLogoFiles;
    }

    public List<String> getSponsorLogos() {
        return sponsorLogos;
    }

    public void setSponsorLogos(List<String> sponsorLogos) {
        this.sponsorLogos = sponsorLogos;
    }

    public void addSponsorLogo(String logo) {
        if (sponsorLogos == null) {
            sponsorLogos = new ArrayList<String>();
        }
        sponsorLogos.add(logo);
    }

    public void addSponsorLogoFile(UploadFile uploadFile) {
        if (sponsorLogoFiles == null) {
            sponsorLogoFiles = new ArrayList<UploadFile>();
        }
        sponsorLogoFiles.add(uploadFile);
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getEndTimeTxt() {
        return endTimeTxt;
    }

    public void setEndTimeTxt(String endTimeTxt) {
        this.endTimeTxt = endTimeTxt;
    }

    public String getStartTimeTxt() {
        return startTimeTxt;
    }

    public void setStartTimeTxt(String startTimeTxt) {
        this.startTimeTxt = startTimeTxt;
    }

    public int getStudyPeriodId() {
        return studyPeriodId;
    }

    public void setStudyPeriodId(int studyPeriodId) {
        this.studyPeriodId = studyPeriodId;
    }

    public List<Integer> getTargetIds() {
        return targetIds;
    }

    public void setTargetIds(List<Integer> targetIds) {
        this.targetIds = targetIds;
    }

    public void addTargetId(int targetId) {
        if (targetIds == null) {
            targetIds = new ArrayList<Integer>();
        }
        targetIds.add(targetId);
    }

    public int getViewRightId() {
        return viewRightId;
    }

    public void setViewRightId(int viewRightId) {
        this.viewRightId = viewRightId;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    private JSONObject uploadFile2JSON(UploadFile file) {
        if (file == null) {
            return null;
        }
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("id", file.getId());
        jsonObj.put("uname", file.getFileName());
        jsonObj.put("dname", file.getDisplayName());
        jsonObj.put("size", file.getSize());
        return jsonObj;
    }

    @Override
    public String toString() {
        return "ProjectDetailVO{" + "id=" + id + ", name=" + name + ", langId=" + langId + ", description=" + description + ", primaryOrgId=" + primaryOrgId + ", secondaryOrgIds=" + secondaryOrgIds + ", primaryAdminId=" + primaryAdminId + ", secondaryAdminIds=" + secondaryAdminIds + ", visibility=" + visibility + ", roleIds=" + roleIds + ", actionRightId=" + actionRightId + ", viewRightId=" + viewRightId + ", startTimeTxt=" + startTimeTxt + ", endTimeTxt=" + endTimeTxt + ", startTime=" + startTime + ", endTime=" + endTime + ", active=" + active + ", studyPeriodId=" + studyPeriodId + ", targetIds=" + targetIds + ", projLogo=" + projLogo + ", sponsorLogos=" + sponsorLogos + '}';
    }
}