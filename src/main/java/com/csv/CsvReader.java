package com.csv;

import com.csv.processor.AnnotationProcessor;
import com.jdbc.model.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class CsvReader {
    private final Scanner reader;

    public CsvReader(Scanner reader) {
        this.reader = reader;
    }

    public<T extends Entity> List<T> read(Class<T> entityType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        List<T> entities = new ArrayList<>();
        while (reader.hasNextLine()){
            String line = reader.nextLine();
            String[] fields = line.split(", ");
            entities.add(annotationProcessor.setFields(fields, entityType));
        }
        return entities;
    }
}
