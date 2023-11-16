package com.yxl.downloadhelper.component.converter;

import org.springframework.stereotype.Component;

@Component
public interface Converter<O, D> {

    public D convert(O object);
    public O reconvert(D object);

}
