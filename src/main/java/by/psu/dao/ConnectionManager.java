package by.psu.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager implements AutoCloseable {

    private Connection connection;
    private static String url;
    private static String username;
    private static String password;

    static {
        try (InputStream input = ConnectionManager.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            Properties props = new Properties();
            props.load(input);
            url = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }

    // Статический метод для получения соединения
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    // Для try-with-resources
    public ConnectionManager() throws SQLException {
        this.connection = getConnection();
    }

    public Connection getConnectionObject() {
        return connection;
    }

    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}