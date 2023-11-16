package com.yxl.downloadhelper.service;

import com.yxl.downloadhelper.common.model.jpa.CommonRepository;
import com.yxl.downloadhelper.common.service.CommonService;
import com.yxl.downloadhelper.model.dto.Chapter;
import com.yxl.downloadhelper.model.dto.Content;
import com.yxl.downloadhelper.model.entity.AuthorEntity;
import com.yxl.downloadhelper.model.entity.ContentEntity;
import com.yxl.downloadhelper.repository.AuthorRepository;
import com.yxl.downloadhelper.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ContentService extends CommonService<ContentEntity> {

    private final ContentRepository contentRepository;

    @Override
    public CommonRepository<ContentEntity> getRepository() {
        return contentRepository;
    }

    public void addContentByChapter(Collection<Chapter> chapters) {
        for (Chapter chapter : chapters) {
            addContentByChapter(chapter);
        }
    }
    public void addContentByChapter(Chapter chapter) {
        ContentEntity content = new ContentEntity();
        content.setChapterId(chapter.getId());
        content.setSequence(0);
        content.setUrl(chapter.getUrl());
        add(content);
    }
}
