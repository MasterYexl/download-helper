package com.yxl.downloadhelper.model.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class BookSource {
    private UUID id;
    private UUID bookId;
    private String website;
    private String path;
    private Integer newSequence;
    private String newChapter;
    private Timestamp createTime;
    private Timestamp updateTime;
}
    
