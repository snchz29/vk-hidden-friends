package ru.snchz29.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.SneakyThrows;

public class JsonGeneratorWrapper {
    private final JsonGenerator generator;

    @SneakyThrows
    public void writeObject(Runnable fn) {
        generator.writeStartObject();
        fn.run();
        generator.writeEndObject();
    }

    @SneakyThrows
    public void writeObjectArray(Runnable fn) {
        generator.writeStartObject();
        generator.writeStartArray();
        fn.run();
        generator.writeEndArray();
        generator.writeEndObject();
    }

    @SneakyThrows
    public void writeArray(Runnable fn) {
        generator.writeStartArray();
        fn.run();
        generator.writeEndArray();
    }

    @SneakyThrows
    public void writeObjectField(String fieldName, Object pojo) {
        generator.writeObjectField(fieldName, pojo);
    }

    @SneakyThrows
    public void writeFieldName(String name) {
        generator.writeFieldName(name);
    }

    public JsonGeneratorWrapper(JsonGenerator generator) {
        this.generator = generator;
    }

    @SneakyThrows
    public void writeObject(Object pojo) {
        generator.writeObject(pojo);
    }

    public JsonGenerator generator() {
        return generator;
    }
}
