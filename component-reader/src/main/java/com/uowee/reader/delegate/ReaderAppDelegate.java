package com.uowee.reader.delegate;

import com.muse.component.common.base.IApplicationDelegate;
import com.muse.router.UIRouter;

/**
 * Created by GuoWee on 2018/4/24.
 */

public class ReaderAppDelegate implements IApplicationDelegate {
    UIRouter uiRouter = UIRouter.getInstance();
    @Override
    public void onCreate() {
        uiRouter.registerUI("reader");
        
    }


    @Override
    public void onTerminate() {
        uiRouter.unregisterUI("reader");
    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onTrimMemory(int level) {

    }
}
