package com.csv;

import com.csv.processor.AnnotationProcessor;
import com.jdbc.model.Entity;

import java.io.FileWriter;
import java.io.IOException;


public class CsvWriter {

    private final FileWriter fileWriter;
    public CsvWriter(FileWriter writer){
        this.fileWriter = writer;
    }

    public void writeEntity(Entity entity) throws IOException, IllegalAccessException, NoSuchFieldException {
        AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        String row = annotationProcessor.getFields(entity);
        fileWriter.write(row);

    }

    public void close() throws IOException {
        fileWriter.flush();
        fileWriter.close();
    }
}
