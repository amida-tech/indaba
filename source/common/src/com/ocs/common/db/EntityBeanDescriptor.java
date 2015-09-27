/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.common.db;

import java.sql.Types;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import javax.persistence.GenerationType;
import org.apache.log4j.Logger;

/**
 * @author Tianguang Tang (tangtianguang@gmail.com)
 * @since 1.0
 */
public class EntityBeanDescriptor {

    private static final Logger logger = Logger.getLogger(EntityBeanDescriptor.class);
    private final Map<String, FieldDescriptor> aliasDescriptorMap = new WeakHashMap<String, FieldDescriptor>();
    private final Map<String, FieldDescriptor> columnDescriptorMap = new WeakHashMap<String, FieldDescriptor>();
    private final Map<String, FieldDescriptor> filedDescriptorMap = new WeakHashMap<String, FieldDescriptor>();
    private final Set<FieldDescriptor> fieldDescriptors = new HashSet<FieldDescriptor>();
    private final Set<FieldDescriptor> manyToOne = new HashSet<FieldDescriptor>();
    private final Set<FieldDescriptor> oneToMany = new HashSet<FieldDescriptor>();
    private final Set<FieldDescriptor> manyToMany = new HashSet<FieldDescriptor>();
    private final int[] columnSqlTypes;
    private final StringBuilder insertClausePrefix;
    private final StringBuilder deleteClausePrefix;
    private final StringBuilder selectClausePrefix;
    private final StringBuilder updateClausePrefix;
    private final String tableName;
    private final String primaryKeyName;
    private final GenerationType primaryKeyGenerationType;

    public EntityBeanDescriptor(String tableName, String primaryKeyName, GenerationType primaryKeyGenerationType, Set<FieldDescriptor> fieldDescriptors) {
        this.tableName = tableName;
        this.primaryKeyName = primaryKeyName;
        this.primaryKeyGenerationType = primaryKeyGenerationType;
        this.fieldDescriptors.addAll(fieldDescriptors);

        for (FieldDescriptor fd : fieldDescriptors) {
            this.columnDescriptorMap.put(fd.getColumnName(), fd);
            this.filedDescriptorMap.put(fd.getFieldName(), fd);
            this.aliasDescriptorMap.put(fd.getAliasName(), fd);
        }

        for (FieldDescriptor descriptor : columnDescriptorMap.values()) {
            if (!descriptor.isManyToMany() && !descriptor.isOneToMany()) {
                if (descriptor.isManyToOne()) {
                    manyToOne.add(descriptor);
                }
            } else if (descriptor.isOneToMany()) {
                oneToMany.add(descriptor);
            } else if (descriptor.isManyToMany()) {
                manyToMany.add(descriptor);
            }
        }

        final int length = fieldDescriptors.size();
        this.columnSqlTypes = (length == 1) ? new int[1] : new int[length - 1];

        this.insertClausePrefix = new StringBuilder("insert into ").append(tableName).append("(");
        this.deleteClausePrefix = new StringBuilder("delete from ").append(tableName).append(" ");
        this.selectClausePrefix = new StringBuilder("select ");
        this.updateClausePrefix = new StringBuilder("update ").append(tableName).append(" set ");

        StringBuilder valuesClause = new StringBuilder(" values(");

        int j = 0;
        for (FieldDescriptor fd : this.fieldDescriptors) {
            final String columnName = fd.getColumnName();
            if (fd.getColumnType() == Types.OTHER) {
                continue;
            }
            if (!columnName.equalsIgnoreCase(primaryKeyName)) {
                this.columnSqlTypes[j++] = fd.getColumnType();
            }
            if (!columnDescriptorMap.get(columnName).isManyToMany() && !columnDescriptorMap.get(columnName).isOneToMany()) {
                if (!columnName.equalsIgnoreCase(primaryKeyName) || !isPKIncremental()) {
                    insertClausePrefix.append("`" + columnName + "`");
                    insertClausePrefix.append(",");
                    valuesClause.append("?,");
                    /*
                    if (columnName.equalsIgnoreCase("password")) {
                    valuesClause.append("password(?),");
                    } else {
                    valuesClause.append("?,");
                    }
                     *
                     */
                }

                selectClausePrefix.append("`" + columnName + "`").append(",");

                if (!columnName.equalsIgnoreCase(primaryKeyName)) {
                    updateClausePrefix.append("`" + columnName + "`").append("=?,");
                }
            }
        }
        insertClausePrefix.deleteCharAt(insertClausePrefix.length() - 1);
        valuesClause.deleteCharAt(valuesClause.length() - 1);
        insertClausePrefix.append(")");
        valuesClause.append(") ");
        insertClausePrefix.append(valuesClause);
        if (this.fieldDescriptors.size() == 1) {
            insertClausePrefix.setLength(0);
            insertClausePrefix.append("insert into ").append(tableName).append("(id) values(?)");
            columnSqlTypes[0] = Types.INTEGER;
        }

        selectClausePrefix.deleteCharAt(selectClausePrefix.length() - 1).append(" from ").append(tableName).append(" ");

        updateClausePrefix.deleteCharAt(updateClausePrefix.length() - 1).append(" where ").append(primaryKeyName).append("=?");
    }

