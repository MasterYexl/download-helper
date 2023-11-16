package com.yxl.downloadhelper.common.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yxl.downloadhelper.common.exception.DataOperationException;
import com.yxl.downloadhelper.common.model.ResponseState;
import com.yxl.downloadhelper.common.model.ResponseTemplate;
import com.yxl.downloadhelper.common.model.entity.CommonEntity;
import com.yxl.downloadhelper.common.model.jpa.CommonRepository;
import com.yxl.downloadhelper.component.converter.Converter;
import com.yxl.downloadhelper.component.converter.ConverterBean;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Slf4j
public abstract class CommonService<T extends CommonEntity> {
    public static final ObjectMapper objectMapper = ConverterBean.getObjectMapper();
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public CommonService() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class<T>) pt.getActualTypeArguments()[0];
    }
    
    public ResponseTemplate<T> find(String id) {
        try {
            T t = getRepository().getById(id);
            return ResponseTemplate.success(t);
        } catch (Exception e) {
            return ResponseTemplate.fail(e.getMessage());
        }
    }

    public ResponseTemplate<T> delete(T entity) {
        try {
            getRepository().delete(entity);
            return ResponseTemplate.success(entity);
        } catch (Exception e) {
            return ResponseTemplate.fail(e.getMessage());
        }
    }

    public ResponseTemplate<T> deleteObject(Object object) {
        try {
            T entity = objectMapper.convertValue(object, type);
            return delete(entity);
        } catch (Exception e) {
            return ResponseTemplate.fail(e.getMessage());
        }
    }

    public ResponseTemplate<T> deleteById(String id) {
        try {
            T detail = getRepository().getById(id);
            getRepository().deleteById(id);
            return ResponseTemplate.success(detail);
        } catch (Exception e) {
            return ResponseTemplate.fail(e.getMessage());
        }
    }

    public ResponseTemplate<T> add(T entity) {
        try {
            entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            entity.setId(UUID.randomUUID());
            return save(entity);
        } catch (Exception e) {
            throw new DataOperationException("insert data fail, cause:"+e.getMessage());
        }
    }

    public ResponseTemplate<T> addObject(Object object) {
        try {
            T entity = objectMapper.convertValue(object, type);
            return add(entity);
        } catch (Exception e) {
            throw new DataOperationException("insert data fail, cause:"+e.getMessage());
        }
    }

    public ResponseTemplate<ResponseState> addAllObject(Collection<?> objects) {
        try {
            List<T> list = new ArrayList<>();
            objects.forEach(o -> list.add(objectMapper.convertValue(o, type)));
            return addAll(list);
        } catch (Exception e) {
            throw new DataOperationException("insert data fail, cause:"+e.getMessage());
        }
    }

    public ResponseTemplate<ResponseState> addAll(Collection<T> entities) {
        try {
            for (T entity : entities) {
                entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
                entity.setId(UUID.randomUUID());
            }
            getRepository().saveAll(entities);
            return ResponseTemplate.success(ResponseState.Success);
        } catch (Exception e) {
            throw new DataOperationException("insert data fail, cause:"+e.getMessage());
        }
    }

    public ResponseTemplate<ResponseState> saveAllObject(Collection<?> objects) {
        try {
            List<T> list = new ArrayList<>();
            objects.forEach(o -> list.add(objectMapper.convertValue(o, type)));
            return saveAll(list);
        } catch (Exception e) {
            throw new DataOperationException("insert data fail, cause:"+e.getMessage());
        }
    }
    
    public ResponseTemplate<ResponseState> saveAll(Collection<T> entities) {
        try {
            for (T entity : entities) {
                entity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            }
            getRepository().saveAll(entities);
            return ResponseTemplate.success(ResponseState.Success);
        } catch (Exception e) {
            throw new DataOperationException("insert data fail, cause:"+e.getMessage());
        }
    }

    public ResponseTemplate<T> saveObject(Object object) {
        try {
            T entity = objectMapper.convertValue(object, type);
            return save(entity);
        } catch (Exception e) {
            throw new DataOperationException("insert data fail, cause:"+e.getMessage());
        }
    }

    public ResponseTemplate<T> save(T entity) {
        try {
            entity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            T save = getRepository().save(entity);
            return ResponseTemplate.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseTemplate<T> fail = ResponseTemplate.fail(e.getMessage());
            fail.setDetail(entity);
            return fail;
        }
    }

    public abstract CommonRepository<T> getRepository();

}
