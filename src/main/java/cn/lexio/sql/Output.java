package cn.lexio.sql;

public interface Output {
    void write(String str);

    default void writeLine(String line) {
        write(line);
        write("\n");
        flush();
    }

    default void writeAndFlush(String str) {
        write(str);
        flush();
    }

    void flush();
}
