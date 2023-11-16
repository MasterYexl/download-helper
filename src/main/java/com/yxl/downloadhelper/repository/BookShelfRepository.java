package com.yxl.downloadhelper.repository;

import com.yxl.downloadhelper.common.model.jpa.CommonRepository;
import com.yxl.downloadhelper.model.entity.BookEntity;
import com.yxl.downloadhelper.model.entity.BookShelfEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface BookShelfRepository extends CommonRepository<BookShelfEntity> {
}
