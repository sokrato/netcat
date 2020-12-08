package cn.lexio.sql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private final Driver driver;
    private final String url;
    private final String user;
    private final String pass;

    public ConnectionFactory(String jdbcURL, String user, String pass) {
        try {
            driver = DriverManager.getDriver(jdbcURL);
        } catch (SQLException ex) {
            throw new RuntimeException("failed to getDriver for url: " + jdbcURL, ex);
        }
        this.url = jdbcURL;
        this.user = user;
        this.pass = pass;
    }

    public Connection connect() {
        Properties props = new Properties();
        if (user != null && pass != null) {
            props.put("user", user);
            props.put("password", pass);
        }
        try {
            return driver.connect(url, props);
        } catch (SQLException ex) {
            throw new RuntimeException("fail to connect: " + url, ex);
        }
    }

    public Connection ensureConnection(Connection conn) {
        try {
            if (!conn.isValid(3)) {
                close(conn);
                return connect();
            }
            return conn;
        } catch (SQLException ex) {
            throw new RuntimeException("connection is lost", ex);
        }
    }

    public void close(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed())
                    conn.close();
            } catch (SQLException ex) {
                System.err.println("failed to close: " + conn);
            }
        }
    }
}
