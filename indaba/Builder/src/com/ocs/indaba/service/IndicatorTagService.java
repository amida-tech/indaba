/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.dao.IndicatorTagDAO;
import com.ocs.indaba.dao.ItagsDAO;
import com.ocs.indaba.po.IndicatorTag;
import com.ocs.indaba.po.Itags;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class IndicatorTagService {

    private ItagsDAO itagsDao;
    private IndicatorTagDAO indicatorTagDao;

    @Autowired
    public void setItagsDAO(ItagsDAO itagsDao) {
        this.itagsDao = itagsDao;
    }

    @Autowired
    public void setIndicatorTagDAO(IndicatorTagDAO indicatorTagDao) {
        this.indicatorTagDao = indicatorTagDao;
    }

    public List<Itags> getAllITags() {
        return itagsDao.findAll();
    }

    public List<IndicatorTag> getIndicatorTagsByIndicatorId(int indicatorId) {
        return indicatorTagDao.getIndicatorTagsByIndicatorId(indicatorId);
    }

    public void deleteIndicatorTagsByIndicatorId(int indicatorId) {
        indicatorTagDao.deleteIndicatorTagsByIndicatorId(indicatorId);
    }

    public List<IndicatorTag> addIndicatorTags(int indicatorId, List<Integer> itags) {
        List<IndicatorTag> indicatorTags = new ArrayList<IndicatorTag>();
        if (itags == null || itags.isEmpty()) {
            return indicatorTags;
        }
        for (int tagId : itags) {
            IndicatorTag itag = addIndicatorTag(indicatorId, tagId);
            indicatorTags.add(itag);
        }
        return indicatorTags;
    }

    public IndicatorTag addIndicatorTag(int indicatorId, int itagId) {
        IndicatorTag indicatorTag = new IndicatorTag();
        indicatorTag.setItagsId(itagId);
        indicatorTag.setSurveyIndicatorId(indicatorId);
        return indicatorTagDao.create(indicatorTag);
    }
}
