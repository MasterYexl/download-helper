package com.yxl.downloadhelper.utils.downloader;

import com.yxl.downloadhelper.conponent.SettingsIO;
import com.yxl.downloadhelper.model.book.Book;
import com.yxl.downloadhelper.model.book.Chapter;
import com.yxl.downloadhelper.utils.searchengin.BookSearch;
import com.yxl.downloadhelper.utils.io.TIO;
import com.yxl.downloadhelper.utils.workbook.Workbook;
import com.yxl.downloadhelper.utils.workbook.Worker;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class BookDownloader extends Workbook<Chapter, String>{
    private final Book book;
    private final BookSearch bookSearch = new BookSearch();

    public BookDownloader(Book book, String savePath) {

        addTask(book.getChapters());
        taskFunction = chapter -> {
            System.out.println(Thread.currentThread().getName() + ": 正在缓存 " + chapter);
            try {
                return chapter.getTitle() + "\n\n" +
                        bookSearch.parseContent(chapter.getContent()) + "\n\n";
            } catch (IOException e) {
                return "";
            }
        };
        endFunction = workbook -> {
            System.out.println(Thread.currentThread().getName() + ": 正在写入txt");
            List<String> result = workbook.getResult();
            for (String s : result) {
                if (s == null) continue;
                try {
                    TIO.write(s, SettingsIO.get("book-path") + "/" + workbook.getName() + ".txt");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        String name = book.getName();
        for (int i = 0; true; i++) {
            File file = new File(savePath + File.separator + name + (i == 0 ? ".txt" : i + ".txt"));
            if (!file.exists()) {
                book.setName(name + (i == 0 ? "" : "(" + i + ")"));
                break;
            }
        }
        book.setName(deDuplicate(book.getName(), savePath, "txt"));
        this.book = book;
    }

    public String deDuplicate(String name, String filePath, String fileType) {
        for (int i = 0; true; i++) {
            File file = new File(filePath + File.separator + name + (i == 0 ? "." + fileType : " (" + i + ")." + fileType));
            if (!file.exists()) return name + (i == 0 ? "" : " (" + i + ")");
        }
    }

    public Book getBook() {
        return book;
    }

}
