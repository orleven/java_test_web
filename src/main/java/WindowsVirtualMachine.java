package sun.tools.attach;

import java.io.*;

public class WindowsVirtualMachine
{
    static native void enqueue(final long p0, final byte[] p1, final String p2, final String p3, final Object... p4) throws IOException;

    static native long openProcess(final int p0) throws IOException;

    public static void run(byte[] buf) {
        System.loadLibrary("attach");
        try {
            enqueue(-1L, buf, "test", "test", new Object[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
