package cn.lexio.sql;

import cn.lexio.sql.impl.ConsoleInput;
import cn.lexio.sql.impl.SimplePrinter;
import cn.lexio.sql.impl.SystemOutput;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Shell {
    private final ConnectionFactory connFactory;
    private final Input input;
    private final Output output;
    private final PrettyPrinter prettyPrinter;

    public Shell(ConnectionFactory connFactory, Input input, Output output, PrettyPrinter prettyPrinter) {
        this.connFactory = connFactory;
        this.input = input;
        this.output = output;
        this.prettyPrinter = prettyPrinter;
    }

    public static void main(String[] args) throws Exception {
        if (args.length % 2 != 0) {
            System.err.println("need even number of parameters");
            return;
        }
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            params.put(args[i].substring(2), args[i + 1]);
        }
        String url = Objects.requireNonNull(param(params, "url", "JDBC_URL"), "jdbc url not defined");
        String user = param(params, "user", "JDBC_USER");
        String pass = param(params, "pass", "JDBC_PASS");
        ConnectionFactory factory = new ConnectionFactory(url, user, pass);
        Output output = new SystemOutput();
        Shell sh = new Shell(factory, new ConsoleInput(), output, new SimplePrinter(output));
        sh.run();
    }

    private static String param(Map<String, String> params, String name, String envKey) {
        return params.getOrDefault(name,
                System.getProperty(envKey, System.getenv(envKey)));
    }

    public void run() throws Exception {
        Connection conn = connFactory.connect();
        while (true) {
            String line = readLine();
            if (isExit(line)) {
                break;
            }

            conn = connFactory.ensureConnection(conn);

            if (isQuery(line)) {
                query(conn, line);
            } else {
                update(conn, line);
            }
        }
        connFactory.close(conn);
    }

    private static final String EXIT = "exit";
    private static final String PROMPT = "> ";

    private static boolean isExit(String line) {
        return EXIT.equals(line) || "quit".equals(line);
    }

    private String readLine() {
        while (true) {
            output.writeAndFlush(PROMPT);
            String line = input.readLine();
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
        return "SELECT".equals(verb) || "SHOW".equals(verb) || "DESCRIBE".equals(verb);
    }

    void update(Connection conn, String line) throws SQLException {
        int cnt = conn.prepareStatement(line).executeUpdate();
        prettyPrinter.printUpdate(cnt);
    }

    void query(Connection conn, String line) throws SQLException {
        try (ResultSet resultSet = conn.prepareStatement(line).executeQuery()) {
            prettyPrinter.printQuery(resultSet);
        }
    }
}
