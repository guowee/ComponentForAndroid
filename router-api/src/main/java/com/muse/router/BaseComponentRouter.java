package com.muse.router;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by GuoWee on 2018/4/27.
 */

public abstract class BaseComponentRouter implements IComponentRouter {

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

        if (context == null || uri == null) {
            return true;
        }

        String scheme = uri.getScheme();
        String host = uri.getHost();







        return false;
    }


    @NonNull
    @Override
    public VerifyResult verifyUri(Uri uri, Bundle bundle, boolean checkParams) {
        return null;
    }
}
