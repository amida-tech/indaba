/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.po.NoteobjIntl;
import com.ocs.indaba.vo.NoteobjIntlView;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author yc06x
 */
public class NoteobjIntlDAO extends SmartDaoMySqlImpl<NoteobjIntl, Integer> {

    static private final String SELECT_BY_NOTEOBJ_ORDER_BY_CREATE_TIME =
            "SELECT * FROM noteobj_intl WHERE noteobj_id=? ORDER BY create_time ASC";

    public List<NoteobjIntl> selectByNoteobjOrderByCreateTime(int noteobjId) {
        return super.find(SELECT_BY_NOTEOBJ_ORDER_BY_CREATE_TIME, noteobjId);
    }


    public NoteobjIntl getDefault(int noteobjId) {
        List<NoteobjIntl> list = selectByNoteobjOrderByCreateTime(noteobjId);

        if (list == null || list.isEmpty()) return null;

        return list.get(0);
    }


    static private final String SELECT_BY_NOTEOBJ_AND_LANGUAGE =
            "SELECT * FROM noteobj_intl WHERE noteobj_id=? AND language_id=?";

    public NoteobjIntl selectByNoteobjAndLanguage(int noteobjId, int langId) {
        return super.findSingle(SELECT_BY_NOTEOBJ_AND_LANGUAGE, noteobjId, langId);
    }


    public NoteobjIntl saveText(int userId, int noteobjId, int langId, String text) {
        NoteobjIntl noi = selectByNoteobjAndLanguage(noteobjId, langId);

        Date now = new Date();
        if (noi == null) {
            noi = new NoteobjIntl();
            noi.setId(null);
            noi.setCreateTime(now);
            noi.setCreateUserId(userId);
            noi.setLanguageId(langId);
            noi.setNoteobjId(noteobjId);
        }
        noi.setLastUpdateTime(now);
        noi.setLastUpdateUserId(userId);
        noi.setNote(text);

        if (noi.getId() == null) {
            return this.create(noi);
        } else {
            return this.update(noi);
        }
    }


    static private final String SELECT_BY_HORSE =
            "SELECT noi.*, no.notedef_id, no.survey_question_id, lang.language_desc " +
            "FROM noteobj_intl noi, noteobj no, language lang " +
            "WHERE no.horse_id=? AND noi.noteobj_id = no.id AND lang.id=noi.language_id " +
            "ORDER BY no.survey_question_id, no.notedef_id, noi.language_id";



    public List<NoteobjIntlView> selectByHorseId(final int horseId) {
        RowMapper mapper = new RowMapper() {

            public NoteobjIntlView mapRow(ResultSet rs, int rowNum) throws SQLException {
                NoteobjIntlView vo = new NoteobjIntlView();

                vo.setHorseId(horseId);
                vo.setId(rs.getInt("noi.id"));
                vo.setLanguageId(rs.getInt("noi.language_id"));
                vo.setNote(rs.getString("noi.note"));
                vo.setNotedefId(rs.getInt("no.notedef_id"));
                vo.setSurveyQuestionId(rs.getInt("no.survey_question_id"));
                vo.setLanguage(rs.getString("lang.language_desc"));

                return vo;
            }
        };

        List<NoteobjIntlView> list = getJdbcTemplate().query(SELECT_BY_HORSE,
                new Object[]{horseId}, mapper);
        return list;
    }
}
