package com.yxl.downloadhelper.component;

import com.yxl.downloadhelper.model.dto.Chapter;
import com.yxl.downloadhelper.utils.io.TIO;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class BookShelfIO {

    static String libFileName = "bookshelf.ini";

    static String libMapPath = SettingsIO.binPath+ File.separator+libFileName;

    static List<Chapter> libMap = new LinkedList<>();

    public static void init() throws IOException {
        File file = new File(libMapPath);
        if (!file.exists()){
            file.createNewFile();
        }
        String[] content = TIO.fileRead(libMapPath).split("=");
        if (content.length<2) return;
        for (int i = 0; i < content.length; i+=2) {
            Chapter Chapter = new Chapter();
            Chapter.setTitle(content[i+1].replace("\n",""));
            Chapter.setUrl(content[i].replace("\n",""));
            libMap.add(Chapter);
        }
    }

    public static Chapter get(int index){
        return libMap.get(index);
    }

    public static Chapter get(String key){
        for (Chapter Chapter : libMap){
            if (Chapter.getUrl().equals(key)) return Chapter;
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
        Chapter Chapter = get(key);
        if (Chapter!=null) {
            Chapter.setTitle(value);
            save();
        }
    }

    public static List<Chapter> getList(){
        return libMap;
    }

    public static void add(String key, String value) throws Exception {
        Chapter Chapter = new Chapter();
        Chapter.setTitle(value);
        Chapter.setUrl(key);
        if (contain(key)) set(key, value);
        libMap.add(Chapter);
        save();
    }

    public static void save() throws Exception {
        String s = libMap.toString();
        if (libMap.size()==0) {
            TIO.write("",libMapPath, false);
            return;
        }
        s = s.replaceAll(", ","=\n").replace("[","").replace("]","=");
        TIO.write(s, libMapPath, false);
    }

}
