package com.yxl.downloadhelper.controller;

import com.yxl.downloadhelper.model.Url;
import com.yxl.downloadhelper.utils.searchengin.BookSearch;
import com.yxl.downloadhelper.utils.downloader.BookDownloadableLinkSearch;
import com.yxl.downloadhelper.utils.workbook.Workbook;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
        BookDownloadableLinkSearch linkSearch = new BookDownloadableLinkSearch(search);
        linkSearch.setWorkerNumber(search.size());
        linkSearch.workStart();
        for (int i = 0; i < 30; i++) {
            if (linkSearch.isFinish()) break;
            System.out.println("主线程：等待完成");
            Thread.sleep(100);
        }
        System.out.println("结束爬取, 用时:"+(System.currentTimeMillis()-time));
        return urlToJson(linkSearch.getResult());
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
