package com.yxl.downloadhelper.repository;

import com.yxl.downloadhelper.common.model.jpa.CommonRepository;
import com.yxl.downloadhelper.model.entity.BookShelfEntity;
import com.yxl.downloadhelper.model.entity.BookSourceEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface BookSourceRepository extends CommonRepository<BookSourceEntity> {
}
