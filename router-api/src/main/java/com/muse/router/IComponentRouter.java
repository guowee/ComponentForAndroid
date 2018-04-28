package com.muse.router;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created by GuoWee on 2018/4/27.
 */

public interface IComponentRouter {

    boolean openUri(Context context, String url, Bundle bundle);

    boolean openUri(Context context, Uri uri, Bundle bundle);

    boolean openUri(Context context, String url, Bundle bundle, Integer requestCode);

    boolean openUri(Context context, Uri uri, Bundle bundle, Integer requestCode);

    @NonNull
    VerifyResult verifyUri(Uri uri, Bundle bundle, boolean checkParams);


}
