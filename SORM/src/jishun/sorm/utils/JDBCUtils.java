package jishun.sorm.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCUtils {

    public static void setParams(PreparedStatement statement,Object... params) {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                try {
                    statement.setObject(i+1, params[i]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void closeAll(AutoCloseable... closeables) {
        for (AutoCloseable autoCloseable : closeables) {
            if (autoCloseable != null) {
                try {
                    autoCloseable.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
