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
@Table(name = "journal_config")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JournalConfig.findAll", query = "SELECT j FROM JournalConfig j"),
    @NamedQuery(name = "JournalConfig.findById", query = "SELECT j FROM JournalConfig j WHERE j.id = :id"),
    @NamedQuery(name = "JournalConfig.findByName", query = "SELECT j FROM JournalConfig j WHERE j.name = :name"),
    @NamedQuery(name = "JournalConfig.findByDescription", query = "SELECT j FROM JournalConfig j WHERE j.description = :description"),
    @NamedQuery(name = "JournalConfig.findByMinWords", query = "SELECT j FROM JournalConfig j WHERE j.minWords = :minWords"),
    @NamedQuery(name = "JournalConfig.findByMaxWords", query = "SELECT j FROM JournalConfig j WHERE j.maxWords = :maxWords"),
    @NamedQuery(name = "JournalConfig.findByExportableItems", query = "SELECT j FROM JournalConfig j WHERE j.exportableItems = :exportableItems")})
public class JournalConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Lob
    @Column(name = "instructions")
    private String instructions;
    @Basic(optional = false)
    @Column(name = "min_words")
    private int minWords;
    @Basic(optional = false)
    @Column(name = "max_words")
    private int maxWords;
    @Basic(optional = false)
    @Column(name = "exportable_items")
    private short exportableItems;

    public JournalConfig() {
    }

    public JournalConfig(Integer id) {
        this.id = id;
    }

    public JournalConfig(Integer id, String name, String instructions, int minWords, int maxWords, short exportableItems) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.minWords = minWords;
        this.maxWords = maxWords;
        this.exportableItems = exportableItems;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public int getMinWords() {
        return minWords;
    }

    public void setMinWords(int minWords) {
        this.minWords = minWords;
    }

    public int getMaxWords() {
        return maxWords;
    }

    public void setMaxWords(int maxWords) {
        this.maxWords = maxWords;
    }

    public short getExportableItems() {
        return exportableItems;
    }

    public void setExportableItems(short exportableItems) {
        this.exportableItems = exportableItems;
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
        if (!(object instanceof JournalConfig)) {
            return false;
        }
        JournalConfig other = (JournalConfig) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ocs.indaba.po.JournalConfig[ id=" + id + " ]";
    }
    
}
