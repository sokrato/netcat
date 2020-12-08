package cn.lexio.sql.impl;

import cn.lexio.sql.Output;

import java.io.PrintStream;

public class SystemOutput implements Output {
    private final PrintStream out = System.out;

    @Override
    public void write(String str) {
        out.print(str);
    }

    @Override
    public void flush() {
        out.flush();
    }
}
