/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.SourceFileDAO;
import com.ocs.indaba.dao.SourceFileTextResourceDAO;
import com.ocs.indaba.dao.TextItemDAO;
import com.ocs.indaba.dao.TextResourceDAO;
import com.ocs.indaba.po.SourceFile;
import com.ocs.indaba.po.SourceFileTextResource;
import com.ocs.indaba.po.TextItem;
import com.ocs.indaba.po.TextResource;
import com.ocs.indaba.vo.TextResourceItemVO;
import com.ocs.indaba.vo.TextResourceVO;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class TextResourceService {

    private SourceFileDAO sourceFileDao;
    private SourceFileTextResourceDAO sourceFileTextResourceDao;
    private TextResourceDAO textResourceDao;
    private TextItemDAO textItemDao;

    public List<SourceFile> getActiveSourceFiles() {
        return sourceFileDao.selectActiveSourceFiles();
    }

    public List<SourceFile> getAllSourceFiles() {
        return sourceFileDao.findAll();
    }

    public List<SourceFile> searchSourceFilesByFilenameLike(String searchFilename) {
        return sourceFileDao.selectSourceFilesByFilenameLike(searchFilename);
    }

    public SourceFile getSourceFile(int id) {
        return sourceFileDao.get(id);
    }

    public SourceFile getSourceFileByFilename(String filename) {
        return sourceFileDao.selectSourceFileByFilename(filename);
    }

    public SourceFile addSourceFile(SourceFile sourceFile) {
        return sourceFileDao.create(sourceFile);
    }

    public SourceFile updateSourceFile(SourceFile sourceFile) {
        return sourceFileDao.update(sourceFile);
    }

    public void deleteSourceFile(int id) {
        List<Integer> txtRsrcIds = sourceFileTextResourceDao.selectTextResourceIdsBySourceFileId(id);
        textItemDao.deleteTextItemsByResourceIds(txtRsrcIds);
        textResourceDao.deleteTextResourcesByIds(txtRsrcIds);
        sourceFileTextResourceDao.deleteBySourceFileId(id);
        sourceFileDao.delete(id);
    }

    public List<SourceFileTextResource> getAllSourceFileTextResources() {
        return sourceFileTextResourceDao.findAll();
    }

    public SourceFileTextResource getSourceFileTextResource(int id) {
        return sourceFileTextResourceDao.get(id);
    }

    public SourceFileTextResource getByTextResourceId(int txtRsrcId) {
        return sourceFileTextResourceDao.selectByTextResourceId(txtRsrcId);
    }

    public SourceFileTextResource addSourceFileTextResource(SourceFileTextResource sourceFileTextResource) {
        return sourceFileTextResourceDao.create(sourceFileTextResource);
    }

    public SourceFileTextResource addSourceFileTextResource(SourceFileTextResource sourceFileTextResource, boolean autoIncrement) {
        return sourceFileTextResourceDao.create(sourceFileTextResource, autoIncrement);
    }

    public SourceFileTextResource updateSourceFileTextResource(SourceFileTextResource sourceFileTextResource) {
        return sourceFileTextResourceDao.update(sourceFileTextResource);
    }

    public Map<Integer, Integer> getTextResourceCountsMap() {
        return sourceFileTextResourceDao.selectTextResourceCountsMap();
    }

    public void deleteSourceFileTextResource(int id) {
        SourceFileTextResource obj = sourceFileTextResourceDao.get(id);
        textItemDao.deleteTextItemByResourceId(obj.getTextResourceId());
        textResourceDao.delete(obj.getTextResourceId());
        sourceFileTextResourceDao.delete(id);
    }

    public List<TextResource> getAllTextResources() {
        return textResourceDao.findAll();
    }

    public TextResource addTextResource(int sourceFileId, TextResource textResource) {
        textResource = textResourceDao.create(textResource);

        SourceFileTextResource sourceFileTextResource = new SourceFileTextResource(textResource.getId(), sourceFileId, textResource.getId());
        sourceFileTextResourceDao.create(sourceFileTextResource);
        return textResource;
    }

    public TextResource addTextResourceWithId(int sourceFileId, TextResource textResource) {

        textResource = textResourceDao.create(textResource);

        SourceFileTextResource sourceFileTextResource = new SourceFileTextResource();
        sourceFileTextResource.setSourceFileId(sourceFileId);
        sourceFileTextResource.setTextResourceId(textResource.getId());
        sourceFileTextResourceDao.create(sourceFileTextResource);
        return textResource;
    }

    public List<TextResource> getAllTextResourcesOfSouceFile(int sourceFileId) {
        return textResourceDao.selectAllTextResourcesOfSouceFile(sourceFileId);
    }

    public TextResource getTextResourceByResourceName(String rsrcName) {
        return textResourceDao.selectTextResourceByResourceName(rsrcName);
    }

    public long getTextResourceCountOfSouceFile(int sourceFileId) {
        return textResourceDao.selectTextResourceCountOfSouceFile(sourceFileId);
    }

    public int getMaxTextResourceIdOfSouceFile(int sourceFileId) {
        return textResourceDao.selectMaxTextResourceIdOfSouceFile(sourceFileId);
    }

    public TextResourceVO getTextResource(int sourceFileId, String resourceName) {
        return textResourceDao.selectTextResource(sourceFileId, resourceName);
    }

    public TextResource getTextResource(int id) {
        return textResourceDao.get(id);
    }

    public TextResource addTextResource(TextResource textRrsrc) {
        return textResourceDao.create(textRrsrc);
    }

    public TextResource addTextResource(TextResource textRrsrc, boolean autoIncrement) {
        return textResourceDao.create(textRrsrc, autoIncrement);
    }

    public TextResource updateTextResource(TextResource textRrsrc) {
        return textResourceDao.update(textRrsrc);
    }

    public void deleteTextResource(int id) {
        textItemDao.deleteTextItemByResourceId(id);
        sourceFileTextResourceDao.deleteByResourceId(id);
        textResourceDao.delete(id);
    }

    public TextItem getTextItem(int txtRsrcId, int langId) {
        return textItemDao.selectTextItem(txtRsrcId, langId);
    }

    public List<TextResourceItemVO> getResourceItemsOfSourceFile(int sourceFileId) {
        return textItemDao.selectTextResourceItemsOfSourceFile(sourceFileId);
    }

    public List<TextResourceItemVO> getResourceItemsOfSourceFile(int sourceFileId, String field, String contains) {
        return textItemDao.selectTextResourceItemsOfSourceFile(sourceFileId, field, contains);
    }

    public TextResourceItemVO saveTextResourceItem(TextResourceItemVO txtRsrcItem) {
        TextResource txtRsrc = textResourceDao.get(txtRsrcItem.getTextResourceId());
        String txtRsrcName = txtRsrcItem.getResourceName();
        if (txtRsrcName != null && !txtRsrcName.equals(txtRsrc.getResourceName())) {
            txtRsrc.setResourceName(txtRsrcName);
            textResourceDao.update(txtRsrc);
        }
        TextItem textItem = txtRsrcItem.getTextItem(Constants.LANG_EN);
        if (textItem != null && textItem.getId() > 0) {
            textItem = textItemDao.update(textItem);
        } else {
        	textItem.setId(null);
            textItem = textItemDao.create(textItem);
        }
        txtRsrcItem.putTextItem(Constants.LANG_EN, textItem);

        textItem = txtRsrcItem.getTextItem(Constants.LANG_FR);
        if (textItem != null && textItem.getId() > 0) {
            textItem = textItemDao.update(textItem);
        } else {
        	textItem.setId(null);
            textItem = textItemDao.create(textItem);
        }
        txtRsrcItem.putTextItem(Constants.LANG_FR, textItem);
        return txtRsrcItem;
    }

    public void deleteTextReSourceItem(int txtRsrcId) {
        textItemDao.deleteTextItemByResourceId(txtRsrcId);
        sourceFileTextResourceDao.deleteByResourceId(txtRsrcId);
        textResourceDao.delete(txtRsrcId);
    }

    public Map<Integer, Map<String, String>> getTextResourceMap() {
        return textItemDao.selectAllTextResourceMap();
    }

    public List<TextItem> getAllTextItems() {
        return textItemDao.findAll();
    }

    public TextItem getTextItem(int id) {
        return textItemDao.get(id);
    }

    public TextItem addTextItem(TextItem txtItem) {
        return textItemDao.create(txtItem);
    }

    public TextItem addTextItem(TextItem txtItem, boolean autoIncrement) {
        return textItemDao.create(txtItem, autoIncrement);
    }

    public TextItem updateTextItem(TextItem txtItem) {
        return textItemDao.update(txtItem);
    }

    public void deleteTextItem(int id) {
        textItemDao.delete(id);
    }

    @Autowired
    public void setSourceFileDao(SourceFileDAO sourceFileDao) {
        this.sourceFileDao = sourceFileDao;
    }

    @Autowired
    public void setSourceFileTextResourceDao(SourceFileTextResourceDAO sourceFileTextResourceDao) {
        this.sourceFileTextResourceDao = sourceFileTextResourceDao;
    }

    @Autowired
    public void setTextItemDao(TextItemDAO textItemDao) {
        this.textItemDao = textItemDao;
    }

    @Autowired
    public void setTextResourceDao(TextResourceDAO textResourceDao) {
        this.textResourceDao = textResourceDao;
    }
}
