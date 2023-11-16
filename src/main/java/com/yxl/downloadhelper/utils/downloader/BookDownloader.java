package com.yxl.downloadhelper.utils.downloader;

import com.yxl.downloadhelper.component.SettingsIO;
import com.yxl.downloadhelper.model.dto.Book;
import com.yxl.downloadhelper.model.dto.Chapter;
import com.yxl.downloadhelper.utils.io.TIO;
import com.yxl.downloadhelper.component.searchengin.BookSearch;
import com.yxl.downloadhelper.utils.workbook.Workbook;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class BookDownloader extends Workbook<Chapter, String>{
    @Getter
    private final Book book;
    private final BookSearch bookSearch = new BookSearch();

    public BookDownloader(Book book, String savePath) {
        addTask(book.getChapterList());
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

    @Override
    protected Consumer<Workbook<Chapter, String>> onEnd() {
        return workbook -> {
            log.info("正在写入txt...");
            List<String> result = workbook.getResult();
            for (String s : result) {
                if (s == null) continue;
                try {
                    TIO.write(s, SettingsIO.get("book-path") + "/" + workbook.getName() + ".txt");
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        };
    }

    @Override
    protected Function<Chapter, String> workflow() {
        return chapter -> {
            log.info("正在缓存 " + chapter);
            try {
                return chapter.getTitle() + "\n\n" +
                        bookSearch.parseContent(chapter.getContent()) + "\n\n";
            } catch (IOException e) {
                log.error(e.getMessage());
                return "";
            }
        };
    }

}
