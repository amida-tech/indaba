package com.ocs.indaba.dao.common;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.ocs.indaba.util.EntityBeanDescriptor;
import com.ocs.indaba.util.EntityBeanPersistentHelper;
import com.ocs.indaba.util.EntityBeanDescriptor.FieldDescriptor;
import org.apache.log4j.Logger;

@SuppressWarnings("unchecked")
public class SmartDaoMySQLHelper {

	public static final int MAXIMUM_AUTO_JOIN_LEVEL = 1;
	
	private static final Logger logger = Logger.getLogger(SmartDaoMySQLHelper.class);

	public static String generateSelectClause(Class clazz) {
		final StringBuilder selectClause = new StringBuilder();
		final StringBuilder fromClause = new StringBuilder();
		final EntityBeanDescriptor descriptor = EntityBeanPersistentHelper.getEntityBeanDescriptor(clazz);

		final String tableName = descriptor.getTableName();

		selectClause.append("select ");
		selectClause.append(generateSelectColumnsClause(null, tableName, clazz, 0)).deleteCharAt(selectClause.length() - 1);

		fromClause.append(" from ").append(tableName).append(generateFromClause(clazz, 0));

		return selectClause.append(fromClause).append(" ").toString();
	}

	private static String generateSelectColumnsClause(String prefix, String tableName, Class clazz, int i) {
		StringBuilder sb = new StringBuilder();
		// maximum auto join level is 1
		if (i > MAXIMUM_AUTO_JOIN_LEVEL)
			return sb.toString();

		final EntityBeanDescriptor descriptor = EntityBeanPersistentHelper.getEntityBeanDescriptor(clazz);
		
		for (FieldDescriptor fd : descriptor.getFieldDescriptors()) {
			if (fd.isManyToOne()) {
				final Class targetType = fd.getTargetType();
				if (!clazz.isAssignableFrom(targetType)) {
					sb.append(generateSelectColumnsClause(fd.getFieldName(), EntityBeanPersistentHelper.getEntityBeanDescriptor(targetType).getTableName(),targetType, i + 1));
				}
				else {
					// FIXME - check whether it works for self many-to-one relation
					sb.append(generateSelectColumnsClause(fd.getFieldName(), tableName + '_' + fd.getFieldName(), targetType, i + 1));
				}
			} else {
				sb.append(tableName);
				sb.append(".");
				sb.append(fd.getColumnName());
				sb.append(" as ");
				sb.append(getNormalizedAliasName(prefix, fd.getAliasName()));
				sb.append(",");
			}
		}

		return sb.toString();
	}

	private static String getNormalizedAliasName(String prefix, final String aliasName) {
		return StringUtils.isNotBlank(prefix) ? prefix + "_" + aliasName : aliasName;
	}

	private static String generateFromClause(Class clazz, int i) {
		StringBuilder sb = new StringBuilder();

		if (i > MAXIMUM_AUTO_JOIN_LEVEL)
			return sb.toString();

		final EntityBeanDescriptor descriptor = EntityBeanPersistentHelper.getEntityBeanDescriptor(clazz);
		final String tableName = descriptor.getTableName();
		
		for (FieldDescriptor fd : descriptor.getFieldDescriptors()) {
			if (fd.isManyToOne()) {
				final Class targetType = fd.getTargetType();
				if (!clazz.isAssignableFrom(targetType)) {
					sb.append(" join ");
					final EntityBeanDescriptor targetTypeDescriptor = EntityBeanPersistentHelper.getEntityBeanDescriptor(targetType);
					final String targetTypeTableName = targetTypeDescriptor.getTableName();
					sb.append(targetTypeTableName);
					sb.append(" on ");
					sb.append(tableName);
					sb.append(".");
					sb.append(fd.getColumnName());
					sb.append(" = ");
					sb.append(targetTypeTableName);
					sb.append(".");
					sb.append(targetTypeDescriptor.getPrimaryKeyName());

					sb.append(generateFromClause(targetType, i + 1));
				}
				else {
					// TODO - self many-to-one relation
					String aliasTableName = tableName + "_" + fd.getFieldName();
					
					sb.append(" join `");
					sb.append(tableName);
					sb.append("` as ");
					sb.append(aliasTableName);
					sb.append(" on `");
					sb.append(tableName);
					sb.append("`.`");
					sb.append(fd.getColumnName());
					sb.append("` = `");
					sb.append(aliasTableName);
					sb.append("`.`");
					sb.append(descriptor.getPrimaryKeyName()).append("`");
					
					sb.append(generateFromClause(targetType, MAXIMUM_AUTO_JOIN_LEVEL + 1));
				}
			}
		}

		return sb.toString();
	}
	
