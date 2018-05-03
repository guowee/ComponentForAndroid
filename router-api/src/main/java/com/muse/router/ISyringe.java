package com.muse.router;

import android.os.Bundle;

import com.muse.router.exceptions.ParamException;


public interface ISyringe {
    /**
     * @param self the container itself, members to be inject into have been annotated
     *             with one annotation called Autowired
     */
    void inject(Object self);

    void preCondition(Bundle bundle) throws ParamException;
}
