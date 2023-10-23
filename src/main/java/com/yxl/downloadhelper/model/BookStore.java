package com.yxl.downloadhelper.model;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class BookStore {
    String url;
    String name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return url + '=' + name;
    }
    @Test
    public void listTest(){
        List<BookStore> bookstores = new LinkedList<>();
        BookStore bookstore = new BookStore();
        bookstore.setName("123");
        bookstore.setUrl("aaa");
        BookStore bookstore1 = new BookStore();
        bookstore1.setName("566");
        bookstore1.setUrl("vbb");
        bookstores.add(bookstore);
        bookstores.add(bookstore1);
        System.out.println(bookstores);
    }
}
