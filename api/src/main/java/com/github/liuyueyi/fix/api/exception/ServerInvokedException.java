package com.github.liuyueyi.fix.api.exception;

/**
 * Created by @author yihui in 15:24 18/12/13.
 */
public class ServerInvokedException extends RuntimeException {
    public ServerInvokedException(String message, Throwable e) {
        super(message, e);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
