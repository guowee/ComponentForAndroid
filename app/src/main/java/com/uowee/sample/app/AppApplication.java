package com.uowee.sample.app;

import com.alibaba.android.arouter.launcher.ARouter;
import com.muse.component.common.BuildConfig;
import com.muse.component.common.base.BaseApplication;

/**
 * Created by GuoWee on 2018/4/19.
 */

public class AppApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();
        }
        ARouter.init(this);
    }
}
