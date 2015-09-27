/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.vo.NoteBook;
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
public class NoteBookDAO extends BaseDAO {

    private static final Logger logger = Logger.getLogger(NoteBookDAO.class);
    private static final String SELECT_NOTEBOOK_BY_ID =
            "SELECT n.notebook_id,  n.status, n.owner_id, n.action, n.content"
            + " FROM notebook n"
            + " WHERE n.notebook_id=?";
    private static final String SELECT_NOTEBOOKS_BY_OWNERID =
            "SELECT n.notebook_id,  n.status, n.owner_id, n.action, n.content"
            + " FROM notebook n"
            + " WHERE n.owner_id=?";
    private static final String SELECT_ALL_NOTEBOOKS =
            "SELECT n.notebook_id,  n.status, n.owner_id, n.action, n.content"
            + " FROM notebook n";
    private static final String UPDATE_NOTEBOOK =
            "UPDATE notebook n SET n.status=?, n.owner_id=?, n.action=?, n.content=?"
            + " WHERE n.notebook_id=?";
    private static final String UPDATE_NOTEBOOK_ACTION_BY_ID =
            "UPDATE notebook n SET n.action = ? WHERE n.notebook_id = ?";

    public void saveNoteBook(NoteBook notebook) {
        getJdbcTemplate().update(UPDATE_NOTEBOOK, new Object[]{/*notebook.getName(), notebook.getDescription(),*/
                    notebook.getStatus(), notebook.getOwnerId(), notebook.getAction(), notebook.getContent(),
                    notebook.getId()});
    }

    /**
     * Select all of the notebooks
     *
     * @return notebook list.
     */
    public List<NoteBook> selectAllNoteBooks() {
        logger.debug("Select table notebook.");
        RowMapper mapper = new NoteBookRowMapper();

        List<NoteBook> list = getJdbcTemplate().query(SELECT_ALL_NOTEBOOKS,
                new Object[]{},
                new int[]{},
                mapper);

        return list;
    }

    public List<NoteBook> selectNoteBooksByOwnerId(long ownerId) {
        logger.debug("Select table notebook.");
        RowMapper mapper = new NoteBookRowMapper();

        List<NoteBook> list = getJdbcTemplate().query(SELECT_NOTEBOOKS_BY_OWNERID,
                new Object[]{ownerId},
                new int[]{INTEGER},
                mapper);

        return list;
    }

    /**
     * select the notebook by the specified id.
     * 
     * @param id
     * @return
     */
    public NoteBook selectNoteBookById(long id) {
        logger.debug("Select table notebook.");
        RowMapper mapper = new NoteBookRowMapper();

        List<NoteBook> list = getJdbcTemplate().query(SELECT_NOTEBOOK_BY_ID,
                new Object[]{id},
                new int[]{INTEGER},
                mapper);

        if (list != null && list.size() > 0) {
            return list.get(0);
        }

        return null;
    }

    public void updateNoteBookActionById(long id, int action) throws SQLException {
        logger.debug("Update table notebook.");
        System.out.println("id=" + id + ", new action=" + action);

        Object[] values = new Object[]{action, id};
        this.getJdbcTemplate().update(UPDATE_NOTEBOOK_ACTION_BY_ID, values);
    }
}

class NoteBookRowMapper implements RowMapper {

    public NoteBook mapRow(ResultSet rs, int rowNum) throws SQLException {
        NoteBook notebook = new NoteBook();
        notebook.setId(rs.getInt("notebook_id"));
        //notebook.setName(rs.getString("name"));
        //notebook.setDescription(rs.getString("description"));
        notebook.setStatus(rs.getInt("status"));
        notebook.setOwnerId(rs.getInt("owner_id"));
        notebook.setAction(rs.getInt("action"));
        notebook.setContent(rs.getString("content"));

        return notebook;

    }
}
