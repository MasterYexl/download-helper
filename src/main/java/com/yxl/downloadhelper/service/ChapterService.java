package com.yxl.downloadhelper.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yxl.downloadhelper.common.model.jpa.CommonRepository;
import com.yxl.downloadhelper.common.service.CommonService;
import com.yxl.downloadhelper.model.dto.Chapter;
import com.yxl.downloadhelper.model.entity.ChapterEntity;
import com.yxl.downloadhelper.repository.ChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChapterService extends CommonService<ChapterEntity> {

    private final ChapterRepository chapterRepository;

    private final ObjectMapper objectMapper;

    @Override
    public CommonRepository<ChapterEntity> getRepository() {
        return chapterRepository;
    }

    public List<ChapterEntity> saveChapters(List<Chapter> chapters) {
        List<ChapterEntity> list = new ArrayList<>();
        for (Chapter chapter : chapters) {
            ChapterEntity entity = objectMapper.convertValue(chapter, ChapterEntity.class);
            list.add(entity);
        }
        saveAll(list);
        return list;
    }

    public List<ChapterEntity> addChapters(List<Chapter> chapters) {
        List<ChapterEntity> list = new ArrayList<>();
        for (Chapter chapter : chapters) {
            ChapterEntity entity = objectMapper.convertValue(chapter, ChapterEntity.class);
            list.add(entity);
        }
        addAll(list);
        return list;
    }

    public List<ChapterEntity> getByBookId(UUID bookID) {
        return chapterRepository.getByBookId(bookID);
    }
}
