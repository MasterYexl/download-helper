package com.yxl.downloadhelper.service;

import com.yxl.downloadhelper.common.model.jpa.CommonRepository;
import com.yxl.downloadhelper.common.service.CommonService;
import com.yxl.downloadhelper.model.entity.AuthorEntity;
import com.yxl.downloadhelper.model.entity.SettingEntity;
import com.yxl.downloadhelper.repository.AuthorRepository;
import com.yxl.downloadhelper.repository.SeriesRepository;
import com.yxl.downloadhelper.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingService extends CommonService<SettingEntity> {

    private final SettingRepository settingRepository;

    @Override
    public CommonRepository<SettingEntity> getRepository() {
        return settingRepository;
    }
}
