package com.git.hui.fix.api.exception;

/**
 * Created by @author yihui in 15:45 18/12/13.
 */
public class IllegalInvokeArgumentException extends RuntimeException {

    public IllegalInvokeArgumentException(String message) {
        super(message);
    }

    public IllegalInvokeArgumentException(String message, Throwable e) {
        super(message, e);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
