/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.po;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jiangjeff
 */
@Entity
@Table(name = "config")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Config.findAll", query = "SELECT c FROM Config c"),
    @NamedQuery(name = "Config.findById", query = "SELECT c FROM Config c WHERE c.id = :id"),
    @NamedQuery(name = "Config.findBySiteName", query = "SELECT c FROM Config c WHERE c.siteName = :siteName"),
    @NamedQuery(name = "Config.findBySrvOtisValue", query = "SELECT c FROM Config c WHERE c.srvOtisValue = :srvOtisValue"),
    @NamedQuery(name = "Config.findBySrvScorecard", query = "SELECT c FROM Config c WHERE c.srvScorecard = :srvScorecard"),
    @NamedQuery(name = "Config.findBySrvScorecardAnswer", query = "SELECT c FROM Config c WHERE c.srvScorecardAnswer = :srvScorecardAnswer"),
    @NamedQuery(name = "Config.findBySrvTdsValue", query = "SELECT c FROM Config c WHERE c.srvTdsValue = :srvTdsValue"),
    @NamedQuery(name = "Config.findByComOtisValue", query = "SELECT c FROM Config c WHERE c.comOtisValue = :comOtisValue"),
    @NamedQuery(name = "Config.findByComScorecard", query = "SELECT c FROM Config c WHERE c.comScorecard = :comScorecard"),
    @NamedQuery(name = "Config.findByComScorecardAnswer", query = "SELECT c FROM Config c WHERE c.comScorecardAnswer = :comScorecardAnswer"),
    @NamedQuery(name = "Config.findByComTdsValue", query = "SELECT c FROM Config c WHERE c.comTdsValue = :comTdsValue"),
    @NamedQuery(name = "Config.findByTableSwapTime", query = "SELECT c FROM Config c WHERE c.tableSwapTime = :tableSwapTime")})
public class Config implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "site_name")
    private String siteName;
    @Lob
    @Column(name = "site_intro")
    private String siteIntro;
    @Basic(optional = false)
    @Column(name = "srv_otis_value")
    private String srvOtisValue;
    @Basic(optional = false)
    @Column(name = "srv_scorecard")
    private String srvScorecard;
    @Basic(optional = false)
    @Column(name = "srv_scorecard_answer")
    private String srvScorecardAnswer;
    @Basic(optional = false)
    @Column(name = "srv_tds_value")
    private String srvTdsValue;
    @Basic(optional = false)
    @Column(name = "com_otis_value")
    private String comOtisValue;
    @Basic(optional = false)
    @Column(name = "com_scorecard")
    private String comScorecard;
    @Basic(optional = false)
    @Column(name = "com_scorecard_answer")
    private String comScorecardAnswer;
    @Basic(optional = false)
    @Column(name = "com_tds_value")
    private String comTdsValue;
    @Column(name = "table_swap_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tableSwapTime;

    public Config() {
    }

    public Config(Integer id) {
        this.id = id;
    }

    public Config(Integer id, String srvOtisValue, String srvScorecard, String srvScorecardAnswer, String srvTdsValue, String comOtisValue, String comScorecard, String comScorecardAnswer, String comTdsValue) {
        this.id = id;
        this.srvOtisValue = srvOtisValue;
        this.srvScorecard = srvScorecard;
        this.srvScorecardAnswer = srvScorecardAnswer;
        this.srvTdsValue = srvTdsValue;
        this.comOtisValue = comOtisValue;
        this.comScorecard = comScorecard;
        this.comScorecardAnswer = comScorecardAnswer;
        this.comTdsValue = comTdsValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteIntro() {
        return siteIntro;
    }

    public void setSiteIntro(String siteIntro) {
        this.siteIntro = siteIntro;
    }

    public String getSrvOtisValue() {
        return srvOtisValue;
    }

    public void setSrvOtisValue(String srvOtisValue) {
        this.srvOtisValue = srvOtisValue;
    }

    public String getSrvScorecard() {
        return srvScorecard;
    }

    public void setSrvScorecard(String srvScorecard) {
        this.srvScorecard = srvScorecard;
    }

    public String getSrvScorecardAnswer() {
        return srvScorecardAnswer;
    }

    public void setSrvScorecardAnswer(String srvScorecardAnswer) {
        this.srvScorecardAnswer = srvScorecardAnswer;
    }

    public String getSrvTdsValue() {
        return srvTdsValue;
    }

    public void setSrvTdsValue(String srvTdsValue) {
        this.srvTdsValue = srvTdsValue;
    }

    public String getComOtisValue() {
        return comOtisValue;
    }

    public void setComOtisValue(String comOtisValue) {
        this.comOtisValue = comOtisValue;
    }

    public String getComScorecard() {
        return comScorecard;
    }

    public void setComScorecard(String comScorecard) {
        this.comScorecard = comScorecard;
    }

    public String getComScorecardAnswer() {
        return comScorecardAnswer;
    }

    public void setComScorecardAnswer(String comScorecardAnswer) {
        this.comScorecardAnswer = comScorecardAnswer;
    }

    public String getComTdsValue() {
        return comTdsValue;
    }

    public void setComTdsValue(String comTdsValue) {
        this.comTdsValue = comTdsValue;
    }

    public Date getTableSwapTime() {
        return tableSwapTime;
    }

    public void setTableSwapTime(Date tableSwapTime) {
        this.tableSwapTime = tableSwapTime;
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
        if (!(object instanceof Config)) {
            return false;
        }
        Config other = (Config) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.aggregation.po.Config[ id=" + id + " ]";
    }
    
}
