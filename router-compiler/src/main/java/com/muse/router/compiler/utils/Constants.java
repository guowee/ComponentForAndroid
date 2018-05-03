package com.muse.router.compiler.utils;

/**
 * Created by GuoWee on 2018/4/26.
 */

public interface Constants {

    String KEY_HOST_NAME = "host";
    String ANNO_FACADE_PKG = "com.muse.router.facade";

    String ANNOTATION_TYPE_ROUTE = ANNO_FACADE_PKG + ".annotation.Route";
    String ANNOTATION_TYPE_AUTOWIRED = ANNO_FACADE_PKG + ".annotation.Autowired";

    String PREFIX_OF_LOGGER = "[Router_ANNO_Compiler]---";

    // System interface
    String ACTIVITY = "android.app.Activity";
    String FRAGMENT = "android.app.Fragment";
    String FRAGMENT_V4 = "android.support.v4.app.Fragment";
    String SERVICE = "android.app.Service";
    String PARCELABLE = "android.os.Parcelable";
    String SERIALIZABLE = "java.io.Serializable";
    String BUNDLE = "android.os.Bundle";

    // Java type
    String LANG = "java.lang";
    String BYTE = LANG + ".Byte";
    String SHORT = LANG + ".Short";
    String INTEGER = LANG + ".Integer";
    String LONG = LANG + ".Long";
    String FLOAT = LANG + ".Float";
    String DOUBLE = LANG + ".Double";
    String BOOLEAN = LANG + ".Boolean";
    String STRING = LANG + ".String";

    String ISYRINGE = "com.muse.router.ISyringe";
    String JSON_SERVICE = "com.muse.router.service.JsonService";
    String BASECOMPROUTER = "com.muse.router.BaseComponentRouter";

}
