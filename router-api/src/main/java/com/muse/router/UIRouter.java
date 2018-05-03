package com.muse.router;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.muse.router.exceptions.UiRouterException;
import com.muse.router.facade.utils.RouterUtils;
import com.muse.router.utils.UriUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by GuoWee on 2018/4/27.
 */

public class UIRouter implements IUIRouter {

    private static volatile UIRouter instance;

    private static Map<String, IComponentRouter> routerInstanceCache = new HashMap<>();
    private List<IComponentRouter> uiRouters = new ArrayList<>();
    private HashMap<IComponentRouter, Integer> priorities = new HashMap<>();

    private UIRouter() {
    }

    public static UIRouter getInstance() {
        if (instance == null) {
            synchronized (UIRouter.class) {
                if (instance == null) {
                    instance = new UIRouter();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean openUri(Context context, String url, Bundle bundle) {
        return openUri(context, url, bundle, 0);
    }

    @Override
    public boolean openUri(Context context, Uri uri, Bundle bundle) {
        return openUri(context, uri, bundle, 0);
    }

    @Override
    public boolean openUri(Context context, String url, Bundle bundle, Integer requestCode) {
        url = url.trim();
        if (!TextUtils.isEmpty(url)) {
            if (!url.contains("://") &&
                    (!url.startsWith("tel:") ||
                            !url.startsWith("smsto:") ||
                            !url.startsWith("file:"))) {
                url = "http://" + url;
            }
            Uri uri = Uri.parse(url);
            return openUri(context, uri, bundle, requestCode);
        }
        return false;
    }

    @Override
    public boolean openUri(Context context, Uri uri, Bundle bundle, Integer requestCode) {
        for (IComponentRouter temp : uiRouters) {
            try {
                //ignore params check
                VerifyResult verifyResult = temp.verifyUri(uri, bundle, false);
                if (verifyResult.isSuccess() && temp.openUri(context, uri, bundle, requestCode))
                    return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @NonNull
    @Override
    public VerifyResult verifyUri(Uri uri, Bundle bundle, boolean checkParams) {
        StringBuilder verifyMsg = new StringBuilder();
        for (IComponentRouter temp : uiRouters) {
            VerifyResult result = temp.verifyUri(uri, bundle, checkParams);
            if (result.isSuccess())
                return result;
            else if (result.getThrowable() != null)
                verifyMsg.append(result.getThrowable().getMessage()).append("\r");

        }

        String msg = "cannot verify uri for: " + UriUtils.toSafeString(uri) +
                ";\r cannot navigate to the target\r"
                + verifyMsg.toString() + "check if uri error，params error or component has not been mounted";


        return new VerifyResult(false, new UiRouterException(msg) {
        });
    }

    @Override
    public void registerUI(String host, int priority) {
        IComponentRouter router = fetch(host);
        if (router != null) {
            registerUI(router, priority);
        }
    }

    @Override
    public void registerUI(String host) {
        IComponentRouter router = fetch(host);
        if (router != null) {
            registerUI(router, PRIORITY_NORMAL);
        }
    }

    @Override
    public void registerUI(IComponentRouter router, int priority) {
        if (priorities.containsKey(router) && priority == priorities.get(router)) {
            return;
        }

        removeOldUIRouter(router);
        int i = 0;
        for (IComponentRouter temp : uiRouters) {
            Integer tp = priorities.get(temp);
            if (tp == null || tp <= priority) {
                break;
            }
            i++;
        }
        uiRouters.add(i, router);
        priorities.put(router, priority);
    }


    @Override
    public void registerUI(IComponentRouter router) {
        registerUI(router, PRIORITY_NORMAL);
    }

    @Override
    public void unregisterUI(String host) {
        IComponentRouter router = fetch(host);
        if (router != null) {
            unregisterUI(router);
        }
    }

    @Override
    public void unregisterUI(IComponentRouter router) {
        for (int i = 0; i < uiRouters.size(); i++) {
            if (router == uiRouters.get(i)) {
                uiRouters.remove(i);
                priorities.remove(router);
                break;
            }
        }
    }


    private void removeOldUIRouter(IComponentRouter router) {
        Iterator<IComponentRouter> iterator = uiRouters.iterator();
        while (iterator.hasNext()) {
            IComponentRouter tmp = iterator.next();
            if (tmp == router) {
                iterator.remove();
                priorities.remove(tmp);
            }
        }
    }

    private IComponentRouter fetch(@NonNull String host) {
        if (TextUtils.isEmpty(host)) {
            return null;
        }

        String path = RouterUtils.genHostUIRouterClass(host);

        if (routerInstanceCache.containsKey(path))
            return routerInstanceCache.get(path);

        try {
            Class cla = Class.forName(path);
            IComponentRouter instance = (IComponentRouter) cla.newInstance();
            routerInstanceCache.put(path, instance);
            return instance;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
