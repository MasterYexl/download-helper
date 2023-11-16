package com.yxl.downloadhelper.repository;

import com.yxl.downloadhelper.common.model.jpa.CommonRepository;
import com.yxl.downloadhelper.model.entity.BookShelfEntity;
import com.yxl.downloadhelper.model.entity.SeriesEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesRepository extends CommonRepository<SeriesEntity> {
}
