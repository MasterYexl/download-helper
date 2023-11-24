package com.yxl.downloadhelper.component;

import com.yxl.downloadhelper.utils.io.TIO;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SettingsIO {
    static String systemPath;
    static String bookPath;
    static String settingsFileName = "BookSearchSetting.ini";
    static String binPath;
    static String settingFilePath;
    static File settingFile;
    static Map<String, String> map = new HashMap<>();

    public static void  init(String path) throws Exception {
        systemPath = path;
        bookPath = systemPath+File.separator+"books";
        binPath = bookPath+File.separator+"bin";
        settingFilePath = systemPath+File.separator+settingsFileName;
        settingFile = new File(settingFilePath);
        if (!settingFile.exists()){
            System.out.println("正在创建配置文件");
            createSettingFile();
        }
        else {
            String[] split = TIO.fileRead(systemPath+File.separator+settingsFileName).split("=");
            for (int i = 0; i < split.length; i+=2) {
                map.put(split[i].replace("\n",""), split[i+1].replace("\n",""));
            }
        }
        bookPath = map.get("book-path");
        binPath = bookPath+File.separator+"bin";
        File file = new File(bookPath);
        File binFile = new File(binPath);
        if (!file.exists()){
            System.out.println("正在创建book目录");
            file.mkdirs();
        }
        if (!binFile.exists()){
            binFile.mkdirs();
        }
        BookShelfIO.init();
    }

    public static void createSettingFile() throws Exception {
        File file = new File(systemPath +File.separator+settingsFileName);
        file.createNewFile();
        map.put("book-path", bookPath);
        map.put("bin-path", binPath);
        map.put("downer", "3");
        map.put("bc-color", "#C7EDCC #CCE8CF #FAF9DE #FFF2E2 #FDE6E0 #E3EDCD #DCE2F1 #EAEAEF #E9EBFE black white gray wheat");
        map.put("fc-color", "black #002752 gray #0b2e13");
        map.put("font-size", "21");
        map.put("read-auto-save", "1");
        map.put("default-fc","#CCE8CF");
        map.put("default-bc","black");
        map.put("buffer-chapter","1");
        save();
    }

    public static void set(String key, String value){
        if (key.equals("book-path")) {
            binPath = value+File.separator+"bin";
            map.replace("bin-path", binPath);
        }
        map.replace(key, value);
    }
    public static void add(String key, String value){
        map.put(key, value);
    }
    public static String get(String key){
        return map.get(key);
    }

    public static void save() throws Exception {
        String s = map.toString();
        //重写HashMap可以省下这步
        s = s.replaceAll(", ","=\n").replace("{","").replace("}","=");
        TIO.write(s, systemPath+File.separator+settingsFileName, false);
    }

    @Override
    public String toString() {
        return "SettingsIO{" +
                "settingsFileName='" + settingsFileName + '\'' +
                ", settingFile=" + settingFile +
                ", map=" + map +
                '}';
    }
}

