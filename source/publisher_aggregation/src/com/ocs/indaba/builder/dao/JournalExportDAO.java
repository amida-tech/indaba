/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.builder.dao;

import com.ocs.indaba.dao.BaseDAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author sjf
 */
public class JournalExportDAO extends BaseDAO {

    private static final Logger log = Logger.getLogger(JournalExportDAO.class);
    private static final String SELECT_HORSE_ID_BY_PRODUCT_ID_TARGET_ID = "select id from horse where product_id = ? and target_id = ?";
    private static final String SELECT_EXPORTABLE_ITEMS_BY_HORSE_ID =
            "SELECT jc.exportable_items FROM journal_config jc "
            + "JOIN journal_content_object jco ON (jc.id = jco.journal_config_id) "
            + "JOIN content_header ch ON (ch.id = jco.content_header_id) "
            + "JOIN horse h ON (h.id = ch.horse_id) "
            + "WHERE h.id = ?";

    public Integer selectExportableItemsByHorseId(int horseId) {
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getInt("exportable_items");
            }
        };

        List<Integer> list = getJdbcTemplate().query(
                SELECT_EXPORTABLE_ITEMS_BY_HORSE_ID, new Object[]{horseId}, mapper);
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }

    public Integer selectHorseIdbyProductIdandTargetId(int productId, int targetId) {
        log.debug("Select horse id by product_id=" + productId + " and target_id=" + targetId);
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {

                return rs.getInt("id");
            }
        };
        List<Integer> list = getJdbcTemplate().query(
                SELECT_HORSE_ID_BY_PRODUCT_ID_TARGET_ID,
                new Object[]{productId, targetId},
                mapper);
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }
}
