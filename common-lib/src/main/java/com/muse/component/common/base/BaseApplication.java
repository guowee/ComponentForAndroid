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
    public static final String ROOT_PACKAGE = "com.uowee.share";
    private static BaseApplication mAppContext;

    private List<IApplicationDelegate> mAppDelegateList;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;
        Utils.init(this);
        mAppDelegateList = ClassUtils.getObjectsWithInterface(this, IApplicationDelegate.class, ROOT_PACKAGE);
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onCreate();
        }
    }

    @Nullable
    public static Application getAppContext() {
        return mAppContext;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onTerminate();
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onLowMemory();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onTrimMemory(level);
        }
    }
}

