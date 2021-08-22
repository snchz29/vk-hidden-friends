package ru.snchz29.services;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

@Service
public class ResponseGeneratorWrapper {

    public static ResponseGenerator json() {
        return new ResponseGenerator();
    }

    public static class ResponseGenerator {
        private final OutputStream stream;
        private final JsonGenerator generator;

        @SneakyThrows
        public ResponseGenerator() {
            JsonFactory factory = new JsonFactory();
            this.stream = new ByteArrayOutputStream();
            this.generator = factory.createGenerator(stream, JsonEncoding.UTF8);
            generator.setCodec(new ObjectMapper());
        }

        @SneakyThrows
        public ResponseGenerator writeStartObject() {
            generator.writeStartObject();
            return this;
        }

        @SneakyThrows
        public ResponseGenerator writeBooleanField(String fieldName, boolean value) {
            generator.writeBooleanField(fieldName, value);
            return this;
        }

        @SneakyThrows
        public ResponseGenerator writeObjectArray(String fieldName, List<?> array) {
            generator.writeFieldName(fieldName);
            generator.writeStartArray();
            for (Object obj : array) {
                generator.writeObject(obj);
            }
            generator.writeEndArray();
            return this;
        }

        @SneakyThrows
        public ResponseGenerator writeEndObject() {
            generator.writeEndObject();
            return this;
        }

        public String toString() {
            return stream.toString();
        }

        @SneakyThrows
        public ResponseGenerator close() {
            generator.close();
            return this;
        }
    }
}
