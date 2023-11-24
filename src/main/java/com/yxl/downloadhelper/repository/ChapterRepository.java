package com.yxl.downloadhelper.repository;

import com.yxl.downloadhelper.common.model.jpa.CommonRepository;
import com.yxl.downloadhelper.model.entity.ChapterEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChapterRepository extends CommonRepository<ChapterEntity> {
    List<ChapterEntity> getByBookSourceId(UUID bookSourceID);
}
