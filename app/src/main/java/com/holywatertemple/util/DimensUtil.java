package com.holywatertemple.util;

import android.content.Context;
import android.util.TypedValue;

public class DimensUtil {
    public static float dp2px(Context context,float dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }


    public static float px2dp(Context context,float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }
}
