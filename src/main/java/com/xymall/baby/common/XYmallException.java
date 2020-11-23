package com.xymall.baby.common;

public class XYmallException extends RuntimeException {

    public XYmallException() {
    }

    public XYmallException(String message) {
        super(message);
    }

    /**
     * 丢出一个异常
     *
     * @param message
     */
    public static void fail(String message) {
        throw new XYmallException(message);
    }

}
