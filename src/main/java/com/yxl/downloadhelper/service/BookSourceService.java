package com.yxl.downloadhelper.service;

import com.yxl.downloadhelper.common.model.ResponseTemplate;
import com.yxl.downloadhelper.common.model.jpa.CommonRepository;
import com.yxl.downloadhelper.common.service.CommonService;
import com.yxl.downloadhelper.model.dto.Book;
import com.yxl.downloadhelper.model.dto.Chapter;
import com.yxl.downloadhelper.model.entity.AuthorEntity;
import com.yxl.downloadhelper.model.entity.BookSourceEntity;
import com.yxl.downloadhelper.model.entity.WebsiteEntity;
import com.yxl.downloadhelper.repository.AuthorRepository;
import com.yxl.downloadhelper.repository.BookSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookSourceService extends CommonService<BookSourceEntity> {

    private final BookSourceRepository bookSourceRepository;

    @Override
    public CommonRepository<BookSourceEntity> getRepository() {
        return bookSourceRepository;
    }

    public BookSourceEntity add(Book book, WebsiteEntity website) {
        BookSourceEntity bookSourceEntity = new BookSourceEntity();
        bookSourceEntity.setBookId(book.getId());
        List<Chapter> chapterList = book.getChapterList();
        Chapter lastChapter = chapterList.get(chapterList.size() - 1);
        bookSourceEntity.setNewSequence(lastChapter.getSequence());
        bookSourceEntity.setNewChapter(lastChapter.getTitle());
        bookSourceEntity.setWebsiteId(website.getId());
        ResponseTemplate<BookSourceEntity> add = add(bookSourceEntity);
        return add.getDetail();
    }
}
