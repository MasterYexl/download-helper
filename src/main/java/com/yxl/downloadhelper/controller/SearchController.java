package com.yxl.downloadhelper.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yxl.downloadhelper.model.Url;
import com.yxl.downloadhelper.web.BookSearch;
import com.yxl.downloadhelper.web.DownloadableLinkSearch;
import com.yxl.downloadhelper.web.WorkBook;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;


@Controller
@RequestMapping("/search")
public class SearchController {
    BookSearch bookSearch = null;
    @GetMapping("/book")
    @ResponseBody
    public String searchBook(@RequestParam("book_name") String bookName){
        System.out.println("搜索: "+bookName);
        if (bookSearch==null) bookSearch = new BookSearch();
        try {
            List<Url> search = bookSearch.search(bookName);
//            for (Url url : search){
//                System.out.println(url);
//            }
            if (search.size()!=0) return urlToJson(search);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("null");
        return "null";
    }
    @GetMapping("/next")
    @ResponseBody
    public String searchNext(){
        if (bookSearch==null) bookSearch = new BookSearch();
        try {
            List<Url> search = bookSearch.searchNext();
//            for (Url url : search){
//                System.out.println(url);
//            }
            if (search.size()!=0) return urlToJson(search);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("null");
        return "null";
    }
    @GetMapping("/txt_next")
    @ResponseBody
    public String searchTxtNext(){
        if (bookSearch==null) bookSearch = new BookSearch();
        try {
            List<Url> search = bookSearch.searchTxtNext();
//            for (Url url : search){
//                System.out.println(url);
//            }
            if (search.size()!=0) return getDownloadLink(search);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("null");
        return "null";
    }

    @GetMapping("/downloadable")
    @ResponseBody
    public String searchDownloadable(@RequestParam("book_name") String bookName){
        System.out.println("搜索TXT: "+bookName);
        if (bookSearch==null) bookSearch = new BookSearch();
        try {
            List<Url> search = bookSearch.searchTxt(bookName);
            if (search.size()!=0) return getDownloadLink(search);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("null");
        return "null";
    }

    public String getDownloadLink(List<Url> search) throws InterruptedException {
        long time = System.currentTimeMillis();
        WorkBook<Url,Url> workBook = new WorkBook<>(search);
        for (int i = 0; i < search.size(); i++) {
            new DownloadableLinkSearch(workBook).start();
        }
        for (int i = 0; i < 30; i++) {
            if (workBook.isFinish()) break;
            System.out.println("主线程：等待完成");
            Thread.sleep(100);
        }
        System.out.println("结束爬取, 用时:"+(System.currentTimeMillis()-time));
        return urlToJson(workBook.getResult());
    }

    public String urlToJson(List<Url> urls){
        StringBuilder stringBuilder = new StringBuilder("[");
        for (Url url: urls){
            stringBuilder.append("{\"name\":\"").append(url.getTitle()).append("\", \"url\":\"").append(url.getUrl()).append("\"},");
        }
        if (stringBuilder.length()>1) stringBuilder.delete(stringBuilder.length()-1,stringBuilder.length());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
    @Test
    public void jsonTest(){
        System.out.println(searchBook("斗罗大陆"));
    }
}
