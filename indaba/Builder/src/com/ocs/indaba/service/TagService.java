/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.service;

import com.ocs.indaba.dao.TagDAO;
import com.ocs.indaba.po.Tag;
import com.ocs.indaba.vo.TagContent;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeanbone
 */
public class TagService {
    private TagDAO tagDao;

    public Tag addTag(Tag tag) {
        return tagDao.insertTag(tag);
    }

    public List<String[]> selectTag(int horseId, String startWith) {
        return tagDao.selectTagLabel(horseId, startWith);
    }

    public List<TagContent> selectTagContent(int horseId, String label) {
        return tagDao.selectTagContent(horseId, label);
    }

    public List<String[]> selectTagLabelsByAnswerId(int answerId, int contentObjectId) {
        return tagDao.selectTagLabelsByAnswerId(answerId,contentObjectId);
    }

    public int deleteTag(Tag tag){
        return tagDao.deleteTag(tag);
    }
    
    @Autowired
    public void setTagDao(TagDAO tagDao) {
        this.tagDao = tagDao;
    }
}


