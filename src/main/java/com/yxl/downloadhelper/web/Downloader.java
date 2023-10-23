package com.yxl.downloadhelper.web;

import com.yxl.downloadhelper.conponent.SettingsIO;
import com.yxl.downloadhelper.model.Chapter;
import com.yxl.downloadhelper.model.Settings;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

public class Downloader extends Thread{
    WorkBook<Chapter, String > workBook;
    public Downloader(WorkBook<Chapter, String> workBook){
        this.workBook = workBook;

    }

    @Override
    public void run() {
        Chapter chapter = workBook.get();
        BookSearch bookSearch = new BookSearch();
        while (chapter!=null) {
            try {
                System.out.println(Thread.currentThread().getName()+": 正在缓存 "+chapter);
                String stringBuilder = chapter.getTitle() + "\n\n" +
                        bookSearch.parseContent(chapter.getContent())+"\n\n";
                workBook.put(stringBuilder, chapter.getNumber());
                chapter = workBook.get();
            } catch (IOException e) {
                workBook.add(chapter, true);
                e.printStackTrace();
            }
        }
        int code = workBook.getFinalWork();
        if (code!=-1){
            System.out.println(Thread.currentThread().getName()+": 正在写入txt");
            List<String> result = workBook.getResult();
            for (String s: result){
                if (s==null) continue;
                try {

                    TIO.write(s, SettingsIO.get("book-path")+"/"+workBook.getName()+".txt");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        workBook.workFinish(code);
    }
}
