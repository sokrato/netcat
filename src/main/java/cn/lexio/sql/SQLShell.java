package cn.lexio.sql;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SQLShell {

    public static void main(String[] args) throws Exception {
        if (args.length % 2 != 0) {
            System.err.println("need even number of parameters");
            return;
        }
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            params.put(args[i].substring(2), args[i + 1]);
        }
        SQLShell sh = new SQLShell();
        sh.run(
                Objects.requireNonNull(param(params, "url", "JDBC_URL"), "jdbc url not defined"),
                param(params, "user", "JDBC_USER"),
                param(params, "pass", "JDBC_PASS")
        );
    }

    private static String param(Map<String, String> params, String name, String envKey) {
        return params.getOrDefault(name,
                System.getProperty(envKey, System.getenv(envKey)));
    }

    public void run(String url, String user, String pass) throws Exception {
        DataSource ds = DataSourceFactory.defaultFactory().build(url, user, pass);
        try (Connection conn = ds.getConnection()) {
            while (true) {
                String line = readLine();
                if (isExit(line)) {
                    break;
                }

                if (isQuery(line)) {
                    query(conn, line);
                } else {
                    update(conn, line);
                }
            }
        }
    }

    private static final String EXIT = "exit";

    private static boolean isExit(String line) {
        return EXIT.equals(line) || "quit".equals(line);
    }

    private String readLine() {
        while (true) {
            System.out.print("> ");
            System.out.flush();

            String line = System.console().readLine();
            if (line == null) {
                return EXIT;
            }
            line = line.trim();
            if (!line.isEmpty()) {
                return line;
            }
        }
    }

    private boolean isQuery(String line) {
        String[] parts = line.split("\\s+");
        String verb = parts[0].toUpperCase();
        return "SELECT".equals(verb) || "SHOW".equals(verb);
    }

    void update(Connection conn, String line) throws SQLException {
        int cnt = conn.prepareStatement(line).executeUpdate();
        System.out.println("affected rows: " + cnt);
    }

    void query(Connection conn, String line) throws SQLException {
        ResultSet resultSet = conn.prepareStatement(line).executeQuery();
        int cnt = printResult(resultSet);
        resultSet.close();
        System.out.println(cnt + " rows selected.");
    }

    int printResult(ResultSet resultSet) throws SQLException {
        int cnt = 0;
        ResultSetMetaData meta = resultSet.getMetaData();
        while (resultSet.next()) {
            for (int i = 1; i <= meta.getColumnCount(); ++i) {
                String col = meta.getColumnName(i);
                Object val = resultSet.getObject(i);
                System.out.printf("%s: %s\n", col, val);
            }
            System.out.println("-----");
            ++cnt;
        }
        return cnt;
    }
}
