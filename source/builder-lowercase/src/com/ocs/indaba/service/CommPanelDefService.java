/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.service;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.GroupdefDAO;
import com.ocs.indaba.dao.GroupdefRoleDAO;
import com.ocs.indaba.dao.GroupdefUserDAO;
import com.ocs.indaba.dao.NotedefDAO;
import com.ocs.indaba.dao.NotedefRoleDAO;
import com.ocs.indaba.dao.NotedefUserDAO;
import com.ocs.indaba.dao.NoteobjDAO;
import com.ocs.indaba.dao.NoteobjIntlDAO;
import com.ocs.indaba.po.Groupdef;
import com.ocs.indaba.po.GroupdefRole;
import com.ocs.indaba.po.GroupdefUser;
import com.ocs.indaba.po.Notedef;
import com.ocs.indaba.po.NotedefRole;
import com.ocs.indaba.po.NotedefUser;
import com.ocs.indaba.vo.GroupdefActionResult;
import com.ocs.indaba.vo.GroupdefView;
import com.ocs.indaba.vo.NotedefActionResult;
import com.ocs.indaba.vo.NotedefView;
import com.ocs.util.Pagination;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ningshan
 */
public class CommPanelDefService {
    private static final Logger logger = Logger.getLogger(CommPanelDefService.class);
    @Autowired
    private NotedefDAO notedefDao = null;
    
    @Autowired
    private NotedefUserDAO notedefUserDao = null;
    
    @Autowired
    private NotedefRoleDAO notedefRoleDao = null;

    @Autowired
    private NoteobjDAO noteobjDao = null;

    @Autowired
    private NoteobjIntlDAO noteobjIntlDao = null;
    
    @Autowired
    private GroupdefDAO groupdefDao = null;
    
    @Autowired
    private GroupdefUserDAO groupdefUserDao = null;
    
    @Autowired
    private GroupdefRoleDAO groupdefRoleDao = null;


    
    public Pagination<NotedefView> getNotedefViewsByProductId(int productId, String sortName, String sortOrder, int page, int pageSize) {
        try {
            logger.debug("Enter getNotedefViewsByProductId");
            int offset = (page - 1) * pageSize;
            int count = pageSize;
            long totalCount = notedefDao.countNotedefsByProductId(productId, false);
            List<NotedefView> notedefs = notedefDao.selectNotedefViewsByProductId(productId, false, sortName, sortOrder, offset, count);

            Pagination<NotedefView> pagination = new Pagination<NotedefView>(totalCount, page, pageSize);
            pagination.setRows(notedefs);
            logger.debug(notedefs);
            return pagination;
        }
        finally {
            logger.debug("Exit getNotedefViewsByProductId");
        }
    }

    public Pagination<GroupdefView> getGroupdefViewsByProductId(int productId, String sortName, String sortOrder, int page, int pageSize) {
        try {
            logger.debug("Enter getGroupdefViewsByProductId");
            int offset = (page - 1) * pageSize;
            int count = pageSize;
            long totalCount = groupdefDao.countGroupdefsByProductId(productId, false);
            List<GroupdefView> groupdefs = groupdefDao.selectGroupdefViewsByProductId(productId, false, sortName, sortOrder, offset, count);

            Pagination<GroupdefView> pagination = new Pagination<GroupdefView>(totalCount, page, pageSize);
            pagination.setRows(groupdefs);
            logger.debug(groupdefs);
            return pagination;
        }
        finally {
            logger.debug("Exit getGroupdefViewsByProductId");
        }
    }
    public GroupdefView getGroupdefViewsByGroupdefID(int groupdefId) {
        try {
            logger.debug("Enter getGroupdefViewsByNotedefID(" + groupdefId+")");
            GroupdefView vw  = groupdefDao.selectGroupdefViewByNotedefId(groupdefId);
            logger.debug(vw);
            return vw;
        }
        finally {
            logger.debug("Exit getGroupdefViewsByNotedefID");
        }
    }
    
    public NotedefView getNotedefViewsByNotedefID(int notedefId) {
        try {
            logger.debug("Enter getNotedefViewsByNotedefID(" + notedefId+")");
            NotedefView nv  = notedefDao.selectNotedefViewByNotedefId(notedefId);
            logger.debug(nv);
            return nv;
        }
        finally {
            logger.debug("Exit getNotedefViewsByNotedefID");
        }
    }
    
    public int deleteNotedef(int notdefId) {
        int rc = notedefDao.call(Constants.PROCEDURE_DELETE_NOTEDEF, notdefId);
        logger.debug("Stored proc RC: " + rc);
        return rc;
    }


    public int disableNotedef(int notedefId) {
        notedefDao.setEnabled(notedefId, false);
        return 0;
    }


    public int enableNotedef(int notedefId) {
        notedefDao.setEnabled(notedefId, true);
        return 0;
    }

    
    public int deleteGroupdef(int groupdefId) {
        int rc = groupdefDao.call(Constants.PROCEDURE_DELETE_GROUPDEF, groupdefId);
        logger.debug("Stored proc RC: " + rc);
        return rc;
    }


    public int disableGroupdef(int groupdefId) {
        groupdefDao.setEnabled(groupdefId, false);
        return 0;
    }


    public int enableGroupdef(int groupdefId) {
        groupdefDao.setEnabled(groupdefId, true);
        return 0;
    }

    
    public NotedefActionResult save(Notedef notedef, List<NotedefUser> users, List<NotedefRole> roles) {
        NotedefActionResult result = new NotedefActionResult();
        notedef = notedefDao.save(notedef, users, roles);
        if (notedef == null) {
            result.setCode(NotedefActionResult.RESULT_CODE_ERROR);
            result.setErrMessage("Can not update Notedef");
        }
        else {
            result.setCode(NotedefActionResult.RESULT_CODE_OK);
            result.setNotedef(notedef);
        }
        return result;
    }

    
    public GroupdefActionResult save(Groupdef groupdef, List<GroupdefUser> users, List<GroupdefRole> roles) {
        GroupdefActionResult result = new GroupdefActionResult();
        groupdef = groupdefDao.save(groupdef, users, roles);
        if (groupdef == null) {
            result.setCode(GroupdefActionResult.RESULT_CODE_ERROR);
            result.setErrMessage("Can not update Notedef");
        }
        else {
            result.setCode(GroupdefActionResult.RESULT_CODE_OK);
            result.setGroupdef(groupdef);
        }
        return result;
    }
    
}
