#include <stdio.h>
#include <jni.h>
#include "JNIClass.h"
#include <time.h>

/*
* This function computes local time, updates
* a jObject parameter and returns it to the caller.
* @author wmabebe
*/

JNIEXPORT jobject JNICALL Java_JNIClass_C_1GetLocalTime
  (JNIEnv *env, jobject object,jobject obj) {
  
  time_t seconds;
  jint tme = (unsigned) time(&seconds);
  jchar valid = (tme > 0) ? 1 : 0;

  jclass cls = (*env)->GetObjectClass(env,obj);
  jmethodID mid = (*env)->GetMethodID(env,cls,"setTime","(I)V");
  (*env)->CallVoidMethod(env,obj,mid,tme);
  jmethodID mid2 = (*env)->GetMethodID(env,cls,"setValid","(C)V");
  (*env)->CallVoidMethod(env,obj,mid2,valid);
  
  return obj;
}
