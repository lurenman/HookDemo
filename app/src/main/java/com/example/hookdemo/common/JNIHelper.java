package com.example.hookdemo.common;

public class JNIHelper {
    public native static void RegisterNativeTest(String str);

    public native static int add(int a, int b);

    public native static void InlineHook();

    public native static boolean RootCheck();

    public native static void XHook();

    public native static void InlineAsm();
    public native static void Sandhook();
}
