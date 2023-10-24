package com.yxl.downloadhelper.utils.searchengin;

import com.yxl.downloadhelper.model.Url;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Spider {

    public static Element parseLocalFile(File file){
        Document document = null;
        try {
            document = Jsoup.parse(file, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    public Element getElement(String url) throws IOException {
        Connection connect = Jsoup.connect(url);
        //设置useragent,设置超时时间，并以get请求方式请求服务器
        setHeader(connect);
        Document document = null;
        document = connect.ignoreContentType(true).get();
        //获取指定标签的数据
        //输出html数据
        return document;
    }

    public List<Url> search(String key) throws IOException {
        List<Url> list = baidu(key);
        return list;
    }

    //number、start为10的整数
    public List<Url> search(String key, int number) throws IOException {
        List<Url> list = baidu(key);
        for (int i=10;i<number;i+=10){
            list.addAll(baidu(key, i));
        }
        return list;
    }

    public List<Url> search(String key, int start, int number) throws IOException {
        List<Url> list = baidu(key, start);
        for (int i=start;i<number;i+=10){
            list.addAll(baidu(key, i));
        }
        return list;
    }

    public List<Url> baidu(String key) throws IOException {
        return baidu(key,0);
    }

    public List<Url> baidu(String key, int pn) throws IOException {
        String baiduUrl = "https://www.baidu.com/s?wd=";
        Element element = getElement(baiduUrl+key+"&pn="+pn);
        Elements h3 = element.select("h3[class~=.* *t *.*]");
        List<Url> list = new LinkedList<>();
        for (Element rs: h3){
            Url url = new Url();
            Elements a = rs.select("a");
            if (a.size()>1) url.setOfficial(true);
            url.setTitle(a.get(0).text());
            url.setUrl(a.get(0).attr("href"));
            list.add(url);
        }
        return list;
    }

    public static String getRealUrl(String url){
        if (!url.matches("^https?://www\\.baidu\\.com.*")) return url;
        Connection connect = Jsoup.connect(url);
        try {
            setHeader(connect);
            Connection.Response execute = connect.method(Connection.Method.GET).followRedirects(false).execute();
            String rs = execute.header("Location");
            if (rs==null) rs=url;
            return rs;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void setHeader(Connection connect){
        connect.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        connect.header("Connection", "keep-alive");
        connect.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/17.17134");
    }


    @Test
    public void urlTest(){
        System.out.println(getRealUrl("http://www.baidu.com/link?url=NxqkSd4EPXZypgv1NrqBvoOcuCVKo3ZPrVWtKLSULz6YWfNjNC08TpuVxw3YSqVtHKvQa15X4Em1aVLlaynuZUog7xtB9OsvpPGoBNekrz3"));
    }

    @Test
    public void tmpTest(){
        try {
            System.out.println(getElement("http://product.dangdang.com/28992419.html"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
