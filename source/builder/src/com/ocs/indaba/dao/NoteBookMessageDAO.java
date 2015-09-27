/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

//import com.ocs.indaba.po.NoteBookMessage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import static java.sql.Types.INTEGER;

/**
 *
 * @author Jeff
 */
public class NoteBookMessageDAO extends BaseDAO {

    private static final Logger logger = Logger.getLogger(NoteBookMessageDAO.class);
    /*private static final String SELECT_MESSAGE_BY_OWNERID =
    "SELECT t.horse_id, t.owner_id, t.action, t.message"
    + " FROM nbmsg t"
    + " WHERE t.owner_id=?";*/
    private static final String SELECT_MESSAGES_BY_OWNERID =
            "SELECT horse_id, t.owner_id, t.action, message"
            + " FROM notebook n, nbmsg t"
            + " WHERE n.notebook_id=horse_id AND n.action=t.action AND t.owner_id=?";
    private static final String INSERT_MESSAGE =
            "INSERT INTO nbmsg(horse_id, owner_id, action, message) VALUES(?, ?, ?, ?)";

    /*public void insertNoteBookMessage(NoteBookMessage nbMsg) {
    logger.debug("Insert into NoteBookMessage.");

    Object[] values = new Object[]{nbMsg.getHorseId(), nbMsg.getOwnerId(), nbMsg.getAction(), nbMsg.getMessage()};
    this.getJdbcTemplate().update(INSERT_MESSAGE, values);
    }
     */
    /**
     * Select the assignment by the specified id
     *
     * @param id
     * @return
     */
    /*
    public List<NoteBookMessage> selectNoteBookMessagesByOwnerId(long ownerId) {
    logger.debug("Select table notebook.");

    RowMapper mapper = new NoteBookMessageRowMapper();

    List<NoteBookMessage> list = getJdbcTemplate().query(SELECT_MESSAGES_BY_OWNERID,
    new Object[]{ownerId},
    new int[]{INTEGER},
    mapper);

    //        if (list != null && list.size() == 1) {
    //            return list.get(0);
    //        }

    return list;
    }
    }*/
    /*
    class NoteBookMessageRowMapper implements RowMapper {

    public NoteBookMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
    NoteBookMessage nbMsg = new NoteBookMessage();
    //nbMsg.setId(rs.getInt("id"));
    nbMsg.setOwnerId(rs.getInt("owner_id"));
    nbMsg.setHorseId(rs.getInt("horse_id"));
    nbMsg.setAction(rs.getInt("action"));
    nbMsg.setMessage(rs.getString("message"));
    return nbMsg;
    }
     */
}
