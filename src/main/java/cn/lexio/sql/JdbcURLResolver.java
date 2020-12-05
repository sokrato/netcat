package cn.lexio.sql;

import javax.sql.DataSource;

@FunctionalInterface
public interface JdbcURLResolver {
    default DataSource resolve(String jdbcURL) {
        return resolve(jdbcURL, null, null);
    }

    DataSource resolve(String jdbcURL, String user, String pass);
}
