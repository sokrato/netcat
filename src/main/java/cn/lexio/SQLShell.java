package app;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SQLShell {
    DataSource dataSource(String[] args) {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setURL("jdbc:mysql://risk.cbahigmnfpc1.af-south-1.rds.amazonaws.com:3306/fcctubizdb?useUnicode=true&characterEncoding=utf-8");
        ds.setUser("alipaypluscore");
        ds.setPassword("zCmdH6m7V9O");
        return ds;
    }

    public static void main(String[] args) throws Exception {
        SQLShell sh = new SQLShell();
        sh.run(args);
    }

    public void run(String[] args) throws Exception {
        DataSource ds = dataSource(args);
        try (Connection conn = ds.getConnection()) {
            while (true) {
                String line = readLine();
                if (EXIT.equals(line)) {
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
        }
        return cnt;
    }
}
