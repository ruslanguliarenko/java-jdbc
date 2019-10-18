package com.csv.processor;

import java.util.*;
import com.csv.annotation.List;
import com.csv.annotation.Nested;
import com.csv.annotation.NestedList;
import com.csv.annotation.Parsed;
import com.jdbc.model.Entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class AnnotationProcessor {

    private ParsedFieldProcessor parsedFieldProcessor = new ParsedFieldProcessor();
    private NestedFieldProcessor nestedFieldProcessor = new NestedFieldProcessor();

    public String getFields(Entity entity) throws IllegalAccessException, NoSuchFieldException {
        Field[] fields = entity.getClass().getDeclaredFields();
        StringBuilder result = new StringBuilder();

        for (Field field:fields){
            if(field.isAnnotationPresent(Parsed.class)) {

                field.setAccessible(true);
                result.append(field.get(entity).toString()).append(", ");

            }else if(field.isAnnotationPresent(Nested.class)){
                field.setAccessible(true);

                if(Collection.class.isAssignableFrom(field.getType())){
                    result.append("[");

                    Collection collection  = (Collection) field.get(entity);
                    Iterator iterator = collection.iterator();

                    while(iterator.hasNext()){
                        result.append(getFields((Entity) iterator.next()));
                    }
                    result.setLength(result.length()-2);
                    result.append("]");
                }else{
                    result.append(getFields((Entity) field.get(entity)));
                }
            }
        }
        return result.toString();
    }

    public <T extends Entity> T setFields(String[] valueFields, Class<T> entityType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        T entity =  entityType.getDeclaredConstructor().newInstance();
            Field[] fields = entity.getClass().getDeclaredFields();
            int i =0;
            for(Field field : fields){
            if(field.isAnnotationPresent(Parsed.class)){
                field.setAccessible(true);
                field.set(entity, valueFields[i]);
            }else if(field.isAnnotationPresent(Nested.class)){
            }
        }

        //entity.setParameters(valueFields);

        return entity;
    }

    public String processEntity(Entity entity) throws IllegalAccessException {
        Field[] fields = entity.getClass().getDeclaredFields();
        StringBuilder dto = new StringBuilder();
        for (Field field:fields) {
                if (field.isAnnotationPresent(Parsed.class)) {
                    dto.append(singleFieldProcess(parsedFieldProcessor, field, entity));
                } else if (field.isAnnotationPresent(Nested.class)) {
                    dto.append(singleFieldProcess(nestedFieldProcessor, field, entity));
                }else if( field.isAnnotationPresent(List.class)){
                    dto.append(listFieldProcess(parsedFieldProcessor, field, entity));
                }else if( field.isAnnotationPresent(NestedList.class)){
                    dto.append(listFieldProcess(nestedFieldProcessor, field, entity));
                }
        }
        dto.setLength(dto.length()-2);
        return dto.toString();
    }

    private String singleFieldProcess(FieldProcessor fieldProcessor, Field field, Entity entity) throws IllegalAccessException {
        field.setAccessible(true);
        return fieldProcessor.convertToDto(field.get(entity));
    }

    private String listFieldProcess(FieldProcessor fieldProcessor, Field field, Entity entity) throws IllegalAccessException {
        field.setAccessible(true);
        Collection collection = (Collection) field.get(entity);
        Iterator iterator = collection.iterator();
        StringBuilder listField = new StringBuilder();

        listField.append("[");

        while(iterator.hasNext()) {
            listField.append(fieldProcessor.convertToDto(iterator.next()));
        }

        listField.setLength(listField.length()-2);
        listField.append("]");

        return listField.toString() + ", ";
    }


}
