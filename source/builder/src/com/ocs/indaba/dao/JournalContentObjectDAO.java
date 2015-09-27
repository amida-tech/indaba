/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Attachment;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.JournalContentObject;
import com.ocs.indaba.util.ResultSetUtil;
import com.ocs.indaba.vo.JournalContentView;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import static java.sql.Types.INTEGER;

/**
 *
 * @author Luke Shi
 */
public class JournalContentObjectDAO extends SmartDaoMySqlImpl<JournalContentObject, Integer> {

    private static final Logger log = Logger.getLogger(JournalContentObjectDAO.class);
    private AttachmentDAO attachmentDao = null;
    private static final String SELECT_JOURNAL_CONTENT_BY_ID =
            "SELECT * FROM journal_content_object j, content_header c"
            + " WHERE j.id=? AND c.id=j.content_header_id";
    private static final String SELECT_JOURNAL_CONTENT_BY_ID_OR_HORSE_ID =
            "SELECT jc.min_words, jc.max_words, jcobj.id content_object_id, jcobj.body, ch.id content_header_id, ch.author_user_id, "
            + "ch.content_type, ch.create_time, ch.delete_time, ch.deleted_by_user_id, ch.horse_id, ch.internal_msgboard_id, "
            + "ch.last_update_time, ch.last_update_user_id, ch.staff_author_msgboard_id, ch.status, ch.title, ch.project_id, ch.submit_time, "
            + "jc.id journal_config_id, ch.reviewable, ch.editable, ch.peer_reviewable, ch.approvable "
            + "FROM journal_content_object jcobj, journal_config jc,  content_header ch"
            + " WHERE (jcobj.id=? OR ch.horse_id=?) AND ch.id=jcobj.content_header_id AND ch.content_object_id=jcobj.id AND jc.id=jcobj.journal_config_id";
    private static final String SELECT_JOURNAL_CONTENT_BY_HORSE_ID =
            "SELECT * FROM journal_content_object j, content_header c"
            + " WHERE c.horse_id=? AND c.id=j.content_header_id";
    private static final String UPDATE_JOURNAL_BODY_BY_ID =
            "UPDATE journal_content_object n SET n.body = ? WHERE n.id = ?";

    public JournalContentObject updateJournalContentObject(JournalContentObject journal) {
        return update(journal);
    }

    public JournalContentObject createJournalContentObject(JournalContentObject journal) {
        return create(journal);
    }

    /**
     * Select all of the notebooks
     *
     * @return notebook list.
     */
    public List<JournalContentObject> selectAllJournals() {
        log.debug("Select all rows in the table JournalContentObject.");
        return super.findAll();
    }

    /**
     * Select Journal Content by the specified horse id
     * @param horseId
     * @return
     */
    public JournalContentView selectJournalContentByHorseId(int horseId) {
        log.debug("Select journal content by Id: " + horseId + ". [" + SELECT_JOURNAL_CONTENT_BY_HORSE_ID + "]");
        RowMapper mapper = new JournalContentRowMapper();

        List<JournalContentView> list = getJdbcTemplate().query(SELECT_JOURNAL_CONTENT_BY_HORSE_ID,
                new Object[]{horseId},
                new int[]{INTEGER},
                mapper);

        if (list != null && list.size() > 0) {
            return list.get(0);
        }

        return null;
    }

    public JournalContentView getJournalContentById(int journalObjId) {
        log.debug("Select journal_content_object by Id: " + journalObjId + ". [" + SELECT_JOURNAL_CONTENT_BY_ID + "]");
        RowMapper mapper = new JournalContentRowMapper();

        List<JournalContentView> list = getJdbcTemplate().query(SELECT_JOURNAL_CONTENT_BY_ID,
                new Object[]{journalObjId},
                new int[]{INTEGER},
                mapper);

        if (list != null && list.size() > 0) {
            return list.get(0);
        }

        return null;
    }

    public JournalContentView getJournalContentByCntObjIdOrHorseId(int journalObjId, int horseId) {
        log.debug("Select journal_content_object view by id: " + journalObjId + " or horse id: " + horseId + ". [" + SELECT_JOURNAL_CONTENT_BY_ID_OR_HORSE_ID + "]");
        RowMapper mapper = new JournalContentRowMapper();

        List<JournalContentView> list = getJdbcTemplate().query(SELECT_JOURNAL_CONTENT_BY_ID_OR_HORSE_ID,
                new Object[]{journalObjId, horseId},
                new int[]{INTEGER, INTEGER},
                mapper);

        if (list != null && list.size() > 0) {
            return list.get(0);
        }

        return null;
    }

    /**
     * select the notebook by the specified id.
     *
     * @param id
     * @return
     */
    public JournalContentObject selectJournalById(int journalObjectId) {
        log.debug("Select table journal_content_object by id: " + journalObjectId);

        return super.get(journalObjectId);
    }

