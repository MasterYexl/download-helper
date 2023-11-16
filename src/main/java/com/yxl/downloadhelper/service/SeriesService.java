package com.yxl.downloadhelper.service;

import com.yxl.downloadhelper.common.model.jpa.CommonRepository;
import com.yxl.downloadhelper.common.service.CommonService;
import com.yxl.downloadhelper.model.entity.AuthorEntity;
import com.yxl.downloadhelper.model.entity.SeriesEntity;
import com.yxl.downloadhelper.repository.AuthorRepository;
import com.yxl.downloadhelper.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeriesService extends CommonService<SeriesEntity> {

    private final SeriesRepository seriesRepository;

    @Override
    public CommonRepository<SeriesEntity> getRepository() {
        return seriesRepository;
    }
}
