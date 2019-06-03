package jishun.sorm.bean;

import java.util.List;
import java.util.Map;

public class TableInfo {
    
    private String tableName;
    
    private Map<String, ColumnInfo> columns;
    
    private ColumnInfo singlePrimaryKey;
    
    private List<ColumnInfo> primaryKeys;

    public TableInfo() {
        
    }
    
    public List<ColumnInfo> getPrimaryKeys() {
        return primaryKeys;
    }
    

    public TableInfo(String tableName, Map<String, ColumnInfo> columns, List<ColumnInfo> primaryKeys) {
        super();
        this.tableName = tableName;
        this.columns = columns;
        this.primaryKeys = primaryKeys;
    }


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public ColumnInfo getSinglePrimaryKey() {
        return singlePrimaryKey;
    }


    public void setSinglePrimaryKey(ColumnInfo singlePrimaryKey) {
        this.singlePrimaryKey = singlePrimaryKey;
    }


    public Map<String, ColumnInfo> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, ColumnInfo> columns) {
        this.columns = columns;
    }


    public void setPrimaryKeys(List<ColumnInfo> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }


}
