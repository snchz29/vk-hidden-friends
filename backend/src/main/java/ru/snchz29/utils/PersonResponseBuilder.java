package ru.snchz29.utils;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import lombok.SneakyThrows;

public class PersonResponseBuilder {

    public static JSONBuilder json() {
        return new JSONBuilder();
    }

    public static class JSONBuilder {
        private final JsonGeneratorWrapper wrapper;
        private final ByteArrayOutputStream stream;
        public JSONBuilder addField(String name, Object obj) {
            wrapper.writeObjectField(name, obj);
            return this;
        }

        @SneakyThrows
        public String toString() {
            wrapper.generator().writeEndObject();
            return stream.toString();
        }

        @SneakyThrows
        JSONBuilder() {
            stream = new ByteArrayOutputStream();
            JsonGenerator generator = new JsonFactory().createGenerator(stream, JsonEncoding.UTF8);
            generator.setCodec(new ObjectMapper());
            generator.writeStartObject();
            wrapper = new JsonGeneratorWrapper(generator);
        }
    }
}
