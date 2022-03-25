package com.orleven.test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Test {
    private static String FileName = "/Users/orleven/Product/";// 文件夹名字

    private static String FileEnd = ".py";// 文件名称后缀 txt sql bat

    private static String SearchStr = "password";//要查找的字符串

    private static Boolean IngronCase = true;// 是否区分大小写

    private static List<String> pathlist = new ArrayList<>();



    public Test()  {

    }

    public static void main(String[] args) throws IOException {
        Test t = new Test();
        String x = t.find("/Users/orleven/Product/", ".py", "password");
        System.out.println(x);
    }

    public String find(String filename, String filenameKey, String keyword) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if(filenameKey.length()<3 || keyword.length() < 3){
            stringBuilder.append("filenameKey or keyword is too shortly!");
        }else{
            stringBuilder = getSearchResult(stringBuilder, filename, filenameKey, keyword);
        }
        return stringBuilder.toString();
    }

    public StringBuilder getSearchResult(StringBuilder stringBuilder, String strPath, String filenameKey, String keyword) {
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) {
                    stringBuilder = getSearchResult(stringBuilder, files[i].getAbsolutePath(), filenameKey, keyword);
                } else if (fileName.contains(filenameKey)) {
                    // 匹配文件内容
                    File file = files[i];
                    if (file.exists()) {
                        String s = file.getAbsolutePath();
                        try {
                            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(s)), "UTF-8"));
                            String lineTxt = null;
                            while ((lineTxt = br.readLine()) != null) {
                                // 忽略大小写
                                if (lineTxt.toLowerCase().endsWith(keyword.toLowerCase())) {
                                    stringBuilder.append("find: " + s + ": " + lineTxt);
                                    break;
                                }
                            }
                            br.close();
                        } catch (Exception e) {
                        }
                    }
                } else {
                    continue;
                }
            }
        }
        return stringBuilder;
    }

}

