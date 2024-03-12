#include <jni.h>
#include <string>
#include "common/logger.h"

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

jint add(JNIEnv *env, jclass clazz, jint ja, jint jb) {
    int result = ja + jb;
    return static_cast<jint>(result);
}

// Define JNI methods to be registered
static JNINativeMethod jniMethods[] = {
        {"add", "(II)I", (void *) add}
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