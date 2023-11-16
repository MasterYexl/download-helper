package com.yxl.downloadhelper.utils.downloader;

import com.yxl.downloadhelper.model.Url;
import com.yxl.downloadhelper.component.searchengin.BookSearch;
import com.yxl.downloadhelper.utils.workbook.Workbook;
import lombok.Getter;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class BookDownloadableLinkSearch extends Workbook<Url, Url>{
    BookSearch bookSearch = new BookSearch();
    @Getter
    private Url url;

    public BookDownloadableLinkSearch(Url url){
        addTask(url);
    }

    public BookDownloadableLinkSearch(List<Url> url){
        addTask(url);
    }

    @Override
    protected Function<Url, Url> workflow() {
        return url1 -> {
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

}
