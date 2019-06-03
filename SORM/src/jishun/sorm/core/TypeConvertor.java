package jishun.sorm.core;

/**
 * The TypeConvertor is to convert between database data type and java data type.
 * 
 * For different database a specified convertor is needed.
 * 
 * @author Sagi
 *
 */
public interface TypeConvertor {

    /**
     * 
     * @param columnType database data type
     * @return String represented a full-path java data type
     */
    String databaseType2JavaType(String columnType);
    
    /**
     * 
     * @param javaDataType java data type
     * @return database data type
     */
    String javaType2DatabaseType(String javaDataType);

}
