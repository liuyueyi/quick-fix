package com.git.hui.fix.api.exception;

/**
 * Created by @author yihui in 15:24 18/12/13.
 */
public class ServerNotFoundException extends RuntimeException {
    public ServerNotFoundException(String message) {
        super(message);
    }

    public ServerNotFoundException(String message, Throwable e) {
        super(message, e);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
