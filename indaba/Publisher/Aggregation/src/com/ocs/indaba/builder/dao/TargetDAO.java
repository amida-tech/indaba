/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.builder.dao;

import com.ocs.indaba.aggregation.vo.HorseBriefVO;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Target;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Jeanbone
 */
public class TargetDAO extends SmartDaoMySqlImpl<Target, Integer> {

    private String SELECT_TARGETS_BY_PRODUCT_ID =
            "SELECT DISTINCT t.* FROM target t "
            + "JOIN horse h ON (h.target_id = t.id) "
            + "JOIN workflow_object wo ON (h.workflow_object_id = wo.id) "
            + "WHERE h.product_id = ? AND wo.status = 3";

    private String SELECT_TARGETS_BY_PRODUCT_ID_FOR_EXPORT =
            "SELECT DISTINCT t.* FROM target t "
            + "JOIN horse h ON (h.target_id = t.id) "
            + "JOIN workflow_object wo ON (h.workflow_object_id = wo.id) "
            + "WHERE h.product_id = ? AND wo.status != 5 "
            + "ORDER BY t.id";

    private String SELECT_TARGETS_BY_PRODUCT_IDS =
            "SELECT DISTINCT t.* FROM target t "
            + "JOIN horse h ON (h.target_id = t.id) "
            + "JOIN workflow_object wo ON (h.workflow_object_id = wo.id) "
            + "WHERE h.product_id IN #1 AND wo.status = 3";
    private String SELECT_TARGET_TAG_TERMS_BY_TARGET_ID =
            "SELECT DISTINCT ttags.term FROM ttags "
            + "JOIN target_tag tt ON (tt.ttags_id = ttags.id) "
            + "JOIN target t ON (tt.target_id = t.id) "
            + "WHERE t.id = ?";
    private String SELECT_TARGETS_BY_IDS =
            "SELECT * FROM target WHERE id IN #1";
    private String SELECT_TARGETS_BY_WORKSET_ID =
            "SELECT t.* FROM `indaba`.`target` t "
            + "JOIN `indaba_publisher`.`ws_target` wt ON (wt.target_id = t.id) "
            + "WHERE wt.workset_id = ?";
    private final static String SELECT_NOT_CANCELLED_TARGETS_FOR_PRODUCT = "SELECT t.id target_id, t.name target_name, h.id horse_id FROM target t, horse h, "
            + "workflow_object wfo WHERE h.product_id=? AND t.id=h.target_id AND h.workflow_object_id=wfo.id "
            + "AND wfo.status!=5";
    private final static String SELECT_COMPLETED_TARGETS_FOR_PRODUCT = "SELECT t.id target_id, t.name target_name, h.id horse_id FROM target t, horse h, "
            + "workflow_object wfo WHERE h.product_id=? AND t.id=h.target_id AND h.workflow_object_id=wfo.id "
            + "AND wfo.status=3";
    private final static String SELECT_ALL_TARGET_ID_ORDER_BY_PRODUCT_ID =
            "SELECT id, product_id, target_id FROM horse ORDER BY product_id";
    private final static String SELECT_ALL_HORSE_TARGET_MAP = "SELECT h.id horse_id, t.short_name target_name FROM horse h, target t WHERE h.target_id=t.id";
    private final static String SELECT_TARGET_BY_HORSE_ID = "SELECT h.id horse_id, t.short_name target_name FROM horse h, target t WHERE h.target_id=t.id AND h.id=?";

    public Map<String, String> selectAllHorseTargetMap() {
        final Map<String, String> map = new HashMap<String, String>();
        getJdbcTemplate().query(SELECT_ALL_HORSE_TARGET_MAP,
                new Object[]{}, new RowMapper() {

            public Map<String, String> mapRow(ResultSet rs, int i) throws SQLException {
                map.put(String.valueOf(rs.getInt("horse_id")), rs.getString("target_name"));
                return map;
            }
        });
        return map;
    }

    public String selectTargetByHorseId(int horseId) {
        List<String> list = getJdbcTemplate().query(SELECT_TARGET_BY_HORSE_ID,
                new Object[]{horseId}, new RowMapper() {

            public String mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString("target_name");
            }
        });
        return (list != null && !list.isEmpty()) ? list.get(0) : "";
    }

    public List<HorseBriefVO> selectNotCancelledTargetsForProduct(int productId) {

        return getJdbcTemplate().query(SELECT_NOT_CANCELLED_TARGETS_FOR_PRODUCT,
                new Object[]{productId}, new RowMapper() {

            public HorseBriefVO mapRow(ResultSet rs, int i) throws SQLException {
                HorseBriefVO horseBriefVO = new HorseBriefVO();
                horseBriefVO.setHorseId(rs.getInt("horse_id"));
                horseBriefVO.setTargetId(rs.getInt("target_id"));
                horseBriefVO.setTargetName(rs.getString("target_name"));
                return horseBriefVO;
            }
        });

    }

    public List<HorseBriefVO> selectCompletedTargetsForProduct(int productId) {

        return getJdbcTemplate().query(SELECT_COMPLETED_TARGETS_FOR_PRODUCT,
                new Object[]{productId}, new RowMapper() {

            public HorseBriefVO mapRow(ResultSet rs, int i) throws SQLException {
                HorseBriefVO horseBriefVO = new HorseBriefVO();
                horseBriefVO.setHorseId(rs.getInt("horse_id"));
                horseBriefVO.setTargetId(rs.getInt("target_id"));
                horseBriefVO.setTargetName(rs.getString("target_name"));
                return horseBriefVO;
            }
        });

    }

    public List<Integer> selectTargetIdsByWorksetId(int worksetId) {
        List<Target> targets = selectTargetsByWorksetId(worksetId);
        List<Integer> ids = new ArrayList<Integer>();
        for (Target t : targets) {
            ids.add(t.getId());
        }
        return ids;
    }

    public List<Target> selectTargetsByWorksetId(int id) {
        return super.find(SELECT_TARGETS_BY_WORKSET_ID, id);
    }

    public List<Target> selectTargetsByIds(List<Integer> ids) {
        String idsStr = ids.toString().replace("[", "(").replace("]", ")");
        return super.find(SELECT_TARGETS_BY_IDS.replace("#1", idsStr));
    }

    public List<Target> selectTargetsByProductIds(List<Integer> productIds) {
        String queryStr = SELECT_TARGETS_BY_PRODUCT_IDS.replace("#1",
                productIds.toString().replace("[", "(").replace("]", ")"));
        return super.find(queryStr);
    }

    public List<String> selectTargetTagTermsByTargetId(int targetId) {
        return super.getJdbcTemplate().query(SELECT_TARGET_TAG_TERMS_BY_TARGET_ID,
                new Object[]{targetId}, new TargetTagTermRowMapper());
    }

    public List<Target> selectTargetsByProductIdForExport(int productId) {
        return super.find(SELECT_TARGETS_BY_PRODUCT_ID_FOR_EXPORT, productId);
    }

    public List<Integer[]> selectAllTargetId() {
        return getJdbcTemplate().query(SELECT_ALL_TARGET_ID_ORDER_BY_PRODUCT_ID,
                new Object[]{}, new RowMapper() {

            public Integer[] mapRow(ResultSet rs, int i) throws SQLException {
                Integer[] horse = new Integer[3];
                horse[0] = rs.getInt("id");
                horse[1] = rs.getInt("product_id");
                horse[2] = rs.getInt("target_id");
                return horse;
            }
        });
    }

    private final class TargetTagTermRowMapper implements RowMapper {

        public String mapRow(ResultSet rs, int i) throws SQLException {
            return rs.getString("term");
        }
    }
}
