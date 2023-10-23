package com.yxl.downloadhelper.web;

import com.yxl.downloadhelper.model.Url;

import java.io.IOException;
import java.util.List;

public class DownloadableLinkSearch extends Thread{
    BookSearch bookSearch = new BookSearch();
    WorkBook<Url, Url> workBook;
    public DownloadableLinkSearch(WorkBook<Url, Url> workBook){
        this.workBook = workBook;
        workBook.workStart();
    }
    public void getDownloadLink() throws IOException {
        Url url;
        while ((url = workBook.get())!=null){
            String downloadLink = bookSearch.getDownloadLink(url.getUrl());
            if (downloadLink!=null){
                url.setUrl(downloadLink);
                workBook.put(url);
            }
        }
        System.out.println(Thread.currentThread().getName()+" 完成任务 "+url);
        workBook.workFinish(workBook.getFinalWork());
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName()+" 开始任务");
            getDownloadLink();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
