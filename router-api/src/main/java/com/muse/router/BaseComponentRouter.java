package com.muse.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.muse.router.exceptions.ParamException;
import com.muse.router.service.AutowiredService;
import com.muse.router.utils.UriUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GuoWee on 2018/4/27.
 */

public abstract class BaseComponentRouter implements IComponentRouter {
    protected Map<String, Class> routeMapper = new HashMap<>();
    protected Map<Class, Map<String, Integer>> paramsMapper = new HashMap<>();

    protected boolean hasInitMap = false;

    protected abstract String getHost();

    protected void initMap() {
        hasInitMap = true;
    }

    @Override
    public boolean openUri(Context context, Uri uri, Bundle bundle) {
        return openUri(context, uri, bundle, 0);
    }

    @Override
    public boolean openUri(Context context, String url, Bundle bundle) {
        if (TextUtils.isEmpty(url) || context == null) {
            return true;
        }
        return openUri(context, Uri.parse(url), bundle, 0);
    }


    @Override
    public boolean openUri(Context context, String url, Bundle bundle, Integer requestCode) {
        if (TextUtils.isEmpty(url) || context == null) {
            return true;
        }
        return openUri(context, Uri.parse(url), bundle, requestCode);
    }


    @Override
    public boolean openUri(Context context, Uri uri, Bundle bundle, Integer requestCode) {
        if (!hasInitMap) {
            initMap();
        }
        if (context == null || uri == null) {
            return true;
        }

        String scheme = uri.getScheme();
        String host = uri.getHost();
        if (!getHost().equals(host)) {
            return false;
        }
        List<String> pathSegments = uri.getPathSegments();
        String path = "/" + TextUtils.join("/", pathSegments);
        if (routeMapper.containsKey(path)) {
            Class target = routeMapper.get(path);
            if (bundle == null) {
                bundle = new Bundle();
            }
            Map<String, String> params = UriUtils.parseParams(uri);
            Map<String, Integer> paramsType = paramsMapper.get(target);
            UriUtils.setBundleValue(bundle, params, paramsType);
            Intent intent = new Intent(context, target);
            intent.putExtras(bundle);
            if (requestCode > 0 && context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, requestCode);
                return true;
            }
            context.startActivity(intent);
            return true;
        }
        return false;
    }


    @NonNull
    @Override
    public VerifyResult verifyUri(Uri uri, Bundle bundle, boolean checkParams) {
        if (uri == null) {
            return new VerifyResult(false);
        }
        String host = uri.getHost();
        if (!getHost().equals(host)) {
            return new VerifyResult(false);
        }

        if (!hasInitMap) {
            initMap();
        }
        List<String> pathSegments = uri.getPathSegments();
        String path = "/" + TextUtils.join("/", pathSegments);

        if (!routeMapper.containsKey(path)) {
            return new VerifyResult(false);
        }
        try {
            if (checkParams) {
                Class target = routeMapper.get(path);
                if (bundle == null) {
                    bundle = new Bundle();
                }
                Map<String, String> params = UriUtils.parseParams(uri);
                Map<String, Integer> paramsType = paramsMapper.get(target);
                UriUtils.setBundleValue(bundle, params, paramsType);
                AutowiredService.Factory.getSingletonImpl().preCondition(target, bundle);
                return new VerifyResult(true);
            }
        } catch (ParamException e) {
            e.printStackTrace();
            return new VerifyResult(false, e);
        }

        return null;
    }
}
