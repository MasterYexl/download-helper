package com.yxl.downloadhelper.service;

import com.yxl.downloadhelper.common.model.jpa.CommonRepository;
import com.yxl.downloadhelper.common.service.CommonService;
import com.yxl.downloadhelper.model.entity.AuthorEntity;
import com.yxl.downloadhelper.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService extends CommonService<AuthorEntity> {

    private final AuthorRepository authorRepository;

    @Override
    public CommonRepository<AuthorEntity> getRepository() {
        return authorRepository;
    }
}