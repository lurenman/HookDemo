#include <jni.h>
#include <string>
#include <stdio.h>
#include <dlfcn.h>
#include <android/log.h>
#include <malloc.h>
#include <stdbool.h>
#include <stdlib.h>
#include "common/logger.h"
#include "core/RootCheck.hpp"
#include "xhook/xhook.h"
#include "core/InlineAsm.h"

#ifdef __aarch64__
#include "inlineHook64/And64InlineHook.hpp"
#else

#include "Substrate/SubstrateHook.h"

#endif

using namespace std;

extern "C" JNIEXPORT jstring



JNICALL
Java_com_example_hookdemo_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    string hello = "Hello from C++";
    LOGD("stringFromJNI: %s", hello.c_str());
    return env->NewStringUTF(hello.c_str());
}

void RegisterNativeTest(JNIEnv *env, jclass clazz, jstring str) {
    LOGD("RegisterNativeTest 执行");
}

jint add(JNIEnv *env, jclass clazz, jint ja, jint jb) {
    int result = ja + jb;
    LOGD("add: %d", result);
    return static_cast<jint>(result);
}

void (*Source_RegisterNativeTest)(JNIEnv *env, jclass clazz, jstring str) = nullptr;

void MyRegisterNativeTest(JNIEnv *env, jclass clazz, jstring str) {
    LOGD("MyRegisterNativeTest 执行");
    return Source_RegisterNativeTest(env, clazz, str);
}

static inline void
hook_function(void *addr, void *new_func, void **old_func) {
#ifdef __aarch64__
    A64HookFunction(addr, new_func, old_func);
    LOGD("InlineHook A64HookFunction 执行");
#else
    MSHookFunction(addr, new_func, old_func);
    LOGD("Substrate MSHookFunction 执行");
#endif

}

int (*source_openat)(int fd, const char *path, int oflag, int mode) = nullptr;

int MyOpenAt(int fd, const char *pathname, int flags, int mode) {
    //LOG(ERROR) << "MyOpenAt  pathname   "<<pathname;
    LOGD("MyOpenAt pathname: %s", pathname);
    if (strcmp(pathname, "/system/xbin/su") != 0 || strcmp(pathname, "/system/bin/su") != 0) {
        pathname = "/system/xbin/Mysu";
    }
    return source_openat(fd, pathname, flags, mode);
}

int my_system_log_print(int prio, const char *tag, const char *fmt, ...) {
    va_list ap;
    char buf[1024];
    int r;

    snprintf(buf, sizeof(buf), "[%s] %s", (NULL == tag ? "" : tag), (NULL == fmt ? "" : fmt));

    va_start(ap, fmt);
    r = __android_log_vprint(prio, "xhook_system", buf, ap);
    va_end(ap);
    return r;
}

void hookOpenAt() {
    void *libc_handle = dlopen("libc.so", RTLD_NOW);
    if (!libc_handle) {
        LOGD("libc_handle is null");
    }
    void *_openat = dlsym(libc_handle, "openat");
    void *_openat1 = dlsym(libc_handle, "openat64");
    void *_openat2 = dlsym(libc_handle, "__openat_2");
    if (_openat == nullptr) {
        LOGD("_openat nullptr");
        return;
    }
    hook_function((void *) _openat,
                  (void *) MyOpenAt,
                  (void **) &source_openat);
    if (_openat1 == nullptr) {
        LOGD("_openat1 nullptr");
        return;
    }
    hook_function((void *) _openat1,
                  (void *) MyOpenAt,
                  (void **) &source_openat);
    if (_openat2 == nullptr) {
        LOGD("_openat2 nullptr");
        return;
    }
    hook_function((void *) _openat2,
                  (void *) MyOpenAt,
                  (void **) &source_openat);
    LOGD("hook_function __openat");
}

void inlineHook(JNIEnv *env, jclass clazz) {
    LOGD("inlineHook 执行");
//    hook_function((void *) RegisterNativeTest,
//                  (void *) MyRegisterNativeTest,
//                  (void **) &Source_RegisterNativeTest);
    hookOpenAt();
}

void xHook(JNIEnv *env, jclass clazz) {
    LOGD("xHook 执行");
    int result_system = xhook_register("^/system/.*\\.so$", "__android_log_print",
                                       (void *) my_system_log_print, NULL);
    int result_vendor = xhook_register("^/vendor/.*\\.so$", "__android_log_print",
                                       (void *) my_system_log_print, NULL);
    int result_hookdemo = xhook_register(".*/libhookdemo\\.so$", "__android_log_print",
                                         (void *) my_system_log_print,
                                         NULL);
    LOGD("result_system :%d", result_system);
    LOGD("result_vendor :%d", result_vendor);
    LOGD("result_hookdemo :%d", result_hookdemo);

}

jboolean rootCheck(JNIEnv *env, jclass clazz) {
    bool root = RootCheck::getroot();
    LOGD("RootCheck root = %d", root);
    return root;
}

/**
 * 内联汇编
 * @param env
 * @param clazz
 */
void inlineAsm(JNIEnv *env, jclass clazz) {
    long result = InlineAsm::test_inline_asm_add(20);
    LOGD("inlineAsm result = %ld", result);
}

void sandhook(JNIEnv *env, jclass clazz) {

}

// Define JNI methods to be registered
static JNINativeMethod jniMethods[] = {
        {"add",                "(II)I",                 (void *) add},
        {"RegisterNativeTest", "(Ljava/lang/String;)V", (void *) RegisterNativeTest},
        {"InlineHook",         "()V",                   (void *) inlineHook},
        {"RootCheck",          "()Z",                   (void *) rootCheck},
        {"XHook",              "()V",                   (void *) xHook},
        {"InlineAsm",          "()V",                   (void *) inlineAsm},
        {"Sandhook",           "()V",                   (void *) sandhook},
};

// Define JNI library registration function
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *__unused) {
    JNIEnv *env = nullptr;
    jint result = -1;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return result;
    }

    jclass clazz = env->FindClass("com/example/hookdemo/common/JNIHelper");
    if (clazz == nullptr) {
        return result;
    }
    if (env->RegisterNatives(clazz, jniMethods, sizeof(jniMethods) / sizeof(jniMethods[0])) < 0) {
        return result;
    }

    result = JNI_VERSION_1_6;

    LOGD("JNI_OnLoad called!");

    return result;
}

/**
 * 编译生成后在.init段
 */
extern "C" void _init(void) {
    LOGD("ndk so init!");
}

/**
 * 编译生成后在.init_array段
 * 编译生成后在.init_array段 [名字可以更改]
 */
__attribute__((constructor)) void _pp_init(void) {
    LOGD("ndk so init_array _pp_init!");
}

__attribute__((constructor)) void _pp_init1(void) {
    LOGD("ndk so init_array _pp_init1!");
}