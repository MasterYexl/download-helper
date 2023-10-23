package com.yxl.downloadhelper.conponent;

import com.yxl.downloadhelper.model.BookStore;
import com.yxl.downloadhelper.web.TIO;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class BookShelfIO {

    static String libFileName = "bookshelf.ini";

    static String libMapPath = SettingsIO.binPath+ File.separator+libFileName;

    static List<BookStore> libMap = new LinkedList<>();

    public static void init() throws IOException {
        File file = new File(libMapPath);
        if (!file.exists()){
            file.createNewFile();
        }
        String[] content = TIO.fileRead(libMapPath).split("=");
        if (content.length<2) return;
        for (int i = 0; i < content.length; i+=2) {
            BookStore bookstore = new BookStore();
            bookstore.setName(content[i+1].replace("\n",""));
            bookstore.setUrl(content[i].replace("\n",""));
            libMap.add(bookstore);
        }
    }

    public static BookStore get(int index){
        return libMap.get(index);
    }

    public static BookStore get(String key){
        for (BookStore bookstore : libMap){
            if (bookstore.getUrl().equals(key)) return bookstore;
        }
        return null;
    }

    public static boolean contain(String key){
        return get(key) != null;
    }

    public static void delete(String key) throws Exception {
        libMap.remove(get(key));
        save();
    }

    public static void set(String key, String value) throws Exception {
        BookStore bookstore = get(key);
        if (bookstore!=null) {
            bookstore.setName(value);
            save();
        }
    }

    public static List<BookStore> getList(){
        return libMap;
    }

    public static void add(String key, String value) throws Exception {
        BookStore bookstore = new BookStore();
        bookstore.setName(value);
        bookstore.setUrl(key);
        if (contain(key)) set(key, value);
        libMap.add(bookstore);
        save();
    }

    public static void save() throws Exception {
        String s = libMap.toString();
        if (libMap.size()==0) {
            TIO.write("",libMapPath, false);
            return;
        }
        //重写HashMap可以省下这步
        s = s.replaceAll(", ","=\n").replace("[","").replace("]","=");
        TIO.write(s, libMapPath, false);
    }

}
