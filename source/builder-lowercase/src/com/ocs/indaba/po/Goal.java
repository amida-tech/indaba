/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.po;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "goal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Goal.findAll", query = "SELECT g FROM Goal g"),
    @NamedQuery(name = "Goal.findById", query = "SELECT g FROM Goal g WHERE g.id = :id"),
    @NamedQuery(name = "Goal.findByWorkflowSequenceId", query = "SELECT g FROM Goal g WHERE g.workflowSequenceId = :workflowSequenceId"),
    @NamedQuery(name = "Goal.findByWeight", query = "SELECT g FROM Goal g WHERE g.weight = :weight"),
    @NamedQuery(name = "Goal.findByName", query = "SELECT g FROM Goal g WHERE g.name = :name"),
    @NamedQuery(name = "Goal.findByDescription", query = "SELECT g FROM Goal g WHERE g.description = :description"),
    @NamedQuery(name = "Goal.findByAccessMatrixId", query = "SELECT g FROM Goal g WHERE g.accessMatrixId = :accessMatrixId"),
    @NamedQuery(name = "Goal.findByDuration", query = "SELECT g FROM Goal g WHERE g.duration = :duration"),
    @NamedQuery(name = "Goal.findByEntranceRuleFileName", query = "SELECT g FROM Goal g WHERE g.entranceRuleFileName = :entranceRuleFileName"),
    @NamedQuery(name = "Goal.findByInflightRuleFileName", query = "SELECT g FROM Goal g WHERE g.inflightRuleFileName = :inflightRuleFileName"),
    @NamedQuery(name = "Goal.findByExitRuleFileName", query = "SELECT g FROM Goal g WHERE g.exitRuleFileName = :exitRuleFileName")})
public class Goal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "workflow_sequence_id")
    private int workflowSequenceId;
    @Basic(optional = false)
    @Column(name = "weight")
    private int weight;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "access_matrix_id")
    private int accessMatrixId;
    @Basic(optional = false)
    @Column(name = "duration")
    private int duration;
    @Column(name = "entrance_rule_file_name")
    private String entranceRuleFileName;
    @Column(name = "inflight_rule_file_name")
    private String inflightRuleFileName;
    @Column(name = "exit_rule_file_name")
    private String exitRuleFileName;
    @Basic(optional = false)
    @Lob
    @Column(name = "entrance_rule_desc")
    private String entranceRuleDesc;
    @Basic(optional = false)
    @Lob
    @Column(name = "inflight_rule_desc")
    private String inflightRuleDesc;
    @Basic(optional = false)
    @Lob
    @Column(name = "exit_rule_desc")
    private String exitRuleDesc;
    @Column(name = "create_content_version")
    private short createContentVersion;
    public Goal() {
    }

    public Goal(Integer id) {
        this.id = id;
    }

    public Goal(Integer id, int workflowSequenceId, int weight, String name, int accessMatrixId, int duration, String entranceRuleDesc, String inflightRuleDesc, String exitRuleDesc) {
        this.id = id;
        this.workflowSequenceId = workflowSequenceId;
        this.weight = weight;
        this.name = name;
        this.accessMatrixId = accessMatrixId;
        this.duration = duration;
        this.entranceRuleDesc = entranceRuleDesc;
        this.inflightRuleDesc = inflightRuleDesc;
        this.exitRuleDesc = exitRuleDesc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getWorkflowSequenceId() {
        return workflowSequenceId;
    }

    public void setWorkflowSequenceId(int workflowSequenceId) {
        this.workflowSequenceId = workflowSequenceId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAccessMatrixId() {
        return accessMatrixId;
    }

    public void setAccessMatrixId(int accessMatrixId) {
        this.accessMatrixId = accessMatrixId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getEntranceRuleFileName() {
        return entranceRuleFileName;
    }

    public void setEntranceRuleFileName(String entranceRuleFileName) {
        this.entranceRuleFileName = entranceRuleFileName;
    }

    public String getInflightRuleFileName() {
        return inflightRuleFileName;
    }

    public void setInflightRuleFileName(String inflightRuleFileName) {
        this.inflightRuleFileName = inflightRuleFileName;
    }

    public String getExitRuleFileName() {
        return exitRuleFileName;
    }

    public void setExitRuleFileName(String exitRuleFileName) {
        this.exitRuleFileName = exitRuleFileName;
    }

    public String getEntranceRuleDesc() {
        return entranceRuleDesc;
    }

    public void setEntranceRuleDesc(String entranceRuleDesc) {
        this.entranceRuleDesc = entranceRuleDesc;
    }

    public String getInflightRuleDesc() {
        return inflightRuleDesc;
    }

    public void setInflightRuleDesc(String inflightRuleDesc) {
        this.inflightRuleDesc = inflightRuleDesc;
    }

    public String getExitRuleDesc() {
        return exitRuleDesc;
    }

    public void setExitRuleDesc(String exitRuleDesc) {
        this.exitRuleDesc = exitRuleDesc;
    }

    public short getCreateContentVersion() {
        return createContentVersion;
    }

    public void setCreateContentVersion(short createContentVersion) {
        this.createContentVersion = createContentVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Goal)) {
            return false;
        }
        Goal other = (Goal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Goal[ id=" + id + " ]";
    }
    
}
