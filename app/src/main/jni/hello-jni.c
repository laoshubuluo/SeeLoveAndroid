/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <string.h>
#include <jni.h>
#include <stdio.h>
#include "monitor.h"

/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
 */


#if defined(__arm__)
#if defined(__ARM_ARCH_7A__)
#if defined(__ARM_NEON__)
  #if defined(__ARM_PCS_VFP)
    #define ABI "armeabi-v7a/NEON (hard-float)"
  #else
    #define ABI "armeabi-v7a/NEON"
  #endif
#else
  #if defined(__ARM_PCS_VFP)
    #define ABI "armeabi-v7a (hard-float)"
  #else
    #define ABI "armeabi-v7a"
  #endif
#endif
#else
#define ABI "armeabi"
#endif
#elif defined(__i386__)
#define ABI "x86"
#elif defined(__x86_64__)
#define ABI "x86_64"
#elif defined(__mips64)  /* mips64el-* toolchain defines __mips__ too */
#define ABI "mips64"
#elif defined(__mips__)
#define ABI "mips"
#elif defined(__aarch64__)
#define ABI "arm64-v8a"
#else
#define ABI "unknown"
#endif

jstring
Java_com_tianyu_seelove_service_MessageSendService_startMonitor(JNIEnv* env,
                                                jobject thiz,
                                                jstring jUserId,
                                                jstring jWorkDir,
                                                jint forkChildCount,
                                                jint forkGrandSonDepth)  {
    char tmp[256] = {0};

    const char* userId = NULL;
    const char* workDir = NULL;

    if (jUserId)
        userId = (*env)->GetStringUTFChars(env, jUserId, NULL);
    else
        userId = "" ;

    if (jWorkDir)
        workDir = (*env)->GetStringUTFChars(env, jWorkDir, NULL);

    sprintf(tmp, "start JNI monitor ABI:－－－－－－－－－funck %s, userId: %s, forkChildCount: %d, forkGrandSonDepth: %d, workDir: %s",
            ABI, userId, forkChildCount, forkGrandSonDepth, workDir ? workDir : "null") ;

    LOGE("%s", tmp) ;

    startMonitor(env, thiz, userId, workDir, forkChildCount, forkGrandSonDepth) ;

    if (jUserId)
        (*env)->ReleaseStringUTFChars(env, jUserId, userId);

    if (jWorkDir)
        (*env)->ReleaseStringUTFChars(env, jWorkDir, workDir);

    return (*env)->NewStringUTF(env, tmp);
}

void
Java_com_tianyu_seelove_service_MessageSendService_sendMsg(JNIEnv* env, jobject thiz, jint msgId)  {

    sendMsg((int)msgId) ;
}

