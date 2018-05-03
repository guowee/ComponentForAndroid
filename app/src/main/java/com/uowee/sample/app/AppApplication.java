package com.uowee.sample.app;

import com.muse.component.common.base.BaseApplication;
import com.muse.router.UIRouter;

/**
 * Created by GuoWee on 2018/4/19.
 */

public class AppApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        UIRouter.getInstance().registerUI("app");
    }
}