	public static <T> RowMapper generateRowMapper(final Class<T> entityClass, final boolean normalized) {
		final EntityBeanDescriptor entityBeanDescriptor = EntityBeanPersistentHelper.getEntityBeanDescriptor(entityClass);
		
		return new RowMapper() {
            public T mapRow(ResultSet rs, int rowNum) throws SQLException {
                try {
                    T object = (T) entityClass.newInstance();

                    final ResultSetMetaData metaData = rs.getMetaData();
                    int columnLength = metaData.getColumnCount();
                    
                    Pattern p = Pattern.compile("^" + entityBeanDescriptor.getTableName() + "_\\d+$");
                    for (int i = 1; i <= columnLength; i++) {
                        String aliasName = metaData.getColumnLabel(i);
                        
                        if (normalized && p.matcher(aliasName).matches()) { // means simple property                        	
                            String columnName = entityBeanDescriptor.getColumnNameByAliasName(aliasName);
                        	String fieldName = entityBeanDescriptor.getFieldNameByAliasName(aliasName);               
                        	
                            Field field = object.getClass().getDeclaredField(fieldName);                            
                        	
                        	if (boolean.class.isAssignableFrom(field.getType()) || Boolean.class.isAssignableFrom(field.getType())) {
                                PropertyUtils.setProperty(object, fieldName, rs.getBoolean(aliasName));
                                continue;
                            }

                            if (field.getType().isEnum()) {
                                int ordinal = rs.getInt(aliasName);
                                PropertyUtils.setProperty(object, fieldName, field.getType().getEnumConstants()[ordinal]);
                                continue;
                            }
                            
                        	int type = entityBeanDescriptor.getColumnType(columnName);
                        	PropertyUtils.setProperty(object, fieldName, getValueFromResultSet(rs, aliasName, type));
                        }
                        else if (normalized) {// many-to-one relation
                        	try {
	                        	String fieldName = aliasName.split("_")[0];
	                        	FieldDescriptor fd = entityBeanDescriptor.getFieldDescriptorByFieldName(fieldName);
	                        	final Class targetType = fd.getTargetType();
								EntityBeanDescriptor targetTypeDescriptor = EntityBeanPersistentHelper.getEntityBeanDescriptor(targetType);
	                        	
	                        	String tmpAliasName = aliasName.substring(aliasName.indexOf('_') + 1);
	                        	Object fieldObject = safeGetProperty(object, fieldName, targetType);
	                        	
	                        	if (tmpAliasName.startsWith(targetTypeDescriptor.getTableName())) {
	                        		final String columnName = targetTypeDescriptor.getColumnNameByAliasName(tmpAliasName);
									PropertyUtils.setProperty(fieldObject, targetTypeDescriptor.getFieldNameByAliasName(tmpAliasName), getValueFromResultSet(rs, aliasName, targetTypeDescriptor.getColumnType(columnName)));
	                        	} else {
	                        		// auto join level > 1 
	                        	}
	                        } catch (Exception e) {
	                            // skip this column name
	                            logger.error(e);
	                        }
                        } else {
                        	String columnName = metaData.getColumnName(i);
                        	FieldDescriptor fd = entityBeanDescriptor.getFieldDescriptorByColumnName(columnName);     
                        	if (fd == null) {
                        		// skip
                        		continue;
                        	}
                        	if (!fd.isManyToOne() && !fd.isManyToMany() && !fd.isOneToMany()) {
	                        	final String fieldName = entityBeanDescriptor.getFieldNameByColumnName(columnName);
	                        	
								Field field = object.getClass().getDeclaredField(fieldName);                       
	                        	
	                        	if (boolean.class.isAssignableFrom(field.getType()) || Boolean.class.isAssignableFrom(field.getType())) {
	                                PropertyUtils.setProperty(object, fieldName, rs.getBoolean(columnName));
	                                continue;
	                            }
	
	                            if (field.getType().isEnum()) {
	                                int ordinal = rs.getInt(columnName);
	                                PropertyUtils.setProperty(object, fieldName, field.getType().getEnumConstants()[ordinal]);
	                                continue;
	                            }
	                            
	                            int type = entityBeanDescriptor.getColumnType(columnName);
	                        	try {
	                        		PropertyUtils.setProperty(object, fieldName, getValueFromResultSet(rs, columnName, type));
	                        	} catch (Exception e) {
	                        		PropertyUtils.setProperty(object, StringUtils.capitalize(fieldName), getValueFromResultSet(rs, columnName, type));
								}
                        	}
                        	else if (fd.isManyToOne()) {
                            	final Class targetType = fd.getTargetType();
                            	final SmartDao targetTypeDao = DaoRepository.getDao(targetType);
                            	if (targetTypeDao != null) {
                            		Object v = targetTypeDao.get((Serializable) getValueFromResultSet(rs, columnName, entityBeanDescriptor.getColumnType(columnName)));
                            		PropertyUtils.setProperty(object, fd.getFieldName(), v);
                            	}
                            }
	                    }
                    }                    

                    return object;
                } catch (Exception e) {
                    throw new RuntimeException("Can not instantiate a new object of class '" + entityClass + "' due to: ", e);
                }
            }

			private Object getValueFromResultSet(ResultSet rs, String columnName, int type)
					throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SQLException {
				
				switch (type) {
				    case Types.VARCHAR:
				    //case Types.NVARCHAR:
				    case Types.CHAR:
				    //case Types.NCHAR:
				    case Types.LONGVARCHAR:
				    //case Types.LONGNVARCHAR:
				        return rs.getString(columnName);
				    case Types.BOOLEAN:
				        return rs.getBoolean(columnName);
				    case Types.TINYINT:
				        return rs.getByte(columnName);
				    case Types.SMALLINT:
				        return rs.getShort(columnName);
				    case Types.INTEGER:
				        return rs.getInt(columnName);
				    case Types.BIGINT:
				        return rs.getLong(columnName);
				    case Types.REAL:
				        return rs.getFloat(columnName);
				    case Types.FLOAT:
				        return rs.getDouble(columnName);
				    case Types.DOUBLE:
				        return rs.getDouble(columnName);
				    case Types.BINARY:
				    case Types.VARBINARY:
				    case Types.LONGVARBINARY:
				        return rs.getBytes(columnName);
				    case Types.DATE:
				        return rs.getDate(columnName);
				    case Types.TIME:
				        return rs.getTime(columnName);
				    case Types.TIMESTAMP:
				        return rs.getTimestamp(columnName);
				    case Types.BLOB:
				        return rs.getBlob(columnName);
				    default:
				        return rs.getObject(columnName);
				}
			}

			private Object safeGetProperty(Object object, String fieldName, final Class targetType) 
				throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
				
				Object o = PropertyUtils.getProperty(object, fieldName);
				if (o == null) {
					o = targetType.newInstance();
					PropertyUtils.setProperty(object, fieldName, o);
				}
				
				return o;
			}
        };
    }
}

