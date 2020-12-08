package cn.lexio.sql.impl;

import cn.lexio.sql.Input;

public class ConsoleInput implements Input {
    @Override
    public String readLine() {
        return System.console().readLine();
    }
}
