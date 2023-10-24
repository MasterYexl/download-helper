package com.yxl.downloadhelper;

import com.yxl.downloadhelper.controller.BookshelfController;
import com.yxl.downloadhelper.model.book.Book;
import com.yxl.downloadhelper.model.book.Chapter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DownloadHelperApplicationTests {

    @Test
    void contextLoads() {
        BookshelfController bookshelfController = new BookshelfController();
        Book book = new Book("my");
        Chapter chapter = new Chapter();
        chapter.setNumber(1);
        chapter.setContent("123");
        chapter.setTitle("第一章");
        Chapter chapter2 = new Chapter();
        chapter2.setNumber(2);
        chapter2.setContent("123");
        chapter2.setTitle("第二章");
        book.addChapters(chapter);
        book.addChapters(chapter2);
        bookshelfController.addBook(book);
    }

}
