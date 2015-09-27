/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.po;

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

/**
 *
 * @author yc06x
 */
@Entity
@Table(name = "user")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),
    @NamedQuery(name = "User.findByLastPasswordChangeTime", query = "SELECT u FROM User u WHERE u.lastPasswordChangeTime = :lastPasswordChangeTime"),
    @NamedQuery(name = "User.findByCreateTime", query = "SELECT u FROM User u WHERE u.createTime = :createTime"),
    @NamedQuery(name = "User.findByLastLogoutTime", query = "SELECT u FROM User u WHERE u.lastLogoutTime = :lastLogoutTime"),
    @NamedQuery(name = "User.findByStatus", query = "SELECT u FROM User u WHERE u.status = :status"),
    @NamedQuery(name = "User.findByTimezone", query = "SELECT u FROM User u WHERE u.timezone = :timezone"),
    @NamedQuery(name = "User.findByLanguageId", query = "SELECT u FROM User u WHERE u.languageId = :languageId"),
    @NamedQuery(name = "User.findByMsgboardId", query = "SELECT u FROM User u WHERE u.msgboardId = :msgboardId"),
    @NamedQuery(name = "User.findByForwardInboxMsg", query = "SELECT u FROM User u WHERE u.forwardInboxMsg = :forwardInboxMsg"),
    @NamedQuery(name = "User.findByNumberMsgsPerScreen", query = "SELECT u FROM User u WHERE u.numberMsgsPerScreen = :numberMsgsPerScreen"),
    @NamedQuery(name = "User.findByFirstName", query = "SELECT u FROM User u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "User.findByLastName", query = "SELECT u FROM User u WHERE u.lastName = :lastName"),
    @NamedQuery(name = "User.findByPhone", query = "SELECT u FROM User u WHERE u.phone = :phone"),
    @NamedQuery(name = "User.findByCellPhone", query = "SELECT u FROM User u WHERE u.cellPhone = :cellPhone"),
    @NamedQuery(name = "User.findByPhoto", query = "SELECT u FROM User u WHERE u.photo = :photo"),
    @NamedQuery(name = "User.findByLocation", query = "SELECT u FROM User u WHERE u.location = :location"),
    @NamedQuery(name = "User.findByEmailDetailLevel", query = "SELECT u FROM User u WHERE u.emailDetailLevel = :emailDetailLevel"),
    @NamedQuery(name = "User.findByDefaultProjectId", query = "SELECT u FROM User u WHERE u.defaultProjectId = :defaultProjectId"),
    @NamedQuery(name = "User.findByLastLoginTime", query = "SELECT u FROM User u WHERE u.lastLoginTime = :lastLoginTime"),
    @NamedQuery(name = "User.findBySiteAdmin", query = "SELECT u FROM User u WHERE u.siteAdmin = :siteAdmin"),
    @NamedQuery(name = "User.findByOrganizationId", query = "SELECT u FROM User u WHERE u.organizationId = :organizationId"),
    @NamedQuery(name = "User.findByPrivacyPolicyAcceptTime", query = "SELECT u FROM User u WHERE u.privacyPolicyAcceptTime = :privacyPolicyAcceptTime"),
    @NamedQuery(name = "User.findByImportId", query = "SELECT u FROM User u WHERE u.importId = :importId")})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Column(name = "last_password_change_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordChangeTime;
    @Basic(optional = false)
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Column(name = "last_logout_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogoutTime;
    @Basic(optional = false)
    @Column(name = "status")
    private short status;
    @Column(name = "timezone")
    private Integer timezone;
    @Column(name = "language_id")
    private Integer languageId;
    @Column(name = "msgboard_id")
    private Integer msgboardId;
    @Column(name = "forward_inbox_msg")
    private Boolean forwardInboxMsg;
    @Column(name = "number_msgs_per_screen")
    private Integer numberMsgsPerScreen;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "phone")
    private String phone;
    @Column(name = "cell_phone")
    private String cellPhone;
    @Lob
    @Column(name = "address")
    private String address;
    @Lob
    @Column(name = "bio")
    private String bio;
    @Column(name = "photo")
    private String photo;
    @Column(name = "location")
    private String location;
    @Column(name = "email_detail_level")
    private Short emailDetailLevel;
    @Column(name = "default_project_id")
    private Integer defaultProjectId;
    @Column(name = "last_login_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;
    @Column(name = "site_admin")
    private Short siteAdmin;
    @Basic(optional = false)
    @Column(name = "organization_id")
    private int organizationId;
    @Column(name = "privacy_policy_accept_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date privacyPolicyAcceptTime;
    @Column(name = "import_id")
    private Integer importId;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String username, String email, String password, Date createTime, short status, int organizationId) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.createTime = createTime;
        this.status = status;
        this.organizationId = organizationId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastPasswordChangeTime() {
        return lastPasswordChangeTime;
    }

    public void setLastPasswordChangeTime(Date lastPasswordChangeTime) {
        this.lastPasswordChangeTime = lastPasswordChangeTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastLogoutTime() {
        return lastLogoutTime;
    }

    public void setLastLogoutTime(Date lastLogoutTime) {
        this.lastLogoutTime = lastLogoutTime;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Integer getTimezone() {
        return timezone;
    }

    public void setTimezone(Integer timezone) {
        this.timezone = timezone;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public Integer getMsgboardId() {
        return msgboardId;
    }

    public void setMsgboardId(Integer msgboardId) {
        this.msgboardId = msgboardId;
    }

    public Boolean getForwardInboxMsg() {
        return forwardInboxMsg;
    }

    public void setForwardInboxMsg(Boolean forwardInboxMsg) {
        this.forwardInboxMsg = forwardInboxMsg;
    }

    public Integer getNumberMsgsPerScreen() {
        return numberMsgsPerScreen;
    }

    public void setNumberMsgsPerScreen(Integer numberMsgsPerScreen) {
        this.numberMsgsPerScreen = numberMsgsPerScreen;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Short getEmailDetailLevel() {
        return emailDetailLevel;
    }

    public void setEmailDetailLevel(Short emailDetailLevel) {
        this.emailDetailLevel = emailDetailLevel;
    }

    public Integer getDefaultProjectId() {
        return defaultProjectId;
    }

    public void setDefaultProjectId(Integer defaultProjectId) {
        this.defaultProjectId = defaultProjectId;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Short getSiteAdmin() {
        return siteAdmin;
    }

    public void setSiteAdmin(Short siteAdmin) {
        this.siteAdmin = siteAdmin;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public Date getPrivacyPolicyAcceptTime() {
        return privacyPolicyAcceptTime;
    }

    public void setPrivacyPolicyAcceptTime(Date privacyPolicyAcceptTime) {
        this.privacyPolicyAcceptTime = privacyPolicyAcceptTime;
    }

    public Integer getImportId() {
        return importId;
    }

    public void setImportId(Integer importId) {
        this.importId = importId;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.User[id=" + id + "]";
    }

}
