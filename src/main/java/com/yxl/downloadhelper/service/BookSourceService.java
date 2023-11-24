package com.yxl.downloadhelper.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yxl.downloadhelper.common.model.ResponseTemplate;
import com.yxl.downloadhelper.common.model.jpa.CommonRepository;
import com.yxl.downloadhelper.common.service.CommonService;
import com.yxl.downloadhelper.model.dto.Book;
import com.yxl.downloadhelper.model.dto.BookSource;
import com.yxl.downloadhelper.model.dto.Chapter;
import com.yxl.downloadhelper.model.entity.BookSourceEntity;
import com.yxl.downloadhelper.repository.BookSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookSourceService extends CommonService<BookSourceEntity> {

    private final BookSourceRepository bookSourceRepository;
    private final ObjectMapper objectMapper;

    @Override
    public CommonRepository<BookSourceEntity> getRepository() {
        return bookSourceRepository;
    }

    public BookSource add(Book book, UUID websiteID) {
        BookSourceEntity bookSourceEntity = bookSourceRepository.getByBookIdAndWebsiteId(book.getId(), websiteID);
        if (bookSourceEntity != null) {
            updateChapter(book, bookSourceEntity);
            BookSourceEntity detail = save(bookSourceEntity).getDetail();
            return objectMapper.convertValue(detail, BookSource.class);
        }
        bookSourceEntity = new BookSourceEntity();
        bookSourceEntity.setBookId(book.getId());
        updateChapter(book, bookSourceEntity);
        bookSourceEntity.setWebsiteId(websiteID);
        bookSourceEntity.setUrl(book.getUrl());
        ResponseTemplate<BookSourceEntity> add = add(bookSourceEntity);
        BookSource bookSource = objectMapper.convertValue(add.getDetail(), BookSource.class);
        bookSource.setNew(true);
        return bookSource;
    }

    private void updateChapter(Book book, BookSourceEntity bookSourceEntity) {
        List<Chapter> chapterList = book.getChapterList();
        Chapter lastChapter = chapterList.get(chapterList.size() - 1);
        bookSourceEntity.setNewSequence(lastChapter.getSequence());
        bookSourceEntity.setNewChapter(lastChapter.getTitle());
    }

    public String parsePath(String url) {
        return url.replaceAll("(.*//)?[^/]+", "");
    }
}
