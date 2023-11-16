package com.yxl.downloadhelper.service;

import com.yxl.downloadhelper.common.model.jpa.CommonRepository;
import com.yxl.downloadhelper.common.service.CommonService;
import com.yxl.downloadhelper.model.entity.AuthorEntity;
import com.yxl.downloadhelper.model.entity.BookShelfEntity;
import com.yxl.downloadhelper.repository.AuthorRepository;
import com.yxl.downloadhelper.repository.BookShelfRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookShelfService extends CommonService<BookShelfEntity> {

    private final BookShelfRepository bookShelfRepository;

    @Override
    public CommonRepository<BookShelfEntity> getRepository() {
        return bookShelfRepository;
    }
}