    public void updateJournalById(long journal_object_id, String body) throws SQLException {
        log.debug("Update table journal.");
        System.out.println("id=" + journal_object_id + ", new body=" + body);

        Object[] values = new Object[]{body, journal_object_id};
        this.getJdbcTemplate().update(UPDATE_JOURNAL_BODY_BY_ID, values);
    }

    public List<Attachment> getAttachmentsByCntHdrId(int cntHdrId) {
        return attachmentDao.selectAttachmentsByCntHdrId(cntHdrId);
    }

    public List<Attachment> getAttachmentsByHorseId(int horseId) {
        return attachmentDao.selectAttachmentsByHorseId(horseId);
    }
    
    public Attachment getAttachmentById(int attachmentId) {
        return attachmentDao.selectAttachmentById(attachmentId);
    }

    public void deleteAttachmentById(int attachmentId) {
        attachmentDao.deleteAttachmentById(attachmentId);
    }

    public Attachment addAttachment(Attachment attachment) {
        return attachmentDao.insertAttachment(attachment);
    }

    public Attachment updateAttachment(Attachment attachment) {
        return attachmentDao.updateAttachment(attachment);
    }

    public Attachment getAttachmentByName(String filename) {
          return attachmentDao.selectAttachmentByName(filename);
    }

    @Autowired
    public void setAttachmentDAO(AttachmentDAO attachmentDao) {
        this.attachmentDao = attachmentDao;
    }
}

class JournalContentRowMapper implements RowMapper {

    private static final Logger log = Logger.getLogger(JournalContentRowMapper.class);

    public JournalContentView mapRow(ResultSet rs, int rowNum) throws SQLException {
        JournalContentView journalCntView = new JournalContentView();

        journalCntView.setMinWords(ResultSetUtil.getInt(rs, "min_words"));
        journalCntView.setMaxWords(ResultSetUtil.getInt(rs, "max_words"));

        JournalContentObject cntObj = new JournalContentObject();
        ContentHeader cntHdr = new ContentHeader();
        cntObj.setId(ResultSetUtil.getInt(rs, "content_object_id"));
        cntObj.setBody(ResultSetUtil.getString(rs, "body"));
        cntObj.setContentHeaderId(ResultSetUtil.getInt(rs, "content_header_id"));
        cntObj.setJournalConfigId(ResultSetUtil.getInt(rs, "journal_config_id"));
        //cntObj.setTaskId(ResultSetUtil.getInt(rs, "task_id"));

        cntHdr.setAuthorUserId(ResultSetUtil.getInt(rs, "author_user_id"));
        cntHdr.setContentObjectId(ResultSetUtil.getInt(rs, "content_object_id"));
        cntHdr.setSubmitTime(ResultSetUtil.getDate(rs, "submit_time"));
        cntHdr.setId(ResultSetUtil.getInt(rs, "content_header_id"));
        cntHdr.setContentType(ResultSetUtil.getInt(rs, "content_type"));
        cntHdr.setCreateTime(ResultSetUtil.getDate(rs, "create_time"));
        cntHdr.setDeleteTime(ResultSetUtil.getDate(rs, "delete_time"));
        cntHdr.setDeletedByUserId(ResultSetUtil.getInt(rs, "deleted_by_user_id"));
        cntHdr.setHorseId(ResultSetUtil.getInt(rs, "horse_id"));
        cntHdr.setInternalMsgboardId(ResultSetUtil.getInt(rs, "internal_msgboard_id"));
        cntHdr.setLastUpdateTime(ResultSetUtil.getDate(rs, "last_update_time"));
        cntHdr.setLastUpdateUserId(ResultSetUtil.getInt(rs, "last_update_user_id"));
        cntHdr.setStaffAuthorMsgboardId(ResultSetUtil.getInt(rs, "staff_author_msgboard_id"));
        cntHdr.setStatus(ResultSetUtil.getShort(rs, "status"));
        cntHdr.setTitle(ResultSetUtil.getString(rs, "title"));
        cntHdr.setProjectId(ResultSetUtil.getInt(rs, "project_id"));
        cntHdr.setReviewable(ResultSetUtil.getBoolean(rs, "reviewable"));
        cntHdr.setEditable(ResultSetUtil.getBoolean(rs, "editable"));
        cntHdr.setPeerReviewable(ResultSetUtil.getBoolean(rs, "peer_reviewable"));
        cntHdr.setApprovable(ResultSetUtil.getBoolean(rs, "approvable"));
        journalCntView.setContentHeader(cntHdr);
        journalCntView.setJournalContentObject(cntObj);
        return journalCntView;
    }
}
