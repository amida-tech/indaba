/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
*/

package com.ocs.indaba.dao;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * Base class for all DAOs.
 *
 * For all data access, using the supplied Spring JdbcTemplate(s) is encouraged as
 * it lends to simple, cleaner code.  But IF the JDBC Connection object is
 * absolutely needed (to perform regular JDBC code for instance), the
 * Connection MUST BE retrieved via the supplied getConnection() method
 * (and subsequently closed via the supplied releaseConnection()).  This
 * will allow Spring to continue managing the transactions.
 *
 * @author nml
 */
public abstract class BaseDAO {

    // classic Spring JdbcTemplate
    private JdbcTemplate jdbcTemplate;
    // Java 5 wrapper around JdbcTemplate
    private SimpleJdbcTemplate simpleJdbcTemplate;

    @Autowired
    public final void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcTemplate = new SimpleJdbcTemplate(this.jdbcTemplate);
    }

    /**
     * Returns the classic Spring JdbcTemplate class
     */
    protected JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    /**
     * Returns the Java 5 convenience wrapper
     */
    protected SimpleJdbcTemplate getSimpleJdbcTemplate() {
        return this.simpleJdbcTemplate;
    }

    /**
     * Return the JDBC DataSource used by this DAO.
     */
    protected final DataSource getDataSource() {
        return (this.jdbcTemplate != null ? this.jdbcTemplate.getDataSource() : null);
    }

    /**
     * Get a JDBC Connection, either from the current transaction or a new one.
     * @return the JDBC Connection
     * @throws CannotGetJdbcConnectionException if the attempt to get a Connection failed
     * @see org.springframework.jdbc.datasource.DataSourceUtils#getConnection(javax.sql.DataSource)
     */
    protected final Connection getConnection() throws CannotGetJdbcConnectionException {
        return DataSourceUtils.getConnection(getDataSource());
    }

    /**
     * Close the given JDBC Connection, created via this DAO's DataSource,
     * if it isn't bound to the thread.
     * @param con Connection to close
     * @see org.springframework.jdbc.datasource.DataSourceUtils#releaseConnection
     */
    protected final void releaseConnection(Connection con) {
        DataSourceUtils.releaseConnection(con, getDataSource());
    }
}
