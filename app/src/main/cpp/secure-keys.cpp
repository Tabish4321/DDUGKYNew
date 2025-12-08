#include <jni.h>
#include <string>

extern "C" {

JNIEXPORT jstring JNICALL
Java_com_deendayalproject_security_SecureConfig_getEncryptIvKeyNative(JNIEnv* env, jobject) {
    return env->NewStringUTF("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
}

}
