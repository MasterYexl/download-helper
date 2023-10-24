package com.yxl.downloadhelper.controller;

import com.yxl.downloadhelper.conponent.BookShelfIO;
import com.yxl.downloadhelper.conponent.SettingsIO;
import com.yxl.downloadhelper.model.book.Book;
import com.yxl.downloadhelper.model.BookStore;
import com.yxl.downloadhelper.model.book.Chapter;
import com.yxl.downloadhelper.tool.Tool;
import com.yxl.downloadhelper.utils.searchengin.BookSearch;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/parse")
public class ParseController {
    BookSearch bookSearch = null;

    @PostMapping("/catalog")
    public String parseCatalog(@RequestParam("url") String url, Model model, HttpSession session) {
        if (bookSearch == null) bookSearch = new BookSearch();
        Map<String, Book> books = Tool.getBooks(session);
        try {
            Book book = books.get(url);
            if (book==null) {
                try {
                    System.out.println("尝试从本地加载");
                    book = Tool.getBookFromBin(url);
                    if (book!=null) System.out.println("本地加载成功！ 上次阅读章节: "+book.getReadingChapter()+" 上次阅读进度: "+book.getReadingPos());
                } catch (ClassNotFoundException e) {
                    System.out.println("本地未找到本书");
                }
            }
            if (book==null) {
                book = bookSearch.parseCatalog(url);
            }
            if (book.getChapters().size() != 0) {
                Tool.addBookToSession(url, book, session);
                model.addAttribute("inShelf", BookShelfIO.contain(Tool.transUrl(book.getFromURL())));
                model.addAttribute("book", book);
                model.addAttribute("id", Tool.getId(url,session));
                model.addAttribute("downer", SettingsIO.get("downer"));
                return "catalog";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pageCanNotParse(url, "请确保该页面有文章目录，或者可以直接浏览原页面", model);
    }


    @PostMapping("/content")
    @ResponseBody
    public String parseContent(@RequestParam("id") int id, @RequestParam("num") int num, HttpSession session) {
        if (bookSearch == null) bookSearch = new BookSearch();
        try {
            Book book1 = Tool.getBookFromID(id, session);
            if (book1==null) return "null";
            Chapter chapter = book1.getChapter(num);
            return bookSearch.parseContent(chapter.getContent(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "null";
    }

    @GetMapping("/content")
    public String parseContent(@RequestParam("id") int id, @RequestParam("num") int num, Model model, HttpSession session) {
        if (bookSearch == null) bookSearch = new BookSearch();
        Chapter chapter = null;
        try {
            Book book1 = Tool.getBookFromID(id, session);
            if (book1==null) return pageCanNotParse("/", "当前浏览器未解析该小说目录", model);
            chapter = book1.getChapter(num);
//            System.out.println("parseContent.chapter:"+chapter);
            String content = bookSearch.parseContent(chapter.getContent(), true);
            if (!"".equals(content)) {
                model.addAttribute("chapter", chapter);
                model.addAttribute("content", content);
                model.addAttribute("num", num);
                model.addAttribute("id", id);
                model.addAttribute("chapters", book1.getChapters());
                String[] bcColors = SettingsIO.get("bc-color").split(" ");
                String[] fcColors = SettingsIO.get("fc-color").split(" ");
                model.addAttribute("fontSize", SettingsIO.get("font-size"));
                model.addAttribute("ras", SettingsIO.get("read-auto-save"));
                model.addAttribute("bcColors", bcColors);
                model.addAttribute("fcColors", fcColors);
                model.addAttribute("dfc", SettingsIO.get("default-fc"));
                model.addAttribute("dbc", SettingsIO.get("default-bc"));
                model.addAttribute("bufferChapter", SettingsIO.get("buffer-chapter"));
                model.addAttribute("preChapter", book1.getReadingChapter());
                model.addAttribute("prePos", book1.getReadingPos());
                return "read";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pageCanNotParse(chapter.getContent(), "请确保该页面有文章，或者可以直接浏览原页面", model);
    }

    @RequestMapping("/open")
    public String open(@RequestParam("index") int index, Model model, HttpSession session){
        BookStore bookstore = BookShelfIO.get(index);
        try {
            Book bookFromBin = Tool.getBookFromBin(bookstore.getUrl());
            Tool.addBookToSession(bookFromBin.getFromURL(), bookFromBin, session);
            int num = bookFromBin.getReadingChapter();
            if (num==-1) num=0;
            return parseContent(Tool.getBooks(session).size()-1, num,model, session);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "error/404";
    }

    public String pageCanNotParse(String url, String msg, Model model) {
        model.addAttribute("url", url);
        model.addAttribute("msg", msg);
        return "error/406";
    }
}
