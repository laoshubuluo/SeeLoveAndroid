//
// Created by 简志展 on 16/3/8.
//

#ifndef HELLO_JNI_MONITOR_H
#define HELLO_JNI_MONITOR_H

#include <jni.h>
#include <android/log.h>

//#define __USE_LOG

#ifdef __USE_LOG
#define LOG_TAG "Monitor"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#else
#define LOGI(...)
#define LOGW(...)
#define LOGE(...)
#endif


enum {
    EMsgIdResetPriority = 0,
    EMsgCount
};

#ifdef __cplusplus
extern "C" {
#endif

void startMonitor(JNIEnv* env,
                  jobject thiz,
                  const char* userId,
                  const char* workDir,
                  unsigned int forkChildCount,
                  unsigned int forkGrandSonDepth) ;

void sendMsg(unsigned int msgId) ;

#ifdef __cplusplus
}
#endif

#endif // HELLO_JNI_MONITOR_H
