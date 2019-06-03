package jishun.sorm.core;

/**
 *  get the query instance according to the jdb.properties file
 *  <p> to use a different query class change the properties file</p>
 * 
 * @author Sagi
 *
 */

public class QueryFactory {

    private static Class<?> queryClass;
    
    static {
        try {
            queryClass = Class.forName(DBManager.getConfiguration().getQueryClass());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private QueryFactory() {
    }
    /**
     * get the Query instance
     * 
     * @return the Query instance
     * 
     */
    public static Query createQuery() {
        try {
            return (Query) queryClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
}
