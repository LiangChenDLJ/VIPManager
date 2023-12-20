package com.chen.zhou.liang.lancet.utils;

public class DisplayableException extends Exception {
    public DisplayableException(String message) {
        super(message);
    }
    public DisplayableException(String message, Throwable e) {
        super(message, e);
    }
}
