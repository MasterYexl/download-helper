package com.yxl.downloadhelper.model.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
public class BookShelf {
    private UUID id;
    private UUID usrId;
    private List<Book> bookList;
    private boolean onShelf;
    private Timestamp createTime;
    private Timestamp updateTime;
}
    
