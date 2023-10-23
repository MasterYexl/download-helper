package com.yxl.downloadhelper.tool;

import com.yxl.downloadhelper.conponent.SettingsIO;
import com.yxl.downloadhelper.model.Book;
import com.yxl.downloadhelper.model.Settings;
import com.yxl.downloadhelper.web.BookSearch;
import com.yxl.downloadhelper.web.Spider;
import org.junit.Test;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

public class Tool {

    public static Map<String ,Book> getBooks(HttpSession session){
        Map<String, Book> books = (Map<String, Book>) session.getAttribute("books");
        if (books == null) {
            books = new HashMap<>();
            session.setAttribute("books", books);
            List<String> indexList = new LinkedList<>();
            session.setAttribute("indexList", indexList);
        }
        return books;
    }

    public static Book getBookFromSession(String url, HttpSession session){
        return getBooks(session).get(url);
    }

    public static void addBookToSession(String url, Book book, HttpSession session){
        Map<String, Book> books = getBooks(session);
        if (!books.containsKey(url)){
            books.put(url, book);
            List<String > list = (List<String>) session.getAttribute("indexList");
            list.add(url);
        }
    }

    public static Book getBookFromID(int id, HttpSession session){
        List<String > list = (List<String>) session.getAttribute("indexList");
        if (id<list.size()) return getBookFromSession(list.get(id), session);
        return null;
    }

    public static Book getBookFromBin(String url) throws IOException, ClassNotFoundException {
        url = transUrl(Spider.getRealUrl(url));
        File file = new File(SettingsIO.get("bin-path")+File.separator+url+".ser");
        System.out.println(file.getAbsolutePath());
        if (!file.exists()) return null;
        return serToBook(file);
    }

    public static int getId(String url, HttpSession session){
        List<String > list = (List<String>) session.getAttribute("indexList");
        return list.indexOf(url);
    }
    public static String transUrl(String url){
        return url.replaceAll("/",",").replaceAll("\\.","'").replaceAll(":","~").replaceAll("\\?","`").replaceAll("=","·");
    }
    public static String deTransUrl(String url){
        return url.replaceAll(",", "/").replaceAll("'",".").replaceAll("~",":").replaceAll("`","?").replaceAll("·","=");
    }

    public static Book serToBook(File file) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        return (Book) ois.readObject();
    }

    @Test
    public void indexTest(){
        List<String> list = new LinkedList<>();
        list.add("1234");
        list.add("www.baidu.com");
        list.add("yexl");
        System.out.println(list.indexOf("12"));
    }
}
