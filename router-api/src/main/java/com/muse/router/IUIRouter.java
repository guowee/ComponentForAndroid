package com.muse.router;

/**
 * Created by GuoWee on 2018/4/27.
 */

public interface IUIRouter extends IComponentRouter {
    void registerUI(String host);

    void unregisterUI(String host);

    void registerUI(IComponentRouter router);

    void unregisterUI(IComponentRouter router);
}
