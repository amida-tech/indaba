/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.dao.NoteBookDAO;
import com.ocs.indaba.vo.NoteBook;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class NoteBookService {

    private static final Logger logger = Logger.getLogger(NoteBookService.class);
    private NoteBookDAO noteBookDao;

    public void updateNoteBook(NoteBook notebook) {
        logger.debug("Save notebook.");
        noteBookDao.saveNoteBook(notebook);
    }

    public NoteBook getNoteBookById(long id) {
        logger.debug("Get notebook by id: " + id);
        NoteBook notebook = noteBookDao.selectNoteBookById(id);
        return notebook;
    }

    public List<NoteBook> getNoteBookByOwnerId(int ownerId) {
        logger.debug("Get notebook by owner id: " + ownerId);
        List<NoteBook> notebooks = noteBookDao.selectNoteBooksByOwnerId(ownerId);
        return notebooks;
    }

    @Autowired
    public void setNoteBookDao(NoteBookDAO noteBookDao) {
        this.noteBookDao = noteBookDao;
    }
}
