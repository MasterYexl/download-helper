package com.yxl.downloadhelper.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Book implements Serializable {
    String name;
    List<Chapter> chapters = new ArrayList<>();
    List<String> catalog = new ArrayList<>();
    int readingChapter = -1;
    String readingPos;
    String fromURL;

    public void setFromURL(String fromURL) {
        this.fromURL = fromURL;
    }

    public String getFromURL() {
        return fromURL;
    }

    public int getReadingChapter() {
        return readingChapter;
    }

    public void setReadingChapter(int readingChapter) {
        this.readingChapter = readingChapter;
    }

    public String getReadingPos() {
        return readingPos;
    }

    public void setReadingPos(String readingPos) {
        this.readingPos = readingPos;
    }

    private int size;

    public int getSize() {
        return size;
    }

    public Book(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    //目录需强制更新
    public List<String> getCatalog() {
        if (catalog.size() == 0) {
            for (Chapter chapter : chapters) {
                catalog.add(chapter.getTitle());
            }
        }
        return catalog;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Chapter getChapter(int number) {
        return chapters.get(number);
    }

    public List<Chapter> getChapters(){
        return chapters;
    }

    public void deleteChapter(int index) {
        chapters.remove(index);
        size--;
    }

    public void addChapters(Chapter chapter) {
        chapters.add(chapter);
        size++;
    }

    @Override
    public String toString() {
        return "《" + name + "》";
    }
}
