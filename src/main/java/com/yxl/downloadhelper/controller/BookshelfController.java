package com.yxl.downloadhelper.controller;


import com.yxl.downloadhelper.conponent.BookShelfIO;
import com.yxl.downloadhelper.conponent.SettingsIO;
import com.yxl.downloadhelper.model.Book;
import com.yxl.downloadhelper.tool.Tool;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

@Controller
@RequestMapping("/bookshelf")
public class BookshelfController {


    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("bookList", BookShelfIO.getList());
        return "bookshelf";
    }

    @RequestMapping("/add")
    @ResponseBody
    public String add(@RequestParam("id") int id, HttpSession session){
        Book book = Tool.getBookFromID(id,session);
        if (book==null) return "BOOK LIST IS EMPTY";
        addBook(book);
        return "OK";
    }
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("id") int id, HttpSession session){
        Book book = Tool.getBookFromID(id,session);
        if (book==null) return "BOOK LIST IS EMPTY";
        try {
            BookShelfIO.delete(Tool.transUrl(book.getFromURL()));
        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL";
        }
        return "OK";
    }
    @RequestMapping("/update-info")
    @ResponseBody
    public String updateReadingInfo(@RequestParam("id") int id, @RequestParam("chapter") int chapter,@RequestParam("pos") String pos, HttpSession session){
        Book book = Tool.getBookFromID(id, session);
        if (book==null) return "BOOK LIST IS EMPTY";
        book.setReadingChapter(chapter);
        book.setReadingPos(pos);
//        System.out.println("当前阅读第"+book.getReadingChapter()+"章，位置"+book.getReadingPos());
        bookToObjFile(book, SettingsIO.get("book-path")+File.separator+"bin");
        return "OK";
    }
    public void addBook(Book book){
        String binPath = SettingsIO.get("book-path")+File.separator+"bin";
        bookToObjFile(book, binPath);
        try {
            BookShelfIO.add(Tool.transUrl(book.getFromURL()), book.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void bookToObjFile(Book book, String folderPath){
        FileOutputStream fileOutputStream;
        ObjectOutputStream outputStream = null;
        try {
            String name = folderPath+ File.separator+Tool.transUrl(book.getFromURL())+".ser";
            fileOutputStream = new FileOutputStream(name);
            outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(book);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream!=null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
