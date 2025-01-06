package com.ruiteapp.pettranslator.utils.dj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


/**
 * 
 * Acitivity跳转工具类
 * 
 * @author lizhiyong<lizhiyong@haodou.com>
 *　
 * $Id$
 *
 */
public class IntentUtil {
    

    private IntentUtil() {
    	
    }
    
	/**
	 * 
	 * 从某个Activity跳转到另外一个Activity的通用函数
	 * 
	 * @param context  当前所在activity的上下文
	 * @param cls　　　　需要跳转的activity的class
	 * @param finishSelf　　是否结束当前activity自身　true：结束　false：不结束
	 * @param bundle    传递到下一个activity的值，没有时传null
	 */
	public static void redirect(Context context, Class<?> cls,
                                boolean finishSelf, Bundle bundle) {
		redirect(context, null, cls, finishSelf, bundle);
	}

    /**
     *
     * 从某个Activity跳转到另外一个Activity的通用函数, forresult
     *
     * @param context  当前所在activity的上下文
     * @param cls　　　　需要跳转的activity的class
     * @param finishSelf　　是否结束当前activity自身　true：结束　false：不结束
     * @param bundle    传递到下一个activity的值，没有时传null
     * @param  requestCode   回传code
     */
    public static void redirectForResult(Context context, Class<?> cls,
                                         boolean finishSelf, Bundle bundle, int requestCode) {
        redirectForResult(context, null, cls, finishSelf, bundle, requestCode);
    }
    /**
     *
     * 从某个Fragment或Activity跳转到另外一个Activity的通用函数, forresult
     *
     * @param context  当前所在activity的上下文
     * @param fragment  当前所在fragment
     * @param cls　　　　需要跳转的activity的class
     * @param finishSelf　　是否结束当前activity自身　true：结束　false：不结束
     * @param bundle    传递到下一个activity的值，没有时传null
     * @param  requestCode   回传code
     */
    public static void redirectForResult(Context context, Fragment fragment, Class<?> cls,
										 boolean finishSelf, Bundle bundle, int requestCode) {
        Intent it = new Intent();
        it.setClass(context, cls);
        if (!(context instanceof Activity)) {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        if (bundle != null) {
            it.putExtras(bundle);
        }

        if (fragment != null) {
            fragment.startActivityForResult(it, requestCode);
        } else if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivityForResult(it, requestCode);
        }

        if (context instanceof Activity) {
            if (finishSelf) {
                ((Activity) context).finish();
            }

        }
    }
	
	/**
	 * 
	 * 从某个Activity或者Fragment跳转到另外一个Activity的通用函数
	 * 
	 * @param context  当前所在activity的上下文
	 * @param fragment  当前所在fragment
	 * @param cls　　　　需要跳转的activity的class
	 * @param finishSelf　　是否结束当前activity自身　true：结束　false：不结束
	 * @param bundle    传递到下一个activity的值，没有时传null
	 */
	public static void redirect(Context context, Fragment fragment, Class<?> cls,
                                boolean finishSelf, Bundle bundle) {
		Intent it = new Intent();
		it.setClass(context, cls);
        if (!(context instanceof Activity)) {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (bundle != null) {
			it.putExtras(bundle);
		}

        if ((context instanceof Activity) && finishSelf) {
            Activity activity = (Activity) context;
            activity.finish();
        }

		if (fragment != null) {
			fragment.startActivity(it);
		} else {
			context.startActivity(it);
		}
	}

}
