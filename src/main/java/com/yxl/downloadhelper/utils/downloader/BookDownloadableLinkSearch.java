package com.yxl.downloadhelper.utils.downloader;

import com.yxl.downloadhelper.model.Url;
import com.yxl.downloadhelper.utils.searchengin.BookSearch;
import com.yxl.downloadhelper.utils.workbook.Workbook;

import java.io.IOException;
import java.util.List;

public class BookDownloadableLinkSearch extends Workbook<Url, Url>{
    BookSearch bookSearch = new BookSearch();
    private Url url;

    public BookDownloadableLinkSearch(Url url){
        addTask(url);
        initFunction();
    }

    public BookDownloadableLinkSearch(List<Url> url){
        addTask(url);
        initFunction();
    }

    private void initFunction() {
        taskFunction = url1 -> {
            String downloadLink = null;
            try {
                downloadLink = bookSearch.getDownloadLink(url1.getUrl());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (downloadLink!=null){
                url1.setUrl(downloadLink);
                return url1;
            }
            return null;
        };
    }

    public Url getUrl() {
        return url;
    }

}
