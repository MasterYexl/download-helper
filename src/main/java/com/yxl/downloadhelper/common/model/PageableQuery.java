package com.yxl.downloadhelper.common.model;

import lombok.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class PageableQuery implements Pageable {

    private int pageNumber;
    private int pageSize;
    private long offset;
    private Sort sort;

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return null;
    }

    @Override
    public Pageable first() {
        return null;
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }
}
