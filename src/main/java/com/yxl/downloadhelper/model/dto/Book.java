package com.yxl.downloadhelper.model.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
public class Book {
    private UUID id;
    private String name;
    private Author author;
    private UUID authorId;
    private Series series;
    private List<Chapter> chapterList;
    private Integer seriesSequence;
    private Integer readingSequence;
    private Double chapterProgress;
    private String url;
    private Timestamp createTime;
    private Timestamp updateTime;

    public int getSize() {
        if (chapterList == null) {
            return 0;
        }
        return chapterList.size();
    }

    public Chapter getChapter(int index) {
        return chapterList.get(index);
    }
}
