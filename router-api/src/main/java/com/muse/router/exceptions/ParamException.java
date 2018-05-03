package com.muse.router.exceptions;

/**
 * Created by GuoWee on 2018/5/2.
 */

public class ParamException extends UiRouterException {

    public ParamException(String paramName) {
        super(paramName + "is required param, but didn't contain in the bundle.");
    }
}
