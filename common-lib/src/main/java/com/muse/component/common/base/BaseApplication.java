package com.muse.component.common.base;

import android.app.Application;
import android.support.annotation.Nullable;
import android.util.Log;

import com.muse.component.common.utils.ClassUtils;
import com.muse.component.common.utils.Utils;

import java.util.List;

/**
 * Created by GuoWee on 2018/4/19.
 */

public class BaseApplication extends Application {
    private static BaseApplication mAppContext;


    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;

    }

    @Nullable
    public static Application getAppContext() {
        return mAppContext;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();

    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

    }
}

