package jishun.sorm.core;

/**
 * <p>this class is a implementation of {@link TypeConvertor} for MYSQL.</p>
 * <p>the class use singleton pattern for efficiency consideration.</p>
 * <p>use static method {@link jishun.sorm.core.MysqlTypeConvertor#getMysqlTypeConvertor()} to get an instance of this class</p>
 * 
 * @author Sagi
 *
 */

public class MysqlTypeConvertor implements TypeConvertor {
    
    // this is only for the singleton pattern, could be static method instead
    private static final MysqlTypeConvertor CONVERTOR = new MysqlTypeConvertor();

    @Override
    public String databaseType2JavaType(String columnType) {
        String outputType = null;
        switch (columnType.toLowerCase()) {
        case "varchar":
            outputType = "java.lang.String";
            break;
        case "int":
        case "tinyint":
        case "smallint":
        case "integer":
            outputType = "java.lang.Integer";
            break;
        case "bigint":
            outputType = "java.lang.Long";
            break;
        case "double":
            outputType = "java.lang.Double";
            break;
        case "date":
            outputType = "java.sql.Date";
            break;   
        case "time":
            outputType = "java.sql.Time";
            break;   
        case "timestamp":
            outputType = "java.sql.Timestamp";
            break;   
        case "clob":
        case "longtext":
        case "mediumtext":
        case "tinytext":
        case "text":
            outputType = "java.sql.Clob";
            break;   
        case "longblob":
        case "tinyblob":
        case "mediumblob":
        case "blob":
            outputType = "java.sql.Blob";
            break;   
        default:
            break;
        }
        return outputType;
    }
    
    private MysqlTypeConvertor() {
    }
    
    public static MysqlTypeConvertor getMysqlTypeConvertor() {
        return CONVERTOR;
    }
    
    //  not implemented for this is no use at this time  
    @Override
    public String javaType2DatabaseType(String javaDataType) {
        return null;
    }
}
