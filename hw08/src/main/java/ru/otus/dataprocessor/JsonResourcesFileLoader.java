package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.util.List;

public class JsonResourcesFileLoader implements Loader {
    private final String fileName;

    private final ObjectMapper objectMapper;

    public JsonResourcesFileLoader(String fileName) {
        this.fileName = fileName;
        this.objectMapper = new ObjectMapper();

        var module = new SimpleModule();
        module.addDeserializer(Measurement.class, new MeasureDeserializer());
        objectMapper.registerModule(module);
    }

    @Override
    public List<Measurement> load() {
        try (var fis = JsonResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            return objectMapper.readValue(fis, new TypeReference<List<Measurement>>() {

            });
        } catch (Exception ex) {
            throw new FileProcessException(ex);
        }
    }

    private static class MeasureDeserializer extends StdDeserializer<Measurement> {

        protected MeasureDeserializer() {
            this(null);
        }
        protected MeasureDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Measurement deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node = jp.getCodec().readTree(jp);
            String name = node.get("name").asText();
            double value = node.get("value").asDouble();

            return new Measurement(name, value);
        }
    }
}
