package jishun.sorm.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jishun.sorm.bean.ColumnInfo;
import jishun.sorm.bean.TableInfo;
import jishun.sorm.utils.JavaFileUtils;
/**
 * <p> this class is to store the database tables metadata in {@link jishun.sorm.bean.TableInfo} object</p>
 * <p> the java bean files are generated automatically according to database table is done by call {@link TableContext#staticInit()}, which will be called in this class when this class inited</p>
 * @author Sagi
 *
 */
public class TableContext {

    private static boolean initilized = false;

    private static Map<String, TableInfo> tables = new HashMap<>();

    private static Map<Class<?>, TableInfo> poClassTableMap = new HashMap<>();

    private TableContext() {
    }

    static {
        System.out.println("----------------------------------------------\nTableContext: initilizing...");
        staticInit(false);
        System.out.println("TableContext: initilized!\n----------------------------------------------");
    }

    // PO relatives
    // create po Class from the tables which contains database tables metadata
    private static void updateJavaPO() {
        for (TableInfo tableInfo : tables.values()) {
            Class<?> tableClass = JavaFileUtils.createJavaFile(tableInfo, MysqlTypeConvertor.getMysqlTypeConvertor());
            poClassTableMap.put(tableClass, tableInfo);
        }
    }
    
    // reading from database tables and store metadata in tables
    private static void updateTables() {
        try {
            Connection connection = DBManager.getConnection();
            DatabaseMetaData databaseMetaData;
            databaseMetaData = connection.getMetaData();

            ResultSet tablesSet = databaseMetaData.getTables("testjdbc", null, "%", new String[] { "TABLE" });

            while (tablesSet.next()) {
                System.out.println();
                String tableName = (String) tablesSet.getObject("TABLE_NAME");
                System.out.println("TableContext: table name: " + tableName + "\n");

                TableInfo tableInfo = new TableInfo(tableName, new HashMap<>(), new ArrayList<>());
                tables.put(tableName, tableInfo);
                ResultSet columnsSet = databaseMetaData.getColumns(null, null, tableName, null);
                while (columnsSet.next()) {
                    ColumnInfo columnInfo = new ColumnInfo(columnsSet.getString("COLUMN_NAME"),
                            columnsSet.getString("TYPE_NAME"), 0);
                    System.out.print(String.format("TableContext: column name:%s data type: %s\n", columnInfo.getName(),
                            columnInfo.getDataType()));
                    tableInfo.getColumns().put(columnsSet.getString("COLUMN_NAME"), columnInfo);
                }

                ResultSet primaryKeySet = databaseMetaData.getPrimaryKeys("testjdbc", null, tableName);
                while (primaryKeySet.next()) {
                    ColumnInfo primaryColumnInfo = tableInfo.getColumns().get(primaryKeySet.getObject("COLUMN_NAME"));
                    primaryColumnInfo.setKeyType(1); // could be bug
                    tableInfo.getPrimaryKeys().add(primaryColumnInfo);
                }

                if (tableInfo.getPrimaryKeys().size() == 1) {
                    tableInfo.setSinglePrimaryKey(tableInfo.getPrimaryKeys().get(0));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage() + " state: " + e.getSQLState() + " errorcode: " + e.getErrorCode());
            e.printStackTrace();
        }

    }

    /**
     * update ContextTables and the java files,
     * used when the database change tables relations when the connection is still up
     * to avoid asynchrony due to changes to database tables, the Query should call update every time before query, 
     * which will increase the overhead because the update is not stateful.
     * 
     */
    public static void updateContext() {
        System.out.println("updating context");
        staticInit(true);
        System.out.println("context updated");
    }
    
    /**
     * initialize this class, if already inited, nothing will be done
     * 
     * <p> (was called when this class is inited)</p>
     */
    public static void staticInit() {
        staticInit(false);
    }

    private static void staticInit(boolean force) {
        if (!force && initilized) {
            return;
        }
        initilized = true;
        updateTables();
        updateJavaPO();
    }

    public static Map<String, TableInfo> getTableInfos() {
        return tables;
    }

    public static Map<Class<?>, TableInfo> getPoClassTableMap() {
        return poClassTableMap;
    }

}
