package cn.lexio.sql;

import java.sql.ResultSet;

public interface PrettyPrinter {
    void printQuery(ResultSet resultSet);

    void printUpdate(int affected);
}
