package com.yxl.downloadhelper.model;

import com.yxl.downloadhelper.web.Work;

import java.io.Serializable;

public class Chapter implements Work, Serializable {
    String title;
    String content;
    int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return title + " " + content;
    }

    @Override
    public int getId() {
        return number;
    }

    @Override
    public void setId(int id) {
        setNumber(id);
    }
}