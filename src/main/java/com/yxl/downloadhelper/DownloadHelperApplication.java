package com.yxl.downloadhelper;

import com.yxl.downloadhelper.conponent.SettingsIO;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.system.ApplicationHome;

@SpringBootApplication
public class DownloadHelperApplication {

    public static void main(String[] args) {
        ApplicationHome home = new ApplicationHome(SettingsIO.class);
        String basePath= home.getSource().getParentFile().toString();
        try {
            SettingsIO.init(basePath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("初始化配置文件时出错！");
            return;
        }
        SpringApplicationBuilder builder = new SpringApplicationBuilder(DownloadHelperApplication.class);
        builder.headless(false).run(args);
//        try {
//            System.out.println("请稍后..");
//            TIO.openBrowse("http://localhost:2333");
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("无法打开浏览器，请手动打开浏览器进入http://localhost:2333");
//        }
    }

}
