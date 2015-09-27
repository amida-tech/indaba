/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.TextItem;
import com.ocs.indaba.vo.TextResourceItemVO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Jeff
 */
public class TextItemDAO extends SmartDaoMySqlImpl<TextItem, Integer> {

    private static final Logger log = Logger.getLogger(TextItemDAO.class);
    private static final String SELECT_TEXT_ITEM_BY_RESOURCE_ID_AND_LANG_ID =
            "SELECT * FROM text_item WHERE text_resource_id=? AND language_id=?";
    private static final String DELETE_TEXT_ITEM_BY_RESOURCE_ID =
            "DELETE FROM text_item WHERE text_resource_id=?";
    private static final String DELETE_TEXT_ITEMS_BY_RESOURCE_ID_SET =
            "DELETE FROM text_item WHERE text_resource_id IN {0}";
    private static final String SELECT_TEXT_ITEM_BY_SOURCE_FILE_ID =
            "SELECT sftr.source_file_id source_file_id, tr.id text_resource_id, tr.resource_name resource_name, tr.description description, "
            + "ti.id text_item_id, ti.language_id language_id, ti.text txt FROM source_file_text_resource sftr, text_resource tr, text_item ti "
            + "WHERE sftr.source_file_id=? AND sftr.text_resource_id=tr.id AND tr.id=ti.text_resource_id";
    private static final String SELECT_TEXT_ITEM_BY_SOURCE_FILE_ID_LIKE_RESOURCE_NAME =
            "SELECT sftr.source_file_id source_file_id, tr.id text_resource_id, tr.resource_name resource_name, tr.description description, "
            + "ti.id text_item_id, ti.language_id language_id, ti.text txt FROM source_file_text_resource sftr, text_resource tr, text_item ti "
            + "WHERE sftr.source_file_id=? AND sftr.text_resource_id=tr.id AND tr.id=ti.text_resource_id AND tr.resource_name LIKE ''%{0}%''";
    private static final String SELECT_TEXT_ITEM_BY_SOURCE_FILE_ID_LIKE_TEXT =
            "SELECT sftr.source_file_id source_file_id, tr.id text_resource_id, tr.resource_name resource_name, tr.description description, "
            + "ti.id text_item_id, ti.language_id language_id, ti.text txt FROM source_file_text_resource sftr, text_resource tr, text_item ti "
            + "WHERE sftr.source_file_id=? AND sftr.text_resource_id=tr.id AND tr.id=ti.text_resource_id AND ti.text LIKE ''%{0}%''";
    private static final String SELECT_ALL_TEXT_ITEMS =
            "SELECT tr.id text_resource_id, tr.resource_name resource_name, tr.description description, ti.id text_item_id, "
            + "ti.language_id language_id, ti.text txt FROM text_resource tr, text_item ti "
            + "WHERE tr.id=ti.text_resource_id";

    public TextItem selectTextItem(int txtRsrcId, int langId) {
        return super.findSingle(SELECT_TEXT_ITEM_BY_RESOURCE_ID_AND_LANG_ID, txtRsrcId, langId);
    }

    public void deleteTextItemByResourceId(int txtRsrcId) {
        super.delete(DELETE_TEXT_ITEM_BY_RESOURCE_ID, txtRsrcId);
    }

    public void deleteTextItemsByResourceIds(List<Integer> txtRsrcIds) {
        super.delete(MessageFormat.format(DELETE_TEXT_ITEMS_BY_RESOURCE_ID_SET, txtRsrcIds.toString().replaceAll("[\\[|\\]]", "")));
    }

    public List<TextResourceItemVO> selectTextResourceItemsOfSourceFile(int sourceFileId) {
        TextResourceItemRowMapper mapper = new TextResourceItemRowMapper();
        getJdbcTemplate().query(SELECT_TEXT_ITEM_BY_SOURCE_FILE_ID,
                new Object[]{sourceFileId},
                mapper);
        mapper.destroy();
        return mapper.getTextItemResources();
    }

    public List<TextResourceItemVO> selectTextResourceItemsOfSourceFile(int sourceFileId, String field, String contains) {
        TextResourceItemRowMapper mapper = new TextResourceItemRowMapper();
        if ("rsrcName".equals(field)) {
            getJdbcTemplate().query(MessageFormat.format(SELECT_TEXT_ITEM_BY_SOURCE_FILE_ID_LIKE_RESOURCE_NAME, contains),
                    new Object[]{sourceFileId},
                    mapper);
        } else {
            getJdbcTemplate().query(MessageFormat.format(SELECT_TEXT_ITEM_BY_SOURCE_FILE_ID_LIKE_TEXT, contains),
                    new Object[]{sourceFileId},
                    mapper);
        }
        mapper.destroy();
        return mapper.getTextItemResources();
    }

    public Map<Integer, Map<String, String>> selectAllTextResourceMap() {
        Map<Integer, Map<String, String>> textRrscMap = new HashMap<Integer, Map<String, String>>();
        getJdbcTemplate().query(SELECT_ALL_TEXT_ITEMS, new TextResourcesMapper(textRrscMap));
        return textRrscMap;
    }

    class TextResourcesMapper implements RowMapper {

        Map<Integer, Map<String, String>> textRrscMap = null;

        public TextResourcesMapper(Map<Integer, Map<String, String>> textRrscMap) {
            this.textRrscMap = textRrscMap;
        }

        public Map<Integer, Map<String, String>> mapRow(ResultSet rs, int i) throws SQLException {
            int langId = rs.getInt("language_id");
            Map<String, String> langRsrcMap = textRrscMap.get(langId);
            if (langRsrcMap == null) {
                langRsrcMap = new HashMap<String, String>();
                textRrscMap.put(langId, langRsrcMap);
            }
//            if("common.js.alert.attachfile".equals(rs.getString("resource_name"))){
//            	System.err.println( rs.getString("txt")+"_"+langId);
//            }
            langRsrcMap.put(rs.getString("resource_name"), rs.getString("txt"));
            return textRrscMap;
        }
    }

    class TextResourceItemRowMapper implements RowMapper {

        private Map<Integer, TextResourceItemVO> textRrscMap = null;
        private List<TextResourceItemVO> txtItemRsrclist = null;

        public TextResourceItemRowMapper() {
            txtItemRsrclist = new ArrayList<TextResourceItemVO>();
            textRrscMap = new HashMap<Integer, TextResourceItemVO>();
        }

        public List<TextResourceItemVO> getTextItemResources() {
            return txtItemRsrclist;
        }

        public TextResourceItemVO mapRow(ResultSet rs, int i) throws SQLException {
            TextItem txtItem = new TextItem();
            txtItem.setId(rs.getInt("text_item_id"));
            txtItem.setTextResourceId(rs.getInt("text_resource_id"));
            txtItem.setLanguageId(rs.getInt("language_id"));
            txtItem.setText(rs.getString("txt"));
            int txtRsrcId = rs.getInt("text_resource_id");
            TextResourceItemVO vo = textRrscMap.get(txtRsrcId);
            if (vo == null) {
                vo = new TextResourceItemVO();
                vo.setSourceFileId(rs.getInt("source_file_id"));
                vo.setTextResourceId(txtRsrcId);
                vo.setResourceName(rs.getString("resource_name"));
                vo.setDescription(rs.getString("description"));
                textRrscMap.put(vo.getTextResourceId(), vo);
                txtItemRsrclist.add(vo);
            }
            vo.putTextItem(txtItem.getLanguageId(), txtItem);
            return vo;
        }

        public void destroy() {
            textRrscMap.clear();
            textRrscMap = null;
        }
    }
}
