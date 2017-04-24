package com.sunsun.myapplication;

import android.content.Context;

/**
 * Created by lidexian on 2017/4/24.
 */

public class UIUtils {

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
