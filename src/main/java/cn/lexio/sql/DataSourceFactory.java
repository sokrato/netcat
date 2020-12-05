package cn.lexio.sql;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class DataSourceFactory {
    private final Map<String, JdbcURLResolver> resolverMap;

    public DataSourceFactory(Map<String, JdbcURLResolver> resolverMap) {
        this.resolverMap = resolverMap;
    }

    public DataSource build(String jdbcURL) {
        return build(jdbcURL, null, null);
    }

    public DataSource build(String jdbcURL, String user, String pass) {
        String[] parts = jdbcURL.split(":", 3);
        if (parts.length < 3) {
            throw new IllegalArgumentException("invalid jdbcURL: " + jdbcURL);
        }

        String type = parts[1];
        JdbcURLResolver resolver = resolverMap.get(type);
        if (resolver == null) {
            throw new IllegalArgumentException("unsupported db type: " + type);
        }

        return resolver.resolve(jdbcURL, user, pass);
    }

    public static DataSourceFactory defaultFactory() {
        Map<String, JdbcURLResolver> map = new HashMap<>();
        map.put("h2", (url, user, pass) -> {
            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL(url);
            if (user != null) {
                ds.setUser(user);
            }
            if (pass != null) {
                ds.setPassword(pass);
            }
            return ds;
        });

        map.put("mysql", (url, user, pass) -> {
            MysqlDataSource ds = new MysqlDataSource();
            ds.setURL(url);
            if (user != null) {
                ds.setUser(user);
            }
            if (pass != null) {
                ds.setPassword(pass);
            }
            return ds;
        });
        return new DataSourceFactory(map);
    }
}
