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

}
