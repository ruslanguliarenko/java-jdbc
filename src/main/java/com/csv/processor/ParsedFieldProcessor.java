package com.csv.processor;

import java.lang.reflect.InvocationTargetException;

public class ParsedFieldProcessor implements FieldProcessor {


    @Override
    public <T> String convertToDto(T field) {
        return field.toString() + ", ";
    }

    @Override
    public <T> T parseFromDto(String field, Class<T> fieldType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        T t = fieldType.getDeclaredConstructor(String.class).newInstance(field);
        return t;
    }
}
