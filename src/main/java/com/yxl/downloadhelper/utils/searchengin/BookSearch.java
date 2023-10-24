package com.yxl.downloadhelper.utils.searchengin;

import com.yxl.downloadhelper.model.book.Book;
import com.yxl.downloadhelper.model.book.Chapter;
import com.yxl.downloadhelper.model.Url;
import com.yxl.downloadhelper.utils.io.TIO;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

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


    /*
    https://www.15xs.com/down/txt/32294/我是猫.txt
     */
    public String getDownloadLink(String url) throws IOException {
        Element element = spider.getElement(url);
        Elements a = element.select("a");
        for (Element link : a) {
            if (link.text().matches("(.*[tT][xX][tT].*)|(.*下载本书.*)")) {
                if (link.attr("href").contains("down") && link.attr("href").contains("txt")) return link.attr("href");
            }
//            else System.out.println(link.text());
        }
        return null;
    }

    @Test
    public void dlTest() {
        List<Url> search = null;
        try {
            search = searchTxt("美食供应商");
            for (Url url : search) {
                System.out.println(url);
                try {
                    String link = getDownloadLink(url.getUrl());
                    if (link != null) System.out.println(link);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //解析目录
    public Book parseCatalog(String url) throws IOException {
        Element element = spider.getElement(url);
        //越往后精确度越低
        String[] tags = {"li", "span", "a"};
        Book book = new Book(parseTitle(element));
        verifyName(book);
        for (String tag : tags) {
            Elements li = element.select(tag);
            if (li.size() == 0) continue;
            int size = 0;
            String realUrl = null;
            boolean start = false;
            int number = 0;
            for (Element attr : li) {
                //确保从第一章开始
                if (attr.text().matches("(前 *言.*)|(序.*)|(.*引子.*)|(第?0*[序一1][章集部卷话回节篇] ?.*)|(^1[.、] ?.+)")) start = true;
                if (start && attr.text().toLowerCase().matches("(前 *言.*)|(序.*)|(.*引子.*)|(.*第?[序零一二三四五六七八九十百千万壹贰叁肆伍陆柒捌玖拾佰仟亿0-9]+[章集部卷话回节篇] ?.*)|(^[1-9]+[.、] ?.+)|(champter ?[0-9]+.*)")) {
                    Chapter chapter = new Chapter();
                    chapter.setTitle(attr.text());
                    chapter.setNumber(number++);
                    Elements a = attr.select("a");
                    if (a.size() == 0) continue;
                    //获取真实地址
                    if (realUrl == null) realUrl = spider.getRealUrl(url);
                    chapter.setContent(parseHref(realUrl, a.get(0).attr("href")));
                    size++;
                    book.addChapters(chapter);
                }
            }
            if (size != 0) {
                book.setFromURL(realUrl);
                return book;
            }
        }
        return book;
    }

    public void verifyName(Book book) {
        if (book.getName().equals("")) book.setName(nowSearch == null ? "NULL" : nowSearch);
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
    @Test
    public void psTest(){
        System.out.println(parseHref("https://www.shizongzui.com/", "/qianzhuan/diyijuan/2.html"));
    }

    public String parseTitle(Element page) {
        String name = null;
        Elements select = page.select("h1");
        if (select.size() == 0) select = page.select("h2");
        name = select.text();
        if (name == null || "".equals(name)) {
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
        if (content.size() == 0) content = element.select("[class~=.*[cC][oO][nN][tT][eE][nN][tT].*]");
        if (content.size() == 0) content = element.select("[id~=.*[aA][rR][tT][iI][cC][lL][eE].*]");
        if (content.size() == 0) content = element.select("[class~=.*[aA][rR][tT][iI][cC][lL][eE].*]");
        if (content.size() == 0) {
            for (Element element1 : element.getElementsByTag("body")) {
                if (mostLikely == null || element1.text().length() > mostLikely.text().length())
                    mostLikely = element1.getAllElements();
            }
            content = mostLikely;
        }
        if (content.size() == 0) return "";
        Elements tmp = content.clone();
//        for (Element real : content) {
//            if (mostLikely==null||real.text().length()>mostLikely.text().length()) mostLikely = real.getAllElements();
//            real.children().select("[class~=.+]").remove();
//        }
        String finalContent = content.text();
        if (finalContent.length() == 0) finalContent = tmp.text();
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

    @Test
    public void modeTest() {
        try {
            String content = parseContent("https://vipreader.qidian.com/chapter/1019490575/524303614");
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @Test
    public void titleTest() {
//        File file = new File("D:\\YXL\\WorkSpace\\java\\springboot\\download-helper\\src\\main\\java\\com\\yxl\\downloadhelper\\web\\斗罗大陆无弹窗_斗罗大陆最新章节列表_看笔趣阁.html");
//        Element element = Spider.parseLocalFile(file);
        List<Url> search = null;
        try {
            search = search("末世大回炉");
            for (Url url : search) {
                Book book = parseCatalog(url.getUrl());
                System.out.println(book.getName() + " \n" + url.getTitle() + " " + url.getUrl() + " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        BookSearch bookSearch = new BookSearch();
        List<Url> search = null;
        try {
            search = bookSearch.search("斗罗大陆");
            for (Url url : search) {
                Book book = bookSearch.parseCatalog(url.getUrl());
                System.out.println(url + "\n是否写入? >");
                Scanner scanner = new Scanner(System.in);
                if (scanner.nextLine().equals("y")) {
                    bookSearch.bookWriter(book);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        List<Url> search = null;
        try {
            search = search("美食供应商");
            for (Url url : search) {
                System.out.println(url);
                parseCatalog(url.getUrl());
                System.out.println("\n\n下一个\n\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void dialogTest() {
        try {
            parseCatalog("http://www.wanjie8.com/files/article/html/2/2689/index.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseTest() {
        try {
            Book book = parseCatalog("https://www.shizongzui.com/");
            for (Chapter chapter : book.getChapters()){
                System.out.println(chapter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void searchTest() {
        List<Url> list = null;
        try {
            list = search("斗罗大陆");
            for (Url url : list) {
                System.out.println(url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}


