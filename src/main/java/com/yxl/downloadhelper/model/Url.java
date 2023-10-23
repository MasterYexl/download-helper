package com.yxl.downloadhelper.model;

public class Url{
    String title;
    String url;
    boolean isOfficial = false;

    public String getTitle() {
        return title;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(boolean official) {
        isOfficial = official;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return (isOfficial? "<官方>":"")+title+" "+url;
    }
}