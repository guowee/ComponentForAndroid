package com.muse.basic;

import android.support.annotation.Keep;

/**
 * Created by GuoWee on 2018/4/19.
 */
@Keep
public interface BaseView<T> {
    void setPresenter(T presenter);
}
