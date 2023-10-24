package com.yxl.downloadhelper.controller;

import com.yxl.downloadhelper.conponent.SettingsIO;
import com.yxl.downloadhelper.model.book.Book;
import com.yxl.downloadhelper.model.book.Chapter;
import com.yxl.downloadhelper.tool.Tool;
import com.yxl.downloadhelper.utils.downloader.BookDownloader;
import com.yxl.downloadhelper.utils.workbook.Workbook;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/download")
public class DownloadController {


    @GetMapping("/{id}")
    public String getDown(@PathVariable("id") int id, @RequestParam("downer") int downer, HttpSession session, Model model){
        List<Workbook<Chapter, String>> workbooks = (List<Workbook<Chapter, String>>) session.getAttribute("workBooks");
        if (workbooks ==null) workbooks = new LinkedList<>();
        if (workbooks.size()>id) {
            if (workbooks.get(id).isWorking()) return "download";
        }
        Book book = Tool.getBookFromID(id, session);
        if (book==null) return "download";
        try {
            Workbook<Chapter, String> workbook = down(book, downer);
            if (workbooks.size()<id){
                for (int i = workbooks.size(); i<id; i++){
                    workbooks.add(new Workbook<>(null,null));
                }
            }
            workbooks.add(workbook);
            model.addAttribute("id", id);
            session.setAttribute("workBooks", workbooks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "download";
    }

    @GetMapping("/progress")
    @ResponseBody
    public String getProgress(@RequestParam int id, HttpSession session){
        List<Workbook<Chapter, String>> list = (List<Workbook<Chapter, String>>) session.getAttribute("workBooks");
        Workbook<Chapter, String> workbook = list.get(id);
        return "{\"progress\":" + workbook.getProgress() + ",\"tot\":" + workbook.getTot() + ",\"finish\":" + workbook.isFinish() + "}";
    }

    @GetMapping("/open")
    @ResponseBody
    public String open(@RequestParam int id, HttpSession session){
        List<Workbook<Chapter, String>> list = (List<Workbook<Chapter, String>>) session.getAttribute("workBooks");
        Workbook<Chapter, String> workbook = list.get(id);
        try {
            System.out.println("地址："+SettingsIO.get("book-path")+File.separator+ workbook.getName()+".txt");
            Runtime.getRuntime().exec("explorer /e,/select, "+ SettingsIO.get("book-path")+File.separator+ workbook.getName()+".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    @ResponseBody
    @GetMapping("/copy")
    public String copy(@RequestParam int id, HttpSession session, HttpServletResponse response){
        List<Workbook<Chapter, String>> list = (List<Workbook<Chapter, String>>) session.getAttribute("workBooks");
        Workbook<Chapter, String> workbook = list.get(id);
        File file = new File(SettingsIO.get("book-path")+File.separator+ workbook.getName()+".txt");
        byte[] buffer = new byte[1024];
        FileInputStream is = null;
        BufferedInputStream bis = null;
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(workbook.getName()+".txt","UTF-8"));
            is = new FileInputStream(file);
            bis = new BufferedInputStream(is);
            ServletOutputStream outputStream = response.getOutputStream();
            for (int i = bis.read(buffer); i!=-1; i = bis.read(buffer)){
                outputStream.write(buffer,0,i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bis!=null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public Workbook<Chapter, String> down(Book book, int downer) throws InterruptedException {
        BookDownloader downloader = new BookDownloader(book, book.getName());
        downloader.setWorkerNumber(downer);
        downloader.workStart();
        return downloader;
    }

}