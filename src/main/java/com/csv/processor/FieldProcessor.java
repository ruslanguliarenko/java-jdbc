package com.csv.processor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public interface FieldProcessor {
    <T> String convertToDto(T field) throws IllegalAccessException;
    <T> T parseFromDto(String field, Class<T> fieldType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;
}
