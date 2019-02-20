#include <jni.h>
#include <string>
#include <android/log.h>

#include "client/linux/handler/exception_handler.h"
#include "client/linux/handler/minidump_descriptor.h"

#ifdef NDEBUG
#define DEBUG(...)
#else
#define DEBUG(...) __android_log_print(ANDROID_LOG_DEBUG, __VA_ARGS__)
#endif

static google_breakpad::ExceptionHandler* exceptionHandler;
bool DumpCallback(const google_breakpad::MinidumpDescriptor& descriptor,
                  void* context,
                  bool succeeded) {
    DEBUG("breakpad-android", "Dump path: %s\n", descriptor.path());
    return succeeded;
}

void Crash() {
    volatile int* a = reinterpret_cast<volatile int*>(NULL);
    *a = 1;
}

extern "C" JNIEXPORT void JNICALL
Java_com_daya_android_breakpad_MainActivity_initialize(
        JNIEnv* env,
        jobject obj,
        jstring filepath) {
    DEBUG("breakpad-android", "initialize call");
    const char *path = env->GetStringUTFChars(filepath, 0);
    DEBUG("breakpad-android", "Dump path: %s", path);

    google_breakpad::MinidumpDescriptor descriptor(path);
    exceptionHandler = new google_breakpad::ExceptionHandler(descriptor, nullptr, DumpCallback, nullptr, true, -1);
}

extern "C" JNIEXPORT void JNICALL
Java_com_daya_android_breakpad_MainActivity_crash(
        JNIEnv* env,
        jobject /* this */) {
    DEBUG("breakpad-android", "crash call");
    Crash();
}
