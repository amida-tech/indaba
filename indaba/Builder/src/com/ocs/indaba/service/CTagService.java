/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
*/

package com.ocs.indaba.service;

import com.ocs.indaba.dao.CTagDAO;
import com.ocs.indaba.po.Ctags;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author menglong
 */
public class CTagService {
    
    private CTagDAO ctagDAO;

    public List<Ctags> getAllCTags() {
        return ctagDAO.selectAllTags();
    }

    public List<Ctags> getAllCTagsOrderByTerm() {
        return ctagDAO.selectAllTagsOrderByTerm();
    }

    @Autowired
    public void setCtagDao(CTagDAO ctagDAO) {
        this.ctagDAO = ctagDAO;
    }
}
