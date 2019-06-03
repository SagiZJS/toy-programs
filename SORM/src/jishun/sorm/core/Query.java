package jishun.sorm.core;

import java.util.List;

/**
 * 
 * a query interface which defines the basic operation supported by this class
 * 
 * @author Sagi
 * 
 */
public interface Query {

    /**
     * execute a DML statement
     * 
     * @return number of rows that are modified by this statement
     * 
     */
    int executeDML(String sql, Object... params);
    
    /**
     * insert a Object
     * @param obj to insert
     */
    void insert(Object obj);
    
    /**
     * delete the row where id equals the input id in the specific table
     * 
     * @param table where the row to be deleted
     * @param primaryKeyValue the key for identifying the data
     */
    
    void delete(Class<?> tableClass, Object primaryKeyValue);
    
    /**
     * delete the object in corresponding table
     * @param obj to be deleted
     */
    void delete(Object obj); 
    
    
    /**
     * update one row
     * 
     * @param obj the updated object representing a row in database
     * 
     * @param fieldNames fields needed to be updated
     * 
     */
    void update(Object obj, String... fieldNames);
    
    /**
     * execute a SQL query and get the result in a list
     * @param sql statement to be execute
     * @param table the bean class representing a row in database
     * @param params for SQL statement
     * @return  list of the bean object
     */
    List<?> queryRows(String sql, Class<?> table, Object... params);
    
    /**
     * execute a SQL query and get the result in a bean class object
     * <p>if the result has multiple rows only the first row will be return</p> 
     * @param sql statement to be execute
     * @param table the bean class representing a row in database
     * @param params for SQL statement
     * @return bean object representing the result row
     */
    Object queryUniqueRow(String sql, Class<?> table, Object... params);
    
    /**
     * execute a SQL query and get the result value in a object
     * @param sql statement to be execute
     * @param params for SQL statement
     * @return object representing the result value
     */
    Object queryValue(String sql, Object... params);
    
    
}
