package com.yxl.downloadhelper.repository;

import com.yxl.downloadhelper.common.model.jpa.CommonRepository;
import com.yxl.downloadhelper.model.entity.BookShelfEntity;
import com.yxl.downloadhelper.model.entity.BookSourceEntity;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookSourceRepository extends CommonRepository<BookSourceEntity> {
    public BookSourceEntity getByBookIdAndWebsiteId(UUID bookID, UUID websiteID);
}
