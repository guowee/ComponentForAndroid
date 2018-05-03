package com.uowee.share.delegate;

import com.muse.component.common.base.IApplicationDelegate;
import com.muse.router.UIRouter;

/**
 * Created by GuoWee on 2018/4/24.
 */

public class ShareAppDelegate implements IApplicationDelegate {
    UIRouter uiRouter = UIRouter.getInstance();

    @Override
    public void onCreate() {
        uiRouter.registerUI("share");
    }

    @Override
    public void onTerminate() {
        uiRouter.unregisterUI("share");
    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onTrimMemory(int level) {

    }
}
