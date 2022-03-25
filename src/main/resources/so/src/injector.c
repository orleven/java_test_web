#include <stdlib.h>
#include <stdio.h>
#include <injector.h>

JNIEXPORT void JNICALL Java_LibC2Loader_inject(JNIEnv * env, jobject object, jbyteArray jdata) {
   jbyte * data = (*env)->GetByteArrayElements(env, jdata, 0);
   jsize length = (*env)->GetArrayLength(env, jdata);
   inject((LPCVOID)data, (SIZE_T)length);
   (*env)->ReleaseByteArrayElements(env, jdata, data, 0);
}

/* inject some shellcode... enclosed stuff is the shellcode y0 */
void inject(LPCVOID buffer, int length) {
    STARTUPINFO si;
    PROCESS_INFORMATION pi;
    HANDLE hProcess   = NULL;
    SIZE_T wrote;
    LPVOID ptr;
    char lbuffer[1024];
    char cmdbuff[1024];

    printf("%s","cpp library\n");
}


