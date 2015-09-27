/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.ReferenceObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author luwb
 */
public class ReferenceObjectDAO extends SmartDaoMySqlImpl<ReferenceObject, Integer> {

    private static final Logger log = Logger.getLogger(ReferenceObjectDAO.class);
    private static final String INSERT_REFERENCE_OBJECT =
            "INSERT INTO reference_object (reference_id, source_description, comments, choices) "
            + " VALUES(?,?,?,?)";
    private static final String SELECT_LAST_INSERT_ID =
            "SELECT LAST_INSERT_ID()";

    private static final String SELECT_ALL_REFERECNES_BY_HORSE_ID = "SELECT ro.* FROM survey_answer sa, reference_object ro, content_header ch "
            + "WHERE sa.survey_content_object_id=ch.content_object_id AND ch.horse_id=? AND "
            + "ro.id=sa.reference_object_id";

    private static final String SELECT_ALL_REFERECNES_BY_PRODUCT_ID = "SELECT ro.* FROM survey_answer sa, reference_object ro, horse, content_header ch "
            + "WHERE sa.survey_content_object_id=ch.content_object_id AND ch.horse_id=horse.id AND "
            + "ro.id=sa.reference_object_id AND horse.product_id=?";

    public int insertReferenceObject(int id, int referenceId, String sourceDesc, String comments, long choices) {
        logger.debug("====>>>Insert reference_object values: id=" + referenceId + " " + sourceDesc + ", " + comments + " " + choices);
        if (id > 0) {
            this.updateReferenceObject(id, referenceId, sourceDesc, comments, choices);
            return id;
        }
        ReferenceObject refObject = new ReferenceObject();
        refObject.setReferenceId(referenceId);
        refObject.setChoices(choices);
        refObject.setComments(comments);
        refObject.setSourceDescription(sourceDesc);
        return super.save(refObject).getId();
    }

    public void updateReferenceObject(int id, int referenceId, String source, String comments, long choices) {
        ReferenceObject refObject = new ReferenceObject();
        refObject.setId(id);
        refObject.setReferenceId(referenceId);
        refObject.setChoices(choices);
        refObject.setComments(comments);
        refObject.setSourceDescription(source);
        super.update(refObject);
    }

    public List<ReferenceObject> selectReferenceObjectsByHorseId(int horseId) {
        return super.find(SELECT_ALL_REFERECNES_BY_HORSE_ID, horseId);
    }

     public List<ReferenceObject> selectReferenceObjectsByProductId(int productId) {
        return super.find(SELECT_ALL_REFERECNES_BY_PRODUCT_ID, productId);
    }

    public ReferenceObject getReferenceObjectById(int referenceObjectId) {
        return super.get(referenceObjectId);
    }

    private int getLastInsertId() {
        logger.debug("Select last insert id");

        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt(1);
            }
        };

        List<Integer> list = getJdbcTemplate().query(this.SELECT_LAST_INSERT_ID, mapper);

        return list.get(0);
    }
}
