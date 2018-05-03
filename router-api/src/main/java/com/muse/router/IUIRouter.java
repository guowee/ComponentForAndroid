package com.muse.router;

/**
 * Created by GuoWee on 2018/4/27.
 */

public interface IUIRouter extends IComponentRouter {
    int PRIORITY_NORMAL = 0;
    int PRIORITY_LOW = -1000;
    int PRIORITY_HEIGHT = 1000;

    void registerUI(String host, int priority);

    void registerUI(String host);

    void registerUI(IComponentRouter router, int priority);

    void registerUI(IComponentRouter router);


    void unregisterUI(String host);

    void unregisterUI(IComponentRouter router);


}
