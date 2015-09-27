/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.dao.NoteBookMessageDAO;
//import com.ocs.indaba.po.NoteBookMessage;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class NoteBookMessageService {

    private static final Logger logger = Logger.getLogger(NoteBookMessageService.class);
    private NoteBookMessageDAO mbMsgDao = null;

    /*public void addNoteBookMessage(NoteBookMessage nbMsg) {
    logger.debug("Save notebook.");
    mbMsgDao.insertNoteBookMessage(nbMsg);
    }

    public List<NoteBookMessage> getNoteBookMessageByOwnerId(long ownerId) {
    logger.debug("Get notebook by owner id: " + ownerId);
    List<NoteBookMessage> nbMsgList = mbMsgDao.selectNoteBookMessagesByOwnerId(ownerId);
    return nbMsgList;
    }*/

    @Autowired
    public void setNbmsgDao(NoteBookMessageDAO mbMsgDao) {
        this.mbMsgDao = mbMsgDao;
    }
}
