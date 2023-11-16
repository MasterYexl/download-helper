package com.yxl.downloadhelper.repository;

import com.yxl.downloadhelper.common.model.jpa.CommonRepository;
import com.yxl.downloadhelper.model.entity.AuthorEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CommonRepository<AuthorEntity> {
}
