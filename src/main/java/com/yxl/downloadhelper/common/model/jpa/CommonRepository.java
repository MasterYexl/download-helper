package com.yxl.downloadhelper.common.model.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CommonRepository<T> extends JpaRepository<T, String> {
}
