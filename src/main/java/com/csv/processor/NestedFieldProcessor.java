package com.csv.processor;

import com.jdbc.model.Entity;

import java.lang.reflect.InvocationTargetException;

public class NestedFieldProcessor implements FieldProcessor {

    @Override
    public <T> String convertToDto(T field) throws IllegalAccessException {
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        return "{" + annotationProcessor.processEntity((Entity) field) + "}, ";
    }

    @Override
    public <T> T parseFromDto(String field, Class<T> fieldType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return null;
    }
}
