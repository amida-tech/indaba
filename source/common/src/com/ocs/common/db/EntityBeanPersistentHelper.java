/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.common.db;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Types;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import javax.persistence.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author Tianguang Tang (tangtianguang@gmail.com)
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public class EntityBeanPersistentHelper {

    private static final Logger logger = Logger.getLogger(EntityBeanPersistentHelper.class);
    private static final Map<Class, EntityBeanDescriptor> recentlyParsedMap = new WeakHashMap<Class, EntityBeanDescriptor>();

    public static EntityBeanDescriptor getEntityBeanDescriptor(Class clazz) {
        logger.debug("Get entity bean's descriptor of class: " + clazz.getCanonicalName());

        if (!recentlyParsedMap.containsKey(clazz)) {
            logger.debug("Cache the entity bean's descriptor: " + clazz.getCanonicalName());
            EntityBeanDescriptor descriptor = parse(clazz);

            if (descriptor == null) {
                logger.fatal("No EntityBeanDescriptor found for class: " + clazz.getCanonicalName());
                return null;
            }

            recentlyParsedMap.put(clazz, descriptor);
        }
        logger.debug("===> getEntityBeanDescriptor(" + clazz + "): " + recentlyParsedMap.get(clazz));
        return recentlyParsedMap.get(clazz);
    }

    private static EntityBeanDescriptor parse(Class clazz) {
        if (clazz == null) {
            return null;
        }

        final String className = clazz.getCanonicalName();

        logger.debug("Begin to parse class '" + className + "'");

        String tableName = null;
        String primaryKeyName = null;
        String primaryKeyFieldName = null;

        GenerationType primaryKeyGenerationType = null;

        if (!clazz.isAnnotationPresent(Entity.class)) {
            logger.fatal("Failed to parse class '" + className + "'! It's not annotated by @Entity!");
            return null;
        }

        if (clazz.isAnnotationPresent(Table.class)) {
            tableName = ((Table) clazz.getAnnotation(Table.class)).name();
        }

        if (StringUtils.isBlank(tableName)) {
            tableName = StringUtils.uncapitalize(clazz.getSimpleName());
        }

        logger.debug("Entity bean '" + className + "' maps to table '" + tableName + "'");

        Field[] fields = clazz.getDeclaredFields();

        Set<EntityBeanDescriptor.FieldDescriptor> fieldDescriptors = new HashSet<EntityBeanDescriptor.FieldDescriptor>();

        logger.debug("Begin to parse fields of persistent class '" + className + "'");

        // first loop to find out the primary key of this entity bean in case of self many-to-one relation
        for (Field field : fields) {
            String fieldName = field.getName();
            String columnName = "";

            Method readMethod;
            final Class fieldType = field.getType();

            try {
                readMethod = clazz.getMethod("get" + StringUtils.capitalize(fieldName));

                if (readMethod == null) {
                    logger.fatal("Field '" + fieldName + "' does not have getter/setter methods!");
                    return null;
                }

                if (field.isAnnotationPresent(Id.class) || readMethod.isAnnotationPresent(Id.class)) {
                    if (field.isAnnotationPresent(Column.class)) {
                        columnName = field.getAnnotation(Column.class).name();
                    } else if (readMethod.isAnnotationPresent(Column.class)) {
                        columnName = readMethod.getAnnotation(Column.class).name();
                    }

                    if (StringUtils.isBlank(columnName)) {
                        columnName = fieldName;
                    }

                    primaryKeyName = columnName;
                    primaryKeyFieldName = fieldName;

                    if (field.isAnnotationPresent(GeneratedValue.class)) {
                        primaryKeyGenerationType = field.getAnnotation(GeneratedValue.class).strategy();
                    }

                }
            } catch (Exception e) {
                // skip this field
            }
        }

        logger.debug("The primary key of table '" + tableName + "' is '" + primaryKeyName + "'");

        int i = 0;
        for (Field field : fields) {
            boolean m2m = false, o2m = false, m2o = false;

            String fieldName = field.getName();
            String columnName = "";

            logger.debug("Processing field '" + fieldName + "'");

            Method readMethod;
            try {
                final Class fieldType = field.getType();

                readMethod = clazz.getMethod("get" + StringUtils.capitalize(fieldName));

                if (readMethod == null) {
                    logger.fatal("Field '" + fieldName + "' does not have getter/setter methods!");
                    return null;
                }

                if (field.isAnnotationPresent(Transient.class) || readMethod.isAnnotationPresent(Transient.class)) {
                    logger.debug("Skip field '" + fieldName + "' because it is annonated by @Transient");
                    continue;
                }

                if (field.isAnnotationPresent(OneToMany.class) || readMethod.isAnnotationPresent(OneToMany.class)) {
                    logger.debug("Field '" + fieldName + "' is a one-to-many relation, it will be mapped as a collection of '" + fieldType + "'");
                    o2m = true;
                }

                if (field.isAnnotationPresent(ManyToMany.class) || readMethod.isAnnotationPresent(ManyToMany.class)) {
                    logger.debug("Field '" + fieldName + "' is a many-to-many relation, it will be mapped as a collection of '" + fieldType + "'");
                    m2m = true;
                }

                if (field.isAnnotationPresent(ManyToOne.class) || readMethod.isAnnotationPresent(ManyToOne.class)) {
                    logger.debug("Field '" + fieldName + "' is a many-to-one relation, it will be mapped as an '" + fieldType + "' object");
                    m2o = true;
                }

                if (field.isAnnotationPresent(Column.class)) {
                    columnName = field.getAnnotation(Column.class).name();
                } else if (readMethod.isAnnotationPresent(Column.class)) {
                    columnName = readMethod.getAnnotation(Column.class).name();
                }

                if (StringUtils.isBlank(columnName)) {
                    columnName = fieldName;
                }

                int sqlType = getSqlType(fieldType);
                if (m2o) {
                    JoinColumn jc = field.getAnnotation(JoinColumn.class);
                    if (jc == null) {
                        jc = readMethod.getAnnotation(JoinColumn.class);
                    }

                    if (jc != null && StringUtils.isNotBlank(jc.name())) {
                        columnName = jc.name();
                    }

                    if (fieldType.isAssignableFrom(clazz)) {
                        sqlType = getSqlType(fieldType.getDeclaredField(primaryKeyFieldName).getType());
                    } else {
                        logger.debug("====>>>>> Load From ");
                        final EntityBeanDescriptor desc = EntityBeanPersistentHelper.getEntityBeanDescriptor(fieldType);
                        sqlType = getSqlType(fieldType.getDeclaredField(desc.getFieldNameByColumnName(desc.getPrimaryKeyName())).getType());
                    }
                }

                logger.debug("Field '" + fieldName + "' maps to column '" + columnName + "'");
                logger.debug("Field '" + fieldName + "' maps to column type '" + sqlType + "'");

                EntityBeanDescriptor.FieldDescriptor descriptor = new EntityBeanDescriptor.FieldDescriptor();

                descriptor.setColumnName(columnName);
                descriptor.setFieldName(fieldName);
                descriptor.setAliasName(tableName + "_" + i++);
                descriptor.setColumnType(sqlType);

                if (m2m || o2m) {
                    final Type genericType = field.getGenericType();
                    if (genericType instanceof ParameterizedType) {
                        descriptor.setTargetType((Class) ((ParameterizedType) genericType).getActualTypeArguments()[0]);
                    } else {
                        throw new RuntimeException(clazz + "'s ManyToMany or OneToMany was not configured properly.");
                    }

                    descriptor.setFieldType(field.getType());

                    if (m2m) {
                        JoinTable joinTable = field.getAnnotation(JoinTable.class);
                        if (joinTable == null) {
                            joinTable = readMethod.getAnnotation(JoinTable.class);
                        }

                        if (joinTable != null) {
                            descriptor.setJoinTableName(joinTable.name());
                            descriptor.setJoinColumnName(joinTable.joinColumns()[0].name());
                            descriptor.setInverseJoinColumnName(joinTable.inverseJoinColumns()[0].name());
                        }
                    }

                    if (o2m) {
                        // TODO
                    }
                } else {
                    descriptor.setTargetType(fieldType);
                }

                descriptor.setManyToMany(m2m);
                descriptor.setOneToMany(o2m);
                descriptor.setManyToOne(m2o);

                fieldDescriptors.add(descriptor);
            } catch (Exception e) {
                // skip this field
            }

        }

        return new EntityBeanDescriptor(
                tableName,
                primaryKeyName,
                primaryKeyGenerationType, fieldDescriptors);
    }

    public static int[] getSqlTypes(Object... objects) {
        int[] types = new int[objects.length];

        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null) {
                types[i] = Types.NULL;
            } else {
                types[i] = getSqlType(objects[i].getClass());
            }
        }

        return types;
    }

    private static int getSqlType(Class clazz) {
        if (Enum.class.isAssignableFrom(clazz)) {
            return Types.INTEGER;
        } else if (String.class.equals(clazz)) {
            return Types.VARCHAR;
        } else if (Boolean.class.equals(clazz) || boolean.class.equals(clazz)) {
            return Types.TINYINT;
        } else if (Byte.class.equals(clazz) || byte.class.equals(clazz)) {
            return Types.TINYINT;
        } else if (Short.class.equals(clazz) || short.class.equals(clazz)) {
            return Types.SMALLINT;
        } else if (Integer.class.equals(clazz) || int.class.equals(clazz)) {
            return Types.INTEGER;
        } else if (Long.class.equals(clazz) || long.class.equals(clazz)) {
            return Types.BIGINT;
        } else if (Float.class.equals(clazz) || float.class.equals(clazz)) {
            return Types.REAL;
        } else if (Double.class.equals(clazz) || Number.class.equals(clazz) || double.class.equals(clazz)) {
            return Types.DOUBLE;
        } else if (byte[].class.equals(clazz)) {
            return Types.VARBINARY;
        } else if (java.sql.Date.class.equals(clazz)) {
            return Types.DATE;
        } else if (java.sql.Time.class.equals(clazz)) {
            return Types.TIME;
        } else if (java.sql.Timestamp.class.equals(clazz) || java.util.Date.class.equals(clazz)) {
            return Types.TIMESTAMP;
        } else if (BigDecimal.class.equals(clazz)) {
            return Types.DECIMAL;
        } else if (Blob.class.equals(clazz)) {
            return Types.BLOB;
        } else if (Clob.class.equals(clazz)) {
            return Types.CLOB;
        } else if (Enum.class.isAssignableFrom(clazz)) {
            return Types.INTEGER;
        } else {
            return Types.OTHER;
        }
    }

    public static Serializable getId(Serializable object) {
        try {
            EntityBeanDescriptor beanDescriptor = EntityBeanPersistentHelper.getEntityBeanDescriptor(object.getClass());

            if (beanDescriptor == null) {
                logger.error("");
                return null;
            }

            String idName = beanDescriptor.getFieldNameByColumnName(beanDescriptor.getPrimaryKeyName());

            return (Serializable) PropertyUtils.getProperty(object, idName);
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    public static void setId(Serializable object, Serializable id) {
        try {
            EntityBeanDescriptor beanDescriptor = EntityBeanPersistentHelper.getEntityBeanDescriptor(object.getClass());

            if (beanDescriptor == null) {
                logger.error("");
            }

            String idName = beanDescriptor.getFieldNameByColumnName(beanDescriptor.getPrimaryKeyName());

            PropertyUtils.setProperty(object, idName, id);
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
