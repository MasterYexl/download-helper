package com.yxl.downloadhelper.service;

import com.yxl.downloadhelper.common.model.ResponseTemplate;
import com.yxl.downloadhelper.common.model.jpa.CommonRepository;
import com.yxl.downloadhelper.common.service.CommonService;
import com.yxl.downloadhelper.component.searchengin.BookSearch;
import com.yxl.downloadhelper.model.Url;
import com.yxl.downloadhelper.model.dto.Author;
import com.yxl.downloadhelper.model.dto.Book;
import com.yxl.downloadhelper.model.dto.BookSource;
import com.yxl.downloadhelper.model.dto.Chapter;
import com.yxl.downloadhelper.model.entity.AuthorEntity;
import com.yxl.downloadhelper.model.entity.BookEntity;
import com.yxl.downloadhelper.model.entity.BookSourceEntity;
import com.yxl.downloadhelper.model.entity.WebsiteEntity;
import com.yxl.downloadhelper.repository.BookRepository;
import com.yxl.downloadhelper.utils.downloader.BookDownloadableLinkSearch;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class BookService extends CommonService<BookEntity> {

    private final BookRepository bookRepository;
    private final ChapterService chapterService;
    private final WebsiteService websiteService;
    private final ContentService contentService;
    private final BookSourceService bookSourceService;
    private final AuthorService authorService;
    private final BookSearch bookSearch;

    public String searchBook(String bookName) {
        try {
            List<Url> search = bookSearch.search(bookName);
            if (!search.isEmpty()) {
                return urlToJson(search);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public String searchNext(){
        try {
            List<Url> search = bookSearch.searchNext();
            if (!search.isEmpty()) {
                return urlToJson(search);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public String searchTxtNext(){
        try {
            List<Url> search = bookSearch.searchTxtNext();
            if (!search.isEmpty()) {
                return getDownloadLink(search);
            }
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public String searchDownloadable(String bookName){
        log.info("搜索TXT: "+bookName);
        try {
            List<Url> search = bookSearch.searchTxt(bookName);
            if (!search.isEmpty()) {
                return getDownloadLink(search);
            }
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public List<Book> getBooks(String name) {
        return null;
    }

    @Transactional
    public Book parseBookWithUrl(String url) {
        try {
            Book book = bookSearch.parseBook(url);
            saveBookWithoutContent(book);
            return book;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Transactional
    public void saveBookWithoutContent(Book book) {
        Author author = book.getAuthor();
        AuthorEntity authorEntity = authorService.addAuthor(author);
        author.setId(authorEntity.getId());
        BookEntity bookEntity = addOrGetBook(book);
        book.setId(bookEntity.getId());
        WebsiteEntity website = websiteService.add(book.getUrl(), null);
        BookSource source = bookSourceService.add(book, website.getId());
        List<Chapter> chapterList = book.getChapterList();
        List<Chapter> newChapterList = new ArrayList<>();
        int startSeq = source.isNew()? 0 : source.getNewSequence() + 1;
        for (int i = startSeq; i < chapterList.size(); i++) {
            Chapter chapter = chapterList.get(i);
            chapter.setBookSourceId(source.getId());
            newChapterList.add(chapter);
        }
        chapterService.addAllObject(newChapterList);
    }

    public BookEntity addOrGetBook(Book book) {
        String name = book.getName();
        Author author = book.getAuthor();
        BookEntity entity = bookRepository.getFirstByAuthorIdAndName(author.getId(), name);
        if (entity == null) {
            book.setAuthorId(author.getId());
            entity = addObject(book).getDetail();
        }
        return entity;
    }

    @Override
    public CommonRepository<BookEntity> getRepository() {
        return bookRepository;
    }

    public String getDownloadLink(List<Url> search) throws InterruptedException {
        long time = System.currentTimeMillis();
        BookDownloadableLinkSearch linkSearch = new BookDownloadableLinkSearch(search);
        linkSearch.setWorkerNumber(search.size());
        linkSearch.workStart();
        for (int i = 0; i < 30; i++) {
            if (linkSearch.isFinish()) {
                break;
            }
            log.info("等待获取链接...");
            Thread.sleep(100);
        }
        log.debug("结束爬取, 用时:"+(System.currentTimeMillis()-time));
        return urlToJson(linkSearch.getResult());
    }

    public String urlToJson(List<Url> urls){
        StringBuilder stringBuilder = new StringBuilder("[");
        for (Url url: urls){
            stringBuilder.append("{\"name\":\"").append(url.getTitle()).append("\", \"url\":\"").append(url.getUrl()).append("\"},");
        }
        if (stringBuilder.length()>1) stringBuilder.delete(stringBuilder.length()-1,stringBuilder.length());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
