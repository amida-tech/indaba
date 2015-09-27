/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;


import com.ocs.indaba.po.EventLog;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;


/**
 *
 * @author flyaway
 */
public class EventLogDAO extends SmartDaoMySqlImpl<EventLog, Integer> {

    private static final Logger log = Logger.getLogger(EventLogDAO.class);

    private static final String SQL_INSERT_EVENT_LOG =
            "INSERT INTO event_log " +
            "Values()";

    public long insertEventLog(EventLog eventLog) {
        log.debug("Insert eventLog");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {
        public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
               PreparedStatement prepareStatement =
                        conn.prepareStatement(SQL_INSERT_EVENT_LOG, Statement.RETURN_GENERATED_KEYS);

                return prepareStatement;
            }
        }, keyHolder
        );
        return keyHolder.getKey().longValue();
    }
}