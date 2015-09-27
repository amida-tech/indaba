package com.ocs.indaba.dao.common;

import com.mysql.jdbc.Blob;
import com.mysql.jdbc.Clob;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.ocs.indaba.util.DefaultPageImpl;
import com.ocs.indaba.util.EntityBeanDescriptor;
import com.ocs.indaba.util.EntityBeanPersistentHelper;
import com.ocs.indaba.util.GenericTypeUtils;
import com.ocs.indaba.util.Page;
import com.ocs.indaba.util.EntityBeanDescriptor.FieldDescriptor;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Tianguang Tang (tangtianguang@gmail.com)
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public class SmartDaoMySqlImpl<T extends Serializable, Id extends Serializable> extends JdbcDaoSupport implements SmartDao<T, Id> {

    private static final String FOUND_ROWS = "SELECT FOUND_ROWS() AS rows";
    public static final boolean SQL_NORMALIZE_ENABELD = false;
    public static final int INSERT = 0;
    public static final int DELETE = 1;
    public static final int SELECT = 2;
    public static final int UPDATE = 3;
    private static final Logger log = Logger.getLogger(SmartDaoMySqlImpl.class);
    protected Map<Serializable, T> map = new HashMap<Serializable, T>();
    protected Map<String, T> map0 = new HashMap<String, T>();
    protected Class<? extends Serializable> entityClass;
    protected EntityBeanDescriptor entityBeanDescriptor;
    protected final String tableName;
    protected final String primaryKeyName;
    protected boolean useCache = false;

    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    public Class<? extends Serializable> getEntityClass() {
        return entityClass;
    }

    public SmartDaoMySqlImpl() {
        entityClass = GenericTypeUtils.getSuperClassGenricType(getClass());
        entityBeanDescriptor = EntityBeanPersistentHelper.getEntityBeanDescriptor(entityClass);
        tableName = entityBeanDescriptor.getTableName();
        primaryKeyName = entityBeanDescriptor.getPrimaryKeyName();

        DaoRepository.registerDao(entityClass, this);
    }

    public T create(T object) {
        return create(object, true);

    }

    public T create(T object, boolean autoIncrement) {
        final boolean hasId = (EntityBeanPersistentHelper.getId(object) != null);
        final String sql = hasId ? entityBeanDescriptor.getInsertWithIdClausePrefix().toString() : entityBeanDescriptor.getInsertClausePrefix().toString();

        if (autoIncrement && !entityBeanDescriptor.isPKIncremental()) {
            EntityBeanPersistentHelper.setId(object, UUID.randomUUID().toString().replace("-", ""));
        }


        final Object[] args = normalizeParamValues(hasId ? getColumnWithIdValues(object, INSERT) : getColumnValues(object, INSERT));

        final int[] argTypes = hasId ? entityBeanDescriptor.getColumnWithIdSqlTypes() : entityBeanDescriptor.getColumnSqlTypes();
        final GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con)
                    throws SQLException {
                final PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int argIndx = 1;
                for (int i = 0; i < args.length; i++) {
                    //logger.debug(args.length + ": " + i + ": " + args[i]);
                    Object arg = args[i];
                    if (arg instanceof Collection && argTypes[i] != Types.ARRAY) {
                        Collection entries = (Collection) arg;
                        for (Iterator it = entries.iterator(); it.hasNext();) {
                            Object entry = it.next();
                            if (entry instanceof Object[]) {
                                Object[] valueArray = ((Object[]) entry);
                                for (int k = 0; k < valueArray.length; k++) {
                                    Object argValue = valueArray[k];
                                    StatementCreatorUtils.setParameterValue(ps, argIndx++, argTypes[i], argValue);
                                }
                            } else {
                                StatementCreatorUtils.setParameterValue(ps, argIndx++, argTypes[i], entry);
                            }
                        }
                    } else {
                        StatementCreatorUtils.setParameterValue(ps, argIndx++, argTypes[i], arg);
                    }
                }
                return ps;
            }
        }, generatedKeyHolder);
        if (autoIncrement) {
            EntityBeanPersistentHelper.setId(object, generatedKeyHolder.getKey().intValue());
        }
