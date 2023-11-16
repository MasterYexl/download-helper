package com.yxl.downloadhelper;

import com.yxl.downloadhelper.model.dto.Book;
import com.yxl.downloadhelper.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DownloadHelperApplicationTests {

    @Autowired
    private BookService bookService;

    @Test
    void testParse() {
        //https://www.52bqg.org/book_10049/
//        Book book = bookService.parseBookWithUrl("https://www.52bqg.org/book_10049/");
        Book book = bookService.parseBookWithUrl("http://localhost:8080/test/%E6%97%A0%E9%99%90%E6%81%90%E6%80%96%E6%9C%80%E6%96%B0%E7%AB%A0%E8%8A%82_%E6%97%A0%E9%99%90%E6%81%90%E6%80%96%E5%85%A8%E6%96%87%E5%85%8D%E8%B4%B9%E9%98%85%E8%AF%BB-%E7%AC%94%E8%B6%A3%E9%98%81.html");
        System.out.println(book);
    }

}
