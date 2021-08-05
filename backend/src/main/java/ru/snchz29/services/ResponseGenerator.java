package ru.snchz29.services;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Multimap;
import org.springframework.stereotype.Service;
import ru.snchz29.models.Person;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class ResponseGenerator {
    private final OutputStream stream;
    private final JsonGenerator generator;

    public ResponseGenerator() throws IOException {
        JsonFactory factory = new JsonFactory();
        this.stream = new ByteArrayOutputStream();
        this.generator = factory.createGenerator(stream, JsonEncoding.UTF8);
        generator.setCodec(new ObjectMapper());
    }

    public String writeResult(Multimap<Person, Person> result, boolean loginState) throws IOException {
        generator.writeStartObject();
        generator.writeBooleanField("loggedIn", loginState);
        generator.writeFieldName("result");
        generator.writeStartArray();
        for (Person lhs : result.keySet()) {
            generator.writeStartObject();
            generator.writeObjectField("lhs", lhs);
            generator.writeFieldName("rhs");
            generator.writeStartArray();
            for (Person rhs : result.get(lhs)) {
                generator.writeObject(rhs);
            }
            generator.writeEndArray();
            generator.writeEndObject();
        }
        generator.writeEndArray();
        generator.writeEndObject();
        generator.close();
        return stream.toString();
    }
}
