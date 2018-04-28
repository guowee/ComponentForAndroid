package com.muse.router.exceptions;

/**
 * Created by GuoWee on 2018/4/27.
 */
public abstract class UiRouterException extends Exception {
    public UiRouterException() {
    }

    public UiRouterException(String message) {
        super(message);
    }

    public UiRouterException(String message, Throwable cause) {
        super(message, cause);
    }

    public UiRouterException(Throwable cause) {
        super(cause);
    }
}
