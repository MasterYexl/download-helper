package com.yxl.downloadhelper.model.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
public class Series {
    private UUID id;
    private String name;
    private Author author;
    private String description;
    private List<Book> bookList;
    private Timestamp createTime;
    private Timestamp updateTime;
}
    
