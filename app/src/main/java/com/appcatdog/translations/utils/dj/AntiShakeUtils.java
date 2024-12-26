package com.appcatdog.translations.utils.dj;


import android.view.View;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.appcatdog.translations.R;


/**
 * 防重点击
 *
 */
public class AntiShakeUtils {

    private final static long INTERNAL_TIME = 500;

    private static long LAST_CURRENT_TIME_MILLIS = 0;

    /**
     * 外部调用 带View
     */

    public static boolean isInvalidClick(@NonNull View target) {
        return isInvalidClick(target, INTERNAL_TIME);
    }

    /**
     * 内部调用 带View
     */

    public static boolean isInvalidClick(@NonNull View target, @IntRange(from = 0) long internalTime) {
        long curTimeStamp = System.currentTimeMillis();
        long lastClickTimeStamp;
        Object o = target.getTag(R.id.last_click_time);
        if (o == null) {
            target.setTag(R.id.last_click_time, curTimeStamp);
            return false;
        }
        lastClickTimeStamp = (Long) o;
        long timeDif = curTimeStamp - lastClickTimeStamp;
        boolean isInvalid = (timeDif < internalTime) && (timeDif > 0);
        if (!isInvalid) {
            target.setTag(R.id.last_click_time, curTimeStamp);
        }
        return isInvalid;
    }

    /**
     * 外部调用 不带View
     */

    public static boolean isInvalidClick() {
        long curTimeStamp = System.currentTimeMillis();
        long poorDifference = curTimeStamp - LAST_CURRENT_TIME_MILLIS;
        LAST_CURRENT_TIME_MILLIS = curTimeStamp;
        return poorDifference < INTERNAL_TIME;
    }

}
