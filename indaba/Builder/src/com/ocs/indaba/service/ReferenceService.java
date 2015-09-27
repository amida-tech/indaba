/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
*/

package com.ocs.indaba.service;

import com.ocs.indaba.dao.ReferenceChoiceDAO;
import com.ocs.indaba.dao.ReferenceDAO;
import com.ocs.indaba.dao.ReferenceObjectDAO;
import com.ocs.indaba.po.Reference;
import com.ocs.indaba.po.ReferenceChoice;
import com.ocs.indaba.po.ReferenceObject;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author luwb
 */
public class ReferenceService {
    private static final Logger logger = Logger.getLogger(ReferenceService.class);

    private ReferenceDAO referenceDao = null;
    private ReferenceChoiceDAO referenceChoiceDao = null;
    private ReferenceObjectDAO referenceObjectDao = null;

    @Autowired
    public void setReferenceDAO(ReferenceDAO referenceDao) {
        this.referenceDao = referenceDao;
    }

    @Autowired
    public void setReferenceChoiceDAO(ReferenceChoiceDAO referenceChoiceDao) {
        this.referenceChoiceDao = referenceChoiceDao;
    }

    @Autowired
    public void setReferenceObjectDAO(ReferenceObjectDAO referenceObjectDao) {
        this.referenceObjectDao = referenceObjectDao;
    }
    
    public List<Reference> getAllReferences(){
        return referenceDao.findAll();
    }
    
    public Reference getReference(int referId){
        return referenceDao.selectReferenceById(referId);
    }

    public List<ReferenceChoice> getReferenceChoice(int referId){
        return referenceChoiceDao.selectReferenceByReferId(referId);
    }

    public ReferenceObject getReferenceObject(int referObjectId){
        return referenceObjectDao.getReferenceObjectById(referObjectId);
    }

    public List<ReferenceObject> getReferenceObjectsByHorseId(int horseId) {
        return referenceObjectDao.selectReferenceObjectsByHorseId(horseId);
    }

    public List<ReferenceObject> getReferenceObjectsByProductId(int productId) {
        return referenceObjectDao.selectReferenceObjectsByProductId(productId);
    }
}
