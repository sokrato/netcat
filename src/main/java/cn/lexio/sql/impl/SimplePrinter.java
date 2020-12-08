package cn.lexio.sql.impl;

import cn.lexio.sql.Output;
import cn.lexio.sql.PrettyPrinter;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SimplePrinter implements PrettyPrinter {
    private static final String SEPARATOR = "------------";
    private final Output output;

    public SimplePrinter(Output output) {
        this.output = output;
    }

    @Override
    public void printQuery(ResultSet resultSet) {
        try {
            printQuery0(resultSet);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void printQuery0(ResultSet resultSet) throws SQLException {
        int cnt = 0;
        ResultSetMetaData meta = resultSet.getMetaData();
        while (resultSet.next()) {
            output.writeLine(SEPARATOR);
            for (int i = 1; i <= meta.getColumnCount(); ++i) {
                String col = meta.getColumnName(i);
                Object val = resultSet.getObject(i);

                String line = String.format("%s: %s", col, val);
                output.writeLine(line);
            }
            ++cnt;
        }
        if (cnt > 0) {
            output.writeLine(SEPARATOR);
            output.writeLine("rows count: " + cnt);
        } else {
            output.writeLine("no matching rows");
        }
    }

    @Override
    public void printUpdate(int affected) {
        output.writeLine("affected rows: " + affected);
    }
}
