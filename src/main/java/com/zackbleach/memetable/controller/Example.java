package com.zackbleach.memetable.controller;

public class Example {
    private String foo;
    private int bar;

    public Example(String foo, int bar) {
        this.foo = foo;
        this.bar = bar;
    }

    public String getFoo() {
        return foo;
    }

    public int getBar() {
        return bar;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }

    public void setBar(int bar) {
        this.bar = bar;
    }
}

