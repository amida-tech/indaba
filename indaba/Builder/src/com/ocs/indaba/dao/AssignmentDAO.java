/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.vo.Assignment;
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
public class AssignmentDAO extends BaseDAO {

    private static final Logger logger = Logger.getLogger(NoteBookDAO.class);
    private static final String SELECT_ASSIGNMENT_BY_ID =
            "SELECT t.assign_id, t.owner_id, t.horse_id, t.task"
            + " FROM assignment t"
            + " WHERE t.assign_id=?";
    private static final String SELECT_ASSIGNMENT_BY_HORSE_ID =
            "SELECT t.assign_id, t.owner_id, t.horse_id, t.task"
            + " FROM assignment t"
            + " WHERE t.horse_id=?";
    private static final String SELECT_ALL_ASSIGNMENTS =
            "SELECT t.assign_id, t.owner_id, t.horse_id, t.task"
            + " FROM assignment t";
    private static final String SELECT_ASSIGNMENTS_BY_OWNERID =
            "SELECT t.assign_id, t.owner_id, t.horse_id, t.task"
            + " FROM assignment t"
            + " WHERE t.owner_id=?";
    private static final String UPDATE_ASSIGNMENTS_TASK_BY_HORSE_ID = 
    	"UPDATE assignment n SET n.task = ?, n.assigntime = ? WHERE n.horse_id = ?";

    private static final String DELETE_ASSIGNMENTS_TASK_BY_HORSE_ID =
    	"DELETE FROM assignment WHERE assignment.horse_id = ?";

    private static final String DELETE_ASSIGNMENTS =
    	"DELETE FROM assignment";

    private static final String INSERT_ASSIGNMENTS =
    	"INSERT INTO assignment (assign_id, owner_id, horse_id, task, assigntime) " +
    	" VALUES(?,?,?,?,?)";
    
    public void insertAssignment(int assign_id, int owner_id, int horse_id, int action, long assigntime)
    	throws SQLException {
    	logger.debug("insert one record to assignment");
    	Object [] values = new Object[] { assign_id, owner_id, horse_id, action, assigntime };
    	this.getJdbcTemplate().update(INSERT_ASSIGNMENTS, values);
    }
    public void deleteAssignmentByHorseID(long id) throws SQLException {
    	logger.debug("delete assignment");
    	Object [] values = new Object[] { id };
    	this.getJdbcTemplate().update(DELETE_ASSIGNMENTS_TASK_BY_HORSE_ID, values);
    }
    
    public void updateAssignmentTaskByHorseId(long id, int task, long assigntime) throws SQLException {
    	logger.debug("Update table assignment.");
    	System.out.println("id=" + id + ", new task=" + task);
    	
    	 Object[] values = new Object[] { task, assigntime, id };      
    	 this.getJdbcTemplate().update(UPDATE_ASSIGNMENTS_TASK_BY_HORSE_ID, values);
    }
    
    /**
     * Select the assignment by the specified id
     *
     * @param id
     * @return
     */
    public Assignment selectAssignmentById(int id) {
        logger.debug("Select table notebook.");

        RowMapper mapper = new AssignmentRowMapper();

        List<Assignment> list = getJdbcTemplate().query(SELECT_ASSIGNMENT_BY_ID,
                new Object[]{id},
                new int[]{INTEGER},
                mapper);

        if (list != null && list.size() > 0) {
            return list.get(0);
        }

        return null;
    }

    /**
     * Select the assignment by the specified id
     *
     * @param id
     * @return
     */
    public Assignment selectAssignmentByHorseId(long horseId) {
        logger.debug("Select table notebook.");
        RowMapper mapper = new AssignmentRowMapper();

        List<Assignment> list = getJdbcTemplate().query(SELECT_ASSIGNMENT_BY_HORSE_ID,
                new Object[]{horseId},
                new int[]{INTEGER},
                mapper);
        logger.debug("Assignment list: " + list.size());
        if (list != null && list.size() > 0) {
            return list.get(0);
        }

        return null;
    }

    /**
     * Select all of the assignments
     *
     * @return the list of assignmment. Null will be returned if no assginments.
     */
    public List<Assignment> selectAllAssignments() {
        logger.debug("Select table notebook.");
        RowMapper mapper = new AssignmentRowMapper();

        List<Assignment> list = getJdbcTemplate().query(SELECT_ALL_ASSIGNMENTS,
                new Object[]{},
                new int[]{},
                mapper);

        return list;
    }

    /**
     * Select all of the assignments
     *
     * @return the list of assignmment. Null will be returned if no assginments.
     */
    public List<Assignment> selectAssignmentsByOwnerId(int ownerId) {
        logger.debug("Select table notebook.");
        RowMapper mapper = new AssignmentRowMapper();

        List<Assignment> list = getJdbcTemplate().query(SELECT_ASSIGNMENTS_BY_OWNERID,
                new Object[]{ownerId},
                new int[]{INTEGER},
                mapper);

        return list;
    }
}

class AssignmentRowMapper implements RowMapper {

    public Assignment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Assignment assingment = new Assignment();
        assingment.setId(rs.getInt("assign_id"));
        assingment.setOwnerId(rs.getInt("owner_id"));
        assingment.setHorseId(rs.getInt("horse_id"));
        assingment.setTask(rs.getInt("task"));
        return assingment;
    }
}
