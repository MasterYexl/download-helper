package com.yxl.downloadhelper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    @RequestMapping("/get")
    @ResponseBody
    public String test(@RequestParam("account") String account, @RequestParam("password")String password){
        System.out.println(account+" "+password);
        return "success";
    }
    @RequestMapping("/test")
    public String t(){
        return "ajTest";
    }
}
