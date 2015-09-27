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
@Table(name = "text_item")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TextItem.findAll", query = "SELECT t FROM TextItem t"),
    @NamedQuery(name = "TextItem.findById", query = "SELECT t FROM TextItem t WHERE t.id = :id"),
    @NamedQuery(name = "TextItem.findByTextResourceId", query = "SELECT t FROM TextItem t WHERE t.textResourceId = :textResourceId"),
    @NamedQuery(name = "TextItem.findByLanguageId", query = "SELECT t FROM TextItem t WHERE t.languageId = :languageId")})
public class TextItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "text_resource_id")
    private int textResourceId;
    @Basic(optional = false)
    @Column(name = "language_id")
    private int languageId;
    @Basic(optional = false)
    @Lob
    @Column(name = "text")
    private String text;

    public TextItem() {
    }

    public TextItem(Integer id) {
        this.id = id;
    }

    public TextItem(Integer id, int textResourceId, int languageId, String text) {
        this.id = id;
        this.textResourceId = textResourceId;
        this.languageId = languageId;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getTextResourceId() {
        return textResourceId;
    }

    public void setTextResourceId(int textResourceId) {
        this.textResourceId = textResourceId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        if (!(object instanceof TextItem)) {
            return false;
        }
        TextItem other = (TextItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.TextItem[ id=" + id + " ]";
    }
    
}
