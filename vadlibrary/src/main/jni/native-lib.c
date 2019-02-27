#include <jni.h>
#include "vad_src/webrtc_vad.h"

JNIEXPORT jboolean JNICALL
Java_tech_oom_vadlibrary_Vad_vad(JNIEnv *env, jobject instance, jint sampleSize, jint mode,
                                 jshortArray audioData_, jint offsetInShort, jint readSize) {

    VadInst *handle = WebRtcVad_Create();
    WebRtcVad_Init(handle);
    WebRtcVad_set_mode(handle, mode);
    jint size_10_second = sampleSize / 100;
    int index = readSize / size_10_second;
    jshort *pcm_data = (*env)->GetShortArrayElements(env, audioData_, JNI_FALSE);
    jsize i = (*env)->GetArrayLength(env, audioData_);
    jboolean b;
    for (int i = 0; i < index; ++i) {
        jshort *frame = pcm_data + offsetInShort + i * size_10_second;
        int vad = WebRtcVad_Process(handle, sampleSize,
                                    frame,
                                    size_10_second);

        if (vad == 1) {
            b = JNI_TRUE;
        } else {
            b = JNI_FALSE;
        }
    }
    (*env)->ReleaseShortArrayElements(env, audioData_, pcm_data, JNI_ABORT);
    WebRtcVad_Free(handle);
    return b;


}