package com.yxl.downloadhelper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yxl.downloadhelper.model.dto.Book;
import com.yxl.downloadhelper.model.entity.BookEntity;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MapperTest {

    private ObjectMapper objectMapper = new ObjectMapper();



    @Test
    public void testMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Book book = new Book();
        book.setName("test");
        book.setId(UUID.randomUUID());
        Book book2 = new Book();
        book2.setName("test");
        book2.setId(UUID.randomUUID());
        List<Book> list = new ArrayList<>();
        list.add(book2);
        list.add(book);
        BookEntity bookEntity = objectMapper.convertValue(book, BookEntity.class);
        System.out.println(bookEntity);
        List<BookEntity> bookEntities = objectMapper.convertValue(list, new TypeReference<List<BookEntity>>() {
        });
        System.out.println(bookEntities);
    }


}
