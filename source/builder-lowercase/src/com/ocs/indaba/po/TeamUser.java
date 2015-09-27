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
@Table(name = "team_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TeamUser.findAll", query = "SELECT t FROM TeamUser t"),
    @NamedQuery(name = "TeamUser.findById", query = "SELECT t FROM TeamUser t WHERE t.id = :id"),
    @NamedQuery(name = "TeamUser.findByUserId", query = "SELECT t FROM TeamUser t WHERE t.userId = :userId"),
    @NamedQuery(name = "TeamUser.findByTeamId", query = "SELECT t FROM TeamUser t WHERE t.teamId = :teamId")})
public class TeamUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @Column(name = "team_id")
    private int teamId;

    public TeamUser() {
    }

    public TeamUser(Integer id) {
        this.id = id;
    }

    public TeamUser(Integer id, int userId, int teamId) {
        this.id = id;
        this.userId = userId;
        this.teamId = teamId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
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
        if (!(object instanceof TeamUser)) {
            return false;
        }
        TeamUser other = (TeamUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.TeamUser[ id=" + id + " ]";
    }
    
}
