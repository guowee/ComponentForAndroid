package com.muse.component.common.base;

import android.support.annotation.Keep;

/**
 * Created by GuoWee on 2018/4/19.
 */
@Keep
public interface IApplicationDelegate {

    void onCreate();

    void onTerminate();

    void onLowMemory();

    void onTrimMemory(int level);

}
