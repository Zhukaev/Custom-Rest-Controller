package com.qiwi.servlet.converter;

import  org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PathToIDConverter implements Converter<String, Long> {

    @Override
    public Long convert(String path) {
        try{
            Long id = Long.valueOf(path);
            return id;
        }catch (NumberFormatException e){
            return null;
        }
    }
}
