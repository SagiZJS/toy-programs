package jishun.sorm.bean;

/**
 * 
 * @author Sagi
 *
 */
public class ColumnInfo {
    
    public static final int PRIMARY = 1;
    public static final int NORMAL = 0;
    public static final int FOREIGN = 1;
    
    private String name;
    
    private String dataType;
    
    private int keyType;
    
    public ColumnInfo() {
    }
    
    public ColumnInfo(String name, String dataType, int keyType) {
        super();
        this.name = name;
        this.dataType = dataType;
        this.keyType = keyType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getKeyType() {
        return keyType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }

    
}
