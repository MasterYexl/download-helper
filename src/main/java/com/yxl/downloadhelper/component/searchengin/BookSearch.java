package com.yxl.downloadhelper.component.searchengin;

import com.yxl.downloadhelper.model.dto.Book;
import com.yxl.downloadhelper.model.dto.Chapter;
import com.yxl.downloadhelper.model.Url;
import com.yxl.downloadhelper.utils.io.TIO;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookSearch {

    Spider spider = new Spider();

    int answersLength = 0;
    int txtAnswersLength = 0;
    String nowSearch;

    public void getText(String name) throws IOException {
        List<Url> search = search(name);
        for (Url url : search) {
            System.out.println(url);
        }
    }

    //搜索在线资源
    public List<Url> search(String name) throws IOException {
        answersLength = 0;
        nowSearch = name;
        return spider.search(name + "全文");
    }

    //搜索下载链接
    public List<Url> searchTxt(String name) throws IOException {
        txtAnswersLength = 0;
        nowSearch = name;
        return spider.search(name + " 全文 txt 下载");
    }

    public List<Url> searchNext() throws IOException {
        answersLength += 10;
        return spider.search(nowSearch + "全文", answersLength, answersLength);
    }

    //搜索下载链接
    public List<Url> searchTxtNext() throws IOException {
        txtAnswersLength += 10;
        return spider.search(nowSearch + " 全文 txt 下载", answersLength, answersLength);
    }

    public String getDownloadLink(String url) throws IOException {
        Element element = spider.getElement(url);
        Elements a = element.select("a");
        for (Element link : a) {
            if (link.text().matches("(.*[tT][xX][tT].*)|(.*下载本书.*)")) {
                if (link.attr("href").contains("down") && link.attr("href").contains("txt")) return link.attr("href");
            }
        }
        return null;
    }

    //解析目录
    public Book parseCatalog(String url) throws IOException {
        Element element = spider.getElement(url);
        //越往后精确度越低
        String[] tags = {"li", "span", "a"};
        Book book = new Book();
        book.setName(parseTitle(element));
        verifyName(book);
        for (String tag : tags) {
            Elements li = element.select(tag);
            if (li.isEmpty()) continue;
            int size = 0;
            String realUrl = null;
            boolean start = false;
            int number = 0;
            List<Chapter> chapters = new ArrayList<>();
            for (Element attr : li) {
                //确保从第一章开始
                if (attr.text().matches("(前 *言.*)|(序.*)|(.*引子.*)|(第?0*[序一1][章集部卷话回节篇] ?.*)|(^1[.、] ?.+)")) start = true;
                if (start && attr.text().toLowerCase().matches("(前 *言.*)|(序.*)|(.*引子.*)|(.*第?[序零一二三四五六七八九十百千万壹贰叁肆伍陆柒捌玖拾佰仟亿0-9]+[章集部卷话回节篇] ?.*)|(^[1-9]+[.、] ?.+)|(champter ?[0-9]+.*)")) {
                    Chapter chapter = new Chapter();
                    chapter.setTitle(attr.text());
                    chapter.setSequence(number++);
                    Elements a = attr.select("a");
                    if (a.isEmpty()) continue;
                    //获取真实地址
                    if (realUrl == null) realUrl = Spider.getRealUrl(url);
                    chapter.setContent(parseHref(realUrl, a.get(0).attr("href")));
                    size++;
                    chapters.add(chapter);
                }
            }
            book.setChapterList(chapters);
            if (size != 0) {
                book.setUrl(realUrl);
                return book;
            }
        }
        return book;
    }

    public void verifyName(Book book) {
        if (book.getName().isEmpty()) book.setName(nowSearch == null ? "NULL" : nowSearch);
        String name = book.getName().replace(":", "");
        name = name.replace("?", "");
        name = name.replace("*", "");
        name = name.replace("\\", "");
        name = name.replace("/", "");
        name = name.replace("<", "");
        name = name.replace(">", "");
        name = name.replace("\"", "");
        name = name.replace("|", "");
        book.setName(name.substring(0,Math.min(16, name.length())));
    }

    public String parseHref(String homePage, String href) {
        if (href.matches("^https?://.*")) return href;
        if (href.matches("^//.*")) {
            int i = homePage.indexOf("//");
            return homePage.substring(0,i) + href;
        } else if (href.matches("^/.*")) {
            int i = homePage.indexOf("/", 10);
            return homePage.substring(0, i) + href;
        } else {
            int last = homePage.lastIndexOf("/");
            return homePage.substring(0, last + 1) + href;
        }
    }

    public String parseTitle(Element page) {
        String name = null;
        Elements select = page.select("h1");
        if (select.isEmpty()) select = page.select("h2");
        name = select.text();
        if (name == null || name.isEmpty()) {
            String text = page.text();
            if (text.contains("《") && text.contains("》"))
                name = text.substring(text.indexOf("《") + 1, text.indexOf("》"));
        }
        return name;
    }

    //解析内容
    public String parseContent(String url, boolean html) throws IOException {
        Element element = spider.getElement(url);
//        System.out.println("parseContent.element: "+element);
        Elements content;
        Elements mostLikely = null;
        content = element.select("[id~=.*[cC][oO][nN][tT][eE][nN][tT].*]");
        if (content.isEmpty()) content = element.select("[class~=.*[cC][oO][nN][tT][eE][nN][tT].*]");
        if (content.isEmpty()) content = element.select("[id~=.*[aA][rR][tT][iI][cC][lL][eE].*]");
        if (content.isEmpty()) content = element.select("[class~=.*[aA][rR][tT][iI][cC][lL][eE].*]");
        if (content.isEmpty()) {
            for (Element element1 : element.getElementsByTag("body")) {
                if (mostLikely == null || element1.text().length() > mostLikely.text().length())
                    mostLikely = element1.getAllElements();
            }
            content = mostLikely;
        }
        assert content != null;
        if (content.isEmpty()) return "";
        Elements tmp = content.clone();
        String finalContent = content.text();
        if (finalContent.isEmpty()) finalContent = tmp.text();
        String[] analyze = finalContent.split(" ");
        StringBuilder stringBuilder = new StringBuilder("");
        int st = 0, ed = analyze.length - 1;
        for (String line : analyze) {
            if (line.matches(".*[。！？”.?!\"']")) break;
            st++;
        }
        for (int i = ed; i >= 0; i--) {
            if (analyze[i].matches(".*[。！？”.?!…\"']")) break;
            ed--;
        }
        for (int i = st; i <= ed; i++) {
            stringBuilder.append("\n").append(analyze[i]).append("\n");
        }
        finalContent = stringBuilder.toString();
        return (html ? "&nbsp&nbsp&nbsp&nbsp" : "       ") + finalContent.replaceAll("\n", html ? "<br />&nbsp&nbsp&nbsp&nbsp" : "\n       ");
    }

    public String parseContent(String url) throws IOException {
        return parseContent(url, false);
    }

    public void bookWriter(Book book) {
        try {
            TIO.write("\n\n\n\n" + book.getName() + "\n\n\n\n", book.getName() + ".txt");
            for (int i = 0; i < book.getSize(); i++) {
                Chapter chapter = book.getChapter(i);
                String s = parseContent(chapter.getContent());
//                System.out.println("正在写入:"+chapter.getTitle());
                TIO.write("\n\n" + chapter.getTitle() + "\n\n", book.getName());
                TIO.write(s, book.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


