package com.yxl.downloadhelper.utils.io;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

public class TIO {
    public static String fileRead(BufferedInputStream in) throws IOException {
        StringBuilder ans = new StringBuilder();
        byte[] bytes = new byte[2048];
        int n;
        while ((n = in.read(bytes, 0, bytes.length)) != -1) {
            ans.append(new String(bytes, 0, n, StandardCharsets.UTF_8));
        }
        return ans.toString();
    }

    public static String fileRead(String name) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(name));
        return fileRead(in);
    }

    public static boolean write(String content, String name, boolean append) throws Exception {
        File file = new File(name);
        return writeTxtFile(content,file, append);
    }
    public static boolean write(String content, String name) throws Exception {
        return write(content, name, true);
    }

    public static boolean writeTxtFile(String content, File fileName) throws Exception {
        return writeTxtFile(content, fileName, true);
    }

    public static boolean writeTxtFile(String content, File fileName, boolean append) throws Exception {
        boolean flag = false;

        FileOutputStream fileOutputStream = new FileOutputStream(fileName, append);
        BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
        try {
            bos.write(content.getBytes(StandardCharsets.UTF_8));
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            bos.close();
        }
        return flag;
    }

    public static void logWrite(String content, String file) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            out.write(content + "\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static File createFile(String name){
        File infoLog = new File(name);
        if (!infoLog.exists()) {
            try {
                infoLog.createNewFile();
                return infoLog;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static File createFile(String path, String name){
        File infoLog = new File(path+"/"+name);
        File pat = new File(path);
        if (!pat.exists()){
            pat.mkdirs();
        }
        if (!infoLog.exists()) {
            try {
                infoLog.createNewFile();
                return infoLog;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return infoLog;
    }
    public static String getTime(){
        return new SimpleDateFormat("yyyy年MM月dd日HH时mm分").format(System.currentTimeMillis());
    }
    public static String getTimeNub(){
        return new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
    }
    public static void openBrowse(String url) throws Exception {
        Desktop desktop = Desktop.getDesktop();
        if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
            URI uri = new URI(url);
            desktop.browse(uri);
        }
    }

    public static void main(String[] args) {
        System.out.println("123".substring(0,5));
    }
}
