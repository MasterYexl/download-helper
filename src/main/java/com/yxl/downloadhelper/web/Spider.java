package com.yxl.downloadhelper.web;

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
        //链接到目标地址
//        System.out.println("getElement: "+url);
        Connection connect = Jsoup.connect(url);
        //设置useragent,设置超时时间，并以get请求方式请求服务器
        setHeader(connect);
        Document document = null;
        document = connect.ignoreContentType(true).get();
        //获取指定标签的数据
        //输出文本数据
        //System.out.println(elementById.text());
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

//        System.out.println(element);
//        File html = new File("D:\\YXL\\WorkSpace\\java\\springboot\\download-helper\\src\\main\\java\\com\\yxl\\downloadhelper\\web\\test.html");
        //            element = Jsoup.parse(html, "UTF-8", baiduUrl + key);
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
//        Connection: keep-alive
//        Accept: text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01
//User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36 Edg/89.0.774.68
//X-Requested-With: XMLHttpRequest
//Sec-Fetch-Site: same-origin
//Sec-Fetch-Mode: cors
//Sec-Fetch-Dest: empty
//Referer: https://www.baidu.com/s?wd=%E7%88%AC%E8%99%AB%E7%99%BE%E5%BA%A6%E5%AE%89%E5%85%A8%E9%AA%8C%E8%AF%81&rsv_spt=1&rsv_iqid=0xc00489cb00000246&issp=1&f=8&rsv_bp=1&rsv_idx=2&ie=utf-8&rqlang=cn&tn=baiduhome_pg&rsv_enter=1&rsv_dl=tb&oq=html%25E6%2589%2593%25E5%25BC%2580%25E6%2596%25B0%25E7%25AA%2597%25E5%258F%25A3%2526lt%253Ba%2526gt%253B%25E7%259A%2584%25E5%25B1%259E%25E6%2580%25A7&rsv_btype=t&inputT=7918&rsv_t=0fedEUKm6X%2BQZ5V4VdEJF8AfvR%2FoJvvqIQNgSbIFS0%2FfZHZmGNb8oJw2GPOeaR42f8i7&rsv_pq=965af15c000acc8a&rsv_sug3=111&rsv_sug1=73&rsv_sug7=100&sug=%25E7%2588%25AC%25E8%2599%25AB&rsv_n=1&rsv_sug2=0&rsv_sug4=7918
//Accept-Encoding: gzip, deflate, br
//Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6

        connect.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//        connect.header("Accept", "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
        connect.header("Connection", "keep-alive");
        connect.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/17.17134");


    }


    @Test
    public void urlTest(){
        System.out.println(getRealUrl("http://www.baidu.com/link?url=NxqkSd4EPXZypgv1NrqBvoOcuCVKo3ZPrVWtKLSULz6YWfNjNC08TpuVxw3YSqVtHKvQa15X4Em1aVLlaynuZUog7xtB9OsvpPGoBNekrz3"));
    }
//
//    public static void main(String[] args) throws IOException {
//        List<Url> rs = baidu("扫雷F");
//        for (Url url : rs){
//            System.out.println(url);
//        }
//    }
    @Test
    public void tmpTest(){
        try {
            System.out.println(getElement("http://product.dangdang.com/28992419.html"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