//        object.setId(generatedKeyHolder.getKey().intValue());

//        getJdbcTemplate().update(entityBeanDescriptor.getInsertClausePrefix().toString(),
//                args,
//                argTypes);


        for (EntityBeanDescriptor.FieldDescriptor descriptor : entityBeanDescriptor.getOneToMany()) {
            final Class<? extends Serializable> targetType = descriptor.getTargetType();
            final SmartDao targetTypeDao = DaoRepository.getDao(targetType);
            final Class fieldType = descriptor.getFieldType();
            if (Iterable.class.isAssignableFrom(fieldType)) {
                try {
                    final String fieldName = descriptor.getFieldName();
                    Object v;
                    try {
                        v = PropertyUtils.getProperty(object, fieldName);
                    } catch (Exception e) {
                        v = PropertyUtils.getProperty(object, StringUtils.capitalize(fieldName));
                    }

                    if (v != null) {
                        Iterator<? extends Serializable> iterator = ((Iterable<? extends Serializable>) v).iterator();
                        while (iterator.hasNext()) {
                            Serializable o = iterator.next();
                            if (EntityBeanPersistentHelper.getId(o) == null) {
                                PropertyUtils.setProperty(o, EntityBeanPersistentHelper.getEntityBeanDescriptor(targetType).getFieldNameByColumnName(descriptor.getJoinColumnName()), object);
                                targetTypeDao.create(o);
                            }
                        }
                    }
                } catch (Exception e) {
                    // just log and skip
                    log.error(e);
                }
            }
        }

        for (EntityBeanDescriptor.FieldDescriptor descriptor : entityBeanDescriptor.getManyToMany()) {
            try {
                final String fieldName = descriptor.getFieldName();
                Object v;
                try {
                    v = PropertyUtils.getProperty(object, fieldName);
                } catch (Exception e) {
                    v = PropertyUtils.getProperty(object, StringUtils.capitalize(fieldName));
                }

                if (v != null) {
                    final Class<? extends Serializable> targetType = descriptor.getTargetType();
                    final SmartDao targetTypeDao = DaoRepository.getDao(targetType);
                    final Class fieldType = descriptor.getFieldType();
                    if (Iterable.class.isAssignableFrom(fieldType)) {

                        Iterator<? extends Serializable> iterator = ((Iterable<? extends Serializable>) v).iterator();
                        while (iterator.hasNext()) {
                            Serializable o = iterator.next();
                            if (EntityBeanPersistentHelper.getId(o) == null) {
                                final String joinColumnName = descriptor.getJoinColumnName();
                                final String inverseJoinColumnName = descriptor.getInverseJoinColumnName();
                                o = (Serializable) targetTypeDao.create(o);
                                final Object[] params = {EntityBeanPersistentHelper.getId(object), EntityBeanPersistentHelper.getId(o)};
                                getJdbcTemplate().update("insert into `" + descriptor.getJoinTableName() + "`("
                                        + joinColumnName + "," + inverseJoinColumnName
                                        + ") values(?,?)", params);
                            }
                        }
                    }
                }

            } catch (Exception e) {
                // just log and skip
                log.error(e);
            }
        }

        if (useCache) {
            map.put(EntityBeanPersistentHelper.getId(object), object);
        }

        return object;
    }

    public T get(Id id) {
        if (useCache && map.containsKey(id)) {
            return map.get(id);
        }

        final StringBuilder selectClause = new StringBuilder().append("select * from ").
                append(tableName).
                append(" where ").
                append(tableName).
                append(".").
                append(primaryKeyName).
                append(" = ?");
        return findSingle(selectClause.toString(), id);
    }

    public T update(T object) {
        try {
            //String primaryKeyName = entityBeanDescriptor.getPrimaryKeyName();

            String updateSql = entityBeanDescriptor.getUpdateClausePrefix().toString();

            Object[] params = getColumnValues(object, UPDATE);
            int length = params.length;
            Object[] newParams = new Object[length + 1];
            System.arraycopy(params, 0, newParams, 0, length);
            newParams[length] = PropertyUtils.getProperty(object, entityBeanDescriptor.getFieldNameByColumnName(primaryKeyName));
            getJdbcTemplate().update(updateSql, normalizeParamValues(newParams));

        } catch (Exception ex) {
            // just skip, could not happen
            logger.error("Error occurs!", ex);
        }

        return object;
    }

    public int update(String clause, Object... params) {
        return getJdbcTemplate().update(clause, params);
    }

    public void delete(Id id) {
        getJdbcTemplate().update(new StringBuilder(entityBeanDescriptor.getDeleteClausePrefix()).append(" where ").append(primaryKeyName).append(" = ?").toString(),
                new Object[]{id},
                new int[]{entityBeanDescriptor.getColumnType(primaryKeyName)});
    }

    public void delete(String clause, Object... params) {
        getJdbcTemplate().update(clause, normalizeParamValues(params));

        if (useCache) {
        }
    }

    public T save(T object) {
        if (EntityBeanPersistentHelper.getId(object) != null) {
            return update(object);
        } else {
//            object.setId(UUID.randomUUID().toString());
            return create(object);
        }
    }

    public boolean exists(Id id) {
        StringBuilder sql = new StringBuilder("select count(*) from `").append(tableName).append("` where ").append(primaryKeyName).append(" = ?");
        return count(sql.toString(), id) > 0;
    }

    public List<T> findAll() {
        StringBuilder sql = new StringBuilder("select * from `").append(tableName).append("`");
        return find(sql.toString());
    }

    public T findSingle(String query, Object... params) {
        logger.debug("findSingle - Actual SQL: " + query);
        try {
            List<T> ts = find(query, params);
            return ts.isEmpty() ? null : ts.get(0);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<T> find(String query, Object... params) {
        boolean normalized = false;
        if (SQL_NORMALIZE_ENABELD) {
            if (query.toLowerCase().startsWith("select *")) {
                query = normalizeSQLSelectClause(query);
                normalized = true;
            }
        }

        logger.debug("Actual SQL: " + query);
        return getJdbcTemplate().query(query, normalizeParamValues(params), getRowMapper(normalized));
    }
    
    public void run(String sql) {
        getJdbcTemplate().execute(sql);
    }

    public List<T> find(String query, RowMapper mapper, Object... params) {
        logger.debug("Actual SQL: " + query);
        return getJdbcTemplate().query(query, normalizeParamValues(params), mapper);
    }

    private Object[] normalizeParamValues(Object... params) {
        Object[] normParams = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            final Object param = params[i];
            if (param != null && param.getClass().isEnum()) {
                normParams[i] = ((Enum) param).ordinal();
            } //            else if (param != null && (param.getClass().equals(boolean.class) || param.getClass().equals(Boolean.class))) {
            //            	normParams[i] = ((Boolean) param) ? true : false;
            //            }
            else {
                normParams[i] = param;
            }
        }
        return normParams;
    }

    public long count(String query, Object... params) {
        return getJdbcTemplate().queryForLong(query, normalizeParamValues(params));
    }

    public Set<T> get(Id[] ids) {
        if (useCache) {
            Set<T> ts = new HashSet<T>();
            for (Id id : ids) {
                ts.add(map.get(id));
            }

            return ts;
        }

        StringBuilder sqlBuilder = new StringBuilder(SmartDaoMySQLHelper.generateSelectClause(getEntityClass()));
        sqlBuilder.append(" where 1 = 0 ");
        for (int i = 0; i < ids.length; i++) {
            sqlBuilder.append(" or id = ? ");
            sqlBuilder.append(" or ").append(tableName).append('.').append(primaryKeyName).append(" = ? ");
        }

        return new HashSet<T>(find(sqlBuilder.toString(), new Object[]{ids}));
    }
    /*
     * public Page<T> find(int pageNumber, int pageSize) { StringBuilder sql =
     * new StringBuilder("select * from ").append(tableName); return
     * find(sql.toString(), pageNumber, pageSize); }
     */

    public Page<T> find(String query, int pageNumber, int pageSize, Object... params) {
        String countQuery = new StringBuilder("select count(*) ").append(removeSelect(query)).toString();
        return find(query, countQuery, pageNumber, pageSize, params);
    }

    private String removeSelect(String query) {
        int beginIndex = query.toLowerCase().indexOf("from");
        return query.substring(beginIndex);
    }

    public Page<T> find(String query, String countQuery, int pageNumber, int pageSize, Object... params) {
        if (pageNumber <= 0) {
            pageNumber = Page.DEFAULT_PAGE_NUMBER;
        }
        if (pageSize <= 0) {
            pageSize = Page.DEFAULT_PAGESIZE;
        }

        int startOffset = (pageNumber - 1) * pageSize;

        List<T> elems = find(new StringBuilder(query).append(" limit ").append(startOffset).append(",").append(pageSize).toString(), normalizeParamValues(params));

        if (StringUtils.isBlank(countQuery)) {
            countQuery = new StringBuilder("select count(*) ").append(removeSelect(query)).toString();
        }

        return new DefaultPageImpl(pageNumber, pageSize, new Long(count(countQuery, normalizeParamValues(params))).intValue(), elems);
    }

    protected RowMapper getRowMapper(boolean normalized) {
        return SmartDaoMySQLHelper.generateRowMapper(entityClass, normalized);
    }

    public Object[] getColumnValues(Object object, int operationType) {
        EntityBeanDescriptor beanDescriptor = EntityBeanPersistentHelper.getEntityBeanDescriptor(entityClass);
        if (operationType == INSERT && (beanDescriptor.getFieldDescriptors().size() == 1)) {
            try {
                return new Object[]{PropertyUtils.getProperty(object, "id")};
            } catch (Exception ex) {
                logger.error(ex);
            }
        }

        Object[] objects = new Object[beanDescriptor.getFieldDescriptors().size() - 1];
        int i = 0;
        for (FieldDescriptor fd : beanDescriptor.getFieldDescriptors()) {
            final String columnName = fd.getColumnName();
            final String fieldName = fd.getFieldName();

            if (columnName.equalsIgnoreCase(beanDescriptor.getPrimaryKeyName()) && operationType == UPDATE) {
                continue;
            }
            if (columnName.equalsIgnoreCase(beanDescriptor.getPrimaryKeyName()) && beanDescriptor.isPKIncremental() && operationType == INSERT) {
                continue;
            }

            try {
                if (!fd.isManyToOne() && !fd.isManyToMany() && !fd.isOneToMany()) {
                    try {
                        objects[i] = PropertyUtils.getProperty(object, fieldName);
                    } catch (Exception e) {
                        objects[i] = PropertyUtils.getProperty(object, StringUtils.capitalize(fieldName));
                    }
                } else if (fd.isManyToOne()) {
                    Object value;
                    try {
                        value = PropertyUtils.getProperty(object, fieldName);
                    } catch (Exception e) {
                        value = PropertyUtils.getProperty(object, StringUtils.capitalize(fieldName));
                    }
                    if (value != null) {
                        final EntityBeanDescriptor targetTypeDescriptor = EntityBeanPersistentHelper.getEntityBeanDescriptor(fd.getTargetType());
                        objects[i] = PropertyUtils.getProperty(value, targetTypeDescriptor.getFieldNameByColumnName(targetTypeDescriptor.getPrimaryKeyName()));
                    } else {
                        objects[i] = null;
                    }
                }
            } catch (Exception e) {
                logger.error(e);
            }

            i++;
        }

        return objects;
    }

    public Object[] getColumnWithIdValues(Object object, int operationType) {
        Object[] objects = getColumnValues(object, operationType);
        Object[] arr = new Object[objects.length + 1];
        try {
            arr[0] = PropertyUtils.getProperty(object, "id");
        } catch (Exception ex) {
        }
        System.arraycopy(objects, 0, arr, 1, objects.length);

        return arr;
    }

    public T findSingle(String query, CustomizedRowMapper<T> m, Object... params) {
        try {
            List<T> ts = find(query, m, params);
            return ts.isEmpty() ? null : ts.get(0);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<T> find(String query, final CustomizedRowMapper<T> m, Object... params) {
        try {
            boolean normalized = false;
            if (SQL_NORMALIZE_ENABELD && query.toLowerCase().startsWith("select *")) {
                normalized = true;
                query = normalizeSQLSelectClause(query);
            }

            logger.debug("Actual SQL: " + query);
            final RowMapper rowMapper = getRowMapper(normalized);

            return getJdbcTemplate().query(query, normalizeParamValues(params), new RowMapper() {

                public T mapRow(ResultSet rs, int rowNum) throws SQLException {
                    T object = (T) rowMapper.mapRow(rs, rowNum);

                    m.map(object, rs);

                    return object;
                }
            });

        } catch (EmptyResultDataAccessException e) {
            return Collections.EMPTY_LIST;
        }
    }

    private String normalizeSQLSelectClause(final String query) {
        String temp = query.toLowerCase();
        boolean hasConditionClause = temp.contains("where");
        boolean hasOrderbyClause = temp.contains("order by");

        if (temp.startsWith("select *")) {
            StringBuilder sb = new StringBuilder().append(SmartDaoMySQLHelper.generateSelectClause(getEntityClass()));
            if (hasConditionClause) {
                sb.append(temp.substring(temp.indexOf("where")));
            }
            if (hasOrderbyClause) {
                sb.append(temp.substring(temp.indexOf("order by")));
            }

            return sb.toString();
        }
        return query;
    }
    /*
     * public Page<T> find(int pageNumber, int pageSize, CustomizedRowMapper<T>
     * crm) { return
     * find(SmartDaoMySQLHelper.generateSelectClause(getEntityClass()),
     * pageNumber, pageSize, crm); }
     */

    public Page<T> find(String query, CustomizedRowMapper<T> crm, int pageNumber, int pageSize, Object[] params) {
        String countQuery = new StringBuilder("select count(*) ").append(removeSelect(query)).toString();
        return find(query, countQuery, pageNumber, pageSize, crm, params);
    }

    public Page<T> find(String query, String countQuery, int pageNumber, int pageSize, final CustomizedRowMapper<T> crm, Object... params) {
        if (pageNumber <= 0) {
            pageNumber = Page.DEFAULT_PAGE_NUMBER;
        }
        if (pageSize <= 0) {
            pageSize = Page.DEFAULT_PAGESIZE;
        }

        int startOffset = (pageNumber - 1) * pageSize;
        int endOffset = pageNumber * pageSize;

        boolean normalized = false;
        if (SQL_NORMALIZE_ENABELD && query.toLowerCase().startsWith("select *")) {
            query = normalizeSQLSelectClause(query);
            normalized = true;
        }

        final RowMapper rowMapper = getRowMapper(normalized);
        List<T> elems = find(new StringBuilder(query).append(" limit ").append(startOffset).append(",").append(endOffset).toString(), new RowMapper() {

            public T mapRow(ResultSet rs, int rowNum) throws SQLException {
                T object = (T) rowMapper.mapRow(rs, rowNum);

                crm.map(object, rs);

                return object;
            }
        }, normalizeParamValues(params));

        if (StringUtils.isBlank(countQuery)) {
            countQuery = new StringBuilder("select count(*) ").append(removeSelect(query)).toString();
        }

        return new DefaultPageImpl(pageNumber, pageSize, new Long(count(countQuery, normalizeParamValues(params))).intValue(), elems);
    }

    protected String appendSQLParameters(StringBuffer sBuf, String paramName, Object[] params) {
        if (params != null && params.length != 0) {
            sBuf.append(" AND (");
            for (int i = 0, size = params.length; i < size; ++i) {
                sBuf.append(paramName).append("=").append(params[i]);
                if (i != size - 1) {
                    sBuf.append(" OR ");
                }
            }
            sBuf.append(") ");
        }
        return sBuf.toString();
    }

    protected String appendSQLParameters(String paramName, Object[] params) {
        StringBuffer sBuf = new StringBuffer();
        if (params != null && params.length != 0) {
            sBuf.append(" AND (");
            for (int i = 0, size = params.length; i < size; ++i) {
                sBuf.append(paramName).append("=").append(params[i]);
                if (i != size - 1) {
                    sBuf.append(" OR ");
                }
            }
            sBuf.append(") ");
        }
        return sBuf.toString();
    }

    protected String appendSQLInParameters(String paramName, Object[] params) {
        StringBuffer sBuf = new StringBuffer();
        if (params != null && params.length != 0) {
            sBuf.append(" AND ").append(paramName).append(" IN (");
            for (int i = 0, size = params.length; i < size; ++i) {
                sBuf.append(params[i]);
                if (i != size - 1) {
                    sBuf.append(", ");
                }
            }
            sBuf.append(") ");
        }
        return sBuf.toString();
    }

    protected long getTotalFoundRows(JdbcTemplate jdbcTmpl) {
        return jdbcTmpl.queryForLong(FOUND_ROWS);
    }

     public int call(String procName, Object... params){
        Object obj =  getJdbcTemplate().execute(new ProcCallableStatementCreator(procName, params), new ProcCallableStatementCallback(params.length + 1));
        if(Integer.class.equals(obj.getClass()) || int.class.equals(obj.getClass()))
            return (Integer)obj;
        else
            return -1;
    }

    private class ProcCallableStatementCreator implements CallableStatementCreator {
        private String procName;
        private Object[] objects;
        public CallableStatement createCallableStatement(Connection con) throws SQLException {
            int inputLen = objects.length;
            StringBuffer call = new StringBuffer("{call ");
            call.append(procName + "(");
            for(int i=0 ;i<inputLen; i++){
                call.append("?");
                call.append(",");
            }//for input
            call.append("?");
            call.append(")}");//for output

            CallableStatement cs = null;
            cs = con.prepareCall(call.toString());
            cs.registerOutParameter(inputLen + 1, java.sql.Types.INTEGER);//output
            int index = 1;
            for(Object obj : objects){
                Class clazz = obj.getClass();
                if(String.class.equals(clazz))
                    cs.setString(index, (String)obj);
                else if (Boolean.class.equals(clazz) || boolean.class.equals(clazz)) {
                    cs.setBoolean(index, (Boolean)obj);
                } else if (Byte.class.equals(clazz) || byte.class.equals(clazz)) {
                    cs.setByte(index, (Byte)obj);
                } else if (Short.class.equals(clazz) || short.class.equals(clazz)) {
                    cs.setShort(index, (Short)obj);
                } else if (Integer.class.equals(clazz) || int.class.equals(clazz)) {
                    cs.setInt(index, (Integer)obj);
                } else if (Long.class.equals(clazz) || long.class.equals(clazz)) {
                    cs.setLong(index, (Long)obj);
                } else if (Float.class.equals(clazz) || float.class.equals(clazz)) {
                    cs.setFloat(index, (Float)obj);
                } else if (Double.class.equals(clazz) || Number.class.equals(clazz) || double.class.equals(clazz)) {
                    cs.setDouble(index, (Double)obj);
                } else if (byte[].class.equals(clazz)) {
                    cs.setBytes(index, (byte[])obj);
                } else if (java.sql.Date.class.equals(clazz)) {
                    cs.setDate(index, (java.sql.Date)obj);
                } else if (java.sql.Time.class.equals(clazz)) {
                    cs.setTime(index, (java.sql.Time)obj);
                } else if (java.sql.Timestamp.class.equals(clazz) || java.util.Date.class.equals(clazz)) {
                    cs.setTimestamp(index, (java.sql.Timestamp)obj);
                } else if (BigDecimal.class.equals(clazz)) {
                    cs.setBigDecimal(index, (BigDecimal)obj);
                } else if (Blob.class.equals(clazz)) {
                    cs.setBlob(index, (Blob)obj);
                } else if (Clob.class.equals(clazz)) {
                    cs.setClob(index, (Clob)obj);
                }else {
                }
                index++;
            }//for input
            return cs;
        }

        public ProcCallableStatementCreator(String proc, Object[] object){
            this.procName = proc;
            this.objects = object;
        }
    }

    private class ProcCallableStatementCallback implements CallableStatementCallback{
        private int index;

        public ProcCallableStatementCallback(int index){
            this.index = index;
        }

        public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
            cs.executeQuery();
            int rt = cs.getInt(index);
            return rt;
        }
    }
}
