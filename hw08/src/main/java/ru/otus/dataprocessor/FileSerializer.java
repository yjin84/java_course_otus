package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileOutputStream;
import java.util.Map;

public class FileSerializer implements Serializer {
    private final ObjectMapper objectMapper;

    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void serialize(Map<String, Double> data) {
        try (var fos = new FileOutputStream(fileName)) {
            objectMapper.writeValue(fos,data);
        } catch (Exception ex) {
            throw new FileProcessException(ex);
        }
    }
}
