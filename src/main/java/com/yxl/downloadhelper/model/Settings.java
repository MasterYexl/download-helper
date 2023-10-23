package com.yxl.downloadhelper.model;

public class Settings {
    String bookPath;//下载目录
    int downer;//下载线程数
    String[] bcColor;//背景颜色
    String[] fcColor;//字体颜色
    int fontSize;

    public String getBookPath() {
        return bookPath;
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }

    public int getDowner() {
        return downer;
    }

    public void setDowner(int downer) {
        this.downer = downer;
    }

    public String[] getBcColor() {
        return bcColor;
    }

    public void setBcColor(String[] bcColor) {
        this.bcColor = bcColor;
    }

    public String[] getFcColor() {
        return fcColor;
    }

    public void setFcColor(String[] fcColor) {
        this.fcColor = fcColor;
    }
}
