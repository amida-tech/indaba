/**
 *
 */
package com.ocs.indaba.dao.common;


import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.ocs.indaba.util.Page;

/**
 * @author Tiger Tang (tangtianguang@gmail.com)
 * @since 1.0
 */
public interface SmartDao<T extends Serializable, Id extends Serializable> {

    /**
     * @param object
     * @return
     */
    T create(T object);

    /**
     * @param id
     * @return
     */
    T get(Id id);

    /**
     * @param object
     * @return
     */
    T update(T object);

    /**
     * @param clause
     * @param params
     * @return
     */
    int update(String clause, Object... params);

    /**
     * @param id
     */
    void delete(Id id);

    /**
     * @param clause
     * @param params
     */
    void delete(String clause, Object... params);

    /**
     * @param object
     * @return
     */
    T save(T object);

    /**
     * @param id
     * @return
     */
    boolean exists(Id id);

    /**
     * @return
     */
    List<T> findAll();

    /**
     * @param query
     * @param params
     * @return
     */
    T findSingle(String query, Object... params);
    
    /**
     * @param query
     * @param m
     * @param params
     * @return
     */
    T findSingle(String query, CustomizedRowMapper<T> m, Object... params);

    /**
     * @param query
     * @param params
     * @return
     */
    List<T> find(String query, Object... params);
    
    /**
     * @param query
     * @param m
     * @param params
     * @return
     */
    List<T> find(String query, CustomizedRowMapper<T> m, Object... params);

    /**
     * @param query
     * @param params
     * @return
     */
    long count(String query, Object... params);

    /**
     * @param ids
     * @return
     */
    Set<T> get(Id[] ids);

    /**
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    //Page<T> find(int pageNumber, int pageSize);

    /**
     * @param pageNumber
     * @param pageSize
     * @param m
     * @return
     */
    //Page<T> find(int pageNumber, int pageSize, CustomizedRowMapper<T> m);
    
    /**
     * @param query
     * @param pageNumber
     * @param pageSize
     * @param params
     * @return
     */
    Page<T> find(String query, int pageNumber, int pageSize, Object[] params);

    /**
     * @param query
     * @param pageNumber
     * @param pageSize
     * @param crm
     * @param params
     * @return
     */
    Page<T> find(String query, CustomizedRowMapper<T> crm, int pageNumber, int pageSize, Object... params);
    
    /**
     * @param query
     * @param countQuery
     * @param pageNumber
     * @param pageSize
     * @param params
     * @return
     */
    Page<T> find(String query, String countQuery, int pageNumber, int pageSize, Object... params);
    
    /**
     * @param query
     * @param countQuery
     * @param pageNumber
     * @param pageSize
     * @param crm
     * @param params
     * @return
     */
    Page<T> find(String query, String countQuery, int pageNumber, int pageSize, CustomizedRowMapper<T> crm, Object... params);
}
