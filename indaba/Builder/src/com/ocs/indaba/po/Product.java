/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.po;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author yc06x
 */
@Entity
@Table(name = "product")
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p"),
    @NamedQuery(name = "Product.findById", query = "SELECT p FROM Product p WHERE p.id = :id"),
    @NamedQuery(name = "Product.findByWorkflowId", query = "SELECT p FROM Product p WHERE p.workflowId = :workflowId"),
    @NamedQuery(name = "Product.findByName", query = "SELECT p FROM Product p WHERE p.name = :name"),
    @NamedQuery(name = "Product.findByDescription", query = "SELECT p FROM Product p WHERE p.description = :description"),
    @NamedQuery(name = "Product.findByProjectId", query = "SELECT p FROM Product p WHERE p.projectId = :projectId"),
    @NamedQuery(name = "Product.findByAccessMatrixId", query = "SELECT p FROM Product p WHERE p.accessMatrixId = :accessMatrixId"),
    @NamedQuery(name = "Product.findByProductConfigId", query = "SELECT p FROM Product p WHERE p.productConfigId = :productConfigId"),
    @NamedQuery(name = "Product.findByContentType", query = "SELECT p FROM Product p WHERE p.contentType = :contentType"),
    @NamedQuery(name = "Product.findByMode", query = "SELECT p FROM Product p WHERE p.mode = :mode"),
    @NamedQuery(name = "Product.findByReportUrl", query = "SELECT p FROM Product p WHERE p.reportUrl = :reportUrl")})
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "workflow_id")
    private int workflowId;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "project_id")
    private int projectId;
    @Column(name = "access_matrix_id")
    private Integer accessMatrixId;
    @Basic(optional = false)
    @Column(name = "product_config_id")
    private int productConfigId;
    @Basic(optional = false)
    @Column(name = "content_type")
    private short contentType;
    @Basic(optional = false)
    @Column(name = "mode")
    private short mode;
    @Column(name = "report_url")
    private String reportUrl;
    @Column(name = "analytics_url")
    private String analyticsUrl;

    public Product() {
    }

    public Product(Integer id) {
        this.id = id;
    }

    public Product(Integer id, int workflowId, String name, int projectId, int productConfigId, short contentType, short mode) {
        this.id = id;
        this.workflowId = workflowId;
        this.name = name;
        this.projectId = projectId;
        this.productConfigId = productConfigId;
        this.contentType = contentType;
        this.mode = mode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(int workflowId) {
        this.workflowId = workflowId;
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

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public Integer getAccessMatrixId() {
        return accessMatrixId;
    }

    public void setAccessMatrixId(Integer accessMatrixId) {
        this.accessMatrixId = accessMatrixId;
    }

    public int getProductConfigId() {
        return productConfigId;
    }

    public void setProductConfigId(int productConfigId) {
        this.productConfigId = productConfigId;
    }

    public short getContentType() {
        return contentType;
    }

    public void setContentType(short contentType) {
        this.contentType = contentType;
    }

    public short getMode() {
        return mode;
    }

    public void setMode(short mode) {
        this.mode = mode;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getAnalyticsUrl() {
        return analyticsUrl;
    }

    public void setAnalyticsUrl(String url) {
        this.analyticsUrl = url;
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
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.Product[id=" + id + "]";
    }

}
