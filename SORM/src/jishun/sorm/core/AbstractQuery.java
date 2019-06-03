package jishun.sorm.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import jishun.sorm.bean.ColumnInfo;
import jishun.sorm.bean.TableInfo;
import jishun.sorm.utils.JDBCUtils;
import jishun.sorm.utils.ReflectUtils;

public abstract class AbstractQuery implements Query {

    public static interface ResultSetProcessor {
        Object processResultSet(ResultSet resultSet);
    }

    private Object queryTemplate(String sql, ResultSetProcessor processor, Object... params) {
        Object results = null;
        Connection connection = DBManager.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            JDBCUtils.setParams(statement, params);
            try (ResultSet resultSet = statement.executeQuery()) {
                results = processor.processResultSet(resultSet);
//                ResultSetMetaData metaData = resultSet.getMetaData();
//                while (resultSet.next()) {
//                    
//                    try {
//                        Object row = beanClass.newInstance();
//                        for (int i = 0; i < metaData.getColumnCount(); i++) {
//                            Object columnValue = resultSet.getObject(i + 1);
//                            if (columnValue == null) {
//                                continue;
//                            }
//
//                            String columnTypeName = metaData.getColumnTypeName(i + 1);
//                            String columnJavaTypeName = MysqlTypeConvertor.getMysqlTypeConvertor()
//                                    .databaseType2JavaType(columnTypeName);
//                            if (columnJavaTypeName.equals("java.sql.Blob")) {
//                                columnValue = resultSet.getBlob(i + 1);
//                            }
//                            if (columnJavaTypeName.equals("java.sql.Clob")) {
//                                columnValue = resultSet.getClob(i + 1);
//                            }
//
//                            String columnName = metaData.getColumnLabel(i + 1);
//
//                            ReflectUtils.invokeSetter(row, columnName, columnJavaTypeName, columnValue);
//
//                        }
//                        results.add(row);
//                    } catch (InstantiationException e) {
//                        e.printStackTrace();
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBManager.endConnection(connection);
        return results;
    }

    @Override
    public int executeDML(String sql, Object... params) {
        Connection connection = DBManager.getConnection();
        int count = 0;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            JDBCUtils.setParams(statement, params);
            count = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBManager.endConnection(connection);
        return count;
    }

    @Override
    public void insert(Object obj) {
        // insert value that not null
        StringBuilder columnsNameBlock = new StringBuilder();
        StringBuilder valuesBlock = new StringBuilder();

        List<Object> params = new ArrayList<>();
        Object param = null;

        // to get the fields name, there are two ways,
        // first the already defined tableInfo,
        // second use Reflect, Class.getDecaledFields()
        TableInfo tableInfo = TableContext.getPoClassTableMap().get(obj.getClass());

        if (ReflectUtils.invokeGetter(obj, tableInfo.getSinglePrimaryKey().getName()) == null) {
            throw new IllegalArgumentException("primaryKey cannot be null");
        }
        for (ColumnInfo columnInfo : tableInfo.getColumns().values()) {

            if ((param = ReflectUtils.invokeGetter(obj, columnInfo.getName())) != null) {
                if (columnsNameBlock.length() != 0) {
                    columnsNameBlock.append(", ");
                    valuesBlock.append(", ");
                }
                columnsNameBlock.append(columnInfo.getName());
                valuesBlock.append("?");
                params.add(param);
            }
        }
        String insert = String.format("insert into %s (%s) values (%s)", tableInfo.getTableName(), columnsNameBlock,
                valuesBlock);

        System.out.println(String.format("inserted into %s %d row(s)", tableInfo.getTableName(),
                executeDML(insert, params.toArray())));

    }

    @Override
    public void delete(Class<?> POClass, Object id) {
        // delete from t_users where id=2
        Map<Class<?>, TableInfo> map = TableContext.getPoClassTableMap();
        TableInfo tableInfo = map.get(POClass);
        String delete = String.format("delete from %s where %s=?", tableInfo.getTableName(),
                tableInfo.getSinglePrimaryKey().getName());
        System.out.println(String.format("deleted %d row(s)", executeDML(delete, id)));
    }

    @Override
    public void delete(Object obj) {
        // reflect - get field
        Class<?> POClass = obj.getClass();
        TableInfo tableInfo = TableContext.getPoClassTableMap().get(POClass);
        ColumnInfo primaryKey = tableInfo.getSinglePrimaryKey();
        delete(obj.getClass(), ReflectUtils.invokeGetter(obj, primaryKey.getName()));
    }

    @Override
    public void update(Object obj, String... fieldNames) {

        StringBuilder columnsNameBlock = new StringBuilder();
        List<Object> params = new ArrayList<>();
        Object param = null;
        for (String field : fieldNames) {
            if ((param = ReflectUtils.invokeGetter(obj, field)) != null) {
                if (columnsNameBlock.length() != 0) {
                    columnsNameBlock.append(", ");
                }
                columnsNameBlock.append(field + "=?");
                params.add(param);
            }
        }

        StringBuilder primaryKeyBlock = new StringBuilder();
        TableInfo tableInfo = TableContext.getPoClassTableMap().get(obj.getClass());
        String primaryKeyName = tableInfo.getSinglePrimaryKey().getName();
        if ((param = ReflectUtils.invokeGetter(obj, primaryKeyName)) != null) {
            primaryKeyBlock.append(primaryKeyName + "=");
            primaryKeyBlock.append("?");
            params.add(param);
        }

        // UPDATE `testjdbc`.`t_users` SET `username` = 'update' WHERE (`ID` = '123');

        String update = String.format("update %s set %s where (%s)", tableInfo.getTableName(), columnsNameBlock,
                primaryKeyBlock);
        System.out.println(
                String.format("updated %s %d row(s)", tableInfo.getTableName(), executeDML(update, params.toArray())));

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object> queryRows(String sql, Class<?> beanClass, Object... params) {
        List<Object> results = new ArrayList<>();
        results = (List<Object>) queryTemplate(sql, new ResultSetProcessor() {
            @Override
            public Object processResultSet(ResultSet resultSet) {
                List<Object> results = new ArrayList<>();
                ResultSetMetaData metaData;
                try {
                    metaData = resultSet.getMetaData();
                    while (resultSet.next()) {
                        try {
                            Object row = beanClass.newInstance();
                            for (int i = 0; i < metaData.getColumnCount(); i++) {
                                Object columnValue = resultSet.getObject(i + 1);
                                if (columnValue == null) {
                                    continue;
                                }

                                String columnTypeName = metaData.getColumnTypeName(i + 1);
                                String columnJavaTypeName = MysqlTypeConvertor.getMysqlTypeConvertor()
                                        .databaseType2JavaType(columnTypeName);
                                if (columnJavaTypeName.equals("java.sql.Blob")) {
                                    columnValue = resultSet.getBlob(i + 1);
                                }
                                if (columnJavaTypeName.equals("java.sql.Clob")) {
                                    columnValue = resultSet.getClob(i + 1);
                                }

                                String columnName = metaData.getColumnLabel(i + 1);

                                ReflectUtils.invokeSetter(row, columnName, columnJavaTypeName, columnValue);

                            }
                            results.add(row);
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                return results;
            }
        }, params);
        return results;
    }

    @Override
    public Object queryUniqueRow(String sql, Class<?> table, Object... params) {
        List<Object> result = queryRows(sql, table, params);
        return result.size() == 0 ? null : result;
    }

    @Override
    public Object queryValue(String sql, Object... params) {
        return queryTemplate(sql, new ResultSetProcessor() {
            @Override
            public Object processResultSet(ResultSet resultSet) {
                try {
                    if (resultSet.next()) {
                        return resultSet.getObject(1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, params);
    }
    
    protected AbstractQuery() {
    }
}
