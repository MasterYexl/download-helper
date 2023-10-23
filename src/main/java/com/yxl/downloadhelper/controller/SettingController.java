package com.yxl.downloadhelper.controller;

import com.yxl.downloadhelper.conponent.BookShelfIO;
import com.yxl.downloadhelper.conponent.SettingsIO;
import com.yxl.downloadhelper.model.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.annotation.WebServlet;
import java.io.File;

@Controller
@RequestMapping("/setting")
public class SettingController {

    @GetMapping("/")
    public String getSettingPage(Model model){
        model.addAttribute("ras", SettingsIO.get("read-auto-save"));
        model.addAttribute("downer", SettingsIO.get("downer"));
        model.addAttribute("folder", SettingsIO.get("book-path"));
        model.addAttribute("bufferChapter", SettingsIO.get("buffer-chapter"));
        return "setting";
    }

    @PostMapping("/save")
    @ResponseBody
    public String save(@RequestParam("key") String key, @RequestParam("value") String value){
        SettingsIO.set(key, value);
        try {
            SettingsIO.save();
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }
    @PostMapping("/save-all")
    @ResponseBody
    public String saveAll(@RequestParam("keys") String[] keys, @RequestParam("values") String[] values){
        int i=0;
        for (String key : keys){
            SettingsIO.set(key, values[i++]);
        }
        try {
            SettingsIO.save();
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }
    @RequestMapping("clear-buffer")
    @ResponseBody
    public String clearBuffer(){
        File file = new File(SettingsIO.get("bin-path"));
        File[] files = file.listFiles();
        if (files==null) return "null";
        for (File ob: files){
            if (ob.getName().contains(".ser")){
                String name = ob.getName().split(".ser")[0];
                if (!BookShelfIO.contain(name)) {
                    if (!ob.delete()) return "FAIL";
                }
            }
        }
        return "OK";
    }
}
