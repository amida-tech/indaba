package com.ocs.common.db;

import com.ocs.common.db.EntityBeanDescriptor.FieldDescriptor;
import com.ocs.util.Page;
import com.ocs.util.StringUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;

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
    protected Map<Serializable, T> map = new WeakHashMap<Serializable, T>();
    protected Map<String, T> map0 = new WeakHashMap<String, T>();
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
        if (!entityBeanDescriptor.isPKIncremental()) {
            EntityBeanPersistentHelper.setId(object, UUID.randomUUID().toString().replace("-", ""));
        }

        final String sql = entityBeanDescriptor.getInsertClausePrefix().toString();

        final Object[] args = normalizeParamValues(getColumnValues(object, INSERT));
        final int[] argTypes = entityBeanDescriptor.getColumnSqlTypes();
        final GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con)
                    throws SQLException {
                final PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                logger.debug("SQL: " + sql);
                int argIndx = 1;
                for (int i = 0; i < args.length; i++) {
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

        EntityBeanPersistentHelper.setId(object, generatedKeyHolder.getKey().intValue());

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
                                getJdbcTemplate().update("insert into `" + descriptor.getJoinTableName() + "`(`"
                                        + joinColumnName + "`,`" + inverseJoinColumnName
                                        + "`) values(?, ?)", params);
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

        final StringBuilder selectClause = new StringBuilder().append("SELECT * FROM ").
                append('`').append(tableName).append('`').
                append(" WHERE ").
                append('`').append(tableName).append('`').
                append(".").
               append('`'). append(primaryKeyName).append('`').
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

        } catch (Exception e) {
            // just skip, could not happen
            logger.error(e);
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
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM `").append(tableName).append("` WHERE `").append(primaryKeyName).append("` = ?");
        return count(sql.toString(), id) > 0;
    }

    public List<T> findAll() {
        StringBuilder sql = new StringBuilder("SELECT * FROM `").append(tableName).append("`");
        return find(sql.toString());
    }

    public T findSingle(String query, Object... params) {
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
            sqlBuilder.append(" or `").append(tableName).append("`.`").append(primaryKeyName).append("` = ? ");
        }

        return new HashSet<T>(find(sqlBuilder.toString(), new Object[]{ids}));
    }

    public Page<T> find(int pageNumber, int pageSize) {
        StringBuilder sql = new StringBuilder("SELECT * FROM `").append(tableName).append("`");
        return find(sql.toString(), pageNumber, pageSize, (Object[]) null);
    }

    public Page<T> find(String query, int pageNumber, int pageSize, Object... params) {
        String countQuery = new StringBuilder("SELECT COUNT(*) ").append(removeSelect(query)).toString();
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
        int endOffset = pageNumber * pageSize;

        List<T> elems = find(new StringBuilder(query).append(" limit ").append(startOffset).append(",").append(endOffset).toString(), normalizeParamValues(params));

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

        //Object[] objects = new Object[beanDescriptor.getFieldDescriptors().size() - 1];
        List<Object> objects = new ArrayList<Object>();
        int i = 0;
        for (FieldDescriptor fd : beanDescriptor.getFieldDescriptors()) {
            if (fd.getColumnType() == Types.OTHER) {
                continue;
            }
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
                        //objects[i] = PropertyUtils.getProperty(object, fieldName);
                        objects.add(PropertyUtils.getProperty(object, fieldName));
                    } catch (Exception e) {
                        //objects[i] = PropertyUtils.getProperty(object, StringUtils.capitalize(fieldName));
                        objects.add(PropertyUtils.getProperty(object, StringUtils.capitalize(fieldName)));
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
                        //objects[i] = PropertyUtils.getProperty(value, targetTypeDescriptor.getFieldNameByColumnName(targetTypeDescriptor.getPrimaryKeyName()));
                        objects.add(PropertyUtils.getProperty(value, targetTypeDescriptor.getFieldNameByColumnName(targetTypeDescriptor.getPrimaryKeyName())));
                    } else {
                        //objects[i] = null;
                        objects.add(null);
                    }
                }
            } catch (Exception e) {
                logger.error(e);
            }

            i++;
        }

        return objects.toArray();
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

    public Page<T> find(int pageNumber, int pageSize, CustomizedRowMapper<T> crm) {
        return find(SmartDaoMySQLHelper.generateSelectClause(getEntityClass()), pageNumber, pageSize, crm, (Object[]) null);
    }

    public Page<T> find(String query, int pageNumber, int pageSize, CustomizedRowMapper<T> crm, Object... params) {
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
        StringBuilder sBuf = new StringBuilder();
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
        StringBuilder sBuf = new StringBuilder();
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

    public int call(String procName, Object... params) {
        List<Object> outList = call(procName, new int[]{java.sql.Types.INTEGER}, new ArrayList<Object>(1), params);
        if (outList == null || outList.isEmpty()) {
            return -1;
        } else {
            Object ret = outList.get(0);
            if (Integer.class.equals(ret.getClass()) || int.class.equals(ret.getClass())) {
                return (Integer) ret;
            } else if (ret instanceof String) {
                return StringUtils.str2int((String)ret);
            } else {
                return -1;
            }
        }
    }

    public List<Object> call(String procName, int[] outParamTypes, List<Object> outList, Object... params) {
        Object obj = getJdbcTemplate().execute(new ProcCallableStatementCreator(procName, outParamTypes, params), new ProcCallableStatementCallback((params == null ? 1 : params.length + 1), outParamTypes, outList));
        return (List<Object>)obj;
    }

    private class ProcCallableStatementCreator implements CallableStatementCreator {

        private String procName;
        private int[] outParamTypes;
        private Object[] objects;

        public CallableStatement createCallableStatement(Connection con) throws SQLException {
            int inputLen = (objects == null) ? 0 : objects.length;
            StringBuilder call = new StringBuilder("{call ");
            call.append(procName).append('(');
            for (int i = 0; i < inputLen; i++) {
                call.append('?');
                call.append(',');
            }//for input
            if (outParamTypes != null) {
                for (int j = 0; j < outParamTypes.length;) {
                    call.append('?');
                    if (++j < outParamTypes.length) {
                        call.append(',');
                    }
                } // for output
            }
            call.append(")}");//for output

            CallableStatement cs = null;
            cs = con.prepareCall(call.toString());
            //cs.registerOutParameter(inputLen + 1, java.sql.Types.INTEGER);//output
            int index = 1;
            if (objects != null) {
                for (Object obj : objects) {
                    Class clazz = obj.getClass();
                    if (String.class.equals(clazz)) {
                        cs.setString(index, (String) obj);
                    } else if (Boolean.class.equals(clazz) || boolean.class.equals(clazz)) {
                        cs.setBoolean(index, (Boolean) obj);
                    } else if (Byte.class.equals(clazz) || byte.class.equals(clazz)) {
                        cs.setByte(index, (Byte) obj);
                    } else if (Short.class.equals(clazz) || short.class.equals(clazz)) {
                        cs.setShort(index, (Short) obj);
                    } else if (Integer.class.equals(clazz) || int.class.equals(clazz)) {
                        cs.setInt(index, (Integer) obj);
                    } else if (Long.class.equals(clazz) || long.class.equals(clazz)) {
                        cs.setLong(index, (Long) obj);
                    } else if (Float.class.equals(clazz) || float.class.equals(clazz)) {
                        cs.setFloat(index, (Float) obj);
                    } else if (Double.class.equals(clazz) || Number.class.equals(clazz) || double.class.equals(clazz)) {
                        cs.setDouble(index, (Double) obj);
                    } else if (byte[].class.equals(clazz)) {
                        cs.setBytes(index, (byte[]) obj);
                    } else if (java.sql.Date.class.equals(clazz)) {
                        cs.setDate(index, (java.sql.Date) obj);
                    } else if (java.sql.Time.class.equals(clazz)) {
                        cs.setTime(index, (java.sql.Time) obj);
                    } else if (java.sql.Timestamp.class.equals(clazz) || java.util.Date.class.equals(clazz)) {
                        cs.setTimestamp(index, (java.sql.Timestamp) obj);
                    } else if (BigDecimal.class.equals(clazz)) {
                        cs.setBigDecimal(index, (BigDecimal) obj);
                    } else if (Blob.class.equals(clazz)) {
                        cs.setBlob(index, (Blob) obj);
                    } else if (Clob.class.equals(clazz)) {
                        cs.setClob(index, (Clob) obj);
                    } else {
                    }
                    index++;
                }//for input
            }
            if (outParamTypes != null) {
                for (int j = 0; j < outParamTypes.length; ++j) {
                    cs.registerOutParameter(index++, outParamTypes[j]);
                } // register output params
            }
            return cs;
        }

        public ProcCallableStatementCreator(String proc, int[] outParamTypes, Object[] objects) {
            this.procName = proc;
            this.outParamTypes = outParamTypes;
            this.objects = objects;
        }
    }

    private class ProcCallableStatementCallback implements CallableStatementCallback {

        private int fromIndex;
        private int[] outParamTypes = null;
        private List<Object> outList = null;

        public ProcCallableStatementCallback(int fromIndex, int[] outParamTypes, List<Object> outList) {
            this.fromIndex = fromIndex;
            this.outParamTypes = outParamTypes;
            this.outList = outList;
        }

        public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
            cs.executeQuery();
            if (outParamTypes != null) {
                if (outList == null) {
                    outList = new ArrayList<Object>(outParamTypes.length);
                }
                for (int t : outParamTypes) {
                    switch (t) {
                        case java.sql.Types.INTEGER:
                        case java.sql.Types.BIGINT:
                        case java.sql.Types.TINYINT:
                            outList.add(cs.getInt(fromIndex++));
                            break;
                        case java.sql.Types.CHAR:
                        case java.sql.Types.VARCHAR:
                        case java.sql.Types.LONGVARCHAR:
                            outList.add(cs.getString(fromIndex++));
                            break;
                        case java.sql.Types.BOOLEAN:
                            outList.add(cs.getBoolean(fromIndex++));
                            break;
                        case java.sql.Types.FLOAT:
                            outList.add(cs.getFloat(fromIndex++));
                            break;
                        case java.sql.Types.DOUBLE:
                            outList.add(cs.getDouble(fromIndex++));
                            break;
                        case java.sql.Types.DATE:
                        case java.sql.Types.TIMESTAMP:
                            outList.add(cs.getTime(fromIndex++));
                            break;
                    }
                }
            }
            return outList;
        }
    }

    protected RowMapper getIdRowMapper(String colName) {
        return new IdRowMapper(colName);
    }

    class IdRowMapper implements RowMapper {

        private String colName = "id";

        IdRowMapper() {
        }

        IdRowMapper(String colName) {
            if (!StringUtils.isEmpty(colName)) {
                this.colName = colName;
            }
        }

        public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getInt(colName);
        }
    }

    protected RowMapper getStringRowMapper(String colName) {
        return new StringRowMapper(colName);
    }

    class StringRowMapper implements RowMapper {

        private String colName = "id";

        StringRowMapper() {
        }

        StringRowMapper(String colName) {
            if (!StringUtils.isEmpty(colName)) {
                this.colName = colName;
            }
        }

        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString(colName);
        }
    }
}