    public Set<FieldDescriptor> getFieldDescriptors() {
        return fieldDescriptors;
    }

    public FieldDescriptor getFieldDescriptorByFieldName(String fieldName) {
        return filedDescriptorMap.get(fieldName);
    }

    public FieldDescriptor getFieldDescriptorByColumnName(String columnName) {
        return columnDescriptorMap.get(columnName);
    }

    public Set<FieldDescriptor> getManyToOne() {
        return manyToOne;
    }

    public Set<FieldDescriptor> getOneToMany() {
        return oneToMany;
    }

    public Set<FieldDescriptor> getManyToMany() {
        return manyToMany;
    }

    public String getTableName() {
        return tableName;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public GenerationType getPrimaryKeyGenerationType() {
        return primaryKeyGenerationType;
    }

    public boolean isPKIncremental() {
        return getPrimaryKeyGenerationType() == GenerationType.SEQUENCE || getPrimaryKeyGenerationType() == GenerationType.IDENTITY;
    }

    public StringBuilder getInsertClausePrefix() {
        return insertClausePrefix;
    }

    public StringBuilder getDeleteClausePrefix() {
        return deleteClausePrefix;
    }

    public StringBuilder getSelectClausePrefix() {
        return selectClausePrefix;
    }

    public StringBuilder getUpdateClausePrefix() {
        return updateClausePrefix;
    }

    public int[] getColumnSqlTypes() {
        return columnSqlTypes;
    }

    public String getFieldNameByColumnName(String columnName) {
        return columnDescriptorMap.get(columnName).getFieldName();
    }

    public String getColumnNameByFieldName(String fieldName) {
        return filedDescriptorMap.get(fieldName).getColumnName();
    }

    public String getColumnNameByAliasName(String aliasName) {
        return aliasDescriptorMap.get(aliasName).getColumnName();
    }

    public String getFieldNameByAliasName(String aliasName) {
        return aliasDescriptorMap.get(aliasName).getFieldName();
    }

    public String[] getFieldNames() {
        Set<String> fieldNameSet = filedDescriptorMap.keySet();
        return fieldNameSet.toArray(new String[fieldNameSet.size()]);
    }

    public int getColumnType(String columnName) {
        return columnDescriptorMap.get(columnName).getColumnType();
    }

    @SuppressWarnings("unchecked")
    public static class FieldDescriptor {

        private String fieldName;
        private String columnName;
        private String aliasName;
        private int columnType;
        private Class fieldType;
        private Class targetType;
        private boolean manyToMany;
        private boolean oneToMany;
        private boolean manyToOne;
        private String joinColumnName;
        private String inverseJoinColumnName;
        private String joinTableName;

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getAliasName() {
            return aliasName;
        }

        public void setAliasName(String aliasName) {
            this.aliasName = aliasName;
        }

        public int getColumnType() {
            return columnType;
        }

        public void setColumnType(int columnType) {
            this.columnType = columnType;
        }

        public Class getFieldType() {
            return fieldType;
        }

        public void setFieldType(Class fieldType) {
            this.fieldType = fieldType;
        }

        public Class getTargetType() {
            return targetType;
        }

        public void setTargetType(Class targetType) {
            this.targetType = targetType;
        }

        public boolean isManyToMany() {
            return manyToMany;
        }

        public void setManyToMany(boolean manyToMany) {
            this.manyToMany = manyToMany;
        }

        public boolean isOneToMany() {
            return oneToMany;
        }

        public void setOneToMany(boolean oneToMany) {
            this.oneToMany = oneToMany;
        }

        public boolean isManyToOne() {
            return manyToOne;
        }

        public void setManyToOne(boolean manyToOne) {
            this.manyToOne = manyToOne;
        }

        public String getJoinColumnName() {
            return joinColumnName;
        }

        public void setJoinColumnName(String joinColumnName) {
            this.joinColumnName = joinColumnName;
        }

        public String getInverseJoinColumnName() {
            return inverseJoinColumnName;
        }

        public void setInverseJoinColumnName(String inverseJoinColumnName) {
            this.inverseJoinColumnName = inverseJoinColumnName;
        }

        public String getJoinTableName() {
            return joinTableName;
        }

        public void setJoinTableName(String joinTableName) {
            this.joinTableName = joinTableName;
        }
    }
}
