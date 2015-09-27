/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.po.TextItem;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author Jeff
 */
public class TextResourceItemVO {

    private int sourceFileId;
    private int textResourceId;
    private String resourceName;
    private String description;
    Map<Integer, TextItem> textItems;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getSourceFileId() {
        return sourceFileId;
    }

    public void setSourceFileId(int sourceFileId) {
        this.sourceFileId = sourceFileId;
    }

    public int getTextResourceId() {
        return textResourceId;
    }

    public void setTextResourceId(int textResourceId) {
        this.textResourceId = textResourceId;
    }

    public TextItem getTextItem(int languageId) {
        return (textItems != null) ? textItems.get(languageId) : null;
    }

    public void putTextItem(int languageId, TextItem textItem) {
        if (textItems == null) {
            textItems = new HashMap<Integer, TextItem>();
        }
        textItems.put(languageId, textItem);
    }

    public Map<Integer, TextItem> getTextItems() {
        return textItems;
    }

    public void setTextItems(Map<Integer, TextItem> textItems) {
        this.textItems = textItems;
    }
    
    public void destroy(){
        if(textItems != null) {
            textItems.clear();
            textItems = null;
        }
    }

    @Override
    public String toString() {
        return "TextResourceItemVO{" + "sourceFileId=" + sourceFileId + ", textResourceId=" + textResourceId + ", resourceName=" + resourceName + ", description=" + description + ", textItems=" + textItems + '}';
    }
    
}
