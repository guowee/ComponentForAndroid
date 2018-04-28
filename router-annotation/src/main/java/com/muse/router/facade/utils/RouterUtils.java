package com.muse.router.facade.utils;

/**
 * Created by GuoWee on 2018/4/27.
 */

public class RouterUtils {

    private static final String ROUTERIMPL_OUTPUT_PKG = "com.muse.gen.router";
    private static final String DOT = ".";
    private static final String UIROUTER = "UIRouter";


    public static String genHostUIRouterClass(String host) {
        String claName = ROUTERIMPL_OUTPUT_PKG + DOT + firstCharUpperCase(host) + UIROUTER;
        return new String(claName);
    }


    private static String firstCharUpperCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }
}
