package com.example.hookdemo.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.swift.sandhook.xposedcompat.XposedCompat;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

/**
 * 创建日期：2024-04-15
 * 作者:baiyang
 */
public class SandhookUtils {
    private static final String TAG = "SandhookUtils";

    /**
     * xposed的方式hook
     * @param context
     */
    public static void hook(Context context) {
        //setup for xposed
        //for xposed compat only(no need xposed comapt new)
//        XposedCompat.cacheDir = context.getCacheDir();
//
//        //for load xp module(sandvxp)
//        XposedCompat.context = context;
//        XposedCompat.classLoader = context.getClassLoader();
//        XposedCompat.isFirstApplication = true;

        //do hook
        XposedHelpers.findAndHookMethod(Activity.class, "onResume", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.d(TAG, "beforeHookedMethod: " + param.method.getName());
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(TAG, "afterHookedMethod: " + param.method.getName());
            }
        });

    }
}
