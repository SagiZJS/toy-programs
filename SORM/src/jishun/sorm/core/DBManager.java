package jishun.sorm.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import jishun.sorm.bean.Configuration;

/**
 * <p>
 * the {@link DBManager} class is responsible for setup connection to the
 * database using the jdb.properties file.
 * </p>
 * <p>
 * this class will hold a {@link Configuration} reference containing the
 * properties file values, it will also hold the {@link Connection} to the
 * database
 * <p>
 * 
 * @author Sagi
 *
 */
public class DBManager {

    private static final int DEFAULT_CONNECTION_NUMBER = 5;

    private static final Configuration CONFIGURATION;

    private static final BlockingQueue<Connection> connectionPool = new ArrayBlockingQueue<>(DEFAULT_CONNECTION_NUMBER);

    static {
        System.out.println("----------------------------------------------\nDBManager: initilizing...");
        Properties properties = null;
        try {
            properties = new Properties();
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("jdb.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        CONFIGURATION = new Configuration(properties.getProperty("URL"), properties.getProperty("user"),
                properties.getProperty("pwd"), properties.getProperty("DB"), properties.getProperty("src"),
                properties.getProperty("poPackage"), properties.getProperty("queryClass"));
        try {
            for (int i = 0; i < 5; i++) {
                connectionPool.add(DriverManager.getConnection(CONFIGURATION.getURL(), CONFIGURATION.getUser(),
                        CONFIGURATION.getPwd()));
                System.out.println(String.format("DBManager: %d connection(s) to %s is established",
                        DEFAULT_CONNECTION_NUMBER, CONFIGURATION.getURL().split("/")[2]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println("DBManager: initilized!\n----------------------------------------------");
        TableContext.staticInit();
    }

    public static Configuration getConfiguration() {
        return CONFIGURATION;
    }

    public static Connection getConnection() {
        try {
            return connectionPool.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return null;
    }

    public static void endConnection(Connection connection) {
        new Thread(() -> {
            try {
                connectionPool.put(connection);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static String getSrcPath() {
        return CONFIGURATION.getSrc();
    }

    public static String getPoPackage() {
        return CONFIGURATION.getPoPackage();
    }

    /**
     * close the connection
     */
    public static void shutDown() {
        for (Connection connection : connectionPool) {
            try {
                connection.close();
            } catch (SQLException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

}
